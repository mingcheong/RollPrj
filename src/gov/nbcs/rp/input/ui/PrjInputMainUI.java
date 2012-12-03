package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import mappingfiles.sysdb.RpXmjl;
import mappingfiles.sysdb.RpXmjlKm;
import mappingfiles.sysdb.RpXmsb;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;




public class PrjInputMainUI extends RpModulePanel implements PrjActionUI
{
	protected AcctSelectDialog acctSelect = new AcctSelectDialog();
	private static final long serialVersionUID = 1L;

	/** -1 ���״̬ 0:���� 1���޸� */
	private int state = -1;

	CustomTree treeEn = null;

	CustomTable tbPrj = null;

	DataSet dsPrj = null;

	/** ��Ŀ�� */
	CustomTree treePrj = null;

	/** ��Ŀ���� */
	FTextField tfPrjCode = null; // ��Ŀ����

	/** ��Ŀ���� */
	FTextField tfPrjName = null; // ��Ŀ����

	/** ��ʼ��� */
	FComboBox cbYearBegin = null; // ��ʼ���

	/** ������� */
	FComboBox cbYearEnd = null; // �������

	/** ִ������ */
	FComboBox cbPrjCir = null; // ִ������

	/** ��Ŀ���� */
	FComboBox cbPrjSort = null; // ��Ŀ����

	/** ��Ŀ���� */
	FTreeAssistInput fxmsx = null;// ��Ŀ����

	/** ��Ŀ״̬ */
	FComboBox cbPrjState = null; // ��Ŀ״̬

	/** ��Ŀѡ�� */
	FTextField tfAcct = null; // ��Ŀѡ��

	/** ���ܿ�Ŀѡ��ť */
	FButton btnAcct;

	/** ѡ�񷵻صĹ��ܿ�Ŀ���� */
	private String[] sAcctCodes;

	/** ѡ�񷵻صĹ��ܿ�Ŀ���� */
	private String[] sAcctNames;

	/** ѡ�񷵻صĹ�����ˮ�� */
	private String[] sAcctBsIDs;

	/** ��Ŀ״̬ */
	private List xmztList;

	/** ��Ŀ���� */
	private List xmflList;

	private PrjInputDTO dto = new PrjInputDTO();

	FComboBox cbYear = null; // ��ѡ���
	String selectYear = GlobalEx.loginYear;



	public void initize()
	{
		try
		{
			this.add(getBasePanel());
			this.createToolBar();
			setPanelEnable(false);
			setButtonState(true);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "�����ʼ��ʧ��");
		}
	}


	/**
	 * ���ù������İ�ť״̬
	 * 
	 * @param aState
	 */
	public void setButtonState1(boolean state)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("�޸�".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("ɾ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("ȡ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("�����޸�".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
		}
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
				if (!selectYear.equals(GlobalEx.loginYear))
				{
					// setCompEnable(false);
					setButtonState1(false);
				}
				else
				{
					setButtonState1(true);
				}
				// ?else setButtonStateAn(false, false, true, false, false);
				// refreshTreeData();// ˢ���б�

				refreshPrjData(selectYear);
			}

		});
		return cbYear;
	}


	/**
	 * ��ȡ�����
	 * 
	 * @return
	 * @throws Exception
	 */
	private FPanel getBasePanel() throws Exception
	{

		FPanel pnlff = new FPanel();
		RowPreferedLayout layff = new RowPreferedLayout(1);
		pnlff.setLayout(layff);

		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		FPanel xmxx = new FPanel();
		xmxx.setLayout(xmLay);

		tfPrjCode = new FTextField("��Ŀ����");
		tfPrjCode.setEnabled(false);
		tfPrjCode.setEditable(false);
		tfPrjName = new FTextField("��Ŀ����");
		cbYearBegin = new FComboBox("��ʼ���");
		String qsndTmp = "";
		int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
		for (int i = 0; i <= 5; i++)
		{

			qsndTmp += (numQs + i) + "#" + (numQs + i) + "+";

		}
		if (qsndTmp.length() > 0)
		{
			qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
		}
		cbYearBegin.setRefModel(qsndTmp);
		cbYearBegin.setValue("");

		cbYearEnd = new FComboBox("�������");
		String jsndTmp = "";
		int num = Integer.parseInt(GlobalEx.loginYear);
		for (int i = 0; i < 10; i++)
		{
			jsndTmp += (num + i) + "#" + (num + i) + "+";
		}
		if (jsndTmp.length() > 0)
		{
			jsndTmp = jsndTmp.substring(0, jsndTmp.length() - 1);
		}
		cbYearEnd.setRefModel(jsndTmp);
		cbYearEnd.setValue("");
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
			cbPrjCir = new FComboBox("ִ������");
			cbPrjCir.setRefModel(zxzqTmp);
			cbPrjCir.setValue("");
			cbPrjCir.addValueChangeListener(new ValueChangeListener()
			{
				public void valueChanged(ValueChangeEvent e)
				{

					if (cbPrjCir.getSelectedIndex() != 2)
					{
						// ���������ԣ��������ʼ���ֹ���ǵ�ǰ��ȣ������ɱ༭
						cbYearBegin.setSelectedIndex(5);
						cbYearEnd.setSelectedIndex(0);
						cbYearBegin.setEnabled(false);
						cbYearEnd.setEnabled(false);
						dto.setOneYearPrj(true);
					}
					else
					{
						// ����������ԣ�����Ա༭
						cbYearBegin.setEnabled(true);
						cbYearEnd.setEnabled(true);
					}

				}
			});

			xmflList = PrjInputStub.getMethod().getDmXmfl(Global.loginYear, Global.getCurrRegion());
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
			cbPrjSort = new FComboBox("��Ŀ���");
			cbPrjSort.setRefModel(xmflTmp);
			cbPrjSort.setValue("");

			List xmsxList = PrjInputStub.getMethod().getDmXmsx(Global.loginYear, Global.getCurrRegion());
			fxmsx = new FTreeAssistInput("��Ŀ����");
			XMLData treeData = new XMLData();
			treeData.put("row", xmsxList);
			fxmsx.setData(treeData);
			fxmsx.setIsCheck(true);
			fxmsx.setOnlyLeafCanBeSelected(true);
			fxmsx.addValueChangeListener(new ValueChangeListener()
			{
				public void valueChanged(ValueChangeEvent e)
				{
					String[] fxmsxStr = fxmsx.getValue() == null ? null : (String[]) fxmsx.getValue();
					if (fxmsxStr == null)
						return;
					String[] fxmsxf1 = new String[fxmsxStr.length];
					String xmsxstr = "";
					try
					{

						for (int i = 0; i < fxmsxStr.length; i++)
						{

							DataSet ds1 = DBSqlExec.client().getDataSet("select * from dm_xmsx  where chr_id ='" + fxmsxStr[i] + "'");
							ds1.fieldByName("chr_code").getValue();
							xmsxstr += ds1.fieldByName("chr_code").getValue().toString() + " " + ds1.fieldByName("chr_name").getValue().toString() + ";";
							fxmsxf1[i] = ds1.fieldByName("chr_code1").getValue().toString();
							if (i > 0)
							{
								if (!fxmsxf1[i].equals(fxmsxf1[i - 1]))
								{
									JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ���Դ����������޸�,�������ͷ������ظ�ѡ��");
									fxmsx.setValue("");

								}

							}
							if (xmsxstr.length() > 0)
							{
								xmsxstr = xmsxstr.substring(0, xmsxstr.length() - 1);
							}

						}
					}
					catch (Exception ee)
					{
						// TODO Auto-generated catch block
						ee.printStackTrace();
					}

				}
			});

			xmztList = PrjInputStub.getMethod().getDmXmzt(Global.loginYear, Global.getCurrRegion());
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
			cbPrjState = new FComboBox("��Ŀ״̬");
			cbPrjState.setRefModel(xmztTmp);
			cbPrjState.setValue("");

			tfAcct = new FTextField("��Ŀѡ��");
			tfAcct.setProportion(0.083f);
			tfAcct.setEnabled(false);
			tfAcct.setEditable(false);
			btnAcct = new FButton("btnAcct", "ѡ���Ŀ");
			btnAcct.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					acctSelect.showAcctSelect(Common.nonNullStr(tfAcct.getValue()));
					Tools.centerWindow(acctSelect);
					acctSelect.setVisible(true);
					if (acctSelect.isOK)
					{
						List value = acctSelect.getTreeSelect();
						if (value != null && !value.isEmpty())
						{
							sAcctCodes = (String[]) value.get(0);
							sAcctNames = (String[]) value.get(1);
							String rvalue = Common.nonNullStr(value.get(2));
							sAcctBsIDs = (String[]) value.get(3);
							tfAcct.setValue(rvalue);
						}
					}
				}
			});
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmxx.setLeftInset(10);
		xmxx.setRightInset(10);
		xmxx.setBottomInset(10);
		xmxx.addControl(getYear(), new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfPrjCode, new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfPrjName, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjSort, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fxmsx, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjCir, new TableConstraints(1, 2, false, true));

		xmxx.addControl(cbYearBegin, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbYearEnd, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjState, new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfAcct, new TableConstraints(1, 7, false, true));
		xmxx.addControl(btnAcct, new TableConstraints(1, 1, false, true));

		pnlff.addControl(xmxx, new TableConstraints(2, 1000, true, true));

		tbPrj = new CustomTable(new String[] { "��Ŀ����", "��Ŀ����", "��ʼ���", "�������", "ִ������", "��Ŀ����", "��Ŀ����", "��Ŀ״̬", "��Ŀ" }, new String[] { "xmbm", "xmmc", "c1", "c2", "c3", "c4", "c5", "c6", "accts" },
				null, true, null);
		tbPrj.reset();
		tbPrj.getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(9).setPreferredWidth(100);
		tbPrj.setSize(2000, 2000);
		tbPrj.getTable().getTableHeader().setBackground(new Color(250, 228, 184));

		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(200);
		FScrollPane pnlTree = new FScrollPane();
		DataSet ds = PubInterfaceStub.getMethod().getDivDataPop(GlobalEx.loginYear);
		treeEn = new CustomTree("Ԥ�㵥λ", ds, "en_id", "code_name", "parent_id", null, "div_code");
		treeEn.reset();
		treeEn.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1 && state == -1)
				{
					refreshPrjData();
					setPanelValue(true);
				}
			}
		});
		CustomTreeFinder cf = new CustomTreeFinder(treeEn);
		pnlTree.addControl(treeEn);
		FPanel pnlCF = new FPanel();
		RowPreferedLayout layCF = new RowPreferedLayout(1);
		pnlCF.setLayout(layCF);
		pnlCF.addControl(cf, new TableConstraints(1, 1, false, true));
		pnlCF.addControl(pnlTree, new TableConstraints(1, 1, true, true));
		pnlCF.setTopInset(10);
		pnlCF.setLeftInset(10);
		pnlCF.setRightInset(10);
		pnlCF.setBottomInset(10);

		FPanel pnlXM = new FPanel();
		RowPreferedLayout layXM = new RowPreferedLayout(4);
		pnlXM.setLayout(layXM);

		pnlff.addControl(tbPrj, new TableConstraints(10, 1000, true, true));

		pnlXM.addControl(pnlff, new TableConstraints(10, 1000, true, true));

		pnlXM.setTopInset(10);
		pnlXM.setLeftInset(10);
		pnlInfo.addControl(pnlCF);
		pnlInfo.addControl(pnlXM);

		pnlInfo.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		pnlBase.addControl(pnlInfo, new TableConstraints(1, 1, true, true));
		return pnlBase;
	}


	public void refreshPrjData()
	{
		refreshPrjData(Global.loginYear);
	}


	private void refreshPrjData(String year)
	{
		if (treeEn.getSelectedNode() != null)
		{
			MyPfNode node = (MyPfNode) treeEn.getSelectedNode().getUserObject();
			if (node == null)
				return;
			if (state != -1)
				return;
			StringBuffer filter = new StringBuffer();
			filter.append(" and a.set_year = " + year);
			filter.append(" and a.rg_code = " + Global.getCurrRegion());
			DataSet ds = treeEn.getDataSet();
			try
			{
				if (treeEn.getSelectedNode() != treeEn.getRoot())
				{
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
					{
						String divCode = ds.fieldByName("div_code").getString();
						filter.append(" and a.div_code like '" + divCode + "%'");
					}
				}
				// filter.append(" order by xmbm");
				tbPrj.setDataSet(PrjInputStub.getMethod().getPrjCreateInfo(filter.toString()));
				tbPrj.reset();
				setTableProp();
				// setPanelValue(true);
			}
			catch (Exception ee)
			{
				ErrorInfo.showErrorDialog(ee, "ˢ�µ�λ��Ϣ����");
				return;
			}

		}

	}


	public void doAdd()
	{
		// TODO ���Ӳ���
		try
		{
			MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
			if (nodeSel != null && nodeSel.isLeaf())
			{
				state = 0;
				setPanelEnable(true);
				setPanelValue(true);
				setButtonState(false);
				String aPrjCode = dto.getNewPrjCode(treeEn.getDataSet().fieldByName("div_code").getString());
				// PrjInputStub.getMethod()
				// .getXmbm(
				// treeEn.getDataSet().fieldByName("div_code")
				// .getString(),GlobalEx.loginYear);
				tfPrjCode.setValue(aPrjCode);
				cbPrjState.setSelectedIndex(0);
				tfPrjName.setFocus();
				tbPrj.getDataSet().append();
				tbPrj.getDataSet().fieldByName("set_year").setValue(Global.loginYear);
				tbPrj.getDataSet().fieldByName("rg_code").setValue(Global.getCurrRegion());
				tbPrj.getDataSet().fieldByName("xmxh").setValue(UUIDRandom.generate());
				tbPrj.getDataSet().fieldByName("xmbm").setValue(aPrjCode);
				tbPrj.getDataSet().fieldByName("en_id").setValue(treeEn.getDataSet().fieldByName("en_id").getString());
				tbPrj.getDataSet().fieldByName("div_code").setValue(treeEn.getDataSet().fieldByName("div_code").getString());
				tbPrj.getDataSet().fieldByName("div_name").setValue(treeEn.getDataSet().fieldByName("div_name").getString());
				tbPrj.getDataSet().fieldByName("lrr_dm").setValue(GlobalEx.getUserId());
				tbPrj.getDataSet().fieldByName("lrrq").setValue(new Date());
				tbPrj.getDataSet().fieldByName("xgr_dm").setValue(GlobalEx.getUserId());
				tbPrj.getDataSet().fieldByName("xgrq").setValue(new Date());
				tbPrj.getDataSet().fieldByName("gkdw_id").setValue(treeEn.getDataSet().fieldByName("en_id").getString());
				tbPrj.getDataSet().fieldByName("gkdw_code").setValue(treeEn.getDataSet().fieldByName("div_code").getString());
				tbPrj.getDataSet().fieldByName("gkdw_name").setValue(treeEn.getDataSet().fieldByName("div_name").getString());

			}
			else
			{
				JOptionPane.showMessageDialog(null, "��ѡ��Ԥ�㵥λ");
				return;
			}
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "����ʧ��");
		}
	}


	public void doDelete()
	{
		// TODO ɾ������
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
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "��ѡ����Ŀ");
			return;
		}
		try
		{
			String xmxhs = "";
			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						xmxhs += "'" + xmxh + "',";
					}
				}
			}
			if (xmxhs.length() > 0)
			{
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
			}
			if (!xmxhs.equals(""))
			{
				List isExists = PrjInputStub.getMethod().isExistsXm(xmxhs);
				String xmmcs = "";
				if (isExists.size() > 0)
				{
					for (int i = 0; i < isExists.size(); i++)
					{
						Map map = new HashMap();
						map = (Map) isExists.get(i);
						xmmcs += map.get("xmmc").toString() + ",";
					}
				}
				if (xmmcs.length() > 0)
				{
					xmmcs = xmmcs.substring(0, xmmcs.length() - 1);
					JOptionPane.showMessageDialog(null, "������Ŀ�Ѿ���ʹ�ã�����ɾ��\n" + xmmcs);
					return;
				}
			}
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "ȷ��ɾ����ѡ��Ŀ��", "��ʾ", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) { return; }

			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						List listSql = new ArrayList();
						listSql.add("delete from rp_xmjl_km where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " ");
						listSql.add("delete from rp_xmjl where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
						PrjInputStub.getMethod().postData(listSql);
					}
				}
			}
			// MyPfNode enID = (MyPfNode)
			// treeEn.getSelectedNode().getUserObject();
			// dsPrj = PrjInputStub.getMethod().getXmByEnId(GlobalEx.loginYear,
			// GlobalEx.getCurrRegion(), enID.getValue());
			// tbPrj.setDataSet(dsPrj);
			// tbPrj.reset();
			// setTableProp();
			refreshPrjData();
			JOptionPane.showMessageDialog(null, "ɾ���ɹ�");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ɾ��ʧ��");
			e.printStackTrace();
		}
	}


	public void doModify()
	{
		// TODO �޸Ĳ���
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
			JOptionPane.showMessageDialog(null, "��ѡ��һ����Ŀ�����޸�");
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
					JOptionPane.showMessageDialog(null, "��ѡ����Ŀ");
					return;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "��ѡ����Ŀ");
				return;
			}

		}
		catch (HeadlessException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// PrjInfoDlg.setDoStates(PrjInfoDlg.IS_EDIT);
		// PrjInfoDlg userInfo = new PrjInfoDlg(this, treeEn, tbPrj, dsPrj);
		// setSizeAndLocation(userInfo);
		// userInfo.setVisible(true);
		state = 1;
		setPanelEnable(true);

		setButtonState(false);

		try
		{
			tbPrj.getDataSet().edit();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void doSave()
	{
		// TODO �������
		String bmk = tbPrj.getDataSet().toogleBookmark();
		InfoPackage info = checkData();
		if (!info.getSuccess())
		{
			JOptionPane.showMessageDialog(Global.mainFrame, info.getsMessage());
			return;
		}
		else
		{
			info = saveData();
			if (!info.getSuccess())
			{
				JOptionPane.showMessageDialog(Global.mainFrame, info.getsMessage());
				return;
			}
		}
		setPanelEnable(false);
		setButtonState(true);
		state = -1;
		refreshPrjData();
		try
		{
			if (tbPrj.getDataSet().gotoBookmark(bmk))
			{
				DataSet ds = tbPrj.getDataSet();
				setPanelValue(ds.fieldByName("xmbm").getString(), ds.fieldByName("xmmc").getString(), ds.fieldByName("c1").getString(), ds.fieldByName("c2").getString(), ds.fieldByName("c3")
						.getString(), ds.fieldByName("c4").getString(), ds.fieldByName("c5").getString(), ds.fieldByName("c6").getString(), ds.fieldByName("accts").getString());
			}

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private InfoPackage checkData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		String yb = cbYearBegin.getValue() == null ? "" : cbYearBegin.getValue().toString();
		String ye = cbYearEnd.getValue() == null ? "" : cbYearEnd.getValue().toString();
		if (Common.isNullStr(Common.nonNullStr(tfPrjName.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ���Ʋ�����Ϊ��");
			tfPrjName.setFocus();
		}
		MyPfNode enID = (MyPfNode) treeEn.getSelectedNode().getUserObject();
		List list = null;
		try
		{
			list = PrjInputStub.getMethod().isExistsXmmc(enID.getValue(), Common.nonNullStr(tfPrjName.getValue()), Global.loginYear, Global.getCurrRegion());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (state == 0)
		{
			// ��������ӣ��������ӵ���Ŀ�����ڸõ�λ�²������ظ�
			if (list != null && list.size() > 0)
			{
				info.setSuccess(false);
				info.setsMessage("��Ŀ�����Ѿ����ڣ�����������");
				tfPrjName.setFocus();
				return info;
			}
		}
		else if (state == 1)
		{
			// ������޸ģ����޸ĵ���Ŀ���Ʋ�������ڣ����벻ͬ��������ͬ
			int i = 0;
			try
			{
				i = DBSqlExec.client().getRecordCount(
						"rp_xmjl",
						"xmmc='" + Common.nonNullStr(tfPrjName.getValue()) + "' and xmxh!='" + tbPrj.getDataSet().fieldByName("xmxh").getString() + "'and div_code='"
								+ tbPrj.getDataSet().fieldByName("gkdw_code").getString() + "' and set_year =" + Global.loginYear);
			}
			catch (Exception ee)
			{
				i = 0;
			}
			if (i > 0)
			{
				info.setSuccess(false);
				info.setsMessage("��Ŀ�����Ѿ����ڣ�����������");
				tfPrjName.setFocus();
				return info;
			}
		}
		if (!Common.isNullStr(yb) && !Common.isNullStr(ye))
		{
			if (Integer.valueOf(yb).intValue() > Integer.valueOf(ye).intValue())
			{
				info.setSuccess(false);
				info.setsMessage("��ʼ��Ȳ�������ڽ�����ȣ�����������");
				cbYearBegin.setFocus();
				return info;
			}
		}
		else
		{
			info.setSuccess(false);
			info.setsMessage("��ʼ���/������� ������Ϊ�գ�����������");
			return info;
		}
		return info;
	}


	private InfoPackage saveData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try
		{
			String xmmcStr = tfPrjName.getValue().toString();
			String qsndStr = cbYearBegin.getValue() == null ? "" : cbYearBegin.getValue().toString();
			String jsndStr = cbYearEnd.getValue() == null ? "" : cbYearEnd.getValue().toString();
			String zxzqStr = cbPrjCir.getValue() == null ? "" : cbPrjCir.getValue().toString();
			String xmflStr = getPrjSortCode();
			String xmsxStr = fxmsx.getText().toString();
			String xmztStr = getPrjStateCode();
			String accts = Common.nonNullStr(tfAcct.getValue());
			tbPrj.getDataSet().fieldByName("xmmc").setValue(xmmcStr);
			tbPrj.getDataSet().fieldByName("c1").setValue(qsndStr);
			tbPrj.getDataSet().fieldByName("c2").setValue(jsndStr);
			tbPrj.getDataSet().fieldByName("c3").setValue(zxzqStr);
			tbPrj.getDataSet().fieldByName("c4").setValue(xmflStr);
			tbPrj.getDataSet().fieldByName("c5").setValue(xmsxStr);
			tbPrj.getDataSet().fieldByName("c6").setValue(xmztStr);
			tbPrj.getDataSet().fieldByName("accts").setValue(accts);
			PrjInputStub.getMethod().savePrjCreateInfo(Global.loginYear, tbPrj.getDataSet().fieldByName("xmxh").getString(), tbPrj.getDataSet(), sAcctBsIDs, Global.getCurrRegion(), "-1", "0");
		}
		catch (Exception ee)
		{
			info.setSuccess(false);
			info.setsMessage("����ʧ�ܣ�������ϢΪ:" + ee.getMessage());
		}
		return info;
	}


	public void doCancel()
	{
		// TODO ȡ������
		setPanelEnable(false);
		setButtonState(true);
		state = -1;
		try
		{
			tbPrj.getDataSet().cancel();
			tbPrj.getDataSet().applyUpdate();
			refreshPrjData();
		}
		catch (Exception ee)
		{

		}
	}


	public void doImpProject()
	{
		try
		{
			List dbList = PrjInputStub.getMethod().getDBLink();
			if (dbList.size() == 0)
			{
				JOptionPane.showMessageDialog(null, "������DBLink����");
				return;
			}

			String dbLinkName = ((Map) dbList.get(0)).get("dblink_name").toString();
			// ��ͬ����Ŀ
			// ���������Ŀ��е�FB_P_BASE������Ǩ�Ƶ�RP_XMJL����ȥ
			// һ����ȫ�����룬�����л��ڴ���������Էֵ�λ����
			List xmList = null;
			List kmList = null;
			List tbList = null;

			List disDivCode = PrjInputStub.getMethod().getDisDivCode(dbLinkName);

			for (int i = 0; i < disDivCode.size(); i++)
			{

				Map m = (Map) disDivCode.get(i);
				String divCode = m.get("div_code").toString();
				List baseData = PrjInputStub.getMethod().getDBDataXm(dbLinkName, "FB_P_BASE", divCode);
				for (int k = 0; k < baseData.size(); k++)
				{
					xmList = new ArrayList();
					tbList = new ArrayList();
					kmList = new ArrayList();

					RpXmjl rpXm = new RpXmjl();
					Map map = (Map) baseData.get(k);
					String xmbm = PrjInputStub.getMethod().getXmbm(divCode, Global.loginYear);
					String xmxh = UUIDRandom.generate();
					rpXm.setSetYear(Long.valueOf(map.get("set_year").toString()));
					rpXm.setXmxh(xmxh);
					rpXm.setXmbm(xmbm);
					rpXm.setXmmc(map.get("prj_name").toString());
					rpXm.setC4(map.get("prjsort_code").toString());
					rpXm.setEnId(map.get("en_id").toString());
					rpXm.setLrrDm(GlobalEx.getUserId());
					rpXm.setLrrq(new Date());
					rpXm.setXgrDm(GlobalEx.getUserId());
					rpXm.setXgrq(new Date());
					rpXm.setDivCode(divCode);
					rpXm.setC6("001");
					rpXm.setDivName(map.get("div_name").toString());
					rpXm.setRgCode(map.get("rg_code").toString());
					// rpXm.setGkdwCode("div_code");
					// rpXm.setGkdwId(map.get("en_id").toString())
					xmList.add(rpXm);

					// ��Ŀ
					List dbDataKm = PrjInputStub.getMethod().getDisDivKm(dbLinkName, divCode, map.get("prj_code").toString());
					for (int p = 0; p < dbDataKm.size(); p++)
					{

						Map mm = (Map) dbDataKm.get(p);
						RpXmjlKm rpKm = new RpXmjlKm();
						rpKm.setSetYear(Long.valueOf(GlobalEx.loginYear));
						rpKm.setXhid(UUIDRandom.generate());
						rpKm.setRpXmjl(rpXm);
						rpKm.setKmdm(mm.get("bs_id").toString());
						kmList.add(rpKm);
					}

					// �
					List tbData = PrjInputStub.getMethod().getDataTbxm(dbLinkName, divCode, map.get("prj_code").toString());
					int num = 1;
					int sbCode = 1;
					Map mTb = null;
					for (int tb = 0; tb < tbData.size(); tb++)
					{

						mTb = (Map) tbData.get(tb);
						RpXmsb rpSb = new RpXmsb();
						String detailType = mTb.get("detail_type").toString();

						rpSb.setSetYear(Long.valueOf(mTb.get("set_year").toString()));
						rpSb.setXmsbid(UUIDRandom.generate());
						rpSb.setRpXmjl(rpXm);
						Double f2 = Double.valueOf(mTb.get("f2").toString());
						Double f3 = Double.valueOf(mTb.get("f3").toString());
						Double f6 = Double.valueOf(mTb.get("f6").toString());
						Double f7 = Double.valueOf(mTb.get("f7").toString());
						double total = Double.parseDouble(mTb.get("f2").toString()) + Double.parseDouble(mTb.get("f3").toString()) + Double.parseDouble(mTb.get("f6").toString())
								+ Double.parseDouble(mTb.get("f7").toString());
						rpSb.setF2(f2);
						rpSb.setF3(f3);
						rpSb.setF6(f6);
						rpSb.setF7(f7);
						rpSb.setEnId(mTb.get("en_id").toString());
						rpSb.setRgCode(mTb.get("rg_code").toString());
						rpSb.setWsztDm("0");
						rpSb.setLrrDm(GlobalEx.getUserId());
						rpSb.setLrrq(new Date());
						rpSb.setXgrDm(GlobalEx.getUserId());
						rpSb.setXgrq(new Date());
						rpSb.setTotalSum(Double.valueOf(total + ""));
						if ("1".equals(detailType))
						{
							rpSb.setSbType("��Ԥ��");
							rpSb.setSbCode("111");
						}
						else if ("2".equals(detailType))
						{
							rpSb.setSbType("�Ѱ�����");
							rpSb.setSbCode("222");
						}
						else if ("11".equals(detailType))
						{
							rpSb.setSbType(num++ + "");
							rpSb.setBsId(mTb.get("bs_id").toString());
							rpSb.setBsiId(mTb.get("bsi_id") == null ? "" : mTb.get("bsi_id").toString());
							int sbCodeLength = (sbCode + "").length();
							if (sbCodeLength == 1)
							{
								rpSb.setSbCode("40000" + sbCode++);
							}
							else if (sbCodeLength == 2)
							{
								rpSb.setSbCode("4000" + sbCode++);
							}
							else if (sbCodeLength == 3)
							{
								rpSb.setSbCode("400" + sbCode++);
							}
							else if (sbCodeLength == 4)
							{
								rpSb.setSbCode("40" + sbCode++);
							}
							else if (sbCodeLength == 5)
							{
								rpSb.setSbCode("4" + sbCode++);
							}
						}
						tbList.add(rpSb);

					}
					RpXmsb rpSbTS = new RpXmsb();
					rpSbTS.setSetYear(Long.valueOf(mTb.get("set_year").toString()));
					rpSbTS.setXmsbid(UUIDRandom.generate());
					rpSbTS.setRpXmjl(rpXm);
					rpSbTS.setEnId(mTb.get("en_id").toString());
					rpSbTS.setRgCode(mTb.get("rg_code").toString());
					rpSbTS.setWsztDm("0");
					rpSbTS.setLrrDm(GlobalEx.getUserId());
					rpSbTS.setLrrq(new Date());
					rpSbTS.setXgrDm(GlobalEx.getUserId());
					rpSbTS.setXgrq(new Date());
					rpSbTS.setSbType("����Ԥ��");
					rpSbTS.setSbCode("333");
					rpSbTS.setF2(Double.valueOf("0"));
					rpSbTS.setF3(Double.valueOf("0"));
					rpSbTS.setF6(Double.valueOf("0"));
					rpSbTS.setF7(Double.valueOf("0"));
					rpSbTS.setTotalSum(Double.valueOf("0"));
					tbList.add(rpSbTS);

					if (xmList.size() > 0)
					{
						PrjInputStub.getMethod().saveXmjl(xmList);// ������Ŀ
					}
					if (kmList.size() > 0)
					{
						PrjInputStub.getMethod().saveXmjl(kmList);// �����Ŀ
					}
					if (tbList.size() > 0)
					{
						PrjInputStub.getMethod().saveXmjl(tbList);// �����ϱ�
					}
				}

			}

			JOptionPane.showMessageDialog(Global.mainFrame, "����ɹ�");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��");
			e.printStackTrace();
		}
	}


	public void doUpdatedlg()
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
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "��ѡ����Ŀ");
			return;
		}
		try
		{
			String xmxhs = "";
			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						xmxhs += "'" + xmxh + "',";
					}
				}
			}
			if (xmxhs.length() > 0)
			{
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
			}

			TbPrjUpdateDlgzt tpud = new TbPrjUpdateDlgzt(xmxhs);
			Tools.centerWindow(tpud);
			setSizeAndLocation(tpud);
			tpud.setVisible(true);

			dsPrj = PrjInputStub.getMethod().getEnxmxhs(xmxhs);
			dsPrj.edit();
			dsPrj.beforeFirst();

			while (dsPrj.next())
			{
				String xmxh = dsPrj.fieldByName("xmxh").getString();
				List xmkmList = PrjInputStub.getMethod().getKmmcByXmxh(xmxh);
				String kmmc = "";
				for (int i = 0; i < xmkmList.size(); i++)
				{
					Map m = (Map) xmkmList.get(i);
					kmmc += m.get("chr_name") + ";";
				}
				if (kmmc.length() > 0)
				{
					kmmc = kmmc.substring(0, kmmc.length() - 1);
				}
				dsPrj.fieldByName("km").setValue(kmmc);
			}
			refreshPrjData();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void setPanelEnable(boolean isEnable)
	{
		treeEn.setEnabled(!isEnable);
		btnAcct.setEnabled(isEnable);
		tbPrj.setEnabled(!isEnable);
		tbPrj.getTable().setEnabled(!isEnable);
		tfPrjName.setEnabled(isEnable);
		tfPrjName.setEditable(isEnable);
		if (1 == state)
		{
			if (cbPrjCir.getValue() != null && "003".equals(cbPrjCir.getValue().toString()))
			{
				cbYearBegin.setEnabled(isEnable);
				cbYearEnd.setEnabled(isEnable);
			}
		}
		else
		{
			cbYearBegin.setEnabled(false);
			cbYearEnd.setEnabled(false);

		}
		cbPrjCir.setEnabled(isEnable);
		cbPrjSort.setEnabled(isEnable);
		fxmsx.setEnabled(isEnable);
		cbPrjState.setEnabled(isEnable);
		cbYear.setEnabled(!isEnable);
	}


	/**
	 * �Ƿ���ձ�
	 * 
	 * @param isClear
	 */
	private void setPanelValue(boolean isClear)
	{
		if (isClear)
		{
			tfPrjName.setValue("");
			cbYearBegin.setSelectedIndex(-1);
			cbYearEnd.setSelectedIndex(-1);
			cbPrjCir.setSelectedIndex(-1);
			cbPrjSort.setSelectedIndex(-1);
			fxmsx.setValue("");
			cbPrjState.setSelectedIndex(-1);
		}
	}


	/**
	 * �������ֵ
	 * 
	 * @param bm
	 *            ��Ŀ����
	 * @param mc
	 *            ����
	 * @param qn
	 *            ��ʼ���
	 * @param jn
	 *            �������
	 * @param zq
	 *            ִ������
	 * @param fl
	 *            ��Ŀ����
	 * @param sx
	 *            ��Ŀ����
	 * @param zt
	 *            ��Ŀ״̬
	 * @param km
	 *            ��Ŀ��Ϣ
	 */
	private void setPanelValue(String bm, String mc, String qn, String jn, String zq, String fl, String sx, String zt, String km)
	{
		tfPrjCode.setValue(bm);
		tfPrjName.setValue(mc);
		fxmsx.setText(sx);
		cbPrjState.setSelectedIndex(getIndexByName(cbPrjState, zt));
		cbPrjSort.setSelectedIndex(getIndexByName(cbPrjSort, fl));
		cbPrjCir.setSelectedIndex(getIndexByName(cbPrjCir, zq));
		cbYearBegin.setSelectedIndex(getIndexByName(cbYearBegin, qn));
		cbYearEnd.setSelectedIndex(getIndexByName(cbYearEnd, jn));
		tfAcct.setValue(km);
	}


	private void setSizeAndLocation(TbPrjUpdateDlgzt userInfo)
	{
		Dimension userInfoSize = new Dimension(200, 100);
		userInfo.setSize(userInfoSize);
		// userInfo.setLocation(400, 300);

	}


	/**
	 * ���ù������İ�ť״̬
	 * 
	 * @param aState
	 */
	public void setButtonState(boolean isEditState)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("�޸�".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("ɾ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
			if ("ȡ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
		}
	}


	private void setTableProp() throws Exception
	{
		tbPrj.getTable().setRowHeight(25);
		if (tbPrj != null && tbPrj.getDataSet() != null)
		{
			tbPrj.getTable().addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (tbPrj.getTable() != null)
					{

						int row = tbPrj.getTable().getSelectedRow();
						if (row < 0)
							return;
						String bmk = tbPrj.rowToBookmark(row);
						try
						{
							if (tbPrj.getDataSet() != null && !tbPrj.getDataSet().isEmpty() && tbPrj.getDataSet().gotoBookmark(bmk))
							{
								DataSet ds = tbPrj.getDataSet();
								if (!ds.isEmpty() && !ds.bof() && !ds.eof())
								{
									setPanelValue(ds.fieldByName("xmbm").getString(), ds.fieldByName("xmmc").getString(), ds.fieldByName("c1").getString(), ds.fieldByName("c2").getString(), ds
											.fieldByName("c3").getString(), ds.fieldByName("c4").getString(), ds.fieldByName("c5").getString(), ds.fieldByName("c6").getString(), ds.fieldByName(
											"accts").getString());
								}
							}
							else
							{
								setPanelValue(true);
							}
							cbYearBegin.setEnabled(false);
							cbYearEnd.setEnabled(false);
						}
						catch (Exception ee)
						{
							ErrorInfo.showErrorDialog(ee, "ˢ����Ŀ��Ϣ����������ϢΪ:" + ee.getMessage());
							return;
						}
					}
				}
			});
		}
	}


	private String getPrjStateCode()
	{
		return Common.nonNullStr(cbPrjState.getValue());
	}


	private String getPrjSortCode()
	{
		return Common.nonNullStr(this.cbPrjSort.getValue());
	}


	private int getIndexByName(FComboBox cb, String name)
	{
		int index = -1;
		if (Common.isNullStr(name))
			return index;
		for (int i = 0; i < cb.getItemsCount(); i++)
		{
			if (cb.getItemAt(i) != null)
			{
				if (name.trim().equalsIgnoreCase(Common.nonNullStr(cb.getItemAt(i)))) { return i; }
			}
		}
		return index;
	}


	public void doAuditToBack()
	{
		// TODO Auto-generated method stub

	}


	public void doBackToAudit()
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
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "��ѡ����Ŀ");
			return;
		}
		try
		{
			String xmmcs = "";

			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmbm = tbPrj.getDataSet().fieldByName("xmbm").getString();
						String xmmc = tbPrj.getDataSet().fieldByName("xmmc").getString();
						xmmcs += "[" + xmbm + "]" + xmmc + ",";
					}
				}
			}
			if (xmmcs.length() > 0)
			{
				xmmcs = xmmcs.substring(0, xmmcs.length() - 1);
			}

			if (JOptionPane.showConfirmDialog(Global.mainFrame, "ȷ��ɾ��" + xmmcs + "--��ɾ���޿��ƻ᳹��ȥ����Ŀ������Ϣ", "��ʾ", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) { return; }

			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						String xmbm = tbPrj.getDataSet().fieldByName("xmbm").getString();

						List listSql = new ArrayList();
						listSql.add("delete from rp_xmjl_km where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " ");
						listSql.add("delete from rp_xmsb where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " ");
						listSql.add("delete from rp_xmjl where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
						listSql.add("delete from rp_audit_cur where prj_code = '" + xmbm + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
						PrjInputStub.getMethod().postData(listSql);
					}
				}
			}
			// MyPfNode enID = (MyPfNode)
			// treeEn.getSelectedNode().getUserObject();
			// dsPrj = PrjInputStub.getMethod().getXmByEnId(GlobalEx.loginYear,
			// GlobalEx.getCurrRegion(), enID.getValue());
			// tbPrj.setDataSet(dsPrj);
			// tbPrj.reset();
			// setTableProp();
			refreshPrjData();
			JOptionPane.showMessageDialog(null, "����ɹ�");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "���ʧ��");
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}


	public void doExport()
	{
		// TODO Auto-generated method stub

	}


	public void doSendToAudit()
	{
		// TODO Auto-generated method stub

	}

}
