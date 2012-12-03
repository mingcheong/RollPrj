/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.supportvalue;

import gov.nbcs.rp.common.datactrl.Query;

import java.util.List;
import java.util.Map;


public abstract class SupportValueStore {

	/** The support_value_context. */
	protected SupportValueContext support_value_context;

	/** The support_value_store. */
	public static SupportValueStore support_value_store;

	/**
	 * The client SupportValueStore.
	 * 
	 * @return the support value store
	 */
	public static SupportValueStore client() {
		if (support_value_store == null) {
			support_value_store = new ClientSupportValueStore();
		}
		return support_value_store;
	}

	/**
	 * The server SupportValueStore.
	 * 
	 * @return the support value store
	 */
	public static SupportValueStore server() {
		if (support_value_store == null) {
			support_value_store = new ServerSupportValueStore();
		}
		return support_value_store;
	}

	/** The Constant SVConfig_XML_Location. */
	protected static final String SV_Config_XML_Location = "gov/nbcs/rp/common/supportvalue/supportvalue-conf.xml";

	/**
	 * Gets the real time support value.
	 * 
	 * @param supType
	 *            the sup type
	 * @return the real time support value
	 * @throws Exception
	 */
	public SupportValue getRealTimeSupportValue(String supType) {
		String sql = "select SUPTYPE, N_VALUE, C_VALUE, F_VALUE, REMARK, RG_CODE, LAST_VER from FB_S_SUPPORT where SUPTYPE=?";
		Object[] parameters = new Object[] { supType };
		SupportValue sv;
		try {
			List lst = getQueryTool().executeQuery2(sql, parameters);
			if ((lst != null) && (lst.size() > 0)) {
				Map map = (Map) lst.get(0);
				sv = SupportValueContext.populateSupportValue(map);
			} else {
				sv = SupportValueContext.empty_support_value;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sv = SupportValueContext.empty_support_value;
		}
		return sv;
	}

	/**
	 * Gets the support value.
	 * 
	 * @param supType
	 *            the sup type
	 * @return the support value
	 */
	public SupportValue getCachedSupportValue(String supType) {
		return support_value_context.getSupportValue(supType);
	}

	/**
	 * Gets the support value. 客户端默认为“缓存”取值；服务端默认为“实时”取值。
	 * 
	 * @param supType
	 *            the sup type
	 * @return the support value
	 */
	public abstract SupportValue getSupportValue(String supType);

	// public SupportValue rtsv(String supType) {
	// return getRealTimeSupportValue(supType);
	// }
	//
	// public SupportValue sv(String supType) {
	// return getSupportValue(supType);
	// }

	/**
	 * Gets the query tool.
	 * 
	 * @return the query tool
	 */
	protected abstract Query getQueryTool();
}
