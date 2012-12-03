/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.qrbudget.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.common.XmlTools;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:
 * </p>
 * <p>

 */
public class ReadWriteFile {

	/**
	 * 保存文件
	 * 
	 * @param list数据内容
	 * @param para参数
	 * @throws Exception
	 */
	public static void saveFile(List list, DataSet dsHeader, Map para)
			throws Exception {
		if (list.size() == 0)
			return;
		// 文件名
		String sReportId = para.get(IQrBudget.REPORT_ID).toString();
		File filePath = new File(IDefineReport.PATH_ + sReportId + "_data.xml");
		Document document;
		// 判断文件是否存在
		if (filePath.exists()) {
			// // 读文件
			// document = XmlTools.read(filePath.toString());
			// // 检查参数是否相同
			// boolean flag = check(para, document);
			// if (!flag)
			// 参数不相同删除文件
			deleteFile(sReportId);
			// else
			// return;
		}

		document = XmlTools.createDocument();
		Element rootElement = document.addElement("root");
		// 批次
		Element elementTmp = rootElement.addElement(IDefineReport.BATCH_NO);
		elementTmp.setText(para.get(IDefineReport.BATCH_NO).toString());
		// 类型
		elementTmp = rootElement.addElement(IDefineReport.DATA_TYPE);
		elementTmp.setText(para.get(IDefineReport.DATA_TYPE).toString());
		// 版本
		elementTmp = rootElement.addElement(IDefineReport.VER_NO);
		elementTmp.setText(para.get(IDefineReport.VER_NO).toString());
		// 单位
		elementTmp = rootElement.addElement(IDefineReport.DIV_CODE);
		elementTmp.setText(para.get(IDefineReport.DIV_CODE).toString());
		// 点击查询按钮生成的标记
		elementTmp = rootElement.addElement(IQrBudget.UUID);
		if (para.get(IQrBudget.UUID) != null)
			elementTmp.setText(para.get(IQrBudget.UUID).toString());

		// 存表头信息
		// Element hittingElement = rootElement.addElement("Header");
		Object[] values;
		Object[] keySet = null;
		// dsHeader.beforeFirst();
		// if (dsHeader.next()) {
		// keySet = dsHeader.getOriginData().entrySet().toArray();
		// }
		// while (dsHeader.next()) {
		// elementTmp = hittingElement.addElement("Attribute");
		// values = dsHeader.getOriginData().values().toArray();
		// for (int j = 0; j < values.length; j++) {
		// if (values[j] == null) {
		// elementTmp.addAttribute(keySet[j].toString(), "");
		// } else {
		// elementTmp.addAttribute(keySet[j].toString(), values[j]
		// .toString());
		// }
		// }
		// }

		// 存表体信息
		Element hittingElement = rootElement.addElement("Value");
		int size = list.size();

		for (int i = 0; i < size; i++) {
			keySet = ((Map) list.get(i)).keySet().toArray();
			elementTmp = hittingElement.addElement("Attribute");
			values = ((Map) list.get(i)).values().toArray();
			for (int j = 0; j < values.length; j++) {
				if (values[j] == null) {
					elementTmp.addAttribute(keySet[j].toString(), "");
				} else {
					elementTmp.addAttribute(keySet[j].toString(), values[j]
							.toString());
				}
			}
		}

		XmlTools.write(document, filePath.toString());
		filePath.deleteOnExit();
	}

	/**
	 * 读文件
	 * 
	 * @param list数据内容
	 * @param para参数
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static List readFile(Map para) throws IOException, DocumentException {
		// 文件名
		String sReportId = para.get(IQrBudget.REPORT_ID).toString();
		File filePath = new File(IDefineReport.PATH_ + sReportId + "_data.xml");
		if (!filePath.exists()) {
			return null;
		}

		Document document = XmlTools.read(filePath.toString());
		Element rootElement = document.getRootElement();

		// boolean flag = check(para, document);
		// if (!flag) {
		// filePath.delete();
		// return null;
		// }

		Element element = rootElement.element("Value");

		List lstContent = element.content();
		List lstAttr;
		Attribute attribute;
		int size = lstContent.size();
		List lstData = new ArrayList();
		XMLData xmlData;
		for (int i = 0; i < size; i++) {
			element = (Element) lstContent.get(i);
			lstAttr = element.attributes();
			xmlData = new XMLData();
			for (int j = 0; j < lstAttr.size(); j++) {
				attribute = (Attribute) lstAttr.get(j);
				xmlData.put(attribute.getName(), attribute.getValue());
			}
			lstData.add(xmlData);
		}
		return lstData;
	}

	/**
	 * 检查参数值是否相同
	 * 
	 * @return
	 */
	public static boolean check(Map curPara, Map filePara) {
		if (filePara == null || curPara == null) {
			return false;
		}
		if (curPara.get(IQrBudget.UUID) == null
				|| !curPara.get(IQrBudget.UUID).toString().equals(
						filePara.get(IQrBudget.UUID))) {
			return false;
		}

		if (!curPara.get(IDefineReport.BATCH_NO).toString().equals(
				filePara.get(IDefineReport.BATCH_NO))) {
			return false;
		}
		if (!curPara.get(IDefineReport.DATA_TYPE).toString().equals(
				filePara.get(IDefineReport.DATA_TYPE))) {
			return false;
		}
		if (!curPara.get(IDefineReport.VER_NO).toString().equals(
				filePara.get(IDefineReport.VER_NO))) {
			return false;
		}

		if (!curPara.get(IDefineReport.DIV_CODE).toString().equals(
				filePara.get(IDefineReport.DIV_CODE))) {
			return false;
		}
		return true;
	}

	/**
	 * 得到提示信息
	 * 
	 * @param filePara
	 */
	public static String getInfo(Map filePara) {
		if (filePara.get(IDefineReport.SHOW_INFO) != null)
			return "";
		else
			return (String) filePara.get(IDefineReport.SHOW_INFO);
	}

	/**
	 * 删除文件
	 * 
	 * @param reportId
	 * @throws Exception
	 */
	public static void deleteFile(String sReportId) throws Exception {
		File filePath = new File(IDefineReport.PATH_ + sReportId + "_data.xml");
		if (filePath.exists()) {
			System.gc();
			Thread.sleep(1000);
			if (!filePath.delete()) {
				throw new Exception("删除文件失败！");
			}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param sReportId
	 * @return
	 */
	public static boolean isExistFile(String sReportId) {
		File filePath = new File(IDefineReport.PATH_ + sReportId + "_data.xml");
		if (filePath.exists()) {
			return true;
		}
		return false;
	}
}
