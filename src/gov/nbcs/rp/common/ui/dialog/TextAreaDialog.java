/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.dialog;

import com.foundercy.pf.control.FTextArea;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JOptionPane;

public class TextAreaDialog extends RpDialog {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The text area. */
	FTextArea textArea = null;
	
	/** The value is true if it is ok. */
	boolean isOk = false;

	/**
	 * @param owner
	 * @param modal
	 */
	public TextAreaDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		init();
	}

	/**
	 * @param owner
	 * @param modal
	 */
	public TextAreaDialog(Frame owner, boolean modal) {
		super(owner, modal);
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		textArea = new FTextArea();
		textArea.setTitleVisible(false);
		this.getBodyPane().add(textArea);
	}

	/* (non-Javadoc)
	 * @see gov.nbcs.rp.common.ui.dialog.FbDialog#confirmClose()
	 */
	protected boolean confirmClose() {
		isOk = JOptionPane.showConfirmDialog(null, "确定保存吗？", "保存提示",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.ui.dialog.FbDialog#initComponents()
	 */
	protected void initComponents() {
	}

	/* (non-Javadoc)
	 * @see gov.nbcs.rp.common.ui.dialog.FbDialog#bindListeners()
	 */
	protected void bindListeners() {
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return isOk ? (String) textArea.getValue() : "";
	}

}
