package com.macrosoft.common.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件工具类
 * 
 * @author 呆呆
 *
 */
public class FileUtils {
	static final int BUFFER = 8192;
	static final int BIGBUFFER = 524288;
	static final String PATHSEPARATOR = File.separator;

	/**
	 * 是否存在
	 * 
	 * @param strPath
	 * @return
	 */
	public static boolean isExists(String strPath) {
		return instanceFile(strPath).exists();
	}

	private static File instanceFile(String strPath) {
		if ((strPath == null) || ("".equals(strPath.trim()))) {
			throw new IllegalArgumentException("实例化文件对象参数异常,strPath=" + strPath);
		}
		return new File(strPath);
	}

	public static String fileToString(String strFilePath, String strCoding) {
		InputStream is = null;
		ByteArrayOutputStream out = null;
		try {
			File file = instanceFile(strFilePath);
			is = new FileInputStream(file);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[524288];
			int n = 0;
			while (-1 != (n = is.read(buffer))) {
				out.write(buffer, 0, n);
			}
			if ((strCoding != null) && (!"".equals(strCoding.trim()))) {
				String str = out.toString(strCoding);
				return str;
			}
			String str = out.toString();
			return str;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(is, out);
		}
		return null;
	}

	public static byte[] fileToByte(String filePath) {
		InputStream is = null;
		ByteArrayOutputStream out = null;
		try {
			is = new FileInputStream(instanceFile(filePath));
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[524288];
			int n = 0;
			while (-1 != (n = is.read(buffer))) {
				out.write(buffer, 0, n);
			}
			byte[] arrayOfByte1 = out.toByteArray();
			return arrayOfByte1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(is, out);
		}
		return null;
	}

	public static InputStream fileToStream(String filePath) {
		try {
			return new FileInputStream(instanceFile(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean appendStringToFile(String strContent,
			String strFilePath, String strCoding) {
		BufferedReader bufferedReader = null;
		FileOutputStream stream = null;
		OutputStreamWriter writer = null;
		try {
			File distFile = instanceFile(strFilePath);
			if (!distFile.exists()) {
				distFile.mkdirs();
			}
			if (strContent == null) {
				strContent = "UTF-8";
			}
			bufferedReader = new BufferedReader(new StringReader(strContent));
			stream = new FileOutputStream(strFilePath, true);
			writer = new OutputStreamWriter(stream);
			char[] buf = new char[8192];
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				writer.write(buf, 0, len);
			}
			writer.flush();
			boolean i = true;
			return i;
		} catch (IOException e) {
			e.printStackTrace();
			boolean buf = false;
			return buf;
		} finally {
			close(bufferedReader, writer);
			close(null, stream);
		}
//		throw localObject;
	}

	public static boolean byteToFile(byte[] b, String strFilePath) {
		return streamToFile(new ByteArrayInputStream(b), strFilePath);
	}

	public static boolean streamToFile(InputStream in, String strFilePath) {
		InputStreamReader streamReader = null;
		OutputStreamWriter bufferedWriter = null;
		try {
			streamReader = new InputStreamReader(in, "UTF-8");
			bufferedWriter = new OutputStreamWriter(new FileOutputStream(
					instanceFile(strFilePath)), "UTF-8");
			char[] buf = new char[8192];
			int len;
			while ((len = streamReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			boolean i = true;
			return i;
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(streamReader, bufferedWriter);
		}
		return false;
	}

	public static void copyFile(File srcFile, File desFile) {
		if (srcFile == null) {
			throw new IllegalArgumentException("参数异常,srcFile=null.");
		}
		if (desFile == null) {
			throw new IllegalArgumentException("参数异常,desFile=null.");
		}
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(srcFile);
			outputStream = new FileOutputStream(desFile);
			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			outputStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(inputStream, outputStream);
		}
	}

	public static boolean copyFile(String srcPath, String desPath) {
		File resFile = instanceFile(srcPath);
		File disFile = instanceFile(desPath);
		copyFile(resFile, disFile);
		return true;
	}

	public static void copyFileToDir(String desPath, String[] filePath) {
		File desFile = instanceFile(desPath);
		createFile(desFile, false);
		for (String path : filePath) {
			File file = new File(path);
			if (file.isDirectory())
				copyFileToDir(
						filePathEndWithSeparator(desPath) + file.getName(),
						dirFilePath(file));
			else
				copyFileToDir(desPath, file, null);
		}
	}

	public static void copyFolder(String srcPath, String desPath) {
		File resFile = instanceFile(srcPath);
		if (resFile.isDirectory())
			copyDir(srcPath, desPath);
		else if (resFile.isFile())
			copyFile(srcPath, desPath);
	}

	public static void copyFolderDir(String srcPath, String desPath) {
		File srcFile = instanceFile(srcPath);
		createFile(desPath, false);
		if (srcFile.isDirectory()) {
			File[] files = srcFile.listFiles();
			for (File subFile : files) {
				String filePath = filePathEndWithSeparator(desPath)
						+ subFile.getName();
				if (subFile.isFile())
					copyFile(subFile, new File(filePath));
				else
					copyFileFromDir(subFile.getPath(), filePath);
			}
		}
	}

	public static void copyFileFromDir(String srcPath, String desPath) {
		File srcFile = instanceFile(srcPath);
		createFile(srcPath, false);
		if (srcFile.isDirectory()) {
			File[] files = srcFile.listFiles();
			for (File subFile : files)
				if (subFile.isFile())
					copyFile(subFile,
							new File(filePathEndWithSeparator(desPath)
									+ subFile.getName()));
		}
	}

	public static void copyDir(String srcPath, String desPath) {
		File desFile = instanceFile(desPath);
		createFile(desFile, false);
		File srcFile = instanceFile(srcPath);
		if ((srcFile.exists()) && (srcFile.isDirectory()))
			copyFileToDir(filePathEndWithSeparator(desFile.getAbsolutePath())
					+ srcFile.getName(), dirFilePath(srcFile));
	}

	public static void copyFileToDir(String targetDir, File srcFile,
			String newName) {
		String newFilePath = "";
		if ((newName != null) && (!"".equals(newName)))
			newFilePath = filePathEndWithSeparator(targetDir) + newName;
		else {
			newFilePath = filePathEndWithSeparator(targetDir)
					+ srcFile.getName();
		}
		copyFile(srcFile, new File(newFilePath));
	}

	public static void createFile(String path, boolean isFile) {
		createFile(instanceFile(path), isFile);
	}

	public static void createFile(File file, boolean isFile) {
		if (file.exists()) {
			return;
		}
		if (isFile)
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			file.mkdirs();
	}

	public static boolean delFile(String strFilePath) {
		File file = instanceFile(strFilePath);
		if (file.exists()) {
			deleteFile(file);
		}
		return true;
	}

	private static boolean deleteFile(File file) {
		if (file.isFile()) {
			return file.delete();
		}
		return false;
	}

	public static boolean delFolder(String strFolderPath) {
		File file = instanceFile(strFolderPath);
		if (file.exists()) {
			delFolder(file);
		}
		return true;
	}

	public static void delFolder(File file) {
		File[] files = file.listFiles();
		for (File file2 : files)
			if (file2.isFile())
				file2.delete();
			else
				delFolder(file2);
	}

	public static boolean delAllFile(String strFolderPath) {
		File file = instanceFile(strFolderPath);
		if ((file.exists()) && (file.isDirectory())) {
			File[] files = file.listFiles();
			for (File f : files) {
				f.delete();
			}
			return true;
		}
		return false;
	}

	public static String[] dirFileName(String folderPath) {
		if (folderPath == null) {
			throw new IllegalArgumentException("参数异常,folderPath=null.");
		}
		File dir = new File(folderPath);
		return dir.list();
	}

	public static String[] dirFilePath(File dir) {
		if (dir == null) {
			throw new IllegalArgumentException("参数异常,dir=null.");
		}
		String absolutPath = filePathEndWithSeparator(dir.getAbsolutePath());
		String[] paths = dir.list();
		String[] files = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			files[i] = (absolutPath + paths[i]);
		}
		return files;
	}

	public static File[] getDirAll(String folderPath) {
		File file = instanceFile(folderPath);
		if ((file.exists()) && (file.isDirectory())) {
			return file.listFiles();
		}
		return null;
	}

	public static File[] getDirFolders(String folderPath) {
		File[] files = getDirAll(folderPath);
		List folders = new ArrayList();
		for (File file : files) {
			if (file.isDirectory()) {
				folders.add(file);
			}
		}
		return (File[]) folders.toArray(new File[folders.size()]);
	}

	public static File[] getDirFiles(String folderPath) {
		File[] files = getDirAll(folderPath);
		List listFiles = new ArrayList();
		for (File file : files) {
			if (file.isFile()) {
				listFiles.add(file);
			}
		}
		return (File[]) listFiles.toArray(new File[listFiles.size()]);
	}

	private static void close(Reader reader, Writer writer) {
		if (reader != null) {
			try {
				reader.close();
				reader = null;
			} catch (IOException e) {
				reader = null;
				e.printStackTrace();
			}
		}
		if (writer != null)
			try {
				writer.close();
				writer = null;
			} catch (IOException e) {
				writer = null;
				e.printStackTrace();
			}
	}

	private static void close(InputStream inputStream, OutputStream outputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
				inputStream = null;
				e.printStackTrace();
			}
		}
		if (outputStream != null)
			try {
				outputStream.close();
				outputStream = null;
			} catch (IOException e) {
				outputStream = null;
				e.printStackTrace();
			}
	}

	private static String filePathEndWithSeparator(String filePath) {
		if ((filePath == null) || ("".equals(filePath.trim()))) {
			throw new IllegalArgumentException("参数异常,zipFile=" + filePath);
		}
		if (filePath.endsWith(PATHSEPARATOR)) {
			return filePath;
		}
		return filePath + PATHSEPARATOR;
	}

	public static String[] separatFileName(String fileName) {
		if ((fileName == null) || (fileName.indexOf(".") == -1)) {
			throw new IllegalArgumentException("参数异常,fileName=" + fileName);
		}
		int index = fileName.lastIndexOf(".");
		String fName = fileName.substring(0, index);
		String fSuffix = fileName.substring(index + 1);
		return new String[] { fName, fSuffix };
	}

	public static boolean createDir(String filePath) {
		return makeFile(filePath);
	}

	public static boolean makeDir(String filePath) {
		return makeFile(filePath);
	}

	public static boolean makeFile(String filePath) {
		boolean result = false;
		try {
			File file = new File(filePath);
			if (file.exists() == true) {
				result = true;
			} else {
				file.mkdirs();
				file = new File(filePath);
				if (file.exists() == true)
					result = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}
		return result;
	}

	public static void downFile(HttpServletRequest request,
			HttpServletResponse response, String oFileName,
			String filePathAndFileName) throws UnsupportedEncodingException,
			IOException {
		String contentType = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		ByteArrayOutputStream byteArrOutputStream = null;
		byte[] buffer = null;
		int read = 0;
		contentType = request.getSession().getServletContext()
				.getMimeType(filePathAndFileName);
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		response.setContentType(contentType);
		try {
			oFileName = new String(oFileName.getBytes("gb2312"), "iso8859-1");
			response.setHeader("Content-disposition", "attachment;filename=\""
					+ oFileName + "\"");

			inputStream = new BufferedInputStream(new FileInputStream(
					filePathAndFileName));
			byteArrOutputStream = new ByteArrayOutputStream();
			outputStream = new BufferedOutputStream(response.getOutputStream());
			buffer = new byte[8192];
			while ((read = inputStream.read(buffer)) != -1) {
				byteArrOutputStream.write(buffer, 0, read);
			}
			outputStream.write(byteArrOutputStream.toByteArray());
			byteArrOutputStream.flush();
			outputStream.flush();
		} catch (UnsupportedEncodingException unsupportex) {
			unsupportex.printStackTrace();
			throw unsupportex;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			contentType = null;
			buffer = null;
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputStream = null;
			outputStream = null;
			byteArrOutputStream = null;
		}
	}

	public static String getFileExtName(String fileName) {
		int i = fileName.indexOf(".");
		int leg = fileName.length();
		return i > 0 ? fileName.substring(i, fileName.length())
				: i + 1 == leg ? "" : "";
	}

	public static boolean hasFileExtName(String fileName) {
		int i = fileName.indexOf(".");
		return i > 0;
	}

	public static boolean writeFile(FileInputStream in,
			String filePathAndFileName) throws UnsupportedEncodingException,
			IOException {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(in);
			outputStream = new FileOutputStream(
					instanceFile(filePathAndFileName));
			byte[] buf = new byte[8192];
			int len;
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
			outputStream.close();
			boolean i = true;
			return i;
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(inputStream, outputStream);
		}
		return false;
	}
}