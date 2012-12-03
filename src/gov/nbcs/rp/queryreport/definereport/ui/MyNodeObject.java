/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

/**
 * <p>
 * Title:定义数据源关联关系树节点信息对象
 * </p>
 * <p>
 * Description:定义数据源关联关系树节点信息对象
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-26
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MyNodeObject {
	// 源AID
	String sSourceIDA;

	// 源BID
	String sSourceIDB;

	// 源A列名称
	String sSourceColIDA;

	// 源B列名称
	String sSourceColIDB;

	String sRelationsName;

	public String toString() {
		return sRelationsName;
	}

	public String getSRelationsName() {
		return sRelationsName;
	}

	public void setSRelationsName(String relationsName) {
		sRelationsName = relationsName;
	}

	public String getSSourceColIDA() {
		return sSourceColIDA;
	}

	public void setSSourceColIDA(String sourceColIDA) {
		sSourceColIDA = sourceColIDA;
	}

	public String getSSourceColIDB() {
		return sSourceColIDB;
	}

	public void setSSourceColIDB(String sourceColIDB) {
		sSourceColIDB = sourceColIDB;
	}

	public String getSSourceIDA() {
		return sSourceIDA;
	}

	public void setSSourceIDA(String sourceIDA) {
		sSourceIDA = sourceIDA;
	}

	public String getSSourceIDB() {
		return sSourceIDB;
	}

	public void setSSourceIDB(String sourceIDB) {
		sSourceIDB = sourceIDB;
	}

}
