package com.meijia.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SignUtil {
	public static void main(String[] args) {
		String key = "jhj123";
		HashMap<String, String> maptest = new HashMap<String, String>();
		maptest.put("tel", "010-66574455");
		maptest.put("addrName", "北京市昌平区天通东苑一区-10号楼");
		maptest.put("addrLat", "40.071828");
		maptest.put("addrLng", "116.439321");
		maptest.put("serviceDate", "1489104000");
		maptest.put("serviceHour", "3");
		maptest.put("serviceType", "28");
		maptest.put("staffNum", "1");
		maptest.put("linkOrderNo", "T200xxxx");
		maptest.put("orderFrom", "105");

		Collection<String> keyset = maptest.keySet();
		List<String> list = new ArrayList<String>(keyset);

		// 对key键值按字典升序排序
		Collections.sort(list);

		String params = "";
		for (int i = 0; i < list.size(); i++) {
			System.out.println("key键---值: " + list.get(i) + "," + maptest.get(list.get(i)));
			params += list.get(i) + "=" + maptest.get(list.get(i)) + "&";
		}

		try {

			System.out.println(params);
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacSHA1");

			mac.init(secret);
			byte[] digest = mac.doFinal(params.getBytes("UTF-8"));

			String hmac = Hex.encodeHexString(digest);
			System.out.println(hmac);
		} catch (Exception e) {

		}
	}

}
