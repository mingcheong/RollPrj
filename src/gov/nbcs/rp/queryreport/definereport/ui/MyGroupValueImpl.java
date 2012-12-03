/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.foundercy.pf.reportcy.summary.object.cellvalue.GroupValueImpl;
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
 * CreateData 2011-3-28
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MyGroupValueImpl extends GroupValueImpl {

	private static final long serialVersionUID = 1L;

	private String sValue = "";

	private String ename = "";

	public MyGroupValueImpl() {
		super();
	}

	public String toString() {
		return sValue;
	}

	public MyGroupValueImpl(String sValue) {
		super();
		this.sValue = sValue;
	}

	public void asXML(StringBuffer buffer) {
		super.asXML(buffer);

		if (sValue != null) {
			buffer.append("<ToString>");
			AsXMLUtil.appendCDATA(buffer, sValue);
			buffer.append("</ToString>");

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

			if ("ToString".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);

				this.sValue = tmpVal;
			}
		}

	}

	public String getSValue() {
		return sValue;
	}

	public void setSValue(String value) {
		sValue = value;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public Object clone() {
		return super.clone();
	}

}
