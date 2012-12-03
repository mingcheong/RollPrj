/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.BatchUpdateException;

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
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.common.ui.input.IntegerTextField;
import gov.nbcs.rp.common.ui.list.CustomComboBox;
import gov.nbcs.rp.common.ui.list.CustomList;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
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
 * Title:支出资金来源管理客户端主界面
 * </p>
 * <p>
 * Description:支出资金来源管理客户端主界面
 * </p>
 * <p>
 *
 */
public class PayOutFS extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 支出资金来源DataSet
	DataSet dsPayOutFS = null;

	// 支出资金来源性质
	DataSet dsKind = null;

	// 右边编辑区
	FPanel rightPanel = null;

	// 支出资金来源内码文本框
	FTextField ftxtPriCode = null;

	// 支出资金来源编码文本框
	IntegerTextField ftxtfPfsCode = null;

	// 支出资金来源名称文本框
	FTextField ftxtfPfsName = null;

	// 支出资金来源性质下拉列表框
	CustomList flstPfsKind = null;

	// 参与控制数分配选择框
	FCheckBox fchkPfsFlag = null;

	// 默认隐藏
	FCheckBox fchkHide = null;

	// 是否收支平衡
	FCheckBox fchkIsBalance = null;

	// 支持项目情况选择框
	FRadioGroup frdoSupPrj = null;

	// 数据来源单选框
	FRadioGroup frdoIncColDts = null;

	// 显示格式组合框
	CustomComboBox fcbxSFormate = null;

	// 计算公式按钮
	FButton fbtnPfsEditFormula = null;

	// 计算公式多行文本框
	FTextArea ftxtaPfsFormula = null;

	// 计算优先级微调框
	IntegerSpinner jspPfsCalcPRI = null;

	// 支出资金来源树
	CustomTree ftreePayOutFS = null;

	// 层次码编码规则
	SysCodeRule lvlIdRule = null;

	// 保存类型add增加,addfirstson增加第一个儿子,mod修改叶节点未被使用,modformate修改叶节点已被使用,modname修改节点，此节点有叶节
	String sSaveType = null;

	// 支出资金来源数据对象
	PayOutFsObj payOutFsObj = null;

	// 数据库接口
	ISysIaeStru sysIaeStruServ = null;

	// 定义功能面板
	AcctPanel fpnlAcct = null;

	// 录入或计算面板
	private FPanel fpnlInOrCompute = null;

	// 定义计算公式按钮面板
	private FPanel fpnlEditFormula = null;

	// 定义计算优先级面板
	FPanel fpnlPfsCalcPRI = null;

	// 功能科目
	private DataSet dsAcct = null;

	// 支出资金来源查询报表名称文本框
	FTextField ftxtReportPfsName = null;

	/**
	 * 构造函数
	 * 
	 */
	public PayOutFS() {
	}

	/**
	 * 初始化界面
	 */
	public void initize() {
		try {
			// 定义数据库接口
			sysIaeStruServ = SysIaeStruI.getMethod();
			// 设置分栏
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// 定义支出资金来源树
			ftreePayOutFS = new CustomTree(IPayOutFS.PFS_ROOT, null,
					IPayOutFS.LVL_ID, ISysIaeStru.NAME, IPayOutFS.PAR_ID, null);
			FScrollPane fspnlPayOutFs = new FScrollPane(ftreePayOutFS);
			fSplitPane.addControl(fspnlPayOutFs);

			// 定义右边详细信息编辑区面板
			rightPanel = new FPanel();
			fSplitPane.addControl(rightPanel);
			// 设置rightPanel边界像素为10
			rightPanel.setTopInset(10);
			rightPanel.setLeftInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);
			RowPreferedLayout rLayout = new RowPreferedLayout(4);
			rLayout.setColumnWidth(80);
			rightPanel.setLayout(rLayout);

			// 定义内码文本框
			ftxtPriCode = new FTextField("内码：");
			ftxtPriCode.setProportion(0.21f);

			// 定义层次码文本框
			ftxtfPfsCode = new IntegerTextField("编码：");
			ftxtfPfsCode.setProportion(0.21f);

			// 定义名称文本框
			ftxtfPfsName = new FTextField("名称：");
			ftxtfPfsName.setProportion(0.21f);

			ftxtReportPfsName = new FTextField("报表名称：");
			ftxtReportPfsName.setProportion(0.21f);

			// 定义性质
			FLabel flblPfsKind = new FLabel();
			flblPfsKind.setTitle("性质：");
			// 定义性质面板
			FPanel fpnlPfsKind = new FPanel();
			RowPreferedLayout rLayoutKind = new RowPreferedLayout(2);
			rLayoutKind.setColumnWidth(55);
			fpnlPfsKind.setLayout(rLayoutKind);
			// 获得性质信息
			dsKind = sysIaeStruServ.getKind("IAEFSSTANDITEM", Global.loginYear);
			// 定义性质列表
			flstPfsKind = new CustomList(dsKind, "code", "name");
			FScrollPane fspnlPfsKind = new FScrollPane();
			fspnlPfsKind.addControl(flstPfsKind);
			fpnlPfsKind.addControl(flblPfsKind, new TableConstraints(1, 1,
					false, false));
			fpnlPfsKind.addControl(fspnlPfsKind, new TableConstraints(3, 1,
					false, true));

			// 参与控制数分配
			fchkPfsFlag = new FCheckBox("参与控制数分配");
			fchkPfsFlag.setTitlePosition("RIGHT");

			// 参与控制数分配
			fchkHide = new FCheckBox("默认隐藏");
			fchkHide.setTitlePosition("RIGHT");

			fchkIsBalance = new FCheckBox("收支平衡");
			fchkIsBalance.setTitlePosition("RIGHT");

			// 支持项目情况
			FPanel fpnlSupPrj = new FPanel();
			fpnlSupPrj.setLayout(new RowPreferedLayout(1));
			fpnlSupPrj.setTitle("支持项目情况");
			fpnlSupPrj.setFontSize(this.getFont().getSize());
			fpnlSupPrj.setFontName(this.getFont().getName());
			fpnlSupPrj.setTitledBorder();
			frdoSupPrj = new FRadioGroup("", FRadioGroup.HORIZON);
			frdoSupPrj.setRefModel("0#不支持+1#支持+2#仅支持");
			frdoSupPrj.setValue("0");
			fpnlSupPrj.addControl(frdoSupPrj, new TableConstraints(1, 1, true));

			// 数据来源panel
			FPanel fpnlPfsDts = new FPanel();
			fpnlPfsDts.setLayout(new RowPreferedLayout(1));
			fpnlPfsDts.setTitle("数据来源");
			fpnlPfsDts.setFontSize(this.getFont().getSize());
			fpnlPfsDts.setFontName(this.getFont().getName());
			fpnlPfsDts.setTitledBorder();
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
			frdoIncColDts.setRefModel("0#录入+1#计算");
			fpnlPfsDts.addControl(frdoIncColDts, new TableConstraints(1, 1,
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

			// 获得显示格式信息
			DataSet dsSFormate = sysIaeStruServ.getSFormate(Integer
					.parseInt(Global.loginYear));
			// 定义显示格式组合框
			fcbxSFormate = new CustomComboBox(dsSFormate, "name", "name");
			fcbxSFormate.setTitle("显示格式：");
			fcbxSFormate.setProportion(0.21f);

			// 定义计算公式按钮面板
			fpnlEditFormula = new FPanel();
			RowPreferedLayout editFormulaLayout = new RowPreferedLayout(5);
			editFormulaLayout.setColumnWidth(30);
			fpnlEditFormula.setLayout(editFormulaLayout);
			// 定义计算公式Label
			FLabel flblPfsEditFormula = new FLabel();
			flblPfsEditFormula.setTitle("计算公式：");
			// 定义计算公式点击按钮
			fbtnPfsEditFormula = new FButton("jsButton", "...");
			fpnlEditFormula.addControl(flblPfsEditFormula,
					new TableConstraints(1, 1, true, true));
			fpnlEditFormula.addControl(fbtnPfsEditFormula,
					new TableConstraints(1, 1, true, false));

			// 计算公式按钮事件
			fbtnPfsEditFormula.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					FormulaPanelCheck formulaPanel = new FormulaPanelCheck(
//							false, null);
					try {
						DataSet dsIncColCalc = sysIaeStruServ.getPayOutFSCalc(
								Integer.parseInt(Global.loginYear), dsPayOutFS
										.fieldByName("pfs_code").getString());
						CustomTree ftreePfsCalc = new CustomTree("可选栏目",
								dsIncColCalc, IPayOutFS.PFS_CODE,
								IPayOutFS.PFS_FNAME, IIncColumn.PAR_ID, null);
//						formulaPanel.initTree(ftreePfsCalc);
//						formulaPanel.setFormula(ftxtaPfsFormula.getValue()
//								.toString());
//
//						formulaPanel.setTreField(IPayOutFS.PFS_CODE,
//								IPayOutFS.PFS_FNAME);
//						Tools.centerWindow(formulaPanel);
//						formulaPanel.setVisible(true);
//						String sForumla = formulaPanel.getFormula();
//						ftxtaPfsFormula.setValue(sForumla);
//
//						// 替换公式中文内容
//						String sForumlaEname = PubInterfaceStub.getMethod()
//								.replaceTextExDs(sForumla, 0, dsPayOutFS,
//										IPayOutFS.PFS_FNAME,
//										IPayOutFS.PFS_ENAME);
//						// 根据公式内容，得到sup_prj值
//						int supPrj = getsupPrjValue(sForumlaEname, dsPayOutFS);
//						frdoSupPrj.setValue(String.valueOf(supPrj));
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(PayOutFS.this,
								"显示公式编辑界面发生错误，错误信息：" + ex.getMessage(), "提示",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			// 计算公式
			ftxtaPfsFormula = new FTextArea(false);

			// 定义计算优先级面板
			fpnlPfsCalcPRI = new FPanel();
			fpnlPfsCalcPRI.setLayout(new RowPreferedLayout(4));
			// 定义计算优先级Label
			FLabel flblPfsCalcPRI = new FLabel();
			flblPfsCalcPRI.setTitle("计算优先级：");
			// 定义计算优先级微调框
			SpinnerModel modelCalcPRI = new SpinnerNumberModel(0, 0, 100, 1);
			jspPfsCalcPRI = new IntegerSpinner(modelCalcPRI);
			fpnlPfsCalcPRI.addControl(flblPfsCalcPRI, new TableConstraints(1,
					1, true, true));
			fpnlPfsCalcPRI.add(jspPfsCalcPRI, new TableConstraints(1, 3, true,
					true));

			// 定义功能科目面板
			fpnlAcct = new AcctPanel();

			// 定义录入和计算显示不同信息的面板
			fpnlInOrCompute = new FPanel();

			// 内码文本框加入右边详细信息编辑区面板
			rightPanel.addControl(ftxtPriCode, new TableConstraints(1, 4,
					false, false));
			// 编码文本框内码文本框加入右边详细信息编辑区面板
			rightPanel.addControl(ftxtfPfsCode, new TableConstraints(1, 4,
					false, false));
			// 名称文本框内码文本框加入右边详细信息编辑区面板
			rightPanel.addControl(ftxtfPfsName, new TableConstraints(1, 4,
					false, false));
			// 查询报表名称文本框内码文本框加入右边详细信息编辑区面板
			rightPanel.addControl(ftxtReportPfsName, new TableConstraints(1, 4,
					false, false));

			// 性质FPanel加入右边详细信息编辑区面板
			rightPanel.addControl(fpnlPfsKind, new TableConstraints(3, 4,
					false, false));
			// 参与控制数分配选择框加入右边详细信息编辑区面板
			rightPanel.addControl(fchkPfsFlag, new TableConstraints(1, 2,
					false, false));
			// 默认隐藏
			rightPanel.addControl(fchkHide, new TableConstraints(1, 1, false,
					false));
			// 收支平衡
			rightPanel.addControl(this.fchkIsBalance, new TableConstraints(1,
					1, false, false));
			// 支持项目情况选择框加入右边详细信息编辑区面板
			rightPanel.addControl(fpnlSupPrj, new TableConstraints(2, 4, false,
					false));
			// 数据来源panel加入右边详细信息编辑区面板
			rightPanel.addControl(fpnlPfsDts, new TableConstraints(2, 4, false,
					false));
			// 显示格式列表框加入右边详细信息编辑区面板
			rightPanel.addControl(fcbxSFormate, new TableConstraints(1, 4,
					false, false));
			// 录入和计算显示不同面板
			rightPanel.addControl(fpnlInOrCompute, new TableConstraints(2, 4,
					true, false));

			// 数据来源默认设计为录入
			frdoIncColDts.setValue("0");

			// 设编辑区控件不可编辑
			setAllControlEnabledFalse();
			// 获得层次规则
			lvlIdRule = UntPub.lvlRule;
			this.createToolBar();
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsPayOutFS,
					this, ftreePayOutFS);
			setActionStatus.setState(true, true);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "显示支出资金来源界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 收费项目面板
	 * 
	 */
	public class AcctPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		private CustomTree ftreAcct = null;

		public AcctPanel() throws Exception {

			ftreAcct = new CustomTree("功能科目", null, IPubInterface.BS_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BS_PARENT_ID, null,
					IPubInterface.ACCT_CODE, true);
			ftreAcct.setIsCheckBoxEnabled(false);
			FScrollPane fspnlAcct = new FScrollPane();
			fspnlAcct.addControl(ftreAcct);
			this.setTitle("支出资金来源对应功能科目");
			this.setLayout(new RowPreferedLayout(1));
			this.addControl(fspnlAcct);
		}

		public CustomTree getFtreAcct() {
			return ftreAcct;
		}

		public void setFtreAcct(CustomTree ftreAcct) {
			this.ftreAcct = ftreAcct;
		}
	}

	/**
	 * 加载界面数据
	 * 
	 */
	public void modulePanelActived() {
		try {
			// 判断支出资金来源DataSet是否为空
			if (dsPayOutFS != null) {
				return;
			}

			// 获得支出资金来源信息
			dsPayOutFS = sysIaeStruServ.getPayOutFSTre(Integer
					.parseInt(Global.loginYear));
			// 为支出资金来源DataSet加状态转换事件
			dsPayOutFS.addStateChangeListener(new PayOutFsStateChangeListener(
					this));
			ftreePayOutFS.setDataSet(dsPayOutFS);
			ftreePayOutFS.reset();

			// 为支出资金来源树加选择事件
			ftreePayOutFS
					.addTreeSelectionListener(new PayOutFSTreeSelectionListener(
							this));

			// 获得功能科目信息
			String sFilter = "set_year =" + Global.loginYear;
			dsAcct = PubInterfaceStub.getMethod().getAcctFunc(sFilter);
			fpnlAcct.getFtreAcct().setDataSet(dsAcct);
			fpnlAcct.getFtreAcct().reset();

		} catch (NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "加载支出资金来源数据发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "加载支出资金来源数据发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
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
			fpnlAcct.getFtreAcct().setIsCheckBoxEnabled(false);
		}
		jspPfsCalcPRI.setEnabled(false);
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
			// 支持项目情况选择
			frdoSupPrj.setEditable(true);
		} else {
			// 设置功能科树可编辑
			fpnlAcct.getFtreAcct().setIsCheckBoxEnabled(true);
			// 支持项目情况选择
			frdoSupPrj.setEditable(false);
		}

		ftxtaPfsFormula.setEditable(false);
		ftxtPriCode.setEditable(false);
		jspPfsCalcPRI.setEnabled(false);
	}

	/**
	 * 增加支出资金来源方法
	 */
	public void doAdd() {
		try {
			if (ftreePayOutFS.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this,
						"请选择一个支出资金来源,作为新增支出资金来源的父节点！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			PayOutFSAdd payOutFSAdd = new PayOutFSAdd(this);
			if (!payOutFSAdd.add())
				return;
			ftxtfPfsName.setFocus();
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "增加支出资金来源信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 删除支出资金来源方法
	 */
	public void doDelete() {
		try {
			if (ftreePayOutFS.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "请选择一个支出资金来源！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			PayOutFSDel payOutFSDel = new PayOutFSDel(this);
			if (payOutFSDel.delete()) {
				clearShowInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "删除支出资金来源信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 取消支出资金来源编辑方法
	 */
	public void doCancel() {
		try {
			dsPayOutFS.cancel();
			if ("".equals(payOutFsObj.lvl_id))
				clearShowInfo();
			else
				ftreePayOutFS.expandTo("lvl_id", payOutFsObj.lvl_id);
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "取消支出资金来源信息编辑发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 插入方法
	 */
	public void doInsert() {

	}

	/**
	 * 修改支出资金来源方法
	 */
	public void doModify() {
		try {
			saveParInfo();
			PayOutFSModify payOutFSModify = new PayOutFSModify(this);
			payOutFSModify.modify();
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "修改支出资金来源信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * 保存支出资金来源方法
	 */
	public void doSave() {
		try {
			PayOutFSSave payOutFSSave = new PayOutFSSave(this);
			payOutFSSave.save();
			this.isDataSaved = true;
		} catch (BatchUpdateException e) {
			e.printStackTrace();
			if (e.getErrorCode() == 17081) {
				JOptionPane.showMessageDialog(this,
						"保存支出资金来源发生错误，填写的字段长度超出数据库中字段长度!", "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "保存支出资金来源信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
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
	private void saveParInfo() throws NumberFormatException, Exception {
		payOutFsObj = new PayOutFsObj();
		// 判断选择的是否是根节点，如果选中的是根节点，payOutFsObj设为初始值
		if (ftreePayOutFS.getSelectedNode() == ftreePayOutFS.getRoot()
				|| dsPayOutFS.isEmpty()
				|| ftreePayOutFS.getSelectedNode() == null) {
			// 资金来源编码
			payOutFsObj.PFS_CODE = "";
			// 资金来源名称
			payOutFsObj.PFS_NAME = "";
			// 资金来源全名
			payOutFsObj.pfs_fname = "";
			// 资金来源英文名
			payOutFsObj.pfs_ename = "";
			// 标准支出类型编码
			payOutFsObj.std_type_code = "";
			// 数据来源
			payOutFsObj.data_source = 0;
			// 计算公式内容
			payOutFsObj.formula_det = "";
			// 计算优先级
			payOutFsObj.calc_pri = 0;
			// 是否支持项目
			payOutFsObj.sup_prj = 0;
			// 是否参与控制数分配
			payOutFsObj.cf_pfs_flag = 0;
			// 默认隐藏
			payOutFsObj.in_common_use = 0;
			// 末级标志
			payOutFsObj.end_flag = 0;
			// 显示格式
			payOutFsObj.display_format = "";
			// 编辑格式
			payOutFsObj.edit_format = "";
			// 层次码
			payOutFsObj.lvl_id = "";
			// 父层次码
			payOutFsObj.par_id = "";
			// 年度
			payOutFsObj.set_year = Integer.parseInt(Global.loginYear);

		} else {
			BeanUtil.mapping(payOutFsObj, dsPayOutFS.getOriginData());
		}
	}

	/**
	 * 根据数据来源，控制控件状态(可用或不可用)
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
			fpnlInOrCompute.addControl(fpnlAcct);
			fpnlInOrCompute.updateUI();
			// 支持项目情况按
			if (dsPayOutFS == null) {
				frdoSupPrj.setEditable(false);
			} else if (dsPayOutFS != null
					&& (dsPayOutFS.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				frdoSupPrj.setEditable(false);
			} else {
				frdoSupPrj.setEditable(true);
			}

		} else if ("1".equals(frdoIncColDts.getValue().toString())) {// 计算
			fpnlInOrCompute.setLayout(new RowPreferedLayout(2));
			// 计算公式panel加入右边详细信息编辑区面板
			fpnlInOrCompute.addControl(fpnlEditFormula, new TableConstraints(1,
					2, false, true));
			// 计算公式多行文本框加入右边详细信息编辑区面板
			fpnlInOrCompute.addControl(ftxtaPfsFormula, new TableConstraints(1,
					2, true, true));
			// 计算优先级面板加入右边详细信息编辑区面板
			// fpnlInOrCompute.addControl(fpnlPfsCalcPRI, new
			// TableConstraints(1,
			// 2, false, true));
			fpnlInOrCompute.updateUI();
			frdoSupPrj.setEditable(false);
		}
	}

	/**
	 * 清空控件显示信息
	 * 
	 */
	private void clearShowInfo() {
		// 内码
		ftxtPriCode.setValue("");
		// 编码
		ftxtfPfsCode.setValue("");
		// 名称
		ftxtfPfsName.setValue("");
		// 报表名称
		ftxtReportPfsName.setValue("");
		// 性质
		flstPfsKind.setSelectedIndex(0);
		// 参与控制数分配
		((JCheckBox) fchkPfsFlag.getEditor()).setSelected(false);
		// 默认隐藏
		((JCheckBox) fchkHide.getEditor()).setSelected(false);
		// 收支平衡
		((JCheckBox) this.fchkIsBalance.getEditor()).setSelected(true);
		// 支持项目情况
		frdoSupPrj.setValue("0");
		// 数据来源
		frdoIncColDts.setValue("0");
		// 显示格式
		fcbxSFormate.setValue("");
		// 计算公式按钮
		fbtnPfsEditFormula.setEnabled(false);
		// 计算公式
		ftxtaPfsFormula.setValue("");
		ftxtaPfsFormula.setEditable(false);
		// 计算优先级
		jspPfsCalcPRI.setValue(new Integer(0));
		// 设置收费项目树所有节点为不选中状态
		SetSelectTree.setIsNoCheck(fpnlAcct.getFtreAcct());
	}

	/**
	 * 根据公式内容，得到sup_prj值
	 * 
	 * @param sForumla
	 *            如：{F1}+{F2}
	 * @param dsPfs
	 * @return
	 * @throws Exception
	 */
	public int getsupPrjValue(String sForumla, DataSet dsPfs) throws Exception {
		// 替换公式中文内容
		String sValue = PubInterfaceStub.getMethod().replaceTextExDs(sForumla,
				0, dsPfs, IPayOutFS.PFS_ENAME, IPayOutFS.SUP_PRJ);
		// 取得sup_prj(支持项目情况）字段值
		String sArrayValue[] = Common.splitString(sValue);
		// 数据来源为计算时，支持项目情况根据选择字段支持项目情况设置
		if (sArrayValue != null && sArrayValue.length > 0) {
			int supPrj = Integer.parseInt(sArrayValue[0]);
			int len = sArrayValue.length;
			for (int i = 1; i < len; i++) {
				if (supPrj != Integer.parseInt(sArrayValue[i])) {
					supPrj = 1;
					break;
				}
			}
			return supPrj;

		} else {
			return 1;
		}
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
