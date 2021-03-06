package com.neuqsoft.cxjmcj.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neuqsoft.cxjmcj.RcConstant;
import com.neuqsoft.cxjmcj.server.dto.FamilyDTO;
import com.neuqsoft.cxjmcj.server.dto.FamilyMemberDTO;
import com.neuqsoft.cxjmcj.server.dto.MemberDTO;
import com.neuqsoft.cxjmcj.server.dto.ResponseDTO;

public class Ao {
	FamilyMemberDTO FM;

	public FamilyMemberDTO queryFamilyMember(String countryCode) throws ParseException, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 创建httpget.
		@SuppressWarnings("deprecation")
		HttpGet httpget = new HttpGet(RcConstant.httpPath + "jbxx/" + countryCode);
		System.out.println("executing request " + httpget.getURI());
		// 执行get请求.
		HttpResponse response = httpclient.execute(httpget);
		// 获取响应实体
		HttpEntity entity = response.getEntity();
		System.out.println("--------------------------------------");
		// 打印响应状态
		System.out.println(response.getStatusLine());

		if (entity != null) {
			// 打印响应内容长度
			System.out.println("Response content length: " + entity.getContentLength());
			// 打印响应内容
			System.out.println("Response content: " + EntityUtils.toString(entity));
			Type typeList = new TypeToken<FamilyMemberDTO>() {
			}.getType();
			Gson gson = new Gson();
			FM = gson.fromJson(EntityUtils.toString(entity), typeList);
		}
		System.out.println("------------------------------------");
		return FM;
	}

	/**
	 * tengzj@neuq 2016-10-18
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AppException
	 * 
	 *             xzqh String 行政区划 czr String 操作人 List<FamilyDTO> List 家庭信息
	 *             List<MemberDTO> List 家庭成员信息
	 */
	public ResponseDTO sendMessage(String xzqh, String czr, List<FamilyDTO> family, List<MemberDTO> member)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 创建httppost
		HttpPost httppost = new HttpPost(RcConstant.httpPath + "sendMessage");
		ResponseDTO res = null;
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		
		Gson gson = new Gson();
		formparams.add(new BasicNameValuePair("xzqh",xzqh));
		formparams.add(new BasicNameValuePair("czr", czr));
		formparams.add(new BasicNameValuePair("List<FamilyDTO>", gson.toJson(family)));
		formparams.add(new BasicNameValuePair("List<MemberDTO>", gson.toJson(member)));

		UrlEncodedFormEntity uefEntity;

		uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		httppost.setEntity(uefEntity);
		System.out.println("executing request " + httppost.getURI());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			System.out.println("--------------------------------------");
			System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
			System.out.println("--------------------------------------");
			
			Type typeList = new TypeToken<ResponseDTO>() {
			}.getType();
			res = gson.fromJson(EntityUtils.toString(entity, "UTF-8"), typeList);
		}
		return res;
	}

}
