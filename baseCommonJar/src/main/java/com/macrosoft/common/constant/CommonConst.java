package com.macrosoft.common.constant;

/**
 * 系统默认常量
 * 
 * @author 呆呆
 */
public final class CommonConst {
	/**
	 * 字符编码
	 */
	public static final String UTF8 = "UTF-8";
	/**
	 * 字符串分割
	 */
	public static final String SYS_SPLIT = ";";
	/**
	 * 名称分隔符
	 */
	public final static String SYS_NAME_SPLIT="$$";
	//冒号分隔符
	public final static String SYS_SPLIT_COLON=":";
	//分号分隔符
	public final static String SYS_SPLIT_SEMICOLON=";";
	//,号分隔符
	public final static String SYS_SPLIT_COMMA=","; 
	//| 分隔符
	public final static String SYS_SPLIT_LINE="|"; 
	
	
	
	/**
	 * 响应成功
	 */
	public static final String RESPONSE_SUCCESS_KEY = "success";
	/**
	 * 响应失败
	 */
	public static final String RESPONSE_FAILURE_KEY = "failure";
	
	/**
	 * 路径code的分隔符
	 */
	public final static String SYS_PATH_CODE_SPLIT=".";
	/**
	 * 每页数据大小
	 */ 
	public final static int SYS_DEFAULT_PAGESIE=15; 
	
	public static final String RESPONSE_TEXT_KEY = "text";
	public static final String RESPONSE_EXTRA_KEY = "extra";
	public static final String PAGE_ROOT_KEY = "data";
	public static final String PAGIE_TOTAL_KEY = "totalCount";
	/**
	 * BaseForm的Model
	 */
	public static final String ENTITY_ROOT_KEY = "model";
	public static final String FORM_ACTION_KEY = "result";
	public static final String FORM_ID_KEY = "id";
	public static final String LIST_ROOT_KEY = "dataList";
	public static final String TABLE_T_CP_USER = "t_cp_user";
	public static final String TABLE_T_CP_DEPARTMENT = "t_cp_department";
	public static final String TABLE_T_CP_ACL = "t_cp_acl";
	public static final String TABLE_T_CP_ROLE = "t_cp_role";
	public static final String TABLE_T_CP_USER_ROLE = "t_cp_user_role";
	public static final String TABLE_T_CP_MODULE = "t_cp_module";
	public static final String TABLE_T_CP_DIC_CONTENT = "t_cp_dic_content";
	public static final String TABLE_T_CP_DIC_GROUP = "t_cp_dic_GROUP";
	public static final String TABLE_T_BD_SHOP = "t_bd_shop";
	public static final String TABLE_T_BD_GROUP = "T_BD_GROUP";
	public static final String TABLE_T_BD_RETAIL_FORMAT = "T_BD_RETAIL_FORMAT";
	public static final String TABLE_T_BA_TEMPDATA = "t_ba_tempdata";
	
	
	public final static String VERSION_NO="2.0.1";  
	public final static String REPORT_ROOTNAME="report";  
	public final static String SYS_SUCCESS="success";
	public final static String VALID_CODE="verifyCode";
	public final static String SYS_LOGIN="login"; 
	public final static String SYS_ERROR="error";
	public final static String SYS_FAIL="fail";
	public final static String SYS_INPUT="input";  
	/**
	 * 缓冲区大小
	 */
	public static final int BUFFER_SIZE=10 * 1024 ; 
	
	public final static String UTF_8="UTF-8";
    /**
     * 应用名称
     */
	public   static String SYS_APPLICATION_NAME="";
    /**
     * 应用路径
     */
  public   static String SYS_APPLICATION_CONTEXT_PATH="";
	/**
	 * 不限制页数的
	 */
	public final static int SYS_PAGE_NOLIMITED=10000;

 
	/**
	 * 根节点标记
	 */
	public final static String SYS_ROOT="0";
	/**
	 * 根节点中文名
	 */
	public final static String SYS_ROOT_NAME="根节点"; 
	

	//有效数据标记
	public final static String SYS_USE_FLAG="1";
	//选中数据标记
	public final static String SYS_SELECT_FLAG="2";

	/**
	 * 超级管理员帐号
	 */
	public final static String SYS_PARAM_SUPERADMIN="superadmin"; 
	/**
	 * 编号长度
	 */
	public final static int SYS_LINE_LENGTH=4;
	/**
	 * 未使用
     *  标准类型任务 
     *  0:表示月视图
	 */
	public final static String SYS_NO_USE_FLAG="0";

	/**
	 * 
	 */
	public final static String SYS_DEFAULT_PASSWORD="1"; 
	public final static String SYS_LOG_LOGIN="login";
	public final static String SYS_LOG_LOGIN_OUT="login_out";
	public final static String SYS_LOG_OPER="oper"; 
	public final static String SYS_LOGIN_METHOD="pc";
	public final static String SYS_LOGIN_METHOD_MOBILE="mobile";
	
	//邮件发送状态
	public final static long SYS_MAIL_NO_SEND=10;
	public final static long SYS_MAIL_SENDING=20;
	public final static long SYS_MAIL_SENDED=30;
	public final static long SYS_MAIL_SEND_CANCEL=40;


	public final static String SYS_ILLEGAL_LOGIN="illegal_login";
	
	/**
	 * cookie名
	 */
	public final static String SYS_COOKIE_NAME="nav_info_nav_info";
	/**
	 * cookie路径
	 */
	public final static String SYS_COOKIE_PATH="/";//ids_path/
}