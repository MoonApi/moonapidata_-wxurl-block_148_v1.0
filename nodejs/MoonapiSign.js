var crypto = require('crypto');

function generateHmacSHA1Signature(parameters, accessKeySecret)
{
    var signString = generateSignString(parameters, accessKeySecret);

    return crypto.createHmac('sha1', accessKeySecret).update(signString).digest('hex');;
}

function generateMd5Signature(parameters, accessKeySecret)
{
    var signString = generateSignString(parameters, accessKeySecret);
    return crypto.createHash('md5').update(signString).digest("hex");
}

function generateSignString(parameters, accessKeySecret)
{
    var arrString = [];

    for (var i in parameters)
    {
        arrString.push(i + "=" + parameters[i]);
    }

    arrString.sort();

    return arrString.join("&") + ":" + accessKeySecret;
}

function getUrlQueryFromParams(parameters)
{
    var arrString = [];

    for (var i in parameters)
    {
        arrString.push(i + "=" + parameters[i]);
    }

    return arrString.join("&");
}

module.exports = {
    generateHmacSHA1Signature,
    generateMd5Signature,
    getUrlQueryFromParams
}  