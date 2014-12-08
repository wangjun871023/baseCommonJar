package com.macrosoft.common.date;

import java.util.Date;
//import com.navinfo.core.utils.CurrentDateProvider;

/**
 * 日期提供者，使用它而不是直接取得系统时间，将方便测试。
 * 
 * @author xiao
 *
 */
public interface DateProvider {

	Date getDate();

	public static final DateProvider DEFAULT = new CurrentDateProvider();

	public static class CurrentDateProvider implements DateProvider {

		//xiaolin@Override
		public Date getDate() {
			return new Date();
		}
	}

	public static class ConfigurableDateProvider implements DateProvider {

		private final Date date;

		public ConfigurableDateProvider(Date date) {
			this.date = date;
		}

		//xiaolin@Override
		public Date getDate() {
			return date;
		}
	}

}
