/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import gov.nbcs.rp.queryreport.definereport.ibs.ICustomSummaryReportBasicAttr;
import com.foundercy.pf.reportcy.summary.object.base.SummaryReportBasicAttr;
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
 * CreateDate 2009-3-3
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MySummaryReportBasicAttr extends SummaryReportBasicAttr implements
		ICustomSummaryReportBasicAttr {

	private static final long serialVersionUID = 1L;

	// 是否显示合计行,=1显示 =0不显示
	private String isShowTotalRow = "1";

	public void asXML(StringBuffer buffer) {
		super.asXML(buffer);
		
		buffer.append("<IsShowTotalRow>");
		AsXMLUtil.appendCDATA(buffer, isShowTotalRow);
		buffer.append("</IsShowTotalRow>");

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

			if ("IsShowTotalRow".equals(tmpName)) {
				String tmpVal = BaseUtils.getElementValue(element1);
				this.isShowTotalRow = tmpVal;
			}
		}
	}

	public void setIsShowTotalRow(String isShowTotalRow) {
		this.isShowTotalRow = isShowTotalRow;

	}

	public String getIsShowTotalRow() {
		return this.isShowTotalRow;
	}

}
