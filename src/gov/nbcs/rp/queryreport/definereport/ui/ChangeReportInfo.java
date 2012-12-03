/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.List;


import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryParameterImpl;
import com.foundercy.pf.reportcy.summary.object.base.ToSource;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.report.GroupReport;


public class ChangeReportInfo {

	/**
	 * 更换支出资金来源
	 * 
	 * @param sFieldEname
	 */
	static public void changetDataSourceField(GroupReport groupReport,
			ReportQuerySource querySource, List lstFieldEname) {
		if (lstFieldEname == null || lstFieldEname.size() != 2)
			return;
		String sFieldEname = (String) lstFieldEname.get(0);
		if (Common.isNullStr(sFieldEname)) {
			return;
		}
		// 保存计算列使用到的
		List lstSourceID = new ArrayList();
		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
		int col = groupReport.getColumnCount();
		List lstReulst = new ArrayList();
		Object value;
		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value == null) {
				lstReulst.add(null);
				continue;
			}
			if (value instanceof MyCalculateValueImpl) {
				IMeasureAttr[] meaAttr = ((MyCalculateValueImpl) value)
						.getMeasureArray();
				for (int j = 0; j < meaAttr.length; j++) {
					meaAttr[j].setSourceColID(sFieldEname);
					//
					if (!lstSourceID.contains(meaAttr[j].getSourceID())) {
						lstSourceID.add(meaAttr[j].getSourceID());
					}
				}
			}
		}
		String sFilter = (String) lstFieldEname.get(1);
		// 增加数据源查询条件
		AddDataSourceFilter(querySource, lstSourceID, sFilter);
	}

	/**
	 * 增加数据源查询条件,过滤数据值为零的记录
	 * 
	 * @param querySource
	 * @param definePub
	 * @param lstSourceID
	 */
	private static void AddDataSourceFilter(ReportQuerySource querySource,
			List lstSourceID, String sFilter) {
		int size = lstSourceID.size();
		if (size == 0)
			return;
		// 得到当前参数
		IParameter[] iParameter = querySource.getParameterArray();
		int iStartCount = iParameter.length;
		SummaryParameterImpl[] summaryParameterImpl = null;
		String chName = null;
		for (int i = 0; i < iStartCount; i++) {
			// 得到chName名称
			chName = iParameter[i].getChName();
			// 判断是否是默认参数
			if ("F_F".equals(chName)) {
				continue;
			}
			summaryParameterImpl = ReportGuideUI.addArrayLength(
					summaryParameterImpl, 1);
			summaryParameterImpl[summaryParameterImpl.length - 1] = (SummaryParameterImpl) iParameter[i];
		}

		summaryParameterImpl = ReportGuideUI.addArrayLength(
				summaryParameterImpl, 1);
		iStartCount = summaryParameterImpl.length - 1;
		// 初始化参数
		summaryParameterImpl[iStartCount] = new SummaryParameterImpl();
		// id
		summaryParameterImpl[iStartCount].setName(DefinePub.getRandomUUID());
		// chName
		summaryParameterImpl[iStartCount].setChName("F_F");
		// compareType比较类型
		summaryParameterImpl[iStartCount].setCompareType("");
		// 参数值
		summaryParameterImpl[iStartCount].setValue(sFilter);

		summaryParameterImpl[iStartCount].setJoinBefore("and");

		// 定义ToSource对象
		ToSource toSource[] = new ToSource[size];
		for (int i = 0; i < size; i++) {
			toSource[i] = new ToSource();
			// soureceId 数据源ID
			toSource[i].setSourceID(lstSourceID.get(i).toString());
			// 列ID
			toSource[i].setSourceColID("");
			// 是否作用于枚举
			toSource[i].setToEnumSource(false);
		}
		summaryParameterImpl[iStartCount].setToSourceArray(toSource);
		querySource.setParameterArray(summaryParameterImpl);
	}

	/**
	 * 对比分析
	 * 
	 * @param conditionObj
	 * @throws Exception
	 */
	static public void compareDataSource(GroupReport groupReport,
			ReportQuerySource querySource, ConditionObj conditionObj)
			throws Exception {
		changeColFilter(groupReport, querySource, conditionObj);
	}

	/**
	 * 改变列对应的条件
	 * 
	 * @param groupReport
	 * @param querySource
	 * @param definePub
	 * @param conditionObj
	 * @throws Exception
	 */
	private static void changeColFilter(GroupReport groupReport,
			ReportQuerySource querySource, ConditionObj conditionObj)
			throws Exception {
//		IDataDictBO dataDictBO = (IDataDictBO) BOCache
//		.getBO("fb.dataDictService");
		IDataDictBO dataDictBO = null;
		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
		int col = groupReport.getColumnCount();
		List lstReulst = new ArrayList();
		Object value;
		Object valueHeader;
		XMLData xmlDictMain;
		String sCompareFlag;

		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value == null) {
				lstReulst.add(null);
				continue;
			}
			if (value instanceof MyCalculateValueImpl) {
				// 得到对应的表头信息
				valueHeader = groupReport.getCellValue(i, rowOpe - 1);
				if (valueHeader instanceof FundSourceImpl) {
					sCompareFlag = ((FundSourceImpl) valueHeader)
							.getCompareFlag();
				} else {
					continue;
				}

				IMeasureAttr[] meaAttr = ((MyCalculateValueImpl) value)
						.getMeasureArray();
				for (int j = 0; j < meaAttr.length; j++) {
					xmlDictMain = dataDictBO.getTableMainInfo(meaAttr[i]
							.getSourceID(), Global.loginYear);
					if (xmlDictMain.get(IDataDictBO.YEAR_FLAG) == null)
						throw new Exception("未设置数据源是本年还是上年数据标识!");
					String sYearFlag = (String) xmlDictMain
							.get(IDataDictBO.YEAR_FLAG);

					if (ReportHeader.COMPARE_1.equals(sCompareFlag)) {// 比较1
						if (sYearFlag.equals(conditionObj.getOneYear())) {
							continue;
						}

					} else if (ReportHeader.COMPARE_2.equals(sCompareFlag)) { // 比较2
						if (sYearFlag.equals(conditionObj.getTwoYear())) {
							continue;
						}
					}
					if (xmlDictMain.get(IDataDictBO.COMPARE_DICID) == null) {
						throw new Exception("未设置数据源对比的数据源ID!");
					}
					// 改变列对应的数据源
					meaAttr[i].setSourceID((String) xmlDictMain
							.get(IDataDictBO.COMPARE_DICID));
				}
			}
		}
	}

}
