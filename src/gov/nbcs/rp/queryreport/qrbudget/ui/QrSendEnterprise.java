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

	// ԭʼ����
	Report originReport;

	ReportUI originReportUI;

	// ��ͷ��
	DataSet dsReportHeader;

	// ��������
	DataSet dsReportBody;

	public void initize() {
		try {

			FPanel basePanel = new FPanel();
			basePanel.setLayout(new RowPreferedLayout(1));
			basePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					javax.swing.BorderFactory.createLineBorder(
							java.awt.SystemColor.inactiveCaption, 1), "��λ�ϱ����",
					javax.swing.border.TitledBorder.LEADING,
					javax.swing.border.TitledBorder.DEFAULT_POSITION,
					new java.awt.Font("����", java.awt.Font.PLAIN, 12),
					java.awt.SystemColor.inactiveCaption));
			this.add(basePanel);
			// �����
			FPanel statePanel = new FPanel();
			statePanel.setLayout(new RowPreferedLayout(7));

			// ��λ״̬
			DataSet ds = QrBudgetI.getMethod().getCondition();
			cmbState = new CustomComboBox(ds, "code", "name");
			cmbState.setTitle("״̬");
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
			// �����
			TableHeader tableHeader = null;
			DataSet dsValue = null;
			PropertyProvider pp = this;
			originReport = new Report(tableHeader, dsValue, pp);
			originReportUI = new ReportUI(originReport);
			// ȥ����ҳ����,����һ���Ƚϴ��ҳ
			originReportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			originReport.maskZeroValue(true);
			originReportUI.fireReportDataChanged();
			originReportUI.clearColumnHeaderContent();
			ViewReportHeader();
			FPanel centerBottomPnl = new FPanel(); // ���centerBottomPnl
			centerBottomPnl.setLayout(new RowPreferedLayout(13));
			centerBottomPnl.setBorder(javax.swing.BorderFactory
					.createTitledBorder(javax.swing.BorderFactory
							.createLineBorder(
									java.awt.SystemColor.inactiveCaption, 1),
							"", javax.swing.border.TitledBorder.LEADING,
							javax.swing.border.TitledBorder.DEFAULT_POSITION,
							new java.awt.Font("����", java.awt.Font.PLAIN, 12),
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
			// ��ͷ
			dsReportHeader = QrBudgetI.getMethod().getEnterpriseReportHeader();
			if (dsReportHeader == null)
				return;
			TableHeader tableHeader = HeaderUtility.createHeader(
					dsReportHeader, "code", "name", "", "id");
			originReport.setReportHeader(tableHeader);
			// ���ò��ɱ༭�ĵ�Ԫ����ɫ
			// originReport.setReadOnlyBackground(new Color(0xe8f2fe));
			originReport.maskZeroValue(true);
			originReportUI.setFrozenRow(tableHeader.getRows());
			// ���õ�Ԫ���ʽ
			Style style = Style.getInstance();
			style.deriveHorizontalAlignment(Constants.CENTER);
			style.deriveVerticalAlignment(Constants.CENTER);
			style.deriveBackground(ColorBackground.getInstance(new Color(250,
					228, 184)));
			style.deriveFRFont(FRFont
					.getInstance(new Font("����", Font.PLAIN, 12)));
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
			// ��ͷ
			dsReportHeader = QrBudgetI.getMethod().getEnterpriseReportHeader();
			if (dsReportHeader == null)
				return;
			TableHeader tableHeader = HeaderUtility.createHeader(
					dsReportHeader, "code", "name", "", "id");
			originReport.setReportHeader(tableHeader);
			// ���ò��ɱ༭�ĵ�Ԫ����ɫ
			// originReport.setReadOnlyBackground(new Color(0xe8f2fe));
			originReport.maskZeroValue(true);
			originReportUI.setFrozenRow(tableHeader.getRows());
			// ���õ�Ԫ���ʽ
			Style style = Style.getInstance();
			style.deriveHorizontalAlignment(Constants.CENTER);
			style.deriveVerticalAlignment(Constants.CENTER);
			style.deriveBackground(ColorBackground.getInstance(new Color(250,
					228, 184)));
			style.deriveFRFont(FRFont
					.getInstance(new Font("����", Font.PLAIN, 12)));
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

			// ����
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
