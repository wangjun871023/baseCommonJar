package com.macrosoft.common.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.macrosoft.common.file.FileUtils;
import com.macrosoft.common.log.LoggerUtils;


/**
 * 文件上传工具
 * 
 * @author 呆呆
 *
 */
public class UploadUtils {
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
	 * 当文件名重名时，生成文件名算法
	 * 
	 * @param path
	 * @param fileName 文件名 
	 * @param type 1=(1)结尾  2=以uuid结尾 
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
	 * 以文件的形式上传文件
	 * 
	 * @param formfile 上传文件
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
			path = FileUtils.filePathEndWithSeparator(path) + filename;
			FileUtils.createFile(path, true);
			FileUtils.writeFile(new FileInputStream(formfile), path);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
		return "";
	}

	/**
	 * 以数据流的形式上传文件
	 * 
	 * @param fileform 上传文件数据流
	 * @param filePath
	 * @return
	 */
	public static String upload(InputStream fileform, String filePath) {
		if ((filePath == null) || ("".equals(filePath.trim()))) {
			throw new IllegalArgumentException("象参数异常,filePath=" + filePath);
		}
		return FileUtils.streamToFile(fileform, filePath) ? filePath : "";
	}

	/**
	 * 下载文件 未完成(wangjun)
	 * @param filepath
	 * @param filename
	 * @param response
	 */
	@Deprecated
	public void downFile(String filepath, String filename,
			HttpServletResponse response) {
	}
}