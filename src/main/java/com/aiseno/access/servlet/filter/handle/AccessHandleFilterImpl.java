package com.aiseno.access.servlet.filter.handle;

import com.aiseno.access.exception.IFilterException;
import com.aiseno.access.exception.UnauthorizedAccessException;
import com.aiseno.access.servlet.filter.support.AccessPathMatcherSupport;

public class AccessHandleFilterImpl extends AbstractAccessHandleFilter implements AccessHandleFilter {

	private static ThreadLocal<IFilterException> exception = new ThreadLocal<>();

	private static UnauthorizedAccessException notLoginEx = new UnauthorizedAccessException("Unauthorized. Please login first");

	@Override
	public boolean handle(String uri, AccessPathMatcherSupport support) {
		if (support.isSuccessUri(uri)) {
			return this.doit(notLoginEx); // 是成功接口 (顺序 1)
		}
		if (support.isValidLogoutUri(uri)) { // 是登出接口 (顺序 2)
			return this.doit(notLoginEx);
		}
		if (support.isValidIgnoreUri(uri)) { // 不用校验 (顺序 3)
			return true;
		}
		if (support.isValidUri(uri)) { // 需要检验的接口 (顺序 4)
			return this.doit(notLoginEx);
		}
		return true;
	}

	@Override
	public IFilterException exception() {
		return exception.get();
	}

	@Override
	boolean doit(IFilterException e) {
		if (!super.isAlreadyLogin()) {
			exception.set(e);
			return false;
		}
		return true;
	}
}
