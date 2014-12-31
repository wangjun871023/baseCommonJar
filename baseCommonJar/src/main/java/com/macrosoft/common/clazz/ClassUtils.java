package com.macrosoft.common.clazz;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 类工具
 * @author 呆呆 
 */
public class ClassUtils {
	/**
	 * 获取同一路径下所有子类或接口实现类
	 * @param cls
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getAllAssignedClass(Class<?> cls)
			throws IOException, ClassNotFoundException {
		List classes = new ArrayList();
		for (Class c : getClasses(cls)) {
			if ((cls.isAssignableFrom(c)) && (!cls.equals(c))) {
				classes.add(c);
			}
		}
		return classes;
	}

	public static List<Class<?>> getClasses(Class<?> cls) throws IOException,
			ClassNotFoundException {
		String pk = cls.getPackage().getName();
		String path = pk.replace('.', '/');
		ClassLoader classloader = Thread.currentThread()
				.getContextClassLoader();

		URL url = classloader.getResource(path);
		return getClasses(new File(url.getFile()), pk);
	}

	/**
	 * 包名下的类
	 * @param pakeage
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getClasses(String pakeage) throws IOException,
			ClassNotFoundException {
		String path = pakeage.replace('.', '/');
		ClassLoader classloader = Thread.currentThread()
				.getContextClassLoader();

		URL url = classloader.getResource(path);
		return getClasses(new File(url.getFile()), pakeage);
	}

	/**
	 * 包名下的类 指定路径
	 * @param dir
	 * @param pk
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> getClasses(File dir, String pk)
			throws ClassNotFoundException {
		List classes = new ArrayList();
		if (!dir.exists()) {
			return classes;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				classes.addAll(getClasses(f, pk + "." + f.getName()));
			}
			String name = f.getName();
			if (name.endsWith(".class")) {
				classes.add(Class.forName(pk + "."
						+ name.substring(0, name.length() - 6)));
			}
		}

		return classes;
	}

	/**
	 * 得到类路径
	 * 
	 * @param cls
	 * @return
	 */
	public static String getAppPath(Class cls) {
		if (cls == null) {
			throw new IllegalArgumentException("参数不能为空！");
		}
		ClassLoader loader = cls.getClassLoader();

		String clsName = cls.getName() + ".class";

		Package pack = cls.getPackage();
		String path = "";

		if (pack != null) {
			String packName = pack.getName();

			if ((packName.startsWith("java."))
					|| (packName.startsWith("javax."))) {
				throw new IllegalArgumentException("不要传送系统类！");
			}
			clsName = clsName.substring(packName.length() + 1);

			if (packName.indexOf(".") < 0) {
				path = packName + "/";
			} else {
				int start = 0;
				int end = 0;
				end = packName.indexOf(".");
				while (end != -1) {
					path = path + packName.substring(start, end) + "/";
					start = end + 1;
					end = packName.indexOf(".", start);
				}
				path = path + packName.substring(start) + "/";
			}
		}

		URL url = loader.getResource(path + clsName);

		String realPath = url.getPath();

		int pos = realPath.indexOf("file:");
		if (pos > -1) {
			realPath = realPath.substring(pos + 5);
		}
		pos = realPath.indexOf(path + clsName);
		realPath = realPath.substring(0, pos - 1);

		if (realPath.endsWith("!")) {
			realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		}

		try {
			realPath = URLDecoder.decode(realPath, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return realPath;
	}
}