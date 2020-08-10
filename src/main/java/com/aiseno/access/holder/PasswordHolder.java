package com.aiseno.access.holder;

import org.apache.commons.codec.digest.Md5Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PasswordHolder implements IPasswordEncoder {

	private static Logger logger = LoggerFactory.getLogger(PasswordHolder.class);

	private PasswordBuilder config;

	private PasswordHolder(PasswordBuilder config) {
		this.config = config;
	}

	public static PasswordBuilder create() {
		return new PasswordBuilder();
	}

	public static PasswordBuilder create(String salt) {
		return new PasswordBuilder(salt);
	}

	public static PasswordBuilder create(PasswordBuilder config) {
		return config == null ? create() : config;
	}

	@Override
	public String encode(String plaintextPassword) {
		String salt = config.getSalt(), prefix = config.getPrefix();
		String password = Md5Crypt.md5Crypt(plaintextPassword.getBytes(), (prefix + salt), prefix);
		logger.info("密码盐:{} , 前缀: {} , 密码: {} ", salt, prefix, password);
		return password;
	}

	@Override
	public boolean matches(String oldPassword, String plaintextPassword) {
		String password = this.encode(plaintextPassword);
		return oldPassword.equals(password);
	}

	/**
	 * -配置类
	 * 
	 * @author VULCAN
	 */
	protected static class PasswordBuilder {

		private Integer saltLength = SALT_LENGTH;

		private String prefix = DEFAULT_PREFIX;

		private String salt = IPasswordEncoder.getRandomSalt(SALT_LENGTH);

		public PasswordBuilder() {

		}

		public PasswordBuilder(String salt) {
			this.salt = salt;
		}

		public PasswordBuilder(String prefix, String salt) {
			this.prefix = prefix;
			this.salt = salt;
		}

		public PasswordBuilder(Integer saltLength, String prefix, String salt) {
			this.saltLength = saltLength;
			this.prefix = prefix;
			this.salt = salt;
		}

		public Integer getSaltLength() {
			return saltLength;
		}

		public void setSaltLength(Integer saltLength) {
			this.saltLength = saltLength;
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getSalt() {
			return salt;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}

		public PasswordHolder builder() {
			return new PasswordHolder(this);
		}
	}
}
