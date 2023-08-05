package com.github.desperado2.data.open.api.common.manage;


import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

class CommonManageApplicationTests {

	private static final char[] DIGITS_DATA = {'0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	/**
	 * 签名
	 * @param data 待签名数据
	 * @param secret appSecret
	 * @return 签名结果
	 */
	public static String sign(Map<String, Object> data, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, Object> result = new LinkedHashMap<>();
		// 按照key的字典序升序排列
		data.entrySet().stream().filter(it -> it.getValue() != null)
				.sorted(Map.Entry.comparingByKey())
				.forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		// 拼接字符串
		String param = StringUtils.join(Arrays.asList(result.entrySet().stream()
				.map(it -> it.getKey() + "=" + String.valueOf(it.getValue())).toArray()),"&");
		// MD5加密
		MessageDigest msgDigest = MessageDigest.getInstance("MD5");
		msgDigest.update((param + secret).getBytes(StandardCharsets.UTF_8));
		// 转为16进制字符串
		byte[] digest = msgDigest.digest();
		int l = digest.length;
		char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_DATA[(0xF0 & digest[i]) >>> 4];
			out[j++] = DIGITS_DATA[0x0F & digest[i]];
		}
		// 小写
		String signResult = new String(out).toLowerCase();
		return signResult;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

			System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date(1687795200000L)));

	}
}
