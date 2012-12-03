/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.datactrl.DataSet;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.reportcy.summary.exception.SummaryConverException;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.fr.report.GroupReport;


 
public class BuildSql {

	// groupReport
	private GroupReport groupReport = null;

	// ��ŷ����ͷename��ֵ
	private Map enameMap;

	// ���ʹ�õ��ֶεķ����enameֵ
	private Map fieldEnameMap;

	private ReportQuerySource querySource;

	// ��������Ԫ����
	private List lstCells;

	// ����ͬ���ֵ�refIDУ��Ϊ��ͬ��
	private CheckSameCol checkSameCol;

	/**
	 * ���캯��
	 * 
	 * @param groupReport
	 * @param sReportId
	 */
	public BuildSql(GroupReport groupReport) {
		this.groupReport = groupReport;

		// ����querySource
		try {
			// ����querySource
			querySource = (ReportQuerySource) ReportConver
					.getReportQuerySource(this.groupReport);
		} catch (SummaryConverException e) {
			new MessageBox("����sql����", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	/**
	 * �õ����ɵ�sSql���
	 * 
	 * @param dsHeader��ͷDataSet
	 * @return
	 * @throws Exception
	 */
	public List getSqlLinesSql(DataSet dsHeader) throws Exception {
		Map enameTmpMap = ReportHeader.getHeaderEname(dsHeader);
		return getSqlLinesSql(enameTmpMap);
	}

	/**
	 * �õ�����sql������ֵ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getSqlLinesSql(Map enameMap) throws Exception {
		this.enameMap = enameMap;

		// �õ�������cell�б�
		lstCells = DefinePubOther.getCells(groupReport);
		// У��������
		CheckOutEnumName checkOutEnumName = new CheckOutEnumName(querySource);
		checkOutEnumName.check(lstCells);

		// ����ͬ���ֵ�refIDУ��Ϊ��ͬ
		checkSameCol = new CheckSameCol(querySource);
		checkSameCol.check(lstCells);

		// Ϊ����ʹ�õ�ÿ�з���enameֵ
		fieldEnameMap = getFieldEname();

		List lstResult = new ArrayList();
		// ������ʱ��
		BuildTempTableSql buildTempTableSql = new BuildTempTableSql(this);
		lstResult.add(buildTempTableSql.getTempTableSql());
		// �м�Sql
		BuildMidQuerySql buildMidQuerySql = new BuildMidQuerySql(this);
		lstResult.addAll(buildMidQuerySql.getMidQuery());
		// �������
		BuildLastQuerySql buildLastQuerySql = new BuildLastQuerySql(this);
		lstResult.add(buildLastQuerySql.getLastQuery());
		return lstResult;
	}

	/**
	 * ����ÿ��enameֵ
	 * 
	 * @return
	 */
	private Map getFieldEname() {
		// ��Ԫ��
		Object value;
		// ������ֵ
		MyGroupValueImpl groupValueImpl;
		// ������ֵ
		MyCalculateValueImpl calculateValueImpl;

		int iFMax = 0;
		int iCMax = 0;
		FunctionRef[] functionRef;

		Map resultMap = new HashMap();
		for (int i = 0; i < lstCells.size(); i++) {
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			// Ϊÿ������enameֵ
			if (value instanceof MyGroupValueImpl) {// ������
				groupValueImpl = (MyGroupValueImpl) value;
				// ѭ������ÿ��ÿ���ֶ�,�����ö�ٱ��������ʹ���Լ����������ƣ��������µ�����
				for (int j = 0; j < groupValueImpl.getGroupAbleArray().length; j++) {
					functionRef = groupValueImpl.getGroupAbleArray()[j]
							.getFormula().getFunctionRefArray();
					for (int k = 0; k < functionRef.length; k++) {

						boolean isEnum = DefinePub.judgetEnumWithColID(
								querySource, functionRef[k].getSourceID(),
								functionRef[k].getSourceColID(), false);
						if (isEnum) {
							// �Ƿ���ͬ�ֶ��Ѵ���
							if (resultMap
									.containsKey(functionRef[k].getRefID()))
								continue;

							// �ж�enameֵ�Ƿ��Ѵ���
							if (resultMap.containsValue(functionRef[k]
									.getSourceColID())) {
								iCMax++;
								resultMap.put(functionRef[k].getRefID(), "C_"
										+ iCMax);
							} else {
								resultMap.put(functionRef[k].getRefID(),
										functionRef[k].getSourceColID());
							}
						} else {
							iCMax++;
							resultMap.put(functionRef[k].getRefID(), "C_"
									+ iCMax);

						}
					}
				}
			} else if (value instanceof MyCalculateValueImpl) {// ������
				calculateValueImpl = (MyCalculateValueImpl) value;
				// �ж��ǲ��ǵ�Ԫ�������
				if (DefinePub.checkIsCellFormula(calculateValueImpl))
					continue;
				if (calculateValueImpl.getMeasureArray() == null)
					continue;
				for (int j = 0; j < calculateValueImpl.getMeasureArray().length; j++) {

					// �ж��Ƿ��Ѵ���
					if (resultMap.containsKey(calculateValueImpl
							.getMeasureArray()[j].getMeasureID())) {
						continue;
					}

					// �ж��Ƿ���count����
					if (DefinePubOther.isCountField(calculateValueImpl
							.getMeasureArray()[j])) {
						iCMax++;

						resultMap.put(calculateValueImpl.getMeasureArray()[j]
								.getMeasureID(), "C_" + iCMax);
					} else {
						iFMax++;
						resultMap.put(calculateValueImpl.getMeasureArray()[j]
								.getMeasureID(), "F_" + iFMax);
					}
				}
			}
		}
		return resultMap;
	}

	/**
	 * ����֧���ʽ���Դ
	 * 
	 * @param sFieldEname
	 */
	public void changetDataSourceField(List lstFieldEname) {
		ChangeReportInfo.changetDataSourceField(groupReport, querySource,
				lstFieldEname);
	}

	public Map getEnameMap() {
		return enameMap;
	}

	public Map getFieldEnameMap() {
		return fieldEnameMap;
	}

	public ReportQuerySource getQuerySource() {
		return querySource;
	}

	public List getLstCells() {
		return lstCells;
	}

	public GroupReport getGroupReport() {
		return groupReport;
	}

}
