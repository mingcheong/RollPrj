/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;

/**
 * <p>
 * Title:改变查询表表头显示内容
 * </p>
 * <p>
 * Description:改变查询表表头显示内容

 */
public class ReportHeaderOpe {

	/**
	 * 改变资金来源，表头显示内容改变
	 * 
	 * @param dsHeader
	 *            表头dataset
	 * @param sFundSourceName资金来源名称
	 * @throws Exception
	 */
	public static DataSet changeDataSource(DataSet dsReportHeader,
			String sFundSourceName) throws Exception {
		dsReportHeader.beforeFirst();
		dsReportHeader.edit();
		while (dsReportHeader.next()) {
			// 判断是否需要改变资金来源的单元格
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
	 * 对比分析,表头显示内容改变
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
			// 判断是否需要改变资金来源的单元格
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
