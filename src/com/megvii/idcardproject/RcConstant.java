package com.megvii.idcardproject;

import com.megvii.idcardproject.utils.HttpManager;

/**
 * @绫诲瀷鍚嶇О锛歊cConstant锛圧c涓氬姟绋嬪簭涓殑甯搁噺锛� @涓昏鍔熻兘锛氫繚�?�楻c涓氬姟涓敤鍒扮殑甯搁噺鏍囪�?? @鍘熷浣滆�
 * 咃細闄堜繆宀�??2016骞�8鏈�29鏃�
 */
public class RcConstant {
    
	/********** 绋嬪簭鐨勮姹傚湴鍧�?? ***********/
	public static final String httpPath = "http://serverIP:serverPort/jmcj/services/";
	public static final String serverPath = "http://service.neuqsoft.com/cxjmcj/";
	//public static final String loginPath = "http://service.neuqsoft.com/cxjmcj/api/login";
	public static final String loginPath = "http://172.30.3.164:8082/cxjmcj/api/login";
	//public static final String usertasksPath = "http://service.neuqsoft.com/cxjmcj/api/usertasks";
	public static final String usertasksPath = "http://172.30.3.164:8082/cxjmcj/api/usertasks";
	/*
	 * public static final String testPath =
	 * "http://service.neuqsoft.com/jmcjyw"; public static final String getPath
	 * = testPath + "/services/jbxx/"; public static final String postPath =
	 * testPath + "/services/cjxx/";
	 */
	// public static final String getPath =
	// "http://10.19.95.68:8080/eapdomain/si/mobileInteraction.do?method=downloadData&eap_username=130302008&eap_password=123456&countycode=";
	// http://10.19.95.68:8080/eapdomain/si/mobileInteraction.do?method=downloadData&eap_username=130302008&eap_password=123456&countycode=130302013188
	// public static String getPath =
	// "http://172.30.2.11:9001/eapdomain/si/mobileInteraction.do?method=downloadData&eap_username=130729001001&eap_password=123456&countycode=";
	// public static String postPath =
	// "http://172.30.2.11:9001/eapdomain/si/mobileInteraction.do?method=uploadData&eap_username=130729001001&eap_password=123456&countyCode=";
	public static String postPath = "/eapdomain/si/mobileInteraction.do?method=uploadData&eap_username=modelapp&eap_password=123456&countyCode=";
	public static String getPath = "/eapdomain/si/mobileInteraction.do?method=downloadData&eap_username=modelapp&eap_password=123456&countyCode=";
	public static String codePath = "/eapdomain/si/mobileInteraction.do?method=aa10&eap_username=modelapp&eap_password=123456&aaa100=";
	//public static String xzqhPath = "http://172.30.2.11:9001/eapdomain/si/mobileInteraction.do?method=xzqh&eap_username=modelapp&eap_password=123456&countyCode=";
	public static String xzqhPath = "http://172.30.3.164:8082/cxjmcj/api/xzqh/";
}