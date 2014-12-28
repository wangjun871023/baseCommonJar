package test;

import java.io.IOException;
import java.util.List;

import com.macrosoft.common.base64.Base64Utils;
import com.macrosoft.common.clazz.ClassUtils;
import com.macrosoft.common.file.FileUtils;



public class Test {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		List<Class<?>> classes = ClassUtils.getClasses(Base64Utils.class);
		System.out.println(classes.size());
	}
}
