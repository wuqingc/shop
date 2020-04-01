package com.lele.seckill_shop.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lele.seckill_shop.domain.User;
import com.lele.seckill_shop.result.Result;
import okhttp3.*;


public class UserUtil {

	public static final MediaType JSON
			= MediaType.get("application/json; charset=utf-8");
	
	private static void createUser(int count) throws Exception {
		/*
		 * 生成用户.
		 */
		List<User> users = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			User user = new User();
			user.setId(13000000000L + i);
			user.setLoginCount(1);
			user.setNickname("user" + i);
			user.setRegisterDate(new Date());
			user.setSalt("1a2b3c");
			user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
			users.add(user);
		}


		for (User user : users) {
			OkHttpClient client = new OkHttpClient();
			String url = "http://localhost:8080/insert";
			String json = "{\"mobile\":" + user.getId() + ",\"password\":\"" +
					MD5Util.inputPassToFormPass("123456") + "\"}";
			RequestBody body = RequestBody.create(json, JSON);
			Request request = new Request.Builder()
					.url(url)
					.post(body)
					.build();
			try (Response response = client.newCall(request).execute()) {

			}
		}


		File file = new File("D:/tokens.txt");
		if(file.exists()) {
			file.delete();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		for (User user : users) {
			OkHttpClient client = new OkHttpClient();
			String url = "http://localhost:8080/doLogin";
			String json = "{\"mobile\":" + user.getId() + ",\"password\":\"" +
					MD5Util.inputPassToFormPass("123456") + "\"}";
			RequestBody body = RequestBody.create(json, JSON);
			Request request = new Request.Builder()
					.url(url)
					.post(body)
					.build();
			try (Response response = client.newCall(request).execute()) {
				Result<String> result = JSONObject.parseObject(response.body().string(),Result.class);
				fileOutputStream.write((user.getId() + ",").getBytes());
				fileOutputStream.write(result.getData().getBytes());
				fileOutputStream.write("\n".getBytes());
			}
		}
	}
	
	public static void main(String[] args)throws Exception {
		createUser(1000);
	}
}
