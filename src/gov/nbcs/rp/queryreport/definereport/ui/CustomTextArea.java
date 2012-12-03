package gov.nbcs.rp.queryreport.definereport.ui;

import javax.swing.JTextArea;
import javax.swing.text.Document;

public class CustomTextArea extends JTextArea {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public CustomTextArea() {
		super();
	}

	public CustomTextArea(Document arg0, String arg1, int arg2, int arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CustomTextArea(Document arg0) {
		super(arg0);
	}

	public CustomTextArea(int arg0, int arg1) {
		super(arg0, arg1);
	}

	public CustomTextArea(String arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public CustomTextArea(String arg0) {
		super(arg0);
	}

	public void setValue(String str, int pos) {
		int iStart = this.getSelectionStart();
		int iEnd = this.getSelectionEnd();
		String value = this.getText();
		value = value.substring(0, iStart) + str + value.substring(iEnd);
		this.setText(value);
		this.setSelectionEnd(iStart);
		// 获得焦点
		this.requestFocus();
		// 设置光标显示位置
		this.setCaretPosition(iStart + pos);
	}

	public void setValue(String str) {
		this.setValue(str, str.length());
	}

}
