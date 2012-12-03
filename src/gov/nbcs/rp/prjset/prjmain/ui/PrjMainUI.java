/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.ui;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.prjset.prjmain.action.PrjMainListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.service.Log;

/**
 * <p>
 * Title:项目申报界面设置单元界面
 * </p>
 * <p>
 * Description:项目申报界面设置，主要显示已经设置的信息，并提供增删改的操作
 * </p>
 * <p>
 * CreateData 2011-4-27
 * </p>
 * 
 * @author qzc
 * @version 1.0
 */
public class PrjMainUI extends RpModulePanel {
	private static final long serialVersionUID = 1L;

	/** 业务处理类 IBS */
	private PrjMainIBS prjMainService;

	/** 业务处理类 IBS */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/** 监听类 */
	private PrjMainListener prjMainListener;
	/*
	 * -1-------------------主面板 -1.0-----------------按钮面板
	 * -1.0.1---------------"添加"按钮 -1.0.2---------------"删除"按钮
	 * -1.0.3---------------"修改"按钮 -1.0.4---------------"查询"按钮
	 * -1.0.5---------------"选项维护"按钮 -1.1-----------------中间面板
	 * -1.1.1---------------中间面板左边面板，放置树形结构 -1.1.1.1-------------数据显示树
	 * -1.1.2---------------中间面板右边面板，放置数据显示区域
	 * -1.1.2.1-------------主表数据显示卡片,处于上半部分 -1.1.2.1.1-----------设置表编号
	 * -1.1.2.1.2-----------设置表名称 -1.1.2.1.3-----------设置表简称
	 * -1.1.2.1.4-----------设置表全称 -1.1.2.1.5-----------默认设置
	 * -1.1.2.2-------------明细数据显示卡片,处于下半部分 -1.1.2.2.1-----------明细数据显示表格
	 */

	/** -1-------------------主面板 */
	private FPanel panel_main = null;

	/**
	 * @return -1-------------------主面板
	 */
	public FPanel getPanel_main() {
		return panel_main;
	}

	/** -1.0-----------------按钮面板 */
	private FToolBarPanel toolBar = null;

	/**
	 * @return -1.0-----------------按钮面板
	 */
	public FToolBarPanel getToolBar() {
		return toolBar;
	}

	/** -1.0.1---------------"添加"按钮 */
	private FButton btn_add = null;

	/**
	 * @return -1.0.1---------------"添加"按钮
	 */
	public FButton getBtn_add() {
		return btn_add;
	}

	/** -1.0.2---------------"删除"按钮 */
	private FButton btn_del = null;

	/**
	 * @return -1.0.2---------------"删除"按钮
	 */
	public FButton getBtn_del() {
		return btn_del;
	}

	/** -1.0.3---------------"修改"按钮 */
	private FButton btn_mod = null;

	/**
	 * @return -1.0.3---------------"修改"按钮
	 */
	public FButton getBtn_mod() {
		return btn_mod;
	}

	/** -1.0.4---------------"查询"按钮 */
	private FButton btn_qry = null;

	// 关闭按钮
	private FButton btn_close = null;

	/**
	 * @return -1.0.4---------------"查询"按钮
	 */
	public FButton getBtn_qry() {
		return btn_qry;
	}

	/** -1.0.5---------------"选项维护"按钮 */
	private FButton btn_selectmod = null;

	/**
	 * @return -1.0.5---------------"选项维护"按钮
	 */
	public FButton getBtn_selectmod() {
		return btn_selectmod;
	}

	/** -1.1-----------------中间面板 */
	private FSplitPane spt_main = null;

	/**
	 * @return -1.1-----------------中间面板
	 */
	public FSplitPane getSpt_main() {
		return spt_main;
	}

	/** -1.1.1---------------中间面板左边面板，放置树形结构 */
	private FScrollPane spn_left = null;

	/**
	 * @return -1.1.1---------------中间面板左边面板，放置树形结构
	 */
	public FScrollPane getSpn_left() {
		return spn_left;
	}

	/** -1.1.1.1-------------数据显示树 */
	private CustomTree tree_data = null;

	/**
	 * @return -1.1.1.1-------------数据显示树
	 */
	public CustomTree getTree_data() {
		return tree_data;
	}

	/** -1.1.2---------------中间面板右边面板，放置数据显示区域 */
	private FPanel panel_right = null;

	/**
	 * @return -1.1.2---------------中间面板右边面板，放置数据显示区域
	 */
	public FPanel getPanel_right() {
		return panel_right;
	}

	/** -1.1.2.1-------------主表数据显示卡片,处于上半部分 */
	private FPanel panel_right_up = null;

	/**
	 * @return -1.1.2.1-------------主表数据显示卡片,处于上半部分
	 */
	public FPanel getPanel_right_up() {
		return panel_right_up;
	}

	/** -1.1.2.1.1-----------设置表编号 */
	private FTextField input_set_id = null;

	/**
	 * @return -1.1.2.1.1-----------设置表编号
	 */
	public FTextField getInput_set_id() {
		return input_set_id;
	}

	/** -1.1.2.1.2-----------设置表名称 */
	private FTextField input_set_name = null;

	/**
	 * @return -1.1.2.1.2-----------设置表名称
	 */
	public FTextField getInput_set_name() {
		return input_set_name;
	}

	/** -1.1.2.1.3-----------设置表简称 */
	private FTextField input_set_shortname = null;

	/**
	 * @return -1.1.2.1.3-----------设置表简称
	 */
	public FTextField getInput_set_shortname() {
		return input_set_shortname;
	}

	/** -1.1.2.1.4-----------设置表全称 */
	private FTextField input_set_longname = null;

	/**
	 * @return -1.1.2.1.4-----------设置表全称
	 */
	public FTextField getInput_set_longname() {
		return input_set_longname;
	}

	/** -1.1.2.1.5-----------默认设置 */
	private FCheckBox is_default = null;

	/**
	 * @return -1.1.2.1.5-----------默认设置
	 */
	public FCheckBox getIs_default() {
		return is_default;
	}

	/** -1.1.2.2-------------明细数据显示卡片,处于下半部分 */
	private FPanel panel_right_down = null;

	/**
	 * @return -1.1.2.2-------------明细数据显示卡片,处于下半部分
	 */
	public FPanel getPanel_right_down() {
		return panel_right_down;
	}

	/** -1.1.2.2.1-----------明细数据显示表格 */
	private FTable table_detail = null;

	/**
	 * @return -1.1.2.2.1-----------明细数据显示表格
	 */
	public FTable getTable_detail() {
		return table_detail;
	}

	/**
	 * 功能按钮的初始化:"增加"，"删除"等
	 */
	private void createButtons() {
		// 创建各个功能按钮
		// "增加"按钮的创建及添加
		btn_add = new FButton();
		btn_add.setTitle("增加");
		btn_add.setIcon("images/rp/add.gif");
		// "删除"按钮的创建及添加
		btn_del = new FButton();
		btn_del.setTitle("删除");
		btn_del.setIcon("images/rp/del.gif");
		// "修改"按钮的创建及添加
		btn_mod = new FButton();
		btn_mod.setTitle("修改");
		btn_mod.setIcon("images/rp/mod.gif");
		// "保存"按钮的创建及添加
		btn_selectmod = new FButton();
		btn_selectmod.setTitle("选项维护");
		btn_selectmod.setIcon("images/rp/set.gif");
		// "刷新"按钮的创建及添加
		btn_qry = new FButton();
		btn_qry.setTitle("刷新");
		btn_qry.setIcon("images/rp/query.gif");
		// "关闭"按钮的创建及添加
		btn_close = new FButton();
		btn_close.setTitle("关闭");
		btn_close.setIcon("images/rp/close.gif");
		// 为各个按钮增加相应的监听
		btn_add.addActionListener(new RefActionListener(prjMainListener,
				"btn_addAction"));
		btn_del.addActionListener(new RefActionListener(prjMainListener,
				"btn_delAction"));
		btn_mod.addActionListener(new RefActionListener(prjMainListener,
				"btn_modAction"));
		btn_selectmod.addActionListener(new RefActionListener(prjMainListener,
				"btn_selectmodAction"));
		btn_qry.addActionListener(new RefActionListener(prjMainListener,
				"btn_qryAction"));
		btn_close.addActionListener(new RefActionListener(prjMainListener,
				"btn_closeAction"));
		// 创建按钮栏，并将按钮按顺序放置
		toolBar = new FToolBarPanel();
		toolBar.addControl(btn_close);// yangxuefeng add 20080428
		toolBar.addControl(btn_add);
		toolBar.addControl(btn_del);
		toolBar.addControl(btn_mod);
		toolBar.addControl(btn_selectmod);
		// toolBar.addControl(btn_qry);
		// 按钮栏嵌入模块
		this.setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * 主面板左半部分的初始化，放置树形结构
	 * 
	 * @return 返回滚动面板
	 */
	private FScrollPane getLeftPanel() {

		try {
			// 数据显示树的创建
			tree_data = new CustomTree();
			tree_data.setEditable(false);
			// 选择值变化的监听程序
			tree_data.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					prjMainListener.tree_datavalueChanged(e);
				}
			});
			// 鼠标点击的监听，主要用于双击控制
			tree_data.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 系统结构开关打开时，不可以操作
					try {
						prjMainListener.tree_dataClicked(e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		} catch (Exception e1) {
			Log.error(e1.getMessage());
		}
		// 创建面板，并且将苏醒结构放置到面板上
		spn_left = new FScrollPane();
		spn_left.addControl(tree_data);
		return spn_left;
	}

	/**
	 * 主面板右上部分的初始化
	 * 
	 * @return 主面板右上部分的面板
	 */
	private FPanel setPanelDataMain() {
		// 创建各个明细控件
		// "设置表编号"控件的创建，设置以及添加
		input_set_id = new FTextField("设置表编号");
		input_set_id.setProportion(0.10f);
		input_set_id.setEditable(false);
		input_set_id.setVisible(true);
		// "设置表名称"控件的创建，设置以及添加
		input_set_name = new FTextField("设置表名称");
		input_set_name.setProportion(0.10f);
		input_set_name.setEditable(false);
		input_set_name.setVisible(true);
		// "设置表简称"控件的创建，设置以及添加
		input_set_shortname = new FTextField("设置表简称");
		input_set_shortname.setProportion(0.10f);
		input_set_shortname.setEditable(false);
		input_set_shortname.setVisible(true);
		// "设置表全称"控件的创建，设置以及添加
		input_set_longname = new FTextField("设置表全称");
		input_set_longname.setProportion(0.10f);
		input_set_longname.setEditable(false);
		input_set_longname.setVisible(true);
		// "默认设置"控件的创建，设置以及添加
		is_default = new FCheckBox("默认设置");
		is_default.setProportion(0.10f);
		is_default.setEditable(false);
		is_default.setEnabled(false);
		is_default.setVisible(true);
		// 主表数据展示的面板的生成
		panel_right_up = new FPanel();
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(6);
		panel_right_up.setLayout(rowPreferedLayout);
		panel_right_up.setTitle("主表数据");
		// 将控件按顺序摆放进面板
		panel_right_up.addControl(input_set_id, new TableConstraints(1, 1,
				false, true));
		panel_right_up.addControl(input_set_name, new TableConstraints(1, 2,
				false, true));
		panel_right_up.addControl(input_set_shortname, new TableConstraints(1,
				3, false, true));
		panel_right_up.addControl(input_set_longname, new TableConstraints(1,
				4, false, true));
		panel_right_up.addControl(is_default, new TableConstraints(1, 5, false,
				true));
		return panel_right_up;
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
	private FBaseTableColumn returnFtableColumn(String column_id,
			String column_name, String alignment, int width) {
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

	/**
	 * 主面板右下部分的初始化
	 * 
	 * @return 主面板右下部分的面板
	 */
	private FPanel setPanelDataDetail() {
		try {
			// 创建明细显示Table，并且添加上相应的显示列
			table_detail = new FTable();
			table_detail.addColumn(returnFtableColumn("comp_id", "控件编码",
					FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("is_must_name", "是否必填",
					FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("comp_name", "控件名称",
					FBaseTableColumn.RIGHT, 100));
			table_detail.addColumn(returnFtableColumn("comp_ename", "字段名称",
					FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("field_type_name",
					"控件类型", FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("is_comm", "必填字段",
			// FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("comp_min_len", "最小长度",
			// FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("comp_max_len", "最大长度",
			// FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("comp_min_value",
			// "最小值", FBaseTableColumn.RIGHT, 80));
			// table_detail.addColumn(returnFtableColumn("comp_max_value",
			// "最大值", FBaseTableColumn.RIGHT, 80));
			table_detail.addColumn(returnFtableColumn("is_inner", "是否内置",
					FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("data_format",
			// "格式化字符串", FBaseTableColumn.RIGHT, 80));
			table_detail.setRowSelectionAllowed(true);
			table_detail.setColumnSelectionAllowed(false);
			// 创建面板，并且把显示表放置上去
			panel_right_down = new FPanel();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(table_detail, BorderLayout.CENTER);
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return panel_right_down;
	}

	/**
	 * 主面板右半部分的初始化，明细数据分上下两个面板
	 * 
	 * @return 主面板右半部分的面板
	 */
	private FPanel getRightPanel() {
		// 创建主面板右半部分的面板，并且将上下两部分放置进去
		panel_right = new FPanel();
		panel_right.setLayout(new BorderLayout());
		panel_right.addControl(this.setPanelDataMain(), BorderLayout.NORTH);
		panel_right.addControl(this.setPanelDataDetail(), BorderLayout.CENTER);
		return panel_right;
	}

	/**
	 * 主面板的初始化，分左右两个面板
	 */
	private void createMainPanel() {
		// 创建Split，方便控制左右两边面板的大小
		spt_main = new FSplitPane();
		spt_main.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spt_main.setResizeWeight(0.40);
		// 在Split两边分别放置两个面板
		spt_main.addControl(this.getLeftPanel());
		spt_main.addControl(this.getRightPanel());
		panel_main.add(spt_main);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foundercy.pf.framework.systemmanager.FModulePanel#initize()
	 *      初始化PrjMainUI的界面
	 */
	public void initize() {
		// 结构开关
		try {
			// 获得业务处理类
			BeanFactory beanfactory = BeanFactoryUtil
					.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
			prjMainService = (PrjMainIBS) beanfactory
					.getBean("rp.PrjMainService");
			// 创建监听类
			prjMainListener = new PrjMainListener(this);
			// 创建所有控制按钮
			this.createButtons();
			// 创建主面板
			panel_main = new FPanel();
			panel_main.setLayout(new RowPreferedLayout(1));
			// 填充主面板，并且将主面板放置到页面内
			this.createMainPanel();
			this.add(panel_main);
			// this.createPopupMenu();
			this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			// 初始化某些控件的值
			this.initComponent();
			// 页面显示
			this.setVisible(true);

			// yangxuefeng add 20080419 begin
			// 当结构关闭时禁止增删改，当结构打开时可以正常的增删改
			// 工作流接口
			this.btn_add.setEnabled(true);
			this.btn_del.setEnabled(true);
			this.btn_mod.setEnabled(true);
			this.btn_selectmod.setEnabled(true);
		} catch (Exception e) {
			Log.error("PrjMainUI.class界面初始化失败！");
			Log.error(e.getMessage());
		}
	}

	/**
	 * 初始化某些控件的值：树形显示的数据的初始化
	 */
	private void initComponent() {
		// 初始化数据表示树的值
		this.initTree();
	}

	/**
	 * 初始化数据表示树的值，公共函数，可以被外部调用
	 */
	public void initTree() {
		// 根据业务处理类获得树结构的xmldata
		DataSet ds_tree = prjMainService.getInputSetTreeData();
		try {
			// 设置CustomTree的根节点，Dataset，ID编号，显示名称，父节点ID
			tree_data.setRootName("所有设置");
			tree_data.setDataSet(ds_tree);
			tree_data.setIdName("CHR_ID");
			tree_data.setTextName("CHR_NAME");
			tree_data.setParentIdName("PARENT_ID");
			// 自动生成显示树
			tree_data.reset();
		} catch (Exception e) {
			Log.error("初始化数据表示树错误！");
			Log.error(e.getMessage());
		}
	}

	public FButton getBtn_close() {
		return btn_close;
	}

	public void setBtn_close(FButton btn_close) {
		this.btn_close = btn_close;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
