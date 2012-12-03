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

import com.foundercy.pf.control.Control;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.prjset.prjmain.action.PrjMain_DetailListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JDialog;

import org.springframework.beans.factory.BeanFactory;

/**
 * <p>
 * Title:项目申报界面设置增加（修改）单元界面
 * </p>
 * <p>
 * Description:项目申报界面设置单元界面上点击“增加”或者“修改”时弹出的界面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-17
 * </p>
 * 
 * @author 孙瑞敏
 * @version 1.0
 */
public class PrjMain_DetailUI extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * 主界面传递进来的数据集
	 */
	private DataSet mainData = null;

	/**
	 * @return 主界面传递进来的数据集
	 */
	public DataSet getMainData() {
		return mainData;
	}

	/**
	 * 操作类型
	 */
	private int operationType = PrjMainIBS.OPERATION_ADD;

	/**
	 * @return 操作类型
	 */
	public int getOperationType() {
		return operationType;
	}

	/**
	 *  业务处理类 IBS 
	 */
	private PrjMainIBS prjMainService = null;

	/**
	 * @return 业务处理类 IBS 
	 */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/**
	 * 监听类
	 */
	private PrjMain_DetailListener prjMainDetailListener = null;

	/*
	 * -1-------------------主面板 
	 * -1.1-----------------操作区面板
	 * -1.1.1---------------主数据面板 
	 * -1.1.1.1-------------设置表编号
	 * -1.1.1.2-------------设置表名称 
	 * -1.1.1.3-------------设置表简称
	 * -1.1.1.4-------------设置表全称 
	 * -1.1.1.5-------------默认设置
	 * -1.1.2---------------明细数据面板 
	 * -1.1.2.1-------------明细数据操作按钮面板
	 * -1.1.2.1.1-----------"设置数据"按钮 
	 * -1.1.2.1.2-----------"上移"按钮
	 * -1.1.2.1.3-----------"下移"按钮 
	 * -1.1.2.2-------------明细数据显示面板
	 * -1.1.2.2.1-----------明细数据显示Table 
	 * -1.2-----------------按钮面板
	 * -1.2.1---------------"确定"按钮 
	 * -1.2.2---------------"取消"按钮
	 */
	/** -1-------------------主面板 */
	private FPanel panel_main = null;

	/**
	 * @return -1-------------------主面板
	 */
	public FPanel getPanel_main() {
		return panel_main;
	}

	/** -1.1-----------------操作区面板 */
	private FPanel panel_data = null;

	/**
	 * @return -1.1-----------------操作区面板
	 */
	public FPanel getPanel_data() {
		return panel_data;
	}

	/** -1.1.1---------------主数据面板 */
	private FPanel panel_data_main = null;

	/**
	 * @return -1.1.1---------------主数据面板
	 */
	public FPanel getPanel_data_main() {
		return panel_data_main;
	}

	/** -1.1.1.1-------------设置表编号 */
	private FTextField input_set_id = null;

	/**
	 * @return -1.1.1.1-------------设置表编号
	 */
	public FTextField getInput_set_id() {
		return input_set_id;
	}

	/** -1.1.1.2-------------设置表名称 */
	private FTextField input_set_name = null;

	/**
	 * @return -1.1.1.2-------------设置表名称
	 */
	public FTextField getInput_set_name() {
		return input_set_name;
	}

	/** -1.1.1.3-------------设置表简称 */
	private FTextField input_set_shortname = null;

	/**
	 * @return -1.1.1.3-------------设置表简称
	 */
	public FTextField getInput_set_shortname() {
		return input_set_shortname;
	}

	/** -1.1.1.4-------------设置表全称 */
	private FTextField input_set_longname = null;

	/**
	 * @return -1.1.1.4-------------设置表全称
	 */
	public FTextField getInput_set_longname() {
		return input_set_longname;
	}

	/** -1.1.1.5-------------默认设置 */
	private FCheckBox is_default = null;

	/**
	 * @return -1.1.1.5-------------默认设置
	 */
	public FCheckBox getIs_default() {
		return is_default;
	}

	/** -1.1.2---------------明细数据面板 */
	private FPanel panel_data_detail = null;

	/**
	 * @return -1.1.2---------------明细数据面板
	 */
	public FPanel getPanel_data_detail() {
		return panel_data_detail;
	}

	/** -1.1.2.1-------------明细数据操作按钮面板 */
	private FFlowLayoutPanel panel_data_detail_button = null;

	/**
	 * @return -1.1.2.1-------------明细数据操作按钮面板
	 */
	public FFlowLayoutPanel getPanel_data_detail_button() {
		return panel_data_detail_button;
	}

	/** -1.1.2.1.1-----------"设置数据"按钮 */
	private FButton btn_mod = null;

	/**
	 * @return -1.1.2.1.1-----------"设置数据"按钮
	 */
	public FButton getBtn_mod() {
		return btn_mod;
	}

	/** -1.1.2.1.2-----------"上移"按钮 */
	private FButton btn_up = null;

	/**
	 * @return -1.1.2.1.2-----------"上移"按钮
	 */
	public FButton getBtn_up() {
		return btn_up;
	}

	/** -1.1.2.1.3-----------"下移"按钮 */
	private FButton btn_down = null;

	/**
	 * @return -1.1.2.1.3-----------"下移"按钮
	 */
	public FButton getBtn_down() {
		return btn_down;
	}

	/** -1.1.2.2-------------明细数据显示面板 */
	private FPanel panel_data_detail_table = null;

	/**
	 * @return -1.1.2.2-------------明细数据显示面板
	 */
	public FPanel getPanel_data_detail_table() {
		return panel_data_detail_table;
	}

	/** -1.1.2.2.1-----------明细数据显示Table */
	private FTable table_detail = null;

	/**
	 * @return -1.1.2.2.1-----------明细数据显示Table
	 */
	public FTable getTable_detail() {
		return table_detail;
	}

	/** -1.2-----------------按钮面板 */
	private FFlowLayoutPanel panel_buttons = null;

	/**
	 * @return -1.2-----------------按钮面板
	 */
	public FFlowLayoutPanel getPanel_buttons() {
		return panel_buttons;
	}

	/** -1.2.1---------------"确定"按钮 */
	private FButton btn_ok = null;

	/**
	 * @return -1.2.1---------------"确定"按钮
	 */
	public FButton getBtn_ok() {
		return btn_ok;
	}

	/** -1.2.2---------------"取消"按钮 */
	private FButton btn_cancel = null;

	/**
	 * @return -1.2.2---------------"取消"按钮
	 */
	public FButton getBtn_cancel() {
		return btn_cancel;
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
	 * 构造函数，初始化本界面
	 * @param dataSet 父界面数据集
	 * @param operType 操作类型
	 */
	public PrjMain_DetailUI(DataSet dataSet, int operType) {
		//根据操作参数显示本页面的标题
		super(Global.mainFrame, "添加", true);
		if ( operType == PrjMainIBS.OPERATION_MOD ) {
			this.setTitle("项目申报界面设置(修改)");
		} else {
			this.setTitle("项目申报界面设置(添加)");
		}
		//初始化服务，监听等对象
		initService();
		try {
			//获取全局的父界面数据集
			this.mainData = dataSet;
			//获取操作类型
			operationType = operType;
			//初始化界面
			dgInit();
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * 初始化服务，监听等对象
	 */
	private void initService() {
		//初始化业务操作类
		BeanFactory beanfactory = BeanFactoryUtil.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
		prjMainService = (PrjMainIBS) beanfactory.getBean("rp.PrjMainService");
		//初始化监听
		prjMainDetailListener = new PrjMain_DetailListener(this);
	}

	/**
	 * 初始化并返回主数据显示面板
	 * @return 主数据显示面板
	 */
	private Control getMainPanel() {
		//"设置表编号"控件的创建，设置
		input_set_id = new FTextField("设置表编号");
		input_set_id.setProportion(0.24f);
		input_set_id.setEditable(false);
		input_set_id.setVisible(true);
		//"设置表名称"控件的创建，设置
		input_set_name = new FTextField("设置表名称");
		input_set_name.setProportion(0.24f);
		input_set_name.setEditable(true);
		input_set_name.setVisible(true);
		//"设置表简称"控件的创建，设置
		input_set_shortname = new FTextField("设置表简称");
		input_set_shortname.setProportion(0.24f);
		input_set_shortname.setEditable(true);
		input_set_shortname.setVisible(true);
		//"设置表全称"控件的创建，设置
		input_set_longname = new FTextField("设置表全称");
		input_set_longname.setProportion(0.24f);
		input_set_longname.setEditable(true);
		input_set_longname.setVisible(true);
		//"默认设置"控件的创建，设置
		is_default = new FCheckBox("默认设置");
		is_default.setProportion(0.24f);
		is_default.setEnabled(true);
		is_default.setVisible(true);
		//添加相关的监听
		input_set_name.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				prjMainDetailListener.input_set_nameValueChanged(arg0);
			}
		});
		//主表数据展示的面板的生成
		panel_data_main = new FPanel();
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(6);
		panel_data_main.setLayout(rowPreferedLayout);
		panel_data_main.setTitle("主表数据");
		//将控件按顺序添加进主面板
		panel_data_main.addControl(input_set_id, new TableConstraints(1, 1, false, true));
		panel_data_main.addControl(input_set_name, new TableConstraints(1, 2, false, true));
		panel_data_main.addControl(input_set_shortname, new TableConstraints(1, 3, false, true));
		panel_data_main.addControl(input_set_longname, new TableConstraints(1, 4, false, true));
		panel_data_main.addControl(is_default, new TableConstraints(1, 5, false, true));
		return panel_data_main;
	}

	/**
	 * 初始化并返回明细数据显示面板
	 * @return 明细数据显示面板
	 */
	private FPanel getDetailPanel() {
		//明细数据操作按钮-"设置明细"按钮的创建，设置
		btn_mod = new FButton();
		btn_mod.setTitle("设置明细");
		btn_mod.setEnabled(true);
		btn_mod.addActionListener(new RefActionListener(prjMainDetailListener, "btn_modAction"));
		//明细数据操作按钮-"上移"按钮的创建，设置
		btn_up = new FButton();
		btn_up.setTitle("↑");
		btn_up.setEnabled(true);
		btn_up.addActionListener(new RefActionListener(prjMainDetailListener, "btn_upAction"));
		//明细数据操作按钮-"下移"按钮的创建，设置
		btn_down = new FButton();
		btn_down.setTitle("↓");
		btn_down.setEnabled(true);
		btn_down.addActionListener(new RefActionListener(prjMainDetailListener, "btn_downAction"));
		//创建明细按钮面板，并将按钮按顺序添加进去
		panel_data_detail_button = new FFlowLayoutPanel();
		panel_data_detail_button.setAlignment(2);
		panel_data_detail_button.addControl(btn_mod);
		panel_data_detail_button.addControl(btn_up);
		panel_data_detail_button.addControl(btn_down);
		//明细数据展示表的创建及设置
		try {
			table_detail = new FTable();
			//按顺序添加各显示列
			table_detail.addColumn(returnFtableColumn("comp_id", "控件编码", FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("is_must_name", "是否必填", FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("comp_name", "控件名称", FBaseTableColumn.RIGHT, 100));
			table_detail.addColumn(returnFtableColumn("comp_ename", "字段名称", FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("field_type_name", "控件类型", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("is_comm", "必填字段", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("comp_min_len", "最小长度", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("comp_max_len", "最大长度", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("comp_min_value", "最小值", FBaseTableColumn.RIGHT, 80));
//			table_detail.addColumn(returnFtableColumn("comp_max_value", "最大值", FBaseTableColumn.RIGHT, 80));
			table_detail.addColumn(returnFtableColumn("is_inner", "隐藏控件", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("data_format", "格式化字符串", FBaseTableColumn.RIGHT, 80));
			table_detail.setEnabled(false);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		//放置明细数据展示表的面板的创建及放置
		panel_data_detail_table = new FPanel();
		panel_data_detail_table.setLayout(new BorderLayout());
		panel_data_detail_table.addControl(table_detail, BorderLayout.CENTER);
		//明细数据操作和展示面板均放置到明细面板内
		panel_data_detail = new FPanel();
		panel_data_detail.setLayout(new BorderLayout());
		panel_data_detail.addControl(panel_data_detail_button, BorderLayout.NORTH);
		panel_data_detail.addControl(panel_data_detail_table, BorderLayout.CENTER);
		return panel_data_detail;
	}

	/**
	 * 全部数据操作面板的创建
	 * @return 全部数据操作面板
	 */
	private FPanel getUpPanel() {
		//数据操作面板的创建
		panel_data = new FPanel();
		panel_data.setLayout(new BorderLayout());
		//上面板的放置，盛放主数据
		panel_data.addControl(this.getMainPanel(), BorderLayout.NORTH);
		//上面板的放置，盛放明细数据
		panel_data.addControl(this.getDetailPanel(), BorderLayout.CENTER);
		return panel_data;
	}

	/**
	 * 按钮面板的创建
	 * @return 按钮面板
	 */
	private FFlowLayoutPanel getDownPanel() {
		//"确定"按钮的创建，设置
		btn_ok = new FButton();
		btn_ok.setTitle("确  定");
		btn_ok.setEnabled(true);
		//"取消"按钮的创建，设置
		btn_cancel = new FButton();
		btn_cancel.setTitle("取  消");
		btn_cancel.setEnabled(true);
		//增加各按钮的监听
		btn_ok.addActionListener(new RefActionListener(prjMainDetailListener, "btn_okAction"));
		btn_cancel.addActionListener(new RefActionListener(prjMainDetailListener, "btn_cancelAction"));
		//按钮面板的创建及放置
		panel_buttons = new FFlowLayoutPanel();
		panel_buttons.setAlignment(2);
		panel_buttons.addControl(btn_ok);
		panel_buttons.addControl(btn_cancel);
		return panel_buttons;
	}

	/**
	 * 放置主面板，包括数据操作面板和按钮面板
	 */
	private void createMainPanel() {
		//按钮面板
		panel_main.addControl(this.getDownPanel(), BorderLayout.SOUTH);
		//数据操作面板
		panel_main.addControl(this.getUpPanel(), BorderLayout.CENTER);

	}

	/**
	 * 界面的初始化
	 */
	private void dgInit() {
		try {
			//主面板的创建
			panel_main = new FPanel();
			panel_main.setLayout(new BorderLayout());
			//设置主面板
			this.createMainPanel();
			this.getContentPane().add(panel_main);
			//设置本界面属性
			Dimension d = new Dimension(500, 600);
			this.setSize(d);
			this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60, (Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);
			this.setAllValues();
			//显示本界面
			this.setVisible(true);
		} catch ( Exception e ) {
			Log.error("PrjMain_DetailUI.class界面初始化失败！");
			Log.error(e.getMessage());
		}
	}

	/**
	 * 初始化各控件的值
	 */
	private void setAllValues() {
		//如果是"添加"操作，退出
		if ( operationType == PrjMainIBS.OPERATION_ADD ) {
			return;
		}
		//没有数据集退出
		if ( mainData == null ) {
			return;
		}
		try {
			//设置主表数据
			input_set_id.setValue(mainData.fieldByName("input_set_id").getString());
			input_set_name.setValue(mainData.fieldByName("input_set_name").getString());
			input_set_shortname.setValue(mainData.fieldByName("input_set_shortname").getString());
			input_set_longname.setValue(mainData.fieldByName("input_set_longname").getString());
			is_default.setValue(Boolean.valueOf(mainData.fieldByName("is_default").getString().equals("1")));
			table_detail.deleteAllRows();
			try {
				List list = prjMainService.getSetDetailValues(input_set_id.getValue().toString());
				table_detail.setData(list);
			} catch ( Exception e ) {
				Log.error(e.getMessage());
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
	}

}
