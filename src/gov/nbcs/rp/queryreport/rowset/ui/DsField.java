/**
 * @# DsField.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

/**
 * ����˵��:��ק������
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
