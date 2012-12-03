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

import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.util.XMLData;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailSelectUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_SelectModUI;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title:控件选择单元监听
 * </p>
 * <p>
 * Description:控件选择单元对应的监听类
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
public class PrjMain_DetailSelectListener {
	//
	private static Logger logger  = Logger.getLogger(PrjMain_DetailSelectListener.class);

	private boolean can_value_change = false;
	/**
	 * 主界面，便于调用各属性
	 */
	private PrjMain_DetailSelectUI prjMainDetailSelectUI = null;

	/**
	 * 将主窗口UI传递进来，便于获取主窗口的各属性
	 * 
	 * @param selectUI
	 *            主窗口UI对象
	 */
	public PrjMain_DetailSelectListener(PrjMain_DetailSelectUI selectUI) {
		// 本地指针指向该对象
		this.prjMainDetailSelectUI = selectUI;
	}

	/**
	 * 点击进入设置界面
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_setAction(ActionEvent e) {
		// 进入设置界面
		new PrjMain_SelectModUI();
		//-----------------------------
		//Added by GeXinying
		// 退出后需要重新初始化界面
		// 首先得到已经选中的行
		List selected = prjMainDetailSelectUI.getTable_top().getSelectedDataByCheckBox();
		//
		this.prjMainDetailSelectUI.initTableData();
		// Added by GeXinying
		// 将已选中的行再重新选中
		// 如果没有list中的值，跳过
		Iterator iter_b = selected.iterator();
		Iterator iter_a = null;
		XMLData data_b = null;
		XMLData data_a = null;
		int index = 0;
		/**
		 * 先遍历 开始已经选中的数据信息
		 * 接着遍历 目前表中的数据
		 * 因为comp_id是不可以重复的,可以根据comp_id来定位当前表中的行
		 * 然后根据此将 已选中的数据的信息 再填写到当前与之comp_id一样的数据上
		 */
		while(iter_b.hasNext()) {
			index = 0;
			data_b = (XMLData)iter_b.next();
			iter_a = prjMainDetailSelectUI.getTable_top().getData().iterator();			
			while(iter_a.hasNext()) {
				data_a = (XMLData)iter_a.next();
				if(data_b.get("comp_id").equals(data_a.get("comp_id"))) {
					prjMainDetailSelectUI.getTable_top().setCheckBoxSelectedAtRow(index,true);
					data_a.put("is_must",data_b.get("is_must"));
					data_a.put("is_must_name",data_b.get("is_must_name"));
					prjMainDetailSelectUI.getTable_top().getData().set(index,data_b);
					logger.debug("row ---"+index);
					break;
				}
				index++;
			}
		}
		
	}

	/**
	 * 确定选择数据
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_okAction(ActionEvent e) {
		// 得到勾选的数据
		prjMainDetailSelectUI.selectReturnData();
		// 关闭
		prjMainDetailSelectUI.dispose();
	}

	/**
	 * 取消选择数据
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_cancelAction(ActionEvent e) {
		// 关闭
		prjMainDetailSelectUI.dispose();

	}

	/**
	 * 刷新明细数据
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void table_topValueChanged(ListSelectionEvent e) {
		// 设置明细数据
		prjMainDetailSelectUI.setDetailTableData();
		// 设置对应的CheckBox值（是否必须）
		XMLData xmlData = prjMainDetailSelectUI.getTable_top().getCurrentRow();
		if ( xmlData != null ) {
			Object is_must = xmlData.get("is_must");
			if ( "1".equals(is_must) ) {
				// 如果是"必须填写"的，打钩
				can_value_change = false;
				prjMainDetailSelectUI.getCbx_is_must().setValue(Boolean.valueOf(true));
				can_value_change = true;
			} else {
				// 如果不是"必须填写"的，取消打钩
				can_value_change = false;
				prjMainDetailSelectUI.getCbx_is_must().setValue(Boolean.valueOf(false));
				can_value_change = true;
			}
		}
	}


	public void cbx_is_mustValueChanged(ValueChangeEvent arg0) {
		if (!can_value_change) {return;}
		XMLData xmlData = prjMainDetailSelectUI.getTable_top().getCurrentRow();
		if ( xmlData != null ) {
			Object is_must = prjMainDetailSelectUI.getCbx_is_must().getValue();
			if ( Boolean.valueOf(is_must.toString()).booleanValue() ) {
				// 如果打钩，是"必须填写"的
				xmlData.put("is_must","1");
				xmlData.put("is_must_name","必填");
				prjMainDetailSelectUI.getTable_top().getData().set(prjMainDetailSelectUI.getTable_top().getCurrentRowIndex(),xmlData);
				prjMainDetailSelectUI.getTable_top().repaint();
			} else {
				// 如果取消打钩，不是"必须填写"的
				xmlData.put("is_must","0");
				xmlData.put("is_must_name","非必填");
				prjMainDetailSelectUI.getTable_top().getData().set(prjMainDetailSelectUI.getTable_top().getCurrentRowIndex(),xmlData);
				prjMainDetailSelectUI.getTable_top().repaint();
			}
		}
	}
}
