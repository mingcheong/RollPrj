/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.supportvalue;

import java.math.BigDecimal;

/**
 * The Class SupportValue.
 */
public class SupportValue {
	private String supType;
	private BigDecimal nValue;
	private String cValue;
	private BigDecimal fValue;
	private String remark;
	private String rgCode;
	private String lastVer;

	public SupportValue() {
		super();
	}

	/**
	 * Instantiates a new support value.
	 * 
	 * @param supType
	 *            the sup type
	 * @param nValue
	 *            the n value
	 * @param cValue
	 *            the c value
	 * @param fValue
	 *            the f value
	 * @param remark
	 *            the remark
	 * @param rgCode
	 *            the rg code
	 * @param lastVer
	 *            the last ver
	 */
	public SupportValue(String supType, BigDecimal nValue, String cValue,
			BigDecimal fValue, String remark, String rgCode, String lastVer) {
		super();
		this.supType = supType;
		this.nValue = nValue;
		this.cValue = cValue;
		this.fValue = fValue;
		this.remark = remark;
		this.rgCode = rgCode;
		this.lastVer = lastVer;
	}

	/**
	 * @return the supType
	 */
	public String getSupType() {
		return supType;
	}

	/**
	 * @return the nValue
	 */
	public BigDecimal getNValue() {
		return nValue;
	}

	/**
	 * @return the cValue
	 */
	public String getCValue() {
		return cValue;
	}

	/**
	 * @return the fValue
	 */
	public BigDecimal getFValue() {
		return fValue;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @return the rgCode
	 */
	public String getRgCode() {
		return rgCode;
	}

	/**
	 * @return the lastVer
	 */
	public String getLastVer() {
		return lastVer;
	}

	/**
	 * Gets the n_value as int.
	 * 
	 * @return the n value as int
	 * 
	 * 注意: 当该字段未设置或该常量未设置时，也会返回0。对于默认值非0的常量，注意判断。
	 */
	public int getNValueAsInt() {
		if (nValue != null) {
			return nValue.intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Gets the n_value as long.
	 * 
	 * @return the n value as long
	 * 
	 * 注意: 当该字段未设置或该常量未设置时，也会返回0L。对于默认值非0L的常量，注意判断。
	 */
	public long getNValueAsLong() {
		if (nValue != null) {
			return nValue.longValue();
		} else {
			return 0L;
		}
	}

	/**
	 * Gets the n_value as string.
	 * 
	 * @return the n value as string
	 * 
	 * 注意: 当该字段未设置或该常量未设置时，也会返回""。
	 */
	public String getNValueAsString() {
		if (nValue != null) {
			return nValue.toString();
		} else {
			return "";
		}
	}

	/**
	 * Gets the c_value not null.
	 * 
	 * @return the c value not null
	 * 
	 * 注意: 当该字段未设置或该常量未设置时，也会返回""。
	 */
	public String getCValueNotNull() {
		if (cValue != null) {
			return cValue;
		} else {
			return "";
		}
	}

	/**
	 * Gets the f_value as float.
	 * 
	 * @return the f value as float
	 * 
	 * 注意: 当该字段未设置或该常量未设置时，也会返回0f。对于默认值非0f的常量，注意判断。
	 */
	public float getFValueAsFloat() {
		if (fValue != null) {
			return fValue.floatValue();
		} else {
			return 0F;
		}

	}

	/**
	 * Gets the f_value as double.
	 * 
	 * @return the f value as double
	 * 
	 * 注意: 当该字段未设置或该常量未设置时，也会返回0d。对于默认值非0d的常量，注意判断。
	 */
	public double getFValueAsDouble() {
		if (fValue != null) {
			return fValue.doubleValue();
		} else {
			return 0D;
		}

	}
}