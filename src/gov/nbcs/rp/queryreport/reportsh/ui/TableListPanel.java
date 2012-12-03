///**
// * @# TableListPanel.java    <�ļ���>
// */
//package gov.nbcs.rp.queryreport.reportsh.ui;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.text.DecimalFormat;
//import java.text.Format;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//
//import gov.nbcs.rp.common.Common;
//import gov.nbcs.rp.common.ErrorInfo;
//import gov.nbcs.rp.common.UntPub;
//import gov.nbcs.rp.common.datactrl.DataSet;
//import gov.nbcs.rp.common.export.action.ExportProp;
//import gov.nbcs.rp.common.export.action.ExportToExcel;
//import gov.nbcs.rp.common.print.PrintUtility;
//import gov.nbcs.rp.common.tree.HierarchyListGenerator;
//import gov.nbcs.rp.common.tree.Node;
//import gov.nbcs.rp.common.ui.progress.ProgressBar;
//import gov.nbcs.rp.common.ui.report.HeaderUtility;
//import gov.nbcs.rp.common.ui.report.Report;
//import gov.nbcs.rp.common.ui.report.TableHeader;
//import gov.nbcs.rp.common.ui.report.cell.Cell;
//import gov.nbcs.rp.common.ui.report.cell.PropertyProviderEx;
//import com.foundercy.pf.control.FPanel;
//import com.foundercy.pf.control.MessageBox;
//import com.foundercy.pf.util.Global;
//import com.fr.base.Constants;
//import com.fr.base.Style;
//import com.fr.base.background.ColorBackground;
//import com.fr.cell.editor.CellEditor;
//
///**
// * ����˵��:��ʾ��������б�
// *<P> Copyright 
// * <P>All rights reserved.
//
// */
//public class TableListPanel extends FPanel implements PropertyProviderEx {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	private FFilterTable tblData;//���
//
//	private List lstData;
//
//	private DataSet dsHeader;
//	
//	private int batchNo;
//	private int dataType;
//
//	public TableListPanel() {
//		this.setLayout(new BorderLayout());
//		tblData = new FFilterTable();
//		this.add(tblData, BorderLayout.CENTER);
//	}
//
//	public void refreshData(DataSet dsHeader, List lstData) {
//		this.dsHeader = dsHeader;
//		this.lstData = lstData;
//		this.remove(tblData);
//		if (dsHeader == null)
//			return;
//		try {
//			tblData = BaseUtil.createTable(dsHeader, IBasInputTable.LVL_ID,
//					IBasInputTable.FIELD_ENAME, IBasInputTable.FIELD_CNAME,
//					IBasInputTable.IS_LEAF, IBasInputTable.FIELD_TYPE,
//					IBasInputTable.FIELD_COLUMN_WIDTH,
//					IBasInputTable.FIELD_DISFORMAT,
//					IBasInputTable.IS_COLNEEDSUM, UntPub.lvlRule);
//			tblData.setData(lstData);
//			this.add(tblData, BorderLayout.CENTER);
//			
//			tblData.addMouseListener(new MouseAdapter(){
//
//				public void mouseClicked(MouseEvent e) {
//					if (e.getClickCount() == 2) {
//						viewPrjInfo();
//					}
//				}
//				
//			});
//		} catch (Exception e) {
//			new MessageBox("�������ʧ��!", e.getMessage(), MessageBox.ERROR,
//					MessageBox.BUTTON_OK).show();
//			e.printStackTrace();
//		}
//		this.validate();
//		this.repaint();
//	}
//
//	public void doExport() {
//		List lstData = tblData.getData();
//		if (lstData == null || lstData.isEmpty()) {
//			MessageBox mb = new MessageBox("û�����ݿ��Ե���!", MessageBox.INFOMATION,
//					MessageBox.BUTTON_OK);
//			mb.setVisible(true);
//			mb.dispose();
//			return;
//		}
//		try {
//			HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
//
//			Node aNode = hg.generate(dsHeader, IBasInputTable.LVL_ID,
//					IBasInputTable.FIELD_CNAME, UntPub.lvlRule,
//					IBasInputTable.LVL_ID);
//			Exportpp pp = new Exportpp();
//			pp.setTitle("Ԥ�����ݲ�ѯ��");
//			ExportToExcel ee = new ExportToExcel(aNode, dsHeader, lstData,
//					IBasInputTable.FIELD_ENAME, pp);
//			ee.doExport();
//		} catch (Exception e1) {
//			new MessageBox("����ʧ��!", e1.getMessage(), MessageBox.ERROR,
//					MessageBox.BUTTON_OK).show();
//			e1.printStackTrace();
//		}
//	}
//
//	public void doPrint() {
//		try {
//			TableHeader header = HeaderUtility.createHeader(dsHeader,
//					IBasInputTable.LVL_ID, IBasInputTable.FIELD_CNAME,
//					UntPub.lvlRule, IBasInputTable.LVL_ID);
//
//			DataSet dsData = DataSet.create();
//			if (lstData != null && !lstData.isEmpty()) {
//				int iCount = lstData.size();
//				for (int i = 0; i < iCount; i++) {
//					dsData.append();
//					dsData.setOriginData((Map) lstData.get(i));
//				}
//			}
//
//			Report report = new Report(header, dsData, this);
//			report.setPrinting(true);
//
//			PrintUtility.printPreview(null, report, "budgetQuery", Global
//					.getSetYear(), new String[] { "", "��Ԫ" });
//
//		} catch (Exception e) {
//			new MessageBox("��ӡʧ��!", e.getMessage(), MessageBox.ERROR,
//					MessageBox.BUTTON_OK).show();
//			e.printStackTrace();
//		}
//
//	}
//
//	private class Exportpp implements ExportProp {
//
//		String title = "";
//
//		public String getFieldTName() {
//			return IBasInputTable.FIELD_TYPE;
//		}
//
//		public String getFieldFName() {
//			return null;
//		}
//
//		public String getFieldWName() {
//
//			return IBasInputTable.FIELD_COLUMN_WIDTH;
//		}
//
//		public String[] getNumberViewInTable() {
//
//			return new String[] { "����", "����", "������", "����" };
//		}
//
//		public String getTitle() {
//			return title;
//		}
//
//		public String[] getTitle_Child() {
//			// TODO Auto-generated method stub
//			return new String[] { "", "��λ��Ԫ" };
//		}
//
//		public String getDefaultAddres() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public List getSqls_BeforeGetBody() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public List getSqls_AfterGetBody() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public DataSet[] getDsFace() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public float[] getWidths() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public String[] getFormats() {
//			String[] arr=new String[tblData.getColumnCount()];
//			int iCount=arr.length;
//			for (int i = 0; i < iCount; i++) {
//				arr[i]="#,###.00";
//			}
//			return arr;
//		}
//
//		public String[] getTypes() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public void setTitle(String title) {
//			this.title = title;
//		}
//	}
//
//	public Style getCellStyle(Cell cell) {
//		try {
//			if (dsHeader.locate("field_ename", cell.getFieldName())) {
//				{
//					if ("������".equals(dsHeader.fieldByName(
//							IBasInputTable.FIELD_TYPE).getString())) {
//						Style style = cell.getStyle()
//								.deriveHorizontalAlignment(Constants.RIGHT);
//						return style.deriveBackground(ColorBackground
//								.getInstance(Color.white));
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public double getColumnWidth(Object fieldId) {
//		try {
//			if (dsHeader.locate("lvl_id", fieldId))
//				return dsHeader.fieldByName("field_column_width").getInteger();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0;
//	}
//
//	public CellEditor getEditor(String bookmark, Object fieldId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Object getFieldId(String fieldName) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getFieldName(Object fieldId) {
//		try {
//			if (dsHeader.locate("lvl_id", fieldId))
//				return dsHeader.fieldByName("field_ename").getString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public Format getFormat(String bookmark, Object fieldId) {
//		try {
//			if (dsHeader.locate("lvl_id", fieldId)) {
//				if ("������".equals(dsHeader
//						.fieldByName(IBasInputTable.FIELD_TYPE).getString())) {
//					return new DecimalFormat("#,###.##");
//				}
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public boolean isEditable(String bookmark, Object fieldId) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public void setBatchNo(int batchNo) {
//		this.batchNo = batchNo;
//	}
//
//	public void setDataType(int dataType) {
//		this.dataType = dataType;
//	}
//
//	ProgressBar pgBar = ProgressBar.createRefreshing();
//	
//	/**
//	 * �鿴��Ŀ��Ϣ
//	 */
//	public void viewPrjInfo() {
//		List sel = tblData.getSelectedData();
//		if (sel == null || sel.size() == 0)
//			return;
//		
//		final String prjCode = (String) ((Map) sel.get(0)).get("prj_code");
//		// ������Ŀ������
//		if (Common.isNullStr(prjCode) || "�ܼ�".equals(prjCode)) {
//			// ErrorInfo.showMessageDialog("��ѡ�������Ŀ��");
//			return;
//		}
//
//		try {
//			// ������Ŀ���룬�ҵ������ĵ�λ����
//			final String prjDivCode ="";
//				//TODO ��ע����PrjSHStub.getMethod().getDivCodeByPrjCode(
//				//	prjCode, batchNo, dataType, GlobalEx.loginYear);
//			if (Common.isNullStr(prjDivCode))
//				return;
//			
//			new Thread() {
//
//				public void run() {
//					//TODO ��ע��
////					PrjAuditPayOutDlg prjAuditPayOutDlg;
////					try {
////						prjAuditPayOutDlg = new PrjAuditPayOutDlg(getPrjCodeList(),
////								prjCode, prjDivCode, batchNo, dataType, false, true, true);
////						prjAuditPayOutDlg.setTitle("��Ŀ��Ϣ");
////						Tools.centerWindow(prjAuditPayOutDlg);
////					} finally {
////						ProgressBar.hide(pgBar);
////					}
////					prjAuditPayOutDlg.show();
//				}
//				
//			}.start();
//			ProgressBar.show(pgBar);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void viewBudget() {
//		List sel = tblData.getSelectedData();
//		if (sel == null || sel.size() == 0)
//			return;
//		
//		final String divCode = (String) ((Map) sel.get(0)).get("div_code");
//		final String divName = (String) ((Map) sel.get(0)).get("div_name");
//		// ������Ŀ������
//		if (Common.isNullStr(divCode) || "�ܼ�".equals(divCode)) {
//			ErrorInfo.showMessageDialog("��ѡ�����Ԥ�㵥λ��");
//			return;
//		}
//		
//		new Thread() {
//
//			public void run() {
//				//TODO ��ע��
////				PayoutBudgetShowDialog payoutBudgetShowDialog;
////				try {
////					TableParameters parameters = new TableParameters();
////					parameters.setDivCode(divCode);
////					parameters.setDiv_name(divName);
////					parameters.setBatchNo(batchNo);
////					parameters.setDataType(dataType);
////					payoutBudgetShowDialog = new PayoutBudgetShowDialog(null, parameters, null);
////					Tools.centerWindow(payoutBudgetShowDialog);
////				} finally {
////					ProgressBar.hide(pgBar);
////				}
////				payoutBudgetShowDialog.setVisible(true);
//			}
//			
//		}.start();
//		ProgressBar.show(pgBar);
//	}
//	
//	private List getPrjCodeList() {
//		List ls = new ArrayList();
//		for (Iterator itr = tblData.getData().iterator(); itr.hasNext();) {
//			Map r = (Map) itr.next();
//			Object prjCode = r.get("prj_code");
//			if (!Common.isNullStr((String) prjCode)) {
//				if(!ls.contains(prjCode)) {
//					ls.add(prjCode);
//				}
//			}
//		}
//		return ls;
//	}
//
//	public void viewPayout() {
//		List sel = tblData.getSelectedData();
//		if (sel == null || sel.size() == 0)
//			return;
//
//		final String divCode = (String) ((Map) sel.get(0)).get("div_code");
//		final String divName = (String) ((Map) sel.get(0)).get("div_name");
//		// ������Ŀ������
//		if (Common.isNullStr(divCode) || "�ܼ�".equals(divCode)) {
//			ErrorInfo.showMessageDialog("��ѡ�����Ԥ�㵥λ��");
//			return;
//		}
//
//		new Thread() {
//
//			public void run() {
//				//TODO ��ע��
////				ShInputDataDialog shInputDataDialog;
////				try {
////					shInputDataDialog = new ShInputDataDialog(Global.mainFrame,
////							"Ԥ������ - " + divName, true, divCode);
////					Tools.centerWindow(shInputDataDialog);
////				} finally {
////					ProgressBar.hide(pgBar);
////				}
////				shInputDataDialog.setVisible(true);
////				shInputDataDialog = null;
//			}
//
//		}.start();
//		ProgressBar.show(pgBar);
//
//	}
//
//}
