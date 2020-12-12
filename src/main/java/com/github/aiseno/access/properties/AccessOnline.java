package com.github.aiseno.access.properties;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class AccessOnline implements Serializable {
	
	private static final long serialVersionUID = 199000103752681090L;

	private Date lastUpdateTime;
	
	private Integer onLineNumber;
	
	private Collection<String> sessionIdList;

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Integer getOnLineNumber() {
		return onLineNumber;
	}
	public void setOnLineNumber(Integer onLineNumber) {
		this.onLineNumber = onLineNumber;
	}
	public Collection<String> getSessionIdList() {
		return sessionIdList;
	}
	public void setSessionIdList(Collection<String> sessionIdList) {
		this.sessionIdList = sessionIdList;
	}
}
