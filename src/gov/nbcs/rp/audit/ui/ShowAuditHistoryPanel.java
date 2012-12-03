package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.action.PrjAuditStub;
import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.ui.list.CustomComboBox;
import gov.nbcs.rp.common.ui.table.CustomTable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.SwingConstants;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;

public class ShowAuditHistoryPanel extends FDialog {

	private CustomTable tableAll;

	private CustomComboBox cbQueryType;

	private PrjAuditDTO pd;

//	private String[] divCodes;

	private DataSet dsAuditAll;

	// private FPanel pnlBase;

	private FTextArea ta;
	
	private List prjCodes ;

	private static final long serialVersionUID = 1L;

	public ShowAuditHistoryPanel(PrjAuditDTO pd, String[] divCodes,
			List prjCodes) {
		super(Global.mainFrame); // 指定窗体父亲
		this.setSize(835, 600); // 设置窗体大小
		this.setResizable(false); // 设置窗体大小是否可变
		this.getContentPane().add(getBasePanel());
		this.dispose(); // 窗体组件自动充满。
		this.setTitle("审核轨迹记录查看界面"); // 设置窗体标题
		this.setModal(true); // 设置窗体模态显示
		this.pd = pd;
		this.prjCodes = prjCodes;
		doQuery();
//		this.divCodes = divCodes;
	}

	private FPanel getBasePanel() {
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(12);
		pnl.setLayout(lay);
		FToolBar toolbar = new FToolBar();
		FButton btnClose = new FButton("", "关闭");
		btnClose.setIcon("images/rp/close.gif");
		btnClose.setVerticalTextPosition(SwingConstants.BOTTOM);
		toolbar.addControl(btnClose);
		FButton btnExp = new FButton("", "导为EXCEL");
		btnExp.setIcon("images/rp/excel.gif");
		btnExp.setVerticalTextPosition(SwingConstants.BOTTOM);
		toolbar.addControl(btnExp);
		pnl.addControl(toolbar, new TableConstraints(2, 12, true, true));

//		ta = new FTextArea();
////		FLabel tfs = new FLabel();
////		tfs.setText("  审核意见");
////		ta.setTitle("  审核意见：");
//		ta.setProportion(0f);
//		ta.setAutoscrolls(true);
//		ta.setLayoutHgap(0);
//		ta.setEditable(false);
		
//		FPanel pnlRB = new FPanel();
//		RowPreferedLayout layRB = new RowPreferedLayout(12);
//		pnlRB.setLayout(layRB);
//		pnlRB.addControl(tfs, new TableConstraints(1, 4, false, true));
//		pnlRB.addControl(ta, new TableConstraints(11, 12, false, true));
//		pnlRB.setLeftInset(leftInset)
		pnl.setLeftInset(10);
		pnl.setBottomInset(10);
		pnl.addControl(getRTPanel(), new TableConstraints(10, 12, true, true));
//		pnl.addControl(tfs, new TableConstraints(1, 4, false, true));
		pnl.addControl(ta, new TableConstraints(12, 12, false, true));
		pnl.setRightInset(10);
		pnl.setBottomInset(10);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnExp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				List list = new ArrayList();
//				if (tableAll.getDataSet() != null) {
//					try {
//						tableAll.getDataSet().beforeFirst();
//						while (tableAll.getDataSet().next()) {
//							list.add(tableAll.getDataSet().getOriginData());
//						}
//						// String[] cnames = new String[] { "单位编码", "单位名称",
//						// "项目编码", "项目名称", "当前状态", "是否退回", "审核时间", "审核意见",
//						// "审核人" };
//						// String[] enames = new String[] {
//						// IPrjAppr.AuditNewInfo.DIV_CODE,
//						// IPrjAppr.AuditNewInfo.DIV_NAME,
//						// IPrjAppr.AuditNewInfo.PRJ_CODE,
//						// IPrjAppr.AuditNewInfo.PRJ_NAME,
//						// IPrjAppr.AuditNewInfo.STATE_NAME,
//						// IPrjAppr.AuditNewInfo.ISBACK,
//						// IPrjAppr.AuditNewInfo.LAST_VER,
//						// IPrjAppr.AuditNewInfo.AUDIT_CONTEXT,
//						// IPrjAppr.AuditNewInfo.AUDIT_USER };
//
//						String[] cnames = new String[] { "单位编码", "单位名称",
//								"项目编码", "项目名称", "当前状态", "是否退回", "审核时间", "审核意见" };
//						String[] enames = new String[] {
//								IPrjAudit.AuditNewInfo.DIV_CODE,
//								IPrjAudit.AuditNewInfo.DIV_NAME,
//								IPrjAudit.AuditNewInfo.PRJ_CODE,
//								IPrjAudit.AuditNewInfo.PRJ_NAME,
//								IPrjAudit.AuditNewInfo.AudtiStep.WNAME,
//								IPrjAudit.AuditNewInfo.ISBACK,
//								IPrjAudit.AuditNewInfo.LAST_VER,
//								IPrjAudit.AuditNewInfo.AuditCurPrjState.AUDITCONTEXT };
//						DataSet dsHeader = DataSet.createClient();
//						for (int i = 0; i < enames.length; i++) {
//							dsHeader.append();
//							dsHeader.fieldByName("field_id")
//									.setValue("000" + i);
//							dsHeader.fieldByName("field_ename").setValue(
//									enames[i]);
//							dsHeader.fieldByName("field_cname").setValue(
//									cnames[i]);
//						}
//						dsHeader.applyUpdate();
//						HierarchyListGenerator hg = HierarchyListGenerator
//								.getInstance();
//						Node node = hg.generate(dsHeader, "field_id",
//								"field_cname",
//								SysCodeRule.createClient(new int[] { 4, 6, 8,
//										10 }), "field_id");
//						Exportpp pp = new Exportpp();
//						ReportType ret = (ReportType) ReportType
//								.create(ReportType.DsLstWithoutSet);
//						ret.setParam("jsjxpj", node, dsHeader, list,
//								"field_ename");
//						ExportToExcel eep = new ExportToExcel(
//								new int[] { ReportType.DsLstWithoutSet },
//								new ReportType[] { ret }, pp, true, false, null);
//						eep.doExport();
//						if (!eep.getIsCancel())
//							JOptionPane.showMessageDialog(Global.mainFrame,
//									"导出成功");
//					} catch (Exception ee) {
//						ErrorInfo.showErrorDialog(ee, "导出失败");
//					}
//				}
			}
		});
		return pnl;
	}

	private FPanel getRTPanel() {
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(16);
		pnl.setLayout(lay);
		try {
			String[] columnText = new String[] { "单位编码", "单位名称", "项目编码",
					"项目名称", "当前状态", "是否退回", "审核时间", "审核意见","审核人" };
			String[] columnField = new String[] {
					IPrjAudit.AuditNewInfo.DIV_CODE,
					IPrjAudit.AuditNewInfo.DIV_NAME,
					IPrjAudit.AuditNewInfo.PRJ_CODE,
					IPrjAudit.AuditNewInfo.PRJ_NAME, 
					"step_name",
					IPrjAudit.AuditNewInfo.ISBACK,
					"audit_time",
					"audit_text",
					"audit_username"};
			tableAll = new CustomTable(columnText, columnField, null, false,
					null);
			tableAll.reset();
//			pnl.addControl(getQueryType(), new TableConstraints(1, 5, false,
//					true));
			pnl.addControl(new FLabel(), new TableConstraints(1, 11, false,
					true));
			pnl.addControl(tableAll, new TableConstraints(10, 16, true, true));
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "生成审核信息表出错");
		}
		return pnl;
	}

	private void setTableAllProp() throws Exception {
		// tableAll.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableAll.reset();
		tableAll.getTable().getColumnModel().getColumn(1).setPreferredWidth(60);
		for (int i = 1; i < tableAll.getTable().getColumnCount(); i++) {
			switch (i) {
			case 0:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(60);
				break;
			case 1:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(150);
				break;
			case 2:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(120);
				break;
			case 3:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(200);
				break;
			case 4:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(150);
				break;
			case 5:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(60);
				break;
			case 6:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(120);
				break;
			case 7:
				tableAll.getTable().getColumnModel().getColumn(i)
						.setPreferredWidth(300);
				break;
			default:
				break;
			}
			tableAll.getTable().getTableHeader().setBackground(
					new Color(250, 228, 184));
			tableAll.getTable().getTableHeader().setPreferredSize(
					new Dimension(0, 30));
			tableAll.getTable().setRowHeight(25);
			tableAll.getDataSet().addDataChangeListener(new DataChangeListener(){

				private static final long serialVersionUID = 1L;

				public void onDataChange(DataChangeEvent event)
						throws Exception {
					DataSet ds = tableAll.getDataSet();
					if ((ds!=null) && !ds.isEmpty() && !ds.bof() && !ds.eof()){
						ta.setValue(ds.fieldByName("audit_text").getString());
					} else {
						ta.setValue("");
					}
				}
			});

		}
	}

//	private CustomComboBox getQueryType() throws Exception {
//		DataSet ds = DataSet.createClient();
//		ds.append();
//		ds.fieldByName("id").setValue("1");
//		ds.fieldByName("name").setValue("全部");
//		ds.append();
//		ds.fieldByName("id").setValue("2");
//		ds.fieldByName("name").setValue("未审核");
//		ds.append();
//		ds.fieldByName("id").setValue("3");
//		ds.fieldByName("name").setValue("已审核");
//		ds.append();
//		ds.fieldByName("id").setValue("4");
//		ds.fieldByName("name").setValue("已退回");
//		cbQueryType = new CustomComboBox(ds, "id", "name");
//		cbQueryType.setTitle("  请选择数据类型：");
//		cbQueryType.setProportion(0.45f);
//		cbQueryType.addValueChangeListener(new ValueChangeListener() {
//			public void valueChanged(ValueChangeEvent arg0) {
//				// TODO Auto-generated method stub
//					doQuery();
//			}
//		});
//		return cbQueryType;
//	}

	public void doQuery() {
		// TODO 查询
		try {
//			String filter_P = " and "+pd.getArraySqlFilter(IPrjAudit.PrjAuditTable.prj_code, prjCodes);
//			switch (this.cbQueryType.getSelectedIndex()) {
//			case 0:
//				// 全部
//				this.dsAuditAll = PrjAuditStub.getMethod().getAuditInfo(1,
//						null, null, Global.loginYear, pd.getUserCode(),
//						pd.getDepCode(), pd.getBathcNo(), 0, filter_P);
//				break;
//			case 1:
//				// 未审核
//				StringBuffer filter = new StringBuffer();
//				filter.append("and ");
//				filter.append(" isnull(audit_isend, 0) = 0 ");
//				this.dsAuditAll = PrjAuditStub.getMethod().getAuditInfo(1,
//						null, null, Global.loginYear, pd.getUserCode(),
//						pd.getDepCode(), pd.getBathcNo(), 0, filter.toString()+filter_P);
//				break;
//			case 2:
//				// 已审核
//				StringBuffer filter1 = new StringBuffer();
//				filter1.append("and ");
//				filter1.append(" isnull(audit_isend, 0) = 1 ");
//				this.dsAuditAll = PrjAuditStub.getMethod()
//						.getAuditInfo(1, null, null, Global.loginYear,
//								pd.getUserCode(), pd.getDepCode(),
//								pd.getBathcNo(), 0, filter1.toString()+filter_P);
//				break;
//			case 3:
//				// 已退回
//				StringBuffer filter11 = new StringBuffer();
//				filter11.append(" and is_back = '是'");
//				this.dsAuditAll = PrjAuditStub.getMethod().getAuditInfo(1,
//						null, null, Global.loginYear, pd.getUserCode(),
//						pd.getDepCode(), pd.getBathcNo(), 0,
//						filter11.toString()+filter_P);
//				break;
//			default:
//				this.dsAuditAll = PrjAuditStub.getMethod().getAuditInfo(1,
//						null, null, Global.loginYear, pd.getUserCode(),
//						pd.getDepCode(), pd.getBathcNo(), 0, filter_P);
//				break;
//			}
//			this.dsAuditAll = PrjAuditStub.getMethod().getHistoryInfo();
//			tableAll.setDataSet(dsAuditAll);
			setTableAllProp();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "查询出错");
		}
	}

//	private class Exportpp implements ExportBatchProp {
//
//		public String[] getFieldTName() {
//			return null;
//		}
//
//		public String[] getFieldFName() {
//			return null;
//		}
//
//		public String[] getFieldWName() {
//			return null;
//		}
//
//		public String[] getTitles() {
//			return new String[] { "绩效评价审核过程记录" };
//		}
//
//		public List getTitle_Childs() {
//			return null;
//		}
//
//		public String[] getNumberViewInTable() {
//			return new String[] { "字符型" };
//		}
//
//		public String getDefaultAddres() {
//
//			return null;
//		}
//
//		public List getSqls_BeforeGetBody() {
//
//			return null;
//		}
//
//		public List getSqls_AfterGetBody() {
//
//			return null;
//		}
//
//		public DataSet[] getFaceData() {
//			return null;
//		}
//
//		public List getWidths() {
//			List list = new ArrayList();
//			float[] widths = new float[] { 50, 150, 100, 200, 60, 30, 70, 300,
//					50 };
//			list.add(widths);
//			return list;
//		}
//
//		public List getFormats() {
//
//			return null;
//		}
//
//		public List getTypes() {
//			return null;
//		}
//	}

}
