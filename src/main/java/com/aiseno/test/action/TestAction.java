package com.aiseno.test.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiseno.access.annotation.AccessPermissions;
import com.aiseno.access.annotation.AccessRoles;
import com.aiseno.access.annotation.authority.AccessDefaultPointcutAdvisor;
import com.aiseno.access.holder.AccessContextHolder;
import com.aiseno.access.servlet.registration.AccessServletRegistrationBean;

@RestController
@RequestMapping("test")
public class TestAction {

	@Autowired
	private ApplicationContext applicationContext;
	
	@GetMapping("hello")
    public Object test() {
        return AccessContextHolder.getAccessPrincipal();
    }

	@GetMapping("v1")
	public Object testV1(HttpServletRequest request) {
		return request.getUserPrincipal();
	}
	
	@GetMapping("v2")
	@AccessRoles(value = "666")
	public Object testV2() {
		return AccessContextHolder.getAccessPrincipal();
	}

	
	@GetMapping("v3")
	@AccessPermissions(value = "te")
	public Object testV3() {
		return AccessContextHolder.getAccessPrincipal();
	}
	
	@GetMapping("v4")
	public Object testV4() {
		
		AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor = applicationContext.getBean(AccessDefaultPointcutAdvisor.class);
		System.out.println(accessDefaultPointcutAdvisor);
		return AccessContextHolder.getAccessOnline();
	}
	
}
