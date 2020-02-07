package com.jt;

import com.jt.util.HttpClientService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class HttpClientTest {

	@Test
	public void deGet() throws IOException {
		HttpClient httpClient = HttpClients.createDefault();
		String url = "http://www.baidu.com";
		HttpGet get = new HttpGet(url);
		HttpResponse response = httpClient.execute(get);
		if(response.getStatusLine().getStatusCode() == 200){
			System.out.println("请求成功");
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity,"utf8");
			System.out.println(result);

		}else{
			System.out.println("请求失败");
		}

	}

	@Autowired
	private HttpClientService service;
	@Test
	public void test1(){
		String url = "http://www.baidu.com";
		Map<String,String> param = new HashMap <>();
		param.put("id","1");
		param.put("id2","2");
		String s = service.doGet(url,param,null);
		System.out.println(s);

	}
}
