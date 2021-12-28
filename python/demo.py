import MoonapiSign
import time
import json
import MoonApiDataWXUrlBlock as MoonApiData

#ApiId
apiId = 148

#TODO: KeyID，在http:#www.moonapi.com中生成的key id
accessKeyId = 0

#TODO: KeyCode，在http:#www.moonapi.com中生成的Key Code
accessKeySecret = ""

#0为简单签名模式, 直接将sign设置为Key Code即可, 1为认证方式二md5签名认证， 2为认证方式二hmac签名认证
signMethod = 0

parameters = {}
signParameters = {}
parameters["keyid"] = accessKeyId
parameters["_t"] = time.time()
#TODO: 此处添加其它参数, POST参数加入signParameters中以参与签名，GET参数加入parameters中以参与签名及URL组装
#TODO: 需检查的URL, 如http://www.baidu.com
parameters["url"] = "xxx"

#sign
sign = accessKeySecret
if (signMethod == 1):
    sign = MoonapiSign.generateMd5Signature(parameters, accessKeySecret)
elif (signMethod == 2):
    sign = MoonapiSign.generateHmacSHA1Signature(parameters, accessKeySecret)

#组装URL
strParams = MoonapiSign.getUrlQueryFromParams(parameters)
urlApi = "http://api.moonapi.com/" + str(apiId) + "?" + strParams + "&sign=" + sign
print(urlApi)

import requests

url = urlApi

payload={}
headers = {}

response = requests.request("GET", url, headers=headers, data=payload)

print(response.text)


data = MoonApiData.moon_api_data_wxurl_block_from_dict(json.loads(response.text))

if (data.status == 'success'):
    #成功获取数据
    print("成功获取数据")
else:
    #异常，根据msg与code查看异常信息
    print("数据异常！！！ " + data.code + ", " + data.message)