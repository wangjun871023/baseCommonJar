package com.macrosoft.common.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.macrosoft.common.file.FileUtils;


/**
 * 文件上传
 * 
 * @author 呆呆
 *
 */
public class UploadUtils {
	private static String filePathEndWithSeparator(String filePath) {
		if ((filePath == null) || ("".equals(filePath.trim()))) {
			throw new IllegalArgumentException("参数异常,zipFile=" + filePath);
		}
		if (filePath.endsWith(File.separator)) {
			return filePath;
		}
		return filePath + File.separator;
	}

	/**
	 * 判断文件是否重名
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static boolean isRepeatedName(String path, String fileName) {
		if ((path == null) || ("".equals(path.trim()))) {
			throw new IllegalArgumentException("参数异常,path=" + path);
		}
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile()) {
				return !file.getName().equals(fileName);
			}
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i].equals(fileName)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 设置文件名
	 * 
	 * @param path
	 * @param fileName
	 * @param type
	 * @return
	 */
	public static String setFileName(String path, String fileName, int type) {
		if ((path == null) || ("".equals(path.trim()))) {
			throw new IllegalArgumentException("参数异常,path=" + path);
		}
		if (fileName == null) {
			throw new IllegalArgumentException("参数异常,fileName=" + fileName);
		}
		if ((type != 1) && (type != 2)) {
			throw new UnsupportedOperationException("参数异常,type=" + type);
		}
		File file = new File(path);
		if ((file.exists()) && (file.isDirectory())) {
			String[] files = file.list();
			Pattern pattern = Pattern.compile("\\(\\d+\\).");
			Matcher matcher = pattern.matcher(fileName);
			int index = 0;
			if (matcher.find()) {
				String groupStr = matcher.group();
				index = Integer.parseInt(groupStr.substring(1,
						groupStr.length() - 2));
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].equals(fileName)) {
					String ext = fileName.substring(fileName.lastIndexOf("."));
					if (type == 1) {
						if (index != 0) {
							index++;
							fileName = fileName.replace(
									"(" + index + ")" + ext, "(" + index + ")"
											+ ext);
						} else {
							index++;
							fileName = fileName.replace(ext, "(" + index + ")"
									+ ext);
						}
						fileName = setFileName(path, fileName, type);
					} else {
						fileName = fileName.replace(ext, UUID.randomUUID()
								+ ext);
						break;
					}
				}
			}
		}
		return fileName;
	}

	/**
	 * 判断文件是否为指定类型
	 * 
	 * @param fileName
	 * @param types
	 * @return
	 */
	public static boolean isType(String fileName, String[] types) {
		if (fileName == null) {
			throw new IllegalArgumentException("参数异常,fileName=" + fileName);
		}
		if (types == null) {
			return false;
		}
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		for (int i = 0; i < types.length; i++) {
			if (ext.equals(types[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 上传文件
	 * 
	 * @param formfile
	 * @param path
	 * @param filename
	 * @return
	 */
	public static String upload(File formfile, String path, String filename) {
		try {
			if ((path == null) || ("".equals(path.trim()))) {
				throw new IllegalArgumentException("象参数异常,path=" + path);
			}
			filename = filename.trim()
					+ FileUtils.getFileExtName(formfile.getName());
			path = filePathEndWithSeparator(path) + filename;
			FileUtils.createFile(path, true);
			FileUtils.writeFile(new FileInputStream(formfile), path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 上传文件
	 * 
	 * @param fileform
	 * @param filePath
	 * @return
	 */
	public static String upload(InputStream fileform, String filePath) {
		if ((filePath == null) || ("".equals(filePath.trim()))) {
			throw new IllegalArgumentException("象参数异常,filePath=" + filePath);
		}
		return FileUtils.streamToFile(fileform, filePath) ? filePath : "";
	}

	@Deprecated
	public void downFile(String filepath, String filename,
			HttpServletResponse response) {
	}
}