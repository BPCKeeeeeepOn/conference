package com.youyu.conference.utils.qiniuupload;

import com.qiniu.util.Auth;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class QiNiuDownload {

	@Autowired
	private QiNiuProperties qiNiuProperties;

	/**
	 * 下载
	 */
	public void download(String targetUrl) {
		// 获取downloadUrl
		Auth auth = Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
		String downloadUrl = auth.privateDownloadUrl(qiNiuProperties.getBucketUrl() + "/" + targetUrl);
		// 本地保存路径
		String filePath = "D:/";
		download(downloadUrl, filePath);
	}

	/**
	 * 通过发送http get 请求获取文件资源
	 * 
	 * @param url
	 * @param filePath
	 * @return
	 */
	public boolean download(String url, String filePath) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		try {
			Response resp = client.newCall(request).execute();
			if (resp.isSuccessful()) {
				ResponseBody body = resp.body();
				InputStream is = body.byteStream();
				byte[] data = readInputStream(is);
				FileOutputStream fops = new FileOutputStream(filePath);
				fops.write(data);
				fops.close();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 读取字节输入流内容
	 * 
	 * @param is
	 * @return
	 */
	private static byte[] readInputStream(InputStream is) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		byte[] buff = new byte[1024 * 2];
		int len = 0;
		try {
			while ((len = is.read(buff)) != -1) {
				writer.write(buff, 0, len);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toByteArray();
	}
}
