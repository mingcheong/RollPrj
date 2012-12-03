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

	// ������Ϣ�Ƿ����
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
					"��ȡ���ز��������ļ��������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ��鱨�����
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
	 * ɾ�������ļ���ĳһ�������,���ݱ���ID
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
	 * �жϱ��ز����ļ��ͱ�����lev�ֶ��Ƿ�һ�����粻һ������ʾ�����޸Ĺ������ز����ļ���ɾ���ñ��������
	 * �����޸�lstLevIsTotalField������Ϊ���ز����ļ���Ϣ����
	 * 
	 * @return true :����һ��, false :���ݲ�һ��
	 */
	private boolean checkReportPara(List lstLevIsTotalField) {
		if (!isReportExists)
			return false;
		Element levElement = reportElement.element(LEVEL_INFO);
		List lstElements = levElement.content();
		List lstAttribute;
		Element element;
		// �ж�ֵ�Ƿ�Ϊ��
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
				// �����Ƿ���ͬ
				if (lstElements.size() == lstLevIsTotalField.size()) {
					// �ж������Ƿ���ͬ
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
							// �õ��ļ���SUMMARY_ID
							String fileSummaryId = getAttributeValue(
									lstAttribute, SUMMARY_ID);
							// �õ��ļ���SOURCE_ID
							String fileSourceID = getAttributeValue(
									lstAttribute, SOURCE_ID);
							// �õ��ļ���SOURCECOL_ID
							String fileSourceColID = getAttributeValue(
									lstAttribute, SOURCECOL_ID);
							// �ж�SUMMARY_ID,SOURCE_ID,SOURCECOL_IDֵ��ֵ���
							if (summaryId.equalsIgnoreCase(fileSummaryId)
									&& sourceID.equalsIgnoreCase(fileSourceID)
									&& sourceColID
											.equalsIgnoreCase(fileSourceColID)) {// ���
								break;
							} else {// ����
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
	 * �õ�Attributeֵ��Ϣ�������ֶ�����
	 * 
	 * @param lstAttribute
	 * @param fieldName
	 *            �ֶ�����
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
	 * �õ���ȡ�����ļ����lstLevIsTotalField
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
				// �õ��ļ���SUMMARY_ID
				String fileSummaryId = getAttributeValue(lstAttribute,
						SUMMARY_ID);
				// �õ��ļ���SOURCE_ID
				String fileSourceID = getAttributeValue(lstAttribute, SOURCE_ID);
				// �õ��ļ���SOURCECOL_ID
				String fileSourceColID = getAttributeValue(lstAttribute,
						SOURCECOL_ID);
				// �ж�SUMMARY_ID,SOURCE_ID,SOURCECOL_IDֵ��ֵ���
				if (summaryId.equalsIgnoreCase(fileSummaryId)
						&& sourceID.equalsIgnoreCase(fileSourceID)
						&& sourceColID.equalsIgnoreCase(fileSourceColID)) {// ���
					// ��lstLevIsTotalField���ֵ��Ϊ���ز����ļ��е�ֵ
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
	 * �õ�֧���ʽ���Դ��Ϣ
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public List getPfsCode() throws DocumentException {
		List lstPfsCode = null;
		// �õ�֧���ʽ���Դ
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
	 * �õ�֧���ʽ�fname��Դ��Ϣ
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public String getPfsFname() throws DocumentException {
		// �õ�֧���ʽ���Դ
		Element tmpElement = reportElement.element(PFS_FIELDFNAME);
		if (tmpElement == null)
			return null;
		return tmpElement.getText();

	}

	/**
	 * �õ���ʾ��Ϣ
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public String getPromptInfo() throws DocumentException {
		// �õ���ʾ��Ϣ
		Element tmpElement = reportElement.element(PROMPT_INFO);
		if (tmpElement == null)
			return null;
		return tmpElement.getText();

	}

	/**
	 * �õ��Ա�����ֵ
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
	 * �����������ò���
	 * 
	 * @param reportId
	 *            ��������
	 * @param lstLevIsTotalField
	 *            ��������
	 * @param lstFieldEname
	 *            ֧���ʽ���Դ
	 * @param conditionObj
	 *            �Ա�����
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void saveFile(String reportId, List lstLevIsTotalField,
			List lstFieldEname, ConditionObj conditionObj, String sFieldFname,
			String info) throws DocumentException, IOException {
		File filePath = new File(PARA_FILE_NAME);
		Document document;
		Element rootElement;
		// �ж��ļ��Ƿ����
		if (filePath.exists()) {
			document = XmlTools.read(PARA_FILE_NAME);
			rootElement = document.getRootElement();
		} else {
			document = XmlTools.createDocument();
			rootElement = document.addElement("root");
		}
		// �û�ID
		Element userIdElement = rootElement.element(USER_ID + MID_LINE
				+ Global.getUserId());
		// �û�ID
		if (userIdElement == null) {
			userIdElement = rootElement.addElement(USER_ID + MID_LINE
					+ Global.getUserId());
			userIdElement.addAttribute("UserName", Global.getUserName());
		}

		Element reportElement = userIdElement.element(REPORT_ID + MID_LINE
				+ reportId);
		// �жϱ���ڵ��Ƿ����
		if (reportElement != null) {
			userIdElement.remove(reportElement);

		}
		reportElement = userIdElement.addElement(REPORT_ID + MID_LINE
				+ reportId);
		Element LevElement = reportElement.addElement(LEVEL_INFO);

		Element elementTmp;
		// ����lstLevIsTotalField����
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
		// ����lstFieldEname����
		if (lstFieldEname != null && !lstFieldEname.isEmpty()) {
			elementTmp = reportElement.addElement(PFS_CODE_FIELD);
			elementTmp.setText(lstFieldEname.get(0).toString());
			elementTmp = reportElement.addElement(PFS_CODE_FILTER);
			elementTmp.setText(lstFieldEname.get(1).toString());
		}

		if (sFieldFname != null) {
			// ֧���ʽ���ԴFName
			elementTmp = reportElement.addElement(PFS_FIELDFNAME);
			elementTmp.setText(sFieldFname);
		}
		// ��ʾ��Ϣ
		if (!Common.isNullStr(info)) {
			elementTmp = reportElement.addElement(PROMPT_INFO);
			elementTmp.setText(info);
		}

		// ����conditionObj����
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
	 * ������б������
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
	 * ���ĳһ�������
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
