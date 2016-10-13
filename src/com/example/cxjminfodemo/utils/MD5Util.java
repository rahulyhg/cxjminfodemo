package com.example.cxjminfodemo.utils;

import java.security.MessageDigest;

public class MD5Util {
	public static String encode(String str){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(str.getBytes());
			StringBuffer buffer=new StringBuffer();
			for(byte b:result){
				int number=b&255;
				String numberStr = Integer.toHexString(number);
				if(numberStr.length()==1){
					buffer.append("0");
				}
				buffer.append(numberStr);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
