package gov.nbcs.rp.sys.sysrefcol.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * 引用列管理连接数据库类
 * 

 * 
 */
public class SysRefColI {

	public static ISysRefCol getMethod() {
		ISysRefCol sysRefColServ = (ISysRefCol) ServiceFactory
				.getServiceInterface("rp.sysRefColService");
		return sysRefColServ;
	}

	/**
	 * 服务端获取类接口
	 * 
	 * @return
	 */
	public static ISysRefCol getServerMethod() {
		return (ISysRefCol) SessionUtil.getServerBean("rp.sysRefColService");
	}

}
