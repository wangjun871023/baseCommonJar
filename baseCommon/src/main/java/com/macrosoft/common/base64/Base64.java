package com.macrosoft.common.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Base64 加密/解密
 * 
 * @author 呆呆
 */
public class Base64 {
	
	/**
	 * 加密
	 * @param bytes
	 * @return
	 * @throws RuntimeException
	 */
	public static String encrypt(byte[] bytes) throws RuntimeException {
		String result = null;
		if (bytes == null) {
			return result;
		}
		byte[] encoded = encode(bytes); 
		try {
			result = new String(encoded, "ASCII");
		} catch (UnsupportedEncodingException e) {
			result = null;
			throw new RuntimeException("ASCII is not supported!", e);
		}
		return result;
	}

	/**
	 * 解密
	 * @param str
	 * @return
	 * @throws RuntimeException
	 */
	public static byte[] unEncrypt(String str) throws RuntimeException {
		byte[] result = null;
		try {
			if (str == null) {
				return result;
			}
			result = str.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("ASCII is not supported!", e);
		}
		return decode(result);
	}
	
	/**
	 * 对字符串加密,默认以ASCII对加密后的字符串编码
	 * 
	 * @param str
	 * @return
	 * @throws RuntimeException
	 */
	public static String encode(String str) throws RuntimeException {
		byte[] bytes = str.getBytes();
		byte[] encoded = encode(bytes);
		try {
			return new String(encoded, "ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("ASCII is not supported!", e);
		}
	}

	/**
	 * 对字符串解密，默认以ASCII对加密字符串解码
	 * 
	 * @param str
	 * @return
	 * @throws RuntimeException
	 */
	public static String decode(String str) throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("ASCII is not supported!", e);
		}
		byte[] decoded = decode(bytes);
		return new String(decoded);
	}

	/**
	 * 对字符串加密，指定加密之后的字符串编码
	 * 
	 * @param str
	 * @param encoding
	 *            加密之后的编码
	 * @return
	 * @throws RuntimeException
	 */
	public static String encodeByEncoding(String str, String encoding)
			throws RuntimeException {
		byte[] bytes = str.getBytes();
		byte[] encoded = encode(bytes);
		try {
			return new String(encoded, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(encoding + " is not supported!", e);
		}
	}

	/**
	 * 对字符串解密，指定加密时的字符串编码
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 * @throws RuntimeException
	 */
	public static String decodeByEncoding(String str, String encoding)
			throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(encoding + " is not supported!", e);
		}
		byte[] decoded = decode(bytes);
		return new String(decoded);
	}

	/**
	 * 对字符串加密，指定源字符串的字符编码
	 * 
	 * @param str
	 * @param charset
	 *            源字符串的字符编码
	 * @return
	 * @throws RuntimeException
	 */
	public static String encode(String str, String charset)
			throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
		byte[] encoded = encode(bytes);
		try {
			return new String(encoded, "ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("ASCII is not supported!", e);
		}
	}

	/**
	 * 对字符串加密，指定密码之后字符串的字符编码
	 * 
	 * @param str
	 * @param charset
	 * @return
	 * @throws RuntimeException
	 */
	public static String decode(String str, String charset)
			throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("ASCII is not supported!", e);
		}
		byte[] decoded = decode(bytes);
		try {
			return new String(decoded, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
	}

	/**
	 * 对字符串加密，指定源字符串的字符编码与加密之后字符串的编码
	 * 
	 * @param str
	 * @param charset
	 *            源字符串的编码
	 * @param encoding
	 *            加密之后的编码
	 * @return
	 * @throws RuntimeException
	 */
	public static String encode(String str, String charset, String encoding)
			throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
		byte[] encoded = encode(bytes);
		try {
			return new String(encoded, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(encoding + " is not supported!", e);
		}
	}

	/**
	 * 对字符串解密，指定源字符串的字符编码与解密之后字符串的编码
	 * 
	 * @param str
	 * @param charset
	 *            源字符串的编码
	 * @param encoding
	 *            解密之后的编码
	 * @return
	 * @throws RuntimeException
	 */
	public static String decode(String str, String charset, String encoding)
			throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(encoding + " is not supported!", e);
		}
		byte[] decoded = decode(bytes);
		try {
			return new String(decoded, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
	}

	public static byte[] encode(byte[] bytes) throws RuntimeException {
		return encode(bytes, 0);
	}

	public static byte[] encode(byte[] bytes, int wrapAt)
			throws RuntimeException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			encode(inputStream, outputStream, wrapAt);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O error", e);
		} finally {
			try {
				inputStream.close();
			} catch (Throwable t) {
			}
			inputStream = null;
			try {
				outputStream.close();
			} catch (Throwable t) {
			}
		}
		return outputStream.toByteArray();
	}

	public static byte[] decode(byte[] bytes) throws RuntimeException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			decode(inputStream, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O error", e);
		} finally {
			try {
				inputStream.close();
			} catch (Throwable t) {
			}
			inputStream = null;
			try {
				outputStream.close();
			} catch (Throwable t) {
			}
		}
		return outputStream.toByteArray();
	}

	
	/**
	 * 对输入流进行base64加密
	 * @param inputStream
	 * @param outputStream
	 * @throws IOException
	 */
	public static void encode(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		encode(inputStream, outputStream, 0);
	}

	public static void encode(InputStream inputStream,
			OutputStream outputStream, int wrapAt) throws IOException {
		Base64OutputStream aux = new Base64OutputStream(outputStream, wrapAt);
		copy(inputStream, aux);
		aux.commit();
	}

	public static void decode(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		copy(new Base64InputStream(inputStream), outputStream);
	}

	public static void encode(File source, File target, int wrapAt)
			throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			encode(inputStream, outputStream, wrapAt);
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (Throwable t) {
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Throwable t) {
				}
		}
	}

	public static void encode(File source, File target) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			encode(inputStream, outputStream);
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (Throwable t) {
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Throwable t) {
				}
		}
	}

	public static void decode(File source, File target) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			decode(inputStream, outputStream);
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (Throwable t) {
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Throwable t) {
				}
		}
	}

	private static void copy(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		byte[] b = new byte[1024];
		int len;
		while ((len = inputStream.read(b)) != -1)
			outputStream.write(b, 0, len);
	}
}