/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSource;
import com.foundercy.pf.reportcy.summary.object.DefaultDictionaryImpl;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryParameterImpl;
import com.foundercy.pf.reportcy.summary.object.base.ToSource;
import com.foundercy.pf.reportcy.summary.object.enumer.SummaryEnumSourceImpl;
import com.foundercy.pf.reportcy.summary.object.enumer.SummaryEnumSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.RefEnumSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceImpl;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.SummarySourceCol;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Resource;
import com.foundercy.pf.util.Tools;

/**
 * <p>
 * Title:�Զ����ѯ����ͷ��˽���
 * </p>
 * <p>
 * Description:�Զ����ѯ����ͷ��˽���
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾

 */
public class ReportGuideUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// ����ID
	String sReportId = null;

	// �����������
	RepSetObject repSetObject = null;

	// ��������Դ��ʾ��
	private CustomTree datasChoicTre = null;

	SubFieldSetDialog subFieldSetDialog;

	// ���屨��������ҳ��
	ReportInfoSet reportInfoSet = null;

	// ���屨������ҳ��
	ReportOrderSet reportOrderSet = null;

	// ��������Դ��ϵ����
	ReportDSRelaSet reportDSRelaSet = null;

	// ���屨���������ҳ��
	ReportGroupSet reportGroupSet = null;

	// �������ݿ�ӿ�
	IDefineReport definReportServ = null;

	// ѡ�е�����Դ
	private List lstDataSource = null;

	// ��������Դѡ����
	private DataSourceChoiceUI dataSourceChoice = null;

	// ������Դ���öԻ���
	private DataSourceSet dataSourceSet = null;

	ReportQuerySource querySource = null;

	// ���屨�������
	ReportPanel fpnlDefineReport = null;

	// �õ���������������
	private Map refColPriMap = null;

	// ����������
	DefineReport defineReport;

	/**
	 * ���캯��
	 * 
	 */
	public ReportGuideUI(RepSetObject repSetObject, DefineReport defineReport) {
		this.setSize(900, 650);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		if (repSetObject == null) {
			this.setTitle("����������ܱ�");
		} else {
			this.setTitle("�޸ķ�����ܱ�");
		}
		if (repSetObject != null) {
			this.sReportId = repSetObject.getREPORT_ID();
		}

		this.repSetObject = repSetObject;
		this.defineReport = defineReport;

		if (this.repSetObject == null) {
			this.repSetObject = new RepSetObject();
		}

		// �������ݿ�ӿ�
		definReportServ = DefineReportI.getMethod();

		// ���ý����ʼ������
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(ReportGuideUI.this, e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}

		// �ж������ӱ������޸ı���
		if (sReportId != null && !"".equals(sReportId)) {// �޸�
			// �����ļ����ݵõ�����Դ��Ϣ
			getLstDataSource();
			setDataSourceTreeValue(lstDataSource);
		}

	}

	/**
	 * �����ʼ������
	 * 
	 * @throws Exception
	 * 
	 */
	private void jbInit() throws Exception {
		datasChoicTre = new CustomDragTree("����Դ", null, "chr_id",
				"FIELD_FNAME", "parent_id", null, "chr_id") {

			private static final long serialVersionUID = -556491557372220658L;

			public boolean isDragAble() {
				if (this.getSelectedNode() == null
						|| !this.getSelectedNode().isLeaf())
					return false;
				if (this.getSelectionPath().getLastPathComponent() == this
						.getRoot())
					return false;
				return true;
			}

		};

		FScrollPane datasChoicScroll = new FScrollPane(datasChoicTre);

		// ������߱�������Դ���
		FPanel datasChoicePnl = new FPanel();
		datasChoicePnl.setTitle("��������Դ");
		datasChoicePnl.setLayout(new RowPreferedLayout(1));
		// ����Դ��ʾ��������߱�������Դ���
		datasChoicePnl.addControl(datasChoicScroll);

		// ���屨�������
		fpnlDefineReport = new ReportPanel(this);

		// ����querySource
		querySource = (ReportQuerySource) ReportConver
				.getReportQuerySource(fpnlDefineReport.groupReport);

		// ���屨�����Զ�ҳ�����
		FTabbedPane ftabPnlReportSet = new FTabbedPane();
		// ���屨��������ҳ��
		reportInfoSet = new ReportInfoSet(this);
		ftabPnlReportSet.addControl("������������", reportInfoSet);
		// ��������Դ��ϵ����
		reportDSRelaSet = new ReportDSRelaSet(this);
		ftabPnlReportSet.addControl("����Դ��ϵ����", reportDSRelaSet);
		// ���屨���������ҳ��
		reportGroupSet = new ReportGroupSet(this);
		ftabPnlReportSet.addControl("�����������", reportGroupSet);
		// ���屨������ҳ��
		reportOrderSet = new ReportOrderSet(this);
		ftabPnlReportSet.addControl("��������", reportOrderSet);

		// �����ұ����·����
		FSplitPane spnlRight = new FSplitPane();
		spnlRight.setBorder(null);
		spnlRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spnlRight.setDividerLocation(Toolkit.getDefaultToolkit()
				.getScreenSize().height - 300);
		// �������������ϲ�
		spnlRight.addControl(fpnlDefineReport);
		// �������Զ�ҳ������������²�
		spnlRight.addControl(ftabPnlReportSet);

		// Ԥ�����
		// PriviewPanel priviewPanel = new PriviewPanel();

		FTabbedPane ftabPnlRight = new FTabbedPane();
		ftabPnlRight.setBorder(null);
		ftabPnlRight.addTab("���", Resource.getImage("images/fbudget/set.gif"),
				spnlRight);
		// ftabPnlRight.addTab("Ԥ��",
		// Resource.getImage("images/fbudget/prw.gif"),
		// priviewPanel);

		// ��������Դ��ť���
		ChoiceBtnPanel choiceBtnPanel = new ChoiceBtnPanel();

		// ������ɡ�ȡ����ť�����
		ButtonPanel btnPanel = new ButtonPanel(this);

		// �������ҷ������
		FSplitPane spnlMain = new FSplitPane();
		spnlMain.setBorder(null);
		spnlMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spnlMain.setDividerLocation(300);
		// ��������Դ���������ҷ���������
		spnlMain.addControl(datasChoicePnl);
		// �ұ����·����������ҷ�������ұ�
		spnlMain.addControl(ftabPnlRight);

		// ���������
		FPanel fpnlMain = new FPanel();
		RowPreferedLayout rLayRight = new RowPreferedLayout(2);
		rLayRight.setRowHeight(35);
		rLayRight.setColumnWidth(300);
		fpnlMain.setLayout(rLayRight);
		// �������Զ�ҳ���������ұ����
		fpnlMain.add(spnlMain, new TableConstraints(1, 2, true, true));
		// ��ɡ�ȡ����ť���������ұ����
		fpnlMain.add(choiceBtnPanel, new TableConstraints(1, 1, false, false));
		// ��ɡ�ȡ����ť���������ұ����
		fpnlMain.add(btnPanel, new TableConstraints(1, 1, false, true));

		this.getContentPane().add(fpnlMain);

	}

	/**
	 * ��������Դ���ð�ť���
	 */
	private class ChoiceBtnPanel extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		// "����Դ����"��ť
		public FButton fbtnDataSourceSet = null;

		// "����Դѡ��"��ť
		public FButton fbtnDataSourceChoice = null;

		/**
		 * ���췽��
		 * 
		 */
		public ChoiceBtnPanel() {
			// ���ÿ�����ʾ
			this.setAlignment(FlowLayout.RIGHT);

			fbtnDataSourceSet = new FButton("fbtnDataSourceSet", "����Դ��������");
			fbtnDataSourceSet
					.addActionListener(new DataSourceSetActionListener());
			fbtnDataSourceChoice = new FButton("nextBtn", "����Դѡ��");
			fbtnDataSourceChoice
					.addActionListener(new DataSourceChoiceActionListener());

			// ����Դ����
			this.addControl(fbtnDataSourceSet, new TableConstraints(1, 1, true,
					false));
			// ����Դѡ��
			this.addControl(fbtnDataSourceChoice, new TableConstraints(1, 1,
					true, false));

		}
	}

	/**
	 * �����ļ����ݵõ�����Դ��Ϣ
	 * 
	 */
	private void getLstDataSource() {
		if (lstDataSource == null) {
			lstDataSource = new ArrayList();
		}
		// �õ�����Դ��Ϣ
		if (querySource.getDataSourceManager() == null)
			return;

		IDataSource[] summaryDataSourceImpl = querySource
				.getDataSourceManager().getDataSourceArray();
		Map mapDataSource = null;
		for (int i = 0; i < summaryDataSourceImpl.length; i++) {
			mapDataSource = new HashMap();
			mapDataSource.put(IDefineReport.DICID, summaryDataSourceImpl[i]
					.getSourceID());
			lstDataSource.add(mapDataSource);
		}
	}

	/**
	 * ������Դ������ʾ����Դ��Ϣ
	 * 
	 * @param lstDataSource
	 *            ��ʾ����
	 */
	private void setDataSourceTreeValue(List lstDataSource) {
		if (lstDataSource == null)
			return;
		// ��������Դ�б�ת�����ַ���
		String sDataSource = getDataSource(lstDataSource);

		// �õ�ѡ�е�����Դ����ϸ��Ϣ
		DataSet dsDataSourceDetail = null;
		try {
			if (Common.isNullStr(sDataSource))
				return;
			dsDataSourceDetail = definReportServ
					.getDataSoureDetail(sDataSource);
			// ���õ�ѡ�е�����Դ����ϸ��Ϣ�����������Դ��ʾ��
			datasChoicTre.setDataSet(dsDataSourceDetail);
			datasChoicTre.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ��������Դ�б�ת�����ַ���
	 * 
	 * @param lstDataSource
	 *            ����Դ�б�
	 * @return
	 */
	private String getDataSource(List lstDataSource) {
		// �õ�ѡ������Դ�ı����ͼ����
		String sDataSource = "";
		String sTemp = null;
		// ѭ��ȡ��ѡ������Դ�ı����ͼ����
		for (int i = 0; i < lstDataSource.size(); i++) {
			// ȡ��ѡ������Դ�ı����ͼ����
			sTemp = "'" + ((Map) lstDataSource.get(i)).get(IDefineReport.DICID)
					+ "'";
			// ��֯�ɲ�ѯ����
			if ("".equals(sDataSource)) {
				sDataSource = sDataSource + sTemp;
			} else {
				sDataSource = sDataSource + "," + sTemp;
			}
		}
		return sDataSource;

	}

	/**
	 * ����Դѡ�а�ť����¼�
	 */
	private class DataSourceChoiceActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// ��������Դѡ����
			if (dataSourceChoice == null) {
				dataSourceChoice = new DataSourceChoiceUI(ReportGuideUI.this,
						definReportServ, lstDataSource);
			}
			// ����Դѡ���������ʾ
			Tools.centerWindow(dataSourceChoice);
			// ��ʾ����Դѡ����
			dataSourceChoice.setVisible(true);

			// �õ�ѡ�е�����Դ
			List lstTmp = dataSourceChoice.getDataSource();

			// �ж�ѡ�е�����Դ�Ƿ����˸ı�
			if (lstDataSource != null) {
				if (lstDataSource.equals(lstTmp)) {
					return;
				} else {
					lstDataSource = lstTmp;
				}
			} else { // ��ѡ�е�����Ϣ����lstDataSource
				lstDataSource = lstTmp;
			}
			// ������Դ������ʾ����Դ��Ϣ
			setDataSourceTreeValue(lstDataSource);

			// ��������Դ����Ϣ
			saveSummaryDataSourceManagerImpl(lstDataSource);
			// ����ö������Դ��Ϣ
			saveEnumSourceManager(lstDataSource);
			// ����Ĭ�ϲ���(��λdiv_code,����Batch_no,����Data_type)
			try {
				saveDefaultParameter(lstDataSource);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ReportGuideUI.this,
						"����Դѡ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * ��������Դ����Ϣ
	 * 
	 * @param lstDataSource
	 *            ����ԴList
	 */
	private void saveSummaryDataSourceManagerImpl(List lstDataSource) {
		if (lstDataSource == null)
			return;

		if (refColPriMap == null) {
			refColPriMap = getRefColPriCode();
		}

		// ѡ�е���������
		int iCount = lstDataSource.size();
		SummaryDataSourceImpl[] summaryDataSourceImpl = new SummaryDataSourceImpl[iCount];
		Map mapDataSource = null;
		// ����Դ����
		String sDataSourceName = null;
		// ���������ֵ�Dictionary
		DefaultDictionaryImpl defaultDictionaryImpl = null;
		String sSourceID = null;
		// ��������ԴDatasource,ѭ������ѡ�е�����Դ
		for (int i = 0; i < iCount; i++) {
			summaryDataSourceImpl[i] = new SummaryDataSourceImpl();
			mapDataSource = (Map) lstDataSource.get(i);
			// ����Դ����
			sDataSourceName = mapDataSource.get(IDefineReport.OBJECT_CNAME)
					.toString();
			summaryDataSourceImpl[i].setDataSourceName(sDataSourceName);
			// ����Դ����
			summaryDataSourceImpl[i]
					.setSourceType(getSourceType(mapDataSource));
			// ����ԴID
			sSourceID = mapDataSource.get(IDefineReport.DICID).toString();
			summaryDataSourceImpl[i].setSourceID(sSourceID);
			// sourceAlais����Դ����
			summaryDataSourceImpl[i].setSourceAlais(ReportUtil
					.translateToColumnName(i));
			// ����Դ���ƻ�Sql���
			summaryDataSourceImpl[i].setSource(getSource(mapDataSource));
			// ��������Դ����ȡ������Դ����ϸ�ֶ���Ϣ

			// ���������ֵ�Dictionary
			defaultDictionaryImpl = saveDataSourceDictionary(sSourceID);
			summaryDataSourceImpl[i].setDictionary(defaultDictionaryImpl);
			// ��������Դ��ö��Դ����Ϣ
			SummarySourceCol[] summarySourceCol = saveDataSourceColArray(sSourceID);
			summaryDataSourceImpl[i].setColArray(summarySourceCol);
		}
		// ��������Դ���ݹ���DataSourceManager
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();
		if (summaryDataSourceManagerImpl == null)
			summaryDataSourceManagerImpl = new SummaryDataSourceManagerImpl();
		summaryDataSourceManagerImpl.setDataSourceArray(summaryDataSourceImpl);
		querySource.setDataSourceManager(summaryDataSourceManagerImpl);
	}

	/**
	 * ����Դ����
	 * 
	 * @param mapDataSource
	 * @return
	 */
	private int getSourceType(Map mapDataSource) {
		String objectEname = mapDataSource.get(IDefineReport.OBJECT_ENAME)
				.toString();
		if (IDefineReport.TAB_FB_B_INFO.equals(objectEname)) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * ����Դsourceֵ
	 * 
	 * @param mapDataSource
	 * @return
	 */
	private String getSource(Map mapDataSource) {
		String objectEname = mapDataSource.get(IDefineReport.OBJECT_ENAME)
				.toString();
		if (IDefineReport.TAB_FB_B_INFO.equals(objectEname)) {
			return "select * from " + objectEname + " where report_id = '"
					+ mapDataSource.get(IDefineReport.DICID).toString() + "'";
		} else {
			return objectEname;
		}
	}

	/**
	 * �õ���������������Map
	 * 
	 * @return
	 */
	private Map getRefColPriCode() {
		Map resultMap = new HashMap();

		List lstRefColPriCode = DefineReportI.getMethod().getRefColPriCode();
		Map tempMap;
		for (int i = 0; i < lstRefColPriCode.size(); i++) {
			tempMap = (Map) lstRefColPriCode.get(i);
			resultMap.put(tempMap.get(ISysRefCol.REFCOL_ID), tempMap
					.get(ISysRefCol.LVL_FIELD));
		}
		return resultMap;
	}

	/**
	 * ���������ֵ�Dictionary
	 * 
	 * @param sObjectEName
	 *            ����ԴENameֵ
	 * @return ����Դ�����ֵ�Dictionary
	 */
	private DefaultDictionaryImpl saveDataSourceDictionary(String sObjectEName) {
		String sCode = null;
		String sName = null;
		Map mapField = null;
		DefaultDictionaryImpl defaultDictionaryImpl = new DefaultDictionaryImpl();
		// ��������ԴENameֵȡ���ֶ���ϸ��Ϣ
		List lstField = definReportServ.getFieldWithEname(sObjectEName);
		for (int i = 0; i < lstField.size(); i++) {
			mapField = (Map) lstField.get(i);
			sCode = mapField.get(IDefineReport.FIELD_ENAME).toString();
			sName = mapField.get(IDefineReport.FIELD_FNAME).toString();
			defaultDictionaryImpl.put(sCode, sName);
		}
		return defaultDictionaryImpl;
	}

	/**
	 * ��������Դ��ö��Դ����Ϣ
	 * 
	 * @param sObjectEName
	 *            ����ԴENameֵ
	 * @return ����Դ��ö��Դ�ж���
	 */
	private SummarySourceCol[] saveDataSourceColArray(String sObjectEName) {
		List lstEnum = definReportServ.getEnumWhere(sObjectEName);
		int iCount = lstEnum.size();
		Map mapEnum = null;
		// ����ö��Դ�ж���
		RefEnumSource refEnumSource = null;
		SummarySourceCol[] summarySourceCol = new SummarySourceCol[iCount];
		String sEnumCol = null;
		for (int i = 0; i < iCount; i++) {
			summarySourceCol[i] = new SummarySourceCol();
			mapEnum = (Map) lstEnum.get(i);
			// ����Դ���ֶ�����
			summarySourceCol[i].setColID(mapEnum.get(IDefineReport.FIELD_ENAME)
					.toString());
			// ����Դ���ֶ�����
			summarySourceCol[i].setColType(DefinePub.getFieldTypeWithCh(mapEnum
					.get(IDefineReport.FIELD_TYPE).toString()));
			// ����ö��Դ�ж���
			refEnumSource = new RefEnumSource();
			// ö��Դ��ID
			if (mapEnum.get(IDataDictBO.REFCOL_ID) != null) {
				refEnumSource.setEnumID(IDefineReport.ENUM_
						+ mapEnum.get(IDataDictBO.REFCOL_ID).toString());
			}
			// ��������Դ��ö�����ӹ�ϵĬ��Ϊ������
			refEnumSource.setJoinType(IDefineReport.LEFT_JOIN);
			// ö��Դ�б���
			refEnumSource.setRefEnumColCode(mapEnum.get(
					IDataDictBO.CON_FIELDENAME).toString());
			// ��ʶ�Ƿ��ǲ���ö��Դ����;0:������1:������
			if (refColPriMap.get(mapEnum.get(IDataDictBO.REFCOL_ID)) == null) {
				refEnumSource.setRefType(1);
			} else {
				sEnumCol = refColPriMap.get(mapEnum.get(IDataDictBO.REFCOL_ID))
						.toString();
				if (mapEnum.get(IDataDictBO.CON_FIELDENAME).toString()
						.equalsIgnoreCase(sEnumCol)) {
					refEnumSource.setRefType(0);
				} else {
					refEnumSource.setRefType(1);
				}
			}
			summarySourceCol[i].setRefEnumSource(refEnumSource);
		}
		return summarySourceCol;
	}

	/**
	 * ����ö������Դ��Ϣ
	 * 
	 * @param lstDataSource
	 *            ����ԴList
	 */
	private void saveEnumSourceManager(List lstDataSource) {
		if (lstDataSource == null)
			return;
		Map mapEnumSource = null;
		String sCode = null;
		String sName = null;
		// ��������Դ�б�ת�����ַ���
		String sDataSource = getDataSource(lstDataSource);
		List lstEnumSource = definReportServ.getEnumInfo(sDataSource);
		int iCount = lstEnumSource.size();
		SummaryEnumSourceImpl[] summaryEnumSourceImpl = new SummaryEnumSourceImpl[iCount];
		for (int i = 0; i < lstEnumSource.size(); i++) {
			summaryEnumSourceImpl[i] = new SummaryEnumSourceImpl();
			mapEnumSource = (Map) lstEnumSource.get(i);
			// enumIDö��ԴID
			summaryEnumSourceImpl[i].setEnumID(IDefineReport.ENUM_
					+ mapEnumSource.get(ISysRefCol.REFCOL_ID).toString());
			// enumType,1:Ҫ��,2:sql
			summaryEnumSourceImpl[i].setEnumType("2");
			// ö��Դ���� sourcetype ,1,������2��sql,
			summaryEnumSourceImpl[i].setSourceType("2");
			// ����enumSourceAlais
			summaryEnumSourceImpl[i].setEnumSourceAlais(IDefineReport.ENUM_
					+ mapEnumSource.get(ISysRefCol.REFCOL_ID).toString());
			// enumName
			summaryEnumSourceImpl[i].setEnumName(mapEnumSource.get(
					ISysRefCol.REFCOL_NAME).toString());
			// levelInfo
			summaryEnumSourceImpl[i].setLevelInfo(setLevel(mapEnumSource.get(
					ISysRefCol.LVL_STYLE).toString()));
			// levelCode
			summaryEnumSourceImpl[i].setLevelCode(mapEnumSource.get(
					ISysRefCol.LVL_FIELD).toString());
			// pk
			summaryEnumSourceImpl[i].setPk(mapEnumSource.get(
					ISysRefCol.PRIMARY_FIELD).toString());
			// Source
			summaryEnumSourceImpl[i].setSource(setSql(mapEnumSource.get(
					ISysRefCol.SQL_DET).toString()));
			// Dictionary
			DefaultDictionaryImpl defaultDictionaryImpl = new DefaultDictionaryImpl();
			sCode = mapEnumSource.get(ISysRefCol.CODE_FIELD).toString();
			sName = mapEnumSource.get(ISysRefCol.REFCOL_NAME).toString() + "����";
			defaultDictionaryImpl.put(sCode, sName);
			sCode = mapEnumSource.get(ISysRefCol.NAME_FIELD).toString();
			sName = mapEnumSource.get(ISysRefCol.REFCOL_NAME).toString() + "����";
			defaultDictionaryImpl.put(sCode, sName);
			summaryEnumSourceImpl[i].setDictionary(defaultDictionaryImpl);
		}

		SummaryEnumSourceManagerImpl SummaryEnumSourceManagerImpl = new SummaryEnumSourceManagerImpl();
		SummaryEnumSourceManagerImpl.setEnumSourceArray(summaryEnumSourceImpl);
		querySource.setEnumSourceManager(SummaryEnumSourceManagerImpl);
	}

	/**
	 * ȥ�������У���ݺ���������Ϣ
	 * 
	 * @param sSqlDet
	 * @return
	 */
	private String setSql(String sSqlDet) {
		sSqlDet = sSqlDet.toUpperCase();
		int index = sSqlDet.indexOf("ORDER");
		if (index != -1) {
			sSqlDet = sSqlDet.substring(0, index);
		}
		String sSetYear = "#SET_YEAR#";
		index = sSqlDet.indexOf(sSetYear);
		int startIndex = sSqlDet.lastIndexOf("SET_YEAR", index);
		if (index != -1 && startIndex != -1) {
			String value = sSqlDet.substring(startIndex, index
					+ sSetYear.length());
			sSqlDet = sSqlDet.replaceAll(value, "1=1");
		}

		return sSqlDet;
	}

	/**
	 * ����Ĭ�ϲ���(��λdiv_code,����Batch_no,����Data_type)
	 * 
	 * @param lstDataSource
	 * @throws Exception
	 */
	private void saveDefaultParameter(List lstDataSource) throws Exception {
		if (lstDataSource == null)
			return;
		// ����Ĭ�ϲ���(��λdiv_code)
		SummaryParameterImpl[] divCodeSummaryParameterImpl = saveDivCodeParameter(lstDataSource);
		// ����Ĭ�ϲ���(����Batch_no,����Data_type)
		SummaryParameterImpl[] batchNoSummaryParameterImpl = saveBatchNoParameter(lstDataSource);
		// �汾�Ų���
		SummaryParameterImpl verNoSummaryParameterImpl = saveVerNoParameter(lstDataSource);
		// ��λ����
		SummaryParameterImpl divNameSummaryParameterImpl = saveDivNameParameter();

		// �������
		SummaryParameterImpl[] summaryParameterImpl = divCodeSummaryParameterImpl;

		// ����batch_no����,����data_type����
		if (batchNoSummaryParameterImpl != null) {
			int iCount = batchNoSummaryParameterImpl.length;

			int iStart = 0;
			// �ж��Ƿ��Ѷ���
			if (summaryParameterImpl == null) {// δ����
				summaryParameterImpl = new SummaryParameterImpl[batchNoSummaryParameterImpl.length];
			} else {// �Ѷ��壬�������鳤��
				// ����ԭ���鳤��
				iStart = summaryParameterImpl.length;
				// �������ݳ���
				summaryParameterImpl = addArrayLength(summaryParameterImpl,
						iCount);
			}
			// ѭ��,�����κ����Ͳ�����������ܵĲ�������
			for (int i = 0; i < iCount; i++) {
				summaryParameterImpl[iStart + i] = batchNoSummaryParameterImpl[i];
			}
		}

		// ����汾����
		int iStart = 0;
		if (verNoSummaryParameterImpl != null) {
			// �ж��Ƿ��Ѷ���
			if (summaryParameterImpl == null) {// δ����
				summaryParameterImpl = new SummaryParameterImpl[batchNoSummaryParameterImpl.length];
			} else {// �Ѷ��壬�������鳤��
				// ����ԭ���鳤��
				iStart = summaryParameterImpl.length;
				// �������ݳ���
				summaryParameterImpl = addArrayLength(summaryParameterImpl, 1);
			}
			summaryParameterImpl[iStart] = verNoSummaryParameterImpl;
		}

		// �����������λ���Ʋ���
		iStart = 0;
		// �ж��Ƿ��Ѷ���
		if (summaryParameterImpl == null) {// δ����
			summaryParameterImpl = new SummaryParameterImpl[batchNoSummaryParameterImpl.length];
		} else {// �Ѷ��壬�������鳤��
			// ����ԭ���鳤��
			iStart = summaryParameterImpl.length;
			// �������ݳ���
			summaryParameterImpl = addArrayLength(summaryParameterImpl, 1);
		}
		summaryParameterImpl[iStart] = divNameSummaryParameterImpl;

		IParameter[] parameter = querySource.getParameterArray();
		if (parameter != null)
			for (int i = 0; i < parameter.length; i++) {
				if (DataSourceSet.checkDefaultPara(parameter[i].getChName())) {
					continue;
				}
				iStart = summaryParameterImpl.length;
				summaryParameterImpl = addArrayLength(summaryParameterImpl, 1);
				summaryParameterImpl[iStart] = (SummaryParameterImpl) parameter[i];
			}

		// Ĭ�ϲ���ֵ��������������
		querySource.setParameterArray(summaryParameterImpl);
	}

	/**
	 * �������ݳ���
	 * 
	 * @param summaryParameterImpl
	 *            ԭ����
	 * @param iNum
	 *            ���ӵĳ���
	 * @return
	 */
	public static SummaryParameterImpl[] addArrayLength(
			SummaryParameterImpl[] summaryParameterImpl, int iNum) {
		if (iNum <= 0)
			return summaryParameterImpl;
		SummaryParameterImpl[] tempSummaryParameterImpl = null;
		if (summaryParameterImpl == null) {
			tempSummaryParameterImpl = new SummaryParameterImpl[1];
		} else {
			int iOldCount = summaryParameterImpl.length;
			int iCount = iOldCount + iNum;
			tempSummaryParameterImpl = new SummaryParameterImpl[iCount];
			for (int i = 0; i < iOldCount; i++) {
				tempSummaryParameterImpl[i] = summaryParameterImpl[i];
			}
		}
		return tempSummaryParameterImpl;
	}

	/**
	 * ����Ĭ�ϲ���(��λdiv_code)
	 * 
	 * @param lstDataSource
	 *            ����ԴList
	 * @return ��λ��������
	 */

	private SummaryParameterImpl[] saveDivCodeParameter(List lstDataSource) {
		if (lstDataSource == null)
			return null;

		// ����Դ��¼��Ϣ
		Map mapDataSource = null;
		// ����Դ����
		int iCount = lstDataSource.size();

		SummaryParameterImpl[] summaryParameterImpl = new SummaryParameterImpl[iCount];
		// �������õ�����Դ����
		ToSource toSource[] = null;
		// ѭ������ѡ�е�����Դ
		for (int i = 0; i < iCount; i++) {
			summaryParameterImpl[i] = new SummaryParameterImpl();
			// �������õ�����Դ����
			toSource = new ToSource[1];
			toSource[0] = new ToSource();
			mapDataSource = (Map) lstDataSource.get(i);
			// ����ԴID
			toSource[0].setSourceID(mapDataSource.get(IDefineReport.DICID)
					.toString());
			// ��id
			toSource[0].setSourceColID(IDefineReport.DIV_CODE);
			// �Ƿ�������ö��Դ,��Ϊ�񣨲�������ö��Դ)
			toSource[0].setToEnumSource(false);
			// ����ID
			summaryParameterImpl[i].setName(DefinePub.getRandomUUID());
			// ��������
			summaryParameterImpl[i].setChName(IDefineReport.DIV_CODE);
			// ������������Դ
			summaryParameterImpl[i].setToSourceArray(toSource);
		}

		return summaryParameterImpl;
	}

	/**
	 * ����Ĭ�ϲ���(����Batch_no,����Data_type)
	 * 
	 * @param lstDataSource����ԴList
	 * @return �����������(0:����Batch_no,1:����Data_type)
	 */
	private SummaryParameterImpl[] saveBatchNoParameter(List lstDataSource) {
		if (lstDataSource == null)
			return null;
		// ѭ������
		int iCount = 0;
		// ����Դ��¼��Ϣ
		Map mapDataSource = null;
		// ������Ҫ���β�ѯ����������Դ
		List lstIsBatchNoDataSource = null;
		// ���α��
		Object tempObject = null;
		String sIsBatchNo = null;
		// ����Դ����
		iCount = lstDataSource.size();
		// ѭ������ѡ�е�����Դ,�Ƿ�������β�ѯ����
		for (int i = 0; i < iCount; i++) {
			mapDataSource = (Map) lstDataSource.get(i);
			// �õ�������Ϣ
			tempObject = mapDataSource.get(IDefineReport.IS_BATCHNO);
			if (tempObject != null) {// �ж�����Դ�Ƿ�Ҫ�����β�ѯ����
				sIsBatchNo = tempObject.toString();
			} else {// isBatch_noΪnull,Ĭ��Ϊ����Ҫ���β�ѯ����
				continue;
			}
			// ����Batch_no,����Data_type����
			if (IDefineReport.TRUE_FLAG.equals(sIsBatchNo)) {
				if (lstIsBatchNoDataSource == null) {
					lstIsBatchNoDataSource = new ArrayList();
				}
				lstIsBatchNoDataSource.add(mapDataSource);
			}
		}
		// ��ѡ������Դ����������β�ѯ����������null
		if (lstIsBatchNoDataSource == null)
			return null;

		// �õ�����������������Դ����
		int iBatchNoNum = lstIsBatchNoDataSource.size();

		// 0:����Batch_no,1:����Data_type
		SummaryParameterImpl summaryParameterImpl[] = new SummaryParameterImpl[2];
		// ������������Դ����
		ToSource[] toSource = null;
		iCount = summaryParameterImpl.length;
		for (int i = 0; i < iCount; i++) {
			summaryParameterImpl[i] = new SummaryParameterImpl();
			// id
			summaryParameterImpl[i].setName(DefinePub.getRandomUUID());
			// compareType,�Ƚ����ͣ���Ϊ"="
			summaryParameterImpl[i].setCompareType(IDefineReport.EQUAL_FLAG);
			// �������õ�����Դ����
			toSource = new ToSource[iBatchNoNum];
			// ѭ����������������������Դ
			for (int j = 0; j < iBatchNoNum; j++) {
				mapDataSource = (Map) lstIsBatchNoDataSource.get(j);
				toSource[j] = new ToSource();
				// ����ԴID
				toSource[j].setSourceID(mapDataSource.get(IDefineReport.DICID)
						.toString());
				// �Ƿ�������ö��Դ,��Ϊ�񣨲�������ö��Դ)
				toSource[j].setToEnumSource(false);
				if (i == 0) {// 0:����Batch_no
					toSource[j].setSourceColID(IDefineReport.BATCH_NO);
				} else if (i == 1) {// 1:����Data_type
					toSource[j].setSourceColID(IDefineReport.DATA_TYPE);
				}
			}

			summaryParameterImpl[i].setToSourceArray(toSource);
		}
		// ����batch_no,��������
		summaryParameterImpl[0].setChName(IDefineReport.BATCH_NO);
		// ����data_type,��������
		summaryParameterImpl[1].setChName(IDefineReport.DATA_TYPE);

		return summaryParameterImpl;
	}

	/**
	 * ����Ĭ�ϲ���(����Batch_no,����Data_type)
	 * 
	 * @param lstDataSource����ԴList
	 * @return �����������(0:����Batch_no,1:����Data_type)
	 * @throws Exception
	 */
	private SummaryParameterImpl saveVerNoParameter(List lstDataSource)
			throws Exception {
		if (lstDataSource == null)
			return null;
		// ����Դ����
		int iCount = lstDataSource.size();

		List lstVerNo = new ArrayList();

		String isVerNo = null;
		// ѭ����������������������Դ
		for (int i = 0; i < iCount; i++) {
			isVerNo = ((Map) lstDataSource.get(i)).get(IDefineReport.SUP_VER)
					.toString();
			// �ж��Ƿ���Ҫ�汾������
			if (Common.estimate(isVerNo)) {
				lstVerNo.add(lstDataSource.get(i));
			}
		}

		ToSource[] toSource = null;
		iCount = lstVerNo.size();
		if (iCount > 0) {
			toSource = new ToSource[iCount];
		} else {
			return null;
		}

		Map mapDataSource;
		for (int i = 0; i < iCount; i++) {
			mapDataSource = (Map) lstVerNo.get(i);
			toSource[i] = new ToSource();
			// ����ԴID
			toSource[i].setSourceID(mapDataSource.get(IDefineReport.DICID)
					.toString());
			toSource[i].setToEnumSource(false);
			toSource[i].setSourceColID(IDefineReport.VER_NO);
		}
		SummaryParameterImpl summaryParameterImpl = new SummaryParameterImpl();
		summaryParameterImpl.setToSourceArray(toSource);

		// id
		summaryParameterImpl.setName(DefinePub.getRandomUUID());
		// compareType,�Ƚ����ͣ���Ϊ"="
		summaryParameterImpl.setCompareType(IDefineReport.EQUAL_FLAG);
		summaryParameterImpl.setChName(IDefineReport.VER_NO);

		return summaryParameterImpl;
	}

	/**
	 * ���浥λ���Ʋ�����Ĭ����ʾ�ڱ��������һ�У������һ����Ԫ��
	 * 
	 */
	private SummaryParameterImpl saveDivNameParameter() {
		SummaryParameterImpl summaryParameterImpl = new SummaryParameterImpl();
		summaryParameterImpl.setName(IDefineReport.DIVNAME_PARA);
		summaryParameterImpl.setChName(IDefineReport.DIVNAME_PARA);
		summaryParameterImpl.setValue("ҵ���ҡ����ܾ֡���λ����");
		return summaryParameterImpl;
	}

	/**
	 * ���ò�����ʽ
	 * 
	 * @param sLvlCode
	 * @return
	 */
	private String setLevel(String sLvlCode) {
		Pattern codeSplitter = Pattern.compile("\\s*\\|\\s*");
		String[] ruleArray = codeSplitter.split(sLvlCode);
		if (ruleArray.length == 0) {
			return "";
		}
		String sLvlCodeValue = ruleArray[0];
		int iLvlCodeValue = Integer.parseInt(ruleArray[0]);
		for (int i = 1; i < ruleArray.length; i++) {
			sLvlCodeValue = sLvlCodeValue
					+ "-"
					+ String.valueOf(Integer.parseInt(ruleArray[i])
							- iLvlCodeValue);
			iLvlCodeValue = Integer.parseInt(ruleArray[i]);
		}
		return sLvlCodeValue;
	}

	/**
	 * ����Դ���ð�ť����¼�
	 */
	private class DataSourceSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// ��������Դ������
			dataSourceSet = new DataSourceSet(ReportGuideUI.this);
			// ����Դ���ô��������ʾ
			Tools.centerWindow(dataSourceSet);
			// ��ʾ����Դ���ô���
			try {
				dataSourceSet.setVisible(true);
			} finally {
				dataSourceSet.dispose();
			}

		}
	}

	public DataSet getDataSource() {
		return datasChoicTre.getDataSet();
	}

	public SummaryReportPane getReport() {
		return this.fpnlDefineReport.designGroupReportPane;
	}

}
