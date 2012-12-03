/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:自定义报表类型选择客户端主界面
 * </p>
 * <p>
 * Description:自定义报表类型选择客户端主界面
 * </p>
 * <p>
 
 */
public class DefineReportTypeChoice extends FDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 报表类型列表
	private FList reportTypeLst = null;

	// 报表类型说明多行文本框
	private JTextArea typeExplainTxta = null;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	// 报表类型的数值
	private List reportType_list = null;

	// 定义报表类型
	private int reportType = 0;

	/**
	 * 构造函数
	 * 
	 * @param frame父级窗口
	 */
	public DefineReportTypeChoice() {
		super(Global.mainFrame);
		this.setModal(true);
		this.setSize(600, 450);
		this.setTitle("新建报表");
		// 调用界面初始化方法
		jbInit();
	}

	/**
	 * 界面初始化方法
	 * 
	 */
	private void jbInit() {
		// 定义数据库接口
		definReportServ = DefineReportI.getMethod();

		// 取得报表类型的数值
		reportType_list = definReportServ.getReportSort();

		// 定义报表类型
		Vector reportTypeVertor = new Vector();
		Map reportTypeMa = null;
		String report_type;
		for (int i = 0; i < reportType_list.size(); i++) {
			reportTypeMa = (Map) reportType_list.get(i);
			report_type = reportTypeMa.get("reporttype_name").toString();
			reportTypeVertor.addElement(report_type);
		}

		// 定义报表类型列表
		reportTypeLst = new FList(reportTypeVertor);
		DefaultListSelectionModel reportTypeMod = new DefaultListSelectionModel();
		reportTypeMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		reportTypeLst.setSelectionModel(reportTypeMod);
		reportTypeLst.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() != 2)
					return;
				selectOpe();
			}
		});

		// 报表类型列表加valueChanged事件
		reportTypeLst.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (reportType_list == null)
					return;
				if (reportTypeLst.getSelectedIndex() == -1)
					return;
				Map tmpMap = (Map) reportType_list.get(reportTypeLst
						.getSelectedIndex());
				typeExplainTxta.setText(tmpMap.get("reporttype_explain")
						.toString());
			}
		});

		// 定义报表类型列表滚动面板
		FScrollPane reportTypeSpnl = new FScrollPane(reportTypeLst);

		// 定义报表类型说明多行文本框
		typeExplainTxta = new JTextArea();
		typeExplainTxta.setEditable(false);
		typeExplainTxta.setBackground(this.getBackground());
		// 定义报表类型说明滚动面板
		FScrollPane typeExplainSpnl = new FScrollPane(typeExplainTxta);

		// 定义报表选择面板
		FPanel choiceReportPnl = new FPanel();
		choiceReportPnl.setTitle("请选择一个报表");
		choiceReportPnl.setLayout(new RowPreferedLayout(5));
		// 报表类型列表加入报表选择面板
		choiceReportPnl.add(reportTypeSpnl, new TableConstraints(1, 2, true,
				true));
		// 报表类型说明多行文本框加入报表选择面板
		choiceReportPnl.add(typeExplainSpnl, new TableConstraints(1, 3, true,
				true));

		// 定义按钮面板
		CustomBtnButton buttonPnl = new CustomBtnButton();

		// 定义主面板及布局
		FPanel mainPnl = new FPanel();
		RowPreferedLayout mainRly = new RowPreferedLayout(1);
		mainRly.setRowHeight(30);
		mainPnl.setLayout(mainRly);
		// 报表选择面板加入主面板
		mainPnl.addControl(choiceReportPnl, new TableConstraints(1, 1, true,
				true));
		// 按钮面板加入主面板
		mainPnl.addControl(buttonPnl, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(mainPnl);

		// 设置分组汇总表为默认选择
		if (reportType_list != null) {
			int size = reportType_list.size();
			int index = -1;
			int reporttypeid;
			for (int i = 0; i < size; i++) {
				reporttypeid = Integer.parseInt(((Map) reportType_list.get(i))
						.get("reporttype_id").toString());
				if (reporttypeid == QrBudget.TYPE_GROUP) {
					index = i;
					break;
				}
			}
			if (index != -1)
				reportTypeLst.setSelectedIndex(index);
		}
	}

	/**
	 * 定义按钮面板
	 */
	private class CustomBtnButton extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		public CustomBtnButton() {
			// 设置靠右显示
			this.setAlignment(FlowLayout.RIGHT);
			// 定义确定按
			FButton okBtn = new FButton("okBtn", "确定");
			// 确定按钮点击事件
			okBtn.addActionListener(new okActionListener());

			// 定义取消按钮
			FButton cancelBtn = new FButton("cancelBtn", "取 消");
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 设置未选择报表
					reportType = -1;
					DefineReportTypeChoice.this.setVisible(false);
				}
			});

			// 确定按钮加入按钮面板
			this.addControl(okBtn, new TableConstraints(1, 1, true, false));
			// 取消按钮加入按钮面板
			this.addControl(cancelBtn, new TableConstraints(1, 1, true, false));

		}

	}

	/**
	 * 确定按钮点击事件
	 */
	private class okActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			selectOpe();
		}
	}

	/**
	 * 使用报表操作
	 * 
	 */
	private void selectOpe() {
		// 得到选中的报表
		int selectIndex = reportTypeLst.getSelectedIndex();
		Map reportTypeMa = (Map) reportType_list.get(selectIndex);
		reportType = Integer.parseInt(reportTypeMa.get("reporttype_id")
				.toString());
		DefineReportTypeChoice.this.setVisible(false);
	}

	public int getReportType() {
		return reportType;
	}

}
