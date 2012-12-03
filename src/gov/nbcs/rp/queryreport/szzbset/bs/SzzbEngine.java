/**
 * @# SzzbEngine.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.szzbset.bs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

//import gov.nbcs.rp.basinfo.common.baseDataUtil.PubDataCache;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.FormulaTool;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.queryreport.szzbset.ui.CellFieldInfo;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.fr.cell.CellSelection;

/**
 * ����˵��:��֧�ܱ��ѯ�����࣬���������SQL����ѯ������һ��XMLData�����ݣ������ǲ�ѯ����ֵ
 * ע���ص�ֵ��'F'+col+'_'+rowΪ����ֵΪֵ
 * <P>
 * ��ΪCELL��col:row�������XMLData�ĸ�ʽΪ��CELL��col:row ,ֵΪCellFieldInfo����
 * <P>
 * һ��ֻ�ں�̨ʹ�� Copyright
 * <P>
 * All rights reserved.
 * <P>
 * ��Ȩ���У��㽭����
 * <P>
 * δ������˾��ɣ��������κη�ʽ���ƻ�ʹ�ñ������κβ��֣�
 * <P>
 * ��Ȩ�߽��ܵ�����׷����
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-3-21
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class SzzbEngine {
	private XMLData xmlForCells;

	private String sqlWhere;

	private GeneralDAO dao;

	public static final String TABLE_ZC = "vw_fb_u_payout_budget ";

	public static final String TABLE_SR = " vw_fb_div_incoming_budget ";

	public static final String ZCTYPE_BASE = "0";// ����֧��

	public static final String ZCTYPE_PRO = "150";// ��Ŀ֧��

	public static final String SR_MONEY_FIELD = "INC_MONEY";

	public static final String SR_CODE_FIELD = "INCTYPE_CODE";

	private List lstSr = new ArrayList();// ������

	private List lstZc = new ArrayList();// ֧��������

	private List lstFormula = new ArrayList();// ��ʽ��

	private List lstSql = new ArrayList();// ��ѯ��

	public SzzbEngine(XMLData xmlForCell, GeneralDAO dao) {

		this.xmlForCells = xmlForCell;

		this.dao = dao;
		seperateFields();
	}

	private void seperateFields() {
		if (xmlForCells == null)
			return;
		Iterator it = xmlForCells.values().iterator();
		while (it.hasNext()) {
			CellFieldInfo cellField = (CellFieldInfo) it.next();
			switch (cellField.getSourceType()) {
			case CellFieldInfo.SOURCETYPE_ZC:
				lstZc.add(cellField);

				break;

			case CellFieldInfo.SOURCETYPE_SR:
				lstSr.add(cellField);
				break;

			case CellFieldInfo.SOURCETYPE_FOR:
				lstFormula.add(cellField);
				break;

			case CellFieldInfo.SOURCETYPE_SQL:
				lstSql.add(cellField);
				break;

			}
		}
	}

	/**
	 * ע���ص�ֵ��'F'+col+'_'+row ����֧�����ֵ ȡ��֧��������
	 * 
	 * @return
	 */
	private XMLData getZc(boolean isConvert) {
		int iCount = lstZc.size();
		if (iCount < 1)
			return new XMLData();
		CellFieldInfo aField;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select ");
		for (int i = 0; i < iCount; i++) {
			aField = (CellFieldInfo) lstZc.get(i);
			sbSql.append(getZcCase(aField,isConvert) + ",");

		}
		sbSql.delete(sbSql.length() - 1, sbSql.length());
		sbSql.append("  from ").append(TABLE_ZC).append(" where 1=1 ").append(
				this.sqlWhere);
		if (Common.RUNTIME_DEBUG)
			System.out.println(sbSql);
//		List lstData = PubDataCache.convertToXMLList(dao.findBySqlByUpper(sbSql
//				.toString()));
		List lstData = null;
		return getValueFromResult(lstData);

	}

	// ȡ��֧����CASE���
	private String getZcCase(CellFieldInfo aField, boolean isConvert) {
		StringBuffer sbCase = new StringBuffer();
		String[] arrCode = null;
		String sCodeField = null;
		if (!Common.isNullStr(aField.getAcctCode())) {// ���ܿ�Ŀ�ֽ�
			arrCode = aField.getAcctCode().split(";");
			sCodeField = IPubInterface.ACCT_CODE;
		} else if (!Common.isNullStr(aField.getAcctJJ())) {// ���ÿ�Ŀ
			arrCode = aField.getAcctJJ().split(";");
			sCodeField = IPubInterface.ACCT_CODE_JJ;
		}

		sbCase.append("sum( case  when (");

		if (!Common.isNullStr(sCodeField)) {
			sbCase.append("(");
			int iCount = arrCode.length;
			for (int i = 0; i < iCount; i++) {
				sbCase.append(sCodeField).append("  like '").append(arrCode[i])
						.append("%' or ");
			}
			String sZcType = "";// ֧������
			// modified by xxl ������ǹ��ܿ�Ŀ���Ҳ���ȫ����ͬ�����֧�����
			if (Common.isNullStr(aField.getAcctCode())
					&& !String.valueOf(aField.getZcType()).equals(
							CellFieldInfo.ZCTYPE_ALL)) {
				// ����֧����������֧������0��50
				if (String.valueOf(aField.getZcType()).equals(
						CellFieldInfo.ZCTYPE_BASE))
					sZcType = " and  (Data_src=" + aField.getZcType()
							+ " or Data_src=" + CellFieldInfo.ZCTYPE_BASE2
							+ ")";
				else
					// ��Ŀ֧��
					sZcType = " and  Data_src=" + aField.getZcType();
			}
			sbCase.delete(sbCase.length() - 3, sbCase.length());// ɾ��OR

			sbCase.append(")   ").append(sZcType);
		}

		// ֧����Ŀ���
		if (!Common.isNullStr(aField.getPayoutKind())) {
			if (!Common.isNullStr(sCodeField)) {
				sbCase.append(" and ");
			}
			String payoutKindCode = aField.getPayoutKindForSql();
			sbCase.append(" (Payout_Kind_Code in (" + payoutKindCode + "))");
		}

		sbCase.append(") then ").append(aField.getFieldNameForSql()).append(
				" end )");
		//���ת��
		if (isConvert) {
			sbCase.append("/10000");
		}

		sbCase.append(" as ").append("F").append(aField.getCellCol()).append(
				"_").append(aField.getCellRow());
		return sbCase.toString();
	}

	private XMLData getValueFromResult(List lstData) {

		if (lstData == null || lstData.size() < 1)
			return new XMLData();
		return (XMLData) lstData.get(0);// ��ѯ����ֵ
	}

	// ȡ��������ֵ
	private XMLData getSr(boolean isConvert) {
		int iCount = lstSr.size();
		if (iCount < 1)
			return new XMLData();
		CellFieldInfo aField;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select ");
		for (int i = 0; i < iCount; i++) {
			aField = (CellFieldInfo) lstSr.get(i);
			sbSql.append(getSrCase(aField,isConvert) + ",");

		}
		sbSql.delete(sbSql.length() - 1, sbSql.length());
		sbSql.append("  from ").append(TABLE_SR).append(" where 1=1 ").append(
				this.sqlWhere);
		if (Common.RUNTIME_DEBUG)
			System.out.println(sbSql);
//		List lstData = PubDataCache.convertToXMLList(dao.findBySqlByUpper(sbSql
//				.toString()));
		List lstData = null;
		return getValueFromResult(lstData);
	}

	/**
	 * ȡ������Case
	 * 
	 * @param aField
	 * @return
	 */
	public String getSrCase(CellFieldInfo aField,boolean isConvert) {
		String sSrCode = aField.getIncType();
		String arrCode[] = sSrCode.split(";");
		StringBuffer sbSr = new StringBuffer();
		int iCount = arrCode.length;
		sbSr.append("sum( case  when (");
		for (int i = 0; i < iCount; i++) {
			sbSr.append(SR_CODE_FIELD).append("  like '").append(arrCode[i])
					.append("%' or ");
		}
		sbSr.delete(sbSr.length() - 3, sbSr.length());// ɾ��OR
		sbSr.append(")   then ").append(SR_MONEY_FIELD).append(" end )");
				
		//���ת��
		if (isConvert) {
			sbSr.append("/10000");
		}
		sbSr.append(" as ").append("F").append(aField.getCellCol()).append(
						"_").append(aField.getCellRow());
		return sbSr.toString();
	}

	/**
	 * ����������ֵ
	 * 
	 * @return
	 */
	private XMLData getSqlValue() {
		XMLData aData = new XMLData();
		int iCount = lstSql.size();
		if (iCount < 1)
			return aData;
		CellFieldInfo aField;

		for (int i = 0; i < iCount; i++) {
			aField = (CellFieldInfo) lstSql.get(i);
			aData.put("F" + aField.getCellCol() + "_" + aField.getCellRow(),
					exeSql(aField.getSqlStatement()));
		}
		return aData;

	}

	private Double exeSql(String sSql) {
		if (sSql.toLowerCase().indexOf(" where ") == -1)
			sSql = sSql + " where 1=1 ";

		if (Common.RUNTIME_DEBUG)
			System.out.println(sSql + "  " + sqlWhere);
		List lstData = dao.findBySqlByUpper(sSql + "  " + sqlWhere);

		if (lstData == null || lstData.size() == 0) {
			return new Double(0.00);
		}
		Map aData = (Map) lstData.get(0);
		Iterator it = aData.values().iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj == null)
				return new Double(0.00);
			return new Double(Double.parseDouble(obj.toString()));
		}
		return new Double(0.00);

	}

	/**
	 * ���ҹ�ʽ���ֵ
	 * 
	 * @return
	 */
	private XMLData getFormulaValue(XMLData xmlReady) {

		if (lstFormula == null || lstFormula.size() < 1)
			return xmlReady;
		// ��������
		lstFormula = orderFormula(lstFormula);
		int iCount = lstFormula.size();
		CellFieldInfo aField;
		for (int i = 0; i < iCount; i++) {
			aField = (CellFieldInfo) lstFormula.get(i);
			xmlReady.put("F" + aField.getCellCol() + "_" + aField.getCellRow(),
					calcFormula(xmlReady, aField.getFormula()));
		}
		return xmlReady;
	}

	// TODO û�п��˵���ʽ����
	public Double calcFormula(XMLData xmlSource, String formula) {
		while (true) {
			String subString = getAFormulaEle(formula);
			if (subString.equals(""))
				break;
			double value = findValue(xmlSource, subString);
			formula = StringUtils.replace(formula, subString, String
					.valueOf(value));
		}
		try {
			return new Double(FormulaTool.getValue(formula, null).doubleValue());
		} catch (Exception e) {
			e.printStackTrace();
			return new Double(0);
		}

	}

	// ���������{D1}
	private double findValue(XMLData xmlSource, String statement) {
		statement = statement.toUpperCase()
				.substring(1, statement.length() - 1);
		CellSelection cells = ReportUtil.translateToNumber(statement + ":"
				+ statement);
		Object obj = xmlSource.get("F" + cells.getColumn() + "_"
				+ (cells.getRow()));
		if (obj == null)
			return 0;
		else
			return Double.parseDouble(obj.toString());

	}

	// ִ�в�ѯ
	//isConvert �ǲ���Ҫת������Ԫ
	public XMLData doQuery(String sqlWhere, boolean isConvert) {
		this.sqlWhere = sqlWhere;
		XMLData aData = new XMLData();
		aData.putAll(getZc(isConvert));
		aData.putAll(getSr(isConvert));
		aData.putAll(getSqlValue());
		aData = noNullValue(aData);
		return getFormulaValue(aData);

	}

	public static void main(String[] args) {
		SzzbEngine sd = new SzzbEngine(null, null);

		String i = sd.getAFormulaEle("dd{d},{2}");
		System.out.println(i);
	}

	/**
	 * ȥ����NULL��ֵ
	 * 
	 * @param aData
	 * @return
	 */
	private XMLData noNullValue(XMLData aData) {
		Iterator it = aData.keySet().iterator();
		while (it.hasNext()) {
			Object objKey = it.next();
			Object obj = aData.get(objKey);
			if (obj == null || obj.equals("null")) {
				aData.put(objKey, "0");
				it = aData.keySet().iterator();
			}
		}
		return aData;
	}

	/**
	 * ȡ��һ��ʽ��Ԫ��
	 * 
	 * @param sFormula
	 * @return
	 */
	private String getAFormulaEle(String sFormula) {
		if (Common.isNullStr(sFormula))
			return "";
		int iBegin, iEnd;
		iBegin = sFormula.indexOf("{");
		iEnd = sFormula.indexOf("}");
		if (iBegin == -1 || iEnd == -1)
			return "";
		return sFormula.substring(iBegin, iEnd + 1);
	}

	private List orderFormula(List lstFormula) {
		if (lstFormula == null || lstFormula.size() == 0)
			return lstFormula;
		List lstReslut = new ArrayList();
		// ��������
		int iCount = lstFormula.size();
		while (iCount > 0) {

			CellFieldInfo cf = (CellFieldInfo) lstFormula.get(0);
			for (int i = 1; i < iCount; i++) {
				CellFieldInfo cfTemp = (CellFieldInfo) lstFormula.get(i);
				if (cf.getForLvl() > cfTemp.getForLvl()) {
					// ����
					cf = cfTemp;
				}
			}
			lstReslut.add(cf);
			lstFormula.remove(cf);
			iCount = iCount - 1;
		}
		return lstReslut;
	}
}
