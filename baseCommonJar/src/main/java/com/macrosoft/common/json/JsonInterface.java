package com.macrosoft.common.json;

import java.util.List;
import java.util.Map;

/**
 * Json工具类接口
 * 
 * @author 呆呆
 *
 */
public abstract interface JsonInterface {
	/**
	 * 以集合的形式发送
	 * @param paramList
	 * @return
	 */
	public abstract Map<String, Object> sendList(List paramList);

	public abstract Map<String, Object> sendList(List paramList,
			boolean paramBoolean);

	public abstract Map<String, Object> sendObject(Object paramObject);

	public abstract Map<String, Object> sendObject(Object paramObject,
			boolean paramBoolean);

	public abstract Map<String, Object> sendObject(Object paramObject,
			String paramString);

	public abstract Map<String, Object> sendObject(Object paramObject,
			boolean paramBoolean, String paramString);

	public abstract List parseObject(String paramString, Object paramObject);

	public abstract List<Object> parseObject(String paramString1,
			Object paramObject, String paramString2);

	public abstract String toJson(Object paramObject);
}