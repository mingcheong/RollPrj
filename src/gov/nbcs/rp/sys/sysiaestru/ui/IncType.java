/* 
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved. * 
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.sql.BatchUpdateException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.ui.input.IntegerTextField;
import gov.nbcs.rp.common.ui.list.CustomList;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RadioModel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入项目类别管理客户端主界面
 * </p>
 * <p>
 * Description:收入栏目管理客户端主界面
 * </p>

 */

public class IncType extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 收入项目类别DataSet
	DataSet dsIncType = null;

	// 收入项目类别
	CustomTree ftreeIncType = null;

	// 内码文本框
	FTextField ftxtPriCode = null;

	// 编码文本框
	IntegerTextField ftxtfIncTypeCode = null;

	// 名称文本框
	FTextField ftxtfIncTypeName = null;

	// 性质列表框
	CustomList flstIncTypeKind = null;

	// 是否其中数
	FCheckBox fchkIsMid = null;

	// 0:数据录入，1：从非税收入表取数
	FRadioGroup frdoIsInc = null;

	// 保存类型add增加,addfirstson增加第一个儿子,mod修改叶节点未被使用,
	// modformate修改叶节点已被使用,modname修改节点，此节点有叶节
	String sSaveType = null;

	// 右边编辑区面板
	FPanel rightPanel = null;

	// 收入项目类别对象
	IncTypeObj incTypeObj = null;

	// 层次码编码规则
	SysCodeRule lvlIdRule = null;

	// 连接数据库接口
	ISysIaeStru sysIaeStruServ = null;

	// 对应关系选择面板
	ChoiceRelaPanel fpnlChoiceRela;

	// 支出资金来源面板
	PfsPanel fpnlPfs = null;

	// 收入栏目面板
	IncColumnPanel fpnlIncCol;

	// 对应关系切换面板，根据数据来源选择加载收入栏目或支出资金来源
	FPanel fpnlRelaChange;

	// 收入项目类别与收入栏目对应关系
	DataSet dsInccolumnToInc;

	DataSet dsPayOutFS;

	Object valueTmp;

	/**
	 * 构造函数
	 * 
	 */
	public IncType() {

	}

	/**
	 * 初始化界面
	 */
	public void initize() {
		sysIaeStruServ = SysIaeStruI.getMethod();

		try {
			// 设置分栏
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// 定义收入项目类别树
			ftreeIncType = new CustomTree(IIncType.INCTYPE_ROOT, null,
					IIncType.LVL_ID, ISysIaeStru.NAME, IIncType.PAR_ID, null);
			ftreeIncType
					.addTreeSelectionListener(new IncTypeTreeSelectionListener(
							this));
			FScrollPane fspnlIncType = new FScrollPane(ftreeIncType);
			fSplitPane.addControl(fspnlIncType);

			// 定义内码文本框
			ftxtPriCode = new FTextField("内码：");
			ftxtPriCode.setProportion(0.21f);

			// 定义编码文本框
			ftxtfIncTypeCode = new IntegerTextField("编号：");
			ftxtfIncTypeCode.setProportion(0.21f);

			// 定义名称文本框
			ftxtfIncTypeName = new FTextField("名称：");
			ftxtfIncTypeName.setProportion(0.21f);

			// 定义性质Label
			FLabel flblIncTypeKind = new FLabel();
			flblIncTypeKind.setTitle("性质：");
			// 定义性质面板
			FPanel fpnlIncTypeKind = new FPanel();
			RowPreferedLayout rLayoutKind = new RowPreferedLayout(2);
			rLayoutKind.setColumnWidth(55);
			fpnlIncTypeKind.setLayout(rLayoutKind);
			// 获得收入项目类别性质信息
			DataSet dsKind = sysIaeStruServ.getKind("IAEFSSTANDITEM",
					Global.loginYear);
			// 定义性质列表框
			flstIncTypeKind = new CustomList(dsKind, "code", "name");
			FScrollPane fspnlIncTypeKind = new FScrollPane();
			fspnlIncTypeKind.addControl(flstIncTypeKind);
			fpnlIncTypeKind.addControl(flblIncTypeKind, new TableConstraints(1,
					1, false, false));
			fpnlIncTypeKind.addControl(fspnlIncTypeKind, new TableConstraints(
					3, 1, false, true));

			// 定义是否其中数选择框
			fchkIsMid = new FCheckBox("是否其中数");
			fchkIsMid.setTitlePosition("RIGHT");

			// 定义数据来源面板
			FPanel fpnlInc = new FPanel();
			fpnlInc.setLayout(new RowPreferedLayout(1));
			fpnlInc.setTitle("数据来源");
			fpnlInc.setFontSize(this.getFont().getSize());
			fpnlInc.setFontName(this.getFont().getName());
			fpnlInc.setTitledBorder();

			// 定义数据来源
			frdoIsInc = new FRadioGroup("", FRadioGroup.HORIZON) {
				private static final long serialVersionUID = 1L;

				public void setValue(Object value) {
					RadioModel model = (RadioModel) frdoIsInc.getRefModel();
					if (model == null)
						return;
					JRadioButton radios[] = frdoIsInc.getRadios();
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
					try {
						setState();
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(IncType.this,
								"选择数据来源发生错误，错误信息：" + e.getMessage(), "提示",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			};
			frdoIsInc.setTitleVisible(false);
			frdoIsInc.setRefModel("0#录入     +1#从非税收入表取数 +2#从支出预算表取数");

			// frdoIsInc.addValueChangeListener(new ValueChangeListener() {
			//
			// public void valueChanged(ValueChangeEvent valuechangeevent) {
			// try {
			// setState();
			// } catch (Exception e) {
			// e.printStackTrace();
			// JOptionPane.showMessageDialog(IncType.this,
			// "选择数据来源发生错误，错误信息：" + e.getMessage(), "提示",
			// JOptionPane.ERROR_MESSAGE);
			// }
			//
			// }
			// });

			fpnlInc.addControl(frdoIsInc, new TableConstraints(1, 1, true));

			// // 为数据来源单选框加鼠标点击事件
			// JRadioButton radios[] = frdoIsInc.getRadios();
			// for (int i = 0; i < radios.length; i++) {
			// radios[i].addChangeListener(new ChangeListener() {
			// public void stateChanged(ChangeEvent evt) {
			// try {
			// setState();
			// } catch (Exception e) {
			// e.printStackTrace();
			// JOptionPane.showMessageDialog(IncType.this,
			// "选择数据来源发生错误，错误信息：" + e.getMessage(), "提示",
			// JOptionPane.ERROR_MESSAGE);
			// }
			//
			// }
			// });
			// }

			// 收入栏目面板
			fpnlIncCol = new IncColumnPanel();
			// 对应关系切换面板，根据数据来源选择加载收入栏目或支出资金来源
			fpnlRelaChange = new FPanel();
			fpnlRelaChange.setLayout(new RowPreferedLayout(1));
			fpnlRelaChange.addControl(fpnlIncCol, new TableConstraints(1, 1,
					true, true));

			// 定义右边编辑区面板
			rightPanel = new FPanel();
			// 设置右边编辑区布局
			rightPanel.setLayout(new RowPreferedLayout(1));
			rightPanel.addControl(ftxtPriCode, new TableConstraints(1, 1,
					false, true));
			rightPanel.addControl(ftxtfIncTypeCode, new TableConstraints(1, 1,
					false, true));
			rightPanel.addControl(ftxtfIncTypeName, new TableConstraints(1, 1,
					false, true));
			rightPanel.addControl(fpnlIncTypeKind, new TableConstraints(3, 1,
					false, true));
			rightPanel.addControl(fchkIsMid, new TableConstraints(1, 1, false,
					true));
			rightPanel.addControl(fpnlInc, new TableConstraints(2, 1, false,
					true));
			rightPanel.addControl(fpnlRelaChange, new TableConstraints(1, 1,
					true, true));

			// 对应关系选择面板
			fpnlChoiceRela = new ChoiceRelaPanel();
			FPanel fpnlInput = new FPanel();
			RowPreferedLayout rLayout = new RowPreferedLayout(2);
			rLayout.setColumnWidth(350);
			fpnlInput.setLayout(rLayout);
			// 设置右边编辑区面板边界像素为10
			fpnlInput.setLeftInset(10);
			fpnlInput.setTopInset(10);
			fpnlInput.setRightInset(10);
			fpnlInput.setBottomInset(10);
			fpnlInput.addControl(rightPanel, new TableConstraints(1, 1, true,
					false));
			fpnlInput.addControl(fpnlChoiceRela, new TableConstraints(1, 1,
					true, false));

			fSplitPane.addControl(fpnlInput);

			// 设置默认数据来源为录入
			frdoIsInc.setValue("0");

			// 设编辑区控件不可编辑
			setAllControlEnabledFalse();

			// 获得层次规则
			lvlIdRule = UntPub.lvlRule;
			// 创建工具栏
			this.createToolBar();
			// 设置工具栏按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsIncType,
					this, ftreeIncType);
			setActionStatus.setState(true, true);
			fpnlChoiceRela.setVisible(false);
			fpnlPfs = new PfsPanel();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "显示收入项目类别界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 收入预算科目和收费项目对应关系选择面板
	 * 
	 */
	class ChoiceRelaPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		// 收入预算科目
		CustomTree ftreeIncAcctitem = null;

		// 收费项目
		CustomTree ftreIncomeSubItem = null;

		public ChoiceRelaPanel() {

			try {
				// 收入预算科目
//				ftreeIncAcctitem = new CustomTree(IIncType.ACCTITEM_INC_ROOT,
//						null, IPubInterface.IN_BS_ID,
//						IPubInterface.ACCT_JJ_FNAME,
//						IPubInterface.IN_BS_PARENT_ID, null,
//						IPubInterface.ACCT_CODE_INC, true);
				ftreeIncAcctitem
						.addMouseListener(new IncTypeRelaTreMouseListener(
								IncType.this));
				FScrollPane fspnlIncAcctitem = new FScrollPane(ftreeIncAcctitem);
				FPanel fpnlIncAcctitem = new FPanel();
				fpnlIncAcctitem.setTitle("收入项目类别对应收入预算科目");
				fpnlIncAcctitem.setLayout(new RowPreferedLayout(1));
				fpnlIncAcctitem.addControl(fspnlIncAcctitem);

				// 收费项目
//				ftreIncomeSubItem = new CustomTree("收费项目", null,
//						IPubInterface.IncSubItem_ID,
//						IPubInterface.IncSubItem_FNAME,
//						IPubInterface.IncSubItem_PARENT_ID, null,
//						IPubInterface.IncSubItem_Code, true);
				ftreIncomeSubItem
						.addMouseListener(new IncTypeRelaTreMouseListener(
								IncType.this));
				FScrollPane fspnlIncomeSubItem = new FScrollPane(
						ftreIncomeSubItem);
				FPanel fpnlIncomeSubItem = new FPanel();

				fpnlIncomeSubItem.setTitle("收入项目类别对应收费项目");
				fpnlIncomeSubItem.setLayout(new RowPreferedLayout(1));
				fpnlIncomeSubItem.addControl(fspnlIncomeSubItem);

				this.setLayout(new RowPreferedLayout(1));
				this.addControl(fpnlIncAcctitem, new TableConstraints(1, 1,
						true, true));
				this.addControl(fpnlIncomeSubItem, new TableConstraints(1, 1,
						true, true));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "显示收入项目类别对应关系界面发生错误，错误信息："
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * 支出资金来源面板
	 * 
	 */
	class PfsPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		// 支出资金来源
		CustomTree ftreePfs = null;

		public PfsPanel() {
			try {
				ftreePfs = new CustomTree(IPayOutFS.PFS_ROOT, null,
						IPayOutFS.LVL_ID, ISysIaeStru.NAME, IPayOutFS.PAR_ID,
						null, IPayOutFS.LVL_ID, true);
				FScrollPane fspnlPfs = new FScrollPane(ftreePfs);
				this.setTitle("收入项目类别对应支出资金来源");
				this.setLayout(new RowPreferedLayout(1));
				this.addControl(fspnlPfs,
						new TableConstraints(1, 1, true, true));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "显示收入项目类别对应关系界面发生错误，错误信息："
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 收入栏目面板
	 * 
	 */
	class IncColumnPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		// 收入栏目树
		public CustomTree ftreeIncColumn = null;

		public IncColumnPanel() {
			// 收入科目
			try {
				// 定义收入栏目树
				ftreeIncColumn = new CustomTree(IIncColumn.INCCOL_ROOT, null,
						IIncColumn.LVL_ID, ISysIaeStru.NAME, IIncColumn.PAR_ID,
						null, IIncColumn.LVL_ID, true);
				ftreeIncColumn.setIsCheckBoxEnabled(false);
				ftreeIncColumn
						.addTreeSelectionListener(new IncTypeToIncColTreSelListener(
								IncType.this));

				FScrollPane fspnlIncColumn = new FScrollPane();
				fspnlIncColumn.addControl(ftreeIncColumn);
				this.setTitle("收入项目类别对应收入栏目");
				this.setLayout(new RowPreferedLayout(1));
				this.addControl(fspnlIncColumn);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "显示收入项目类别对应关系界面发生错误，错误信息："
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * 加载界面数据
	 * 
	 */
	public void modulePanelActived() {

		try {
			// 从数据库中获取收入栏目数据集
			DataSet dsIncCol = sysIaeStruServ.getIncColTre(Integer
					.parseInt(Global.loginYear));
			// 为收入栏目树设置数据集
			fpnlIncCol.ftreeIncColumn.setDataSet(dsIncCol);
			fpnlIncCol.ftreeIncColumn.reset();

			if (dsIncType != null)
				return;

			// 从数据库中获取收入项目类别数据集
			dsIncType = sysIaeStruServ.getIncTypeTre(Integer
					.parseInt(Global.loginYear));
			// 为收入项目类别数据集增加状态转换事件
			dsIncType.addStateChangeListener(new IncTypeStateChangeListener(
					this));
			ftreeIncType.setDataSet(dsIncType);
			ftreeIncType.reset();

			dsPayOutFS = sysIaeStruServ.getPayOutFSNotCalcTre(Integer
					.parseInt(Global.loginYear));

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "加载收入项目类别数据发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 增加收入项目类别
	 */
	public void doAdd() {
		try {
			if (ftreeIncType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this,
						"请选择一个收入项目类别,作为新增收入项目类别的父节点！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			IncTypeAdd incTypeAdd = new IncTypeAdd(this);
			if (!incTypeAdd.add())
				return;
			this.isDataSaved = false;
			ftxtfIncTypeName.setFocus();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "增加收入项目类别信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 删除收入项目类别
	 */
	public void doDelete() {
		if (ftreeIncType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "请选择一个收入项目类别！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			if (ftreeIncType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "请选择一个收入项目类别！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			IncTypeDel incTypeDel = new IncTypeDel(this);
			if (incTypeDel.delete())
				clearShowInfo();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "删除收入项目类别信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 取消收入项目类别编辑
	 */
	public void doCancel() {
		try {
			dsIncType.cancel();
			if ("".equals(incTypeObj.lvl_id))
				clearShowInfo();
			else
				ftreeIncType.expandTo("lvl_id", incTypeObj.lvl_id);
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "取消收入项目类别编辑发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 插入收入项目类别
	 */
	public void doInsert() {

	}

	/**
	 * 修改收入项目类别
	 */
	public void doModify() {
		if (ftreeIncType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "请选择一个收入项目类别！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			saveParInfo();
			IncTypeModify incTypeModify = new IncTypeModify(this);
			incTypeModify.modify();
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "修改收入项目类别信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 保存收入项目类别
	 */
	public void doSave() {
		try {
			IncTypeSave incTypeSave = new IncTypeSave(this);
			incTypeSave.save();
			this.isDataSaved = true;
		} catch (BatchUpdateException e) {
			e.printStackTrace();
			if (e.getErrorCode() == 17081) {
				JOptionPane.showMessageDialog(this,
						"保存收入项目类别发生错误，填写的字段长度超出数据库中字段长度!", "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "保存收入项目类别信息发生错误，错误信息："
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
		incTypeObj = new IncTypeObj();
		// 判断选择的是否是根节点，如果选中的是根节点，inctypeObj设为初始值
		if (ftreeIncType.getSelectedNode() == ftreeIncType.getRoot()
				|| dsIncType.isEmpty()) {
			incTypeObj.inctype_code = "";// 编码
			incTypeObj.inctype_name = "";// 名称
			incTypeObj.std_type_code = "";// 标准支出类型编码
			incTypeObj.is_inc = 0; // 数据来源，录入或从非税表中算出
			incTypeObj.is_sum = 0; // 是否其中数
			incTypeObj.end_flag = 0;// 末级标志
			incTypeObj.lvl_id = "";// 层次码
			incTypeObj.par_id = "";// 父层次码
			incTypeObj.set_year = Integer.parseInt(Global.loginYear);// 年度
		} else {
			BeanUtil.mapping(incTypeObj, dsIncType.getOriginData());
		}
	}

	/**
	 * 根据RadioGroup是setState，设置收入栏目树是否可以勾选
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	public void setState() throws NumberFormatException, Exception {
		if (frdoIsInc.getValue() == null) {
			return;
		}
		// 根据界面选择的数据来源，设置收入栏目树是否可以勾选
		String sIncValue = frdoIsInc.getValue().toString();
		if ("0".equals(sIncValue)) {// 录入
			// 收入栏目树所有节点设置为未选中状态
			SetSelectTree.setIsNoCheck(fpnlIncCol.ftreeIncColumn);
			// 设置收入栏目树不可以勾选
			fpnlIncCol.ftreeIncColumn.setIsCheckBoxEnabled(false);
			SetSelectTree.setIsNoCheck(fpnlIncCol.ftreeIncColumn);

			// 支出资金来源树所有节点设置为未选中状态
			if (fpnlPfs != null) {
				SetSelectTree.setIsNoCheck(fpnlPfs.ftreePfs);
				fpnlPfs.ftreePfs.setIsCheckBoxEnabled(false);
				SetSelectTree.setIsNoCheck(fpnlPfs.ftreePfs);
			}
			// 收入预算科目和收费项目
			if (fpnlChoiceRela != null) {
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreeIncAcctitem);
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreIncomeSubItem);
				fpnlChoiceRela.ftreeIncAcctitem.setIsCheckBoxEnabled(false);
				fpnlChoiceRela.ftreIncomeSubItem.setIsCheckBoxEnabled(false);
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreeIncAcctitem);
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreIncomeSubItem);
				// 从非税表取数的对应关系
				dsInccolumnToInc = null;
			}

		} else if ("1".equals(sIncValue)) {// 从非税收入表取数
			if ((dsIncType.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				// 设置收入栏目树可以勾选
				fpnlIncCol.ftreeIncColumn.setIsCheckBoxEnabled(false);
				fpnlChoiceRela.ftreeIncAcctitem.setIsCheckBoxEnabled(false);
				fpnlChoiceRela.ftreIncomeSubItem.setIsCheckBoxEnabled(false);
			} else {
				// 设置收入栏目树可以勾选
				fpnlIncCol.ftreeIncColumn.setIsCheckBoxEnabled(true);
				fpnlChoiceRela.ftreeIncAcctitem.setIsCheckBoxEnabled(true);
				fpnlChoiceRela.ftreIncomeSubItem.setIsCheckBoxEnabled(true);
			}

			// 收入预算科目
			if (fpnlChoiceRela.ftreeIncAcctitem.getDataSet() == null) {// 公共方法数据接口
				String sFilter = "set_year =" + Global.loginYear;
//				DataSet dsIncAcctitem = PubInterfaceStub.getMethod()
//						.getAcctInc(sFilter);
//				fpnlChoiceRela.ftreeIncAcctitem.setDataSet(dsIncAcctitem);
				fpnlChoiceRela.ftreeIncAcctitem.reset();
			}
			// 收费项目
			if (fpnlChoiceRela.ftreIncomeSubItem.getDataSet() == null) {
				String sFilter = "set_year =" + Global.loginYear;
//				DataSet dsIncomeSubItem = PubInterfaceStub.getMethod()
//						.getIncSubItem(sFilter);
//				fpnlChoiceRela.ftreIncomeSubItem.setDataSet(dsIncomeSubItem);
				fpnlChoiceRela.ftreIncomeSubItem.reset();
			}

			// 收入预算科目，收费项目选择面板
			fpnlChoiceRela.setVisible(true);

			// 更换成收入栏目选择面板
			fpnlRelaChange.removeAll();
			fpnlRelaChange.setLayout(new RowPreferedLayout(1));
			fpnlRelaChange.addControl(fpnlIncCol, new TableConstraints(1, 1,
					true, true));
			fpnlRelaChange.updateUI();
		} else { // 从支出预算表取数
			fpnlPfs.ftreePfs.setDataSet(dsPayOutFS);
			fpnlPfs.ftreePfs.reset();
			// 设置支出资金来源树可以勾选
			if ((dsIncType.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				fpnlPfs.ftreePfs.setIsCheckBoxEnabled(false);
			} else {
				fpnlPfs.ftreePfs.setIsCheckBoxEnabled(true);
			}

			// 收入预算科目，收费项目选择面板
			fpnlChoiceRela.setVisible(false);

			// 更换成支出资金来源选择面板
			fpnlRelaChange.removeAll();
			fpnlRelaChange.setLayout(new RowPreferedLayout(1));
			fpnlRelaChange.addControl(fpnlPfs, new TableConstraints(1, 1, true,
					true));
			fpnlRelaChange.updateUI();

			// 显示收入项目与支出资金来源的对应关系
			IncTypeTreeSelectionListener.showIncTypeToPfs(this);

		}
	}

	/**
	 * 清空控件显示信息
	 * 
	 */
	private void clearShowInfo() {
		// 内码
		ftxtPriCode.setValue("");
		ftxtfIncTypeCode.setValue("");
		ftxtfIncTypeName.setValue("");
		flstIncTypeKind.setSelectedIndex(0);
		// 是否其中数
		((JCheckBox) fchkIsMid.getEditor()).setSelected(false);
		// 数据来源设置为录入
		frdoIsInc.setValue("0");
		// 设置收入栏目树所有节点为未勾选状态
		SetSelectTree.setIsNoCheck(fpnlIncCol.ftreeIncColumn);
		dsInccolumnToInc = null;
		// 支出资金来源
		if (fpnlPfs != null) {
			SetSelectTree.setIsNoCheck(fpnlPfs.ftreePfs);
		}
		// 收入预算科目，收费项目
		if (fpnlChoiceRela != null) {
			SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreeIncAcctitem);
			SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreIncomeSubItem);
		}
	}

	/**
	 * 
	 * 设编辑区控件不可编辑
	 */
	public void setAllControlEnabledFalse() {
		Common.changeChildControlsEditMode(rightPanel, false);
		if (fpnlChoiceRela != null) {
			Common
					.changeChildControlsEditMode(fpnlChoiceRela,
							false);
		}
		if (fpnlPfs != null) {
			Common.changeChildControlsEditMode(fpnlPfs, false);
		}

	}

	/**
	 * 
	 * 设编辑区控件可编辑
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public void setAllControlEnabledTrue() throws NumberFormatException,
			Exception {
		Common.changeChildControlsEditMode(rightPanel, true);
		if (fpnlChoiceRela != null) {
			Common.changeChildControlsEditMode(fpnlChoiceRela, true);
		}
		if (fpnlPfs != null) {
			Common.changeChildControlsEditMode(fpnlPfs, true);
		}
		// 设置内码文本框不可编辑
		ftxtPriCode.setEditable(false);
		// 根据RadioGroup是setState，设置收入栏目树是否可以勾选
		setState();
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
