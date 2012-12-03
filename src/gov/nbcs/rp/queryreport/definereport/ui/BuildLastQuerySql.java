/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.action.SearchCalcCol;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomCalculateValueAble;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;
import com.foundercy.pf.reportcy.summary.object.source.RefEnumSource;


public class BuildLastQuerySql {

	private BuildSql buildSql;

	private List lstCells;

	private Map fieldEnameMap;

	private ReportQuerySource querySource;

	// 存放分配表头ename的值
	private Map enameMap;

	// 保存计算列汇总sql,Sum语句
	private String sCalcSql = "";

	// 保存计算列汇总sql,Sum语句(不向上汇总)
	private String sCalcNotSumUpSql = null;

	// 序号
	private String xh = "";

	// 分组列
	private String groupBy = "";

	// 报表计算列类
	private SearchCalcCol reportCalcCol;

	// 计算列是否向上汇总
	private boolean isSumUp;

	private int maxSummaryIndex = 0;

	public BuildLastQuerySql(BuildSql buildSql) {
		this.buildSql = buildSql;
		this.lstCells = buildSql.getLstCells();
		this.fieldEnameMap = buildSql.getFieldEnameMap();
		this.querySource = buildSql.getQuerySource();
		this.enameMap = buildSql.getEnameMap();
	}

	/**
	 * LASTQUERY
	 * 
	 * @param list
	 * @return
	 */
	public Map getLastQuery() {
		// 计算列是否向上汇总
		isSumUp = this.isSumUpCal(this.lstCells);

		reportCalcCol = new SearchCalcCol(buildSql);
		// 得到计算列汇总sql
		sCalcSql = reportCalcCol.getCalc(false, true);
		if (!isSumUp) {
			// 得到计算列汇总sql(未向上汇总）
			sCalcNotSumUpSql = reportCalcCol.getCalc(false, false);
		}

		StringBuffer sSql = new StringBuffer();
		// 是否显示合计行
		String isShowTotalRow = DefinePub.getIsShowTotalRow(querySource);
		// 1. 组织合计行sql
		if (Common.estimate(isShowTotalRow)) {
			sSql.append(getHjSql());
		}

		List lstGroupAble;
		String sAddSql;
		// 得到最大summaryIndex值，循环处理
		maxSummaryIndex = DefinePubOther.getMaxSummaryIndex(lstCells);
		Map groupAbleValueMap;
		for (int i = 1; i <= maxSummaryIndex; i++) {
			groupAbleValueMap = DefinePubOther
					.getGroupAbleValueMap(lstCells, i);
			if (groupAbleValueMap == null)
				continue;
			lstGroupAble = DefinePubOther.convertType(groupAbleValueMap
					.values().toArray());
			if (DefinePubOther.isDetail(lstGroupAble)) {
				// 4. 组织明细信息
				sAddSql = getDetialSql(groupAbleValueMap);
			} else if (DefinePubOther.isLevIsTotal(lstGroupAble)) {
				// 2.得到分组有向上汇总信息
				sAddSql = getLevIsTotalSql(groupAbleValueMap);
			} else {
				// 3. 组织分组信息
				sAddSql = getGroupSql(groupAbleValueMap);
			}
			addLink(sSql, sAddSql);

		}
		return DefinePubOther.getBuildSqlMap(sSql.toString(), "",
				IDefineReport.LAST_QUERY);
	}

	/**
	 * Sql语句拼接
	 * 
	 * @param sSql
	 * @param sAddSql
	 * @return
	 */
	private void addLink(StringBuffer sSql, String sAddSql) {
		if (!Common.isNullStr(sSql.toString()) && !Common.isNullStr(sAddSql)) {
			sSql.append("\n union all\n ");
		}
		if (!Common.isNullStr(sAddSql)) {
			sSql.append(sAddSql);
		}
	}

	/**
	 * 1.组织合计行sql
	 * 
	 * @return
	 */
	private String getHjSql() {
		// 判断是否有计算列,没有计算列不显示合计行，否则会出现显示多个合计行问题
		String sCalcSqlTmp;
		if (isSumUp) {
			sCalcSqlTmp = this.sCalcSql;
		} else {
			sCalcSqlTmp = this.sCalcNotSumUpSql;
		}
		if (Common.isNullStr(sCalcSqlTmp)) {
			return "";
		}

		Object value;
		// 组织合计行
		String sSqlTemp = " ' ' as xh ";
		boolean bNotHjFlag = true;
		int isVisual;
		for (int i = 0; i < lstCells.size(); i++) {
			value = lstCells.get(i);
			if (value == null) {
				sSqlTemp = sSqlTemp + this.addEmptyField(i);
			} else if (value instanceof MyGroupValueImpl) {
				isVisual = ((MyGroupValueImpl) value).getGroupAbleArray()[0]
						.getIsVisual();
				// 未隐藏列
				if (bNotHjFlag && Common.estimate(String.valueOf(isVisual))) {
					sSqlTemp = sSqlTemp + ",'合计'  AS "
							+ enameMap.get(new Integer(i));
					bNotHjFlag = false;
				} else {
					sSqlTemp = sSqlTemp + this.addEmptyField(i);
				}
			} else if (value instanceof MyCalculateValueImpl) {
				continue;
			} else {
				sSqlTemp = sSqlTemp + this.addEmptyField(i);
			}

		}

		if (!Common.isNullStr(sSqlTemp))
			sSqlTemp = sSqlTemp + ",";
		sSqlTemp = "select " + sSqlTemp + sCalcSqlTmp + " from ";
		// 判断是否都不向上汇总
		if (DefinePubOther.isAllNotSumUp(lstCells)) {
			sSqlTemp = sSqlTemp + "dual";
		} else {
			sSqlTemp = sSqlTemp + "{" + IDefineReport.TEMP_TABLE + "}";
		}
		return sSqlTemp;
	}

	/**
	 * 2.组织向上汇总，小计列sql
	 * 
	 * @return
	 */
	private String getLevIsTotalSql(Map groupAbleValueMap) {

		StringBuffer sSql = new StringBuffer();
		// 业务处室汇总
		String sSqlTmp = this.getMbSummarySql(groupAbleValueMap);
		this.addLink(sSql, sSqlTmp);
		// 循环处理向上汇总
		sSqlTmp = this.getLevelSql(groupAbleValueMap);
		this.addLink(sSql, sSqlTmp);
		// 组织末级汇总sql
		sSqlTmp = this.getIsTotalSql(groupAbleValueMap);
		this.addLink(sSql, sSqlTmp);
		return sSql.toString();
	}

	/**
	 * 2.1 业务处室汇总组织Sql
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	private String getMbSummarySql(Map groupAbleValueMap) {
		boolean isMbSummary = DefinePubOther
				.chekcIsMbSummary(groupAbleValueMap);
		if (!isMbSummary)
			return "";

		// 先将序号组组入sql
		String sSqlTmp = xh;
		sSqlTmp = DefinePubOther.addStrLink(sSqlTmp);
		sSqlTmp = sSqlTmp + IDefineReport.DEP_CODE + " as xh ";

		Object value;
		IGroupAble groupAble = null;
		List lstGroupByTmp = null;
		for (int j = 0; j < lstCells.size(); j++) {
			value = lstCells.get(j);
			if (value == null) {
				sSqlTmp = sSqlTmp + this.addEmptyField(j);
			}

			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			value = groupAbleValueMap.get(String.valueOf(j));
			if (value == null) {
				sSqlTmp = sSqlTmp + addEmptyField(j);
				continue;
			}
			groupAble = (IGroupAble) value;
			// 判断是否业务处室汇总
			if (groupAble.getIsMbSummary() != 1) {
				sSqlTmp = sSqlTmp + this.addEmptyField(j);
				continue;
			}

			String sFormula = getFormula(groupAble, null);
			sFormula = sFormula.toLowerCase().replaceAll(
					IDefineReport.DIV_CODE, IDefineReport.DEP_CODE);
			sFormula = sFormula.replaceAll(IDefineReport.DIV_NAME,
					IDefineReport.DEP_NAME);

			if (lstGroupByTmp == null)
				lstGroupByTmp = new ArrayList();
			getFieldWithFormula(lstGroupByTmp, sFormula);

			String sBlank = BuildSqlBlank.getBlankMb(
					(MyGroupValueImpl) lstCells.get(j), groupAble);

			sSqlTmp = sSqlTmp + "," + sBlank + sFormula + " as "
					+ enameMap.get(new Integer(j));

		}

		// 当前SummaryIndex值
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());

		String sCalcSqlTmp;
		// 判断是否有下级统计记录
		if (this.isSumUp) {
			sCalcSqlTmp = sCalcSql;
		} else {
			if (!Common.isNullStr(groupAble.getLevel())
					|| Common.estimate(groupAble.getIsTotal())
					|| curSummaryIndex < maxSummaryIndex) {
				sCalcSqlTmp = sCalcNotSumUpSql;
			} else {
				sCalcSqlTmp = sCalcSql;
			}
		}
		if (!Common.isNullStr(sCalcSqlTmp)) {
			sSqlTmp = sSqlTmp + ",";
		}
		sSqlTmp = "select " + sSqlTmp + sCalcSqlTmp + " from {"
				+ IDefineReport.TEMP_TABLE + "}  "
				+ getGroupByStr(groupBy, lstGroupByTmp);

		// 保存分组的序号和分组
		if (!Common.isNullStr(xh)) {
			xh = xh + "||";
		}
		if (!Common.isNullStr(groupBy)) {
			groupBy = groupBy + ",";
		}
		xh = xh + IDefineReport.DEP_CODE;
		groupBy = groupBy + IDefineReport.DEP_CODE;

		return sSqlTmp;

	}

	/**
	 * 2.2 向上汇总，层次码汇总Sql
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	private String getLevelSql(Map groupAbleValueMap) {

		StringBuffer sSqlBuff = new StringBuffer();
		String sSqlTmp = null;
		Object value;
		List lstGroupByTmp = null;
		String sFormula;
		String sBlank;
		IGroupAble curGroupAble;

		IGroupAble groupAble = this.getLevelField(groupAbleValueMap);
		if (groupAble == null)
			return "";

		// 层次数组
		String[] levelArray = groupAble.getLevel().split(",");
		// 得到ename值
		String sEname = getEname(groupAble.getSourceID(), groupAble
				.getSourceColID(), groupAble);

		// 循环处理向上汇总
		int len = levelArray.length;
		for (int j = 0; j < len; j++) {
			// 先将序号组组入sql
			sSqlTmp = xh;
			sSqlTmp = DefinePubOther.addStrLink(sSqlTmp);
			sSqlTmp = sSqlTmp + sEname + "_" + levelArray[j] + "j" + " as xh ";

			lstGroupByTmp = new ArrayList();
			for (int k = 0; k < lstCells.size(); k++) {
				value = lstCells.get(k);
				if (value == null) {
					sSqlTmp = sSqlTmp + this.addEmptyField(k);
				}

				if (!(value instanceof MyGroupValueImpl)) {
					continue;
				}

				value = groupAbleValueMap.get(String.valueOf(k));
				if (value == null) {
					sSqlTmp = sSqlTmp + this.addEmptyField(k);
					continue;
				}
				curGroupAble = (IGroupAble) value;
				// 判断是否分组汇总
				if (Common.isNullStr(curGroupAble.getLevel())) {
					sSqlTmp = sSqlTmp + this.addEmptyField(k);
					continue;
				}

				sFormula = getFormula(curGroupAble, levelArray[j]);

				// 得到分组列信息

				getFieldWithFormula(lstGroupByTmp, sFormula);

				sBlank = BuildSqlBlank.getBlankLev((MyGroupValueImpl) lstCells
						.get(k), curGroupAble, levelArray[j]);
				sSqlTmp = sSqlTmp + "," + sBlank + sFormula + " as "
						+ enameMap.get(new Integer(k));

			}

			// 当前SummaryIndex值
			int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());
			String sCalcSqlTmp;
			// 判断是否有下级统计记录
			if (this.isSumUp) {
				sCalcSqlTmp = sCalcSql;
			} else {
				if (Common.estimate(groupAble.getIsTotal())
						|| curSummaryIndex < maxSummaryIndex) {
					sCalcSqlTmp = sCalcNotSumUpSql;
				} else {
					sCalcSqlTmp = sCalcSql;
				}
			}

			if (!Common.isNullStr(sCalcSqlTmp)) {
				sSqlTmp = sSqlTmp + ",";
			}

			sSqlTmp = " select " + sSqlTmp + sCalcSqlTmp + " from {"
					+ IDefineReport.TEMP_TABLE + "} where " + sEname + "_"
					+ levelArray[j] + "J is not null "
					+ getGroupByStr(groupBy, lstGroupByTmp);
			this.addLink(sSqlBuff, sSqlTmp);

			// 保存xh,groupby值
			if (!Common.isNullStr(xh)) {
				xh = xh + "||";
			}
			if (!Common.isNullStr(groupBy)) {
				groupBy = groupBy + ",";
			}

			xh = xh + sEname + "_" + levelArray[j] + "j";
			groupBy = groupBy + sEname + "_" + levelArray[j] + "j";

		}
		return sSqlBuff.toString();
	}

	/**
	 * 2.3 组织末级汇总sql
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	private String getIsTotalSql(Map groupAbleValueMap) {
		Object value;
		String sFormula;
		String sBlank = "";
		List lstGroupByTmp = null;

		// 先将序号组组入sql
		String sSqlTmp = xh;
		sSqlTmp = DefinePubOther.addStrLink(sSqlTmp);

		IGroupAble groupAble = this.getIsTotalField(groupAbleValueMap);
		if (groupAble == null)
			return "";
		String summaryIndex = groupAble.getSummaryIndex();
		// 得到ename值
		String sEname = getEname(groupAble.getSourceID(), groupAble
				.getSourceColID(), groupAble);
		sSqlTmp = sSqlTmp + sEname + " as xh";

		for (int j = 0; j < lstCells.size(); j++) {
			value = lstCells.get(j);
			if (value == null) {
				sSqlTmp = sSqlTmp + this.addEmptyField(j);
			}

			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}

			value = groupAbleValueMap.get(String.valueOf(j));
			if (value == null) {
				sSqlTmp = sSqlTmp + this.addEmptyField(j);
				continue;
			}
			groupAble = (IGroupAble) value;
			if (!summaryIndex.equals(groupAble.getSummaryIndex())) {
				continue;
			}

			sFormula = getFormula(groupAble, null);
			if (!Common.isNullStr(groupAble.getIsTotal())
					&& Common.estimate(groupAble.getIsTotal())) {
				// 得到分组列信息
				if (lstGroupByTmp == null)
					lstGroupByTmp = new ArrayList();
				getFieldWithFormula(lstGroupByTmp, sFormula);

				sBlank = BuildSqlBlank.getBlankIsTotal(
						(MyGroupValueImpl) lstCells.get(j), groupAble,
						querySource);
			} else {
				sBlank = BuildSqlBlank.getBlankNumOther(
						(MyGroupValueImpl) lstCells.get(j), groupAble);
				sFormula = "Max(" + sFormula + ")";
			}
			sSqlTmp = sSqlTmp + "," + sBlank + sFormula + " as "
					+ enameMap.get(new Integer(j));
		}

		// 当前SummaryIndex值
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());

		String sCalcSqlTmp;
		// 判断是否有下级统计记录
		if (this.isSumUp || curSummaryIndex == maxSummaryIndex) {
			sCalcSqlTmp = sCalcSql;
		} else {
			sCalcSqlTmp = sCalcNotSumUpSql;
		}

		if (!Common.isNullStr(sCalcSqlTmp)) {
			sSqlTmp = sSqlTmp + ",";
		}
		sSqlTmp = "select " + sSqlTmp + sCalcSqlTmp + " from {"
				+ IDefineReport.TEMP_TABLE + "}  "
				+ getGroupByStr(groupBy, lstGroupByTmp);

		// 保存xh,groupby值
		xh = DefinePubOther.addStrLink(xh);
		groupBy = DefinePubOther.addComma(groupBy);
		xh = xh + sEname;
		groupBy = groupBy + sEname;

		return sSqlTmp;
	}

	/**
	 * 3.组织分组列sql
	 * 
	 * @return
	 */
	private String getGroupSql(Map groupAbleValueMap) {
		String sSqlTemp = "";
		String sFormula;
		// 单元格值
		Object value;
		List lstGroupByTmp = null;

		// 存放排序信息
		List lstOrder = null;
		List lstGroupAble = null;

		IGroupAble groupAble = null;
		String blank;

		// where条件
		String sFilter = "";
		for (int i = 0; i < lstCells.size(); i++) {
			value = lstCells.get(i);
			if (value == null) {
				sSqlTemp = sSqlTemp + this.addEmptyField(i);
			}

			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}

			value = groupAbleValueMap.get(String.valueOf(i));
			if (value == null) {
				sSqlTemp = sSqlTemp + this.addEmptyField(i);
				continue;
			}

			groupAble = (IGroupAble) value;

			sFormula = getFormula(groupAble, null);
			if (lstOrder == null)
				lstOrder = new ArrayList();
			if (lstGroupAble == null)
				lstGroupAble = new ArrayList();
			// 保存排序列信息
			setOrder(lstOrder, lstGroupAble, groupAble);

			if (sFormula == null || Common.isNullStr(sFormula)) {
				sSqlTemp = sSqlTemp + this.addEmptyField(i);
			} else {
				blank = BuildSqlBlank.getBlankNumOther(
						(MyGroupValueImpl) lstCells.get(i), groupAble);
				sSqlTemp = sSqlTemp + "," + blank + sFormula + " as "
						+ enameMap.get(new Integer(i));
				// 得到分组列信息
				if (lstGroupByTmp == null)
					lstGroupByTmp = new ArrayList();
				getFieldWithFormula(lstGroupByTmp, sFormula);

				// NULL值过滤条件
				sFilter = addNullFilter(sFilter, sFormula);
			}

		}

		// 当前SummaryIndex值
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());

		String sCalcSqlTmp;
		// 判断是否都是向上汇总列,分组汇总下面是否有明细列
		if (isSumUp || curSummaryIndex == maxSummaryIndex) {
			sCalcSqlTmp = sCalcSql;
		} else {
			sCalcSqlTmp = sCalcNotSumUpSql;
		}

		// 组织sql
		if (!Common.isNullStr(sCalcSqlTmp)) {
			sSqlTemp = sSqlTemp + ",";
		}
		// 保存分组的序号和分组
		xh = getOrderStr(xh, lstOrder, lstGroupAble, false);
		sSqlTemp = "select " + xh + " as xh " + sSqlTemp + sCalcSqlTmp
				+ " from {" + IDefineReport.TEMP_TABLE + "} ";
		if (!Common.isNullStr(sFilter)) {
			sSqlTemp = sSqlTemp + " where " + sFilter;
		}
		sSqlTemp = sSqlTemp + getGroupByStr(groupBy, lstGroupByTmp);

		// 保存xh,groupby值
		groupBy = DefinePubOther.addComma(groupBy);
		groupBy = groupBy + getGroupByStr_A(lstGroupByTmp);

		return sSqlTemp;
	}

	/**
	 * 4.组织明细列sql
	 * 
	 * @return
	 */
	private String getDetialSql(Map groupAbleValueMap) {
		String sSqlTemp = "";
		// 单元格值
		Object value;
		String sFormula;

		// 存放排序信息
		List lstOrder = null;
		List lstValue = null;

		IGroupAble groupAble = null;
		String blank;
		// where条件
		String sFilter = "";

		// 判断是否需要查询明细
		for (int i = 0; i < lstCells.size(); i++) {
			value = lstCells.get(i);
			if (value == null) {
				sSqlTemp = sSqlTemp + this.addEmptyField(i);
			}

			if (!(value instanceof MyGroupValueImpl)
					&& !(value instanceof MyCalculateValueImpl)) {
				continue;
			}

			value = lstCells.get(i);
			// 判断是否分组列
			if (value instanceof MyGroupValueImpl) {
				value = groupAbleValueMap.get(String.valueOf(i));
				if (value == null) {
					sSqlTemp = sSqlTemp + this.addEmptyField(i);
					continue;
				}

				groupAble = (IGroupAble) value;
				sFormula = getFormula(groupAble, null);

				if (lstOrder == null)
					lstOrder = new ArrayList();
				if (lstValue == null)
					lstValue = new ArrayList();
				// 保存排序列信息
				setOrder(lstOrder, lstValue, groupAble);

				if (sFormula == null) {
					sSqlTemp = sSqlTemp + this.addEmptyField(i);
				} else {
					blank = BuildSqlBlank.getBlankNumOther(
							(MyGroupValueImpl) lstCells.get(i), groupAble);
					sSqlTemp = sSqlTemp + "," + blank + sFormula + " as "
							+ enameMap.get(new Integer(i));

					// NULL值过滤条件
					sFilter = addNullFilter(sFilter, sFormula);
				}
			} else if (value instanceof MyCalculateValueImpl) {
				// 判断是否排序列
				if (Common.isNullStr(((MyCalculateValueImpl) value)
						.getOrderIndex())) {
					continue;
				}
				if (lstOrder == null)
					lstOrder = new ArrayList();
				if (lstValue == null)
					lstValue = new ArrayList();
				// 保存排序列信息
				setOrder(lstOrder, lstValue, value);
			}
		}

		// 当前SummaryIndex值
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());
		// 得到计算列明细sql
		String sCalcSqlDetail;
		// 判断是否都是向上汇总列,判断分组汇总下面是否有明细列
		if (isSumUp || curSummaryIndex == maxSummaryIndex) {
			sCalcSqlDetail = reportCalcCol.getCalc(true, true);
		} else {
			sCalcSqlDetail = reportCalcCol.getCalc(true, false);
		}

		if (!Common.isNullStr(sCalcSqlDetail)) {
			sSqlTemp = sSqlTemp + ",";
		}
		sSqlTemp = " select " + getOrderStr(xh, lstOrder, lstValue, true)
				+ " as xh " + sSqlTemp + sCalcSqlDetail + " from {"
				+ IDefineReport.TEMP_TABLE + "}";

		if (!Common.isNullStr(sFilter)) {
			sSqlTemp = sSqlTemp + " where " + sFilter;
		}

		return sSqlTemp;
	}

	/**
	 * 得到ename值
	 * 
	 * @param sourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @param groupAble
	 * @return
	 */
	private String getEname(String sourceID, String sourceColID,
			IGroupAble groupAble) {
		FunctionRef[] functionRef = groupAble.getFormula()
				.getFunctionRefArray();
		for (int i = 0; i < functionRef.length; i++) {
			if (sourceID.equalsIgnoreCase(groupAble.getSourceID())
					&& sourceColID.equalsIgnoreCase(groupAble.getSourceColID())) {
				return fieldEnameMap.get(functionRef[i].getRefID()).toString();
			}
		}
		return null;
	}

	/**
	 * 组织分组sql
	 * 
	 * @param lstGroup
	 * @return
	 */
	private String getGroupByStr(String groupBy, List lstGroup) {
		String sGroupBy = groupBy;
		if (lstGroup == null) {
			return "";
		} else
			for (int k = 0; k < lstGroup.size(); k++) {
				if (!Common.isNullStr(sGroupBy))
					sGroupBy = sGroupBy + ",";
				sGroupBy = sGroupBy + lstGroup.get(k);
			}
		if (Common.isNullStr(sGroupBy)) {
			return "";
		}
		return " group by " + sGroupBy;
	}

	/**
	 * 组织分组sql
	 * 
	 * @param lstGroup
	 * @return
	 */
	private String getGroupByStr_A(List lstGroup) {
		String sGroupBy = "";
		if (lstGroup == null) {
			return "";
		} else
			for (int k = 0; k < lstGroup.size(); k++) {
				if (!Common.isNullStr(sGroupBy))
					sGroupBy = sGroupBy + ",";
				sGroupBy = sGroupBy + lstGroup.get(k);
			}
		return sGroupBy;
	}

	/**
	 * 根据公式得到字段，用于分组字段
	 * 
	 * @param sFormula
	 * @return
	 */
	private void getFieldWithFormula(List lstGroup, String sFormula) {
		// 判断是否是编码加名称列
		int index = sFormula.indexOf("||");
		if (index >= 0) {
			lstGroup.add(sFormula.substring(0, index));
			lstGroup.add(sFormula.substring(index + 2));
		} else {
			lstGroup.add(sFormula);
		}
	}

	/**
	 * 得到公式
	 * 
	 * @param sourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @param groupAble
	 * @param level节次
	 * @return
	 */
	private String getFormula(IGroupAble groupAble, String level) {
		String sFormula = groupAble.getFormula().getContent().substring(1);
		FunctionRef[] functionRef = groupAble.getFormula()
				.getFunctionRefArray();
		String levelValue = "";
		if (!Common.isNullStr(level)) {
			levelValue = "_" + level + "J";
		}
		for (int i = 0; i < functionRef.length; i++) {
			sFormula = sFormula.replaceAll(functionRef[i].getRefID(),
					fieldEnameMap.get(functionRef[i].getRefID()).toString()
							+ levelValue);
		}
		int index = sFormula.indexOf("+");
		if (index >= 0)
			sFormula = sFormula.substring(0, index) + "||"
					+ sFormula.substring(index + 1);
		return sFormula;
	}

	/**
	 * 得排序字段
	 * 
	 * @param xh序号值
	 * @param lstOrder得到的字段值
	 * @param lstValue值列表
	 * @return
	 */
	private String getOrderStr(String xh, List lstOrder, List lstValue,
			boolean isDetail) {
		String sOrderStr = xh;
		String eNameXh;
		List lstTemp;
		if (lstOrder != null && lstOrder.size() != 0) {
			lstTemp = lstOrder;
		} else {
			lstTemp = lstValue;
		}
		int iCount = lstTemp.size();
		if (iCount == 0)
			return "''";
		Object value;
		IGroupAble groupAble;
		for (int i = 0; i < iCount; i++) {
			value = lstTemp.get(i);
			if (!isDetail && (value instanceof MyCalculateValueImpl))
				continue;

			if (!Common.isNullStr(sOrderStr)) {
				sOrderStr = sOrderStr + "||";
			}

			if (value instanceof IGroupAble) {
				groupAble = (IGroupAble) value;
				eNameXh = getEnameXh(groupAble.getSourceID(), groupAble
						.getSourceColID(), groupAble);
				// sybase库字段没有
				sOrderStr = sOrderStr + "case when " + eNameXh
						+ "='' then 'A' else " + eNameXh + " end";
			} else if (value instanceof MyCalculateValueImpl) {
				String sFormula = reportCalcCol
						.getFormula((MyCalculateValueImpl) value);
				sOrderStr = sOrderStr + "substr('0000000000',length("
						+ sFormula + ")+1)||" + sFormula;
			}
		}
		if (Common.isNullStr(sOrderStr)) {
			sOrderStr = "''";
		}
		return sOrderStr;
	}

	/**
	 * 设置排序
	 * 
	 * @param lstOrder排序列
	 * @param lstValue值列表
	 * @param value
	 */
	private void setOrder(List lstOrder, List lstValue, Object value) {

		// 得到排序顺序号
		String orderIndex = null;
		if (value instanceof IGroupAble) {
			lstValue.add(value);
			orderIndex = ((IGroupAble) value).getOrderIndex();
		} else if (value instanceof MyCalculateValueImpl) {
			orderIndex = ((MyCalculateValueImpl) value).getOrderIndex();
		}
		if (Common.isNullStr(orderIndex)) {
			return;
		}

		if (Integer.parseInt(orderIndex) <= 0) {
			return;
		}

		int orderIndexTmp;
		if (lstOrder.size() == 0) {
			lstOrder.add(value);
		} else {
			for (int k = lstOrder.size() - 1; k >= 0; k--) {
				orderIndexTmp = Integer.parseInt(((IGroupAble) lstOrder.get(k))
						.getOrderIndex());
				if (orderIndexTmp < Integer.parseInt(orderIndex)) {
					lstOrder.add(k + 1, value);
					break;
				} else if (k == 0) {
					lstOrder.add(k, value);
					break;
				}
			}
		}
	}

	/**
	 * 得到ename值（排序xh使用)
	 * 
	 * @param sourceID
	 * @param sourceColID
	 * @param groupAble
	 * @return
	 */
	private String getEnameXh(String sourceID, String sourceColID,
			IGroupAble groupAble) {
		String sResult = groupAble.getFormula().getContent().substring(1);
		FunctionRef[] functionRef = groupAble.getFormula()
				.getFunctionRefArray();
		for (int i = 0; i < functionRef.length; i++) {
			if (sourceID.equalsIgnoreCase(groupAble.getSourceID())
					&& sourceColID.equalsIgnoreCase(groupAble.getSourceColID())) {
				// 功能科目别处理，考虑类、款、项问题
				sResult = sResult
						.replaceAll(functionRef[i].getRefID(), fieldEnameMap
								.get(functionRef[i].getRefID()).toString());

			}
		}
		sResult = sResult.replaceAll("\\+", "||");
		return sResult;
	}

	/**
	 * 是否设置了计算列未向上汇总
	 * 
	 * @return
	 */
	private boolean isSumUpCal(List lstCells) {
		// 单元格值
		Object value;
		ICustomCalculateValueAble calculateValueAble;
		// 判断是否需要查询明细
		for (int i = 0; i < lstCells.size(); i++) {
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			// 判断是否明细列
			if (value instanceof ICustomCalculateValueAble) {
				calculateValueAble = ((ICustomCalculateValueAble) value);
				if (!Common.estimate(calculateValueAble.getIsSumUp())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 得到需向上汇总，小计的列
	 * 
	 * @param list
	 * @return
	 */
	public static List getLevIsTotalField(List lstCells,
			ReportQuerySource querySource) {
		List lstResult = null;
		int summaryIndex;
		int summaryIndexTmp;

		IGroupAble[] groupAble;

		Object value;
		for (int i = 0; i < lstCells.size(); i++) {
			if (lstResult == null)
				lstResult = new ArrayList();
			value = lstCells.get(i);

			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				for (int j = 0; j < groupAble.length; j++) {
					// 判断是否设置了向上汇总或显示末级小计
					// if (groupAble[j].getIsMbSummary() == 0
					// && Common.isNullStr(groupAble[j].getLevel())
					// && !Common.estimate(groupAble[j].getIsTotal())) {
					// continue;
					// }
					// 判断是否枚举主键
					if (!DefinePub.judgetEnumWithColID(querySource,
							groupAble[j].getSourceID(), groupAble[j]
									.getSourceColID(), true))
						continue;

					// 判断向上汇总列是否已增加
					if (isExistLevField(groupAble[j], lstResult, querySource))
						continue;

					summaryIndex = Integer.parseInt(groupAble[j]
							.getSummaryIndex());
					if (lstResult.size() == 0) {
						lstResult.add(groupAble[j]);
					} else {
						for (int k = lstResult.size() - 1; k >= 0; k--) {
							summaryIndexTmp = Integer
									.parseInt(((IGroupAble) lstResult.get(k))
											.getSummaryIndex());
							if (summaryIndexTmp <= summaryIndex) {
								lstResult.add(k + 1, groupAble[j]);
								break;
							} else if (k == 0) {
								lstResult.add(k, groupAble[j]);
								break;
							}
						}
					}
				}
			}
		}
		return lstResult;
	}

	/**
	 * 判断向上汇总列是否已增加，在报表中存在两个相同字段（如单位编码或功能科目编码时）
	 * 
	 * @param groupAble
	 * @param lstReulst
	 * @return
	 */
	private static boolean isExistLevField(IGroupAble groupAble,
			List lstReulst, ReportQuerySource querySource) {

		// 判断是不是对应同一枚举，如对应同一枚举，进行校验
		RefEnumSource enumIDCode = DefinePub.getEnumRefWithColID(querySource,
				groupAble.getSourceID(), groupAble.getSourceColID());
		RefEnumSource enumID;
		IGroupAble groupAbleTmp;
		int size = lstReulst.size();
		for (int i = 0; i < size; i++) {
			groupAbleTmp = (IGroupAble) lstReulst.get(i);
			enumID = DefinePub.getEnumRefWithColID(querySource, groupAbleTmp
					.getSourceID(), groupAbleTmp.getSourceColID());
			if (enumIDCode.getEnumID().equals(enumID.getEnumID())
					&& enumIDCode.getRefEnumColCode().equals(
							enumID.getRefEnumColCode())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 得到层次码列
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	private IGroupAble getLevelField(Map groupAbleValueMap) {
		IGroupAble groupAble;

		Object[] valueArray = groupAbleValueMap.values().toArray();
		int size = valueArray.length;
		for (int i = 0; i < size; i++) {
			groupAble = (IGroupAble) valueArray[i];
			if (Common.isNullStr(groupAble.getLevel())) {
				continue;
			}

			// 判断是否枚举主键
			if (DefinePub.judgetEnumWithColID(querySource, groupAble
					.getSourceID(), groupAble.getSourceColID(), true)) {
				return groupAble;
			}
		}
		return null;
	}

	/**
	 * 得到末级汇总列
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	private IGroupAble getIsTotalField(Map groupAbleValueMap) {
		IGroupAble groupAble;

		Object[] valueArray = groupAbleValueMap.values().toArray();
		int size = valueArray.length;
		for (int i = 0; i < size; i++) {
			groupAble = (IGroupAble) valueArray[i];
			if (!Common.estimate(groupAble.getIsTotal())) {
				continue;
			}

			// 判断是否枚举主键
			if (DefinePub.judgetEnumWithColID(querySource, groupAble
					.getSourceID(), groupAble.getSourceColID(), true)) {
				return groupAble;
			}
		}
		return null;
	}

	/**
	 * 增加空filed
	 * 
	 * @param col
	 * @return
	 */
	private String addEmptyField(int col) {
		return ",''" + " AS " + enameMap.get(new Integer(col));
	}

	/**
	 * null值过滤条件
	 * 
	 * @param sFilter
	 * @param sFormula
	 * @return
	 */
	private String addNullFilter(String sFilter, String sFormula) {
		if (!Common.isNullStr(sFilter))
			sFilter = sFilter + " or ";
		return sFilter + sFormula + " is not null ";
	}

}
