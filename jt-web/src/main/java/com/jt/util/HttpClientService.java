package com.jt.util;

import com.jt.config.HttpClientClose;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

@Service
public class HttpClientService {

	@Autowired
	private RequestConfig requestConfig;
	@Autowired
	private CloseableHttpClient httpClient;;

	public String doGet(String url, Map<String,String> param,String charset){
		if(StringUtils.isEmpty(charset)){
			charset = "UTF-8";
		}
		if(param != null) {
			url += "?";
			for (Map.Entry <String, String> entry : param.entrySet()) {
				String name = entry.getKey();
				String value = entry.getValue();
				url += name + "=" + value + "&";
			}
			url = url.substring(0, url.length() - 1);
		}

		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,charset);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}

	public String doGet(String url) {

		return doGet(url, null, null);
	}

	public String doGet(String url,Map<String,String> params) {

		return doGet(url, params, null);
	}

}
