/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.DefaultFComboBoxModel;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FComboBoxItem;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.summary.object.source.RefSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceRelationsImpl;
import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:分组报表定义,数据源关系设置客服端页面
 * </p>
 * <p>
 * Description:分组报表定义,数据源关系设置客服端页面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-25
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class ReportDSRelaUI extends FDialog {

	private static final long serialVersionUID = 1L;

	private ReportGuideUI reportGuideUI = null;

	// 定义数据源1
	private FComboBox fcbxDataSourceA = null;

	// 定义数据源2
	private FComboBox fcbxDataSourceB = null;

	// 数据源A字段表格
	private FTable ftabDataSourceA = null;

	// 数据源B字段表格
	private FTable ftabDataSourceB = null;

	// 数据源值
	private String sDataSourceRef = "";

	private String sOldSourceIDA = null;

	private String sOldSourceIDB = null;

	private String sCurSourceIDA = null;

	private String sCurSourceIDB = null;

	// 关联关系树
	MyTree treRela;

	// 1:点击OK按钮，0:取消按钮
	private int resultMode = 0;

	/**
	 * 构造函数
	 * 
	 */
	public ReportDSRelaUI(ReportGuideUI reportGuideUI) {
		super(reportGuideUI);
		this.setSize(700, 525);
		this.setTitle("数据源关联关系设置");
		this.setModal(true);

		this.reportGuideUI = reportGuideUI;

		// 调用界面初始化方法
		init();
	}

	/**
	 * 初始化界面
	 * 
	 */
	private void init() {
		// 得到数据源列表
		List lstDataSource = DefinePub.getDataSource(reportGuideUI.querySource);

		// 获得Vector值
		Map dataSourceMap = null;
		if (lstDataSource != null) {
			for (int i = 0; i < lstDataSource.size(); i++) {
				dataSourceMap = (Map) lstDataSource.get(i);
				if (!"".equals(sDataSourceRef)) {
					sDataSourceRef = sDataSourceRef + "+";
				}
				sDataSourceRef = sDataSourceRef
						+ dataSourceMap.get(IDefineReport.SOURCE_ID) + "#"
						+ dataSourceMap.get(IDefineReport.DATASOURCE_NAME);
			}
		}

		// 定义数据源1
		fcbxDataSourceA = new FComboBox("数据源1：");
		fcbxDataSourceA.setRefModel(sDataSourceRef);
		fcbxDataSourceA.setSelectedIndex(-1);
		((JComboBox) fcbxDataSourceA.getComponent(1))
				.addItemListener(new SelectAItemListener());

		// 定义数据源2
		fcbxDataSourceB = new FComboBox("数据源2：");
		fcbxDataSourceB.setRefModel(sDataSourceRef);
		fcbxDataSourceB.setSelectedIndex(-1);
		((JComboBox) fcbxDataSourceB.getComponent(1))
				.addItemListener(new SelectBItemListener());

		// 数据源A字段表格
		ftabDataSourceA = new MyFTable();
		// 数据源B字段表格
		ftabDataSourceB = new MyFTable();

		// 得到根节点
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("关联关系");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		treRela = new MyTree(treeModel);
		JScrollPane fpnltalRela = new JScrollPane(treRela);

		// 关联设置
		FButton addBtn = new FButton("addBtn", "增加");
		addBtn.addActionListener(new AddActionListener());
		FButton delBtn = new FButton("delBtn", "删除");
		delBtn.addActionListener(new DelActionListener());
		// 定义按钮面板
		FFlowLayoutPanel operBtnPnl = new FFlowLayoutPanel();
		operBtnPnl.addControl(addBtn, new TableConstraints(1, 1, true, false));
		operBtnPnl.addControl(delBtn, new TableConstraints(1, 1, true, false));
		// 表面板
		FPanel fpnlTable = new FPanel();
		RowPreferedLayout rLay = new RowPreferedLayout(2);
		rLay.setColumnWidth(60);
		rLay.setColumnGap(1);
		fpnlTable.setLayout(rLay);
		fpnlTable.add(fpnltalRela, new TableConstraints(1, 1, true, true));
		fpnlTable.addControl(operBtnPnl,
				new TableConstraints(1, 1, true, false));

		BtnPanel btnPanel = new BtnPanel();

		FPanel fpnlMain = new FPanel();
		fpnlMain.setTitle("数据源关联关系设置");
		fpnlMain.setLayout(new RowPreferedLayout(2));
		fpnlMain.addControl(fcbxDataSourceA, new TableConstraints(1, 1, false,
				true));
		fpnlMain.addControl(fcbxDataSourceB, new TableConstraints(1, 1, false,
				true));
		fpnlMain.addControl(ftabDataSourceA, new TableConstraints(10, 1, false,
				true));
		fpnlMain.addControl(ftabDataSourceB, new TableConstraints(10, 1, false,
				true));
		fpnlMain.addControl(fpnlTable, new TableConstraints(1, 2, true, true));

		FPanel fpnlPane = new FPanel();
		RowPreferedLayout rLayPanel = new RowPreferedLayout(1);
		rLayPanel.setRowHeight(35);
		fpnlPane.setLayout(rLayPanel);
		fpnlPane.addControl(fpnlMain, new TableConstraints(1, 1, true, true));
		fpnlPane.addControl(btnPanel, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(fpnlPane);

	}

	/**
	 * 设置表格列
	 * 
	 * @param ftable需设置的表格
	 */
	private class MyFTable extends FTable {

		private static final long serialVersionUID = 1L;

		public MyFTable() {
			super();
			init();
		}

		public MyFTable(boolean arg0) {
			super(arg0);
			init();
		}

		private void init() {
			String[][] columns = new String[][] {
					{ IDefineReport.FIELD_FNAME, "字段名", "150" },
					{ IDefineReport.FIELD_TYPE, "字段类型", "80" } };

			for (int i = 0; i < columns.length; i++) {
				String[] col = columns[i];
				FBaseTableColumn column = new FBaseTableColumn();
				if ("joinType".equals(col[0])) {
					column.setEditable(true);
				}
				column.setId(col[0]);
				column.setTitle(col[1]);
				if (col.length == 3) {
					column.setWidth(Integer.parseInt(col[2]));
					column.setPreferredWidth(Integer.parseInt(col[2]));
					column.setMaxWidth(500);
					column.setMinWidth(0);
				}
				this.addColumn(column);
			}
			// 设置单选
			this.getRightActiveTable().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
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
			okBtn.addActionListener(new SaveActionListener());
			// 定义”取消“按钮
			FButton cancelBtn = new FButton("cancelBtn", "取 消");
			// 实现“取消”按钮点击事件
			cancelBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					ReportDSRelaUI.this.setVisible(false);
					resultMode = 0;
				}

			});

			// “确定”按钮加入按钮面板
			this.addControl(okBtn);
			// “取消”按钮加入按钮面板
			this.addControl(cancelBtn);
		}
	}

	/**
	 * DataSourceA值改变事件
	 */
	private class SelectAItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {

			if (fcbxDataSourceA.getSelectedIndex() == -1)
				return;

			String sSourceID = ((FComboBoxItem) arg0.getItem()).getValue()
					.toString();
			if (sSourceID.equals(sOldSourceIDA))
				return;

			if (arg0.getStateChange() == 2) {
				if (sCurSourceIDA != null) {
					if (sCurSourceIDA.equals(sOldSourceIDA)) {
						return;
					}
				}
			}

			List lstdsDictionary = DefineReportI.getMethod().getFieldWithEname(
					sSourceID);
			ftabDataSourceA.setData(lstdsDictionary);

			if (fcbxDataSourceB.getValue() == null) {
				sCurSourceIDB = "";
			} else {
				sCurSourceIDB = fcbxDataSourceB.getValue().toString();
			}
			fcbxDataSourceB.setRefModel(sDataSourceRef);

			int iCount = fcbxDataSourceB.getItemsCount();
			if ("".equals(sCurSourceIDB))
				fcbxDataSourceB.setSelectedIndex(-1);
			else
				fcbxDataSourceB.setValue(sCurSourceIDB);

			DefaultFComboBoxModel model = (DefaultFComboBoxModel) fcbxDataSourceB
					.getRefModel();
			for (int i = 0; i < iCount; i++) {
				if (sSourceID.equals(((FComboBoxItem) model.getElementAt(i))
						.getValue())) {
					sOldSourceIDA = sSourceID;
					model.removeElementAt(i);
					ftabDataSourceB.removeData();
					break;
				}
			}

		}
	}

	/**
	 * DataSourceB值改变事件
	 */
	private class SelectBItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {

			if (fcbxDataSourceB.getSelectedIndex() == -1)
				return;

			String sSourceID = ((FComboBoxItem) arg0.getItem()).getValue()
					.toString();

			if (sSourceID.equals(sOldSourceIDB))
				return;

			if (arg0.getStateChange() == 2)
				if (sCurSourceIDB != null) {
					if (sCurSourceIDB.equals(sOldSourceIDB)) {
						return;
					}
				}

			List lstdsDictionary = DefineReportI.getMethod().getFieldWithEname(
					sSourceID);
			ftabDataSourceB.setData(lstdsDictionary);

			if (fcbxDataSourceA.getValue() == null) {
				sCurSourceIDA = "";
			} else {
				sCurSourceIDA = fcbxDataSourceA.getValue().toString();
			}
			fcbxDataSourceA.setRefModel(sDataSourceRef);
			int iCount = fcbxDataSourceA.getItemsCount();
			if ("".equals(sCurSourceIDA))
				fcbxDataSourceA.setSelectedIndex(-1);
			else
				fcbxDataSourceA.setValue(sCurSourceIDA);

			DefaultFComboBoxModel model = (DefaultFComboBoxModel) fcbxDataSourceA
					.getRefModel();
			for (int i = 0; i < iCount; i++) {
				if (sSourceID.equals(((FComboBoxItem) model.getElementAt(i))
						.getValue())) {
					sOldSourceIDB = sSourceID;
					model.removeElementAt(i);
					break;
				}
			}
		}
	}

	/**
	 * 增加关联关系
	 */
	private class AddActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 得到当前选中的行
			XMLData lstRelaA = ftabDataSourceA.getCurrentRow();
			if (lstRelaA == null) {
				JOptionPane.showMessageDialog(ReportDSRelaUI.this,
						"请选择数据源1字段！", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			XMLData lstRelaB = ftabDataSourceB.getCurrentRow();
			if (lstRelaB == null) {
				JOptionPane.showMessageDialog(ReportDSRelaUI.this,
						"请选择数据源2字段！", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// 判断字段类型是否相同
			String sFieldTypeA = lstRelaA.get(IDefineReport.FIELD_TYPE)
					.toString();
			String sFieldTypeB = lstRelaB.get(IDefineReport.FIELD_TYPE)
					.toString();
			if (!sFieldTypeA.equals(sFieldTypeB)) {
				JOptionPane.showMessageDialog(ReportDSRelaUI.this,
						"字段类型不相同,不能建立关联关系！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			MyNodeObject myNodeObject = new MyNodeObject();

			// 得到数据源AID和列名
			myNodeObject.sSourceIDA = fcbxDataSourceA.getValue().toString();
			String sSourceIDAName = fcbxDataSourceA.getRefModel()
					.getNameByValue(myNodeObject.sSourceIDA);
			myNodeObject.sSourceColIDA = lstRelaA
					.get(IDefineReport.FIELD_ENAME).toString();
			String sSourceColIDAName = lstRelaA.get(IDefineReport.FIELD_FNAME)
					.toString();

			// 得到数据源B的ID和列名
			myNodeObject.sSourceIDB = fcbxDataSourceB.getValue().toString();
			String sSourceIDBName = fcbxDataSourceB.getRefModel()
					.getNameByValue(myNodeObject.sSourceIDB);
			myNodeObject.sSourceColIDB = lstRelaB
					.get(IDefineReport.FIELD_ENAME).toString();
			String sSourceColIDBName = lstRelaB.get(IDefineReport.FIELD_FNAME)
					.toString();

			// 得到父节点
			DefaultTreeModel treeModel = (DefaultTreeModel) treRela.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
					.getRoot();
			// 判断增加的父节点是否已存在,默认父节点不存在
			boolean sFlag = false;
			MyNodeObject curMyNodeObject = null;
			DefaultMutableTreeNode curNode = null;
			int iChildCount = root.getChildCount();
			// 得到根节点下的子节点
			for (int i = 0; i < iChildCount; i++) {
				curNode = (DefaultMutableTreeNode) root.getChildAt(i);
				curMyNodeObject = (MyNodeObject) curNode.getUserObject();
				if (myNodeObject.sSourceIDA.equals(curMyNodeObject
						.getSSourceIDA())
						&& myNodeObject.sSourceIDB.equals(curMyNodeObject
								.getSSourceIDB())) {
					sFlag = true;
					break;
				}
			}

			DefaultMutableTreeNode ANode = null;
			if (!sFlag) {// 父节点不存在
				// 增加父节点
				MyNodeObject myParNodeObject = new MyNodeObject();
				myParNodeObject.setSSourceIDA(myNodeObject.sSourceIDA);
				myParNodeObject.setSSourceIDB(myNodeObject.sSourceIDB);
				myParNodeObject.sRelationsName = sSourceIDAName + "="
						+ sSourceIDBName;
				ANode = new DefaultMutableTreeNode(myParNodeObject);
				treeModel.insertNodeInto(ANode, root, root.getChildCount());
				curNode = (DefaultMutableTreeNode) root.getLastChild();
			}
			// 增加子节点
			myNodeObject.sRelationsName = sSourceColIDAName + "="
					+ sSourceColIDBName;
			ANode = new DefaultMutableTreeNode(myNodeObject);
			treeModel.insertNodeInto(ANode, curNode, curNode.getChildCount());
			// 定位到新增加的节点
			treRela.expendTo(ANode);
		}
	}

	/**
	 * 删除关联关系
	 */
	private class DelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treRela
					.getLastSelectedPathComponent();
			if (!defaultMutableTreeNode.isLeaf())
				return;

			// 得到父对象节点
			DefaultMutableTreeNode parNode = (DefaultMutableTreeNode) defaultMutableTreeNode
					.getParent();
			// 得到父对象的子节点数
			int iChildCount = parNode.getChildCount();

			DefaultTreeModel model = (DefaultTreeModel) treRela.getModel();
			model.removeNodeFromParent(defaultMutableTreeNode);
			// 点有一个子节点，子节点删除时，删除父节点
			if (iChildCount == 1) {
				model.removeNodeFromParent(parNode);
			}
		}
	}

	/**
	 * 保存按钮点击事件
	 */
	private class SaveActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 保存数据源关联关系设置
			saveSummaryDataSourceRelationsImpl();
			ReportDSRelaUI.this.setVisible(false);
			resultMode = 1;
		}
	}

	/**
	 * 保存关联关系信息到报表对象
	 * 
	 */
	private void saveSummaryDataSourceRelationsImpl() {
		// 当前节点对象
		MyNodeObject myNodeObject = null;
		// 关联关系数组
		RefSource[] refSource = null;
		// 得到叶节点
		List lstLeafNode = treRela.getIsLeafNode();
		int iCount = lstLeafNode.size();
		// 定义关联关系数据
		SummaryDataSourceRelationsImpl[] summaryDataSourceRelationsImpl = new SummaryDataSourceRelationsImpl[iCount];
		for (int i = 0; i < iCount; i++) {
			summaryDataSourceRelationsImpl[i] = new SummaryDataSourceRelationsImpl();
			myNodeObject = (MyNodeObject) lstLeafNode.get(i);
			summaryDataSourceRelationsImpl[i].setRelationsID(DefinePub
					.getRandomUUID());
			// 名称
			summaryDataSourceRelationsImpl[i]
					.setRelationsName(myNodeObject.sRelationsName);
			// 关联关系数组
			refSource = new RefSource[2];
			refSource[0] = new RefSource();
			refSource[1] = new RefSource();
			refSource[0].setSourceID(myNodeObject.sSourceIDA);
			refSource[1].setSourceID(myNodeObject.sSourceIDB);
			refSource[0].setSourceColID(myNodeObject.sSourceColIDA);
			refSource[1].setSourceColID(myNodeObject.sSourceColIDB);
			summaryDataSourceRelationsImpl[i].setRefSourceArray(refSource);
		}

		// 保存关联关系
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) reportGuideUI.querySource
				.getDataSourceManager();
		summaryDataSourceManagerImpl
				.setDataSourceRelationsArray(summaryDataSourceRelationsImpl);
	}

	public int getResultMode() {
		return resultMode;
	}

}
