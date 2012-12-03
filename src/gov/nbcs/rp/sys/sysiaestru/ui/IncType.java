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
 * Title:������Ŀ������ͻ���������
 * </p>
 * <p>
 * Description:������Ŀ����ͻ���������
 * </p>

 */

public class IncType extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// ������Ŀ���DataSet
	DataSet dsIncType = null;

	// ������Ŀ���
	CustomTree ftreeIncType = null;

	// �����ı���
	FTextField ftxtPriCode = null;

	// �����ı���
	IntegerTextField ftxtfIncTypeCode = null;

	// �����ı���
	FTextField ftxtfIncTypeName = null;

	// �����б��
	CustomList flstIncTypeKind = null;

	// �Ƿ�������
	FCheckBox fchkIsMid = null;

	// 0:����¼�룬1���ӷ�˰�����ȡ��
	FRadioGroup frdoIsInc = null;

	// ��������add����,addfirstson���ӵ�һ������,mod�޸�Ҷ�ڵ�δ��ʹ��,
	// modformate�޸�Ҷ�ڵ��ѱ�ʹ��,modname�޸Ľڵ㣬�˽ڵ���Ҷ��
	String sSaveType = null;

	// �ұ߱༭�����
	FPanel rightPanel = null;

	// ������Ŀ������
	IncTypeObj incTypeObj = null;

	// �����������
	SysCodeRule lvlIdRule = null;

	// �������ݿ�ӿ�
	ISysIaeStru sysIaeStruServ = null;

	// ��Ӧ��ϵѡ�����
	ChoiceRelaPanel fpnlChoiceRela;

	// ֧���ʽ���Դ���
	PfsPanel fpnlPfs = null;

	// ������Ŀ���
	IncColumnPanel fpnlIncCol;

	// ��Ӧ��ϵ�л���壬����������Դѡ�����������Ŀ��֧���ʽ���Դ
	FPanel fpnlRelaChange;

	// ������Ŀ�����������Ŀ��Ӧ��ϵ
	DataSet dsInccolumnToInc;

	DataSet dsPayOutFS;

	Object valueTmp;

	/**
	 * ���캯��
	 * 
	 */
	public IncType() {

	}

	/**
	 * ��ʼ������
	 */
	public void initize() {
		sysIaeStruServ = SysIaeStruI.getMethod();

		try {
			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// ����������Ŀ�����
			ftreeIncType = new CustomTree(IIncType.INCTYPE_ROOT, null,
					IIncType.LVL_ID, ISysIaeStru.NAME, IIncType.PAR_ID, null);
			ftreeIncType
					.addTreeSelectionListener(new IncTypeTreeSelectionListener(
							this));
			FScrollPane fspnlIncType = new FScrollPane(ftreeIncType);
			fSplitPane.addControl(fspnlIncType);

			// ���������ı���
			ftxtPriCode = new FTextField("���룺");
			ftxtPriCode.setProportion(0.21f);

			// ��������ı���
			ftxtfIncTypeCode = new IntegerTextField("��ţ�");
			ftxtfIncTypeCode.setProportion(0.21f);

			// ���������ı���
			ftxtfIncTypeName = new FTextField("���ƣ�");
			ftxtfIncTypeName.setProportion(0.21f);

			// ��������Label
			FLabel flblIncTypeKind = new FLabel();
			flblIncTypeKind.setTitle("���ʣ�");
			// �����������
			FPanel fpnlIncTypeKind = new FPanel();
			RowPreferedLayout rLayoutKind = new RowPreferedLayout(2);
			rLayoutKind.setColumnWidth(55);
			fpnlIncTypeKind.setLayout(rLayoutKind);
			// ���������Ŀ���������Ϣ
			DataSet dsKind = sysIaeStruServ.getKind("IAEFSSTANDITEM",
					Global.loginYear);
			// ���������б��
			flstIncTypeKind = new CustomList(dsKind, "code", "name");
			FScrollPane fspnlIncTypeKind = new FScrollPane();
			fspnlIncTypeKind.addControl(flstIncTypeKind);
			fpnlIncTypeKind.addControl(flblIncTypeKind, new TableConstraints(1,
					1, false, false));
			fpnlIncTypeKind.addControl(fspnlIncTypeKind, new TableConstraints(
					3, 1, false, true));

			// �����Ƿ�������ѡ���
			fchkIsMid = new FCheckBox("�Ƿ�������");
			fchkIsMid.setTitlePosition("RIGHT");

			// ����������Դ���
			FPanel fpnlInc = new FPanel();
			fpnlInc.setLayout(new RowPreferedLayout(1));
			fpnlInc.setTitle("������Դ");
			fpnlInc.setFontSize(this.getFont().getSize());
			fpnlInc.setFontName(this.getFont().getName());
			fpnlInc.setTitledBorder();

			// ����������Դ
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
								"ѡ��������Դ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			};
			frdoIsInc.setTitleVisible(false);
			frdoIsInc.setRefModel("0#¼��     +1#�ӷ�˰�����ȡ�� +2#��֧��Ԥ���ȡ��");

			// frdoIsInc.addValueChangeListener(new ValueChangeListener() {
			//
			// public void valueChanged(ValueChangeEvent valuechangeevent) {
			// try {
			// setState();
			// } catch (Exception e) {
			// e.printStackTrace();
			// JOptionPane.showMessageDialog(IncType.this,
			// "ѡ��������Դ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
			// JOptionPane.ERROR_MESSAGE);
			// }
			//
			// }
			// });

			fpnlInc.addControl(frdoIsInc, new TableConstraints(1, 1, true));

			// // Ϊ������Դ��ѡ���������¼�
			// JRadioButton radios[] = frdoIsInc.getRadios();
			// for (int i = 0; i < radios.length; i++) {
			// radios[i].addChangeListener(new ChangeListener() {
			// public void stateChanged(ChangeEvent evt) {
			// try {
			// setState();
			// } catch (Exception e) {
			// e.printStackTrace();
			// JOptionPane.showMessageDialog(IncType.this,
			// "ѡ��������Դ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
			// JOptionPane.ERROR_MESSAGE);
			// }
			//
			// }
			// });
			// }

			// ������Ŀ���
			fpnlIncCol = new IncColumnPanel();
			// ��Ӧ��ϵ�л���壬����������Դѡ�����������Ŀ��֧���ʽ���Դ
			fpnlRelaChange = new FPanel();
			fpnlRelaChange.setLayout(new RowPreferedLayout(1));
			fpnlRelaChange.addControl(fpnlIncCol, new TableConstraints(1, 1,
					true, true));

			// �����ұ߱༭�����
			rightPanel = new FPanel();
			// �����ұ߱༭������
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

			// ��Ӧ��ϵѡ�����
			fpnlChoiceRela = new ChoiceRelaPanel();
			FPanel fpnlInput = new FPanel();
			RowPreferedLayout rLayout = new RowPreferedLayout(2);
			rLayout.setColumnWidth(350);
			fpnlInput.setLayout(rLayout);
			// �����ұ߱༭�����߽�����Ϊ10
			fpnlInput.setLeftInset(10);
			fpnlInput.setTopInset(10);
			fpnlInput.setRightInset(10);
			fpnlInput.setBottomInset(10);
			fpnlInput.addControl(rightPanel, new TableConstraints(1, 1, true,
					false));
			fpnlInput.addControl(fpnlChoiceRela, new TableConstraints(1, 1,
					true, false));

			fSplitPane.addControl(fpnlInput);

			// ����Ĭ��������ԴΪ¼��
			frdoIsInc.setValue("0");

			// ��༭���ؼ����ɱ༭
			setAllControlEnabledFalse();

			// ��ò�ι���
			lvlIdRule = UntPub.lvlRule;
			// ����������
			this.createToolBar();
			// ���ù�������ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsIncType,
					this, ftreeIncType);
			setActionStatus.setState(true, true);
			fpnlChoiceRela.setVisible(false);
			fpnlPfs = new PfsPanel();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ʾ������Ŀ�����淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����Ԥ���Ŀ���շ���Ŀ��Ӧ��ϵѡ�����
	 * 
	 */
	class ChoiceRelaPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		// ����Ԥ���Ŀ
		CustomTree ftreeIncAcctitem = null;

		// �շ���Ŀ
		CustomTree ftreIncomeSubItem = null;

		public ChoiceRelaPanel() {

			try {
				// ����Ԥ���Ŀ
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
				fpnlIncAcctitem.setTitle("������Ŀ����Ӧ����Ԥ���Ŀ");
				fpnlIncAcctitem.setLayout(new RowPreferedLayout(1));
				fpnlIncAcctitem.addControl(fspnlIncAcctitem);

				// �շ���Ŀ
//				ftreIncomeSubItem = new CustomTree("�շ���Ŀ", null,
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

				fpnlIncomeSubItem.setTitle("������Ŀ����Ӧ�շ���Ŀ");
				fpnlIncomeSubItem.setLayout(new RowPreferedLayout(1));
				fpnlIncomeSubItem.addControl(fspnlIncomeSubItem);

				this.setLayout(new RowPreferedLayout(1));
				this.addControl(fpnlIncAcctitem, new TableConstraints(1, 1,
						true, true));
				this.addControl(fpnlIncomeSubItem, new TableConstraints(1, 1,
						true, true));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "��ʾ������Ŀ����Ӧ��ϵ���淢�����󣬴�����Ϣ��"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * ֧���ʽ���Դ���
	 * 
	 */
	class PfsPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		// ֧���ʽ���Դ
		CustomTree ftreePfs = null;

		public PfsPanel() {
			try {
				ftreePfs = new CustomTree(IPayOutFS.PFS_ROOT, null,
						IPayOutFS.LVL_ID, ISysIaeStru.NAME, IPayOutFS.PAR_ID,
						null, IPayOutFS.LVL_ID, true);
				FScrollPane fspnlPfs = new FScrollPane(ftreePfs);
				this.setTitle("������Ŀ����Ӧ֧���ʽ���Դ");
				this.setLayout(new RowPreferedLayout(1));
				this.addControl(fspnlPfs,
						new TableConstraints(1, 1, true, true));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "��ʾ������Ŀ����Ӧ��ϵ���淢�����󣬴�����Ϣ��"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * ������Ŀ���
	 * 
	 */
	class IncColumnPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		// ������Ŀ��
		public CustomTree ftreeIncColumn = null;

		public IncColumnPanel() {
			// �����Ŀ
			try {
				// ����������Ŀ��
				ftreeIncColumn = new CustomTree(IIncColumn.INCCOL_ROOT, null,
						IIncColumn.LVL_ID, ISysIaeStru.NAME, IIncColumn.PAR_ID,
						null, IIncColumn.LVL_ID, true);
				ftreeIncColumn.setIsCheckBoxEnabled(false);
				ftreeIncColumn
						.addTreeSelectionListener(new IncTypeToIncColTreSelListener(
								IncType.this));

				FScrollPane fspnlIncColumn = new FScrollPane();
				fspnlIncColumn.addControl(ftreeIncColumn);
				this.setTitle("������Ŀ����Ӧ������Ŀ");
				this.setLayout(new RowPreferedLayout(1));
				this.addControl(fspnlIncColumn);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "��ʾ������Ŀ����Ӧ��ϵ���淢�����󣬴�����Ϣ��"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * ���ؽ�������
	 * 
	 */
	public void modulePanelActived() {

		try {
			// �����ݿ��л�ȡ������Ŀ���ݼ�
			DataSet dsIncCol = sysIaeStruServ.getIncColTre(Integer
					.parseInt(Global.loginYear));
			// Ϊ������Ŀ���������ݼ�
			fpnlIncCol.ftreeIncColumn.setDataSet(dsIncCol);
			fpnlIncCol.ftreeIncColumn.reset();

			if (dsIncType != null)
				return;

			// �����ݿ��л�ȡ������Ŀ������ݼ�
			dsIncType = sysIaeStruServ.getIncTypeTre(Integer
					.parseInt(Global.loginYear));
			// Ϊ������Ŀ������ݼ�����״̬ת���¼�
			dsIncType.addStateChangeListener(new IncTypeStateChangeListener(
					this));
			ftreeIncType.setDataSet(dsIncType);
			ftreeIncType.reset();

			dsPayOutFS = sysIaeStruServ.getPayOutFSNotCalcTre(Integer
					.parseInt(Global.loginYear));

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "����������Ŀ������ݷ������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����������Ŀ���
	 */
	public void doAdd() {
		try {
			if (ftreeIncType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this,
						"��ѡ��һ��������Ŀ���,��Ϊ����������Ŀ���ĸ��ڵ㣡", "��ʾ",
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
			JOptionPane.showMessageDialog(this, "����������Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ɾ��������Ŀ���
	 */
	public void doDelete() {
		if (ftreeIncType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ��������Ŀ���", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			if (ftreeIncType.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ��������Ŀ���", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			saveParInfo();
			IncTypeDel incTypeDel = new IncTypeDel(this);
			if (incTypeDel.delete())
				clearShowInfo();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "ɾ��������Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ȡ��������Ŀ���༭
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
			JOptionPane.showMessageDialog(this, "ȡ��������Ŀ���༭�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����������Ŀ���
	 */
	public void doInsert() {

	}

	/**
	 * �޸�������Ŀ���
	 */
	public void doModify() {
		if (ftreeIncType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ��������Ŀ���", "��ʾ",
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
			JOptionPane.showMessageDialog(this, "�޸�������Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����������Ŀ���
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
						"����������Ŀ�����������д���ֶγ��ȳ������ݿ����ֶγ���!", "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "����������Ŀ�����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �رս���
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
		incTypeObj = new IncTypeObj();
		// �ж�ѡ����Ƿ��Ǹ��ڵ㣬���ѡ�е��Ǹ��ڵ㣬inctypeObj��Ϊ��ʼֵ
		if (ftreeIncType.getSelectedNode() == ftreeIncType.getRoot()
				|| dsIncType.isEmpty()) {
			incTypeObj.inctype_code = "";// ����
			incTypeObj.inctype_name = "";// ����
			incTypeObj.std_type_code = "";// ��׼֧�����ͱ���
			incTypeObj.is_inc = 0; // ������Դ��¼���ӷ�˰�������
			incTypeObj.is_sum = 0; // �Ƿ�������
			incTypeObj.end_flag = 0;// ĩ����־
			incTypeObj.lvl_id = "";// �����
			incTypeObj.par_id = "";// �������
			incTypeObj.set_year = Integer.parseInt(Global.loginYear);// ���
		} else {
			BeanUtil.mapping(incTypeObj, dsIncType.getOriginData());
		}
	}

	/**
	 * ����RadioGroup��setState������������Ŀ���Ƿ���Թ�ѡ
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	public void setState() throws NumberFormatException, Exception {
		if (frdoIsInc.getValue() == null) {
			return;
		}
		// ���ݽ���ѡ���������Դ������������Ŀ���Ƿ���Թ�ѡ
		String sIncValue = frdoIsInc.getValue().toString();
		if ("0".equals(sIncValue)) {// ¼��
			// ������Ŀ�����нڵ�����Ϊδѡ��״̬
			SetSelectTree.setIsNoCheck(fpnlIncCol.ftreeIncColumn);
			// ����������Ŀ�������Թ�ѡ
			fpnlIncCol.ftreeIncColumn.setIsCheckBoxEnabled(false);
			SetSelectTree.setIsNoCheck(fpnlIncCol.ftreeIncColumn);

			// ֧���ʽ���Դ�����нڵ�����Ϊδѡ��״̬
			if (fpnlPfs != null) {
				SetSelectTree.setIsNoCheck(fpnlPfs.ftreePfs);
				fpnlPfs.ftreePfs.setIsCheckBoxEnabled(false);
				SetSelectTree.setIsNoCheck(fpnlPfs.ftreePfs);
			}
			// ����Ԥ���Ŀ���շ���Ŀ
			if (fpnlChoiceRela != null) {
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreeIncAcctitem);
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreIncomeSubItem);
				fpnlChoiceRela.ftreeIncAcctitem.setIsCheckBoxEnabled(false);
				fpnlChoiceRela.ftreIncomeSubItem.setIsCheckBoxEnabled(false);
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreeIncAcctitem);
				SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreIncomeSubItem);
				// �ӷ�˰��ȡ���Ķ�Ӧ��ϵ
				dsInccolumnToInc = null;
			}

		} else if ("1".equals(sIncValue)) {// �ӷ�˰�����ȡ��
			if ((dsIncType.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				// ����������Ŀ�����Թ�ѡ
				fpnlIncCol.ftreeIncColumn.setIsCheckBoxEnabled(false);
				fpnlChoiceRela.ftreeIncAcctitem.setIsCheckBoxEnabled(false);
				fpnlChoiceRela.ftreIncomeSubItem.setIsCheckBoxEnabled(false);
			} else {
				// ����������Ŀ�����Թ�ѡ
				fpnlIncCol.ftreeIncColumn.setIsCheckBoxEnabled(true);
				fpnlChoiceRela.ftreeIncAcctitem.setIsCheckBoxEnabled(true);
				fpnlChoiceRela.ftreIncomeSubItem.setIsCheckBoxEnabled(true);
			}

			// ����Ԥ���Ŀ
			if (fpnlChoiceRela.ftreeIncAcctitem.getDataSet() == null) {// �����������ݽӿ�
				String sFilter = "set_year =" + Global.loginYear;
//				DataSet dsIncAcctitem = PubInterfaceStub.getMethod()
//						.getAcctInc(sFilter);
//				fpnlChoiceRela.ftreeIncAcctitem.setDataSet(dsIncAcctitem);
				fpnlChoiceRela.ftreeIncAcctitem.reset();
			}
			// �շ���Ŀ
			if (fpnlChoiceRela.ftreIncomeSubItem.getDataSet() == null) {
				String sFilter = "set_year =" + Global.loginYear;
//				DataSet dsIncomeSubItem = PubInterfaceStub.getMethod()
//						.getIncSubItem(sFilter);
//				fpnlChoiceRela.ftreIncomeSubItem.setDataSet(dsIncomeSubItem);
				fpnlChoiceRela.ftreIncomeSubItem.reset();
			}

			// ����Ԥ���Ŀ���շ���Ŀѡ�����
			fpnlChoiceRela.setVisible(true);

			// ������������Ŀѡ�����
			fpnlRelaChange.removeAll();
			fpnlRelaChange.setLayout(new RowPreferedLayout(1));
			fpnlRelaChange.addControl(fpnlIncCol, new TableConstraints(1, 1,
					true, true));
			fpnlRelaChange.updateUI();
		} else { // ��֧��Ԥ���ȡ��
			fpnlPfs.ftreePfs.setDataSet(dsPayOutFS);
			fpnlPfs.ftreePfs.reset();
			// ����֧���ʽ���Դ�����Թ�ѡ
			if ((dsIncType.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				fpnlPfs.ftreePfs.setIsCheckBoxEnabled(false);
			} else {
				fpnlPfs.ftreePfs.setIsCheckBoxEnabled(true);
			}

			// ����Ԥ���Ŀ���շ���Ŀѡ�����
			fpnlChoiceRela.setVisible(false);

			// ������֧���ʽ���Դѡ�����
			fpnlRelaChange.removeAll();
			fpnlRelaChange.setLayout(new RowPreferedLayout(1));
			fpnlRelaChange.addControl(fpnlPfs, new TableConstraints(1, 1, true,
					true));
			fpnlRelaChange.updateUI();

			// ��ʾ������Ŀ��֧���ʽ���Դ�Ķ�Ӧ��ϵ
			IncTypeTreeSelectionListener.showIncTypeToPfs(this);

		}
	}

	/**
	 * ��տؼ���ʾ��Ϣ
	 * 
	 */
	private void clearShowInfo() {
		// ����
		ftxtPriCode.setValue("");
		ftxtfIncTypeCode.setValue("");
		ftxtfIncTypeName.setValue("");
		flstIncTypeKind.setSelectedIndex(0);
		// �Ƿ�������
		((JCheckBox) fchkIsMid.getEditor()).setSelected(false);
		// ������Դ����Ϊ¼��
		frdoIsInc.setValue("0");
		// ����������Ŀ�����нڵ�Ϊδ��ѡ״̬
		SetSelectTree.setIsNoCheck(fpnlIncCol.ftreeIncColumn);
		dsInccolumnToInc = null;
		// ֧���ʽ���Դ
		if (fpnlPfs != null) {
			SetSelectTree.setIsNoCheck(fpnlPfs.ftreePfs);
		}
		// ����Ԥ���Ŀ���շ���Ŀ
		if (fpnlChoiceRela != null) {
			SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreeIncAcctitem);
			SetSelectTree.setIsNoCheck(fpnlChoiceRela.ftreIncomeSubItem);
		}
	}

	/**
	 * 
	 * ��༭���ؼ����ɱ༭
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
	 * ��༭���ؼ��ɱ༭
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
		// ���������ı��򲻿ɱ༭
		ftxtPriCode.setEditable(false);
		// ����RadioGroup��setState������������Ŀ���Ƿ���Թ�ѡ
		setState();
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
