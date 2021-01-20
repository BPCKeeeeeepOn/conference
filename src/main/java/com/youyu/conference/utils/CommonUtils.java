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
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.config.TencentConfig;
import com.youyu.conference.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CommonUtils {

    static Set<String> IMG_TYPE_DICT = Stream.of(".png", ".jpg", ".jpeg", ".gif").collect(Collectors.toSet());
    static Set<String> VIDEO_TYPE_DICT = Stream.of(".mp4", ".mpeg4", ".m4v", ".mov").collect(Collectors.toSet());
    private final static String PREFIX = "tyson";


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
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return map;
    }

    /**
     * 随机生成6位数
     *
     * @return
     */
    public static String randomNum() {
        int num = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(num);
    }

    /**
     * @param number 需要保留两位的数
     * @return
     */
    public static double doubleFormat(double number) {
        // 将double类型转为BigDecimal
        BigDecimal bigDecimal = new BigDecimal(number);
        // 保留两位小数,并且四舍五入
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param number 需要保留五位的数
     * @return
     */
    public static double doubleFormat5(double number) {
        // 将double类型转为BigDecimal
        BigDecimal bigDecimal = new BigDecimal(number);
        // 保留两位小数,并且四舍五入
        return bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
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
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
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
}
