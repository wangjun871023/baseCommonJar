package com.macrosoft.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.macrosoft.common.log.LoggerUtils;

/**
 * 线程相关工具类
 * 
 * @author 呆呆
 */
public class Threads {

	/**
	 * sleep等待,单位为毫秒,忽略InterruptedException.
	 * 
	 * @param millis
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
			// Ignore.
		}
	}

	/**
	 * sleep等待,忽略InterruptedException.
	 * 
	 * @param duration
	 * @param unit
	 */
	public static void sleep(long duration, TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(duration));
		} catch (InterruptedException e) {
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
			// Ignore.
		}
	}

	/**
	 * 未完成(wangjun) 
	 * 按照ExecutorService JavaDoc示例代码编写的Graceful Shutdown方法.
	 * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务. 如果超时, 则调用shutdownNow,
	 * 取消在workQueue中Pending的任务,并中断所有阻塞函数. 如果仍人超時，則強制退出.
	 * 另对在shutdown时线程本身被调用中断做了处理.
	 */
	public static void gracefulShutdown(ExecutorService pool,
			int shutdownTimeout, int shutdownNowTimeout, TimeUnit timeUnit) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(shutdownTimeout, timeUnit)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(shutdownNowTimeout, timeUnit)) {
					System.err.println("Pool did not terminated");
				}
			}
		} catch (InterruptedException e) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			LoggerUtils.logger.error(e, e);
			// Preserve interrupt status
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 未完成(wangjun) 
	 * 直接调用shutdownNow的方法,
	 * 有timeout控制.取消在workQueue中Pending的任务,并中断所有阻塞函数.
	 */
	public static void normalShutdown(ExecutorService pool, int timeout,
			TimeUnit timeUnit) {
		try {
			pool.shutdownNow();
			if (!pool.awaitTermination(timeout, timeUnit)) {
				System.err.println("Pool did not terminated");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
			LoggerUtils.logger.error(e, e);
		}
	}
}
