package gov.nbcs.rp.common;

import com.foundercy.pf.util.Global;
import gov.nbcs.rp.common.datactrl.QueryStub;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import jxl.Sheet;
import jxl.Workbook;


public class ImportExcel {
	
	private static File lastSelectedFile = null;

	private String tableName;

	private String[] fieldNames;
	
	private boolean isHaveEnID = false;
	
	/**
	 * 构造函数
	 * @param tableName
	 * @param fieldNames
	 * @param isHaveEnID
	 */
	public ImportExcel(String tableName,String[] fieldNames,boolean isHaveEnID){
		this.tableName = tableName;
		this.fieldNames = fieldNames; 
		this.isHaveEnID = isHaveEnID;
	}

	/**
	 * 获取文件路径	
	 * 
	 * @throws Exception
	 */
	public InfoPackage getFilePath() throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		JFileChooser fileChooser = getFileChoose();
		if (lastSelectedFile != null) {
			fileChooser.setSelectedFile(lastSelectedFile);
		}
		int returnval = fileChooser.showDialog(Global.mainFrame, "导入");
		if (returnval == JFileChooser.CANCEL_OPTION) {
			info.setSuccess(false);
			info.setsMessage("CANCEL");
			return info;
		}
		File file = fileChooser.getSelectedFile();
		String sFilePath = file.getPath();
		checkFileName(sFilePath);
		if (Common.isNullStr(sFilePath)) {
			info.setSuccess(false);
			return info;
		} else {
			info.setsMessage(sFilePath);
		}
		lastSelectedFile = file;
		return info;
	}

	/**
	 * 获得特定类型文件的路径
	 * 
	 * @param aFilePath
	 * @param aFileType
	 * @return
	 */
	private void checkFileName(String aFilePath) throws Exception {
		if (Common.isNullStr(aFilePath)) {
			return;
		}
		int idex = aFilePath.lastIndexOf("\\");
		String sFileName = aFilePath.substring(idex + 1, aFilePath.length());
		if (sFileName.length() <= 4) {
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "文件名不能为空，请确定",
					"提示信息", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
				return;
			}
		} else {
			int length = sFileName.length();
			String sLastStr = sFileName.substring(length - 4, length);
			if (!".xls".equalsIgnoreCase(sLastStr)) {
				if (JOptionPane.showConfirmDialog(Global.mainFrame,
						"只能选取EXCEL文件", "提示信息", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
		}
	}

	/**
	 * 创建文件选择器
	 * 
	 * @throws Exception
	 */
	private JFileChooser getFileChoose() throws Exception {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("获取导入文件");
		// 设定可用的文件的后缀名
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().endsWith(".xls".toLowerCase())
						|| f.getName().endsWith(".XLS") || f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "所有文件(*.xls)";
			}
		});
		return fileChooser;
	}

	/**
	 * 开始获取
	 * 
	 * @return
	 */
	public InfoPackage doImport() {
		InfoPackage info = new InfoPackage();
		try {
			info = getFilePath();
			String sFilePath = null;
			if (info.getSuccess()) {
				sFilePath = info.getsMessage();
			} else {
				if (!"CANCEL".equalsIgnoreCase(info.getsMessage())) {
					JOptionPane.showMessageDialog(Global.mainFrame, info
							.getsMessage());
				} else {
					info.setsMessage("CANCEL");
					return info;
				}
			}
			if (Common.isNullStr(sFilePath)) {
				info.setsMessage("CANCEL");
				return info;
			}
			InputStream is = new FileInputStream(new File(sFilePath));
			Workbook wb = Workbook.getWorkbook(is);
			if (!info.getSuccess()) {
				JOptionPane.showMessageDialog(Global.mainFrame, info
						.getsMessage());
				return info;
			}
			Sheet sheet = wb.getSheet(0);
			int iD = getDataRow(sheet);
			List list = getImportSql(getImportData(sheet, iD));
			list.add(updateEnIDSql());
			QueryStub.getClientQueryTool().executeBatch(list);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "导入EXCEL文件失败");
		}
		return info;
	}

	/**
	 * 获取数据所在的行数
	 * 
	 * @param ws
	 * @return
	 */
	private int getDataRow(Sheet ws) {
		int col = ws.getColumns();
		for (int i = 0; i < col; i++) {
			for (int j = 1; j < 20; j++) {
				// if (count == 2)
				// break;
				String contentn = ws.getCell(i, j).getContents();
				if (Common.isNullStr(contentn)) {
					continue;
				}
				if (Common.isNumber(contentn)) {
					return j;
				}
			}
		}
		return -1;
	}

	private List getImportData(Sheet sheet, int iD) throws Exception {
		List list = new ArrayList();
		for (int i = iD; i < sheet.getRows(); i++) {
			Map map = new HashMap();
			for (int j = 0; j < this.fieldNames.length; j++) {
				if (sheet.getCell(j, i) == null) {
					continue;
				}
				if (Common.isNullStr(sheet.getCell(j, i).getContents())) {
					continue;
				}
				map.put(this.fieldNames[j], sheet.getCell(j, i).getContents());
			}
			list.add(map);
		}
		return list;
	}

	private List getImportSql(List list1) {
		List list = new ArrayList();
		for (int i = 0; i < list1.size(); i++) {
			if (list1.get(i) != null) {
				Map map = (Map) list1.get(i);
				list.add(getDelSql(map));
				list.add(getInsertSql(map));
			}
		}
		return list;
	}

	private String getDelSql(Map map) {
		StringBuffer str = new StringBuffer();
		str.append("delete from " + this.tableName);
		str.append(" where ");
		for (int i = 0; i < this.fieldNames.length; i++) {
			if (Common
					.isNullStr(Common.nonNullStr(map.get(this.fieldNames[i])))) {
				return "";
			}
			if (i != 0) {
				str.append(" and ");
			}
			str.append(this.fieldNames[i] + " = '"
					+ map.get(this.fieldNames[i]) + "'");
		}
		return str.toString();
	}

	private String getInsertSql(Map map) {
		StringBuffer str = new StringBuffer();
		str.append("insert into " + this.tableName + "(");
		for (int i = 0; i < this.fieldNames.length; i++) {
			if (Common
					.isNullStr(Common.nonNullStr(map.get(this.fieldNames[i])))) {
				return "";
			}
			if (i != 0) {
				str.append(",");
			}
			str.append(this.fieldNames[i]);
		}
		if (this.isHaveEnID) {
			str.append(",en_id");
		}
		str.append(") values(");
		for (int i = 0; i < this.fieldNames.length; i++) {
			if (i != 0) {
				str.append(",");
			}
			str.append("'" + map.get(this.fieldNames[i]) + "'");
		}
		if (this.isHaveEnID) {
			str.append(",newid");
		}
		str.append(")");
		return str.toString();
	}
	
	private String updateEnIDSql(){
		if (isHaveEnID){
			StringBuffer sql = new StringBuffer();
			sql.append("update "+this.tableName+" a ");
			sql.append(" set a.en_id = (select en_id from vw_fb_division b where a.div_code = b.div_code) ");
			return sql.toString();
		} else {
			return "";
		}
	}
}
