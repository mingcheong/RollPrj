/**
 * @# FieldInfo.java    <文件名>
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

/**
 * 功能说明:
 * <P>
 * Copyright
 * <P>

 */
public class FieldInfo {

	String fieldTitle;

	String fieldEName;

	String fieldType;

	String fieldFormat;

	public String getFieldEName() {
		return fieldEName;
	}

	public void setFieldEName(String fieldEName) {
		this.fieldEName = fieldEName;
	}

	public String getFieldFormat() {
		return fieldFormat;
	}

	public void setFieldFormat(String fieldFormat) {
		this.fieldFormat = fieldFormat;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public FieldInfo(String fieldTitle, String fieldEName, String fieldType,
			String fieldFormat) {
		this.fieldTitle = fieldTitle;
		this.fieldEName = fieldEName;
		this.fieldType = fieldType;
		this.fieldFormat = fieldFormat;
	}

}
