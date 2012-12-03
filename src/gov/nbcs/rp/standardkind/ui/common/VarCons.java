/*
 * @(#)VariableConstraints.java	Feb 28, 2008
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
*/
package gov.nbcs.rp.standardkind.ui.common;
/**
 * VariableConstraints.java
 * <p>
 * Title: 变量约束
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData: Feb 28, 2008
 * </p>
 * 
 * @author GeXinying
 * @version 1.0
 */
public interface VarCons {
	//浏览
	public final static int STATE_BRO = 0;
	//增加
	public final static int STATE_ADD = 1;
	//保存
	public final static int STATE_SAVE = 2;
	//编辑
	public final static int STATE_EDIT = 3;
	//删除
	public final static int STATE_DELE = 4;
	//取消
	public final static int STATE_CANL = 5;
	//选中
	public final static int STATE_SELECT = 6;
	//选中父
	public final static int STATE_SELECT_PARENT = 7;
	
	//内置的不能修改和删除
	public final static int BUILD_IN_STATE = 1;
	
}
