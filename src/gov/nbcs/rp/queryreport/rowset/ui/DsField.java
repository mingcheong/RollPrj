/**
 * @# DsField.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

/**
 * 功能说明:拖拽的载体
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public class DsField {
	private String field;

	private String title;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public DsField(String field, String title) {
		this.field = field;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		if (title != null && !this.title.equals(""))
			return this.title;
		return this.field;
	}

}
