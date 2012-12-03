package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * �����б༭����
 * 

 * 
 */
public class SysRefColSet extends FDialog {

	private static final long serialVersionUID = 1L;

	private FPanel fpnlRefColArray[]; // ����ҳ������

	private FPanel fpnlRefCol;// �������ҳ���FPanel

	private int iRefColFlag = 0; // ��־��ǰ��ʾ�ڼ���ҳ��

	private int iOperationType;// ��Ų�������

	private FButton fbtnTest;

	private FButton fbtnPrev;

	private FButton fbtnNext;

	private FButton fbtnDown;

	private FButton fbtnCancel;

	private DataSet dsRefCol;

	private DataSet dsRefColDetail;

	private DataSet dsRefColRelatable;

	private final ISysRefCol sysRefColServ = SysRefColI
			.getMethod();

	public SysRefColSet(DataSet dsRefCol) throws Exception {
		super(Global.mainFrame);
		this.setSize(800, 600);
		this.setResizable(false);
		this.dispose();
		this.setModal(true);
		this.setTitle("������������");

		this.dsRefCol = dsRefCol;
		init(); // ��ʼ������
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				try {
					cacelOperate();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SysRefColSet.this,
							"�ر������б༭���淢�����󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	// ��ʼ������
	private void init() {
		// ����fpnlPanel�ŵ�FDialog
		FPanel fpnlPanel = new FPanel();
		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setRowHeight(25);
		fpnlPanel.setLayout(rLay);
		this.getContentPane().add(fpnlPanel);
		// �����ϰ벿��Panel(��������ťPanel),��Ų�ͬ��Panel
		fpnlRefCol = new FPanel();
		fpnlRefCol.setLayout(new RowPreferedLayout(1));
		// ��������ҳ��
		fpnlRefColArray = new FPanel[3];
		fpnlRefColArray[0] = new SysRefColOnePanel();
		fpnlRefColArray[0].setTitle("��һ��������������Ϣ����");
		fpnlRefColArray[1] = new SysRefColTwoPanel();
		fpnlRefColArray[1].setTitle("�ڶ���������������������");
		fpnlRefColArray[2] = new SysRefColThreePanel();
		fpnlRefColArray[2].setTitle("������������������������");

		fpnlRefCol.addControl(fpnlRefColArray[iRefColFlag],
				new TableConstraints(1, 1, true, true));
		// ��ťPanel
		FPanel fpnlBtnButtom = new FPanelBtnButtom();
		// ���ð�ť״̬
		setButtonState();
		fpnlPanel
				.addControl(fpnlRefCol, new TableConstraints(1, 1, true, true));
		fpnlPanel.addControl(fpnlBtnButtom, new TableConstraints(1, 1, false,
				true));
	}

	/**
	 * ��ť���(����һ������һ������ɵȰ�ť)
	 * 
	 * @author qzc
	 * 
	 */
	private class FPanelBtnButtom extends FPanel {

		private static final long serialVersionUID = 1L;

		public FPanelBtnButtom() {
			this.setLeftInset(10);
			this.setRightInset(10);
			// ����fpnlBtnButtom�Ĳ���
			RowPreferedLayout rLayout = new RowPreferedLayout(7);
			rLayout.setColumnGap(2);
			rLayout.setColumnWidth(82);
			this.setLayout(rLayout);
			fbtnTest = new FButton("fbtnTest", "��������");
			fbtnTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					InfoPackage infoPackage = null;
					try {
						infoPackage = ((SysRefColOnePanel) fpnlRefColArray[0])
								.testSql(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(SysRefColSet.this,
								infoPackage.getsMessage(), "��ʾ",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					} else {
						JOptionPane.showMessageDialog(SysRefColSet.this,
								"����ͨ����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			fbtnPrev = new FButton("fbtnPrev", "��һ��");
			fbtnPrev.addActionListener(new AddActionListenerpre());
			fbtnNext = new FButton("fbtnNext", "��һ��");
			fbtnNext.addActionListener(new AddActionListenernext());
			fbtnDown = new FButton("fbtnDown", "�� ��");
			fbtnDown.addActionListener(new DownActionListener());
			fbtnCancel = new FButton("fbtnCancel", "ȡ ��");
			fbtnCancel.addActionListener(new CancelActionListener());
			FLabel flblEmpty1 = new FLabel();
			FLabel flblEmpty2 = new FLabel();

			// ��������FButton
			this.addControl(fbtnTest, new TableConstraints(1, 1, false, false));
			// ��Label
			this
					.addControl(flblEmpty1, new TableConstraints(1, 1, false,
							true));
			// ��һ��
			this.addControl(fbtnPrev, new TableConstraints(1, 1, false, false));
			// ��һ��
			this.addControl(fbtnNext, new TableConstraints(1, 1, false, false));
			// ���
			this.addControl(fbtnDown, new TableConstraints(1, 1, false, false));
			// ��Label
			this.addControl(flblEmpty2,
					new TableConstraints(1, 1, false, false));
			// ȡ��
			this.addControl(fbtnCancel,
					new TableConstraints(1, 1, false, false));
		}
	}

	/**
	 * ��һ����ť����ť�¼�
	 */
	private class AddActionListenernext implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if (!checkFillInfo())
					return;
				setupOperate();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColSet.this,
						"��һ�������������󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			iRefColFlag = iRefColFlag + 1;
			refreshShowPanel();
		}
	}

	/**
	 * ��һ����ť����ť�¼�
	 */
	private class AddActionListenerpre implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			iRefColFlag = iRefColFlag - 1;
			refreshShowPanel();
		}
	}

	private void refreshShowPanel() {
		fpnlRefCol.removeAll();
		setButtonState();
		fpnlRefCol.setLayout(new RowPreferedLayout(1));
		fpnlRefCol.addControl(fpnlRefColArray[iRefColFlag],
				new TableConstraints(1, 1, true, true));
		fpnlRefCol.updateUI();

	}

	/**
	 * �������Ű�ť��״̬
	 * 
	 */
	private void setButtonState() {
		if (iRefColFlag == 0) {
			fbtnTest.setVisible(true);
			fbtnPrev.setEnabled(false);
			fbtnNext.setEnabled(true);
			fbtnDown.setVisible(false);
		} else if (iRefColFlag > 0 && iRefColFlag < fpnlRefColArray.length - 1) {
			fbtnTest.setVisible(false);
			fbtnPrev.setEnabled(true);
			fbtnNext.setEnabled(true);
			fbtnDown.setVisible(false);
		}
		if (iRefColFlag == fpnlRefColArray.length - 1) {
			fbtnTest.setVisible(false);
			fbtnPrev.setEnabled(true);
			fbtnNext.setEnabled(false);
			if (SysRefColOperation.rcoView == iOperationType)
				fbtnDown.setVisible(false);
			else
				fbtnDown.setVisible(true);
		}
	}

	/**
	 * �жϵ�ǰ������д����Ϣ�Ƿ���������ȷ
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkFillInfo() throws Exception {
		InfoPackage infoPackage;
		switch (iRefColFlag) {
		case 0:
			infoPackage = ((SysRefColOnePanel) fpnlRefColArray[0])
					.nextOperate();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(this, infoPackage.getsMessage(),
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			break;
		case 1:
			infoPackage = ((SysRefColTwoPanel) fpnlRefColArray[1])
					.nextOperate();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(this, infoPackage.getsMessage(),
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			break;
		case 2:
			break;
		}
		return true;
	}

	/**
	 * �����һ������һ��ÿ����ͬ�Ĳ���
	 * 
	 * @throws Exception
	 * 
	 */
	private void setupOperate() throws Exception {
		switch (iRefColFlag) {
		case 0:
			// ����һ����д��sql��Ϣ�����ڶ���
			((SysRefColTwoPanel) fpnlRefColArray[1])
					.setDsQuery(((SysRefColOnePanel) fpnlRefColArray[0])
							.getDsDetSql());
			((SysRefColTwoPanel) fpnlRefColArray[1]).showInfo(dsRefCol,
					dsRefColDetail, iOperationType);
			break;
		case 1:
			// ����ͷList��Ϣ����������
			((SysRefColThreePanel) fpnlRefColArray[2])
					.setTableColumnInfo(((SysRefColTwoPanel) fpnlRefColArray[1])
							.getTableColumnInfo());
			((SysRefColThreePanel) fpnlRefColArray[2]).showInfo(dsRefCol,
					dsRefColDetail, dsRefColRelatable, iOperationType);
			break;
		case 2:
			break;
		}
	}

	/**
	 * ���ݲ�ͬ�ı༭���ͣ����ÿؼ�ֵ�Ϳؼ�״̬���ɱ༭�򲻿ɱ༭)
	 * 
	 * @throws Exception
	 * 
	 */
	public void initInfo(int iOperationType) throws Exception {
		this.iOperationType = iOperationType;
		if (iOperationType == SysRefColOperation.rcoAdd)
			addRefCol();
		else if (iOperationType == SysRefColOperation.rcoModify)
			dsRefCol.edit();

		String sRefColId = dsRefCol.fieldByName("refcol_id").getString();
		dsRefColDetail = sysRefColServ.getRefColDetailWithRefColId(sRefColId,
				Global.loginYear);
		dsRefColRelatable = sysRefColServ.getRefColRelatable(sRefColId,
				Global.loginYear);
		((SysRefColOnePanel) fpnlRefColArray[0]).showInfo(dsRefCol,
				iOperationType);
		iRefColFlag = 0;
		refreshShowPanel();
	}

	/**
	 * ����������
	 * 
	 * @throws Exception
	 */
	private void addRefCol() throws Exception {
		String sRefColId = PubInterfaceStub.getMethod()
				.getMaxCode("fb_s_RefCol", "REFCOL_ID",
						"set_Year = " + Global.loginYear, 4);
		dsRefCol.append();
		dsRefCol.fieldByName("Refcol_Id").setValue(sRefColId);
		dsRefCol.fieldByName("RefCol_Kind").setValue(new Integer(0));
	}

	private class DownActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				InfoPackage infoPackage = ((SysRefColThreePanel) fpnlRefColArray[2])
						.nextOperate();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(SysRefColSet.this,
							infoPackage.getsMessage(), "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (SysRefColOperation.rcoAdd == iOperationType) {
					DataSet ds = DataSetUtil.subDataSet(dsRefCol, DataSet.FOR_APPEND);
					ds.edit();
					sysRefColServ.saveRefCol(ds, dsRefColDetail,
							dsRefColRelatable);
				} else if (SysRefColOperation.rcoModify == iOperationType) {
					DataSet ds = DataSetUtil.subDataSet(dsRefCol, DataSet.FOR_UPDATE);
					ds.edit();
					sysRefColServ.saveRefCol(ds, dsRefColDetail,
							dsRefColRelatable);
				}
				dsRefCol.applyUpdate();
				SysRefColSet.this.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColSet.this,
						"�����б��淢�����󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private class CancelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				cacelOperate();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColSet.this,
						"ȡ�������������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void cacelOperate() throws Exception {
		dsRefCol.cancel();
		SysRefColSet.this.setVisible(false);
	}
}
