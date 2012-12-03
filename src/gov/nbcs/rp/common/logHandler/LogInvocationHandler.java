package gov.nbcs.rp.common.logHandler;

import com.foundercy.pf.util.Global;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class LogInvocationHandler implements MethodInterceptor {
	// 要代理的原始对象
	private ILogAdvice ilog;

	public LogInvocationHandler() {
	}

	public Object invoke(MethodInvocation mi) throws Throwable {
		Object result = null;
		// 方法调用之前
		// 离线版不加
		if (Global.loginmode == 1) {
			result = mi.proceed();
			return result;
		}
		this.ilog.before(mi.getMethod(), mi.getArguments(), mi.getThis());
		// 调用原始对象的方法
		try {
			result = mi.proceed();
			// 方法调用之后
			this.ilog.afterReturning(result, mi.getMethod(), mi.getArguments(),
					mi.getThis());
		} catch (Throwable e) {
			// 方法调用异常
			this.ilog.afterThrowing(e, mi.getMethod(), mi.getArguments(), mi
					.getThis());
			throw e;
		}
		return result;
	}

	public ILogAdvice getIlog() {
		return ilog;
	}

	public void setIlog(ILogAdvice ilog) {
		this.ilog = ilog;
	}
}
