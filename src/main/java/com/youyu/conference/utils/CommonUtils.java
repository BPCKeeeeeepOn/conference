package com.youyu.conference.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.model.ciModel.snapshot.SnapshotRequest;
import com.qcloud.cos.model.ciModel.snapshot.SnapshotResponse;
import com.tencent.cloud.CosStsClient;
import com.youyu.conference.common.PrizeConfig;
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.config.TencentConfig;
import com.youyu.conference.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CommonUtils {

    public static Set<String> IMG_TYPE_DICT = Stream.of(".png", ".jpg", ".jpeg", ".gif").collect(Collectors.toSet());
    public static Set<String> VIDEO_TYPE_DICT = Stream.of(".mp4", ".mpeg4", ".m4v", ".mov").collect(Collectors.toSet());
    public final static String PREFIX = "tyson";
    static final String PERSONAL_DRAW_LUCK_KEY = "PERSONAL_DRAW_LUCK_";

    static final List<PrizeConfig> prizeConfigList = new ArrayList<PrizeConfig>() {{
        add(new PrizeConfig(0, generatePrizeKey("0_0"), 0, "未中奖", 40.6, 0));

        add(new PrizeConfig(1, generatePrizeKey("1_1"), 1, "100元", 0.3, 67));
        add(new PrizeConfig(2, generatePrizeKey("1_2"), 1, "50元", 0.6, 134));
        add(new PrizeConfig(3, generatePrizeKey("1_3"), 1, "20元", 1.5, 335));
        add(new PrizeConfig(4, generatePrizeKey("1_4"), 1, "10元", 3.0, 670));
        add(new PrizeConfig(5, generatePrizeKey("1_5"), 1, "5元", 4.5, 1005));
        add(new PrizeConfig(6, generatePrizeKey("1_6"), 1, "2元", 10.0, 1340));
        add(new PrizeConfig(7, generatePrizeKey("1_7"), 1, "1元", 18.6, 4154));

        add(new PrizeConfig(8, generatePrizeKey("2_1"), 2, "100元", 0.3, 67));
        add(new PrizeConfig(9, generatePrizeKey("2_2"), 2, "50元", 0.6, 134));
        add(new PrizeConfig(10, generatePrizeKey("2_3"), 2, "20元", 1.5, 335));
        add(new PrizeConfig(11, generatePrizeKey("2_4"), 2, "10元", 3.0, 670));
        add(new PrizeConfig(12, generatePrizeKey("2_5"), 2, "5元", 4.5, 1005));
        add(new PrizeConfig(13, generatePrizeKey("2_6"), 2, "2元", 10.0, 1340));
        add(new PrizeConfig(14, generatePrizeKey("2_7"), 2, "1元", 9.6, 2144));

    }};

    public static String generatePrizeKey(String key) {
        return PERSONAL_DRAW_LUCK_KEY + key;
    }


    public static int generateWorkerId() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String hostname = address.getHostAddress();
            if (StringUtils.isNotBlank(hostname)) {
                String bits = new BigInteger(hostname.getBytes()).toString(2);
                BigInteger rightPart = new BigInteger(StringUtils.right(bits, 10), 2);
                return rightPart.intValue();
            }
        } catch (UnknownHostException e) {
            log.error("unknown host, Reason: ", e);
        }
        return 0;
    }

    /**
     * @param number 需要保留{scale}位数
     * @return
     */
    public static double doubleFormat(double number, int scale) {
        // 将double类型转为BigDecimal
        BigDecimal bigDecimal = new BigDecimal(number);
        // 保留两位小数,并且四舍五入
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @Description list 随机取数据
     * @params list    list集合
     * num     随机取多少条
     **/
    public static List getRandomList(List list, int num) {
        List olist = new ArrayList<>();
        if (list.size() <= num) {
            return list;
        } else {
            Random random = new Random();
            for (int i = 0; i < num; i++) {
                int intRandom = random.nextInt(list.size() - 1);
                olist.add(list.get(intRandom));
                list.remove(list.get(intRandom));
            }
            return olist;
        }
    }

    /**
     * 上传文件并返回路径
     * //如果是视频文件 返回首帧图
     *
     * @return //绝对路径和相对路径都OK
     */
    public static Map<String, String> uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException(ResponseResult.fail(ResultCode.CODE1003));
        }
        HashMap<String, String> result = new HashMap<>();
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String contentType = file.getContentType();
        Long size = file.getSize();
        if (!IMG_TYPE_DICT.contains(suffixName) && !VIDEO_TYPE_DICT.contains(suffixName)) {
            throw new BizException(ResponseResult.fail(ResultCode.CODE1007));
        }
        if (IMG_TYPE_DICT.contains(suffixName) && size > 1024 * 1000 * 10) { //图片小于10M
            throw new BizException(ResponseResult.fail(ResultCode.CODE1005));
        }
        if (VIDEO_TYPE_DICT.contains(suffixName) && size > 1024 * 1000 * 100) { //视频小于100M
            throw new BizException(ResponseResult.fail(ResultCode.CODE1005));
        }
        try {
            InputStream inputStream = file.getInputStream();
            //生成key
            String fileKey = StringUtils.join(PREFIX, File.separator, RandomStringUtils.randomAlphanumeric(32), suffixName);
            String headImgKey = null;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(size);
            // 设置 Content type, 默认是 application/octet-stream
            objectMetadata.setContentType(contentType);
            PutObjectRequest putObjectRequest = new PutObjectRequest(TencentConfig.BUCKET_NAME, fileKey, inputStream, objectMetadata);
            // 设置存储类型, 默认是标准(Standard), 低频(standard_ia),一般为标准的
            putObjectRequest.setStorageClass(StorageClass.Standard);
            COSClient cc = TencentConfig.intiClient();
            PutObjectResult putObjectResult = cc.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
            if (StringUtils.isNotBlank(etag)) {
                if (VIDEO_TYPE_DICT.contains(suffixName)) {
                    headImgKey = StringUtils.join(PREFIX, File.separator, RandomStringUtils.randomAlphanumeric(32), ".jpg");
                    SnapshotRequest request = new SnapshotRequest();
                    //2.添加请求参数 参数详情请见api接口文档
                    request.setBucketName(TencentConfig.BUCKET_NAME);
                    request.getInput().setObject(fileKey);
                    request.getOutput().setBucket(TencentConfig.BUCKET_NAME);
                    request.getOutput().setRegion(TencentConfig.REGION_ID);
                    request.getOutput().setObject(headImgKey);
                    request.setTime("1");//视频第一秒
                    cc.generateSnapshot(request);
                }
                result.put("work_url", fileKey);
                result.put("work_head_img", headImgKey);
                return result;
            }
        } catch (CosServiceException e) {
            log.error("上传文件错误,code:{},message:{}", e.getErrorCode(), e.getErrorMessage());
        } catch (CosClientException e) {
            log.error("上传文件错误,code:{},message:{}", e.getErrorCode(), e.getMessage());
        } catch (IOException e) {
            log.error("UploadFile InputStream failed,message:{}", e.getMessage());
        }
        return null;
    }

    public static PrizeConfig personalDrawLuck() {

        int size = prizeConfigList.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = prizeConfigList.stream().mapToDouble(PrizeConfig::getProbability).sum();

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<>(size);
        Double tempSumRate = 0d;

        for (PrizeConfig rate : prizeConfigList) {
            tempSumRate += rate.getProbability();
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return prizeConfigList.get(sortOrignalRates.indexOf(nextDouble));
    }


    public static JSONObject getQcloudCredential() {
        JSONObject credential = null;
        TreeMap<String, Object> config = new TreeMap<>();
        try {
            // 替换为您的 SecretId
            config.put("SecretId", TencentConfig.SECRET_ID);
            // 替换为您的 SecretKey
            config.put("SecretKey", TencentConfig.SECRET_KEY);

            // 临时密钥有效时长，单位是秒，默认1800秒，目前主账号最长2小时（即7200秒），子账号最长36小时（即129600秒）
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", TencentConfig.BUCKET_NAME);
            // 换成 bucket 所在地区
            config.put("region", TencentConfig.REGION_ID);

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子：a.jpg 或者 a/* 或者 * 。
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefix", "tyson/*");

            // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            credential = CosStsClient.getCredential(config);
            //成功返回临时密钥信息，如下打印密钥信息
            log.info("credential:{}", credential);
        } catch (Exception e) {
            //失败抛出异常
            log.error("no valid secret");
        }
        return credential;
    }

    public static void main(String[] args) {
        getQcloudCredential();
    }
}
