package com.macrosoft.common.clazz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

public class ObjectCommonUtil {
	/**
	 * 复制对象
	 * @param source
	 * @param target
	 */
	public static void copyObjectToAnother(Object source, Object target) {
		Class resultCls = target.getClass();
		Class sourceCls = source.getClass();
		Method[] resultMethods = resultCls.getMethods();
		String setter = null;
		String getter = null;
		Class paramType = null;
		Method sourceMethod = null;
		for (int i = 0; i < resultMethods.length; i++) {
			setter = resultMethods[i].getName();
			if (setter.startsWith("set")) {
				setter = setter.substring(3);

				paramType = resultMethods[i].getParameterTypes()[0];
				if (paramType == Boolean.TYPE)
					getter = "is" + setter;
				else {
					getter = "get" + setter;
				}
				try {
					sourceMethod = sourceCls.getMethod(getter, new Class[0]);
					if ((sourceMethod != null)
							&& (sourceMethod.getReturnType() == paramType)) {
						Object value = sourceMethod.invoke(source,
								new Object[0]);

						resultMethods[i].invoke(target, new Object[] { value });
					}
				} catch (NoSuchMethodException e) {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 打印对象
	 * @param object
	 */
	public static void printObject(Object object) {
		Class objCls = object.getClass();
		Method[] objMethods = objCls.getDeclaredMethods();
		String getter = null;
		Class paramType = null;
		System.out
				.println("******************** PRINT OBJECT START *************************");
		for (int i = 0; i < objMethods.length; i++) {
			getter = objMethods[i].getName();
			paramType = objMethods[i].getReturnType();
			if ((paramType == Class.class)
					|| ((!getter.startsWith("get")) && (!getter
							.startsWith("is"))))
				continue;
			try {
				System.out.println(getter + "=="
						+ objMethods[i].invoke(object, new Object[0]));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		System.out
				.println("******************** PRINT OBJECT END *************************");
	}

	/**
	 * 打印对象集合中的对象
	 * @param obj
	 */
	public static void printObjInCollection(Collection obj) {
		for (Iterator it = obj.iterator(); it.hasNext();)
			printObject(it.next());
	}
}