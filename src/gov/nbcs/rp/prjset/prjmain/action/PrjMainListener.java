/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.action;

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMainUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_SelectModUI;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;

/**
 * <p>
 * Title:项目申报界面设置单元监听
 * </p>
 * <p>
 * Description:项目申报界面设置单元界面对应的监听类
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
public class PrjMainListener {

	/** 主界面，便于调用各属性 */
	private PrjMainUI prjMainUI = null;

	/**
	 * 清除主界面显示数据
	 */
	private void clearUICompotents() {
		// 清除主表数据
		prjMainUI.getInput_set_id().setValue("");
		prjMainUI.getInput_set_name().setValue("");
		prjMainUI.getInput_set_shortname().setValue("");
		prjMainUI.getInput_set_longname().setValue("");
		prjMainUI.getIs_default().setValue("");
		// 清除明细数据（相关的Table）
		prjMainUI.getTable_detail().deleteAllRows();
	}

	/**
	 * 设置明细表数据
	 */
	private void setTable_data_detail() {
		// 清除明细表数据
		prjMainUI.getTable_detail().deleteAllRows();
		// 判断一下选择的树节点是不是为空，或者是根节点
		if ( prjMainUI.getTree_data() != null ) {
			if ( prjMainUI.getTree_data().getSelectionPath() != null ) {
				if ( prjMainUI.getTree_data().getSelectionPath().getParentPath() != null ) {
					//当前设置编号（主键）
					String input_set_id;
					try {
						input_set_id = prjMainUI.getTree_data().getDataSet().fieldByName("input_set_id").getString();
						//获取相应的查询结果s
						List list = prjMainUI.getPrjMainService().getSetDetailValues(input_set_id);
						prjMainUI.getTable_detail().setData(list);
					} catch ( Exception e ) {
						Log.error(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 将主窗口UI传递进来，便于获取主窗口的各属性
	 * @param mainUI 主窗口UI对象
	 */
	public PrjMainListener(PrjMainUI mainUI) {
		//本地指针指向该对象
		this.prjMainUI = mainUI;
	}

	/**
	 * "添加"按钮所对应的功能
	 * @param e 事件封装对象
	 */
	public void btn_addAction(ActionEvent e) {
		//弹出维护界面
		new PrjMain_DetailUI(prjMainUI.getTree_data().getDataSet(), PrjMainIBS.OPERATION_ADD);
		//重新刷新
		btn_qryAction(e);
	}
	
	/**
	 * "关闭"按钮对应的功能
	 * @param e
	 * yangxuefeng add 20080428
	 */
	public void btn_closeAction(ActionEvent e){
		((FFrame)Global.mainFrame).closeMenu();
	}

	/**
	 * "删除"按钮所对应的功能
	 * @param e 事件封装对象
	 */
	public void btn_delAction(ActionEvent e) {
		//判断一下是否选择了相应的数据，如未选择则需提示
		if ( (prjMainUI.getTree_data().getSelectionPath() == null)
				|| (prjMainUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainUI, "请选择一个设置信息删除!", MessageBox.WARNING);
			return;
		}
		try {
			//提示是否删除
			MessageBox messageBox = new MessageBox("确定要删除所选择的设置信息吗？", MessageBox.MESSAGE, MessageBox.BUTTON_OK
					| MessageBox.BUTTON_CANCEL);
			messageBox.setVisible(true);
			if ( messageBox.getResult() == MessageBox.CANCEL ) {
				return;
			}
			messageBox = null;
			//判断一下是否可以删除选择的信息，如果不允许则提示
			String dialogResult = prjMainUI.getPrjMainService().canDeleteInputSet(prjMainUI.getInput_set_id().getValue().toString());
			if ( (dialogResult != null) && !dialogResult.equals("") ) {
				MessageBox.msgBox(prjMainUI, dialogResult, MessageBox.WARNING);
				return;
			}
			//进行删除操作，并且根据返回的信息判断是否删除成功
			String returnValue = ""
					+ prjMainUI.getPrjMainService().deleteInputSet(prjMainUI.getInput_set_id().getValue().toString());
			if ( returnValue.equals("-1") ) {
				MessageBox.msgBox(prjMainUI, "删除设置信息失败!", MessageBox.ERROR);
				return;
			}
			MessageBox.msgBox(prjMainUI, "删除设置信息成功!", MessageBox.MESSAGE);
			//重新刷新页面
			btn_qryAction(e);
		} catch ( Exception e1 ) {
			Log.error(e1.getMessage());
		}

	}

	/**
	 * "修改"按钮所对应的功能
	 * @param e 事件封装对象
	 */
	public void btn_modAction(ActionEvent e) {
		//判断一下是否选择了相应的数据，如未选择则需提示
		if ( (prjMainUI.getTree_data().getSelectionPath() == null)
				|| (prjMainUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainUI, "请选择一个设置信息进行修改!", MessageBox.WARNING);
			return;
		}
		//弹出编辑页面
		new PrjMain_DetailUI(prjMainUI.getTree_data().getDataSet(), PrjMainIBS.OPERATION_MOD);
		//重新刷新页面
		btn_qryAction(e);
	}

	/**
	 * "控件设置"按钮所对应的功能
	 * @param e 事件封装对象
	 */
	public void btn_selectmodAction(ActionEvent e) {
		//弹出明细设置界面
		new PrjMain_SelectModUI();
	}

	/**
	 * "刷新"按钮所对应的功能
	 * @param e 事件封装对象
	 */
	public void btn_qryAction(ActionEvent e) {
		//重新初始化树结构数据
		prjMainUI.initTree();
		//清除控件的值
		clearUICompotents();
	}

	/**
	 * 树节点双击操作
	 * @param e 事件封装对象
	 */
	public void tree_dataClicked(MouseEvent e) {
		//判断是否进行的是双击，不是双击则不再继续执行
		if ( e.getClickCount() != 2 ) {
			return;
		}
		//如果不是左键则退出
		if ( e.getButton() != MouseEvent.BUTTON1 ) {
			return;
		}
		//判断一下是否选择了相应的数据
		if ( (prjMainUI.getTree_data().getSelectionPath() != null)
				&& (prjMainUI.getTree_data().getSelectionPath().getParentPath() != null) ) {
			//弹出编辑页面
			new PrjMain_DetailUI(prjMainUI.getTree_data().getDataSet(), PrjMainIBS.OPERATION_MOD);
			//重新刷新页面
			btn_qryAction(null);
		}
	}

	/**
	 * 选择树节点时需要刷新相关数据
	 * @param e 事件封装对象
	 */
	public void tree_datavalueChanged(TreeSelectionEvent e) {
		try {
			//如果未选择节点或者选择根节点则清除数据
			if ( (prjMainUI.getTree_data().getSelectionPath() == null) || (prjMainUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
				clearUICompotents();
			} else {
				//选择一般节点，显示数据
				//设置主表数据
				prjMainUI.getInput_set_id().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_id").getString());
				prjMainUI.getInput_set_name().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_name").getString());
				prjMainUI.getInput_set_shortname().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_shortname").getString());
				prjMainUI.getInput_set_longname().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_longname").getString());
				prjMainUI.getIs_default().setValue(Boolean.valueOf(prjMainUI.getTree_data().getDataSet().fieldByName("is_default").getString().equals("1")));
				//设置明细表数据
				this.setTable_data_detail();
			}
		} catch ( Exception e1 ) {
			Log.error(e1.getMessage());
		}
	}
}
