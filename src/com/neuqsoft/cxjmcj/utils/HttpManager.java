package com.neuqsoft.cxjmcj.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.neuqsoft.cxjmcj.RcConstant;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Code;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.dto.UserDetail;
import com.neuqsoft.cxjmcj.dto.Xzqh;
import com.neuqsoft.cxjmcj.server.dto.CjUser;
import com.neuqsoft.cxjmcj.server.dto.CodeDTO;
import com.neuqsoft.cxjmcj.server.dto.FamilyDTO;
import com.neuqsoft.cxjmcj.server.dto.FamilyMemberDTO;
import com.neuqsoft.cxjmcj.server.dto.MemberDTO;

import android.content.Context;
import android.content.SharedPreferences;

public class HttpManager extends HttpUtils {
	Gson gson = new Gson();
	HttpUtils httpUtils = new HttpUtils(1500);
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

	List<Family> DTOtoF(List<FamilyDTO> dto) {
		List<Family> familyList = new ArrayList<Family>();
		for (FamilyDTO d : dto) {
			Family family = new Family();
			family.edit_jtbh = d.getAab999();
			family.edit_hzxm = d.getAab400();
			family.edit_gmcfzh = d.getAae135();
			family.edit_jhzzjlx = d.getAac058();
			family.edit_hjbh = d.getAab401();
			family.edit_cjqtbxrs = d.getBab041();
			family.edit_lxdh = d.getAae005();
			family.edit_hkxxdz = d.getAae006();
			family.edit_djrq = d.getAab050();
			family.xzqh = country;
			family.isEdit = "0";
			family.isUpload = "1";
			family.id = d.getLsh();
			familyList.add(family);
		}
		return familyList;
	}

	List<Personal> DTOtoM(List<MemberDTO> dto) {
		List<Personal> personalList = new ArrayList<Personal>();
		for (MemberDTO d : dto) {
			Personal personal = new Personal();
			personal.edit_cbrxm = d.getAac003();
			personal.edit_gmcfzh = d.getAae135();
			personal.edit_grbh = d.getAac999();
			personal.edit_mz = d.getAac005();
			personal.edit_xb = d.getAac004();
			personal.edit_csrq = d.getAac006();
			personal.edit_cbrylb = d.getBac067();
			personal.edit_cbrq = d.getAac030();
			personal.edit_yhzgx = d.getAac069();
			personal.edit_xxjzdz = d.getAae006();
			personal.edit_hkxz = d.getAac009();
			personal.HZSFZ = d.getHzsfz();
			personal.edit_jf = d.getJfbz();
			personal.edit_zjlx = d.getAac058();
			personal.edit_lxdh = d.getAae005();
			personal.isUpload = "1";
			personal.isEdit = "0";
			personal.id = d.getLsh();
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

	List<MemberDTO> MtoDTO(List<Personal> dto) {
		// ����
		List<MemberDTO> personalList = new ArrayList<MemberDTO>();
		for (Personal d : dto) {
			MemberDTO personal = new MemberDTO();
			personal.setAac999(d.edit_grbh);
			personal.setAac003(d.edit_cbrxm);
			personal.setAae135(d.edit_gmcfzh);
			personal.setAac005(d.edit_mz);
			personal.setAac004(d.edit_xb);
			personal.setAac006(d.edit_csrq);
			personal.setBac067(d.edit_cbrylb);
			personal.setAac030(d.edit_cbrq);
			personal.setAac069(d.edit_yhzgx);
			personal.setAae006(d.edit_xxjzdz);
			personal.setAac009(d.edit_hkxz);
			personal.setHzsfz(d.HZSFZ);
			personal.setJfbz(d.edit_jf);
			personal.setAac058(d.edit_zjlx);
			personal.setAae005(d.edit_lxdh);
			personal.setXgbz(d.isEdit);
			personal.setLsh(MD5Util.encode(d.edit_gmcfzh));
			personalList.add(personal);
		}
		return personalList;
	}

	FamilyDTO FMtoDTO(Family d) {
		// ����
		FamilyDTO family = new FamilyDTO();
		family.setAab999(d.edit_jtbh);
		family.setAab400(d.edit_hzxm);
		family.setAae135(d.edit_gmcfzh);
		family.setAac058(d.edit_jhzzjlx);
		family.setAab401(d.edit_hjbh);
		family.setBab041(d.edit_cjqtbxrs);
		family.setAae005(d.edit_lxdh);
		family.setAae006(d.edit_hkxxdz);
		family.setAab050(d.edit_djrq);
		family.setLsh(MD5Util.encode(d.edit_gmcfzh));
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
				// �����Ա��Ϣ
				List<Personal> personal = db.queryPersonal(family.getEdit_gmcfzh());
				memberdto.addAll((MtoDTO(personal)));

				db.updateFamily(family);
				family.setIsUpload("1");
				List<Family> temp = new ArrayList<Family>();
				temp.add(family);
				db.addFamily(temp);
			}
		}
		f.setJt(familyDTOList);
		f.setRy(memberdto);
		return f;
	}

	/**
	 * get���� �������ؼ�ͥ���Ա��Ϣ 2016��10��24��14:59:49
	 * 
	 * @return
	 * 
	 * @return
	 * 
	 */
	public void getJbxx(final String countryCode) {
		country = countryCode;
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json");
		params.addHeader("Accept", "application/json");
		String url = RcConstant.getPath + countryCode;
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			// ����ʧ�ܵ��ôη���
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

			// ����ɹ����ô˷���
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				isError = false;
				String temp = arg0.result;
				System.out.println(temp);
				dto = gson.fromJson(temp, new TypeToken<FamilyMemberDTO>() {
				}.getType());
				// ����sql
				db.addFamily(DTOtoF(dto.getJt()));
				db.addPersonal(DTOtoM(dto.getRy()));
				// ���سɹ�user���download��1
				db.update_df(context, countryCode, "downloadflag");
				isAlive = false;
			}
		});
	}

	// �ϴ�
	public void getCjxx(final String countryCode, String usertoken, String account)
			throws UnsupportedEncodingException {
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
			// ����ʧ�ܵ��ôη���
			@Override
			public void onFailure(HttpException error, String msg) {
				isError = true;
				int exceptionCode = error.getExceptionCode();
				if (exceptionCode == 0) {
					errorMessage = "�������������Ƿ�������";
					System.out.println(errorMessage);
				}
				isAlive = false;
			}

			// ����ɹ����ô˷���
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				isError = false;
				System.out.println("�ϴ��ɹ�");
				System.out.println(arg0.result);
				db.update_df(context, countryCode, "uploadflag");
				isAlive = false;
			}
		});
	}

	// ��ô����
	public void getCode(final String aaa100) throws UnsupportedEncodingException {
		RequestParams params = new RequestParams();
		String url = RcConstant.codePath + aaa100;
		params.addHeader("Content-Type", "application/xml");
		params.addHeader("Accept", "application/xml");
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			// ����ʧ�ܵ��ôη���
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

			// ����ɹ����ô˷���
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				isError = false;
				String temp = arg0.result;
				System.out.println(temp);
				List<CodeDTO> dto = gson.fromJson(temp, new TypeToken<List<CodeDTO>>() {
				}.getType());
				// ����sql
				db.addCode(DTOtoC(dto));
				isAlive = false;
			}
		});
	}

	public void getXzqh(final String cjarea) throws UnsupportedEncodingException {
		RequestParams params = new RequestParams();
		String url = RcConstant.xzqhPath + cjarea;
		params.addHeader("Content-Type", "application/xml");
		params.addHeader("Accept", "application/xml");
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			// ����ʧ�ܵ��ôη���
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

			// ����ɹ����ô˷���
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				isError = false;
				String temp = arg0.result;
				System.out.println(temp);
				Xzqh dto = gson.fromJson(temp, new TypeToken<Xzqh>() {
				}.getType());
				// ����sql
				db.addXzqh(dto);
				isAlive = false;
			}
		});
	}

	/**
	 * ----��SP��ȡ��tokenֵ--- ---��������� --
	 */
	public void getUserDetail(String sToken) {
		HttpUtils httpUtils = new HttpUtils();
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
				// ��ȡ�û�����ϸ��Ϣ����ӵ�list��
				Gson gson = new Gson();
				List<UserDetail> list;
				list = gson.fromJson(result, new TypeToken<List<UserDetail>>() {
				}.getType());

				/**
				 * �ж�user�����Ƿ��д������� û�о���ӽ�ȥ
				 */
				String account = list.get(1).getAccount();
				String query_usern = db.query_usern(context, account);
				if (query_usern.isEmpty()) {
					db.addUserDetail(list);
				}
				isAlive = false;
			}
		});
	}
}
