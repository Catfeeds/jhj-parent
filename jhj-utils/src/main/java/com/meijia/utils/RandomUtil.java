package com.meijia.utils;

import org.apache.commons.lang.RandomStringUtils;

public class RandomUtil {

	private static String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * @return 返回4位随机数
     */
    public static String randomNumber()
    {
        return RandomStringUtils.randomNumeric(6);
    }
    /**
     * @param parm
     * @return 返回指定位数随机数
     */
    public static String randomNumber(int parm)
    {
        return RandomStringUtils.randomNumeric(parm);
    }

    /**
     * @param parm
     * @return 返回指定位数随机串
     */
    public static String randomCode(int parm)
    {
        return RandomStringUtils.random(parm, chars);
    }
    
    public static void main(String[] args) {
		System.out.println(randomCode(4));
	}
    
}