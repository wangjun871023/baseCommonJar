 验证码工具类
 
/**
 * 使用系统默认字符源生成验证码
 * @param verifySize 验证码长度
 * @return
 */
public static String generateVerifyCode(int verifySize);

/**
 * 生成随机验证码文件,并返回验证码值
 * @param w
 * @param h
 * @param outputFile
 * @param verifySize
 * @return
 * @throws IOException
 */
public static String outputVerifyImage(int w, int h, File outputFile, int verifySize);
	
/**
 * 输出随机验证码图片流,并返回验证码值
 * @param w
 * @param h
 * @param os
 * @param verifySize
 * @return
 * @throws IOException
 */
public static String outputVerifyImage(int w, int h, OutputStream os,int verifySize);
