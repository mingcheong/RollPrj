/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.qrbudget.common;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @version 6.2.40
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlTools {

	/**
	 * ���ļ���ȡXML������XML�ĵ�
	 * 
	 * @param filePath
	 * @return
	 * @throws DocumentException
	 */
	public static Document read(String filePath) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(filePath));
		return document;
	}

	/**
	 * ����XML
	 * 
	 * @return
	 */
	public static Document createDocument() {
		Document document = DocumentHelper.createDocument();
		// document.addElement("root");
		// Element author1 =
		// root
		// .addElement(author)
		// .addAttribute(name, James)
		// .addAttribute(location, UK)
		// .addText(James Strachan);
		// Element author2 =
		// root
		// .addElement(author)
		// .addAttribute(name, Bob)
		// .addAttribute(location, US)
		// .addText(Bob McWhirter);
		return document;
	}

	/**
	 * ȡ��Root�ڵ�
	 * 
	 * @param document
	 * @return
	 */
	public static Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	/**
	 * �����ַ����ļ���ø��ڵ�
	 * 
	 * @param str
	 * @return
	 * @throws DocumentException
	 */
	public static Element getRootElement(String str) throws DocumentException {
		Element root = null;
		root = DocumentHelper.parseText(str).getRootElement();
		return root;
	}

	/**
	 * �����
	 */
	public static void writeSimply(Document document, String filePath)
			throws IOException {
		FileWriter out = new FileWriter(filePath);
		document.write(out);
		out.close();
	}

	/**
	 * ����ڵ�
	 * 
	 * @param element
	 * @param filePath
	 *            �ļ�·��ȫ��
	 * @throws IOException
	 */
	public static void writeElement(Element element, String filePath)
			throws IOException {
		String oneXml = element.asXML();
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		out.write(oneXml);
		out.close();
	}

	/**
	 * xml��� (��GB2312��������xml���޸�xml����encoding="UTF-8" Ϊencoding="GB2312")
	 * 
	 * @param document
	 * @param filePath
	 * @throws IOException
	 */
	public static void write(Document document, String filePath)
			throws IOException {
		// ָ���ļ�
		XMLWriter writer = new XMLWriter(new FileWriter(filePath));
		writer.write(document);
		writer.close();
		// logger.info("������Ϣ��xml�ļ�:" + filePath);
		// �޸��ַ���ʽencoding="UTF-8" Ϊencoding="GB2312"
		String newcode = StringTools.fileToString(filePath);
		newcode = StringTools.Replace(newcode, "UTF-8", "GB2312");
		StringTools.stringToFile(newcode, filePath);

	}

	/**
	 * д��xml�ļ�
	 * 
	 * @param document
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeToXml(Document document, String filePath)
			throws IOException {
		String oneXml = document.asXML();
		// logger.info(oneXml);
		// oneXml=StringTools.toUTF(oneXml);
		// logger.info(oneXml);
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		out.write(oneXml);
		out.close();
	}

	public static void main(String[] args) {

		Document document = XmlTools.createDocument();

		/*
		 * Element catalogElement = document.addElement("catalog");
		 * 
		 * catalogElement.addComment("An XML Catalog"); Element journalElement =
		 * catalogElement.addElement("journal");
		 * journalElement.addAttribute("title", "������");
		 * journalElement.addAttribute("name", "�ҵ�����");
		 * journalElement.setText("testtest");
		 */

		Element rootElement = document.addElement("root");
		Element titleElement = rootElement.addElement("title");
		titleElement.setText("���а�");
		Element hittingElement = rootElement.addElement("hitting");
		Element row, index, name, score, detail;
		row = hittingElement.addElement("r");

		index = row.addElement("i");
		name = row.addElement("n");
		score = row.addElement("s");
		detail = row.addElement("d");
		index.setText("����");
		name.setText("�û�");
		score.setText("����");
		detail.setText("��ϸ");
		List lstArray = new ArrayList();
		Map a = new HashMap();
		a.put("hhh", "dd");
		lstArray.add(a);
		lstArray.add("def");
		for (int i = 1; i <= 10; i++) {
			row = hittingElement.addElement("r");

			index = row.addElement("i");
			name = row.addElement("n");
			score = row.addElement("s");
			detail = row.addElement("d");
			index.setText(String.valueOf(i));
			name.setText("��" + String.valueOf(i) + "��");
			score.setText(String.valueOf(i * 1000));
			detail.setText(String.valueOf(900 + i));
			detail.setContent(lstArray);
			detail.setData("uuu");

		}

		try {
			String filepath = "F:\\catalog.xml";
			XmlTools.write(document, filepath);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
