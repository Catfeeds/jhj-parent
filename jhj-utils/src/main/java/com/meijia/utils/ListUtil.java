package com.meijia.utils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ListUtil {

	/**
	 * 对list的元素按照多个属性名称排序,
	 * list元素的属性可以是数字（byte、short、int、long、float、double等，支持正数、负数、0）、char、String、
	 * java.util.Date
	 * 
	 * 
	 * @param lsit
	 * @param sortname
	 *            list元素的属性名称
	 * @param isAsc
	 *            true升序，false降序
	 */
	public static <E> void sort(List<E> list, final boolean isAsc, final String... sortnameArr) {
		Collections.sort(list, new Comparator<E>() {

			public int compare(E a, E b) {
				int ret = 0;
				try {
					for (int i = 0; i < sortnameArr.length; i++) {
						ret = ListUtil.compareObject(sortnameArr[i], isAsc, a, b);
						if (0 != ret) {
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ret;
			}
		});
	}

	/**
	 * 给list的每个属性都指定是升序还是降序
	 * 
	 * @param list
	 * @param sortnameArr
	 *            参数数组
	 * @param typeArr
	 *            每个属性对应的升降序数组， true升序，false降序
	 */

	public static <E> void sort(List<E> list, final String[] sortnameArr, final boolean[] typeArr) {
		if (sortnameArr.length != typeArr.length) {
			throw new RuntimeException("属性数组元素个数和升降序数组元素个数不相等");
		}
		Collections.sort(list, new Comparator<E>() {
			public int compare(E a, E b) {
				int ret = 0;
				try {
					for (int i = 0; i < sortnameArr.length; i++) {
						ret = ListUtil.compareObject(sortnameArr[i], typeArr[i], a, b);
						if (0 != ret) {
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ret;
			}
		});
	}

	/**
	 * 对2个对象按照指定属性名称进行排序
	 * 
	 * @param sortname
	 *            属性名称
	 * @param isAsc
	 *            true升序，false降序
	 * @param a
	 * @param b
	 * @return
	 * @throws Exception
	 */
	private static <E> int compareObject(final String sortname, final boolean isAsc, E a, E b) throws Exception {
		int ret;
		Object value1 = ListUtil.forceGetFieldValue(a, sortname);
		Object value2 = ListUtil.forceGetFieldValue(b, sortname);
		String str1 = value1.toString();
		String str2 = value2.toString();
		if (value1 instanceof Number && value2 instanceof Number) {
			int maxlen = Math.max(str1.length(), str2.length());
			str1 = ListUtil.addZero2Str((Number) value1, maxlen);
			str2 = ListUtil.addZero2Str((Number) value2, maxlen);
		} else if (value1 instanceof Date && value2 instanceof Date) {
			long time1 = ((Date) value1).getTime();
			long time2 = ((Date) value2).getTime();
			int maxlen = Long.toString(Math.max(time1, time2)).length();
			str1 = ListUtil.addZero2Str(time1, maxlen);
			str2 = ListUtil.addZero2Str(time2, maxlen);
		}
		if (isAsc) {
			ret = str1.compareTo(str2);
		} else {
			ret = str2.compareTo(str1);
		}
		return ret;
	}

	/**
	 * 给数字对象按照指定长度在左侧补0.
	 * 
	 * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
	 * 
	 * @param numObj
	 *            数字对象
	 * @param length
	 *            指定的长度
	 * @return
	 */
	public static String addZero2Str(Number numObj, int length) {
		NumberFormat nf = NumberFormat.getInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(length);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(length);
		return nf.format(numObj);
	}

	/**
	 * 获取指定对象的指定属性值（去除private,protected的限制）
	 * 
	 * @param obj
	 *            属性名称所在的对象
	 * @param fieldName
	 *            属性名称
	 * @return
	 * @throws Exception
	 */
	public static Object forceGetFieldValue(Object obj, String fieldName) throws Exception {
		Field field = obj.getClass().getDeclaredField(fieldName);
		Object object = null;
		boolean accessible = field.isAccessible();
		if (!accessible) {
			// 如果是private,protected修饰的属性，需要修改为可以访问的
			field.setAccessible(true);
			object = field.get(obj);
			// 还原private,protected属性的访问性质
			field.setAccessible(accessible);
			return object;
		}
		object = field.get(obj);
		return object;
	}

	/**
	 * Create a new list which contains the specified number of elements from
	 * the source list, in a
	 * random order but without repetitions.
	 *
	 * @param sourceList
	 *            the list from which to extract the elements.
	 * @param itemsToSelect
	 *            the number of items to select
	 * @param random
	 *            the random number generator to use
	 * @return a new list containg the randomly selected elements
	 */
	public static <T> List<T> chooseRandomly(List<T> sourceList, int n) {
		Random random = new Random();
		int sourceSize = sourceList.size();

		if (sourceSize < n)
			n = sourceSize;
		// Generate an array representing the element to select from 0... number
		// of available
		// elements after previous elements have been selected.
		int[] selections = new int[n];

		// Simultaneously use the select indices table to generate the new
		// result array
		ArrayList<T> resultArray = new ArrayList<T>();

		for (int count = 0; count < n; count++) {

			// An element from the elements *not yet chosen* is selected
			int selection = random.nextInt(sourceSize - count);
			selections[count] = selection;
			// Store original selection in the original range 0.. number of
			// available elements

			// This selection is converted into actual array space by iterating
			// through the elements
			// already chosen.
			for (int scanIdx = count - 1; scanIdx >= 0; scanIdx--) {
				if (selection >= selections[scanIdx]) {
					selection++;
				}
			}
			// When the first selected element record is reached all selections
			// are in the range
			// 0.. number of available elements, and free of collisions with
			// previous entries.

			// Write the actual array entry to the results
			resultArray.add(sourceList.get(selection));
		}
		return resultArray;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < 1; i++) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("aaa", String.valueOf(i));
			item.put("aaa", String.valueOf(i));
			list.add(item);
		}

		List<Map<String, String>> randList = ListUtil.chooseRandomly(list, 2);
		for (Map item : randList) {
			System.out.println(item.get("aaa"));
		}
	}

}
