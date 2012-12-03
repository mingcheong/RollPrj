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

	// ��ŷ����ͷename��ֵ
	private Map enameMap;

	// ��������л���sql,Sum���
	private String sCalcSql = "";

	// ��������л���sql,Sum���(�����ϻ���)
	private String sCalcNotSumUpSql = null;

	// ���
	private String xh = "";

	// ������
	private String groupBy = "";

	// �����������
	private SearchCalcCol reportCalcCol;

	// �������Ƿ����ϻ���
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
		// �������Ƿ����ϻ���
		isSumUp = this.isSumUpCal(this.lstCells);

		reportCalcCol = new SearchCalcCol(buildSql);
		// �õ������л���sql
		sCalcSql = reportCalcCol.getCalc(false, true);
		if (!isSumUp) {
			// �õ������л���sql(δ���ϻ��ܣ�
			sCalcNotSumUpSql = reportCalcCol.getCalc(false, false);
		}

		StringBuffer sSql = new StringBuffer();
		// �Ƿ���ʾ�ϼ���
		String isShowTotalRow = DefinePub.getIsShowTotalRow(querySource);
		// 1. ��֯�ϼ���sql
		if (Common.estimate(isShowTotalRow)) {
			sSql.append(getHjSql());
		}

		List lstGroupAble;
		String sAddSql;
		// �õ����summaryIndexֵ��ѭ������
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
				// 4. ��֯��ϸ��Ϣ
				sAddSql = getDetialSql(groupAbleValueMap);
			} else if (DefinePubOther.isLevIsTotal(lstGroupAble)) {
				// 2.�õ����������ϻ�����Ϣ
				sAddSql = getLevIsTotalSql(groupAbleValueMap);
			} else {
				// 3. ��֯������Ϣ
				sAddSql = getGroupSql(groupAbleValueMap);
			}
			addLink(sSql, sAddSql);

		}
		return DefinePubOther.getBuildSqlMap(sSql.toString(), "",
				IDefineReport.LAST_QUERY);
	}

	/**
	 * Sql���ƴ��
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
	 * 1.��֯�ϼ���sql
	 * 
	 * @return
	 */
	private String getHjSql() {
		// �ж��Ƿ��м�����,û�м����в���ʾ�ϼ��У�����������ʾ����ϼ�������
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
		// ��֯�ϼ���
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
				// δ������
				if (bNotHjFlag && Common.estimate(String.valueOf(isVisual))) {
					sSqlTemp = sSqlTemp + ",'�ϼ�'  AS "
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
		// �ж��Ƿ񶼲����ϻ���
		if (DefinePubOther.isAllNotSumUp(lstCells)) {
			sSqlTemp = sSqlTemp + "dual";
		} else {
			sSqlTemp = sSqlTemp + "{" + IDefineReport.TEMP_TABLE + "}";
		}
		return sSqlTemp;
	}

	/**
	 * 2.��֯���ϻ��ܣ�С����sql
	 * 
	 * @return
	 */
	private String getLevIsTotalSql(Map groupAbleValueMap) {

		StringBuffer sSql = new StringBuffer();
		// ҵ���һ���
		String sSqlTmp = this.getMbSummarySql(groupAbleValueMap);
		this.addLink(sSql, sSqlTmp);
		// ѭ���������ϻ���
		sSqlTmp = this.getLevelSql(groupAbleValueMap);
		this.addLink(sSql, sSqlTmp);
		// ��֯ĩ������sql
		sSqlTmp = this.getIsTotalSql(groupAbleValueMap);
		this.addLink(sSql, sSqlTmp);
		return sSql.toString();
	}

	/**
	 * 2.1 ҵ���һ�����֯Sql
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	private String getMbSummarySql(Map groupAbleValueMap) {
		boolean isMbSummary = DefinePubOther
				.chekcIsMbSummary(groupAbleValueMap);
		if (!isMbSummary)
			return "";

		// �Ƚ����������sql
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
			// �ж��Ƿ�ҵ���һ���
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

		// ��ǰSummaryIndexֵ
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());

		String sCalcSqlTmp;
		// �ж��Ƿ����¼�ͳ�Ƽ�¼
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

		// ����������źͷ���
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
	 * 2.2 ���ϻ��ܣ���������Sql
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

		// �������
		String[] levelArray = groupAble.getLevel().split(",");
		// �õ�enameֵ
		String sEname = getEname(groupAble.getSourceID(), groupAble
				.getSourceColID(), groupAble);

		// ѭ���������ϻ���
		int len = levelArray.length;
		for (int j = 0; j < len; j++) {
			// �Ƚ����������sql
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
				// �ж��Ƿ�������
				if (Common.isNullStr(curGroupAble.getLevel())) {
					sSqlTmp = sSqlTmp + this.addEmptyField(k);
					continue;
				}

				sFormula = getFormula(curGroupAble, levelArray[j]);

				// �õ���������Ϣ

				getFieldWithFormula(lstGroupByTmp, sFormula);

				sBlank = BuildSqlBlank.getBlankLev((MyGroupValueImpl) lstCells
						.get(k), curGroupAble, levelArray[j]);
				sSqlTmp = sSqlTmp + "," + sBlank + sFormula + " as "
						+ enameMap.get(new Integer(k));

			}

			// ��ǰSummaryIndexֵ
			int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());
			String sCalcSqlTmp;
			// �ж��Ƿ����¼�ͳ�Ƽ�¼
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

			// ����xh,groupbyֵ
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
	 * 2.3 ��֯ĩ������sql
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	private String getIsTotalSql(Map groupAbleValueMap) {
		Object value;
		String sFormula;
		String sBlank = "";
		List lstGroupByTmp = null;

		// �Ƚ����������sql
		String sSqlTmp = xh;
		sSqlTmp = DefinePubOther.addStrLink(sSqlTmp);

		IGroupAble groupAble = this.getIsTotalField(groupAbleValueMap);
		if (groupAble == null)
			return "";
		String summaryIndex = groupAble.getSummaryIndex();
		// �õ�enameֵ
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
				// �õ���������Ϣ
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

		// ��ǰSummaryIndexֵ
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());

		String sCalcSqlTmp;
		// �ж��Ƿ����¼�ͳ�Ƽ�¼
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

		// ����xh,groupbyֵ
		xh = DefinePubOther.addStrLink(xh);
		groupBy = DefinePubOther.addComma(groupBy);
		xh = xh + sEname;
		groupBy = groupBy + sEname;

		return sSqlTmp;
	}

	/**
	 * 3.��֯������sql
	 * 
	 * @return
	 */
	private String getGroupSql(Map groupAbleValueMap) {
		String sSqlTemp = "";
		String sFormula;
		// ��Ԫ��ֵ
		Object value;
		List lstGroupByTmp = null;

		// ���������Ϣ
		List lstOrder = null;
		List lstGroupAble = null;

		IGroupAble groupAble = null;
		String blank;

		// where����
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
			// ������������Ϣ
			setOrder(lstOrder, lstGroupAble, groupAble);

			if (sFormula == null || Common.isNullStr(sFormula)) {
				sSqlTemp = sSqlTemp + this.addEmptyField(i);
			} else {
				blank = BuildSqlBlank.getBlankNumOther(
						(MyGroupValueImpl) lstCells.get(i), groupAble);
				sSqlTemp = sSqlTemp + "," + blank + sFormula + " as "
						+ enameMap.get(new Integer(i));
				// �õ���������Ϣ
				if (lstGroupByTmp == null)
					lstGroupByTmp = new ArrayList();
				getFieldWithFormula(lstGroupByTmp, sFormula);

				// NULLֵ��������
				sFilter = addNullFilter(sFilter, sFormula);
			}

		}

		// ��ǰSummaryIndexֵ
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());

		String sCalcSqlTmp;
		// �ж��Ƿ������ϻ�����,������������Ƿ�����ϸ��
		if (isSumUp || curSummaryIndex == maxSummaryIndex) {
			sCalcSqlTmp = sCalcSql;
		} else {
			sCalcSqlTmp = sCalcNotSumUpSql;
		}

		// ��֯sql
		if (!Common.isNullStr(sCalcSqlTmp)) {
			sSqlTemp = sSqlTemp + ",";
		}
		// ����������źͷ���
		xh = getOrderStr(xh, lstOrder, lstGroupAble, false);
		sSqlTemp = "select " + xh + " as xh " + sSqlTemp + sCalcSqlTmp
				+ " from {" + IDefineReport.TEMP_TABLE + "} ";
		if (!Common.isNullStr(sFilter)) {
			sSqlTemp = sSqlTemp + " where " + sFilter;
		}
		sSqlTemp = sSqlTemp + getGroupByStr(groupBy, lstGroupByTmp);

		// ����xh,groupbyֵ
		groupBy = DefinePubOther.addComma(groupBy);
		groupBy = groupBy + getGroupByStr_A(lstGroupByTmp);

		return sSqlTemp;
	}

	/**
	 * 4.��֯��ϸ��sql
	 * 
	 * @return
	 */
	private String getDetialSql(Map groupAbleValueMap) {
		String sSqlTemp = "";
		// ��Ԫ��ֵ
		Object value;
		String sFormula;

		// ���������Ϣ
		List lstOrder = null;
		List lstValue = null;

		IGroupAble groupAble = null;
		String blank;
		// where����
		String sFilter = "";

		// �ж��Ƿ���Ҫ��ѯ��ϸ
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
			// �ж��Ƿ������
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
				// ������������Ϣ
				setOrder(lstOrder, lstValue, groupAble);

				if (sFormula == null) {
					sSqlTemp = sSqlTemp + this.addEmptyField(i);
				} else {
					blank = BuildSqlBlank.getBlankNumOther(
							(MyGroupValueImpl) lstCells.get(i), groupAble);
					sSqlTemp = sSqlTemp + "," + blank + sFormula + " as "
							+ enameMap.get(new Integer(i));

					// NULLֵ��������
					sFilter = addNullFilter(sFilter, sFormula);
				}
			} else if (value instanceof MyCalculateValueImpl) {
				// �ж��Ƿ�������
				if (Common.isNullStr(((MyCalculateValueImpl) value)
						.getOrderIndex())) {
					continue;
				}
				if (lstOrder == null)
					lstOrder = new ArrayList();
				if (lstValue == null)
					lstValue = new ArrayList();
				// ������������Ϣ
				setOrder(lstOrder, lstValue, value);
			}
		}

		// ��ǰSummaryIndexֵ
		int curSummaryIndex = Integer.parseInt(groupAble.getSummaryIndex());
		// �õ���������ϸsql
		String sCalcSqlDetail;
		// �ж��Ƿ������ϻ�����,�жϷ�����������Ƿ�����ϸ��
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
	 * �õ�enameֵ
	 * 
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ��ID
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
	 * ��֯����sql
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
	 * ��֯����sql
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
	 * ���ݹ�ʽ�õ��ֶΣ����ڷ����ֶ�
	 * 
	 * @param sFormula
	 * @return
	 */
	private void getFieldWithFormula(List lstGroup, String sFormula) {
		// �ж��Ƿ��Ǳ����������
		int index = sFormula.indexOf("||");
		if (index >= 0) {
			lstGroup.add(sFormula.substring(0, index));
			lstGroup.add(sFormula.substring(index + 2));
		} else {
			lstGroup.add(sFormula);
		}
	}

	/**
	 * �õ���ʽ
	 * 
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ��ID
	 * @param groupAble
	 * @param level�ڴ�
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
	 * �������ֶ�
	 * 
	 * @param xh���ֵ
	 * @param lstOrder�õ����ֶ�ֵ
	 * @param lstValueֵ�б�
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
				// sybase���ֶ�û��
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
	 * ��������
	 * 
	 * @param lstOrder������
	 * @param lstValueֵ�б�
	 * @param value
	 */
	private void setOrder(List lstOrder, List lstValue, Object value) {

		// �õ�����˳���
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
	 * �õ�enameֵ������xhʹ��)
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
				// ���ܿ�Ŀ���������ࡢ�������
				sResult = sResult
						.replaceAll(functionRef[i].getRefID(), fieldEnameMap
								.get(functionRef[i].getRefID()).toString());

			}
		}
		sResult = sResult.replaceAll("\\+", "||");
		return sResult;
	}

	/**
	 * �Ƿ������˼�����δ���ϻ���
	 * 
	 * @return
	 */
	private boolean isSumUpCal(List lstCells) {
		// ��Ԫ��ֵ
		Object value;
		ICustomCalculateValueAble calculateValueAble;
		// �ж��Ƿ���Ҫ��ѯ��ϸ
		for (int i = 0; i < lstCells.size(); i++) {
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			// �ж��Ƿ���ϸ��
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
	 * �õ������ϻ��ܣ�С�Ƶ���
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
					// �ж��Ƿ����������ϻ��ܻ���ʾĩ��С��
					// if (groupAble[j].getIsMbSummary() == 0
					// && Common.isNullStr(groupAble[j].getLevel())
					// && !Common.estimate(groupAble[j].getIsTotal())) {
					// continue;
					// }
					// �ж��Ƿ�ö������
					if (!DefinePub.judgetEnumWithColID(querySource,
							groupAble[j].getSourceID(), groupAble[j]
									.getSourceColID(), true))
						continue;

					// �ж����ϻ������Ƿ�������
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
	 * �ж����ϻ������Ƿ������ӣ��ڱ����д���������ͬ�ֶΣ��絥λ������ܿ�Ŀ����ʱ��
	 * 
	 * @param groupAble
	 * @param lstReulst
	 * @return
	 */
	private static boolean isExistLevField(IGroupAble groupAble,
			List lstReulst, ReportQuerySource querySource) {

		// �ж��ǲ��Ƕ�Ӧͬһö�٣����Ӧͬһö�٣�����У��
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
	 * �õ��������
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

			// �ж��Ƿ�ö������
			if (DefinePub.judgetEnumWithColID(querySource, groupAble
					.getSourceID(), groupAble.getSourceColID(), true)) {
				return groupAble;
			}
		}
		return null;
	}

	/**
	 * �õ�ĩ��������
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

			// �ж��Ƿ�ö������
			if (DefinePub.judgetEnumWithColID(querySource, groupAble
					.getSourceID(), groupAble.getSourceColID(), true)) {
				return groupAble;
			}
		}
		return null;
	}

	/**
	 * ���ӿ�filed
	 * 
	 * @param col
	 * @return
	 */
	private String addEmptyField(int col) {
		return ",''" + " AS " + enameMap.get(new Integer(col));
	}

	/**
	 * nullֵ��������
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
