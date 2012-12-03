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
 * Title:֧����Ŀ������ͻ���������
 * </p>
 * <p>
 * Description:֧����Ŀ������ͻ���������
 * </p>
 * 
 */

public class PayOutType extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// ֧����Ŀ������
	PayOutTypeObj payOutTypeObj = null;

	// �����������
	SysCodeRule lvlIdRule = null;

	// ��������add����,addfirstson���ӵ�һ������,mod�޸�Ҷ�ڵ�δ��ʹ��,modformate�޸�Ҷ�ڵ��ѱ�ʹ��,modname�޸Ľڵ㣬�˽ڵ���Ҷ��
	String sSaveType = null;

	// ֧����Ŀ�����
	CustomTree ftreePayOutType = null;

	// ֧����Ŀ������ݼ�
	DataSet dsPayOutType = null;

	// ���ÿ�Ŀ���ݼ�
	DataSet dsAcctJJ = null;

	// �����ı���
	FTextField ftxtPriCode = null;

	// ������ı���
	IntegerTextField ftxtfPayOutTypeCode = null;

	// �����ı���
	FTextField ftxtfPayOutTypeName = null;

	// ����
	CustomList flstPayOutTypeKind = null;

	// ���������䵽��ϸ
	FCheckBox fchkPayOutFlag = null;

	// �ұ߱༭�������
	FPanel rightPanel = null;

	// ���ÿ�Ŀ��
	CustomTree ftreeAcctJJ = null;

	// ���ݿ�ӿ�
	ISysIaeStru sysIaeStruServ = null;

	// �����������ݽӿ�
	IPubInterface iPubInterface = null;

	// �Ƿ�Ի�����ʽ��ʾ
	private boolean isDialogShowType = false;

	private FDialog fdlgShow;

	private CustomToolBar customToolBar;

	// ������
	private boolean changeFlag = false;

	/**
	 * ���캯��
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
	 * ���캯��
	 * 
	 * @param isDialogShow�Ƿ�Ի�����ʽ��ʾ
	 */
	public PayOutType(boolean isDialogShow, FDialog fdlgShow) {
		this.isDialogShowType = isDialogShow;
		this.fdlgShow = fdlgShow;
		// ��ʼ������
		initize();
	}

	/**
	 * ��ʼ������
	 */
	public void initize() {
		// �������ݿ�ӿ�
		sysIaeStruServ = SysIaeStruI.getMethod();

		// �����������ݿ�ӿ�
		iPubInterface = PubInterfaceStub.getMethod();
		try {
			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);

			// �������
			ftreePayOutType = new CustomTree(IPayOutType.PAYOUTKIND_ROOT, null,
					IPayOutType.LVL_ID, ISysIaeStru.NAME, IPayOutType.PAR_ID,
					null);
			FScrollPane fspnlPayOutType = new FScrollPane(ftreePayOutType);
			fSplitPane.addControl(fspnlPayOutType);

			// ���ұ���ϸ��Ϣ
			rightPanel = new FPanel();
			fSplitPane.addControl(rightPanel);
			// ����rightPanel�߽�����Ϊ10
			rightPanel.setLeftInset(10);
			rightPanel.setTopInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);
			RowPreferedLayout rLayout = new RowPreferedLayout(1);
			rLayout.setColumnWidth(350);
			rightPanel.setLayout(rLayout);

			ftxtPriCode = new FTextField("���룺");
			ftxtPriCode.setProportion(0.21f);
			ftxtfPayOutTypeCode = new IntegerTextField("��ţ�");
			ftxtfPayOutTypeCode.setProportion(0.21f);
			ftxtfPayOutTypeName = new FTextField("���ƣ�");
			ftxtfPayOutTypeName.setProportion(0.21f);

			FLabel flblPayOutKind = new FLabel();
			flblPayOutKind.setTitle("���ʣ�");
			// ����Tabel
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
			// �������������
			fchkPayOutFlag = new FCheckBox("���������䵽��ϸ");
			fchkPayOutFlag.setTitlePosition("RIGHT");

			// ���ÿ�ĿPanel
			FPanel fpnlAcctJJ = new FPanel();
			fpnlAcctJJ.setTitle("֧����Ŀ����Ӧ���ÿ�Ŀ");
			fpnlAcctJJ.setFontSize(this.getFont().getSize());
			fpnlAcctJJ.setFontName(this.getFont().getName());
			fpnlAcctJJ.setTitledBorder();

			ftreeAcctJJ = new CustomTree(IPayOutType.ACCTITEM_JJ_ROOT, null,
					IPubInterface.BSI_ID, IPubInterface.ACCT_JJ_FNAME,
					IPubInterface.BSI_PARENT_ID, null,
					IPubInterface.ACCT_CODE_JJ, true);
			ftreeAcctJJ.setIsCheckBoxEnabled(true);
			// ����¼�
			ftreeAcctJJ.addMouseListener(new TreAcctJJMouseListener());
			FScrollPane fsPaneAcctJJ = new FScrollPane();
			fpnlAcctJJ.setLayout(new RowPreferedLayout(1));
			fsPaneAcctJJ.addControl(ftreeAcctJJ);
			fpnlAcctJJ.addControl(fsPaneAcctJJ,
					new TableConstraints(1, 1, true));

			// ����
			rightPanel.addControl(ftxtPriCode, new TableConstraints(1, 1,
					false, false));
			// ���
			rightPanel.addControl(ftxtfPayOutTypeCode, new TableConstraints(1,
					1, false, false));
			// ����
			rightPanel.addControl(ftxtfPayOutTypeName, new TableConstraints(1,
					1, false, false));
			// ����
			rightPanel.addControl(fpnlPayOutTypeKind, new TableConstraints(3,
					1, false, false));
			// ���������䵽��ϸ
			rightPanel.addControl(fchkPayOutFlag, new TableConstraints(1, 1,
					false, false));
			// ���ÿ�ĿPanel
			rightPanel.addControl(fpnlAcctJJ, new TableConstraints(1, 1, true,
					false));

			ftreePayOutType
					.addTreeSelectionListener(new PayOutTypeTreeSelectionListener(
							this));
			// ��༭���ؼ����ɱ༭
			Common.changeChildControlsEditMode(rightPanel, false);
			// ��ò�ι���
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

			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsPayOutType,
					this.getToolbarPanel(), ftreePayOutType);
			setActionStatus.setState(true, true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ʾ֧����Ŀ�����淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ���ؽ�������
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
			JOptionPane.showMessageDialog(this, "����֧����Ŀ������ݷ������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "����֧����Ŀ������ݷ������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����֧����Ŀ���
	 * 
	 */
	public void doAdd() {
		try {
			if (ftreePayOutType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this,
						"��ѡ��һ��֧����Ŀ���,��Ϊ����֧����Ŀ���ĸ��ڵ㣡", "��ʾ",
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
			JOptionPane.showMessageDialog(this, "����֧����Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ɾ��֧����Ŀ���
	 * 
	 */
	public void doDelete() {
		if (ftreePayOutType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ��֧����Ŀ���", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			if (ftreePayOutType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ��֧����Ŀ���", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			PayOutTypeDel payOutTypeDel = new PayOutTypeDel(this);
			if (payOutTypeDel.delete())
				clearShowInfo();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "ɾ��֧����Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ȡ��֧����Ŀ���
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
			JOptionPane.showMessageDialog(this, "ȡ��֧����Ŀ�����Ϣ�༭�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void doInsert() {
	}

	/**
	 * �޸�֧����Ŀ���
	 * 
	 */
	public void doModify() {
		if (ftreePayOutType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ��֧����Ŀ���", "��ʾ",
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
			JOptionPane.showMessageDialog(this, "�޸�֧����Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * ����֧����Ŀ���
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
						"����֧����Ŀ�����������д���ֶγ��ȳ������ݿ����ֶγ���!", "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "����֧����Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �رս���
	 * 
	 */
	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();

	}

	/**
	 * add,mod,del����ǰ�����游�ڵ���Ϣ
	 * 
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private void saveParInfo() throws NumberFormatException, Exception {
		payOutTypeObj = new PayOutTypeObj();
		// �ж�ѡ����Ƿ��Ǹ��ڵ㣬���ѡ�е��Ǹ��ڵ㣬payOutTypeObj��Ϊ��ʼֵ
		if (ftreePayOutType.getSelectedNode() == ftreePayOutType.getRoot()
				|| dsPayOutType.isEmpty()) {
			payOutTypeObj.payout_kind_code = "";// ����
			payOutTypeObj.payout_kind_name = "";// ����
			payOutTypeObj.std_type_code = "";// ��׼֧�����ͱ���
			payOutTypeObj.end_flag = 0;// ĩ����־
			payOutTypeObj.lvl_id = "";// �����
			payOutTypeObj.par_id = "";// �������
			payOutTypeObj.set_year = Integer.parseInt(Global.loginYear);// ���
		} else {
			BeanUtil.mapping(payOutTypeObj, dsPayOutType.getOriginData());
		}
	}

	/**
	 * ��տؼ���ʾ��Ϣ
	 * 
	 */
	private void clearShowInfo() {
		// ����
		ftxtPriCode.setValue("");
		ftxtfPayOutTypeCode.setValue("");
		ftxtfPayOutTypeName.setValue("");
		flstPayOutTypeKind.setSelectedIndex(0);
		// ���������䵽��ϸ
		((JCheckBox) fchkPayOutFlag.getEditor()).setSelected(false);
		// ��վ��ÿ�Ŀѡ��
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
			fbtnClose = new FButton("fbtnClose", "�ر�");
			fbtnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fdlgShow.dispose();
				}
			});
			fbtnClose.setIcon("images/fbudget/close.gif");

			fbtnAdd = new FButton("fbtnAdd", "����");
			fbtnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doAdd();
				}
			});
			fbtnAdd.setIcon("images/fbudget/add.gif");

			fbtnModify = new FButton("fbtnModify", "�޸�");
			fbtnModify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doModify();
				}
			});
			fbtnModify.setIcon("images/fbudget/mod.gif");

			fbtnDel = new FButton("fbtnDel", "ɾ��");
			fbtnDel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doDelete();
				}
			});
			fbtnDel.setIcon("images/fbudget/del.gif");

			fbtnSave = new FButton("fbtnSave", "����");
			fbtnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doSave();
				}
			});
			fbtnSave.setIcon("images/fbudget/save.gif");

			fbtnCancel = new FButton("fbtnCancel", "ȡ��");
			fbtnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PayOutType.this.doCancel();
				}
			});
			fbtnCancel.setIcon("images/fbudget/cancl.gif");

			fbtnChangePayoutKind = new FButton("fbtnCancel", "�ı�֧����Ŀ���");
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
	 * ���ÿ�Ŀ����¼�
	 * 
	 */
	private class TreAcctJJMouseListener extends java.awt.event.MouseAdapter {

		public void mousePressed(java.awt.event.MouseEvent mouseevent) {
			if (mouseevent.getButton() != 1
					|| !(mouseevent.getSource() instanceof CustomTree))
				return;
			CustomTree treeAcctJj = (CustomTree) mouseevent.getSource();
			// �ж�checkbox���Ƿ���Ա༩
			if (!treeAcctJj.getIsCheckBoxEnabled())
				return;

			int row = treeAcctJj.getRowForLocation(mouseevent.getX(),
					mouseevent.getY());
			if (row < 0) {
				return;
			}

			// �õ�ѡ�еĽڵ�
			TreePath path = treeAcctJj.getPathForRow(row);
			if (path == null) {
				return;
			}
			MyTreeNode node = (MyTreeNode) path.getLastPathComponent();

			if (node != null) {
				MyPfNode myPfNode = (MyPfNode) node.getUserObject();
				int state = myPfNode.getSelectStat();

				// �ж��Ƿ�δѡ��״̬����̬��
				if (state == MyPfNode.UNSELECT)
					return;

				try {
					// ֧����Ŀ������
					String payOutKindCode = dsPayOutType.fieldByName(
							IPayOutType.PAYOUT_KIND_CODE).getString();
					// �жϴ�֧����Ŀ����Ƿ������ù�ʽ����ʽ�뵥λ��Ӧ��ϵ���綼û�У�����Ҫ�ж�
					InfoPackage info = sysIaeStruServ
							.judgePayoutTypeFormulaUse(payOutKindCode);
					if (info.getSuccess())
						return;

					DataSet dsAcctJj = treeAcctJj.getDataSet();
					// ����DataSet��ǰ�α�
					String curBookmark = dsAcctJj.toogleBookmark();

					// �ж��Ƿ������ù�ʽ������
					if (myPfNode.getIsLeaf()) {
						// �жϾ��ÿ�Ŀ��֧����Ŀ����Ӧ��ϵ�Ƿ��ѱ�ҵ������ʹ�û������ù�ʽ
						PayOutType.this.checkIsUse(node, treeAcctJj);
					} else {
						// ��������Ҷ�ڵ��ж�
						Enumeration allNodes = node.breadthFirstEnumeration();
						MyTreeNode curNode;
						MyPfNode curMyPfNode;
						while (allNodes.hasMoreElements()) {
							curNode = (MyTreeNode) allNodes.nextElement();
							curMyPfNode = (MyPfNode) curNode.getUserObject();
							// �ж��Ƿ�Ҷ�ڵ�
							if (!curMyPfNode.getIsLeaf()) {
								continue;
							}
							// �жϾ��ÿ�Ŀ��֧����Ŀ����Ӧ��ϵ�Ƿ��ѱ�ҵ������ʹ�û������ù�ʽ
							PayOutType.this.checkIsUse(curNode, treeAcctJj);
						}
					}
					// ��λ����ǰ�α�
					dsAcctJj.gotoBookmark(curBookmark);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(Global.mainFrame,
							"�жϾ��ÿ�Ŀ�Ƿ�ʹ�÷������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * �жϾ��ÿ�Ŀ��֧����Ŀ����Ӧ��ϵ�Ƿ��ѱ�ҵ������ʹ�û������ù�ʽ
	 * 
	 * @param node�жϵľ��ÿ�Ŀ�ڵ�
	 * @param ds���ÿ�Ŀ���ݼ�
	 * @throws HeadlessException
	 * @throws Exception
	 */
	private void checkIsUse(MyTreeNode node, CustomTree tree)
			throws HeadlessException, Exception {
		DataSet ds = tree.getDataSet();
		if (!ds.gotoBookmark(node.getBookmark())) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"�����α궨λ���ÿ�Ŀ���ݼ���������,�������Ա��ϵ��", "��ʾ",
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

		info = "[" + acctJjCode + "]" + acctJjName + "��" + info;

		if (!Common.isNullStr(info)) {
			// ��ʾ�Ƿ�ȡ����ѡ
			if (JOptionPane.showConfirmDialog(Global.mainFrame, info
					+ ",���Ƿ�ȷ��ȡ��ѡ��?", "��ʾ", JOptionPane.YES_NO_OPTION,
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
