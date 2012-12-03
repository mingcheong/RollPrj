/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.BatchUpdateException;
import java.util.Enumeration;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.input.IntegerTextField;
import gov.nbcs.rp.common.ui.list.CustomList;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.action.ChangePayoutKindAction;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:支出项目类别管理客户端主界面
 * </p>
 * <p>
 * Description:支出项目类别管理客户端主界面
 * </p>
 * 
 */

public class PayOutType extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 支出项目类别对象
	PayOutTypeObj payOutTypeObj = null;

	// 层次码编码规则
	SysCodeRule lvlIdRule = null;

	// 保存类型add增加,addfirstson增加第一个儿子,mod修改叶节点未被使用,modformate修改叶节点已被使用,modname修改节点，此节点有叶节
	String sSaveType = null;

	// 支出项目类别树
	CustomTree ftreePayOutType = null;

	// 支出项目类别数据集
	DataSet dsPayOutType = null;

	// 经济科目数据集
	DataSet dsAcctJJ = null;

	// 内码文本框
	FTextField ftxtPriCode = null;

	// 层次码文本框
	IntegerTextField ftxtfPayOutTypeCode = null;

	// 名称文本框
	FTextField ftxtfPayOutTypeName = null;

	// 性质
	CustomList flstPayOutTypeKind = null;

	// 控制数分配到明细
	FCheckBox fchkPayOutFlag = null;

	// 右边编辑区域面板
	FPanel rightPanel = null;

	// 经济科目树
	CustomTree ftreeAcctJJ = null;

	// 数据库接口
	ISysIaeStru sysIaeStruServ = null;

	// 公共方法数据接口
	IPubInterface iPubInterface = null;

	// 是否对话框行式显示
	private boolean isDialogShowType = false;

	private FDialog fdlgShow;

	private CustomToolBar customToolBar;

	// 变更标记
	private boolean changeFlag = false;

	/**
	 * 构造函数
	 * 
	 */
	public PayOutType() {

	}

	public FToolBarPanel getToolbarPanel() {
		if (isDialogShowType) {
			return this.customToolBar;
		} else {
			return super.getToolbarPanel();
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param isDialogShow是否对话框行式显示
	 */
	public PayOutType(boolean isDialogShow, FDialog fdlgShow) {
		this.isDialogShowType = isDialogShow;
		this.fdlgShow = fdlgShow;
		// 初始化界面
		initize();
	}

	/**
	 * 初始化界面
	 */
	public void initize() {
		// 定义数据库接口
		sysIaeStruServ = SysIaeStruI.getMethod();

		// 公共方法数据库接口
		iPubInterface = PubInterfaceStub.getMethod();
		try {
			// 设置分栏
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);

			// 建左边树
			ftreePayOutType = new CustomTree(IPayOutType.PAYOUTKIND_ROOT, null,
					IPayOutType.LVL_ID, ISysIaeStru.NAME, IPayOutType.PAR_ID,
					null);
			FScrollPane fspnlPayOutType = new FScrollPane(ftreePayOutType);
			fSplitPane.addControl(fspnlPayOutType);

			// 建右边详细信息
			rightPanel = new FPanel();
			fSplitPane.addControl(rightPanel);
			// 设置rightPanel边界像素为10
			rightPanel.setLeftInset(10);
			rightPanel.setTopInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);
			RowPreferedLayout rLayout = new RowPreferedLayout(1);
			rLayout.setColumnWidth(350);
			rightPanel.setLayout(rLayout);

			ftxtPriCode = new FTextField("内码：");
			ftxtPriCode.setProportion(0.21f);
			ftxtfPayOutTypeCode = new IntegerTextField("编号：");
			ftxtfPayOutTypeCode.setProportion(0.21f);
			ftxtfPayOutTypeName = new FTextField("名称：");
			ftxtfPayOutTypeName.setProportion(0.21f);

			FLabel flblPayOutKind = new FLabel();
			flblPayOutKind.setTitle("性质：");
			// 性质Tabel
			FPanel fpnlPayOutTypeKind = new FPanel();
			RowPreferedLayout rLayoutKind = new RowPreferedLayout(2);
			rLayoutKind.setColumnWidth(55);
			fpnlPayOutTypeKind.setLayout(rLayoutKind);
			DataSet dsKind = sysIaeStruServ.getKind("IAEPAYOUTKINDSTAND",
					Global.loginYear);
			flstPayOutTypeKind = new CustomList(dsKind, "code", "name");
			FScrollPane fspnlPfsKind = new FScrollPane();
			fspnlPfsKind.addControl(flstPayOutTypeKind);
			fpnlPayOutTypeKind.addControl(flblPayOutKind, new TableConstraints(
					1, 1, false, false));
			fpnlPayOutTypeKind.addControl(fspnlPfsKind, new TableConstraints(3,
					1, false, true));
			// 参与控制数分配
			fchkPayOutFlag = new FCheckBox("控制数分配到明细");
			fchkPayOutFlag.setTitlePosition("RIGHT");

			// 经济科目Panel
			FPanel fpnlAcctJJ = new FPanel();
			fpnlAcctJJ.setTitle("支出项目类别对应经济科目");
			fpnlAcctJJ.setFontSize(this.getFont().getSize());
			fpnlAcctJJ.setFontName(this.getFont().getName());
			fpnlAcctJJ.setTitledBorder();

			ftreeAcctJJ = new CustomTree(IPayOutType.ACCTITEM_JJ_ROOT, null,
					IPubInterface.BSI_ID, IPubInterface.ACCT_JJ_FNAME,
					IPubInterface.BSI_PARENT_ID, null,
					IPubInterface.ACCT_CODE_JJ, true);
			ftreeAcctJJ.setIsCheckBoxEnabled(true);
			// 鼠标事件
			ftreeAcctJJ.addMouseListener(new TreAcctJJMouseListener());
			FScrollPane fsPaneAcctJJ = new FScrollPane();
			fpnlAcctJJ.setLayout(new RowPreferedLayout(1));
			fsPaneAcctJJ.addControl(ftreeAcctJJ);
			fpnlAcctJJ.addControl(fsPaneAcctJJ,
					new TableConstraints(1, 1, true));

			// 内码
			rightPanel.addControl(ftxtPriCode, new TableConstraints(1, 1,
					false, false));
			// 编号
			rightPanel.addControl(ftxtfPayOutTypeCode, new TableConstraints(1,
					1, false, false));
			// 名称
			rightPanel.addControl(ftxtfPayOutTypeName, new TableConstraints(1,
					1, false, false));
			// 性质
			rightPanel.addControl(fpnlPayOutTypeKind, new TableConstraints(3,
					1, false, false));
			// 控制数分配到明细
			rightPanel.addControl(fchkPayOutFlag, new TableConstraints(1, 1,
					false, false));
			// 经济科目Panel
			rightPanel.addControl(fpnlAcctJJ, new TableConstraints(1, 1, true,
					false));

			ftreePayOutType
					.addTreeSelectionListener(new PayOutTypeTreeSelectionListener(
							this));
			// 设编辑区控件不可编辑
			Common.changeChildControlsEditMode(rightPanel, false);
			// 获得层次规则
			lvlIdRule = UntPub.lvlRule;

			FPanel fpnlMain = new FPanel();
			fpnlMain.setLayout(new RowPreferedLayout(1));
			if (!isDialogShowType) {
				this.createToolBar();
			} else {
				customToolBar = new CustomToolBar();
				fpnlMain.addControl(customToolBar, new TableConstraints(2, 1,
						false, true));
			}

			fpnlMain.addControl(fSplitPane, new TableConstraints(1, 1, true,
					true));
			this.add(fpnlMain);

			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsPayOutType,
					this.getToolbarPanel(), ftreePayOutType);
			setActionStatus.setState(true, true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "显示支出项目类别界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 加载界面数据
	 * 
	 */
	public void modulePanelActived() {
		if (dsPayOutType != null)
			return;
		try {
			dsPayOutType = sysIaeStruServ.getPayOutKind(Integer
					.parseInt(Global.loginYear));
			dsPayOutType
					.addStateChangeListener(new PayOutTypeStateChangeListener(
							this));
			ftreePayOutType.setDataSet(dsPayOutType);
			ftreePayOutType.reset();
			String aFilter = "set_year =" + Global.loginYear;
			dsAcctJJ = iPubInterface.getAcctEconomy(aFilter);
			ftreeAcctJJ.setDataSet(dsAcctJJ);
			ftreeAcctJJ.reset();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "加载支出项目类别数据发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "加载支出项目类别数据发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 增加支出项目类别
	 * 
	 */
	public void doAdd() {
		try {
			if (ftreePayOutType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this,
						"请选择一个支出项目类别,作为新增支出项目类别的父节点！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			PayOutTypeAdd payOutTypeAdd = new PayOutTypeAdd(this);
			if (!payOutTypeAdd.add())
				return;
			ftxtfPayOutTypeName.setFocus();
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "增加支出项目类别信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 删除支出项目类别
	 * 
	 */
	public void doDelete() {
		if (ftreePayOutType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "请选择一个支出项目类别！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			if (ftreePayOutType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "请选择一个支出项目类别！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			PayOutTypeDel payOutTypeDel = new PayOutTypeDel(this);
			if (payOutTypeDel.delete())
				clearShowInfo();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "删除支出项目类别信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 取消支出项目类别
	 * 
	 */
	public void doCancel() {
		try {
			dsPayOutType.cancel();
			this.isDataSaved = true;
			if ("".equals(payOutTypeObj.lvl_id))
				clearShowInfo();
			else
				ftreePayOutType.expandTo("lvl_id", payOutTypeObj.lvl_id);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "取消支出项目类别信息编辑发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void doInsert() {
	}

	/**
	 * 修改支出项目类别
	 * 
	 */
	public void doModify() {
		if (ftreePayOutType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "请选择一个支出项目类别！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			saveParInfo();
			PayOutTypeModify payOutTypeModify = new PayOutTypeModify(this);
			payOutTypeModify.modify();
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "修改支出项目类别信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * 保存支出项目类别
	 * 
	 */
	public void doSave() {
		try {
			PayOutTypeSave payOutTypeSave = new PayOutTypeSave(this);
			payOutTypeSave.save();
			this.isDataSaved = true;
		} catch (BatchUpdateException e) {
			e.printStackTrace();
			if (e.getErrorCode() == 17081) {
				JOptionPane.showMessageDialog(this,
						"保存支出项目类别发生错误，填写的字段长度超出数据库中字段长度!", "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "保存支出项目类别信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 关闭界面
	 * 
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
		payOutTypeObj = new PayOutTypeObj();
		// 判断选择的是否是根节点，如果选中的是根节点，payOutTypeObj设为初始值
		if (ftreePayOutType.getSelectedNode() == ftreePayOutType.getRoot()
				|| dsPayOutType.isEmpty()) {
			payOutTypeObj.payout_kind_code = "";// 编码
			payOutTypeObj.payout_kind_name = "";// 名称
			payOutTypeObj.std_type_code = "";// 标准支出类型编码
			payOutTypeObj.end_flag = 0;// 末级标志
			payOutTypeObj.lvl_id = "";// 层次码
			payOutTypeObj.par_id = "";// 父层次码
			payOutTypeObj.set_year = Integer.parseInt(Global.loginYear);// 年度
		} else {
			BeanUtil.mapping(payOutTypeObj, dsPayOutType.getOriginData());
		}
	}

	/**
	 * 清空控件显示信息
	 * 
	 */
	private void clearShowInfo() {
		// 内码
		ftxtPriCode.setValue("");
		ftxtfPayOutTypeCode.setValue("");
		ftxtfPayOutTypeName.setValue("");
		flstPayOutTypeKind.setSelectedIndex(0);
		// 控制数分配到明细
		((JCheckBox) fchkPayOutFlag.getEditor()).setSelected(false);
		// 清空经济科目选择
		SetSelectTree.setIsNoCheck(ftreeAcctJJ);
	}

	private class CustomToolBar extends FToolBarPanel {

		private static final long serialVersionUID = 1L;

		private FButton fbtnClose;

		private FButton fbtnAdd;

		private FButton fbtnModify;

		private FButton fbtnDel;

		private FButton fbtnSave;

		private FButton fbtnCancel;

		private FButton fbtnChangePayoutKind;

		public CustomToolBar() {
			fbtnClose = new FButton("fbtnClose", "关闭");
			fbtnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fdlgShow.dispose();
				}
			});
			fbtnClose.setIcon("images/fbudget/close.gif");

			fbtnAdd = new FButton("fbtnAdd", "增加");
			fbtnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doAdd();
				}
			});
			fbtnAdd.setIcon("images/fbudget/add.gif");

			fbtnModify = new FButton("fbtnModify", "修改");
			fbtnModify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doModify();
				}
			});
			fbtnModify.setIcon("images/fbudget/mod.gif");

			fbtnDel = new FButton("fbtnDel", "删除");
			fbtnDel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doDelete();
				}
			});
			fbtnDel.setIcon("images/fbudget/del.gif");

			fbtnSave = new FButton("fbtnSave", "保存");
			fbtnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doSave();
				}
			});
			fbtnSave.setIcon("images/fbudget/save.gif");

			fbtnCancel = new FButton("fbtnCancel", "取消");
			fbtnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doCancel();
				}
			});
			fbtnCancel.setIcon("images/fbudget/cancl.gif");

			fbtnChangePayoutKind = new FButton("fbtnCancel", "改变支出项目类别");
			fbtnChangePayoutKind.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					ChangePayoutKindAction changePayoutKind = new ChangePayoutKindAction();
					changePayoutKind.setPayOutType(PayOutType.this);
					changePayoutKind.actionPerformed(evt);
				}
			});
			fbtnChangePayoutKind.setIcon("images/fbudget/a17.gif");

			this.addControl(fbtnClose);
			this.addControl(fbtnAdd);
			this.addControl(fbtnModify);
			this.addControl(fbtnDel);
			this.addControl(fbtnSave);
			this.addControl(fbtnCancel);
			this.addControl(fbtnChangePayoutKind);
		}
	}

	/**
	 * 经济科目鼠标事件
	 * 
	 */
	private class TreAcctJJMouseListener extends java.awt.event.MouseAdapter {

		public void mousePressed(java.awt.event.MouseEvent mouseevent) {
			if (mouseevent.getButton() != 1
					|| !(mouseevent.getSource() instanceof CustomTree))
				return;
			CustomTree treeAcctJj = (CustomTree) mouseevent.getSource();
			// 判断checkbox框是否可以编缉
			if (!treeAcctJj.getIsCheckBoxEnabled())
				return;

			int row = treeAcctJj.getRowForLocation(mouseevent.getX(),
					mouseevent.getY());
			if (row < 0) {
				return;
			}

			// 得到选中的节点
			TreePath path = treeAcctJj.getPathForRow(row);
			if (path == null) {
				return;
			}
			MyTreeNode node = (MyTreeNode) path.getLastPathComponent();

			if (node != null) {
				MyPfNode myPfNode = (MyPfNode) node.getUserObject();
				int state = myPfNode.getSelectStat();

				// 判断是否未选中状态（三态）
				if (state == MyPfNode.UNSELECT)
					return;

				try {
					// 支出项目类别编码
					String payOutKindCode = dsPayOutType.fieldByName(
							IPayOutType.PAYOUT_KIND_CODE).getString();
					// 判断此支出项目类别是否已设置公式及公式与单位对应关系，如都没有，不需要判断
					InfoPackage info = sysIaeStruServ
							.judgePayoutTypeFormulaUse(payOutKindCode);
					if (info.getSuccess())
						return;

					DataSet dsAcctJj = treeAcctJj.getDataSet();
					// 保存DataSet当前游标
					String curBookmark = dsAcctJj.toogleBookmark();

					// 判断是否已设置公式和数据
					if (myPfNode.getIsLeaf()) {
						// 判断经济科目与支出项目类别对应关系是否已被业务数据使用或已设置公式
						PayOutType.this.checkIsUse(node, treeAcctJj);
					} else {
						// 遍历下面叶节点判断
						Enumeration allNodes = node.breadthFirstEnumeration();
						MyTreeNode curNode;
						MyPfNode curMyPfNode;
						while (allNodes.hasMoreElements()) {
							curNode = (MyTreeNode) allNodes.nextElement();
							curMyPfNode = (MyPfNode) curNode.getUserObject();
							// 判断是否叶节点
							if (!curMyPfNode.getIsLeaf()) {
								continue;
							}
							// 判断经济科目与支出项目类别对应关系是否已被业务数据使用或已设置公式
							PayOutType.this.checkIsUse(curNode, treeAcctJj);
						}
					}
					// 定位到当前游标
					dsAcctJj.gotoBookmark(curBookmark);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(Global.mainFrame,
							"判断经济科目是否被使用发生错误，错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * 判断经济科目与支出项目类别对应关系是否已被业务数据使用或已设置公式
	 * 
	 * @param node判断的经济科目节点
	 * @param ds经济科目数据集
	 * @throws HeadlessException
	 * @throws Exception
	 */
	private void checkIsUse(MyTreeNode node, CustomTree tree)
			throws HeadlessException, Exception {
		DataSet ds = tree.getDataSet();
		if (!ds.gotoBookmark(node.getBookmark())) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"根据游标定位经济科目数据集发生错误,请与管理员联系！", "提示",
					JOptionPane.ERROR_MESSAGE);
		}
		String acctJjCode = ds.fieldByName(IPubInterface.ACCT_CODE_JJ)
				.getString();
		String payOutKindCode = dsPayOutType.fieldByName(
				IPayOutType.PAYOUT_KIND_CODE).getString();
		String info = sysIaeStruServ.judgePayoutTypeAcctJjUse(payOutKindCode,
				acctJjCode);
		if (Common.isNullStr(info)) {
			MyPfNode myPfNode = (MyPfNode) node.getUserObject();
			myPfNode.setIsSelect(false);
			((DefaultTreeModel) tree.getModel()).nodeChanged(node);
			return;
		}

		String acctJjName = ds.fieldByName(IPubInterface.ACCT_NAME_JJ)
				.getString();

		info = "[" + acctJjCode + "]" + acctJjName + "在" + info;

		if (!Common.isNullStr(info)) {
			// 提示是否取消勾选
			if (JOptionPane.showConfirmDialog(Global.mainFrame, info
					+ ",您是否确认取消选择?", "提示", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				MyPfNode myPfNode = (MyPfNode) node.getUserObject();
				myPfNode.setIsSelect(false);
				((DefaultTreeModel) tree.getModel()).nodeChanged(node);
			}
		}
	}

	public CustomTree getFtreeAcctJJ() {
		return ftreeAcctJJ;
	}

	public CustomTree getFtreePayOutType() {
		return ftreePayOutType;
	}

	public ISysIaeStru getSysIaeStruServ() {
		return sysIaeStruServ;
	}

	public boolean isChangeFlag() {
		return changeFlag;
	}

	public void setChangeFlag(boolean changeFlag) {
		this.changeFlag = changeFlag;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
