/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.dicinfo.prjdetail.action.PrjDetailAction;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.sys.besqryreport.action.BesAct;

import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;

/**

 */
public class ReportHeader {

	static public String COMPARE_1 = "1";

	static public String COMPARE_2 = "2";

	// ��ŷ����ͷename��ֵ
	private Map enameMap;

	/**
	 * �õ���ͷDataSet
	 * 
	 * @param sReportId
	 *            ����ID
	 * @return��ͷDataSet
	 * @throws Exception
	 */
	public DataSet getHeader(GroupReport groupReport, String sReportId)
			throws Exception {

		// �õ���ͷ������
		int indexs[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_HEADER, groupReport);
		int startRow = indexs[0];
		int endRow = indexs[indexs.length - 1];
		int col = groupReport.getColumnCount();
		// // editColumn = i;
		// // editRow = j;
		// // editColumnSpan = k;
		// // editRowSpan = l;
		// // column = i1;
		// // row = j1;
		// // columnSpan = k1;
		// // rowSpan = l1;

		CellSelection cs = new CellSelection(0, startRow, col, endRow
				- startRow + 1, 0, startRow, col, endRow - startRow + 1);

		if (cs == null)
			return null;

		Node nodeC = ReportUtil.parseDocument(groupReport, cs);
		return fillIntoDataSet(nodeC, BesAct.codeRule_Col, groupReport,
				sReportId);
	}

	/**
	 * ������Ϣnode ����������dataset��
	 * 
	 * @throws Exception
	 */

	private DataSet fillIntoDataSet(Node aNode, SysCodeRule codeRule,
			GroupReport groupReport, String sReportId) throws Exception {
		// �����ж�Ӧ���ɵı�������
		if (enameMap == null)
			enameMap = new HashMap();
		int iFMax = 0;
		int iCMax = 0;

		// �õ��������к�
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// �����ͷDataSet
		DataSet dsHeader = DataSet.create();
		// ����PrjDetailAction
		PrjDetailAction prjAct = new PrjDetailAction();

		// node��Ψһ��ʾ��
		String nodeID = null;
		// ����field_idʹ��
		int iFieldID = 0;
		String sfieldID = "";
		// ������
		MyGroupValueImpl groupValueImpl;
		// ������
		MyCalculateValueImpl calculateValueImpl;
		IGroupAble groupAble;
		// �ڵ�
		Node nodeGet;
		// ������ֵ
		Object objOpe;
		// ��ͷ��cell
		CellElement cell;
		// ENameֵ
		String sEName = "";
		// �Ƿ�����
		String isHide = "";
		// �ֶ�����
		String fieldType = "";
		// �ֶθ�ʽ
		String fieldDisformat = "";

		Node[][] nodeArray = aNode.toArray();
		// ѭ�������ݵ�dataSet
		for (int i = 1; i < nodeArray.length; i++) {
			for (int j = 0; j < nodeArray[i].length; j++) {
				nodeGet = nodeArray[i][j];
				cell = (CellElement) nodeGet.getValue();

				dsHeader.append();
				nodeID = nodeGet.getIdentifier().toString();

				dsHeader.fieldByName("nodeID").setValue(nodeID);
				// reportid
				dsHeader.fieldByName(IBesQryReport.REPORT_ID).setValue(
						sReportId);
				// field_code
				String sFieldID = prjAct.createLvlID(nodeGet, dsHeader,
						BesAct.codeRule_Col, IBesQryReport.FIELD_CODE);
//				String sFieldID ="";
				dsHeader.fieldByName(IBesQryReport.FIELD_CODE).setValue(
						sFieldID);
				// field_id
				if (i == 1 && j == 0)
					sfieldID = Common.getStrID(new BigDecimal(iFieldID)
							.add(new BigDecimal(1)), 4);
				else
					sfieldID = Common.getStrID(new BigDecimal(sfieldID)
							.add(new BigDecimal(1)), 4);
				// parid
				dsHeader.fieldByName("PARID").setValue(
						nodeGet.getParent().getIdentifier());
				dsHeader.fieldByName(IBesQryReport.FIELD_ID).setValue(sfieldID);
				// field_cname
				dsHeader.fieldByName(IBesQryReport.FIELD_CNAME).setValue(
						nodeGet.getText());
				// ���� FName
//				String sParentFName = prjAct.getParentFName(dsHeader, nodeGet);
				String sParentFName ="";
				String sFName = nodeGet.getText();
				if (!Common.isNullStr(sParentFName))
					sFName = sParentFName + "." + sFName;
				dsHeader.fieldByName(IBesQryReport.FIELD_FNAME)
						.setValue(sFName);
				String sLeaf = null;
				if (nodeGet.getChildrenCount() > 0)
					sLeaf = "0";
				else
					sLeaf = "1";
				// is_leaf
				dsHeader.fieldByName(IBesQryReport.IS_LEAF).setValue(sLeaf);
				// field_level
				String sLevel = String.valueOf(nodeGet.getLevel());
				dsHeader.fieldByName(IBesQryReport.FIELD_LEVEL)
						.setValue(sLevel);
				// �ж��ǲ���Ҷ�ڵ�
				if (Common.estimate(sLeaf)) {// ��Ҷ�ڵ�
					// �п�
					dsHeader.fieldByName(IBesQryReport.FIELD_DISPWIDTH)
							.setValue(
									new Double(groupReport.getColumnWidth(cell
											.getColumn())));

					// �õ���ǰ�ж�Ӧ�Ĳ�����cell
					objOpe = groupReport.getCellValue(cell.getColumn(), rowOpe);
					// �ж�ֵ�Ƿ�Ϊnull
					if (objOpe != null) {
						// �ж��Ƿ����л��Ǽ�����
						if (objOpe instanceof MyGroupValueImpl) {// ������
							groupValueImpl = (MyGroupValueImpl) objOpe;
							groupAble = groupValueImpl.getGroupAbleArray()[0];
							isHide = groupAble.getIsVisual() == 1 ? "��" : "��";
							fieldType = IDefineReport.CHAR_TYPE;
							fieldDisformat = "";
							iCMax++;
							sEName = "C" + iCMax;
						} else if (objOpe instanceof MyCalculateValueImpl) {// ������
							calculateValueImpl = (MyCalculateValueImpl) objOpe;
							// �Ƿ�����
							isHide = calculateValueImpl.getIsVisual() == 1 ? "��"
									: "��";

							if (calculateValueImpl.getIntNumber() == 0) {
								fieldType = IDefineReport.INTT_TYPE;
								fieldDisformat = IDefineReport.INT_FORMATE;
							} else {
								fieldType = IDefineReport.FLOAT_TYPE;
								fieldDisformat = IDefineReport.FLOAT_FORMATE;
							}
							iFMax++;
							sEName = "F" + iFMax;
						} else { // ��������Ĭ��Ϊ�ַ���
							isHide = "��";
							fieldType = IDefineReport.CHAR_TYPE;
							fieldDisformat = "";
							iCMax++;
							sEName = "C" + iCMax;
						}
					} else { // CellElement==null
						isHide = "��";
						fieldType = IDefineReport.CHAR_TYPE;
						fieldDisformat = "";
						iCMax++;
						sEName = "C" + iCMax;
					}
					// ename
					enameMap.put(new Integer(cell.getColumn()), sEName);
					dsHeader.fieldByName(IBesQryReport.FIELD_ENAME).setValue(
							sEName);
				} else {// ����Ҷ�ڵ�
					isHide = "��";
					fieldType = IDefineReport.CHAR_TYPE;
					fieldDisformat = "";
				}
				// is_hidecol
				dsHeader.fieldByName(IBesQryReport.IS_HIDECOL).setValue(isHide);
				// field_type
				dsHeader.fieldByName(IBesQryReport.FIELD_TYPE).setValue(
						fieldType);
				// field_disformat
				dsHeader.fieldByName(IBesQryReport.FIELD_DISFORMAT).setValue(
						fieldDisformat);
				// field_width
				dsHeader.fieldByName("FIELD_WIDTH").setValue("");
				// set_year
				dsHeader.fieldByName(IBesQryReport.SET_YEAR).setValue(
						Global.loginYear);
				// rg_code
				dsHeader.fieldByName("RG_CODE")
						.setValue(Global.getCurrRegion());

				if (cell.getValue() instanceof FundSourceImpl) {
					// �Ƿ�֧���ʽ���Դ���
					dsHeader.fieldByName(IBesQryReport.FUNDSOURCE_FLAG)
							.setValue(
									(((FundSourceImpl) cell.getValue())
											.isReserveExecute()) ? "1" : "0");
					// �Աȷ������
					dsHeader.fieldByName(IBesQryReport.COMPARE_FLAG)
							.setValue(
									((FundSourceImpl) cell.getValue())
											.getCompareFlag());
				} else {
					dsHeader.fieldByName(IBesQryReport.FUNDSOURCE_FLAG)
							.setValue("0");
					dsHeader.fieldByName(IBesQryReport.COMPARE_FLAG).setValue(
							"");
				}

			}
		}
		return dsHeader;
	}

	/**
	 * ѭ���õ�enameֵ
	 * 
	 * @param dsHeader��ͷDataSet
	 * @return
	 * @throws Exception
	 */
	public static Map getHeaderEname(DataSet dsHeader) throws Exception {
		int i = 0;
		Map enameMap = new HashMap();
		dsHeader.beforeFirst();
		while (dsHeader.next()) {
			if (dsHeader.fieldByName(IQrBudget.IS_LEAF).getValue() == null)
				continue;
			if (dsHeader.fieldByName(IQrBudget.IS_LEAF).getInteger() == 0)
				continue;
			enameMap.put(new Integer(i), dsHeader.fieldByName(
					IQrBudget.FIELD_ENAME).getString());
			i++;
		}
		return enameMap;
	}

	public Map getEnameMap() {
		return enameMap;
	}

}
