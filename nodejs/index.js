const MoonapiSign = require("./MoonapiSign");

//ApiId
var apiId = 148;

//TODO: KeyID，在http://www.moonapi.com中生成的key id
var accessKeyId = 0;

//TODO: KeyCode，在http://www.moonapi.com中生成的Key Code
var accessKeySecret = "";

//0为简单签名模式, 直接将sign设置为Key Code即可, 1为认证方式二md5签名认证， 2为认证方式二hmac签名认证
var signMethod = 0;

var parameters = [];
var signParameters = [];
parameters["keyid"] = accessKeyId;
parameters["_t"] = Date.parse(new Date())/1000;
//TODO: 此处添加其它参数, POST参数加入signParameters中以参与签名，GET参数加入parameters中以参与签名及URL组装
//TODO: 需检查的URL, 如http://www.baidu.com
parameters["url"] = "xxx";

//sign
var sign = accessKeySecret;
if (signMethod == 1)
    sign = MoonapiSign.generateMd5Signature(parameters, accessKeySecret);
else if (signMethod == 2)
    sign = MoonapiSign.generateHmacSHA1Signature(parameters, accessKeySecret);

//组装URL
var strParams = MoonapiSign.getUrlQueryFromParams(parameters);
var urlApi = "http://api.moonapi.com/" + apiId + "?" + strParams + "&sign=" + sign;
console.log(urlApi);

var request = require('request');
var options = {
   'method': 'GET',
   'url': urlApi,
   'headers': {
   }
};
request(options, function (error, response) {
   if (error) throw new Error(error);
   console.log(response.body);
   dataCallBack(JSON.parse(response.body))
});


function dataCallBack(data){
    if (data['status'] == 'success')
    {
        //成功获取数据
        console.log("成功获取数据");
    }
    else
    {
        //异常，根据msg与code查看异常信息
        console.log("数据异常！！！ " + data['code'] + ", " + data['message']);
    }
}