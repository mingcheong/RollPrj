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
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.paramanage.IParaManage;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

public class ToolsEx {


    /**
     * Gets the server date. 仅在服务端调用
     * 
     * <p> 注意：Tools.getServerDate() 仅可在在线版的客户端调用
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
