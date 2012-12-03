/**
 * @# BOCache.java
 */
package gov.nbcs.rp.basinfo.common;

import gov.nbcs.rp.common.ServiceFactory;
import com.foundercy.pf.util.XMLData;

/**
 * @Description 服务统一处理类。

 */
public class BOCache {

	private static XMLData xmlBO;

	public static XMLData getXmlBO() {
		return xmlBO;
	}

	/**
	 * 取得服务类
	 * 
	 * @param sFile
	 *            配置文件名
	 * @param sName
	 *            服务类的服务名 如：ast.astUtilService;
	 * @return 如果找到或创建成功则返回服务类，否则返回NULL
	 */
	public static Object getBO(String sFile, String sName) {
		// 先找在缓存中有没有此服务类
		// 默认每一个服务类名是唯一的
		if (xmlBO == null) {
			xmlBO = new XMLData();
		}

		Object obj = xmlBO.get(sName);
		if (obj != null)
			return obj;
		else {
			// BeanFactory factoryBean;
			// factoryBean = BeanFactoryUtil.getBeanFactory(sFile);
			try {
				obj = ServiceFactory.getServiceInterface(sName, sFile);
				xmlBO.put(sName, obj);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return obj;
		}

	}

	public static Object getBO(String sName) {
		return ServiceFactory.getServiceInterface(sName);
	}

}
