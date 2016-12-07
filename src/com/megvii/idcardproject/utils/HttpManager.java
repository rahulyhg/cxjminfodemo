package com.megvii.idcardproject.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.megvii.idcardproject.RcConstant;
import com.megvii.idcardproject.db.DBManager;
import com.megvii.idcardproject.dto.Code;
import com.megvii.idcardproject.dto.Family;
import com.megvii.idcardproject.dto.Personal;
import com.megvii.idcardproject.dto.UserDetail;
import com.megvii.idcardproject.dto.Xzqh;
import com.megvii.idcardproject.server.dto.CodeDTO;
import com.megvii.idcardproject.server.dto.FamilyDTO;
import com.megvii.idcardproject.server.dto.FamilyMemberDTO;
import com.megvii.idcardproject.server.dto.MemberDTO;

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
			family.edit_hkxxdz = d.getAac010();
			family.edit_jtxxdz = d.getAae006();
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
		// ����
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
		// ����
		FamilyDTO family = new FamilyDTO();
		family.setLsh(d.lsh);
		family.setAab999(d.edit_jtbh);
		family.setAab400(d.edit_hzxm);
		family.setAae135(d.edit_gmcfzh);
		family.setAac058(db.queryCodeFromName(d.edit_jhzzjlx));
		family.setAab401(d.edit_hjbh);
		family.setBab041(d.edit_cjqtbxrs);
		family.setAae005(d.edit_lxdh);
		family.setAac010(d.edit_hkxxdz);
		family.setAae006(d.edit_jtxxdz);
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
			}

			if (family.isUpload.equals("2")) {
				if (family.getIsEdit().equals("1")) {
					familyDTOList.add(FMtoDTO(family));
				}
			}
			List<Personal> personals = db.queryPersonal(family.getEdit_jtbh());
			for (Personal personal : personals) {
				if (personal.getIsUpload().equals("0") && personal.getEdit_jf().equals("1")) {
					memberdto.add(MtoDTO(personal));
				}

				if (personal.getIsUpload().equals("2")) {
					if (personal.getIsEdit().equals("1") && personal.getEdit_jf().equals("1")) {
						memberdto.add(MtoDTO(personal));
					}
				}
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
		Init();
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
				// ���سɹ�user����download��1
				isAlive = false;
			}
		});
	}

	// �ϴ�
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
				isAlive = false;
				UpdateUpload();
			}

			private void UpdateUpload() {
				// TODO Auto-generated method stub
				List<Family> familys = db.queryFamily(country);
				for (Family family : familys) {
					if (family.isUpload.equals("0")) {
						family.setIsUpload("1");
						db.updateFamily(family);
					}

					if (family.isUpload.equals("2")) {
						if (family.getIsEdit().equals("1")) {
							family.setIsUpload("1");
							db.updateFamily(family);
						}
					}
					List<Personal> personals = db.queryPersonal(family.getEdit_jtbh());
					for (Personal personal : personals) {
						if (personal.getIsUpload().equals("0") && personal.getEdit_jf().equals("1")) {
							personal.setIsUpload("1");
							db.updatePersonal(personal);
						}

						if (personal.getIsUpload().equals("2")) {
							if (personal.getIsEdit().equals("1") && personal.getEdit_jf().equals("1")) {
								personal.setIsUpload("1");
								db.updatePersonal(personal);
							}
						}
					}
				}
			}
		});
	}

	// ��ô����
	public void getCode(final String aaa100, String xzqh) throws UnsupportedEncodingException {
		Init();
		RequestParams params = new RequestParams();
		String url = RcConstant.codePath + aaa100 + "&countyCode=" + xzqh;
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
		Init();
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
				// ��ȡ�û�����ϸ��Ϣ�����ӵ�list��
				Gson gson = new Gson();
				List<UserDetail> list;
				list = gson.fromJson(result, new TypeToken<List<UserDetail>>() {
				}.getType());

				/**
				 * �ж�user�����Ƿ��д������� û�о����ӽ�ȥ
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