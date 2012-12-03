/*
 * $Id: ExcVerifyType.java,v 1.1 2011/03/14 08:39:48 administrator Exp $
 *
 * Copyright (C) 2006 财政部. All rights reserved.
 */

package mappingfiles.sysdb;

import java.io.Serializable;

/**
 * 校验类型持久化类
 * 
 * @author liujiaguang
 * @version $Revision: 1.1 $, $Date: 2011/03/14 08:39:48 $
 */
public class ExcVerifyType implements Serializable {
	/** 串行版本标识 */
	private static final long serialVersionUID = -5023579301748975161L;

	/** XML文件的扩展名 */
	public static final String FILE_EXT_XML = ".xml";
	/** TXT文件的扩展名 */
	public static final String FILE_EXT_TXT = ".txt";
	/** 是否启用: true/1-启用 */
	public static final Integer ENABLED_TRUE = new Integer(1);
	/** 是否启用: false/0-禁用 */
	public static final Integer ENABLED_FALSE = new Integer(0);

	/** 校验类型ID */
	private String verifyTypeId;
	/** 校验类型编码verifyTypeCode(用于分类排序) */
	private String verifyTypeCode;
	/** 文件扩展名（".xml" 或 ".txt"） */
	private String fileExt;
	/** 校验类名（全名，含包路径） */
	private String verifyImpl;
	/** 校验名称verifyName */
	private String verifyName;
	/** 是否启用（true/1-启用 false/0-禁用） */
	private boolean enabled;
	/** 备注 */
	private String remark;

	/**
	 * 无参构造方法
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