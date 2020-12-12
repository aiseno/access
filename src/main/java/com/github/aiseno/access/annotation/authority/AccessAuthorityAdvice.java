package com.github.aiseno.access.annotation.authority;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AccessAuthorityAdvice implements MethodInterceptor, Advice {

	private IAccessAuthority accessAuthority;

	public AccessAuthorityAdvice(IAccessAuthority accessAuthority) {
		this.accessAuthority = accessAuthority;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		accessAuthority.handle(invocation.getMethod());
		return invocation.proceed();
	}

}
