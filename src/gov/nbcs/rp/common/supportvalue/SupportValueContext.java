/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.supportvalue;

import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.datactrl.Query;
import gov.nbcs.rp.common.datactrl.QueryStub;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SupportValueContext {

	/** The support_values. */
	protected Map supportValues;

	/** The Constant empty_support_value. */
	public static final SupportValue empty_support_value = new SupportValue();

	/** The value is true if it is client end. */
	private boolean isClientEnd;

	/**
	 * Client.
	 * 
	 * @return a new support value context
	 */
	public static SupportValueContext newClient() {
		return new SupportValueContext(true);
	}

	/**
	 * Server.
	 * 
	 * @return a new support value context
	 */
	public static SupportValueContext newServer() {
		return new SupportValueContext(false);
	}

	/**
	 * @param isClientEnd
	 */
	public SupportValueContext(boolean isClientEnd) {
		this.isClientEnd = isClientEnd;
	}

	/**
	 * Reload support values.
	 * 
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	protected int loadSupportValues() throws Exception {
		int result = 0;

		String sql = "select SUPTYPE, N_VALUE, C_VALUE, F_VALUE, REMARK, RG_CODE, LAST_VER from FB_S_SUPPORT";
		supportValues = new MyMap();
		try {
			List lst = getQueryTool().executeQuery(sql);
			if ((lst != null) && (lst.size() > 0)) {
				SupportValue sv;
				for (Iterator itr = lst.iterator(); itr.hasNext();) {
					Map map = (Map) itr.next();
					String strSupType = (String) map.get("SUPTYPE");
					sv = populateSupportValue(map);
					if (strSupType != null) {
						supportValues.put(strSupType, sv);
					}
				}
				result = lst.size();
			} else {
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		// System.out.println(">>>> " + result + " ��Ԥ�����ϵͳ���������룡");
		return result;
	}

	/**
	 * Populate support value.
	 * 
	 * @param map
	 *            the map
	 * @return the support value
	 */
	public static SupportValue populateSupportValue(Map map) {
		String strSupType = (String) map.get("SUPTYPE");
		if (strSupType != null) {
			BigDecimal strNValue = (BigDecimal) map.get("N_VALUE");
			String strCValue = (String) map.get("C_VALUE");
			BigDecimal strFValue = (BigDecimal) map.get("F_VALUE");
			String strRemark = (String) map.get("REMARK");
			String strRgCode = (String) map.get("RG_CODE");
			String strLastVer = (String) map.get("LAST_VER");
			return new SupportValue(strSupType, strNValue, strCValue,
					strFValue, strRemark, strRgCode, strLastVer);
		} else {
			return empty_support_value;
		}
	}

	/**
	 * Gets the support value. �ͻ���Ĭ��Ϊ�����桱ȡֵ�������Ĭ��Ϊ��ʵʱ��ȡֵ��
	 * 
	 * @param supType
	 *            the sup type
	 * @return the support value
	 */
	public SupportValue getSupportValue(String supType) {
		if (supType == null) {
			return empty_support_value;
		} else {
			// �����Ϊ�գ�����ز���Ԥ��ϵͳ����
			if (supportValues == null) {
				try {
					loadSupportValues();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				// �˴�Ӧ��ͬ���������ǵ�Ч�ʺ�reloadSupportValues()�Ǿ����Ե��ã���ֻ���쳣����
				Object value = null;
				if (supportValues != null) {
					value = supportValues.get(supType);
				}
				return value == null ? empty_support_value
						: (SupportValue) value;
			} catch (Exception e) {
				e.printStackTrace();
				return empty_support_value;
			}
		}
	}

	/**
	 * Gets the query tool.
	 * 
	 * @return the query tool
	 */
	protected Query getQueryTool() {
		return isClientEnd ? QueryStub.getClientQueryTool() : QueryStub
				.getQueryTool();
	}
}
