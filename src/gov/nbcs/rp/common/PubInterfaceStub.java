package gov.nbcs.rp.common;

import com.foundercy.pf.util.sessionmanager.SessionUtil;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;

public class PubInterfaceStub {
	 /**
     * �������ܺ����ͻ�����Ľӿ�
     * @return
     */	
    public static IPubInterface getMethod() {
		IPubInterface pubInterface = (IPubInterface) ServiceFactory
				.getServiceInterface("rp.pubInterfaceService",
						"gov/nbcs/rp/common/pubinterface/conf/pubConf.xml");
		return pubInterface;
    }
    
    /**
     * ����˻�ȡ��ӿ�
     * @return
     */
    public static IPubInterface getServerMethod() {
		return (IPubInterface)SessionUtil.getServerBean("rp.pubInterfaceService");
	}
}