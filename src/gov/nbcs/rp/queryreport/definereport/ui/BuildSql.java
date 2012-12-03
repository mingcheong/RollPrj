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

	// 存放分配表头ename的值
	private Map enameMap;

	// 存放使用到字段的分配的ename值
	private Map fieldEnameMap;

	private ReportQuerySource querySource;

	// 操作区单元格列
	private List lstCells;

	// 将相同列字的refID校验为相同类
	private CheckSameCol checkSameCol;

	/**
	 * 构造函数
	 * 
	 * @param groupReport
	 * @param sReportId
	 */
	public BuildSql(GroupReport groupReport) {
		this.groupReport = groupReport;

		// 定义querySource
		try {
			// 定义querySource
			querySource = (ReportQuerySource) ReportConver
					.getReportQuerySource(this.groupReport);
		} catch (SummaryConverException e) {
			new MessageBox("创建sql出错", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	/**
	 * 得到生成的sSql语句
	 * 
	 * @param dsHeader表头DataSet
	 * @return
	 * @throws Exception
	 */
	public List getSqlLinesSql(DataSet dsHeader) throws Exception {
		Map enameTmpMap = ReportHeader.getHeaderEname(dsHeader);
		return getSqlLinesSql(enameTmpMap);
	}

	/**
	 * 得到生成sql语句的列值
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getSqlLinesSql(Map enameMap) throws Exception {
		this.enameMap = enameMap;

		// 得到操作区cell列表
		lstCells = DefinePubOther.getCells(groupReport);
		// 校验名称列
		CheckOutEnumName checkOutEnumName = new CheckOutEnumName(querySource);
		checkOutEnumName.check(lstCells);

		// 将相同列字的refID校验为相同
		checkSameCol = new CheckSameCol(querySource);
		checkSameCol.check(lstCells);

		// 为表中使用的每列分配ename值
		fieldEnameMap = getFieldEname();

		List lstResult = new ArrayList();
		// 创建临时表
		BuildTempTableSql buildTempTableSql = new BuildTempTableSql(this);
		lstResult.add(buildTempTableSql.getTempTableSql());
		// 中间Sql
		BuildMidQuerySql buildMidQuerySql = new BuildMidQuerySql(this);
		lstResult.addAll(buildMidQuerySql.getMidQuery());
		// 最终语句
		BuildLastQuerySql buildLastQuerySql = new BuildLastQuerySql(this);
		lstResult.add(buildLastQuerySql.getLastQuery());
		return lstResult;
	}

	/**
	 * 设置每列ename值
	 * 
	 * @return
	 */
	private Map getFieldEname() {
		// 单元格
		Object value;
		// 分组列值
		MyGroupValueImpl groupValueImpl;
		// 计算列值
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
			// 为每列设置ename值
			if (value instanceof MyGroupValueImpl) {// 分组列
				groupValueImpl = (MyGroupValueImpl) value;
				// 循环处理每列每个字段,如果是枚举编码或名称使用自己本身列名称，不分配新的名称
				for (int j = 0; j < groupValueImpl.getGroupAbleArray().length; j++) {
					functionRef = groupValueImpl.getGroupAbleArray()[j]
							.getFormula().getFunctionRefArray();
					for (int k = 0; k < functionRef.length; k++) {

						boolean isEnum = DefinePub.judgetEnumWithColID(
								querySource, functionRef[k].getSourceID(),
								functionRef[k].getSourceColID(), false);
						if (isEnum) {
							// 是否相同字段已存在
							if (resultMap
									.containsKey(functionRef[k].getRefID()))
								continue;

							// 判断ename值是否已存在
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
			} else if (value instanceof MyCalculateValueImpl) {// 计算列
				calculateValueImpl = (MyCalculateValueImpl) value;
				// 判断是不是单元格相加列
				if (DefinePub.checkIsCellFormula(calculateValueImpl))
					continue;
				if (calculateValueImpl.getMeasureArray() == null)
					continue;
				for (int j = 0; j < calculateValueImpl.getMeasureArray().length; j++) {

					// 判断是否已存在
					if (resultMap.containsKey(calculateValueImpl
							.getMeasureArray()[j].getMeasureID())) {
						continue;
					}

					// 判断是否是count函数
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
	 * 更换支出资金来源
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
