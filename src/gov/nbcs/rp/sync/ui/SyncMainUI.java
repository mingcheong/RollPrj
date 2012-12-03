package gov.nbcs.rp.sync.ui;

/**
 * 数据同步主界面
 * 
 */

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.sync.action.SyncServiceStub;
import gov.nbcs.rp.sync.ibs.SyncInterface;
import gov.nbcs.rp.sync.util.MainInfoOper;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.service.Log;
import com.foundercy.pf.control.FTextArea;

public class SyncMainUI extends FModulePanel {

	protected static final long serialVersionUID = 1L;
	// 业务处理类
	protected MainInfoOper oper = null;
	// 主面板
	public FPanel mainPanel = null;
	// topPanel
	public FPanel topPanel = null;

	public FTextField dbLinkName;
	public FComboBox exportLineComboBox;
	public FComboBox execFuncComboBox1;
	public FComboBox execFuncComboBox2;

	// middlePanel
	public FPanel middlePanel = null;
	// 执行前脚本Field
	public FTextArea beforeTField;
	// 执行后脚本Field
	public FTextArea afterTField;

	public FComboBox schemeComboBox;

	// bottomPanel
	public FPanel bottomPanel;

	public CustomTable tbDetail;

	SyncInterface syncService;
	
	

	/**
	 * 构造方法
	 */
	public SyncMainUI() {
	}

	/**
	 * 界面初始化
	 */
	public void initize() {
		syncService = SyncServiceStub.getMethod();
		try {
			this.createToolBar();
			this.add(getPanel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setVisible(true);

	}

	/**
	 * 取得必须的属性面板
	 * 
	 * @return
	 * @throws ControlException
	 */
	public FPanel getPanel() throws Exception {
		if (mainPanel == null) {
			mainPanel = new FPanel();
			RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
			mainPanel.setLayout(rowPreferedLayout);
			mainPanel.addControl(getTopPanel(), new TableConstraints(1, 1,
					false, true));
			mainPanel.addControl(getMiddlePanel(), new TableConstraints(1, 1,
					false, true));
			mainPanel.addControl(getBottomPanel(), new TableConstraints(1, 1,
					true, true));
			mainPanel.setLeftInset(10);
			mainPanel.setRightInset(10);
			mainPanel.setBottomInset(10);
			mainPanel.setTopInset(10);
		}
		return mainPanel;
	}

	protected FPanel getTopPanel() throws ControlException {
		if (topPanel == null) {
			topPanel = new FPanel();
		}

		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(4);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(1);
		topPanel.setLayout(rowPreferedLayout);

		dbLinkName = new FTextField("DB_LINK名称：");
		dbLinkName.setValue("DBLINK");
		dbLinkName.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if ((dbLinkName.getValue() != null)
						&& !"".equals(dbLinkName.getValue())) {
					if (dbLinkName.getValue().toString().length() > 10) {
						dbLinkName.setValue(dbLinkName.getValue().toString()
								.subSequence(0, 10));
					}
				}
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		dbLinkName.setProportion(0.30f);

		exportLineComboBox = new FComboBox(" 导出方向：");
		String modelString = "0" + "#" + "项目库到平台" + "+";
		modelString += "1" + "#" + "平台到项目库";
		exportLineComboBox.setRefModel(modelString);
		exportLineComboBox.setProportion(0.23f);

		execFuncComboBox1 = new FComboBox("  操作方式：");
		modelString = null;
		modelString = "0" + "#" + "同步前清空表数据" + "+";
		modelString += "1" + "#" + "同步前不清空表数据";
		execFuncComboBox1.setRefModel(modelString);
		execFuncComboBox1.setProportion(0.23f);

		execFuncComboBox1.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				if (arg0 != null) {
					if (arg0.getNewValue() != null) {
						if ("0".equals(arg0.getNewValue().toString())) {
							execFuncComboBox2.setEditable(false);
							execFuncComboBox2.setEnabled(false);
						}
						if ("1".equals(arg0.getNewValue().toString())) {
							execFuncComboBox2.setEditable(true);
							execFuncComboBox2.setEnabled(true);
						}
					}
				}

			}
		});

		execFuncComboBox2 = new FComboBox("");
		modelString = null;
		modelString = "0" + "#" + "删除同步重复数据" + "+";
		modelString += "1" + "#" + "不删除同步重复数据";
		execFuncComboBox2.setRefModel(modelString);
		execFuncComboBox2.setProportion(0f);
		execFuncComboBox2.setEditable(false);
		execFuncComboBox2.setEnabled(false);

		topPanel
				.addControl(dbLinkName, new TableConstraints(1, 1, false, true));
		topPanel.addControl(exportLineComboBox, new TableConstraints(1, 1,
				false, true));
		topPanel.addControl(execFuncComboBox1, new TableConstraints(1, 1,
				false, true));
		topPanel.addControl(execFuncComboBox2, new TableConstraints(1, 1,
				false, true));
		return topPanel;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */

	protected FPanel getMiddlePanel() throws Exception {
		if (middlePanel == null) {
			middlePanel = new FPanel();
		}

		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(3);
		middlePanel.setLayout(rowPreferedLayout);
		beforeTField = new FTextArea("同步前脚本录入：");
		beforeTField.setProportion(0.25f);
		afterTField = new FTextArea("同步后脚本录入：");
		afterTField.setProportion(0.25f);
		schemeComboBox = new FComboBox("表方案：");
		schemeComboBox.setProportion(0.15f);

		List list = syncService.getSchemaData(null,null);
		String modelString ="all#全部+";
		for (int i = 0; i < list.size(); i++) {
			modelString += ((XMLData) list.get(i)).get("type").toString()
					+ "#"
					+ ((XMLData) list.get(i)).get("name").toString()
					+ "+";
		}
		modelString = modelString.substring(0, modelString.length() - 1);
		schemeComboBox.setRefModel(modelString);
		schemeComboBox.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				setTableData(arg0.getNewValue().toString());
			}
		});
		middlePanel.addControl(schemeComboBox, new TableConstraints(1, 1,
				false, true));
		middlePanel.addControl(beforeTField, new TableConstraints(1, 1, false,
				true));
		middlePanel.addControl(afterTField, new TableConstraints(1, 1, false,
				true));

		return middlePanel;
	}

	protected void setTableData(String string) {
		tbDetail.setDataSet(setListData(string));
		try {
			tbDetail.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected DataSet setListData(String string) {
		DataSet dataSet = null;
		try {
			if ((string==null) ||"".equals(string) || "all".equals(string.trim())) {
				dataSet = syncService.getTableData("");
			} else {
				dataSet = syncService.getTableData(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 
	 * @return
	 * @throws ControlException
	 */
	protected FPanel getBottomPanel() throws ControlException {
		if (bottomPanel == null) {
			bottomPanel = new FPanel();
		}

		try {

			DataSet ds = this.setListData(null);
			tbDetail = new CustomTable(new String[]{"表名","中文名称","对照表名","对照表中文名称"},new String[]{"table_ename","table_cname","pf_ename","pf_cname"},ds,true,null);
			tbDetail.reset();
			// 把显示表放置上去
			bottomPanel.setLayout(new BorderLayout());
			bottomPanel.addControl(tbDetail, BorderLayout.CENTER);
			
			
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return bottomPanel;
	}

	public MainInfoOper getOper() {
		return oper;
	}

	/**
	 * 创建Table列
	 * 
	 * @param column_id
	 *            列名的后台编码
	 * @param column_name
	 *            列标题
	 * @param alignment
	 *            值显示的位置
	 * @param width
	 *            默认长度
	 * @return 根据以上属性生成的Table列
	 */
	private FBaseTableColumn returnFtableColumn(String column_id, String column_name, String alignment, int width) {
		// 创建Table列
		FBaseTableColumn column = new FBaseTableColumn();
		// 设置后台编码
		column.setId(column_id);
		// 设置值显示的位置
		column.setAlignment(alignment);
		// 设置编辑属性
		column.setEditable(false);
		// 设置列标题
		column.setTitle(column_name);
		// 标题显示居中
		column.setHeadAlignment(FBaseTableColumn.CENTER);
		// 设置默认长度
		column.setWidth(width);
		return column;
	}
}
