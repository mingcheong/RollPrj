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
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_SelectModUI;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title:项目申报控件维护监听
 * </p>
 * <p>
 * Description:项目申报控件维护界面对应的监听类
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
public class PrjMain_SelectModListener {

	private static Logger log = Logger.getLogger(PrjMain_SelectModListener.class);
	//可用字体的信息列表
	private List resultList = new ArrayList();
	/**
	 * 主界面，便于调用各属性
	 */
	private PrjMain_SelectModUI prjMainSelectModUI;
	/**
	 * GeXinying
	 * @throws Exception
	 */
	private void setCompEnameValue() throws Exception {
		List hadList = prjMainSelectModUI.getPrjMainService().getFieldHadUsed();
		List allList = prjMainSelectModUI.getPrjMainService().getFieldNameByTable("RP_P_BASE");
		//
		allList.removeAll(hadList);
		resultList.clear();
		for(int i=0;i<allList.size();i++) {
			resultList.add(new FieldInfo((String)allList.get(i)));
		}
		//排序
		Collections.sort(resultList);
		if(resultList.size()>0) {
			prjMainSelectModUI.getComp_ename().setValue(((FieldInfo)resultList.get(0)).getName());
		}else {
			throw new Exception("以字母C开头的字段已经用完无法继续增加新的信息！");
		}
	}
	/**
	 * 初始化主界面数据
	 */
	private void clearUICompotents() {
		prjMainSelectModUI.getComp_id().setValue("");
		prjMainSelectModUI.getComp_name().setValue("");
		
		prjMainSelectModUI.getComp_ename().setValue("");

		//---
		prjMainSelectModUI.getField_type().setValue("");
//		prjMainSelectModUI.getIs_comm().setValue("");
//		prjMainSelectModUI.getComp_min_len().setValue("0");
//		prjMainSelectModUI.getComp_max_len().setValue("0");
//		prjMainSelectModUI.getComp_min_value().setValue("");
//		prjMainSelectModUI.getComp_max_value().setValue("");
		prjMainSelectModUI.getIs_hide().setValue("");
//		prjMainSelectModUI.getData_format().setValue("");
		// 清空明细数据表
		prjMainSelectModUI.getTable_data_detail().deleteAllRows();
	}

	/**
	 * 设置明细数据表
	 */
	private void setTable_data_detail() {
		// 清空明细数据表
		prjMainSelectModUI.getTable_data_detail().deleteAllRows();
		// 如果没有选择树节点 或者 选择根节点 则退出
		if ( prjMainSelectModUI.getTree_data().getSelectionPath() != null ) {
			if ( prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() != null ) {
				String comp_id;
				try {
					// 获取主表编号
					comp_id = prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_id").getString();
					// 获取明细数据
					List list = prjMainSelectModUI.getPrjMainService().getComboxValues(comp_id);
					// 设置明细数据
					prjMainSelectModUI.getTable_data_detail().setData(list);
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断列表是否存在相关的数据
	 * 
	 * @param xmlData
	 *            需要判断的数据
	 * @param exceptData
	 *            需要除去判断的数据
	 * @return 是否存在相关的数据
	 */
	private boolean HasItemcode(XMLData xmlData, XMLData exceptData) {
		// 获得所有明细数据
		List list = prjMainSelectModUI.getTable_data_detail().getData();
		// 获得需要判断的编号
		String sID = (String) xmlData.get("item_code");
		// 得到除去判断的编号
		String sExceptID = null;
		if ( exceptData != null ) {
			sExceptID = (String) exceptData.get("item_code");
		}
		// 如果没有相关数据直接返回false
		if ( list != null ) {
			// 对明细表数据进行遍历并判断
			for ( int i = 0 ; i < list.size() ; i++ ) {
				String data = (String) ((XMLData) list.get(i)).get("item_code");
				// 如果是原来修改的这个数据，就允许通过（也就是说不修改原有数据）
				if ( !data.equals(sExceptID) ) {
					// 如果list里面的数据包含修改的数据，就不允许通过
					if ( sID.equals(data) ) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 将主窗口UI传递进来，便于获取主窗口的各属性
	 * 
	 * @param modUI
	 *            主窗口UI对象
	 */
	public PrjMain_SelectModListener(PrjMain_SelectModUI modUI) {
		this.prjMainSelectModUI = modUI;
	}

	/**
	 * 树节点选择事件
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void tree_datavalueChanged(TreeSelectionEvent e) {
		try {
			// 如果未选择树节点或者选择根节点 初始化数据
			if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
					|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
				clearUICompotents();
			} else {
				// 根据节点数据显示出来
				prjMainSelectModUI.getComp_id().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_id").getString());
				prjMainSelectModUI.getComp_name().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_name").getString());
				prjMainSelectModUI.getComp_ename().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_ename").getString());
				prjMainSelectModUI.getField_type().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("field_type").getString());
//				prjMainSelectModUI.getIs_comm().setValue(Boolean.valueOf(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("is_comm").getString().equals("1")));
//				prjMainSelectModUI.getComp_min_len().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_min_len").getString());
//				prjMainSelectModUI.getComp_max_len().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_max_len").getString());
//				prjMainSelectModUI.getComp_min_value().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_min_value").getString());
//				prjMainSelectModUI.getComp_max_value().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_max_value").getString());
				prjMainSelectModUI.getIs_hide().setValue(Boolean.valueOf(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("is_inner").getString().equals("1")));
//				prjMainSelectModUI.getData_format().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("data_format").getString());
				// 设置明细数据
				this.setTable_data_detail();
			}
		} catch ( Exception e1 ) {
			Log.error(e1.getMessage());
		}
	}

	/**
	 * 树节点点击事件，只对双击进行操作
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void tree_dataClicked(MouseEvent e) {
		// 如果不是双击则退出
		if ( e.getClickCount() != 2 ) {
			return;
		}
		// 如果不是左键则退出
		if ( e.getButton() != MouseEvent.BUTTON1 ) {
			return;
		}
		// 如果未选择树节点或者选择根节点则退出
		if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
				|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			return;
		}
		// 得到编号
		String tmpString = null;
		try {
			tmpString = prjMainSelectModUI.getTree_data().getDataSet().fieldByName("chr_id").getString();
		} catch ( Exception e1 ) {
			e1.printStackTrace();
		}
		// 设为修改状态
		if ( (tmpString != null) && !"".equals(tmpString) ) {
			prjMainSelectModUI.setModStatus(PrjMainIBS.MOD_STATUS);
		}
	}

	/**
	 * 点击关闭按钮，执行关闭操作
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_closeAction(ActionEvent e) {
		prjMainSelectModUI.dispose();
	}

	/**
	 * 点击添加按钮，设为添加状态
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_addAction(ActionEvent e) {
		// 清除界面设置
		this.clearUICompotents();
		//
		try {
			setCompEnameValue();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(Global.mainFrame, e1.getMessage(),"新增信息", MessageBox.WARNING);
			return;
		}
		// 设为添加状态
		prjMainSelectModUI.setModStatus(PrjMainIBS.ADD_STATUS);
	}

	/**
	 * 点击删除按钮,进行删除操作
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_delAction(ActionEvent e) {
		//如果是内置的字段，不能删除
		boolean is_inner = ((Boolean)prjMainSelectModUI.getIs_hide().getValue()).booleanValue();
		if(is_inner) {
			MessageBox.msgBox(prjMainSelectModUI, "内置字段无法删除!", MessageBox.WARNING);
			return;
		}
		// 如果未选择树节点或者选择根节点则提示
		if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
				|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainSelectModUI, "请选择一个设置信息删除!", MessageBox.WARNING);
			return;
		}
		try {
			// 提示是否删除
			MessageBox messageBox = new MessageBox("确定要删除所选择的设置信息吗？", MessageBox.MESSAGE, MessageBox.BUTTON_OK
					| MessageBox.BUTTON_CANCEL);
			messageBox.setVisible(true);
			if ( messageBox.getResult() == MessageBox.CANCEL ) {
				return;
			}
			messageBox = null;
			// 获得明细书籍
			XMLData xmldata = new XMLData();
			xmldata.put("comp_id", prjMainSelectModUI.getComp_id().getValue());
			// 判断能否删除，并根据返回信息提示信息
			String dialogResult = prjMainSelectModUI.getPrjMainService().componentCanDelete(xmldata);
			if ( (dialogResult != null) && !dialogResult.equals("") ) {
				MessageBox.msgBox(prjMainSelectModUI, dialogResult, MessageBox.WARNING);
				return;
			}
			// 执行删除操作，并根据返回信息提示信息
			String returnValue = Integer.toString(prjMainSelectModUI.getPrjMainService().deleteComponent(xmldata));
			if ( returnValue.equals("-1") ) {
				MessageBox.msgBox(prjMainSelectModUI, "删除控件失败!", MessageBox.ERROR);
				return;
			}
			MessageBox.msgBox(prjMainSelectModUI, "删除控件成功!", MessageBox.MESSAGE);
			// 设置为正常状态，并且重新生成树
			prjMainSelectModUI.setModStatus(PrjMainIBS.NORMAL_STATUS);
			prjMainSelectModUI.initTree();
		} catch ( Exception e1 ) {
		}
	}

	/**
	 * 点击修改按钮，并设置为修改状态
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_modAction(ActionEvent e) {
		// 如果未选择树节点或者选择根节点则提示
		if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
				|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainSelectModUI, "请选择一个设置信息进行修改!", MessageBox.WARNING);
			return;
		}
		// 设置为修改状态
		prjMainSelectModUI.setModStatus(PrjMainIBS.MOD_STATUS);
	}

	/**
	 * 点击保存按钮并执行保存操作
	 * 
	 * @param e
	 *            事件封装对象
	 */
	public void btn_saveAction(ActionEvent e) {
		// 判断是否写控件名称的
		if (prjMainSelectModUI.getComp_name().getValue().equals("")
				|| (prjMainSelectModUI.getComp_name().getValue() == null)) {
			MessageBox.msgBox(prjMainSelectModUI, "请填写控件名称!",
					MessageBox.WARNING);
			return;
		}
		// 判断是否写控件字段名称
		if (prjMainSelectModUI.getComp_ename().getValue().equals("")
				|| (prjMainSelectModUI.getComp_name().getValue() == null)) {
			MessageBox.msgBox(prjMainSelectModUI, "请填写控件字段名称!",
					MessageBox.WARNING);
			return;
		}
		// 判断是否选择控件类型
		if (prjMainSelectModUI.getField_type().getValue().equals("")
				|| (prjMainSelectModUI.getComp_name().getValue() == null)) {
			MessageBox.msgBox(prjMainSelectModUI, "请选择控件类型!",
					MessageBox.WARNING);
			return;
		}
		// 封装页面数据
		String returnValue = null;
		XMLData xmldata = new XMLData();
		xmldata.put("comp_id", prjMainSelectModUI.getComp_id().getValue());
		xmldata.put("comp_name", prjMainSelectModUI.getComp_name().getValue());
		xmldata
				.put("comp_ename", prjMainSelectModUI.getComp_ename()
						.getValue());
		xmldata
				.put("field_type", prjMainSelectModUI.getField_type()
						.getValue());
		// xmldata.put("is_comm", prjMainSelectModUI.getIs_comm().getValue());
		// xmldata.put("comp_min_len",
		// prjMainSelectModUI.getComp_min_len().getValue());
		// xmldata.put("comp_max_len",
		// prjMainSelectModUI.getComp_max_len().getValue());
		// xmldata.put("comp_min_value",
		// prjMainSelectModUI.getComp_min_value().getValue());
		// xmldata.put("comp_max_value",
		// prjMainSelectModUI.getComp_max_value().getValue());
		xmldata.put("is_inner", prjMainSelectModUI.getIs_hide().getValue());
		// xmldata.put("data_format",
		// prjMainSelectModUI.getData_format().getValue());
		// 封装明细数据
		List tableData = null;
		// "4"是指下拉框
		if ("4".equals(prjMainSelectModUI.getField_type().getValue())) {
			tableData = prjMainSelectModUI.getTable_data_detail().getData();
		}
		switch (prjMainSelectModUI.getModStatus()) {
		// 如果是添加操作
		case PrjMainIBS.ADD_STATUS: {
			// 判断是否允许添加，并根据返回值提示信息
			String dialogResult = prjMainSelectModUI.getPrjMainService()
					.componentCanInsert(xmldata);
			if ((dialogResult != null) && !dialogResult.equals("")) {
				MessageBox.msgBox(prjMainSelectModUI, dialogResult,
						MessageBox.WARNING);
				return;
			}
			// 添加数据，并根据返回值提示是否成功信息
			returnValue = ""
					+ prjMainSelectModUI.getPrjMainService().insertComponent(
							xmldata, tableData);
			if (returnValue.equals("-1")) {
				MessageBox.msgBox(prjMainSelectModUI, "添加控件失败!",
						MessageBox.ERROR);
				return;
			} else {
				MessageBox.msgBox(prjMainSelectModUI, "添加控件成功!",
						MessageBox.MESSAGE);
			}
		}
			break;
		// 如果是修改操作
		case PrjMainIBS.MOD_STATUS: {
			// 判断是否允许修改，并根据返回值提示信息
			String dialogResult = prjMainSelectModUI.getPrjMainService()
					.componentCanUpdate(xmldata);
			if ((dialogResult != null) && !dialogResult.equals("")) {
				MessageBox.msgBox(prjMainSelectModUI, dialogResult,
						MessageBox.WARNING);
				return;
			}
			// 修改数据，并根据返回值提示是否成功信息
			returnValue = ""
					+ prjMainSelectModUI.getPrjMainService().updateComponent(
							xmldata, tableData);
			if (returnValue.equals("-1")) {
				MessageBox.msgBox(prjMainSelectModUI, "修改控件失败!",
						MessageBox.ERROR);
				return;
			} else {
				MessageBox.msgBox(prjMainSelectModUI, "修改控件成功!",
						MessageBox.MESSAGE);
			}
		}
			break;
		default: {

		}
		}
		// 设为默认状态，重新获取树
		prjMainSelectModUI.setModStatus(PrjMainIBS.NORMAL_STATUS);
		prjMainSelectModUI.initTree();
		tree_datavalueChanged(null);
	}

	/**
	 * 点击取消按钮，并取消 添加或者是修改操作
	 * @param e
	 *            事件封装对象
	 */
	public void btn_cancelAction(ActionEvent e) {
		//取消操作
		prjMainSelectModUI.setModStatus(PrjMainIBS.NORMAL_STATUS);
		tree_datavalueChanged(null);
	}

	/**
	 * 重新刷新数据
	 * @param e
	 *            事件封装对象
	 */
	public void btn_qryAction(ActionEvent e) {
		// Object selectValue = prjMainSelectModUI.getTree_data().getValue();
		try {
			prjMainSelectModUI.initTree();
		} finally {
			// prjMainSelectModUI.getTree_data().setValue(selectValue);
		}
	}
	/**
	 * 验证,编码是否为汉字
	 * @throws Exception
	 * @author GeXinying
	 */
	private void isNumberCheck() throws NumberFormatException {
		/*
		 * 编码不能为汉字,by GeXinying
		 */
		String new_code = prjMainSelectModUI.getItem_code().getValue().toString();
		long l_value=0;
		try {
			l_value = Long.parseLong(new_code);
		} catch (NumberFormatException e) {
			log.info("parse... "+l_value);
			throw new NumberFormatException("对不起，编码["+new_code+"]必须是数字！");
		}
	}
	/**
	 * 增加明细数据
	 * @param e
	 *            事件封装对象
	 */
	public void btn_adddetailAction(ActionEvent e) {
		int iRow = prjMainSelectModUI.getTable_data_detail().getCurrentRowIndex();
		//判断是否填写了编码
		if ( (prjMainSelectModUI.getItem_code().getValue() == null)
				|| prjMainSelectModUI.getItem_code().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，请先填写编码！", MessageBox.WARNING);
			return;
		}
		//判断是否填写了名称
		if ( (prjMainSelectModUI.getItem_name().getValue() == null)
				|| prjMainSelectModUI.getItem_name().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，请先填写名称！", MessageBox.WARNING);
			return;
		}
		try {
			//编码不能为汉字,by GeXinying
			isNumberCheck();
		} catch (NumberFormatException e1) {
			MessageBox.msgBox(prjMainSelectModUI, e1.getMessage(), MessageBox.WARNING);
			return;
		}
		//封装数据
		XMLData xmldata = new XMLData();
		xmldata.put("item_code", prjMainSelectModUI.getItem_code().getValue());
		xmldata.put("item_name", prjMainSelectModUI.getItem_name().getValue());
		//判断是否可以增加
		if ( this.HasItemcode(xmldata, null) ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，该编码已经存在！", MessageBox.WARNING);
			return;
		}

		//增加明细数据
		if ( iRow < 0 ) {
			prjMainSelectModUI.getTable_data_detail().addRow(xmldata);
		} else {
			prjMainSelectModUI.getTable_data_detail().addRow(xmldata, iRow + 1);
		}
	}

	/**
	 * 修改明细数据
	 * @param e
	 *            事件封装对象
	 */
	public void btn_moddetailAction(ActionEvent e) {
		int iRow = prjMainSelectModUI.getTable_data_detail().getCurrentRowIndex();
		//判断是否选择了数据
		if ( iRow < 0 ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，请先选择需要修改的数据！", MessageBox.WARNING);
			return;
		}
		//判断是否填写了编码
		if ( (prjMainSelectModUI.getItem_code().getValue() == null)
				|| prjMainSelectModUI.getItem_code().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，请先填写编码！", MessageBox.WARNING);
			return;
		}
		//判断是否填写了名称
		if ( (prjMainSelectModUI.getItem_name().getValue() == null)
				|| prjMainSelectModUI.getItem_name().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，请先填写名称！", MessageBox.WARNING);
			return;
		}
		/*
		 * 编码不能为汉字,by GeXinying
		 */
		try {
			//编码不能为汉字,by GeXinying
			isNumberCheck();
		} catch (NumberFormatException e1) {
			MessageBox.msgBox(prjMainSelectModUI, e1.getMessage(), MessageBox.WARNING);
			return;
		}
		//封装数据
		XMLData xmldata = new XMLData();
		xmldata.put("item_code", prjMainSelectModUI.getItem_code().getValue());
		xmldata.put("item_name", prjMainSelectModUI.getItem_name().getValue());
		//判断是否可以增加
		if ( this.HasItemcode(xmldata, prjMainSelectModUI.getTable_data_detail().getCurrentRow()) ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，该编码已经存在！", MessageBox.WARNING);
			return;
		}
		//修改明细数据
		prjMainSelectModUI.getTable_data_detail().deleteRow(iRow);
		prjMainSelectModUI.getTable_data_detail().addRow(xmldata, iRow);
	}

	/**
	 * 删除明细数据
	 * @param e
	 *            事件封装对象
	 */
	public void btn_deldetailAction(ActionEvent e) {
		int iRow = prjMainSelectModUI.getTable_data_detail().getCurrentRowIndex();
		//判断是否选择了数据
		if ( iRow < 0 ) {
			MessageBox.msgBox(prjMainSelectModUI, "对不起，请先选择需要删除的数据！", MessageBox.WARNING);
			return;
		}
		//删除明细数据
		prjMainSelectModUI.getTable_data_detail().deleteRow(iRow);
	}

	/**
	 * 明细数据选择时控件的变化
	 * @param e
	 *            事件封装对象
	 */
	public void table_data_detailValueChanged(ListSelectionEvent e) {
		XMLData currentRow = prjMainSelectModUI.getTable_data_detail().getCurrentRow();
		if ( currentRow != null ) {
			prjMainSelectModUI.getItem_code().setValue(currentRow.get("item_code"));
			prjMainSelectModUI.getItem_name().setValue(currentRow.get("item_name"));
		}
	}
}
