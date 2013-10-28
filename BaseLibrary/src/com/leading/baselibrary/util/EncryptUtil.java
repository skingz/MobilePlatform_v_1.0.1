package com.leading.baselibrary.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密.
 * @author Jiantao.tu
 *
 */
public class EncryptUtil {


	/**
	 * 单字符串加密.
	 * @author JianTao.tu
	 * @param s
	 * @return
	 */
	public static String md5One(String s){
		MessageDigest  md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        md.update(s.getBytes());
        return byteArrayToHexString(md.digest());
	}
	

	/**
	 * 多字符串加密
	 * @author JianTao.tu
	 * @param clientId
	 * @param pwd
	 * @param timestamp
	 * @return
	 */
	public static String md5Three(String clientId,String pwd,String timestamp){
			clientId=clientId==null?"":clientId;
			pwd=pwd==null?"":pwd;
			timestamp=timestamp==null?"":timestamp;
			while(timestamp.length()<10){
				timestamp="0"+timestamp;
			}
			MessageDigest  md = null;
	        try {
	            md = MessageDigest.getInstance("MD5");
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e.getMessage());
	        }
	        md.update(clientId.getBytes());
	        md.update(new byte[7]);
	        md.update(pwd.getBytes());
	        md.update(timestamp.getBytes());
	        
	        return byteArrayToHexString(md.digest());        
	}
	private static String[] HexCode ={ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteToHexString(byte b) {
			int n = b;
			if (n < 0) 
				n = 256 + n;
			int d1 = n / 16;
			int d2 = n % 16;
			return HexCode[d1] + HexCode[d2];
	}

	public static String byteArrayToHexString(byte[] b) {
			String result = "";
			for (int i = 0; i < b.length; i++) {
				result = result + byteToHexString(b[i]);
			}
			return result;
	}
	
	
}

