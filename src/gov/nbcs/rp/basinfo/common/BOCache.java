/**
 * @# BOCache.java
 */
package gov.nbcs.rp.basinfo.common;

import gov.nbcs.rp.common.ServiceFactory;
import com.foundercy.pf.util.XMLData;

/**
 * @Description ����ͳһ�����ࡣ

 */
public class BOCache {

	private static XMLData xmlBO;

	public static XMLData getXmlBO() {
		return xmlBO;
	}

	/**
	 * ȡ�÷�����
	 * 
	 * @param sFile
	 *            �����ļ���
	 * @param sName
	 *            ������ķ����� �磺ast.astUtilService;
	 * @return ����ҵ��򴴽��ɹ��򷵻ط����࣬���򷵻�NULL
	 */
	public static Object getBO(String sFile, String sName) {
		// �����ڻ�������û�д˷�����
		// Ĭ��ÿһ������������Ψһ��
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
