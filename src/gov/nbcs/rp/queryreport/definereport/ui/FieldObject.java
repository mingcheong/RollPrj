/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.io.Serializable;

import com.fr.base.FCloneable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-24
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class FieldObject implements Serializable, FCloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String DICID = null;

	// 数据源EName
//	String OBJECT_ENAME = null;

	// 数据源中文名
	String OBJECT_CNAME = null;

	// 字段中文名
	String FIELD_FNAME = null;

	// 字段名
	String FIELD_ENAME = null;

	// 字段类型
	String FIELD_TYPE = null;

	public FieldObject() {
	}

	public String getFIELD_ENAME() {
		return FIELD_ENAME;
	}

	public void setFIELD_ENAME(String field_ename) {
		FIELD_ENAME = field_ename;
	}

	public String getFIELD_FNAME() {
		return FIELD_FNAME;
	}

	public void setFIELD_FNAME(String field_fname) {
		FIELD_FNAME = field_fname;
	}

	public String getFIELD_TYPE() {
		return FIELD_TYPE;
	}

	public void setFIELD_TYPE(String field_type) {
		FIELD_TYPE = field_type;
	}

//	public String getOBJECT_ENAME() {
//		return OBJECT_ENAME;
//	}
//
//	public void setOBJECT_ENAME(String object_ename) {
//		OBJECT_ENAME = object_ename;
//	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getOBJECT_CNAME() {
		return OBJECT_CNAME;
	}

	public void setOBJECT_CNAME(String object_cname) {
		OBJECT_CNAME = object_cname;
	}

	public String getDICID() {
		return DICID;
	}

	public void setDICID(String dicid) {
		DICID = dicid;
	}

}
