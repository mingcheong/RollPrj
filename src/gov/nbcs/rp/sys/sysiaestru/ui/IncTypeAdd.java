/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收入项目类别增加操作类
 * </p>
 * <p>
 * Description:收入项目类别增加操作类

 */
public class IncTypeAdd {
	// 收入项目类别管理客户端主界面类
	private IncType incType = null;

	// 收入项目类别对象
	private IncTypeObj incTypeObj = null;

	// 定义数据库接口
	private ISysIaeStru sysIaeStruServ = null;

	// 公共方法数据接口
	private IPubInterface iPubInterface = null;

	/**
	 * 构造函数
	 * 
	 * @param incType
	 *            收入项目类别管理客户端主界面类
	 */
	public IncTypeAdd(IncType incType) {
		this.incType = incType;
		this.incTypeObj = incType.incTypeObj;
		this.sysIaeStruServ = incType.sysIaeStruServ;
		iPubInterface = PubInterfaceStub.getMethod();
	}

	/**
	 * 增加收入项目类别操作方法
	 * 
	 * @return 返回增加操作是否成功，true:成功，false：失败
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {
		// 判断增加，是不是增加第一个叶节子，如果增加第一个叶节点，将父节点的部分信息传给子节点
		if (incTypeObj.end_flag == 1)
			incType.sSaveType = "addfirstson";
		else
			incType.sSaveType = "add";

		// 判断在节点下增加第一个子节点，如果节点已被使用，不能增加子节点
		if ("addfirstson".equals(incType.sSaveType)) {
			InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeEnableDel(
					incTypeObj.inctype_code, Global.loginYear);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(incType, infoPackage
						.getsMessage()
						+ "，不能增加子节点。", "提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}

		// 获得自动生成的编码
		String sIncTypeCode = iPubInterface.getMaxCode("fb_iae_inctype",
				"inctype_CODE", "set_Year = " + Global.loginYear,
				ISysIaeStru.iCodeLen);
		// 判断编码是否获得成功
		if (sIncTypeCode == null) {
			JOptionPane.showMessageDialog(incType, "自动生成编码失败，增加失败！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 获得自动生成的层次码
		String sLvlIdCode;
		if ("".equals(incTypeObj.lvl_id)) {

			sLvlIdCode = iPubInterface.getNodeID("fb_iae_inctype", "LVL_ID",
					incTypeObj.lvl_id, "set_Year = " + Global.loginYear
							+ " and par_id is null", incType.lvlIdRule);
		} else {
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_inctype", "LVL_ID",
					incTypeObj.lvl_id, "set_Year = " + Global.loginYear
							+ " and par_id ='" + incTypeObj.lvl_id + "'",
					incType.lvlIdRule);
		}
		// 判断层次码是否获得成功
		if (sLvlIdCode == null) {
			JOptionPane.showMessageDialog(incType, "无法增加下一级代码，已到最大可增加级次！",
					"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		incType.dsIncType.append();
		// 判断树中选中的节点是否是叶节点,如是是叶节点，将他的信息传给第一个叶节点
		incType.dsIncType.maskDataChange(true);
		incType.ftxtPriCode.setValue(sIncTypeCode);// 内码
		incType.ftxtfIncTypeCode.setValue(sLvlIdCode);// 层次编码
		incType.ftxtfIncTypeName.setValue("");// 名称
		if ("addfirstson".equals(incType.sSaveType)) {// 增加第一个子节点
		} else {// 增加子节点
			// 性质
			incType.flstIncTypeKind.setSelectedIndex(0);
			((JCheckBox) incType.fchkIsMid.getEditor()).setSelected(false);
			incType.fchkIsMid.setValue("0");
			incType.frdoIsInc.setValue("0");
		}
		incType.dsIncType.fieldByName("INCTYPE_CODE").setValue(sIncTypeCode);
		incType.dsIncType.fieldByName("END_FLAG").setValue(new Integer(1));
		incType.dsIncType.fieldByName("set_Year").setValue(Global.loginYear);
		incType.dsIncType.fieldByName("RG_CODE").setValue(
				Global.getCurrRegion());
		incType.dsIncType.maskDataChange(false);
		incType.ftxtfIncTypeName.setFocus();
		return true;
	}
}
