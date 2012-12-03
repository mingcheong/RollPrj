/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.common.ui.input.IntegerTextField;
import gov.nbcs.rp.common.ui.list.CustomComboBox;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RadioModel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入栏目管理客户端主界面
 * </p>
 * <p>
 * Description:收入栏目管理客户端主界面
 * </p>

 */
public class IncColumn extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 右边详细信息编辑面板
	FPanel rightPanel = null;

	// 收入栏目数据集
	DataSet dsIncCol = null;

	// 内码文本框
	FTextField ftxtPriCode = null;

	// 编号数字框
	IntegerTextField ftxtfIncColCode = null;

	// 名称文本框
	FTextField ftxtfIncColName = null;

	// 该栏目需纵向求和选择框
	FCheckBox fchkSumFlag = null;

	// 该栏目隐藏选择框
	FCheckBox fchkHideFlag = null;

	// CL,09,08,24
	// 该栏目为预留比例
	FCheckBox fchkRPFlag = null;

	// 数据来源单选框
	FRadioGroup frdoIncColDts = null;

	// 显示格式组合框
	CustomComboBox fcbxSFormate = null;

	// 计算公式按钮
	FButton fbtnIncColEditFormula = null;

	// 计算公式多行文本框
	FTextArea ftxtaIncColFormula = null;

	// 计算优先级数字微调框
	IntegerSpinner jspIncColCalcPRI = null;

	// 定义计算公式按钮面板
	private FPanel fpnlEditFormula = null;

	// 定义计算优先级面板
	FPanel fpnlIncColCalcPRI = null;

	// 收费项目树
	CustomTree ftreIncomeSubItem = null;

	// 收费项目DataSet
	DataSet dsIncome = null;

	// 定义收费项目面板
	private IncomeSubItem fsPnlIncomeSubItem = null;

	// boolean DSFlag = false;

	// 录入和计算显示不同信息的面板
	private FPanel fpnlInOrCompute = null;

	// 收入栏目树
	CustomTree ftreeIncColumn = null;

	// 创建一个收入栏目对象
	IncColumnObj incColumnObj = null;

	// 层次码编码规则
	SysCodeRule lvlIdRule = UntPub.lvlRule;;

	// 保存类型参数
	// sSaveType=modname：不是叶节点，只能修改名称;
	// sSaveType=mod:修改叶节点;
	// sSaveType=modformate:只能修改格式;
	// sSaveType=add增加节点;
	// sSaveType=addfirstson增加第一个子节点
	String sSaveType = null;

	// 连接数据库接口
	ISysIaeStru sysIaeStruServ = null;

	// 公共方法数据接口
	IPubInterface iPubInterface = null;

	/**
	 * 构造函数
	 * 
	 */
	public IncColumn() {

	}

	/**
	 * 初始化界面
	 */
	public void initize() {
		// 定义数据库接口
		sysIaeStruServ = SysIaeStruI.getMethod();
		// 公共方法数据接口
		iPubInterface = PubInterfaceStub.getMethod();
		try {
			// 创建左右分隔的分隔控件
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// 左边树,放到fSplitPane左边
			ftreeIncColumn = new CustomTree(IIncColumn.INCCOL_ROOT, null,
					IIncColumn.LVL_ID, ISysIaeStru.NAME, IIncColumn.PAR_ID,
					null);
			FScrollPane fspnlIncColumn = new FScrollPane(ftreeIncColumn);
			fSplitPane.addControl(fspnlIncColumn);

			// 右边详细信息编辑面板,放到分隔控件右边
			rightPanel = new FPanel();
			RowPreferedLayout rLayout = new RowPreferedLayout(1);
			rLayout.setColumnWidth(450);
			rightPanel.setLayout(rLayout);
			fSplitPane.addControl(rightPanel);
			// 设置rightPanel边界像素为10
			rightPanel.setLeftInset(10);
			rightPanel.setTopInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);

			// 定义内码文本框
			ftxtPriCode = new FTextField("内码：");
			ftxtPriCode.setProportion(0.24f);
			// 定义数字框
			ftxtfIncColCode = new IntegerTextField("编码：");
			ftxtfIncColCode.setProportion(0.24f);
			// 定义名称文本框
			ftxtfIncColName = new FTextField("名称：");
			ftxtfIncColName.setProportion(0.24f);
			// 定义该栏目需纵向求和选择框
			fchkSumFlag = new FCheckBox("该栏目需纵向求和");
			fchkSumFlag.setTitlePosition("RIGHT");
			// 定义该栏目隐藏选择框
			fchkHideFlag = new FCheckBox("该栏目隐藏");
			fchkHideFlag.setTitlePosition("RIGHT");
			// 定义该栏目为预留比例
			fchkRPFlag = new FCheckBox("该栏目为预留比例（按收费项目预设预留比例时使用）");
			fchkRPFlag.setTitlePosition("RIGHT");

			// 定义数据来源面板
			FPanel fpnlIncColDts = new FPanel();
			fpnlIncColDts.setLayout(new RowPreferedLayout(1));
			fpnlIncColDts.setTitle("数据来源");
			fpnlIncColDts.setFontSize(this.getFont().getSize());
			fpnlIncColDts.setFontName(this.getFont().getName());
			fpnlIncColDts.setTitledBorder();

			// 定义数据来源单选框
			frdoIncColDts = new FRadioGroup("", FRadioGroup.HORIZON) {

				private static final long serialVersionUID = 1L;

				public void setValue(Object value) {
					RadioModel model = (RadioModel) frdoIncColDts.getRefModel();
					if (model == null)
						return;
					JRadioButton radios[] = frdoIncColDts.getRadios();
					if (radios == null)
						return;
					for (int i = radios.length - 1; i >= 0; i--) {
						if (model.getValueAt(i).toString().equals(value)) {
							if (!radios[i].isSelected())
								radios[i].setSelected(true);
							continue;
						}
						if (radios[i].isSelected())
							radios[i].setSelected(false);
					}

				}

				protected void fireValueChange(Object oldValue, Object newValue) {
					super.fireValueChange(oldValue, newValue);
					// 根据RadioGroup是录入还是计算状态，显示收费项目树还是设置公式界面
					setState();
				}

			};
			frdoIncColDts.setRefModel("0#录入        +1#计算        ");
			fpnlIncColDts.addControl(frdoIncColDts, new TableConstraints(1, 1,
					true));

			// frdoIncColDts.addValueChangeListener(new ValueChangeListener() {
			//
			// public void valueChanged(ValueChangeEvent valuechangeevent) {
			// // 根据RadioGroup是录入还是计算状态，显示收费项目树还是设置公式界面
			// setState();
			//
			// }
			// });

			// // 为数据来源单选框加值改变事件
			// JRadioButton radios[] = frdoIncColDts.getRadios();
			// for (int i = 0; i < radios.length; i++) {
			// radios[i].addChangeListener(new ChangeListener() {
			//
			// public void stateChanged(ChangeEvent arg0) {
			// // 根据RadioGroup是录入还是计算状态，显示收费项目树还是设置公式界面
			// setState();
			//
			// }
			// });
			// }

			// 获得显示格式数据集
			DataSet dsSFormate = sysIaeStruServ.getSFormate(Integer
					.parseInt(Global.loginYear));
			// 定义显示格式组合框
			fcbxSFormate = new CustomComboBox(dsSFormate, "name", "name");
			fcbxSFormate.setTitle("显示格式：");
			fcbxSFormate.setProportion(0.24f);

			// 定义计算公式按钮面板
			fpnlEditFormula = new FPanel();
			RowPreferedLayout editFormulaLayout = new RowPreferedLayout(5);
			editFormulaLayout.setColumnWidth(30);
			fpnlEditFormula.setLayout(editFormulaLayout);
			FLabel flblIncColEditFormula = new FLabel();
			flblIncColEditFormula.setTitle("计算公式：");
			fbtnIncColEditFormula = new FButton("jsButton", "...");
			fpnlEditFormula.addControl(flblIncColEditFormula,
					new TableConstraints(1, 1, true, true));
			fpnlEditFormula.addControl(fbtnIncColEditFormula,
					new TableConstraints(1, 1, true, false));
			// 为计算公式按钮加点击事件
//			fbtnIncColEditFormula.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
////					FormulaPanelCheck formulaPanel = new FormulaPanelCheck(
////							false, null);
//					try {
//						DataSet dsIncColCalc = sysIaeStruServ
//								.getIncColTreCalc(Integer
//										.parseInt(Global.loginYear), dsIncCol
//										.fieldByName("inccol_code").getString());
//						CustomTree ftreeIncColCalc = new CustomTree("可选栏目",
//								dsIncColCalc, IIncColumn.INCCOL_CODE,
//								IIncColumn.INCCOL_FNAME, IIncColumn.PAR_ID,
//								null);
////						formulaPanel.initTree(ftreeIncColCalc);
////						formulaPanel.setFormula(ftxtaIncColFormula.getValue()
////								.toString());
//					} catch (Exception ex) {
//						JOptionPane.showMessageDialog(IncColumn.this,
//								"显示设置公式界面发生错误，错误信息：" + ex.getMessage(), "提示",
//								JOptionPane.ERROR_MESSAGE);
//					}
//
//					formulaPanel.setTreField(IIncColumn.INCCOL_CODE,
//							IIncColumn.INCCOL_FNAME);
//					Tools.centerWindow(formulaPanel);
//					formulaPanel.setVisible(true);
//					ftxtaIncColFormula.setValue(formulaPanel.getFormula());
//				}
//			});

			// 定义计算公式多行编辑框
			ftxtaIncColFormula = new FTextArea(false);
			ftxtaIncColFormula.setEditable(false);

			// 定义计算优先级面板
			fpnlIncColCalcPRI = new FPanel();
			fpnlIncColCalcPRI.setLayout(new RowPreferedLayout(4));
			// 定义计算优先级Label
			FLabel flblIncColCalcPRI = new FLabel();
			flblIncColCalcPRI.setTitle("计算优先级：");
			// 定义计算优先级微调框
			SpinnerModel modelCalcPRI = new SpinnerNumberModel(0, 0, 100, 1);
			jspIncColCalcPRI = new IntegerSpinner(modelCalcPRI);
			fpnlIncColCalcPRI.addControl(flblIncColCalcPRI,
					new TableConstraints(1, 1, false, true));
			fpnlIncColCalcPRI.add(jspIncColCalcPRI, new TableConstraints(1, 3,
					false, true));

			// 定义收费项目面板
			fsPnlIncomeSubItem = new IncomeSubItem();

			// mod by CL， 09，08，24
			// 定义计算收费项目面板
			// fsPnlIncomeSubItem1 = new IncomeSubItem();
			// 定义录入和计算显示不同信息的面板
			fpnlInOrCompute = new FPanel();

			// 内码文本控件
			rightPanel.addControl(ftxtPriCode, new TableConstraints(1, 1,
					false, false));
			// 编号数字框
			rightPanel.addControl(ftxtfIncColCode, new TableConstraints(1, 1,
					false, false));
			// 名称文本控件
			rightPanel.addControl(ftxtfIncColName, new TableConstraints(1, 1,
					false, false));
			// 该栏目需纵向求和选择框
			rightPanel.addControl(fchkSumFlag, new TableConstraints(1, 1,
					false, false));
			// 该栏目隐藏选择框
			rightPanel.addControl(fchkHideFlag, new TableConstraints(1, 1,
					false, false));
			// 该栏目为预留比例
			rightPanel.addControl(fchkRPFlag, new TableConstraints(1, 1, false,
					false));
			// 数据来源单选框
			rightPanel.addControl(fpnlIncColDts, new TableConstraints(2, 1,
					false, false));
			// 显示格式组合框
			rightPanel.addControl(fcbxSFormate, new TableConstraints(1, 1,
					false, false));
			// 录入和计算显示不同面板
			rightPanel.addControl(fpnlInOrCompute, new TableConstraints(1, 1,
					true, false));

			// 数据来源默认设计为录入
			frdoIncColDts.setValue("0");

			// 默认设右边详细信息编辑面板中控件不可编辑
			setAllControlEnabledFalse();

			// 创建工具栏
			this.createToolBar();
			// 根据系统状态设置工具栏按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsIncCol,
					this, ftreeIncColumn);
			setActionStatus.setState(true, true);
		} catch (Exception e) {
			// throw new FAppException("WF-000001", e.getMessage());
			JOptionPane.showMessageDialog(Global.mainFrame,
					"显示收入栏目显示界面发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 收费项目面板
	 * 
	 */
	public class IncomeSubItem extends FPanel {

		private static final long serialVersionUID = 1L;

		public IncomeSubItem() throws Exception {

//			ftreIncomeSubItem = new CustomTree("收费项目", null,
//					IPubInterface.IncSubItem_ID,
//					IPubInterface.IncSubItem_FNAME,
//					IPubInterface.IncSubItem_PARENT_ID, null,
//					IPubInterface.IncSubItem_Code, true);
			ftreIncomeSubItem.setIsCheckBoxEnabled(false);
			FScrollPane fpnlIncomeSubItem = new FScrollPane();
			fpnlIncomeSubItem.addControl(ftreIncomeSubItem);
			this.setTitle("收入栏目对应收费项目");
			this.setLayout(new RowPreferedLayout(1));
			this.addControl(fpnlIncomeSubItem);
		}
	}

	/**
	 * 加载界面数据
	 * 
	 */
	public void modulePanelActived() {
		// 如果收入栏目数据集已从数据库中获取，退出
		if (dsIncCol != null) {
			return;
		}
		try {
			// 从数据库中获取收入栏目数据集
			dsIncCol = sysIaeStruServ.getIncColTre(Integer
					.parseInt(Global.loginYear));
			// 为收入栏目数据集增加状态转换事件
			dsIncCol.addStateChangeListener(new IncColumnStateChangeListener(
					this));
			// 为收入栏目树设置数据集
			ftreeIncColumn.setDataSet(dsIncCol);
			ftreeIncColumn.reset();
			// 为收入栏目树设置SelectionListener事件
			ftreeIncColumn
					.addTreeSelectionListener(new IncColumnTreeSelectionListener(
							this));
			// 获得收费项目信息
			String sFilter = "set_year =" + Global.loginYear;
//			dsIncome = PubInterfaceStub.getMethod().getIncSubItem(sFilter);
			ftreIncomeSubItem.setDataSet(dsIncome);
			ftreIncomeSubItem.reset();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"收入栏目加载数据发生错误，请与管理联系。", "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 增加收入栏目
	 * 
	 */
	public void doAdd() {
		try {
			// 如果未选中节点（即新增节点的父节点）,退出
			if (ftreeIncColumn.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"请选择一个收入栏目,作为新增收入栏目的父节点！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// 定义一个收入栏目对象
			incColumnObj = new IncColumnObj();
			// 保存当前选中的节点信息（即新增节点的父节点）信息于收入栏目对象
			saveParInfo(incColumnObj);
			// 创建收入栏目增加操作类
			IncColumnAdd incColumnAdd = new IncColumnAdd(this);
			// 执行增加收入栏目操作，判断操作是否成功
			if (!incColumnAdd.add()) {
				return;
			}
			// 设置未保存状态标态为false
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"增加收入栏目信息发生错误，错误信息:" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 删除收入栏目
	 * 
	 */
	public void doDelete() {
		try {
			// 判断是否选中节点, 如未选中(==null)，退出;已选中(!=null)继续向下执行
			if (ftreeIncColumn.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "请选择一个收入栏目！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// 创建一个收入栏目对象
			incColumnObj = new IncColumnObj();
			// 保存当前删除节点的信息
			saveParInfo(incColumnObj);
			// 创建收入栏目删除操作类
			IncColumnDel incColumnDel = new IncColumnDel(this);
			// 执行删除收入栏目操作
			if (incColumnDel.delete()) {// 调用删除方法
				// 清空控件显示信息
				clearShowInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"删除收入栏目信息发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * 取消收入栏目编辑
	 */
	public void doCancel() {
		try {
			// 数据集取消编辑
			dsIncCol.cancel();
			// 当前节点是根节点
			if ("".equals(incColumnObj.LVL_ID)) {
				clearShowInfo();
				// 定位到当前选中的节点
			} else {
				ftreeIncColumn.expandTo(IIncColumn.LVL_ID, incColumnObj.LVL_ID);
			}
			// 设置数据保存状态为true
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"取消收入栏目辑编发生错误，，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 插入收入栏目编辑
	 */
	public void doInsert() {

	}

	/**
	 * 修改收入栏目编辑
	 */
	public void doModify() {
		try {
			// 判断是否选中一收入栏目
			if (ftreeIncColumn.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "请选择一个收入栏目！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// 创建一收入栏目节点保存对象
			incColumnObj = new IncColumnObj();
			// 保存选中的节点信息
			saveParInfo(incColumnObj);

			// 创建收入栏目修改控制类
			IncColumnModify incColumnModify = new IncColumnModify(this);
			// 判断是否修改成功
			if (!incColumnModify.modify())
				return;
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"修改收入栏目信息发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 保存收入栏目
	 */
	public void doSave() {
		try {
			// 创建收入栏目保存类
			IncColumnSave incColumnSave = new IncColumnSave(this);
			// 调用收入栏保存类保存方法
			incColumnSave.save();
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"保存收入栏目信息发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 关闭界面
	 */
	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	/**
	 * add,mod,del操作前，保存父节点信息
	 * 
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private void saveParInfo(IncColumnObj incColumnObj)
			throws NumberFormatException, Exception {
		// 判断选择的是否是根节点，如果选中的是根节点，incColumnObj设为初始值
		if (ftreeIncColumn.getSelectedNode() == ftreeIncColumn.getRoot()
				|| dsIncCol.isEmpty()
				|| ftreeIncColumn.getSelectedNode() == null) {// 根节点
			incColumnObj.INCCOL_CODE = "";// 收入栏目编码
			incColumnObj.INCCOL_NAME = "";// 收入栏目中文名
			incColumnObj.INCCOL_FNAME = "";// 收入栏目全名
			incColumnObj.INCCOL_ENAME = "";// 收入栏目全名
			incColumnObj.DATA_SOURCE = 0;// 数据来源
			incColumnObj.FORMULA_DET = "";// 计算公式内容
			incColumnObj.CALC_PRI = 0;// 计算优先级
			incColumnObj.SUM_FLAG = 0;// 栏目是否需要纵向求和
			incColumnObj.IS_HIDE = 0;// 是否隐藏
			incColumnObj.N2 = 0;// 是否需要预留比例
			incColumnObj.END_FLAG = 0;// 末级标志
			incColumnObj.DISPLAY_FORMAT = "";// 显示格式
			incColumnObj.EDIT_FORMAT = "";// 编辑格式
			incColumnObj.LVL_ID = ""; // 层次码
			incColumnObj.PAR_ID = ""; // 父层次码
			incColumnObj.SET_YEAR = Integer.parseInt(Global.loginYear);// 预算年度
		} else {// 不是根节点
			BeanUtil.mapping(incColumnObj, dsIncCol.getOriginData());
		}
	}

	/**
	 * 根据RadioGroup是录入还是计算状态，显示收费项目树还是设置公式界面
	 * 
	 */
	public void setState() {
		if (frdoIncColDts.getValue() == null) {
			return;
		}
		fpnlInOrCompute.removeAll();
		// 根据界面选择的数据来源，显示收费项目树还是设置公式界面
		if ("0".equals(frdoIncColDts.getValue().toString())) {// 录入
			fpnlInOrCompute.setLayout(new RowPreferedLayout(1));
			fpnlInOrCompute.addControl(fsPnlIncomeSubItem);
			fpnlInOrCompute.updateUI();

		} else if ("1".equals(frdoIncColDts.getValue().toString())) {// 计算
			fpnlInOrCompute.setLayout(new RowPreferedLayout(2));
			// 计算公式面板
			// fpnlInOrCompute.addControl(fpnlEditFormula, new
			// TableConstraints(1,
			// 2, false, true));
			// // 计算公式多行编辑框
			// fpnlInOrCompute.addControl(ftxtaIncColFormula,
			// new TableConstraints(1, 2, true, true));
			// // 计算优先级面板
			// fpnlInOrCompute.addControl(fpnlIncColCalcPRI, new
			// TableConstraints(
			// 1, 2, false, true));
			// mod
			// 计算公式面板
			fpnlInOrCompute.addControl(fpnlEditFormula, new TableConstraints(1,
					2, false, true));
			// 计算公式多行编辑框
			fpnlInOrCompute.addControl(ftxtaIncColFormula,
					new TableConstraints(1, 2, true, true));
			// 计算优先级面板
			fpnlInOrCompute.addControl(fpnlIncColCalcPRI, new TableConstraints(
					1, 2, false, true));
			fpnlInOrCompute.addControl(fsPnlIncomeSubItem,
					new TableConstraints(1, 2, true, true));
			// fpnlInOrCompute.addControl(fsPnlIncomeSubItem1,new
			// TableConstraints(1, 2, true, true));
			fpnlInOrCompute.updateUI();
		}
	}

	/**
	 * 清空控件显示信息
	 * 
	 */
	private void clearShowInfo() {
		// 内码
		ftxtPriCode.setValue("");
		ftxtfIncColCode.setValue("");
		// 名称
		ftxtfIncColName.setValue("");
		// 该栏目需纵向求和
		((JCheckBox) fchkSumFlag.getEditor()).setSelected(false);
		// 该栏目隐藏
		((JCheckBox) fchkHideFlag.getEditor()).setSelected(false);
		// 该栏目为预留比例
		((JCheckBox) fchkRPFlag.getEditor()).setSelected(false);
		// 数据来源
		frdoIncColDts.setValue("0");
		// 显示格式
		fcbxSFormate.setValue("");
		// 计算公式按钮
		fbtnIncColEditFormula.setEnabled(false);
		// 计算公式
		ftxtaIncColFormula.setValue("");
		ftxtaIncColFormula.setEditable(false);
		// 计算优先级
		jspIncColCalcPRI.setValue(new Integer(0));
		// 设置收费项目树所有节点为不选中状态
		SetSelectTree.setIsNoCheck(ftreIncomeSubItem);
	}

	/**
	 * 
	 * 设编辑区控件不可编辑
	 */
	public void setAllControlEnabledFalse() {
		Common.changeChildControlsEditMode(rightPanel, false);

		// 如果当前选中录入界面设置计算面板上的组件为不可用
		if ("0".equals(frdoIncColDts.getValue().toString())) {
			// 计算按钮面板
			Common.changeChildControlsEditMode(fpnlEditFormula,
					false);
		} else {
			// 设置收费项目树不可编辑
			ftreIncomeSubItem.setIsCheckBoxEnabled(false);
		}
		// 设置计算优先级数字微调框不可编辑
		jspIncColCalcPRI.setEnabled(false);
	}

	/**
	 * 
	 * 设编辑区控件可编辑
	 */
	public void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(rightPanel, true);

		// 如果当前选中录入界面设置计算面板上的组件为可用
		if ("0".equals(frdoIncColDts.getValue().toString())) {
			// 计算按钮面板
			Common
					.changeChildControlsEditMode(fpnlEditFormula,
							true);
		} else {
			// 设置收费项目树可编辑
			ftreIncomeSubItem.setIsCheckBoxEnabled(true);
		}
		// 设置计算公式多行文本框不可编辑
		ftxtaIncColFormula.setEditable(false);
		// 设置内码文本框不可编辑
		ftxtPriCode.setEditable(false);
		// 设置计算优先级数字微调框可编辑
		jspIncColCalcPRI.setEnabled(true);
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
