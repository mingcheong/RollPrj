/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.HeadlessException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.rowset.ui.RowSetFrame;
import gov.nbcs.rp.queryreport.szzbset.ui.ConverSet;
import gov.nbcs.rp.queryreport.szzbset.ui.SzzbSetFrame;

import gov.nbcs.rp.sys.besqryreport.ui.BesUI;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:�������������
 * </p>
 * <p>
 * Description:�������������
 * </p>
 * <p>

 */
public class DefineReport extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// �������ݿ�ӿ�
	private IDefineReport definReportServ = null;

	// ������
	private CustomTree ftreReport = null;

    //	 �������
	private FTextField reportNameCode = null;
	// ��������
	private FTextField reportNameTxt = null;

	// �������
	private ReportTypeList reportTypeList;

	// �����û�����
	private FRadioGroup reportUserTypeGrp = null;

	// �Ƿ��������������ٻ���
	// private FRadioGroup frdoIsMoneyOp = null;

	// // �Ƿ�����
	// private FCheckBox fchkIsMulYear = null;
	//
	// // �Ƿ��Ƿ������
	// private FCheckBox fchkIsMulRgion = null;

	// �Ƿ�����
	private FCheckBox fchkIsActice = null;

	// ��������
	private FComboBox cbxReoportSort = null;

	// ȡ�ñ������͵���ֵ
	private List lstReportSort = null;

	// ����������Ϣ
	private RepSetObject repSetObject = null;

	// �õ�������Ϣ
	private DataSet dsReport = null;

	public void initize() {
		// �������ݿ�ӿ�
		definReportServ = DefineReportI.getMethod();

		try {
			// �õ�������Ϣ
			dsReport = definReportServ.getReport();
			// �������õı���
			ftreReport = new CustomTree("����ı���", dsReport,
					IDefineReport.SHOW_LVL, IQrBudget.REPORT_CNAME,
					IDefineReport.PAR_ID, null, IQrBudget.LVL_ID,false);

			// ��������ѡ���¼�
			ftreReport.addTreeSelectionListener(new TreeSelect());
			// ����������
			FScrollPane spnlReport = new FScrollPane(ftreReport);

			ReportInfoPanel reportInfoPanel = new ReportInfoPanel();

			// �������ҷָ����
			FSplitPane slpnlBack = new FSplitPane();
			slpnlBack.setDividerLocation(300);
			slpnlBack.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			slpnlBack.add(spnlReport, JSplitPane.LEFT);
			slpnlBack.add(reportInfoPanel, JSplitPane.RIGHT);
			this.add(slpnlBack);

			// ����������
			this.createToolBar();
		} catch (Exception e) {
			new MessageBox("��ʾ���淢�����󣬴�����Ϣ:" + e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK + MessageBox.BUTTON_CANCEL).show();
			e.printStackTrace();
		}

	}

	private class ReportInfoPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public ReportInfoPanel() {
			jbInit();
		}

		private void jbInit() {

			// ���屨�������ı���
//			 ���屨�������ı���
			reportNameCode = new FTextField("������룺");
			reportNameCode.setEditable(false);
			reportNameTxt = new FTextField("�������ƣ�");
			reportNameTxt.setEditable(false);

			// ���������Ϣ
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("�������ͣ�");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));
			reportTypeList.setIsCheckBoxEnabled(false);

			// ȡ�ñ����������ֵ
			lstReportSort = definReportServ.getReportSort();

			// ���屨������
			Vector reportTypeVertor = new Vector();
			Map reportTypeMa = null;
			String report_type;
			for (int i = 0; i < lstReportSort.size(); i++) {
				reportTypeMa = (Map) lstReportSort.get(i);
				report_type = reportTypeMa.get("reporttype_name").toString();
				reportTypeVertor.add(report_type);
			}
			// ��������
			cbxReoportSort = new FComboBox("�������ࣺ");
			cbxReoportSort.setRefModel(reportTypeVertor);
			cbxReoportSort.setEnabled(false);

			// ���屨���û�����
			reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
			reportUserTypeGrp.setRefModel("0#����λʹ�� +1#������ʹ��+2#������λ��ͬʹ��");
			reportUserTypeGrp.setTitleVisible(false);
			reportUserTypeGrp.setValue("0");
			reportUserTypeGrp.setEditable(false);

			// ���屨���û��������
			FPanel reportUserTypePnl = new FPanel();
			reportUserTypePnl.setTitle("�û�����");
			reportUserTypePnl.setLayout(new RowPreferedLayout(1));
			// �����û����͵�ѡ����뱨���û��������
			reportUserTypePnl.addControl(reportUserTypeGrp,
					new TableConstraints(1, 1, true, true));

			// // �Ƿ��������������ٻ���
			// frdoIsMoneyOp = new FRadioGroup("", FRadioGroup.HORIZON);
			// frdoIsMoneyOp.setRefModel("0#����������+1#����������ٻ���+2#���ܺ�����������");
			// frdoIsMoneyOp.setTitleVisible(false);
			// frdoIsMoneyOp.setValue("0");
			//
			// FPanel isMoneyOpPnl = new FPanel();
			// isMoneyOpPnl.setTitle("��������ѡ��");
			// isMoneyOpPnl.setLayout(new RowPreferedLayout(1));
			// isMoneyOpPnl.addControl(frdoIsMoneyOp, new TableConstraints(1, 1,
			// true, true));
			//
			// // �Ƿ�����
			// fchkIsMulYear = new FCheckBox("�Ƿ�����");
			// fchkIsMulYear.setTitlePosition("RIGHT");
			//
			// // �Ƿ������
			// fchkIsMulRgion = new FCheckBox("�Ƿ������");
			// fchkIsMulRgion.setTitlePosition("RIGHT");

			// �Ƿ�����
			fchkIsActice = new FCheckBox("�Ƿ�����");
			fchkIsActice.setTitlePosition("RIGHT");
			fchkIsActice.setEditable(false);

			// ���������ʾ���
			FPanel leftPanel = new FPanel();
			leftPanel.setTitle("������Ϣ");
			RowPreferedLayout leftRlay = new RowPreferedLayout(1);
			leftRlay.setRowGap(8);
			leftPanel.setLayout(leftRlay);

			// ���������ı�����������ʾ���
			leftPanel.addControl(reportNameCode, new TableConstraints(1, 1,
					false, true));
			leftPanel.addControl(reportNameTxt, new TableConstraints(1, 1,
					false, true));
			// ��������
			leftPanel.addControl(cbxReoportSort, new TableConstraints(1, 1,
					false, true));
			// ���屨���û����������������ʾ���
			leftPanel.addControl(reportUserTypePnl, new TableConstraints(2, 1,
					false, true));

			// // �Ƿ��������������ٻ���ѡ�����������ʾ���
			// leftPanel.addControl(isMoneyOpPnl, new TableConstraints(2, 1,
			// false, true));
			// // �Ƿ�����ѡ�����������ʾ���
			// leftPanel.addControl(fchkIsMulYear, new TableConstraints(1, 1,
			// false, true));
			// // �Ƿ��Ƿ������ѡ�����������ʾ���
			// leftPanel.addControl(fchkIsMulRgion, new TableConstraints(1, 1,
			// false, true));

			// ���������Ͽ���������ʾ���
			leftPanel.addControl(fpnlReportType, new TableConstraints(5, 1,
					false, true));
			// �Ƿ�����
			leftPanel.addControl(fchkIsActice, new TableConstraints(1, 1,
					false, true));

			// ���ò���
			this.setLayout(new RowPreferedLayout(2));
			this.addControl(leftPanel, new TableConstraints(1, 1, true, true));

		}
	}

	/**
	 * ������ѡ�нڵ�ı��¼�
	 */
	private class TreeSelect implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
			try {
				repSetObject = new RepSetObject();
				BeanUtil.mapping(repSetObject, dsReport.getOriginData());
				// ��������
				String sReportCode = repSetObject.getREPORT_ID();
				reportNameCode.setValue(sReportCode);
				String sReportName = repSetObject.getREPORT_CNAME();
				reportNameTxt.setValue(sReportName);

				// // �Ƿ��������������ٻ���
				// int isMoneyOp = repSetObject.getIS_MONEYOP();
				// frdoIsMoneyOp.setValue(String.valueOf(isMoneyOp));
				// // �Ƿ�����
				// int isMulYear = repSetObject.getIS_MONEYOP();
				// ((JCheckBox) fchkIsMulYear.getEditor()).setSelected(Common
				// .estimate(new Integer(isMulYear)));
				// // �Ƿ������
				// int isMulRgion = repSetObject.getIS_MULRGION();
				// ((JCheckBox) fchkIsMulRgion.getEditor()).setSelected(Common
				// .estimate(new Integer(isMulRgion)));

				// ���屨�����
				reportTypeList.setSelected(repSetObject.getREPORT_ID());
				// ���屨���û�����
				String sUserType = String.valueOf(repSetObject.getDATA_USER());
				reportUserTypeGrp.setValue(sUserType);

				int iTypeFalg = repSetObject.getTYPE_FLAG();
				String sValue = null;
				Map reportTypeMa;
				for (int i = 0; i < lstReportSort.size(); i++) {
					reportTypeMa = (Map) lstReportSort.get(i);
					if (Integer.parseInt(reportTypeMa.get("reporttype_id")
							.toString()) == iTypeFalg) {
						sValue = reportTypeMa.get("reporttype_name").toString();
					}
				}
				cbxReoportSort.setValue(sValue);

				// �Ƿ������
				String sIsActice = repSetObject.getIS_ACTIVE();
				if ("��".equals(sIsActice))
					((JCheckBox) fchkIsActice.getEditor()).setSelected(true);
				else
					((JCheckBox) fchkIsActice.getEditor()).setSelected(false);

			} catch (Exception e1) {
				new MessageBox("�������󣬴�����Ϣ:" + e1.getMessage(),
						MessageBox.ERROR, MessageBox.BUTTON_OK
								+ MessageBox.BUTTON_CANCEL).show();
				e1.printStackTrace();
			}
		}
	}

	/**
	 * ���ӱ���
	 */
	public void doAdd() {
		// ���屨������ѡ����
		DefineReportTypeChoice reportTypeChoice = new DefineReportTypeChoice();
		// ����ѡ��ı�������
		int reportType;
		try {
			Tools.centerWindow(reportTypeChoice);
			reportTypeChoice.setVisible(true);
			// �õ�ѡ�еı�������
			reportType = reportTypeChoice.getReportType();
		} finally {
			reportTypeChoice.dispose();
		}
		// �ж��Ƿ�ѡ�б���
		if (reportType == -1)
			return;

		// ���ݱ������ͣ����ò�ͬ������������
		switch (reportType) {
		case IDefineReport.REPORTTYPE_COVER: {// Ŀ¼�����
			new ConverSet(null, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_SZZB: {// ��֧�ܱ�����
			new SzzbSetFrame("", Global.loginYear, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_ROW: {// ��λ�ۺ������
			new RowSetFrame("", Global.loginYear, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_GROUP: {// ������ܱ�����
			JFrame reportGuide = new ReportGuideUI(null, this);
			reportGuide.setVisible(true);
			break;
		}
		case IDefineReport.REPORTTYPE_OTHER: {// ������������
			new BesUI(null, this).show();
			break;
		}
		}
	}

	/**
	 * ɾ������
	 */
	public void doDelete() {
		try {
			// ɾ��ǰ���
			if (ftreReport.getSelectedNode() == null
					|| ftreReport.getSelectionPath().getLastPathComponent() == ftreReport
							.getRoot()
					|| ftreReport.getDataSet().fieldByName("end_flag")
							.getString().equals("0")) {
				new MessageBox("��ѡ��Ҫɾ���ı���!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			// ɾ��ǰ��ʾ
			String sReportName = ftreReport.getDataSet().fieldByName(
					"report_cname").getString();
			String sReportID = ftreReport.getDataSet().fieldByName("report_id")
					.getString();
			String isPrivate = ftreReport.getDataSet().fieldByName("c1")
			.getString();
			if(!GlobalEx.isFisVis()&&!"1".equals(isPrivate))
			{	
			
				JOptionPane.showMessageDialog(Global.mainFrame, "�Ǹ��˱�������ɾ��!"
						);
			return;
			}
				
				
			if (JOptionPane.showConfirmDialog(this, "ȷ��Ҫɾ������:[" + sReportID
					+ "]" + sReportName + "  ��?", "��ʾ",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return;
			// ִ��ɾ��
			String sErr = definReportServ.deleteReport(sReportID, Global
					.getSetYear());
			if (!sErr.equals("")) {
				new MessageBox(sErr, MessageBox.MESSAGE, MessageBox.BUTTON_OK)
						.show();
				return;
			}

			// ˢ������ɾ���ڵ�ʱ
			refreshNodeDel();

			new MessageBox("ɾ���ɹ�!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox("ɾ������ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return;
		}

	}

	/**
	 * ˢ������ɾ���ڵ�ʱ
	 * 
	 * @throws Exception
	 */
	private void refreshNodeDel() throws Exception {
		DataSet ds = ftreReport.getDataSet();
		String curNodeReportID = ds.fieldByName(IQrBudget.REPORT_ID)
				.getString();
		removeNode(curNodeReportID);
	}

	/**
	 * ���ݱ���ID�Ƴ����ڵ�
	 * 
	 * @param sReportId����ID
	 * @throws Exception
	 */
	private void removeNode(String sReportId) throws Exception {
		DataSet ds = ftreReport.getDataSet();
		MyTreeNode node;
		// ɾ��ͬһ����������ڵ�
		DefaultMutableTreeNode root = ftreReport.getRoot();
		Enumeration enume = root.breadthFirstEnumeration();
		while (enume.hasMoreElements()) {
			node = (MyTreeNode) enume.nextElement();
			ds.gotoBookmark(node.getBookmark());
			if (sReportId.equals(ds.fieldByName(IQrBudget.REPORT_ID)
					.getString())) {
				node.removeFromParent();
				ds.delete();
			}
		}
		if (ds.getState() != StatefulData.DS_BROWSE)
			ds.applyUpdate();
		ftreReport.updateUI();
	}

	/**
	 * �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
	 * 
	 * @param repSetObjectA
	 * @param lstType
	 * @throws Exception
	 */
	public void refreshNodeEdit(RepSetObject repSetObjectA, List lstType,
			String sReportId) throws Exception {
		// �޸Ľڵ�
		if (!Common.isNullStr(sReportId)) {
			// ���ݱ���ID�Ƴ����ڵ�
			removeNode(sReportId);
		}

		DataSet ds = ftreReport.getDataSet();
		ds.maskDataChange(true);

		Map mapData;
		int size = lstType.size();
		for (int i = 0; i < size; i++) {
			mapData = new HashMap();
			repSetObjectA.setSHOW_LVL(repSetObjectA.getREPORT_ID()
					+ lstType.get(i));
			repSetObjectA.setPAR_ID(lstType.get(i).toString());
			BeanUtil.decodeObject(mapData, repSetObjectA);
			ds.append();
			ds.setOriginData(mapData);
		}
		ds.applyUpdate();
		ds.maskDataChange(false);
		ftreReport.reset();
//		ftreReport.reset();
		// ��λ�������ӵĽڵ�
		ftreReport.expandTo(IDefineReport.SHOW_LVL, repSetObjectA
				.getREPORT_ID()
				+ lstType.get(0));
	}
	/**
	 * �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
	 * 
	 * @param ds
	 * @param lstType
	 * @throws Exception
	 */
	public void refreshNodeEdit(DataSet ds, List lstType, String sReportId)
			throws Exception {
		RepSetObject repSetObjectTmp = new RepSetObject();
		BeanUtil.mapping(repSetObjectTmp, ds.getOriginData());
		this.refreshNodeEdit(repSetObjectTmp, lstType, sReportId);
	}

	/**
	 * �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
	 * 
	 * @param ds
	 * @param lstType
	 * @throws Exception
	 */
	public void refreshNodeEdit(XMLData report, List lstType, String sReportId)
			throws Exception {
		RepSetObject repSetObjectTmp = new RepSetObject();
		BeanUtil.mapping(repSetObjectTmp, report);
		this.refreshNodeEdit(repSetObjectTmp, lstType, sReportId);
	}

	public void doCancel() {

	}

	public void doInsert() {

	}

	public void doModify() {
		// ɾ��ǰ���
		if (ftreReport.getSelectedNode() == null
				|| ftreReport.getSelectionPath().getLastPathComponent() == ftreReport
						.getRoot() || !ftreReport.getSelectedNode().isLeaf()) {
			new MessageBox("��ѡ��Ҫ�޸ĵı���!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
//		try {
//			if(!GlobalEx.isFisVis()&&!"1".equals(ftreReport.getDataSet().fieldByName("c1")
//					.getString().toString()))
//			{	
//			
//				JOptionPane.showMessageDialog(Global.mainFrame, "�Ǹ��˱�����λ���޸�!"
//						);
//			return;
//			}
//		} catch (HeadlessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// ����ѡ��ı�������
		int typeFlag = repSetObject.getTYPE_FLAG();

		// ���ݱ������ͣ����ò�ͬ������������
		switch (typeFlag) {
		case IDefineReport.REPORTTYPE_COVER: {// Ŀ¼�����
			new ConverSet(repSetObject, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_SZZB: {// ��֧�ܱ�����
			new SzzbSetFrame(repSetObject.getREPORT_ID(), Global.loginYear,
					this).show();

			break;
		}
		case IDefineReport.REPORTTYPE_ROW: {// ��λ�ۺ������
			new RowSetFrame(repSetObject.getREPORT_ID(), Global.loginYear, this)
					.show();
			break;
		}
		case IDefineReport.REPORTTYPE_GROUP: {// ������ܱ�����
			new ReportGuideUI(repSetObject, this).show();
			break;
		}
		case IDefineReport.REPORTTYPE_OTHER: {// ������������
			new MessageBox("�������ⱨ��֧���޸�!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			// new BesUI(repSetObject.getREPORT_ID(), this).show();
			break;
		}
		}
	}

	public void doSave() {

	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	public CustomTree getFtreReport() {
		return ftreReport;
	}

	public RepSetObject getRepSetObject() {
		return repSetObject;
	}

	public DataSet getDsReport() {
		return dsReport;
	}

	public IDefineReport getDefinReportServ() {
		return definReportServ;
	}

	public ReportTypeList getReportTypeList() {
		return reportTypeList;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
