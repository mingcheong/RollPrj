package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.util.Global;
import com.fr.cell.JWorkBook;
import com.fr.report.WorkBook;
import com.fr.report.io.ExcelImporter;

/**
 * ������֧�ܱ�����,��Excelģ��
 * 
 * @author qzc
 * 
 */
public class OpenExcelActionListener implements ActionListener {

	JWorkBook jWorkBook;

	FLabel flblOpenExcel;

	public OpenExcelActionListener(JWorkBook jWorkBook, FLabel flblOpenExcel) {
		this.jWorkBook = jWorkBook;
		this.flblOpenExcel = flblOpenExcel;
	}

	public void actionPerformed(ActionEvent arg0) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		// �趨���õ��ļ��ĺ�׺��
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().toLowerCase().endsWith(".xls")
						|| f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "�����ļ�(*.xls)";
			}
		});
		fileChooser.showOpenDialog(jWorkBook.getParent());
		if (fileChooser.getSelectedFile() == null)
			return;
		try {

			ExcelImporter ei = new ExcelImporter(fileChooser.getSelectedFile());
			WorkBook wb = ei.generateWorkBook();
			jWorkBook.setWorkBook(wb);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(jWorkBook, " ���ļ��������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(jWorkBook, " ��Excelģ�巢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
		String filepath = fileChooser.getSelectedFile().getPath();
		flblOpenExcel.setTitle(filepath);
	}
}
