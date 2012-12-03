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

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.prjset.prjmain.action.PrjMain_DetailSelectListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.BeanFactory;

/**
 * <p>
 * Title:控件选择单元界面
 * </p>
 * <p>
 * Description:增加（修改）界面设置时，弹出本界面供选择需要添加的控件
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
public class PrjMain_DetailSelectUI extends JDialog {

	private static final long serialVersionUID = 1L;

	/** 业务处理类 IBS */
	private PrjMainIBS prjMainService = null;

	/**
	 * @return 业务处理类 IBS
	 */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/** 返回的数据集 */
	private List returnData = null;

	/**
	 * @return 返回的数据集
	 */
	public List getReturnData() {
		return returnData;
	}

	/** 所有明细数据的本地集，便于更新 */
	private List allDetailList = null;

	/**
	 * 监听类
	 */
	private PrjMain_DetailSelectListener prjMainDetailSelectListener = null;

	/*
	 * -1-------------------主面板 -1.1-----------------中间Split面板
	 * -1.1.1---------------上选择面板 -1.1.1.1-------------上选择Table
	 * -1.1.1.2-------------上编辑面板 -1.1.1.2.1-----------是否必填
	 * -1.1.2---------------明细数据面板 -1.1.2.1-------------明细数据Table
	 * -1.2-----------------按钮面板 -1.2.1---------------"设置"按钮
	 * -1.2.2---------------"确定"按钮 -1.2.3---------------"取消"按钮
	 */
	/** -1-------------------主面板 */
	private FPanel panel_main = null;

	/**
	 * @return -1-------------------主面板
	 */
	public FPanel getPanel_main() {
		return panel_main;
	}

	/** -1.1-----------------中间Split面板 */
	private FSplitPane spt_main = null;

	/**
	 * @return -1.1-----------------中间Split面板
	 */
	public FSplitPane getSpt_main() {
		return spt_main;
	}

	/** -1.1.1---------------上选择面板 */
	private FPanel panel_top = null;

	/**
	 * @return -1.1.1---------------上选择面板
	 */
	public FPanel getPanel_top() {
		return panel_top;
	}

	/** -1.1.1.1-------------上选择Table */
	private FTable table_top = null;

	/**
	 * @return -1.1.1.1-------------上选择Table
	 */
	public FTable getTable_top() {
		return table_top;
	}

	/** -1.1.1.2-------------上编辑面板 */
	private FPanel panel_top_edit = null;

	/**
	 * @return -1.1.1.2-------------上编辑面板
	 */
	public FPanel getPanel_top_edit() {
		return panel_top_edit;
	}

	/** -1.1.1.2.1-----------是否必填 */
	private FCheckBox cbx_is_must = null;

	/**
	 * @return -1.1.1.2.1-----------是否必填
	 */

	public FCheckBox getCbx_is_must() {
		return cbx_is_must;
	}

	/** -1.1.2---------------明细数据面板 */
	private FPanel panel_center = null;

	/**
	 * @return -1.1.2---------------明细数据面板
	 */
	public FPanel getPanel_center() {
		return panel_center;
	}

	/** -1.1.2.1-------------明细数据Table */
	private FTable table_center = null;

	/**
	 * @return -1.1.2.1-------------明细数据Table
	 */
	public FTable getTable_center() {
		return table_center;
	}

	/** -1.2-----------------按钮面板 */
	private FFlowLayoutPanel panel_bottom = null;

	/**
	 * @return -1.2-----------------按钮面板
	 */
	public FFlowLayoutPanel getPanel_bottom() {
		return panel_bottom;
	}

	/** -1.2.1---------------"设置"按钮 */
	private FButton btn_set = null;

	/**
	 * @return -1.2.1---------------"设置"按钮
	 */
	public FButton getBtn_set() {
		return btn_set;
	}

	/** -1.2.2---------------"确定"按钮 */
	private FButton btn_ok = null;

	/**
	 * @return -1.2.2---------------"确定"按钮
	 */
	public FButton getBtn_ok() {
		return btn_ok;
	}

	/** -1.2.3---------------"取消"按钮 */
	private FButton btn_cancel = null;

	/**
	 * @return -1.2.3---------------"取消"按钮
	 */
	public FButton getBtn_cancel() {
		return btn_cancel;
	}

	/**
	 * 清除需要返回的数据
	 */
	public void clearReturnData() {
		this.returnData = null;
	}

	/**
	 * 根据打钩的选项重新选择数据
	 */
	public void selectReturnData() {
		this.returnData = this.table_top.getSelectedDataByCheckBox();
	}

	/**
	 * 构造函数
	 * 
	 * @param data
	 *            传递进来的数据集，用来构造默认显示
	 */
	public PrjMain_DetailSelectUI(List data) {
		// 初始化界面，并显示标题
		super(Global.mainFrame, "选择控件", true);
		// 初始化服务，监听等对象
		initService();
		try {
			// 默认返回值是传递进来的值
			this.returnData = data;
			// 初始化界面
			dgInit(data);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * 初始化服务，监听等对象
	 */
	private void initService() {
		// 初始化业务操作类
		BeanFactory beanfactory = BeanFactoryUtil.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
		prjMainService = (PrjMainIBS) beanfactory.getBean("rp.PrjMainService");
		// 初始化监听
		prjMainDetailSelectListener = new PrjMain_DetailSelectListener(this);
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
	 * 获取主数据面板
	 * 
	 * @return 主数据面板
	 */
	private FPanel getUpPanel() {
		try {
			// 创建展示Table控件
			table_top = new FTable(false);
			// 增加Checkbox选择控件
			table_top.setIsCheck(true);
			table_top.addColumn(returnFtableColumn("comp_id", "控件编码", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("is_must_name", "是否必填", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_name", "控件名称", FBaseTableColumn.RIGHT, 100));
			table_top.addColumn(returnFtableColumn("comp_ename", "字段名称", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("field_type_name", "控件类型", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("is_comm", "必填字段", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_min_len", "最小长度", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_max_len", "最大长度", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_min_value", "最小值", FBaseTableColumn.RIGHT, 80));
			table_top.addColumn(returnFtableColumn("comp_max_value", "最大值", FBaseTableColumn.RIGHT, 80));
			table_top.addColumn(returnFtableColumn("is_hide", "隐藏控件", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("data_format", "格式化字符串", FBaseTableColumn.RIGHT, 80));
			// 监听程序
			table_top.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					prjMainDetailSelectListener.table_topValueChanged(e);
				}
			});
			table_top.setCheckBoxAffectedByClickRow(false);
		} catch ( Exception e ) {
		}
		cbx_is_must = new FCheckBox("必填");
		cbx_is_must.setEnabled(true);
		cbx_is_must.addValueChangeListener(new ValueChangeListener(){

			public void valueChanged(ValueChangeEvent arg0) {
				prjMainDetailSelectListener.cbx_is_mustValueChanged(arg0);
			}
			
		});
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(2);
		panel_top_edit = new FPanel();
		panel_top_edit.setLayout(rowPreferedLayout);
		panel_top_edit.addControl(cbx_is_must, new TableConstraints(1, 1, false, true));
		// 主数据面板的初始化，并且放置相关Table
		panel_top = new FPanel();
		panel_top.setLayout(new BorderLayout());
		panel_top.addControl(panel_top_edit, BorderLayout.SOUTH);
		panel_top.addControl(table_top, BorderLayout.CENTER);
		return panel_top;
	}

	/**
	 * 获取明细面板数据
	 * 
	 * @return 明细面板
	 */
	private FPanel getDownPanel() {
		try {
			// 创建展示Table控件
			table_center = new FTable(false);
			table_center.addColumn(returnFtableColumn("item_code", "编码", FBaseTableColumn.RIGHT, 60));
			table_center.addColumn(returnFtableColumn("item_name", "名称", FBaseTableColumn.RIGHT, 160));
			table_center.setRowSelectionAllowed(true);
			table_center.setColumnSelectionAllowed(false);
		} catch ( Exception e ) {
		}
		// 明细数据面板的初始化，并且放置相关Table
		panel_center = new FPanel();
		panel_center.setLayout(new BorderLayout());
		panel_center.addControl(table_center, BorderLayout.CENTER);
		return panel_center;
	}

	/**
	 * 数据面板（可以调整大小的Split）的创建
	 * 
	 * @return 数据面板
	 */
	private FSplitPane getSplitPanel() {
		// 面板的创建
		spt_main = new FSplitPane();
		spt_main.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spt_main.setResizeWeight(0.60);
		// 放置上面板
		spt_main.addControl(this.getUpPanel());
		// 放置面板
		spt_main.addControl(this.getDownPanel());
		return spt_main;
	}

	/**
	 * 按钮面板的创建
	 * 
	 * @return 按钮面板
	 */
	private FFlowLayoutPanel getButtonPanel() {
		// "设置"按钮的创建和相关设置
		btn_set = new FButton();
		btn_set.setTitle("设  置");
		btn_set.setEnabled(true);
		// "确定"按钮的创建和相关设置
		btn_ok = new FButton();
		btn_ok.setTitle("确  定");
		btn_ok.setEnabled(true);
		// "取消"按钮的创建和相关设置
		btn_cancel = new FButton();
		btn_cancel.setTitle("取  消");
		btn_cancel.setEnabled(true);
		// 添加相关的监听程序
		btn_set.addActionListener(new RefActionListener(prjMainDetailSelectListener, "btn_setAction"));
		btn_ok.addActionListener(new RefActionListener(prjMainDetailSelectListener, "btn_okAction"));
		btn_cancel.addActionListener(new RefActionListener(prjMainDetailSelectListener, "btn_cancelAction"));
		// 按钮面板的创建和放置
		panel_bottom = new FFlowLayoutPanel();
		panel_bottom.setAlignment(2);
		panel_bottom.addControl(btn_set);
		panel_bottom.addControl(btn_ok);
		panel_bottom.addControl(btn_cancel);
		return panel_bottom;
	}

	/**
	 * 窗体的面板布局
	 */
	private void createMainPanel() {
		// 按钮面板的放置
		panel_main.addControl(this.getButtonPanel(), BorderLayout.SOUTH);
		// huju面板的放置
		panel_main.addControl(this.getSplitPanel(), BorderLayout.CENTER);
	}

	/**
	 * 界面的初始化
	 * 
	 * @param data
	 */
	private void dgInit(List data) {
		try {
			// 主面板的创建
			panel_main = new FPanel();
			panel_main.setLayout(new BorderLayout());
			// 创建相关面板并放置进主窗体
			this.createMainPanel();
			this.getContentPane().add(panel_main);
			// 主窗体的其它设置
			Dimension d = new Dimension(600, 500);
			this.setSize(d);
			this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60, (Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);
			// 初始化表数据（主表数据及从表的缓存）
			this.initTableData();
			// 根据默认值设定打钩情况
			this.setTableData(data);
			// 显示窗体
			this.setVisible(true);
		} catch ( Exception e ) {
			Log.error("PrjMain_DetailSelectUI.class界面初始化失败！");
			Log.error(e.getMessage());
		}
	}

	/**
	 * 初始化表数据
	 */
	public void initTableData() {
		// 先删除主表数据
		this.table_top.deleteAllRows();
		// 获取主表的数据集
		List list = this.prjMainService.getAllComponetsList();
		// 设置主表的数据集
		this.table_top.setData(list);
		// 获取从表的数据集
		allDetailList = this.prjMainService.getAllComBoxValueList();
	}

	/**
	 * 设置主表打钩情况
	 * 
	 * @param data
	 */
	private void setTableData(List data) {
		// 所有数据的编号列表
		List allItem = new Vector();

		// 打钩数据的编号列表
		List selectItemID = new Vector();
		List selectItemIsMust = new Vector();
		List selectItemIsMustName = new Vector();
		// 获取所有数据的编号列表
		if ( table_top.getData() != null ) {
			for ( int i = 0 ; i < this.table_top.getData().size() ; i++ ) {
				String s = ((XMLData) table_top.getData().get(i)).get("comp_id").toString();
				allItem.add(s);

			}
		}
		// /获取打钩数据的编号列表
		if ( data != null ) {
			for ( int i = 0 ; i < data.size() ; i++ ) {
				String s = ((XMLData) data.get(i)).get("comp_id").toString();
				selectItemID.add(s);
				s = ((XMLData) data.get(i)).get("is_must").toString();
				selectItemIsMust.add(s);
				s = ((XMLData) data.get(i)).get("is_must_name").toString();
				selectItemIsMustName.add(s);
			}
		}
		// 判断并且对数据进行打钩
		for ( int i = 0 ; i < allItem.size() ; i++ ) {
			table_top.setCheckBoxSelectedAtRow(i, false);
			// 判断是否存在打钩的数据
			int index = selectItemID.indexOf(allItem.get(i));
			if ( index != -1 ) {
				Map map = (Map) table_top.getData().get(i);
				map.put("is_must", selectItemIsMust.get(index));
				map.put("is_must_name", selectItemIsMustName.get(index));
				table_top.getData().set(i, map);
				table_top.setCheckBoxSelectedAtRow(i, true);
			}
		}
	}

	/**
	 * 设置明细表的数据
	 */
	public void setDetailTableData() {
		// 先删除所有列
		table_center.deleteAllRows();
		// 获取当前主表数据
		XMLData currentRow = table_top.getCurrentRow();
		// 如果没有主表数据，退出
		if ( currentRow == null ) {
			return;
		}
		// 得到控件编号
		String comp_id = currentRow.get("comp_id").toString();
		List list = new Vector();
		// 循环得到相关明细数据
		for ( int i = 0 ; i < this.allDetailList.size() ; i++ ) {
			String newComp_id = ((Map) allDetailList.get(i)).get("comp_id").toString();
			// 判断是否改控件对应的数据
			if ( comp_id.equals(newComp_id) ) {
				XMLData xmlData = new XMLData();
				xmlData.put("comp_id", ((Map) allDetailList.get(i)).get("comp_id"));
				xmlData.put("item_code", ((Map) allDetailList.get(i)).get("item_code"));
				xmlData.put("item_name", ((Map) allDetailList.get(i)).get("item_name"));
				list.add(xmlData);
			}
		}
		// 设置Data
		this.table_center.setData(list);
	}

}
