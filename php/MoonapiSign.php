<?php

class MoonapiSign{
    public static function generateHmacSHA1Signature($parameters, $accessKeySecret)
    {
        $signString = MoonapiSign::generateSignString($parameters, $accessKeySecret);

        return hash_hmac("sha1", $signString, $accessKeySecret, false);
    }

    public static function generateMd5Signature($parameters, $accessKeySecret)
    {
        $signString = MoonapiSign::generateSignString($parameters, $accessKeySecret);
        return md5($signString);
    }

    public static function generateSignString($parameters, $accessKeySecret)
    {
        $arrString = [];

        foreach ($parameters as $key=>$value)
        {
            $arrString[] = $key . "=" . $value;
        }

        sort($arrString);

        return join("&", $arrString) . ":" . $accessKeySecret;
    }

    public static function getUrlQueryFromParams($parameters)
    {
        $arrString = [];

        foreach ($parameters as $key=>$value)
        {
            $arrString[] = $key . "=" . $value;
        }

        return join("&", $arrString);
    }
}