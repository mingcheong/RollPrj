/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import com.foundercy.pf.util.sessionmanager.SessionUtil;
import com.foundercy.pf.util.sessionmanager.UserInfoContext;

/**
 * The Class SessionUtilEx. ��չ��SessionUtil
 */
public class SessionUtilEx extends SessionUtil {

	/**
	 * ���clientContext�е��û���Ϣ
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
	 * ��ȡ��ǰ��¼�û������������ͣ����ڷ���ˣ�
	 */
	public static String getBelongType() {
		syncUserInfoContext(getUserInfoContext());
		String belongType = (String) getUserInfoContext().getAttribute(
				GlobalEx.BELONG_TYPE);
		if ((belongType == null) || (belongType.length() == 0)) {
			// ���û�м�¼����������ȡ���������ڿͻ���
			// ��Ϊƽ̨����δȡ����ֵ������һ�λ�ȡ���ڷ�������ʱ����ֵΪnull
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
	 * ��ȡ��ǰ��¼�û�����������λ���루���ڷ���ˣ�
	 * 
	 * @return
	 */
	public static String getBelongOrgCode() {
		syncUserInfoContext(getUserInfoContext());
		String belongOrgCode = (String) getUserInfoContext().getAttribute(
				GlobalEx.BELONG_ORG_CODE);
		if ((belongOrgCode == null) || (belongOrgCode.length() == 0)) {
			// ���û�м�¼����������ȡ��������
			// ��Ϊƽ̨����δȡ����ֵ������һ�λ�ȡ���ڷ�������ʱ����ֵΪnull
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
	 * ��ǰ��¼�û��Ƿ�����û������ڷ���ˣ�
	 * 
	 * @return true, if ��ǰ��¼�û� is �����û�
	 */
	public static boolean isFisVis() {
		return UntPub.FIS_VIS.equals(getBelongType());
	}
}
