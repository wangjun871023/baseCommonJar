package com.macrosoft.common.pathCode;

import com.macrosoft.common.constant.CommonConst;
import com.macrosoft.common.string.StringUtils;

/**
 * 路径Code
 * 
 * @author 呆呆
 */
public class PathCodeUtils{

	private PathCodeUtils() {
	}

	/**
	 * @param fatherPathcode
	 *            父节点的最大值
	 * @param currentMaxValue
	 *            当前编号的最大值
	 * @param fixLength
	 *            int 流水字符串的长度
	 * @param beforeFill
	 *            boolean 标记如果长度不够的话,0填充是前填充还是后填充
	 * @return
	 */
	public static String getLineIdBySequenceName(String fatherPathcode,
			String currentMaxValue, int fixLength, boolean beforeFill) {
		String resultLineIdStr = null;
		long tempLineIdLong = 0;
		try {
			if (StringUtils.isEmpty(fatherPathcode) == true) {
				if (StringUtils.isEmpty(currentMaxValue) == true) {
					tempLineIdLong = 1;
				} else {
					tempLineIdLong = Long.parseLong(currentMaxValue) + 1;
				}
			} else {
				if (StringUtils.isEmpty(currentMaxValue) == true) {
					tempLineIdLong = 1;
				} else {
					int posInt = -1;
					posInt = currentMaxValue.indexOf(fatherPathcode);
					currentMaxValue = currentMaxValue.substring(posInt
							+ fatherPathcode.length() + 1,
							currentMaxValue.length());
					tempLineIdLong = Long.parseLong(currentMaxValue) + 1;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		resultLineIdStr = Long.toString(tempLineIdLong);
		if (fixLength < 0) {
			fixLength = CommonConst.SYS_LINE_LENGTH;
		}
		/**
		 * 如果长度小于设置的长度,用0填充, 如果beforeFill为true的话,就是用0前填充,否则就是用0后填充
		 */
		if (resultLineIdStr.length() < fixLength) {
			for (int i = resultLineIdStr.length(); i < fixLength; i++) {
				if (beforeFill == true) {
					resultLineIdStr = "0" + resultLineIdStr;
				} else {
					resultLineIdStr += "0";
				}
			}
		}
		if (StringUtils.isEmpty(fatherPathcode) == true) {

		} else {
			resultLineIdStr = fatherPathcode + CommonConst.SYS_PATH_CODE_SPLIT
					+ resultLineIdStr;
		}
		return resultLineIdStr;
	}
}