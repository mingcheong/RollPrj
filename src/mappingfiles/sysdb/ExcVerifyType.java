/*
 * $Id: ExcVerifyType.java,v 1.1 2011/03/14 08:39:48 administrator Exp $
 *
 * Copyright (C) 2006 ������. All rights reserved.
 */

package mappingfiles.sysdb;

import java.io.Serializable;

/**
 * У�����ͳ־û���
 * 
 * @author liujiaguang
 * @version $Revision: 1.1 $, $Date: 2011/03/14 08:39:48 $
 */
public class ExcVerifyType implements Serializable {
	/** ���а汾��ʶ */
	private static final long serialVersionUID = -5023579301748975161L;

	/** XML�ļ�����չ�� */
	public static final String FILE_EXT_XML = ".xml";
	/** TXT�ļ�����չ�� */
	public static final String FILE_EXT_TXT = ".txt";
	/** �Ƿ�����: true/1-���� */
	public static final Integer ENABLED_TRUE = new Integer(1);
	/** �Ƿ�����: false/0-���� */
	public static final Integer ENABLED_FALSE = new Integer(0);

	/** У������ID */
	private String verifyTypeId;
	/** У�����ͱ���verifyTypeCode(���ڷ�������) */
	private String verifyTypeCode;
	/** �ļ���չ����".xml" �� ".txt"�� */
	private String fileExt;
	/** У��������ȫ��������·���� */
	private String verifyImpl;
	/** У������verifyName */
	private String verifyName;
	/** �Ƿ����ã�true/1-���� false/0-���ã� */
	private boolean enabled;
	/** ��ע */
	private String remark;

	/**
	 * �޲ι��췽��
	 */
	public ExcVerifyType() {
	}

	public String getVerifyTypeId() {
		return verifyTypeId;
	}

	public void setVerifyTypeId(String verifyTypeId) {
		this.verifyTypeId = verifyTypeId;
	}

	public String getVerifyTypeCode() {
		return verifyTypeCode;
	}

	public void setVerifyTypeCode(String verifyTypeCode) {
		this.verifyTypeCode = verifyTypeCode;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getVerifyImpl() {
		return verifyImpl;
	}

	public void setVerifyImpl(String verifyImpl) {
		this.verifyImpl = verifyImpl;
	}

	public String getVerifyName() {
		return verifyName;
	}

	public void setVerifyName(String verifyName) {
		this.verifyName = verifyName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("[ExcVerifyType");
		sb.append(": verifyTypeId=").append(verifyTypeId);
		sb.append(": verifyTypeCode=").append(verifyTypeCode);
		sb.append(", fileExt=").append(fileExt);
		sb.append(", verifyImpl=").append(verifyImpl);
		sb.append(", verifyName=").append(verifyName);
		sb.append(", enabled=").append(enabled);
		sb.append(", remark=").append(remark);
		sb.append("]");

		return sb.toString();
	}
}