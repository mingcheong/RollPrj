/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import gov.nbcs.rp.queryreport.definereport.ibs.ICustomStatisticCaliber;
import com.foundercy.pf.reportcy.summary.object.cellvalue.SummaryStatisticCaliberImpl;
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
 * CreateData 2011-3-30
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MySummaryStatisticCaliberImpl extends SummaryStatisticCaliberImpl
		implements ICustomStatisticCaliber {

	private static final long serialVersionUID = 1L;

	private String context = "";

	// 左括号
	private String lParenthesis = "";

	// 右括号
	private String rParenthesis = "";

	// 枚举ID
	protected String enumID;

	// 选中节点ID
	protected String nodeID;

	public String toString() {
		return context;
	}

	public MySummaryStatisticCaliberImpl() {
		super();
	}

	public MySummaryStatisticCaliberImpl(String value) {
		super();
		this.context = value;
	}

	public void asXML(StringBuffer buffer) {
		super.asXML(buffer);

		if (context != null) {
			buffer.append("<ToString>");
			AsXMLUtil.appendCDATA(buffer, context);
			buffer.append("</ToString>");
		}

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

		if (buffer == null)
			return;
		buffer.append("<FilterEnum");
		AsXMLUtil.append(buffer, "enumID", enumID);
		AsXMLUtil.append(buffer, "nodeID", nodeID);
		buffer.append("/>");
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

			if ("ToString".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);

				this.context = tmpVal;
			}
			if ("LParenthesis".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);

				this.lParenthesis = tmpVal;
			}
			if ("RParenthesis".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);

				this.rParenthesis = tmpVal;
			}

			if ("FilterEnum".equals(tmpName)) {
				String s3;
				if ((s3 = BaseUtils.getAttrValue(element1, "enumID")) != null)
					setEnumID(s3);
				if ((s3 = BaseUtils.getAttrValue(element1, "nodeID")) != null)
					setNodeID(s3);
				continue;
			}
		}

	}

	public String getContext() {
		return context;
	}

	public void setContext(String value) {
		this.context = value;
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

	public String getEnumID() {
		return enumID;
	}

	public void setEnumID(String enumID) {
		this.enumID = enumID;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

}
