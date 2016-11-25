package com.neuqsoft.cxjmcj.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.neuqsoft.cxjmcj.RcConstant;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Code;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.dto.UserDetail;
import com.neuqsoft.cxjmcj.dto.Xzqh;
import com.neuqsoft.cxjmcj.server.dto.CodeDTO;
import com.neuqsoft.cxjmcj.server.dto.FamilyDTO;
import com.neuqsoft.cxjmcj.server.dto.FamilyMemberDTO;
import com.neuqsoft.cxjmcj.server.dto.MemberDTO;
import com.neuqsoft.cxjmcj.server.dto.ResponseDTO;

import android.content.Context;

public class HttpManager extends HttpUtils {
	Gson gson = new Gson();
	HttpUtils httpUtils = new HttpUtils(3000);
	public FamilyMemberDTO dto;
	public boolean isAlive = true;
	public boolean isError = false;
	public String errorMessage = "";
	Context context;
	DBManager db;
	String country = "";

	public HttpManager(Context context) {
		this.context = context;
		db = new DBManager(context);
	}

	public void Init() {
		isAlive = true;
		isError = false;
	}

	List<Family> DTOtoF(List<FamilyDTO> dto) {
		List<Family> familyList = new ArrayList<Family>();
		for (FamilyDTO d : dto) {
			Family family = new Family();
			family.lsh = d.getLsh();
			family.edit_jtbh = d.getAab999();
			family.edit_hzxm = d.getAab400();
			family.edit_gmcfzh = d.getAae135();
			family.edit_jhzzjlx = db.queryCodeFromName(d.getAac058());
			family.edit_hjbh = d.getAab401();
			family.edit_cjqtbxrs = d.getBab041();
			family.edit_lxdh = d.getAae005();
			family.edit_hkxxdz = d.getAae006();
			family.edit_djrq = d.getAab050();
			family.xzqh = country;
			family.isEdit = "0";
			family.isUpload = "2";
			familyList.add(family);
		}
		return familyList;
	}

	List<Personal> DTOtoM(List<MemberDTO> dto) {
		List<Personal> personalList = new ArrayList<Personal>();
		for (MemberDTO d : dto) {
			Personal personal = new Personal();
			personal.lsh = d.getLsh();
			personal.edit_cbrxm = d.getAac003();
			personal.edit_gmcfzh = d.getAae135();
			personal.edit_grbh = d.getAac999();
			personal.edit_mz = db.queryCodeFromName(d.getAac005());
			personal.edit_xb = db.queryCodeFromName(d.getAac004());
			personal.edit_csrq = d.getAac006();
			personal.edit_cbrylb = db.queryCodeFromName(d.getBac067());
			personal.edit_cbrq = d.getAac030();
			personal.edit_yhzgx = db.queryCodeFromName(d.getAac069());
			personal.edit_xxjzdz = d.getAae006();
			personal.edit_hkxz = db.queryCodeFromName(d.getAac009());
			personal.HZSFZ = d.getHzsfz();
			personal.edit_jf = d.getJfbz();
			personal.edit_zjlx = db.queryCodeFromName(d.getAac058());
			personal.edit_lxdh = d.getAae005();
			personal.isUpload = "2";
			personal.isEdit = "0";
			personalList.add(personal);
		}
		return personalList;
	}

	List<Code> DTOtoC(List<CodeDTO> dto) {
		List<Code> Codes = new ArrayList<Code>();
		for (CodeDTO c : dto) {
			Code code = new Code();
			code.AAA100 = c.getAAA100();
			code.AAA101 = c.getAAA101();
			code.AAA102 = c.getAAA102();
			code.AAA103 = c.getAAA103();
			Codes.add(code);
		}
		return Codes;
	}

	private MemberDTO MtoDTO(Personal d) {
		// 下载
		MemberDTO personal = new MemberDTO();
		personal.setLsh(d.lsh);
		personal.setAac999(d.edit_grbh);
		personal.setAac003(d.edit_cbrxm);
		personal.setAae135(d.edit_gmcfzh);
		personal.setAac005(db.queryCodeFromName(d.edit_mz));
		personal.setAac004(db.queryCodeFromName(d.edit_xb));
		personal.setAac006(d.edit_csrq);
		personal.setBac067(db.queryCodeFromName(d.edit_cbrylb));
		personal.setAac030(d.edit_cbrq);
		personal.setAac069(db.queryCodeFromName(d.edit_yhzgx));
		personal.setAae006(d.edit_xxjzdz);
		personal.setAac009(db.queryCodeFromName(d.edit_hkxz));
		personal.setHzsfz(d.HZSFZ);
		personal.setJfbz(d.edit_jf);
		personal.setAac058(db.queryCodeFromName(d.edit_zjlx));
		personal.setAae005(d.edit_lxdh);
		personal.setXgbz(d.isEdit);
		return personal;
	}

	FamilyDTO FMtoDTO(Family d) {
		// 下载
		FamilyDTO family = new FamilyDTO();
		family.setLsh(d.lsh);
		family.setAab999(d.edit_jtbh);
		family.setAab400(d.edit_hzxm);
		family.setAae135(d.edit_gmcfzh);
		family.setAac058(db.queryCodeFromName(d.edit_jhzzjlx));
		family.setAab401(d.edit_hjbh);
		family.setBab041(d.edit_cjqtbxrs);
		family.setAae005(d.edit_lxdh);
		family.setAae006(d.edit_hkxxdz);
		family.setAab050(d.edit_djrq);
		return family;
	}

	private FamilyMemberDTO getInfo(FamilyMemberDTO f) {
		// TODO Auto-generated method stub
		List<FamilyDTO> familyDTOList = new ArrayList<FamilyDTO>();
		List<MemberDTO> memberdto = new ArrayList<MemberDTO>();

		List<Family> familys = db.queryFamily(country);
		for (Family family : familys) {

			if (family.isUpload.equals("0")) {
				familyDTOList.add(FMtoDTO(family));
				db.updateFamily(family);
				family.setIsUpload("1");
				List<Family> temp = new ArrayList<Family>();
				temp.add(family);
				db.addFamily(temp);
			}

			if (family.isUpload.equals("2")) {
				if (family.getIsEdit().equals("1")) {
					familyDTOList.add(FMtoDTO(family));
					db.updateFamily(family);
					family.setIsUpload("1");
					List<Family> temp = new ArrayList<Family>();
					temp.add(family);
					db.addFamily(temp);
				}
			}

			List<Personal> personals = db.queryPersonal(family.getEdit_jtbh());
			for (Personal personal : personals) {
				if (personal.getIsUpload().equals("0") && personal.getEdit_jf().equals("1")) {
					memberdto.add(MtoDTO(personal));
					db.deletePersonal(personal);
					personal.setIsUpload("1");
					List<Personal> temp = new ArrayList<Personal>();
					temp.add(personal);
					db.addPersonal(temp);
				}

				if (personal.getIsUpload().equals("2")) {
					if (personal.getIsEdit().equals("1") && personal.getEdit_jf().equals("1")) {
						memberdto.add(MtoDTO(personal));
						db.deletePersonal(personal);
						personal.setIsUpload("1");
						List<Personal> temp = new ArrayList<Personal>();
						temp.add(personal);
						db.addPersonal(temp);
					}
				}
			}
		}
		f.setJt(familyDTOList);
		f.setRy(memberdto);
		return f;
	}

	/**
	 * get请求 用于下载家庭与成员信息 2016年10月24日14:59:49
	 * 
	 * @return
	 * 
	 * @return
	 * 
	 */
	public void getJbxx(final String countryCode) {
		Init();
		country = countryCode;
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json");
		params.addHeader("Accept", "application/json");
		String url = RcConstant.getPath + countryCode;
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			// 请求失败调用次方法
			@Override
			public void onFailure(HttpException error, String msg) {
				isError = true;

				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}).start();
				isAlive = false;
			}

			// 请求成功调用此方法
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				isError = false;
				String temp = arg0.result;
				System.out.println(temp);
				dto = gson.fromJson(temp, new TypeToken<FamilyMemberDTO>() {
				}.getType());
				// 存入sql
				db.addFamily(DTOtoF(dto.getJt()));
				db.addPersonal(DTOtoM(dto.getRy()));
				// 下载成功user表的download置1
				isAlive = false;
			}
		});
	}

	// 上传
	public void getCjxx(final String countryCode, String usertoken, String account)
			throws UnsupportedEncodingException {
		Init();
		country = countryCode;
		RequestParams params = new RequestParams();
		String url = RcConstant.postPath + countryCode;
		params.addHeader("Content-Type", "application/json");
		params.addHeader("Accept", "application/json");
		params.addHeader("usertoken", usertoken);
		FamilyMemberDTO f = new FamilyMemberDTO();
		f = getInfo(f);
		f.setXzqh(countryCode);
		f.setCzr(account);
		f.setSfcl("");
		String jsonStr = gson.toJson(f);
		params.setBodyEntity(new StringEntity(jsonStr, "utf-8"));

		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			// 请求失败调用次方法
			@Override
			public void onFailure(HttpException error, String msg) {
				isError = true;
				int exceptionCode = error.getExceptionCode();
				if (exceptionCode == 0) {
					errorMessage = "请检查网络连接是否正常！";
					System.out.println(errorMessage);
				}
				isAlive = false;
			}

			// 请求成功调用此方法
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				isError = false;
				System.out.println("上传成功");
				System.out.println(arg0.result);
				isAlive = false;
			}
		});
	}

	// 获得代码表
	public void getCode(final String aaa100) throws UnsupportedEncodingException {
		Init();
		RequestParams params = new RequestParams();
		String url = RcConstant.codePath + aaa100;
		params.addHeader("Content-Type", "application/xml");
		params.addHeader("Accept", "application/xml");
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			// 请求失败调用次方法
			@Override
			public void onFailure(HttpException error, String msg) {
				isError = true;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				isAlive = false;
			}

			// 请求成功调用此方法
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String temp = arg0.result;
				System.out.println(temp);
				List<CodeDTO> dto = gson.fromJson(temp, new TypeToken<List<CodeDTO>>() {
				}.getType());
				// 存入sql
				db.addCode(DTOtoC(dto));
				isAlive = false;
			}
		});
	}

	public void getXzqh(final String cjarea) throws UnsupportedEncodingException {
		Init();
		RequestParams params = new RequestParams();
		String url = RcConstant.xzqhPath + cjarea;
		params.addHeader("Content-Type", "application/xml");
		params.addHeader("Accept", "application/xml");
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			// 请求失败调用次方法
			@Override
			public void onFailure(HttpException error, String msg) {
				isError = true;

				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}).start();
				isAlive = false;
			}

			// 请求成功调用此方法
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				isError = false;
				String temp = arg0.result;
				System.out.println(temp);
				Xzqh dto = gson.fromJson(temp, new TypeToken<Xzqh>() {
				}.getType());
				// 存入sql
				db.addXzqh(dto);
				isAlive = false;
			}
		});
	}

	/**
	 * ----从SP中取出token值--- ---请求服务器 --
	 */
	public void getUserDetail(String sToken) {
		Init();
		httpUtils.configCurrentHttpCacheExpiry(0);
		RequestParams params1 = new RequestParams();
		params1.addHeader("token", sToken);
		params1.addHeader("Content-Type", "application/json;charset=utf-8");
		params1.addHeader("Accept", "*/*");
		params1.addHeader("client_id", "1");
		httpUtils.send(HttpMethod.GET, RcConstant.usertasksPath, params1, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException error, String msg) {
				isError = true;

				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}).start();
				isAlive = false;
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				isError = false;
				String result = responseInfo.result;
				// 获取用户的详细信息并添加到list中
				Gson gson = new Gson();
				List<UserDetail> list;
				list = gson.fromJson(result, new TypeToken<List<UserDetail>>() {
				}.getType());

				/**
				 * 判断user表中是否有此条数据 没有就添加进去
				 */
				String account = list.get(0).getAccount();
				String query_usern = db.query_usern(context, account);
				if (query_usern.isEmpty()) {
					db.addUserDetail(list);
				}
				isAlive = false;
			}
		});
	}
}
