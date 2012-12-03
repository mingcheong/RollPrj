/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.io.PrintWriter;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.foundercy.pf.reportcy.common.util.StringEx;
import com.foundercy.pf.reportcy.common.util.TextXML;
import com.fr.base.BaseUtils;
import com.fr.report.cellElement.Formula;

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
 * CreateDate 2011-9-4
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class FundSourceImpl extends Formula {

	public void readXML(Element element) {
		super.readXML(element);
		NodeList nodeList = element.getChildNodes();
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node node = nodeList.item(i);
			if (!(node instanceof Element))
				continue;
			Element tmpElem = (Element) node;
			String tmpName = tmpElem.getNodeName();
			if ("Attributes".equals(tmpName)) {
				String tmpVal;
				if ((tmpVal = BaseUtils.getAttrValue(tmpElem, "displayName")) != null)
					setDisplayName(tmpVal);
				continue;
			}
			if ("DisplayName".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(tmpElem);
				setDisplayName(tmpVal);
			}
			if ("CompareFlag".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(tmpElem);
				setCompareFlag(tmpVal);
			}
		}

	}

	public void writeXML(PrintWriter writer) {
		super.writeXML(writer);
		if (!StringEx.isEmpty(getDisplayName()))
			TextXML.writeCDATA(writer, "DisplayName", getDisplayName());
		if (!StringEx.isEmpty(getCompareFlag()))
			TextXML.writeCDATA(writer, "CompareFlag", getCompareFlag());
	}

	public FundSourceImpl() {
		super("");
	}

	public FundSourceImpl(String content) {
		super(content);
	}

	public FundSourceImpl(String content, String displayName) {
		super(content);
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String toString() {
		String tmp = "";
		if (StringEx.isEmpty(getDisplayName()))
			tmp = getContent().substring(1);
		else if (getDisplayName().trim().startsWith("="))
			tmp = getDisplayName();
		else
			tmp = "=" + getDisplayName();
		return tmp;
	}

	public String getContent() {
		return super.getContent();
	}

	public Object getResult() {
		return super.getResult();
	}

	private String displayName;

	// 对比分析表使用，表对比1，对比2
	private String compareFlag;

	private static final long serialVersionUID = -3191455886419506572L;

	public String getCompareFlag() {
		return compareFlag;
	}

	public void setCompareFlag(String compareFlag) {
		this.compareFlag = compareFlag;
	}
}
