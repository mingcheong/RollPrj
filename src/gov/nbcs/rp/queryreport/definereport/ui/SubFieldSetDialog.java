/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomStatisticCaliber;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.iface.cell.IStatisticCaliber;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title: �������ÿͷ��˽���
 * </p>
 * <p>
 * Description: �������ÿͷ��˽���
 * </p>

 */
public class SubFieldSetDialog extends FDialog {

	private static final long serialVersionUID = 1L;

	private ReportGuideUI reportGuideUI = null;

	// ����SummaryReport����
	// private SummaryReport summaryReport = null;

	private ReportQuerySource querySource = null;

	// ����ö��Tabel
	private EnumTable ftabEnum = null;

	// ������������ı���
	private FTextField ftxtSubField1 = null;

	private FTextField ftxtSubField2 = null;

	// �����Ƿ�����ϴ�����ѡ���
	private FCheckBox fchkSubField1 = null;

	private FCheckBox fchkSubField2 = null;

	// �������ݿ�ӿ�
	private IDefineReport definReportServ = null;

	// ����Դ��1
	private CustomTree enumATree = null;

	// ����Դ��2
	private CustomTree enumBTree = null;

	// ����ѡ��ڴ����ֿ�
	private IntegerSpinner jspSubField1 = null;

	private IntegerSpinner jspSubField2 = null;

	// �����в�����ֿ�
	private IntegerSpinner jspRow = null;

	// ���ѡ�е�ö��Դ
	private List enumInfoMap = null;

	private SummaryReportPane summaryReportPane = null;

	private GroupReport groupReport = null;

	// �����ϴ����ӵ���,����ȡ������ʹ��
	private int lastTimeAddCol = 0;

	public SubFieldSetDialog(ReportGuideUI reportGuideUI) {
		this.setModal(true);
		this.setSize(700, 525);
		// this.setExtendedState(JFrame.NORMAL);
		this.setTitle("��������");
		this.reportGuideUI = reportGuideUI;
		this.querySource = reportGuideUI.querySource;
		this.definReportServ = reportGuideUI.definReportServ;
		this.summaryReportPane = reportGuideUI.fpnlDefineReport.designGroupReportPane;
		this.groupReport = reportGuideUI.fpnlDefineReport.groupReport;

		// stayOnTop();
		enumInfoMap = new ArrayList();
		// ��ʼ������
		init();
		// ���ؽ�������
		moduleActive();
	}

	/**
	 * ��ʼ������
	 * 
	 */
	private void init() {
		// ����ö��Tabel
		ftabEnum = new EnumTable();

		// ����ѡ��ť
		FButton fbtnChoice = new FButton("", "ѡ��>");
		fbtnChoice.addActionListener(new ChoiceActionListener());
		// ���������ť
		FButton fbtnClear = new FButton("", "���<");
		fbtnClear.addActionListener(new ClearActionListener());
		// ����ȡ��������ť
		FButton fbtnSetWhere = new FButton("", "��������");
		fbtnSetWhere.addActionListener(new SetActionListener());
		FLabel flblEmpty1 = new FLabel();
		FLabel flblEmpty2 = new FLabel();
		FLabel flblEmpty3 = new FLabel();
		// ���尴ť���
		FPanel btnPanel = new FPanel();
		btnPanel.setLayout(new RowPreferedLayout(1));
		btnPanel.addControl(flblEmpty1, new TableConstraints(1, 1, true, true));
		btnPanel
				.addControl(fbtnChoice, new TableConstraints(1, 1, false, true));
		btnPanel.addControl(fbtnClear, new TableConstraints(1, 1, false, true));

		btnPanel.addControl(flblEmpty2, new TableConstraints(1, 1, true, true));
		btnPanel.addControl(fbtnSetWhere, new TableConstraints(1, 1, false,
				true));
		btnPanel
				.addControl(flblEmpty3, new TableConstraints(1, 1, false, true));

		// ��������
		try {
			enumATree = new CustomTree("����1", null, "code", "name", null, null,
					"code", true);
			enumATree.setIsCheckBoxEnabled(true);
			enumBTree = new CustomTree("����2", null, "code", "name", null, null,
					"code", true);
			enumBTree.setIsCheckBoxEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		FScrollPane fspnlEnumA = new FScrollPane(enumATree);
		FScrollPane fspnlEnumB = new FScrollPane(enumBTree);

		FSplitPane fSplitPaneTre = new FSplitPane();
		fSplitPaneTre.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		fSplitPaneTre.setDividerLocation(180);
		fSplitPaneTre.addControl(fspnlEnumA);
		fSplitPaneTre.addControl(fspnlEnumB);

		FPanel fpnlEnum = new FPanel();
		RowPreferedLayout rLayEnum = new RowPreferedLayout(5);
		rLayEnum.setColumnWidth(82);
		rLayEnum.setColumnGap(1);
		fpnlEnum.setLayout(rLayEnum);
		fpnlEnum.addControl(ftabEnum, new TableConstraints(1, 3, true, false));
		fpnlEnum.addControl(btnPanel, new TableConstraints(1, 1, true, false));
		fpnlEnum.addControl(fSplitPaneTre, new TableConstraints(1, 1, true,
				true));

		// ��������ı���
		ftxtSubField1 = new FTextField("��:");
		ftxtSubField1.setEditable(false);
		ftxtSubField1.setProportion(0.1f);
		ftxtSubField2 = new FTextField("��:");
		ftxtSubField2.setEditable(false);
		ftxtSubField2.setProportion(0.1f);

		FLabel flblSubField1 = new FLabel();
		flblSubField1.setText("��Ҫϸ����");
		FLabel flblSubField2 = new FLabel();
		flblSubField2.setText("��Ҫϸ����");

		// ����ѡ��ڴ����ֿ�
		SpinnerModel modelSubField1 = new SpinnerNumberModel(1, 1, 100, 1);
		jspSubField1 = new IntegerSpinner(modelSubField1);
		SpinnerModel modelSubField2 = new SpinnerNumberModel(1, 1, 100, 1);
		jspSubField2 = new IntegerSpinner(modelSubField2);

		FLabel flblSubField11 = new FLabel();
		flblSubField11.setText("����");
		FLabel flblSubField22 = new FLabel();
		flblSubField22.setText("����");

		FFlowLayoutPanel fpnlSubField11 = new FFlowLayoutPanel();
		fpnlSubField11.setAlignment(FlowLayout.LEFT);
		fpnlSubField11.addControl(flblSubField1);
		fpnlSubField11.add(jspSubField1);
		fpnlSubField11.addControl(flblSubField11);

		FFlowLayoutPanel fpnlSubField12 = new FFlowLayoutPanel();
		fpnlSubField12.setAlignment(FlowLayout.LEFT);
		fpnlSubField12.addControl(flblSubField2);
		fpnlSubField12.add(jspSubField2);
		fpnlSubField12.addControl(flblSubField22);

		// �����Ƿ�����ϴ�����ѡ���
		fchkSubField1 = new FCheckBox("�����ϼ�");
		fchkSubField1.setTitlePosition("RIGHT");
		fchkSubField2 = new FCheckBox("�����ϼ�");
		fchkSubField2.setTitlePosition("RIGHT");

		FLabel flblRow = new FLabel();
		flblRow.setText("������ͷ������λ�ڵ�");
		// �����в�����ֿ�
		int row[] = DefinePub.getHeaderRow(groupReport);
		int headRowLast = 3;
		if (row != null) {
			headRowLast = row[row.length - 1] + 1;
		}
		SpinnerModel modelRow = new SpinnerNumberModel(headRowLast, 1, 100, 1);
		jspRow = new IntegerSpinner(modelRow);
		FLabel flblRow1 = new FLabel();
		flblRow1.setText("��");
		FFlowLayoutPanel fpnlRow = new FFlowLayoutPanel();
		fpnlRow.setAlignment(FlowLayout.LEFT);
		fpnlRow.addControl(flblRow);
		fpnlRow.add(jspRow);
		fpnlRow.addControl(flblRow1);

		FButton fbtnAutoSet = new FButton("", "�Զ�����");
		fbtnAutoSet.addActionListener(new AutoSetActionListener());
		FButton fbtnCancelSet = new FButton("", "ȡ���ϴ��Զ�����");
		fbtnCancelSet.addActionListener(new CanelSetActionListener());
		FFlowLayoutPanel fpnlBtn = new FFlowLayoutPanel();
		// ���尴ť���
		fpnlBtn.setAlignment(FlowLayout.LEFT);
		fpnlBtn.addControl(fbtnAutoSet);
		fpnlBtn.addControl(fbtnCancelSet);

		FPanel fpnlAutoSet = new FPanel();
		fpnlAutoSet.setTitle("�Զ���������:");
		RowPreferedLayout rLay = new RowPreferedLayout(3);
		rLay.setColumnWidth(240);
		rLay.setColumnGap(0);
		rLay.setRowHeight(23);
		fpnlAutoSet.setLayout(rLay);
		fpnlAutoSet.addControl(ftxtSubField1, new TableConstraints(1, 1, false,
				false));
		fpnlAutoSet.addControl(fpnlSubField11, new TableConstraints(1, 1,
				false, false));
		fpnlAutoSet.addControl(fchkSubField1, new TableConstraints(1, 1, false,
				true));
		fpnlAutoSet.addControl(ftxtSubField2, new TableConstraints(1, 1, false,
				false));
		fpnlAutoSet.addControl(fpnlSubField12, new TableConstraints(1, 1,
				false, true));
		fpnlAutoSet.addControl(fchkSubField2, new TableConstraints(1, 1, false,
				true));
		fpnlAutoSet
				.addControl(fpnlRow, new TableConstraints(1, 3, false, true));
		fpnlAutoSet.addControl(fpnlBtn, new TableConstraints(1, 3, true, true));

		ClosePanel closePanel = new ClosePanel();

		FPanel fpnlMain = new FPanel();
		fpnlMain.setLayout(new RowPreferedLayout(1));

		fpnlMain.addControl(fpnlEnum, new TableConstraints(1, 1, true, true));
		fpnlMain.addControl(fpnlAutoSet,
				new TableConstraints(6, 1, false, true));
		fpnlMain
				.addControl(closePanel, new TableConstraints(2, 1, false, true));

		this.getContentPane().add(fpnlMain);
	}

	/**
	 * ���ñ����
	 * 
	 * @param ftable�����õı��
	 */
	private class EnumTable extends FTable {

		private static final long serialVersionUID = 1L;

		public EnumTable() {
			super();
			this.setShowSumRow(false);
			init();
		}

		public EnumTable(boolean arg0) {
			super(arg0);
			init();
		}

		private void init() {
			String[][] columns = new String[][] {
					{ IDefineReport.DATASOURCE_NAME, "����Դ��", "100" },
					{ IDefineReport.FIELD_FNAME, "�ֶ���", "100" } };

			for (int i = 0; i < columns.length; i++) {
				String[] col = columns[i];
				FBaseTableColumn column = new FBaseTableColumn();
				column.setId(col[0]);
				column.setTitle(col[1]);
				if (col.length == 3) {
					column.setWidth(Integer.parseInt(col[2]));
					column.setPreferredWidth(Integer.parseInt(col[2]));
					column.setMaxWidth(500);
					column.setMinWidth(0);
				}
				this.addColumn(column);
			}
		}
	}

	/**
	 * ���ؽ�������
	 * 
	 */
	private void moduleActive() {
		// ��鵱ǰ��Ԫ�Ƿ���������������
		int col = summaryReportPane.getCellSelection().getColumn();
		int row = summaryReportPane.getCellSelection().getRow();
		Object value = groupReport.getCellValue(col, row);

		if (!(value instanceof MyCalculateValueImpl)) {
			return;
		}

		List lstSourceID = null;
		IMeasureAttr[] measureAttr = ((MyCalculateValueImpl) value)
				.getMeasureArray();
		int length = measureAttr.length;
		for (int i = 0; i < length; i++) {
			if (lstSourceID == null)
				lstSourceID = new ArrayList();
			lstSourceID.add(measureAttr[i].getSourceID());
		}

		List lstEnumWhereSet = DefinePub.showEnumWhereSetInfo(querySource,
				true, lstSourceID);
		ftabEnum.setData(lstEnumWhereSet);

		// �õ�����
		IStatisticCaliber[] statisticCaliberArray = ((MyCalculateValueImpl) value)
				.getStatisticCaliberArray();
		if (statisticCaliberArray == null)
			return;
		length = statisticCaliberArray.length;
		String sEnumId;
		String sNodeId;
		Map selectData;
		CustomTree curTree;
		for (int i = 0; i < length; i++) {
			// �ж��Ƿ��Ƿ������õ�����
			if (DefinePub.isPACaliber(statisticCaliberArray[i])) {
				if (statisticCaliberArray[i] instanceof ICustomStatisticCaliber) {
					sEnumId = ((ICustomStatisticCaliber) statisticCaliberArray[i])
							.getEnumID();
					sNodeId = ((ICustomStatisticCaliber) statisticCaliberArray[i])
							.getNodeID();
					selectData = getDataWithEnumId(sEnumId, lstEnumWhereSet);
					curTree = showEnumTree(sEnumId, selectData);
					if (curTree != null)
						setNodeSelect(sNodeId, curTree);
				}
			}
		}
	}

	/**
	 * ����ö��ID�õ�Mapֵ
	 * 
	 * @param sEnumId
	 * @param lstEnumWhereSet
	 * @return
	 */
	private Map getDataWithEnumId(String sEnumId, List lstEnumWhereSet) {
		if (sEnumId == null)
			return null;
		if (lstEnumWhereSet == null || lstEnumWhereSet.size() == 0)
			return null;
		String sEnumIdTmp;
		int size = lstEnumWhereSet.size();
		for (int i = 0; i < size; i++) {
			if (((Map) lstEnumWhereSet.get(i)).get(IDefineReport.ENUM_ID) == null) {
				continue;
			}
			sEnumIdTmp = ((Map) lstEnumWhereSet.get(i)).get(
					IDefineReport.ENUM_ID).toString();
			if (sEnumId.equals(sEnumIdTmp)) {
				return (Map) lstEnumWhereSet.get(i);
			}
		}
		return null;
	}

	/**
	 * �رհ�ť���
	 */
	private class ClosePanel extends FFlowLayoutPanel {
		private static final long serialVersionUID = 1L;

		public ClosePanel() {
			this.setAlignment(FlowLayout.RIGHT);

			FButton fbtnClose = new FButton("fbtnClose", "�ر�");
			fbtnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					SubFieldSetDialog.this.dispose();
				}
			});

			this.addControl(fbtnClose);
		}
	}

	/**
	 * ѡ��ö��Դ��ť����¼�
	 */
	private class ChoiceActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// �ж��Ƿ���ѡ������ö��
			if (enumInfoMap.size() == 2) {
				return;
			}

			// �õ���ǰѡ�е���
			int index = ftabEnum.getCurrentRowIndex();
			if (index < 0) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"��ѡ������У�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			Map selectData = ftabEnum.getDataByIndex(index);

			// �õ�ö��ID
			String sEnumId = selectData.get(IDefineReport.ENUM_ID).toString();

			// �ж�ö��Դ�Ƿ���ѡ��
			for (int i = 0; i < enumInfoMap.size(); i++) {
				if (((Map) enumInfoMap.get(i)).get(ISysRefCol.REFCOL_ID)
						.equals(sEnumId)) {
					JOptionPane.showMessageDialog(SubFieldSetDialog.this,
							"��ѡ��ķ�����Ϣ���ѱ�ѡ��,��ѡ�������У�", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}

			// ��ʾ����Ϣ
			showEnumTree(sEnumId, selectData);
		}
	}

	/**
	 * ��ʾ����Ϣ,����ö��ID
	 * 
	 * @param sEnumId
	 * @param selectData
	 */
	private CustomTree showEnumTree(String sEnumId, Map selectData) {
		if (Common.isNullStr(sEnumId))
			return null;
		String sEnumIdTmp = sEnumId.substring(IDefineReport.ENUM_.length());

		// ����enumId�õ���������Ϣ
		Map selectDataMap = null;
		try {
			selectDataMap = definReportServ.getEnumDataWithEnumID(sEnumIdTmp);

			if (selectDataMap == null) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"δ�ҵ���Ӧ��������", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			}
			selectDataMap.put(ISysRefCol.REFCOL_ID, sEnumIdTmp);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SubFieldSetDialog.this,
					"��ѯ���ݳ��ִ���,������Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
		if (selectDataMap == null) {
			return null;
		}

		selectDataMap.putAll(selectData);
		// �õ�����ID
		// selectDataMap.put(IDefineReport.SOURCE_ID, selectData
		// .get(IDefineReport.SOURCE_ID));
		// // ����Դ����
		// selectDataMap.put(IDefineReport.DATASOURCE_NAME, selectData
		// .get(IDefineReport.DATASOURCE_NAME));
		// // �õ�������
		// selectDataMap.put(IDefineReport.COL_ID, selectData
		// .get(IDefineReport.COL_ID));
		// // �õ�����������
		// selectDataMap.put(IDefineReport.FIELD_FNAME, selectData
		// .get(IDefineReport.FIELD_FNAME));

		String sLvlField = selectDataMap.get(ISysRefCol.LVL_FIELD).toString();
		// refcol_name
		String sRefcolName = selectDataMap.get(ISysRefCol.REFCOL_NAME)
				.toString();
		// lvl_style
		String sLvlStyle = selectDataMap.get(ISysRefCol.LVL_STYLE).toString();
		// ö��ԴDataSet
		DataSet dsEnum = (DataSet) selectDataMap.get(IDefineReport.ENUM_DATA);

		if (enumInfoMap.size() == 0) {
			// ���ø�����
			enumATree.setRootName(sRefcolName);
			// ����DataSet
			enumATree.setDataSet(dsEnum);
			// ����IdName
			enumATree.setIdName(sLvlField);
			// ���ñ������
			enumATree.setCodeRule(SysCodeRule.createClient(sLvlStyle));
			// ����SortKey
			enumATree.setSortKey(sLvlField);
			try {
				enumATree.reset();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"��ѯ���ݳ��ִ���,������Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			ftxtSubField1.setValue(sRefcolName);

			MyTreeNode root = (MyTreeNode) enumATree.getRoot();
			((SpinnerNumberModel) jspSubField1.getModel())
					.setMaximum(new Integer(root.getDepth()));
			enumInfoMap.add(selectDataMap);
			return enumATree;

		} else if (enumInfoMap.size() == 1) {
			// ���ø�����
			enumBTree.setRootName(sRefcolName);
			// ����DataSet
			enumBTree.setDataSet(dsEnum);
			// ����IdName
			enumBTree.setIdName(sLvlField);
			// ���ñ������
			enumBTree.setCodeRule(SysCodeRule.createClient(sLvlStyle));
			// ����SortKey
			enumBTree.setSortKey(sLvlField);
			try {
				enumBTree.reset();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"��ѯ���ݳ��ִ���,������Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			ftxtSubField2.setValue(sRefcolName);
			MyTreeNode root = (MyTreeNode) enumBTree.getRoot();
			((SpinnerNumberModel) jspSubField2.getModel())
					.setMaximum(new Integer(root.getDepth()));
			enumInfoMap.add(selectDataMap);
			return enumBTree;

		} else {
			return null;
		}

	}

	/**
	 * ���<��ť����¼�
	 */
	private class ClearActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// �ж�����Դ2�Ƿ�������
			if (enumInfoMap.size() == 2) {
				// ���ø�����
				enumBTree.setRootName("����2");
				// ����DataSet
				enumBTree.setDataSet(DataSet.create());
				// ����IdName
				enumBTree.setIdName(null);
				// ����TextName
				// customTreeB.setTextName(null);
				// ���ñ������
				enumBTree.setCodeRule(null);
				// ����SortKey
				enumBTree.setSortKey(null);
				try {
					enumBTree.reset();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SubFieldSetDialog.this,
							"��ѯ���ݳ��ִ���,������Ϣ��" + e.getMessage(), "��ʾ",
							JOptionPane.ERROR_MESSAGE);
				}
				ftxtSubField2.setValue("");
			} else if (enumInfoMap.size() == 1) {// �ж�����Դ1�Ƿ�������
				// ���ø�����
				enumATree.setRootName("����1");
				// ����DataSet
				enumATree.setDataSet(DataSet.create());
				// ����IdName
				enumATree.setIdName(null);
				// ����TextName
				// customTreeA.setTextName(null);
				// ���ñ������
				enumATree.setCodeRule(null);
				// ����SortKey
				enumATree.setSortKey(null);
				try {
					enumATree.reset();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SubFieldSetDialog.this,
							"��ѯ���ݳ��ִ���,������Ϣ��" + e.getMessage(), "��ʾ",
							JOptionPane.ERROR_MESSAGE);
				}
				ftxtSubField1.setValue("");
			}
			enumInfoMap.remove(enumInfoMap.size() - 1);
		}
	}

	/**
	 * ȡ����ť����¼�
	 */
	private class SetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// ��鵱ǰ��Ԫ�Ƿ���������������
			int col = summaryReportPane.getCellSelection().getColumn();
			int row = summaryReportPane.getCellSelection().getRow();
			if (!checkCell(col, row)) {
				return;
			}

			// ����ö��ԴA��B����֯�ɲ�ѯ����SQL������Ҫƴ������Դ������
			List lstAddSql = null;
			try {
				lstAddSql = getAddSqlValue();
				if (lstAddSql == null || lstAddSql.size() == 0) {
					new MessageBox(SubFieldSetDialog.this, "��ѡ���������!",
							MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
					return;
				}

				// �����������
				Object value = groupReport.getCellValue(col, row);

				saveCellValue(col, row, value, lstAddSql);
				// ˢ����ʾ��������Ϣ
				refreshFilter(col, row);
				// ˢ����ʾ
				reportGuideUI.fpnlDefineReport.designGroupReportPane
						.fireReportDataChanged();

				new MessageBox(SubFieldSetDialog.this, "���óɹ�!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox(SubFieldSetDialog.this, "�������󣬴�����Ϣ��"
						+ e.getMessage(), MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
			}

		}
	}

	/**
	 * ˢ����ʾ��������Ϣ
	 */
	private void refreshFilter(int col, int row) {
		Object obj = groupReport.getCellElement(col, row).getValue();
		if (obj == null || !(obj instanceof MyCalculateValueImpl)) {
			return;
		}
		MyCalculateValueImpl calculateValueImpl = (MyCalculateValueImpl) obj;
		String sFilter = DefinePub.getFilter(querySource, calculateValueImpl
				.getStatisticCaliberArray());

		CellElement cellFilter;
		if (groupReport.getCellElement(col, row + 1) == null) {
			cellFilter = new CellElement(col, row + 1);
			cellFilter.setValue(sFilter);
			groupReport.addCellElement(cellFilter, true);
		} else {
			cellFilter = groupReport.getCellElement(col, row + 1);
		}
		cellFilter.setValue(sFilter);
	}

	/**
	 * ����Ƿ����з�������,���з���������ɾ����������
	 * 
	 * @param iStatisticCaliber
	 * @param lstRow,�к��б�
	 * @return
	 */
	private IStatisticCaliber[] delStatisticCaliberArray(
			IStatisticCaliber[] iStatisticCaliber) {

		// ��¼���������к�
		Map rowMap = new HashMap();

		if (iStatisticCaliber == null) {
			return null;
		}

		// �õ������������к�
		for (int i = 0; i < iStatisticCaliber.length; i++) {
			if (DefinePub.isPACaliber(iStatisticCaliber[i])) {
				rowMap.put(String.valueOf(i), String.valueOf(i));
			}
		}

		IStatisticCaliber[] iCurStatisticCaliber = new IStatisticCaliber[iStatisticCaliber.length
				- rowMap.size()];

		int j = 0;
		// ɾ����������
		for (int i = 0; i < iStatisticCaliber.length; i++) {
			if (rowMap.containsKey(String.valueOf(i))) {
				continue;
			}
			iCurStatisticCaliber[j] = iStatisticCaliber[i];
			j++;
		}

		return iCurStatisticCaliber;
	}

	/**
	 * IStatisticCaliberArray���ݳ��ȼӳ�
	 * 
	 * @param iStatisticCaliber
	 * @param iNum
	 */
	private IStatisticCaliber[] addStatisticCaliberArrayLegth(
			IStatisticCaliber[] iStatisticCaliber, int iNum) {
		if (iNum < 1)
			return null;
		int iCount = 0;
		if (iStatisticCaliber != null) {
			iCount = iStatisticCaliber.length;
		}

		IStatisticCaliber[] iStatisticCaliberTmp = new IStatisticCaliber[iCount
				+ iNum];
		for (int i = 0; i < iCount; i++) {
			iStatisticCaliberTmp[i] = iStatisticCaliber[i];
		}
		return iStatisticCaliberTmp;
	}

	/**
	 * ��鵱ǰ��Ԫ�Ƿ���������������
	 * 
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean checkCell(int col, int row) {
		Object value = reportGuideUI.fpnlDefineReport.groupReport.getCellValue(
				col, row);
		if (value == null) {
			JOptionPane.showMessageDialog(SubFieldSetDialog.this,
					"��ǰ��Ԫ��δ����������Դ��������û�����ã�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(value instanceof MyCalculateValueImpl)) {
			JOptionPane
					.showMessageDialog(SubFieldSetDialog.this,
							"��ǰ��Ԫ��Ǽ����У�������û�����ã�", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (DefinePub.checkIsCellFormula((MyCalculateValueImpl) value)) {
			JOptionPane.showMessageDialog(SubFieldSetDialog.this,
					"��ǰ��Ԫ���ǵ�Ԫ������У�������û�����ã�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * ����ö��ԴA��B����֯�ɲ�ѯ����SQL������Ҫƴ������Դ������
	 * 
	 * @return �б�
	 * @throws Exception
	 */
	private List getAddSqlValue() throws Exception {

		int iCount = enumInfoMap.size();
		// �ж��Ƿ�ѡ����ö��Դ
		if (iCount == 0) {
			return null;
		}

		List lstResult = new ArrayList();
		// �õ���һ��ö��Դ����
		if (iCount >= 1) {
			Map tempMap = (Map) enumInfoMap.get(0);
			getEnumValue(tempMap, enumATree, lstResult);

		}
		// �õ��ڶ���ö��Դ����
		if (iCount == 2) {
			Map tempMap = (Map) enumInfoMap.get(1);
			getEnumValue(tempMap, enumBTree, lstResult);
		}
		return lstResult;
	}

	/**
	 * �õ�һ���ڵ���Ϣ������ڵ�����Դ����Ϣ���õ�����ڵ��ѯ����������Map�У�׷�ϵ�List��
	 * 
	 * @param map
	 *            ���������Ϣ
	 * @param customTree��
	 * @param lstResult������ϴ�List
	 * @throws Exception
	 */
	private void getEnumValue(DataSet ds, Map map, MyTreeNode myTreeNode,
			List lstResult) throws Exception {
		// ����sql
		String sSqlWhere = "";
		String sSqlWhereName = "";

		// ����Դ������
		String sDataSourceName = map.get(IDefineReport.DATASOURCE_NAME)
				.toString();
		// ����
		String sFieldCode = map.get(IDefineReport.COL_ID).toString();
		// ��������
		String sFieldName = map.get(IDefineReport.FIELD_FNAME).toString();

		// �ж������Ͳ�����Ƿ�ͬһ�ֶΣ����в�ͬ����
		String priField = map.get(ISysRefCol.PRIMARY_FIELD).toString();
		boolean isDiffer = isDiffer(map);

		String sSqlWhereTemp = getSqlWhere(ds, myTreeNode, isDiffer, priField,
				sFieldCode);

		if (sSqlWhereTemp != null) {
			sSqlWhere = " (" + sSqlWhereTemp + ")";
		}
		sSqlWhereTemp = getSqlWhereName(myTreeNode.getUserObject().toString(),
				sDataSourceName, sFieldName);
		if (sSqlWhereName != null) {
			sSqlWhereName = " ���� (" + sSqlWhereTemp + ")";
		}

		Map resultMap = new HashMap();
		if ("".equals(sSqlWhere)) {
			resultMap.put(IDefineReport.CODE, null);
		} else {
			resultMap.put(IDefineReport.CODE, sSqlWhere);
		}
		resultMap.put(IDefineReport.NAME, sSqlWhereName);
		resultMap
				.put(IDefineReport.SOURCE_ID, map.get(IDefineReport.SOURCE_ID));
		resultMap.put(IDefineReport.COL_ID, map.get(IDefineReport.COL_ID));

		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		resultMap.put(IDefineReport.NODE_ID, myPfNode.getValue());
		resultMap.put(IDefineReport.ENUM_ID, map.get(IDefineReport.ENUM_ID));
		lstResult.add(resultMap);
	}

	/**
	 * �õ�һ������Դֵ
	 * 
	 * @param map
	 *            ���������Ϣ
	 * @param customTree��
	 * @param lstResult������ϴ�List
	 * @throws Exception
	 */
	private void getEnumValue(Map map, CustomTree customTree, List lstResult)
			throws Exception {
		// ����sql
		String sSqlWhere = "";
		String sSqlWhereName = "";
		// ����Դ������
		String sDataSourceName = map.get(IDefineReport.DATASOURCE_NAME)
				.toString();
		// ����
		String sFieldCode = map.get(IDefineReport.COL_ID).toString();
		// ��������
		String sFieldName = map.get(IDefineReport.FIELD_FNAME).toString();

		// �ж������Ͳ�����Ƿ�ͬһ�ֶΣ����в�ͬ����
		String priField = map.get(ISysRefCol.PRIMARY_FIELD).toString();
		boolean isDiffer = isDiffer(map);

		Map mapValue = getWhere(customTree, isDiffer, priField);
		if (mapValue == null) {
			return;
		}

		List list = (List) mapValue.get(IDefineReport.CODE);
		String sSqlWhereTemp = getSqlWhere(list, sFieldCode, isDiffer);
		if (sSqlWhereTemp != null) {
			sSqlWhere = " (" + sSqlWhereTemp + ")";
		}
		list = (List) mapValue.get(IDefineReport.NAME);
		sSqlWhereTemp = getSqlWhereName(list, sDataSourceName, sFieldName);
		if (sSqlWhereName != null) {
			sSqlWhereName = " ���� (" + sSqlWhereTemp + ")";
		}
		Map resultMap = new HashMap();
		if ("".equals(sSqlWhere)) {
			resultMap.put(IDefineReport.CODE, null);
		} else {
			resultMap.put(IDefineReport.CODE, sSqlWhere);
		}
		resultMap.put(IDefineReport.NAME, sSqlWhereName);
		resultMap
				.put(IDefineReport.SOURCE_ID, map.get(IDefineReport.SOURCE_ID));
		resultMap.put(IDefineReport.COL_ID, map.get(IDefineReport.COL_ID));
		resultMap.put(IDefineReport.NODE_ID, this.getSelectNodeId(customTree));
		resultMap.put(IDefineReport.ENUM_ID, map.get(IDefineReport.ENUM_ID));
		lstResult.add(resultMap);
	}

	/**
	 * �ж������Ͳ�����Ƿ�ͬһ�ֶ�
	 * 
	 * @param map
	 * @return��ͬ����false,��ͬ����true
	 */
	public static boolean isDiffer(Map map) {
		// �ж������Ͳ�����Ƿ�ͬһ�ֶΣ����в�ͬ����
		String priField = map.get(ISysRefCol.PRIMARY_FIELD).toString();
		String lvlField = map.get(ISysRefCol.LVL_FIELD).toString();
		boolean isDiffer;
		if (priField.equalsIgnoreCase(lvlField)) {
			isDiffer = false;
		} else {
			isDiffer = true;
		}
		return isDiffer;
	}

	/**
	 * �õ�SQL��ѯ����������List
	 * 
	 * @param list
	 * @param sFieldCode
	 * @return
	 */
	private String getSqlWhere(List list, String sFieldCode, boolean isDiffer) {
		if (list == null)
			return null;
		StringBuffer sSqlWhere = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (isDiffer) {
				if (!Common.isNullStr(sSqlWhere.toString())) {
					sSqlWhere.append(",");
				}
				sSqlWhere.append(list.get(i).toString());
			} else {
				if (!"".equals(sSqlWhere.toString())) {
					sSqlWhere.append(" or  ");
				}
				sSqlWhere.append(sFieldCode + " like '"
						+ list.get(i).toString() + "%'");
			}
		}
		if (isDiffer) {
			sSqlWhere.insert(0, sFieldCode + " in (");
			sSqlWhere.append(")");
		}
		return sSqlWhere.toString();
	}

	/**
	 * �õ�SQL��ѯ����
	 * 
	 * @param list
	 * @param sFieldCode
	 * @return
	 * @throws Exception
	 */
	private String getSqlWhere(DataSet ds, MyTreeNode node, boolean isDiffer,
			String priField, String sFieldCode) throws Exception {
		StringBuffer sSqlWhere = new StringBuffer();
		String value;
		if (isDiffer) {
			value = getLeafValue(ds, node, priField);
			sSqlWhere.append(sFieldCode + " in (");
			sSqlWhere.append(value);
			sSqlWhere.append(")");
		} else {
			MyPfNode myPfNode = (MyPfNode) node.getUserObject();
			value = myPfNode.getValue();
			if ("".equals(sSqlWhere.toString())) {
				sSqlWhere.append(sFieldCode + " like '" + value + "%'");
			} else {
				sSqlWhere.append(" or  " + sFieldCode + " like '" + value
						+ "%'");
			}
		}

		return sSqlWhere.toString();
	}

	/**
	 * �õ�SQL��ѯ�����ַ���������List
	 * 
	 * @param list
	 * @param sDataSourceName
	 *            ����Դ����
	 * @return
	 */
	private String getSqlWhereName(List list, String sDataSourceName,
			String sFieldName) {
		String sDataSourceNameTmp = sDataSourceName + ".";
		if (list == null)
			return null;
		String sSqlWhereName = "";
		for (int i = 0; i < list.size(); i++) {
			if ("".equals(sSqlWhereName)) {
				sSqlWhereName = "{" + sDataSourceNameTmp + sFieldName + "} ���� "
						+ list.get(i).toString();
			} else {
				sSqlWhereName = sSqlWhereName + " ����  {" + sDataSourceNameTmp
						+ sFieldName + "} ���� " + list.get(i).toString();
			}
		}
		return sSqlWhereName;
	}

	/**
	 * �õ�SQL��ѯ�����ַ���
	 * 
	 * @param value
	 *            ֵ
	 * @param sDataSourceName����Դ����
	 * @param sFieldName��������
	 * @return
	 */
	private String getSqlWhereName(String value, String sDataSourceName,
			String sFieldName) {
		String sDataSourceNameTmp = sDataSourceName + ".";
		String sSqlWhereName = "";
		if ("".equals(sSqlWhereName)) {
			sSqlWhereName = "{" + sDataSourceNameTmp + sFieldName + "} ���� "
					+ value;
		} else {
			sSqlWhereName = sSqlWhereName + " ����  {" + sDataSourceNameTmp
					+ sFieldName + "} ���� " + value;
		}
		return sSqlWhereName;
	}

	/**
	 * �������ڵ㣬�õ���ѯ����
	 * 
	 * @param customTree��
	 * @param listCode�����б�
	 * @param listName�����б�
	 * @param isDiffer�����Ͳ�����Ƿ�ͬһ�ֶΣ���ͬtrue,��ͬfalse
	 * @param priField�����ֶ�
	 * @throws Exception
	 */
	private Map getWhere(CustomTree customTree, boolean isDiffer,
			String priField) throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// �жϸ��ڵ��Ƿ�ѡ��
		myTreeNode = (MyTreeNode) customTree.getRoot();
		myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}
		List listCode = new ArrayList();
		List listName = new ArrayList();

		getSelectDiv(customTree.getDataSet(), myTreeNode, listCode, listName,
				isDiffer, priField);
		Map resultMap = new HashMap();
		resultMap.put(IDefineReport.CODE, listCode);
		resultMap.put(IDefineReport.NAME, listName);
		return resultMap;
	}

	/**
	 * �õ�ѡ�нڵ���룬ʹ���˵ݹ�
	 * 
	 * @param myTreeNode���ڵ�
	 * @param lstCode�����б�
	 * @param lstName�����б�
	 * @param isDiffer�����Ͳ�����Ƿ�ͬһ�ֶΣ���ͬtrue,��ͬfalse
	 * @param priField�����ֶ�
	 * @throws Exception
	 * @throws Exception
	 */
	private void getSelectDiv(DataSet ds, MyTreeNode myTreeNode, List listCode,
			List lstName, boolean isDiffer, String priField) throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				// �����Ͳ�����Ƿ�ͬһ�ֶ�
				if (isDiffer) {// ��ͬ
					listCode.add(getLeafValue(ds, myTreeNodeTmp, priField));
				} else {// ��ͬ
					listCode.add(myPfNode.getValue());
				}
				lstName.add(myTreeNodeTmp.getUserObject().toString());
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectDiv(ds, myTreeNodeTmp, listCode, lstName, isDiffer,
						priField);
			}
		}
	}

	/**
	 * �õ��ڵ�ֵ
	 * 
	 * @param ds���ݼ�
	 * @param node�ڵ�
	 * @param priField�����ֶ�
	 * @return
	 * @throws Exception
	 */
	private String getLeafValue(DataSet ds, MyTreeNode node, String priField)
			throws Exception {
		String curBookmark = ds.toogleBookmark();
		// �ж��Ƿ�Ҷ�ڵ�
		if (node.isLeaf()) {
			ds.gotoBookmark(node.getBookmark());
			return "'" + ds.fieldByName(priField).getString() + "'";
		}

		StringBuffer sFileter = new StringBuffer();
		MyTreeNode myTreeNodeTmp;
		Enumeration enume = node.breadthFirstEnumeration();
		while (enume.hasMoreElements()) {
			myTreeNodeTmp = (MyTreeNode) enume.nextElement();
			if (!myTreeNodeTmp.isLeaf())
				continue;
			ds.gotoBookmark(myTreeNodeTmp.getBookmark());
			if (!Common.isNullStr(sFileter.toString())) {
				sFileter.append(",");
			}
			sFileter.append("'" + ds.fieldByName(priField).getString() + "'");
		}

		ds.gotoBookmark(curBookmark);
		return sFileter.toString();
	}

	/**
	 * �Զ����÷�����ť����¼�
	 */
	private class AutoSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (enumInfoMap == null || enumInfoMap.size() == 0) {
				return;
			}

			// �Զ�������ʼ��
			int startRow = Integer.parseInt(jspRow.getValue().toString()) - 1;
			// �ж�ѡ������ڲ�����ͷ��
			if (!CreateGroupReport.isInGroupRow(startRow,
					RowConstants.UIAREA_HEADER, groupReport)) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"��ѡ����в��ڱ�ͷ������ѡ������У�", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// ȡ��A����Դ�ı���ڴ�
			int iLevelA = Integer.parseInt(jspSubField1.getValue().toString());
			// ȡ��B����Դ�ı���ڴ�
			int iLevelB = Integer.parseInt(jspSubField2.getValue().toString());
			// ȡ��A����Դ�Ľڵ���
			List lstNodsA = getNodes(enumATree, iLevelA);
			// ȡ��B����Դ�Ľڵ���
			List lstNodsB = getNodes(enumBTree, iLevelB);
			int iCountA = lstNodsA.size();
			int iCountB = lstNodsB.size();
			// �ж��Ƿ�����ȡ������
			if (!checkWhereSet(enumInfoMap, iCountA, iCountB))
				return;

			// �ж��Ƿ񳬳�128�У��糬��128�У���ʾ
			int iCountCol = 0;
			if (iCountB == 0) {
				iCountCol = iCountA;
			} else {
				iCountCol = iCountA * iCountB;
			}
			if (iCountCol > 128) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"����Ŀǰ������,����������128�еĲ�ѯ��Ϣ�������²�ѯʱ�����������ٲ�ѯ���ݻ�ּ��Ž��в�ѯ��",
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// ��鵱ǰ��Ԫ�Ƿ���������������
			int col = summaryReportPane.getCellSelection().getColumn();
			int row = summaryReportPane.getCellSelection().getRow();

			if (!checkCell(col, row)) {
				return;
			}

			// �õ���ǰ��Ԫ��ֵ
			Object value = groupReport.getCellValue(col, row);

			// �õ���ͷ���Ŀ�ʼ��
			int rowIndex[] = CreateGroupReport.getRowIndexs(
					RowConstants.UIAREA_HEADER, groupReport);
			int rowFrom = rowIndex[0];

			// �ж��Ƿ�ѡ���˰����ϼ�
			int rowFLagA = 0;
			int rowFLagB = 0;
			if ("false".equals(fchkSubField1.getValue().toString())) {
				rowFLagA = 1;
			} else {
				rowFLagA = iLevelA;
			}
			if (iCountB == 0)
				rowFLagB = 0;
			else if ("false".equals(fchkSubField2.getValue().toString())) {
				rowFLagB = 1;
			} else {
				rowFLagB = iLevelB;
			}

			// �ж�Ҫ��Ҫ������,����Ҫ������-����ʼ����-��ͷ���Ŀ�ʼ��+1��
			int rowNeedAdd = rowFLagA + rowFLagB - (startRow - rowFrom + 1);
			// >0˵����Ҫ������
			for (int i = 0; i < rowNeedAdd; i++) {
				groupReport.addRowBefore(rowFrom);
			}
			if (rowNeedAdd > 0) {
				startRow = startRow + rowNeedAdd;
			} else {
				rowNeedAdd = 0;
			}

			MyTreeNode myTreeNodeA = null;
			MyTreeNode myTreeNodeB = null;

			Map tempMapA = null;
			Map tempMapB = null;

			List lstAddSql = null;

			// ��λԭ��Ԫ��
			summaryReportPane.setCellSelection(new CellSelection(col, row
					+ rowNeedAdd));

			int curCol = 0;
			try {
				// ѭ����������ԴA,
				for (int i = 0; i < iCountA; i++) {
					lstAddSql = new ArrayList();
					tempMapA = (Map) enumInfoMap.get(0);
					myTreeNodeA = (MyTreeNode) lstNodsA.get(i);

					// �õ�һ���ڵ���Ϣ������ڵ�����Դ����Ϣ���õ�����ڵ��ѯ����������Map�У�׷�ӵ�List��
					getEnumValue(enumATree.getDataSet(), tempMapA, myTreeNodeA,
							lstAddSql);

					// �ж�����Դ2��û������������û��������������������Դ1����
					if (iCountB == 0) {
						curCol = col + i;
						// ����һ��
						groupReport.addColumnAfter(curCol);
						// ���浥Ԫ����Ϣ
						saveCellValue(curCol, row + rowNeedAdd, value,
								lstAddSql);
						// ˢ����ʾ��������Ϣ
						refreshFilter(curCol, row + rowNeedAdd);

						// ���ñ�ͷ��Ϣ
						setHeaderValue(curCol, startRow - rowFLagA, startRow,
								myTreeNodeA, "false".equals(fchkSubField1
										.getValue().toString()) ? false : true);

					} else {// ����Դ������
						// ѭ����������ԴB
						for (int j = 0; j < iCountB; j++) {
							myTreeNodeB = (MyTreeNode) lstNodsB.get(j);
							tempMapB = (Map) enumInfoMap.get(1);
							// �õ�һ���ڵ���Ϣ������ڵ�����Դ����Ϣ���õ�����ڵ��ѯ����������Map�У�׷�ϵ�List��
							getEnumValue(enumBTree.getDataSet(), tempMapB,
									myTreeNodeB, lstAddSql);

							curCol = col + i * iCountB + j;
							// ����һ��
							groupReport.addColumnAfter(curCol);
							// ���浥Ԫ����Ϣ
							saveCellValue(curCol, row + rowNeedAdd, value,
									lstAddSql);
							// ˢ����ʾ��������Ϣ
							refreshFilter(curCol, row + rowNeedAdd);
							// �Ƴ�����ԴB�������Ա�ѭ����������ԴB����
							lstAddSql.remove(lstAddSql.size() - 1);

							// ���ñ�ͷ��Ϣ
							setHeaderValue(curCol, startRow - rowFLagA
									- rowFLagB, startRow - rowFLagB,
									myTreeNodeA, "false".equals(fchkSubField1
											.getValue().toString()) ? false
											: true);
							// ���ñ�ͷ��Ϣ
							setHeaderValue(curCol, startRow - rowFLagB,
									startRow, myTreeNodeB, "false"
											.equals(fchkSubField1.getValue()
													.toString()) ? false : true);
						}
					}
				}

				// ˢ����ʾ
				reportGuideUI.fpnlDefineReport.designGroupReportPane
						.fireReportDataChanged();

				// ���汾�����ӵ�����
				lastTimeAddCol = iCountCol;
				new MessageBox(SubFieldSetDialog.this, "���óɹ�!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox(SubFieldSetDialog.this, "�������󣬴�����Ϣ��"
						+ e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}
		}
	}

	/**
	 * ���ñ�ͷ��Ϣ
	 * 
	 * @param col��
	 * @param row��
	 * @param myTreeNode���ڵ�
	 * @param bUpSet�Ƿ����ϻ���
	 */
	private void setHeaderValue(int col, int startRow, int endRow,
			MyTreeNode myTreeNode, boolean bUpSet) {
		int iLevel;
		if (bUpSet)
			iLevel = myTreeNode.getLevel();
		else
			iLevel = 1;
		// ��ʾ��ͷ��Ϣ
		String showValue = myTreeNode.getUserObject().toString();
		showValue = showValue.substring(showValue.indexOf("]") + 1);

		CellElement cell = groupReport.getCellElement(col, startRow + iLevel);
		if (cell == null) {
			cell = new CellElement(col, startRow + iLevel);
			groupReport.addCellElement(cell);
		}
		cell.setValue(showValue);

		// ���õ�Ԫ���ʽ
		Style style = cell.getStyle();
		style = style.deriveHorizontalAlignment(Constants.CENTER);
		cell.setStyle(style);

		if (myTreeNode.isLeaf()) {
			if (endRow != (startRow + iLevel)) {
				// ��ĩ���㣬�Ƿ����ºϲ���Ԫ��
				CellSelection cellSelection = new CellSelection(col, startRow
						+ iLevel, 1, endRow - (startRow + iLevel) + 1, col,
						startRow + iLevel, 1, endRow - (startRow + iLevel) + 1);
				summaryReportPane.mergeCell(cellSelection);
			}

		} else { // ����ĩ���㣬�ϲ���Ԫ��
			// ���Һϲ���Ԫ��

			// // editColumn = i;
			// // editRow = j;
			// // editColumnSpan = k;
			// // editRowSpan = l;
			// // column = i1;
			// // row = j1;
			// // columnSpan = k1;
			// // rowSpan = l1;

			int colNum = getPreNodeNum(col, startRow + iLevel, groupReport);
			if (colNum != 0) {
				colNum = colNum + 1;
				CellSelection cellSelection = new CellSelection(col - colNum
						+ 1, startRow + iLevel, colNum, 1, col - colNum + 1,
						startRow + iLevel, colNum, 1);
				summaryReportPane.mergeCell(cellSelection);
			}
		}

		// �Ƿ����ϻ���
		if (bUpSet) {
			// �õ����ڵ�
			MyTreeNode parNode = (MyTreeNode) myTreeNode.getParent();
			if (parNode.isRoot())
				return;

			setHeaderValue(col, startRow, endRow, parNode, bUpSet);
		}
	}

	/**
	 * �õ�ǰ��Ľڵ���
	 * 
	 * @param myTreeNode
	 * @return
	 */
	private int getPreNodeNum(int col, int row, GroupReport groupReport) {
		int iCount = 0;
		String value;
		if (groupReport.getCellElement(col, row) == null)
			value = "";
		else
			value = groupReport.getCellElement(col, row).getValue().toString();
		String valueTmp = null;
		for (int i = col - 1; i >= 0; i--) {
			if (groupReport.getCellElement(i, row) == null)
				valueTmp = "";
			else
				valueTmp = groupReport.getCellElement(i, row).getValue()
						.toString();
			if (value.equals(valueTmp)) {
				iCount++;
			} else {
				return iCount;
			}
		}
		return iCount;
	}

	/**
	 * �ж��Ƿ�����ȡ������
	 * 
	 * @param enumInfoMap
	 * @param iCountA
	 * @param iCountB
	 * @return
	 */

	private boolean checkWhereSet(List enumInfoMap, int iCountA, int iCountB) {
		// �ж��Ƿ�����ȡ������
		if (enumInfoMap.size() >= 1) {
			if (iCountA == 0) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"δ����ȡ�������������ã�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		if (enumInfoMap.size() == 2) {
			if (iCountB == 0) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"δ����ȡ�������������ã�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		return true;
	}

	/**
	 * ���浥Ԫ�������
	 * 
	 * @param col
	 *            ��
	 * @param row
	 *            ��
	 * @param value
	 *            ��Ԫ��ԭֵ
	 * @param lstAddSql����������
	 */
	private void saveCellValue(int col, int row, Object value, List lstAddSql) {
		if (lstAddSql == null)
			return;

		// �õ�MyCalculateValueImpl����
		MyCalculateValueImpl myCalculateValueImpl = (MyCalculateValueImpl) ((MyCalculateValueImpl) value)
				.clone();

		IMeasureAttr[] measureAttr = myCalculateValueImpl.getMeasureArray();
		int length = measureAttr.length;
		String sFormula = myCalculateValueImpl.getFormula().getContent();
		String randomUUID;
		for (int i = 0; i < length; i++) {
			randomUUID = DefinePub.getRandomUUID();
			sFormula = sFormula.replaceAll(measureAttr[i].getMeasureID(),
					randomUUID);
			measureAttr[i].setMeasureID(randomUUID);
		}
		myCalculateValueImpl.getFormula().setContent(sFormula);

		// �õ�����������
		IStatisticCaliber[] iStatisticCaliber = myCalculateValueImpl
				.getStatisticCaliberArray();
		iStatisticCaliber = delStatisticCaliberArray(iStatisticCaliber);

		Map addSqlMap = null;
		String addSql;
		String addSqlStr;
		// �����������
		int iCount = lstAddSql.size();
		for (int i = 0; i < iCount; i++) {
			addSqlMap = (Map) lstAddSql.get(i);
			if (addSqlMap.get(IDefineReport.CODE) == null)
				return;
			addSql = addSqlMap.get(IDefineReport.CODE).toString();
			addSqlStr = addSqlMap.get(IDefineReport.NAME).toString();

			iStatisticCaliber = addStatisticCaliberArrayLegth(
					iStatisticCaliber, 1);
			int index = iStatisticCaliber.length - 1;
			iStatisticCaliber[index] = new MySummaryStatisticCaliberImpl(
					addSqlStr);
			// ����CaliberID(����ID)ֵ
			iStatisticCaliber[index].setCaliberID(IDefineReport.PA_
					+ DefinePub.getRandomUUID());
			iStatisticCaliber[index].setSourceID(addSqlMap.get(
					IDefineReport.SOURCE_ID).toString());
			iStatisticCaliber[index].setSourceColID(addSqlMap.get(
					IDefineReport.COL_ID).toString());
			iStatisticCaliber[index].setJoinBefore("and");
			// ����AddSqlֵ
			iStatisticCaliber[index].setAddSQL(addSql);
			if (iStatisticCaliber[index] instanceof ICustomStatisticCaliber) {
				// if (addSqlMap.get(IDefineReport.ENUM_ID) != null) {
				((ICustomStatisticCaliber) iStatisticCaliber[index])
						.setEnumID(addSqlMap.get(IDefineReport.ENUM_ID)
								.toString());
				// }
				// if (addSqlMap.get(IDefineReport.ENUM_ID) != null) {
				((ICustomStatisticCaliber) iStatisticCaliber[index])
						.setNodeID(addSqlMap.get(IDefineReport.NODE_ID)
								.toString());
				// }
			}
		}

		myCalculateValueImpl.setStatisticCaliberArray(iStatisticCaliber);
		groupReport.getCellElement(col, row).setValue(myCalculateValueImpl);
	}

	/**
	 * �õ����ڵ�List,�������Ĳ��
	 * 
	 * @param customTree
	 * @param level���
	 * @return
	 */
	private List getNodes(CustomTree customTree, int level) {
		List listNodes = new ArrayList();
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.depthFirstEnumeration();
		// ������
		while (enuma.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enuma.nextElement();
			MyPfNode myPfNode = (MyPfNode) node.getUserObject();
			// �ж��ǲ��Ǹ��ڵ�
			if (node == root) {
				continue;
			}

			// �жϽڵ��״̬�ǲ���ȫѡ��
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				if (node.getLevel() > level)
					continue;
				else if (node.getChildCount() == 0)
					listNodes.add(node);
				else if (node.getLevel() == level) {// �ж��ǲ�����Ҫ�Ľڴ�
					listNodes.add(node);
				}
			}
		}
		return listNodes;

	}

	/**
	 * �Զ����÷�����ť����¼�
	 */
	private class CanelSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			int col = summaryReportPane.getCellSelection().getColumn();
			for (int i = lastTimeAddCol - 1; i >= 0; i--) {
				groupReport.removeColumn(col + i);
			}
			// ˢ����ʾ
			reportGuideUI.fpnlDefineReport.designGroupReportPane
					.fireReportDataChanged();
			new MessageBox(SubFieldSetDialog.this, "��ȡ�ϴ����óɹ���",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
		}
	}

	// public void stayOnTop() {
	// this.addWindowListener(new WindowListener() {
	//
	// public void windowActivated(WindowEvent e) {
	// // TODO �Զ����ɷ������
	//
	// }
	//
	// public void windowClosed(WindowEvent e) {
	// // TODO �Զ����ɷ������
	//
	// }
	//
	// public void windowClosing(WindowEvent e) {
	// // TODO �Զ����ɷ������
	//
	// }
	//
	// public void windowDeactivated(WindowEvent e) {
	// SubFieldSetDialog.this.toFront();
	//
	// }
	//
	// public void windowDeiconified(WindowEvent e) {
	// // TODO �Զ����ɷ������
	// }
	//
	// public void windowIconified(WindowEvent e) {
	// // TODO �Զ����ɷ������
	//
	// }
	//
	// public void windowOpened(WindowEvent e) {
	// // TODO �Զ����ɷ������
	//
	// }
	//
	// });
	// }

	/**
	 * �õ�ѡ�нڵ�ID
	 * 
	 * @param customTree
	 */
	private String getSelectNodeId(CustomTree customTree) throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// �жϸ��ڵ��Ƿ�ѡ��
		myTreeNode = (MyTreeNode) customTree.getRoot();
		myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}

		StringBuffer sSqlNode = new StringBuffer();
		// �õ�ѡ�нڵ�ID
		getSelectNodeIdRecursion(customTree.getDataSet(), myTreeNode, sSqlNode);
		return sSqlNode.toString();
	}

	/**
	 * �õ�ѡ�нڵ�ID��ʹ���˵ݹ�
	 * 
	 * @param ds
	 *            ���ݼ�
	 * @param myTreeNode
	 *            ���ڵ�
	 * @param sSqlNode
	 *            ��ѡ�нڵ�ID
	 * @throws Exception
	 */
	private void getSelectNodeIdRecursion(DataSet ds, MyTreeNode myTreeNode,
			StringBuffer sSqlNode) throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				if (!Common.isNullStr(sSqlNode.toString())) {
					sSqlNode.append(",");
				}
				sSqlNode.append(myPfNode.getValue());
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectNodeIdRecursion(ds, myTreeNodeTmp, sSqlNode);
			}
		}
	}

	/**
	 * ���ýڵ�Ϊѡ��״̬
	 * 
	 * @param sNodeId
	 * @param customTree
	 */
	public static void setNodeSelect(String sNodeId, CustomTree customTree) {
		if (sNodeId == null)
			return;
		String[] sNodeIdArray = sNodeId.split(",");
		// SetSelectTree.setIsCheck(customTree, sNodeIdArray);
		MyTreeNode node;
		PfTreeNode pNode;
		int len = sNodeIdArray.length;
		for (int i = 0; i < len; i++) {
			node = (MyTreeNode) customTree.getNodeById(sNodeIdArray[i]);
			if (node != null) {
				pNode = (PfTreeNode) node.getUserObject();
				pNode.setIsSelect(true);
			}
		}
	}
}
