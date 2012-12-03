package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.reportcy.common.gui.dnd.TransferableObj;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.common.util.StringEx;
import com.foundercy.pf.reportcy.summary.constants.ColumnConstants;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.exception.SummaryConverException;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.AbstractFormula;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;
import com.foundercy.pf.reportcy.summary.object.cellvalue.GroupBeanImpl;
import com.foundercy.pf.reportcy.summary.object.cellvalue.MeasureAttr;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.cell.Grid;
import com.fr.cell.ReportPane;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;
import com.fr.report.Report;
import com.fr.report.highlight.HighlightGroup;

/**
 * <p>
 * Title:ʵ��ѡ������Դ��������϶�ʵ��
 * </p>
 * <p>
 * Description:ʵ��ѡ������Դ��������϶�ʵ��
 * </p>

 */
public class ReportPaneDropTarget implements DropTargetListener {

	private ReportPane reportPane = null;

	private ReportGuideUI reportGuideUI = null;

	public ReportPaneDropTarget(ReportGuideUI reportGuideUI,
			ReportPane reportPane) {
		new DropTarget(reportPane.getGrid(), this);
		this.reportPane = reportPane;
		this.reportGuideUI = reportGuideUI;
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(dtde.getDropAction());
	}

	/*
	 * (non-Javadoc) ���ڽ��� drag ����ʱ���ã���ʱ���ָ����Ȼ���� DropTarget��ʹ�ô� listener ע�ᣩ��
	 * drop λ�õĿɲ������֡�
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 *      <p>edit by mawenming at 2007-6-6������08:00:31
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		// XXL ֻ�б༭���ſ��Խ���
		Point p = dtde.getLocation();
		DropTargetContext dtc = dtde.getDropTargetContext();
		Grid grid = (Grid) dtc.getComponent();
		grid.doMousePress(p.getX(), p.getY());
		if (reportPane.getCellSelection() == null) {
			dtde.rejectDrag();
			return;
		}

		if (CreateGroupReport.isInGroupRow(reportPane.getCellSelection()
				.getRow(), RowConstants.UIAREA_OPERATION,
				(GroupReport) reportPane.getReport()))
			dtde.acceptDrag(dtde.getDropAction());
		else
			dtde.rejectDrag();
	}

	public void dragExit(DropTargetEvent dte) {
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	public void drop(DropTargetDropEvent dtde) {
		DropTargetContext dtc = dtde.getDropTargetContext();
		Grid grid = (Grid) dtc.getComponent();
		ReportPane reportPane = grid.getReportPane();
		Report report = reportPane.getReport();
		CellSelection cellSelection = reportPane.getCellSelection();
		if (cellSelection == null) {
			dtde.rejectDrop();
			return;
		}

		int editRow = cellSelection.getEditRow();
		try {
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (!tr.isDataFlavorSupported(flavors[i])) {
					continue;
				}

				dtde.acceptDrop(dtde.getDropAction());
				Object userObj = tr.getTransferData(flavors[i]);

				if (userObj instanceof TransferableObj) {

					if (report instanceof GroupReport) {
						GroupReport groupReport = (GroupReport) report;

						drogToGroupReportCell_SimpleDetail(groupReport,
								cellSelection, editRow,
								(TransferableObj) userObj, reportPane);
					}
				}

				dtde.dropComplete(true);
			}
			dtde.rejectDrop();
		} catch (Exception e) {
			com.foundercy.pf.reportcy.common.util.Log.logger.error(
					StringEx.sNull, e);
			dtde.rejectDrop();

		}
	}

	/**
	 * ����鱨��������ק����
	 * 
	 * @param groupReport
	 * @param cellSelection
	 * @param editRow
	 * @param transferableObj
	 *            <p>
	 *            edit by mawenming at Jun 2, 2007��5:43:40 PM
	 * @param reportPane
	 *            TODO
	 */
	private void drogToGroupReportCell_SimpleDetail(GroupReport groupReport,
			CellSelection cellSelection, int editRow,
			TransferableObj transferableObj, ReportPane reportPane) {

		if (!(transferableObj.getObject() instanceof FieldObject)) {
			return;
		}
		FieldObject fieldObject = (FieldObject) transferableObj.getObject();
		// �õ��ֶ�����
		String sFieldType = fieldObject.getFIELD_TYPE();

		// �õ���ǰ�к�
		int editCol = cellSelection.getColumn();

		// �����ֶβ�ͬ���Ͳ�ͬ�������ͻ�����Ĭ��Ϊsum,��������Ϊgroup��max
		if (IDefineReport.INT_TYPE.equals(sFieldType)
				|| IDefineReport.INTT_TYPE.equals(sFieldType)
				|| IDefineReport.CURRENCY_TYPE.equals(sFieldType)
				|| IDefineReport.FLOAT_TYPE.equals(sFieldType)) {// ���ͻ���

			// ��Ԫ����ʾ����
			String sValue = "=Sum({" + fieldObject.OBJECT_CNAME + "."
					+ fieldObject.getFIELD_FNAME() + "})";
			// ����CalculateValueImpl��
			MyCalculateValueImpl myCalculateValueImpl = new MyCalculateValueImpl(
					sValue);

			// ��ʾС��λ��,���Ͳ���ʾС��λ
			if (IDefineReport.INT_TYPE.equals(sFieldType)
					|| IDefineReport.INTT_TYPE.equals(sFieldType)) {
				myCalculateValueImpl.setIntNumber(0);
			} else {
				myCalculateValueImpl.setIntNumber(1);
			}
			// ����Ҫ����,��չ��
			// myCalculateValueImpl.setIsCorss(0);
			// �Ƿ���ܵ�ҵ����,���账��ֻ�����õ�λ������
			// myCalculateValueImpl.setIsMbSummary(0);

			// ��Ԫ����ʾ������,1:��ʾ��0������
			myCalculateValueImpl.setIsVisual(1);
			// ����Ϊ������
			myCalculateValueImpl
					.setColumnType(ColumnConstants.UITYPE_1_CALCULATE);

			// ����MeasureAttr��
			MeasureAttr measureAttr = new MeasureAttr();
			// MeasureAttr,id
			String measureID = DefinePub.getRandomUUID();
			measureAttr.setMeasureID(measureID);
			// MeasureAttr����Դid
			measureAttr.setSourceID(fieldObject.DICID);
			// MeasureAttr,��id
			measureAttr.setSourceColID(fieldObject.FIELD_ENAME);
			// MeasureAttr,��������,Ĭ��Ϊ���(sum)
			measureAttr.setGroupType("sum");
			// MeasureAttr,������
			measureAttr.setColType(DefinePub.getFieldTypeWithCh(sFieldType));

			AbstractFormula abstractFormula = new AbstractFormula();
			abstractFormula.setContent("Sum(" + measureID + ")");
			myCalculateValueImpl.setFormula(abstractFormula);

			// MeasureAttr���ø�MyCalculateValueImpl
			myCalculateValueImpl
					.setMeasureArray(new IMeasureAttr[] { measureAttr });

			// ������GroupValueImpl�ึֵ����Ԫ��
			CellElement cellElement = groupReport.getCellElement(editCol,
					editRow);
			if (cellElement == null) {
				cellElement = new CellElement(editCol, editRow);
				cellElement.setValue(myCalculateValueImpl);
				groupReport.addCellElement(cellElement);
			} else {
				cellElement.setValue(myCalculateValueImpl);
			}

			Style style = cellElement.getStyle();
			style = style.deriveHorizontalAlignment(Constants.RIGHT);
			if (IDefineReport.INT_TYPE.equals(sFieldType)
					|| IDefineReport.INTT_TYPE.equals(sFieldType)) {
				style = style.deriveFormat(new DecimalFormat(
						IDefineReport.INT_FORMATE));
			} else {
				style = style.deriveFormat(new DecimalFormat(
						IDefineReport.FLOAT_FORMATE));
			}
			cellElement.setStyle(style);
			// ���嵥Ԫ�����������Ŀ�ģ�����Ԫ��ֵΪ��ʱ����ʾ
			HighlightGroup highlightGroup = new CustomHighlightGroup();
			cellElement.setHighlightGroup(highlightGroup);

		} else if (IDefineReport.CHAR_TYPE.equals(sFieldType)
				|| IDefineReport.CHAR_TYPE_A.equals(sFieldType)
				|| IDefineReport.DATE_TYPE.equals(sFieldType)
				|| sFieldType == null || "".equals(sFieldType)) {// ��������,�ַ�����������

			// ����GroupBeanImpl����
			IGroupAble groupBeanImpl = new GroupBeanImpl();
			// ID
			groupBeanImpl.setSummaryID(DefinePub.getRandomUUID());

			// ����ԴID
			groupBeanImpl.setSourceID(fieldObject.DICID);

			// �õ�����Դ�ֶ�ֵ
			String[] sourceColID = getSourceColIdWithEname(fieldObject.FIELD_ENAME);

			// �ֶ�ID
			groupBeanImpl.setSourceColID(sourceColID[0]);

			// ת���ɴ���XML�ļ����ֶ�����ֵ
			String sFieldTypeVal = DefinePub.getFieldTypeWithCh(sFieldType);
			// �Ƿ����ϻ���,1:���ϻ��ܣ�0:�����ϻ���
			groupBeanImpl.setIsTotal("0");
			// �ֶ�����
			groupBeanImpl.setColType(sFieldTypeVal);
			// ��ʾ����
			groupBeanImpl.setDispType(sFieldTypeVal);
			// �����ֶ��Ⱥ���,0,1,2,3 ��ʶ
			groupBeanImpl.setOrderIndex("");
			// ��������,�������
			// groupBeanImpl.setOrderType("ASC");
			// �Ƿ���ܵ�ҵ����
			groupBeanImpl.setIsMbSummary(0);
			// ��Ԫ����ʾ������,1:��ʾ��0������
			groupBeanImpl.setIsVisual(1);

			// �ж��ֶ��ǲ���ö���ֶ�(�������ǲ�������ֵ)
			ReportQuerySource querySource = reportGuideUI.querySource;
			boolean bIsEnum = DefinePub.judgetEnumWithColID(querySource,
					fieldObject.DICID, fieldObject.FIELD_ENAME, true);
			// ���ͣ���Group��Max��0:group,1:max
			if (bIsEnum) {
				groupBeanImpl.setIsMax(0);
			} else {
				groupBeanImpl.setIsMax(1);
			}

			// ��Ԫ����ʾ����
			String sShowValue = "{" + fieldObject.OBJECT_CNAME + "."
					+ fieldObject.FIELD_FNAME + "}";

			int iCount = sourceColID.length;
			FunctionRef[] functionRef = new FunctionRef[iCount];
			for (int i = 0; i < iCount; i++) {
				// ���ù�ʽԪ��
				functionRef[i] = new FunctionRef();
				functionRef[i].setRefID(DefinePub.getRandomUUID());
				functionRef[i].setSourceID(fieldObject.DICID);
				functionRef[i].setSourceColID(sourceColID[i]);
			}

			// ���ù�ʽ
			AbstractFormula abstractFormula = new AbstractFormula();
			abstractFormula.setContent(getFormulaWithEname(
					fieldObject.FIELD_ENAME, functionRef));
			abstractFormula.setDisplayValue(sShowValue);
			if (bIsEnum) {
				sShowValue = "Group(" + sShowValue + ")";
			} else {
				sShowValue = "" + sShowValue;
			}
			abstractFormula.setFunctionRefArray(functionRef);
			groupBeanImpl.setFormula(abstractFormula);

			String sErr = checkColIsExist(groupReport.getCellValue(editCol,
					editRow), groupBeanImpl);
			if (!Common.isNullStr(sErr)) {
				new MessageBox(reportGuideUI, sErr, MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			// �õ���ǰ��Ԫ������
			MyGroupValueImpl groupValueImpl = null;
			boolean isMultiGroup = false;
			// �жϵ�Ԫ�����ͣ����������ͬ���Ƿ����У���ʾ�Ƿ��滻�����滻��׷��
			CellElement cell = groupReport.getCellElement(editCol, editRow);
			if (cell != null && cell.getValue() instanceof MyGroupValueImpl) { // ��ͬ����
				// ��ʾ�Ƿ�ȷ��ɾ��
				if (JOptionPane
						.showConfirmDialog(reportPane,
								"�滻��ǰ��Ԫ����Ϣ��?������ǡ��滻���������׷�ӡ�", "��ʾ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {// �滻
					// ����GroupValueImpl��
					groupValueImpl = new MyGroupValueImpl("=" + sShowValue);
					groupValueImpl
							.setGroupAbleArray(new IGroupAble[] { groupBeanImpl });
				} else {// ׷��
					// �õ���ǰ��Ԫ��ֵ
					groupValueImpl = (MyGroupValueImpl) cell.getValue();
					// �������鳤��
					IGroupAble[] iGroupAble = addGroupAbleArrayLegth(
							groupValueImpl.getGroupAbleArray(), 1);
					// �������ӵ�ֵ׷�ӵ����������
					iGroupAble[iGroupAble.length - 1] = groupBeanImpl;
					groupValueImpl.setGroupAbleArray(iGroupAble);
					groupValueImpl.setSValue(groupValueImpl.getSValue() + "\n"
							+ sShowValue);
					isMultiGroup = true;
				}
			} else {// ��ͬ����,ֱ���滻
				// ����GroupValueImpl��
				groupValueImpl = new MyGroupValueImpl("=" + sShowValue);
				groupValueImpl
						.setGroupAbleArray(new IGroupAble[] { groupBeanImpl });

			}

			// ���ϻ����ֶ��Ⱥ���,1,2,3 ��ʶ
			try {
				groupBeanImpl.setSummaryIndex(String.valueOf(this
						.getSummaryIndex(bIsEnum, isMultiGroup, groupReport)));
			} catch (SummaryConverException e) {
				e.printStackTrace();
				new MessageBox("�������󣬴�����Ϣ��" + e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

			// ������GroupValueImpl�ึֵ����Ԫ��
			if (cell == null) {
				cell = new CellElement(editCol, editRow);
				cell.setValue(groupValueImpl);
				groupReport.addCellElement(cell);
			}

			cell.setValue(groupValueImpl);
			Style style = cell.getStyle();
			style = style.deriveHorizontalAlignment(Constants.LEFT);
			cell.setStyle(style);
			cell.setHighlightGroup(null);

			// У��ö��summaryIndexֵ
			CheckOutEnumName checkOutEnumName = new CheckOutEnumName(
					querySource);
			checkOutEnumName.checkEnumSummaryIndex(groupReport);

			// ˢ�����л�����ʾ˳��
			reportGuideUI.reportGroupSet.showGroupSet(groupReport);
		} else {
			new MessageBox("�޷�������������ͣ�", MessageBox.INFOMATION,
					MessageBox.BUTTON_OK).show();
		}
		// ˢ����ʾ
		reportPane.fireReportDataChanged();
	}

	/**
	 * ���ݱ�����������õ�summaryIndexֵ
	 * 
	 * @param bIsEnum�Ƿ�ö������
	 * @param isMultiGroupһ��Ԫ���Ƿ���groupAbleֵ
	 * @param groupReport
	 * @return
	 * @throws SummaryConverException
	 */
	private int getSummaryIndex(boolean bIsEnum, boolean isMultiGroup,
			GroupReport groupReport) throws SummaryConverException {
		int summaryIndex;
		if (isMultiGroup && bIsEnum) {
			summaryIndex = Integer.parseInt(SummaryIndexUnt
					.getSummaryIndex(groupReport));
		} else {
			List lstCells = DefinePubOther.getCellsWithOutClone(groupReport);
			if (bIsEnum) {
				summaryIndex = SummaryIndexUnt.getGroupSummaryIndex(lstCells);
			} else {
				ReportQuerySource querySource = (ReportQuerySource) ReportConver
						.getReportQuerySource(groupReport);
				summaryIndex = SummaryIndexUnt.getDetailSummaryIndex(
						querySource, lstCells);
			}
		}
		if (summaryIndex == -1) {
			summaryIndex = Integer.parseInt(SummaryIndexUnt
					.getSummaryIndex(groupReport));
		}
		return summaryIndex;
	}

	/**
	 * ����Enameֵ�õ�������ֵ
	 * 
	 * @param fieldEname
	 * @return
	 */
	private String[] getSourceColIdWithEname(String fieldEname) {
		// ������������
		int index = fieldEname.indexOf("||");
		if (fieldEname.indexOf("||") >= 0) {
			return new String[] { fieldEname.substring(0, index),
					fieldEname.substring(index + 2) };
		}
		// �����ࡢ�������
		index = fieldEname.toLowerCase().indexOf(GroupColumnDialog.OPER_SUBSTR);
		if (index >= 0) {
			String value = fieldEname.substring(index + 7, fieldEname
					.indexOf(","));
			return new String[] { value };
		}
		return new String[] { fieldEname };
	}

	/**
	 * ����Enameֵ�õ���ʽ
	 * 
	 * @param fieldEname
	 * @param functionRef
	 * @return
	 */
	private String getFormulaWithEname(String fieldEname,
			FunctionRef[] functionRef) {
		String value = fieldEname;
		for (int i = 0; i < functionRef.length; i++) {
			value = value.replaceAll(functionRef[i].getSourceColID(),
					functionRef[i].getRefID());
		}
		int index = value.indexOf("||");
		if (index >= 0)
			value = value.substring(0, index) + "+"
					+ value.substring(index + 2);
		return value;
	}

	/**
	 * ����IGroupAble���ݳ���
	 * 
	 * @param iGroupAble
	 * @param num
	 * @return
	 */
	private IGroupAble[] addGroupAbleArrayLegth(IGroupAble[] iGroupAble, int num) {
		if (iGroupAble == null)
			return null;
		int iCount = iGroupAble.length;
		IGroupAble[] iGroupAbleTmp = new GroupBeanImpl[iCount + 1];
		for (int i = 0; i < iCount; i++) {
			iGroupAbleTmp[i] = iGroupAble[i];
		}
		return iGroupAbleTmp;
	}

	/**
	 * ����з������Ƿ��Ѵ���
	 * 
	 * @return
	 */
	private String checkColIsExist(Object obj, IGroupAble groupBeanImpl) {
		if (obj == null)
			return "";
		if (!(obj instanceof MyGroupValueImpl)) {
			return "";
		}

		String formula = getFormula(groupBeanImpl);
		String formulaTmp;

		IGroupAble[] groupAble = ((MyGroupValueImpl) obj).getGroupAbleArray();
		for (int j = 0; j < groupAble.length; j++) {
			formulaTmp = getFormula(groupAble[j]);
			if (formula.equals(formulaTmp)) {
				return "�����Ѵ��ڣ������ظ����ӣ�";
			}
		}
		return "";
	}

	/**
	 * ����ʽ�滻�ɱ���.�ֶ�����ʽ
	 * 
	 * @param groupAble
	 * @return
	 */
	private String getFormula(IGroupAble groupAble) {
		String sFormula = groupAble.getFormula().getContent().substring(1);
		FunctionRef[] functionRef = groupAble.getFormula()
				.getFunctionRefArray();

		for (int i = 0; i < functionRef.length; i++) {
			sFormula = sFormula.replaceAll(functionRef[i].getRefID(),
					functionRef[i].getSourceID() + "."
							+ functionRef[i].getSourceColID());
		}
		return sFormula;
	}

}
