/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import com.foundercy.pf.util.Global;
public class GlobalEx extends Global {
	
	/** 所属机构类型. */
	public static final String BELONG_TYPE = "belong_type";
	
	/** 所属机构单位编码. */
	public static final String BELONG_ORG_CODE = "belong_org_code";
	
	/** The Constant BELONG_DIV_LEVEL_NUM. */
	public static final String BELONG_DIV_LEVEL_NUM = "belong_div_level_num";
	
	/** The Constant BELONG_DIV_IS_LEAF. */
	public static final String BELONG_DIV_IS_LEAF = "belong_div_is_leaf";
	
	/** The Constant KEY_USER_ID. */
	public static final String KEY_USER_ID = "##KEY_USER_ID##";

	/**
	 * 检查clientContext中的用户信息
	 */
	private static synchronized void syncClientContext() {
		String keyUserId = (String) clientContext.getAttribute(KEY_USER_ID);
		if (!Global.getUserId().equals(keyUserId)) {
			clientContext.setAttribute(BELONG_TYPE, null);
			clientContext.setAttribute(BELONG_ORG_CODE, null);	
			clientContext.setAttribute(BELONG_DIV_LEVEL_NUM, null);
			clientContext.setAttribute(BELONG_DIV_IS_LEAF, null);			

			clientContext.setAttribute(KEY_USER_ID, Global.getUserId());
		}
	}
		
	/**
	 * 获取当前登录用户所属机构类型（用于客户端）
	 */
	public static String getBelongType() {
		syncClientContext();
		String belongType = (String) clientContext.getAttribute(BELONG_TYPE);
		if ((belongType == null) || (belongType.length() == 0)) {
			// 如果没有记录，则主动获取，并保存在客户端
			// 因为平台包中未取出该值，当第一次获取是在服务器端时，该值为null
			try {
				belongType = PubInterfaceStub.getMethod().getUserBelongType(
						Global.getUserId());
				clientContext.setAttribute(BELONG_TYPE, belongType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongType == null ? "" : belongType;
	}
	
	/**
	 * 所属处室机构编码
	 * @return
	 */
	public static String getBelongDepCode() {
		syncClientContext();
		String belongBranchCode = (String) clientContext.getAttribute(BELONG_ORG_CODE);
		if ((belongBranchCode == null) || (belongBranchCode.length() == 0)) {
			// 如果没有记录，则主动获取，并保存在客户端
			// 因为平台包中未取出该值，当第一次获取是在服务器端时，该值为null
			try {
				belongBranchCode = PubInterfaceStub.getMethod()
						.getBranchCode(Global.getUserBeLong());
				clientContext.setAttribute(BELONG_ORG_CODE, belongBranchCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongBranchCode == null ? "" : belongBranchCode;}
	
	/**
	 * 获取当前登录用户所属机构单位编码（用于客户端）
	 * 
	 * @return
	 */
	public static String getBelongDivCode() {
		syncClientContext();
		String belongDivCode = (String) clientContext.getAttribute(BELONG_ORG_CODE);
		if ((belongDivCode == null) || (belongDivCode.length() == 0)) {
			// 如果没有记录，则主动获取，并保存在客户端
			// 因为平台包中未取出该值，当第一次获取是在服务器端时，该值为null
			try {
				belongDivCode = PubInterfaceStub.getMethod()
						.getEnDivCode(Global.getUserBeLong());
				clientContext.setAttribute(BELONG_ORG_CODE, belongDivCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongDivCode == null ? "" : belongDivCode;
	}
	
	public static int getBelongDivLevelNum() {
		syncClientContext();
		Integer s = (Integer) clientContext.getAttribute(BELONG_DIV_LEVEL_NUM);
		if (s == null) {
			// 如果没有记录，则主动获取，并保存在客户端
			// 因为平台包中未取出该值，当第一次获取是在服务器端时，该值为null
			try {
				int s1 = PubInterfaceStub.getMethod()
						.getEnDivLevelNum(Global.getUserBeLong());
				s = new Integer(s1);
				clientContext.setAttribute(BELONG_DIV_LEVEL_NUM, s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return s == null ? 0 : s.intValue();
	}
	
	public static int getBelongDivIsLeaf() {
		syncClientContext();
		Integer s = (Integer) clientContext.getAttribute(BELONG_DIV_IS_LEAF);
		if (s == null) {
			// 如果没有记录，则主动获取，并保存在客户端
			// 因为平台包中未取出该值，当第一次获取是在服务器端时，该值为null
			try {
				int s1 = PubInterfaceStub.getMethod()
						.getEnDivLevelNum(Global.getUserBeLong());
				s = new Integer(s1);
				clientContext.setAttribute(BELONG_DIV_IS_LEAF, s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return s == null ? 1 : s.intValue();
	}

	/**
	 * 当前登录用户是否财政用户（用于客户端）.
	 * 
	 * @return true, if 当前登录用户 is 财政用户
	 */
	public static boolean isFisVis() {
		return UntPub.FIS_VIS.equals(getBelongType());
	}
	
	/**
	 * 当前登陆用户是否主管部门用户（用户客户端）
	 * 
	 * @return true, if 当前登录用户 is 主管部门用户
	 */
	public static boolean isChargeDiv() {
		if (!isFisVis()) {
			// 判断依据是所属单位为ele_enterprise.level_num=1
			return getBelongDivLevelNum() == 1;
		}
		return false;
	}
	
	/**
	 * 是否末级单位
	 * 
	 * @return true, if 当前登录用户 is 末级单位用户
	 */
	public static boolean isLeafDiv() {
		if (!isFisVis()) {
			// 判断依据是所属单位为ele_enterprise.is_leaf=1
			return getBelongDivIsLeaf() == 1;
		}
		return false;
	}

	/**
	 * Checks if is at online server.
	 * 
	 * @return true, if is at online server
	 */
	public static boolean isServer() {
		return (Global.loginmode == 0) && (Global.loginYear == null);
	}

	/**
	 * Checks if is at online client.
	 * 
	 * @return true, if is at online client
	 */
	public static boolean isClient() {
		return (Global.loginmode == 0) && (Global.loginYear != null);
	}

	/**
	 * Checks if is offline.
	 * 
	 * @return true, if is offline
	 */
	public static boolean isOffline() {
		return Global.loginmode == 1;
	}
}
