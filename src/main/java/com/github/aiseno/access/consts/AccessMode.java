package com.github.aiseno.access.consts;

/**
 * @author aiseno
 */
public enum AccessMode {
	/**
	 * 会话模式 , 依赖 cookie,不支持多端登录
	 */
	SESSION ,
	
	/**
	 * token模式 , 依赖 消息头,支持多端登录
	 */
	TOKEN ,
}
