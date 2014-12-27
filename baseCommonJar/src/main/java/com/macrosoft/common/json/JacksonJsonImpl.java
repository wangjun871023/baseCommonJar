package com.macrosoft.common.json;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macrosoft.common.string.StringUtils;

/**
 * Json工具类实现
 * 
 * @author 呆呆
 *
 */
public class JacksonJsonImpl implements JsonInterface {
	/**
	 * 以集合的形式发送
	 * 
	 * @param paramList
	 * @return
	 */
	public Map<String, Object> sendList(List dataList) {
		return sendList(dataList, true);
	}
	
	/**
	 * dataList为root的
	 * json集合 
	 */
	public Map sendList(List dataList, boolean success) {
		Map map = getInstanceMap();
		if (dataList != null) {
			map.put("dataList", dataList);
		}

		if (success == true) {
			map.put("success", Boolean.valueOf(true));
			map.put("result", Boolean.valueOf(true));
		} else {
			map.put("success", Boolean.valueOf(false));
			map.put("result", Boolean.valueOf(false));
		}
		return map;
	}

	/**
	 * 单例初始化Map
	 * @return
	 */
	private Map<String, Object> getInstanceMap() {
		return new HashMap();
	}
	/**
	 * 发送对象
	 * root为model
	 */
	public Map<String, Object> sendObject(Object form) {
		return sendObject(form, true, "model");
	}
	/**
	 * 发送对象
	 * root默认为model
	 * 加入success
	 */
	public Map<String, Object> sendObject(Object form, boolean success) {
		return sendObject(form, success, "model");
	}
	/**
	 * 发送对象
	 * root可以设置
	 * 默认加入success
	 */
	public Map<String, Object> sendObject(Object form, String rootKey) {
		return sendObject(form, true, rootKey);
	}
	/**
	 * 发送对象
	 * root可以设置
	 * 是否加入success
	 */
	public Map<String, Object> sendObject(Object form, boolean success,
			String rootKey) {
		Map map = getInstanceMap();
		if (form != null) {
			if (StringUtils.isEmpty(rootKey) == true)
				map.put("model", form);
			else {
				map.put(rootKey, form);
			}
			if (success == true) {
				map.put("success", Boolean.valueOf(true));
				map.put("result", Boolean.valueOf(true));
			} else {
				map.put("success", Boolean.valueOf(false));
				map.put("result", Boolean.valueOf(false));
			}
		}
		return map;
	}
	
	/**
	 * 
	 */
	public List parseObject(String jsonStr, Object object) {
		return parseObject(jsonStr, object, null);
	}

	public List<Object> parseObject(String jsonStr, Object object,
			String rootKey) {
		ObjectMapper mapper = new ObjectMapper();
		List result = null;
		JsonNode rootNode = null;
		String root = rootKey;
		Object form = null;
		try {
			if ((StringUtils.isEmpty(jsonStr) == true) || (object == null)) {
				return result;
			}
			rootNode = mapper.readTree(jsonStr);

			if (!StringUtils.isEmpty(root)) {
				JsonNode node = rootNode.path(root);
				int size = 0;
				if (node != null)
					if (node.isArray() == true) {
						size = node.size();
						result = new ArrayList(size);
						for (int i = 0; i < size; i++) {
							JsonNode childNode = node.get(i);
							Iterator itr = childNode.fieldNames();
							form = object.getClass().newInstance();
							while (itr.hasNext() == true) {
								String key = (String) itr.next();
								String value = childNode.get(key).asText();

								BeanUtils.setProperty(form, key, value);
							}
							result.add(form);
						}
					} else {
						Iterator itr = node.fieldNames();
						form = object.getClass().newInstance();
						result = new ArrayList();
						while (itr.hasNext() == true) {
							String key = (String) itr.next();
							String value = node.get(key).asText();

							BeanUtils.setProperty(form, key, value);
						}
						result.add(form);
					}
			} else {
				Iterator itr = rootNode.fieldNames();
				form = object.getClass().newInstance();
				result = new ArrayList();
				while (itr.hasNext() == true) {
					String key = (String) itr.next();
					String value = rootNode.get(key).asText();

					BeanUtils.setProperty(form, key, value);
				}
				result.add(form);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	/**
	 * 对象生成json
	 */
	public String toJson(Object obj) {
		String result = null;
		if (obj == null) {
			return result;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			result = mapper.writeValueAsString(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		JacksonJsonImpl jsonImple = new JacksonJsonImpl();
		try {
			ObjectMapper mapper = new ObjectMapper();
			List dataList = new ArrayList();
			Map map = new HashMap();
			BaseObject object1 = new BaseObject();
			object1.setUserName("张三");
			object1.setWeight(65.5D);
			object1.setHeight(170);
			object1.setSex(true);
			String[] score = { "80", "90", "95" };
			object1.setArray(score);
			BaseObject object2 = new BaseObject();
			object2.setUserName("李四");
			object2.setWeight(75.5D);
			object2.setHeight(171);
			object2.setSex(true);
			score = new String[3];
			score[0] = "65";
			score[1] = "68";
			score[2] = "75";
			object2.setArray(score);
			object1.setInnerObject(object2);
			dataList.add(object1);

			object1 = new BaseObject();
			object1.setUserName("020");
			object1.setUserCode("广州市");
			dataList.add(object1);
			object1 = new BaseObject();
			object1.setUserName("0755");
			object1.setUserCode("深圳市");
			dataList.add(object1);
			object1 = new BaseObject();
			object1.setUserName("021");
			object1.setUserCode("上海市");
			dataList.add(object1);
			map.put("baseObject", dataList);
			String json = mapper.writeValueAsString(object1);
			System.out.println(json);
			BaseObject object = new BaseObject();
			List tempList = jsonImple.parseObject(json, object, "baseObject");
			System.out.println(json);
			map = new HashMap();
			object1 = new BaseObject();
			object1.setUserName("1");
			object1.setUserCode("test1");
			map.put("baseObject", object1);
			json = mapper.writeValueAsString(map);
			tempList = jsonImple.parseObject(json, object, "baseObject");

			map = new HashMap();
			object1 = new BaseObject();

			map.put("baseObject", object1);
			json = mapper.writeValueAsString(object1);
			tempList = jsonImple.parseObject(json, object, null);
			mapper.writeValue(new File("c:\\a.json"), map);
		} catch (Exception ex) {
		}
	}
}