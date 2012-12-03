/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import java.io.File;

import javax.swing.JFileChooser;

import com.foundercy.pf.util.Global;
import com.fr.report.io.ExcelExporter;

/**
 * The class ReportHelper.
 * 
 * @author qj
 * @version 1.0, Apr 6, 2009
 * @since fb6.3.70
 */
public class ReportHelper {

	/** The Constant EXCEL_FILE_EXT. */
	private static final String EXCEL_FILE_EXT = ".xls";

	/**
	 * Export excel.
	 * 
	 * @param report
	 *            the report
	 */
	public static String exportExcel(com.fr.report.Report report) {
		try {
			JFileChooser fileChooser = null;// �ļ�ѡ����
			// �����ļ�ѡ����
			fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			// �趨���õ��ļ��ĺ�׺��
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					if (f.getName().toLowerCase().endsWith(EXCEL_FILE_EXT)
							|| f.isDirectory()) {
						return true;
					}
					return false;
				}

				public String getDescription() {
					return "Excel�ļ�(*.xls)";
				}
			});

			fileChooser.showSaveDialog(Global.mainFrame);
			if (fileChooser.getSelectedFile() != null) {
				String filepath = "";
				filepath = fileChooser.getSelectedFile().getPath();
				if (!EXCEL_FILE_EXT.equalsIgnoreCase(filepath.substring(
						filepath.length() - 4, filepath.length()))) {
					filepath = filepath + EXCEL_FILE_EXT;
				}
				if (!"".equals(filepath)) {
					ExcelExporter eep = new ExcelExporter(filepath);
					eep.exportReport(report);
					return filepath;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
