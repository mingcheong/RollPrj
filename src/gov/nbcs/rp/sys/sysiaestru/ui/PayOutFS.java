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
 * Title:֧���ʽ���Դ����ͻ���������
 * </p>
 * <p>
 * Description:֧���ʽ���Դ����ͻ���������
 * </p>
 * <p>
 *
 */
public class PayOutFS extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// ֧���ʽ���ԴDataSet
	DataSet dsPayOutFS = null;

	// ֧���ʽ���Դ����
	DataSet dsKind = null;

	// �ұ߱༭��
	FPanel rightPanel = null;

	// ֧���ʽ���Դ�����ı���
	FTextField ftxtPriCode = null;

	// ֧���ʽ���Դ�����ı���
	IntegerTextField ftxtfPfsCode = null;

	// ֧���ʽ���Դ�����ı���
	FTextField ftxtfPfsName = null;

	// ֧���ʽ���Դ���������б��
	CustomList flstPfsKind = null;

	// �������������ѡ���
	FCheckBox fchkPfsFlag = null;

	// Ĭ������
	FCheckBox fchkHide = null;

	// �Ƿ���֧ƽ��
	FCheckBox fchkIsBalance = null;

	// ֧����Ŀ���ѡ���
	FRadioGroup frdoSupPrj = null;

	// ������Դ��ѡ��
	FRadioGroup frdoIncColDts = null;

	// ��ʾ��ʽ��Ͽ�
	CustomComboBox fcbxSFormate = null;

	// ���㹫ʽ��ť
	FButton fbtnPfsEditFormula = null;

	// ���㹫ʽ�����ı���
	FTextArea ftxtaPfsFormula = null;

	// �������ȼ�΢����
	IntegerSpinner jspPfsCalcPRI = null;

	// ֧���ʽ���Դ��
	CustomTree ftreePayOutFS = null;

	// �����������
	SysCodeRule lvlIdRule = null;

	// ��������add����,addfirstson���ӵ�һ������,mod�޸�Ҷ�ڵ�δ��ʹ��,modformate�޸�Ҷ�ڵ��ѱ�ʹ��,modname�޸Ľڵ㣬�˽ڵ���Ҷ��
	String sSaveType = null;

	// ֧���ʽ���Դ���ݶ���
	PayOutFsObj payOutFsObj = null;

	// ���ݿ�ӿ�
	ISysIaeStru sysIaeStruServ = null;

	// ���幦�����
	AcctPanel fpnlAcct = null;

	// ¼���������
	private FPanel fpnlInOrCompute = null;

	// ������㹫ʽ��ť���
	private FPanel fpnlEditFormula = null;

	// ����������ȼ����
	FPanel fpnlPfsCalcPRI = null;

	// ���ܿ�Ŀ
	private DataSet dsAcct = null;

	// ֧���ʽ���Դ��ѯ���������ı���
	FTextField ftxtReportPfsName = null;

	/**
	 * ���캯��
	 * 
	 */
	public PayOutFS() {
	}

	/**
	 * ��ʼ������
	 */
	public void initize() {
		try {
			// �������ݿ�ӿ�
			sysIaeStruServ = SysIaeStruI.getMethod();
			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// ����֧���ʽ���Դ��
			ftreePayOutFS = new CustomTree(IPayOutFS.PFS_ROOT, null,
					IPayOutFS.LVL_ID, ISysIaeStru.NAME, IPayOutFS.PAR_ID, null);
			FScrollPane fspnlPayOutFs = new FScrollPane(ftreePayOutFS);
			fSplitPane.addControl(fspnlPayOutFs);

			// �����ұ���ϸ��Ϣ�༭�����
			rightPanel = new FPanel();
			fSplitPane.addControl(rightPanel);
			// ����rightPanel�߽�����Ϊ10
			rightPanel.setTopInset(10);
			rightPanel.setLeftInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);
			RowPreferedLayout rLayout = new RowPreferedLayout(4);
			rLayout.setColumnWidth(80);
			rightPanel.setLayout(rLayout);

			// ���������ı���
			ftxtPriCode = new FTextField("���룺");
			ftxtPriCode.setProportion(0.21f);

			// ���������ı���
			ftxtfPfsCode = new IntegerTextField("���룺");
			ftxtfPfsCode.setProportion(0.21f);

			// ���������ı���
			ftxtfPfsName = new FTextField("���ƣ�");
			ftxtfPfsName.setProportion(0.21f);

			ftxtReportPfsName = new FTextField("�������ƣ�");
			ftxtReportPfsName.setProportion(0.21f);

			// ��������
			FLabel flblPfsKind = new FLabel();
			flblPfsKind.setTitle("���ʣ�");
			// �����������
			FPanel fpnlPfsKind = new FPanel();
			RowPreferedLayout rLayoutKind = new RowPreferedLayout(2);
			rLayoutKind.setColumnWidth(55);
			fpnlPfsKind.setLayout(rLayoutKind);
			// ���������Ϣ
			dsKind = sysIaeStruServ.getKind("IAEFSSTANDITEM", Global.loginYear);
			// ���������б�
			flstPfsKind = new CustomList(dsKind, "code", "name");
			FScrollPane fspnlPfsKind = new FScrollPane();
			fspnlPfsKind.addControl(flstPfsKind);
			fpnlPfsKind.addControl(flblPfsKind, new TableConstraints(1, 1,
					false, false));
			fpnlPfsKind.addControl(fspnlPfsKind, new TableConstraints(3, 1,
					false, true));

			// �������������
			fchkPfsFlag = new FCheckBox("�������������");
			fchkPfsFlag.setTitlePosition("RIGHT");

			// �������������
			fchkHide = new FCheckBox("Ĭ������");
			fchkHide.setTitlePosition("RIGHT");

			fchkIsBalance = new FCheckBox("��֧ƽ��");
			fchkIsBalance.setTitlePosition("RIGHT");

			// ֧����Ŀ���
			FPanel fpnlSupPrj = new FPanel();
			fpnlSupPrj.setLayout(new RowPreferedLayout(1));
			fpnlSupPrj.setTitle("֧����Ŀ���");
			fpnlSupPrj.setFontSize(this.getFont().getSize());
			fpnlSupPrj.setFontName(this.getFont().getName());
			fpnlSupPrj.setTitledBorder();
			frdoSupPrj = new FRadioGroup("", FRadioGroup.HORIZON);
			frdoSupPrj.setRefModel("0#��֧��+1#֧��+2#��֧��");
			frdoSupPrj.setValue("0");
			fpnlSupPrj.addControl(frdoSupPrj, new TableConstraints(1, 1, true));

			// ������Դpanel
			FPanel fpnlPfsDts = new FPanel();
			fpnlPfsDts.setLayout(new RowPreferedLayout(1));
			fpnlPfsDts.setTitle("������Դ");
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
					// ����RadioGroup��¼�뻹�Ǽ���״̬����ʾ�շ���Ŀ���������ù�ʽ����
					setState();
				}

			};
			frdoIncColDts.setRefModel("0#¼��+1#����");
			fpnlPfsDts.addControl(frdoIncColDts, new TableConstraints(1, 1,
					true));

			// frdoIncColDts.addValueChangeListener(new ValueChangeListener() {
			//
			// public void valueChanged(ValueChangeEvent valuechangeevent) {
			// // ����RadioGroup��¼�뻹�Ǽ���״̬����ʾ�շ���Ŀ���������ù�ʽ����
			// setState();
			//
			// }
			// });

			// // Ϊ������Դ��ѡ���ֵ�ı��¼�
			// JRadioButton radios[] = frdoIncColDts.getRadios();
			// for (int i = 0; i < radios.length; i++) {
			// radios[i].addChangeListener(new ChangeListener() {
			//
			// public void stateChanged(ChangeEvent arg0) {
			// // ����RadioGroup��¼�뻹�Ǽ���״̬����ʾ�շ���Ŀ���������ù�ʽ����
			// setState();
			//
			// }
			// });
			// }

			// �����ʾ��ʽ��Ϣ
			DataSet dsSFormate = sysIaeStruServ.getSFormate(Integer
					.parseInt(Global.loginYear));
			// ������ʾ��ʽ��Ͽ�
			fcbxSFormate = new CustomComboBox(dsSFormate, "name", "name");
			fcbxSFormate.setTitle("��ʾ��ʽ��");
			fcbxSFormate.setProportion(0.21f);

			// ������㹫ʽ��ť���
			fpnlEditFormula = new FPanel();
			RowPreferedLayout editFormulaLayout = new RowPreferedLayout(5);
			editFormulaLayout.setColumnWidth(30);
			fpnlEditFormula.setLayout(editFormulaLayout);
			// ������㹫ʽLabel
			FLabel flblPfsEditFormula = new FLabel();
			flblPfsEditFormula.setTitle("���㹫ʽ��");
			// ������㹫ʽ�����ť
			fbtnPfsEditFormula = new FButton("jsButton", "...");
			fpnlEditFormula.addControl(flblPfsEditFormula,
					new TableConstraints(1, 1, true, true));
			fpnlEditFormula.addControl(fbtnPfsEditFormula,
					new TableConstraints(1, 1, true, false));

			// ���㹫ʽ��ť�¼�
			fbtnPfsEditFormula.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					FormulaPanelCheck formulaPanel = new FormulaPanelCheck(
//							false, null);
					try {
						DataSet dsIncColCalc = sysIaeStruServ.getPayOutFSCalc(
								Integer.parseInt(Global.loginYear), dsPayOutFS
										.fieldByName("pfs_code").getString());
						CustomTree ftreePfsCalc = new CustomTree("��ѡ��Ŀ",
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
//						// �滻��ʽ��������
//						String sForumlaEname = PubInterfaceStub.getMethod()
//								.replaceTextExDs(sForumla, 0, dsPayOutFS,
//										IPayOutFS.PFS_FNAME,
//										IPayOutFS.PFS_ENAME);
//						// ���ݹ�ʽ���ݣ��õ�sup_prjֵ
//						int supPrj = getsupPrjValue(sForumlaEname, dsPayOutFS);
//						frdoSupPrj.setValue(String.valueOf(supPrj));
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(PayOutFS.this,
								"��ʾ��ʽ�༭���淢�����󣬴�����Ϣ��" + ex.getMessage(), "��ʾ",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			// ���㹫ʽ
			ftxtaPfsFormula = new FTextArea(false);

			// ����������ȼ����
			fpnlPfsCalcPRI = new FPanel();
			fpnlPfsCalcPRI.setLayout(new RowPreferedLayout(4));
			// ����������ȼ�Label
			FLabel flblPfsCalcPRI = new FLabel();
			flblPfsCalcPRI.setTitle("�������ȼ���");
			// ����������ȼ�΢����
			SpinnerModel modelCalcPRI = new SpinnerNumberModel(0, 0, 100, 1);
			jspPfsCalcPRI = new IntegerSpinner(modelCalcPRI);
			fpnlPfsCalcPRI.addControl(flblPfsCalcPRI, new TableConstraints(1,
					1, true, true));
			fpnlPfsCalcPRI.add(jspPfsCalcPRI, new TableConstraints(1, 3, true,
					true));

			// ���幦�ܿ�Ŀ���
			fpnlAcct = new AcctPanel();

			// ����¼��ͼ�����ʾ��ͬ��Ϣ�����
			fpnlInOrCompute = new FPanel();

			// �����ı�������ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(ftxtPriCode, new TableConstraints(1, 4,
					false, false));
			// �����ı��������ı�������ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(ftxtfPfsCode, new TableConstraints(1, 4,
					false, false));
			// �����ı��������ı�������ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(ftxtfPfsName, new TableConstraints(1, 4,
					false, false));
			// ��ѯ���������ı��������ı�������ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(ftxtReportPfsName, new TableConstraints(1, 4,
					false, false));

			// ����FPanel�����ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(fpnlPfsKind, new TableConstraints(3, 4,
					false, false));
			// �������������ѡ�������ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(fchkPfsFlag, new TableConstraints(1, 2,
					false, false));
			// Ĭ������
			rightPanel.addControl(fchkHide, new TableConstraints(1, 1, false,
					false));
			// ��֧ƽ��
			rightPanel.addControl(this.fchkIsBalance, new TableConstraints(1,
					1, false, false));
			// ֧����Ŀ���ѡ�������ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(fpnlSupPrj, new TableConstraints(2, 4, false,
					false));
			// ������Դpanel�����ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(fpnlPfsDts, new TableConstraints(2, 4, false,
					false));
			// ��ʾ��ʽ�б������ұ���ϸ��Ϣ�༭�����
			rightPanel.addControl(fcbxSFormate, new TableConstraints(1, 4,
					false, false));
			// ¼��ͼ�����ʾ��ͬ���
			rightPanel.addControl(fpnlInOrCompute, new TableConstraints(2, 4,
					true, false));

			// ������ԴĬ�����Ϊ¼��
			frdoIncColDts.setValue("0");

			// ��༭���ؼ����ɱ༭
			setAllControlEnabledFalse();
			// ��ò�ι���
			lvlIdRule = UntPub.lvlRule;
			this.createToolBar();
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsPayOutFS,
					this, ftreePayOutFS);
			setActionStatus.setState(true, true);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ʾ֧���ʽ���Դ���淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �շ���Ŀ���
	 * 
	 */
	public class AcctPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		private CustomTree ftreAcct = null;

		public AcctPanel() throws Exception {

			ftreAcct = new CustomTree("���ܿ�Ŀ", null, IPubInterface.BS_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BS_PARENT_ID, null,
					IPubInterface.ACCT_CODE, true);
			ftreAcct.setIsCheckBoxEnabled(false);
			FScrollPane fspnlAcct = new FScrollPane();
			fspnlAcct.addControl(ftreAcct);
			this.setTitle("֧���ʽ���Դ��Ӧ���ܿ�Ŀ");
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
	 * ���ؽ�������
	 * 
	 */
	public void modulePanelActived() {
		try {
			// �ж�֧���ʽ���ԴDataSet�Ƿ�Ϊ��
			if (dsPayOutFS != null) {
				return;
			}

			// ���֧���ʽ���Դ��Ϣ
			dsPayOutFS = sysIaeStruServ.getPayOutFSTre(Integer
					.parseInt(Global.loginYear));
			// Ϊ֧���ʽ���ԴDataSet��״̬ת���¼�
			dsPayOutFS.addStateChangeListener(new PayOutFsStateChangeListener(
					this));
			ftreePayOutFS.setDataSet(dsPayOutFS);
			ftreePayOutFS.reset();

			// Ϊ֧���ʽ���Դ����ѡ���¼�
			ftreePayOutFS
					.addTreeSelectionListener(new PayOutFSTreeSelectionListener(
							this));

			// ��ù��ܿ�Ŀ��Ϣ
			String sFilter = "set_year =" + Global.loginYear;
			dsAcct = PubInterfaceStub.getMethod().getAcctFunc(sFilter);
			fpnlAcct.getFtreAcct().setDataSet(dsAcct);
			fpnlAcct.getFtreAcct().reset();

		} catch (NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "����֧���ʽ���Դ���ݷ������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "����֧���ʽ���Դ���ݷ������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 
	 * ��༭���ؼ����ɱ༭
	 */
	public void setAllControlEnabledFalse() {
		Common.changeChildControlsEditMode(rightPanel, false);

		// �����ǰѡ��¼��������ü�������ϵ����Ϊ������
		if ("0".equals(frdoIncColDts.getValue().toString())) {
			// ���㰴ť���
			Common.changeChildControlsEditMode(fpnlEditFormula,
					false);
		} else {
			// �����շ���Ŀ�����ɱ༭
			fpnlAcct.getFtreAcct().setIsCheckBoxEnabled(false);
		}
		jspPfsCalcPRI.setEnabled(false);
	}

	/**
	 * 
	 * ��༭���ؼ��ɱ༭
	 */
	public void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(rightPanel, true);
		// �����ǰѡ��¼��������ü�������ϵ����Ϊ����
		if ("0".equals(frdoIncColDts.getValue().toString())) {
			// ���㰴ť���
			Common
					.changeChildControlsEditMode(fpnlEditFormula,
							true);
			// ֧����Ŀ���ѡ��
			frdoSupPrj.setEditable(true);
		} else {
			// ���ù��ܿ����ɱ༭
			fpnlAcct.getFtreAcct().setIsCheckBoxEnabled(true);
			// ֧����Ŀ���ѡ��
			frdoSupPrj.setEditable(false);
		}

		ftxtaPfsFormula.setEditable(false);
		ftxtPriCode.setEditable(false);
		jspPfsCalcPRI.setEnabled(false);
	}

	/**
	 * ����֧���ʽ���Դ����
	 */
	public void doAdd() {
		try {
			if (ftreePayOutFS.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this,
						"��ѡ��һ��֧���ʽ���Դ,��Ϊ����֧���ʽ���Դ�ĸ��ڵ㣡", "��ʾ",
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
			JOptionPane.showMessageDialog(this, "����֧���ʽ���Դ��Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ɾ��֧���ʽ���Դ����
	 */
	public void doDelete() {
		try {
			if (ftreePayOutFS.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ��֧���ʽ���Դ��", "��ʾ",
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
			JOptionPane.showMessageDialog(this, "ɾ��֧���ʽ���Դ��Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ȡ��֧���ʽ���Դ�༭����
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
			JOptionPane.showMessageDialog(this, "ȡ��֧���ʽ���Դ��Ϣ�༭�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ���뷽��
	 */
	public void doInsert() {

	}

	/**
	 * �޸�֧���ʽ���Դ����
	 */
	public void doModify() {
		try {
			saveParInfo();
			PayOutFSModify payOutFSModify = new PayOutFSModify(this);
			payOutFSModify.modify();
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "�޸�֧���ʽ���Դ��Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * ����֧���ʽ���Դ����
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
						"����֧���ʽ���Դ����������д���ֶγ��ȳ������ݿ����ֶγ���!", "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "����֧���ʽ���Դ��Ϣ�������󣬴�����Ϣ��"
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
		payOutFsObj = new PayOutFsObj();
		// �ж�ѡ����Ƿ��Ǹ��ڵ㣬���ѡ�е��Ǹ��ڵ㣬payOutFsObj��Ϊ��ʼֵ
		if (ftreePayOutFS.getSelectedNode() == ftreePayOutFS.getRoot()
				|| dsPayOutFS.isEmpty()
				|| ftreePayOutFS.getSelectedNode() == null) {
			// �ʽ���Դ����
			payOutFsObj.PFS_CODE = "";
			// �ʽ���Դ����
			payOutFsObj.PFS_NAME = "";
			// �ʽ���Դȫ��
			payOutFsObj.pfs_fname = "";
			// �ʽ���ԴӢ����
			payOutFsObj.pfs_ename = "";
			// ��׼֧�����ͱ���
			payOutFsObj.std_type_code = "";
			// ������Դ
			payOutFsObj.data_source = 0;
			// ���㹫ʽ����
			payOutFsObj.formula_det = "";
			// �������ȼ�
			payOutFsObj.calc_pri = 0;
			// �Ƿ�֧����Ŀ
			payOutFsObj.sup_prj = 0;
			// �Ƿ�������������
			payOutFsObj.cf_pfs_flag = 0;
			// Ĭ������
			payOutFsObj.in_common_use = 0;
			// ĩ����־
			payOutFsObj.end_flag = 0;
			// ��ʾ��ʽ
			payOutFsObj.display_format = "";
			// �༭��ʽ
			payOutFsObj.edit_format = "";
			// �����
			payOutFsObj.lvl_id = "";
			// �������
			payOutFsObj.par_id = "";
			// ���
			payOutFsObj.set_year = Integer.parseInt(Global.loginYear);

		} else {
			BeanUtil.mapping(payOutFsObj, dsPayOutFS.getOriginData());
		}
	}

	/**
	 * ����������Դ�����ƿؼ�״̬(���û򲻿���)
	 * 
	 */
	public void setState() {

		if (frdoIncColDts.getValue() == null) {
			return;
		}
		fpnlInOrCompute.removeAll();
		// ���ݽ���ѡ���������Դ����ʾ�շ���Ŀ���������ù�ʽ����
		if ("0".equals(frdoIncColDts.getValue().toString())) {// ¼��
			fpnlInOrCompute.setLayout(new RowPreferedLayout(1));
			fpnlInOrCompute.addControl(fpnlAcct);
			fpnlInOrCompute.updateUI();
			// ֧����Ŀ�����
			if (dsPayOutFS == null) {
				frdoSupPrj.setEditable(false);
			} else if (dsPayOutFS != null
					&& (dsPayOutFS.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				frdoSupPrj.setEditable(false);
			} else {
				frdoSupPrj.setEditable(true);
			}

		} else if ("1".equals(frdoIncColDts.getValue().toString())) {// ����
			fpnlInOrCompute.setLayout(new RowPreferedLayout(2));
			// ���㹫ʽpanel�����ұ���ϸ��Ϣ�༭�����
			fpnlInOrCompute.addControl(fpnlEditFormula, new TableConstraints(1,
					2, false, true));
			// ���㹫ʽ�����ı�������ұ���ϸ��Ϣ�༭�����
			fpnlInOrCompute.addControl(ftxtaPfsFormula, new TableConstraints(1,
					2, true, true));
			// �������ȼ��������ұ���ϸ��Ϣ�༭�����
			// fpnlInOrCompute.addControl(fpnlPfsCalcPRI, new
			// TableConstraints(1,
			// 2, false, true));
			fpnlInOrCompute.updateUI();
			frdoSupPrj.setEditable(false);
		}
	}

	/**
	 * ��տؼ���ʾ��Ϣ
	 * 
	 */
	private void clearShowInfo() {
		// ����
		ftxtPriCode.setValue("");
		// ����
		ftxtfPfsCode.setValue("");
		// ����
		ftxtfPfsName.setValue("");
		// ��������
		ftxtReportPfsName.setValue("");
		// ����
		flstPfsKind.setSelectedIndex(0);
		// �������������
		((JCheckBox) fchkPfsFlag.getEditor()).setSelected(false);
		// Ĭ������
		((JCheckBox) fchkHide.getEditor()).setSelected(false);
		// ��֧ƽ��
		((JCheckBox) this.fchkIsBalance.getEditor()).setSelected(true);
		// ֧����Ŀ���
		frdoSupPrj.setValue("0");
		// ������Դ
		frdoIncColDts.setValue("0");
		// ��ʾ��ʽ
		fcbxSFormate.setValue("");
		// ���㹫ʽ��ť
		fbtnPfsEditFormula.setEnabled(false);
		// ���㹫ʽ
		ftxtaPfsFormula.setValue("");
		ftxtaPfsFormula.setEditable(false);
		// �������ȼ�
		jspPfsCalcPRI.setValue(new Integer(0));
		// �����շ���Ŀ�����нڵ�Ϊ��ѡ��״̬
		SetSelectTree.setIsNoCheck(fpnlAcct.getFtreAcct());
	}

	/**
	 * ���ݹ�ʽ���ݣ��õ�sup_prjֵ
	 * 
	 * @param sForumla
	 *            �磺{F1}+{F2}
	 * @param dsPfs
	 * @return
	 * @throws Exception
	 */
	public int getsupPrjValue(String sForumla, DataSet dsPfs) throws Exception {
		// �滻��ʽ��������
		String sValue = PubInterfaceStub.getMethod().replaceTextExDs(sForumla,
				0, dsPfs, IPayOutFS.PFS_ENAME, IPayOutFS.SUP_PRJ);
		// ȡ��sup_prj(֧����Ŀ������ֶ�ֵ
		String sArrayValue[] = Common.splitString(sValue);
		// ������ԴΪ����ʱ��֧����Ŀ�������ѡ���ֶ�֧����Ŀ�������
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
