/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

/**
 * <p>
 * Title:��������Դ������ϵ���ڵ���Ϣ����
 * </p>
 * <p>
 * Description:��������Դ������ϵ���ڵ���Ϣ����
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-26
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MyNodeObject {
	// ԴAID
	String sSourceIDA;

	// ԴBID
	String sSourceIDB;

	// ԴA������
	String sSourceColIDA;

	// ԴB������
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
