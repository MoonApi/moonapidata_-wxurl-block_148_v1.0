using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;

namespace MoonApiDemo
{
    class MoonapiSign
    {
        public static String generateHmacSHA1Signature(Dictionary<String, String> parameters, String accessKeySecret)
        {
            String signString = generateSignString(parameters, accessKeySecret);
            byte[] signBytes = hmacSHA1Signature(accessKeySecret, signString);

            return byteToHex(signBytes);
        }

        public static String generateMd5Signature(Dictionary<String, String> parameters, String accessKeySecret)
        {
            String signString = generateSignString(parameters, accessKeySecret);
            byte[] signBytes = md5Signature(accessKeySecret, signString);

            return byteToHex(signBytes);
        }

        public static String generateSignString(Dictionary<String, String> parameters, String accessKeySecret)
        {
            ArrayList arrString = new ArrayList();

            foreach (var param in parameters)
            {
                arrString.Add(param.Key + "=" + param.Value);
            }

            arrString.Sort();

            return String.Join("&", (string[])arrString.ToArray(typeof(string))) + ":" + accessKeySecret;
        }

        public static String getUrlQueryFromParams(Dictionary<String, String> parameters)
        {
            ArrayList arrString = new ArrayList();

            foreach (var param in parameters)
            {
                arrString.Add(param.Key + "=" + param.Value);
            }

            return String.Join("&", (string[])arrString.ToArray(typeof(string)));
        }

        public static byte[] hmacSHA1Signature(String secret, String baseString)
        {
            if (String.IsNullOrEmpty(secret))
            {
                throw new Exception("secret can not be empty");
            }
            if (String.IsNullOrEmpty(baseString))
            {
                return null;
            }
            HMACSHA1 algorithm = new HMACSHA1(Encoding.UTF8.GetBytes(secret));
            byte[] hash = algorithm.ComputeHash(Encoding.UTF8.GetBytes(baseString));

            return hash;
        }

        public static byte[] md5Signature(String secret, String baseString)
        {
            if (String.IsNullOrEmpty(secret))
            {
                throw new Exception("secret can not be empty");
            }
            if (String.IsNullOrEmpty(baseString))
            {
                return null;
            }

            MD5 md5 = new MD5CryptoServiceProvider();

            return md5.ComputeHash(Encoding.UTF8.GetBytes(baseString));
        }

        private static String byteToHex(byte[] hash)
        {
            StringBuilder strB = new StringBuilder();
            foreach (byte b in hash)
            {
                strB.AppendFormat("{0:x2}", b);
            }
            String result = strB.ToString();
            return result;
        }
    }
}
