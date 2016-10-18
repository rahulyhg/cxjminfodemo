package com.example.cxjminfodemo.server.ao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import com.example.cxjminfodemo.RcConstant;
import com.example.cxjminfodemo.server.DTO.FamilyDTO;
import com.example.cxjminfodemo.server.DTO.FamilyMemberDTO;
import com.example.cxjminfodemo.server.DTO.MemberDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Ao {
	FamilyMemberDTO FM;

	public FamilyMemberDTO queryFamilyMember(String countryCode) throws ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// ����httpget.
		@SuppressWarnings("deprecation")
		HttpGet httpget = new HttpGet(RcConstant.httpPath + "jbxx/" + countryCode);
		System.out.println("executing request " + httpget.getURI());
		// ִ��get����.
		CloseableHttpResponse response = httpclient.execute(httpget);
		// ��ȡ��Ӧʵ��
		HttpEntity entity = response.getEntity();
		System.out.println("--------------------------------------");
		// ��ӡ��Ӧ״̬
		System.out.println(response.getStatusLine());

		if (entity != null) {
			// ��ӡ��Ӧ���ݳ���
			System.out.println("Response content length: " + entity.getContentLength());
			// ��ӡ��Ӧ����
			System.out.println("Response content: " + EntityUtils.toString(entity));
			Type typeList = new TypeToken<FamilyMemberDTO>() {
			}.getType();
			Gson gson = new Gson();
			FM = gson.fromJson(EntityUtils.toString(entity), typeList);
		}
		System.out.println("------------------------------------");
		response.close();
		// �ر�����,�ͷ���Դ
		httpclient.close();
		return FM;
	}

	/**
	 * maw@neuq 2016-9-7
	 * 
	 * @param cc22dto
	 * @param code
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AppException
	 * 
	 *             xzqh String �������� czr String ������ List<FamilyDTO> List ��ͥ��Ϣ
	 *             List<MemberDTO> List ��ͥ��Ա��Ϣ
	 */
	public void sendMessage(String xzqh, String czr, List<FamilyDTO> family, List<MemberDTO> member)
			throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		// ����httppost
		HttpPost httppost = new HttpPost(RcConstant.httpPath + "sendMessage");
		// ������������
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		
		Gson gson = new Gson();
		formparams.add(new BasicNameValuePair("type", "house"));
		UrlEncodedFormEntity uefEntity;

		uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		httppost.setEntity(uefEntity);
		System.out.println("executing request " + httppost.getURI());
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			System.out.println("--------------------------------------");
			System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
			System.out.println("--------------------------------------");
		}
		response.close();
		// �ر�����,�ͷ���

		httpclient.close();

	}

}