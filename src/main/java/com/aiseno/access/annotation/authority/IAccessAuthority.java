package com.aiseno.access.annotation.authority;

import java.lang.reflect.Method;

public interface IAccessAuthority {
	
	void handle(Method method);
}
