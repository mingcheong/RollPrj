package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.action.PrjAuditStub;
import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.action.OperatorUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.query.action.QrBudgetAction;
import gov.nbcs.rp.query.action.QrSync;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.gl.viewer.FListPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;




public class FinshAudit extends RpModulePanel implements PrjAuditActionUI,OperatorUI
{

	private static final long serialVersionUID = 1L;

	private FSplitPane pnlBase;

	private CustomTree treeDiv = null;

	private DataSet dsDiv;

	private PrjAuditDTO pd = new PrjAuditDTO();

	CustomTree xmTree = null; // ��Ŀ��

	FTextField fxmbm = null; // ��Ŀ����

	FTextField fxmmc = null; // ��Ŀ����

	FTextField fqsnd = null; // ��ʼ���

	FTextField fjsnd = null; // �������

	FComboBox fzxzq = null; // ִ������

	FComboBox fxmfl = null; // ��Ŀ����

	FTreeAssistInput fxmsx = null;// ��Ŀ����

	FComboBox fxmzt = null; // ��Ŀ״̬

	FTreeAssistInput fkmxz = null; // ��Ŀѡ��

	FListPanel bottomPanel = null;

	FTextArea fSbly = null;

	String[] kmxzListString = null;

	FSplitPane pnlInfoXm;

	private CustomTable table;

	private String[] divCodes;

	private DataSet dsAll;

	private FButton btnE;

	private FComboBox cbQueryType;
	String moduleid = GlobalEx.getModuleId();
	FComboBox cbYear = null; // ��ѡ���
	String selectYear = GlobalEx.loginYear;

	// ����״̬ 0:������1:����
	private int locked = 0;



	public void initize()
	{
		try
		{
			initData();
			this.add(getBasePanel());
			this.createToolBar();
			this.refreshprj();
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "��ʼ�����ʧ��");
		}
	}


	private void initData() throws Exception
	{

	}


	public FSplitPane getBasePanel() throws Exception
	{
		pnlBase = new FSplitPane();
		pnlBase.setDividerLocation(200);
		pnlBase.addControl(getLeftTopPanel());
		pnlBase.addControl(getRPanel());
		return pnlBase;

	}


	/**
	 * �����
	 * 
	 * @return
	 */
	private FPanel getLeftTopPanel()
	{
		FPanel pnlLTop = new FPanel();
		FLabel label = new FLabel();
		label.setText("Ԥ�㵥λ");
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlLTop.setLayout(lay);
		getDivTree();
		CustomTreeFinder cf = new CustomTreeFinder(treeDiv);
		pnlLTop.addControl(cf, new TableConstraints(1, 1, false, true));
		pnlLTop.addControl(getTreePanel(treeDiv), new TableConstraints(10, 1, true, true));
		pnlLTop.setTopInset(10);
		pnlLTop.setLeftInset(10);
		pnlLTop.setRightInset(10);
		pnlLTop.setBottomInset(10);
		return pnlLTop;

	}


	private FScrollPane getTreePanel(CustomTree tree)
	{
		FScrollPane pnlTree = new FScrollPane();
		pnlTree.addControl(tree);
		return pnlTree;
	}


	/**
	 * ��ȡ��λ��
	 * 
	 * @return
	 */
	private CustomTree getDivTree()
	{
		try
		{
			// if (GlobalEx.isFisVis()) {
			// dsDiv = QrBudgetAction.getMethod().getDepToDivData(
			// Global.loginYear, 10, true, pd.getBathcNo());
			// } else {
			dsDiv = QrBudgetAction.getMethod().getDivDataPop(Global.loginYear, pd.getBathcNo());

			treeDiv = new CustomTree("��λ��Ϣ��", dsDiv, "En_ID", IPubInterface.DIV_CODE_NAME, IPubInterface.DIV_PARENT_ID, null, IPubInterface.DIV_CODE, true);
			treeDiv.setIsCheckBoxEnabled(true);
			treeDiv.reset();
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "������λ��ʧ��");
		}
		return treeDiv;
	}


	private FPanel getRPanel() throws Exception
	{
		FPanel pnlInfo = new FPanel();

		// FPanel pnlCF = new FPanel();
		RowPreferedLayout layCF = new RowPreferedLayout(1);
		pnlInfo.setLayout(layCF);
		//		
		//		
		//
		// pnlCF.setLeftInset(10);
		// pnlCF.setRightInset(10);
		// pnlCF.setBottomInset(10);

		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		FPanel xmxx = new FPanel();
		xmxx.setLayout(xmLay);
		fxmbm = new FTextField("��Ŀ����");
		fxmmc = new FTextField("��Ŀ����");
		fqsnd = new FTextField("��ʼ���");
		fjsnd = new FTextField("�������");

		try
		{
			List zxzqList = PrjInputStub.getMethod().getDmZxzq(Global.loginYear, Global.getCurrRegion());
			String zxzqTmp = "";
			for (int i = 0; i < zxzqList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) zxzqList.get(i);
				zxzqTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (zxzqTmp.length() > 0)
			{
				zxzqTmp = zxzqTmp.substring(0, zxzqTmp.length() - 1);
			}
			fzxzq = new FComboBox("ִ������");
			fzxzq.setRefModel(zxzqTmp);
			fzxzq.setValue("");

			List xmflList = PrjInputStub.getMethod().getDmXmfl(Global.loginYear, Global.getCurrRegion());
			String xmflTmp = "";
			for (int i = 0; i < xmflList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) xmflList.get(i);
				xmflTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (xmflTmp.length() > 0)
			{
				xmflTmp = xmflTmp.substring(0, xmflTmp.length() - 1);
			}
			fxmfl = new FComboBox("��Ŀ���");
			fxmfl.setRefModel(xmflTmp);
			fxmfl.setValue("");

			List xmsxList = PrjInputStub.getMethod().getDmXmsx(Global.loginYear, Global.getCurrRegion());
			fxmsx = new FTreeAssistInput("��Ŀ����");
			XMLData treeData = new XMLData();
			treeData.put("row", xmsxList);
			fxmsx.setData(treeData);

			List xmztList = PrjInputStub.getMethod().getDmXmzt(Global.loginYear, Global.getCurrRegion());
			String xmztTmp = "";
			for (int i = 0; i < xmztList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) xmztList.get(i);
				xmztTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (xmztTmp.length() > 0)
			{
				xmztTmp = xmztTmp.substring(0, xmztTmp.length() - 1);
			}
			fxmzt = new FComboBox("��Ŀ״̬");
			fxmzt.setRefModel(xmztTmp);
			fxmzt.setValue("");

			List kmxzList = PrjInputStub.getMethod().getDmKmxz(Global.loginYear, Global.getCurrRegion());
			fkmxz = new FTreeAssistInput("��Ŀѡ��");
			fkmxz.setProportion(0.082f);
			XMLData treeDataKm = new XMLData();
			treeDataKm.put("row", kmxzList);
			fkmxz.setData(treeDataKm);
			fkmxz.setIsCheck(true);
			btnE = new FButton("ele", "����");

		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
		btnE = new FButton("ele", "��  ��");
		btnE.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					// dsStep.cancel();
					// dsStep.applyUpdate();

					setTableProp();
					fxmbm.setValue("");
					fxmmc.setValue("");
					fzxzq.setValue("");

					fqsnd.setValue("");
					fjsnd.setValue("");
					fxmfl.setValue("");
					fxmsx.setValue("");
					fxmzt.setValue("");
					fkmxz.setValue("");

				}
				catch (Exception ee)
				{
					ErrorInfo.showErrorDialog(ee, "ȡ��ʧ��");
				}

			}
		});

		xmxx.setLeftInset(10);
		xmxx.setRightInset(10);
		xmxx.setBottomInset(10);
		xmxx.setTopInset(10);

		xmxx.addControl(fxmbm, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fxmmc, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fqsnd, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fjsnd, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fzxzq, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fxmfl, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fxmsx, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fxmzt, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fkmxz, new TableConstraints(1, 7, false, true));
		xmxx.addControl(btnE, new TableConstraints(1, 1, false, true));

		pnlInfoXm = new FSplitPane();
		pnlInfoXm.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pnlInfoXm.setDividerLocation(100);
		// pnlInfoXm.addControl(xmxx);

		// pnlInfo.addControl(pnlCF);
		// pnlInfo.addControl(pnlInfoXm);

		// FPanel pnlCF = new FPanel();

		// pnlInfo.addControl(getQueryType());

		FPanel tabpane = new FPanel();

		try
		{
			RowPreferedLayout lay1 = new RowPreferedLayout(4);
			tabpane.setLayout(lay1);
			String[] columnText = new String[] { "��λ����", "��λ����", "��Ŀ����", "��Ŀ����", "�ϼ�", "һ��Ԥ��", "����Ԥ��", "����", "�ϼ�", "״̬" };
			String[] columnField = new String[] { IPrjAudit.AuditNewInfo.DIV_CODE, IPrjAudit.AuditNewInfo.DIV_NAME, IPrjAudit.AuditNewInfo.PRJ_CODE, IPrjAudit.AuditNewInfo.PRJ_NAME,

			"smoney", "f2", "f3", "f6", "f7", "locked" };

			table = new CustomTable(columnText, columnField, null, true, null);
			setTableProp();
			tabpane.addControl(getQueryType(), new TableConstraints(1, 1, true, true));
			tabpane.addControl(getYear(), new TableConstraints(1, 1, true, true));

			tabpane.addControl(table, new TableConstraints(25, 4, true, true));
			table.getTable().addMouseListener(new MouseAdapter()
			{
				private static final long serialVersionUID = 1L;



				public void mouseClicked(MouseEvent e)
				{
					if ((e.getButton() != MouseEvent.BUTTON2) && (e.getButton() != MouseEvent.BUTTON3))
					{
						try
						{
							if (dsAll.gotoBookmark(table.rowToBookmark(table.getTable().getSelectedRow())))
							{
								fxmbm.setValue(dsAll.fieldByName("xmbm").getString());
								fxmmc.setValue(dsAll.fieldByName("xmmc").getString());
								fqsnd.setValue(dsAll.fieldByName("qsnd").getString());
								fjsnd.setValue(dsAll.fieldByName("jsnd").getString());
								fzxzq.setValue(dsAll.fieldByName("zxzq").getString());
								fxmfl.setValue(dsAll.fieldByName("xmfl").getString());
								fxmsx.setValue(dsAll.fieldByName("xmsx").getString());
								fxmzt.setValue(dsAll.fieldByName("xmzt").getString());
								String xmxh = dsAll.fieldByName("xmxh").getString();
								fkmxz.setText(QrBudgetAction.getMethod().findkmdm(xmxh));
							}
							// fkmxz.setValue(dsAll.fieldByName("kmdm").getString());

						}
						catch (Exception e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}
			});

			// tabpane.addControl(table, new TableConstraints(10, 16, true,
			// true));
		}
		catch (Exception ee)
		{
			// TODO Auto-generated catch block
			ErrorInfo.showErrorDialog(ee, "������Ŀ�б�ʧ��");
		}

		// pnlInfoXm.addControl(tabpane);
		pnlInfo.addControl(tabpane);

		return pnlInfo;

	}


	private FComboBox getYear()
	{
		cbYear = new FComboBox("��ȣ�");
		String[] years = new String[] { "2010", "2011", "2012", "2013" };
		String YearValue = "";
		for (int i = 0; i < years.length; i++)
		{
			YearValue += years[i] + "#" + years[i] + "+";
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
				if (!selectYear.equals(Global.loginYear))
				{
					setButtonState1(false);
					// setButtonStateAn(false, false, false, false, false);
				}
				else
				{
					setButtonState1(true);
				}
				// ?else setButtonStateAn(false, false, true, false, false);
				// refreshTreeData();// ˢ���б�

				refreshprj(selectYear);
			}

		});
		return cbYear;
	}


	public void setButtonState1(boolean state)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("�ر�".equals(btnGet.getText().trim()))
				continue;
			btnGet.setEnabled(state);
		}
	}


	private void setTableProp() throws Exception
	{
		table.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.reset();

		table.getTable().getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(9).setPreferredWidth(100);

		table.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTable().setRowHeight(30);
		table.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
		table.getTable().getTableHeader().setPreferredSize(new Dimension(4, 30));
	}


	public void refreshprj()
	{
		refreshprj(Global.loginYear);
	}


	public void refreshprj(String year)
	{
		// TODO ��ѯ
		try
		{
			divCodes = getDivRecords();
			// if ((divCodes == null) || (divCodes.length == 0)) {
			// JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ��Ҫ��ѯ��λ");
			// return;
			// }

			// String stepCode = pd.getMaxStepByUserCode(false,false);
			// String stepCode_P = pd.getMaxStepByUserCode(true,false);
			// String stepCode_N = pd.getMaxStepByUserCode(false,true);
			String flowstatus = "001";// Ĭ��ֵ
			// TODO ��ѯ
			switch (this.cbQueryType.getSelectedIndex())
			{
				case 0:
					// δ����
					flowstatus = "001";
					break;
				case 1:
					// ������
					flowstatus = "002";
					break;
			}
			this.dsAll = PrjAuditStub.getMethod().getAuditInfo(0, divCodes, null, year, pd.getUserCode(), pd.getDepCode(), pd.getBathcNo(), 0, "", flowstatus, moduleid);

			table.setDataSet(dsAll);

			setTableProp();

			// String str1 = String.valueOf(fxmbm.getValue());
			// String str2 = String.valueOf(fxmmc.getValue());
			// String str3 = String.valueOf(fqsnd.getValue());
			// String str4 = String.valueOf(fjsnd.getValue());
			// String str5 = String.valueOf(fzxzq.getValue());
			// String str6 = String.valueOf(fxmfl.getValue());
			// String str7 = String.valueOf(fxmsx.getValue());
			// String str8 = String.valueOf(fxmzt.getValue());
			// String[] kmxz = (String[]) fkmxz.getValue();
			// Map parMap = new HashMap();
			// parMap.put("fxmbm", str1);
			// parMap.put("fxmmc", str2);
			// parMap.put("fqsnd", str3);
			// parMap.put("fjsnd", str4);
			// parMap.put("fzxzq", str5);
			// parMap.put("fxmfl", str6);
			// parMap.put("fxmsx", str7);
			// parMap.put("fxmzt", str8);
			//
			// dsAll = QrBudgetAction.getMethod().findProjectByPara(divCodes,
			// parMap, kmxz);
			//
			// table.setDataSet(dsAll);
			// setTableProp();

		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "��ѯ����");
		}
	}


	private String[] getDivRecords()
	{
		MyTreeNode root = (MyTreeNode) treeDiv.getRoot(); // ���ڵ�
		Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
		int i = 0;
		divCodes = new String[this.treeDiv.getSelectedNodeCount(true)];
		while (enumeration.hasMoreElements())
		{ // ��ʼ����
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (node.getChildCount() > 0)
			{
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null)
			{
				continue;
			}
			if (pNode.getIsSelect())
			{
				// ����ýڵ�Ϊѡ��״̬�����������list��
				divCodes[i] = (pNode.getShowContent().substring(pNode.getShowContent().indexOf("[") + 1, pNode.getShowContent().indexOf("]")));
				i++;
			}
		}
		try
		{
			String bmk = dsDiv.toogleBookmark();
			dsDiv.maskDataChange(true);
			for (int k = 0; k < divCodes.length; k++)
			{
				if (dsDiv.locate("EID", divCodes[k]))
				{
					divCodes[k] = dsDiv.fieldByName("DIV_CODE").getString();
				}
			}
			dsDiv.maskDataChange(false);
			dsDiv.gotoBookmark(bmk);
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "��ȡ��ѡ��λ����");
		}
		return divCodes;
	}


	public void doSave()
	{
		int count = table.getTable().getRowCount();
		int num = 0;
		for (int i = 0; i < count; i++)
		{
			Object check = table.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue())
			{
				num++;
			}
		}
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "��ѡ����Ҫͬ������Ŀ");
			return;
		}

		try
		{
			String xmxhs = "";
			for (int i = 0; i < count; i++)
			{
				Object check = table.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (table.getDataSet().gotoBookmark(table.rowToBookmark(i)))
					{
						String xmxh = table.getDataSet().fieldByName("xmbm").getString();
						xmxhs += "'" + xmxh + "',";
					}
				}
			}
			if (xmxhs.length() > 0)
			{
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
			}
			String whereSql = " and prj_code in (" + xmxhs + ")";
			int typeCode = 1;
			QrSync.getMethod().syncTableByType(Global.loginYear, typeCode, whereSql);

			JOptionPane.showMessageDialog(null, "ͬ���ɹ�");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ͬ��ʧ��");
			e.printStackTrace();
		}
	}


	public void doSendAudit()
	{

		int[] rows = table.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		List list = new ArrayList();
		String[] prjCodes = new String[rows.length];

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = table.rowToBookmark(rows[i]);

				if (table.getDataSet().gotoBookmark(bmk))
				{
					Map values = new HashMap();
					values.put("xmxh", table.getDataSet().fieldByName("xmxh").getString());
					values.put("enid", table.getDataSet().fieldByName("en_id").getString());
					list.add(values);
					prjCodes[i] = table.getDataSet().fieldByName("xmxh").getString();
				}
			}

			PubInterfaceStub.getMethod().savehistory(prjCodes, true);

			PrjAuditStub.getMethod().finshAudit(list, GlobalEx.user_code, GlobalEx.getCurrRegion(), Global.loginYear, moduleid, 0);

			JOptionPane.showMessageDialog(Global.mainFrame, "�´�ɹ�");
			refreshprj();

		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "�´�ʧ��");

		}

	}


	public void doImpProject()
	{
		// TODO Auto-generated method stub

	}


	public void doBackAudit()
	{
		// TODO Auto-generated method stub

		int[] rows = table.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		// String[] prjcodes = new String[rows.length];
		List list = new ArrayList();
		// ֻ��û����˼�¼������˽��������������ʸ�

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = table.rowToBookmark(i);

				if (table.getDataSet().gotoBookmark(bmk))
				{
					Map values = new HashMap();
					values.put("xmxh", table.getDataSet().fieldByName("xmxh").getString());
					values.put("enid", table.getDataSet().fieldByName("en_id").getString());
					list.add(values);
				}
			}

			PrjAuditStub.getMethod().backFinshAudit(list, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid, 1);
			// }
			JOptionPane.showMessageDialog(null, "�����ɹ�");
			refreshprj();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "����ʧ��");

		}
		// TODO Auto-generated method stub

	}


	// private void setButtonStateModify(boolean isEditEnable) {
	// if (this.getToolbarPanel() == null) {
	// return;
	// }
	// List controls = this.getToolbarPanel().getSubControls();
	// for (int i = 0; i < controls.size(); i++) {
	// FButton btnGet = (FButton) controls.get(i);
	//
	// if ("�´�".equals(btnGet.getText())) {
	// btnGet.setEnabled(isEditEnable);
	// }
	// if ("�����´�".equals(btnGet.getText())) {
	// btnGet.setEnabled(!isEditEnable);
	// }
	//
	// }
	// }

	public void backToAuditback()
	{
		// TODO Auto-generated method stub

	}


	public void doBackToAudit()
	{
		// TODO Auto-generated method stub

	}


	public void doBackToDivAudit()
	{
		// TODO Auto-generated method stub

	}


	public void doExport()
	{
		// TODO Auto-generated method stub

	}


	public void doExprtOtherInfo()
	{
		// TODO Auto-generated method stub

	}


	public void doImportData()
	{
		// TODO Auto-generated method stub

	}


	public void doSendTo()
	{
		// TODO Auto-generated method stub

	}


	public void doDisable()
	{
		locked = 0;
		doModify();
	}


	public void doEnable()
	{
		locked = 1;
		doModify();
	}


	/**
	 * �޸���Ŀ״̬�����������
	 * 
	 * @return
	 */
	public void doModify()
	{

		int[] rows = table.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "������ѡ��һ����¼���в���!");
			return;
		}

		String ids = "";
		try
		{
			for (int i = 0; i < rows.length; i++)
			{
				String bmk = table.rowToBookmark(i);

				if (table.getDataSet().gotoBookmark(bmk))
				{
					ids += "'" + table.getDataSet().fieldByName("xmxh").getString() + "',";
				}

			}
			if (StringUtils.isNotEmpty(ids))
			{
				ids = ids.substring(0, ids.length() - 1);
				StringBuffer sql = new StringBuffer("UPDATE RP_XMJL T");
				sql.append(" SET T.LOCKED = ").append(locked);
				sql.append(" WHERE T.XMXH IN ");
				sql.append("(").append(ids).append(") AND T.SET_YEAR = ").append(Global.loginYear);

				QueryStub.getClientQueryTool().executeUpdate(sql.toString());
				String strLock = "";
				if (locked == 1)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "������!");
					strLock = "����";
				}
				else
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "�ѽ���!");
					strLock = "δ����";
				}

				for (int i = 0; i < rows.length; i++)
				{
					table.getTable().setValueAt(strLock, rows[i], 10);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��!");
			}

		}
		catch (Exception e1)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��!");
		}
	}


	public void dosearch()
	{
		// TODO Auto-generated method stub
		this.refreshprj();
	}


	private FComboBox getQueryType() throws Exception
	{
		cbQueryType = new FComboBox("");
		cbQueryType.setTitle("��Ŀ״̬��");
		cbQueryType.setProportion(0.4f);
		cbQueryType.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent arg0)
			{
				refreshprj();
			}
		});
		setStatusCbx(cbQueryType);
		try
		{
			createToolBar();
		}
		catch (Exception e)
		{

		}
		return cbQueryType;
	}

}
