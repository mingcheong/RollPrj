/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import gov.nbcs.rp.queryreport.definereport.ibs.ICustomCalculateValueAble;
import com.foundercy.pf.reportcy.summary.object.cellvalue.CalculateValueImpl;
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
public class MyCalculateValueImpl extends CalculateValueImpl implements
		ICustomCalculateValueAble {

	private static final long serialVersionUID = 1L;

	private String sValue = "";

	// 是否向上求和,=1向上汇总 =0不向上汇总
	private String isSumUp = "1";

	private String orderIndex;

	private String orderType;

	public String getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public MyCalculateValueImpl() {
		super();
	}

	public String toString() {
		return sValue;
	}

	public MyCalculateValueImpl(String sValue) {
		super();
		this.sValue = sValue;
	}

	public String getDispContent() {
		return sValue;

	}

	public void setDispContent(String dispContent) {
		this.sValue = dispContent;

	}

	public void asXML(StringBuffer buffer) {
		super.asXML(buffer);

		if (sValue != null) {
			buffer.append("<ToString>");
			AsXMLUtil.appendCDATA(buffer, sValue);
			buffer.append("</ToString>");

		}

		// buffer.append("<IsSumUp>");
		// AsXMLUtil.appendCDATA(buffer, isSumUp);
		// buffer.append("</IsSumUp>");

		if (buffer == null)
			return;
		buffer.append("<OtherAttr");
		AsXMLUtil.append(buffer, "isSumUp", isSumUp);
		AsXMLUtil.append(buffer, "orderIndex", orderIndex);
		AsXMLUtil.append(buffer, "orderType", orderType);
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

				this.sValue = tmpVal;
			}

			// if ("IsSumUp".equals(tmpName)) {
			// String tmpVal = BaseUtils.getElementValue(element1);
			//
			// this.isSumUp = tmpVal;
			// }
			if ("OtherAttr".equals(tmpName)) {
				String s3;
				if ((s3 = BaseUtils.getAttrValue(element1, "isSumUp")) != null)
					setIsSumUp(s3);
				if ((s3 = BaseUtils.getAttrValue(element1, "orderIndex")) != null)
					setOrderIndex(s3);
				if ((s3 = BaseUtils.getAttrValue(element1, "orderType")) != null)
					setOrderType(s3);
				continue;
			}
		}

	}

	public String getIsSumUp() {
		return isSumUp;
	}

	public void setIsSumUp(String isSumUp) {
		this.isSumUp = isSumUp;
	}

}
