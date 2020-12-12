package com.github.aiseno.access.annotation.authority;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class AccessDefaultPointcutAdvisor extends DefaultPointcutAdvisor {

	private static final long serialVersionUID = 6964380896296712651L;

	private static final Integer ORDER = Integer.MIN_VALUE + 1000;
	
	private static final String EXPRESSION = "@annotation(com.github.aiseno.access.annotation.AccessRoles) || @annotation(com.github.aiseno.access.annotation.AccessPermissions)";
	
	private static final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	
	private static final AccessAuthorityHandle accessAuthority = new AccessAuthorityHandle();
	
	static {
		pointcut.setExpression(EXPRESSION);
	}
	
	public AccessDefaultPointcutAdvisor() {
		super.setPointcut(pointcut);
		super.setOrder(ORDER);
		super.setAdvice(new AccessAuthorityAdvice(accessAuthority));
	}
}
