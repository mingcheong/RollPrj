/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The class FbLogger.
 * 
 * @author qj
 * @version 1.0, Jan 15, 2009
 * @since 6.3.50
 */
public class FbLogger {

	static {
		// ����rp��log4j�����ļ�
		PropertyConfigurator.configure(FbLogger.class
				.getResource("/log4j_rp.properties"));
	}

	/**
	 * ��ȡָ�����Ƶ�Logger
	 * 
	 * @param name
	 *            Logger������
	 *            <p>
	 *            ��Ӧlog4j_fb.properties�У���log4j.logger.fb.sql����nameΪfb.sql
	 * @return Logger
	 */
	public static FbLogger logger(String name) {
		return new FbLogger(Logger.getLogger(name));
	}

	/**
	 * ��ȡ���SQL��Logger
	 * 
	 * @return fb.sql��Ӧ��Logger
	 */
	public static FbLogger sqlLogger() {
		return logger("fb.sql");
	}

	/**
	 * ��ȡ�����Console��Logger
	 * 
	 * @return fb.console��Ӧ��Logger
	 */
	public static FbLogger consoleLogger() {
		return logger("fb.console");
	}

	/**
	 * ��ȡ�����file��Logger
	 * 
	 * @return fb.file��Ӧ��Logger
	 */
	public static FbLogger fileLogger() {
		return logger("fb.file");
	}

	/** The logger. */
	private Logger logger;

	/**
	 * Instantiates a new fb logger.
	 * 
	 * @param logger
	 *            the logger
	 */
	private FbLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Debug.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public void debug(Object message, Throwable t) {
		logger.debug(message, t);
	}

	/**
	 * Debug.
	 * 
	 * @param message
	 *            the message
	 */
	public void debug(Object message) {
		logger.debug(message);
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public void error(Object message, Throwable t) {
		logger.error(message, t);
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 */
	public void error(Object message) {
		logger.error(message);
	}

	/**
	 * Fatal.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public void fatal(Object message, Throwable t) {
		logger.fatal(message, t);
	}

	/**
	 * Fatal.
	 * 
	 * @param message
	 *            the message
	 */
	public void fatal(Object message) {
		logger.fatal(message);
	}

	/**
	 * Info.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public void info(Object message, Throwable t) {
		logger.info(message, t);
	}

	/**
	 * Info.
	 * 
	 * @param message
	 *            the message
	 */
	public void info(Object message) {
		logger.info(message);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public void warn(Object message, Throwable t) {
		logger.warn(message, t);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 */
	public void warn(Object message) {
		logger.warn(message);
	}

	/**
	 * ��ȡexp�����logger
	 * 
	 * @param exp
	 *            the exp
	 */
	public void error(Exception exp) {
		synchronized (logger) {
			logger.error(exp);
			StackTraceElement[] trace = exp.getStackTrace();
			for (int i = 0; i < trace.length; i++) {
				logger.error("\tat " + trace[i]);
			}

			logger.error(exp, exp.getCause());
			//            Throwable ourCause = exp.getCause();
			//            if (ourCause != null)
			//                ourCause.printStackTraceAsCause(s, trace);
		}
	}
}
