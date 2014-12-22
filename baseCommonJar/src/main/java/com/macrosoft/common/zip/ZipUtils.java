package com.macrosoft.common.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip 解压与压缩工具
 * 
 * @author 呆呆
 */
public class ZipUtils {
	static final int BUFFER = 1024;// 1K
	static final int BUFFERBIG = 524288;// 512K
	static final String ZIPSUFFIX = ".zip"; // zip后缀

	/**
	 * 使一个文件路径以文件分隔符结尾
	 * 
	 * @param filePath
	 * @return
	 */
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
	 * 递归压缩算法
	 * 
	 * @param sourceDir
	 * @param basePath
	 * @param zos
	 */
	private static void zipFile(String sourceDir, String basePath,
			ZipOutputStream zos) {
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			File file = new File(sourceDir);
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					String pathName = f.getPath();
					zipFile(pathName, basePath, zos);
				}
			} else {
				String pathName = file.getPath().substring(
						basePath.length() + 1);
				is = new FileInputStream(file);
				bis = new BufferedInputStream(is);

				ZipEntry ze = new ZipEntry(pathName);
				ze.setSize(file.length());
				ze.setTime(file.lastModified());
				ze.setCrc(0L);
				CRC32 crc = new CRC32();
				crc.reset();
				zos.putNextEntry(ze);
				byte[] buf = new byte[1024];
				int length = 0;
				while ((length = bis.read(buf, 0, 1024)) != -1) {
					zos.write(buf, 0, length);
					crc.update(buf, 0, length);
				}
				ze.setCrc(crc.getValue());
				zos.flush();
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
					bis = null;
				} catch (IOException e) {
					bis = null;
					e.printStackTrace();
				}
			}
			if (is != null)
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
		}
	}

	/**
	 * 压缩文件，将多个文件同时压缩成一个文件
	 * 
	 * @param srcPath
	 * @param inputFileName
	 * @param desPath
	 * @param desFileName
	 * @return 压缩结果，成功为true，失败为false
	 */
	public static boolean zipFile(String srcPath, String[] inputFileName,
			String desPath, String desFileName) {
		if (inputFileName == null) {
			return false;
		}
		srcPath = filePathEndWithSeparator(srcPath);
		File file = new File(srcPath);
		if ((!file.exists()) || (file.isFile())) {
			return false;
		}
		ZipOutputStream zos = null;
		try {
			desPath = filePathEndWithSeparator(desPath) + desFileName;
			zos = new ZipOutputStream(new FileOutputStream(desPath));
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			int readLen = 0;
			for (int i = 0; i < inputFileName.length; i++) {
				File f = new File(srcPath + inputFileName[i]);
				if (!f.exists()) {
					continue;
				}
				ze = new ZipEntry(f.getName());
				ze.setSize(f.length());
				ze.setTime(f.lastModified());
				ze.setCrc(0L);
				CRC32 crc = new CRC32();
				crc.reset();
				zos.putNextEntry(ze);
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					zos.write(buf, 0, readLen);
					crc.update(buf, 0, readLen);
				}
				ze.setCrc(crc.getValue());
				is.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
					zos = null;
				} catch (IOException e) {
					zos = null;
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 将一个目录进行压缩
	 * 
	 * @param sourceDir
	 * @param zipFile
	 *            压缩后的文件路径
	 */
	public static void zip(String sourceDir, String zipFile) {
		if (sourceDir == null) {
			throw new IllegalArgumentException("参数异常,sourceDir=" + sourceDir);
		}
		if (zipFile == null) {
			throw new IllegalArgumentException("参数异常,zipFile=" + zipFile);
		}
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			File file = new File(sourceDir);
			String basePath = null;
			if (file.isDirectory())
				basePath = file.getPath().substring(0,
						file.getPath().lastIndexOf(File.separator));
			else {
				basePath = file.getParent();
			}
			zipFile(sourceDir, basePath, zos);
			zos.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zos != null)
				try {
					zos.close();
					zos = null;
				} catch (IOException e) {
					zos = null;
					e.printStackTrace();
				}
		}
	}

	/**
	 * 将一个目录进行压缩
	 * 
	 * @param sourceDir
	 */
	public static void zip(String sourceDir) {
		if ((sourceDir == null) || ("".equals(sourceDir.trim()))) {
			throw new IllegalArgumentException("参数异常,sourceDir=" + sourceDir);
		}
		File srcFile = new File(sourceDir);
		String zipFile = null;
		if (srcFile.isDirectory()) {
			zipFile = srcFile.getPath() + ".zip";
			sourceDir = filePathEndWithSeparator(srcFile.getPath());
		} else {
			zipFile = sourceDir.substring(0, sourceDir.lastIndexOf("."))
					+ ".zip";
		}
		zip(sourceDir, zipFile);
	}

	/**
	 * 将一个目录进行压缩
	 * 
	 * @param sourceDir
	 * @param zipName
	 *            压缩文件名
	 */
	public static void zipWithName(String sourceDir, String zipName) {
		if ((sourceDir == null) || ("".equals(sourceDir.trim()))) {
			throw new IllegalArgumentException("参数异常,sourceDir=" + sourceDir);
		}
		if (zipName == null) {
			throw new IllegalArgumentException("参数异常,zipName=" + zipName);
		}
		zipName = zipName + ".zip";
		File srcFile = new File(sourceDir);
		if (srcFile.exists()) {
			String zipFile = filePathEndWithSeparator(srcFile.getParent())
					+ zipName;
			zip(sourceDir, zipFile);
		}
	}

	/**
	 * 解压文件
	 * 
	 * @param zipfile
	 *            解压的文件路径
	 * @param destDir
	 *            解压目标文件路径
	 */
	public static void unZip(String zipfile, String destDir) {
		if ((zipfile == null) || ("".equals(zipfile.trim()))) {
			throw new IllegalArgumentException("参数异常,zipfile=" + zipfile);
		}
		InputStream inputStream = null;
		try {
			destDir = filePathEndWithSeparator(destDir);
			ZipFile zipFile = new ZipFile(new File(zipfile));
			Enumeration enumeration = zipFile.entries();
			ZipEntry zipEntry = null;
			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				File loadFile = new File(destDir + zipEntry.getName());
				if ((!loadFile.exists()) && (zipEntry.isDirectory())) {
					loadFile.mkdirs();
				} else {
					if (!loadFile.getParentFile().exists()) {
						loadFile.getParentFile().mkdirs();
					}
					OutputStream outputStream = new FileOutputStream(loadFile);
					inputStream = zipFile.getInputStream(zipEntry);
					byte[] b = new byte[1024];
					int length;
					while ((length = inputStream.read(b)) != -1) {
						outputStream.write(b, 0, length);
					}
					inputStream.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					inputStream = null;
					e.printStackTrace();
				}
		}
	}

	/**
	 * 当前文件解压
	 * 
	 * @param zipFile
	 */
	public static void unZip(String zipFile) {
		if ((zipFile == null) || ("".equals(zipFile.trim()))) {
			throw new IllegalArgumentException("参数异常,zipFile=" + zipFile);
		}
		BufferedOutputStream dest = null;
		ZipInputStream zis = null;
		try {
			FileInputStream fis = new FileInputStream(zipFile);
			zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry = null;
			String desPath = zipFile.substring(0, zipFile.lastIndexOf("."));
			File desFile = new File(desPath);
			if (!desFile.exists()) {
				desFile.mkdir();
			}
			while ((entry = zis.getNextEntry()) != null) {
				byte[] data = new byte[1024];

				File outFile = new File(filePathEndWithSeparator(desPath)
						+ entry.getName());
				if ((entry.isDirectory()) && (!outFile.exists())) {
					outFile.mkdirs();
				} else {
					if (!outFile.getParentFile().exists()) {
						outFile.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(outFile);
					dest = new BufferedOutputStream(fos, 1024);
					int count;
					while ((count = zis.read(data, 0, 1024)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dest != null) {
				try {
					dest.close();
					dest = null;
				} catch (IOException e) {
					dest = null;
					e.printStackTrace();
				}
			}
			if (zis != null)
				try {
					zis.close();
					zis = null;
				} catch (IOException e) {
					zis = null;
					e.printStackTrace();
				}
		}
	}

	/**
	 * 以指定文件名解压
	 * 
	 * @param zipfile
	 * @param filename
	 */
	public static void unZipWithName(String zipfile, String filename) {
		if ((zipfile == null) || ("".equals(zipfile.trim()))) {
			throw new IllegalArgumentException("参数异常,zipfile=" + zipfile);
		}
		if (filename == null) {
			throw new IllegalArgumentException("参数异常,filename=" + filename);
		}
		File srcFile = new File(zipfile);
		if (srcFile.exists()) {
			String filepath = filePathEndWithSeparator(srcFile.getParent())
					+ filename;
			unZip(zipfile, filepath);
		}
	}
}