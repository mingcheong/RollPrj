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

import com.foundercy.pf.util.sessionmanager.SessionUtil;
import com.foundercy.pf.util.sessionmanager.UserInfoContext;

/**
 * The Class SessionUtilEx. 扩展的SessionUtil
 */
public class SessionUtilEx extends SessionUtil {

	/**
	 * 检查clientContext中的用户信息
	 */
	private static synchronized void syncUserInfoContext(
			UserInfoContext userInfoContext) {
		String keyUserId = (String) userInfoContext
				.getAttribute(GlobalEx.KEY_USER_ID);
		if (!SessionUtil.getUserInfoContext().getUserID().equals(keyUserId)) {
			userInfoContext.setAttribute(GlobalEx.BELONG_TYPE, null);
			userInfoContext.setAttribute(GlobalEx.BELONG_ORG_CODE, null);

			userInfoContext.setAttribute(GlobalEx.KEY_USER_ID, SessionUtil
					.getUserInfoContext().getUserID());
		}
	}

	/**
	 * 获取当前登录用户所属机构类型（用于服务端）
	 */
	public static String getBelongType() {
		syncUserInfoContext(getUserInfoContext());
		String belongType = (String) getUserInfoContext().getAttribute(
				GlobalEx.BELONG_TYPE);
		if ((belongType == null) || (belongType.length() == 0)) {
			// 如果没有记录，则主动获取，并保存在客户端
			// 因为平台包中未取出该值，当第一次获取是在服务器端时，该值为null
			try {
				belongType = PubInterfaceStub.getServerMethod()
						.getUserBelongType(getUserInfoContext().getUserID());
				getUserInfoContext().setAttribute(GlobalEx.BELONG_TYPE,
						belongType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongType == null ? "" : belongType;
	}

	/**
	 * 获取当前登录用户所属机构单位编码（用于服务端）
	 * 
	 * @return
	 */
	public static String getBelongOrgCode() {
		syncUserInfoContext(getUserInfoContext());
		String belongOrgCode = (String) getUserInfoContext().getAttribute(
				GlobalEx.BELONG_ORG_CODE);
		if ((belongOrgCode == null) || (belongOrgCode.length() == 0)) {
			// 如果没有记录，则主动获取，并保存
			// 因为平台包中未取出该值，当第一次获取是在服务器端时，该值为null
			try {
				if (UntPub.FIS_VIS.equals(getBelongType())) {
					belongOrgCode = PubInterfaceStub.getServerMethod()
							.getBranchCode(getUserInfoContext().getBelongOrg());
				} else {
					belongOrgCode = PubInterfaceStub.getServerMethod()
							.getEnDivCode(getUserInfoContext().getBelongOrg());
				}
				getUserInfoContext().setAttribute(GlobalEx.BELONG_ORG_CODE,
						belongOrgCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongOrgCode == null ? "" : belongOrgCode;
	}

	/**
	 * 当前登录用户是否财政用户（用于服务端）
	 * 
	 * @return true, if 当前登录用户 is 财政用户
	 */
	public static boolean isFisVis() {
		return UntPub.FIS_VIS.equals(getBelongType());
	}
}
