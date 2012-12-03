/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.table.TablePanel;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.DefinePub;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReportI;
import gov.nbcs.rp.queryreport.definereport.ui.GroupColumnDialog;
import gov.nbcs.rp.queryreport.definereport.ui.MyGroupValueImpl;
import gov.nbcs.rp.queryreport.qrbudget.common.ReportUnt;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Global;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:分组条件设置
 * </p>

 */
public class GroupWhereSetPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private String GROUP_NAME = "groupname";

	private String LEVEL_NUM = "levelnum";

	private String GROUPABLE_OBJECT = "groupobject";

	private String IS_TOTAL = "istotal";

	// 可选
	private FTable ftabCanSelect;

	// 已选
	private FTable ftabAlreadySelect;

	private List lstLevIsTotalField;

	private GroupReport groupReport;

	private ReportQuerySource querySource;

	public GroupWhereSetPanel(GroupReport groupReport) {
		try {
			this.groupReport = groupReport;
			querySource = (ReportQuerySource) ReportConver
					.getReportQuerySource(groupReport);
			init();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "分组条件设置界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 初始化界面
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {

		// 可选
		ftabCanSelect = new TablePanel(new String[][] { { GROUP_NAME, "可选分组信息",
				"200" } }, false);
		ftabCanSelect.setShowRowNumber(true);
		// 已选
		ftabAlreadySelect = new TablePanel(new String[][] { { GROUP_NAME,
				"已选分组信息", "200" } }, false);
		ftabAlreadySelect.setShowRowNumber(true);

		SetButtonPanel setButtonPanel = new SetButtonPanel();
		setButtonPanel.setBorder(null);

		FPanel fpnlRight = new FPanel();
		RowPreferedLayout rLay = new RowPreferedLayout(2);
		rLay.setColumnWidth(50);
		rLay.setColumnGap(1);
		fpnlRight.setLayout(rLay);
		fpnlRight.addControl(setButtonPanel, new TableConstraints(1, 1, true,
				false));
		fpnlRight.addControl(ftabAlreadySelect, new TableConstraints(1, 1,
				true, true));
		fpnlRight.setBorder(null);

		FSplitPane fSplitPane = new FSplitPane();
		fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		fSplitPane.setDividerLocation(260);
		fSplitPane.addControl(ftabCanSelect);
		fSplitPane.addControl(fpnlRight);
		this.setLayout(new RowPreferedLayout(1));
		this.addControl(fSplitPane, new TableConstraints(1, 1, true, true));

	}

	/**
	 * 得到LevIsTotalField值
	 * 
	 * @return
	 */
	public List getLevIsTotalField() {
		if (lstLevIsTotalField == null) {
			lstLevIsTotalField = ReportUnt.geLevIsTotalField(groupReport,
					querySource);
		}
		return lstLevIsTotalField;
	}

	/**
	 * 刷新界面显示
	 * 
	 * @throws Exception
	 */
	public void refresh() throws Exception {
		lstLevIsTotalField = this.getLevIsTotalField();
		// 设置列表信息
		setTable(lstLevIsTotalField);
	}

	/**
	 * 刷新界面显示
	 * 
	 * @throws Exception
	 */
	public void refreshClear() throws Exception {
		lstLevIsTotalField = ReportUnt.geLevIsTotalField(groupReport,
				querySource);
		// 设置列表信息
		setTable(lstLevIsTotalField);
	}

	/**
	 * 设置列表信息
	 * 
	 * @throws Exception
	 * 
	 */
	private void setTable(List lstLev) throws Exception {
		IDefineReport definReportServ = DefineReportI.getMethod();

		// 读文件，更改lstLev信息

		List lstLevOrder = orderIndex(lstLev);

		// 最大节次
		int iMaxLev[] = GroupColumnDialog.getLevInfo(
				getGroupAbleArray(lstLevOrder), definReportServ, querySource);

		int iCount = lstLevOrder.size();
		IGroupAble groupAble;
		List lstCanSelect = new ArrayList();
		List lstAlreadySelect = new ArrayList();

		for (int i = 0; i < iCount; i++) {
			if (lstLevOrder.get(i) instanceof IGroupAble) {
				groupAble = (IGroupAble) lstLevOrder.get(i);

				String sLevel[] = null;
				if (!Common.isNullStr(groupAble.getLevel())) {
					sLevel = groupAble.getLevel().split(",");
				}

				// 增加业务处理
				addDivDep(groupAble, lstCanSelect, lstAlreadySelect, sLevel);
				// 得到汇总节次
				String[] sMaxLevArray = getMaxLev(groupAble, iMaxLev[i]);

				// 显示已选的列
				if (sLevel != null)
					for (int j = 0; j < sLevel.length; j++) {
						String sFieldFname = getFieldFname(groupAble,
								sLevel[j], iMaxLev[i], querySource);
						Map mapRecord = new HashMap();
						mapRecord.put(GROUP_NAME, getBlank(lstAlreadySelect)
								+ sFieldFname);
						mapRecord.put(LEVEL_NUM, sLevel[j]);
						mapRecord.put(GROUPABLE_OBJECT, groupAble);
						lstAlreadySelect.add(mapRecord);
					}

				// 显示已选的列(末级)
				if (Common.estimate(groupAble.getIsTotal())) {
					String sFieldFname = getFieldFname(groupAble, String
							.valueOf(iMaxLev[i]), iMaxLev[i], querySource);
					Map mapRecord = new HashMap();
					mapRecord.put(GROUP_NAME, getBlank(lstAlreadySelect)
							+ sFieldFname);
					mapRecord.put(LEVEL_NUM, IS_TOTAL);
					mapRecord.put(GROUPABLE_OBJECT, groupAble);
					lstAlreadySelect.add(mapRecord);
				}

				// 可选列
				for (int j = 0; j < sMaxLevArray.length; j++) {
					if (j == (sMaxLevArray.length - 1)) {
						if (!Common.estimate(groupAble.getIsTotal())) {
							String sFieldFname = getFieldFname(groupAble,
									sMaxLevArray[j], iMaxLev[i], querySource);
							Map mapRecord = new HashMap();
							mapRecord.put(GROUP_NAME, sFieldFname);
							mapRecord.put(LEVEL_NUM, IS_TOTAL);
							mapRecord.put(GROUPABLE_OBJECT, groupAble);
							lstCanSelect.add(mapRecord);
						}
					} else {
						// 判断此节次是否已选
						if (!isExist(sMaxLevArray[j], sLevel)) {
							String sFieldFname = getFieldFname(groupAble,
									sMaxLevArray[j], iMaxLev[i], querySource);
							Map mapRecord = new HashMap();
							mapRecord.put(GROUP_NAME, sFieldFname);
							mapRecord.put(LEVEL_NUM, sMaxLevArray[j]);
							mapRecord.put(GROUPABLE_OBJECT, groupAble);
							lstCanSelect.add(mapRecord);
						}
					}
				}
			}
		}
		ftabCanSelect.setData(lstCanSelect);
		ftabAlreadySelect.setData(lstAlreadySelect);
	}

	private List orderIndex(List lstLev) {
		if (lstLev == null)
			return null;
		if (lstLev.size() == 0)
			return new ArrayList();
		List lstResult = new ArrayList();
		int size = lstLev.size();
		IGroupAble groupAble;
		int summaryIndex;
		int summaryIndexTmp;
		for (int i = 0; i < size; i++) {
			groupAble = (IGroupAble) lstLev.get(i);
			summaryIndex = Integer.parseInt(groupAble.getSummaryIndex());
			if (lstResult.size() == 0) {
				lstResult.add(groupAble);
			} else {
				for (int k = lstResult.size() - 1; k >= 0; k--) {
					summaryIndexTmp = Integer.parseInt(((IGroupAble) lstResult
							.get(k)).getSummaryIndex());
					if (summaryIndexTmp <= summaryIndex) {
						lstResult.add(k + 1, groupAble);
					} else {
						lstResult.add(k, groupAble);
					}
					break;
				}
			}
		}
		return lstResult;
	}

	private String[] getMaxLev(IGroupAble groupAble, int iMaxLev) {
		String sResult = "";
		// if
		// (IDefineReport.DIV_CODE.equalsIgnoreCase(groupAble.getSourceColID()))
		// {
		// sResult = sResult + IDefineReport.DIV_KIND + ","
		// + IDefineReport.DIV_FMKIND;
		// }
		for (int i = 1; i <= iMaxLev; i++) {
			if (!Common.isNullStr(sResult))
				sResult = sResult + ",";
			sResult = sResult + String.valueOf(i);
		}
		return sResult.split(",");
	}

	/**
	 * 增加业务处理和单位性质汇总
	 * 
	 * @param groupAble
	 * @param lstCanSelect
	 * @param lstAlreadySelect
	 */
	private void addDivDep(IGroupAble groupAble, List lstCanSelect,
			List lstAlreadySelect, String iLevel[]) {
		if (IDefineReport.DIV_CODE.equalsIgnoreCase(groupAble.getSourceColID())) {
			// 判断是否业务处理汇总
			if (Common.estimate(String.valueOf(groupAble.getIsMbSummary()))) {
				Map mapRecord = new HashMap();
				mapRecord.put(GROUP_NAME, getBlank(lstAlreadySelect) + "业务处室");
				lstAlreadySelect.add(mapRecord);
				mapRecord.put(LEVEL_NUM, IDefineReport.DEP_CODE);
				mapRecord.put(GROUPABLE_OBJECT, groupAble);
			} else {
				Map mapRecord = new HashMap();
				mapRecord.put(GROUP_NAME, "业务处室");
				lstCanSelect.add(mapRecord);
				mapRecord.put(LEVEL_NUM, IDefineReport.DEP_CODE);
				mapRecord.put(GROUPABLE_OBJECT, groupAble);
			}
		}
	}

	/**
	 * 得到分组汇总列明称
	 * 
	 * @param groupAble
	 * @param iLev
	 * @param iMaxLev
	 * @param definePub
	 * @param querySource
	 * @return
	 */
	private String getFieldFname(IGroupAble groupAble, String sLev,
			int iMaxLev, ReportQuerySource querySource) {
		if (Common.isNumber(sLev)) {
			if ("DIV_CODE".equals(groupAble.getSourceColID().toUpperCase())) {
				if (Integer.parseInt(sLev) == iMaxLev) {
					return "明细单位";
				} else {
					return Common.GetNumberOfHz(Integer.parseInt(sLev))
							+ "级主管局";
				}
			} else if ("ACCT_CODE".equals(groupAble.getSourceColID()
					.toUpperCase())) {
				switch (Integer.parseInt(sLev)) {
				case 1: {
					return "科目--类";
				}
				case 2: {
					return "科目--款";
				}
				case 3: {
					return "科目--项";
				}
				default: {
					return "科目--"
							+ Common.GetNumberOfHz(Integer.parseInt(sLev))
							+ "级";
				}
				}
			} else {
				String sColName = DefinePub.getDataSourceColNameWithID(
						querySource, groupAble.getSourceID(), groupAble
								.getSourceColID());
				return sColName + "--"
						+ Common.GetNumberOfHz(Integer.parseInt(sLev)) + "级";

			}
		} else {
			if (IDefineReport.DIV_FMKIND.equalsIgnoreCase(sLev)) {
				return "管理方式";
			} else if (IDefineReport.DIV_KIND.equalsIgnoreCase(sLev)) {
				return "单位性质";
			} else {
				return null;
			}
		}
	}

	/**
	 * 空格处理
	 * 
	 * @param xmlData
	 * @return
	 */
	private String getBlank(List lstData) {
		int size = lstData.size();
		return "                          ".substring(0, size * 2);
	}

	/**
	 * 判断sValue在sValueArray数组中值是否存在
	 * 
	 * @param sValue
	 * @param sValueArray
	 * @return
	 */
	private boolean isExist(String sValue, String[] sValueArray) {
		if (sValueArray == null)
			return false;
		int iCount = sValueArray.length;
		for (int i = 0; i < iCount; i++) {
			if (sValue.equalsIgnoreCase(sValueArray[i]))
				return true;
		}
		return false;
	}

	/**
	 * 将List转为数组
	 * 
	 * @param lstGroupAble
	 * @return
	 */
	private IGroupAble[] getGroupAbleArray(List lstGroupAble) {
		int iCount = lstGroupAble.size();
		IGroupAble[] groupAbleArray = new IGroupAble[iCount];
		for (int i = 0; i < iCount; i++) {
			groupAbleArray[i] = (IGroupAble) lstGroupAble.get(i);
		}
		return groupAbleArray;
	}

	/**
	 * 按钮面板
	 * 
	 * @author qzc
	 * 
	 */
	private class SetButtonPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public SetButtonPanel() {
			this.setLayout(new RowPreferedLayout(1));
			FButton fbtnLeftAll = new FButton("fbtnLeftAll", "<<");
			fbtnLeftAll.addActionListener(new moveLeftAllBtnActionListener());
			FButton fbtnLeft = new FButton("fbtnLeftAll", "<");
			fbtnLeft.addActionListener(new moveLeftBtnActionListener());
			FButton fbtnRightAll = new FButton("fbtnLeftAll", ">>");
			fbtnRightAll.addActionListener(new moveRightAllBtnActionListener());
			FButton fbtnRight = new FButton("fbtnLeftAll", ">");
			fbtnRight.addActionListener(new moveRightBtnActionListener());
			FLabel flblEmpty1 = new FLabel();
			FLabel flblEmpty2 = new FLabel();
			this.addControl(flblEmpty1, new TableConstraints(1, 1, true));
			this.addControl(fbtnLeftAll, new TableConstraints(1, 1, false));
			this.addControl(fbtnLeft, new TableConstraints(1, 1, false));
			this.addControl(fbtnRightAll, new TableConstraints(1, 1, false));
			this.addControl(fbtnRight, new TableConstraints(1, 1, false));
			this.addControl(flblEmpty2, new TableConstraints(1, 1, true));
		}
	}

	/**
	 * 拖动按钮事件,全部左移<<
	 * 
	 * @author qzc
	 * 
	 */
	public class moveLeftAllBtnActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			List lstLev = lstLevIsTotalField;
			int size = lstLev.size();
			IGroupAble groupAble;
			for (int i = 0; i < size; i++) {
				groupAble = (IGroupAble) lstLev.get(i);
				groupAble.setLevel("");
				groupAble.setIsMbSummary(0);
				groupAble.setIsTotal("0");
			}
			try {
				setTable(lstLev);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(Global.currentPanel,
						"显示设置分组汇总界面发生错误，错误信息：" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * 拖动按钮事件,左移<
	 * 
	 * @author qzc
	 * 
	 */
	public class moveLeftBtnActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			List lstLev = lstLevIsTotalField;
			int[] indexs = ftabAlreadySelect.getSelectedRowModelIndexes();
			if (indexs.length == 0) {
				JOptionPane.showMessageDialog(Global.currentPanel,
						"请选中需移除的已选列！", "提示", JOptionPane.INFORMATION_MESSAGE);
			}

			IGroupAble groupAble;
			for (int i = 0; i < indexs.length; i++) {
				Map mapSelect = ftabAlreadySelect.getDataByIndex(indexs[i]);

				groupAble = (IGroupAble) mapSelect.get(GROUPABLE_OBJECT);
				if (IS_TOTAL.equals(mapSelect.get(LEVEL_NUM))) {
					groupAble.setIsTotal("0");
				} else if (IDefineReport.DEP_CODE.equals(mapSelect
						.get(LEVEL_NUM))) {
					groupAble.setIsMbSummary(0);
				} else {
					String sLevTmp = delLev(
							mapSelect.get(LEVEL_NUM).toString(), groupAble
									.getLevel());
					groupAble.setLevel(sLevTmp);
				}
			}
			try {
				setTable(lstLev);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(Global.currentPanel,
						"显示设置分组汇总界面发生错误，错误信息：" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * 拖动按钮事件,全部右移>>
	 * 
	 * @author qzc
	 * 
	 */
	public class moveRightAllBtnActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			List lstLev = lstLevIsTotalField;
			List lstData = ftabCanSelect.getData();
			int size = lstData.size();
			IGroupAble groupAble;
			for (int i = 0; i < size; i++) {
				groupAble = (IGroupAble) ((Map) lstData.get(i))
						.get(GROUPABLE_OBJECT);
				if (IS_TOTAL.equals(((Map) lstData.get(i)).get(LEVEL_NUM))) {
					groupAble.setIsTotal("1");
				} else if (IDefineReport.DEP_CODE.equals(((Map) lstData.get(i))
						.get(LEVEL_NUM))) {
					groupAble.setIsMbSummary(1);
				} else {
					String sLevTmp = addLev(((Map) lstData.get(i)).get(
							LEVEL_NUM).toString(), groupAble.getLevel());
					groupAble.setLevel(sLevTmp);
				}
				groupAble.setSummaryIndex(String.valueOf(getSummaryIndex(
						ftabAlreadySelect, groupAble)));
			}
			try {
				setTable(lstLev);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(Global.currentPanel,
						"显示设置分组汇总界面发生错误，错误信息：" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * 拖动按钮事件,右移>
	 * 
	 * @author qzc
	 * 
	 */
	public class moveRightBtnActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			List lstLev = lstLevIsTotalField;
			int[] indexs = ftabCanSelect.getSelectedRowModelIndexes();
			if (indexs.length == 0) {
				JOptionPane.showMessageDialog(Global.currentPanel,
						"请选择需增加的可选列！", "提示", JOptionPane.INFORMATION_MESSAGE);
			}

			IGroupAble groupAble;
			for (int i = 0; i < indexs.length; i++) {
				Map mapSelect = ftabCanSelect.getDataByIndex(indexs[i]);

				groupAble = (IGroupAble) mapSelect.get(GROUPABLE_OBJECT);
				if (IS_TOTAL.equals(mapSelect.get(LEVEL_NUM))) {
					groupAble.setIsTotal("1");
				} else if (IDefineReport.DEP_CODE.equals(mapSelect
						.get(LEVEL_NUM))) {
					groupAble.setIsMbSummary(1);
				} else {
					String sLevTmp = addLev(
							mapSelect.get(LEVEL_NUM).toString(), groupAble
									.getLevel());
					groupAble.setLevel(sLevTmp);
				}
				groupAble.setSummaryIndex(String.valueOf(getSummaryIndex(
						ftabAlreadySelect, groupAble)));
			}
			try {
				setTable(lstLev);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(Global.currentPanel,
						"显示设置分组汇总界面发生错误，错误信息：" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private int getSummaryIndex(FTable ftabAlreadySelect, IGroupAble groupAble) {
		int size = ftabAlreadySelect.getRowCount();
		if (size == 0) {
			return 1;
		} else {
			Map mapData;
			for (int i = 0; i < size; i++) {
				mapData = ftabAlreadySelect.getDataByIndex(i);
				IGroupAble groupAbleTmp = (IGroupAble) mapData
						.get(GROUPABLE_OBJECT);
				if (groupAble.equals(groupAbleTmp)) {
					return Integer.parseInt(groupAbleTmp.getSummaryIndex());
				}
			}

			mapData = ftabAlreadySelect.getDataByIndex(size - 1);
			return Integer
					.parseInt(((IGroupAble) mapData.get(GROUPABLE_OBJECT))
							.getSummaryIndex()) + 1;
		}
	}

	/**
	 * 判断sValue在sValueArray数组中值是否存在
	 * 
	 * @param sValue
	 * @param sValueArray
	 * @return
	 */
	private String delLev(String sValue, String sLevel) {
		if (Common.isNullStr(sLevel))
			return "";
		String sLev[] = sLevel.split(",");
		String sResult = "";
		int iCount = sLev.length;
		for (int i = 0; i < iCount; i++) {
			if (sValue.equals(sLev[i]))
				continue;
			if (!Common.isNullStr(sResult)) {
				sResult = sResult + ",";
			}
			sResult = sResult + sLev[i];
		}
		return sResult;
	}

	/**
	 * 判断sValue在sValueArray数组中值是否存在
	 * 
	 * @param sValue
	 * @param sValueArray
	 * @return
	 */
	private String addLev(String sValue, String sLevel) {
		if (Common.isNullStr(sLevel)) {
			return sValue;
		}
		String sResult = "";
		if (Common.isNumber(sValue)) {
			String sLev[] = sLevel.split(",");
			int iCount = sLev.length;
			boolean isExist = false;
			for (int i = 0; i < iCount; i++) {
				if (Common.isNumber(sLev[i])) {
					if (!isExist
							&& Integer.parseInt(sValue) < Integer
									.parseInt(sLev[i])) {
						isExist = true;
						if (!Common.isNullStr(sResult)) {
							sResult = sResult + ",";
						}
						sResult = sResult + sValue;
					}
				}
				if (!Common.isNullStr(sResult)) {
					sResult = sResult + ",";
				}
				sResult = sResult + sLev[i];

			}
			if (!isExist) {
				if (!Common.isNullStr(sLevel)) {
					sResult = sLevel + ",";
				}
				sResult = sResult + sValue;
			}
		} else {// 判断是否增加的单位管理方和单位性质
			if (!Common.isNullStr(sLevel)) {
				sResult = sLevel + ",";
			}
			sResult = sResult + sValue;
		}
		return sResult;
	}

	/**
	 * 变换分组条件设置
	 * 
	 */
	public void changeLevValue() {
		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
		int col = groupReport.getColumnCount();
		Object value;
		String sID1, sID2;
		IGroupAble groupAbleArrayFrom;
		IGroupAble[] groupAbleArrayTo;
		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value instanceof MyGroupValueImpl) {
				groupAbleArrayTo = ((MyGroupValueImpl) value)
						.getGroupAbleArray();
				for (int j = 0; j < groupAbleArrayTo.length; j++) {
					for (int k = 0; k < lstLevIsTotalField.size(); k++) {
						groupAbleArrayFrom = (IGroupAble) lstLevIsTotalField
								.get(k);
						sID1 = groupAbleArrayTo[j].getSummaryID();
						sID2 = groupAbleArrayFrom.getSummaryID();
						if (sID1.equals(sID2)) {
							int summaryIndex = Integer
									.parseInt(groupAbleArrayFrom
											.getSummaryIndex());
							// 校验其他列summaryIndex值（例如单位性质）
							checkColSummaryIndex(groupAbleArrayTo[j],
									summaryIndex, this.groupReport,
									this.querySource);
							copyLevInfo(groupAbleArrayFrom, groupAbleArrayTo[j]);
							break;
							// delGroupAbleWithoutSet((MyGroupValueImpl) value,
							// groupAbleArrayFrom, i, rowOpe, groupReport);
						}
					}
				}
			}
		}
	}

	/**
	 * 变换分组条件设置
	 * 
	 * @param groupReport
	 */
	public static void changeLevValue(GroupReport groupReport,
			List lstLevIsTotalField, ReportQuerySource querySource) {
		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
		int col = groupReport.getColumnCount();
		Object value;
		String sID1, sID2;
		IGroupAble groupAbleArrayFrom;
		IGroupAble[] groupAbleArrayTo;
		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value instanceof MyGroupValueImpl) {
				groupAbleArrayTo = ((MyGroupValueImpl) value)
						.getGroupAbleArray();
				for (int j = 0; j < groupAbleArrayTo.length; j++) {
					for (int k = 0; k < lstLevIsTotalField.size(); k++) {
						groupAbleArrayFrom = (IGroupAble) lstLevIsTotalField
								.get(k);
						sID1 = groupAbleArrayTo[j].getSummaryID();
						sID2 = groupAbleArrayFrom.getSummaryID();
						if (sID1.equals(sID2)) {
							int summaryIndex = Integer
									.parseInt(groupAbleArrayFrom
											.getSummaryIndex());
							// 校验其他列summaryIndex值（例如单位性质）
							checkColSummaryIndex(groupAbleArrayTo[j],
									summaryIndex, groupReport, querySource);
							copyLevInfo(groupAbleArrayFrom, groupAbleArrayTo[j]);
							break;
							// delGroupAbleWithoutSet((MyGroupValueImpl) value,
							// groupAbleArrayFrom, i, rowOpe, groupReport);
						}
					}
				}
			}
		}
	}

	/**
	 * 校验其他列summaryIndex值（例如单位性质）
	 * 
	 * @param groupAble
	 * @param summaryIndex
	 */
	private static void checkColSummaryIndex(IGroupAble groupAble,
			int summaryIndex, GroupReport groupReport,
			ReportQuerySource querySource) {

		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
		int col = groupReport.getColumnCount();
		Object value;
		IGroupAble[] groupAbleArray;
		int summaryIndexOld = Integer.parseInt(groupAble.getSummaryIndex());
		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value instanceof MyGroupValueImpl) {
				if (value instanceof MyGroupValueImpl) {
					groupAbleArray = ((MyGroupValueImpl) value)
							.getGroupAbleArray();
					for (int j = 0; j < groupAbleArray.length; j++) {
						if (groupAble.equals(groupAbleArray[j])) {
							continue;
						}
						// 枚举字段不校验
						if (DefinePub.judgetEnumWithColID(querySource,
								groupAble.getSourceID(), groupAble
										.getSourceColID(), false)) {
							continue;
						}

						int summaryIndexTmp = Integer
								.parseInt(groupAbleArray[j].getSummaryIndex());
						if (summaryIndexOld != summaryIndexTmp)
							continue;
						groupAbleArray[j].setSummaryIndex(String
								.valueOf(summaryIndex));
					}
				}
			}
		}
	}

	/**
	 * 复制汇总节次
	 * 
	 * @param groupAbleFrom
	 * @param GroupAbleTo
	 */
	private static void copyLevInfo(IGroupAble groupAbleFrom,
			IGroupAble GroupAbleTo) {
		GroupAbleTo.setIsMbSummary(groupAbleFrom.getIsMbSummary());
		GroupAbleTo.setLevel(groupAbleFrom.getLevel());
		GroupAbleTo.setSummaryIndex(groupAbleFrom.getSummaryIndex());
		GroupAbleTo.setIsTotal(groupAbleFrom.getIsTotal());
	}

	/**
	 * 移去未设置汇总的字段
	 * 
	 */
	// private void delGroupAbleWithoutSet(MyGroupValueImpl groupValueImpl,
	// IGroupAble groupAble, int col, int row, GroupReport groupReport) {
	// if (!isSetGroup(groupAble)) {
	// IGroupAble[] groupyAbleArray = groupValueImpl.getGroupAbleArray();
	// int size = groupyAbleArray.length;
	// if ((size - 1) == 0) {
	// groupReport.setCellValue(col, row, null);
	// }
	//
	// IGroupAble[] groupyAbleArrayTmp = new IGroupAble[size - 1];
	// int index = 0;
	// for (int i = 0; i < size; i++) {
	// if (!groupyAbleArray[i].getSummaryID().equals(
	// groupAble.getSummaryID())) {
	// groupyAbleArrayTmp[index] = groupyAbleArray[i];
	// index++;
	// }
	// }
	// }
	// }
	/**
	 * 判断是否设置了
	 * 
	 * @param groupAble
	 * @return
	 */
	// private boolean isSetGroup(IGroupAble groupAble) {
	// if (groupAble.getIsMbSummary() == 1) {
	// return true;
	// }
	// if (Common.estimate(groupAble.getIsTotal())) {
	// return true;
	// }
	// if (!Common.isNullStr(groupAble.getLevel())) {
	// return true;
	// }
	// return false;
	// }
	public String getMsgInfo() {
		String sMsg = "";
		int iRowCount = ftabAlreadySelect.getRowCount();
		for (int i = 0; i < iRowCount; i++) {
			if (!Common.isNullStr(sMsg))
				sMsg = sMsg + ",";
			sMsg = sMsg
					+ ftabAlreadySelect.getValueAt(i, GROUP_NAME).toString()
							.trim();
		}
		return sMsg;
	}

}
