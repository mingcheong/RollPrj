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

import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.paramanage.IParaManage;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

public class ToolsEx {


    /**
     * Gets the server date. ���ڷ���˵���
     * 
     * <p> ע�⣺Tools.getServerDate() ���������߰�Ŀͻ��˵���
     * 
     * @return the server date 
     */
    public static String getServerDate()
    {
    	if (Global.loginmode == 0) {
            IParaManage iparamanage = (IParaManage)SessionUtil.getServerBean("sys.paraManService");
            return iparamanage.getServerTime();
		} else {
			return Tools.getCurrDate();
		}
    }

}
