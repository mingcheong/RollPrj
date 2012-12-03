/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;

/**
 * <p>
 * Title:�ı��ѯ���ͷ��ʾ����
 * </p>
 * <p>
 * Description:�ı��ѯ���ͷ��ʾ����

 */
public class ReportHeaderOpe {

	/**
	 * �ı��ʽ���Դ����ͷ��ʾ���ݸı�
	 * 
	 * @param dsHeader
	 *            ��ͷdataset
	 * @param sFundSourceName�ʽ���Դ����
	 * @throws Exception
	 */
	public static DataSet changeDataSource(DataSet dsReportHeader,
			String sFundSourceName) throws Exception {
		dsReportHeader.beforeFirst();
		dsReportHeader.edit();
		while (dsReportHeader.next()) {
			// �ж��Ƿ���Ҫ�ı��ʽ���Դ�ĵ�Ԫ��
			if (Common.estimate(dsReportHeader.fieldByName(
					IBesQryReport.FUNDSOURCE_FLAG).getValue())) {
				dsReportHeader.fieldByName(IBesQryReport.FIELD_CNAME).setValue(
						sFundSourceName);
			}
		}
		dsReportHeader.applyUpdate();
		return dsReportHeader;
	}

	/**
	 * �Աȷ���,��ͷ��ʾ���ݸı�
	 * 
	 * @param dsReportHeader
	 * @param sFieldFName
	 * @param sCompareFlag
	 * @return
	 * @throws Exception
	 */
	public static DataSet compareReport(DataSet dsReportHeader,
			String sFieldFName, String sCompareFlag) throws Exception {
		dsReportHeader.beforeFirst();
		dsReportHeader.edit();
		while (dsReportHeader.next()) {
			// �ж��Ƿ���Ҫ�ı��ʽ���Դ�ĵ�Ԫ��
			if (sCompareFlag.equalsIgnoreCase(dsReportHeader.fieldByName(
					IBesQryReport.COMPARE_FLAG).getString())) {
				dsReportHeader.fieldByName(IBesQryReport.FIELD_CNAME).setValue(
						sFieldFName);
			}
		}
		dsReportHeader.applyUpdate();
		return dsReportHeader;

	}

}
