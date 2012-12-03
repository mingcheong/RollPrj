/**
 * @# CellSetPanel.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;

import gov.nbcs.rp.basinfo.common.BaseUtil;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import gov.nbcs.rp.sys.sysiaestru.ui.SetSelectTree;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;

/**
 * ����˵��:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * ��Ȩ���У��㽭����
 * <P>
 * δ������˾��ɣ��������κη�ʽ���ƻ�ʹ�ñ������κβ��֣�
 * <P>
 * ��Ȩ�߽��ܵ�����׷����
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-3-19
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class CellSetPanel extends FTitledPanel implements ISelectionListener {
	/**
	 * ��Ԫ����������
	 * 
	 * @author qzc
	 * 
	 */
	public static int lastValue_source = -3;// ��¼����ѡ��ı䶯

	public static int lastValue_Acc = -3;// ��¼����ѡ��ı䶯

	private FPanel fpnlType;

	private CellFieldInfo curField;

	private boolean maskChange = false;

	CustomTree ftreePayOutFS, ftreePayoutKind;

	private FPanel fpnlZc, fnlPayOutSort;

	DataSet dsIncType;

	DataSet dsAcctEconomy;

	DataSet dsAcctFunc;

	DataSet dsPfs;

	DataSet dsPayOutKind;

	IncomingPanel incomingPanel;// ����Ԥ���

	PayOutPanel payOutPanel; // ֧��Ԥ���

	GsPanel gsPanel;

	SQLPanel sqlPanel;

	CustomTree ftreeIncType, ftreeAcctFunc, ftreeAcctEconomy;

	FPanel fpnlFuncEconemy;

	FScrollPane fspnlAcctFunc, fspnlAcctEconomy;

	FRadioGroup frdoPayOutSort;

	FRadioGroup frdoSource;

	FRadioGroup frdoType;

	SzzbSet parent;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3944740492527157965L;

	public CellSetPanel(SzzbSet parent) {
		this.parent = parent;
		// ��Դ
		FPanel fpnlSource = new FPanel();
		fpnlSource.setLayout(new RowPreferedLayout(1));
		fpnlSource.setTitle("�� Դ");
		frdoSource = new FRadioGroup("", FRadioGroup.VERTICAL);
		frdoSource.setRefModel("" + CellFieldInfo.SOURCETYPE_SR + "#����Ԥ���+"
				+ CellFieldInfo.SOURCETYPE_ZC + "#֧��Ԥ���+"
				+ CellFieldInfo.SOURCETYPE_FOR + "#���㹫ʽ+"
				+ CellFieldInfo.SOURCETYPE_SQL + "#SQL���+"
				+ CellFieldInfo.SOURCETYPE_NULL + "#����Ϊ��");
		frdoSource.setTitleVisible(false);
		frdoSource.addValueChangeListener(new SourceValueChangeListener());
		fpnlSource.addControl(frdoSource,
				new TableConstraints(1, 1, true, true));
		// ����
		fpnlType = new FPanel();
		fpnlType.setLayout(new RowPreferedLayout(1));
		fpnlType.setTitle("�� ��");
		frdoType = new FRadioGroup("", FRadioGroup.VERTICAL);
		frdoType.setRefModel("1#���ܷ���+2#���÷���");
		frdoType.addValueChangeListener(new TypeValueChangeListener());
		frdoType.setTitleVisible(false);
		fpnlType.addControl(frdoType, new TableConstraints(1, 1, true, true));

		// ֧��
		fpnlZc = new FPanel();
		fpnlZc.setLayout(new RowPreferedLayout(1));
		try {
			incomingPanel = new IncomingPanel();// ����Ԥ���
			payOutPanel = new PayOutPanel(); // ֧��Ԥ���
			gsPanel = new GsPanel();
			sqlPanel = new SQLPanel();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��������Panel
		FPanel fpnlSave = new FPanel();
		fpnlSave.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 65));

		FLabel flblSaveInfo = new FLabel();
		// ��ʱ����XXL
		// flblSaveInfo.setTitle("�ɹ���");
		// FButton fbtnSave = new FButton("fbtnSave", "��������");
		// fbtnSave.addActionListener(new ActionListener() {
		//
		// public void actionPerformed(ActionEvent e) {
		// save();
		//
		// }
		//
		// });
		fpnlSave.addControl(flblSaveInfo);
		// fpnlSave.addControl(fbtnSave);

		RowPreferedLayout rLay = new RowPreferedLayout(8);
		rLay.setColumnWidth(130);
		rLay.setRowHeight(22);
		this.setLayout(rLay);
		this.addControl(fpnlSource, new TableConstraints(5, 1, false, false));
		this.addControl(fpnlZc, new TableConstraints(8, 6, false, false));
		this.addControl(fpnlSave, new TableConstraints(8, 1, false, false));
		this.addControl(fpnlType, new TableConstraints(3, 1, true, false));
		fpnlType.setVisible(false);
	}

	public void sourceValueChanged() {
		fpnlZc.removeAll();
		fpnlZc.setLayout(new RowPreferedLayout(1));
		fpnlType.setVisible(false);
		if (frdoSource.getValue() == null) {
			CellSetPanel.this.setVisible(false);
			CellSetPanel.this.setVisible(true);
			return;
		}

		switch (Integer.parseInt(frdoSource.getValue().toString())) {
		case CellFieldInfo.SOURCETYPE_SR: // ����Ԥ���
			fpnlZc.addControl(incomingPanel, new TableConstraints(1, 1, true,
					true));
			fpnlType.setVisible(false);
			frdoType.setValue("-2");
			break;
		case CellFieldInfo.SOURCETYPE_ZC: // ֧��Ԥ���
			fpnlZc.addControl(payOutPanel, new TableConstraints(1, 1, true,
					true));
			fnlPayOutSort.setVisible(false);
			fpnlType.setVisible(true);
			break;
		case CellFieldInfo.SOURCETYPE_FOR: // ���㹫ʽ
			fpnlZc.addControl(gsPanel, new TableConstraints(1, 1, true, true));
			fpnlType.setVisible(false);
			frdoType.setValue("-2");
			break;
		case CellFieldInfo.SOURCETYPE_SQL: // SQL���
			fpnlZc.addControl(sqlPanel, new TableConstraints(1, 1, true, true));
			fpnlType.setVisible(false);
			frdoType.setValue("-2");
			break;
		case CellFieldInfo.SOURCETYPE_NULL: // ����Ϊ��
			fpnlType.setVisible(false);
			frdoType.setValue("-2");
			break;
		default:
			fpnlType.setVisible(false);
			frdoType.setValue("-2");
		}
		fpnlZc.repaint();
		fpnlZc.updateUI();
		// CellSetPanel.this.repaint(CellSetPanel.this.getVisibleRect());
		CellSetPanel.this.setVisible(false);
		CellSetPanel.this.setVisible(true);
	}

	/**
	 * ����Ԥ���
	 * 
	 * @author qzc
	 * 
	 */
	class IncomingPanel extends FPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8669272749926915153L;

		public IncomingPanel() throws Exception {
			// ������Ŀ���
			ftreeIncType = new CustomTree("������Ŀ���", null, IIncType.LVL_ID,
					ISysIaeStru.NAME, IIncType.PAR_ID, null);
			ftreeIncType.setSortKey(IIncType.INCTYPE_CODE);
			ftreeIncType.setIsCheck(true);
			ftreeIncType.setIsCheckBoxEnabled(true);
			FScrollPane fspnlIncType = new FScrollPane(ftreeIncType);

			this.setLayout(new RowPreferedLayout(3));
			this.addControl(fspnlIncType,
					new TableConstraints(1, 1, true, true));
		}

		public void freshData() {

			maskChange = true;
			clearData();
			String value;
			if (curField != null) {
				value = curField.getIncType();
				try {
					if (!Common.isNullStr(value))
						setChecked(ftreeIncType, IIncType.INCTYPE_CODE, value
								.split(";"));

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			maskChange = false;

		}

		private void clearData() {
			boolean maskChangeTemp = maskChange;
			maskChange = true;
			SetSelectTree.setIsNoCheck(ftreeIncType);
			maskChange = maskChangeTemp;
		}

		// �ڵ�ѡ��ı�ʱʹ��
		public void treeNodesChanged(TreeModelEvent e) {
			if (maskChange || curField == null)
				return;
			String sField = listToString(getIncomeListByLeaf(ftreeIncType));
			curField.setIncType(sField);
			ftreeIncType.updateUI();

		}

	}

	/**
	 * ֧��Ԥ���Panel
	 * 
	 * @author qzc
	 * 
	 */
	class PayOutPanel extends FPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8151209309184093912L;

		public PayOutPanel() throws Exception {
			// �ʽ���Դ
			ftreePayOutFS = new CustomTree("�ʽ���Դ", null, IPayOutFS.LVL_ID,
					ISysIaeStru.NAME, IPayOutFS.PAR_ID, null);
			ftreePayOutFS.setIsCheck(true);
			ftreePayOutFS.setIsCheckBoxEnabled(true);

			FScrollPane fspnlPayOutFS = new FScrollPane(ftreePayOutFS);

			// ֧����Ŀ���
			ftreePayoutKind = new CustomTree("֧����Ŀ���", null,
					IPayOutType.LVL_ID, ISysIaeStru.NAME, IPayOutType.PAR_ID,
					null);
			ftreePayoutKind.setIsCheck(true);
			ftreePayoutKind.setIsCheckBoxEnabled(true);

			FScrollPane fspnlPayOutKind = new FScrollPane(ftreePayoutKind);

			// �Ź��ܿ�Ŀ�����ÿ�ĿPanel
			fpnlFuncEconemy = new FPanel();

			// ���ܿ�Ŀ
			ftreeAcctFunc = new CustomTree("���ܿ�Ŀ", null, IPubInterface.BS_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BS_PARENT_ID, null);
			ftreeAcctFunc.setSortKey(IPubInterface.ACCT_CODE);
			ftreeAcctFunc.setIsCheck(true);
			ftreeAcctFunc.setIsCheckBoxEnabled(true);
			fspnlAcctFunc = new FScrollPane(ftreeAcctFunc);

			// ���ÿ�Ŀ
			ftreeAcctEconomy = new CustomTree("���ÿ�Ŀ", null,
					IPubInterface.BSI_ID, IPubInterface.ACCT_JJ_FNAME,
					IPubInterface.BSI_PARENT_ID, null);
			ftreeAcctEconomy.setSortKey(IPubInterface.ACCT_CODE_JJ);
			ftreeAcctEconomy.setIsCheck(true);
			ftreeAcctEconomy.setIsCheckBoxEnabled(true);

			fspnlAcctEconomy = new FScrollPane(ftreeAcctEconomy);

			// ���
			fnlPayOutSort = new FPanel();
			fnlPayOutSort.setLayout(new RowPreferedLayout(2));

			FPanel fnlPayOutSortA = new FPanel();
			fnlPayOutSortA.setLayout(new RowPreferedLayout(1));
			fnlPayOutSortA.setTitle("���");

			frdoPayOutSort = new FRadioGroup("", FRadioGroup.VERTICAL);
			frdoPayOutSort.setRefModel(CellFieldInfo.ZCTYPE_BASE + "#����֧��+"
					+ CellFieldInfo.ZCTYPE_PRO + "#��Ŀ֧��+"
					+ CellFieldInfo.ZCTYPE_ALL + "#ȫ��֧��");
			frdoPayOutSort.setTitleVisible(false);
			frdoPayOutSort.addValueChangeListener(new ValueChangeListener() {

				public void valueChanged(ValueChangeEvent arg0) {
					if (curField == null || maskChange)
						return;
					if (frdoPayOutSort.getValue() == null)
						return;
					if (arg0.getNewValue() == null)
						return;
					curField.setZcType(Integer.parseInt(frdoPayOutSort
							.getValue().toString()));

				}

			});
			fnlPayOutSortA.addControl(frdoPayOutSort, new TableConstraints(1,
					1, true, true));
			fnlPayOutSort.addControl(fnlPayOutSortA, new TableConstraints(4, 1,
					false, true));

			this.setLayout(new RowPreferedLayout(4));
			this.addControl(fspnlPayOutFS, new TableConstraints(1, 1, true,
					true));
			this.addControl(fspnlPayOutKind, new TableConstraints(1, 1, true,
					true));
			this.addControl(fpnlFuncEconemy, new TableConstraints(1, 1, true,
					true));
			this.addControl(fnlPayOutSort, new TableConstraints(1, 1, true,
					true));
		}

		public void treeNodesChangedJJ(TreeModelEvent e) {
			if (curField == null || maskChange)
				return;
			curField.setAcctJJ(listToString(getIncomeList(ftreeAcctEconomy)));

		}

		public void treeNodesChangedAccFunc(TreeModelEvent e) {

			if (curField == null || maskChange)
				return;
			curField.setAcctCode(listToString(getIncomeList(ftreeAcctFunc)));

		}

		public void treeNodesChangedPayoutKind(TreeModelEvent e) {
			if (maskChange || curField == null)
				return;
			String sPayoutKind = "";

			try {
				sPayoutKind = BaseUtil.getSelectFieldLeafOnTree(
						ftreePayoutKind, IPayOutType.PAYOUT_KIND_CODE);
			} catch (Exception e1) {
				// TODO �Զ����� catch ��
				e1.printStackTrace();

			}

			curField.setPayoutKind(sPayoutKind);
		}

		// �ڵ�״̬�ı�ʱ����
		public void treeNodesChangedPayOut(TreeModelEvent e) {
			String sField = "";

			try {
				sField = ftreePayOutFS.getDataSet().fieldByName(
						IPayOutFS.PFS_ENAME).getString();
			} catch (Exception e1) {
				// TODO �Զ����� catch ��
				e1.printStackTrace();
			}
			if (maskChange || curField == null)
				return;
			if (ftreePayOutFS.getSelectedNode() == null)
				return;
			if (!ftreePayOutFS.getSelectedNode().isLeaf()) {// �ϼ�������ѡ��
				boolean maskChangeTemp = maskChange;
				maskChange = true;
				SetSelectTree.setIsNoCheck(ftreePayOutFS);
				maskChange = maskChangeTemp;
				curField.setFieldName(null);
				return;
			}// ���ȡ��ѡ��,���ƶ�
			if (getNodeState(ftreePayOutFS.getSelectedNode()) == MyPfNode.UNSELECT) {
				if (ftreePayOutFS.getSelectedNodes(true).length == 1)
					return;
				curField.setFieldName(null);
				return;
			} else {// ���ѡ������������Ҫ����������
				MyTreeNode nodeType = ftreePayOutFS.getSelectedNode();
				boolean maskChangeTemp = maskChange;
				maskChange = true;
				SetSelectTree.setIsNoCheck(ftreePayOutFS);
				maskChange = maskChangeTemp;
				((MyPfNode) (nodeType.getUserObject()))
						.setSelectStat(MyPfNode.SELECT);
				curField.setFieldName(sField);
				return;
			}

		}

		// �ڵ�״̬�ı�ʱ���� ���Ը�������
		public void treeNodesChangedPayOut2(TreeModelEvent e) {
			if (maskChange || curField == null)
				return;
			String sField = "";

			try {
				sField = BaseUtil.getSelectFieldLeafOnTree(ftreePayOutFS,
						IPayOutFS.PFS_ENAME);
			} catch (Exception e1) {
				// TODO �Զ����� catch ��
				e1.printStackTrace();

			}

			curField.setFieldName(sField);

		}

		/**
		 * ˢ����ʾ��ֵ
		 * 
		 */
		public void freshData() {
			maskChange = true;
			clearData();
			String value;
			if (curField != null) {
				value = curField.getFieldName();
				try {
					if (!Common.isNullStr(value))
						setChecked(ftreePayOutFS, IPayOutFS.PFS_ENAME, value
								.split(";"));
					// ֧����Ŀ���
					value = curField.getPayoutKind();
					if (!Common.isNullStr(value))
						setChecked(ftreePayoutKind,
								IPayOutType.PAYOUT_KIND_CODE, value.split(";"));

					value = curField.getAcctJJ();
					if (!Common.isNullStr(value))
						setChecked(ftreeAcctEconomy,
								IPubInterface.ACCT_CODE_JJ, value.split(";"));
					value = curField.getAcctCode();
					if (!Common.isNullStr(value))
						setChecked(ftreeAcctFunc, IPubInterface.ACCT_CODE,
								value.split(";"));
					frdoPayOutSort.setValue(String
							.valueOf(curField.getZcType()));
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			maskChange = false;
		}

		private void clearData() {
			SetSelectTree.setIsNoCheck(ftreePayOutFS);
			SetSelectTree.setIsNoCheck(ftreePayoutKind);
			SetSelectTree.setIsNoCheck(ftreeAcctFunc);
			SetSelectTree.setIsNoCheck(ftreeAcctEconomy);
			frdoPayOutSort.setValue("-1");

		}

	}

	/**
	 * ����Panel
	 * 
	 * @author qzc
	 * 
	 */
	class GsPanel extends FPanel {

		/**
		 * 
		 */
		FTextField ftxtFormula;

		IntegerSpinner jspCalcPRI;

		private static final long serialVersionUID = -3281929036129997381L;

		public GsPanel() throws Exception {
			this.setLeftInset(10);
			this.setRightInset(10);

			FLabel flblTopEmpty = new FLabel();

			FLabel flblFormula = new FLabel();
			flblFormula.setText("��ʽ���ݣ�");
			flblFormula.setForeground(Color.blue);
			ftxtFormula = new FTextField(false);
			ftxtFormula.addValueChangeListener(new ValueChangeListener() {

				public void valueChanged(ValueChangeEvent arg0) {
					if (maskChange || curField == null)
						return;
					curField.setFormula((String) ftxtFormula.getValue());

				}

			});
			FLabel flblEmpty = new FLabel();
			FLabel flblInfo = new FLabel();
			flblInfo.setTitle("ע����ʽ��ʽ��{D2}+{F7},�������ַ��е����ݡ�");
			// �������ȼ�Label
			FLabel flblCalcPRI = new FLabel();
			flblCalcPRI.setTitle("�������ȼ���");
			flblCalcPRI.setForeground(Color.blue);
			// �������ȼ�
			SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
			jspCalcPRI = new IntegerSpinner(spinnerModel);
			jspCalcPRI.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					if (maskChange || curField == null)
						return;
					curField.setForLvl(((Integer) ((IntegerSpinner) (e
							.getSource())).getValue()).intValue());

				}

			});

			RowPreferedLayout rLay = new RowPreferedLayout(3);
			rLay.setColumnGap(1);
			rLay.setColumnWidth(80);
			this.setLayout(rLay);

			this.addControl(flblTopEmpty, new TableConstraints(2, 3, false,
					true));
			this.addControl(flblFormula,
					new TableConstraints(1, 1, false, true));
			this.addControl(ftxtFormula,
					new TableConstraints(1, 2, false, true));
			this
					.addControl(flblEmpty, new TableConstraints(1, 1, false,
							false));
			this.addControl(flblInfo, new TableConstraints(1, 2, false, true));
			this.addControl(flblCalcPRI, new TableConstraints(1, 1, false,
					false));
			this.add(jspCalcPRI, new TableConstraints(1, 1, false, false));

		}

		public void freshData() {
			maskChange = true;
			clearData();
			if (curField != null) {
				ftxtFormula.setValue(curField.getFormula());
				jspCalcPRI.setValue(new Integer(curField.getForLvl()));
			}
			maskChange = false;
		}

		private void clearData() {
			ftxtFormula.setValue("");
			jspCalcPRI.setValue(new Integer(1));
		}
	}

	/**
	 * sqlPanel
	 * 
	 * @author qzc
	 * 
	 */
	class SQLPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		FTextArea txtSQL;

		public SQLPanel() {
			this.setLeftInset(10);
			this.setRightInset(10);
			this.setLayout(new BorderLayout());
			txtSQL = new FTextArea("SQL���:");
			txtSQL.setTitleVisible(false);
			txtSQL.addValueChangeListener(new ValueChangeListener() {

				public void valueChanged(ValueChangeEvent arg0) {
					if (maskChange || curField == null)
						return;
					// �����ű������
					String value;
					if (txtSQL.getValue() == null)
						value = "";
					else
						value = txtSQL.getValue().toString();
					// value = value.replaceAll("'", "''");
					curField.setSqlStatement(value);

				}

			});
			this.add(txtSQL, BorderLayout.CENTER);

		}

		public void freshData() {
			maskChange = true;
			clearData();
			if (curField != null) {
				txtSQL.setValue(curField.getSqlStatement());
			}
			maskChange = false;
		}

		private void clearData() {
			txtSQL.setValue("");
		}

	}

	/**
	 * ��Դ�任(����Ԥ���֧��Ԥ������㹫ʽ������Ϊ��)����ʾ��ͬ��Panel
	 * 
	 * @author qzc
	 * 
	 */
	class SourceValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent e) {

			if (e.getNewValue() == null)
				return;
			int iValue = Integer.parseInt(e.getNewValue().toString());
			// if (iValue == lastValue_source)// /���ѡ���ֵ����һ�ε�һ������ˢ��
			// return;
			lastValue_source = iValue;
			if (!maskChange && curField != null) {
				curField.setSourceType(iValue);

			}
			if (!maskChange)
				sourceValueChanged();

		}
	}

	/**
	 * ����任(���ܿ�Ŀ�����ÿ�Ŀ),��ʾ��ͬ��Panel
	 * 
	 * @author Administrator
	 * 
	 */
	class TypeValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent e) {
			if (e.getNewValue() == null) {
				fpnlFuncEconemy.removeAll();
				fnlPayOutSort.setVisible(false);
				return;
			}
			int iValue = Integer.parseInt(e.getNewValue().toString());
			// if (lastValue_Acc == iValue)
			// return;
			// lastValue_Acc = iValue;

			switch (iValue) {
			case 1: // ���ܿ�Ŀ
				if (!maskChange && curField != null) {
					curField.setAcctJJ("");// ���þ��ÿ�ĿΪ��
					payOutPanel.freshData();
				}
				fpnlFuncEconemy.removeAll();
				fpnlFuncEconemy.setLayout(new RowPreferedLayout(1));
				fpnlFuncEconemy.addControl(fspnlAcctFunc, new TableConstraints(
						1, 1, true, true));
				fnlPayOutSort.setVisible(false);// ���
				break;
			case 2:// ���ÿ�Ŀ
				if (!maskChange && curField != null) {
					curField.setAcctCode("");// ���ù��ܿ�ĿΪ��
					payOutPanel.freshData();
				}
				fpnlFuncEconemy.removeAll();
				fpnlFuncEconemy.setLayout(new RowPreferedLayout(1));
				fpnlFuncEconemy.addControl(fspnlAcctEconomy,
						new TableConstraints(1, 1, true, true));
				fnlPayOutSort.setVisible(true); // ���
				break;
			default: {
				fpnlFuncEconemy.removeAll();
				fnlPayOutSort.setVisible(false);
			}
			}
			fpnlFuncEconemy.updateUI();
		}
	}

	public void selectionChangeed(CellSelection cs, CellElement cell,
			CellFieldInfo cf) {
		curField = cf;
		refreshData();
	}

	private int getNodeState(MyTreeNode aNode) {
		return ((MyPfNode) aNode.getUserObject()).getSelectStat();
	}

	/**
	 * �õ�ѡ�еĽڵ����
	 * 
	 * @param customTree
	 * @return
	 */
	private List getIncomeList(CustomTree customTree) {
		List lstResult = new ArrayList();
		// �õ����ڵ���Ϣ
		MyTreeNode myTreeNode = (MyTreeNode) customTree.getRoot();
		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();

		// ���ݸ��ڵ���Ϣ����ȫѡ��ȫ��δѡ���
		// if (myPfNode.getSelectStat() == MyPfNode.SELECT
		// || myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
		// return null;
		// }

		// ���ȫѡ���õ�һ���
		if (myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}
		GetSelectNode(myTreeNode, lstResult);
		return lstResult;
	}

	private List getIncomeListByLeaf(CustomTree customTree) {
		List lstResult = new ArrayList();
		// �õ����ڵ���Ϣ
		MyTreeNode myTreeNode = (MyTreeNode) customTree.getRoot();
		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();

		// ���ȫѡ���õ�һ���
		if (myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}
		GetSelectNodeByLeaf(customTree, lstResult);
		return lstResult;
	}

	/**
	 * ʹ�õݹ飬����һ�ڵ�õ�ѡ���ӽڵ�ı���
	 * 
	 * @param myTreeNode
	 * @param lstResult
	 */
	private void GetSelectNode(MyTreeNode myTreeNode, List lstResult) {
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		int iCount = myTreeNode.getChildCount();
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				lstResult.add(myTreeNodeTmp.sortKeyValue());
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				GetSelectNode(myTreeNodeTmp, lstResult);
			}
		}
	}

	/**
	 * ʹ�õݹ飬����һ�ڵ�õ�ѡ���ӽڵ�ı���
	 * 
	 * @param myTreeNode
	 * @param lstResult
	 */
	private void GetSelectNodeByLeaf(CustomTree customTree, List lstResult) {
		MyTreeNode myTreeNodeTmp;
		MyTreeNode[] nodes = customTree.getSelectedNodes(true);
		if (nodes == null)
			return;
		int iCount = nodes.length;
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = nodes[i];
			lstResult.add(myTreeNodeTmp.sortKeyValue());
			continue;
		}
	}

	private String listToString(List lstData) {
		if (lstData == null)
			return "";
		int iCount = lstData.size();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iCount; i++) {
			sb.append((String) lstData.get(i)).append(";");

		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * ����ָ���ڵ��ѡ��״̬��
	 * 
	 * @param customTree
	 * @param key
	 *            dataset�еļ�
	 * @param value
	 *            ����Ӧ��ֵ
	 * @throws Exception
	 */
	public static void setChecked(CustomTree customTree, String key,
			String value) throws Exception {
		SetSelectTree.setIsNoCheck(customTree);
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		MyTreeNode node = null;
		String lastBookMark = customTree.getDataSet().toogleBookmark();
		DataSet ds = customTree.getDataSet();
		String bookMark;
		while (enuma.hasMoreElements()) {
			node = (MyTreeNode) enuma.nextElement();
			if (node == root) {
				continue;
			}
			bookMark = node.getBookmark();
			ds.gotoBookmark(bookMark);
			if (ds.fieldByName(key).getString().equals(value)) {
				((MyPfNode) node.getUserObject()).setIsSelect(true);
				customTree.updateUI();
				ds.gotoBookmark(lastBookMark);
				return;
			}
		}

	}

	public static void setChecked(CustomTree customTree, String key,
			String[] value) throws Exception {
		SetSelectTree.setIsNoCheck(customTree);
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.breadthFirstEnumeration();
		MyTreeNode node = null;
		String lastBookMark = customTree.getDataSet().toogleBookmark();
		DataSet ds = customTree.getDataSet();
		String bookMark;
		int count = value.length;

		while (enuma.hasMoreElements()) {
			node = (MyTreeNode) enuma.nextElement();
			if (node == root) {
				continue;
			}
			bookMark = node.getBookmark();
			ds.gotoBookmark(bookMark);
			for (int i = 0; i < count; i++) {
				if (ds.fieldByName(key).getString().equals(value[i])) {
					((MyPfNode) node.getUserObject()).setIsSelect(true);
					break;
				}
			}
		}
		ds.gotoBookmark(lastBookMark);
		customTree.updateUI();
		return;

	}

	public void refreshData() {
		freshDataMain();

		payOutPanel.freshData();
		gsPanel.freshData();
		sqlPanel.freshData();
		incomingPanel.freshData();
	}

	private void freshDataMain() {
		maskChange = true;
		clearData();

		if (curField != null) {
			frdoSource.setValue(String.valueOf(curField.getSourceType()));

		}
		sourceValueChanged();
		if (curField != null) {// ���ǵ�˳��Ӱ����ʾ
			if (!Common.isNullStr(curField.getAcctJJ()))
				frdoType.setValue("2");
			else if (!Common.isNullStr(curField.getAcctCode()))
				frdoType.setValue("1");
		}
		maskChange = false;
	}

	private void clearData() {
		frdoSource.setValue("-2");
		frdoType.setValue("-2");
	}
}
