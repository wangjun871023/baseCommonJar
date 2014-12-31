package com.macrosoft.common.dataType;

import java.util.List;
import java.util.Vector;

/**
 * 数据类型工具类
 * @author 呆呆 
 *
 */
public class DataTypeUtils {
	/**
	 * 功能描述：判断是否为质数
	 * 
	 * @param x
	 * @return
	 */
	public static boolean isPrime(int x) {
		if (x <= 7) {
			if (x == 2 || x == 3 || x == 5 || x == 7)
				return true;
		}
		int c = 7;
		if (x % 2 == 0)
			return false;
		if (x % 3 == 0)
			return false;
		if (x % 5 == 0)
			return false;
		int end = (int) Math.sqrt(x);
		while (c <= end) {
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 6;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 6;
		}
		return true;
	}
	/**
	 * 判断Long对象是否为空,空是指: null 或 0
	 * 
	 * @param a_value
	 * @return
	 */
	public static boolean isEmpty(Long str) {
		if (str == null || str.longValue() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmpty(Integer value) {
		return value == null;
	}

	public static boolean isEmpty(Float value) {
		return value == null;
	}

	public static boolean isEmpty(Double value) {
		return value == null;
	}

	public static boolean isEmpty(Object[] value) {
		return value == null;
	}

	public static boolean isEmpty(List value) {
		return value == null;
	}
	
	public static boolean isNull(Integer str) {
		return isEmpty(str);
	}

	public static boolean isNull(Long str) {
		return isEmpty(str);
	}

	public static boolean isNull(Float str) {
		return isEmpty(str);
	}

	public static boolean isNull(Double str) {
		return isEmpty(str);
	}

	public static boolean isNull(Object[] str) {
		return isEmpty(str);
	}

	public static boolean isNull(List str) {
		return isEmpty(str);
	}
	/**
	 * 字符串到字节数组
	 * @param src
	 * @param byte_len
	 * @return
	 */
	public static byte[] str2Bytes(String src, int byte_len) {
		byte[] b1 = new byte[byte_len];
		StringBuffer tempb = new StringBuffer();
		int temp_len = 0;
		int src_len = src.length();
		char zero = '\000';
		if (src_len < byte_len) {
			temp_len = byte_len - src_len;
			for (int i = 0; i < temp_len; i++) {
				tempb.append(zero);
			}
			b1 = (src + tempb.toString()).getBytes();
		} else {
			b1 = src.getBytes();
		}
		return b1;
	}
	/**
	 * 字节数组到字符串
	 * @param b
	 * @return
	 */
	public static String bytes2Str(byte[] b) {
		String result_str = new String(b);
		return result_str;
	}
	/**
	 * 字节数组到整数
	 * @param bytes
	 * @return
	 */
	public static int bytes2Int1(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= bytes[1] << 8 & 0xFF00;
		return num;
	}

	public static int bytes2Int2(byte[] intByte) {
		int fromByte = 0;
		for (int i = 0; i < 2; i++) {
			int n = (intByte[i] < 0 ? intByte[i] + 256 : intByte[i]) << 8 * i;
			fromByte += n;
		}
		return fromByte;
	}

	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= bytes[1] << 8 & 0xFF00;
		addr |= bytes[2] << 16 & 0xFF0000;
		addr |= bytes[3] << 24 & 0xFF000000;
		return addr;
	}
	/**
	 * 整数到字节数组
	 * @param res
	 * @return
	 */
	public static byte[] int2Bytes(int res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xFF);
		targets[1] = (byte) (res >> 8 & 0xFF);
		targets[2] = (byte) (res >> 16 & 0xFF);
		targets[3] = (byte) (res >>> 24);
		return targets;
	}
	/**
	 * 字符到字节数组
	 * @param ch
	 * @return
	 */
	public static byte[] char2Bytes(char ch) {
		int temp = ch;
		byte[] b = new byte[2];
		for (int i = b.length - 1; i > -1; i--) {
			b[i] = new Integer(temp & 0xFF).byteValue();
			temp >>= 8;
		}
		return b;
	}
	/**
	 * 字符到整数
	 * @param c
	 * @return
	 */
	public static int char2Int(char c) {
		return c;
	}
	/**
	 * 整数到字符
	 * @param i
	 * @return
	 */
	public static char int2Char(int i) {
		return (char) i;
	}
	/**
	 * 以万或亿显示float
	 * @param f1
	 * @return
	 */
	public static String getF10000(float f1) {
		String str = "";
		double d1 = 0.0D;
		if ((f1 > 10000.0F) && (f1 < 100000000.0D)) {
			d1 = f1 / 10000.0F;
			str = save2bit(d1) + "万";
		} else if (f1 > 100000000.0D) {
			d1 = f1 / 100000000.0D;
			str = save2bit(d1) + "亿";
		} else {
			d1 = f1;
			str = save2bit(d1) + "";
		}
		return str;
	}

	/**
	 * 字节数组截取成整数
	 * @param src
	 * @param start
	 * @param src_size
	 * @return
	 */
	public static int bytesSub2Int(byte[] src, int start, int src_size) {
		byte[] resBytes = new byte[src_size];
		System.arraycopy(src, start, resBytes, 0, src_size);
		return bytesToInt(resBytes);
	}

	/**
	 * 字节数组截取成字符串
	 * @param src
	 * @param start
	 * @param src_size
	 * @return
	 */
	public static String bytesSub2Str(byte[] src, int start, int src_size) {
		byte[] resBytes = new byte[src_size];
		System.arraycopy(src, start, resBytes, 0, src_size);
		return bytes2Str(resBytes);
	}

	/**
	 * 字节数组截取
	 * @param src
	 * @param start
	 * @param src_size
	 * @return
	 */
	public static byte[] bytesSub(byte[] src, int start, int src_size) {
		byte[] resBytes = new byte[src_size];
		System.arraycopy(src, start, resBytes, 0, src_size);
		return resBytes;
	}

	/**
	 * 取字节数组中的index位置上的Float
	 * @param b
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public static float getFloat(byte[] b, int index) throws Exception {
		int i = 0;
		i = (((b[(index + 3)] & 0xFF) << 8 | b[(index + 2)] & 0xFF) << 8 | b[(index + 1)] & 0xFF) << 8
				| b[(index + 0)] & 0xFF;

		return Float.intBitsToFloat(i);
	}

	/**
	 * 字节数组转化浮点
	 * @param b
	 * @return
	 */
	public static float bytes2Float(byte[] b) {
		float f = 0.0F;
		try {
			f = getFloat(b, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 字节数组变成Short
	 * @param bytes
	 * @return
	 */
	public static short bytesToShort(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= bytes[1] << 8 & 0xFF00;
		return (short) addr;
	}

	/**将double转化为bit存储
	 * @param t1
	 * @return
	 */
	public static double save2bit(double t1) {
		double result = (int) ((t1 + 0.005D) * 100.0D) / 100.0D;
		return result;
	}

	public static double save1bit(double t1) {
		double result = (int) ((t1 + 0.005D) * 10.0D) / 10.0D;
		return result;
	}

	
	public static void main(String[] args) {
		
		
		
		
	}
}