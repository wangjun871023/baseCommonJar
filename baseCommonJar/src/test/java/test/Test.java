package test;

import com.macrosoft.common.pathCode.PathCodeUtils;


public class Test {
	public static void main(String[] args) {
		String result = 
				PathCodeUtils.getLineIdBySequenceName
				("0003", "0002.0003", 4, true);
		System.out.println(result);
		
		
		
		
	}
}
