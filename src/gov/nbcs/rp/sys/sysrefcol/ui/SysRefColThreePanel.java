package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Types;
import java.text.Format;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.cell.editor.CellEditor;
import com.fr.report.PaperSize;

/**
 * 引用列列属性设置(第三步)
 *

 */
public class SysRefColThreePanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private int iOperationType;

	private DataSet dsRefCol;

	private DataSet dsRefColDetail;

	private DataSet dsRefColRelatable;

	private TableColumnInfo tableColumnInfo[];

	private ReportUI reportUI;

	private Report reportPriInfo;

	private FTextArea ftxtaSql;

	private DataSet dsBody = DataSet.create();

	FPanel fpnlFieldProperty;

	// 英文名
	private FTextField ftxtfFieldEName;

	// 列中文名
	private FTextField ftxtfFieldFName;

	// 分组号
	private JSpinner jspFieldGroup;

	// 关联源表字段
	private FTextField ftxtfRelaField;

	// 主键字段
	private FCheckBox fchkPrimaryField;

	// 代码显示列字段
	private FCheckBox fchkCodeField;

	// 名称显示列字段
	private FCheckBox fchkNameField;

	// 编辑层次码
	private FCheckBox fchkbxLvlField;

	private SysRefCodeRule sysRefCodeRule;

	private final String CHECK_FLAG = "√";

	private int iCurRow; // 保存当前显示的行号

	public SysRefColThreePanel() {
		try {
			this.setTopInset(3);
			this.setBottomInset(3);
			this.setLeftInset(3);
			this.setRightInset(3);
			RowPreferedLayout rLayout = new RowPreferedLayout(10);
			rLayout.setColumnGap(1);
			this.setLayout(rLayout);
			// 创建上半部分的表
			TableHeader tableHeader = setReportHeader();
			CellPropertyProvider cellPropertyProvider = new CellPropertyProvider();
			reportPriInfo = new Report(tableHeader, dsBody,
					cellPropertyProvider);
			reportPriInfo.shrinkToFitRowHeight();

			reportUI = new ReportUI(reportPriInfo);
			reportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			// 没有行选效果
			reportUI.setRowSelect(false);
			// 设置固定行和列
			reportUI.setFrozenRow(tableHeader.getRows());
			reportUI.setFrozenColumn(2);
			reportUI.getGrid().addMouseListener(new ReportUIMouseListener());
			reportUI.getGrid().addKeyListener(new ReportUIKeyListener());

			// 下半部分,字段属性FPanel
			fpnlFieldProperty = new FPanel();
			fpnlFieldProperty.setLayout(new RowPreferedLayout(8));
			fpnlFieldProperty.setTitle("字段属性");
			{
				// 放部分信息FPanel
				FPanel fpnlField = new FPanel();
				fpnlField.setLayout(new RowPreferedLayout(6));
				ftxtfFieldEName = new FTextField("英文名：");
				ftxtfFieldEName.setProportion(0.175f);
				ftxtfFieldEName.setEditable(false);
				ftxtfFieldFName = new FTextField("列中文名：");
				ftxtfFieldFName.setProportion(0.175f);
				ftxtfFieldFName.setEditable(false);
				// 分组号Label
				FLabel flblFieldGroup = new FLabel();
				flblFieldGroup.setText("分组号：");
				// 分组号JSpinner
				SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
				jspFieldGroup = new JSpinner(spinnerModel);
				jspFieldGroup.addChangeListener(new FieldGroupChangeListener());

				ftxtfRelaField = new FTextField("关联源表字段：");
				ftxtfRelaField.setProportion(0.34f);
				((JTextField) ftxtfRelaField.getEditor()).getDocument()
						.addDocumentListener(new RelaFieldDocumentListener());

				// 英文名
				fpnlField.addControl(ftxtfFieldEName, new TableConstraints(1,
						6, true, true));
				// 全名
				fpnlField.addControl(ftxtfFieldFName, new TableConstraints(1,
						6, true, true));
				// 分组号Label
				fpnlField.addControl(flblFieldGroup, new TableConstraints(1, 1,
						true, true));
				// 分组号JSpinner
				fpnlField.add(jspFieldGroup);// , new TableConstraints(1, 1,
				// true, true));
				fpnlField.addControl(ftxtfRelaField, new TableConstraints(1, 4,
						true, true));

				// 创建三个FCheckBox
				fchkPrimaryField = new FCheckBox("主键字段");
				fchkPrimaryField.setTitlePosition("RIGHT");
				fchkPrimaryField
						.addValueChangeListener(new PrimaryFieldValueChangeListener());
				fchkCodeField = new FCheckBox("代码显示列字段");
				fchkCodeField.setTitlePosition("RIGHT");
				fchkCodeField
						.addValueChangeListener(new CodeFieldValueChangeListener());
				fchkNameField = new FCheckBox("名称显示列字段");
				fchkNameField.setTitlePosition("RIGHT");
				fchkNameField
						.addValueChangeListener(new NameFieldValueChangeListener());

				// 层次码FPanel
				FPanel fpnlLvlStyle = new FPanel();
				RowPreferedLayout rLay = new RowPreferedLayout(1);
				rLay.setRowGap(1);
				fpnlLvlStyle.setLayout(rLay);
				fpnlLvlStyle.setTitle("层次码");
				fchkbxLvlField = new FCheckBox("编辑层次码");
				fchkbxLvlField.setTitlePosition("RIGHT");
				fchkbxLvlField
						.addValueChangeListener(new LvlFieldValueChangeListener());
				sysRefCodeRule = new SysRefCodeRule();

				((JTextField) sysRefCodeRule.getFtxtCodeRule().getEditor())
						.getDocument().addDocumentListener(
								new CodeRuleDocumentListener());

				fpnlLvlStyle.addControl(fchkbxLvlField, new TableConstraints(1,
						1, false, true));
				fpnlLvlStyle.addControl(sysRefCodeRule, new TableConstraints(1,
						1, true, true));

				// 放部分信息fpnlField
				fpnlFieldProperty.addControl(fpnlField, new TableConstraints(3,
						6, false, true));
				// 三个FCheckBox
				fpnlFieldProperty.addControl(fchkPrimaryField,
						new TableConstraints(1, 2, false, true));
				fpnlFieldProperty.addControl(fchkCodeField,
						new TableConstraints(1, 2, false, true));
				fpnlFieldProperty.addControl(fchkNameField,
						new TableConstraints(1, 2, false, true));
				// 层次码FPanel
				fpnlFieldProperty.addControl(fpnlLvlStyle,
						new TableConstraints(4, 8, false, true));
			}

			// 参考查询语句
			FPanel fpnlSql = new FPanel();
			fpnlSql.setLayout(new RowPreferedLayout(1));
			fpnlSql.setTitle("参考查询语句");
			ftxtaSql = new FTextArea();
			ftxtaSql.setTitleVisible(false);
			ftxtaSql.setEditable(false);
			ftxtaSql.setBorder(null);
			fpnlSql
					.addControl(ftxtaSql,
							new TableConstraints(1, 1, true, true));

			this.add(reportUI, new TableConstraints(1, 10, true, true));
			this.addControl(fpnlFieldProperty, new TableConstraints(8, 7,
					false, true));
			this.addControl(fpnlSql, new TableConstraints(8, 3, false, true));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "引用列列属性信息界面初始化发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 虚拟表头DataSet
	 *
	 * @return
	 * @throws Exception
	 */
	private DataSet getHeaderDataSet() throws Exception {
		DataSet ds = DataSet.create();
		ds.append();
		ds.fieldByName("code").setValue("01");
		ds.fieldByName("name").setValue("列");
		ds.fieldByName("par_id").setValue("");
		ds.append();
		ds.fieldByName("code").setValue("0101");
		ds.fieldByName("name").setValue("英文名");
		ds.fieldByName("par_id").setValue("01");
		ds.append();
		ds.fieldByName("code").setValue("0102");
		ds.fieldByName("name").setValue("列中文名");
		ds.fieldByName("par_id").setValue("01");
		ds.append();
		ds.fieldByName("code").setValue("0103");
		ds.fieldByName("name").setValue("分组号");
		ds.fieldByName("par_id").setValue("01");
		ds.append();
		ds.fieldByName("code").setValue("02");
		ds.fieldByName("name").setValue("特殊列标识");
		ds.fieldByName("par_id").setValue("");
		ds.append();
		ds.fieldByName("code").setValue("0201");
		ds.fieldByName("name").setValue("主键");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("0202");
		ds.fieldByName("name").setValue("代码显示列");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("0203");
		ds.fieldByName("name").setValue("名称显示列");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("0204");
		ds.fieldByName("name").setValue("层次码");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("020401");
		ds.fieldByName("name").setValue("标识");
		ds.fieldByName("par_id").setValue("0204");
		ds.append();
		ds.fieldByName("code").setValue("020402");
		ds.fieldByName("name").setValue("风格");
		ds.fieldByName("par_id").setValue("0204");
		ds.append();
		ds.fieldByName("code").setValue("03");
		ds.fieldByName("name").setValue("关联源表字段");
		ds.fieldByName("par_id").setValue("");
		ds.applyUpdate();
		return ds;
	}

	/**
	 * 显示报表信息
	 *
	 * @throws Exception
	 */
	private TableHeader setReportHeader() throws Exception {
		DataSet dsHeader = getHeaderDataSet();
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsHeader, "code", "name", "par_id", "code");
		TableHeader tableHeader = new TableHeader(node);
		tableHeader.setFont(this.getFont());
		return tableHeader;
	}

	private void setReportShowStyle() {
		// 设表头列宽
		reportPriInfo.getReportHeader().setColor(UntPub.HEADER_COLOR);
		// 设置列宽
		reportPriInfo.setColumnWidth(0, 110);
		reportPriInfo.setColumnWidth(1, 110);
		reportPriInfo.setColumnWidth(2, 50);
		reportPriInfo.setColumnWidth(3, 40);
		reportPriInfo.setColumnWidth(4, 40);
		reportPriInfo.setColumnWidth(5, 40);
		reportPriInfo.setColumnWidth(6, 40);
		reportPriInfo.setColumnWidth(7, 110);
		reportPriInfo.setColumnWidth(8, 200);
	}

	/**
	 * 显示记录信息,根据Dataset内容
	 *
	 * @throws Exception
	 *
	 */
	public void showInfo(DataSet dsRefCol, DataSet dsRefColDetail,
			DataSet dsRefColRelatable, int iOperationType) throws Exception {
		this.iOperationType = iOperationType;
		this.dsRefCol = dsRefCol;
		this.dsRefColDetail = dsRefColDetail;
		this.dsRefColRelatable = dsRefColRelatable;
		ftxtaSql.setValue(dsRefCol.fieldByName("SQL_DET").getString());
		setReport();
		setReportShowStyle();
		gotoFirstRecord();
	}

	/**
	 * 设置report
	 *
	 * @throws Exception
	 *
	 */
	private void setReport() throws Exception {
		dsBody.clearAll();
		for (int i = 0; i < tableColumnInfo.length; i++) {
			String sEname = tableColumnInfo[i].getColumnName().toString();
			dsBody.append();
			dsBody.fieldByName("0101").setValue(sEname);
			if (dsRefColDetail.locate("field_ename", sEname)) {
				String sFieldId = dsRefColDetail.fieldByName("field_id")
						.getString();
				dsBody.fieldByName("0102").setValue(
						dsRefColDetail.fieldByName("field_cname").getString());
				if (dsRefColDetail.fieldByName("field_group").getValue() == null)
					dsBody.fieldByName("0103").setValue("");
				else
					dsBody.fieldByName("0103").setValue(
							dsRefColDetail.fieldByName("field_group")
									.getString());

				// 设代码显示列
				if (dsRefCol.fieldByName("CODE_FIELD").getValue() != null
						&& dsRefCol.fieldByName("CODE_FIELD").getString()
								.equals(sEname)) {
					dsBody.fieldByName("0202").setValue(CHECK_FLAG);
				} else {
					dsBody.fieldByName("0202").setValue("");
				}
				// 设名称显示列
				if (dsRefCol.fieldByName("NAME_FIELD") != null
						&& dsRefCol.fieldByName("NAME_FIELD").getString()
								.equals(sEname)) {
					dsBody.fieldByName("0203").setValue(CHECK_FLAG);
				} else {
					dsBody.fieldByName("0203").setValue("");
				}
				// 设层次码
				if (dsRefCol.fieldByName("LVL_FIELD").getValue() != null
						&& dsRefCol.fieldByName("LVL_FIELD").getString()
								.equals(sEname)) {
					dsBody.fieldByName("020401").setValue(CHECK_FLAG);
					dsBody.fieldByName("020402").setValue(
							dsRefCol.fieldByName("LVL_STYLE").getString());
				} else {
					dsBody.fieldByName("020401").setValue("");
					dsBody.fieldByName("020402").setValue("");
				}
				if (dsRefColRelatable.locate("field_id", sFieldId)) {
					String sRelaField = dsRefColRelatable.fieldByName(
							"RELATION_TABLE").getString()
							+ "."
							+ dsRefColRelatable.fieldByName("RELATION_FIELD")
									.getString();
					dsBody.fieldByName("03").setValue(sRelaField);
				} else {
					dsBody.fieldByName("03").setValue("");
				}
				dsBody.fieldByName("flag").setValue("1");
				dsBody.fieldByName("field_id").setValue(sFieldId);
			} else {
				dsBody.fieldByName("0102").setValue("");
				dsBody.fieldByName("0103").setValue("");
				dsBody.fieldByName("flag").setValue("0");
			}
			// 设主键
			if (dsRefCol.fieldByName("PRIMARY_FIELD").getString()
					.equals(sEname)) {
				dsBody.fieldByName("0201").setValue(CHECK_FLAG);
			} else {
				dsBody.fieldByName("0201").setValue("");
			}

		}
		reportUI.repaint();

	}

	private void showDetailInfo() throws Exception {
		int iRow = reportPriInfo.getSelectedRow();
		if (iRow < reportPriInfo.getReportHeader().getRows())
			return;
		iCurRow = iRow;
		// 英文名
		ftxtfFieldEName.setValue(reportPriInfo.getCellValue(0, iRow));
		// 主键字段
		if (CHECK_FLAG.equals(reportPriInfo.getCellValue(3, iRow)))
			((JCheckBox) fchkPrimaryField.getEditor()).setSelected(true);
		else
			((JCheckBox) fchkPrimaryField.getEditor()).setSelected(false);
		if ("0".equals(dsBody.fieldByName("flag").getString())) {
			// 列中文名
			ftxtfFieldFName.setValue("");
			// 分组号
			jspFieldGroup.setValue(new Integer(0));
			// 关联源表字段
			ftxtfRelaField.setValue("");
			// 代码显示列字段
			((JCheckBox) fchkCodeField.getEditor()).setSelected(false);
			// 名称显示列字段
			((JCheckBox) fchkNameField.getEditor()).setSelected(false);
			// 编辑层次码
			((JCheckBox) fchkbxLvlField.getEditor()).setSelected(false);
			sysRefCodeRule.getFtxtCodeRule().setValue("");
			setAllControlEnabledFalse(true);
		} else {
			// 列中文名
			ftxtfFieldFName.setValue(reportPriInfo.getCellValue(1, iRow));
			// 分组号
			Object sFieldGroup = reportPriInfo.getCellValue(2, iRow);
			if ("".equals(sFieldGroup) || sFieldGroup == null)
				jspFieldGroup.setValue(new Integer(0));
			else
				jspFieldGroup.setValue(new Integer(sFieldGroup.toString()));
			// 关联源表字段
			ftxtfRelaField.setValue(reportPriInfo.getCellValue(8, iRow));
			// 代码显示列字段
			if (CHECK_FLAG.equals(reportPriInfo.getCellValue(4, iRow)))
				((JCheckBox) fchkCodeField.getEditor()).setSelected(true);
			else
				((JCheckBox) fchkCodeField.getEditor()).setSelected(false);
			// 名称显示列字段
			if (CHECK_FLAG.equals(reportPriInfo.getCellValue(5, iRow)))
				((JCheckBox) fchkNameField.getEditor()).setSelected(true);
			else
				((JCheckBox) fchkNameField.getEditor()).setSelected(false);
			// 编辑层次码
			if (CHECK_FLAG.equals(reportPriInfo.getCellValue(6, iRow)))
				((JCheckBox) fchkbxLvlField.getEditor()).setSelected(true);
			else
				((JCheckBox) fchkbxLvlField.getEditor()).setSelected(false);
			sysRefCodeRule.getFtxtCodeRule().setValue(
					reportPriInfo.getCellValue(7, iRow));
			setAllControlEnabledTrue();
		}
		if (iOperationType == SysRefColOperation.rcoView)
			setAllControlEnabledFalse(false);
	}

	/**
	 * 设置所有控件不可用
	 *
	 * @param bShowPrimaryField:true:可编辑,false
	 *            :不可编辑
	 */
	private void setAllControlEnabledFalse(boolean bShowPrimaryField) {
		Common.changeChildControlsEditMode(fpnlFieldProperty, false);
		jspFieldGroup.setEnabled(false);
		sysRefCodeRule.getJspChoiceLen().setEnabled(false);
		if (bShowPrimaryField)
			fchkPrimaryField.setEditable(true);
		else
			fchkPrimaryField.setEditable(false);
	}

	/**
	 * 设置所有控件可用
	 *
	 */
	private void setAllControlEnabledTrue() {
		// 分组号
		jspFieldGroup.setEnabled(true);
		// 关联源表字段
		ftxtfRelaField.setEditable(true);
		// 主键字段
		fchkPrimaryField.setEditable(true);
		// 代码显示列字段
		fchkCodeField.setEditable(true);
		// 名称显示列字段
		fchkNameField.setEditable(true);
		// 编辑层次码
		fchkbxLvlField.setEditable(true);
		setCodeRuleState();
	}

	private void setCodeRuleState() {
		if (Common.estimate(fchkbxLvlField.getValue())) {
			Common.changeChildControlsEditMode(sysRefCodeRule, true);
			sysRefCodeRule.getJspChoiceLen().setEnabled(true);
			((SysRefCodeRule) sysRefCodeRule).getFtxtCodeRule().setEditable(
					false);
		} else {
			Common.changeChildControlsEditMode(sysRefCodeRule, false);
			sysRefCodeRule.getJspChoiceLen().setEnabled(false);
			((SysRefCodeRule) sysRefCodeRule).getFtxtCodeRule().setEditable(
					false);
		}
	}

	/**
	 *
	 * @author Administrator
	 *
	 */
	private class ReportUIMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				showDetailInfo();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"显示引用列列属性信息发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	private class ReportUIKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {
			try {
				showDetailInfo();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"显示引用列列属性信息发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		public void keyTyped(KeyEvent e) {

		}
	};

	private void gotoFirstRecord() throws Exception {
		reportUI.setCellSelection(new CellSelection(0, reportPriInfo
				.getReportHeader().getRows()));
		dsBody.beforeFirst();
		if (!dsBody.isEmpty())
			dsBody.next();
		showDetailInfo();

	}

	private class RelaFieldDocumentListener implements DocumentListener {

		public void changedUpdate(DocumentEvent e) {
			try {
				dsBody.fieldByName("03").setValue(ftxtfRelaField.getValue());
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"编辑关联源表字段发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}

		public void insertUpdate(DocumentEvent e) {
			this.changedUpdate(e);

		}

		public void removeUpdate(DocumentEvent e) {
			this.changedUpdate(e);
		}
	}

	/**
	 * 主键字段
	 *
	 * @author Administrator
	 *
	 */
	private class PrimaryFieldValueChangeListener implements
			ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkPrimaryField.getValue())) {
					clearColShow("0201");
					dsBody.fieldByName("0201").setValue(CHECK_FLAG);
					reportPriInfo.setCellValue(3, iCurRow, CHECK_FLAG);
				} else {
					dsBody.fieldByName("0201").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"选择主键字段发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}
	}

	/**
	 *
	 *
	 * 代码显示列字段
	 *
	 * @author Administrator
	 */
	private class CodeFieldValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkCodeField.getValue())) {
					clearColShow("0202");
					dsBody.fieldByName("0202").setValue(CHECK_FLAG);
					((JCheckBox) fchkNameField.getEditor()).setSelected(false);
				} else {
					dsBody.fieldByName("0202").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"选择代码显示列发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

			reportUI.repaint();
		}
	}

	/**
	 *
	 * 名称显示列字段
	 *
	 * @author Administrator
	 *
	 */
	private class NameFieldValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkNameField.getValue())) {
					clearColShow("0203");
					dsBody.fieldByName("0203").setValue(CHECK_FLAG);
					((JCheckBox) fchkCodeField.getEditor()).setSelected(false);
				} else {
					dsBody.fieldByName("0203").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"选择名称显示列字段发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}
	}

	/**
	 * 清空一列的显示信息
	 *
	 * @param iCol
	 * @throws Exception
	 */
	private void clearColShow(String sFieldName) throws Exception {
		String sBookmark = dsBody.toogleBookmark();
		dsBody.beforeFirst();
		while (dsBody.next()) {
			dsBody.fieldByName(sFieldName).setValue("");
		}
		dsBody.gotoBookmark(sBookmark);
	}

	private class CodeRuleDocumentListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			try {
				dsBody.fieldByName("020402").setValue(
						sysRefCodeRule.getFtxtCodeRule().getValue());
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"编辑层次码发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

			reportUI.repaint();
		}

		public void insertUpdate(DocumentEvent e) {
			this.changedUpdate(e);

		}

		public void removeUpdate(DocumentEvent e) {
			this.changedUpdate(e);
		}
	}

	/**
	 * 编辑层次码
	 *
	 * @author Administrator
	 *
	 */
	private class LvlFieldValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkbxLvlField.getValue())) {
					String sLvlCode = dsBody.fieldByName("020402").getString();
					clearColShow("020401");
					clearColShow("020402");
					dsBody.fieldByName("020401").setValue(CHECK_FLAG);
					dsBody.fieldByName("020402").setValue(sLvlCode);
				} else {
					dsBody.fieldByName("020401").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"选择层次码发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

			reportUI.repaint();
			setCodeRuleState();
		}
	}

	/**
	 * 检查填写的信息
	 *
	 * @throws Exception
	 *
	 */
	private InfoPackage CheckFillInfo() throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sBookmark = dsBody.toogleBookmark();
		// 检查被设置为层次码标识的字段，必须要输入层次码风格
		if (dsBody.locate("020401", CHECK_FLAG)) {
			if (dsBody.fieldByName("020402").getValue() == null
					|| "".equals(dsBody.fieldByName("020402").getString())) {
				dsBody.gotoBookmark(sBookmark);
				sysRefCodeRule.getFtxtCodeRule().setFocus();
				infoPackage.setsMessage("请输入层次码风格！");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
		}
		String sErrorInfo = "";
		// 必须指定主键值
		if (!dsBody.locate("0201", CHECK_FLAG)) {
			sErrorInfo = "主键字段";
		}
		// 引用列选取方式为树时必须要确定显示代码列、显示名称列和层次码标识
		if (dsRefCol.fieldByName("SELECT_KIND").getInteger() == 1) {
			if (!dsBody.locate("0202", CHECK_FLAG)) {
				if (!"".equals(sErrorInfo))
					sErrorInfo = sErrorInfo + ",";
				sErrorInfo = sErrorInfo + "代码显示列";
			}
			if (!dsBody.locate("0203", CHECK_FLAG)) {
				if (!"".equals(sErrorInfo))
					sErrorInfo = sErrorInfo + ",";
				sErrorInfo = sErrorInfo + "名称显示列";
			}
			if (!dsBody.locate("020401", CHECK_FLAG)) {
				if (!"".equals(sErrorInfo))
					sErrorInfo = sErrorInfo + ",";
				sErrorInfo = sErrorInfo + "层次码标识";
			}
		}
		if (!"".equals(sErrorInfo)) {
			sErrorInfo = "请指定：" + sErrorInfo;
			dsBody.gotoBookmark(sBookmark);
			infoPackage.setsMessage(sErrorInfo);
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		// 判断关联源表字段是否填写,是否填写正确
		dsBody.beforeFirst();
		while (dsBody.next()) {
			if (dsBody.fieldByName("flag").getInteger() == 0)
				continue;
			if (dsBody.fieldByName("03").getValue() == null
					|| "".equals(dsBody.fieldByName("03").getString())) {
				dsBody.gotoBookmark(sBookmark);
				infoPackage.setsMessage("请输入有效的关联源表字段信息！");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
			String sRelaField = dsBody.fieldByName("03").getString();
			String[] sRelaFieldArray = sRelaField.split("\\.");
			if (sRelaFieldArray.length != 2) {
				dsBody.gotoBookmark(sBookmark);
				infoPackage.setsMessage("请输入有效的关联源表字段信息,格式:表名.字段名！");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
		}
		dsBody.gotoBookmark(sBookmark);
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	private void saveInfo() throws Exception {
		String sBookmark = dsBody.toogleBookmark();
		dsRefColRelatable.clearAll();
		dsBody.beforeFirst();
		while (dsBody.next()) {
			saveRefCol();// 保存主表信息
			if (dsBody.fieldByName("flag").getInteger() == 0)
				continue;
			saveDetail();// 保存明细表
			saveRelatable();// 保存关联源表
		}
		dsBody.gotoBookmark(sBookmark);
	}

	/**
	 * 保存主表信息
	 *
	 * @throws Exception
	 *
	 */
	private void saveRefCol() throws Exception {
		String sEname = dsBody.fieldByName("0101").getString();
		if (CHECK_FLAG.equals(dsBody.fieldByName("0201").getString())) {
			dsRefCol.fieldByName("PRIMARY_FIELD").setValue(sEname);
			int iFieldType = getFieldTypeWithFieldName(sEname);
			if (Types.CHAR == iFieldType || Types.VARCHAR == iFieldType)
				dsRefCol.fieldByName("PRIMARY_TYPE").setValue("1");
			else
				dsRefCol.fieldByName("PRIMARY_TYPE").setValue("0");
		}
		if (CHECK_FLAG.equals(dsBody.fieldByName("0202").getString())) {
			dsRefCol.fieldByName("CODE_FIELD").setValue(sEname);
		}
		if (CHECK_FLAG.equals(dsBody.fieldByName("0203").getString())) {
			dsRefCol.fieldByName("NAME_FIELD").setValue(sEname);
		}
		if (CHECK_FLAG.equals(dsBody.fieldByName("020401").getString())) {
			dsRefCol.fieldByName("LVL_FIELD").setValue(sEname);
		}
		if (!"".equals(dsBody.fieldByName("020402").getString())) {
			dsRefCol.fieldByName("LVL_STYLE").setValue(
					dsBody.fieldByName("020402").getString());
		}
	}

	private int getFieldTypeWithFieldName(String sFieldName) {
		if (sFieldName == null || "".equals(sFieldName))
			return -1;
		for (int i = 0; i < tableColumnInfo.length; i++) {
			if (sFieldName.equals(tableColumnInfo[i].getColumnName())) {
				return tableColumnInfo[i].getColumnType();
			}
		}
		return -1;
	}

	/**
	 * 保存明细表信息
	 *
	 * @throws Exception
	 *
	 */
	private void saveDetail() throws Exception {
		if (dsRefColDetail.locate("FIELD_ID", dsBody.fieldByName("FIELD_ID")
				.getString())) {
			if (!"".equals(dsBody.fieldByName("0103").getString())) {
				dsRefColDetail.fieldByName("FIELD_GROUP").setValue(
						dsBody.fieldByName("0103").getString());
			}
		}
	}

	private void saveRelatable() throws Exception {
		dsRefColRelatable.append();
		dsRefColRelatable.fieldByName("REFCOL_ID").setValue(
				dsRefCol.fieldByName("refcol_id").getString());
		dsRefColRelatable.fieldByName("FIELD_ID").setValue(
				dsBody.fieldByName("field_id").getString());
		String sReLation = dsBody.fieldByName("03").getString();
		if (!"".equals(sReLation)) {
			String[] sRelationArray = sReLation.split("\\.");
			dsRefColRelatable.fieldByName("RELATION_TABLE").setValue(
					sRelationArray[0]);
			dsRefColRelatable.fieldByName("RELATION_FIELD").setValue(
					sRelationArray[1]);
		}
		dsRefColRelatable.fieldByName("SET_YEAR").setValue(Global.loginYear);
		dsRefColRelatable.fieldByName(ISysRefCol.RG_CODE).setValue(
				Global.getCurrRegion());
	}

	public InfoPackage nextOperate() throws Exception {
		InfoPackage infoPackage = CheckFillInfo();
		if (!infoPackage.getSuccess())
			return infoPackage;
		saveInfo();
		return infoPackage;
	}

	private class FieldGroupChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent arg0) {
			try {
				if ("0".equals(jspFieldGroup.getValue().toString())) {
					dsBody.fieldByName("0103").setValue("");

				} else {
					dsBody.fieldByName("0103").setValue(
							jspFieldGroup.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"编辑分组号发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}

	}

	public void setTableColumnInfo(TableColumnInfo[] tableColumnInfo) {
		this.tableColumnInfo = tableColumnInfo;
	}

	private class CellPropertyProvider implements PropertyProvider {

		public boolean isEditable(String bookmark, Object fieldId) {
			// TODO Auto-generated method stub
			return false;
		}

		public CellEditor getEditor(String bookmark, Object fieldId) {
			// TODO Auto-generated method stub
			return null;
		}

		public double getColumnWidth(Object fieldId) {
			// TODO Auto-generated method stub
			return 0;
		}

		public Format getFormat(String bookmark, Object fieldId) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getFieldName(Object fieldId) {
			return fieldId.toString();
		}

		public Object getFieldId(String fieldName) {
			return fieldName;
		}
	}

}
