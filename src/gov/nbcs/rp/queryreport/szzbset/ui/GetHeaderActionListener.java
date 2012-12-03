package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.dicinfo.prjdetail.action.PrjDetailAction;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.WorkSheet;

/**
 * 设置收支总表设置,获取表头列
 * 
 * @author qzc
 * 
 */
public class GetHeaderActionListener implements ActionListener {
	SzzbSet szzbSet;

	ReportInfoPanel reportInfoPanel;

	public GetHeaderActionListener(SzzbSet szzbSet,
			ReportInfoPanel reportColumnsPanel) {
		this.szzbSet = szzbSet;
		this.reportInfoPanel = reportColumnsPanel;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			reportInfoPanel.getCellInfo(szzbSet.jWorkBook);
			String sTitleArea = reportInfoPanel.ftxtEditRepTitleArea.getValue()
					.toString();
			if (!"".equals(sTitleArea)) {
				CellSelection cells = ReportUtil.translateToNumber(sTitleArea);
				// 得到表头信息
				int iIndex = szzbSet.jWorkBook.getWorkBook().getSelectedIndex();
				szzbSet.dsHeader = DataSet.create();
				getColInfo(szzbSet.dsHeader, (WorkSheet) szzbSet.jWorkBook
						.getWorkBook().getReport(iIndex), cells);
				// 显示列信息
				szzbSet.showColInfo();
				szzbSet.isNeedFresh = true;
				szzbSet.setChanged(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(szzbSet, " 发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 获得表头信息
	 */
	private void getColInfo(DataSet dsColInfo, WorkSheet worksheet,
			CellSelection cs) throws Exception {
		int iCount = cs.getColumnSpan();
		int iColStart = cs.getColumn();
		for (int i = 0; i < iCount; i++) {

			dsColInfo.append();
			dsColInfo.fieldByName(IQrBudget.SET_YEAR).setValue(
					Global.getSetYear());

			dsColInfo.fieldByName(IQrBudget.IS_LEAF).setValue("1");
			dsColInfo.fieldByName(IQrBudget.IS_HIDECOL).setValue("否");

			dsColInfo.fieldByName(ISzzbSet.NODEID).setValue(
					ReportUtil.translateToColumnName(iColStart + i));

			dsColInfo.fieldByName(IQrBudget.FIELD_ID).setValue(
					String.valueOf(i + 1));

			dsColInfo.fieldByName(IQrBudget.FIELD_CODE).setValue(
					StringUtils.leftPad(String.valueOf(i + 1), 3, '0'));

			dsColInfo.fieldByName(IQrBudget.FIELD_CNAME).setValue(
					ReportUtil.translateToColumnName(iColStart + i));
			dsColInfo.fieldByName(IQrBudget.FIELD_FNAME).setValue(
					ReportUtil.translateToColumnName(iColStart + i));
			dsColInfo.fieldByName(IQrBudget.FIELD_LEVEL).setValue("1");

			dsColInfo.fieldByName(IQrBudget.FIELD_TYPE).setValue("字符串");
			// 列宽

			dsColInfo.fieldByName(IQrBudget.FIELD_DISPWIDTH).setValue("12");

		}
	}

	/**
	 * 获得表头信息
	 */
	private void getHeaderInfo(DataSet dsColInfo, WorkSheet worksheet,
			CellSelection cs) throws Exception {
		Node node = ReportUtil.parseDocument(worksheet, cs);
		Node[][] nodeArray = node.toArray();
		BigDecimal k = new BigDecimal(0.0);
		String sField_id = "";
		String sField_Code;

		Node curNode;
		Node parNode;
		SysCodeRule codeRule_Col = BesAct.codeRule_Col;
		PrjDetailAction prjDetailAction = new PrjDetailAction();
		for (int i = 1; i < nodeArray.length; i++) {
			for (int j = 0; j < nodeArray[i].length; j++) {
				k = k.add(new BigDecimal(1));
				sField_id = Common.getStrID(k, 4);
				curNode = nodeArray[i][j];
				parNode = curNode.getParent();

				// 得到FName
				Object parentName = parNode.getValue() == null ? null : parNode
						.getText();
				while (parNode != node) {
					parNode = parNode.getParent();
					parentName = parNode.getValue() == null ? parentName
							: parNode.getText() == null ? "" : parNode
									.getText()
									+ "." + parentName;
				}

				dsColInfo.append();
				dsColInfo.fieldByName(IQrBudget.SET_YEAR).setValue(
						Global.loginYear);
				if (curNode.getNodeType() == Node.LEAF) {
					dsColInfo.fieldByName(IQrBudget.IS_LEAF).setValue("1");
					dsColInfo.fieldByName(IQrBudget.IS_HIDECOL).setValue("否");
				} else {
					dsColInfo.fieldByName(IQrBudget.IS_LEAF).setValue("0");
					dsColInfo.fieldByName(IQrBudget.IS_HIDECOL).setValue("");
				}

				dsColInfo.fieldByName(ISzzbSet.NODEID).setValue(
						curNode.getIdentifier().toString());

				dsColInfo.fieldByName(IQrBudget.FIELD_ID).setValue(sField_id);

				sField_Code = prjDetailAction.createFieldID(dsColInfo, curNode,
						codeRule_Col, IQrBudget.FIELD_CODE);
				dsColInfo.fieldByName(IQrBudget.FIELD_CODE).setValue(
						sField_Code);

				dsColInfo.fieldByName(IQrBudget.FIELD_CNAME).setValue(
						curNode.getText());
				dsColInfo.fieldByName(IQrBudget.FIELD_FNAME).setValue(
						parentName == null ? curNode.getText() : parentName
								+ "." + curNode.getText());
				dsColInfo.fieldByName(IQrBudget.FIELD_LEVEL).setValue(
						new Integer(curNode.getLevel()));

				dsColInfo.fieldByName(IQrBudget.FIELD_TYPE).setValue("字符串");
				// 列宽
				CellElement cell = (CellElement) curNode.getValue();
				Double columnWidth = new Double(worksheet.getColumnWidth(cell
						.getColumn()));
				dsColInfo.fieldByName(IQrBudget.FIELD_DISPWIDTH).setValue(
						String.valueOf(columnWidth));
			}
		}
	}

}
