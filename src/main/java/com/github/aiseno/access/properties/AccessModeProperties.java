package com.github.aiseno.access.properties;

import java.io.Serializable;

import com.github.aiseno.access.consts.AccessConst;
import com.github.aiseno.access.consts.AccessMode;

public class AccessModeProperties implements Serializable {

	private static final long serialVersionUID = -4009788313444250392L;

	private AccessMode mode = AccessMode.SESSION; //默认session模式
	
	/**
	 * .获取 token的消息头字段 名称, mode = AccessMode.TOKEN 时有效
	 */
	private String tokenName = AccessConst.DEFAULT_TOKEN_NAME_KEY;
	
	/**
	 * 当 multipleLogin == true , 时消息 头 必传,用于终端区分,默认: header("_ACCESS_AGENT_ID_","①") ① == tokenPrefix 中的值
	 */
	private String tokenAgent = AccessConst.DEFAULT_AGENT_KEY;
	
	/**
	 * .token存储前缀, multipleLogin = true , 时有效
	 */
	private String[] tokenPrefix;
	
	/**
	 * .是否允许多点登录 , mode = AccessMode.TOKEN 时有效
	 */
	private boolean multipleLogin;

	
	
	
	public static AccessModeProperties defaultSessionMode() {
		return new AccessModeProperties(AccessMode.SESSION);
	}

	public AccessModeProperties() {
		super();
	}

	public AccessModeProperties(AccessMode mode) {
		super();
		this.mode = mode;
	}
	
	public AccessModeProperties(AccessMode mode, String tokenName, String[] tokenPrefix, boolean multipleLogin) {
		super();
		this.mode = mode;
		this.tokenName = tokenName;
		this.tokenPrefix = tokenPrefix;
		this.multipleLogin = multipleLogin;
	}

	public AccessMode getMode() {
		return mode;
	}

	public void setMode(AccessMode mode) {
		this.mode = mode;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String[] getTokenPrefix() {
		return tokenPrefix;
	}

	public void setTokenPrefix(String[] tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public boolean isMultipleLogin() {
		return multipleLogin;
	}

	public void setMultipleLogin(boolean multipleLogin) {
		this.multipleLogin = multipleLogin;
	}

	public String getTokenAgent() {
		return tokenAgent;
	}

	public void setTokenAgent(String tokenAgent) {
		this.tokenAgent = tokenAgent;
	}
}
