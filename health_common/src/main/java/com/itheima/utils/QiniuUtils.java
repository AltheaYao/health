package com.itheima.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;




public class QiniuUtils {
    private static 	String accessKey="LnKANkyK8iEoFjN8BNQLPtAYWMZnPMT2akiGorp4";//第一对密匙
    private static 	String secretKey="3cIkUSgKLsNblueRS-RUI1olo85I0bno3oDIQMwc";//第二队密匙
    private static 	String bucket="healthdemo110";//七牛云存储空间名称

    /*封装工具类需要参数文件路径和文件名称*/
    public static void upload2Qiniu(String localFilePath,String fileName) {
//构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cfg);
        //生成权限令牌传入密钥
        Auth auth = Auth.create(accessKey, secretKey);
        //生成令牌
        String upToken = auth.uploadToken(bucket);
        try {
            //传入文件路径文件名令牌完成上传
            Response response = uploadManager.put(localFilePath, fileName, upToken);
            //解析上传成功的结果返回一个json对象,解析
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /*上传数组形式的文件*/
    public static void upload2Qiniu(byte[] bytes,String fileName) {
//构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cfg);
        //生成权限令牌传入密钥
        Auth auth = Auth.create(accessKey, secretKey);
        //生成令牌
        String upToken = auth.uploadToken(bucket);
        try {
            //传入文件路径文件名令牌完成上传
            Response response = uploadManager.put( bytes, fileName, upToken);
            //解析上传成功的结果返回一个json对象,解析
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /*删除七牛云文件*/
    public static void deleteFileFromQiniu(String fileName){
    //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
    //...其他参数参考类注释
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
