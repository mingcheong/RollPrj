/**
 * @# RowSetUI.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.rowset.ibs.IRowSet;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * ����˵�����в�ѯ��ƽ���
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class RowSetUI extends FModulePanel implements IRowSelectchangeListener {

	/**
	 * 
	 */

	// public static final String TREE_CODE = "CODE";
	//
	// public static final String TREE_FNAME = "FIELD_FNAME";
	//
	// public static final String TREE_ENAME = "OBJECT_ENAME";
	//
	// public static final String TREE_FIELDNAME = "FIELD_ENAME";
	private static final long serialVersionUID = 2851878145388957735L;

	private FDragTree treDataSource;

	private RowPanel pnlRow;

	private ReportInfoPanel pnlReport;

	private RowDetailPanel pnlDetail;

	private IRowSet rowSetBO;

	public JFrame frame = null;

	List lstRows = new ArrayList();

	List lstCons = new ArrayList();

	String reportID = null;

	XMLData xmlReport;

	XMLData xmlTableSource = new XMLData();

	XMLData xmlField = new XMLData();// �˴���Ϊ������ʽ����һ����ʾ�����ͱ��е�XMLData,

	// �ڶ������ֶ�����������

	List lstDataSource;

	String setYear;

	List lstType = new ArrayList();

	DefineReport defineReport;

	// ������
	public RowSetUI() {
		this.reportID = "5000030002";
		init();
	}

	public RowSetUI(String reportID, String setYear, DefineReport defineReport) {
		this.reportID = reportID;
		this.setYear = setYear;
		this.defineReport = defineReport;
		init();
	}

	public void init() {
		try {
			initData();
			// ����Դ��
			treDataSource = new FDragTree();
			FScrollPane spnlTree = new FScrollPane();
			spnlTree.getViewport().add(treDataSource);
			treDataSource.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && e.getButton() == 1) {
						XMLData aData = treDataSource.getSelectedData();
						if (aData == null)
							return;
						pnlDetail.appendField(aData);
					}
				}
			});
			treDataSource.setData(lstDataSource);

			pnlReport = new ReportInfoPanel(xmlReport, lstType);

			// ����
			pnlRow = new RowPanel(lstRows, lstCons, reportID, xmlTableSource,
					xmlField, xmlReport, Common.isNullStr(reportID), pnlReport,
					defineReport);
			// ����
			pnlDetail = new RowDetailPanel(this);
			pnlRow.addSelectChangeListener(pnlDetail);
			pnlRow.addSelectChangeListener(this);
			pnlRow.addTitleChangeedListener(pnlDetail);

			JTabbedPane tpnlBottom = new JTabbedPane();
			tpnlBottom.add("������Ϣ", pnlReport);
			tpnlBottom.add("����ϸ����", pnlDetail);

			JSplitPane spBack = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			JSplitPane spRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			spRight.add(pnlRow, JSplitPane.TOP);
			spRight.add(tpnlBottom, JSplitPane.BOTTOM);
			spBack.add(spnlTree, JSplitPane.LEFT);
			spRight.setDividerLocation(new Double((Toolkit.getDefaultToolkit()
					.getScreenSize().getHeight() - 20) / 2).intValue());
			spBack.add(spRight, JSplitPane.RIGHT);

			spBack.setDividerLocation(300);

			this.add(spBack);
		} catch (Exception e) {

			e.printStackTrace();
			new MessageBox(frame, "�����ʼ��ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return;
		}

	}

	public void setReportID(String sReportID) {
		if (Common.isNullStr(sReportID)) {
			new MessageBox(frame, "û��ָ������ID!,�޷���ʼ��!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
		// ˢ����ͼ

	}

	private void initData() throws Exception {
		rowSetBO = (IRowSet) BOCache.getBO("rp.rowSetService");
		if (!Common.isNullStr(reportID)) {
			xmlReport = rowSetBO.getRepset(Global.getSetYear(), 2, reportID);
			if (xmlReport == null)
				throw new Exception("û�ҵ�ָ���ı�����Ϣ��");
			lstRows = rowSetBO.getReportRows(reportID, Global.getSetYear());
			lstCons = rowSetBO.getReportCons(reportID, Global.getSetYear());
		} else {// �ֶ���ʼ��������Ϣ
			xmlReport = new XMLData();
			xmlReport.put(IQrBudget.SET_YEAR, setYear);
			xmlReport.put(IQrBudget.TYPE_NO, "1");
			xmlReport.put(IQrBudget.REPORT_TYPE, "50");
			xmlReport.put(IQrBudget.REPORT_CNAME, "δ����");
			xmlReport.put(IQrBudget.TITLE, "��");
			xmlReport.put(IQrBudget.REPORT_SOURCE, "����");
			xmlReport.put(IQrBudget.IS_PASSVERIFY, "��");
			xmlReport.put(IQrBudget.IS_ACTIVE, "��");
			xmlReport.put(IQrBudget.DATA_USER, "2");
			xmlReport.put(IQrBudget.IS_HASBATCH, "��");
			xmlReport.put(IQrBudget.IS_MULTICOND, "��");
			xmlReport.put(IQrBudget.IS_END, "1");
			xmlReport.put(IQrBudget.TYPE_FLAG, "2");
			xmlReport.put(IQrBudget.REPORT_ID, reportID);
			xmlReport.put(IQrBudget.FIELD_COLUMN, new Integer(1));
		}
		try {
			lstDataSource = rowSetBO.getDataSoureDetail();
		} catch (Exception e) {
			new MessageBox(frame, "ȡ������Դ����!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
			return;
		}
		if (lstDataSource == null || lstDataSource.size() == 0)
			return;
		int iCount = lstDataSource.size();
		XMLData aData;
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstDataSource.get(i);
			if (IDataDictBO.MARK_TYPE.equals(Common.getAStringField(aData,
					IDataDictBO.fieldMark)))
				continue;
			if (IDataDictBO.MARK_TABLE.equals(Common.getAStringField(aData,
					IDataDictBO.fieldMark))) {// ���
				xmlTableSource.put(aData.get(IDataDictBO.DICID), aData
						.get(IDataDictBO.AFIELD_CNAME));
			} else {// ���ֶ�
				Object objTable = aData.get(IDataDictBO.DICID);
				Object obj = xmlField.get(objTable);
				if (obj == null) {// �����û�д��ű����Ϣ
					List lstData = new ArrayList();
					List lstFieldEname = new ArrayList();
					List lstFieldCname = new ArrayList();
					lstData.add(lstFieldEname);
					lstData.add(lstFieldCname);
					xmlField.put(objTable, lstData);
					lstFieldEname.add(aData.get(IDataDictBO.AFIELD_ENAME));
					lstFieldCname.add(aData.get(IDataDictBO.AFIELD_CNAME));
				} else {
					((List) ((List) obj).get(0)).add(aData
							.get(IDataDictBO.AFIELD_ENAME));
					((List) ((List) obj).get(1)).add(aData
							.get(IDataDictBO.AFIELD_CNAME));
				}
			}

		}

	}

	// ȡ������ѡ����ֶ���Ϣ
	public XMLData getSelectField() {
		return treDataSource.getSelectedData();

	}

	// ѡ�е�һ��ƥ��Ľڵ�,ʹ�õݹ�
	public boolean signFirstBySpecMutiFields(String[] fields, String[] values,
			DefaultMutableTreeNode aNode) {
		// treDataSource.collapsePath(new TreePath(aNode.getPath()));
		if (fields == null || values == null)
			return false;
		if (aNode == null)
			aNode = treDataSource.getRoot();
		// ���ж�����
		if ((PfTreeNode) aNode.getUserObject() != null) {
			XMLData aData = (XMLData) ((PfTreeNode) aNode.getUserObject())
					.getCustomerObject();
			if (aData != null) {
				int i = 0;
				for (; i < fields.length; i++) {
					Object obj = aData.get(fields[i]);
					if (obj == null)
						break;
					if (!obj.toString().equals(values[i]))
						break;
				}
				if (i == fields.length) {// ��������
					// modified by xxl 20081007�޸�������Ӧ�¼���û�в�ѯ������Դ�Ĳ�չ����ϲ�
					treDataSource
							.setSelectionPath(new TreePath(aNode.getPath()));
					treDataSource.expandPath(new TreePath(aNode.getPath()));
					return true;
				}

			}
		}
		// ���û�ҵ�������
		int iChildCount = aNode.getChildCount();
		if (iChildCount < 1)
			return false;
		for (int i = 0; i < iChildCount; i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) aNode
					.getChildAt(i);
			if (signFirstBySpecMutiFields(fields, values, childNode))
				return true;
		}

		return false;

	}

	public void collapsetree(FTree aTree) {
		DefaultMutableTreeNode aNode = aTree.getRoot();
		if (aNode == null)
			return;
		Enumeration em = aNode.depthFirstEnumeration();
		while (em.hasMoreElements()) {
			aTree.collapsePath(new TreePath(((DefaultMutableTreeNode) em
					.nextElement()).getPath()));
		}

	}

	public void selectChange(RowInfo aRow, boolean isOnDrag) {
		// ���ز������ϵĽڵ�
		if (isOnDrag)
			return;
		if (treDataSource.getRoot() == null)
			return;

		treDataSource.expandPath(new TreePath(treDataSource.getRoot()));
		if (aRow == null)
			return;
		if (Common.isNullStr(aRow.getTableName()))
			return;
		if (signFirstBySpecMutiFields(new String[] { IDataDictBO.DICID },
				new String[] { aRow.getSourceID() }, treDataSource.getRoot())) {
			collapsetree(treDataSource);
			signFirstBySpecMutiFields(new String[] { IDataDictBO.DICID },
					new String[] { aRow.getSourceID() }, treDataSource
							.getRoot());
		}

	}

	public void initize() {
		// TODO �Զ����ɷ������

	}
}
