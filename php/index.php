<?php
ini_set('display_errors',1);            //错误信息
error_reporting(-1);

require_once("MoonapiSign.php");

//ApiId
$apiId = 148;

//TODO: KeyID，在http://www.moonapi.com中生成的key id
$accessKeyId = 0;

//TODO: KeyCode，在http://www.moonapi.com中生成的Key Code
$accessKeySecret = "";

//0为简单签名模式, 直接将sign设置为Key Code即可, 1为认证方式二md5签名认证， 2为认证方式二hmac签名认证
$signMethod = 0;

$parameters = [];
$signParameters = [];
$parameters["keyid"] = $accessKeyId;
$parameters["_t"] = time();
//TODO: 此处添加其它参数, POST参数加入$signParameters中以参与签名，GET参数加入$parameters中以参与签名及URL组装
//TODO: 需检查的URL, 如http://www.baidu.com
$parameters["url"] = "xxx";

//sign
$sign = $accessKeySecret;
if ($signMethod == 1)
    $sign = MoonapiSign::generateMd5Signature($parameters, $accessKeySecret);
else if ($signMethod == 2)
    $sign = MoonapiSign::generateHmacSHA1Signature($parameters, $accessKeySecret);

//组装URL
$strParams = MoonapiSign::getUrlQueryFromParams($parameters);
$urlApi = "http://api.moonapi.com/" . $apiId . "?" . $strParams . "&sign=" . $sign;
echo $urlApi."<BR>";

$curl = curl_init();

curl_setopt_array($curl, array(
   CURLOPT_URL => $urlApi,
   CURLOPT_RETURNTRANSFER => true,
   CURLOPT_ENCODING => '',
   CURLOPT_MAXREDIRS => 10,
   CURLOPT_TIMEOUT => 0,
   CURLOPT_FOLLOWLOCATION => true,
   CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
   CURLOPT_CUSTOMREQUEST => 'GET',
));

$response = curl_exec($curl);

curl_close($curl);
echo $response;


$data = json_decode($response, true);

if ($data['status'] == 'success')
{
    //成功获取数据
    echo "<BR>成功获取数据"."<BR>";
}
else
{
    //异常，根据msg与code查看异常信息
    echo "<BR>数据异常！！！ " . $data['code'] . ", " . $data['message']."<BR>";
}