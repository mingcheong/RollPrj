/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.image.ImageObserver;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;
import gov.nbcs.rp.common.ui.input.IntegerTextField;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysiaestru.ui.SetActionStatus;

import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.exception.AppException;

/**
 * <p>
 * Title:��������ά���ͻ��˽���
 * </p>
 * <p>
 * Description:��������ά���ͻ��˽���
 * </p>
 * <p>

 */
public class ReportTypeUI extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// �������ݿ�ӿ�
	private IDefineReport definReportServ = null;

	// ����������
	private CustomTree treReportType;

	private DataSet dsReportType;

	// �ұ߱༭��
	private FPanel fpnlRight;

	// ����
	private IntegerTextField ftxtCode = null;

	// ����
	private FTextField ftxtName = null;
	
	private  FCheckBox ftxtIsdiv = null;

	private String sSaveType;

	/**
	 * ���캯��
	 * 
	 */
	public ReportTypeUI() {

	}

	/**
	 * �����ʼ��
	 */
	public void initize() {
		// �������ݿ�ӿ�
		definReportServ = DefineReportI.getMethod();

		FSplitPane fSplitPane = new FSplitPane();
		fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		fSplitPane.setDividerLocation(300);
		this.add(fSplitPane);

		// �õ������������
		try {
			dsReportType = definReportServ.getReportType();
			// DataSet״̬�ı��¼�
			dsReportType
					.addStateChangeListener(new ReportTypeStateChangeListener());
			// ����������
			treReportType = new CustomTree("��������", dsReportType,
					IDefineReport.LVL_ID, IDefineReport.CODE_NAME, null,
					SysCodeRule.createClient("6"), IDefineReport.LVL_ID);

		} catch (Exception e1) {
			e1.printStackTrace();
			new MessageBox(Global.mainFrame, "������Ϣ��" + e1.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}

		// TreeSelectionListener�¼�
		treReportType
				.addTreeSelectionListener(new ReportTypeTreeSelectionListener());

		ftxtCode = new IntegerTextField("���룺");
		ftxtCode.setMaxLength(6);
		ftxtName = new FTextField("���ƣ�");
		
		ftxtIsdiv = new FCheckBox("���˱���");
		
		fpnlRight = new FPanel();
		fpnlRight.setTopInset(20);
		fpnlRight.setLeftInset(20);
		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setColumnWidth(300);
		fpnlRight.setLayout(rLay);
		fpnlRight
				.addControl(ftxtCode, new TableConstraints(1, 1, false, false));
		fpnlRight
				.addControl(ftxtName, new TableConstraints(1, 1, false, false));
		fpnlRight
		        .addControl(ftxtIsdiv, new TableConstraints(1, 1, false, false));

		fSplitPane.addControl(treReportType);
		fSplitPane.addControl(fpnlRight);

		try {
			this.createToolBar();
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsReportType,
					this, treReportType);
			setActionStatus.setState(true, true);
		} catch (ControlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ��༭���ؼ����ɱ༭
		Common.changeChildControlsEditMode(fpnlRight, false);

	}

	/**
	 * ����������TreeSelectionListener�¼�
	 * 
	 */
	private class ReportTypeTreeSelectionListener implements
			TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent arg0) {

			if (dsReportType.isEmpty() || dsReportType.bof()
					|| dsReportType.eof())
				return;

			try {
				ftxtCode.setValue(dsReportType
						.fieldByName(IDefineReport.LVL_ID).getString());
				ftxtName.setValue(dsReportType.fieldByName(IDefineReport.NAME)
						.getString());
				if("1".equals(dsReportType.fieldByName("c1").getString().toString()))
				ftxtIsdiv.setValue(new Boolean(true));
				else
				ftxtIsdiv.setValue(new Boolean(false));
				
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox(Global.mainFrame, "��ʾ��Ϣ����������Ϣ��"
						+ e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

		}
	}

	/**
	 * DataSet״̬�ı��¼�
	 * 
	 */
	private class ReportTypeStateChangeListener implements StateChangeListener {

		private static final long serialVersionUID = 1L;

		public void onStateChange(DataSetEvent e) throws Exception {

			if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
				ReportTypeUI.this.setAllControlEnabledFalse();
				treReportType.setEnabled(true);
			} else {
				treReportType.setEnabled(false);
				ReportTypeUI.this.setAllControlEnabledTrue();

			}
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsReportType,
					ReportTypeUI.this, treReportType);
			setActionStatus.setState(true, true);
		}
	}

	/**
	 * ����
	 */
	public void doAdd() {
		try {
			dsReportType.append();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(Global.mainFrame, "���ӳ���������Ϣ��" + e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}
		clearShowInfo();
		ftxtCode.setFocus();
		sSaveType = "add";
	}

	/**
	 * ɾ��
	 */
	public void doDelete() {
		if (treReportType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ���������ͣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			String code = dsReportType.fieldByName(IDefineReport.CODE)
					.getString();
			String sErr = definReportServ.deleteReportType(code);
			if (!Common.isNullStr(sErr)) {
				JOptionPane.showMessageDialog(this, sErr, "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			dsReportType.delete();
			dsReportType.applyUpdate();
			JOptionPane.showMessageDialog(this, "ɾ���ɹ���", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * ȡ��
	 */
	public void doCancel() {
		try {
			String lvl_id = dsReportType.fieldByName(IDefineReport.LVL_ID)
					.getString();

			dsReportType.cancel();
			if (Common.isNullStr(lvl_id))
				clearShowInfo();
			else
				treReportType.expandTo(IDefineReport.LVL_ID, lvl_id);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(Global.mainFrame, "ȡ������������Ϣ��" + e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}
	}

	public void doInsert() {

	}

	/**
	 * �޸�
	 */
	public void doModify() {
		if (treReportType.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ���������ͣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {

			dsReportType.edit();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(Global.mainFrame, "�޸ĳ���������Ϣ��" + e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}
		sSaveType = "mod";
	}

	/**
	 * ����
	 * 
	 * @throws Exception
	 */
	public void doSave() {
		String txtIsdiv;
		boolean c1= ((Boolean) ftxtIsdiv.getValue()).booleanValue();
	    if(c1)
		{
	    	txtIsdiv= "1";	
		} else {
			txtIsdiv="0";
		}
		String sLvl = ftxtCode.getValue().toString();
		if (Common.isNullStr(sLvl)) {
			JOptionPane.showMessageDialog(this, "����д���룡", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			ftxtCode.setFocus();
			return;
		}
		if (sLvl.length() != 6) {
			JOptionPane.showMessageDialog(this, "���볤��Ϊ6�����޸ģ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			ftxtCode.setFocus();
			return;
		}

		String sName = ftxtName.getValue().toString();
		if (Common.isNullStr(sName)) {
			JOptionPane.showMessageDialog(this, "����д���ƣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			ftxtName.setFocus();
			return;
		}

		try {
			String sCode;
			if (sSaveType.equals("add")) {// ����
				sCode = definReportServ.saveReportType("", sLvl, sName,txtIsdiv);

			} else { // �޸�
				sCode = dsReportType.fieldByName(IDefineReport.CODE)
						.getString();
				definReportServ.saveReportType(sCode, sLvl, sName,txtIsdiv);
			}
			dsReportType.maskDataChange(true);
			dsReportType.fieldByName(IDefineReport.CODE).setValue(sCode);
			dsReportType.fieldByName(IDefineReport.LVL_ID).setValue(sLvl);
			dsReportType.fieldByName(IDefineReport.NAME).setValue(sName);
			dsReportType.fieldByName(IDefineReport.CODE_NAME).setValue(
					sLvl + " " + sName);
			
			dsReportType.fieldByName("c1").setValue(txtIsdiv);
	
			dsReportType.applyUpdate();
			dsReportType.maskDataChange(false);

			// ˢ����
			treReportType.reset();

			// ��λ���ڵ�
			this.treReportType.expandTo(IDefineReport.LVL_ID, sLvl);

			JOptionPane.showMessageDialog(this, "����ɹ���", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"���淢�����󣬴�����Ϣ��" + e.getMessage(), "��ʾ", ImageObserver.ERROR);
			e.printStackTrace();
		}

	}

	/**
	 * �ر�
	 */
	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	/**
	 * 
	 * ��༭���ؼ����ɱ༭
	 */
	public void setAllControlEnabledFalse() {
		Common.changeChildControlsEditMode(fpnlRight, false);
	}

	/**
	 * 
	 * ��༭���ؼ��ɱ༭
	 */
	public void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(fpnlRight, true);
	}

	private void clearShowInfo() {
		// �����
		ftxtCode.setValue("");
		// ����
		ftxtName.setValue("");
		
		ftxtIsdiv.setValue("");
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
