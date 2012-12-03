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
 * Title:������Ŀ����ͻ���������
 * </p>
 * <p>
 * Description:������Ŀ����ͻ���������
 * </p>

 */
public class IncColumn extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// �ұ���ϸ��Ϣ�༭���
	FPanel rightPanel = null;

	// ������Ŀ���ݼ�
	DataSet dsIncCol = null;

	// �����ı���
	FTextField ftxtPriCode = null;

	// ������ֿ�
	IntegerTextField ftxtfIncColCode = null;

	// �����ı���
	FTextField ftxtfIncColName = null;

	// ����Ŀ���������ѡ���
	FCheckBox fchkSumFlag = null;

	// ����Ŀ����ѡ���
	FCheckBox fchkHideFlag = null;

	// CL,09,08,24
	// ����ĿΪԤ������
	FCheckBox fchkRPFlag = null;

	// ������Դ��ѡ��
	FRadioGroup frdoIncColDts = null;

	// ��ʾ��ʽ��Ͽ�
	CustomComboBox fcbxSFormate = null;

	// ���㹫ʽ��ť
	FButton fbtnIncColEditFormula = null;

	// ���㹫ʽ�����ı���
	FTextArea ftxtaIncColFormula = null;

	// �������ȼ�����΢����
	IntegerSpinner jspIncColCalcPRI = null;

	// ������㹫ʽ��ť���
	private FPanel fpnlEditFormula = null;

	// ����������ȼ����
	FPanel fpnlIncColCalcPRI = null;

	// �շ���Ŀ��
	CustomTree ftreIncomeSubItem = null;

	// �շ���ĿDataSet
	DataSet dsIncome = null;

	// �����շ���Ŀ���
	private IncomeSubItem fsPnlIncomeSubItem = null;

	// boolean DSFlag = false;

	// ¼��ͼ�����ʾ��ͬ��Ϣ�����
	private FPanel fpnlInOrCompute = null;

	// ������Ŀ��
	CustomTree ftreeIncColumn = null;

	// ����һ��������Ŀ����
	IncColumnObj incColumnObj = null;

	// �����������
	SysCodeRule lvlIdRule = UntPub.lvlRule;;

	// �������Ͳ���
	// sSaveType=modname������Ҷ�ڵ㣬ֻ���޸�����;
	// sSaveType=mod:�޸�Ҷ�ڵ�;
	// sSaveType=modformate:ֻ���޸ĸ�ʽ;
	// sSaveType=add���ӽڵ�;
	// sSaveType=addfirstson���ӵ�һ���ӽڵ�
	String sSaveType = null;

	// �������ݿ�ӿ�
	ISysIaeStru sysIaeStruServ = null;

	// �����������ݽӿ�
	IPubInterface iPubInterface = null;

	/**
	 * ���캯��
	 * 
	 */
	public IncColumn() {

	}

	/**
	 * ��ʼ������
	 */
	public void initize() {
		// �������ݿ�ӿ�
		sysIaeStruServ = SysIaeStruI.getMethod();
		// �����������ݽӿ�
		iPubInterface = PubInterfaceStub.getMethod();
		try {
			// �������ҷָ��ķָ��ؼ�
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// �����,�ŵ�fSplitPane���
			ftreeIncColumn = new CustomTree(IIncColumn.INCCOL_ROOT, null,
					IIncColumn.LVL_ID, ISysIaeStru.NAME, IIncColumn.PAR_ID,
					null);
			FScrollPane fspnlIncColumn = new FScrollPane(ftreeIncColumn);
			fSplitPane.addControl(fspnlIncColumn);

			// �ұ���ϸ��Ϣ�༭���,�ŵ��ָ��ؼ��ұ�
			rightPanel = new FPanel();
			RowPreferedLayout rLayout = new RowPreferedLayout(1);
			rLayout.setColumnWidth(450);
			rightPanel.setLayout(rLayout);
			fSplitPane.addControl(rightPanel);
			// ����rightPanel�߽�����Ϊ10
			rightPanel.setLeftInset(10);
			rightPanel.setTopInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);

			// ���������ı���
			ftxtPriCode = new FTextField("���룺");
			ftxtPriCode.setProportion(0.24f);
			// �������ֿ�
			ftxtfIncColCode = new IntegerTextField("���룺");
			ftxtfIncColCode.setProportion(0.24f);
			// ���������ı���
			ftxtfIncColName = new FTextField("���ƣ�");
			ftxtfIncColName.setProportion(0.24f);
			// �������Ŀ���������ѡ���
			fchkSumFlag = new FCheckBox("����Ŀ���������");
			fchkSumFlag.setTitlePosition("RIGHT");
			// �������Ŀ����ѡ���
			fchkHideFlag = new FCheckBox("����Ŀ����");
			fchkHideFlag.setTitlePosition("RIGHT");
			// �������ĿΪԤ������
			fchkRPFlag = new FCheckBox("����ĿΪԤ�����������շ���ĿԤ��Ԥ������ʱʹ�ã�");
			fchkRPFlag.setTitlePosition("RIGHT");

			// ����������Դ���
			FPanel fpnlIncColDts = new FPanel();
			fpnlIncColDts.setLayout(new RowPreferedLayout(1));
			fpnlIncColDts.setTitle("������Դ");
			fpnlIncColDts.setFontSize(this.getFont().getSize());
			fpnlIncColDts.setFontName(this.getFont().getName());
			fpnlIncColDts.setTitledBorder();

			// ����������Դ��ѡ��
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
			frdoIncColDts.setRefModel("0#¼��        +1#����        ");
			fpnlIncColDts.addControl(frdoIncColDts, new TableConstraints(1, 1,
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

			// �����ʾ��ʽ���ݼ�
			DataSet dsSFormate = sysIaeStruServ.getSFormate(Integer
					.parseInt(Global.loginYear));
			// ������ʾ��ʽ��Ͽ�
			fcbxSFormate = new CustomComboBox(dsSFormate, "name", "name");
			fcbxSFormate.setTitle("��ʾ��ʽ��");
			fcbxSFormate.setProportion(0.24f);

			// ������㹫ʽ��ť���
			fpnlEditFormula = new FPanel();
			RowPreferedLayout editFormulaLayout = new RowPreferedLayout(5);
			editFormulaLayout.setColumnWidth(30);
			fpnlEditFormula.setLayout(editFormulaLayout);
			FLabel flblIncColEditFormula = new FLabel();
			flblIncColEditFormula.setTitle("���㹫ʽ��");
			fbtnIncColEditFormula = new FButton("jsButton", "...");
			fpnlEditFormula.addControl(flblIncColEditFormula,
					new TableConstraints(1, 1, true, true));
			fpnlEditFormula.addControl(fbtnIncColEditFormula,
					new TableConstraints(1, 1, true, false));
			// Ϊ���㹫ʽ��ť�ӵ���¼�
//			fbtnIncColEditFormula.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
////					FormulaPanelCheck formulaPanel = new FormulaPanelCheck(
////							false, null);
//					try {
//						DataSet dsIncColCalc = sysIaeStruServ
//								.getIncColTreCalc(Integer
//										.parseInt(Global.loginYear), dsIncCol
//										.fieldByName("inccol_code").getString());
//						CustomTree ftreeIncColCalc = new CustomTree("��ѡ��Ŀ",
//								dsIncColCalc, IIncColumn.INCCOL_CODE,
//								IIncColumn.INCCOL_FNAME, IIncColumn.PAR_ID,
//								null);
////						formulaPanel.initTree(ftreeIncColCalc);
////						formulaPanel.setFormula(ftxtaIncColFormula.getValue()
////								.toString());
//					} catch (Exception ex) {
//						JOptionPane.showMessageDialog(IncColumn.this,
//								"��ʾ���ù�ʽ���淢�����󣬴�����Ϣ��" + ex.getMessage(), "��ʾ",
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

			// ������㹫ʽ���б༭��
			ftxtaIncColFormula = new FTextArea(false);
			ftxtaIncColFormula.setEditable(false);

			// ����������ȼ����
			fpnlIncColCalcPRI = new FPanel();
			fpnlIncColCalcPRI.setLayout(new RowPreferedLayout(4));
			// ����������ȼ�Label
			FLabel flblIncColCalcPRI = new FLabel();
			flblIncColCalcPRI.setTitle("�������ȼ���");
			// ����������ȼ�΢����
			SpinnerModel modelCalcPRI = new SpinnerNumberModel(0, 0, 100, 1);
			jspIncColCalcPRI = new IntegerSpinner(modelCalcPRI);
			fpnlIncColCalcPRI.addControl(flblIncColCalcPRI,
					new TableConstraints(1, 1, false, true));
			fpnlIncColCalcPRI.add(jspIncColCalcPRI, new TableConstraints(1, 3,
					false, true));

			// �����շ���Ŀ���
			fsPnlIncomeSubItem = new IncomeSubItem();

			// mod by CL�� 09��08��24
			// ��������շ���Ŀ���
			// fsPnlIncomeSubItem1 = new IncomeSubItem();
			// ����¼��ͼ�����ʾ��ͬ��Ϣ�����
			fpnlInOrCompute = new FPanel();

			// �����ı��ؼ�
			rightPanel.addControl(ftxtPriCode, new TableConstraints(1, 1,
					false, false));
			// ������ֿ�
			rightPanel.addControl(ftxtfIncColCode, new TableConstraints(1, 1,
					false, false));
			// �����ı��ؼ�
			rightPanel.addControl(ftxtfIncColName, new TableConstraints(1, 1,
					false, false));
			// ����Ŀ���������ѡ���
			rightPanel.addControl(fchkSumFlag, new TableConstraints(1, 1,
					false, false));
			// ����Ŀ����ѡ���
			rightPanel.addControl(fchkHideFlag, new TableConstraints(1, 1,
					false, false));
			// ����ĿΪԤ������
			rightPanel.addControl(fchkRPFlag, new TableConstraints(1, 1, false,
					false));
			// ������Դ��ѡ��
			rightPanel.addControl(fpnlIncColDts, new TableConstraints(2, 1,
					false, false));
			// ��ʾ��ʽ��Ͽ�
			rightPanel.addControl(fcbxSFormate, new TableConstraints(1, 1,
					false, false));
			// ¼��ͼ�����ʾ��ͬ���
			rightPanel.addControl(fpnlInOrCompute, new TableConstraints(1, 1,
					true, false));

			// ������ԴĬ�����Ϊ¼��
			frdoIncColDts.setValue("0");

			// Ĭ�����ұ���ϸ��Ϣ�༭����пؼ����ɱ༭
			setAllControlEnabledFalse();

			// ����������
			this.createToolBar();
			// ����ϵͳ״̬���ù�������ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsIncCol,
					this, ftreeIncColumn);
			setActionStatus.setState(true, true);
		} catch (Exception e) {
			// throw new FAppException("WF-000001", e.getMessage());
			JOptionPane.showMessageDialog(Global.mainFrame,
					"��ʾ������Ŀ��ʾ���淢�����󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �շ���Ŀ���
	 * 
	 */
	public class IncomeSubItem extends FPanel {

		private static final long serialVersionUID = 1L;

		public IncomeSubItem() throws Exception {

//			ftreIncomeSubItem = new CustomTree("�շ���Ŀ", null,
//					IPubInterface.IncSubItem_ID,
//					IPubInterface.IncSubItem_FNAME,
//					IPubInterface.IncSubItem_PARENT_ID, null,
//					IPubInterface.IncSubItem_Code, true);
			ftreIncomeSubItem.setIsCheckBoxEnabled(false);
			FScrollPane fpnlIncomeSubItem = new FScrollPane();
			fpnlIncomeSubItem.addControl(ftreIncomeSubItem);
			this.setTitle("������Ŀ��Ӧ�շ���Ŀ");
			this.setLayout(new RowPreferedLayout(1));
			this.addControl(fpnlIncomeSubItem);
		}
	}

	/**
	 * ���ؽ�������
	 * 
	 */
	public void modulePanelActived() {
		// ���������Ŀ���ݼ��Ѵ����ݿ��л�ȡ���˳�
		if (dsIncCol != null) {
			return;
		}
		try {
			// �����ݿ��л�ȡ������Ŀ���ݼ�
			dsIncCol = sysIaeStruServ.getIncColTre(Integer
					.parseInt(Global.loginYear));
			// Ϊ������Ŀ���ݼ�����״̬ת���¼�
			dsIncCol.addStateChangeListener(new IncColumnStateChangeListener(
					this));
			// Ϊ������Ŀ���������ݼ�
			ftreeIncColumn.setDataSet(dsIncCol);
			ftreeIncColumn.reset();
			// Ϊ������Ŀ������SelectionListener�¼�
			ftreeIncColumn
					.addTreeSelectionListener(new IncColumnTreeSelectionListener(
							this));
			// ����շ���Ŀ��Ϣ
			String sFilter = "set_year =" + Global.loginYear;
//			dsIncome = PubInterfaceStub.getMethod().getIncSubItem(sFilter);
			ftreIncomeSubItem.setDataSet(dsIncome);
			ftreIncomeSubItem.reset();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"������Ŀ�������ݷ����������������ϵ��", "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����������Ŀ
	 * 
	 */
	public void doAdd() {
		try {
			// ���δѡ�нڵ㣨�������ڵ�ĸ��ڵ㣩,�˳�
			if (ftreeIncColumn.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"��ѡ��һ��������Ŀ,��Ϊ����������Ŀ�ĸ��ڵ㣡", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// ����һ��������Ŀ����
			incColumnObj = new IncColumnObj();
			// ���浱ǰѡ�еĽڵ���Ϣ���������ڵ�ĸ��ڵ㣩��Ϣ��������Ŀ����
			saveParInfo(incColumnObj);
			// ����������Ŀ���Ӳ�����
			IncColumnAdd incColumnAdd = new IncColumnAdd(this);
			// ִ������������Ŀ�������жϲ����Ƿ�ɹ�
			if (!incColumnAdd.add()) {
				return;
			}
			// ����δ����״̬��̬Ϊfalse
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"����������Ŀ��Ϣ�������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ɾ��������Ŀ
	 * 
	 */
	public void doDelete() {
		try {
			// �ж��Ƿ�ѡ�нڵ�, ��δѡ��(==null)���˳�;��ѡ��(!=null)��������ִ��
			if (ftreeIncColumn.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ��������Ŀ��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// ����һ��������Ŀ����
			incColumnObj = new IncColumnObj();
			// ���浱ǰɾ���ڵ����Ϣ
			saveParInfo(incColumnObj);
			// ����������Ŀɾ��������
			IncColumnDel incColumnDel = new IncColumnDel(this);
			// ִ��ɾ��������Ŀ����
			if (incColumnDel.delete()) {// ����ɾ������
				// ��տؼ���ʾ��Ϣ
				clearShowInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"ɾ��������Ŀ��Ϣ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * ȡ��������Ŀ�༭
	 */
	public void doCancel() {
		try {
			// ���ݼ�ȡ���༭
			dsIncCol.cancel();
			// ��ǰ�ڵ��Ǹ��ڵ�
			if ("".equals(incColumnObj.LVL_ID)) {
				clearShowInfo();
				// ��λ����ǰѡ�еĽڵ�
			} else {
				ftreeIncColumn.expandTo(IIncColumn.LVL_ID, incColumnObj.LVL_ID);
			}
			// �������ݱ���״̬Ϊtrue
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"ȡ��������Ŀ���෢�����󣬣�������Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����������Ŀ�༭
	 */
	public void doInsert() {

	}

	/**
	 * �޸�������Ŀ�༭
	 */
	public void doModify() {
		try {
			// �ж��Ƿ�ѡ��һ������Ŀ
			if (ftreeIncColumn.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ��������Ŀ��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// ����һ������Ŀ�ڵ㱣�����
			incColumnObj = new IncColumnObj();
			// ����ѡ�еĽڵ���Ϣ
			saveParInfo(incColumnObj);

			// ����������Ŀ�޸Ŀ�����
			IncColumnModify incColumnModify = new IncColumnModify(this);
			// �ж��Ƿ��޸ĳɹ�
			if (!incColumnModify.modify())
				return;
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"�޸�������Ŀ��Ϣ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ����������Ŀ
	 */
	public void doSave() {
		try {
			// ����������Ŀ������
			IncColumnSave incColumnSave = new IncColumnSave(this);
			// ���������������ౣ�淽��
			incColumnSave.save();
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"����������Ŀ��Ϣ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
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
	private void saveParInfo(IncColumnObj incColumnObj)
			throws NumberFormatException, Exception {
		// �ж�ѡ����Ƿ��Ǹ��ڵ㣬���ѡ�е��Ǹ��ڵ㣬incColumnObj��Ϊ��ʼֵ
		if (ftreeIncColumn.getSelectedNode() == ftreeIncColumn.getRoot()
				|| dsIncCol.isEmpty()
				|| ftreeIncColumn.getSelectedNode() == null) {// ���ڵ�
			incColumnObj.INCCOL_CODE = "";// ������Ŀ����
			incColumnObj.INCCOL_NAME = "";// ������Ŀ������
			incColumnObj.INCCOL_FNAME = "";// ������Ŀȫ��
			incColumnObj.INCCOL_ENAME = "";// ������Ŀȫ��
			incColumnObj.DATA_SOURCE = 0;// ������Դ
			incColumnObj.FORMULA_DET = "";// ���㹫ʽ����
			incColumnObj.CALC_PRI = 0;// �������ȼ�
			incColumnObj.SUM_FLAG = 0;// ��Ŀ�Ƿ���Ҫ�������
			incColumnObj.IS_HIDE = 0;// �Ƿ�����
			incColumnObj.N2 = 0;// �Ƿ���ҪԤ������
			incColumnObj.END_FLAG = 0;// ĩ����־
			incColumnObj.DISPLAY_FORMAT = "";// ��ʾ��ʽ
			incColumnObj.EDIT_FORMAT = "";// �༭��ʽ
			incColumnObj.LVL_ID = ""; // �����
			incColumnObj.PAR_ID = ""; // �������
			incColumnObj.SET_YEAR = Integer.parseInt(Global.loginYear);// Ԥ�����
		} else {// ���Ǹ��ڵ�
			BeanUtil.mapping(incColumnObj, dsIncCol.getOriginData());
		}
	}

	/**
	 * ����RadioGroup��¼�뻹�Ǽ���״̬����ʾ�շ���Ŀ���������ù�ʽ����
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
			fpnlInOrCompute.addControl(fsPnlIncomeSubItem);
			fpnlInOrCompute.updateUI();

		} else if ("1".equals(frdoIncColDts.getValue().toString())) {// ����
			fpnlInOrCompute.setLayout(new RowPreferedLayout(2));
			// ���㹫ʽ���
			// fpnlInOrCompute.addControl(fpnlEditFormula, new
			// TableConstraints(1,
			// 2, false, true));
			// // ���㹫ʽ���б༭��
			// fpnlInOrCompute.addControl(ftxtaIncColFormula,
			// new TableConstraints(1, 2, true, true));
			// // �������ȼ����
			// fpnlInOrCompute.addControl(fpnlIncColCalcPRI, new
			// TableConstraints(
			// 1, 2, false, true));
			// mod
			// ���㹫ʽ���
			fpnlInOrCompute.addControl(fpnlEditFormula, new TableConstraints(1,
					2, false, true));
			// ���㹫ʽ���б༭��
			fpnlInOrCompute.addControl(ftxtaIncColFormula,
					new TableConstraints(1, 2, true, true));
			// �������ȼ����
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
	 * ��տؼ���ʾ��Ϣ
	 * 
	 */
	private void clearShowInfo() {
		// ����
		ftxtPriCode.setValue("");
		ftxtfIncColCode.setValue("");
		// ����
		ftxtfIncColName.setValue("");
		// ����Ŀ���������
		((JCheckBox) fchkSumFlag.getEditor()).setSelected(false);
		// ����Ŀ����
		((JCheckBox) fchkHideFlag.getEditor()).setSelected(false);
		// ����ĿΪԤ������
		((JCheckBox) fchkRPFlag.getEditor()).setSelected(false);
		// ������Դ
		frdoIncColDts.setValue("0");
		// ��ʾ��ʽ
		fcbxSFormate.setValue("");
		// ���㹫ʽ��ť
		fbtnIncColEditFormula.setEnabled(false);
		// ���㹫ʽ
		ftxtaIncColFormula.setValue("");
		ftxtaIncColFormula.setEditable(false);
		// �������ȼ�
		jspIncColCalcPRI.setValue(new Integer(0));
		// �����շ���Ŀ�����нڵ�Ϊ��ѡ��״̬
		SetSelectTree.setIsNoCheck(ftreIncomeSubItem);
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
			ftreIncomeSubItem.setIsCheckBoxEnabled(false);
		}
		// ���ü������ȼ�����΢���򲻿ɱ༭
		jspIncColCalcPRI.setEnabled(false);
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
		} else {
			// �����շ���Ŀ���ɱ༭
			ftreIncomeSubItem.setIsCheckBoxEnabled(true);
		}
		// ���ü��㹫ʽ�����ı��򲻿ɱ༭
		ftxtaIncColFormula.setEditable(false);
		// ���������ı��򲻿ɱ༭
		ftxtPriCode.setEditable(false);
		// ���ü������ȼ�����΢����ɱ༭
		jspIncColCalcPRI.setEnabled(true);
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
