package com.meijia.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MathDoubleUtil {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 2;

	// 这个类不能实例化
	private MathDoubleUtil() {
	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	/**
	 * 		这种适用于 订单数量 等 整型参数
	 * 	
	 * 计算增长率 公式：增长率=(value1-value2)/value2*100
	 * 
	 * @param value1
	 * @param value2
	 * @return 增长率
	 */
	public static String getRiseRate(Integer value1, Integer value2) {
		String rate = "-";
		double result = 0d;
		try {
			if (value1 == null || value2 == null) {
				return rate;
			}
			if (Double.parseDouble(value2.toString()) == 0.0) {
				return rate;
			}
			result = (Double.parseDouble(value1.toString()) - Double
					.parseDouble(value2.toString()))
					/ Double.parseDouble(value2.toString()) ;
//			System.out.println(String.valueOf(result));
			if (Double.isNaN(result) || Double.isInfinite(result)) {
				return null;
			}
			DecimalFormat df1 = new DecimalFormat("##.00%");
			return df1.format(result);    
		} catch (Exception e) {

			return "-";
		}
	}
	
	
	
	
	
	
    public static String getPercent(Integer x,Integer total){  

        double tempresult= Double.parseDouble(String.valueOf(x)) / Double.parseDouble(String.valueOf(total));  
//        System.out.println(String.valueOf(tempresult));
        DecimalFormat df1 = new DecimalFormat("0.00%");
   
        return df1.format(tempresult);    

     }  
	

    
    public static void main(String[] args) {
    	
//    	double d1 = s1.subtract(t1).divide(s1).doubleValue();
    	
//    	System.out.println();
    	
//    	BigDecimal a = new BigDecimal("23455.789");
//    	String str = a.toString();
//    	String inte = str.split("\\.")[0];
//    	System.out.println(inte);
//    	BigInteger b = new BigInteger(inte);
//    	
//    	System.out.println(b);
//    	
    	System.out.println(new BigDecimal("1.61").intValue());
    	System.out.println(new BigDecimal("1.11").intValueExact());
    	
    	System.out.println(MathDoubleUtil.getRiseRate(Integer.valueOf("1.00"), Integer.valueOf("52")));
    	System.out.println(MathDoubleUtil.getPercent(1, 1000));
    }
}
