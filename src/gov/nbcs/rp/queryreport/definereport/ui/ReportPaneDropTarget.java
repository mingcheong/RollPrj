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
 * Title:实现选中数据源到报表的拖动实现
 * </p>
 * <p>
 * Description:实现选中数据源到报表的拖动实现
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
	 * (non-Javadoc) 正在进行 drag 操作时调用，此时鼠标指针仍然处于 DropTarget（使用此 listener 注册）的
	 * drop 位置的可操作部分。
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 *      <p>edit by mawenming at 2007-6-6日下午08:00:31
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		// XXL 只有编辑区才可以接受
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
	 * 向分组报表上面拖拽数据
	 * 
	 * @param groupReport
	 * @param cellSelection
	 * @param editRow
	 * @param transferableObj
	 *            <p>
	 *            edit by mawenming at Jun 2, 2007日5:43:40 PM
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
		// 得到字段类型
		String sFieldType = fieldObject.getFIELD_TYPE();

		// 得到当前列号
		int editCol = cellSelection.getColumn();

		// 根据字段不同类型不同处理，整型货币型默认为sum,其他类型为group或max
		if (IDefineReport.INT_TYPE.equals(sFieldType)
				|| IDefineReport.INTT_TYPE.equals(sFieldType)
				|| IDefineReport.CURRENCY_TYPE.equals(sFieldType)
				|| IDefineReport.FLOAT_TYPE.equals(sFieldType)) {// 整型货币

			// 单元格显示内容
			String sValue = "=Sum({" + fieldObject.OBJECT_CNAME + "."
					+ fieldObject.getFIELD_FNAME() + "})";
			// 定义CalculateValueImpl类
			MyCalculateValueImpl myCalculateValueImpl = new MyCalculateValueImpl(
					sValue);

			// 显示小数位数,整型不显示小数位
			if (IDefineReport.INT_TYPE.equals(sFieldType)
					|| IDefineReport.INTT_TYPE.equals(sFieldType)) {
				myCalculateValueImpl.setIntNumber(0);
			} else {
				myCalculateValueImpl.setIntNumber(1);
			}
			// 不需要处理,扩展用
			// myCalculateValueImpl.setIsCorss(0);
			// 是否汇总到业务处室,不需处理，只需设置单位编码列
			// myCalculateValueImpl.setIsMbSummary(0);

			// 单元格显示或隐藏,1:显示，0：隐藏
			myCalculateValueImpl.setIsVisual(1);
			// 设置为计算列
			myCalculateValueImpl
					.setColumnType(ColumnConstants.UITYPE_1_CALCULATE);

			// 定义MeasureAttr类
			MeasureAttr measureAttr = new MeasureAttr();
			// MeasureAttr,id
			String measureID = DefinePub.getRandomUUID();
			measureAttr.setMeasureID(measureID);
			// MeasureAttr数据源id
			measureAttr.setSourceID(fieldObject.DICID);
			// MeasureAttr,列id
			measureAttr.setSourceColID(fieldObject.FIELD_ENAME);
			// MeasureAttr,分组类型,默认为求和(sum)
			measureAttr.setGroupType("sum");
			// MeasureAttr,列类型
			measureAttr.setColType(DefinePub.getFieldTypeWithCh(sFieldType));

			AbstractFormula abstractFormula = new AbstractFormula();
			abstractFormula.setContent("Sum(" + measureID + ")");
			myCalculateValueImpl.setFormula(abstractFormula);

			// MeasureAttr设置给MyCalculateValueImpl
			myCalculateValueImpl
					.setMeasureArray(new IMeasureAttr[] { measureAttr });

			// 将定义GroupValueImpl类付值给单元格
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
			// 定义单元格高亮条件，目的：当单元格值为零时不显示
			HighlightGroup highlightGroup = new CustomHighlightGroup();
			cellElement.setHighlightGroup(highlightGroup);

		} else if (IDefineReport.CHAR_TYPE.equals(sFieldType)
				|| IDefineReport.CHAR_TYPE_A.equals(sFieldType)
				|| IDefineReport.DATE_TYPE.equals(sFieldType)
				|| sFieldType == null || "".equals(sFieldType)) {// 其他类型,字符串、日期型

			// 定义GroupBeanImpl对象
			IGroupAble groupBeanImpl = new GroupBeanImpl();
			// ID
			groupBeanImpl.setSummaryID(DefinePub.getRandomUUID());

			// 数据源ID
			groupBeanImpl.setSourceID(fieldObject.DICID);

			// 得到数据源字段值
			String[] sourceColID = getSourceColIdWithEname(fieldObject.FIELD_ENAME);

			// 字段ID
			groupBeanImpl.setSourceColID(sourceColID[0]);

			// 转换成存入XML文件中字段类型值
			String sFieldTypeVal = DefinePub.getFieldTypeWithCh(sFieldType);
			// 是否向上汇总,1:向上汇总，0:不向上汇总
			groupBeanImpl.setIsTotal("0");
			// 字段类型
			groupBeanImpl.setColType(sFieldTypeVal);
			// 显示类型
			groupBeanImpl.setDispType(sFieldTypeVal);
			// 排序字段先后标记,0,1,2,3 标识
			groupBeanImpl.setOrderIndex("");
			// 排序类型,升序或降序
			// groupBeanImpl.setOrderType("ASC");
			// 是否汇总到业处室
			groupBeanImpl.setIsMbSummary(0);
			// 单元格显示或隐藏,1:显示，0：隐藏
			groupBeanImpl.setIsVisual(1);

			// 判断字段是不是枚举字段(不区分是不是主键值)
			ReportQuerySource querySource = reportGuideUI.querySource;
			boolean bIsEnum = DefinePub.judgetEnumWithColID(querySource,
					fieldObject.DICID, fieldObject.FIELD_ENAME, true);
			// 类型，是Group或Max，0:group,1:max
			if (bIsEnum) {
				groupBeanImpl.setIsMax(0);
			} else {
				groupBeanImpl.setIsMax(1);
			}

			// 单元格显示内容
			String sShowValue = "{" + fieldObject.OBJECT_CNAME + "."
					+ fieldObject.FIELD_FNAME + "}";

			int iCount = sourceColID.length;
			FunctionRef[] functionRef = new FunctionRef[iCount];
			for (int i = 0; i < iCount; i++) {
				// 设置公式元素
				functionRef[i] = new FunctionRef();
				functionRef[i].setRefID(DefinePub.getRandomUUID());
				functionRef[i].setSourceID(fieldObject.DICID);
				functionRef[i].setSourceColID(sourceColID[i]);
			}

			// 设置公式
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

			// 得到当前单元格数据
			MyGroupValueImpl groupValueImpl = null;
			boolean isMultiGroup = false;
			// 判断单元格类型，如果类型相同且是分组列，提示是否替换，不替换则追加
			CellElement cell = groupReport.getCellElement(editCol, editRow);
			if (cell != null && cell.getValue() instanceof MyGroupValueImpl) { // 相同类型
				// 提示是否确定删除
				if (JOptionPane
						.showConfirmDialog(reportPane,
								"替换当前单元格信息吗?点击‘是’替换，点击‘否’追加。", "提示",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {// 替换
					// 定义GroupValueImpl类
					groupValueImpl = new MyGroupValueImpl("=" + sShowValue);
					groupValueImpl
							.setGroupAbleArray(new IGroupAble[] { groupBeanImpl });
				} else {// 追加
					// 得到当前单元格值
					groupValueImpl = (MyGroupValueImpl) cell.getValue();
					// 增加数组长度
					IGroupAble[] iGroupAble = addGroupAbleArrayLegth(
							groupValueImpl.getGroupAbleArray(), 1);
					// 将新增加的值追加到数据最后面
					iGroupAble[iGroupAble.length - 1] = groupBeanImpl;
					groupValueImpl.setGroupAbleArray(iGroupAble);
					groupValueImpl.setSValue(groupValueImpl.getSValue() + "\n"
							+ sShowValue);
					isMultiGroup = true;
				}
			} else {// 不同类型,直接替换
				// 定义GroupValueImpl类
				groupValueImpl = new MyGroupValueImpl("=" + sShowValue);
				groupValueImpl
						.setGroupAbleArray(new IGroupAble[] { groupBeanImpl });

			}

			// 向上汇总字段先后标记,1,2,3 标识
			try {
				groupBeanImpl.setSummaryIndex(String.valueOf(this
						.getSummaryIndex(bIsEnum, isMultiGroup, groupReport)));
			} catch (SummaryConverException e) {
				e.printStackTrace();
				new MessageBox("发生错误，错误信息：" + e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

			// 将定义GroupValueImpl类付值给单元格
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

			// 校验枚举summaryIndex值
			CheckOutEnumName checkOutEnumName = new CheckOutEnumName(
					querySource);
			checkOutEnumName.checkEnumSummaryIndex(groupReport);

			// 刷新向中汇总显示顺序
			reportGuideUI.reportGroupSet.showGroupSet(groupReport);
		} else {
			new MessageBox("无法处理的数据类型！", MessageBox.INFOMATION,
					MessageBox.BUTTON_OK).show();
		}
		// 刷新显示
		reportPane.fireReportDataChanged();
	}

	/**
	 * 根据报表定义情况，得到summaryIndex值
	 * 
	 * @param bIsEnum是否枚举主键
	 * @param isMultiGroup一单元格是否多个groupAble值
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
	 * 根据Ename值得到数据列值
	 * 
	 * @param fieldEname
	 * @return
	 */
	private String[] getSourceColIdWithEname(String fieldEname) {
		// 处理编码加名称
		int index = fieldEname.indexOf("||");
		if (fieldEname.indexOf("||") >= 0) {
			return new String[] { fieldEname.substring(0, index),
					fieldEname.substring(index + 2) };
		}
		// 处理类、款、项问题
		index = fieldEname.toLowerCase().indexOf(GroupColumnDialog.OPER_SUBSTR);
		if (index >= 0) {
			String value = fieldEname.substring(index + 7, fieldEname
					.indexOf(","));
			return new String[] { value };
		}
		return new String[] { fieldEname };
	}

	/**
	 * 根据Ename值得到公式
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
	 * 增加IGroupAble数据长度
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
	 * 检查列分组列是否已存在
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
				return "该列已存在，不允重复增加！";
			}
		}
		return "";
	}

	/**
	 * 将公式替换成表名.字段名格式
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
