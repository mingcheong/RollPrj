/**
 * Copyright zjyq 版权所有
 * 
 * 部门预算编审系统
 * 
 * @title 
 * 
 * @author qzcun
 * 
 * @version 1.0
 */
package gov.nbcs.rp.queryreport.batchreport.bs;

import java.util.List;

import gov.nbcs.rp.queryreport.batchreport.ibs.IBatchReport;
import com.foundercy.pf.dictionary.interfaces.IControlDictionary;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * The Class BatchReportBO.
 * 
 * @author Administrator
 */
public class BatchReportBO implements IBatchReport {
	private GeneralDAO dao;

	/**
	 * @return the dao
	 */
	public GeneralDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seegov.nbcs.rp.queryreport.batchreport.ibs.IBatchReport#
	 * getReportList()
	 */
	public List getReportList(String reportIds) {
		List lstReport = dao
				.findBySql("select t.report_id, t.report_name, "
						+ " t.report_id chr_id, t.report_id chr_code, t.report_name chr_name"
						+ " from reportcy_manager t"
						+ (reportIds != null && reportIds.length() > 0 ? " where report_id in ("
								+ reportIds + ")"
								: "") + " order by t.report_id");
		return lstReport;
	}

	public List getEnList() {
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil
				.getServerBean("sys.controlDictionaryService");
		try {
			String swhere = iCDictionary
					.getSqlElemRight(
							SessionUtil.getUserInfoContext().getUserID(),
							SessionUtil.getUserInfoContext().getRoleID(), "EN",
							"ENTAB").toUpperCase();
			List lstEn = dao
					.findBySql("select chr_id, chr_name, chr_code, parent_id"
							+ " from ele_enterprise ENTAB where set_year="
							+ SessionUtil.getLoginYear() + swhere
							+ " order by chr_code");
			return lstEn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
