package com.macrosoft.common.md5;

import java.security.MessageDigest;

/**
 * MD5加密字符串
 * 
 * @author 呆呆
 */
public final class MD5 {
	public static final String MD5_ENCRY = "MD5";
	/**
	 * 单例模式
	 */
	private static MD5 instance = new MD5();

	/**
	 * 私有构造器
	 */
	private MD5() {
	}

	/**
	 * 单例模式返回单例对象
	 * 
	 * @return md5单例对象
	 */
	public static MD5 getInstance() {
		return instance;
	}


	/**
	 * 31位
	 * 得到MD5值
	 * @param src_txt
	 * @return
	 */
	public static String getMD5(String src_txt) {
		String result = null;
		byte[] bArr = null;
		try {
			if (src_txt != null) {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(src_txt.getBytes());
				bArr = digest.digest();
				result = bytesToHexStr(bArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			bArr = null;
		}
		return result;
	}

	/**
	 * 字节到16进制字符串
	 * @param b
	 * @return
	 */
	private static String bytesToHexStr(byte[] b) {
		StringBuffer result = new StringBuffer("");
		int i = 0;
		for (int len = b.length; i < len; i++) {
			result.append(Integer.toHexString(b[i] & 0xFF));
		}
		return result.toString();
	}

	/**
	 * 32位MD5
	 * @param src_txt
	 * @return
	 */
	public static String getMD5MD5(String src_txt) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src_txt.getBytes());
			byte[] b = md.digest();
			StringBuffer buf = new StringBuffer("");
			int offset = 0;
			for (int len = b.length; offset < len; offset++) {
				int i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String text = "admin";
		System.out.println(getMD5(text));
		System.out.println(getMD5MD5(text));
	}
}