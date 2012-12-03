package gov.nbcs.rp.common.logHandler;

import com.foundercy.pf.util.Global;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class LogInvocationHandler implements MethodInterceptor {
	// Ҫ�����ԭʼ����
	private ILogAdvice ilog;

	public LogInvocationHandler() {
	}

	public Object invoke(MethodInvocation mi) throws Throwable {
		Object result = null;
		// ��������֮ǰ
		// ���߰治��
		if (Global.loginmode == 1) {
			result = mi.proceed();
			return result;
		}
		this.ilog.before(mi.getMethod(), mi.getArguments(), mi.getThis());
		// ����ԭʼ����ķ���
		try {
			result = mi.proceed();
			// ��������֮��
			this.ilog.afterReturning(result, mi.getMethod(), mi.getArguments(),
					mi.getThis());
		} catch (Throwable e) {
			// ���������쳣
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
