package gov.nbcs.rp.queryreport.qrbudget.bs;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.dictionary.interfaces.IControlDictionary;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * 数据传送公共单元

 * 
 */
public class KitPub {
	/**
	 * 过滤用户对单位
	 * 
	 * @return
	 * @throws Exception
	 */
	static public String vw_fb_filterDiv(String setYear, String sFieldName) {
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil
				.getServerBean("sys.controlDictionaryService");
		String swhere = "";
		try {
			swhere = iCDictionary
					.getSqlElemRight(
							SessionUtil.getUserInfoContext().getUserID(),
							SessionUtil.getUserInfoContext().getRoleID(), "EN",
							"ENTAB").toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
		// swhere = StringUtils
		// .replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
		String sSql = "(select " + sFieldName + " from ele_enterprise ENTAB "
				+ " where  set_year=" + setYear
				+ " and IsBudget = 1 and ENABLED=1 and is_deleted =0 " + swhere
				+ ")";
		return sSql;
	}

	/**
	 * 过滤用户对单位(综合处)
	 * 
	 * @return
	 * @throws Exception
	 */
	static public String vw_fb_filterDiv_ZHC(String setYear, String sFieldName)
			throws Exception {
		// IControlDictionary iCDictionary = (IControlDictionary) SessionUtil
		// .getServerBean("sys.controlDictionaryService");
		// String swhere = iCDictionary.getSqlElemRight(
		// SessionUtil.getUserInfoContext().getUserID(),
		// SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB")
		// .toUpperCase();
		String sSql = "(select " + sFieldName + " from ele_enterprise ENTAB "
				+ " where  set_year=" + setYear + ")";
		return sSql;
	}

	static public String getFilterDiv() throws Exception {
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil
				.getServerBean("sys.controlDictionaryService");
		String swhere = iCDictionary.getSqlElemRight(
				SessionUtil.getUserInfoContext().getUserID(),
				SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB")
				.toUpperCase();
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
		swhere = StringUtils
				.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
		return swhere;
	}

	static public String getFilterDiv_A(String alias) throws Exception {
		alias= alias.toUpperCase();
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil
				.getServerBean("sys.controlDictionaryService");
		String swhere = iCDictionary.getSqlElemRight(
				SessionUtil.getUserInfoContext().getUserID(),
				SessionUtil.getUserInfoContext().getRoleID(), "EN", alias)
				.toUpperCase();
		swhere = StringUtils.replace(swhere, alias + ".CHR_ID", alias
				+ ".EN_ID");
		swhere = StringUtils.replace(swhere, alias + ".CHR_CODE", alias
				+ ".Div_Code");
		return swhere;
	}

}
