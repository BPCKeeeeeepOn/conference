package com.youyu.conference.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;

public class TencentConfig {
    public static final String SECRET_ID = "AKIDEB5itsQ1BoBJBKKtGNDm31HKDn8DNOa3";
    public static final String SECRET_KEY = "LQEj24f1f3AVZU8Xfy3rWu3tSkBPMAn7";
    public static final String BUCKET_NAME = "braunbuket-1301530561";
    public static final String APP_ID = "1301530561";
    public static final String REGION_ID = "ap-nanjing";


    public static COSClient intiClient() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
        ClientConfig clientConfig = new ClientConfig(new Region(REGION_ID));
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
}
