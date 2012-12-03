package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.action.PrjAuditStub;
import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class ShowAuditHistoryPanelforInput extends FDialog {

	private CustomTable tableAll;

	// private PrjAuditDTO pd;

	// private String[] divCodes;

	private DataSet dsAuditAll;

	// private FPanel pnlBase;

	// private FTextArea ta;

	private PrjInputDTO dto;

	private String[] prjCodes;

	public String AuditXmbm = "";

	TbPrjAuditAffix tpaa = null;

	FTextArea fSbly = null;

	FTextArea apprCommon = null;

	private static final long serialVersionUID = 1L;

	public ShowAuditHistoryPanelforInput(String[] prjCodes) {
		super(Global.mainFrame); // 指定窗体父亲
		dto = new PrjInputDTO();
		this.setSize(835, 600); // 设置窗体大小
		this.setResizable(false); // 设置窗体大小是否可变
		this.getContentPane().add(getBasePanel());
		this.dispose(); // 窗体组件自动充满。
		this.setTitle("审核轨迹记录查看界面"); // 设置窗体标题
		this.setModal(true); // 设置窗体模态显示
		this.prjCodes = prjCodes;
		doQuery();
	}

	private FPanel getBasePanel() {
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnl.setLayout(lay);
		pnl.addControl(getRTPanel(), new TableConstraints(1, 1, true, true));
		pnl.addControl(getPrjFilePanel(),
				new TableConstraints(1, 1, true, true));
		pnl.setRightInset(10);
		pnl.setLeftInset(10);
		return pnl;
	}

	private FPanel getRTPanel() {
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(16);
		pnl.setLayout(lay);
		try {
			String[] columnText = new String[] { "单位编码", "单位名称", "项目名称", "起始年度", "结束年度", "执行周期", "项目分类", "项目属性", "科目", "审核岗","审核人", "审核时间" };
			String[] columnField = new String[] { "DIV_CODE", "DIV_NAME", "XMMC", "C1", "C2", "C3", "C4", "C5",
					"ACCTS","NODE_NAME","VER_USER", "VER_DATE" };
			tableAll = new CustomTable(columnText, columnField, null, false,
					null);
			tableAll.reset();
			pnl.addControl(new FLabel(), new TableConstraints(1, 11, false,
					true));
			pnl.addControl(tableAll, new TableConstraints(10, 16, true, true));
			tableAll.getTable().addMouseListener(new XmTableMouseListener());
			// doQuery();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "生成审核信息表出错");
		}
		return pnl;
	}

	private void setTableAllProp() throws Exception {
		tableAll.reset();
		tableAll.getTable().setRowHeight(25);
		tableAll.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableAll.getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(3).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(4).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(5).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(6).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(7).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(8).setPreferredWidth(150);
		tableAll.getTable().getColumnModel().getColumn(9).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(10).setPreferredWidth(100);
		tableAll.getTable().getColumnModel().getColumn(11).setPreferredWidth(150);
		tableAll.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
		tableAll.getTable().repaint();
//		tableAll.getTable().getTableHeader().setPreferredSize(new Dimension(0, 30));
//		tableAll.getTable().setRowHeight(25);
	}

	public void doQuery() {
		// TODO 查询
		try {
			this.dsAuditAll = PrjAuditStub.getMethod().getAuditInfoforInput(
					prjCodes);

			tableAll.setDataSet(dsAuditAll);
			setTableAllProp();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "查询出错");
		}
	}

	/**
	 * 多标签
	 * 
	 * @return FTabbedPane
	 */
	public FTabbedPane getPrjFilePanel() {
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("项目填报", getPrjDetailPanel());
		tabpane.addControl("附    件", getPrjAffixPanel());
		tabpane.addControl("申报理由", getPrjRemarkPanel());
		tabpane.addControl("绩效目标说明", getPrjAppPanel());
		return tabpane;
	}

	private FPanel getPrjAppPanel() {
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

	private FPanel getPrjRemarkPanel() {
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

	private FPanel getPrjAffixPanel() {
		PrjAuditDTO pd = new PrjAuditDTO();
		PrjAuditAction pa = new PrjAuditAction(pd);
		tpaa = new TbPrjAuditAffix(pa);
		tpaa.setAuditXmbm(AuditXmbm);
		return tpaa.setBottomPanel();
	}

	private FPanel getPrjDetailPanel() {
		try {
			dto.refreshReportUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FPanel pnlReport = new FPanel();
		RowPreferedLayout layReport = new RowPreferedLayout(1);
		pnlReport.setLayout(layReport);
		pnlReport
				.add(dto.getReportUI(), new TableConstraints(1, 1, true, true));
		return pnlReport;
	}

	public class XmTableMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (dto.getCurState() != -1)
				return;
			if ((e.getButton() != MouseEvent.BUTTON2)
					&& (e.getButton() != MouseEvent.BUTTON3)) {
				if ((tableAll.getDataSet() != null)
						&& !tableAll.getDataSet().isEmpty()) {
					if (tableAll.getTable().getSelectedRow() < 0) {
						return;
					}
					try {
						if (!tableAll.getDataSet().gotoBookmark(
								tableAll.rowToBookmark(tableAll.getTable()
										.getSelectedRow()))) {
							return;
						}
						String xmxh = tableAll.getDataSet().fieldByName("XMXH")
								.getString();
						String aduitRole=tableAll.getDataSet().fieldByName("NODE_NAME").getString();
						String p_no=tableAll.getDataSet().fieldByName("P_NO").getString();
						dto.setXmxh(xmxh);
						refreshListData(xmxh,aduitRole,p_no);
						
						String apprComm = tableAll.getDataSet().fieldByName("APPRCOMMON").getString();
						fSbly.setValue("");
						apprCommon.setValue(apprComm);
						Map map = new HashMap();
						List sblyList = PrjInputStub.getMethod().getXmSbLyByXmxh(GlobalEx.loginYear, GlobalEx.getCurrRegion(), xmxh);
						if (sblyList.size() > 0)
						{
							map = (Map) sblyList.get(0);
							fSbly.setValue(map.get("ly") == null ? "" : map.get("ly").toString());
						}
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
			;
		}

		private void refreshListData(String xmxh,String aduitRole,String p_no) {
			try {

				DataSet ds = null;
				
				ds = PrjAuditStub.getMethod().getAuditHis(xmxh,
						Global.loginYear, Global.getCurrRegion(),aduitRole,p_no);
				ds.beforeFirst();
				ds.next();
				dto.setDsBody(ds);
				dto.setDsAcctSel();
				dto.refreshReportUI();
				((Report) dto.getReportUI().getReport()).setBodyData(dto
						.getDsBody());
				((Report) dto.getReportUI().getReport()).refreshBody();
				dto.getReportUI().repaint();
				tpaa.setAuditXmbm(ds.fieldByName("xmbm").getString());
				tpaa.setAuditXmxh(dto.getXmxh());
				tpaa.setTableData();
				// setCompEnable(false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
