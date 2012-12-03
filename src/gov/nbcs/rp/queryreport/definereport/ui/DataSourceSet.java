/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.lang.StringUtils;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FComboBoxItem;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryParameterImpl;
import com.foundercy.pf.reportcy.summary.object.base.ToSource;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:分组报表定义,选中数据源与枚举关系设置、数据源条件设置客服端页面
 * </p>
 * <p>
 * Description:分组报表定义,选中数据源与枚举关系设置、数据源条件设置客服端页面
 * </p>

 */
public class DataSourceSet extends FDialog {

	private static final long serialVersionUID = 1L;

	private ReportQuerySource querySource = null;

	private ReportGuideUI reportGuideUI = null;

	// 数据源组合框
	private FComboBox dataSourceCbx = null;

	// 字段名称组合框
	private FComboBox cbxFieldName = null;

	// 比较类型
	private FComboBox cbxCompare = null;

	// 条件值
	// private FTextField txtWhereValue = null;
	private FComboBox cbxWhereValue = null;

	// 定义条件类型
	private FRadioGroup frdoType = null;

	// 条件树
	private MyTree treWhere = null;

	private List lstdsDictionary = null;

	// 保存枚举对应的引用列信息
	private Map mapWhereValue;

	// 原选择枚举ID
	private String oldEnumID;

	// 是否字段名标志
	private Map mapFieldFlag;

	/**
	 * 构造函数
	 * 
	 * @param definReportServ
	 *            数据库接口
	 */
	public DataSourceSet(ReportGuideUI reportGuideUI) {
		super(reportGuideUI);
		this.setSize(700, 525);
		this.setTitle("过滤条件设置");
		this.setModal(true);
		this.reportGuideUI = reportGuideUI;
		this.querySource = reportGuideUI.querySource;

		this.reportGuideUI = reportGuideUI;

		jbInit();

	}

	/**
	 * 界面初始化方法
	 * 
	 */
	private void jbInit() {

		// 定义报表属性多页面面板
		FTabbedPane ftabPnlReportSet = new FTabbedPane();

		// 定义数据源及枚举数据源过滤条件设置面板
		DatasWhereSetPnl datasWhereSetPnl = new DatasWhereSetPnl();
		ftabPnlReportSet.addControl("过滤条件设置", datasWhereSetPnl);

		BtnPanel btnPanel = new BtnPanel();

		// 定义主面面板
		FPanel fpnlMain = new FPanel();
		RowPreferedLayout rLayMian = new RowPreferedLayout(1);
		rLayMian.setRowHeight(35);
		fpnlMain.setLayout(rLayMian);
		fpnlMain.addControl(ftabPnlReportSet, new TableConstraints(1, 1, true,
				true));

		fpnlMain.addControl(btnPanel, new TableConstraints(1, 1, false, true));

		this.getContentPane().add(fpnlMain);

	}

	/**
	 * 定义数据源及枚举数据源过滤条件设置面板
	 */
	private class DatasWhereSetPnl extends FTitledPanel {

		private static final long serialVersionUID = 1L;

		public DatasWhereSetPnl() {
			this.setTitle("数据源过滤条件设置");
			this.setTopInset(5);
			this.setLeftInset(5);
			this.setRightInset(5);

			// 数据源组合框
			dataSourceCbx = new MyFComboBox("数据源:", reportGuideUI.querySource);
			((JComboBox) dataSourceCbx.getComponent(1))
					.addItemListener(new SelectItemListener());

			// 字段名
			FLabel fieldNameLbl = new FLabel();
			fieldNameLbl.setTitle("字段名:");
			// 升降序选择
			FLabel operateLbl = new FLabel();
			operateLbl.setText("操作:");
			// 条件值
			FLabel whereValueLbl = new FLabel();
			whereValueLbl.setText("值:");

			// 字段名称组合框
			cbxFieldName = new FComboBox();
			cbxFieldName.setTitleVisible(false);

			((JComboBox) cbxFieldName.getComponent(1))
					.addItemListener(new ItemListener() {

						public void itemStateChanged(ItemEvent arg0) {
							String sFieldTyp = getFieldType();
							// 判断字段类型，如果是数值型包含和不包含不显示
							if (DefinePub.checkCharVal(sFieldTyp)) {
								cbxCompare
										.setRefModel(CompareType.COMPARE_TYPE);
							} else {
								cbxCompare
										.setRefModel(CompareType.COMPARE_TYPE_NUM);
							}
							cbxCompare.setSelectedIndex(-1);
							// 得到基础信息选择列的值，根据报表id和ename值
							if (dataSourceCbx.getValue() != null
									&& !Common.isNullStr(dataSourceCbx
											.getValue().toString())
									&& cbxFieldName.getValue() != null
									&& !Common.isNullStr(cbxFieldName
											.getValue().toString())) {
								String sFeildEname = cbxFieldName.getValue()
										.toString();
								List lstInputValue = null;
								try {
									lstInputValue = DefineReportI.getMethod()
											.getBasSelectInputValue(
													dataSourceCbx.getValue()
															.toString(),
													sFeildEname);
								} catch (Exception e) {
									e.printStackTrace();
									new MessageBox(DataSourceSet.this, "错误信息："
											+ e.getMessage(), MessageBox.ERROR,
											MessageBox.BUTTON_OK).show();
								}
								cbxWhereValue.setEditable(true);

								// 增加字段值信息
								Vector vector = new Vector();
								String[] displayValues = null;
								mapFieldFlag = new HashMap();

								// 字段值内容
								displayValues = setRefValue(lstInputValue,
										IDefineReport.CODE, IDefineReport.NAME,
										vector, displayValues, mapFieldFlag,
										false);

								// 数据源字段
								displayValues = setRefValue(lstdsDictionary,
										IDefineReport.FIELD_ENAME,
										IDefineReport.FIELD_FNAME, vector,
										displayValues, mapFieldFlag, true);

								cbxWhereValue
										.setRefModel(vector, displayValues);
								cbxWhereValue.setSelectedIndex(-1);
							} else {
								cbxWhereValue.removeAllItems();
							}
						}

					});
			// 比较类型
			cbxCompare = new FComboBox();
			cbxCompare.setTitleVisible(false);
			cbxCompare.setRefModel(CompareType.COMPARE_TYPE);
			cbxCompare.setSelectedIndex(-1);

			// 条件值
			FPanel fpnlWhere = new FPanel();
			RowPreferedLayout rLayWhere = new RowPreferedLayout(2);
			rLayWhere.setColumnWidth(30);
			rLayWhere.setColumnGap(1);
			fpnlWhere.setLayout(rLayWhere);
			cbxWhereValue = new FComboBox();
			cbxWhereValue.setTitleVisible(false);
			((JComboBox) cbxWhereValue.getEditor()).setEditable(true);
			cbxWhereValue.setEditable(true);
			// txtWhereValue = new FTextField(false);
			FButton fbtnWhere = new FButton("...", "fbtnWhere");
			fbtnWhere.addActionListener(new WhereActionListener());
			fpnlWhere
					.add(cbxWhereValue, new TableConstraints(1, 1, true, true));
			fpnlWhere.addControl(fbtnWhere, new TableConstraints(1, 1, true,
					false));

			// 定义条件面板
			FPanel orderPnl = new FPanel();
			orderPnl.setLayout(new RowPreferedLayout(5));

			FLabel flblEmpty = new FLabel();
			// 数据来源
			orderPnl.addControl(dataSourceCbx, new TableConstraints(1, 2,
					false, true));
			orderPnl.addControl(flblEmpty, new TableConstraints(1, 3, false,
					true));
			// Label
			orderPnl.addControl(fieldNameLbl, new TableConstraints(1, 2, false,
					true));
			orderPnl.addControl(operateLbl, new TableConstraints(1, 1, false,
					true));
			orderPnl.addControl(whereValueLbl, new TableConstraints(1, 2,
					false, true));
			// 编码框
			orderPnl.addControl(cbxFieldName, new TableConstraints(1, 2, false,
					true));
			orderPnl.addControl(cbxCompare, new TableConstraints(1, 1, false,
					true));
			orderPnl.addControl(fpnlWhere, new TableConstraints(1, 2, false,
					true));

			// 定义条件类型
			frdoType = new FRadioGroup("", FRadioGroup.HORIZON);
			frdoType.setRefModel("and#并且(AND)+or#或者(OR)");
			frdoType.setTitleVisible(false);
			frdoType.setValue("and");

			// 增加、修改按钮
			FButton addBtn = new FButton("addBtn", " 增加 ");
			addBtn.addActionListener(new AddActionListener());

			FButton modifyBtn = new FButton("addBtn", " 修改 ");
			modifyBtn.addActionListener(new ModifyActionListener());
			FFlowLayoutPanel editBtnPnl = new FFlowLayoutPanel();
			editBtnPnl.setAlignment(FlowLayout.RIGHT);
			editBtnPnl.addControl(addBtn, new TableConstraints(1, 1, false,
					true));
			editBtnPnl.addControl(modifyBtn, new TableConstraints(1, 1, false,
					true));

			FPanel fpnlEdit = new FPanel();
			fpnlEdit.setLayout(new RowPreferedLayout(4));
			fpnlEdit.addControl(new FLabel(), new TableConstraints(1, 2, true,
					true));
			fpnlEdit.addControl(frdoType,
					new TableConstraints(1, 1, true, true));
			fpnlEdit.addControl(editBtnPnl, new TableConstraints(1, 1, true,
					true));

			// 定义条件树
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("过滤条件");
			DefaultTreeModel treeModel = new DefaultTreeModel(root);
			treWhere = new MyTree(treeModel);
			treWhere.addTreeSelectionListener(new TreeSelectionListener() {

				public void valueChanged(TreeSelectionEvent arg0) {

					DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) ((MyTree) arg0
							.getSource()).getLastSelectedPathComponent();
					if (curTreeNode == null)
						return;
					if (!(curTreeNode.getUserObject() instanceof ConditionNodeObj))
						return;
					ConditionNodeObj conditionNodeObj = (ConditionNodeObj) curTreeNode
							.getUserObject();

					// 数据源组合框
					dataSourceCbx.setValue(conditionNodeObj.getSourceID());
					// 字段名称组合框
					cbxFieldName.setValue(conditionNodeObj.getSourceColID());
					// 比较类型
					cbxCompare.setValue(conditionNodeObj.getCompareType());

					// 根据sFieldEname返回字段类型
					String sFieldTyp = getFieldType();

					// 判断是不是字段比较
					if (conditionNodeObj.isFieldFlag()) {
						cbxWhereValue.setValue(conditionNodeObj.getParaValue());
					} else {
						// 根据字段类型判断参数加不加引号(')
						// 条件值
						if (DefinePub.checkCharVal(sFieldTyp)) {

							String paraValue = conditionNodeObj.getParaValue()
									.toString();
							// 判断是不是in或not in比较符
							if (CompareType.IN_TYPE
									.equalsIgnoreCase(conditionNodeObj
											.getCompareType())
									|| CompareType.NOTIN_TYPE
											.equalsIgnoreCase(conditionNodeObj
													.getCompareType())) {
								// 去掉左右括号
								paraValue = paraValue.substring(1, paraValue
										.length() - 1);
								// 去掉逗号旁的引号
								paraValue = paraValue.replaceAll("','", ",");
							}
							// 去掉最外层引号
							cbxWhereValue.setValue(paraValue.substring(1,
									paraValue.length() - 1));

						} else {
							cbxWhereValue.setValue(conditionNodeObj
									.getParaValue());
						}
					}

					// 定义条件类型
					if (!Common.isNullStr(conditionNodeObj.joinBefore))
						frdoType.setValue(conditionNodeObj.joinBefore);

				}
			});

			JScrollPane fpnltalRela = new JScrollPane(treWhere);

			// 删除排序条件按钮
			FButton delBtn = new FButton("delBtn", " 删除 ");
			delBtn.addActionListener(new DelActionListener());
			// 上移按钮
			FButton upBtn = new FButton("upBtn", " 上移 ");
			upBtn.addActionListener(new UpActionListener());
			// 下移按钮
			FButton downBtn = new FButton("downBtn", " 下移 ");
			downBtn.addActionListener(new DownActionListener());
			// 左括号按钮
			FButton lParenthesisBtn = new FButton("downBtn", "左括号");
			lParenthesisBtn.addActionListener(new LParenthesisActionListener());
			// 右括号按钮
			FButton rParenthesisBtn = new FButton("downBtn", "右括号");
			rParenthesisBtn.addActionListener(new RParenthesisActionListener());
			// 删括号按钮
			FButton delParenthesisBtn = new FButton("downBtn", "删括号");
			delParenthesisBtn
					.addActionListener(new DelParenthesisActionListener());
			// 定义按钮面板
			FFlowLayoutPanel btnPnl = new FFlowLayoutPanel();
			btnPnl.addControl(delBtn, new TableConstraints(1, 1, true, false));
			btnPnl.addControl(upBtn, new TableConstraints(1, 1, true, false));
			btnPnl.addControl(downBtn, new TableConstraints(1, 1, true, false));
			btnPnl.addControl(lParenthesisBtn, new TableConstraints(1, 1, true,
					false));
			btnPnl.addControl(rParenthesisBtn, new TableConstraints(1, 1, true,
					false));
			btnPnl.addControl(delParenthesisBtn, new TableConstraints(1, 1,
					true, false));

			RowPreferedLayout rLay = new RowPreferedLayout(2);
			rLay.setColumnWidth(70);
			rLay.setColumnGap(1);
			this.setLayout(rLay);
			this.addControl(orderPnl, new TableConstraints(3, 2, false, true));
			this.addControl(fpnlEdit, new TableConstraints(2, 2, false, true));
			this.add(fpnltalRela, new TableConstraints(1, 1, true, true));
			this.addControl(btnPnl, new TableConstraints(1, 1, true, false));

			// 在界面显示参数
			showParameterArray();
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
					DataSourceSet.this.setVisible(false);
				}

			});

			// “确定”按钮加入按钮面板
			this.addControl(okBtn);
			// “取消”按钮加入按钮面板
			this.addControl(cancelBtn);
		}
	}

	/**
	 * 数据源值改变事件
	 */
	private class SelectItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {

			String sSourceID = ((FComboBoxItem) arg0.getItem()).getValue()
					.toString();
			lstdsDictionary = DefineReportI.getMethod().getFieldWithEname(
					sSourceID);

			// 字段名
			ColWhereSetPnl.addRefValue(cbxFieldName, lstdsDictionary,
					IDefineReport.FIELD_ENAME, IDefineReport.FIELD_FNAME);

			cbxFieldName.setSelectedIndex(-1);
		}
	}

	/**
	 * 保存按钮点击事件
	 */
	private class SaveActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String sErr = checkFilter();
			if (!Common.isNullStr(sErr)) {
				JOptionPane.showMessageDialog(DataSourceSet.this, sErr, "提示",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// 保存数据源过滤条件
			saveParameterArray();
			DataSourceSet.this.setVisible(false);
		}
	}

	/**
	 * 保存前检查
	 * 
	 * @return
	 */
	private String checkFilter() {
		// 检查每个数据源左右小括号是否匹配
		DefaultTreeModel treeModel = (DefaultTreeModel) treWhere.getModel();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel
				.getRoot();
		if (rootNode == null)
			return "";
		int count = rootNode.getChildCount();
		if (count == 0)
			return "";

		String rarenthesis;
		String sErr = "";
		Enumeration enuma;
		for (int i = 0; i < count; i++) {
			enuma = ((DefaultMutableTreeNode) rootNode.getChildAt(i))
					.breadthFirstEnumeration();
			rarenthesis = "";
			// 遍历节点
			while (enuma.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) enuma
						.nextElement();
				if (!node.isLeaf())
					continue;
				ConditionNodeObj conditionObj = (ConditionNodeObj) node
						.getUserObject();
				if (conditionObj.getLParenthesis() != null)
					rarenthesis = rarenthesis + conditionObj.getLParenthesis();
				if (conditionObj.getRParenthesis() != null)
					rarenthesis = rarenthesis + conditionObj.getRParenthesis();

			}

			int iLeft, iRight;
			iLeft = StringUtils.countMatches(rarenthesis, "(");
			iRight = StringUtils.countMatches(rarenthesis, ")");
			if (iLeft != iRight)
				sErr = sErr + rootNode.getChildAt(i).toString() + "小括号数量不匹配。\n";

		}

		return sErr;

	}

	/**
	 * 保存数据源过滤条件
	 * 
	 */
	private void saveParameterArray() {
		// 定义summaryParameterImpl对象
		SummaryParameterImpl[] summaryParameterImpl = null;

		// 得到当前参数
		IParameter iParameter[] = querySource.getParameterArray();
		String chName = null;
		// 循环得到默认参数,存于summaryParameterImpl对象
		for (int i = 0; i < iParameter.length; i++) {
			// 得到chName名称
			chName = iParameter[i].getChName();
			// 判断是否是默认参数
			if (checkDefaultPara(chName)) {
				summaryParameterImpl = ReportGuideUI.addArrayLength(
						summaryParameterImpl, 1);
				summaryParameterImpl[summaryParameterImpl.length - 1] = (SummaryParameterImpl) iParameter[i];
			}
		}

		// 得到开始行数
		int iStartCount = summaryParameterImpl.length;
		// 得到树叶节点
		List lstNode = treWhere.getIsLeafNode();
		// 得到节点数
		int iCount = lstNode.size();
		// 定义节点对象
		ConditionNodeObj conditionNodeObj = null;
		// 定义作用的数据源数组
		ToSource toSource[] = null;
		// summaryParameterImpl，扩展iCount长度
		summaryParameterImpl = ReportGuideUI.addArrayLength(
				summaryParameterImpl, iCount);

		for (int i = 0; i < iCount; i++) {
			// 初始化参数
			summaryParameterImpl[i + iStartCount] = new MySummaryParameterImpl();
			// 得到节点对象
			conditionNodeObj = (ConditionNodeObj) lstNode.get(i);
			// id
			summaryParameterImpl[i + iStartCount].setName(DefinePub
					.getRandomUUID());
			// chName
			summaryParameterImpl[i + iStartCount].setChName(conditionNodeObj
					.getChName());
			// compareType比较类型
			summaryParameterImpl[i + iStartCount]
					.setCompareType(conditionNodeObj.getCompareType());
			// 参数值
			summaryParameterImpl[i + iStartCount].setValue(conditionNodeObj
					.getParaValue());

			summaryParameterImpl[i + iStartCount]
					.setJoinBefore(conditionNodeObj.joinBefore);

			if (summaryParameterImpl[i + iStartCount] instanceof MySummaryParameterImpl) {
				((MySummaryParameterImpl) summaryParameterImpl[i + iStartCount])
						.setLParenthesis(conditionNodeObj.getLParenthesis());
				((MySummaryParameterImpl) summaryParameterImpl[i + iStartCount])
						.setRParenthesis(conditionNodeObj.getRParenthesis());
				// 是不是字段比较标志
				((MySummaryParameterImpl) summaryParameterImpl[i + iStartCount])
						.setIsFilterFlag(conditionNodeObj.isFieldFlag() ? "1"
								: "0");
			}

			// 定义ToSource对象
			toSource = new ToSource[1];
			toSource[0] = new ToSource();
			// soureceId 数据源ID
			toSource[0].setSourceID(conditionNodeObj.getSourceID());
			// 列ID
			toSource[0].setSourceColID(conditionNodeObj.getSourceColID());
			// 是否作用于枚举
			toSource[0].setToEnumSource(false);
			summaryParameterImpl[i + iStartCount].setToSourceArray(toSource);
		}
		querySource.setParameterArray(summaryParameterImpl);
	}

	/**
	 * 在界面显示参数
	 * 
	 */
	private void showParameterArray() {
		// 得到当前参数
		IParameter iParameter[] = querySource.getParameterArray();
		if (iParameter == null)
			return;
		// 得到参数数量
		int iCount = iParameter.length;
		// 定义chName参数名称
		String chName = null;
		// 定义ConditionNodeObj对象
		ConditionNodeObj conditionNodeObj = null;
		// 循环处理显示参数
		for (int i = 0; i < iCount; i++) {
			// 得到chName名称
			chName = iParameter[i].getChName();
			// 判断是否是默认参数,如是默认参数不进行处理
			if (checkDefaultPara(chName)) {
				continue;
			}

			// 定义ConditionNodeObj对象
			conditionNodeObj = new ConditionNodeObj();
			// ID
			conditionNodeObj.setID(iParameter[i].getName());
			// 名称
			conditionNodeObj.setChName(iParameter[i].getChName());
			// 比较类型
			conditionNodeObj.setCompareType(iParameter[i].getCompareType());
			// 参数值
			conditionNodeObj.setParaValue(iParameter[i].getValue());
			// 作用数据源ID
			conditionNodeObj.setSourceID(iParameter[i].getToSourceArray()[0]
					.getSourceID());
			// 作用数据源字段ID
			conditionNodeObj.setSourceColID(iParameter[i].getToSourceArray()[0]
					.getSourceColID());
			// and 或 or
			conditionNodeObj.setJoinBefore(iParameter[i].getJoinBefore());
			// 左括号,右括号
			if (iParameter[i] instanceof MySummaryParameterImpl) {
				conditionNodeObj
						.setLParenthesis(((MySummaryParameterImpl) iParameter[i])
								.getLParenthesis());
				conditionNodeObj
						.setRParenthesis(((MySummaryParameterImpl) iParameter[i])
								.getRParenthesis());
				conditionNodeObj
						.setRParenthesis(((MySummaryParameterImpl) iParameter[i])
								.getRParenthesis());
				conditionNodeObj.setFieldFlag(Common
						.estimate(((MySummaryParameterImpl) iParameter[i])
								.getIsFilterFlag()));

			} else {
				conditionNodeObj.setLParenthesis("");
				conditionNodeObj.setRParenthesis("");
			}

			// 参数条件树上增加节点
			addParaTreNode(conditionNodeObj);
		}
		// 节点展开
		treWhere.expandTree();
	}

	/**
	 * 增加按钮点击事件
	 */
	private class AddActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 检查信息是否填写完整
			if (!checkInputInfo())
				return;

			// 定义参数对象
			ConditionNodeObj conditionNodeObj = new ConditionNodeObj();
			// ID
			conditionNodeObj.ID = DefinePub.getRandomUUID();
			// 获得界面信息，设置对象值
			setObjectValue(conditionNodeObj);
			// 参数条件树上增加节点
			TreeNode node = addParaTreNode(conditionNodeObj);
			// 刷新节点中文名称
			refreshNodeChName(node);
			// 定位到新增加的节点
			treWhere.expendTo(node);
		}
	}

	/**
	 * 参数条件树上增加节点
	 * 
	 * @param conditionNodeObj
	 */
	private TreeNode addParaTreNode(ConditionNodeObj conditionNodeObj) {
		// 得到父节点
		DefaultTreeModel treeModel = (DefaultTreeModel) treWhere.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();
		// 判断增加的父节点是否已存在,默认父节点不存在
		boolean sFlag = false;
		ConditionNodeObj curconditionNodeObj = null;
		DefaultMutableTreeNode curNode = null;
		int iChildCount = root.getChildCount();
		// 得到根节点下的子节点
		for (int i = 0; i < iChildCount; i++) {
			curNode = (DefaultMutableTreeNode) root.getChildAt(i);
			curconditionNodeObj = (ConditionNodeObj) curNode.getUserObject();
			if (conditionNodeObj.sourceID.equals(curconditionNodeObj
					.getSourceID())) {
				sFlag = true;
				break;
			}
		}

		DefaultMutableTreeNode ANode = null;
		if (!sFlag) {// 父节点不存在
			// 增加父节点
			ConditionNodeObj myParNodeObject = new ConditionNodeObj();
			myParNodeObject.setSourceID(conditionNodeObj.sourceID);
			// 数据源名称
			String sDataSourceName = dataSourceCbx.getRefModel()
					.getNameByValue(conditionNodeObj.sourceID);
			myParNodeObject.setChName(sDataSourceName);
			ANode = new DefaultMutableTreeNode(myParNodeObject);
			treeModel.insertNodeInto(ANode, root, root.getChildCount());
			curNode = (DefaultMutableTreeNode) root.getLastChild();
		}
		ANode = new DefaultMutableTreeNode(conditionNodeObj);
		treeModel.insertNodeInto(ANode, curNode, curNode.getChildCount());
		return ANode;
	}

	/**
	 * 修改按钮点击事件
	 */
	private class ModifyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 检查信息是否填写完整
			if (!checkInputInfo())
				return;

			DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (defaultMutableTreeNode == null)
				return;
			ConditionNodeObj conditionObj = (ConditionNodeObj) defaultMutableTreeNode
					.getUserObject();
			setObjectValue(conditionObj);
			defaultMutableTreeNode.setUserObject(conditionObj);
			// 刷新节点中文名称
			refreshNodeChName(defaultMutableTreeNode);
			// 刷新节点
			((DefaultTreeModel) treWhere.getModel())
					.nodeChanged(defaultMutableTreeNode);
		}
	}

	/**
	 * 获得界面信息，设置对象值
	 * 
	 * @param conditionNodeObj
	 */
	private void setObjectValue(ConditionNodeObj conditionNodeObj) {
		// 比较类型
		conditionNodeObj.compareType = cbxCompare.getValue().toString();
		// 数据源ID
		conditionNodeObj.sourceID = dataSourceCbx.getValue().toString();
		// 数据源列ID
		conditionNodeObj.sourceColID = cbxFieldName.getValue().toString();

		// 参数值
		conditionNodeObj.paraValue = cbxWhereValue.getValue();

		// 是否字段比较标记
		conditionNodeObj.fieldFlag = this.isFieldFlag();

		// 是否字段比较
		if (!conditionNodeObj.fieldFlag) {
			// 根据sFieldEname返回字段类型
			String sFieldTyp = getFieldType();
			// 根据字段类型判断参数加不加引号(')
			if (DefinePub.checkCharVal(sFieldTyp)) {
				conditionNodeObj.paraValue = "'" + conditionNodeObj.paraValue
						+ "'";
				// 判断是不是in或not in比较符
				if (CompareType.IN_TYPE
						.equalsIgnoreCase(conditionNodeObj.compareType)
						|| CompareType.NOTIN_TYPE
								.equalsIgnoreCase(conditionNodeObj.compareType)) {
					conditionNodeObj.paraValue = conditionNodeObj.paraValue
							.toString().replaceAll(",", "','");
					conditionNodeObj.paraValue = "("
							+ conditionNodeObj.paraValue + ")";
				}
			}
		} else {
			// 字段名中文名
			conditionNodeObj.setFieldFname(cbxWhereValue.getText());
		}

		// 联接类型 and或or
		conditionNodeObj.joinBefore = frdoType.getValue().toString();
		// 名称
		conditionNodeObj.chName = getChName(conditionNodeObj);
	}

	/**
	 * 得到条件中文组成信息
	 * 
	 * @param conditionNodeObj
	 * @param isFirstPara
	 * @return
	 */
	private String getChName(ConditionNodeObj conditionNodeObj) {
		return this.getChName(conditionNodeObj, false);
	}

	private String getChName(ConditionNodeObj conditionNodeObj,
			boolean isFirstPara) {
		String value = "";
		// 名称
		if (!isFirstPara) {
			if ("and".equals(conditionNodeObj.joinBefore)) {
				value = "并且 ";
			} else {
				value = "或者 ";
			}
		}

		if (conditionNodeObj.getLParenthesis() != null) {
			value = value + conditionNodeObj.getLParenthesis();
		}
		// 是否字段比较
		if (conditionNodeObj.isFieldFlag()) {
			value = value
					+ cbxFieldName.getRefModel().getNameByValue(
							conditionNodeObj.sourceColID)
					+ " "
					+ cbxCompare.getRefModel().getNameByValue(
							conditionNodeObj.compareType) + " "
					+ conditionNodeObj.fieldFname;
		} else {
			value = value
					+ cbxFieldName.getRefModel().getNameByValue(
							conditionNodeObj.sourceColID)
					+ " "
					+ cbxCompare.getRefModel().getNameByValue(
							conditionNodeObj.compareType) + " "
					+ conditionNodeObj.paraValue;
		}

		if (conditionNodeObj.getRParenthesis() != null) {
			value = value + conditionNodeObj.getRParenthesis();
		}
		return value;
	}

	/**
	 * 根据sFieldEname返回字段类型
	 * 
	 * @return
	 */
	private String getFieldType() {
		int iIndex = cbxFieldName.getSelectedIndex();
		if (iIndex < 0)
			return null;
		Map dsDictionaryMap = (Map) lstdsDictionary.get(iIndex);
		String fieldType = dsDictionaryMap.get(IDefineReport.FIELD_TYPE)
				.toString();
		return DefinePub.getFieldTypeWithCh(fieldType);
	}

	/**
	 * 删除按钮点击事件
	 */
	private class DelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (!curTreeNode.isLeaf())
				return;

			// 得到父对象节点
			DefaultMutableTreeNode parNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			// 得到父对象的子节点数
			int iChildCount = parNode.getChildCount();

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			model.removeNodeFromParent(curTreeNode);
			// 点有一个子节点，子节点删除时，删除父节点
			if (iChildCount == 1) {
				model.removeNodeFromParent(parNode);
			}
		}
	}

	/**
	 * 上移按钮点击事件
	 */
	private class UpActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null) {
				new MessageBox(reportGuideUI, "请选择树节点!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			if (!curTreeNode.isLeaf())
				return;

			int row = treWhere.getSelectionRows()[0];
			DefaultMutableTreeNode preTreeNode = curTreeNode
					.getPreviousNode();
			if (!preTreeNode.isLeaf())
				return;

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			DefaultMutableTreeNode parTreeNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			int index = parTreeNode.getIndex(curTreeNode);

			model.removeNodeFromParent(curTreeNode);
			model.insertNodeInto(curTreeNode, parTreeNode, index - 1);
			// 刷新节点中文名称
			if (curTreeNode != null)
				refreshNodeChName(curTreeNode);
			if (curTreeNode.getNextSibling() != null)
				refreshNodeChName(curTreeNode.getNextSibling());
			model.nodeChanged(curTreeNode);
			model.nodeChanged(curTreeNode.getNextSibling());

			treWhere.repaint();
			treWhere.setSelectionRow(row - 1);

		}
	}

	/**
	 * 下移按钮点击事件
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();

			if (curTreeNode == null) {
				new MessageBox(reportGuideUI, "请选择树节点!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			if (!curTreeNode.isLeaf())
				return;

			int row = treWhere.getSelectionRows()[0];
			DefaultMutableTreeNode nextTreeNode = curTreeNode
					.getNextNode();
			if (!nextTreeNode.isLeaf())
				return;

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			DefaultMutableTreeNode parTreeNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			int index = parTreeNode.getIndex(curTreeNode);

			model.removeNodeFromParent(curTreeNode);
			model.insertNodeInto(curTreeNode, parTreeNode, index + 1);
			// 刷新节点中文名称
			if (curTreeNode != null)
				refreshNodeChName(curTreeNode);
			if (curTreeNode.getPreviousSibling() != null)
				refreshNodeChName(curTreeNode.getPreviousSibling());
			model.nodeChanged(curTreeNode);
			model.nodeChanged(curTreeNode.getPreviousSibling());

			treWhere.repaint();
			treWhere.setSelectionRow(row + 1);
		}
	}

	/**
	 * 增加或修改时检查信息是否填写完整
	 * 
	 * @return
	 */
	private boolean checkInputInfo() {
		// 字段名称组合框
		if (cbxFieldName.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(DataSourceSet.this, "请选择数据源字段！",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 比较类型
		if (cbxCompare.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(DataSourceSet.this, "请选择比较类型！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 条件值
		if ("".equals(cbxWhereValue.getValue().toString())) {
			JOptionPane.showMessageDialog(DataSourceSet.this, "请填写条件值！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断是不是字段比较
		if (this.isFieldFlag()) {
			if (CompareType.COMPARE_TYPE_NUM.indexOf(cbxCompare.getText()
					.toString()) == -1) {
				JOptionPane.showMessageDialog(DataSourceSet.this,
						"字段和字段比较时，不能使用\"" + cbxCompare.getText()
								+ "\"操作类型，请选择其他类型！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		return true;
	}

	private boolean isFieldFlag() {
		return Common.estimate(mapFieldFlag.get(cbxWhereValue.getValue() + "||"
				+ cbxWhereValue.getText()));
	}

	/**
	 * 判断是否是默认参数
	 * 
	 * @param chName
	 * @return
	 */
	public static boolean checkDefaultPara(String chName) {
		if (IDefineReport.DIV_CODE.equals(chName)
				|| IDefineReport.BATCH_NO.equals(chName)
				|| IDefineReport.DATA_TYPE.equals(chName)
				|| IDefineReport.VER_NO.equals(chName)
				|| IDefineReport.DIVNAME_PARA.equals(chName))
			return true;
		else
			return false;
	}

	/**
	 * 刷新节点中文名称
	 * 
	 * @param node
	 */
	private void refreshNodeChName(TreeNode node) {
		TreeNode parNode = node.getParent();
		int index = parNode.getIndex(node);
		ConditionNodeObj conditionObj = (ConditionNodeObj) ((DefaultMutableTreeNode) node)
				.getUserObject();
		if (index == 0) {
			conditionObj.setChName(this.getChName(conditionObj, true));
		} else {
			conditionObj.setChName(this.getChName(conditionObj, false));
		}
	}

	/**
	 * 左括号按钮点击事件
	 */
	private class LParenthesisActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;

			ConditionNodeObj conditionObj = (ConditionNodeObj) curTreeNode
					.getUserObject();
			if (conditionObj.getLParenthesis() != null) {
				conditionObj.setLParenthesis(conditionObj.getLParenthesis()
						+ "(");
			} else {
				conditionObj.setLParenthesis("(");
			}
			curTreeNode.setUserObject(conditionObj);
			// 刷新节点中文名称
			refreshNodeChName(curTreeNode);
			// 刷新节点
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * 右括号按钮点击事件
	 */
	private class RParenthesisActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;
			ConditionNodeObj conditionObj = (ConditionNodeObj) curTreeNode
					.getUserObject();
			if (conditionObj.getRParenthesis() != null) {
				conditionObj.setRParenthesis(conditionObj.getRParenthesis()
						+ ")");
			} else {
				conditionObj.setRParenthesis(")");
			}
			curTreeNode.setUserObject(conditionObj);
			// 刷新节点中文名称
			refreshNodeChName(curTreeNode);
			// 刷新节点
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * 删括号按钮点击事件
	 */
	private class DelParenthesisActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;
			ConditionNodeObj conditionObj = (ConditionNodeObj) curTreeNode
					.getUserObject();
			conditionObj.setLParenthesis("");
			conditionObj.setRParenthesis("");
			curTreeNode.setUserObject(conditionObj);
			// 刷新节点中文名称
			refreshNodeChName(curTreeNode);
			// 刷新节点
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);

		}
	}

	private class ConditionNodeObj {

		// ID
		String ID;

		// 名称
		String chName;

		// 比较类型
		String compareType;

		// 参数值
		Object paraValue;

		// 作用数据源ID
		String sourceID;

		// 作用数据源字段ID
		String sourceColID;

		// and 或 or
		String joinBefore;

		// 左括号
		String lParenthesis = "";

		// 右括号
		String rParenthesis = "";

		// 是字段与字段比较，还是值比较 ,true：表示字段，false:表示值
		boolean fieldFlag = false;

		// 字段比较时中文字段名
		String fieldFname;

		public String getLParenthesis() {
			return lParenthesis;
		}

		public void setLParenthesis(String parenthesis) {
			lParenthesis = parenthesis;
		}

		public String getRParenthesis() {
			return rParenthesis;
		}

		public void setRParenthesis(String parenthesis) {
			rParenthesis = parenthesis;
		}

		public String toString() {
			return chName;
		}

		public String getChName() {
			return chName;
		}

		public void setChName(String chName) {
			this.chName = chName;
		}

		public String getCompareType() {
			return compareType;
		}

		public void setCompareType(String compareType) {
			this.compareType = compareType;
		}

		public String getID() {
			return ID;
		}

		public void setID(String id) {
			ID = id;
		}

		public Object getParaValue() {
			return paraValue;
		}

		public void setParaValue(Object paraValue) {
			this.paraValue = paraValue;
		}

		public String getSourceColID() {
			return sourceColID;
		}

		public void setSourceColID(String sourceColID) {
			this.sourceColID = sourceColID;
		}

		public String getSourceID() {
			return sourceID;
		}

		public void setSourceID(String sourceID) {
			this.sourceID = sourceID;
		}

		public String getJoinBefore() {
			return joinBefore;
		}

		public void setJoinBefore(String joinBefore) {
			this.joinBefore = joinBefore;
		}

		public boolean isFieldFlag() {
			return fieldFlag;
		}

		public void setFieldFlag(boolean fieldFlag) {
			this.fieldFlag = fieldFlag;
		}

		public String getFieldFname() {
			return fieldFname;
		}

		public void setFieldFname(String fieldFname) {
			this.fieldFname = fieldFname;
		}
	}

	/**
	 * 显示条件值树
	 * 
	 */
	private class WhereActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String sourceID = null;
			String sourceColID = null;
			if (dataSourceCbx.getValue() != null) {
				sourceID = dataSourceCbx.getValue().toString();
			}
			if (cbxFieldName.getValue() != null) {
				sourceColID = cbxFieldName.getValue().toString();
			}
			if (Common.isNullStr(sourceID) || Common.isNullStr(sourceColID)) {
				new MessageBox(DataSourceSet.this, "请选择数据源和字段名!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				return;
			}

			// 得到是不是枚举编码
			boolean isEnumCode = DefinePub.judgetEnumWithColID(querySource,
					sourceID, sourceColID, true);

			DataSourceWhereDlg dataSourceWhereDlg = null;
			try {
				// 判断是否枚举编码
				if (isEnumCode) {
					// 得到枚举ID值
					String sEnumId = DefinePub.getEnumIDWithColID(querySource,
							sourceID, sourceColID);
					// 判断是否原选择枚举列
					if (!sEnumId.equals(oldEnumID)) {
						String sEnumIdTmp = sEnumId
								.substring(IDefineReport.ENUM_.length());
						mapWhereValue = reportGuideUI.definReportServ
								.getEnumDataWithEnumID(sEnumIdTmp);
						mapWhereValue.put(ISysRefCol.REFCOL_ID, sEnumIdTmp);
						oldEnumID = sEnumId;
					}
				} else {
					mapWhereValue = null;
				}

				// 显示条件值选择对话框
				dataSourceWhereDlg = new DataSourceWhereDlg(DataSourceSet.this,
						mapWhereValue, cbxWhereValue.getValue().toString());
				Tools.centerWindow(dataSourceWhereDlg);
				dataSourceWhereDlg.setVisible(true);

				// 得到选择的值
				String whereValue = dataSourceWhereDlg.getResultValue();
				if (whereValue != null) {
					cbxWhereValue.setValue(whereValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(DataSourceSet.this,
						"显示对话框出现错误,错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				if (dataSourceWhereDlg != null)
					dataSourceWhereDlg.dispose();
			}
		}
	}

	/**
	 * 根据列表和字段名，组织语句
	 * 
	 * @param lstValue
	 * @param sFieldName
	 * @return
	 */
	private String[] setRefValue(List lstValue, String codeField,
			String nameField, Vector vector, String[] displayValues,
			Map mapFieldFlag, boolean fieldFlag) {

		if (lstValue != null) {
			if (displayValues == null) {
				displayValues = new String[lstValue.size()];
			} else {
				String[] displayValuesDest = new String[displayValues.length
						+ lstValue.size()];
				System.arraycopy(displayValues, 0, displayValuesDest, 0,
						displayValues.length);
				displayValues = displayValuesDest;
			}
			int i = 0;
			for (Iterator it = lstValue.iterator(); it.hasNext();) {
				XMLData data = (XMLData) it.next();
				vector.add(data.get(codeField).toString());
				displayValues[i] = data.get(nameField).toString();
				i++;
				mapFieldFlag
						.put(data.get(codeField).toString() + "||"
								+ data.get(nameField).toString(),
								fieldFlag ? "1" : "0");
			}
		}
		return displayValues;
	}
}
