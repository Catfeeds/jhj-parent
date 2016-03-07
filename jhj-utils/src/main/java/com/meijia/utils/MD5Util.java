package com.meijia.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class MD5Util {
	/**
	 * 拼接请求参数
	 *
	 * @throws UnsupportedEncodingException
	 * */
	protected String Parameterization(Map<String, String> params, boolean sort,
			boolean urlEncode, String encodeName)
			throws UnsupportedEncodingException {
		List<String> keys = new ArrayList<String>(params.keySet());
		if (sort) {
			Collections.sort(keys);
		}
		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if ((value == null ? "" : value).isEmpty()) {
				continue;
			}
			if (urlEncode) {
				value = URLEncoder.encode(value, encodeName);
			}
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * md5签名
	 * */
	public static String sign(String text, String key, String input_charset)
			throws UnsupportedEncodingException {
		text = text + key;
		byte[] data;
		if (input_charset == null || input_charset == "") {
			data = text.getBytes();
		} else {
			data = text.getBytes(input_charset);
		}
		return DigestUtils.md5Hex(data);
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	// public static String MD5Encode(String origin, String charsetname) {
	// String resultString = null;
	// try {
	// resultString = new String(origin);
	// MessageDigest md = MessageDigest.getInstance("MD5");
	// if (charsetname == null || "".equals(charsetname))
	// resultString = byteArrayToHexString(md.digest(resultString
	// .getBytes()));
	// else
	// resultString = byteArrayToHexString(md.digest(resultString
	// .getBytes(charsetname)));
	// } catch (Exception exception) {
	// }
	// return resultString;
	// }

	public synchronized static final byte[] toMd5(String data,
			String encodingType) {
		MessageDigest digest = null;
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. ");
				nsae.printStackTrace();
			}
		}
		if (StringUtils.isBlank(data)) {
			return null;
		}
		try {
			// 最重要的是这句,需要加上编码类型
			digest.update(data.getBytes(encodingType));
		} catch (UnsupportedEncodingException e) {
			digest.update(data.getBytes());
		}
		return digest.digest();
	}

	public static String MD5Encode(String origin, String encodingType) {
		byte[] md5Bytes = toMd5(origin, encodingType);
		return byteArrayToHexString(md5Bytes);
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

}
