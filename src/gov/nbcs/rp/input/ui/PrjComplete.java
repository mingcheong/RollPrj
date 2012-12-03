package gov.nbcs.rp.input.ui;

/**
 * @author л��ݥ
 * 
 * @version ����ʱ�䣺Jun 7, 20122:38:45 PM
 * 
 * @Description
 */

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.ibs.IPrjAudit.PrjAuditTable.Affix;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.prjsync.action.PrjSyncActionedUI;
import gov.nbcs.rp.statc.ConstStr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.util.Global;




/**
 * @author xys
 * 
 * @version ����ʱ�䣺2012-3-12 ����05:02:14
 * 
 * @Description ���Ŀ
 */
public class PrjComplete extends RpModulePanel implements PrjActionUI,PrjSyncActionedUI
{

	private static final long serialVersionUID = 1L;

	private int state = -1;// -1 ���״̬ 0:���� 1���޸�

	private CustomTree treeEn;// Ԥ�㵥λ

	private CustomTreeFinder cf = null;

	// private CustomComboBox cbQueryType;// ��Ŀ״̬

	public CustomTable tbPrj = null;// �б�

	FTextField tfPrjCode = null; // ��Ŀ����

	FTextField tfPrjName = null; // ��Ŀ����

	FComboBox cbYearBegin = null; // ��ʼ���

	FComboBox cbYearEnd = null; // �������

	FComboBox cbPrjCir = null; // ִ������

	FComboBox cbPrjSort = null; // ��Ŀ����
	FTreeAssistInput fxmsx = null;// ��Ŀ����
	FTreeAssistInput gkdw = null;// ��ڵ�λ
	FComboBox cbPrjState = null; // ��Ŀ״̬
	FComboBox sendState = null; // ���״̬
	FTextField tfAcct = null; // ��Ŀѡ��
	FButton btnAcct; // ѡ���Ŀ��ť
	private FButton btnAdd = null;// ������ϸ
	private FButton btnDel = null;// ɾ����ϸ
	// private FButton btnRead = null;// ��������ϸ
	// private FButton btnMod = null;// �޸ķ�����ϸ
	private FTextArea fSbly;// �걨����
	private Affix tbaffix; // �������

	FComboBox cbYear = null; // ��ѡ���
	String selectYear = GlobalEx.loginYear;
	DataSet treeds = null;
	PrjInputDTO prj_tb = null;
	TbPrjAuditAffix tpaa;
	String module_id = GlobalEx.getModuleId();



	// TbModPanel tbmodpanel=null;
	// private TbModPanel tm=null;
	/**
	 * ��ʼ��
	 */
	public void initize()
	{
		try
		{
			this.initData();
			this.initAffix();
			this.createToolBar();
			this.add(getBasePanel());
			this.controlButton();
		}
		catch (Exception ex)
		{
			ErrorInfo.showErrorDialog(ex, "��Ŀ�걨�����ʼ��ʧ��");
		}
	}


	/**
	 * ʵ���������б�
	 */
	private void initAffix() throws Exception
	{
		// tbaffix = new Affix();
	}


	/**
	 * ��ʼ����Ŀ��ϸ�б�
	 */
	private void initData() throws Exception
	{
		prj_tb = new PrjInputDTO();
	}


	/**
	 * ��ȡ�����
	 * 
	 * @return FPanel
	 * @throws Exception
	 */
	private FPanel getBasePanel() throws Exception
	{
		// �����
		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);

		// �ָ��������
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(300);// �������������λ��Ϊ300px

		// ������
		FPanel leftInfo = new FPanel();
		FScrollPane pnlTree = new FScrollPane();
		RowPreferedLayout leftlay = new RowPreferedLayout(1);
		leftInfo.setLayout(leftlay);

		treeEn = new CustomTree("Ԥ�㵥λ", treeds, "en_id", "code_name", "parent_id", null, "div_code");
		refreshTreeData();
		this.addTreeListener();
		cf = new CustomTreeFinder(treeEn);
		pnlTree.addControl(treeEn);
		leftInfo.addControl(cf, new TableConstraints(1, 1, false, true));
		leftInfo.addControl(pnlTree, new TableConstraints(1, 1, true, true));

		// �ұ����
		FPanel rightInfo = new FPanel();
		RowPreferedLayout rightlay = new RowPreferedLayout(1);
		rightInfo.setLayout(rightlay);
		rightInfo.setTopInset(10);
		rightInfo.setLeftInset(10);
		rightInfo.setRightInset(10);
		rightInfo.setBottomInset(10);

		// �ָ��������
		FSplitPane pnlff = new FSplitPane();
		pnlff.setOrientation(FSplitPane.VERTICAL_SPLIT);
		pnlff.setDividerLocation(200);

		// ���������Ž��ָ��������
		pnlff.addControl(getTopFpanel());
		pnlff.addControl(getRight_BottomPanel());

		// ���ָ��������Ž��ұ����
		rightInfo.add(pnlff);

		// ���������Ž��ָ����
		pnlInfo.addControl(leftInfo);
		pnlInfo.addControl(rightInfo);
		pnlInfo.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		refreshPrjData();// ˢ���б�
		pnlBase.add(pnlInfo);
		return pnlBase;
	}


	public FPanel getPrjAffixPanel() throws Exception
	{
		PrjAuditDTO pd = new PrjAuditDTO();
		PrjAuditAction pa = new PrjAuditAction(pd);
		tpaa = new TbPrjAuditAffix(pa);
		return tpaa.setBottomPanel();
	}


	/**
	 * ʵ���ұ�����ϲ������
	 * 
	 * @return FPanel
	 */
	private FPanel getTopFpanel()
	{
		// �ұ�����ϲ������
		FPanel topInfo = new FPanel();
		RowPreferedLayout toplay = new RowPreferedLayout(1);
		topInfo.setLayout(toplay);

		// �������
		FPanel topdiv = new FPanel();
		RowPreferedLayout topdivlay = new RowPreferedLayout(4);
		topdivlay.setRowGap(10);
		topdiv.setLayout(topdivlay);
		topdiv.setTopInset(10);
		topdiv.setLeftInset(10);
		topdiv.setRightInset(10);
		topdiv.setBottomInset(10);
		try
		{
			tbPrj = new CustomTable(new String[] { "�Ƿ���", "��Ŀ����", "��λ����", "��λ����", "��Ŀ����", "��ʼ���", "�������", "ִ������", "��Ŀ����", "��Ŀ����", "��Ŀ", "ָ����" }, new String[] { "step_id", "xmbm", "div_code",
					"div_name", "xmmc", "c1", "c2", "c3", "c4", "c5", "accts", "budget_money" }, null, true, null);
			// setTableProp();
			this.addTableListener();
			topdiv.addControl(getsendstate(), new TableConstraints(1, 1, false, true));
			topdiv.addControl(getYear(), new TableConstraints(1, 1, false, true));
			topdiv.addControl(tbPrj, new TableConstraints(1, 4, true, true));

			// ���������Ž��ϲ������
			topInfo.add(topdiv);
		}
		catch (Exception ex)
		{
			ErrorInfo.showErrorDialog(ex, "��Ŀ�б��ʼ��ʧ��");
		}
		return topInfo;
	}


	/**
	 * ���ñ����п�
	 * 
	 * @throws Exception
	 */
	protected void setTableProp() throws Exception
	{
		tbPrj.reset();
		tbPrj.getTable().setRowHeight(25);
		tbPrj.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbPrj.getTable().getColumnModel().getColumn(1).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(3).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(4).setPreferredWidth(200);
		tbPrj.getTable().getColumnModel().getColumn(5).setPreferredWidth(200);
		tbPrj.getTable().getColumnModel().getColumn(6).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(7).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(8).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(9).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(10).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(11).setPreferredWidth(200);
		tbPrj.getTable().getColumnModel().getColumn(12).setPreferredWidth(80);
		tbPrj.setSize(2000, 2000);
		tbPrj.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
	}


	/**
	 * ʵ���ұ�����ϲ�������е���Ŀ״̬
	 * 
	 * @return CustomComboBox
	 */
	// private CustomComboBox getQueryType() {
	// try {
	// DataSet ds = DataSet.createClient();
	// if (!GlobalEx.isFisVis()) {
	// ds.append();
	// ds.fieldByName("id").setValue("1");
	// ds.fieldByName("name").setValue("δ����");
	// ds.append();
	// ds.fieldByName("id").setValue("2");
	// ds.fieldByName("name").setValue("�Ѳ���");
	//			
	// } else {
	// ds.append();
	// ds.fieldByName("id").setValue("1");
	// ds.fieldByName("name").setValue("δ����");
	// ds.append();
	// ds.fieldByName("id").setValue("2");
	// ds.fieldByName("name").setValue("�Ѳ���");
	//			
	// }
	// cbQueryType = new CustomComboBox(ds, "id", "name");
	// cbQueryType.setTitle("��Ŀ״̬��");
	// cbQueryType.addValueChangeListener(new ValueChangeListener() {
	// public void valueChanged(ValueChangeEvent arg0) {
	// refreshPrjData();// ˢ���б�
	// controlButton();
	// }
	// });
	//			
	//
	// } catch (Exception ex) {
	// ErrorInfo.showErrorDialog(ex, "��Ŀ״̬��ʼ��ʧ��");
	// }
	// return cbQueryType;
	// }
	// private FComboBox getsendstate()
	// {
	// try
	// {
	// DataSet ds2 = DataSet.createClient();
	// ds2.append();
	// ds2.fieldByName("id").setValue("1");
	// ds2.fieldByName("name").setValue("δ����");
	// ds2.append();
	// ds2.fieldByName("id").setValue("2");
	// ds2.fieldByName("name").setValue("������");
	// sendState = new CustomComboBox(ds2, "id", "name");
	// sendState.setTitle("���״̬��");
	// sendState.addValueChangeListener(new ValueChangeListener()
	// {
	// public void valueChanged(ValueChangeEvent arg0)
	// {
	// refreshPrjData();// ˢ���б�
	// controlButton();
	// }
	// });
	// }
	// catch (Exception ex)
	// {
	// ErrorInfo.showErrorDialog(ex, "���״̬��ʼ��ʧ��");
	// }
	//
	// return sendState;
	// }
	// private CustomComboBox getsendstate(){
	// try{
	// DataSet ds2 = DataSet.createClient();
	// ds2.append();
	// ds2.fieldByName("id").setValue("1");
	// ds2.fieldByName("name").setValue("δ����");
	// ds2.append();
	// ds2.fieldByName("id").setValue("2");
	// ds2.fieldByName("name").setValue("������");
	// sendState = new CustomComboBox(ds2, "id", "name");
	// sendState.setTitle("���״̬��");
	// sendState.addValueChangeListener(new ValueChangeListener() {
	// public void valueChanged(ValueChangeEvent arg0) {
	// refreshPrjData();// ˢ���б�
	// controlButton();
	// }
	// });
	// } catch (Exception ex) {
	// ErrorInfo.showErrorDialog(ex, "���״̬��ʼ��ʧ��");
	// }
	//	
	// return sendState;
	// }
	private FComboBox getsendstate() throws Exception
	{
		sendState = new FComboBox("");
		sendState.setTitle("��Ŀ״̬��");
		sendState.setProportion(0.4f);
		sendState.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent arg0)
			{
				refreshPrjData();
			}
		});
		setStatusCbx(sendState);
		try
		{
			createToolBar();
		}
		catch (Exception e)
		{

		}
		return sendState;
	}


	private FComboBox getYear()
	{
		cbYear = new FComboBox("��ȣ�");
		int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
		String YearValue = "";
		for (int i = 0; i <= 5; i++)
		{
			YearValue += (numQs + i) + "#" + (numQs + i) + "+";
		}
		if (YearValue.length() > 0)
		{
			YearValue = YearValue.substring(0, YearValue.length() - 1);
		}
		cbYear.setRefModel(YearValue);
		cbYear.setValue(Global.loginYear);
		cbYear.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent arg0)
			{
				selectYear = cbYear.getValue().toString();
				if (selectYear != GlobalEx.loginYear)
				{
					setButtonStateAn(false, false, false, false, false);
				}
				else
					setButtonStateAn(false, false, true, false, false);
				refreshTreeData();// ˢ���б�
				refreshPrjData();
			}

		});
		return cbYear;
	}


	/**
	 * ʵ���ұ�����²��ַָ����
	 */
	private FPanel getRight_BottomPanel() throws Exception
	{
		FPanel right_bottom_panel = new FPanel();
		RowPreferedLayout right_bottomlay = new RowPreferedLayout(2);
		right_bottom_panel.setLayout(right_bottomlay);
		right_bottom_panel.addControl(getPrjFilePanel(), new TableConstraints(15, 2, false, true));
		return right_bottom_panel;
	}


	/**
	 * ���ǩ
	 * 
	 * @return FTabbedPane
	 */
	public FTabbedPane getPrjFilePanel() throws Exception
	{
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("��Ŀ�", getPrjTbPanel());
		// tabpane.addControl("�� ��", getPrjAffixPanel());
		tabpane.addControl("��    ��", getPrjAffixPanel());
		tabpane.addControl("�걨����", getPrjRemarkPanel());
		return tabpane;
	}


	/**
	 * ��Ŀ�
	 */
	private FPanel getPrjTbPanel()
	{
		FPanel table_div = new FPanel();
		RowPreferedLayout table_div_lay = new RowPreferedLayout(1);
		table_div.setLayout(table_div_lay);
		table_div.setTopInset(10);
		table_div.setLeftInset(10);
		table_div.setRightInset(10);
		table_div.setBottomInset(10);
		table_div.add(prj_tb.getReportUI(), new TableConstraints(1, 1, true, true));
		// prj_tb.getReportUI().getGrid().addMouseListener(new MouseAdapter()
		// {
		// // �����б��д����¼�
		// public void mouseClicked(MouseEvent e)
		// {
		// try
		// {
		// // Cell cell = (Cell) ((Report) prj_tb.getReportUI()
		// // .getReport()).getSelectedCell();
		// // if (((Report)
		// //
		// prj_tb.getReportUI().getReport()).getBodyData().gotoBookmark(cell.getBookmark()))
		// // {
		// // String supType = ((Report)
		// //
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("SB_CODE").getString();
		// // if ("111".equalsIgnoreCase(supType)||
		// // "222".equalsIgnoreCase(supType)||
		// // "333".equalsIgnoreCase(supType))
		// // {
		// // return;
		// // }
		// String xmxh = ((Report)
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("xmxh").getString();
		// // Double.valueOf(
		// // double sum = Double.valueOf(Common.nonNullStr(((Report)
		// //
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("TOTAL_SUM").getValue())).doubleValue();
		// // oper.getOperationState().setSum(sum);
		// prj_tb.getReportUI().repaint();
		// // }
		// }
		// catch (Exception ex)
		// {
		// // TODO Auto-generated catch block
		// ex.printStackTrace();
		// }
		// }
		// });
		return table_div;
	}


	/**
	 * ���ǩ���걨����
	 * 
	 * @return FPanel
	 */
	private FPanel getPrjRemarkPanel()
	{
		FPanel ply = new FPanel();
		RowPreferedLayout plyRpl = new RowPreferedLayout(1);
		ply.setLayout(plyRpl);
		ply.setTopInset(10);
		ply.setLeftInset(10);
		ply.setRightInset(10);
		ply.setBottomInset(10);
		fSbly = new FTextArea();
		fSbly.setProportion(0.001f);
		fSbly.setEnabled(false);

		fSbly.getEditor().setBackground(Color.white);
		ply.addControl(fSbly, new TableConstraints(1, 1, true, true));
		return ply;
	}


	/**
	 * ���ǩ�и���
	 * 
	 * @return FPanel
	 */
	// private FPanel getPrjAffixPanel() {
	// String[] columnText = new String[] { "��������", "�����ļ�����", "��������" };
	// String[] columnField = new String[] { "affix_title", "affix_file",
	// "affix_type" };
	// tbaffix.setColumnText(columnText);
	// tbaffix.setColumnField(columnField);
	// tbaffix.setBtn(true);
	// tbaffix.setSelectYear(selectYear);
	// tbaffix.setTableName("RP_PRJ_AXFFIX");// ����
	// FPanel affix = tbaffix.setBasePanel();// ��ʼ������������
	// //tbaffix.setButtonStat(false);// ��ֹ������ť
	// tbaffix.setViewAndSave(false);
	// return affix;
	// }
	// �޸Ĳ���
	public void doModify()
	{
		if (state == 0)
			return;
		state = 1;// �޸�״̬
		// tbaffix.setBtn(false);
		// tbaffix.setViewAndSave(true);
		if (tbPrj.getTable().getSelectedRow() < 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ�����޸�");
			return;
		}
		else
		{
			int count = tbPrj.getTable().getRowCount();
			int num = 0;
			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					num++;
				}
			}
			if (num > 1)
			{
				JOptionPane.showMessageDialog(null, "��ѡ��һ����¼�����޸�");
				return;
			}
			int row = tbPrj.getTable().getSelectedRow();
			try
			{

				if (row >= 0)
				{
					String bmk = tbPrj.rowToBookmark(row);
					if (!tbPrj.getDataSet().gotoBookmark(bmk))
					{
						JOptionPane.showMessageDialog(null, "��ѡ���¼");
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "��ѡ���¼");
					return;
				}
				if (prj_tb.getDsBody() != null && !prj_tb.getDsBody().isEmpty())
				{
					// ����reportUI�ɱ༭
					try
					{}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				// tbPrj.getDataSet().fieldByName("bj").setValue("this");//�����
				// ��ֵ������������ʱ��ʹ��
				// tbaffix.setChrId(tbPrj.getDataSet().fieldByName("xmxh")
				// .getString());
				// tbaffix.setPrjCode(tbPrj.getDataSet().fieldByName("xmbm")
				// .getString());
				// tbaffix.setEnId(tbPrj.getDataSet().fieldByName("en_id")
				// .getString());
				// tbaffix.setEnCode(tbPrj.getDataSet().fieldByName("div_code")
				// .getString());
				// tbPrj.getDataSet().edit();// ���ÿɱ༭�б�
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			BtModPanel mod = new BtModPanel(this);
			Dimension d = new Dimension(900, 530);
			mod.setSize(d);
			mod.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60, (Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);

			mod.setEnabled(true);
			mod.setVisible(true);
		}
	}


	// ȡ������
	public void doCancel()
	{
		state = -1;
		// tbaffix.setBtn(true);
		setbtn(false, false, false, false);
		this.setButtonStateAn(true, true, true, false, false);
		this.setPanelEnable(false);
		tbPrj.getTable().addMouseListener(a);

		// tbaffix.setBtn(false);// ��ֹ����
		try
		{
			this.refreshPrjData();
			// this.emptyPrj();
			// getDefaultInputSetStatePanel(false);
			// �����Ŀ��ϸ�б�
			this.emptyPrjInfo();
			// tbaffix.setChrId(null);
			// tbaffix.tbCancel();// ��ո����б�����
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// �������
	public void doSendToAudit()
	{

		String msg = "";
		try
		{
			int[] rows = tbPrj.getTable().getSelectedRows();
			if (rows.length <= 0)
			{
				JOptionPane.showMessageDialog(null, "��ѡ��Ҫ������걨��Ŀ��¼");
				return;
			}
			List xmxhs = new ArrayList();
			String[] prjId = new String[rows.length];
			String[] bmks = getBMKs(rows, tbPrj);
			DataSet ds = tbPrj.getDataSet();
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					prjId[i] = ds.fieldByName("XMXH").getValue().toString();
					Map values = new HashMap();
					values.put("xmxh", tbPrj.getDataSet().fieldByName("xmxh").getString());
					values.put("enid", tbPrj.getDataSet().fieldByName("en_id").getString());
					xmxhs.add(values);
				}
			}
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "��ȷ������" + rows.length + "����Ϣ?", "��ʾ", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return;
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					// msg =
					// RportProjectStub.getMethod().startPrjRportFlow("RP_PRJ_MAIN",
					// "XMXH",
					// ds.fieldByName("XMXH").getValue().toString(),GlobalEx.getModuleId());//
					// ���빤����
					PubInterfaceStub.getMethod().sendAuditInfoByDiv(xmxhs, GlobalEx.user_code, GlobalEx.getCurrRegion(), module_id);
					// PubInterfaceStub.getMethod().startflowInfoByDiv(prjId,
					// GlobalEx.user_code, GlobalEx.getCurrRegion(),
					// Global.getModuleId());

				}
			}
			// msg = RportProjectStub.getMethod().goPrjRportFlow("RP_PRJ_MAIN",
			// "XMXH", valueList, "TONEXT",GlobalEx.getModuleId());
			//			
			if (!"".equals(msg))
			{
				JOptionPane.showMessageDialog(null, msg);
				return;
			}
			refreshPrjData();
			msg = "��˳ɹ�";
			JOptionPane.showMessageDialog(null, msg);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}


	// �����������
	public void doBackToAudit()
	{
		if (GlobalEx.isFisVis())
		{
			// ֻ�е�λ�û����ܲ���
			JOptionPane.showMessageDialog(Global.mainFrame, "ֻ�е�λ�û����ó�������");
			return;
		}
		int[] rows = tbPrj.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		String[] prjcodes = new String[rows.length];

		// ֻ��û����˼�¼������˽��������������ʸ�

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = tbPrj.rowToBookmark(rows[i]);

				if (tbPrj.getDataSet().gotoBookmark(bmk))
					prjcodes[i] = tbPrj.getDataSet().fieldByName("xmxh").getString();

			}
			// ��λ�������
			PubInterfaceStub.getMethod().BackAuditInfoByDiv(1, prjcodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), GlobalEx.getModuleId());
			// }
			JOptionPane.showMessageDialog(null, "�����ɹ�");
			this.refreshPrjData();
			if (!GlobalEx.isFisVis())
			{
				// this.zts =
				// PrjInputStub.getMethod().getxmztNum(GlobalEx.user_code,
				// true);
				// f2.setTitle(" ���Ŀ��" + zts[0] + " ������Ŀ:" + zts[1] + " �˻���Ŀ:"
				// + zts[2] + " ȫ����Ŀ:" + zts[3]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "����ʧ��");

		}
	}


	/**
	 * ���β˵������¼�
	 */
	private void addTreeListener()
	{
		treeEn.addMouseListener(b);
	}



	MouseAdapter b = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() == 1)
			{
				if ((treeEn != null) && (treeEn.getSelectedNode() != null) && (treeEn.getSelectedNode() != null))
				{
					MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode().getUserObject();
					if ((enPer == null) || "".equals(enPer.getValue())) { return; }
					// if (prj_tb.getCurState() != -1)
					// return;
					try
					{
						if (enPer.getIsLeaf())
							enPer.getValue();
						if ("".equals(refreshPrjData()))
						{
							controlButton();
						}// ��ʾ�б���Ϣ
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
	};



	/**
	 * ����б�����Ϣ�����¼�
	 */
	private void addTableListener()
	{
		tbPrj.getTable().addMouseListener(a);
	}



	MouseAdapter a = new MouseAdapter()
	{
		// �����б��д����¼�
		public void mouseClicked(MouseEvent e)
		{
			// TODO Auto-generated method stub
			if (tbPrj != null)
			{
				if (tbPrj.getTable().getSelectedRow() < 0)
					return;
				else
				{
					controlButton();
					try
					{
						if (!tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(tbPrj.getTable().getSelectedRow())))
							return;
						else
						{
							tbPrj.getDataSet().applyUpdate();
							DataSet ds_table = tbPrj.getDataSet();
							String xmxh = ds_table.fieldByName("XMXH").getString();
							// oper.getOperationState().setPrjCategory(ds_table.fieldByName("C11").getString());
							String step_id = ds_table.fieldByName("STEP_ID").getString();// δ����
							// or
							// �Ѳ���
							refreshPrjDetailData(step_id, ds_table.fieldByName("XMBM").getString(), ds_table.fieldByName("div_code").getString(), xmxh);// ��ʾ��Ŀ��ϸ

							// tbaffix.setSelectYear(selectYear);
							// tbaffix.setChrId(xmxh);
							// tbaffix.refreshPrjData();// ��ʾ��Ŀ����

							// ��ʾ�걨������Ϣ
							String ly = ConstStr.nonNullStr(ds_table.fieldByName("sb_ly").getValue());// �걨����
							fSbly.setValue(ly);

							// setbtn(false, false, true, false);
						}
					}
					catch (Exception ex)
					{
						ErrorInfo.showErrorDialog(ex, "��Ŀ�����б��ʼ��ʧ��");
					}
				}
			}
		}
	};



	/**
	 * ��ʾ�б�����
	 */
	// public String refreshPrjData()
	// {
	// try
	// {
	//
	// StringBuffer cond = new StringBuffer();
	// cond.append(" AND SET_YEAR = " + selectYear);
	// cond.append(" AND RG_CODE = "+ Global.getCurrRegion());
	// MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
	// if (nodeSel != null) {
	// DataSet ds = treeEn.getDataSet();// ��ȡԤ�㵥λ
	// if (treeEn.getSelectedNode() != treeEn.getRoot()) {
	// if (ds != null && !ds.isEmpty() && !ds.bof()
	// && !ds.eof()) {
	// String divCode = ds.fieldByName("div_code")
	// .getString();
	// cond
	// .append(" AND EN_CODE LIKE '" + divCode
	// + "%'");
	// }
	// }
	// }
	// DataSet data=null;
	// String condSql = cond.toString();
	// cond.delete(0, cond.length());
	// this.setButtonStateAn(true, true, true, false, false);
	// data = PrjInputStub.getMethod().getQueryProject(condSql);
	// tbPrj.setDataSet(data);// ��ѯ��Ŀ��Ϣ
	// tbPrj.reset();
	// }
	// catch (Exception ex)
	// {
	// ex.printStackTrace();
	// }
	// return "";
	// }
	/**
	 * ��ʾ�б�����
	 */
	public String refreshPrjData()
	{
		try
		{

			MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
			DataSet data = null;
			String divCode = "";
			if (nodeSel != null)
			{
				DataSet ds = treeEn.getDataSet();// ��ȡԤ�㵥λ
				if (treeEn.getSelectedNode() != treeEn.getRoot())
				{
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
					{
						divCode = ds.fieldByName("div_code").getString();
					}
				}
			}
			switch (this.sendState.getSelectedIndex())
			{
				case 0:
					// setButtonStateBn(true);
					// this.setButtonStateAn(true, true, true, false, false);
					data = PrjInputStub.getMethod().getQueryProject(divCode);
					break;
				case 1:
					// setButtonStateBn(false);
					// this.setButtonStateAn(false, false, false, false, false);
					data = PrjInputStub.getMethod().getQueryProject2(divCode);
					break;
			}
			// TODO ��ѯ
			tbPrj.setDataSet(data);// ��ѯ��Ŀ��Ϣ
			setTableProp();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
	}


	/**
	 * ��ʾ�б�����
	 */
	// public String refreshPrjData2() {
	// try {
	// StringBuffer cond = new StringBuffer();
	// // cond.append(" AND RP_PRJ_MAIN.SET_YEAR = " + selectYear);
	// // cond.append(" AND RP_PRJ_MAIN.RG_CODE = "
	// // + Global.getCurrRegion());
	// MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
	// DataSet data=null;
	// String condSql="";
	// if (nodeSel != null) {
	// DataSet ds = treeEn.getDataSet();// ��ȡԤ�㵥λ
	// if (treeEn.getSelectedNode() != treeEn.getRoot()) {
	// if (ds != null && !ds.isEmpty() && !ds.bof()
	// && !ds.eof()) {
	// String divCode = ds.fieldByName("div_code").getString();
	// // switch (this.sendState.getSelectedIndex()) {
	// // case 0:
	// // // ������Ŀ
	// // cond.append(" AND EN_CODE LIKE '" + divCode + "%'");
	// // condSql = cond.toString();
	// // cond.delete(0, cond.length());
	// // // data = PrjSyncStub.getMethod().getQueryProject(condSql);
	// // break;
	// // case 1:
	// // // �Ѳ�����Ŀ
	// // cond.append(" AND DIV_CODE LIKE '" + divCode + "%'");
	// // condSql = cond.toString();
	// // cond.delete(0, cond.length());
	// // //data= PrjSyncStub.getMethod().getQueryProject2(condSql);
	// // break;
	// // }
	// }
	// }
	// }
	// switch (this.sendState.getSelectedIndex()) {
	// case 0:
	// setButtonStateBn(true);
	// data= PrjSyncStub.getMethod().getQueryProject(condSql);
	// break;
	// case 1:
	// // �Ѳ�����Ŀ
	// setButtonStateBn(false);
	// data= PrjSyncStub.getMethod().getQueryProject3(condSql);
	// break;
	// }
	//						
	// // TODO ��ѯ
	// tbPrj.setDataSet(data);// ��ѯ��Ŀ��Ϣ
	// tbPrj.reset();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return "";
	// }
	/**
	 * ��ʾ��Ŀ���ϸ��Ϣ
	 */
	public void refreshPrjDetailData(String step_id, String xmbm, String div_code, String xmxh)
	{
		try
		{
			DataSet data_detail = null;
			if ("δ����".equals(step_id))
			{
				tpaa.setAuditXmxh(xmxh);
				tpaa.setTableData();
				data_detail = PrjInputStub.getMethod().getPrjTbDetailInfo(selectYear, GlobalEx.getCurrRegion(), xmbm, div_code, xmxh);// ��ѯ��Ŀ���ϸ
			}
			if ("�Ѳ���".equals(step_id))
			{

				tpaa.setAuditXmxh(xmxh);

				tpaa.setTableData();
				data_detail = PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, xmxh, Global.getCurrRegion());
			}
			prj_tb.setDsBody(data_detail);
			prj_tb.refreshReportUI();
			((Report) prj_tb.getReportUI().getReport()).setBodyData(prj_tb.getDsBody());
			((Report) prj_tb.getReportUI().getReport()).refreshBody();
			prj_tb.getReportUI().repaint();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	/**
	 * �����ϸ�б�
	 */
	private void emptyPrjInfo()
	{
		try
		{
			prj_tb.refreshReportUI();
			prj_tb.setCurState(-1);
			// ((Report) prj_tb.getReportUI().getReport()).setBodyData(TbPrjStub
			// .getMethod().getPrjTbDetailInfo(selectYear,Global.getCurrRegion(),
			// prj_tb.getXmxh(), 0));
			((Report) prj_tb.getReportUI().getReport()).refreshBody();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	/**
	 * ���ñ�״̬
	 */
	private void setPanelEnable(boolean isEnable)
	{
		// cbQueryType.setEnabled(!isEnable);
		cbYear.setEnabled(!isEnable);
		treeEn.setEnabled(!isEnable);
		btnAcct.setEnabled(isEnable);
		tbPrj.setEnabled(!isEnable);
		tbPrj.getTable().setEnabled(!isEnable);
		tfPrjName.setEnabled(isEnable);
		tfPrjName.setEditable(isEnable);
		cbPrjState.setEnabled(isEnable);
		fxmsx.setEnabled(isEnable);
		gkdw.setEnabled(isEnable);
		cbPrjSort.setEnabled(isEnable);
		cbPrjCir.setEnabled(isEnable);
		cbYearBegin.setEnabled(isEnable);
		cbYearEnd.setEnabled(isEnable);
		fSbly.setEnabled(isEnable);
		cbYear.setEnabled(!isEnable);

	}


	/**
	 * ���ù������İ�ť״̬
	 */
	/**
	 * ���ù������İ�ť״̬
	 */

	private void setButtonStateAn(boolean add, boolean mod, boolean del, boolean save, boolean cancel)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(add);
			}
			if ("�޸�".equals(btnGet.getText()))
			{
				btnGet.setEnabled(mod);
			}
			if ("ɾ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(del);
			}
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(save);
			}
			if ("ȡ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(cancel);
			}
		}
	}


	public void setbtn(boolean add, boolean del, boolean read, boolean mod)
	{
		btnAdd.setEnabled(add);
		btnDel.setEnabled(del);
	}


	/**
	 * ���ù������İ�ť״̬
	 * 
	 * @param aState
	 */
	// public void setButtonStateBn(boolean send)
	// {
	// List controls = null;
	// if (this.getToolbarPanel() != null)
	// {
	// controls = this.getToolbarPanel().getSubControls();
	// }
	// else
	// {
	// return;
	// }
	// for (int i = 0; i < controls.size(); i++)
	// {
	// FButton btnGet = (FButton) controls.get(i);
	// if ("����".equals(btnGet.getText()))
	// {
	// btnGet.setEnabled(send);
	// }
	// }
	// }
	private String[] getBMKs(int[] rows, CustomTable table)
	{
		String[] bmks = new String[rows.length];
		for (int i = 0; i < rows.length; i++)
		{
			bmks[i] = table.rowToBookmark(rows[i]);
		}
		return bmks;
	}


	public void doImpProject()
	{
		// TODO Auto-generated method stub

	}


	public void backToAuditback()
	{
		// TODO Auto-generated method stub

	}


	public void doBackAudit()
	{
		// TODO Auto-generated method stub

	}


	public void doQueryToAudit()
	{
		// TODO Auto-generated method stub

	}


	// ˢ����
	private void refreshTreeData()
	{
		// TODO Auto-generated method stub
		try
		{
			treeds = PubInterfaceStub.getMethod().getDivDataPop(GlobalEx.loginYear);
			treeEn.setDataSet(treeds);
			treeEn.reset();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ˢ��Ԥ�㵥λ����");
			return;
		}
	}


	// ������˰�ť
	public void controlButton()
	{
		if (!selectYear.equals(GlobalEx.loginYear))
		{
			setButtonStateAn(false, false, false, false, false);

		} // else {
		// this.setButtonStateAn(true, true, true, false, false);
		// if (0 == cbQueryType.getSelectedIndex()) {
		// setButtonStateAn(true, true, true, false, false);
		//				
		// } else if (1 == cbQueryType.getSelectedIndex()) {
		// }
		// }

	}


	// /**
	// * ��ȡ��Ŀ����Ԥ���ܼ�
	// */
	// public double getBnysTotalPrice() throws Exception {
	// double result = 0;
	// DataSet bodyDS = prj_tb.getDsBody();
	// Report report = prj_tb.getReport();
	// bodyDS.gotoBookmark(report.rowToBookmark(report.getReportHeader().getRows()
	// + 2));
	// String tmp = bodyDS.fieldByName("TOTAL_SUM").getString();
	// if (!Arith.isNumeric(tmp, Arith.NUMBER_PATTERN)) {
	// tmp = "0";
	// }
	// result = Double.parseDouble(tmp);
	// return result;
	// }

	public void readHistory()
	{
		// TODO Auto-generated method stub

	}


	private InfoPackage getMsg()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (Common.isNullStr(Common.nonNullStr(tfPrjName.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ���Ʋ�����Ϊ��");
			tfPrjName.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjCir.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("ִ�����ڲ���Ϊ��");
			cbPrjCir.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjSort.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ�����Ϊ��");
			cbPrjSort.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjState.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ״̬����Ϊ��");
			cbPrjState.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(fxmsx.getText())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ����Ϊ��,��ѡ��");
			fxmsx.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(gkdw.getText())))
		{
			info.setSuccess(false);
			info.setsMessage("��ڵ�λΪ��,��ѡ��");
			gkdw.setFocus();
		}
		return info;
	}


	public void dosave()
	{
		// TODO Auto-generated method stub

	}


	public void doAuditToBack()
	{
		// TODO Auto-generated method stub

	}


	public void doExport()
	{
		// TODO Auto-generated method stub

	}


	public void doUpdatedlg()
	{
		// TODO Auto-generated method stub

	}


	public void doMerger()
	{

		String msg = "";
		try
		{
			int[] rows = tbPrj.getTable().getSelectedRows();
			if (rows.length <= 0)
			{
				JOptionPane.showMessageDialog(null, "��ѡ��ϲ���ָ����Ŀ��");
				return;
			}
			else if (rows.length <2)
			{
				JOptionPane.showMessageDialog(null, "�ϲ���ָ����Ŀ�������1�����ϣ�");
				return;
			}
			List xmxhs = new ArrayList();
			String[] prjId = new String[rows.length];
			String[] bmks = getBMKs(rows, tbPrj);
			DataSet ds = tbPrj.getDataSet();
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					prjId[i] = ds.fieldByName("XMXH").getValue().toString();
					Map values = new HashMap();
					values.put("xmxh", tbPrj.getDataSet().fieldByName("xmxh").getString());
					values.put("enid", tbPrj.getDataSet().fieldByName("en_id").getString());
					xmxhs.add(values);
				}
			}
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "��ȷ���ϲ�" + rows.length + "��ָ����?", "��ʾ", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return;
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					// msg =
					// RportProjectStub.getMethod().startPrjRportFlow("RP_PRJ_MAIN",
					// "XMXH",
					// ds.fieldByName("XMXH").getValue().toString(),GlobalEx.getModuleId());//
					// ���빤����
					PubInterfaceStub.getMethod().sendAuditInfoByDiv(xmxhs, GlobalEx.user_code, GlobalEx.getCurrRegion(), module_id);
					// PubInterfaceStub.getMethod().startflowInfoByDiv(prjId,
					// GlobalEx.user_code, GlobalEx.getCurrRegion(),
					// Global.getModuleId());

				}
			}
			// msg = RportProjectStub.getMethod().goPrjRportFlow("RP_PRJ_MAIN",
			// "XMXH", valueList, "TONEXT",GlobalEx.getModuleId());
			//			
			if (!"".equals(msg))
			{
				JOptionPane.showMessageDialog(null, msg);
				return;
			}
			refreshPrjData();
			msg = "��˳ɹ�";
			JOptionPane.showMessageDialog(null, msg);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
