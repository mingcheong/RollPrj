/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.action;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.BuildSql;
import gov.nbcs.rp.queryreport.definereport.ui.CalcColumnDialog;
import gov.nbcs.rp.queryreport.definereport.ui.DefinePub;
import gov.nbcs.rp.queryreport.definereport.ui.MyCalculateValueImpl;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.fr.cell.CellSelection;

/**

 * Copyright: Copyright (c) 2011 �㽭����
 * </p>
 * <p>
 * Company: �㽭����
 * </p>
 * <p>
 * CreateDate 20011-7-22
 * </p>
 * 
 * @author wuyal

 * @version 1.0
 */


public class SearchCalcCol {

	private ReportQuerySource querySource;

	private List lstCells;

	private Map fieldEnameMap;

	private Map enameMap;

	public SearchCalcCol(BuildSql buildSql) {
		this.querySource = buildSql.getQuerySource();
		this.lstCells = buildSql.getLstCells();
		this.fieldEnameMap = buildSql.getFieldEnameMap();
		this.enameMap = buildSql.getEnameMap();
	}

	/**
	 * �õ���������Ϣ
	 * 
	 * @param bDetail
	 *            �Ƿ���ϸ��Ϣ
	 * @param isSumUp
	 *            �Ƿ����ϻ���
	 * @return
	 */
	public String getCalc(boolean bDetail, boolean isSumUp) {
		Object value;
		StringBuffer sSqlTmp = new StringBuffer();
		MyCalculateValueImpl calculateValueImpl;

		String sFormula;

		// �õ���������ı�־
		int moneyOp = DefinePub.getMoneyOp(querySource);
		for (int i = 0; i < lstCells.size(); i++) {
			// �õ���Ԫ��ֵ
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			if (value instanceof MyCalculateValueImpl) {// ������
				calculateValueImpl = (MyCalculateValueImpl) value;
				String isSumUpValue = calculateValueImpl.getIsSumUp();

				// �Ƿ����ϻ���
				if (isSumUp
						|| (!isSumUp && (isSumUpValue == null || IDefineReport.TRUE_NUM
								.equals(isSumUpValue)))) {
					// �ж��ǵ�Ԫ����ӻ��ǹ�ʽ
					if (DefinePub.checkIsCellFormula(calculateValueImpl)) {// �ǵ�Ԫ�����
						sFormula = convertCellFormula(calculateValueImpl
								.getFormula().getContent().substring(1),
								bDetail, moneyOp);
					} else {
						sFormula = convertFormula(calculateValueImpl, bDetail,
								moneyOp);
					}
				} else {
					sFormula = " 0 ";
				}

				// ���Ӷ���
				if (!Common.isNullStr(sSqlTmp.toString())) {
					sSqlTmp.append(",");
				}
				sSqlTmp.append(sFormula);
				sSqlTmp.append(" AS " + enameMap.get(new Integer(i)));
			}
		}
		if (bDetail) {
			sSqlTmp.replace(0, sSqlTmp.length(), sSqlTmp.toString()
					.toLowerCase().replaceAll(CalcColumnDialog.OPER_SUM, ""));
			sSqlTmp.replace(0, sSqlTmp.length(), sSqlTmp.toString().replaceAll(
					CalcColumnDialog.OPER_AVG, ""));
		}

		return sSqlTmp.toString();
	}

	// ��ʽת������Ӧ�ֶ�
	private String convertFormula_A(MyCalculateValueImpl calculateValueImpl,
			String sFormula, boolean bDetail) {
		IMeasureAttr measureAttr;
		String sMeasureID;

		// ��ʽ
		for (int j = 0; j < calculateValueImpl.getMeasureArray().length; j++) {
			measureAttr = calculateValueImpl.getMeasureArray()[j];
			sMeasureID = measureAttr.getMeasureID();
			// �ж��ǲ��Ǽ���,�Ƿ�Ҫ��isnull
			if (CalcColumnDialog.OPER_COUNT.equalsIgnoreCase(calculateValueImpl
					.getMeasureArray()[j].getGroupType())) {
				// �ж��Ƿ�����ϸ
				if (bDetail) {
					sFormula = " 0 ";
					break;
				}
				sFormula = sFormula.replaceAll(sMeasureID, fieldEnameMap.get(
						sMeasureID).toString());
			} else {
				sFormula = sFormula.replaceAll(sMeasureID, "ISNULL("
						+ fieldEnameMap.get(sMeasureID).toString() + ",0)");
			}
		}
		return sFormula;

	}

	// ��ʽת������Ӧ�ֶ�
	private String convertFormula(MyCalculateValueImpl calculateValueImpl,
			boolean bDetail, int moneyOp) {
		String sFormula = calculateValueImpl.getFormula().getContent()
				.substring(1);
		sFormula = convertFormula_A(calculateValueImpl, sFormula, bDetail);
		if (bDetail) {// ��ϸ
			if (moneyOp == 2) {// 2:Ԫת��Ϊ��Ԫ���Ȼ�������������
				sFormula = "Round((" + sFormula + ")/10000,2) ";
			}

		} else {// ����,1:Ԫת��Ϊ��Ԫ�������������ٻ���
			if (moneyOp == 2) {// 2:Ԫת��Ϊ��Ԫ���Ȼ�������������
				sFormula = "Round((" + sFormula + ")/10000,2) ";
			}
		}
		return sFormula;
	}

	// ��Ԫ������й�ʽת������Ӧ�ֶ�
	private String convertCellFormula(String sFormula, boolean bDetail,
			int moneyOp) {
		String[] arrStr = sFormula.split("[\\+\\-\\*/(),]");

		MyCalculateValueImpl calculateValueImpl;
		for (int i = 0; i < arrStr.length; i++) {
			if (!check(arrStr[i]))
				continue;

			CellSelection cell = ReportUtil.translateToNumber(arrStr[i]
					.toUpperCase()
					+ ":" + arrStr[i].toUpperCase());
			// �õ��к�
			int col = cell.getColumn();
			Object value = lstCells.get(col);
			if (value instanceof MyCalculateValueImpl) {
				calculateValueImpl = (MyCalculateValueImpl) value;
				if (DefinePub.checkIsCellFormula(calculateValueImpl)) {
					sFormula = replace(arrStr[i], "("
							+ convertCellFormula(calculateValueImpl
									.getFormula().getContent().substring(1),
									bDetail, moneyOp)+")", sFormula);
				}
			}
			sFormula = replace(arrStr[i], "("
					+ this.convertFormula((MyCalculateValueImpl) value,
							bDetail, moneyOp) + ")", sFormula);
		}

		return sFormula;
	}

	public static boolean check(String arrStr) {
		Pattern p = Pattern.compile("[a-zA-Z]{1,2}[0-9]{1,2}");

		Matcher m = p.matcher(arrStr);
		if (!m.matches())
			return false;
		return true;
	}

	/**
	 * �滻�������ڵ�Ԫ��E8+AE8�滻ʱ�账��
	 * 
	 * @param replaceFrom
	 * @param replaceTo
	 * @param sFormula
	 * @return
	 */
	public static String replace(String replaceFrom, String replaceTo,
			String sFormula) {
		if (replaceTo.equalsIgnoreCase(replaceFrom)) {
			return sFormula;
		}
		String pat = "(([^a-z]{1}" + replaceFrom + "|^" + replaceFrom + ")"
				+ "[^a-z]{1})|((([^a-z]{1}" + replaceFrom + "|^" + replaceFrom
				+ ")$))";
		Pattern p = Pattern.compile(pat);
		Matcher m = p.matcher(sFormula);
		while (m.find()) {
			String tableName = sFormula.substring(m.start(), m.end());
			tableName = tableName.replaceAll(replaceFrom, replaceTo);
			sFormula = sFormula.substring(0, m.start()) + tableName
					+ sFormula.substring(m.end());
			m = p.matcher(sFormula);
		}
		return sFormula;
	}

	/**
	 * �õ���ʽֵ
	 * 
	 * @param calculateValueImpl
	 * @return
	 */
	public String getFormula(MyCalculateValueImpl calculateValueImpl) {
		if (DefinePub.checkIsCellFormula(calculateValueImpl)) {// ��Ԫ�����
			String sFormula = calculateValueImpl.getFormula().getContent()
					.substring(1);
			return this.getCellFormula(sFormula);
		} else {
			return this.getNotCellFormula(calculateValueImpl);
		}
	}

	/**
	 * �õ���ʽ�ǵ�Ԫ�������
	 * 
	 * @param calculateValueImpl
	 * @return
	 */
	private String getNotCellFormula(MyCalculateValueImpl calculateValueImpl) {
		IMeasureAttr measureAttr;
		String sMeasureID;
		String sFormula = calculateValueImpl.getFormula().getContent()
				.substring(1);

		// ��ʽ
		for (int j = 0; j < calculateValueImpl.getMeasureArray().length; j++) {
			measureAttr = calculateValueImpl.getMeasureArray()[j];
			sMeasureID = measureAttr.getMeasureID();
			sFormula = sFormula.replaceAll(sMeasureID, fieldEnameMap.get(
					sMeasureID).toString());
		}

		sFormula = sFormula.toLowerCase().replaceAll(CalcColumnDialog.OPER_SUM,
				"");
		sFormula = sFormula.toString()
				.replaceAll(CalcColumnDialog.OPER_AVG, "");
		return sFormula;

	}

	/**
	 * �õ���Ԫ��ename���ݵ�Ԫ�����ƣ���Ԫ������:����A5��B5��
	 * 
	 * @param cellName
	 * @return
	 */
	private String getCellFormula(String sFormula) {
		String[] arrStr = sFormula.split("[\\+\\-\\*/(),]");

		MyCalculateValueImpl calculateValueImpl;
		for (int i = 0; i < arrStr.length; i++) {
			if (!check(arrStr[i]))
				continue;

			CellSelection cell = ReportUtil.translateToNumber(arrStr[i]
					.toUpperCase()
					+ ":" + arrStr[i].toUpperCase());
			// �õ��к�
			int col = cell.getColumn();
			Object value = lstCells.get(col);
			if (value instanceof MyCalculateValueImpl) {
				calculateValueImpl = (MyCalculateValueImpl) value;
				if (DefinePub.checkIsCellFormula(calculateValueImpl)) {
					sFormula = replace(arrStr[i], "("
							+ getCellFormula(calculateValueImpl.getFormula()
									.getContent().substring(1)) + ")", sFormula);
				}
			}
			sFormula = replace(arrStr[i], this
					.getNotCellFormula((MyCalculateValueImpl) value), sFormula);
		}

		return sFormula;
	}

}
