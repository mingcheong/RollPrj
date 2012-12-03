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
 * Title:���鱨����,ѡ������Դ��ö�ٹ�ϵ���á�����Դ�������ÿͷ���ҳ��
 * </p>
 * <p>
 * Description:���鱨����,ѡ������Դ��ö�ٹ�ϵ���á�����Դ�������ÿͷ���ҳ��
 * </p>

 */
public class DataSourceSet extends FDialog {

	private static final long serialVersionUID = 1L;

	private ReportQuerySource querySource = null;

	private ReportGuideUI reportGuideUI = null;

	// ����Դ��Ͽ�
	private FComboBox dataSourceCbx = null;

	// �ֶ�������Ͽ�
	private FComboBox cbxFieldName = null;

	// �Ƚ�����
	private FComboBox cbxCompare = null;

	// ����ֵ
	// private FTextField txtWhereValue = null;
	private FComboBox cbxWhereValue = null;

	// ������������
	private FRadioGroup frdoType = null;

	// ������
	private MyTree treWhere = null;

	private List lstdsDictionary = null;

	// ����ö�ٶ�Ӧ����������Ϣ
	private Map mapWhereValue;

	// ԭѡ��ö��ID
	private String oldEnumID;

	// �Ƿ��ֶ�����־
	private Map mapFieldFlag;

	/**
	 * ���캯��
	 * 
	 * @param definReportServ
	 *            ���ݿ�ӿ�
	 */
	public DataSourceSet(ReportGuideUI reportGuideUI) {
		super(reportGuideUI);
		this.setSize(700, 525);
		this.setTitle("������������");
		this.setModal(true);
		this.reportGuideUI = reportGuideUI;
		this.querySource = reportGuideUI.querySource;

		this.reportGuideUI = reportGuideUI;

		jbInit();

	}

	/**
	 * �����ʼ������
	 * 
	 */
	private void jbInit() {

		// ���屨�����Զ�ҳ�����
		FTabbedPane ftabPnlReportSet = new FTabbedPane();

		// ��������Դ��ö������Դ���������������
		DatasWhereSetPnl datasWhereSetPnl = new DatasWhereSetPnl();
		ftabPnlReportSet.addControl("������������", datasWhereSetPnl);

		BtnPanel btnPanel = new BtnPanel();

		// �����������
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
	 * ��������Դ��ö������Դ���������������
	 */
	private class DatasWhereSetPnl extends FTitledPanel {

		private static final long serialVersionUID = 1L;

		public DatasWhereSetPnl() {
			this.setTitle("����Դ������������");
			this.setTopInset(5);
			this.setLeftInset(5);
			this.setRightInset(5);

			// ����Դ��Ͽ�
			dataSourceCbx = new MyFComboBox("����Դ:", reportGuideUI.querySource);
			((JComboBox) dataSourceCbx.getComponent(1))
					.addItemListener(new SelectItemListener());

			// �ֶ���
			FLabel fieldNameLbl = new FLabel();
			fieldNameLbl.setTitle("�ֶ���:");
			// ������ѡ��
			FLabel operateLbl = new FLabel();
			operateLbl.setText("����:");
			// ����ֵ
			FLabel whereValueLbl = new FLabel();
			whereValueLbl.setText("ֵ:");

			// �ֶ�������Ͽ�
			cbxFieldName = new FComboBox();
			cbxFieldName.setTitleVisible(false);

			((JComboBox) cbxFieldName.getComponent(1))
					.addItemListener(new ItemListener() {

						public void itemStateChanged(ItemEvent arg0) {
							String sFieldTyp = getFieldType();
							// �ж��ֶ����ͣ��������ֵ�Ͱ����Ͳ���������ʾ
							if (DefinePub.checkCharVal(sFieldTyp)) {
								cbxCompare
										.setRefModel(CompareType.COMPARE_TYPE);
							} else {
								cbxCompare
										.setRefModel(CompareType.COMPARE_TYPE_NUM);
							}
							cbxCompare.setSelectedIndex(-1);
							// �õ�������Ϣѡ���е�ֵ�����ݱ���id��enameֵ
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
									new MessageBox(DataSourceSet.this, "������Ϣ��"
											+ e.getMessage(), MessageBox.ERROR,
											MessageBox.BUTTON_OK).show();
								}
								cbxWhereValue.setEditable(true);

								// �����ֶ�ֵ��Ϣ
								Vector vector = new Vector();
								String[] displayValues = null;
								mapFieldFlag = new HashMap();

								// �ֶ�ֵ����
								displayValues = setRefValue(lstInputValue,
										IDefineReport.CODE, IDefineReport.NAME,
										vector, displayValues, mapFieldFlag,
										false);

								// ����Դ�ֶ�
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
			// �Ƚ�����
			cbxCompare = new FComboBox();
			cbxCompare.setTitleVisible(false);
			cbxCompare.setRefModel(CompareType.COMPARE_TYPE);
			cbxCompare.setSelectedIndex(-1);

			// ����ֵ
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

			// �����������
			FPanel orderPnl = new FPanel();
			orderPnl.setLayout(new RowPreferedLayout(5));

			FLabel flblEmpty = new FLabel();
			// ������Դ
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
			// �����
			orderPnl.addControl(cbxFieldName, new TableConstraints(1, 2, false,
					true));
			orderPnl.addControl(cbxCompare, new TableConstraints(1, 1, false,
					true));
			orderPnl.addControl(fpnlWhere, new TableConstraints(1, 2, false,
					true));

			// ������������
			frdoType = new FRadioGroup("", FRadioGroup.HORIZON);
			frdoType.setRefModel("and#����(AND)+or#����(OR)");
			frdoType.setTitleVisible(false);
			frdoType.setValue("and");

			// ���ӡ��޸İ�ť
			FButton addBtn = new FButton("addBtn", " ���� ");
			addBtn.addActionListener(new AddActionListener());

			FButton modifyBtn = new FButton("addBtn", " �޸� ");
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

			// ����������
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("��������");
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

					// ����Դ��Ͽ�
					dataSourceCbx.setValue(conditionNodeObj.getSourceID());
					// �ֶ�������Ͽ�
					cbxFieldName.setValue(conditionNodeObj.getSourceColID());
					// �Ƚ�����
					cbxCompare.setValue(conditionNodeObj.getCompareType());

					// ����sFieldEname�����ֶ�����
					String sFieldTyp = getFieldType();

					// �ж��ǲ����ֶαȽ�
					if (conditionNodeObj.isFieldFlag()) {
						cbxWhereValue.setValue(conditionNodeObj.getParaValue());
					} else {
						// �����ֶ������жϲ����Ӳ�������(')
						// ����ֵ
						if (DefinePub.checkCharVal(sFieldTyp)) {

							String paraValue = conditionNodeObj.getParaValue()
									.toString();
							// �ж��ǲ���in��not in�ȽϷ�
							if (CompareType.IN_TYPE
									.equalsIgnoreCase(conditionNodeObj
											.getCompareType())
									|| CompareType.NOTIN_TYPE
											.equalsIgnoreCase(conditionNodeObj
													.getCompareType())) {
								// ȥ����������
								paraValue = paraValue.substring(1, paraValue
										.length() - 1);
								// ȥ�������Ե�����
								paraValue = paraValue.replaceAll("','", ",");
							}
							// ȥ�����������
							cbxWhereValue.setValue(paraValue.substring(1,
									paraValue.length() - 1));

						} else {
							cbxWhereValue.setValue(conditionNodeObj
									.getParaValue());
						}
					}

					// ������������
					if (!Common.isNullStr(conditionNodeObj.joinBefore))
						frdoType.setValue(conditionNodeObj.joinBefore);

				}
			});

			JScrollPane fpnltalRela = new JScrollPane(treWhere);

			// ɾ������������ť
			FButton delBtn = new FButton("delBtn", " ɾ�� ");
			delBtn.addActionListener(new DelActionListener());
			// ���ư�ť
			FButton upBtn = new FButton("upBtn", " ���� ");
			upBtn.addActionListener(new UpActionListener());
			// ���ư�ť
			FButton downBtn = new FButton("downBtn", " ���� ");
			downBtn.addActionListener(new DownActionListener());
			// �����Ű�ť
			FButton lParenthesisBtn = new FButton("downBtn", "������");
			lParenthesisBtn.addActionListener(new LParenthesisActionListener());
			// �����Ű�ť
			FButton rParenthesisBtn = new FButton("downBtn", "������");
			rParenthesisBtn.addActionListener(new RParenthesisActionListener());
			// ɾ���Ű�ť
			FButton delParenthesisBtn = new FButton("downBtn", "ɾ����");
			delParenthesisBtn
					.addActionListener(new DelParenthesisActionListener());
			// ���尴ť���
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

			// �ڽ�����ʾ����
			showParameterArray();
		}
	}

	/**
	 * ���尴ť��
	 */
	private class BtnPanel extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		public BtnPanel() {
			// ���ÿ�����ʾ
			this.setAlignment(FlowLayout.RIGHT);

			// ���塰ȷ������ť
			FButton okBtn = new FButton("saveBtn", "����");
			okBtn.addActionListener(new SaveActionListener());
			// ���塱ȡ������ť
			FButton cancelBtn = new FButton("cancelBtn", "ȡ ��");
			// ʵ�֡�ȡ������ť����¼�
			cancelBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					DataSourceSet.this.setVisible(false);
				}

			});

			// ��ȷ������ť���밴ť���
			this.addControl(okBtn);
			// ��ȡ������ť���밴ť���
			this.addControl(cancelBtn);
		}
	}

	/**
	 * ����Դֵ�ı��¼�
	 */
	private class SelectItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {

			String sSourceID = ((FComboBoxItem) arg0.getItem()).getValue()
					.toString();
			lstdsDictionary = DefineReportI.getMethod().getFieldWithEname(
					sSourceID);

			// �ֶ���
			ColWhereSetPnl.addRefValue(cbxFieldName, lstdsDictionary,
					IDefineReport.FIELD_ENAME, IDefineReport.FIELD_FNAME);

			cbxFieldName.setSelectedIndex(-1);
		}
	}

	/**
	 * ���水ť����¼�
	 */
	private class SaveActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String sErr = checkFilter();
			if (!Common.isNullStr(sErr)) {
				JOptionPane.showMessageDialog(DataSourceSet.this, sErr, "��ʾ",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// ��������Դ��������
			saveParameterArray();
			DataSourceSet.this.setVisible(false);
		}
	}

	/**
	 * ����ǰ���
	 * 
	 * @return
	 */
	private String checkFilter() {
		// ���ÿ������Դ����С�����Ƿ�ƥ��
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
			// �����ڵ�
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
				sErr = sErr + rootNode.getChildAt(i).toString() + "С����������ƥ�䡣\n";

		}

		return sErr;

	}

	/**
	 * ��������Դ��������
	 * 
	 */
	private void saveParameterArray() {
		// ����summaryParameterImpl����
		SummaryParameterImpl[] summaryParameterImpl = null;

		// �õ���ǰ����
		IParameter iParameter[] = querySource.getParameterArray();
		String chName = null;
		// ѭ���õ�Ĭ�ϲ���,����summaryParameterImpl����
		for (int i = 0; i < iParameter.length; i++) {
			// �õ�chName����
			chName = iParameter[i].getChName();
			// �ж��Ƿ���Ĭ�ϲ���
			if (checkDefaultPara(chName)) {
				summaryParameterImpl = ReportGuideUI.addArrayLength(
						summaryParameterImpl, 1);
				summaryParameterImpl[summaryParameterImpl.length - 1] = (SummaryParameterImpl) iParameter[i];
			}
		}

		// �õ���ʼ����
		int iStartCount = summaryParameterImpl.length;
		// �õ���Ҷ�ڵ�
		List lstNode = treWhere.getIsLeafNode();
		// �õ��ڵ���
		int iCount = lstNode.size();
		// ����ڵ����
		ConditionNodeObj conditionNodeObj = null;
		// �������õ�����Դ����
		ToSource toSource[] = null;
		// summaryParameterImpl����չiCount����
		summaryParameterImpl = ReportGuideUI.addArrayLength(
				summaryParameterImpl, iCount);

		for (int i = 0; i < iCount; i++) {
			// ��ʼ������
			summaryParameterImpl[i + iStartCount] = new MySummaryParameterImpl();
			// �õ��ڵ����
			conditionNodeObj = (ConditionNodeObj) lstNode.get(i);
			// id
			summaryParameterImpl[i + iStartCount].setName(DefinePub
					.getRandomUUID());
			// chName
			summaryParameterImpl[i + iStartCount].setChName(conditionNodeObj
					.getChName());
			// compareType�Ƚ�����
			summaryParameterImpl[i + iStartCount]
					.setCompareType(conditionNodeObj.getCompareType());
			// ����ֵ
			summaryParameterImpl[i + iStartCount].setValue(conditionNodeObj
					.getParaValue());

			summaryParameterImpl[i + iStartCount]
					.setJoinBefore(conditionNodeObj.joinBefore);

			if (summaryParameterImpl[i + iStartCount] instanceof MySummaryParameterImpl) {
				((MySummaryParameterImpl) summaryParameterImpl[i + iStartCount])
						.setLParenthesis(conditionNodeObj.getLParenthesis());
				((MySummaryParameterImpl) summaryParameterImpl[i + iStartCount])
						.setRParenthesis(conditionNodeObj.getRParenthesis());
				// �ǲ����ֶαȽϱ�־
				((MySummaryParameterImpl) summaryParameterImpl[i + iStartCount])
						.setIsFilterFlag(conditionNodeObj.isFieldFlag() ? "1"
								: "0");
			}

			// ����ToSource����
			toSource = new ToSource[1];
			toSource[0] = new ToSource();
			// soureceId ����ԴID
			toSource[0].setSourceID(conditionNodeObj.getSourceID());
			// ��ID
			toSource[0].setSourceColID(conditionNodeObj.getSourceColID());
			// �Ƿ�������ö��
			toSource[0].setToEnumSource(false);
			summaryParameterImpl[i + iStartCount].setToSourceArray(toSource);
		}
		querySource.setParameterArray(summaryParameterImpl);
	}

	/**
	 * �ڽ�����ʾ����
	 * 
	 */
	private void showParameterArray() {
		// �õ���ǰ����
		IParameter iParameter[] = querySource.getParameterArray();
		if (iParameter == null)
			return;
		// �õ���������
		int iCount = iParameter.length;
		// ����chName��������
		String chName = null;
		// ����ConditionNodeObj����
		ConditionNodeObj conditionNodeObj = null;
		// ѭ��������ʾ����
		for (int i = 0; i < iCount; i++) {
			// �õ�chName����
			chName = iParameter[i].getChName();
			// �ж��Ƿ���Ĭ�ϲ���,����Ĭ�ϲ��������д���
			if (checkDefaultPara(chName)) {
				continue;
			}

			// ����ConditionNodeObj����
			conditionNodeObj = new ConditionNodeObj();
			// ID
			conditionNodeObj.setID(iParameter[i].getName());
			// ����
			conditionNodeObj.setChName(iParameter[i].getChName());
			// �Ƚ�����
			conditionNodeObj.setCompareType(iParameter[i].getCompareType());
			// ����ֵ
			conditionNodeObj.setParaValue(iParameter[i].getValue());
			// ��������ԴID
			conditionNodeObj.setSourceID(iParameter[i].getToSourceArray()[0]
					.getSourceID());
			// ��������Դ�ֶ�ID
			conditionNodeObj.setSourceColID(iParameter[i].getToSourceArray()[0]
					.getSourceColID());
			// and �� or
			conditionNodeObj.setJoinBefore(iParameter[i].getJoinBefore());
			// ������,������
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

			// ���������������ӽڵ�
			addParaTreNode(conditionNodeObj);
		}
		// �ڵ�չ��
		treWhere.expandTree();
	}

	/**
	 * ���Ӱ�ť����¼�
	 */
	private class AddActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// �����Ϣ�Ƿ���д����
			if (!checkInputInfo())
				return;

			// �����������
			ConditionNodeObj conditionNodeObj = new ConditionNodeObj();
			// ID
			conditionNodeObj.ID = DefinePub.getRandomUUID();
			// ��ý�����Ϣ�����ö���ֵ
			setObjectValue(conditionNodeObj);
			// ���������������ӽڵ�
			TreeNode node = addParaTreNode(conditionNodeObj);
			// ˢ�½ڵ���������
			refreshNodeChName(node);
			// ��λ�������ӵĽڵ�
			treWhere.expendTo(node);
		}
	}

	/**
	 * ���������������ӽڵ�
	 * 
	 * @param conditionNodeObj
	 */
	private TreeNode addParaTreNode(ConditionNodeObj conditionNodeObj) {
		// �õ����ڵ�
		DefaultTreeModel treeModel = (DefaultTreeModel) treWhere.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();
		// �ж����ӵĸ��ڵ��Ƿ��Ѵ���,Ĭ�ϸ��ڵ㲻����
		boolean sFlag = false;
		ConditionNodeObj curconditionNodeObj = null;
		DefaultMutableTreeNode curNode = null;
		int iChildCount = root.getChildCount();
		// �õ����ڵ��µ��ӽڵ�
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
		if (!sFlag) {// ���ڵ㲻����
			// ���Ӹ��ڵ�
			ConditionNodeObj myParNodeObject = new ConditionNodeObj();
			myParNodeObject.setSourceID(conditionNodeObj.sourceID);
			// ����Դ����
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
	 * �޸İ�ť����¼�
	 */
	private class ModifyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// �����Ϣ�Ƿ���д����
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
			// ˢ�½ڵ���������
			refreshNodeChName(defaultMutableTreeNode);
			// ˢ�½ڵ�
			((DefaultTreeModel) treWhere.getModel())
					.nodeChanged(defaultMutableTreeNode);
		}
	}

	/**
	 * ��ý�����Ϣ�����ö���ֵ
	 * 
	 * @param conditionNodeObj
	 */
	private void setObjectValue(ConditionNodeObj conditionNodeObj) {
		// �Ƚ�����
		conditionNodeObj.compareType = cbxCompare.getValue().toString();
		// ����ԴID
		conditionNodeObj.sourceID = dataSourceCbx.getValue().toString();
		// ����Դ��ID
		conditionNodeObj.sourceColID = cbxFieldName.getValue().toString();

		// ����ֵ
		conditionNodeObj.paraValue = cbxWhereValue.getValue();

		// �Ƿ��ֶαȽϱ��
		conditionNodeObj.fieldFlag = this.isFieldFlag();

		// �Ƿ��ֶαȽ�
		if (!conditionNodeObj.fieldFlag) {
			// ����sFieldEname�����ֶ�����
			String sFieldTyp = getFieldType();
			// �����ֶ������жϲ����Ӳ�������(')
			if (DefinePub.checkCharVal(sFieldTyp)) {
				conditionNodeObj.paraValue = "'" + conditionNodeObj.paraValue
						+ "'";
				// �ж��ǲ���in��not in�ȽϷ�
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
			// �ֶ���������
			conditionNodeObj.setFieldFname(cbxWhereValue.getText());
		}

		// �������� and��or
		conditionNodeObj.joinBefore = frdoType.getValue().toString();
		// ����
		conditionNodeObj.chName = getChName(conditionNodeObj);
	}

	/**
	 * �õ��������������Ϣ
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
		// ����
		if (!isFirstPara) {
			if ("and".equals(conditionNodeObj.joinBefore)) {
				value = "���� ";
			} else {
				value = "���� ";
			}
		}

		if (conditionNodeObj.getLParenthesis() != null) {
			value = value + conditionNodeObj.getLParenthesis();
		}
		// �Ƿ��ֶαȽ�
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
	 * ����sFieldEname�����ֶ�����
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
	 * ɾ����ť����¼�
	 */
	private class DelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (!curTreeNode.isLeaf())
				return;

			// �õ�������ڵ�
			DefaultMutableTreeNode parNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			// �õ���������ӽڵ���
			int iChildCount = parNode.getChildCount();

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			model.removeNodeFromParent(curTreeNode);
			// ����һ���ӽڵ㣬�ӽڵ�ɾ��ʱ��ɾ�����ڵ�
			if (iChildCount == 1) {
				model.removeNodeFromParent(parNode);
			}
		}
	}

	/**
	 * ���ư�ť����¼�
	 */
	private class UpActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null) {
				new MessageBox(reportGuideUI, "��ѡ�����ڵ�!", MessageBox.MESSAGE,
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
			// ˢ�½ڵ���������
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
	 * ���ư�ť����¼�
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();

			if (curTreeNode == null) {
				new MessageBox(reportGuideUI, "��ѡ�����ڵ�!", MessageBox.MESSAGE,
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
			// ˢ�½ڵ���������
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
	 * ���ӻ��޸�ʱ�����Ϣ�Ƿ���д����
	 * 
	 * @return
	 */
	private boolean checkInputInfo() {
		// �ֶ�������Ͽ�
		if (cbxFieldName.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(DataSourceSet.this, "��ѡ������Դ�ֶΣ�",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �Ƚ�����
		if (cbxCompare.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(DataSourceSet.this, "��ѡ��Ƚ����ͣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// ����ֵ
		if ("".equals(cbxWhereValue.getValue().toString())) {
			JOptionPane.showMessageDialog(DataSourceSet.this, "����д����ֵ��", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж��ǲ����ֶαȽ�
		if (this.isFieldFlag()) {
			if (CompareType.COMPARE_TYPE_NUM.indexOf(cbxCompare.getText()
					.toString()) == -1) {
				JOptionPane.showMessageDialog(DataSourceSet.this,
						"�ֶκ��ֶαȽ�ʱ������ʹ��\"" + cbxCompare.getText()
								+ "\"�������ͣ���ѡ���������ͣ�", "��ʾ",
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
	 * �ж��Ƿ���Ĭ�ϲ���
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
	 * ˢ�½ڵ���������
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
	 * �����Ű�ť����¼�
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
			// ˢ�½ڵ���������
			refreshNodeChName(curTreeNode);
			// ˢ�½ڵ�
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * �����Ű�ť����¼�
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
			// ˢ�½ڵ���������
			refreshNodeChName(curTreeNode);
			// ˢ�½ڵ�
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * ɾ���Ű�ť����¼�
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
			// ˢ�½ڵ���������
			refreshNodeChName(curTreeNode);
			// ˢ�½ڵ�
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);

		}
	}

	private class ConditionNodeObj {

		// ID
		String ID;

		// ����
		String chName;

		// �Ƚ�����
		String compareType;

		// ����ֵ
		Object paraValue;

		// ��������ԴID
		String sourceID;

		// ��������Դ�ֶ�ID
		String sourceColID;

		// and �� or
		String joinBefore;

		// ������
		String lParenthesis = "";

		// ������
		String rParenthesis = "";

		// ���ֶ����ֶαȽϣ�����ֵ�Ƚ� ,true����ʾ�ֶΣ�false:��ʾֵ
		boolean fieldFlag = false;

		// �ֶαȽ�ʱ�����ֶ���
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
	 * ��ʾ����ֵ��
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
				new MessageBox(DataSourceSet.this, "��ѡ������Դ���ֶ���!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				return;
			}

			// �õ��ǲ���ö�ٱ���
			boolean isEnumCode = DefinePub.judgetEnumWithColID(querySource,
					sourceID, sourceColID, true);

			DataSourceWhereDlg dataSourceWhereDlg = null;
			try {
				// �ж��Ƿ�ö�ٱ���
				if (isEnumCode) {
					// �õ�ö��IDֵ
					String sEnumId = DefinePub.getEnumIDWithColID(querySource,
							sourceID, sourceColID);
					// �ж��Ƿ�ԭѡ��ö����
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

				// ��ʾ����ֵѡ��Ի���
				dataSourceWhereDlg = new DataSourceWhereDlg(DataSourceSet.this,
						mapWhereValue, cbxWhereValue.getValue().toString());
				Tools.centerWindow(dataSourceWhereDlg);
				dataSourceWhereDlg.setVisible(true);

				// �õ�ѡ���ֵ
				String whereValue = dataSourceWhereDlg.getResultValue();
				if (whereValue != null) {
					cbxWhereValue.setValue(whereValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(DataSourceSet.this,
						"��ʾ�Ի�����ִ���,������Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				if (dataSourceWhereDlg != null)
					dataSourceWhereDlg.dispose();
			}
		}
	}

	/**
	 * �����б���ֶ�������֯���
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
