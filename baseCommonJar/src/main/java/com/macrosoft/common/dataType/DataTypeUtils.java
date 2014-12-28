package com.macrosoft.common.dataType;

import java.util.List;
import java.util.Vector;

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

	public static String fillStr(String src, int byte_len) {
		String fill_str = "";
		StringBuffer tempb = new StringBuffer();
		int temp_len = 0;
		int src_len = src.length();
		char zero = '\000';
		if (src_len < byte_len) {
			temp_len = byte_len - src_len;
			for (int i = 0; i < temp_len; i++) {
				tempb.append(zero);
			}
			fill_str = src + tempb.toString();
		} else {
			fill_str = src;
		}
		return fill_str;
	}

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

	public static String bytes2Str(byte[] b) {
		String result_str = new String(b);
		return result_str;
	}

	public static byte[] int2Bytes(int res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xFF);
		targets[1] = (byte) (res >> 8 & 0xFF);
		targets[2] = (byte) (res >> 16 & 0xFF);
		targets[3] = (byte) (res >>> 24);
		return targets;
	}

	public static byte[] char2Bytes(char ch) {
		int temp = ch;
		byte[] b = new byte[2];
		for (int i = b.length - 1; i > -1; i--) {
			b[i] = new Integer(temp & 0xFF).byteValue();
			temp >>= 8;
		}
		return b;
	}

	public static int char2Int(char c) {
		return c;
	}

	public static char int2Char(int i) {
		return (char) i;
	}

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

	public static int bytesSub2Int(byte[] src, int start, int src_size) {
		byte[] resBytes = new byte[src_size];
		System.arraycopy(src, start, resBytes, 0, src_size);
		return bytesToInt(resBytes);
	}

	public static String bytesSub2Str(byte[] src, int start, int src_size) {
		byte[] resBytes = new byte[src_size];
		System.arraycopy(src, start, resBytes, 0, src_size);
		return bytes2Str(resBytes);
	}

	public static byte[] bytesSub(byte[] src, int start, int src_size) {
		byte[] resBytes = new byte[src_size];
		System.arraycopy(src, start, resBytes, 0, src_size);
		return resBytes;
	}

	public static String[] splite(String src, String sep) {
		Vector v = new Vector();

		int fromIndex = 0;
		int index;
		while ((index = src.indexOf(sep, fromIndex)) != -1) {
			v.addElement(src.substring(fromIndex, index));
			fromIndex = index + sep.length();
		}
		v.addElement(src.substring(fromIndex, src.length()));
		String[] result = new String[v.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((String) v.elementAt(i));
		}
		return result;
	}

	public static String[] splite(String src, String sep, String sep_code) {
		String[] result = splite(src, sep);
		replace(result, sep_code, sep);
		return result;
	}

	public static String replace(String src, String oldStr, String newStr) {
		int oldSize = oldStr.length();
		int newSize = newStr.length();
		int margin = newSize - oldSize;
		int offset = 0;
		StringBuffer sb = new StringBuffer(src);

		int fromIndex = 0;
		int index;
		while ((index = src.indexOf(oldStr, fromIndex)) != -1) {
			fromIndex = index + oldSize;
			sb.delete(index + offset, fromIndex + offset);
			sb.insert(index + offset, newStr);
			offset += margin;
		}
		return sb.toString();
	}

	public static void replace(String[] src, String oldStr, String newStr) {
		for (int i = 0; i < src.length; i++)
			src[i] = replace(src[i], oldStr, newStr);
	}

	public static String getParaVal(String para, String src) {
		String paraval = null;
		String tempPara = para;
		int s1 = 0;
		int s2 = 0;
		int len = 0;
		int httplen = 0;

		if ((tempPara == null) || (src == null))
			return "";
		tempPara = tempPara + "=";
		len = tempPara.length();
		httplen = src.length();
		if (httplen == 0)
			return "";
		if (len == 0) {
			return "";
		}
		s1 = src.indexOf(tempPara);
		if (s1 == -1)
			return "";
		s2 = src.indexOf(38, s1);
		if (s2 == -1) {
			paraval = src.substring(s1 + len);
		} else {
			paraval = src.substring(s1 + len, s2);
		}

		return paraval;
	}

	public static String getParaValEx(String para, String src, char sep) {
		String paraval = null;
		String tempPara = para;
		int s1 = 0;
		int s2 = 0;
		int len = 0;
		int httplen = 0;

		if ((tempPara == null) || (src == null))
			return "";
		tempPara = tempPara + "=";
		len = tempPara.length();
		httplen = src.length();
		if (httplen == 0)
			return "";
		if (len == 0) {
			return "";
		}
		s1 = src.indexOf(tempPara);
		if (s1 == -1)
			return "";
		s2 = src.indexOf(sep, s1);
		if (s2 == -1) {
			paraval = src.substring(s1 + len);
		} else {
			paraval = src.substring(s1 + len, s2);
		}

		return paraval;
	}

	public static float getFloat(byte[] b, int index) throws Exception {
		int i = 0;
		i = (((b[(index + 3)] & 0xFF) << 8 | b[(index + 2)] & 0xFF) << 8 | b[(index + 1)] & 0xFF) << 8
				| b[(index + 0)] & 0xFF;

		return Float.intBitsToFloat(i);
	}

	public static float bytes2Float(byte[] b) {
		float f = 0.0F;
		try {
			f = getFloat(b, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public static short bytesToShort(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= bytes[1] << 8 & 0xFF00;
		return (short) addr;
	}

	public static double save2bit(double t1) {
		double result = (int) ((t1 + 0.005D) * 100.0D) / 100.0D;
		return result;
	}

	public static double save1bit(double t1) {
		double result = (int) ((t1 + 0.005D) * 10.0D) / 10.0D;
		return result;
	}

	public static int[] doSort(int[] source) {
		int length = source.length;
		for (int i = length - 1; i > 1; i--) {
			for (int j = 0; j < i; j++)
				if (source[j] > source[(j + 1)]) {
					int tmp = source[j];
					source[j] = source[(j + 1)];
					source[(j + 1)] = tmp;
				}
		}
		return source;
	}

	public static float[] doSort(float[] source) {
		int length = source.length;
		for (int i = length - 1; i > 0; i--) {
			for (int j = 0; j < i; j++)
				if (source[j] > source[(j + 1)]) {
					float tmp = source[j];
					source[j] = source[(j + 1)];
					source[(j + 1)] = tmp;
				}
		}
		return source;
	}

	public static Vector doKLineVectorSort(Vector vv) {
		Vector vvs = new Vector();
		int size = vv.size();
		for (int i = size - 1; i >= 0; i--) {
			vvs.addElement(vv.elementAt(i));
		}
		return vvs;
	}

	public static String filterStr(String str) {
		StringBuffer buffer = new StringBuffer();
		String filter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((c < '0') || (c > '9')) {
				for (int j = 0; j < 26; j++) {
					char cc = filter.charAt(j);
					if (cc == c)
						buffer.append(c);
				}
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public static String getFilterText(String src) {
		String str = src.trim();
		String str1 = replace(str, "&#8220;", "\"");
		String str2 = replace(str1, "&#8221;", "\"");
		String str3 = replace(str2, "&#183;", ".");
		return str3;
	}
}