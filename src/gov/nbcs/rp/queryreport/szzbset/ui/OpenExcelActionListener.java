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
 * 设置收支总表设置,打开Excel模板
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
		// 设定可用的文件的后缀名
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().toLowerCase().endsWith(".xls")
						|| f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "所有文件(*.xls)";
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
			JOptionPane.showMessageDialog(jWorkBook, " 打开文件发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(jWorkBook, " 打开Excel模板发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
		String filepath = fileChooser.getSelectedFile().getPath();
		flblOpenExcel.setTitle(filepath);
	}
}
