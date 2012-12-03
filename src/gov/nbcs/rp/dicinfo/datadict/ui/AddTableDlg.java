/**
 * @# AddTableDlg.java    <�ļ���>
 */
package gov.nbcs.rp.dicinfo.datadict.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import gov.nbcs.rp.basinfo.common.BOCache;

import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * ����˵��:����ֵ���Ϣ��ѡ��Ի���
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class AddTableDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058579385239720339L;

	private static boolean isPressOK = false;// �Ƿ�ȷ��

	private static List arrTableSelect;// ѡ��ı�

	private static String tabProp;// �����

	private static int page;// ��¼ѡ���ҳ�棬��־��Ҫ��ӵ�����

	private String title = "��ӱ�...";

	private FCheckBox chxUse;

	private FTree treTable;// ��ͨ��

	private FTree treDefTable;// Ԥ�����

	private FTabbedPane tpnlCenter;

	private FComboBox cbxType;// �����

	public static final int PAGE_DEFTABLE = 0;// ҳ����ΪԤ������

	public static final int PAGE_TABLE = 1;// ҳ��һΪ��ͨ��

	// ���ڻ���
	private List lstAllTable;// ȫ����ͼ��

	private List lstUnUseTable;// δʹ����ͼ��

	private List lstAllDefTable;// ȫ��Ԥ�����

	private List lstUnUseDefTable;// δʹ��Ԥ����

	IDataDictBO dataDictBO;

	public AddTableDlg() {
		super(Global.mainFrame, true);
		arrTableSelect = null;
		isPressOK = false;
		dataDictBO = (IDataDictBO) BOCache.getBO("rp.dataDictService");
		initUI();
	}

	// ��ʼ������
	private void initUI() {
		this.setTitle(title);

		chxUse = new FCheckBox();
		chxUse.setTitle("��ʹ�õı���ʾ");
		chxUse.setTitlePosition("right");
		chxUse.setValue(new Boolean(true));
		chxUse.setPreferredSize(new Dimension(200, 30));
		chxUse.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				initTreeData(!((Boolean) arg0.getNewValue()).booleanValue());
			}
		});

		FPanel pnlTop = new FPanel();
		pnlTop.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		pnlTop.setLayout(new FlowLayout(FlowLayout.LEFT));

		cbxType = new FComboBox();
		String sTypeRef = dataDictBO.getTableTypeRefString();
		cbxType.setRefModel(sTypeRef);
		cbxType.setTitle("�����");
		cbxType.setPreferredSize(new Dimension(200, 20));

		pnlTop.add(chxUse);
		pnlTop.add(cbxType);
		// ---------------------
		tpnlCenter = new FTabbedPane();

		// ��ͨ����
		treTable = new FTree();
		treTable.setRoot("��ѡ��");
		treTable.setIsCheck(true);
		treTable.setRootVisible(false);
		FScrollPane spnlTable = new FScrollPane(treTable);
		// Ԥ�������
		treDefTable = new FTree();
		treDefTable.setRoot("��ѡ��");

		treDefTable.setIsCheck(true);
		FScrollPane spnlDefTable = new FScrollPane(treDefTable);
		tpnlCenter.add(spnlDefTable, "ҵ�񱨱�");
		tpnlCenter.add(spnlTable, "���ݿ��/��ͼ");
		
		// --------------------
		// ��ť
		FPanel pnlBottom = new FPanel();
		pnlBottom.setLayout(new FlowLayout(FlowLayout.CENTER));

		FButton btnOK = new FButton("OK", "ȷ��");
		btnOK.addActionListener(new BtnOkClick());
		FButton btnCancel = new FButton("CANEL", "ȡ��");
		btnCancel.addActionListener(new BtnCancelClick());

		pnlBottom.add(btnOK);
		pnlBottom.add(new JLabel("   "));
		pnlBottom.add(btnCancel);
		// --------------
		FPanel pnlBack = new FPanel();
		pnlBack.setLayout(new BorderLayout());
		pnlBack.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		pnlBack.add(pnlTop, BorderLayout.NORTH);
		pnlBack.add(tpnlCenter, BorderLayout.CENTER);
		pnlBack.add(pnlBottom, BorderLayout.SOUTH);

		initTreeData(false);

		this.getContentPane().add(pnlBack);

		Dimension d = new Dimension(500, 600);

		this.setSize(d);
		this
				.setLocation(
						(Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2,
						(Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2);
		this.setVisible(true);

	}

	public static List getArrTableSelect() {
		return arrTableSelect;
	}

	public static boolean isPressOK() {
		return isPressOK;
	}

	// ȷ����ť�¼�
	class BtnOkClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int iCurIndex = tpnlCenter.getSelectedIndex();
			List lstSelected;
			if (iCurIndex == PAGE_DEFTABLE) {// Ԥ�������
				lstSelected = treDefTable.getAllLeafSelectedDatas();
			} else {
				lstSelected = treTable.getAllLeafSelectedDatas();
			}
			if (lstSelected == null || lstSelected.size() == 0) {
				new MessageBox("��ѡ��Ҫ��ӵı�/��ͼ!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			// ȥ�����Ǳ��ѡ�����ʹ�õı�
			int iCount = lstSelected.size();
			String sUseTable = "";
			for (int i = iCount - 1; i >= 0; i--) {
				XMLData aData = (XMLData) lstSelected.get(i);
				if ("1".equals(""))
					lstSelected.remove(i);
				else if ("1".equals("")) {

					sUseTable = sUseTable + "{"
							+"" + "}";

					lstSelected.remove(i);
				}
			}

			if (!sUseTable.equals("")) {
				new MessageBox("��  " + sUseTable + "  ��ʹ�ý����ٽ�����Ӳ���!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			}
			if (lstSelected.size() == 0) {
				new MessageBox("��ѡ��Ҫ��ӵı�(��ͼ)!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			tabProp = (String) cbxType.getValue();
			if (tabProp == null || tabProp.equals("")) {
				new MessageBox("��ѡ��Ҫ��ӵı����!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			// ���÷��صı�
			page = tpnlCenter.getSelectedIndex();
			arrTableSelect = lstSelected;
			isPressOK = true;
			AddTableDlg.this.dispose();
		}

	}

	// ȡ����ť�¼�
	class BtnCancelClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			AddTableDlg.this.dispose();

		}

	}

	private void initTreeData(boolean isAll) {

		try {

			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

			List lstData;
			// ����Ҫ���棬�����������ж�
			{
				if (isAll) {
					if (lstAllDefTable == null)
						lstAllDefTable = dataDictBO.getTableInfo(
								IDataDictBO.DEFINE_MODEL, isAll,Global.loginYear);
					lstData = lstAllDefTable;

				} else {
					if (lstUnUseDefTable == null)
						lstUnUseDefTable = dataDictBO.getTableInfo(
								IDataDictBO.DEFINE_MODEL, isAll,Global.loginYear);
					lstData = lstUnUseDefTable;
				}
				treDefTable.setData(lstData);
			}
			{// �ڶ�ҳ�����б�����
				if (isAll) {
					if (lstAllTable == null)
						lstAllTable = dataDictBO.getTableInfo(
								IDataDictBO.TABLE_MODEL, isAll,Global.loginYear);
					lstData = lstAllTable;

				} else {
					if (lstUnUseTable == null)
						lstUnUseTable = dataDictBO.getTableInfo(
								IDataDictBO.TABLE_MODEL, isAll,Global.loginYear);
					lstData = lstUnUseTable;
				}
				treTable.setData(lstData);
			}

			treTable.updateUI();
			treTable.expendedNode(treTable.getRoot());
			treDefTable.updateUI();
			treDefTable.expendedNode(treDefTable.getRoot());
			page = tpnlCenter.getSelectedIndex();
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}

	public static int getPage() {
		return page;
	}

	public static String getTabProp() {
		return tabProp;
	}

	public static void clearSelect() {
		arrTableSelect = null;
	}
}
