using RestSharp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MoonApiDemo
{
    class Program
    {
        //ApiId
        private static readonly int apiId = 148;

        //TODO: KeyID，在http://www.moonapi.com中生成的key id
        private static readonly int accessKeyId = 0;

        //TODO: KeyCode，在http://www.moonapi.com中生成的Key Code
        private static readonly String accessKeySecret = "";

        //0为简单签名模式, 直接将sign设置为Key Code即可, 1为认证方式二md5签名认证， 2为认证方式二hmac签名认证
        private static readonly int signMethod = 0;

        static void Main(string[] args)
        {
            Dictionary<String, String> parameters = new Dictionary<string, string>();
            Dictionary<String, String> signParameters = new Dictionary<string, string>();
            parameters.Add("keyid", accessKeyId.ToString());
            parameters.Add("_t", Convert.ToInt64((DateTime.Now - new DateTime(1970, 1, 1, 0, 0, 0, 0)).TotalSeconds).ToString());
            //TODO: 此处添加其它参数, POST参数加入signParameters中以参与签名，GET参数加入parameters中以参与签名及URL组装
            //TODO: 需检查的URL, 如http://www.baidu.com
            parameters.Add("url", "xxx");

            //sign
            String sign = accessKeySecret;
            if (signMethod == 1)
                sign = MoonapiSign.generateMd5Signature(parameters, accessKeySecret);
            else if (signMethod == 2)
                sign = MoonapiSign.generateHmacSHA1Signature(parameters, accessKeySecret);

            //组装URL
            String strParams = MoonapiSign.getUrlQueryFromParams(parameters);
            String urlApi = "http://api.moonapi.com/" + apiId + "?" + strParams + "&sign=" + sign;
            Console.WriteLine(urlApi);

            var client = new RestClient(urlApi);
            client.Timeout = -1;
            var request = new RestRequest(Method.GET);
            IRestResponse response = client.Execute(request);
            Console.WriteLine(response.Content);

            String strContent = response.Content;

            var data = MoonApiDataWxUrlBlock.FromJson(strContent);

            if (data.Status.Equals("success"))
            {
                //成功获取数据
                Console.WriteLine("成功获取数据");
            }
            else
            {
                //异常，根据msg与code查看异常信息
                Console.WriteLine("数据异常！！！ " + data.Code + ", " + data.Message);
            }

            Console.ReadKey();
        }
    }
}
