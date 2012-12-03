/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title：错误信息类
 * </p>
 * <p>
 * Description:记录错误日志，并给出用户友好提示信息
 * @version 1.0
 */
public class ErrorInfo {

	/**
	 * Show error dialog.
	 * 
	 * @param e
	 *            the e
	 * @param errInfo
	 *            the err info
	 */
	public static void showErrorDialog(Exception e, String errInfo) {
		// //记录日志
		// LogClient.error(e.getMessage());
		e.printStackTrace();
		// 提示信息窗口
		StackTraceElement[] info = e.getStackTrace();
		String msg = "";
		for (int i = 0; i < info.length; i++) {
			msg += info[i].toString() + "\n";
		}
		MessageBox box = new MessageBox(errInfo, msg, MessageBox.ERROR,
				MessageBox.BUTTON_OK);
		if((msg != null) && (msg.length() > 0)) {
			box.expand();
		}
		box.setVisible(true);
	}

	/**
	 * Show error dialog.
	 * 
	 * @param errInfo
	 *            the err info
	 */
	public static void showErrorDialog(String errInfo) {
		MessageBox msgBox = new MessageBox(errInfo, MessageBox.ERROR,
				MessageBox.BUTTON_OK);
		msgBox.setVisible(true);
	}

	/**
	 * Show error dialog.
	 * 
	 * @param help
	 *            the help
	 * @param errInfo
	 *            the err info
	 */
	public static void showErrorDialog(String errInfo, String help) {
		MessageBox msgBox = new MessageBox(errInfo, help, MessageBox.ERROR,
				MessageBox.BUTTON_OK);
		if((help != null) && (help.length() > 0)) {
			msgBox.expand();
		}
		msgBox.setVisible(true);
	}

	/**
	 * Show error dialog.
	 * 
	 * @param msgInfo
	 *            the message
	 */
	public static void showMessageDialog(String msgInfo) {
		MessageBox msgBox = new MessageBox(msgInfo, MessageBox.MESSAGE,
				MessageBox.BUTTON_OK);
		msgBox.setVisible(true);
	}

	/**
	 * Show error dialog.
	 * 
	 * @param msgInfo
	 *            the message
	 * @param help
	 *            the help
	 */
	public static void showMessageDialog(String msgInfo, String help) {
		MessageBox msgBox = new MessageBox(msgInfo, help, MessageBox.MESSAGE,
				MessageBox.BUTTON_OK);
		if((help != null) && (help.length() > 0)) {
			msgBox.expand();
		}
		msgBox.setVisible(true);
	}

	/**
	 * Prompt to continue.
	 * 
	 * @param message
	 *            the message
	 * @return the boolean
	 */
	public static boolean promptToContinue(String message) {
		MessageBox msgBox = new MessageBox(message, MessageBox.MESSAGE,
				MessageBox.BUTTON_OK | MessageBox.BUTTON_CANCEL);
		msgBox.setVisible(true);
		return msgBox.result == MessageBox.BUTTON_OK;
	}

	/**
	 * Prompt to continue.
	 * 
	 * @param message
	 *            the message
	 * @param help
	 *            the help
	 * @return the boolean
	 */
	public static boolean promptToContinue(String message, String help) {
		MessageBox msgBox = new MessageBox(message, help, MessageBox.MESSAGE,
				MessageBox.BUTTON_OK | MessageBox.BUTTON_CANCEL);
		if((help != null) && (help.length() > 0)) {
			msgBox.expand();
		}
		msgBox.setVisible(true);
		return msgBox.result == MessageBox.BUTTON_OK;
	}

	/**
	 * 确认数据状态，关闭.
	 * 
	 * @param needPrompt
	 *            true if prompt is needed
	 */
	public static void promptToSaveOnClose(boolean needPrompt) {
		promptToSaveOnClose(needPrompt, null);
	}

	/**
	 * Prompt to save on close.
	 * 
	 * @param needPrompt
	 *            the need prompt
	 * @param message
	 *            the message
	 */
	public static void promptToSaveOnClose(boolean needPrompt, String message) {
		if (needPrompt) {
			MessageBox msgBox = new MessageBox(message == null ? "数据未保存，确定关闭吗？"
					: message, "提示", MessageBox.MESSAGE, MessageBox.BUTTON_OK
					| MessageBox.BUTTON_CANCEL);
			msgBox.setVisible(true);
			if (msgBox.result == MessageBox.OK) {
				((FFrame) Global.mainFrame).closeMenu();
			}
		} else {
			((FFrame) Global.mainFrame).closeMenu();
		}

	}
}
