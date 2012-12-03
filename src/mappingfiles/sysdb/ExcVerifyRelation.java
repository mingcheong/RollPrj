/*
 * $Id: ExcVerifyRelation.java,v 1.1 2011/03/14 08:39:48 administrator Exp $
 *
 * Copyright (C) 2006 财政部. All rights reserved.
 */

package mappingfiles.sysdb;

import java.io.Serializable;

/**
 * 校验关系持久化类
 * 
 * @author liujiaguang
 * @version $Revision: 1.1 $, $Date: 2011/03/14 08:39:48 $
 */
public class ExcVerifyRelation implements Serializable {
	/** 串行版本标识 */
	private static final long serialVersionUID = -2825511031703839809L;

	/** 校验关系ID */
	private String verifyRelationId;
	/** 数据类型编码（参见：ExcDataType.DATA_TYPE_CODE） */
	private String dataTypeCode;
	/** 校验类型ID */
	private String verifyTypeId;
	/** 财政年度 */
	private int setYear;
	/** 排序序号 */
	private int orderNo;

	/**
	 * 无参构造方法
	 */
	public ExcVerifyRelation() {
	}

	public String getVerifyRelationId() {
		return verifyRelationId;
	}

	public void setVerifyRelationId(String verifyRelationId) {
		this.verifyRelationId = verifyRelationId;
	}

	public String getDataTypeCode() {
		return dataTypeCode;
	}

	public void setDataTypeCode(String dataTypeCode) {
		this.dataTypeCode = dataTypeCode;
	}

	public String getVerifyTypeId() {
		return verifyTypeId;
	}

	public void setVerifyTypeId(String verifyTypeId) {
		this.verifyTypeId = verifyTypeId;
	}

	public int getSetYear() {
		return setYear;
	}

	public void setSetYear(int setYear) {
		this.setYear = setYear;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("[ExcVerifyRelation");
		sb.append(": verifyRelationId=").append(verifyRelationId);
		sb.append(", dataTypeCode=").append(dataTypeCode);
		sb.append(", verifyTypeId=").append(verifyTypeId);
		sb.append(", setYear=").append(setYear);
		sb.append(", orderNo=").append(orderNo);
		sb.append("]");

		return sb.toString();
	}
}