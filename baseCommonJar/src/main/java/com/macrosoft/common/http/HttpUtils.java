package com.macrosoft.common.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.macrosoft.common.string.StringUtils;

/**
 * Http工具类
 * @author 呆呆
 *
 */
public final class HttpUtils {
	private static HttpUtils instance = new HttpUtils();

	public static HttpUtils getInstance() {
		return instance;
	}

	public HttpClient getHttpClient() {
		DefaultHttpClient result = null;
		HttpParams httpParams = null;
		try {
			httpParams = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
			HttpConnectionParams.setSoTimeout(httpParams, 20000);
			HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

			HttpClientParams.setRedirecting(httpParams, true);

			String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
			HttpProtocolParams.setUserAgent(httpParams, userAgent);

			result = new DefaultHttpClient(httpParams);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			httpParams = null;
		}
		return result;
	}


	/**
	 * 得到请求url:
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestUrl(HttpServletRequest request) {
		StringBuffer result = new StringBuffer("");
		result.append("http://" + request.getServerName());
		result.append(":" + request.getServerPort());
		result.append("/" + request.getContextPath() + "/"
				+ request.getServletPath());
		return result.toString();
	}

	/**
	 * 得到请求数据:
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestData(HttpServletRequest request) {
		StringBuffer result = new StringBuffer("");
		result.append(getRequestUrl(request));
		Enumeration enumType = request.getParameterNames();
		int count = 0;
		while (enumType.hasMoreElements()) {
			String fieldName = (String) enumType.nextElement();
			String fieldValue = request.getParameter(fieldName);
			fieldValue = fieldValue == null ? "" : request
					.getParameter(fieldName);
			if (count > 0) {
				result.append("&" + fieldName + "=" + fieldValue);
			} else {
				result.append("?" + fieldName + "=" + fieldValue);
				count = 1;
			}

		}
		return result.toString();

	}

	/**
	 * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
	 * 
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static StringBuffer getRequestHead(HttpServletRequest request) {
		StringBuffer result = new StringBuffer("");
		String headerName = null;
		if (request != null) {
			Enumeration enumType = request.getHeaderNames();
			if (enumType != null) {
				while (enumType.hasMoreElements() == true) {
					headerName = (String) enumType.nextElement();
					result.append(headerName + ":"
							+ request.getHeader(headerName) + ";");
				}
			}
		}
		return result;
	}



	
	


	/**
	 * 移动运营商 多普达 s900 cellid:61790-8545-460-0 中国电信 运营商 天语手机
	 * 0-539426348-0-2070684148 看是否为有效的移动基站
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean validYidongCellId(String[] arr) {
		boolean result = true;
		if (arr != null && arr.length == 4) {
			if ("0".equals(arr[0]) == true) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * 中国电信 运营商 华为c8500手机 3156-13824-3-460-00 如果是飞行模式 -1--1--1 三星i559基站信息
	 * :0-13824-3 看是否为有效的基站
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean validDianxingCellId(String[] arr, String cell_id) {
		boolean result = true;
		if (arr != null) {
			if ("0".equals(arr[0]) == true
					|| "-1--1--1".equals(cell_id) == true
					|| "[-1,-1]".equals(cell_id) == true || arr.length == 6
					|| arr.length == 3) {
				result = false;
			}
		}
		return result;
	}
	
	
	
	/**
	 * 发送get请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String get(String url) throws Exception {
		Map map = null;
		return post(url, map);
	}

	/**
	 * 发送post请求
	 * @param url
	 * @param rawParams
	 * @return
	 * @throws Exception
	 */
	public String post(String url, Map<String, String> rawParams)
			throws Exception {
		return post(url, rawParams, true);
	}

	public String post(String url, Map<String, String> rawParams,
			boolean acceptReturnData) throws Exception {
		HttpClient httpClient = null;
		HttpPost post = null;
		List params = null;
		HttpResponse response = null;
		String result = null;
		try {
			if (StringUtils.isEmpty(url) == true) {
				String str1 = result;
				return str1;
			}
			httpClient = getHttpClient();
			post = new HttpPost(url);
			Object i$;
			if (post != null) {
				params = new ArrayList();
				if (rawParams != null) {
					for (i$ = rawParams.keySet().iterator(); ((Iterator) i$)
							.hasNext();) {
						String key = (String) ((Iterator) i$).next();
						params.add(new BasicNameValuePair(key,
								(String) rawParams.get(key)));
					}
				}
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}
			if (httpClient != null) {
				response = httpClient.execute(post);
				if (acceptReturnData == true) {
					result = getResponse(response);
				} else {
					i$ = "";
					return (String) i$;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
			post = null;
			params = null;
			response = null;
		}
		return (String) result;
	}

	public String post(String url, StringBuffer jsonStr) throws Exception {
		String json = jsonStr != null ? jsonStr.toString() : null;
		return post(url, json);
	}

	public String post(String url, String jsonStr) throws Exception {
		HttpClient httpClient = getHttpClient();
		HttpPost post = null;
		HttpResponse response = null;
		String result = null;
		StringEntity postEntity = null;
		try {
			if (StringUtils.isEmpty(url) == true) {
				String str1 = result;
				return str1;
			}
			if (jsonStr != null) {
				postEntity = new StringEntity(jsonStr);
				postEntity.setContentEncoding("UTF-8");
				postEntity.setContentType("application/json");
				postEntity.setChunked(true);
			}

			httpClient = getHttpClient();
			post = new HttpPost(url);
			post.setEntity(postEntity);
			if (httpClient != null) {
				response = httpClient.execute(post);
				result = getResponse(response);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
			post = null;
			response = null;
		}
		return result;
	}

	private String getResponse(HttpResponse response) throws Exception {
		HttpEntity entity = null;
		InputStream instream = null;
		BufferedReader in = null;
		StringBuffer result = null;
		try {
			if ((response != null)
					&& (response.getStatusLine().getStatusCode() == 200)) {
				entity = response.getEntity();
				if (entity != null) {
					instream = entity.getContent();
					in = new BufferedReader(new InputStreamReader(instream,
							"UTF-8"));
					result = new StringBuffer();
					String data = null;
					while ((data = in.readLine()) != null)
						result.append(data);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (instream != null) {
				instream.close();
			}
			instream = null;
			if (in != null) {
				in.close();
			}
			in = null;
		}
		if (result != null) {
			return result.toString();
		}
		return null;
	}

	public static void main(String[] args) {
		String cookieValue = "SabSLQKn7IYABG2Y0XcpEeh2y/GV9lOsbsz5qxrruOmMOX99t2NwuWtnAIUK3L1phpW9SN/re/SvEjSdULIBhcFLngY5KHWQBvPRZeafKh1jTobDy0hw4ZJGckrJryBzRyUOrzBSBgdnqOktc0SLyC62rCAxaYLdbzNQAdQnvKSvGM0oKrblpBPmD+tQeMu2zdTpCERySSeWs8YM7VXHFk68/5BPtQiZ";
		cookieValue = "SabSLQKn7IYABG2Y0XcpEeh2y/GV9lOsbsz5qxrruOmMOX99t2NwuWtnAIUK3L1phpW9SN/re/SvEjSdULIBhcFLngY5KHWQBvPRZeafKh1jTobDy0hw4ZJGckrJryBzRyUOrzBSBgdnqOktc0SLyC62rCAxaYLdbzNQAdQnvKSvGM0oKrblpBPmD+tQeMu2zdTpCERySSeWs8YM7VXHFk68/5BPtQiZ";
		Map params = new HashMap();
		String ssoLoginUrl = "http://localhost:8080/coreplat/authLogin.do";
		String jsonStr = null;
		params.put("cookieName", cookieValue);
		try {
			ssoLoginUrl = "http://172.31.10.65:8080/gis_services/gisservice/getlevelregions.do";
			params = new HashMap();
			params.put("baidulevel", "14");
			params.put("isuserdefined", "true");
			jsonStr = getInstance().post(ssoLoginUrl, params);
			System.out.println("jsonStr1:" + jsonStr);
			ssoLoginUrl = "http://172.31.10.65:8080/gis_services/gisservice/getlevelregions.do?baidulevel=14&isuserdefined=true";
			jsonStr = getInstance().get(ssoLoginUrl);
			System.out.println("jsonStr:" + jsonStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}