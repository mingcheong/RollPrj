package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.action.PrjAuditStub;
import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.input.ui.AcctSelectDialog;
import gov.nbcs.rp.input.ui.PrjActionUI;
import gov.nbcs.rp.input.ui.ShowAuditHistoryPanelforInput;
import gov.nbcs.rp.input.ui.TbPrjAuditAffix;
import gov.nbcs.rp.input.ui.TbPrjUpdateDlg;
import gov.nbcs.rp.input.ui.TbProjectUI;

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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.Control;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDecimalField;
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
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.gl.viewer.FListPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;




public class PrjAuditUI extends RpModulePanel implements PrjAuditActionUI
{
	protected AcctSelectDialog acctSelect = new AcctSelectDialog();
	private static final long serialVersionUID = 1L;

	CustomTree treeEn = null;

	CustomTree xmTree = null;

	CustomTree jjkmTree = null;

	FTextField tfPrjCode = null;

	FTextField tfPrjName = null;

	FComboBox cbYearBegin = null;

	FComboBox cbYearEnd = null;

	FComboBox cbPrjCir = null;

	FComboBox cbPrjSort = null;

	FTreeAssistInput fxmsx = null;

	FComboBox cbPrjState = null;


	/** ��Ŀѡ�� */
	FTextField tfAcct = null; // ��Ŀѡ��

	/** ���ܿ�Ŀѡ��ť */
	FButton btnAcct;

	FTextField fshgj = null; // //����λ��

	DataSet dsAcctJJ = null;

	FTextArea fSbly = null;

	FTextArea apprCommon = null;

	FListPanel xmTable = null;

	FTreeAssistInput fgkdw = null;

	private FComboBox cbQueryType;

	// ��������Ϊ��xml�����ȡ
	String listPanelCode = "900002";

	private String xmxh = "";

	private String xmbm = "";

	private String enID = "";

	public String AuditXmbm = "";

	public boolean isOK = false;

	TbPrjAuditAffix tpaa = null;

	String[] kmxzListString = null;

	FButton fb = null;

	List xmTableInfo = null;

	private CustomTable tableAll;

	private DataSet xmTableInfods;

	int[] zts;

	private PrjInputDTO dto;
	private	PrjInputDTO dtoh;

	FButton btnAD;
	FButton btnDD;

	String gkid;

	String gkCode;

	String gkName;

	private boolean isInit = true;

	private boolean isHis = false;

	private boolean isoldPrj = false;

	private FSplitPane pnlInfoXmall;

	private FSplitPane pnlInfoXm;

	private boolean isInitPrj = true;

	private boolean dosavaAudit = false;
	String moduleid = Global.getModuleId();

	FComboBox cbYear = null; // ��ѡ���
	String selectYear = GlobalEx.loginYear;

	private FDecimalField total_sums = null;
	
	/** ������ */
	private PrjAuditDTO pd = new PrjAuditDTO();
	private DataSet dsDiv;
	/** ��λ�� */
	private CustomTree treeDiv = null;
	private String[] divCodes;
	private DataSet dsAuditAll;
	private FTabbedPane tabpane;
	private FPanel pnlHis;
	private TbProjectUI tph = new TbProjectUI();
	private boolean isInitHis = true;

	private boolean isRfreshHis = true;
	
	private PrjAuditAction pa = new PrjAuditAction(pd);
	RunTimebar  bar=null;
	
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
			this.refreshprj();
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
		bar=new RunTimebar();
		dtoh = new PrjInputDTO();
		tph.setPrjInputDTO(dtoh);
		dto = new PrjInputDTO();
		dto.setDsAcctSel();
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
						else
							isoldPrj = false;

						dto.setXmxh(xmxh);

						refreshListData(xmxh);

						dtoh.setXmxh(xmxh);
					
//						tph.pnlInfoXmall = new FSplitPane();
						tph.refreshListData(xmxh);

						
						String locked = tableAll.getDataSet().fieldByName("locked").getString();

						if (StringUtils.isNotEmpty(locked))
						{
							List controls = getToolbarPanel().getSubControls();
							for (int i = 0; i < controls.size(); i++)
							{
								FButton btnGet = (FButton) controls.get(i);
								if ("�޸�".equals(btnGet.getText()))
								{
									if (locked.equals("����"))
									{
										btnGet.setEnabled(false);
									}
									else
									{
										btnGet.setEnabled(true);
									}
								}
							}
						}

					}
					catch (Exception e1)
					{
						ErrorInfo.showErrorDialog(e1.getMessage());
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
				pnlInfoXmall.addControl(getPrjInfoPanel());
				initOhterPanel();
				pnlInfoXmall.setDividerLocation(200);
				isInitPrj = false;
			}
			DataSet ds = null;
			if (tabpane.getSelectedIndex() == 1) {
				isHis=true;
				tph.isHis=true;
				
			} else {
				isHis=false;
				tph.isHis=false;
			}
			
			if (!isHis)
				ds = PrjInputStub.getMethod().getPrjCreateInfo(" and set_year = " + selectYear + " and rg_code= " + Global.getCurrRegion() + " and xmxh = '" + aPrjXH + "'");
			else
				tph.refreshListData(aPrjXH);
//				ds = PrjInputStub.getMethod().getPrjCreateHisInfo(selectYear ," and rg_code= " + Global.getCurrRegion() + " and xmxh = '" + aPrjXH + "'");
			ds.beforeFirst();
			ds.next();
			String apprComm = "";
			if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
			{
				String gkcode = ds.fieldByName("GKDW_CODE").getString();
				apprComm = ds.fieldByName("apprcommon").getString();
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

				String total_sum = ds.fieldByName("total_sum").getValue() == null ? "" : ds.fieldByName("total_sum").getValue().toString();

				dto.setOneYearPrj(Common.isEqual(qsn, jsn));
				dto.setCurYearBegin(Common.isEqual(Global.loginYear, qsn));
				setPanelValue(ds.fieldByName("xmbm").getString(), ds.fieldByName("xmmc").getString(), qsn, jsn, ds.fieldByName("c3").getString(), ds.fieldByName("c4").getString(), ds
						.fieldByName("c5").getString(), ds.fieldByName("c6").getString(), ds.fieldByName("accts").getString(), gkdw, total_sum);
			}
			else
			{
				setPanelValue(true);
			}
			dto.setHavSetAcct(!Common.isNullStr(Common.nonNullStr(tfAcct.getValue())));
			fSbly.setValue("");
			apprCommon.setValue(apprComm);
			Map map = new HashMap();
			List sblyList = PrjInputStub.getMethod().getXmSbLyByXmxh(selectYear, GlobalEx.getCurrRegion(), xmxh);
			if (sblyList.size() > 0)
			{
				map = (Map) sblyList.get(0);
				fSbly.setValue(map.get("ly") == null ? "" : map.get("ly").toString());
			}

			// ��ʾ����б�
			if (!isHis)
				dto.setDsBody(PrjInputStub.getMethod().getPrjDetailInfo(selectYear, dto.getXmxh(), Global.getCurrRegion()));
			else
				dto.setDsBody(PrjInputStub.getMethod().getPrjDetailInfoHis(selectYear, dto.getXmxh(), Global.getCurrRegion()));

			dto.refreshReportUI();
			((Report) dto.getReportUI().getReport()).setBodyData(dto.getDsBody());
			((Report) dto.getReportUI().getReport()).refreshBody();
			dto.getReportUI().repaint();
			tpaa.setAuditXmbm(ds.fieldByName("xmbm").getString());
			tpaa.setAuditXmxh(dto.getXmxh());
			tpaa.setTableData();
			setCompEnable(false);
		}
		catch (Exception e1)
		{}
	}


	private void setPanelValue(String bm, String mc, String qn, String jn, String zq, String fl, String sx, String zt, String km, String gkdw, String total_sum) throws Exception
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
		total_sums.setText(total_sum);
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
		tableAll.getTable().getColumnModel().getColumn(1).setPreferredWidth(200);
		tableAll.getTable().getColumnModel().getColumn(2).setPreferredWidth(200);
		tableAll.getTable().getColumnModel().getColumn(3).setPreferredWidth(200);
		tableAll.getTable().getColumnModel().getColumn(4).setPreferredWidth(200);
		tableAll.getTable().getColumnModel().getColumn(5).setPreferredWidth(150);
		// tableAll.getTable().getColumnModel().getColumn(5)
		// .setPreferredWidth(250);
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

			// List enList = PrjInputStub.getMethod().getList("ele_enterprise");
			//
			// XMLData treeDataEn = new XMLData();
			// treeDataEn.put("row", enList);
			// fgkdw.setData(treeDataEn);
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
					total_sums.setEditable(false);
					total_sums.setValue("");
				}
				else
				{
					// ����������ԣ�����Ա༭
					String q1 = Common.nonNullStr(cbYearBegin.getValue());
					String q2 = Common.nonNullStr(cbYearEnd.getValue());

					boolean change = Common.isEqual(q1, q2);
					if (change)
					{}
					dto.setOneYearPrj(false);
					cbYearBegin.setEnabled(true && !isoldPrj);
					cbYearEnd.setEnabled(true);
					total_sums.setEditable(true);
				}

				if (dto.getDsBody() != null && !dto.getDsBody().isEmpty())
				{
					// ����reportUI�ɱ༭
					try
					{
						dto.refreshReportUI();
						dto.getDsBody().edit();
					}
					catch (Exception e)
					{
						ErrorInfo.showErrorDialog(e, e.getMessage());
					}
				}

			}
		});
		fxmsx.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent e)
			{
				String[] fxmsxStr = fxmsx.getValue() == null ? null : (String[]) fxmsx.getValue();
				if(fxmsxStr==null)
					return;
				String[] fxmsxf1 = new String[fxmsxStr.length];
				String xmsxstr = "";
				try
				{
					DataSet ds1 = DBSqlExec.client().getDataSet("select * from dm_xmsx  where set_year=" + Global.loginYear);
					for (int i = 0; i < fxmsxStr.length; i++)
					{
						if (!ds1.locate("chr_id", fxmsxStr[i]))
							continue;
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

		String[] columnText = new String[] { "��λ����", "��λ����", "��Ŀ����", "��Ŀ����", "����Ԥ��","����״̬" };
		String[] columnField = new String[] { "div_code", "div_name", "prj_code", "prj_name", "smoney","wf_state" };
		tableAll = new CustomTable(columnText, columnField, null, true, null);
		setTableAllProp();

		xmTableAll.addControl(getQueryType(), new TableConstraints(1, 2, false, true));
		// f2 = new FLabel();
		// f2.setForeground(Color.red);
		// xmTableAll.addControl(f2, new TableConstraints(1, 3, false, true));

		// xmTableAll.addControl(getQueryType(), new TableConstraints(1, 2,
		// false,
		// true));
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
					cbQueryType.setEnabled(false);
					cbQueryType.setSelectedIndex(4);
				}
				else
				{
					cbQueryType.setSelectedIndex(0);
					cbQueryType.setEnabled(true);
				}
				setButtonState1(true);
			 
				bar.display();
				//��̬������ʼ�������
				if(cbYearBegin==null||cbYearEnd==null)
					return;
				String qsndTmp = "";
				int numQs = Integer.parseInt(selectYear) - 5;
				for (int i = 0; i <= 5; i++)
				{
					qsndTmp += (numQs + i) + "#" + (numQs + i) + "+";
				}
				if (qsndTmp.length() > 0)
				{
					qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
				}
				
				cbYearBegin.setRefModel(qsndTmp);
				qsndTmp="";
				int num = Integer.parseInt(cbYear.getValue().toString());
				for (int i = 0; i < 10; i++)
				{

					qsndTmp += (num + i) + "#" + (num + i) + "+";

				}
				if (qsndTmp.length() > 0)
				{
					qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
				}
				cbYearEnd.setRefModel(qsndTmp);
			
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
						createDefaultData(value);
						dto.setDsAcctSel((String[]) value.get(0));
					}
				}
			}
		});
		total_sums = new FDecimalField("��Ͷ�룺");
		// ���ö�������
		total_sums.setEditable(false);
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
		xmxx.addControl(tfAcct, new TableConstraints(1, 5, false, true));
		xmxx.addControl(btnAcct, new TableConstraints(1, 1, false, true));
		xmxx.addControl(total_sums, new TableConstraints(1, 2, false, true));
		xmxx.addControl(getPrjDetailButtonPanel(), new TableConstraints(1, 8, false, true));
		return xmxx;
	}


	private void createDefaultData(List value)
	{
		int c = 0;
		try
		{
			DataSet dsBody = ((Report) dto.getReportUI().getReport()).getBodyData();
			if (dsBody != null && !dsBody.isEmpty())
			{

				List list = new ArrayList();
				dsBody.beforeFirst();
				while (dsBody.next())
				{
					String supType = dsBody.fieldByName("SB_CODE").getString();
					if ("111".equalsIgnoreCase(supType) || "222".equalsIgnoreCase(supType) || "333".equalsIgnoreCase(supType))
						continue;

					list.add(dsBody.fieldByName("BS_ID").getString());
				}
				for (int k = 0; k < list.size(); k++)
				{
					if (dsBody.locate("BS_ID", Common.nonNullStr(list.get(k))))
						dsBody.delete();
				}

				String[] sAcctCodes = (String[]) value.get(0);
				String[] sAcctNames = (String[]) value.get(1);
				String[] sAcctBsIDs = (String[]) value.get(3);

				for (int j = 0; j < sAcctCodes.length; j++)
				{

					if (dsBody.locate("BS_ID", sAcctBsIDs[j]))
					{
						c++;
						continue;

					}

					dsBody.append();
					dsBody.fieldByName("xmxh").setValue(xmxh);
					dsBody.fieldByName("rg_code").setValue(Global.getCurrRegion());
					dsBody.fieldByName("ACCT_NAME").setValue("[" + sAcctCodes[j] + "]" + sAcctNames[j]);
					dsBody.fieldByName("BS_ID").setValue(sAcctBsIDs[j]);
					dsBody.fieldByName("ACCT_CODE").setValue(sAcctCodes[j]);
					dsBody.fieldByName("setYear").setValue(Global.loginYear);
					dsBody.fieldByName("YSJC_MC").setValue("");
					// dsBody.fieldByName("SB_TYPE").setValue(String.valueOf(i++));
					// dsBody.fieldByName("SB_CODE").setValue(
					// String.valueOf(400000 + i));
				}

				dsBody.beforeFirst();
				dsBody.edit();
				int i = 1;
				while (dsBody.next())
				{
					String supType = dsBody.fieldByName("SB_CODE").getString();
					if ("111".equalsIgnoreCase(supType) || "222".equalsIgnoreCase(supType) || "333".equalsIgnoreCase(supType))
						continue;
					dsBody.fieldByName("SB_TYPE").setValue(String.valueOf(i++));
					dsBody.fieldByName("SB_CODE").setValue(String.valueOf(400000 + i));
				}
				// dsBody.applyUpdate();
				dto.refreshReportUI();
			}
			if (c == 0)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "���ܿ�Ŀ�޸ģ���Ŀ��ϸ������ݱ����");
			}
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "������Ŀ��ϸ��Ϣʧ�ܣ�������ϢΪ:" + ee.getMessage());
		}
	}


	private FTabbedPane getPrjDownPanel()
	{
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("��Ŀ�", getPrjDetailPanel());
		tabpane.addControl("��    ��", getPrjAffixPanel());
		tabpane.addControl("�걨����", getPrjRemarkPanel());
		tabpane.addControl("��ЧĿ��˵��", getPrjAppPanel());
		return tabpane;
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


	private Control getPrjAppPanel()
	{
		FPanel ply = new FPanel();
		RowPreferedLayout plyRpl = new RowPreferedLayout(1);
		ply.setLayout(plyRpl);
		ply.setTopInset(10);
		ply.setLeftInset(10);
		ply.setRightInset(10);
		ply.setBottomInset(10);
		apprCommon = new FTextArea();
		apprCommon.setProportion(0.001f);
		apprCommon.getEditor().setBackground(Color.white);
		ply.addControl(apprCommon, new TableConstraints(1, 1, true, true));
		return ply;
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
		pnlInfo.addControl(getLeftTopPanel());
		pnlInfo.addControl(getRightPanel());
//		addTreeListener();
		return pnlInfo;
	}


	

	private void addCompListener()
	{
		btnAD.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// ������ϸ
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

					if (!StringUtils.isNumeric(sbType))
					{
						sbType = "0";
						sbCode = "10000";
					}

					ds.fieldByName("SB_TYPE").setValue(String.valueOf((Integer.parseInt(sbType) + 1)));
					ds.fieldByName("SB_CODE").setValue(String.valueOf((Integer.parseInt(sbCode) + 1)));
					ds.fieldByName("ACCT_NAME").setValue("");
					ds.fieldByName("ACCT_NAME_JJ").setValue("");
					dto.getReportUI().repaint();
					ds.maskDataChange(false);
				}
				catch (Exception e1)
				{
					ErrorInfo.showErrorDialog(e1.getMessage());
				}
			}
		});

		btnDD.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// ɾ����ϸ
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
					ErrorInfo.showErrorDialog(e1.getMessage());
				}
			}
		});
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

		if (!dosavaAudit)
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
		if ((cbPrjState.getValue() == null) || cbPrjState.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ״̬����Ϊ��");
			return;
		}
		if (Common.isNullStr(Common.nonNullStr(tfAcct.getValue())))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "���ܿ�Ŀ����Ϊ��");
			return;
		}
		if (Integer.valueOf((String) cbYearBegin.getValue()).intValue() == Integer.valueOf((String) cbYearEnd.getValue()).intValue() && cbPrjCir.getSelectedIndex() == 2)
		{
			if (Common.isNullStr(Common.nonNullStr(total_sums.getValue())))
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "��������Ŀ��Ͷ�벻��Ϊ��");
				return;
			}
			new MessageBox("��������Ŀ��ʼ��Ȳ��ܵ��ڽ�����ȣ�����������!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
		dto.calData();
//		if (cbPrjCir.getValue().equals("003") && Integer.valueOf((String) cbYearBegin.getValue()).intValue() < Integer.valueOf((String) cbYearEnd.getValue()).intValue())
//		{
//			if (((Report) dto.getReportUI().getReport()).getCellElement(4, 3).getValue() == null)
//			{
//				new MessageBox("�Ѱ���������Ϊ��!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
//				return;
//			}
//
//		}

		try
		{
			DataSet ds = ((Report) dto.getReportUI().getReport()).getBodyData();
			if (ds == null || ds.isEmpty())
				return;

			ds.gotoBookmark("3");
			String f2Str = ds.fieldByName("F2").getString();
			String f3Str = ds.fieldByName("F3").getString();
			if(StringUtils.isEmpty(f2Str))
				f2Str = "0";
			if(StringUtils.isEmpty(f3Str))
				f3Str = "0";

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
			String sumSql = "select sum(b.f2) as f2_sum,sum(b.f3) as f3_sum from rp_xmjl a inner join rp_xmsb b on a.xmxh = b.xmxh where a.xmbm != '" + prjCode + "' and a.xmbm like '" + divCode
					+ "%' and a.set_year = '" + Global.loginYear + "' and b.sb_code = '333'";
			String limitSql = "select a.f2,a.f3,a.enable from rp_dwkzs a where a.en_code = '" + divCode + "' and a.set_year = '" + Global.loginYear + "'";
			List sumList = QueryStub.getClientQueryTool().findBySql(sumSql);

			List limitList = QueryStub.getClientQueryTool().findBySql(limitSql);
			double f2Sum = 0;
			double f3Sum = 0;
			if (sumList != null && !sumList.isEmpty())
			{
				if (StringUtils.isNotEmpty(((Map) sumList.get(0)).get("f2_sum").toString()))
					f2Sum = Double.parseDouble(((Map) sumList.get(0)).get("f2_sum").toString()) + Double.parseDouble(f2Str);
				else
					f2Sum = Double.parseDouble(f2Str);

				if (StringUtils.isNotEmpty(((Map) sumList.get(0)).get("f3_sum").toString()))
					f3Sum = Double.parseDouble(((Map) sumList.get(0)).get("f3_sum").toString()) + Double.parseDouble(f3Str);
				else
					f3Sum = Double.parseDouble(f3Str);
			}
			else
			{
				f2Sum = Double.parseDouble(f2Str);
				f3Sum = Double.parseDouble(f3Str);
			}

			if (limitList != null && !limitList.isEmpty())
			{
				Map limit = (Map) limitList.get(0);
				String strF2 = limit.get("f2").toString();
				String strF3 = limit.get("f3").toString();
				String strEnable = limit.get("enable").toString();
				if (StringUtils.isNotEmpty(strEnable) && strEnable.equalsIgnoreCase("1"))
				{
					double f2 = 0;
					double f3 = 0;
					if (StringUtils.isNotEmpty(strF2))
						f2 = Double.parseDouble(strF2);
					if (f2Sum > f2)
					{
						JOptionPane.showMessageDialog(Global.mainFrame, "һ��Ԥ���ѳ�����������" + f2Sum + "Ԫ�������������Ԥ���");
						return;
					}

					if (StringUtils.isNotEmpty(strF3))
						f3 = Double.parseDouble(strF3);
					if (f3Sum > f3)
					{
						JOptionPane.showMessageDialog(Global.mainFrame, "����Ԥ���ѳ�����������" + f3Sum + "Ԫ�������������Ԥ���");
						return;
					}
				}
			}
				
			String sql_lock="select * from rp_xmlock where is_lock=1 " +
					"and xmxh='"+xmxh+"' and set_year="+Global.loginYear+" and rg_code="+Global.getCurrRegion();
			List list = QueryStub.getClientQueryTool().findBySql(sql_lock);
			if (list != null && !list.isEmpty())
			{
				Map limit = (Map) list.get(0);
				String strF2 = limit.get("money1").toString();
				String strF3 = limit.get("money2").toString();
				if(Double.parseDouble(strF2)!= Double.parseDouble(f2Str))
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "һ��Ԥ�㲻���ڿ�������" + strF2 + "Ԫ���������һ��Ԥ���");
					return;
				}
				if(Double.parseDouble(strF3)!= Double.parseDouble(f3Str))
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "����Ԥ�㲻���ڿ�������" + strF3 + "Ԫ�������������Ԥ���");
					return;
				}
			}

		}
		catch (Exception e1)
		{
			ErrorInfo.showErrorDialog(e1, e1.getMessage());
			return;
		}

		InfoPackage info = checkData();
		if (!info.getSuccess())
		{
			ErrorInfo.showErrorDialog("����ʧ�ܣ�������ϢΪ��\n" + info.getsMessage());
			return;
		}
		try
		{
			DataSet xmData = PrjInputStub.getMethod().getXmDataByXmxh(dto.getXmxh());
			if (!xmData.locate("xmxh", dto.getXmxh())) { return; }

			xmData.edit();
			xmData.locate("xmxh", xmxh);
			xmData.fieldByName("c1").setValue(cbYearBegin.getValue());
			xmData.fieldByName("c2").setValue(cbYearEnd.getValue());
			xmData.fieldByName("c3").setValue(cbPrjCir.getValue());
			xmData.fieldByName("c4").setValue(cbPrjSort.getValue());

			String total_sum = total_sums.getValue() == null ? "" : total_sums.getValue().toString();

			xmData.fieldByName("total_sum").setValue(total_sum);
			xmData.fieldByName("apprcommon").setValue(apprCommon.getValue() == null ? "" : apprCommon.getValue().toString());
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
			// RpXmjl rpXmjl = new RpXmjl();
			// rpXmjl.setXmxh(xmxh);
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
			((Report) dto.getReportUI().getReport()).setBodyData(PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, dto.getXmxh(), Global.getCurrRegion()));
			((Report) dto.getReportUI().getReport()).refreshBody();
			dto.getReportUI().repaint();
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
		treeDiv.setEnabled(false);
		isOK = true;
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
		xmxxForAudit(true);
		cbQueryType.setEnabled(false);
		cbYear.setEnabled(false);

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
		dosavaAudit = true;
	}


	public void xmxxForAudit(boolean xxedit)
	{
		setCompEnable(xxedit);
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
					total_sums.setEditable(xxedit);
				}
			}
			else
			{
				total_sums.setEditable(false);
				cbYearBegin.setEnabled(false);
				cbYearEnd.setEnabled(false);
			}
			// cbPrjCir.setEnabled(xxedit && !isoldPrj);
			cbPrjCir.setEnabled(xxedit);
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
			refreshListData(tableAll.getDataSet().fieldByName("xmxh").getString());
			dto.refreshReportUI();
			dto.setCurState(-1);
			((Report) dto.getReportUI().getReport()).setBodyData(PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, dto.getXmxh(), Global.getCurrRegion()));
			((Report) dto.getReportUI().getReport()).refreshBody();
			dto.getReportUI().repaint();
		}
		catch (Exception ee)
		{}
		finally
		{

			xmxxForAudit(false);
			isOK = false;
			if (tableAll != null)
				tableAll.getTable().setEnabled(true);
			if (treeEn != null)
				treeEn.setEnabled(true);
			cbQueryType.setEnabled(true);
			cbYear.setEnabled(true);

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

		public void doSendAudit()
		{
			// TODO Auto-generated method stub
			
			int[] rows = tableAll.getTable().getSelectedRows();
			if (rows.length == 0)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ��Ҫ��˵���Ŀ");
				return;
			}
			List list = getPrjCodes();
			String[] prjCodes = (String[]) list.get(3);
			String[] prjNames = (String[]) list.get(2);
			List prjCodes_CanAudit = new ArrayList();
			StringBuffer info = new StringBuffer();
			for (int i = 0; i < prjCodes.length; i++)
			{
				try
				{
//					DataSet dss = ((Report) dto.getReportUI().getReport()).getBodyData();
//					if (dss == null || dss.isEmpty())
//						return;
//
//					dss.gotoBookmark("3");
//					String f2Str = dss.fieldByName("F2").getString();
//					String f3Str = dss.fieldByName("F3").getString();
//					if(StringUtils.isEmpty(f2Str))
//						f2Str = "0";
//					if(StringUtils.isEmpty(f3Str))
//						f3Str = "0";
//					String sql_lock="select money1,money2 from rp_xmlock where is_lock=1 " +
//					"and xmxh='"+xmxh+"' and set_year="+Global.loginYear+" and rg_code="+Global.getCurrRegion();
//					List lists = QueryStub.getClientQueryTool().findBySql(sql_lock);
//					if (lists != null && !lists.isEmpty())
//					{
//						Map limit = (Map) lists.get(0);
//						String strF2 = limit.get("money1").toString();
//						String strF3 = limit.get("money2").toString();
//						if(Double.parseDouble(strF2)!= Double.parseDouble(f2Str))
//						{
//							JOptionPane.showMessageDialog(Global.mainFrame, "һ��Ԥ�㲻���ڿ�������" + strF2 + "Ԫ���������һ��Ԥ���");
//							return;
//						}
//						if(Double.parseDouble(strF3)!= Double.parseDouble(f3Str))
//						{
//							JOptionPane.showMessageDialog(Global.mainFrame, "����Ԥ�㲻���ڿ�������" + strF3 + "Ԫ�������������Ԥ���");
//							return;
//						}
//					}
					
					
					PubInterfaceStub.getMethod().savehistory(prjCodes,false);
					if (!PubInterfaceStub.getMethod().BackAuditInfoByDiv(0, prjCodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid).getSuccess())
					{
						info.append("��Ŀ: [" + prjCodes[i] + "]" + prjNames[i] + " û�����Ȩ��\n");
						continue;
					}
					else
					{
						prjCodes_CanAudit.add(prjCodes[i]);
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (prjCodes_CanAudit.size() > 0)
			{
				if (!Common.isNullStr(info.toString()))
				{
					info.append("�Ƿ������ˣ�");
					if (JOptionPane.showConfirmDialog(Global.mainFrame, info, "��ʾ", JOptionPane.OK_CANCEL_OPTION) != 0) { return; }
				}
			}
			else
			{
				info.append("û����Ŀ��Ҫ���!");
				JOptionPane.showMessageDialog(Global.mainFrame, info.toString());
				return;
			}
			try
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "����ɹ�");

				refreshprj();
			}
			catch (Exception ee)
			{
				ErrorInfo.showErrorDialog(ee, "���ʧ��");
			}
		}


	public void Sendtoback()
	{
	
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
			PubInterfaceStub.getMethod().BackAuditInfoByDiv(1, prjcodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid);
			// }
			JOptionPane.showMessageDialog(Global.mainFrame, "�����ɹ�");
			refreshprj();
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



	synchronized public void refreshprj()
	{
		refreshprj(Global.loginYear);
	}


	public void doInsert()
	{

		int[] rows = tableAll.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		String[] prjCodes = new String[rows.length];

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = tableAll.rowToBookmark(rows[i]);

				if (tableAll.getDataSet().gotoBookmark(bmk))
				{
					prjCodes[i] = tableAll.getDataSet().fieldByName("xmxh").getString();
				}
			}
				ShowAuditHistoryPanelforInput aa = new ShowAuditHistoryPanelforInput(prjCodes);
				// aa.doQuery();
				Tools.centerWindow(aa);
				aa.setVisible(true);
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


	private FComboBox getQueryType() throws Exception
	{
		cbQueryType = new FComboBox("");
		cbQueryType.setTitle("��Ŀ״̬��");
		cbQueryType.setProportion(0.4f);
		cbQueryType.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent arg0)
			{
				bar.display();
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


	public void setButtonState1(boolean state)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("�ر�".equals(btnGet.getText().trim()))
				continue;
			if ("�鿴".equals(btnGet.getText().trim()))
					continue;
			btnGet.setEnabled(state);;
		}
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
			dsDiv = PrjAuditStub.getMethod().getDivDataPop(Global.loginYear, pd.getBathcNo());
			// }
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
	
	public void dosearch()
	{
		RunTimebar bar=new RunTimebar();
		bar.display();
	}
	
	synchronized public void refreshprj(String year)
	{
		// TODO ��ѯ
		try
		{
			divCodes = getDivRecords();
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
					// ������
					flowstatus = "003";
					break;
				case 3:
					flowstatus = "004";
					// ���˻�
					break;
				case 4:
					flowstatus = "";
					// ȫ����Ŀ
					break;
			}
			dsAuditAll = PrjAuditStub.getMethod().getAuditInfo(0, divCodes, null, year, pd.getUserCode(), pd.getDepCode(), pd.getBathcNo(), 0, "", flowstatus, moduleid);
			if (dsAuditAll != null && !dsAuditAll.isEmpty())
			{
				String nodeName = "";
				String xmxhNo = "";
				dsAuditAll.edit();
				dsAuditAll.beforeFirst();
				while (dsAuditAll.next())
				{
					xmxhNo = dsAuditAll.fieldByName("xmxh").getString();
					nodeName = PrjInputStub.getMethod().getNodeName(xmxhNo,year);
					dsAuditAll.fieldByName("wf_state").setValue(nodeName);
				}
			}
			
			tableAll.setDataSet(dsAuditAll);
			setTableAllProp();
			if(tfPrjCode!=null)
			{
				setPanelValue(true);
				dto.setDsBody(null);
				dto.refreshReportUI();
			}
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "��ѯ����");
		}
	}
	/**
	 * ����ѡ��ĵ�λ
	 * 
	 * @throws Exception
	 */
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
	
	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	private FPanel getLeftTopPanel()
	{
		FPanel pnlLTop = new FPanel();
		FLabel label = new FLabel();
		label.setText("                      Ԥ�㵥λ");
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlLTop.setLayout(lay);
		getDivTree();
		pnlLTop.addControl(getTreePanel(treeDiv), new TableConstraints(10, 1, true, true));
		pnlLTop.setTopInset(10);
		pnlLTop.setLeftInset(10);
		pnlLTop.setRightInset(10);
		pnlLTop.setBottomInset(10);
		return pnlLTop;
	}
	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	private FScrollPane getTreePanel(CustomTree tree)
	{
		FScrollPane pnlTree = new FScrollPane();
		pnlTree.addControl(tree);
		return pnlTree;
	}
	
	private FTabbedPane getPrjInfoPanel() throws Exception {
		tabpane = new FTabbedPane();
		FPanel ply = new FPanel();
		RowPreferedLayout plyRpl = new RowPreferedLayout(1);
		ply.setLayout(plyRpl);
		ply.setTopInset(10);
		ply.setLeftInset(10);
		ply.setRightInset(10);
		ply.setBottomInset(10);
		tabpane.addControl("�������", getPrjGenraAndDetailPanel());
		tabpane.addControl("��ʷ����", getShowInfoPanelHs());
		tabpane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					
						String prjID = tableAll.getDataSet().fieldByName(
								"prj_id").getString();
						if (tabpane.getSelectedIndex() == 1) {
							isHis=true;
							tph.isHis=true;
							tph.isInitPrj=true;
							tph.refreshListData(prjID);
						} else {
							isHis=false;
							tph.isHis=false;
						}
						
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return tabpane;
	}
	private FPanel getShowInfoPanelHs() throws Exception {
		pnlHis = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
	
		lay.setRowHeight(25);
		lay.setRowGap(5);
		lay.setColumns(5);
		pnlHis.setLayout(lay);
		
	
		pnlHis.addControl(tph.getPrjGenraAndDetailPanel(),
				new TableConstraints(1, 10, true, true));
		
		tph.initOhterPanel();
		isInitHis = false;
		return pnlHis;
	}
	
	private List getPrjCodes()
	{
		List list = new ArrayList();
		try
		{
			int[] rows = tableAll.getTable().getSelectedRows();
			if (rows.length == 0) { return null; }
			String[] prjcodes = new String[rows.length];
			String[] prjNames = new String[rows.length];
			String[] divcodes = new String[rows.length];
			String[] xmxh = new String[rows.length];
			String bmk = tableAll.getDataSet().toogleBookmark();
			String[] bmks = pa.getBMKs(rows, tableAll);
			DataSet ds = tableAll.getDataSet();
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					Map map = ds.getOriginData();
					prjcodes[i] = Common.nonNullStr(map.get(IPrjAudit.PrjAuditTable.prj_code));
					prjNames[i] = Common.nonNullStr(map.get(IPrjAudit.PrjAuditTable.prj_name));
					divcodes[i] = Common.nonNullStr(map.get(IPrjAudit.PrjAuditTable.div_code));
					xmxh[i] = Common.nonNullStr(map.get("XMXH"));
				}
				else
				{
					continue;
				}
			}
			tableAll.getDataSet().gotoBookmark(bmk);
			list.add(0, divcodes);
			list.add(1, prjcodes);
			list.add(2, prjNames);
			list.add(3, xmxh);
		}
		catch (Exception ee)
		{
			ee.printStackTrace();
		}
		return list;
	}

	public void backToAuditback() {
		// TODO Auto-generated method stub
		int[] rows = tableAll.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		String[] prjcodes = new String[rows.length];

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = tableAll.rowToBookmark(rows[i]);

				if (tableAll.getDataSet().gotoBookmark(bmk))
					prjcodes[i] = tableAll.getDataSet().fieldByName("xmxh").getString();

			}
			PubInterfaceStub.getMethod().BackAuditInfoByDiv(3, prjcodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid);

			JOptionPane.showMessageDialog(Global.mainFrame, "�˻سɹ�");
			refreshprj();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "�˻�ʧ��");

		}
	}

	public void doBackAudit() {
		// TODO Auto-generated method stub
		int[] rows = tableAll.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		String[] prjcodes = new String[rows.length];

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = tableAll.rowToBookmark(rows[i]);

				if (tableAll.getDataSet().gotoBookmark(bmk))
					prjcodes[i] = tableAll.getDataSet().fieldByName("xmxh").getString();

			}
			PubInterfaceStub.getMethod().BackAuditInfoByDiv(2, prjcodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid);

			JOptionPane.showMessageDialog(Global.mainFrame, "�˻سɹ�");
			refreshprj();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "�˻�ʧ��");

		}
	}

	public void doBackToDivAudit() {
		// TODO Auto-generated method stub
		
	}

	public void doExprtOtherInfo() {
		// TODO Auto-generated method stub
		
	}

	public void doImportData() {
		// TODO Auto-generated method stub
		
	}



	public void doSendTo() {
		// TODO Auto-generated method stub
		
	}
	public void doBackToAudit()
	{
		// TODO Auto-generated method stub

		int[] rows = tableAll.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		String[] prjcodes = new String[rows.length];

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = tableAll.rowToBookmark(rows[i]);

				if (tableAll.getDataSet().gotoBookmark(bmk))
					prjcodes[i] = tableAll.getDataSet().fieldByName("xmxh").getString();

			}
			PubInterfaceStub.getMethod().BackAuditInfoByDiv(1, prjcodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), moduleid);

			JOptionPane.showMessageDialog(Global.mainFrame, "�����ɹ�");
			refreshprj();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "����ʧ��");

		}
	}
	
	public class RunTimebar implements Runnable{
		public RunTimebar() {
		}

		public void display() {
			Thread myThread = new Thread(this);
			myThread.start();
		}

		public void run() {
			ProgressBar pf = new ProgressBar(Global.mainFrame,
					"���ڼ������ݣ����Ժ򡤡���������", false);
			try {
				selectYear = cbYear.getValue().toString();
				refreshprj(selectYear);
				pf.dispose();
			} catch (Exception e) {
				e.printStackTrace();
				ErrorInfo.showErrorDialog("ˢ������ʧ�ܣ�");
				pf.dispose();
			}
		}
	}



}
