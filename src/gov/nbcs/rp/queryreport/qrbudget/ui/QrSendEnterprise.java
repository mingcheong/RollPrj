package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.Color;
import java.awt.Font;
import java.text.Format;
import java.util.Iterator;

import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.list.CustomComboBox;
import gov.nbcs.rp.common.ui.list.MyListElement;
import gov.nbcs.rp.common.ui.report.HeaderUtility;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.fr.base.Constants;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.cell.editor.CellEditor;
import com.fr.report.CellElement;
import com.fr.report.PaperSize;

public class QrSendEnterprise extends FModulePanel implements PropertyProvider,
		ActionedUI {

	private static final long serialVersionUID = 1L;

	CustomComboBox cmbState;

	// 原始报表
	Report originReport;

	ReportUI originReportUI;

	// 表头列
	DataSet dsReportHeader;

	// 表体内容
	DataSet dsReportBody;

	public void initize() {
		try {

			FPanel basePanel = new FPanel();
			basePanel.setLayout(new RowPreferedLayout(1));
			basePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					javax.swing.BorderFactory.createLineBorder(
							java.awt.SystemColor.inactiveCaption, 1), "单位上报情况",
					javax.swing.border.TitledBorder.LEADING,
					javax.swing.border.TitledBorder.DEFAULT_POSITION,
					new java.awt.Font("宋体", java.awt.Font.PLAIN, 12),
					java.awt.SystemColor.inactiveCaption));
			this.add(basePanel);
			// 上面板
			FPanel statePanel = new FPanel();
			statePanel.setLayout(new RowPreferedLayout(7));

			// 单位状态
			DataSet ds = QrBudgetI.getMethod().getCondition();
			cmbState = new CustomComboBox(ds, "code", "name");
			cmbState.setTitle("状态");
			cmbState.setProportion(0.3f);

			cmbState.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent arg0) {
					QrEnterprisePrgBar qrsend = new QrEnterprisePrgBar(
							QrSendEnterprise.this);
					qrsend.display();
				}
			});
			statePanel.addControl(new FPanel(), new TableConstraints(1, 1,
					false, true));
			statePanel.add(cmbState, new TableConstraints(1, 1, false, true));
			basePanel.addControl(statePanel, new TableConstraints(1, 1, true,
					true));
			// 下面板
			TableHeader tableHeader = null;
			DataSet dsValue = null;
			PropertyProvider pp = this;
			originReport = new Report(tableHeader, dsValue, pp);
			originReportUI = new ReportUI(originReport);
			// 去除分页虚线,设置一个比较大的页
			originReportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			originReport.maskZeroValue(true);
			originReportUI.fireReportDataChanged();
			originReportUI.clearColumnHeaderContent();
			ViewReportHeader();
			FPanel centerBottomPnl = new FPanel(); // 面板centerBottomPnl
			centerBottomPnl.setLayout(new RowPreferedLayout(13));
			centerBottomPnl.setBorder(javax.swing.BorderFactory
					.createTitledBorder(javax.swing.BorderFactory
							.createLineBorder(
									java.awt.SystemColor.inactiveCaption, 1),
							"", javax.swing.border.TitledBorder.LEADING,
							javax.swing.border.TitledBorder.DEFAULT_POSITION,
							new java.awt.Font("宋体", java.awt.Font.PLAIN, 12),
							java.awt.SystemColor.inactiveCaption));
			centerBottomPnl.add(originReportUI, new TableConstraints(1, 13,
					true, true));
			basePanel.addControl(centerBottomPnl, new TableConstraints(20, 1,
					true, true));

			this.createToolBar();
			this.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void ViewReportHeader() {
		originReportUI.getReport().removeAllCellElements();
		try {
			// 表头
			dsReportHeader = QrBudgetI.getMethod().getEnterpriseReportHeader();
			if (dsReportHeader == null)
				return;
			TableHeader tableHeader = HeaderUtility.createHeader(
					dsReportHeader, "code", "name", "", "id");
			originReport.setReportHeader(tableHeader);
			// 设置不可编辑的单元格颜色
			// originReport.setReadOnlyBackground(new Color(0xe8f2fe));
			originReport.maskZeroValue(true);
			originReportUI.setFrozenRow(tableHeader.getRows());
			// 设置单元格格式
			Style style = Style.getInstance();
			style.deriveHorizontalAlignment(Constants.CENTER);
			style.deriveVerticalAlignment(Constants.CENTER);
			style.deriveBackground(ColorBackground.getInstance(new Color(250,
					228, 184)));
			style.deriveFRFont(FRFont
					.getInstance(new Font("宋体", Font.PLAIN, 12)));
			style.deriveBorderBottom(1, Color.LIGHT_GRAY);
			style.deriveBorderLeft(1, Color.LIGHT_GRAY);
			style.deriveBorderRight(1, Color.LIGHT_GRAY);
			style.deriveBorderTop(1, Color.LIGHT_GRAY);
			style.deriveTextStyle(Style.TextStyle_WrapText);

			CellElement cell = null;
			int idx = 0;
			Iterator cellIterator = originReport.cellIterator();
			while (cellIterator.hasNext()
					&& idx < dsReportHeader.getRecordCount()) {
				cell = (CellElement) cellIterator.next();
				cell.setStyle(style);
				idx++;
			}
			originReportUI.repaint();
			originReportUI.updateUI();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void ViewEnterprise() {
		originReportUI.getReport().removeAllCellElements();
		originReportUI.repaint();
		MyListElement name = (MyListElement) cmbState.getValue();
		String unitState = name.getId();
		try {
			// 表头
			dsReportHeader = QrBudgetI.getMethod().getEnterpriseReportHeader();
			if (dsReportHeader == null)
				return;
			TableHeader tableHeader = HeaderUtility.createHeader(
					dsReportHeader, "code", "name", "", "id");
			originReport.setReportHeader(tableHeader);
			// 设置不可编辑的单元格颜色
			// originReport.setReadOnlyBackground(new Color(0xe8f2fe));
			originReport.maskZeroValue(true);
			originReportUI.setFrozenRow(tableHeader.getRows());
			// 设置单元格格式
			Style style = Style.getInstance();
			style.deriveHorizontalAlignment(Constants.CENTER);
			style.deriveVerticalAlignment(Constants.CENTER);
			style.deriveBackground(ColorBackground.getInstance(new Color(250,
					228, 184)));
			style.deriveFRFont(FRFont
					.getInstance(new Font("宋体", Font.PLAIN, 12)));
			style.deriveBorderBottom(1, Color.LIGHT_GRAY);
			style.deriveBorderLeft(1, Color.LIGHT_GRAY);
			style.deriveBorderRight(1, Color.LIGHT_GRAY);
			style.deriveBorderTop(1, Color.LIGHT_GRAY);
			style.deriveTextStyle(Style.TextStyle_WrapText);

			CellElement cell = null;
			int idx = 0;
			Iterator cellIterator = originReport.cellIterator();
			while (cellIterator.hasNext()
					&& idx < dsReportHeader.getRecordCount()) {
				cell = (CellElement) cellIterator.next();
				cell.setStyle(style);
				idx++;
			}

			// 表体
			dsReportBody = QrBudgetI.getMethod().getSendEnterprise(unitState);

			originReport.setBodyData(dsReportBody);
			originReport.refreshBody();
			originReport.autoAdjustColumnWidth();
			// originReport.getUI().validate();
			originReport.setCellProperty(this);
			originReportUI.setReport(originReport);
			originReportUI.repaint();
			// originReportUI.updateUI();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void doAdd() {
		// TODO Auto-generated method stub

	}

	public void doDelete() {
		// TODO Auto-generated method stub

	}

	public void doCancel() {
		// TODO Auto-generated method stub

	}

	public void doInsert() {
		// TODO Auto-generated method stub

	}

	public void doModify() {
		// TODO Auto-generated method stub

	}

	public void doSave() {
		// TODO Auto-generated method stub

	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	public boolean isEditable(String bookmark, Object fieldId) {
		return false;
	}

	public CellEditor getEditor(String bookmark, Object fieldId) {
		return null;
	}

	public double getColumnWidth(Object fieldId) {
		return 0;
	}

	public Format getFormat(String bookmark, Object fieldId) {
		return null;
	}

	public String getFieldName(Object fieldId) {
		return fieldId.toString();
	}

	public Object getFieldId(String fieldName) {
		return fieldName.toString();
	}

	public String getUnitState() {
		MyListElement name = (MyListElement) cmbState.getValue();
		String unitState = name.getId();
		return unitState;
	}

	public ReportUI getReport() {
		return this.originReportUI;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
