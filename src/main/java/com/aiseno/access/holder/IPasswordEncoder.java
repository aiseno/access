package com.aiseno.access.holder;

import java.security.SecureRandom;
import java.util.Random;

public interface IPasswordEncoder {
	
	//盐随机字符串
	static final String B64T_STRING = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	//默认前缀
	static final String DEFAULT_PREFIX = "$.zfjRBxm.";
	//密码盐长度
	static final Integer SALT_LENGTH = 10;
	//生成密码盐
	static String getRandomSalt(final int number) {
		final Random random = new SecureRandom();
		final StringBuilder saltString = new StringBuilder(number);
		for (int i = 1; i <= number; i++) {
		   saltString.append(B64T_STRING.charAt(random.nextInt(B64T_STRING.length())));
		}
		return saltString.toString();
	}
	
	/**
	 * -加密
	 * @param plaintextPassword 明文密码
	 * @return
	 */
	public String encode(String plaintextPassword);
	
	/**
	 * -匹配校验密码
	 * @param oldPassword 加密密码
	 * @param plaintextPassword 明文密码
	 * @param salt 密码盐
	 * @return
	 */
	public boolean matches(String oldPassword, String plaintextPassword);
	
}
