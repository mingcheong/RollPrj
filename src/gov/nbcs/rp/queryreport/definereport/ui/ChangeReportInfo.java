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
	 * ����֧���ʽ���Դ
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
		// ���������ʹ�õ���
		List lstSourceID = new ArrayList();
		// �õ��������к�
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// �õ�������
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
		// ��������Դ��ѯ����
		AddDataSourceFilter(querySource, lstSourceID, sFilter);
	}

	/**
	 * ��������Դ��ѯ����,��������ֵΪ��ļ�¼
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
		// �õ���ǰ����
		IParameter[] iParameter = querySource.getParameterArray();
		int iStartCount = iParameter.length;
		SummaryParameterImpl[] summaryParameterImpl = null;
		String chName = null;
		for (int i = 0; i < iStartCount; i++) {
			// �õ�chName����
			chName = iParameter[i].getChName();
			// �ж��Ƿ���Ĭ�ϲ���
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
		// ��ʼ������
		summaryParameterImpl[iStartCount] = new SummaryParameterImpl();
		// id
		summaryParameterImpl[iStartCount].setName(DefinePub.getRandomUUID());
		// chName
		summaryParameterImpl[iStartCount].setChName("F_F");
		// compareType�Ƚ�����
		summaryParameterImpl[iStartCount].setCompareType("");
		// ����ֵ
		summaryParameterImpl[iStartCount].setValue(sFilter);

		summaryParameterImpl[iStartCount].setJoinBefore("and");

		// ����ToSource����
		ToSource toSource[] = new ToSource[size];
		for (int i = 0; i < size; i++) {
			toSource[i] = new ToSource();
			// soureceId ����ԴID
			toSource[i].setSourceID(lstSourceID.get(i).toString());
			// ��ID
			toSource[i].setSourceColID("");
			// �Ƿ�������ö��
			toSource[i].setToEnumSource(false);
		}
		summaryParameterImpl[iStartCount].setToSourceArray(toSource);
		querySource.setParameterArray(summaryParameterImpl);
	}

	/**
	 * �Աȷ���
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
	 * �ı��ж�Ӧ������
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
		// �õ��������к�
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// �õ�������
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
				// �õ���Ӧ�ı�ͷ��Ϣ
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
						throw new Exception("δ��������Դ�Ǳ��껹���������ݱ�ʶ!");
					String sYearFlag = (String) xmlDictMain
							.get(IDataDictBO.YEAR_FLAG);

					if (ReportHeader.COMPARE_1.equals(sCompareFlag)) {// �Ƚ�1
						if (sYearFlag.equals(conditionObj.getOneYear())) {
							continue;
						}

					} else if (ReportHeader.COMPARE_2.equals(sCompareFlag)) { // �Ƚ�2
						if (sYearFlag.equals(conditionObj.getTwoYear())) {
							continue;
						}
					}
					if (xmlDictMain.get(IDataDictBO.COMPARE_DICID) == null) {
						throw new Exception("δ��������Դ�Աȵ�����ԴID!");
					}
					// �ı��ж�Ӧ������Դ
					meaAttr[i].setSourceID((String) xmlDictMain
							.get(IDataDictBO.COMPARE_DICID));
				}
			}
		}
	}

}
