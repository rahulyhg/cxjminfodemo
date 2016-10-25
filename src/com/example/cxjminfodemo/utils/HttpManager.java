package com.example.cxjminfodemo.utils;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.example.cxjminfodemo.RcConstant;
import com.example.cxjminfodemo.server.dto.CjUser;
import com.example.cxjminfodemo.server.dto.FamilyMemberDTO;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HttpManager extends HttpUtils {
	Gson gson=new Gson();
	HttpUtils httpUtils = new HttpUtils();

	/**
	 * get请求 用于下载家庭与成员信息 2016年10月24日14:59:49
	 */
	public void jbxx(String countryCode)
	{
		
		RequestParams params = new RequestParams();
		params.addHeader("Accept", "application/json");
		params.addHeader("Content-Type", "application/json");
		String url=RcConstant.getPath+countryCode;
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			// 请求失败调用次方法
			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("-------------------"+error);
			}

			// 请求成功调用此方法
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String temp = arg0.result;
				System.out.println(temp.toString()+"/n"+temp);
			}
		});
	}
}
