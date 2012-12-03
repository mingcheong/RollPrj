/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.report.cell.inputui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * 
 * 
 * @author 杜双来
 * 
 */

public class TableUI extends AbstractInputDialog {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// 没有check框
	public static final int iNoCheck = 0;

	// 有check框
	public static final int iChcek = 1;

	private Object ob;

	// 返回值是判断是否有check框
	public int iCount = -1;

	// 获取传入的i
	private int Flag;

	// 获取返回内容字段
	private String fieldName;

	private DataSet ds;

	// 项目代码或名称
	FTextField prjTextField;

	// 表
	CustomTable table;

	String[] aFieldName;

	/*
	 * 参数说明：aFieldText,aFieldName,dataset 生成customtable的参数 i check框参数（0 没有，1 有）
	 * fieldEName 获取内容字段，obj 获取返回值的控件
	 */
	public TableUI(String[] aFieldText, String[] aFieldName, DataSet dataSet,
			int i, String fieldEName, Object obj) throws Exception {
		super(Global.mainFrame);
		this.ds = dataSet;
		this.Flag = i;
		this.aFieldName = aFieldName;
		ob = obj;
		this.fieldName = fieldEName;
		ds.beforeFirst();
		ds.next();

		this.setSize(500, 600);
		this.setResizable(false);

		this.dispose();
		this.setTitle("录入项选择");
		this.setModal(true);

		// 主窗体上放入panel，作为主布局panel
		FPanel mainPanel = new FPanel();
		RowPreferedLayout mainLayout = new RowPreferedLayout(1);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLeftInset(10);
		mainPanel.setRightInset(10);
		mainPanel.setTopInset(10);
		// 项目代码或名称
		prjTextField = new FTextField();
		prjTextField.setTitle("请输入查询内容:");
		prjTextField.setProportion(0.25f);

		// 两个查找按钮
		FFlowLayoutPanel findPanel = new FFlowLayoutPanel();
		findPanel.setAlignment(FlowLayout.CENTER);
		// 查找按钮
		FButton findButton = new FButton();
		findButton.setText("查找");
		findButton.addActionListener(new searchAction(this));

		FButton findNextButton = new FButton();
		findNextButton.setText("查找下一个");
		findPanel.addControl(findButton);
		findPanel.addControl(findNextButton);
		findNextButton.addActionListener(new searchNextAction(this));

		// 表
		// 分别显示时候有Check框
		FPanel tablePanel = new FPanel();
		if (Flag == TableUI.iChcek) {
			table = new CustomTable(aFieldText, aFieldName, ds, true,
					new String[] { "" });
		} else {
			table = new CustomTable(aFieldText, aFieldName, ds, false,
					new String[] { "" });
		}
		table.reset();
		RowPreferedLayout tableLayout = new RowPreferedLayout(1);
		tablePanel.setLayout(tableLayout);
		tablePanel.addControl(table, new TableConstraints(1, 1, true, true));

		// 双击表格列，返回所需要的值，功能同【确定】按钮
		table.getTable().addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					iCount = 1;
					try {
						setVisible(false);
						if ((ob != null) && (!Common.isNullStr(fieldName))) {
							resultObject(ob);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

		});

		// "确定"、"取消"按钮
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		// 确定按钮
		FButton okButton = new FButton();
		okButton.setText("确定");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iCount = 1;
				try {
					setVisible(false);
					if ((ob != null) && (!Common.isNullStr(fieldName))) {
						resultObject(ob);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		FButton cancelButton = new FButton();
		cancelButton.setText("取消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iCount = 0;
				setVisible(false);
			}
		});
		choosePanel.addControl(okButton);
		choosePanel.addControl(cancelButton);

		// 把元素都加入到mainPanel中
		mainPanel.addControl(prjTextField, new TableConstraints(1, 1, false,
				true));
		mainPanel
				.addControl(findPanel, new TableConstraints(2, 1, false, true));
		mainPanel.add(tablePanel, new TableConstraints(8, 1, true, true));
		mainPanel.addControl(choosePanel, new TableConstraints(2, 1, false,
				true));
		this.getContentPane().add(mainPanel);
	}

	// 获取返回值
	public Object getValue() throws Exception {
		if (iCount == 1) {
			if ((!ds.bof()) && (!ds.eof())) {
				if (Flag == iNoCheck) {
					return ds.fieldByName(fieldName).getString();
				} else {
					String string = new String();
					int[] row = table.getTable().getSelectedRows();
					for (int i = 0; i < row.length; i++) {
						String bookmark = table.rowToBookmark(row[i]);
						ds.gotoBookmark(bookmark);
						if (i != row.length - 1) {
							string += ds.fieldByName(fieldName).getString()
									+ ";";
						} else {
							string += ds.fieldByName(fieldName).getString();
						}
					}
					return string;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	// 匹配控件,为控件赋值
	public void resultObject(Object ob) throws Exception {
		if (ob instanceof FTextField) {
			((FTextField) ob).setValue(getValue());
		}
		if (ob instanceof FTextArea) {
			((FTextArea) ob).setValue(getValue());
		}
		if (ob instanceof FList) {
			DefaultListModel listModel = (DefaultListModel) ((FList) ob)
					.getModel();
			((FList) ob).setModel(listModel);
			listModel.addElement(getValue());
		}
		if (ob instanceof JTextField) {
			if (getValue() != null) {
				((JTextField) ob).setText(getValue().toString());
			}
		}
	}

	public void setUiTitle(String title) {
		this.setTitle(title);
	}

	public FTextField getPrjTextField() {
		return prjTextField;
	}

	// [查找]按钮监听事件
	public class searchAction implements ActionListener {
		TableUI tableUI;

		public searchAction(TableUI tableUI) {
			this.tableUI = tableUI;
		}

		public void actionPerformed(ActionEvent arg0) {
			try {
				boolean isSel = false;
				String content = Common.nonNullStr(prjTextField.getValue());
				ds.beforeFirst();
				while (ds.next()) {
					String fieldEName = aFieldName[1];
					String fieldValue = Common.nonNullStr(ds.fieldByName(
							fieldEName).getValue());
					if (fieldValue.indexOf(content) >= 0) {
						isSel = true;
						int index = table.bookmarkToRow(ds.toogleBookmark());
						table.getTable().getSelectionModel().setSelectionMode(
								ListSelectionModel.SINGLE_SELECTION);
						table.getTable().getSelectionModel().clearSelection();
						table.getTable().getSelectionModel()
								.setLeadSelectionIndex(index);
						break;
					}
				}
				if (!isSel) {
					JOptionPane.showMessageDialog(tableUI, "没有找到匹配的内容！");
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * 查找下一个操作
	 * @author 杜双来
	 *
	 */
	public class searchNextAction implements ActionListener {
		TableUI tableUI;

		public searchNextAction(TableUI tableUI) {
			this.tableUI = tableUI;
		}

		public void actionPerformed(ActionEvent arg0) {
			try {
				boolean isSel=false;
				String content = Common.nonNullStr(prjTextField.getValue());
				while (ds.next()) {
					String fieldEName = aFieldName[1];
					String fieldValue = Common.nonNullStr(ds.fieldByName(
							fieldEName).getValue());
					if (fieldValue.indexOf(content) >= 0) {
						isSel=true;
						int index = table.bookmarkToRow(ds.toogleBookmark());
						table.getTable().getSelectionModel().setSelectionMode(
								ListSelectionModel.SINGLE_SELECTION);
						table.getTable().getSelectionModel().clearSelection();
						table.getTable().getSelectionModel()
								.setLeadSelectionIndex(index);
						break;
					}
				}
				if(!isSel) {
					JOptionPane.showMessageDialog(tableUI, "没有找到匹配的内容！");
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
