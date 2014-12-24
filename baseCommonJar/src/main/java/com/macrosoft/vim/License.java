package com.macrosoft.vim;

import com.mbartl.viimplementation.license.CheckLicenseFile;

public class License {  
   
    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
   
        CheckLicenseFile licenseFile = new CheckLicenseFile();  
        String valueString = licenseFile.encrypt("test", "test");  
        System.out.println(valueString);  
    }  
   
}  