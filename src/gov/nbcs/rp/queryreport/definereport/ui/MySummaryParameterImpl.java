/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import gov.nbcs.rp.queryreport.definereport.ibs.ICustomParameter;
import com.foundercy.pf.reportcy.summary.object.base.SummaryParameterImpl;
import com.foundercy.pf.reportcy.summary.util.AsXMLUtil;
import com.fr.base.BaseUtils;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateDate 2009-2-25
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MySummaryParameterImpl extends SummaryParameterImpl implements
		ICustomParameter {

	private static final long serialVersionUID = 1L;

	// 左括号
	private String lParenthesis = "";

	// 右括号
	private String rParenthesis = "";

	// 右边值是字段条件
	private String isFilterFlag = "";

	public MySummaryParameterImpl() {
		super();
	}

	public void asXML(StringBuffer buffer) {
		super.asXML(buffer);
		if (lParenthesis != null) {
			buffer.append("<LParenthesis>");
			AsXMLUtil.appendCDATA(buffer, lParenthesis);
			buffer.append("</LParenthesis>");
		}
		if (rParenthesis != null) {
			buffer.append("<RParenthesis>");
			AsXMLUtil.appendCDATA(buffer, rParenthesis);
			buffer.append("</RParenthesis>");

		}
		if (isFilterFlag != null) {
			buffer.append("<IsFilterFlag>");
			AsXMLUtil.appendCDATA(buffer, isFilterFlag);
			buffer.append("</IsFilterFlag>");

		}
	}

	public void readXML(Element element) {
		super.readXML(element);

		NodeList nodeList = element.getChildNodes();
		final int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node node = nodeList.item(i);
			if (!(node instanceof Element))
				continue;
			Element element1 = (Element) node;
			String tmpName = element1.getNodeName();

			if ("LParenthesis".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);

				this.lParenthesis = tmpVal;
			}
			if ("RParenthesis".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);

				this.rParenthesis = tmpVal;
			}
			if ("IsFilterFlag".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);
				this.isFilterFlag = tmpVal;
			}
		}

	}

	public String getLParenthesis() {
		return lParenthesis;
	}

	public void setLParenthesis(String parenthesis) {
		lParenthesis = parenthesis;
	}

	public String getRParenthesis() {
		return rParenthesis;
	}

	public void setRParenthesis(String parenthesis) {
		rParenthesis = parenthesis;
	}

	public String getIsFilterFlag() {
		return isFilterFlag;
	}

	public void setIsFilterFlag(String isFilterFlag) {
		this.isFilterFlag = isFilterFlag;
	}

}
