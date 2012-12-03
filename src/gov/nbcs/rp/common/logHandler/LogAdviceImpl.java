/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.logHandler;

import com.foundercy.pf.util.sessionmanager.SessionUtil;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.QueryStub;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * The Class LogAdviceImpl.
 */
public class LogAdviceImpl implements ILogAdvice {

	/** The BUNDL e_ name. */
	public final String BUNDLE_NAME = "gov/nbcs/rp/common/logHandler/LogProperties";

	/** The RESOURC e_ bundle. */
	public final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.logHandler.ILogAdvice#before(java.lang.reflect.Method,
	 *      java.lang.Object[], java.lang.Object)
	 */
	public void before(Method method, Object aobj[], Object obj) {
		LogDTO logInfo = LogDTO.newInstance();

		String remark = getPropertyValue(obj, method);
		logInfo.setRemark("开始" + String2GBK(remark));

		logInfo.save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.logHandler.ILogAdvice#afterReturning(java.lang.Object,
	 *      java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	public void afterReturning(Object rt, Method method, Object[] aobj,
			Object obj) throws Throwable {
		LogDTO logInfo = LogDTO.newInstance();

		String remark = getPropertyValue(obj, method);
		logInfo.setRemark(String2GBK(remark) + "完成");

		logInfo.save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.logHandler.ILogAdvice#afterThrowing(java.lang.Throwable,
	 *      java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	public void afterThrowing(Throwable ex, Method method, Object aobj[],
			Object obj) {
		LogDTO logInfo = LogDTO.newInstance();

		String remark = getPropertyValue(obj, method);
		logInfo.setRemark(String2GBK(remark) + "失败");

		logInfo.save();
	}

	/**
	 * Gets the property value.
	 * 
	 * @param obj
	 *            the obj
	 * @param method
	 *            the method
	 * @return the property value
	 */
	private String getPropertyValue(Object obj, Method method) {
		// 所属类名
		String clsName = obj.getClass().getName();

		String propKey = clsName + "." + method.getName();
		String remark;
		try {
			remark = RESOURCE_BUNDLE.getString(propKey);
			if (remark == null) {
				remark = RESOURCE_BUNDLE.getString(method.getName());
			}
		} catch (Exception e) {
			remark = RESOURCE_BUNDLE.getString(method.getName());
		}
		return remark;
	}

	/**
	 * String2 gbk.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	public String String2GBK(String str) {
		if ((str == null) || str.trim().equals("")) {
			return "";
		}
		try {
			String result = new String(str.getBytes("ISO-8859-1"), "gb2312");
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * The Class LogDTO.
	 */
	private static class LogDTO {
		/** The format. */
		private static final SimpleDateFormat format = new SimpleDateFormat(
				"yy/MM/dd HH:mm:ss");

		/** The Constant log_insert_sql. */
		private static final String log_insert_sql = "insert into fb_s_log(row_id,userCode,userip,remark,actionTime) values(?,?,?,?,?) ";

		/**
		 * New log info.
		 * 
		 * @return the log info
		 */
		public static LogDTO newInstance() {
			LogDTO logInfo = new LogDTO();

			logInfo.setIp(SessionUtil.getOnlineUser().getUser_ip());
			logInfo.setUserCode(SessionUtil.getOnlineUser().getUser_code());
			logInfo.setActionTime(format.format(new Date()));

			return logInfo;
		}

		/**
		 * Save.
		 */
		public void save() {

			try {
				QueryStub.getQueryTool().executeUpdate2(
						log_insert_sql,
						new Object[] { UUID.randomUUID().toString(), userCode,
								ip, remark, actionTime });
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/** The remark. */
		String remark;

		/** The ip. */
		String ip;

		/** The user code. */
		String userCode;

		/** The action time. */
		String actionTime;

		/**
		 * Gets the remark.
		 * 
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}

		/**
		 * Sets the remark.
		 * 
		 * @param remark
		 *            the new remark
		 */
		public void setRemark(String remark) {
			this.remark = remark;
		}

		/**
		 * Gets the ip.
		 * 
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}

		/**
		 * Sets the ip.
		 * 
		 * @param ip
		 *            the new ip
		 */
		public void setIp(String ip) {
			this.ip = ip;
		}

		/**
		 * Gets the user code.
		 * 
		 * @return the user code
		 */
		public String getUserCode() {
			return userCode;
		}

		/**
		 * Sets the user code.
		 * 
		 * @param userCode
		 *            the new user code
		 */
		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}

		/**
		 * Gets the action time.
		 * 
		 * @return the action time
		 */
		public String getActionTime() {
			return actionTime;
		}

		/**
		 * Sets the action time.
		 * 
		 * @param actionTime
		 *            the new action time
		 */
		public void setActionTime(String actionTime) {
			this.actionTime = actionTime;
		}
	}

}
