#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
import webapp2
from google.appengine.ext.webapp import template

# import urllib2
# import json
from gcm import GCM
import logging

API_KEY = "AIzaSyDcvmqmSyxvVFgBxikAa4-mAMe8mqEZpQQ"
REGISTRATION_IDS = ['APA91bEcpFO2TBaYyAQw9vrGZ0btc-VEeuAEvlKqQ0AoIAemxuqAojftV9HmZmmJmPRhJnPTDCchJEquNIp63b7HlmOqUztUpu8TIMxOysEQrBLRXI4qykWc8XdbvY0sAT05NDpVeaPUC0JICc5tSqFje2PXLir96HnJFI9fx858ZJX2r6WNAjI']

class MainHandler(webapp2.RequestHandler):
    def get(self):
        self.response.out.write(template.render('templates/simplegcm.html', None))

    def post(self):
#         result = self.send_gcm_request(self.request.get('simple_string'))
        gcm = GCM(API_KEY)
        data = {'simple_string': self.request.get('simple_string')}
        if self.request.get('int_value'):
            data['int_value'] = int(self.request.get('int_value'))
        if self.request.get('float_value'):
            data['float_value'] = float(self.request.get('float_value'))
        response = gcm.json_request(registration_ids=REGISTRATION_IDS, data={'data': data})
        self.response.out.write(template.render('templates/simplegcm.html', {'response': response, 'data_sent': data}))

#     def send_gcm_request(self, data):
#             json_data = {"collapse_key" : "msg", 
#                          "data" : {
#                                    "data": data,
#                        }, 
#                     "registration_ids": ['APA91bEcpFO2TBaYyAQw9vrGZ0btc-VEeuAEvlKqQ0AoIAemxuqAojftV9HmZmmJmPRhJnPTDCchJEquNIp63b7HlmOqUztUpu8TIMxOysEQrBLRXI4qykWc8XdbvY0sAT05NDpVeaPUC0JICc5tSqFje2PXLir96HnJFI9fx858ZJX2r6WNAjI'],
#             }
#             url = 'https://android.googleapis.com/gcm/send'
#             myKey = "AIzaSyDcvmqmSyxvVFgBxikAa4-mAMe8mqEZpQQ" 
#             data = json.dumps(json_data)
#             headers = {'Content-Type': 'application/json', 'Authorization': 'key=%s' % myKey}
#             req = urllib2.Request(url, data, headers)
#             f = urllib2.urlopen(req)
#             response = json.loads(f.read())
#             return json.dumps(response,sort_keys=True, indent=2)

app = webapp2.WSGIApplication([
    ('/', MainHandler)
], debug=True)
