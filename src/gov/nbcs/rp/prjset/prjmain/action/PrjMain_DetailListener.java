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
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.util.XMLData;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailSelectUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailUI;

import java.awt.event.ActionEvent;
import java.util.List;

/**
 * <p>
 * Title:项目申报界面设置增加（修改）单元监听
 * </p>
 * <p>
 * Description:项目申报界面设置增加（修改）单元界面对应的监听类
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
public class PrjMain_DetailListener {

	/**
	 * 主界面，便于调用各属性 
	 */
	private PrjMain_DetailUI prjMainDetailUI;

	/**
	 * 将主窗口UI传递进来，便于获取主窗口的各属性
	 * 
	 * @param detailUI
	 *            主窗口UI对象
	 */
	public PrjMain_DetailListener(PrjMain_DetailUI detailUI) {
		// 本地指针指向该对象
		this.prjMainDetailUI = detailUI;
	}

	/**
	 * 选择控件集合
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_modAction(ActionEvent e) {
		// 创建选择界面
		PrjMain_DetailSelectUI prjMainDetailSelectUI = new PrjMain_DetailSelectUI(prjMainDetailUI.getTable_detail().getData());
		// 得到选择界面的数据并展示
		List list = prjMainDetailSelectUI.getReturnData();
		prjMainDetailUI.getTable_detail().setData(list);
	}

	/**
	 * 调整控件位置-靠前
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_upAction(ActionEvent e) {
		prjMainDetailUI.getTable_detail().moveUpCurrentRow();
	}

	/**
	 * 调整控件位置-靠后
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_downAction(ActionEvent e) {
		prjMainDetailUI.getTable_detail().moveDownCurrentRow();
	}

	/**
	 * 确定-保存数据
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_okAction(ActionEvent e) {
		// 判断是否填写了名称，如否则弹出提示
		if ( (prjMainDetailUI.getInput_set_name().getValue() == null)
				|| prjMainDetailUI.getInput_set_name().getValue().equals("") ) {
			MessageBox.msgBox(prjMainDetailUI, "对不起，请先填写名称！", MessageBox.WARNING);
			return;
		}

		// 判断是否填写了简称，如否则弹出提示
		if ( (prjMainDetailUI.getInput_set_shortname().getValue() == null)
				|| prjMainDetailUI.getInput_set_shortname().getValue().equals("") ) {
			MessageBox.msgBox(prjMainDetailUI, "对不起，请先填写简称！", MessageBox.WARNING);
			return;
		}

		// 判断是否填写了全称，如否则弹出提示
		if ( (prjMainDetailUI.getInput_set_longname().getValue() == null)
				|| prjMainDetailUI.getInput_set_longname().getValue().equals("") ) {
			MessageBox.msgBox(prjMainDetailUI, "对不起，请先填写全称！", MessageBox.WARNING);
			return;
		}
		
		//判断是否有明细数据，没有则不允许保存
		List list = prjMainDetailUI.getTable_detail().getData();
		if ( (list == null) || (list.size() == 0) ) {
			MessageBox.msgBox(prjMainDetailUI, "对不起，请先选择明细！", MessageBox.WARNING);
			return;
		}

		//根据不同的操作类型进行不同的操作
		switch ( prjMainDetailUI.getOperationType() ) {
			//添加操作
			case PrjMainIBS.OPERATION_ADD: {
				//封装数据
				XMLData xmlData = new XMLData();
				xmlData.put("input_set_id", "0");
				xmlData.put("input_set_name", prjMainDetailUI.getInput_set_name().getValue());
				xmlData.put("input_set_shortname", prjMainDetailUI.getInput_set_shortname().getValue());
				xmlData.put("input_set_longname", prjMainDetailUI.getInput_set_longname().getValue());
				xmlData.put("is_default", prjMainDetailUI.getIs_default().getValue());
				xmlData.put("detail_data", prjMainDetailUI.getTable_detail().getData());
				//添加操作
				int returnValue = prjMainDetailUI.getPrjMainService().insertPrjSetting(xmlData);
				//根据结果提示信息
				switch ( returnValue ) {
					case -1: {
						MessageBox.msgBox(prjMainDetailUI, "添加控件失败!", MessageBox.ERROR);
						return;
					}
					default: {
						MessageBox.msgBox(prjMainDetailUI, "添加控件成功!", MessageBox.MESSAGE);
					}
				}
			}
				break;
			//修改操作
			case PrjMainIBS.OPERATION_MOD: {
				//封装数据
				XMLData xmlData = new XMLData();
				xmlData.put("input_set_id", prjMainDetailUI.getInput_set_id().getValue());
				xmlData.put("input_set_name", prjMainDetailUI.getInput_set_name().getValue());
				xmlData.put("input_set_shortname", prjMainDetailUI.getInput_set_shortname().getValue());
				xmlData.put("input_set_longname", prjMainDetailUI.getInput_set_longname().getValue());
				xmlData.put("is_default", prjMainDetailUI.getIs_default().getValue());
				xmlData.put("detail_data", prjMainDetailUI.getTable_detail().getData());
				//修改操作
				int returnValue = prjMainDetailUI.getPrjMainService().updatePrjSetting(xmlData);
				//根据结果提示信息
				switch ( returnValue ) {
					case -1: {
						MessageBox.msgBox(prjMainDetailUI, "修改控件失败!", MessageBox.ERROR);
						return;
					}
					default: {
						MessageBox.msgBox(prjMainDetailUI, "修改控件成功!", MessageBox.MESSAGE);
					}
				}
			}
				break;
		}
		//关闭界面
		prjMainDetailUI.dispose();
	}

	/**
	 * 取消-退出界面
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_cancelAction(ActionEvent e) {
//		关闭界面
		prjMainDetailUI.dispose();
	}

	/**
	 * 修改名称时，全称，简称同时修改
	 * 
	 * @param arg0
	 */
	public void input_set_nameValueChanged(ValueChangeEvent arg0) {
		//同步设置简称
		prjMainDetailUI.getInput_set_shortname().setValue(prjMainDetailUI.getInput_set_name().getValue());
		//同步设置全称
		prjMainDetailUI.getInput_set_longname().setValue(prjMainDetailUI.getInput_set_name().getValue());
	}
}
