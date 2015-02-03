zip 解压与压缩工具

/**
 * 压缩文件，将多个文件同时压缩成一个文件
 * @param srcPath 源文件目录
 * @param inputFileName 文件名数组
 * @param desPath 目标文件目录
 * @param desFileName 目标文件名
 * @return 压缩结果，成功为true，失败为false
 */
public static boolean zipFile(String srcPath, String[] inputFileName,String desPath, String desFileName);

/**
 * 将一个目录进行压缩
 * @param sourceDir 源文件目录
 * @param zipFile 压缩后的文件路径
 */
public static void zip(String sourceDir, String zipFile); 

/**
 * 将一个目录进行压缩
 * @param sourceDir
 */
public static void zip(String sourceDir); 

/**
 * 将一个目录进行压缩
 * @param sourceDir
 * @param zipName 压缩文件名
 */
public static void zipWithName(String sourceDir, String zipName) ;

/**
 * 解压文件
 * @param zipfile 解压的文件路径
 * @param destDir 解压目标文件路径
 */
public static void unZip(String zipfile, String destDir) ;

/**
 * 当前文件解压
 * @param zipFile
 */
public static void unZip(String zipFile);

/**
 * 以指定文件名解压
 * @param zipfile
 * @param filename
 */
public static void unZipWithName(String zipfile, String filename);