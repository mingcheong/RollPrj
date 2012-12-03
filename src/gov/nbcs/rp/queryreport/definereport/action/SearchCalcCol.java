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

 * Copyright: Copyright (c) 2011 浙江易桥
 * </p>
 * <p>
 * Company: 浙江易桥
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
	 * 得到计算列信息
	 * 
	 * @param bDetail
	 *            是否明细信息
	 * @param isSumUp
	 *            是否向上汇总
	 * @return
	 */
	public String getCalc(boolean bDetail, boolean isSumUp) {
		Object value;
		StringBuffer sSqlTmp = new StringBuffer();
		MyCalculateValueImpl calculateValueImpl;

		String sFormula;

		// 得到四舍五入的标志
		int moneyOp = DefinePub.getMoneyOp(querySource);
		for (int i = 0; i < lstCells.size(); i++) {
			// 得到单元格值
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			if (value instanceof MyCalculateValueImpl) {// 计算列
				calculateValueImpl = (MyCalculateValueImpl) value;
				String isSumUpValue = calculateValueImpl.getIsSumUp();

				// 是否向上汇总
				if (isSumUp
						|| (!isSumUp && (isSumUpValue == null || IDefineReport.TRUE_NUM
								.equals(isSumUpValue)))) {
					// 判断是单元格相加还是公式
					if (DefinePub.checkIsCellFormula(calculateValueImpl)) {// 是单元格相加
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

				// 增加逗号
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

	// 公式转换成相应字段
	private String convertFormula_A(MyCalculateValueImpl calculateValueImpl,
			String sFormula, boolean bDetail) {
		IMeasureAttr measureAttr;
		String sMeasureID;

		// 公式
		for (int j = 0; j < calculateValueImpl.getMeasureArray().length; j++) {
			measureAttr = calculateValueImpl.getMeasureArray()[j];
			sMeasureID = measureAttr.getMeasureID();
			// 判断是不是计数,是否要加isnull
			if (CalcColumnDialog.OPER_COUNT.equalsIgnoreCase(calculateValueImpl
					.getMeasureArray()[j].getGroupType())) {
				// 判断是否求明细
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

	// 公式转换成相应字段
	private String convertFormula(MyCalculateValueImpl calculateValueImpl,
			boolean bDetail, int moneyOp) {
		String sFormula = calculateValueImpl.getFormula().getContent()
				.substring(1);
		sFormula = convertFormula_A(calculateValueImpl, sFormula, bDetail);
		if (bDetail) {// 明细
			if (moneyOp == 2) {// 2:元转换为成元，先汇总再四舍五入
				sFormula = "Round((" + sFormula + ")/10000,2) ";
			}

		} else {// 汇总,1:元转换为万元，先四舍五入再汇总
			if (moneyOp == 2) {// 2:元转换为成元，先汇总再四舍五入
				sFormula = "Round((" + sFormula + ")/10000,2) ";
			}
		}
		return sFormula;
	}

	// 单元格相加列公式转换成相应字段
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
			// 得到列号
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
	 * 替换函数（在单元格E8+AE8替换时需处理）
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
	 * 得到公式值
	 * 
	 * @param calculateValueImpl
	 * @return
	 */
	public String getFormula(MyCalculateValueImpl calculateValueImpl) {
		if (DefinePub.checkIsCellFormula(calculateValueImpl)) {// 单元格相加
			String sFormula = calculateValueImpl.getFormula().getContent()
					.substring(1);
			return this.getCellFormula(sFormula);
		} else {
			return this.getNotCellFormula(calculateValueImpl);
		}
	}

	/**
	 * 得到公式非单元格相加列
	 * 
	 * @param calculateValueImpl
	 * @return
	 */
	private String getNotCellFormula(MyCalculateValueImpl calculateValueImpl) {
		IMeasureAttr measureAttr;
		String sMeasureID;
		String sFormula = calculateValueImpl.getFormula().getContent()
				.substring(1);

		// 公式
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
	 * 得到单元格ename根据单元格名称（单元格名称:例如A5、B5）
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
			// 得到列号
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
