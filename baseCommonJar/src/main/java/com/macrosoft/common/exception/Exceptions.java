package com.macrosoft.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类
 * 
 * @author 呆呆
 */
public class Exceptions {
	/**
	 * 将CheckedException转换为UncheckedException
	 * 
	 * @param e
	 * @return
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 * 
	 * @param e
	 * @return
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起 未看懂(wangjun)
	 * 
	 * @param ex
	 * @param causeExceptionClasses
	 * @return
	 */
	public static boolean isCausedBy(Exception ex,
			Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
}
