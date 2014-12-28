package com.macrosoft.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.macrosoft.common.base64.Base64Utils;
import com.macrosoft.common.constant.CommonConst;
import com.macrosoft.common.log.LoggerUtils;

public class StringUtils {
	/**
	 * StringBuffer是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(StringBuffer value) {
		return (value == null) || (isNull(value.toString()))
				|| (value.toString().trim().length() == 0);
	}

	/**
	 * 判断字符串是否为空,空是指: null 或 空串 或 全是空格的字符串
	 * 
	 * @param a_value
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (isNull(str) || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为null对象
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		boolean result = false;
		if (str == null) {
			result = true;
		}
		return result;
	}

	/**
	 * 字符数组是否为空
	 * 
	 * @param param
	 * @return
	 */
	public static boolean arrIsEmpty(String[] param) {
		boolean result = true;
		if ((param != null) && (param.length > 0)) {
			int i = 0;
			for (int len = param.length; i < len; i++) {
				if (!isEmpty(param[i])) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 如果str为null,给str赋值""
	 * 
	 * @param str
	 * @return
	 */
	public static String checkNull(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}

	/**
	 * 如果str为null,给str赋值
	 * 
	 * @param str
	 * @param value
	 * @return
	 */
	public static String checkNull(String str, String value) {
		if (isEmpty(str) && value != null) {
			str = value;
		}
		return str;
	}

	/**
	 * 功能描述：是否为空白,包括null和""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 将数组转为String 以默认的分隔符
	 * 
	 * @param param
	 * @return
	 */
	public static String arrToString(String[] param) {
		return arrJoin(param, null);
	}

	/**
	 * 将数组转为String 指定的分隔符
	 * 
	 * @param param
	 * @param splitStr
	 * @return
	 */
	public static String arrToString(String[] param, String splitStr) {
		return arrJoin(param, splitStr);
	}

	/**
	 * 数组转换以","分隔的字符串
	 * 
	 * @param param
	 * @return
	 */
	public static String arrJoin(String[] param) {
		StringBuffer result = new StringBuffer("");
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				if (i > 0) {
					result.append(CommonConst.SYS_SPLIT_COMMA);
				}
				result.append("'" + param[i] + "'");
			}
		}
		return result.toString();
	}

	/**
	 * 数组转换以指定分隔符连接的字符串
	 * 
	 * @param param
	 * @return
	 */
	public static String arrJoin(String[] param, String splitStr) {
		StringBuffer result = new StringBuffer("");
		if (isEmpty(splitStr) == true) {
			splitStr = CommonConst.SYS_SPLIT_COLON;
		}
		if (param != null && param.length > 0) {
			int size = param.length;
			for (int i = 0; i < size; i++) {
				if (i > 0) {
					result.append(splitStr);
				}
				result.append(param[i]);
			}
		}
		return result.toString();
	}

	/**
	 * Base64加密 byte[]数组
	 * 
	 * @param bytes
	 * @return
	 */
	public static String encrypt(byte[] bytes) {
		return Base64Utils.encrypt(bytes);
	}

	/**
	 * Base64加密 返回 byte[]数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] unEncrypt(String str) {
		return Base64Utils.unEncrypt(str);
	}

	/**
	 * Base64加密 指定编码集
	 * 
	 * @param bytes
	 * @return
	 */
	public static String encode(String str, String encoding) {
		return Base64Utils.encodeByEncoding(str, encoding);
	}

	/**
	 * Base64解密 指定编码集
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String decode(String str, String encoding) {
		return Base64Utils.decodeByEncoding(str, encoding);
	}

	/**
	 * 判断是否超长
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static boolean isOverLen(String str, int len) {
		boolean result = false;
		int tempLen = getWordCountReg(str);
		if (tempLen > len) {
			result = true;
		}
		return result;
	}

	/**
	 * 功能描述：判断是不是合法的手机号码
	 * 
	 * @param handset
	 * @return boolean
	 */
	public static boolean isHandset(String handset) {
		try {
			String regex = "^1[\\d]{10}$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(handset);
			return matcher.matches();
		} catch (RuntimeException e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return false;
	}

	/**
	 * 将一个指定字符串以重复的数相连
	 * 
	 * @param repeatTime
	 * @param metaString
	 * @return
	 */
	public static String getRepeatString(int repeatTime, String metaString) {
		String repeatString = null;
		if ((repeatTime > 0) && (metaString != null)) {
			int intMetatStringLength = metaString.length();
			if (intMetatStringLength == 0) {
				repeatString = "";
			} else {
				StringBuffer tempStringBuffer = new StringBuffer(repeatTime
						* intMetatStringLength);

				for (int i = 0; i < repeatTime; i++) {
					tempStringBuffer.append(metaString);
				}
				repeatString = tempStringBuffer.toString();
			}
		}
		return repeatString;
	}

	/**
	 * 把带有分割符的字符串，通过分割符，把字符串放到数组中 如果str是null或“”，则返回null字符串
	 * 如果splitStr是null或“”,则使用默认分割符号
	 * 
	 * @param str
	 * @param splitStr
	 * @return
	 */
	public static String[] strToArray(String str) {
		String[] result = null;
		String splitStr = null;
		try {
			if (isEmpty(str) == true) {
				return result;
			}

			if (str.indexOf(CommonConst.SYS_SPLIT_COLON) >= 0) {
				splitStr = CommonConst.SYS_SPLIT_COLON;
			} else if (str.indexOf(CommonConst.SYS_SPLIT_SEMICOLON) >= 0) {
				splitStr = CommonConst.SYS_SPLIT_SEMICOLON;
			} else if (str.indexOf(CommonConst.SYS_SPLIT_COMMA) >= 0) {
				splitStr = CommonConst.SYS_SPLIT_COMMA;
			}

			result = strToArray(str, splitStr);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return result;
	}

	/**
	 * 把带有分割符的字符串，通过分割符，把字符串放到数组中 如果str是null或“”，则返回null字符串
	 * 如果splitStr是null或“”,则使用默认分割符号
	 * 
	 * @param str
	 * @param splitStr
	 * @return
	 */
	public static String[] strToArray(String str, String splitStr) {
		String[] result = null;
		List tempList = null;
		try {
			if (isEmpty(str) == true) {
				return result;
			}
			if (splitStr == null) {
				splitStr = CommonConst.SYS_SPLIT;
			}
			StringTokenizer st = new StringTokenizer(str, splitStr);
			tempList = new ArrayList();
			while (st.hasMoreElements() == true) {
				tempList.add(st.nextToken());
			}
			if (tempList.size() > 0) {
				int size = tempList.size();
				result = new String[size];
				for (int i = 0; i < size; i++) {
					result[i] = ((String) tempList.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return result;
	}

	/**
	 * 将字符串分割成List
	 * 
	 * @param str
	 * @return
	 */
	public static List strToList(String str) {
		List result = null;
		String splitStr = null;
		try {
			if (isEmpty(str) == true) {
				return result;
			}

			if (str.indexOf(CommonConst.SYS_SPLIT_COLON) >= 0) {
				splitStr = CommonConst.SYS_SPLIT_COLON;
			} else if (str.indexOf(CommonConst.SYS_SPLIT_SEMICOLON) >= 0) {
				splitStr = CommonConst.SYS_SPLIT_SEMICOLON;
			} else if (str.indexOf(CommonConst.SYS_SPLIT_COMMA) >= 0) {
				splitStr = CommonConst.SYS_SPLIT_COMMA;
			}

			result = strToList(str, splitStr);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return result;
	}

	/**
	 * 将字符串分割成List
	 * 
	 * @param str
	 * @param splitStr
	 * @return
	 */
	public static List strToList(String str, String splitStr) {
		List result = null;
		try {
			if (isEmpty(str) == true) {
				return result;
			}
			if (splitStr == null) {
				splitStr = CommonConst.SYS_SPLIT;
			}
			StringTokenizer st = new StringTokenizer(str, splitStr);
			result = new ArrayList();
			while (st.hasMoreElements() == true)
				result.add(st.nextToken());
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return result;
	}

	/**
	 * id是否在数组中
	 * 
	 * @param param
	 * @param id
	 * @return
	 */
	public static boolean idInArr(String[] param, String id) {
		boolean result = false;
		if ((param != null) && (param.length > 0) && (!isEmpty(id))) {
			int i = 0;
			for (int len = param.length; i < len; i++) {
				if (id.equals(param[i]) == true) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * String配置正则表达式
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean match(String str, String regex) {
		boolean result = false;
		if ((isEmpty(str) == true) || (isEmpty(regex) == true)) {
			return result;
		}
		result = str.matches(regex);
		return result;
	}

	/**
	 * 移除过滤空字符
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		String result = str;
		StringBuffer temp = null;
		if (result == null || result.trim().length() == 0) {
			return result;
		} else {
			temp = new StringBuffer("");
			for (int i = 0; i < result.length(); i++) {
				if (result.charAt(i) == ' ') {
				} else {
					temp.append(result.charAt(i));
				}
			}
			result = temp.toString();
		}
		return result;
	}

	/**
	 * 得到 字符字节个数
	 * 
	 * @param str
	 * @return
	 */
	public static int getWordCountReg(String str) {
		str = str.replaceAll(" [^\\x00-\\xff] ", " ** ");
		int length = str.length();
		return length;
	}

	/**
	 * 统计字符串字数
	 * 
	 * @param str
	 * @return
	 */
	public static int getWordCount(String str) {
		int length = 0;
		if (isEmpty(str) == true) {
			return length;
		}
		int i = 0;
		for (int len = str.length(); i < len; i++) {
			int ascii = Character.codePointAt(str, i);
			if ((ascii >= 0) && (ascii <= 255))
				length++;
			else {
				length += 2;
			}
		}
		return length;
	}

	/**
	 * 转换为特殊格式 \' 单引号(\u0027) \" 双引号(\u0022) \\ 反斜杠(\u005c)
	 */
	public static String javaConvert(String str) {
		if (isEmpty(str)) {
			return str;
		}
		str = str.replaceAll("\\", "\\u005c");
		str = str.replaceAll("\'", "\\u0027");
		str = str.replaceAll("\"", "\\u0022");
		return str;
	}

	/**
	 * 功能说明: 将字符串按照制定分隔符拆分成对应的数组
	 * 
	 * @param str
	 *            传入的字符串
	 * @param format
	 *            通过什么间隔符将传入的字符串间隔成数组 例如,
	 * @return 数组
	 */
	public static String[] parseStrToArr(String str, String format) {
		if (str == null)
			return null;
		String[] arr = str.split(format);
		return arr;
	}

	/**
	 * 功能说明:对字符串进行用0补位，例如ZeroString("45",4) 结果为0045
	 * 
	 * @param str
	 *            要补位的字符串
	 * @param size
	 *            最终生成的字符串位数
	 */
	public static String zeroString(String str, int size) {
		str = (str == null) ? "" : str.trim();
		char[] ch = str.toCharArray();
		int len = ch.length;
		int lens = size - len;
		if (lens <= 0) {
			return str;
		}
		String rStr = "";
		for (int i = 0; i < size; i++) {
			if (lens > i) {
				rStr += "0";
				continue;
			}
			rStr += ch[i - lens];
		}
		return rStr;
	}

	/**
	 * 功能描述：替换字符串
	 * 
	 * @param from
	 *            String 原始字符串
	 * @param to
	 *            String 目标字符串
	 * @param source
	 *            String 母字符串
	 * @return String 替换后的字符串
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer str = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			str.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		str.append(source);
		return str.toString();
	}

	/**
	 * 功能描述：判断是否为整数
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是不是合法字符 char 要判断的字符
	 */
	public static boolean isLetter(String str) {
		if (str == null || str.length() < 0) {
			return false;
		}
		Pattern pattern = Pattern.compile("[\\w\\.-_]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 功能描述：判断输入的字符串是否符合Email样式.
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是Email样式返回true,否则返回false
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.length() < 1 || email.length() > 256) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(email).matches();
	}

	/**
	 * 把数组中的id信息输出,组合成sql语句
	 * 
	 * @param arr
	 * @param entityName
	 * @return ( id=1 or id=2 or id=3 )
	 * @throws Exception
	 */
	public static String idsToSqlStr(String ids, String entityName)
			throws Exception {
		return idsToSqlStr(ids, CommonConst.SYS_SPLIT_COLON, entityName);
	}

	/**
	 * 把数组中的id信息输出,组合成sql语句
	 * 
	 * @param arr
	 * @param entityName
	 * @return ( id=1 or id=2 or id=3 )
	 * @throws Exception
	 */
	public static String idsToSqlStr(String ids, String splitStr,
			String entityName) throws Exception {
		StringBuffer result = new StringBuffer("");
		String[] arr = null;
		try {
			arr = StringUtils.strToArray(ids, splitStr);
			if (arr != null && arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					if (i > 0) {
						result.append(" or ");
					}
					result.append(entityName + "='" + (String) arr[i] + "'");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return result.toString();
	}

	/**
	 * 把数组中的id信息输出,组合成sql语句
	 * 
	 * @param arr
	 * @param propertyName
	 * @return ( id=1 or id=2 or id=3 )
	 * @throws Exception
	 */
	public static String arrToSqlStr(String[] arr, String propertyName)
			throws Exception {
		StringBuffer result = new StringBuffer("");
		try {
			if (arr != null && arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					if (i > 0) {
						result.append(" or ");
					}
					result.append(propertyName + "='" + (String) arr[i] + "'");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return result.toString();
	}

	/**
	 * 把数组中的id信息输出,组合成sql语句
	 * 
	 * @param arr
	 * @param propertyName
	 *            比如id
	 * @return( id=1 or id=2 or id=3 )
	 * @throws Exception
	 */
	public static String arrToSqlStr(String[] arr, String propertyName,
			List param) throws Exception {
		StringBuffer result = new StringBuffer("");
		String temp = null;
		try {
			if (arr != null && arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					if (i > 0) {
						result.append(" or ");
					}
					result.append(propertyName + "=?");

					temp = (String) arr[i];
					param.add(checkNull(temp));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return result.toString();
	}

	/**
	 * 功能描述：判断输入的字符串是否为纯汉字
	 * 
	 * @param str
	 *            传入的字符窜
	 * @return 如果是纯汉字返回true,否则返回false
	 */
	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 功能描述：人民币转成大写
	 * 
	 * @param str
	 *            数字字符串
	 * @return String 人民币转换成大写后的字符串
	 */
	public static String hangeToBig(String str) {
		double value;
		try {
			value = Double.parseDouble(str.trim());
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
			return null;
		}
		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		long midVal = (long) (value * 100); // 转化成整形
		String valStr = String.valueOf(midVal); // 转化成字符串

		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		String rail = valStr.substring(valStr.length() - 2); // 取小数部分

		String prefix = ""; // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角"
					+ digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		char zero = '0'; // 标志'0'表示出现过0
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				zeroSerNum++; // 连续0次数递增
				if (zero == '0') { // 标志
					zero = digit[0];
				} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0; // 连续0次数清零
			if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
				prefix += zero;
				zero = '0';
			}
			prefix += digit[chDig[i] - '0']; // 转化该数字表示
			if (idx > 0)
				prefix += hunit[idx - 1];
			if (idx == 0 && vidx > 0) {
				prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
			}
		}

		if (prefix.length() > 0)
			prefix += '圆'; // 如果整数部分存在,则有圆的字样
		return prefix + suffix; // 返回正确表示
	}

	public static void main(String[] args) throws Exception {
		StringUtils stringUtils = new StringUtils();
		String result = stringUtils.getRepeatString(5, "abc");
		System.out.println(result);
	}

}