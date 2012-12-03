/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl;

import com.foundercy.pf.util.sessionmanager.SessionUtil;
import gov.nbcs.rp.common.ServiceFactory;

public class QueryStub {

    /**
     * �ͻ��˻�ȡ��ѯ������Ľӿ�
     *
     * @return
     */
    public static Query getClientQueryTool() {
        Query qry = (Query) ServiceFactory
				.getServiceInterface("rp.queryToolService",
						"gov/nbcs/rp/common/datactrl/conf/datactrlConf.xml");
		return qry;
    }

    /**
     * ����˻�ȡ��ѯ������ӿ�
     *
     * @return
     */
    public static Query getQueryTool() {
        return (Query) SessionUtil.getServerBean("rp.queryToolService");
    }

    /**
     * ����˻�ȡ��ѯ������ӿڣ���������
     *
     * @return
     */
    public static Query getQueryToolNT() {
        return (Query) SessionUtil.getServerBean("rp.queryTool");
    }
}
