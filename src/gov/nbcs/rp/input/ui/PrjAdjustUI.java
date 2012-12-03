package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.gl.viewer.FListPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;




public class PrjAdjustUI extends RpModulePanel implements PrjActionUI
{
	protected AcctSelectDialog acctSelect = new AcctSelectDialog();
	private static final long serialVersionUID = 1L;

	CustomTree treeEn = null;

	CustomTree xmTree = null;

	CustomTree jjkmTree = null;

	FTextField tfPrjCode = null;

	FTextField tfPrjName = null;
	//
	// FTextField flcwz = null;

	FComboBox cbYearBegin = null;

	FComboBox cbYearEnd = null;

	FComboBox cbPrjCir = null;

	FComboBox cbPrjSort = null;

	FTreeAssistInput fxmsx = null;

	FComboBox cbPrjState = null;

	private boolean isoldPrj;

	// FTreeAssistInput fkmxz = null;

	/** ��Ŀѡ�� */
	FTextField tfAcct = null; // ��Ŀѡ��

	/** ���ܿ�Ŀѡ��ť */
	FButton btnAcct;

	FTextField fshgj = null; // //����λ��

	DataSet dsAcctJJ = null;

	FTextArea fSbly = null;

	FListPanel xmTable = null;

	FTreeAssistInput fgkdw = null;

	private FComboBox cbQueryType;

	// ��������Ϊ��xml�����ȡ
	String listPanelCode = "900002";

	private String xmxh = "";

	private String xmbm = "";

	private String enID = "";

	// private String enCode = "";

	public String AuditXmbm = "";

	public boolean isOK = false;

	TbPrjAuditAffix tpaa = null;

	String[] kmxzListString = null;

	FButton fb = null;

	List xmTableInfo = null;

	private CustomTable tableAll;

	private DataSet xmTableInfods;

	int[] zts;

	// private FLabel f2;

	private PrjInputDTO dto;

	FButton btnAD;
	FButton btnDD;

	String gkid;

	String gkCode;

	String gkName;

	private boolean isInit = true;

	private boolean isHis = false;

	private FSplitPane pnlInfoXmall;

	private FSplitPane pnlInfoXm;

	private boolean isInitPrj = true;
	String moduleid = Global.getModuleId();
	FComboBox cbYear = null; // ��ѡ���
	String selectYear = GlobalEx.loginYear;



	public void initize()
	{
		try
		{
			// ��ʼ������
			initData();
			// ��������
			this.add(getBasePanel());
			this.createToolBar();
			// ���ó�ʼ������Ͱ�ť״̬
			setCompEnable(false);
			setButtonState(true);
		}
		catch (Exception e)
		{
			// ����ʱʹ�� e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "�����ʼ��ʧ��");
		}
	}


	private void initData() throws Exception
	{
		dto = new PrjInputDTO();
	}



	public class XmTableMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if (dto.getCurState() != -1)
				return;
			if ((e.getButton() != MouseEvent.BUTTON2) && (e.getButton() != MouseEvent.BUTTON3))
			{
				if ((tableAll.getDataSet() != null) && !tableAll.getDataSet().isEmpty())
				{
					if (tableAll.getTable().getSelectedRow() < 0) { return; }
					try
					{
						if (!tableAll.getDataSet().gotoBookmark(tableAll.rowToBookmark(tableAll.getTable().getSelectedRow()))) { return; }
						xmxh = tableAll.getDataSet().fieldByName("XMXH").getString();
						if (!"".equals(tableAll.getDataSet().fieldByName("wfzt_dm").getString()) && tableAll.getDataSet().fieldByName("wfzt_dm").getString() != null)
							isoldPrj = true;

						dto.setXmxh(xmxh);
						refreshListData(xmxh);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}

			};
		}
	}



	public void refreshListData(String aPrjXH)
	{
		try
		{
			if (this.isInitPrj && pnlInfoXmall != null)
			{
				pnlInfoXmall.addControl(getPrjGenraAndDetailPanel());
				initOhterPanel();
				pnlInfoXmall.setDividerLocation(200);
				isInitPrj = false;
			}
			DataSet ds = null;
			if (!isHis)
				ds = PrjInputStub.getMethod().getPrjCreateInfo(" and set_year = " + Global.loginYear + " and rg_code= " + Global.getCurrRegion() + " and xmxh = '" + aPrjXH + "'");
			else
				ds = PrjInputStub.getMethod().getPrjCreateHisInfo(Global.loginYear, " and rg_code= " + Global.getCurrRegion() + " and xmxh = '" + aPrjXH + "'");
			ds.beforeFirst();
			ds.next();
			if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
			{
				String gkcode = ds.fieldByName("GKDW_CODE").getString();
				gkid = ds.fieldByName("GKDW_ID").getString();
				String gkdw = "";
				if (!Common.isNullStr(gkcode))
				{
					gkdw = ds.fieldByName("GKDW_CODE").getString() + " " + ds.fieldByName("GKDW_NAME").getString();
				}
				else
				{
					gkdw = gkCode + " " + gkName;
				}
				String qsn = ds.fieldByName("c1").getString();
				String jsn = ds.fieldByName("c2").getString();
				dto.setOneYearPrj(Common.isEqual(qsn, jsn));
				dto.setCurYearBegin(Common.isEqual(Global.loginYear, qsn));
				setPanelValue(ds.fieldByName("xmbm").getString(), ds.fieldByName("xmmc").getString(), qsn, jsn, ds.fieldByName("c3").getString(), ds.fieldByName("c4").getString(), ds
						.fieldByName("c5").getString(), ds.fieldByName("c6").getString(), ds.fieldByName("accts").getString(), gkdw);
			}
			else
			{
				setPanelValue(true);
			}
			dto.setHavSetAcct(!Common.isNullStr(Common.nonNullStr(tfAcct.getValue())));
			fSbly.setValue("");

			Map map = new HashMap();
			List sblyList = PrjInputStub.getMethod().getXmSbLyByXmxh(GlobalEx.loginYear, GlobalEx.getCurrRegion(), xmxh);
			if (sblyList.size() > 0)
			{
				map = (Map) sblyList.get(0);
				fSbly.setValue(map.get("ly") == null ? "" : map.get("ly").toString());
			}

			this.setCompEnable(false);
			// ��ʾ����б�
			if (!isHis)
				dto.setDsBody(PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, dto.getXmxh(), Global.getCurrRegion()));
			else
				dto.setDsBody(PrjInputStub.getMethod().getPrjDetailInfoHis(Global.loginYear, dto.getXmxh(), Global.getCurrRegion()));
			dto.setDsAcctSel();
			dto.refreshReportUI();
			tpaa.setAuditXmbm(ds.fieldByName("xmbm").getString());
			tpaa.setAuditXmxh(dto.getXmxh());
			tpaa.setTableData();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}


	private void setPanelValue(String bm, String mc, String qn, String jn, String zq, String fl, String sx, String zt, String km, String gkdw) throws Exception
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
		String[] kms = km.split(";");
		for (int i = 0; i < kms.length; i++)
		{
			kms[i] = kms[i].split("]")[0].substring(1);
		}
		dto.setDsAcctSel(kms);
		if (Common.isNullStr(gkdw))
		{
			if (treeEn != null)
			{
				String divCode = treeEn.getDataSet().fieldByName("div_code").getString();
				String divName = treeEn.getDataSet().fieldByName("div_name").getString();
				gkCode = divCode;
				gkName = divName;
				gkid = treeEn.getDataSet().fieldByName("en_id").getString();
				gkdw = divCode + " " + divName;
			}
			else
			{
				gkdw = gkCode + gkName;
			}
		}
		fgkdw.setText(gkdw);
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
			fgkdw.setText("");
		}
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


	/**
	 * �������ɫ
	 * 
	 * @param tableStandard
	 * @param dsContent
	 * @throws Exception
	 */
	public void setTableColor(CustomTable table, DataSet ds) throws Exception
	{
		if (table == null)
			return;
		for (int i = 0; i < table.getTable().getColumnCount(); i++)
		{
			table.getTable().getColumnModel().getColumn(i).setCellRenderer(new thisTableCellRenderer(ds, table));
		}
	}


	/**
	 * �������ɫ
	 * 
	 * @param tableStandard
	 * @param dsContent
	 * @throws Exception
	 */
	public void setTablePanelRender(FListPanel table) throws Exception
	{
		for (int i = 0; i < table.getColumnCount(); i++)
		{
			table.getColumnModel().getColumn(i).setCellRenderer(new thisPanelCellRenderer());
		}
	}



	/**
	 * 
	 * �ı�
	 * 
	 */
	public class thisPanelCellRenderer extends DefaultTableCellRenderer
	{

		private static final long serialVersionUID = -209603841706635597L;

		DecimalFormat format = new DecimalFormat("#,##0.00");



		public thisPanelCellRenderer()
		{
			super();
		}


		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			FLabel label = new FLabel();
			label.setText(Common.nonNullStr(value));
			if (row < 3)
				return label;
			else
				return c;
		}
	}

	/**
	 * 
	 * �ı�����ɫ
	 * 
	 */
	public class thisTableCellRenderer extends DefaultTableCellRenderer
	{

		private static final long serialVersionUID = -209603841706635597L;

		private DataSet ds;

		private CustomTable ct;

		DecimalFormat format = new DecimalFormat("#,##0.00");



		public thisTableCellRenderer(DataSet ds, CustomTable ct)
		{
			super();
			this.ds = ds;
			this.ct = ct;
		}


		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			try
			{
				ds.maskDataChange(true);
				String bookmark = ct.rowToBookmark(row);
				ds.gotoBookmark(bookmark);
				// if (!Common.isNullStr(sValue)) {
				if (column == 0) { return new JComboBox(); }
				if (column > 8)
				{
					String sValue = (value == null) ? "" : value.toString();
					float aa = Float.parseFloat(sValue);
					format.format(aa);
					((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
					((JLabel) c).setText(Common.nonNullStr(new Float(aa)));
				}
				// }
				ds.maskDataChange(false);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			return c;
		}
	}



	private void setTableAllProp() throws Exception
	{
		tableAll.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableAll.reset();
		tableAll.getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(2).setPreferredWidth(200);
		tableAll.getTable().getColumnModel().getColumn(3).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(4).setPreferredWidth(200);
		tableAll.getTable().getColumnModel().getColumn(5).setPreferredWidth(250);
		tableAll.getTable().setRowHeight(25);
		tableAll.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
		tableAll.getTable().repaint();
	}


	public void initOhterPanel() throws Exception
	{
		if (isInit)
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
			cbPrjCir.setRefModel(zxzqTmp);
			cbPrjCir.setValue("");

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

			cbPrjSort.setRefModel(xmflTmp);
			cbPrjSort.setValue("");

			List xmsxList = PrjInputStub.getMethod().getDmXmsx(Global.loginYear, Global.getCurrRegion());

			XMLData treeData = new XMLData();
			treeData.put("row", xmsxList);
			fxmsx.setData(treeData);
			fxmsx.setIsCheck(true);
			fxmsx.setOnlyLeafCanBeSelected(true);

			List xmztList = PrjInputStub.getMethod().getDmXmzt(Global.loginYear, Global.getCurrRegion());
			String xmztTmp = "";
			int k = 0;
			if (!GlobalEx.isFisVis())
			{
				k = 1;
			}

			for (int i = k; i < xmztList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) xmztList.get(i);
				xmztTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (xmztTmp.length() > 0)
			{
				xmztTmp = xmztTmp.substring(0, xmztTmp.length() - 1);
			}

			cbPrjState.setRefModel(xmztTmp);
			cbPrjState.setValue("");

			List enList = PrjInputStub.getMethod().getList("ele_enterprise");

			XMLData treeDataEn = new XMLData();
			treeDataEn.put("row", enList);
			fgkdw.setData(treeDataEn);
			isInit = false;
			cbYearBegin.addValueChangeListener(new ValueChangeListener()
			{
				public void valueChanged(ValueChangeEvent value)
				{
					// TODO Auto-generated method stub
					if (dto.getCurState() == -1)
						return;
					String qo = Common.nonNullStr(value.getOldValue());
					String j = Common.nonNullStr(cbYearEnd.getValue());
					boolean oldv = Common.isEqual(qo, j);
					String qn = Common.nonNullStr(value.getNewValue());
					boolean newv = Common.isEqual(qn, j);
					if (!Common.isEqual(qo, qn))
					{
						dto.setCurYearBegin(Common.isEqual(qn, Global.loginYear));
						try
						{
							dto.calData();
							dto.refreshReportUI();
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (oldv != newv)
					{
						dto.setOneYearPrj(newv);
						try
						{
							dto.calData();
							dto.refreshReportUI();
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

			cbYearEnd.addValueChangeListener(new ValueChangeListener()
			{
				public void valueChanged(ValueChangeEvent value)
				{
					// TODO Auto-generated method stub
					if (dto.getCurState() == -1)
						return;
					String q = Common.nonNullStr(cbYearBegin.getValue());
					String jo = Common.nonNullStr(value.getOldValue());
					boolean oldv = Common.isEqual(q, jo);
					String jn = Common.nonNullStr(value.getNewValue());
					boolean newv = Common.isEqual(q, jn);
					if (oldv != newv)
					{
						dto.setOneYearPrj(newv);
						try
						{
							dto.calData();
							dto.refreshReportUI();
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
		cbPrjCir.addValueChangeListener(new ValueChangeListener()
		{

			public void valueChanged(ValueChangeEvent value)
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
					cbYearBegin.setEnabled(true && !isoldPrj);
					cbYearEnd.setEnabled(true);
				}
			}
		});
		fxmsx.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent e)
			{
				String[] fxmsxStr = fxmsx.getValue() == null ? null : (String[]) fxmsx.getValue();
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
	}


	/**
	 * ��ȡ�����
	 * 
	 * @return
	 * @throws Exception
	 */
	private FPanel getLeftPanel() throws Exception
	{
		FScrollPane pnlTree = new FScrollPane();
		DataSet ds = PubInterfaceStub.getMethod().getDivDataPop(GlobalEx.loginYear);
		treeEn = new CustomTree("Ԥ�㵥λ", ds, "en_id", "code_name", "parent_id", null, "div_code");
		treeEn.reset();
		pnlTree.addControl(treeEn);
		FPanel pnlCF = new FPanel();
		RowPreferedLayout layCF = new RowPreferedLayout(1);
		pnlCF.setLayout(layCF);
		CustomTreeFinder cf = new CustomTreeFinder(treeEn);
		pnlCF.addControl(cf, new TableConstraints(1, 1, false, true));
		pnlCF.addControl(pnlTree, new TableConstraints(1, 1, true, true));
		pnlCF.setTopInset(10);
		pnlCF.setLeftInset(10);
		pnlCF.setRightInset(10);
		pnlCF.setBottomInset(10);
		return pnlCF;
	}


	private FPanel getTablePanel() throws Exception
	{
		FPanel xmTableAll = new FPanel();
		RowPreferedLayout xmTableLay = new RowPreferedLayout(8);
		xmTableAll.setLayout(xmTableLay);

		String[] columnText = new String[] { "��λ����", "��λ����", "��Ŀ����", "��Ŀ����" };
		String[] columnField = new String[] { "div_code", "div_name", "prj_code", "prj_name" };
		tableAll = new CustomTable(columnText, columnField, null, true, null);
		setTableAllProp();

		xmTableAll.addControl(getQueryType(), new TableConstraints(1, 2, false, true));
		xmTableAll.addControl(getYear(), new TableConstraints(1, 2, false, true));

		// f2 = new FLabel();
		// f2.setForeground(Color.red);
		// xmTableAll.addControl(f2, new TableConstraints(1, 3, false, true));
		FLabel lbUnit = new FLabel();
		// lbUnit.setText(" ��λ����Ԫ");
		xmTableAll.addControl(lbUnit, new TableConstraints(1, 3, false, true));
		xmTableAll.addControl(tableAll, new TableConstraints(1, 8, true, true));
		xmTableAll.setLeftInset(0);
		xmTableAll.setRightInset(0);
		xmTableAll.setBottomInset(10);
		xmTableAll.setTopInset(10);
		tableAll.getTable().addMouseListener(new XmTableMouseListener());
		return xmTableAll;
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
					// setButtonStateAn(false, false, false, false, false);
				}
				// ?else setButtonStateAn(false, false, true, false, false);
				// refreshTreeData();// ˢ���б�

				refreshprj();
			}

		});
		return cbYear;
	}


	private FSplitPane getRightPanel() throws Exception
	{
		pnlInfoXmall = new FSplitPane();
		pnlInfoXmall.setOrientation(FSplitPane.VERTICAL_SPLIT);
		pnlInfoXmall.addControl(getTablePanel());
		pnlInfoXmall.setDividerLocation(20000);
		return pnlInfoXmall;
	}


	/**
	 * ��ȡ��Ŀ������Ϣ����Ŀ��ϸ��Ϣ�Լ��������걨�������
	 * 
	 * @return
	 */
	public FSplitPane getPrjGenraAndDetailPanel()
	{
		pnlInfoXm = new FSplitPane();
		pnlInfoXm.setOrientation(FSplitPane.VERTICAL_SPLIT);
		if (!GlobalEx.isFisVis())
			pnlInfoXm.setDividerLocation(110);
		else
			pnlInfoXm.setDividerLocation(130);
		pnlInfoXm.addControl(getPrjGeneralPanel());
		pnlInfoXm.addControl(getPrjDownPanel());
		addCompListener();
		return pnlInfoXm;
	}


	private FPanel getPrjGeneralPanel()
	{
		FPanel xmxx = new FPanel();
		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		xmxx.setLayout(xmLay);

		tfPrjCode = new FTextField("��Ŀ����");
		tfPrjName = new FTextField("��Ŀ����");
		cbPrjCir = new FComboBox("ִ������");
		cbPrjSort = new FComboBox("��Ŀ����");
		fxmsx = new FTreeAssistInput("��Ŀ����");
		cbPrjState = new FComboBox("��Ŀ״̬");
		fgkdw = new FTreeAssistInput("��ڵ�λ");
		fgkdw.setProportion(0.071f);
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

		// �жϲ����Ƿ�Ϊ����Ŀѡ���˿�Ŀ����ѡ����ֻ���ڲ���ѡ��Ŀ�Ŀ��Χ��ѡ����û��ѡ�񣬵�λ�����Լ�ѡ��
		// fkmxz = new FTreeAssistInput("��Ŀѡ��");
		// fkmxz.setProportion(0.07f);
		// fkmxz.setOnlyLeafCanBeSelected(true);
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
						tfAcct.setValue(Common.nonNullStr(value.get(2)));
						// createDefaultData(value);
						dto.setDsAcctSel((String[]) value.get(0));
					}
				}
			}
		});
		// ���ö�������
		tfPrjCode.setEditable(false);
		tfPrjName.setEditable(false);
		tfAcct.setEditable(false);
		xmxx.setLeftInset(10);
		xmxx.setRightInset(10);
		xmxx.setBottomInset(10);
		xmxx.setTopInset(10);
		xmxx.addControl(tfPrjCode, new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfPrjName, new TableConstraints(1, 2, false, true));

		xmxx.addControl(fxmsx, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjSort, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjCir, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbYearBegin, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbYearEnd, new TableConstraints(1, 2, false, true));

		xmxx.addControl(cbPrjState, new TableConstraints(1, 2, false, true));
		if (GlobalEx.isFisVis())
			xmxx.addControl(fgkdw, new TableConstraints(1, 8, false, true));
		xmxx.addControl(tfAcct, new TableConstraints(1, 7, false, true));
		xmxx.addControl(btnAcct, new TableConstraints(1, 1, false, true));
		// xmxx.addControl(new FLabel(), new TableConstraints(1, 2, false,
		// true));
		xmxx.addControl(getPrjDetailButtonPanel(), new TableConstraints(1, 8, false, true));
		return xmxx;
	}


	// private void createDefaultData(List value) {
	// try {
	// DataSet dsBody = ((Report) dto.getReportUI().getReport())
	// .getBodyData();
	// if (dsBody != null && !dsBody.isEmpty()) {
	// String[] sAcctCodes = (String[]) value.get(0);
	// String[] sAcctNames = (String[]) value.get(1);
	// String[] sAcctBsIDs = (String[]) value.get(3);
	// for (int j = 0; j < sAcctCodes.length; j++) {
	// if (dsBody.locate("BS_ID", sAcctBsIDs[j]))
	// continue;
	// dsBody.append();
	// dsBody.fieldByName("xmxh").setValue(xmxh);
	// dsBody.fieldByName("rg_code").setValue(
	// Global.getCurrRegion());
	// dsBody.fieldByName("ACCT_NAME").setValue(
	// "[" + sAcctCodes[j] + "]" + sAcctNames[j]);
	// dsBody.fieldByName("BS_ID").setValue(sAcctBsIDs[j]);
	// dsBody.fieldByName("ACCT_CODE").setValue(sAcctCodes[j]);
	// dsBody.fieldByName("setYear").setValue(Global.loginYear);
	// dsBody.fieldByName("YSJC_MC").setValue("");
	// // dsBody.fieldByName("SB_TYPE").setValue(String.valueOf(i++));
	// // dsBody.fieldByName("SB_CODE").setValue(
	// // String.valueOf(400000 + i));
	// }
	// List list = new ArrayList();
	// Arrays.sort(sAcctBsIDs);
	// dsBody.beforeFirst();
	// while (dsBody.next()) {
	// String supType = dsBody.fieldByName("SB_CODE").getString();
	// if ("111".equalsIgnoreCase(supType)
	// || "222".equalsIgnoreCase(supType)
	// || "333".equalsIgnoreCase(supType))
	// continue;
	// if (Arrays.binarySearch(sAcctBsIDs, dsBody.fieldByName(
	// "BS_ID").getString()) < 0) {
	// list.add(dsBody.fieldByName("BS_ID").getString());
	// }
	// }
	// for (int k = 0; k < list.size(); k++) {
	// if (dsBody.locate("BS_ID", Common.nonNullStr(list.get(k))))
	// dsBody.delete();
	// }
	// dsBody.beforeFirst();
	// dsBody.edit();
	// int i = 1;
	// while (dsBody.next()) {
	// String supType = dsBody.fieldByName("SB_CODE").getString();
	// if ("111".equalsIgnoreCase(supType)
	// || "222".equalsIgnoreCase(supType)
	// || "333".equalsIgnoreCase(supType))
	// continue;
	// dsBody.fieldByName("SB_TYPE").setValue(String.valueOf(i++));
	// dsBody.fieldByName("SB_CODE").setValue(
	// String.valueOf(400000 + i));
	// }
	// // dsBody.applyUpdate();
	// dto.refreshReportUI();
	// }
	// } catch (Exception ee) {
	// ErrorInfo
	// .showErrorDialog(ee, "������Ŀ��ϸ��Ϣʧ�ܣ�������ϢΪ:" + ee.getMessage());
	// }
	// }

	private FTabbedPane getPrjDownPanel()
	{
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("��Ŀ�", getPrjDetailPanel());
		tabpane.addControl("��    ��", getPrjAffixPanel());
		tabpane.addControl("�걨����", getPrjRemarkPanel());
		return tabpane;
	}


	private FPanel getPrjDetailPanel()
	{
		FPanel pnlReport = new FPanel();
		RowPreferedLayout layReport = new RowPreferedLayout(1);
		pnlReport.setLayout(layReport);
		pnlReport.add(dto.getReportUI(), new TableConstraints(1, 1, true, true));
		// FSplitPane pnlRp = new FSplitPane();
		// // Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
		// // pnlRp.setDividerLocation((dimen.width / 4 * 3));
		// pnlRp.setDividerLocation(1000);
		// pnlRp.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		// pnlRp.addControl(pnlReport);
		// pnlRp.addControl(pnlRpButton);
		return pnlReport;
	}


	private FPanel getPrjDetailButtonPanel()
	{
		FPanel pnlRpButton = new FPanel();
		RowPreferedLayout layRpButton = new RowPreferedLayout(10);
		pnlRpButton.setLayout(layRpButton);
		btnAD = new FButton("ad", "������ϸ");
		btnDD = new FButton("dd", "ɾ����ϸ");
		pnlRpButton.addControl(btnAD, new TableConstraints(1, 2, false, true));
		pnlRpButton.addControl(btnDD, new TableConstraints(1, 2, false, true));
		FLabel dwwy = new FLabel();
		dwwy.setText("                           ��λ����Ԫ");
		pnlRpButton.addControl(dwwy, new TableConstraints(1, 6, true, true));
		btnAD.setEnabled(false);
		btnDD.setEnabled(false);
		return pnlRpButton;
	}


	public void setPrjInputDTO(PrjInputDTO dto)
	{
		this.dto = dto;
	}


	private FPanel getPrjAffixPanel()
	{
		PrjAuditDTO pd = new PrjAuditDTO();
		PrjAuditAction pa = new PrjAuditAction(pd);
		tpaa = new TbPrjAuditAffix(pa);
		tpaa.setAuditXmbm(AuditXmbm);
		return tpaa.setBottomPanel();
	}


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
		fSbly.getEditor().setBackground(Color.white);
		ply.addControl(fSbly, new TableConstraints(1, 1, true, true));
		return ply;
	}


	/**
	 * ��ȡ�����
	 * 
	 * @return
	 * @throws Exception
	 */
	public FSplitPane getBasePanel() throws Exception
	{
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(200);
		pnlInfo.addControl(getLeftPanel());
		pnlInfo.addControl(getRightPanel());
		addTreeListener();
		return pnlInfo;
	}


	private void addTreeListener()
	{
		treeEn.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					if ((treeEn != null) && (treeEn.getSelectedNode() != null) && (treeEn.getSelectedNode() != null))
					{
						MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode().getUserObject();
						if ((enPer == null) || "".equals(enPer.getValue())) { return; }
						if (dto.getCurState() != -1)
							return;
						try
						{
							// if (zts == null) {
							// zts = PrjInputStub.getMethod().getxmztNum(
							// GlobalEx.user_code, false);
							// f2.setTitle(" ���Ŀ��" + zts[0] + " ������Ŀ:"
							// + zts[1] + " �˻���Ŀ:" + zts[2] + " ȫ����Ŀ:"
							// + zts[3]);
							// }
							// if (enPer.getIsLeaf()) {
							// enID = enPer.getValue();
							refreshprj();

							// }
						}
						catch (Exception e1)
						{
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}


	private void addCompListener()
	{
		// fkmxz.addValueChangeListener(new ValueChangeListener() {
		// public void valueChanged(ValueChangeEvent arg0) {
		// if (arg0 != null) {
		// String[] oldValue = (String[]) arg0.getOldValue();
		// String[] newValue = (String[]) arg0.getNewValue();
		// if (((oldValue != null) && (newValue != null))
		// && (oldValue.length == newValue.length)) {
		// for (int i = 0; i < oldValue.length; i++) {
		// if (oldValue[i].equals(newValue[i])) {
		// return;
		// }
		// }
		// }
		// String args[] = (String[]) arg0.getNewValue();
		// kmxzListString = args;
		// }
		// }
		// });
		btnAD.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO ������ϸ
				if (dto.getCurState() == -1)
					return;
				try
				{
					DataSet ds = ((Report) dto.getReportUI().getReport()).getBodyData();
					ds.maskDataChange(true);
					ds.afterLast();
					ds.prior();
					String sbType = ds.fieldByName("SB_TYPE").getString();
					String sbCode = ds.fieldByName("SB_CODE").getString();
					ds.append();
					ds.fieldByName("SB_TYPE").setValue(String.valueOf((Integer.parseInt(sbType) + 1)));
					ds.fieldByName("SB_CODE").setValue(String.valueOf((Integer.parseInt(sbCode) + 1)));
					ds.fieldByName("ACCT_NAME").setValue("");
					ds.fieldByName("ACCT_NAME_JJ").setValue("");
					dto.getReportUI().repaint();
					ds.maskDataChange(false);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});

		btnDD.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO ɾ����ϸ
				if (dto.getCurState() == -1)
					return;
				try
				{
					Cell cell = (Cell) ((Report) dto.getReportUI().getReport()).getSelectedCell();
					if (((Report) dto.getReportUI().getReport()).getBodyData().gotoBookmark(cell.getBookmark()))
					{
						String supType = ((Report) dto.getReportUI().getReport()).getBodyData().fieldByName("SB_CODE").getString();
						if ("111".equalsIgnoreCase(supType) || "222".equalsIgnoreCase(supType) || "333".equalsIgnoreCase(supType))
							return;
						((Report) dto.getReportUI().getReport()).getBodyData().delete();
						dto.getReportUI().repaint();
					}
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}


	/**
	 * �������
	 */
	public void doAdd()
	{
		try
		{
			DataSet ds = getAuditNotPassData();
			DataSet dsEn = treeEn.getDataSet();
			if (ds != null && !ds.isEmpty())
			{
				String divInfo = "[" + dsEn.fieldByName("div_code").getString() + "]" + dsEn.fieldByName("div_name").getString();
				FDialog dg = new AuditDialog(divInfo, ds);
				Tools.centerWindow(dg);
				dg.setVisible(true);

			}
			else
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "�������ͨ��");
			}
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	private InfoPackage checkData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try
		{
			ReportUI reportUI = dto.getReportUI();
			String st = "";
			String sl = "";
			String sn = "";

			for (int i = 6; i < 13; i++)
			{
				if (9 == i)
					continue;
				// �ϼ��м���
				st = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 2).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 2).getValue());
				sl = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 3).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 3).getValue());
				sn = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 4).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 4).getValue());
				if (Double.parseDouble(st) < (Double.parseDouble(sl) + Double.parseDouble(sn)) && cbYearEnd.getSelectedIndex() != 0)
				{
					info.setSuccess(false);
					switch (i)
					{
						// case 4:
						// info.setsMessage("�ʽ���Դ�����ϼơ��ġ�����Ԥ���������ϡ����갲������������Ԥ��");
						// break;
						case 6:
							info.setsMessage("�ʽ���Դ����һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 7:
							info.setsMessage("�ʽ���Դ��������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 8:
							info.setsMessage("�ʽ���Դ�����������ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;

						case 10:
							info.setsMessage("�ʽ���Դ���ϼ� ��һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 11:
							info.setsMessage("�ʽ���Դ���ϼ� ������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 12:
							info.setsMessage("�ʽ���Դ���ϼ� ���������ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;

					}
					return info;
				}
				if (Double.parseDouble(st) != (Double.parseDouble(sl) + Double.parseDouble(sn)) && cbYearEnd.getSelectedIndex() == 0)
				{
					info.setSuccess(false);
					switch (i)
					{
						// case 4:
						// info.setsMessage("�ʽ���Դ�����ϼơ��ġ�����Ԥ���������ϡ����갲������������Ԥ��");
						// break;
						case 6:
							info.setsMessage("�������Ϊ���� �ʽ���Դ����һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 7:
							info.setsMessage("�������Ϊ���� �ʽ���Դ��������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 8:
							info.setsMessage("�������Ϊ���� �ʽ���Դ�����������ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;

						case 10:
							info.setsMessage("�������Ϊ���� �ϼ� �ʽ���Դ����һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 11:
							info.setsMessage("�������Ϊ���� �ϼ� �ʽ���Դ��������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 12:
							info.setsMessage("�������Ϊ���� �ϼ� �ʽ���Դ�����������ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;

					}
					return info;
				}
			}
			for (int i = 1; i < 5; i++)
			{
				// �ӵڶ��п�ʼ
				for (int j = 5; j < reportUI.getReport().getRowCount(); j++)
				{
					if (Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, j).getValue())))
					{
						info.setSuccess(false);
						String value = "��" + (j + 1) + "�У���" + i + "��";
						if (i == 1)
							value += "  Ԥ�㼶�β���Ϊ��";
						else if (i == 2)
							value += "  ���ܿ�Ŀ����Ϊ��";
						else if (i == 3)
							value = "  ���ÿ�Ŀ����Ϊ��";
						else if (i == 4)
							value = "  �ʽ�Ϊ0";
						else
							value = "Ԥ�㼶�Ρ����ܿ�Ŀ�����ÿ�Ŀ ������Ϊ�ա�";
						info.setsMessage(value);
						return info;
					}
				}
			}
			DataSet ds = (((Report) dto.getReportUI().getReport()).getBodyData());
			int k = 1;
			String type = "";
			ds.beforeFirst();
			while (ds.next())
			{
				type = ds.fieldByName("SB_CODE").getString();
				if ("111".equals(type) || "222".equals(type) || "333".equals(type))
					continue;
				ds.fieldByName("SB_TYPE").setValue(String.valueOf(k++));
				ds.fieldByName("SB_CODE").setValue("40000" + k);
			}
		}
		catch (Exception e1)
		{
			info.setSuccess(false);
			info.setsMessage("����ʧ�ܣ�������ϢΪ" + e1.getMessage());
			return info;
		}
		return info;
	}


	private DataSet getAuditNotPassData() throws Exception
	{
		DataSet dsEn = treeEn.getDataSet();
		DataSet ds = null;
		if (dsEn != null && !dsEn.isEmpty() && !dsEn.eof() && !dsEn.bof() && treeEn.getSelectedNode().isLeaf())
		{
			ds = PrjInputStub.getMethod().doAuditDivData(dsEn.fieldByName("div_code").getString(), Global.loginYear, Global.getCurrRegion());
		}
		return ds;
	}



	private class AuditDialog extends FDialog
	{

		private static final long serialVersionUID = 1L;

		private DataSet dsA;

		private String divInfo;



		public AuditDialog(String divInfo, DataSet ds)
		{
			super(Global.mainFrame);
			this.dsA = ds;
			this.divInfo = divInfo;
			this.setSize(600, 400);
			this.getContentPane().add(getBasePanelD());
			this.dispose();
			this.setTitle("������˽��");
			this.setModal(true);
		}


		private FPanel getBasePanelD()
		{
			FPanel pnl = new FPanel();
			RowPreferedLayout lay = new RowPreferedLayout(1);
			pnl.setLayout(lay);
			FLabel label = new FLabel();
			label.setText("��λ��" + divInfo + "  ������˲�ͨ�����������");
			label.setForeground(Color.red);
			CustomTable table = null;
			try
			{
				table = new CustomTable(new String[] { "����", "��λ����", "��λ������", "��ʾ��Ϣ" }, new String[] { "code", "svalue", "kvalue", "remark" }, dsA, false, null);
				table.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.reset();
				table.getTable().getColumnModel().getColumn(0).setPreferredWidth(100);
				table.getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
				table.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
				table.getTable().getColumnModel().getColumn(3).setPreferredWidth(270);
				table.getTable().setRowHeight(25);
				table.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
				table.getTable().getTableHeader().setPreferredSize(new Dimension(30, 30));
				table.getTable().repaint();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FToolBar toolbar = new FToolBar();
			FPanel pnlTop = new FPanel();
			FButton btnOK = new FButton("", "�ر�");
			btnOK.setIcon("images/rp/close.gif");
			btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);
			toolbar.addControl(btnOK);
			RowPreferedLayout layTop = new RowPreferedLayout(1);
			pnlTop.setLayout(layTop);
			layTop.setRowHeight(50);
			pnlTop.addControl(toolbar, new TableConstraints(1, 1, true, true));
			pnl.addControl(pnlTop, new TableConstraints(2, 1, false, true));
			pnl.addControl(label, new TableConstraints(1, 1, false, true));
			pnl.addControl(table, new TableConstraints(1, 1, true, true));
			pnl.setLeftInset(10);
			pnl.setRightInset(10);
			pnl.setBottomInset(10);
			btnOK.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					setVisible(false);
				}
			});
			return pnl;
		}
	}



	public void doSave()
	{
		cbQueryType.setEnabled(true);
		this.tfAcct.setFocus();
		isOK = false;
		if (cbYearBegin.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ʼ��Ȳ���Ϊ��");
			return;
		}
		if (cbYearEnd.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "������Ȳ���Ϊ��");
			return;
		}
		if ((cbPrjCir.getValue() == null) || cbPrjCir.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "ִ�����ڲ���Ϊ��");
			return;
		}
		if ((cbPrjSort.getValue() == null) || cbPrjSort.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ���಻��Ϊ��");
			return;
		}
		// if (fxmsx.getValue() == null) {
		// JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ���Բ���Ϊ��");
		// return;
		// }
		if ((cbPrjState.getValue() == null) || cbPrjState.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ״̬����Ϊ��");
			return;
		}
		if (Common.isNullStr(fgkdw.getText()))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ڵ�λ����Ϊ��");
			return;
		}
		if (Common.isNullStr(Common.nonNullStr(tfAcct.getValue())))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "���ܿ�Ŀ����Ϊ��");
			return;
		}
		if (Integer.valueOf((String) cbYearBegin.getValue()).intValue() == Integer.valueOf((String) cbYearEnd.getValue()).intValue() && cbPrjCir.getSelectedIndex() == 2)
		{
			new MessageBox("��������Ŀ��ʼ��Ȳ��ܵ��ڽ�����ȣ�����������!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}

		dto.calData();

		try
		{
			DataSet ds = ((Report) dto.getReportUI().getReport()).getBodyData();
			if (ds == null || ds.isEmpty())
				return;

			ds.gotoBookmark("3");
			String total = ds.fieldByName("total_sum").getString();

			// ��ȡ��Ŀ��ź͵�λ���
			if ((tableAll.getDataSet() == null) || tableAll.getDataSet().isEmpty())
				return;
			if (tableAll.getTable().getSelectedRow() < 0)
				return;
			if (!tableAll.getDataSet().gotoBookmark(tableAll.rowToBookmark(tableAll.getTable().getSelectedRow())))
				return;
			String divCode = tableAll.getDataSet().fieldByName("div_code").getString();
			String prjCode = tableAll.getDataSet().fieldByName("prj_code").getString();

			// �ж��Ƿ����ƿ��ƽ��
			String sumSql = "select sum(b.total_sum) as total_sum from rp_xmjl a inner join rp_xmsb b on a.xmxh = b.xmxh where a.xmbm != '" + prjCode + "' and a.xmbm like '" + divCode
					+ "%' and a.set_year = '" + Global.loginYear + "' and b.sb_code = '333'";
			String limitSql = "select a.f1,a.enable from rp_dwkzs a where a.en_code = '" + divCode + "' and a.set_year = '" + Global.loginYear + "'";
			List sumList = QueryStub.getClientQueryTool().findBySql(sumSql);

			List limitList = QueryStub.getClientQueryTool().findBySql(limitSql);
			double sum = 0;
			if (sumList != null && !sumList.isEmpty())
			{
				if (StringUtils.isNotEmpty(((Map) sumList.get(0)).get("total_sum").toString()))
					sum = Double.parseDouble(((Map) sumList.get(0)).get("total_sum").toString()) + Double.parseDouble(total);
				else
					sum = Double.parseDouble(total);
			}
			else
			{
				sum = Double.parseDouble(total);
			}

			// if (limitList == null || limitList.isEmpty()) {
			// if (!treeEn.getSelectedNode().isRoot()) {
			// String[] parentCode = treeEn.getSelectedNode().getParent()
			// .toString().split(" ");
			// if (parentCode != null && parentCode.length > 1) {
			// sumSql = "select sum(b.total_sum) as total_sum from rp_xmjl a
			// inner join rp_xmsb b on a.xmxh = b.xmxh where a.xmbm != '"
			// + prjCode
			// + "' and a.xmbm like '"
			// + parentCode[0]
			// + "%' and a.set_year = '"
			// + Global.loginYear + "' and b.sb_code = '333'";
			// limitSql = "select a.f1,a.enable from rp_dwkzs a where a.en_code
			// = '"
			// + parentCode[0]
			// + "' and a.set_year = '"
			// + Global.loginYear + "'";
			// sumList = QueryStub.getClientQueryTool().findBySql(
			// sumSql);
			// limitList = QueryStub.getClientQueryTool().findBySql(
			// limitSql);
			// }
			//
			// sum = 0;
			// if (sumList != null && !sumList.isEmpty()) {
			// if (StringUtils.isNotEmpty(((Map) sumList.get(0)).get(
			// "total_sum").toString()))
			// sum = Double.parseDouble(((Map) sumList.get(0))
			// .get("total_sum").toString())
			// + Double.parseDouble(total);
			// } else {
			// sum = Double.parseDouble(total);
			// }
			// }
			// }

			if (limitList != null && !limitList.isEmpty())
			{
				Map limit = (Map) limitList.get(0);
				String strF1 = limit.get("f1").toString();
				String strEnable = limit.get("enable").toString();
				if (StringUtils.isNotEmpty(strEnable) && strEnable.equalsIgnoreCase("1"))
				{
					double f1 = 0;
					if (StringUtils.isNotEmpty(strF1))
					{
						f1 = Double.parseDouble(strF1);
					}
					if (sum > f1)
					{
						JOptionPane.showMessageDialog(Global.mainFrame, "�ϼƽ���ѳ�����������" + sum + "Ԫ����������ϼƽ�");
						return;
					}
				}
			}

		}
		catch (Exception e1)
		{
			new MessageBox(e1.getMessage(), MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}

		InfoPackage info = checkData();
		if (!info.getSuccess())
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ�ܣ�������ϢΪ��\n" + info.getsMessage());
			return;
		}
		try
		{
			DataSet xmData = PrjInputStub.getMethod().getXmDataByXmxh(dto.getXmxh());
			if (!xmData.locate("xmxh", dto.getXmxh())) { return; }
			if (Common.isNullStr(fgkdw.getText()))
			{
				new MessageBox("��ڵ�λ����Ϊ��", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
				return;
			}
			xmData.edit();
			xmData.locate("xmxh", xmxh);
			xmData.fieldByName("c1").setValue(cbYearBegin.getValue());
			xmData.fieldByName("c2").setValue(cbYearEnd.getValue());
			xmData.fieldByName("c3").setValue(cbPrjCir.getValue());
			xmData.fieldByName("c4").setValue(cbPrjSort.getValue());
			// String xmsx = "";
			String xmsx = this.fxmsx.getText();
			if (Common.isNullStr(xmsx))
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ����Ϊ�գ���ѡ��");
				return;
			}
			xmData.fieldByName("c5").setValue(xmsx);

			// if (!Common.isNullStr(fxmsx.getText()))
			// xmData.fieldByName("c5").setValue(xmsx);
			xmData.fieldByName("c6").setValue(cbPrjState.getValue());
			xmData.fieldByName("gkdw_code").setValue(fgkdw.getText().substring(0, fgkdw.getText().indexOf(" ")));
			xmData.fieldByName("gkdw_name").setValue(fgkdw.getText().substring((fgkdw.getText().indexOf(" ") + 1), fgkdw.getText().length()));
			xmData.fieldByName("gkdw_id").setValue(
					DBSqlExec.client().getStringValue(
							"select chr_name From ele_enterprise where chr_code = '" + fgkdw.getText().substring(0, fgkdw.getText().indexOf(" ")) + "'" + " and set_year = " + Global.loginYear
									+ " and rg_code = " + Global.getCurrRegion()));
			xmData.fieldByName("accts").setValue(tfAcct.getValue());
			xmData.fieldByName("xgr_dm").setValue(GlobalEx.getUserId());
			xmData.fieldByName("xgrq").setValue(new Date());
			xmData.fieldByName("set_year").setValue(Global.loginYear);

			// ��Ŀ
			List listArr = new ArrayList();
			String[] kmxzStr = Common.nonNullStr(tfAcct.getValue()).split(";");
			listArr.add("delete from rp_xmjl_km where xmxh='" + dto.getXmxh() + "' and set_year = " + Global.loginYear);
			for (int i = 0; i < kmxzStr.length; i++)
			{
				if (Common.isNullStr(kmxzStr[i]))
					continue;
				String kmdm = kmxzStr[i].substring(1, kmxzStr[i].indexOf("]"));
				String bs_id = PrjInputStub.getMethod().getBsIDByCode(kmdm);
				listArr.add("insert into rp_xmjl_km (set_year, xhid, xmxh, kmdm) values(" + Global.loginYear + ",newid,'" + dto.getXmxh() + "','" + bs_id + "')");
			}

			// ��Ŀ������Ϣ
			info = PrjInputStub.getMethod().editXmjl(xmData);
			if (!info.getSuccess())
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ�ܣ�������ϢΪ��" + info.getsMessage());
				return;
			}
			// ��Ŀ�걨
			List sqlList = new ArrayList();
			sqlList.add("delete from rp_xmsb where xmxh = '" + xmxh + "'" + " and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
			PrjInputStub.getMethod().postData(sqlList);// ɾ��ԭ���Ŀ�Ŀ
			QueryStub.getClientQueryTool().executeBatch(listArr);
			if (treeEn != null)
				enID = treeEn.getDataSet().fieldByName("en_id").getString();
			InfoPackage xmmxInfo = PrjInputStub.getMethod().savePrjDetailInfo(enID, dto.getXmxh(), ((Report) dto.getReportUI().getReport()).getBodyData(), Global.loginYear, Global.getCurrRegion(),
					Global.getUserId(), Tools.getCurrDate());

			// �����걨����
			List sqlSbly = new ArrayList();
			sqlSbly.add("delete from rp_xmsb_ly where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
			sqlSbly.add("insert into rp_xmsb_ly (set_year,rg_code, ly_id, xmxh, ly) values(" + Global.loginYear + "," + Global.getCurrRegion() + ",newid,'" + dto.getXmxh() + "','"
					+ (fSbly.getValue() == null ? "" : fSbly.getValue().toString()) + "')");
			QueryStub.getClientQueryTool().executeBatch(sqlSbly);
			PrjInputStub.getMethod().savePrjInfoHistory(new String[] { xmxh }, dto.getBatchNoByPrjCode(xmxh), dto.getAuditStepByPrjCode(xmxh));
			if (info.getSuccess() && xmmxInfo.getSuccess())
			{

				xmxxForAudit(false);
				if (tableAll != null)
					tableAll.getTable().setEnabled(true);
				if (treeEn != null)
				{
					treeEn.setEnabled(true);
					treeEn.setEnabled(true);
				}
				dto.setCurState(-1);
				JOptionPane.showMessageDialog(Global.mainFrame, "����ɹ�");
			}
			else
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��");
				return;
			}
			// dto.modData(dto.getReportUI(), (Report) dto.getReportUI()
			// .getReport(), ((Report) dto.getReportUI().getReport())
			// .getBodyData(), (Cell) ((Report) dto.getReportUI()
			// .getReport()).getSelectedCell());
			dto.refreshReportUI();
			isOK = true;
			setButtonState(true);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��");
			e.printStackTrace();
		}

	}


	/**
	 * �޸�
	 */
	public void doModify()
	{
		if (tableAll.getTable().getSelectedRow() < 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ�����޸�");
			return;
		}
		setButtonState(false);
		tableAll.getTable().setEnabled(false);
		treeEn.setEnabled(false);
		isOK = true;
		xmxxForAudit(true);
		cbQueryType.setEnabled(false);
		if (isoldPrj)
		{
			dto.setCurYearBegin(isoldPrj);

		}
		if (dto.getDsBody() != null && !dto.getDsBody().isEmpty())
		{
			// ����reportUI�ɱ༭
			try
			{
				dto.setCurState(1);
				dto.refreshReportUI();
				dto.getDsBody().edit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}


	public void doModifyForAudit()
	{
		dto.setCurState(1);
		try
		{
			dto.refreshReportUI();
			dto.getDsBody().edit();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmxxForAudit(true);
		setPanelElementState(true);
	}


	public void xmxxForAudit(boolean xxedit)
	{
		// if (!GlobalEx.isFisVis() && xxedit) {
		// if ("".equals(cbYearBegin.getValue())
		// || (cbYearBegin.getValue() == null)) {
		// cbYearBegin.setEnabled(xxedit);
		// }
		// if ("".equals(cbYearEnd.getValue())
		// || (cbYearEnd.getValue() == null)) {
		// cbYearEnd.setEnabled(xxedit);
		// }
		// if ("".equals(cbPrjCir.getValue()) || (cbPrjCir.getValue() == null))
		// {
		// cbPrjCir.setEnabled(xxedit);
		// }
		// if ("".equals(cbPrjSort.getValue())
		// || (cbPrjSort.getValue() == null)) {
		// cbPrjSort.setEnabled(xxedit);
		// }
		// if ("".equals(fxmsx.getValue()) || (fxmsx.getValue() == null)) {
		// fxmsx.setEnabled(xxedit);
		// }
		// if ("".equals(cbPrjState.getValue())
		// || (cbPrjState.getValue() == null)) {
		// cbPrjState.setEnabled(xxedit);
		// }
		// if (Common.isNullStr(Common.nonNullStr(tfAcct.getValue()))) {
		// btnAcct.setEnabled(xxedit);
		// }
		// if ("".equals(fgkdw.getValue()) || (fgkdw.getValue() == null)) {
		// fgkdw.setEnabled(xxedit);
		// }
		// btnAD.setEnabled(xxedit);
		// btnDD.setEnabled(xxedit);
		// tpaa.setButtonState(xxedit);
		// } else {
		setCompEnable(xxedit);
		cbPrjState.setEnabled(true);
		// }
	}


	public void setCompEnable(boolean xxedit)
	{
		if (this.pnlInfoXm != null)
		{
			if (dto.getCurState() != -1)
			{
				if ("003".equals(cbPrjCir.getValue().toString()))
				{
					cbYearBegin.setEnabled(xxedit && !isoldPrj);
					cbYearEnd.setEnabled(xxedit);
				}
			}
			else
			{
				cbYearBegin.setEnabled(false);
				cbYearEnd.setEnabled(false);
			}
			cbPrjCir.setEnabled(xxedit && !isoldPrj);
			cbPrjSort.setEnabled(xxedit);
			fxmsx.setEnabled(xxedit);
			cbPrjState.setEnabled(xxedit);
			btnAcct.setEnabled(xxedit);
			btnAcct.setEnabled(xxedit);
			fgkdw.setEnabled(xxedit);
			btnAD.setEnabled(xxedit);
			btnDD.setEnabled(xxedit);
			tpaa.setButtonState(xxedit);
		}
	}


	/**
	 * ȡ��
	 */
	public void doCancel()
	{
		try
		{
			setButtonState(true);
			dto.setCurState(-1);
			// if (tableAll.getDataSet()
			// .gotoBookmark(
			// tableAll.rowToBookmark(tableAll.getTable()
			// .getSelectedRow()))) {
			// refreshListData(tableAll.getDataSet().fieldByName("xmxh")
			// .getString());
			// }
			// ((Report) dto.getReportUI().getReport()).getBodyData().cancel();
			// ((Report) dto.getReportUI().getReport()).getBodyData()
			// .applyUpdate();
			// DataSet ds = ((Report)
			// dto.getReportUI().getReport()).getBodyData();
			// ds.maskDataChange(true);
			// String bmk = ds.toogleBookmark();
			// Cell cell = (Cell) ((Report) dto.getReportUI().getReport())
			// .getSelectedCell();
			// if (ds.gotoBookmark(bmk)) {
			// String supType = ds.fieldByName("SB_CODE").getString();
			// if (!"111".equalsIgnoreCase(supType)
			// && !"222".equalsIgnoreCase(supType)
			// && !"333".equalsIgnoreCase(supType)) {
			// if ("ACCT_NAME_JJ".equalsIgnoreCase(cell.getFieldName())
			// || "ACCT_NAME".equalsIgnoreCase(cell.getFieldName())) {
			// ((TFixTextWithButtonCellEditor) (((Report) dto
			// .getReportUI().getReport()).getCellProperty()
			// .getEditor(bmk, cell.getFieldName())))
			// .stopCellEditing();
			// }
			// }
			// }
			// ds.maskDataChange(false);
			// if (
			// (((Report)dto.getReportUI().getReport()).getCellProperty().getEditor(bmk,
			// cell.getFieldName()))){
			//				
			// }
			refreshListData(tableAll.getDataSet().fieldByName("xmxh").getString());
			dto.refreshReportUI();

			dto.setCurState(-1);
			// Cell cell = (Cell) ((Report) dto.getReportUI().getReport())
			// .getSelectedCell();
			// dto.getReportUI().getGrid().getCellEditor(cell.getColumn(),
			// cell.getRow()).cancelCellEditing();
			// ((Report) dto.getReportUI().getReport()).setBodyData(PrjInputStub
			// .getMethod().getPrjDetailInfoHis(Global.loginYear,
			// dto.getXmxh(), Global.getCurrRegion()));

			((Report) dto.getReportUI().getReport()).setBodyData(PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, dto.getXmxh(), Global.getCurrRegion()));
			((Report) dto.getReportUI().getReport()).refreshBody();
			dto.getReportUI().repaint();
		}
		catch (Exception ee)
		{}
		finally
		{
			cbQueryType.setEnabled(true);
			xmxxForAudit(false);
			isOK = false;
			if (tableAll != null)
				tableAll.getTable().setEnabled(true);
			if (treeEn != null)
				treeEn.setEnabled(true);
		}
	}


	/**
	 * ���ý���Ԫ�ؿɱ༭״̬
	 * 
	 * @param isEditable
	 */
	private void setPanelElementState(boolean isEditable)
	{
		if (GlobalEx.isFisVis())
		{
			// tfPrjCode.setEditable(isEditable);
			// tfPrjName.setEditable(isEditable);
			// cbYearBegin.setEditable(isEditable);
			// cbYearEnd.setEditable(isEditable);
			// cbPrjCir.setEditable(isEditable);
			// cbPrjSort.setEditable(isEditable);
			// fxmsx.setEditable(isEditable);
			// cbPrjState.setEditable(isEditable);
			// fkmxz.setEditable(isEditable);
			// fgkdw.setEditable(isEditable);
			// bottomPanel.setEnabled(isEditable);
		}
	}


	public void doExport()
	{

	}


	public void doSendToAudit()
	{
		InfoPackage info = checkData();
		if (!info.getSuccess())
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ�ܣ�������ϢΪ��\n" + info.getsMessage());
			return;
		}
		try
		{
			DataSet xmData = PrjInputStub.getMethod().getXmDataByXmxh(dto.getXmxh());
			if (!xmData.locate("xmxh", dto.getXmxh())) { return; }

			xmData.fieldByName("accts").setValue(tfAcct.getValue());
			xmData.fieldByName("xgr_dm").setValue(GlobalEx.getUserId());
			xmData.fieldByName("xgrq").setValue(new Date());

			// ��Ŀ

			// ��Ŀ������Ϣ
			info = PrjInputStub.getMethod().editXmjl(xmData);
			if (!info.getSuccess())
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ�ܣ�������ϢΪ��" + info.getsMessage());
				return;
			}

			DataSet ds = getAuditNotPassData();
			DataSet dsEn = treeEn.getDataSet();
			if (ds != null && !ds.isEmpty())
			{
				String divInfo = "[" + dsEn.fieldByName("div_code").getString() + "]" + dsEn.fieldByName("div_name").getString();
				FDialog dg = new AuditDialog(divInfo, ds);
				Tools.centerWindow(dg);
				dg.setVisible(true);
				return;
			}
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (GlobalEx.isFisVis())
		{
			// ֻ�е�λ�û����ܲ���
			JOptionPane.showMessageDialog(Global.mainFrame, "ֻ�е�λ�û����������ʸ�");
			return;
		}
		int[] rows = tableAll.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		String[] prjcodes = new String[rows.length];
		String[] prjId = new String[rows.length];

		try
		{
			List xmxhs = new ArrayList();
			for (int i = 0; i < rows.length; i++)
			{
				String bmk = tableAll.rowToBookmark(rows[i]);
				if (tableAll.getDataSet().gotoBookmark(bmk))
				{
					prjcodes[i] = tableAll.getDataSet().fieldByName("prj_code").getString();
					prjId[i] = tableAll.getDataSet().fieldByName("xmxh").getString();
					Map values = new HashMap();
					values.put("xmxh", tableAll.getDataSet().fieldByName("xmxh").getString());
					values.put("enid", tableAll.getDataSet().fieldByName("en_id").getString());
					xmxhs.add(values);
				}
				else
					continue;
				if (DBSqlExec.client().getRecordCount("rp_xmsb", "xmxh='" + prjId[i] + "'") <= 0)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "��ȷ����Ŀ" + prjcodes[i] + "�Ƿ��Ѿ�¼����Ŀ������Ϣ");
					return;
				}
			}
			PubInterfaceStub.getMethod().saveTZhistory(prjId);
			// ��λ�������
			PubInterfaceStub.getMethod().sendTZAuditInfoByDiv(xmxhs, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid);

			JOptionPane.showMessageDialog(Global.mainFrame, "����ɹ�");
			refreshprj();
			// if (!GlobalEx.isFisVis()) {
			// this.zts = PrjInputStub.getMethod().getxmztNum(
			// GlobalEx.user_code, true);
			// f2.setTitle(" ���Ŀ��" + zts[0] + " ������Ŀ:" + zts[1] + " �˻���Ŀ:"
			// + zts[2] + " ȫ����Ŀ:" + zts[3]);
			// }

		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "����ʧ��");

		}
	}


	public void doBackToAudit()
	{
		if (GlobalEx.isFisVis())
		{
			// ֻ�е�λ�û����ܲ���
			JOptionPane.showMessageDialog(Global.mainFrame, "ֻ�е�λ�û����ó�������");
			return;
		}
		int[] rows = tableAll.getTable().getSelectedRows();
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
				String bmk = tableAll.rowToBookmark(rows[i]);

				if (tableAll.getDataSet().gotoBookmark(bmk))
					prjcodes[i] = tableAll.getDataSet().fieldByName("xmxh").getString();

			}
			// ��λ�������
			PubInterfaceStub.getMethod().BackAuditTZInfoByDiv(1, prjcodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid);
			// }
			JOptionPane.showMessageDialog(null, "�����ɹ�");
			refreshprj();
			// if (!GlobalEx.isFisVis()) {
			// this.zts = PrjInputStub.getMethod().getxmztNum(
			// GlobalEx.user_code, true);
			// f2.setTitle(" ���Ŀ��" + zts[0] + " ������Ŀ:" + zts[1] + " �˻���Ŀ:"
			// + zts[2] + " ȫ����Ŀ:" + zts[3]);
			// }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "����ʧ��");

		}
	}


	private void setSizeAndLocation(TbPrjUpdateDlg userInfo)
	{
		Dimension userInfoSize = new Dimension(800, 450);
		userInfo.setSize(userInfoSize);
		userInfo.setLocation(320, 90);

	}


	public void doImpProject()
	{
		TbPrjUpdateDlg tpud = new TbPrjUpdateDlg(enID);
		setSizeAndLocation(tpud);
		tpud.setVisible(true);
	}


	public void doModifyZt()
	{

	}


	public boolean isCanfix(String type)
	{

		try
		{
			if ("type".equals(PrjInputStub.getMethod().getlcwz(xmbm))) { return true; }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;

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
				refreshprj();;
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


	public void refreshprj()
	{
		// TODO ��ѯ
		setButtonState(true);
		if (treeEn.getSelectedNode() != null)
		{
			MyPfNode node = (MyPfNode) treeEn.getSelectedNode().getUserObject();
			if (node == null)
				return;
			StringBuffer filter = new StringBuffer();
			filter.append(" and a.set_year = " + Global.loginYear);
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
					case 2:
						flowstatus = "004";
						// ���˻�
						break;
					case 3:
						flowstatus = "";
						// ȫ����Ŀ
						break;
				}
				this.xmTableInfods = PrjInputStub.getMethod().getxmTableInfoAllds(filter.toString(), flowstatus, moduleid);
				tableAll.setDataSet(xmTableInfods);
				setTableAllProp();

			}
			catch (Exception ee)
			{
				ErrorInfo.showErrorDialog(ee, "������Ŀʧ��");
			}
		}
	}


	private String getPrjAttrCode()
	{

		String[] fxmsxStr = Common.isNullStr(fxmsx.getText()) ? null : (fxmsx.getText().split(";"));
		if (Common.isNullStr(fxmsx.getText()))
			return "";

		String[] fxmsxf1 = new String[fxmsxStr.length];
		if (fxmsxf1 == null)
			return "";
		String xmsxstr = "";
		try
		{
			for (int i = 0; i < fxmsxStr.length; i++)
			{

				DataSet ds1 = DBSqlExec.client().getDataSet(
						"select * from dm_xmsx  where chr_code ='" + fxmsxStr[i].substring(0, fxmsxStr[i].indexOf(" ")) + "' and set_year = " + Global.loginYear + " and rg_code = "
								+ Global.getCurrRegion());
				ds1.beforeFirst();
				ds1.next();
				if (ds1 != null && !ds1.isEmpty() && !ds1.bof() && !ds1.eof())
				{
					ds1.fieldByName("chr_code").getValue();
					xmsxstr += ds1.fieldByName("chr_code").getValue().toString() + " " + ds1.fieldByName("chr_name").getValue().toString() + ";";
					fxmsxf1[i] = ds1.fieldByName("chr_code1").getValue().toString();
					if (i > 0)
					{
						if (!fxmsxf1[i].equals(fxmsxf1[i - 1]))
						{
							JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ���Դ����������޸�,�������ͷ������ظ�ѡ��");
							return "";
						}

					}
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (xmsxstr.length() > 0)
		{
			xmsxstr = xmsxstr.substring(0, xmsxstr.length() - 1);
		}

		return Common.nonNullStr(xmsxstr);
	}


	public void doInsert()
	{

		try
		{
			if (tableAll.getTable().getSelectedRow() < 0)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ�����޸�");
				return;
			}
			String bmk = tableAll.rowToBookmark(tableAll.getTable().getSelectedRow());
			if (tableAll.getDataSet().gotoBookmark(bmk))
			{
				// ShowAuditHistoryPanelforInput aa = new
				// ShowAuditHistoryPanelforInput(
				// (tableAll.getDataSet().fieldByName("XMXH").getString()));

				// aa.doQuery();
				// Tools.centerWindow(aa);
				// aa.setVisible(true);
			}
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "��ѯ����");
		}
	}


	public void setXmxh(String xmxh)
	{
		this.xmbm = xmxh;
	}


	public String getGkid()
	{
		return gkid;
	}


	public void setGkid(String gkid)
	{
		this.gkid = gkid;
	}


	public String getGkCode()
	{
		return gkCode;
	}


	public void setGkCode(String gkCode)
	{
		this.gkCode = gkCode;
	}


	public String getGkName()
	{
		return gkName;
	}


	public void setGkName(String gkName)
	{
		this.gkName = gkName;
	}


	public String getEnID()
	{
		return enID;
	}


	public void setEnID(String enID)
	{
		this.enID = enID;
	}


	public void setIsHis(boolean isHis)
	{
		this.isHis = isHis;
	}


	public void doAuditToBack()
	{
		// TODO Auto-generated method stub

	}


	public void doUpdatedlg()
	{
		// TODO Auto-generated method stub

	}


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

}
