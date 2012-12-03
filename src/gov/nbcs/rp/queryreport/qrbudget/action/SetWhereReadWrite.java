/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.common.XmlTools;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.util.Global;

/**

 */
public class SetWhereReadWrite {
	private final static String USER_ID = "UserId";

	private final static String REPORT_ID = "ReportId";

	private final static String LEVEL_INFO = "LeveLInfo";

	private final static String SUMMARY_ID = "SummaryID";

	private final static String SOURCE_ID = "SourceID";

	private final static String SOURCECOL_ID = "SourceColID";

	private final static String SUMMARY_INDEX = "SummaryIndex";

	private final static String ISMB_SUMMARY = "IsMbSummary";

	private final static String LEVEL = "Level";

	private final static String IS_TOTAL = "IsTotal";

	private final static String ORDER_INDEX = "OrderIndex";

	private final static String PFS_CODE_FIELD = "PfsCodeField";

	private final static String PFS_CODE_FILTER = "PfsCodeFilter";

	private final static String ONE_BATCHNO = "OneBatchNo";

	private final static String ONE_DATATYPE = "OneDataType";

	private final static String ONE_YEAR = "OneYear";

	private final static String TWO_BATCHNO = "TwoBatchNo";

	private final static String TWO_DATATYPE = "TwoDataType";

	private final static String TWO_YEAR = "TwoYear";

	private final static String MID_LINE = "-";

	private final static String PFS_FIELDFNAME = "PfsFieldFname";

	private final static String PROMPT_INFO = "PromptInfo";

	// 报表信息是否存在
	private boolean isReportExists = false;

	private Document document;

	private Element rootElement;

	private Element userIdelement;

	private Element reportElement;

	private String reportId;

	private static String PARA_FILE_NAME = IDefineReport.PATH_
			+ "reportPara.xml";

	public SetWhereReadWrite(String reportId) {
		this.reportId = reportId;
		File filePath = new File(PARA_FILE_NAME);
		if (!filePath.exists()) {
			return;
		}
		try {
			document = XmlTools.read(filePath.toString());
			rootElement = document.getRootElement();
			if (rootElement == null)
				return;
			userIdelement = rootElement.element(USER_ID + MID_LINE
					+ Global.getUserId());
			if (userIdelement == null)
				return;
			reportElement = userIdelement.element(REPORT_ID + MID_LINE
					+ reportId);
			if (reportElement == null)
				return;
			isReportExists = true;
		} catch (DocumentException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.currentPanel,
					"读取本地参数保存文件发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 检查报表参数
	 * 
	 * @return
	 */
	public boolean checkFilePara(List lstLevIsTotalField) {
		boolean result = this.checkReportPara(lstLevIsTotalField);
		if (!result) {
			delReportParaWithReportId(this.reportId);
		}
		return result;
	}

	/**
	 * 删除本地文件中某一报表参数,根据报表ID
	 * 
	 * @param reportId
	 */
	private void delReportParaWithReportId(String reportId) {
		Element rootElement = document.getRootElement();
		if (rootElement == null)
			return;
		Element userIdelement = rootElement.element(USER_ID + MID_LINE
				+ Global.getUserId());
		if (userIdelement == null)
			return;
		reportElement = userIdelement.element(REPORT_ID + MID_LINE + reportId);
		if (reportElement != null) {
			userIdelement.remove(reportElement);
		}
	}

	/**
	 * 判断本地参数文件和报表中lev字段是否一样，如不一样，表示报表被修改过，本地参数文件中删除该报表参数，
	 * 否则修改lstLevIsTotalField中内容为本地参数文件信息内容
	 * 
	 * @return true :内容一致, false :内容不一致
	 */
	private boolean checkReportPara(List lstLevIsTotalField) {
		if (!isReportExists)
			return false;
		Element levElement = reportElement.element(LEVEL_INFO);
		List lstElements = levElement.content();
		List lstAttribute;
		Element element;
		// 判断值是否为空
		if (lstElements == null || lstElements.isEmpty()) {
			if (lstLevIsTotalField == null || lstLevIsTotalField.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (lstLevIsTotalField == null || lstLevIsTotalField.isEmpty()) {
				return false;
			} else {
				// 数量是否相同
				if (lstElements.size() == lstLevIsTotalField.size()) {
					// 判断内容是否相同
					for (Iterator it = lstLevIsTotalField.iterator(); it
							.hasNext();) {
						Object obj = it.next();
						if (!(obj instanceof IGroupAble)) {
							continue;
						}
						IGroupAble groupAble = (IGroupAble) obj;
						String summaryId = groupAble.getSummaryID();
						String sourceID = groupAble.getSourceID();
						String sourceColID = groupAble.getSourceColID();
						for (Iterator itEle = lstElements.iterator(); itEle
								.hasNext();) {
							element = (Element) itEle.next();
							lstAttribute = element.attributes();
							// 得到文件中SUMMARY_ID
							String fileSummaryId = getAttributeValue(
									lstAttribute, SUMMARY_ID);
							// 得到文件中SOURCE_ID
							String fileSourceID = getAttributeValue(
									lstAttribute, SOURCE_ID);
							// 得到文件中SOURCECOL_ID
							String fileSourceColID = getAttributeValue(
									lstAttribute, SOURCECOL_ID);
							// 判断SUMMARY_ID,SOURCE_ID,SOURCECOL_ID值是值相等
							if (summaryId.equalsIgnoreCase(fileSummaryId)
									&& sourceID.equalsIgnoreCase(fileSourceID)
									&& sourceColID
											.equalsIgnoreCase(fileSourceColID)) {// 相等
								break;
							} else {// 不等
								if (!itEle.hasNext())
									return false;
							}
						}
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 得到Attribute值信息，根据字段名称
	 * 
	 * @param lstAttribute
	 * @param fieldName
	 *            字段名称
	 * @return
	 */
	private String getAttributeValue(List lstAttribute, String fieldName) {
		if (lstAttribute == null || lstAttribute.isEmpty())
			return null;
		for (Iterator it = lstAttribute.iterator(); it.hasNext();) {
			Attribute attribute = (Attribute) it.next();
			if (fieldName.equalsIgnoreCase(attribute.getName())) {
				return attribute.getValue();
			}
		}
		return null;
	}

	/**
	 * 得到读取参数文件后的lstLevIsTotalField
	 * 
	 * @return
	 */
	public List getLevIsTotalField(List lstLevIsTotalField) {
		Element levElement = reportElement.element(LEVEL_INFO);
		List lstElements = levElement.content();
		List lstAttribute;
		Element element;
		for (Iterator it = lstLevIsTotalField.iterator(); it.hasNext();) {
			Object obj = it.next();
			if (!(obj instanceof IGroupAble)) {
				return null;
			}
			IGroupAble groupAble = (IGroupAble) obj;
			String summaryId = groupAble.getSummaryID();
			String sourceID = groupAble.getSourceID();
			String sourceColID = groupAble.getSourceColID();
			for (Iterator itEle = lstElements.iterator(); itEle.hasNext();) {
				element = (Element) itEle.next();
				lstAttribute = element.attributes();
				// 得到文件中SUMMARY_ID
				String fileSummaryId = getAttributeValue(lstAttribute,
						SUMMARY_ID);
				// 得到文件中SOURCE_ID
				String fileSourceID = getAttributeValue(lstAttribute, SOURCE_ID);
				// 得到文件中SOURCECOL_ID
				String fileSourceColID = getAttributeValue(lstAttribute,
						SOURCECOL_ID);
				// 判断SUMMARY_ID,SOURCE_ID,SOURCECOL_ID值是值相等
				if (summaryId.equalsIgnoreCase(fileSummaryId)
						&& sourceID.equalsIgnoreCase(fileSourceID)
						&& sourceColID.equalsIgnoreCase(fileSourceColID)) {// 相等
					// 将lstLevIsTotalField层次值改为本地参数文件中的值
					groupAble.setSummaryIndex(getAttributeValue(lstAttribute,
							SUMMARY_INDEX));
					groupAble.setIsMbSummary(Integer
							.parseInt(getAttributeValue(lstAttribute,
									ISMB_SUMMARY)));
					groupAble.setLevel(getAttributeValue(lstAttribute, LEVEL));
					groupAble.setIsTotal(getAttributeValue(lstAttribute,
							IS_TOTAL));
					groupAble.setOrderIndex(getAttributeValue(lstAttribute,
							ORDER_INDEX));
					continue;
				}
			}
		}
		return lstLevIsTotalField;
	}

	/**
	 * 得到支出资金来源信息
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public List getPfsCode() throws DocumentException {
		List lstPfsCode = null;
		// 得到支出资金来源
		Element tmpElement = reportElement.element(PFS_CODE_FIELD);
		if (tmpElement != null) {
			String pfsCodeField = tmpElement.getText();
			if (lstPfsCode == null)
				lstPfsCode = new ArrayList();
			lstPfsCode.add(pfsCodeField);
		}
		tmpElement = reportElement.element(PFS_CODE_FILTER);
		if (tmpElement != null) {
			String pfsCodeFilter = tmpElement.getText();
			if (lstPfsCode == null)
				lstPfsCode = new ArrayList();
			lstPfsCode.add(pfsCodeFilter);
		}
		return lstPfsCode;
	}

	/**
	 * 得到支出资金fname来源信息
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public String getPfsFname() throws DocumentException {
		// 得到支出资金来源
		Element tmpElement = reportElement.element(PFS_FIELDFNAME);
		if (tmpElement == null)
			return null;
		return tmpElement.getText();

	}

	/**
	 * 得到提示信息
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public String getPromptInfo() throws DocumentException {
		// 得到提示信息
		Element tmpElement = reportElement.element(PROMPT_INFO);
		if (tmpElement == null)
			return null;
		return tmpElement.getText();

	}

	/**
	 * 得到对比条件值
	 * 
	 * @param conditionObj
	 * @param reportElement
	 */
	public ConditionObj getConditionObj() {
		ConditionObj conditionObj = null;
		Element tmpElement = reportElement.element(ONE_BATCHNO);
		if (tmpElement != null) {
			if (conditionObj == null)
				conditionObj = new ConditionObj();
			conditionObj.setOneBatchNo(tmpElement.getText());
		}
		tmpElement = reportElement.element(ONE_DATATYPE);
		if (tmpElement != null) {
			if (conditionObj == null)
				conditionObj = new ConditionObj();
			conditionObj.setOneDataType(tmpElement.getText());
		}
		tmpElement = reportElement.element(ONE_YEAR);
		if (tmpElement != null) {
			if (conditionObj == null)
				conditionObj = new ConditionObj();
			conditionObj.setOneYear(tmpElement.getText());
		}
		tmpElement = reportElement.element(TWO_BATCHNO);
		if (tmpElement != null) {
			if (conditionObj == null)
				conditionObj = new ConditionObj();
			conditionObj.setTwoBatchNo(tmpElement.getText());
		}
		tmpElement = reportElement.element(TWO_DATATYPE);
		if (tmpElement != null) {
			if (conditionObj == null)
				conditionObj = new ConditionObj();
			conditionObj.setTwoDataType(tmpElement.getText());
		}
		tmpElement = reportElement.element(TWO_YEAR);
		if (tmpElement != null) {
			if (conditionObj == null)
				conditionObj = new ConditionObj();
			conditionObj.setTwoYear(tmpElement.getText());
		}
		return conditionObj;
	}

	/**
	 * 保存条件设置参数
	 * 
	 * @param reportId
	 *            报表名称
	 * @param lstLevIsTotalField
	 *            分组条件
	 * @param lstFieldEname
	 *            支出资金来源
	 * @param conditionObj
	 *            对比条件
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void saveFile(String reportId, List lstLevIsTotalField,
			List lstFieldEname, ConditionObj conditionObj, String sFieldFname,
			String info) throws DocumentException, IOException {
		File filePath = new File(PARA_FILE_NAME);
		Document document;
		Element rootElement;
		// 判断文件是否存在
		if (filePath.exists()) {
			document = XmlTools.read(PARA_FILE_NAME);
			rootElement = document.getRootElement();
		} else {
			document = XmlTools.createDocument();
			rootElement = document.addElement("root");
		}
		// 用户ID
		Element userIdElement = rootElement.element(USER_ID + MID_LINE
				+ Global.getUserId());
		// 用户ID
		if (userIdElement == null) {
			userIdElement = rootElement.addElement(USER_ID + MID_LINE
					+ Global.getUserId());
			userIdElement.addAttribute("UserName", Global.getUserName());
		}

		Element reportElement = userIdElement.element(REPORT_ID + MID_LINE
				+ reportId);
		// 判断报表节点是否存在
		if (reportElement != null) {
			userIdElement.remove(reportElement);

		}
		reportElement = userIdElement.addElement(REPORT_ID + MID_LINE
				+ reportId);
		Element LevElement = reportElement.addElement(LEVEL_INFO);

		Element elementTmp;
		// 保存lstLevIsTotalField内容
		if (lstLevIsTotalField != null && !lstLevIsTotalField.isEmpty()) {
			for (Iterator it = lstLevIsTotalField.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (!(obj instanceof IGroupAble)) {
					continue;
				}
				elementTmp = LevElement.addElement("Attribute");
				IGroupAble groupAble = (IGroupAble) obj;
				elementTmp.setData(groupAble);
				elementTmp.addAttribute(SUMMARY_ID, groupAble.getSummaryID());
				elementTmp.addAttribute(SOURCE_ID, groupAble.getSourceID());
				elementTmp.addAttribute(SOURCECOL_ID, groupAble
						.getSourceColID());
				elementTmp.addAttribute(SUMMARY_INDEX, groupAble
						.getSummaryIndex());
				elementTmp.addAttribute(ISMB_SUMMARY, String.valueOf(groupAble
						.getIsMbSummary()));
				elementTmp.addAttribute(LEVEL, groupAble.getLevel());
				elementTmp.addAttribute(IS_TOTAL, groupAble.getIsTotal());
				elementTmp.addAttribute(ORDER_INDEX, groupAble.getOrderIndex());
			}
		}
		// 保存lstFieldEname内容
		if (lstFieldEname != null && !lstFieldEname.isEmpty()) {
			elementTmp = reportElement.addElement(PFS_CODE_FIELD);
			elementTmp.setText(lstFieldEname.get(0).toString());
			elementTmp = reportElement.addElement(PFS_CODE_FILTER);
			elementTmp.setText(lstFieldEname.get(1).toString());
		}

		if (sFieldFname != null) {
			// 支出资金来源FName
			elementTmp = reportElement.addElement(PFS_FIELDFNAME);
			elementTmp.setText(sFieldFname);
		}
		// 提示信息
		if (!Common.isNullStr(info)) {
			elementTmp = reportElement.addElement(PROMPT_INFO);
			elementTmp.setText(info);
		}

		// 保存conditionObj内容
		if (conditionObj != null) {

			elementTmp = reportElement.addElement(ONE_BATCHNO);
			elementTmp.setText(conditionObj.getOneBatchNo());
			elementTmp = reportElement.addElement(ONE_DATATYPE);
			elementTmp.setText(conditionObj.getOneDataType());
			elementTmp = reportElement.addElement(ONE_YEAR);
			elementTmp.setText(conditionObj.getOneYear());
			elementTmp = reportElement.addElement(TWO_BATCHNO);
			elementTmp.setText(conditionObj.getTwoBatchNo());
			elementTmp = reportElement.addElement(TWO_DATATYPE);
			elementTmp.setText(conditionObj.getTwoDataType());
			elementTmp = reportElement.addElement(TWO_YEAR);
			elementTmp.setText(conditionObj.getTwoYear());
		}
		XmlTools.write(document, PARA_FILE_NAME);
	}

	public boolean isReportExists() {
		return isReportExists;
	}

	/**
	 * 清除所有报表参数
	 * 
	 * @throws IOException
	 * 
	 */
	public void clearAllFilter() throws IOException {
		if (rootElement != null && userIdelement != null) {
			rootElement.remove(userIdelement);
			XmlTools.write(document, PARA_FILE_NAME);
		}
	}

	/**
	 * 清除某一报表参数
	 * 
	 * @throws IOException
	 * 
	 */
	public void clearOneReportFilter() throws IOException {
		if (isReportExists) {
			userIdelement.remove(reportElement);
			XmlTools.write(document, PARA_FILE_NAME);
		}
	}
}
