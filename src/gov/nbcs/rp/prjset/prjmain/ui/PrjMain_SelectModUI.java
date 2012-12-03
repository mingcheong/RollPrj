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
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.prjset.prjmain.action.PrjMain_SelectModListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.springframework.beans.factory.BeanFactory;


/**
 * <p>
 * Title:项目申报控件维护界面
 * </p>
 * <p>
 * Description:项目申报主界面定制时，相关控件的维护界面
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
public class PrjMain_SelectModUI extends JDialog {
	/***/
	private static final long serialVersionUID = 1L;

	/** 模块的状态-NORMAL_STATUS，ADD_STATUS，MOD_STATUS，DEL_STATUS */
	private int modStatus = PrjMainIBS.NORMAL_STATUS;

	/**
	 * @return 模块的状态-NORMAL_STATUS，ADD_STATUS，MOD_STATUS，DEL_STATUS
	 */
	public int getModStatus() {
		return modStatus;
	}

	/**
	 * 设置模块的状态，并且界面按钮功能需要做出改变
	 * 
	 * @param modStatus
	 *            模块的状态-NORMAL_STATUS，ADD_STATUS，MOD_STATUS，DEL_STATUS
	 */
	public void setModStatus(int modStatus) {
		this.modStatus = modStatus;
		// 重新设置按钮的状态
		this.setButtons();
	}

	/** 业务处理类 */
	private PrjMainIBS prjMainService;

	/**
	 * @return 业务处理类
	 */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/** 监听类 */
	private PrjMain_SelectModListener prjMain_SelectModListener;

	/*
	 * -1-------------------主面板 -1.0-----------------按钮面板
	 * -1.0.1---------------"关闭"按钮 -1.0.2---------------"添加"按钮
	 * -1.0.3---------------"删除"按钮 -1.0.4---------------"修改"按钮
	 * -1.0.5---------------"保存"按钮 -1.0.6---------------"取消"按钮
	 * -1.0.7---------------"查询"按钮 -1.0.8---------------"帮助"按钮
	 * -1.1-----------------中间面板 -1.1.1---------------中间面板左边面板，放置树形结构
	 * -1.1.1.1-------------数据显示树 -1.1.2---------------中间面板右边面板，放置数据显示区域
	 * -1.1.2.1-------------主表数据显示卡片,处于上半部分 -1.1.2.1.1-----------控件编号
	 * -1.1.2.1.2-----------控件中文名称 -1.1.2.1.3-----------控件字段名称
	 * -1.1.2.1.4-----------控件类型 1:字符串 2:整数 3:浮点数 4:下拉框 5:日期 6:时间
	 * -1.1.2.1.5-----------是否计算列 -1.1.2.1.6-----------控件最小长度
	 * -1.1.2.1.7-----------控件最大长度 -1.1.2.1.8-----------控件最小值
	 * -1.1.2.1.9-----------控件最大值 -1.1.2.1.10----------是否隐藏
	 * -1.1.2.1.11----------格式化字符串 -1.1.2.2-------------明细数据显示卡片,处于下半部分
	 * -1.1.2.2.1-----------明细数据编辑界面 -1.1.2.2.1.1---------明细数据编辑界面操作按钮面板
	 * -1.1.2.2.1.1.1-------"添加明细"按钮 -1.1.2.2.1.1.2-------"修改明细"按钮
	 * -1.1.2.2.1.1.3-------"删除明细"按钮 -1.1.2.2.1.2---------明细数据编辑界面数据填报面板
	 * -1.1.2.2.1.2.1-------下拉框对应数据的编码 -1.1.2.2.1.2.2-------下拉框对应数据的名称
	 * -1.1.2.2.2-----------明细数据显示面板 -1.1.2.2.2.1---------明细数据显示控件
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

	/** -1.0.1-"关闭"按钮 */
	private FButton btn_close = null;

	/**
	 * @return -1.0.1-"关闭"按钮
	 */
	public FButton getBtn_close() {
		return btn_close;
	}

	/** -1.0.2-"添加"按钮 */
	private FButton btn_add = null;

	/**
	 * @return -1.0.2-"添加"按钮
	 */
	public FButton getBtn_add() {
		return btn_add;
	}

	/** -1.0.3-"删除"按钮 */
	private FButton btn_del = null;

	/**
	 * @return -1.0.3-"删除"按钮
	 */
	public FButton getBtn_del() {
		return btn_del;
	}

	/** -1.0.4-"修改"按钮 */
	private FButton btn_mod = null;

	/**
	 * @return -1.0.4-"修改"按钮
	 */
	public FButton getBtn_mod() {
		return btn_mod;
	}

	/** -1.0.5-"保存"按钮 */
	private FButton btn_save = null;

	/**
	 * @return -1.0.5-"保存"按钮
	 */
	public FButton getBtn_save() {
		return btn_save;
	}

	/** -1.0.6-"取消"按钮 */
	private FButton btn_cancel = null;

	/**
	 * @return -1.0.6-"取消"按钮
	 */
	public FButton getBtn_cancel() {
		return btn_cancel;
	}

	/** -1.0.7-"查询"按钮 */
	// private FButton btn_qry = null;
	/**
	 * @return -1.0.7-"查询"按钮
	 */
	// public FButton getBtn_qry() {
	// return btn_qry;
	// }
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
	public FPanel getSpt_right() {
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

	/** -1.1.2.1.1-控件编号 */
	private FTextField comp_id = null;

	/**
	 * @return -1.1.2.1.1-控件编号
	 */
	public FTextField getComp_id() {
		return comp_id;
	}

	/** -1.1.2.1.2-控件中文名称 */
	private FTextField comp_name = null;

	/**
	 * @return -1.1.2.1.2-控件中文名称
	 */
	public FTextField getComp_name() {
		return comp_name;
	}

	/** -1.1.2.1.3-控件字段名称 */
	private FTextField comp_ename = null;

	/**
	 * @return -1.1.2.1.3-控件字段名称
	 */
	public FTextField getComp_ename() {
		return comp_ename;
	}

	/** -1.1.2.1.4-控件类型 1:字符串 2:整数 3:浮点数 4:下拉框 5:日期 6:时间 */
	private FComboBox field_type = null;

	/**
	 * @return-1.1.2.1.4-控件类型 1:字符串 2:整数 3:浮点数 4:下拉框 5:日期 6:时间
	 */
	public FComboBox getField_type() {
		return field_type;
	}

	/** -1.1.2.1.5-是否计算列 */
	// private FCheckBox is_comm = null;
	/**
	 * @return -1.1.2.1.5-是否计算列
	 */
	// public FCheckBox getIs_comm() {
	// return is_comm;
	// }
	/** -1.1.2.1.6-控件最小长度 */
	// private FIntegerField comp_min_len = null;
	/**
	 * @return -1.1.2.1.6-控件最小长度
	 */
	// public FIntegerField getComp_min_len() {
	// return comp_min_len;
	// }
	/** -1.1.2.1.7-控件最大长度 */
	// private FIntegerField comp_max_len = null;
	/**
	 * @return -1.1.2.1.7-控件最大长度
	 */
	// public FIntegerField getComp_max_len() {
	// return comp_max_len;
	// }
	/** -1.1.2.1.8-控件最小值 */
	// private FTextField comp_min_value = null;
	/**
	 * @return -1.1.2.1.8-控件最小值
	 */
	// public FTextField getComp_min_value() {
	// return comp_min_value;
	// }
	/** -1.1.2.1.9-控件最大值 */
	// private FTextField comp_max_value = null;
	/**
	 * @return -1.1.2.1.9-控件最大值
	 */
	// public FTextField getComp_max_value() {
	// return comp_max_value;
	// }
	/** -1.1.2.1.10-是否隐藏 */
	private FCheckBox is_hide = null;

	/**
	 * @return -1.1.2.1.10-是否隐藏
	 */
	public FCheckBox getIs_hide() {
		return is_hide;
	}

	/** -1.1.2.1.11-格式化字符串 */
	// private FTextField data_format = null;
	/**
	 * @return -1.1.2.1.11-格式化字符串
	 */
	// public FTextField getData_format() {
	// return data_format;
	// }
	/** -1.1.2.2-------------明细数据显示卡片,处于下半部分 */
	private FPanel panel_right_down = null;

	/**
	 * @return -1.1.2.2-------------明细数据显示卡片,处于下半部分
	 */
	public FPanel getPanel_right_down() {
		return panel_right_down;
	}

	/** -1.1.2.2.1-----------明细数据编辑界面 */
	private FPanel panel_right_down_detail = null;

	/**
	 * @return -1.1.2.2.1-----------明细数据编辑界面
	 */
	public FPanel getPanel_right_down_detail() {
		return panel_right_down_detail;
	}

	/** -1.1.2.2.1.1---------明细数据编辑界面操作按钮面板 */
	private FFlowLayoutPanel panel_right_down_buttons = null;

	/**
	 * @return -1.1.2.2.1.1---------明细数据编辑界面操作按钮面板
	 */
	public FFlowLayoutPanel getPanel_right_down_buttons() {
		return panel_right_down_buttons;
	}

	/** -1.1.2.2.2.1-"添加明细"按钮 */
	private FButton btn_adddetail = null;

	/**
	 * @return -1.1.2.2.2.1-"添加明细"按钮
	 */
	public FButton getBtn_adddetail() {
		return btn_adddetail;
	}

	/** -1.1.2.2.2.2-"修改明细"按钮 */
	private FButton btn_moddetail = null;

	/**
	 * @return -1.1.2.2.2.2-"修改明细"按钮
	 */
	public FButton getBtn_moddetail() {
		return btn_moddetail;
	}

	/** -1.1.2.2.2.3-"删除明细"按钮 */
	private FButton btn_deldetail = null;

	/**
	 * @return -1.1.2.2.2.3-"删除明细"按钮
	 */
	public FButton getBtn_deldetail() {
		return btn_deldetail;
	}

	/** -1.1.2.2.1.2---------明细数据编辑界面数据填报面板 */
	private FPanel panel_right_down_edit = null;

	/**
	 * @return -1.1.2.2.1.2---------明细数据编辑界面数据填报面板
	 */
	public FPanel getPanel_right_down_edit() {
		return panel_right_down_edit;
	}

	/** -1.1.2.2.1.2.1-下拉框对应数据的编码 */
	private FTextField item_code = null;

	/**
	 * @return -1.1.2.2.1.2.1-下拉框对应数据的编码
	 */
	public FTextField getItem_code() {
		return item_code;
	}

	/** -1.1.2.2.1.2.2-下拉框对应数据的名称 */
	private FTextField item_name = null;

	/**
	 * @return -1.1.2.2.1.2.2-下拉框对应数据的名称
	 */
	public FTextField getItem_name() {
		return item_name;
	}

	/** -1.1.2.2.2-----------明细数据显示面板 */
	private FPanel panel_right_down_table = null;

	/**
	 * @return -1.1.2.2.2-----------明细数据显示面板
	 */
	public FPanel getPanel_right_down_table() {
		return panel_right_down_table;
	}

	/** -1.1.2.2.2.1---------明细数据显示控件 */
	private FTable table_data_detail = null;

	/**
	 * @return -1.1.2.2.2.1---------明细数据显示控件
	 */
	public FTable getTable_data_detail() {
		return table_data_detail;
	}

	/**
	 * 构造函数，进行相应的初始化操作
	 */
	public PrjMain_SelectModUI() {
		// 产生标题
		super(Global.mainFrame, "控件维护", true);
		// 初始化业务处理类和监听类
		initService();
		try {
			// 初始化界面
			dgInit();
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * 初始化业务处理类和监听类
	 */
	private void initService() {
		BeanFactory beanfactory = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
		// 获得业务处理类
		prjMainService = (PrjMainIBS) beanfactory.getBean("rp.PrjMainService");
		// 创建监听类
		prjMain_SelectModListener = new PrjMain_SelectModListener(this);
	}

	/**
	 * 功能按钮的初始化:"关闭"，"增加"，"删除"等
	 */
	private void createButtons() {
		// "关闭"按钮的创建及添加
		btn_close = new FButton();
		btn_close.setTitle("关闭");
		btn_close.setIcon("images/rp/close.gif");
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
		btn_save = new FButton();
		btn_save.setTitle("保存");
		btn_save.setIcon("images/rp/save.gif");
		// "取消"按钮的创建及添加
		btn_cancel = new FButton();
		btn_cancel.setTitle("取消");
		btn_cancel.setIcon("images/rp/cancl.gif");
		// "刷新"按钮的创建及添加
		// btn_qry = new FButton();
		// btn_qry.setTitle("刷新");
		// btn_qry.setIcon("images/rp/query.gif");
		// 添加监听
		btn_close.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_closeAction"));
		btn_add.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_addAction"));
		btn_del.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_delAction"));
		btn_mod.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_modAction"));
		btn_save.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_saveAction"));
		btn_cancel.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_cancelAction"));
		// btn_qry.addActionListener(new
		// RefActionListener(prjMain_SelectModListener, "btn_qryAction"));
		// 创建按钮栏并且将按钮按顺序添加进去
		toolBar = new FToolBarPanel();
		toolBar.addControl(btn_close);
		toolBar.addSeparator();// 添加一个分隔符
		toolBar.addControl(btn_add);
		toolBar.addControl(btn_del);
		toolBar.addControl(btn_mod);
		toolBar.addControl(btn_save);
		toolBar.addControl(btn_cancel);
		// toolBar.addControl(btn_qry);
		// 按钮栏嵌入模块
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * 主面板左半部分的初始化，数据显示树
	 * 
	 * @return 主面板左半部分
	 */
	private FScrollPane getLeftPanel() {
		try {
			// 数据显示树的创建
			tree_data = new CustomTree();
			tree_data.setEditable(false);
			// 创建监听
			tree_data.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					prjMain_SelectModListener.tree_datavalueChanged(e);
				}
			});
			tree_data.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					prjMain_SelectModListener.tree_dataClicked(e);
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 左面板的创建
		spn_left = new FScrollPane();
		spn_left.addControl(tree_data);
		return spn_left;
	}

	/**
	 * 主面板右半部分上面板的初始化，显示主表数据
	 */
	private void setPanelDataMain() {
		comp_id = new FTextField("控件编号:");
		comp_id.setProportion(0.24f);
		comp_id.setEditable(false);
		comp_id.setVisible(true);
		// "控件名称"控件的创建，设置以及添加
		comp_name = new FTextField("控件名称:");
		comp_name.setProportion(0.24f);
		comp_name.setEditable(false);
		comp_name.setVisible(true);
		// "控件字段名称"控件的创建，设置以及添加
		comp_ename = new FTextField("控件字段名称:");
		comp_ename.setProportion(0.24f);
		comp_ename.setEditable(false);
		comp_ename.setVisible(true);
		// "控件类型"控件的创建，设置以及添加
		field_type = new FComboBox("控件类型:");
		field_type.setProportion(0.24f);
		field_type.setEditable(false);
		field_type.setEnabled(false);
		field_type.setRefModel("#+1#录入框+4#下拉框");
		field_type.setVisible(true);
		// "计算列"控件的创建，设置以及添加
		// is_comm = new FCheckBox("计算列:");
		// is_comm.setProportion(0.24f);
		// is_comm.setEditable(false);
		// is_comm.setVisible(true);
		// "最小长度"控件的创建，设置以及添加
		// comp_min_len = new FIntegerField("最小长度:");
		// comp_min_len.setProportion(0.24f);
		// comp_min_len.setEditable(false);
		// comp_min_len.setVisible(true);
		// // "最大长度"控件的创建，设置以及添加
		// comp_max_len = new FIntegerField("最大长度:");
		// comp_max_len.setProportion(0.24f);
		// comp_max_len.setEditable(false);
		// comp_max_len.setVisible(true);
		// // "字段最小值"控件的创建，设置以及添加
		// comp_min_value = new FTextField("字段最小值:");
		// comp_min_value.setProportion(0.24f);
		// comp_min_value.setEditable(false);
		// comp_min_value.setVisible(true);
		// // "字段最大值"控件的创建，设置以及添加
		// comp_max_value = new FTextField("字段最大值:");
		// comp_max_value.setProportion(0.24f);
		// comp_max_value.setEditable(false);
		// comp_max_value.setVisible(true);
		// "隐藏"控件的创建，设置以及添加
		is_hide = new FCheckBox("是否内置:");
		is_hide.setProportion(0.24f);
		is_hide.setEditable(false);
		is_hide.setVisible(true);
		// "字段格式"控件的创建，设置以及添加
		// data_format = new FTextField("字段格式:");
		// data_format.setProportion(0.24f);
		// data_format.setEditable(false);
		// data_format.setVisible(true);
		// 创建监听程序
		field_type.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				field_typeValueChanged(arg0);
			}
		});
		// 主表数据展示的面板的生成
		panel_right_up = new FPanel();
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(13);
		panel_right_up.setLayout(rowPreferedLayout);
		panel_right_up.setTitle("主表数据");
		panel_right_up.addControl(comp_id, new TableConstraints(1, 1, false,
				true));
		panel_right_up.addControl(comp_name, new TableConstraints(1, 2, false,
				true));
		panel_right_up.addControl(comp_ename, new TableConstraints(1, 3, false,
				true));
		panel_right_up.addControl(field_type, new TableConstraints(1, 4, false,
				true));
		// panel_right_up.addControl(is_comm, new TableConstraints(1, 5, false,
		// true));
		// panel_right_up.addControl(comp_min_len, new TableConstraints(1, 6,
		// false, true));
		// panel_right_up.addControl(comp_max_len, new TableConstraints(1, 7,
		// false, true));
		// panel_right_up.addControl(comp_min_value, new TableConstraints(1, 8,
		// false, true));
		// panel_right_up.addControl(comp_max_value, new TableConstraints(1, 9,
		// false, true));
		panel_right_up.addControl(is_hide, new TableConstraints(1, 10, false,
				true));
		// panel_right_up.addControl(data_format, new TableConstraints(1, 11,
		// false, true));
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
	 * 主面板右半部分下面板的初始化，显示从表数据
	 */
	private void setPanelDataDetail() {
		// "添加明细"按钮的创建及添加
		btn_adddetail = new FButton();
		btn_adddetail.setTitle("添加明细");
		btn_adddetail.setEnabled(true);
		btn_adddetail.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_adddetailAction"));
		// "修改明细"按钮的创建及添加
		btn_moddetail = new FButton();
		btn_moddetail.setTitle("修改明细");
		btn_moddetail.setEnabled(true);
		btn_moddetail.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_moddetailAction"));
		// "删除明细"按钮的创建及添加
		btn_deldetail = new FButton();
		btn_deldetail.setTitle("删除明细");
		btn_deldetail.setEnabled(true);
		btn_deldetail.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_deldetailAction"));
		// 创建按钮面板并将按钮放置进去
		panel_right_down_buttons = new FFlowLayoutPanel();
		panel_right_down_buttons.setAlignment(2);
		panel_right_down_buttons.addControl(btn_adddetail);
		panel_right_down_buttons.addControl(btn_moddetail);
		panel_right_down_buttons.addControl(btn_deldetail);
		// /////////////////////////////////////////////////////////////
		// "明细数据编码"控件的创建，设置以及添加
		item_code = new FTextField("明细编码:");
		item_code.setProportion(0.24f);
		item_code.setEditable(true);
		item_code.setVisible(true);
		// "明细数据名称"控件的创建，设置以及添加
		item_name = new FTextField("明细名称:");
		item_name.setProportion(0.24f);
		item_name.setEditable(true);
		item_name.setVisible(true);
		// 创建编辑面板并将控件放置进去
		RowPreferedLayout layoutPanelTexts = new RowPreferedLayout(2);
		layoutPanelTexts.setColumnWidth(15);
		layoutPanelTexts.setColumnGap(1);
		panel_right_down_edit = new FPanel();
		panel_right_down_edit.setLayout(layoutPanelTexts);
		panel_right_down_edit.addControl(item_code);
		panel_right_down_edit.addControl(item_name);
		// /////////////////////////////////////////////////////////////
		// 以上两个面板的整合
		panel_right_down_detail = new FPanel();
		panel_right_down_detail.setLayout(new BorderLayout());
		panel_right_down_detail.addControl(panel_right_down_buttons,
				BorderLayout.NORTH);
		panel_right_down_detail.addControl(panel_right_down_edit,
				BorderLayout.CENTER);
		panel_right_down_detail.setTopInset(0);
		panel_right_down_detail.setBottomInset(5);
		panel_right_down_detail.setLeftInset(5);
		panel_right_down_detail.setRightInset(5);
		// /////////////////////////////////////////////////////////////
		// 明细数据展示区域
		try {
			// 创建明细数据显示树的情况
			table_data_detail = new FTable();
			table_data_detail.addColumn(returnFtableColumn("item_code", "编码",
					FBaseTableColumn.RIGHT, 60));
			table_data_detail.addColumn(returnFtableColumn("item_name", "名称",
					FBaseTableColumn.RIGHT, 160));
			table_data_detail.setRowSelectionAllowed(true);
			table_data_detail.setColumnSelectionAllowed(false);
			table_data_detail
					.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							prjMain_SelectModListener
									.table_data_detailValueChanged(e);
						}
					});
		} catch (Exception e) {
		}
		// 明细数据展示面板的创建及放置
		panel_right_down_table = new FPanel();
		panel_right_down_table.setLayout(new BorderLayout());
		panel_right_down_table.addControl(table_data_detail,
				BorderLayout.CENTER);
		// 明细数据展示面板 和 明细数据操作面板 的整合
		panel_right_down = new FPanel();
		panel_right_down.setLayout(new BorderLayout());
		panel_right_down
				.addControl(panel_right_down_detail, BorderLayout.NORTH);
		panel_right_down
				.addControl(panel_right_down_table, BorderLayout.CENTER);
	}

	/**
	 * 主面板右半部分的初始化，明细数据分上下两个面板
	 * 
	 * @return 主面板右半部分
	 */
	private FPanel getRightPanel() {
		// 创建右面板，方便控制左右两边面板的大小
		panel_right = new FPanel();
		// 右面板主数据的创立
		this.setPanelDataMain();
		// 右面板明细数据的创立
		this.setPanelDataDetail();
		panel_right.setLayout(new BorderLayout());
		// 两个面板的放置
		panel_right.addControl(panel_right_up, BorderLayout.NORTH);
		panel_right.addControl(panel_right_down, BorderLayout.CENTER);
		return panel_right;
	}

	/**
	 * 主面板的初始化，明细数据分左右两个面板
	 */
	private void createMainPanel() {
		// 创建Split，方便控制左右两边面板的大小
		spt_main = new FSplitPane();
		spt_main.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spt_main.setResizeWeight(0.40);
		// 分别放置左面板和右面板
		spt_main.addControl(this.getLeftPanel());
		spt_main.addControl(this.getRightPanel());
		panel_main.add(spt_main);
	}

	/**
	 * 初始化界面
	 */
	private void dgInit() {
		try {
			// 创建按钮栏
			this.createButtons();
			panel_main = new FPanel();
			panel_main.setLayout(new RowPreferedLayout(1));
			// 创建主界面栏
			this.createMainPanel();
			// 窗体的相关设置
			this.getContentPane().add(panel_main);
			Dimension d = new Dimension(800, 600);
			this.setSize(d);
			this
					.setLocation(
							(Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60,
							(Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);
			this.setResizable(false);
			// 初始化某些控件的值
			this.initComponent();
			// 显示窗体
			this.setVisible(true);
		} catch (Exception e) {
			Log.error("PrjMain_SelectModUI.class界面初始化失败！");
			Log.error(e.getMessage());
		}
	}

	/** 初始化某些控件的值 */
	private void initComponent() {
		// 初始化数据表示树的值
		this.initTree();
		// 设置按钮的状态
		// 如果是下拉选择控件需要显示明细数据
		// "4"是指下拉框
		if (field_type.getValue().equals("4")) {
			// 面板布局的重新布置
			panel_right.removeAll();
			panel_right.setLayout(new BorderLayout());
			panel_right.addControl(panel_right_up, BorderLayout.NORTH);
			panel_right.addControl(panel_right_down, BorderLayout.CENTER);
			panel_right.updateUI();
		} else {
			// 面板布局的重新布置
			panel_right.removeAll();
			panel_right.setLayout(new BorderLayout());
			panel_right.addControl(panel_right_up, BorderLayout.CENTER);
			panel_right.updateUI();
		}
		this.setModStatus(PrjMainIBS.NORMAL_STATUS);
	}

	/**
	 * 初始化数据表示树的值
	 */
	public void initTree() {
		// 根据业务处理类获得树结构的xmldata
		DataSet ds_tree = prjMainService.getComponentsTreeData();
		try {
			// 设置相关信息
			tree_data.setRootName("所有控件");
			tree_data.setDataSet(ds_tree);
			tree_data.setIdName("CHR_ID");
			tree_data.setTextName("CHR_NAME");
			tree_data.setParentIdName("PARENT_ID");
			// 自动生成树形结构
			tree_data.reset();
		} catch (Exception e) {
			Log.error("初始化数据表示树错误！");
			Log.error(e.getMessage());
		}
	}

	/**
	 * 设置按钮的状态
	 */
	private void setButtons() {
		switch (modStatus) {
		// 普通状态的控件属性设置
		case PrjMainIBS.NORMAL_STATUS: {
			btn_add.setEnabled(true);
			btn_del.setEnabled(true);
			btn_mod.setEnabled(true);
			btn_save.setEnabled(false);
			btn_cancel.setEnabled(false);
			// btn_qry.setEnabled(true);
			tree_data.setEnabled(true);
			comp_id.setEditable(false);
			comp_name.setEditable(false);
			comp_ename.setEditable(false);
			field_type.setEnabled(false);
			// is_comm.setEditable(false);
			// comp_min_len.setEditable(false);
			// comp_max_len.setEditable(false);
			// comp_min_value.setEditable(false);
			// comp_max_value.setEditable(false);
			is_hide.setEditable(false);
			// data_format.setEditable(false);
			// 明细数据的显示状态
			panel_right_down.removeAll();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(panel_right_down_table,
					BorderLayout.CENTER);
			panel_right_down.updateUI();
			panel_right_down.setEnabled(false);
			// 控件类型是"下拉框"时
			// "4"是指下拉框
			if (field_type.getValue().equals("4")) {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.NORTH);
				panel_right.addControl(panel_right_down, BorderLayout.CENTER);
				panel_right.updateUI();
			} else {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.CENTER);
				panel_right.updateUI();
			}
		}
			break;
		// 添加状态的控件属性设置
		case PrjMainIBS.ADD_STATUS: {
			btn_add.setEnabled(false);
			btn_del.setEnabled(false);
			btn_mod.setEnabled(false);
			btn_save.setEnabled(true);
			btn_cancel.setEnabled(true);
			// btn_qry.setEnabled(false);
			tree_data.setEnabled(false);
			comp_id.setEditable(false);
			comp_name.setEditable(true);
			comp_ename.setEditable(false);
			field_type.setEnabled(true);
			// is_comm.setEditable(true);
			// comp_min_len.setEditable(true);
			// comp_max_len.setEditable(true);
			// comp_min_value.setEditable(true);
			// comp_max_value.setEditable(true);
			is_hide.setEditable(false);
			// data_format.setEditable(true);
			// 明细数据的显示状态
			panel_right_down.removeAll();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(panel_right_down_detail,
					BorderLayout.NORTH);
			panel_right_down.addControl(panel_right_down_table,
					BorderLayout.CENTER);
			panel_right_down.updateUI();
			panel_right_down.setEnabled(true);
			// 控件类型是"下拉框"时
			// "4"是指下拉框
			if (field_type.getValue().equals("4")) {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.NORTH);
				panel_right.addControl(panel_right_down, BorderLayout.CENTER);
				panel_right.updateUI();
			} else {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.CENTER);
				panel_right.updateUI();
			}
		}
			break;
		// 修改状态的控件属性设置
		case PrjMainIBS.MOD_STATUS: {
			btn_add.setEnabled(false);
			btn_del.setEnabled(false);
			btn_mod.setEnabled(false);
			btn_save.setEnabled(true);
			btn_cancel.setEnabled(true);
			// btn_qry.setEnabled(false);
			tree_data.setEnabled(false);
			comp_id.setEditable(false);
			comp_name.setEditable(true);
			comp_ename.setEditable(false);
			// 如果是内置不可编辑
			if (((Boolean) is_hide.getValue()).booleanValue()) {
				field_type.setEnabled(false);
			} else {
				field_type.setEnabled(true);
			}
			// is_comm.setEditable(true);
			// comp_min_len.setEditable(true);
			// comp_max_len.setEditable(true);
			// comp_min_value.setEditable(true);
			// comp_max_value.setEditable(true);
			is_hide.setEditable(false);
			// data_format.setEditable(true);
			// 明细数据的显示状态
			panel_right_down.setEnabled(true);
			panel_right_down.removeAll();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(panel_right_down_detail,
					BorderLayout.NORTH);
			panel_right_down.addControl(panel_right_down_table,
					BorderLayout.CENTER);
			panel_right_down.updateUI();
			// 控件类型是"下拉框"时
			// "4"是指下拉框
			if (field_type.getValue().equals("4")) {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.NORTH);
				panel_right.addControl(panel_right_down, BorderLayout.CENTER);
				panel_right.updateUI();
			} else {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.CENTER);
				panel_right.updateUI();
			}
		}
			break;
		// 删除状态的控件属性设置
		case PrjMainIBS.DEL_STATUS: {
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 控件类型下拉框变化时状态的变化
	 * 
	 * @param arg0
	 *            事件封装对象
	 */
	private void field_typeValueChanged(ValueChangeEvent arg0) {
		setButtons();
	}

}
