package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.fr.cell.editor.CellEditor;
import com.fr.cell.editor.NumberCellEditor;
import com.fr.report.PaperSize;

/**
 * 省厅职务补贴维护
 *
 * @author qzc
 *
 */
public class JsPositionSub extends FModulePanel implements PropertyProvider {

	ReportUI reportUI;

	Report report;

	DataSet dsBody;

	public void initize() {
		try {
			FPanel fpnlMain = new FPanel();
			fpnlMain.setLayout(new RowPreferedLayout(1));
			this.add(fpnlMain);

			// 虚拟表头DataSet
			DataSet dsHeader = getHeaderDataSet();

			// 创建表头
			SysCodeRule codeRule = SysCodeRule.createClient(new int[] { 4 });
			HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
			Node node = hg.generate(dsHeader, "Code", "Name", codeRule, "Code");
			TableHeader tableHeader = new TableHeader(node);
			tableHeader.setFont(this.getFont());
			// 创建Report
			dsBody = SzzbSetI.getMethod().getJsPositionSub(Global.loginYear);
			report = new Report(tableHeader, dsBody, this);
			// 设表头颜色
			report.getReportHeader().setColor(UntPub.HEADER_COLOR);
			report.shrinkToFitRowHeight();
			// 创建ReportUI
			reportUI = new ReportUI(report);

			reportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			reportUI.setColumnEndless(false);
			reportUI.setRowEndless(false);
			reportUI.clearColumnHeaderContent();

			// 设置固定行和列
			reportUI.setFrozenRow(tableHeader.getRows());
			reportUI.setFrozenColumn(1);
			fpnlMain.add(reportUI, new TableConstraints(1, 1, true));

			this.createToolBar();
			FButton fbtnModify = new FButton("fbtnModify", "修改");
			fbtnModify.setIcon("images/fbudget/mod.gif");
			fbtnModify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						dsBody.edit();
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(JsPositionSub.this,
								"修改发生错误，错误信息:" + e.getMessage(), "提示",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			FButton fbtnSave = new FButton("fbtnOK", "保存");
			fbtnSave.setIcon("images/fbudget/save.gif");
			fbtnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						SzzbSetI.getMethod().saveJsPositionSub(dsBody);
						JOptionPane.showMessageDialog(JsPositionSub.this,
								"保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(JsPositionSub.this,
								"修改发生错误，错误信息:" + e.getMessage(), "提示",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// FButton fbtnExport = new FButton("fbtnExport",
			// "工作、生活性津贴补贴导入数据库");
			// fbtnExport.addActionListener(new ActionListener() {
			// public void actionPerformed(ActionEvent arg0) {
			// }
			// });
			//
			// FButton fbtnExportGGjf = new FButton("fbtnExportGGjf",
			// "公用经费导入数据库");
			// fbtnExport.addActionListener(new ActionListener() {
			// public void actionPerformed(ActionEvent arg0) {
			// }
			// });

			this.getToolbarPanel().addControl(fbtnModify);
			this.getToolbarPanel().addControl(fbtnSave);
			// this.getToolbarPanel().addControl(fbtnExport);
			// this.getToolbarPanel().addControl(fbtnExportGGjf);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "初始化界面发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean isEditable(String bookmark, Object fieldId) {
		if ("0002".equals(fieldId) || "0003".equals(fieldId)
				|| "0004".equals(fieldId) || "0005".equals(fieldId)) {
			return true;
		}
		return false;
	}

	public CellEditor getEditor(String bookmark, Object fieldId) {
		if ("0002".equals(fieldId) || "0003".equals(fieldId)
				|| "0004".equals(fieldId) || "0005".equals(fieldId)) {
			return new NumberCellEditor();
		}
		return null;
	}

	public double getColumnWidth(Object fieldId) {
		if ("0001".equals(fieldId)) {
			return 150;
		} else {
			return 80;
		}
	}

	public Format getFormat(String bookmark, Object fieldId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFieldName(Object fieldId) {
		if ("0001".equals(fieldId)) {
			return "JBQK_ZW";
		} else if ("0002".equals(fieldId)) {
			return "ZZ_GZBT";
		} else if ("0003".equals(fieldId)) {
			return "ZZ_SHBT";
		} else if ("0004".equals(fieldId)) {
			return "LX_SHBT";
		} else if ("0005".equals(fieldId)) {
			return "TX_SHBT";
		}
		return null;
	}

	public Object getFieldId(String fieldName) {
		if ("JBQK_ZW".equals(fieldName)) {
			return "0001";
		} else if ("ZZ_GZBT".equals(fieldName)) {
			return "0002";
		} else if ("ZZ_SHBT".equals(fieldName)) {
			return "0003";
		} else if ("LX_SHBT".equals(fieldName)) {
			return "0004";
		} else if ("TX_SHBT".equals(fieldName)) {
			return "0005";
		}
		return null;
	}

	/**
	 * 虚拟表头DataSet
	 *
	 * @return
	 * @throws Exception
	 */
	private DataSet getHeaderDataSet() throws Exception {
		DataSet dsHeader = DataSet.create();
		dsHeader.append();
		dsHeader.fieldByName("Code").setValue("0001");
		dsHeader.fieldByName("Name").setValue("职务");
		dsHeader.append();
		dsHeader.fieldByName("Code").setValue("0002");
		dsHeader.fieldByName("Name").setValue("在职工作性补贴(元)");
		dsHeader.append();
		dsHeader.fieldByName("Code").setValue("0003");
		dsHeader.fieldByName("Name").setValue("在职生活性补贴(元)");
		dsHeader.append();
		dsHeader.fieldByName("Code").setValue("0004");
		dsHeader.fieldByName("Name").setValue("离休生活性补贴(元)");
		dsHeader.append();
		dsHeader.fieldByName("Code").setValue("0005");
		dsHeader.fieldByName("Name").setValue("退休生活性补贴(元)");
		dsHeader.applyUpdate();
		return dsHeader;
	}

}
