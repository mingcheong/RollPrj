/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysrefcol.ui.SysRefColPub;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.reportcy.summary.exception.SummaryConverException;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSource;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.AbstractFormula;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.fr.base.Constants;
import com.fr.dialog.BaseDialog;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;
import com.fr.report.cellElement.CellGroupAttr;

/**
 * <p>
 * Title:数据列信息设置客服端页面
 * </p>
 * <p>
 * Description:数据列信息设置客服端页面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-27
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class GroupColumnDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	public final static String OPER_SUBSTR = "substr";

	// 定义分组列
	private FList flstGroupArray;

	// 是否分组
	private FCheckBox fchkIsMax = null;

	// 是否隐藏
	private FCheckBox fchkIsVisual = null;

	// 定义排序单选框
	private FCheckBox fchkOrder = null;

	// 无数据仍显示
	private FCheckBox fchkIsAll = null;

	private CellElement cellElement = null;

	private MyGroupValueImpl myGroupValueImpl = null;

	private JFrame frame = null;

	private GroupReport groupReport = null;

	// 只存当前分组列所有行
	private int curIndex = -1;

	// 界面刷新标记
	private boolean refreshFlag = true;

	private ReportGuideUI reportGuideUI = null;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	private ReportQuerySource querySource = null;

	// 汇总节次
	private CheckBoxList lstLevel;

	// 最大节次
	int maxLev[];

	/**
	 * 构造函数
	 */
	public GroupColumnDialog(JFrame frame) {
		super(frame);
		this.frame = frame;
		this.setSize(600, 650);
		this.setTitle("分组列");
		this.setModal(true);
		this.reportGuideUI = (ReportGuideUI) frame;
		this.groupReport = reportGuideUI.fpnlDefineReport.groupReport;
		this.definReportServ = reportGuideUI.definReportServ;
		this.querySource = reportGuideUI.querySource;

		// 初始化界面
		init();
	}

	public GroupColumnDialog(Dialog dialog) {
		super(dialog);
		this.setSize(600, 650);
		this.setTitle("分组列");
		this.setModal(true);
		init();
	}

	/**
	 * 初始化界面
	 * 
	 */
	private void init() {
		// 定义分组列
		flstGroupArray = new FList();
		flstGroupArray.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent arg0) {

				// 保存当前页面上内容
				if (curIndex > -1) {

					GroupObj groupObj = (GroupObj) ((DefaultListModel) flstGroupArray
							.getModel()).get(curIndex);
					// 原值
					IGroupAble iGroupAbleOld = (IGroupAble) groupObj
							.getObject();
					String sErr = saveGroupAble(iGroupAbleOld);

					if (!Common.isNullStr(sErr)) {
						new MessageBox(frame, sErr, MessageBox.MESSAGE,
								MessageBox.BUTTON_OK).show();
						flstGroupArray.setSelectedIndex(curIndex);
						refreshFlag = false;
						return;
					}

					cellElement.setValue(myGroupValueImpl);

				}

				IGroupAble[] iGroupAbleArray = myGroupValueImpl
						.getGroupAbleArray();
				IGroupAble iGroupAble = iGroupAbleArray[flstGroupArray
						.getSelectedIndex()];
				// 显示选中列信息
				if (refreshFlag) {
					refreshFlag = true;
					showDetail(iGroupAble, flstGroupArray.getSelectedIndex());
				}
				curIndex = flstGroupArray.getSelectedIndex();

				// 设置是否可编辑
				// 判断是否设置了对应关系
				if (!DefinePub.judgetEnumWithColID(querySource, iGroupAble
						.getSourceID(), iGroupAble.getSourceColID(), true)) {
					fchkIsAll.setEnabled(false);
				} else {
					fchkIsAll.setEnabled(true);
				}
			}
		});

		FScrollPane fspnlGroupyArray = new FScrollPane(flstGroupArray);
		// 定义分组列面板
		FPanel fpnlGroupyArray = new FPanel();
		fpnlGroupyArray.setTitle("字段列");
		fpnlGroupyArray.setLayout(new RowPreferedLayout(1));
		fpnlGroupyArray.addControl(fspnlGroupyArray);

		// 是否隐藏
		fchkIsVisual = new FCheckBox("隐藏列");
		fchkIsVisual.setTitlePosition("RIGHT");

		// 定义排序
		fchkOrder = new FCheckBox("作为排序列");
		fchkOrder.setTitlePosition("RIGHT");

		// 是否分组
		fchkIsMax = new FCheckBox("分组汇总(Group)");
		fchkIsMax.setTitlePosition("RIGHT");
		fchkIsMax.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent evt) {
				// 设置控件状态（可用不可用)
				setControlState(evt.getNewValue());
			}
		});

		// 是否显示所有单位
		fchkIsAll = new FCheckBox("无数据仍显示");
		fchkIsAll.setTitlePosition("RIGHT");

		lstLevel = new CheckBoxList();
		lstLevel.setIsCheckBoxEnabled(false);

		// 汇总设置面板
		FPanel fpnlTotal = new FPanel();
		fpnlTotal.setTitle("汇总级次选择");
		fpnlTotal.setLayout(new RowPreferedLayout(1));
		fpnlTotal.add(lstLevel, new TableConstraints(1, 1, true, true));

		BtnPanel btnPanel = new BtnPanel();

		// 定义主面板
		FPanel fpnlMain = new FPanel();
		fpnlMain.setLayout(new RowPreferedLayout(1));
		// 分组列
		fpnlMain.addControl(fpnlGroupyArray, new TableConstraints(1, 1, true,
				true));
		// 排序面板
		fpnlMain.addControl(fchkOrder, new TableConstraints(1, 1, false, true));
		// 是否汇总
		fpnlMain.add(fchkIsMax, new TableConstraints(1, 1, false, true));
		// 汇总节次面板
		fpnlMain.addControl(fpnlTotal, new TableConstraints(6, 1, false, true));
		// 是否显示所有单位
		fpnlMain.addControl(fchkIsAll, new TableConstraints(1, 1, false, true));
		// 隐藏列
		fpnlMain.addControl(fchkIsVisual, new TableConstraints(1, 1, false,
				true));

		// 按钮面板
		fpnlMain.addControl(btnPanel, new TableConstraints(2, 1, false, true));
		// 设置边距
		fpnlMain.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		this.getContentPane().add(fpnlMain);
	}

	/**
	 * 得到最大节次
	 * 
	 * @param groupAble
	 * @return
	 * @throws Exception
	 */
	public static int[] getLevInfo(IGroupAble[] groupAble,
			IDefineReport definReportServ, ReportQuerySource querySource)
			throws Exception {
		int length = groupAble.length;
		String[] sTableName = new String[length];
		String[] sLevelCodeArray = new String[length];
		String[] sLevelInfoArray = new String[length];

		String enumID;
		IEnumSource enumSource;
		for (int i = 0; i < length; i++) {
			// 判断是否是类款项
			if (groupAble[i].getContent().toLowerCase().indexOf(OPER_SUBSTR) != -1)
				continue;
			// 判断是否是编码列
			if (!DefinePub.judgetEnumWithColID(querySource, groupAble[i]
					.getSourceID(), groupAble[i].getSourceColID(), true)) {
				continue;
			}

			enumID = DefinePub.getEnumIDWithColID(querySource, groupAble[i]
					.getSourceID(), groupAble[i].getSourceColID());
			if (enumID == null) {
				sTableName[i] = "";
				continue;
			}
			// 得到枚举
			enumSource = DefinePub.getEnumInfoWithID(querySource, enumID);
			sTableName[i] = SysRefColPub.ReplaceRefColFixFlag(enumSource
					.getSource().toString().toUpperCase());
			sLevelCodeArray[i] = enumSource.getLevelCode();
			sLevelInfoArray[i] = enumSource.getLevelInfo();
		}

		// 最大节次
		return definReportServ.getMaxLevel(sTableName, sLevelCodeArray,
				sLevelInfoArray);
	}

	/**
	 * 设置控件状态
	 * 
	 * @param obj
	 */
	private void setControlState(Object obj) {
		if (Common.estimate(obj)) {
			lstLevel.setIsCheckBoxEnabled(true);
		} else {
			lstLevel.setIsCheckBoxEnabled(false);
			lstLevel.cancelSelected();
		}
	}

	/**
	 * 定义按钮类
	 */
	private class BtnPanel extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		public BtnPanel() {
			// 设置靠右显示
			this.setAlignment(FlowLayout.RIGHT);

			// 定义“确定”按钮
			FButton okBtn = new FButton("saveBtn", "保存");
			okBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					if (curIndex >= 0) {
						doOK();
						// 刷新显示排序
						reportGuideUI.reportOrderSet.showOrderSet(groupReport);

						// 校验枚举名称summaryIndex值
						try {
							// 定义querySource
							ReportQuerySource querySource = (ReportQuerySource) ReportConver
									.getReportQuerySource(groupReport);
							// 校验枚举summaryIndex值
							CheckOutEnumName checkOutEnumName = new CheckOutEnumName(
									querySource);
							checkOutEnumName.checkEnumSummaryIndex(groupReport);
						} catch (SummaryConverException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(
									GroupColumnDialog.this, " 发生错误，错误信息："
											+ e.getMessage(), "提示",
									JOptionPane.ERROR_MESSAGE);
						}

						// 刷新向中汇总显示顺序
						reportGuideUI.reportGroupSet.showGroupSet(groupReport);
					}

					cellElement.getStyle().deriveHorizontalAlignment(
							Constants.LEFT);
					// 设置高亮条件为null
					cellElement.setHighlightGroup(null);
					GroupColumnDialog.this.setVisible(false);
					GroupColumnDialog.this.dispose();
				}

			});
			// 定义”取消“按钮
			FButton cancelBtn = new FButton("cancelBtn", "取 消");
			// 实现“取消”按钮点击事件
			cancelBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					doCancel();
				}

			});

			// “确定”按钮加入按钮面板
			this.addControl(okBtn);
			// “取消”按钮加入按钮面板
			this.addControl(cancelBtn);
		}
	}

	class FuncListObject {

		private String name;

		private String sCHName;

		public FuncListObject(String name, String sCHName) {
			this.name = name;
			this.sCHName = sCHName;
		}

		public String toString() {
			return sCHName;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * 注入，显示信息
	 * 
	 * @param cellElement
	 * @param value
	 */
	void populate(CellElement cellElement, Object value) {
		this.cellElement = cellElement;

		if (value instanceof MyGroupValueImpl) {
			// 克隆一个MyGroupValueImpl对象，在此对象上操作
			myGroupValueImpl = (MyGroupValueImpl) ((MyGroupValueImpl) value)
					.clone();

			IGroupAble[] iGroupAbleArray = myGroupValueImpl.getGroupAbleArray();
			if (iGroupAbleArray == null)
				return;
			int iCount = iGroupAbleArray.length;
			String sDisplayValue = null;
			IGroupAble iGroupAble = null;
			GroupObj groupObj;
			DefaultListModel listModel = new DefaultListModel();

			// 循环取得IGroupAble[]数组中信息
			for (int i = 0; i < iCount; i++) {
				iGroupAble = iGroupAbleArray[i];
				sDisplayValue = ((AbstractFormula) iGroupAble.getFormula())
						.getDisplayValue();
				groupObj = new GroupObj(sDisplayValue, iGroupAble);
				listModel.addElement(groupObj);
			}
			// 设置字段列表值
			flstGroupArray.setModel(listModel);

			// 得到最大节次
			try {
				maxLev = getLevInfo(iGroupAbleArray, definReportServ,
						querySource);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(GroupColumnDialog.this,
						"获得最大节次出现问题，问题信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

			// 定位到第一节点
			if (listModel.getSize() > 0) {
				flstGroupArray.setSelectedIndex(0);
			}

		}
	}

	private class GroupObj {

		private String value = "";

		private Object object = null;

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public GroupObj(String value, Object object) {
			this.value = value;
			this.object = object;
		}

		public String toString() {
			return value;
		}

	}

	/**
	 * 显示明细信息
	 * 
	 * @param iGroupAble
	 */
	private void showDetail(IGroupAble iGroupAble, int index) {
		// 是否分组
		((JCheckBox) fchkIsMax.getEditor()).setSelected(!Common
				.estimate(new Integer(iGroupAble.getIsMax())));
		// 是否隐藏
		((JCheckBox) fchkIsVisual.getEditor()).setSelected(!Common
				.estimate(new Integer(iGroupAble.getIsVisual())));
		// 是否显示所有单位
		((JCheckBox) fchkIsAll.getEditor()).setSelected(Common
				.estimate(new Integer(iGroupAble.getIsCorss())));
		// 定义排序单选框
		if (iGroupAble.getOrderIndex() == null
				|| "".equals(iGroupAble.getOrderIndex())) {
			((JCheckBox) fchkOrder.getEditor()).setSelected(false);
		} else {
			((JCheckBox) fchkOrder.getEditor()).setSelected(true);
		}

		int lev = maxLev[index];
		int depFlag = 0;

		// 判断是否加业务处室
		if (IDefineReport.DIV_CODE
				.equalsIgnoreCase(iGroupAble.getSourceColID())) {
			depFlag = 1;
		}

		InstallData[] levData = new InstallData[lev + depFlag];
		if (depFlag == 1) {
			levData[0] = new InstallData("业务处室(单位编码列可选)",
					IDefineReport.DEP_CODE);
		}
		for (int i = 0; i < lev; i++) {
			if (i == lev) {
				levData[i + depFlag] = new InstallData("末级", new Integer(i + 1));
			} else {
				levData[i + depFlag] = new InstallData("第" + (i + 1) + "级",
						new Integer(i + 1));
			}
		}

		lstLevel.setData(levData);

		List lstRow = null;
		// 业务处室
		if (iGroupAble.getIsMbSummary() == 1) {
			if (lstRow == null) {
				lstRow = new ArrayList();
			}
			lstRow.add(IDefineReport.DEP_CODE);
		}

		// 节次
		String level = iGroupAble.getLevel();
		String[] levArray;
		if (!Common.isNullStr(level)) {
			levArray = level.split(",");
			int length = levArray.length;
			for (int i = 0; i < length; i++) {
				if (lstRow == null) {
					lstRow = new ArrayList();
				}
				lstRow.add(levArray[i]);
			}
		}

		// 汇总到末级
		if (Common.estimate(iGroupAble.getIsTotal())) {
			if (lstRow == null) {
				lstRow = new ArrayList();
			}
			lstRow.add(String.valueOf(lev));
		}

		lstLevel.setSelected(lstRow, true);
	}

	/**
	 * 组织界面显示内容
	 * 
	 * @param iGroupAble
	 * @return
	 */
	private String getShowValue(MyGroupValueImpl myGroupValueImpl) {

		IGroupAble aIGroupAbleArray[] = myGroupValueImpl.getGroupAbleArray();
		IGroupAble iGroupAble = null;
		int count = aIGroupAbleArray.length;
		String value = "";
		String valueTmp = "";
		for (int i = 0; i < count; i++) {
			iGroupAble = aIGroupAbleArray[i];

			// 判断这个字段是否排序
			if (iGroupAble.getOrderIndex() != null
					&& !"".equals(iGroupAble.getOrderIndex())) {// 排序
				if (IDefineReport.ASC_FLAG.equals(iGroupAble.getOrderType())) {// 升序
					valueTmp = IDefineReport.ASC_ARROW;
				} else if (IDefineReport.DESC_FLAG.equals(iGroupAble
						.getOrderType())) {// 降序
					valueTmp = IDefineReport.DESC_ARROW;
				}
			} else {// 不排序
				valueTmp = "";
			}

			if (iGroupAble.getIsMax() == 0) {// 分组Group
				valueTmp = valueTmp
						+ "Group("
						+ ((AbstractFormula) iGroupAble.getFormula())
								.getDisplayValue() + ")";
			} else {// 求Max
				valueTmp = valueTmp
						+ ((AbstractFormula) iGroupAble.getFormula())
								.getDisplayValue();
			}
			if (Common.isNullStr(value)) {
				value = valueTmp;
			} else {
				value = value + "\n" + valueTmp;
			}
			if (i == 0) {
				value = "=" + value;
			}

		}
		return value;
	}

	protected void doCancel() {
		this.setVisible(false);
		this.dispose();

	}

	/**
	 * 保存IGroupAble内容信息
	 * 
	 * @param iGroupAble
	 * @throws NumberFormatException
	 * @throws SummaryConverException
	 */
	private String saveGroupAble(IGroupAble iGroupAble) {

		// 原groupAble类型
		int oldType = DefinePubOther.getGroupAbleType(iGroupAble);

		InstallData[] dataArray = lstLevel.getData();
		int size = dataArray.length;
		if (size == 0) {
			// 是否显示小计
			iGroupAble.setIsTotal("0");
			// 向上汇总节次
			iGroupAble.setLevel("");
			// 是否汇总到业务处室
			iGroupAble.setIsMbSummary(0);
		} else {
			// 是否显示小计
			InstallData data = dataArray[size - 1];
			iGroupAble.setIsTotal(data.isSelected() ? "1" : "0");

			// 向上汇总节次
			String level = "";
			for (int i = 0; i < size - 1; i++) {
				data = dataArray[i];
				if (IDefineReport.DEP_CODE.equals(data.getValue())) {
					continue;
				}
				if (data.isSelected()) {
					if (!Common.isNullStr(level))
						level = level + ",";
					level = level + data.getValue();
				}
			}
			iGroupAble.setLevel(level);

			// 是否汇总到业处室
			data = dataArray[0];
			if (IDefineReport.DEP_CODE.equals(data.getValue())) {
				iGroupAble.setIsMbSummary(data.isSelected() ? 1 : 0);
			} else {
				iGroupAble.setIsMbSummary(0);
			}
		}

		// 新groupAble类型,
		int newType = DefinePubOther.getGroupAbleType(iGroupAble);
		// 不可放到IsMax后面，原因需得到最大分组summaryIndex
		if (newType != DefinePubOther.LEVISTOTAL_TYPE) {
			if (Common.estimate(fchkIsMax.getValue().toString())) {
				newType = DefinePubOther.GROUP_TYPE;
			} else {
				newType = DefinePubOther.DETAIL_TYPE;
			}
		}
		// 向上汇总字段先后标记,1,2,3 标识summaryIndex
		SummaryIndexSet.setSummaryIndexChangeType(querySource, groupReport,
				iGroupAble, oldType, newType);

		// 排序字段先后标记,1,2,3 标识
		if (Common.estimate(fchkOrder.getValue())) {
			if (Common.isNullStr(iGroupAble.getOrderIndex())) {
				iGroupAble.setOrderIndex(DefinePub.getOrderIndex(groupReport));
				// 排序类型,升序或降序
				iGroupAble.setOrderType(IDefineReport.ASC_FLAG);
			}
		} else {
			iGroupAble.setOrderIndex("");
			iGroupAble.setOrderType("");
		}

		// 单元格显示或隐藏,1:显示，0：隐藏
		IGroupAble[] groupAble = myGroupValueImpl.getGroupAbleArray();
		for (int i = 0; i < groupAble.length; i++) {
			groupAble[i].setIsVisual("false".equals(fchkIsVisual.getValue()
					.toString()) ? 1 : 0);
		}

		// 类型，是Group或Max，0:group,1:max
		iGroupAble.setIsMax("false".equals(fchkIsMax.getValue().toString()) ? 1
				: 0);
		iGroupAble
				.setIsCorss("true".equals(fchkIsAll.getValue().toString()) ? 1
						: 0);

		return "";
	}

	/**
	 * 保存必须调用方法
	 */
	protected void doOK() {

		GroupObj groupObj = (GroupObj) ((DefaultListModel) flstGroupArray
				.getModel()).get(curIndex);
		// 原值
		IGroupAble iGroupAble = (IGroupAble) groupObj.getObject();
		String sErr = saveGroupAble(iGroupAble);
		if (!Common.isNullStr(sErr)) {
			new MessageBox(frame, sErr, MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}

		String sShowValue = getShowValue(myGroupValueImpl);
		myGroupValueImpl.setSValue(sShowValue);
		cellElement.setValue(myGroupValueImpl);

		// 判断是否是分组列，如是分组列设置为:内容合并属性(相同行显示第一行的值)
		// peter: 分组属性,
		CellGroupAttr cellGroupAttr = cellElement.getCellGroupAttr();
		if (cellGroupAttr == null) {
			cellGroupAttr = new CellGroupAttr();
		}
		if (judgetIsLevel(myGroupValueImpl)) {
			// 相同行显示第一行的值
			cellGroupAttr.setMergeSameCellsVertically(true);
			cellGroupAttr.setHideSameCellVertically(true);
		} else {
			cellGroupAttr.setMergeSameCellsVertically(false);
			cellGroupAttr.setHideSameCellVertically(false);
		}

		cellElement.setCellGroupAttr(cellGroupAttr);

	}

	/**
	 * 判断是否向上汇总
	 * 
	 * @param myGroupValueImpl
	 * @return
	 */
	private boolean judgetIsLevel(MyGroupValueImpl myGroupValueImpl) {
		boolean bResult = false;
		IGroupAble iGroupAble[] = myGroupValueImpl.getGroupAbleArray();
		int iCount = iGroupAble.length;
		for (int i = 0; i < iCount; i++) {
			if (!Common.isNullStr(String.valueOf(iGroupAble[i].getLevel()))) {
				return true;
			}
		}
		return bResult;
	}

	public static void main(String a[]) {
		char c = 0x2191;
		c = 0x5355;
		c = 0x6536;
		System.out.println(c);
	}

}
