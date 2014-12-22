package com.macrosoft.common.base64;

import org.junit.Test;

public class Base64Test {
	@Test
	public void test() throws Exception {
		Base64 base64 = new Base64();
//		System.out.println(base64.encode("wangjun"));//d2FuZ2p1bg==
//		System.out.println(base64.encodeByEncoding("王军", "gbk"));//546L5Yab
		System.out.println(base64.encode("王军", "gbk"));//zfW+/A==
		
	}
}