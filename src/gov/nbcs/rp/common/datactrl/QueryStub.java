/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl;

import com.foundercy.pf.util.sessionmanager.SessionUtil;
import gov.nbcs.rp.common.ServiceFactory;

public class QueryStub {

    /**
     * 客户端获取查询工具类的接口
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
     * 服务端获取查询工具类接口
     *
     * @return
     */
    public static Query getQueryTool() {
        return (Query) SessionUtil.getServerBean("rp.queryToolService");
    }

    /**
     * 服务端获取查询工具类接口，无事务处理
     *
     * @return
     */
    public static Query getQueryToolNT() {
        return (Query) SessionUtil.getServerBean("rp.queryTool");
    }
}
