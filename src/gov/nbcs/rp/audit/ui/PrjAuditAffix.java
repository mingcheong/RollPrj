package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.action.PrjAuditStub;
import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.ui.table.CustomTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

public class PrjAuditAffix extends FPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PrjAuditAction pa;

	private PrjAuditDTO pd;

	private FButton btnAffixView;

	private FButton btnAdd;

	private FButton btnSaveAs;

	private FButton btnDel;

	public CustomTable tableAffix;

	private JFileChooser fileChooser;

	private InputStream is = null;

	private ByteArrayOutputStream data = null;

	private byte[] result;

	private int editstate = 0;

	private String rowid;

	/** 项目附件 */
	public FTextField tfAffix;

	private List prjCodes;

	public PrjAuditAffix(PrjAuditAction pa, PrjAuditDTO pd, boolean isEdit,
			List prjCodes) {
		this.pa = pa;
		this.pd = pd;
		this.prjCodes = prjCodes;
		editstate = 0;
		RowPreferedLayout lay = new RowPreferedLayout(4);
		this.setLayout(lay);
		lay.setRowGap(10);
		lay.setRowHeight(20);
		setBottomPanel();
		this.setTopInset(10);
		this.setLeftInset(10);
		this.setRightInset(10);
		this.setBottomInset(10);
		setEdit(isEdit);
	}

	/**
	 * 获取附件面板
	 * 
	 * @return
	 */
	private void setBottomPanel() {
		tfAffix = new FTextField("附件：");
		tfAffix.setProportion(0.3f);
		btnAdd = new FButton("add", "增加");
		btnDel = new FButton("del", "删除");
		btnSaveAs = new FButton("saveas", "另存为");
		btnAffixView = new FButton("btnView", "查看");
		String[] columnText = new String[] { "审核流程", "审核人", "附件名称", "附件文件名称",
				"附件类型" };
		String[] columnField = new String[] {
				IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_NAME,
				IPrjAudit.PrjAuditTable.audit_username,
				IPrjAudit.PrjAuditTable.Affix.AFFIX_TITLE,
				IPrjAudit.PrjAuditTable.Affix.AFFIX_FILE,
				IPrjAudit.PrjAuditTable.Affix.AFFIX_TYPE, };
		try {
			tableAffix = new CustomTable(columnText, columnField, null, false,
					null);
			setTableData();
		} catch (Exception eee) {
			ErrorInfo.showErrorDialog(eee, "生成附件表失败");
		}
		FPanel pnlD = new FPanel();
		RowPreferedLayout layD = new RowPreferedLayout(8);
		// layD.setColumnWidth(10);
		// layD.setRowHeight(200);
		layD.setColumnGap(10);
		pnlD.setLayout(layD);
		pnlD.addControl(btnAdd, new TableConstraints(1, 1, true, true));
		pnlD.addControl(btnDel, new TableConstraints(1, 1, true, true));

		pnlD.addControl(btnAffixView, new TableConstraints(1, 1, true, true));
		// pnlD.addControl(tfAffix, new TableConstraints(1, 14, false, false));
		pnlD.addControl(btnSaveAs, new TableConstraints(1, 1, true, true));
		this.addControl(pnlD, new TableConstraints(1, 8, false, true));
		this.addControl(tableAffix, new TableConstraints(5, 8, true, true));
		this.btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// if (pd.getAuditPrjTable() != null
					// && pd.getAuditPrjTable().getDataSet() != null
					// && !pd.getAuditPrjTable().getDataSet().isEmpty()
					// && !pd.getAuditPrjTable().getDataSet().bof()
					// && !pd.getAuditPrjTable().getDataSet().eof()) {
					editstate = 1;
					createChoose();
					int returnval = fileChooser
							.showOpenDialog(Global.mainFrame);
					if (returnval == JFileChooser.CANCEL_OPTION) {
						return;
					}
					File selectedFile = fileChooser.getSelectedFile();
					saveAffixInfo(selectedFile, 1);
					is = new FileInputStream(selectedFile);
					data = new ByteArrayOutputStream();
					int bit;
					while ((bit = is.read()) != -1) {
						data.write(bit);
					}
					result = data.toByteArray();
					tfAffix.setValue(selectedFile.getPath());
					saveAffixFile();
					editstate = 0;
					tableAffix.getDataSet().edit();
					// } else {
					// JOptionPane.showMessageDialog(Global.mainFrame,
					// "请选择要添加意见的项目");
					// return;
					// }
				} catch (Exception ee) {
					ErrorInfo.showErrorDialog(ee, "附件保存失败");
				}
			}
		});
		btnAffixView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewFile(true);
				} catch (Exception ee) {
					ErrorInfo.showErrorDialog(ee, "附件查看失败");
				}
			}
		});
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveAffixInfo(null, 3);
				} catch (Exception ee) {
					ErrorInfo.showErrorDialog(ee, "附件删除失败");
				}
			}
		});
		btnSaveAs.addActionListener(new SaveAsActionListener());
	}

	private void setEdit(boolean edit) {
		btnAdd.setEnabled(edit);
		btnDel.setEnabled(edit);
	}

	/**
	 * 设置表格数据
	 * 
	 */
	public void setTableData() throws Exception {
		String sql = "select ROW_ID,SET_YEAR,PRJ_CODE,AFFIX_TITLE,AFFIX_TYPE,AFFIX_FILE,LAST_VER"
				+ ",step_id,step_name,audit_username from "
				+ " RP_AUDIT_AFFIX " + " where set_year = " + Global.loginYear;
		if (prjCodes != null) {
			sql = sql
					+ " and "
					+ pd.getArraySqlFilter(IPrjAudit.PrjAuditTable.prj_code,
							prjCodes);
		}
		DataSet ds = DBSqlExec.client().getDataSet(sql);
		this.tableAffix.setDataSet(ds);
		tableAffix.reset();
		this.setTableProp();
	}

	/**
	 * 保存附件文件
	 * 
	 * @throws Exception
	 */
	public void saveAffixFile() throws Exception {
		if (!Common.isNullStr(Common.nonNullStr(tfAffix.getValue()))) {
			// blob
			String row_id = null;
			if (editstate == 1) {
				row_id = rowid;
			} else {
				row_id = this.tableAffix.getDataSet().fieldByName("row_id")
						.getString();
			}
			PrjAuditStub.getMethod().modifyDocBlobs("RP_AUDIT_AFFIX", row_id,
					Global.getSetYear(), result);
			is.close();
			data.close();
		}
	}

	/**
	 * 创建文件选择器
	 * 
	 * @throws Exception
	 */
	private void createChoose() throws Exception {
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("选择附件");
		// 设定可用的文件的后缀名
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				// if (f.isDirectory()) {
				// return true;
				// }
				// return false;
				return true;
			}

			public String getDescription() {
				return "所有文件(*.*)";
			}
		});
	}

	/**
	 * 
	 * @param file
	 * @param state
	 *            0：浏览， 1：增加 2：修改 3：删除
	 * @throws Exception
	 */
	private void saveAffixInfo(File file, int state) throws Exception {
		StringBuffer sql = new StringBuffer();
		for (int i = 0; i < prjCodes.size(); i++) {
			if (1 == state) {
				tableAffix.getDataSet().append();
				String path = file.getPath();
				String title = path.substring(path.lastIndexOf("\\") + 1, path
						.lastIndexOf("."));
				String type = path.substring(path.lastIndexOf(".") + 1, path
						.length());
				sql.append("insert into rp_audit_affix");
				sql.append("(");
				sql.append(IPrjAudit.PrjAuditTable.Affix.ROW_ID);
				sql.append(",");
				sql.append(IPrjAudit.MainTable.Affix.PRJ_CODE);
				sql.append(",");
				sql.append(IPrjAudit.MainTable.Affix.AFFIX_TITLE);
				sql.append(",");
				sql.append(IPrjAudit.MainTable.Affix.AFFIX_TYPE);
				sql.append(",");
				sql.append(IPrjAudit.MainTable.Affix.AFFIX_FILE);
				sql.append(",");
				sql.append(IPrjAudit.MainTable.Affix.SET_YEAR);
				sql.append(",");
				sql.append(IPrjAudit.AuditNewInfo.EXPERT.LAST_VER);
				sql.append(",");
				sql.append(IPrjAudit.MainTable.Affix.AFFIX_CONT);
				sql.append(",STEP_ID,STEP_NAME,AUDIT_USERID,AUDIT_USERNAME");
				sql.append(") values ('");
				rowid = UUID.randomUUID().toString();
				sql.append(rowid);
				sql.append("','");
				sql.append(prjCodes.get(i));
				sql.append("','");
				sql.append(title);
				sql.append("','");
				sql.append(type);
				sql.append("','");
				sql.append(title + "." + type);
				sql.append("',");
				sql.append(Global.loginYear);
				sql.append(",");
				sql.append("'" + Tools.getCurrDate() + "',");
				sql.append("empty_blob()");
				sql.append("," + pd.getStepID());
				sql.append(",'" + pd.getStepName() + "'");
				sql.append(",'" + Global.getUserId() + "'");
				sql.append(",'" + Global.getUserName() + "'");
				sql.append(")");
			} else if (3 == state) {
				if ((tableAffix.getDataSet() != null)
						&& !tableAffix.getDataSet().isEmpty()
						&& !tableAffix.getDataSet().bof()
						&& !tableAffix.getDataSet().eof()) {
					// tableAffix.getDataSet().delete();
					sql.append("delete from rp_audit_affix");
					sql.append(" where ");
					sql.append(IPrjAudit.MainTable.Affix.ROW_ID);
					sql.append("='");
					sql.append(tableAffix.getDataSet().fieldByName(
							IPrjAudit.MainTable.Affix.ROW_ID).getString());
					sql.append("'");
					sql.append(" and set_year = ");
					sql.append(Global.loginYear);
				} else {
					JOptionPane
							.showMessageDialog(Global.mainFrame, "请选择要删除的附件");
					return;
				}
			}
		}
		QueryStub.getClientQueryTool().executeUpdate(sql.toString());
		tableAffix.getDataSet().applyUpdate();
		setTableData();
	}

	public void setTableProp() throws Exception {
		tableAffix.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableAffix.reset();
		tableAffix.getTable().getColumnModel().getColumn(0).setPreferredWidth(
				120);
		tableAffix.getTable().getColumnModel().getColumn(1).setPreferredWidth(
				100);
		tableAffix.getTable().getColumnModel().getColumn(2).setPreferredWidth(
				100);
		tableAffix.getTable().getColumnModel().getColumn(3).setPreferredWidth(
				200);
		tableAffix.getTable().getColumnModel().getColumn(4).setPreferredWidth(
				60);
		tableAffix.getTable().getColumnModel().getColumn(5).setPreferredWidth(
				60);
	}

	/**
	 * 查看(通过WINDOWS接口打开)
	 * 
	 * @throws Exception
	 */
	protected void viewFile(boolean isCanEdit) throws Exception {
		if ((tableAffix.getDataSet() != null)
				&& !tableAffix.getDataSet().isEmpty()
				&& !tableAffix.getDataSet().bof()
				&& !tableAffix.getDataSet().eof()) {
		} else {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体附件");
			return;
		}
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			// 根据记录主键，获取文件内容
			// 清空临时文件夹中当天以前的历史文件
			File cleaner = File.createTempFile("temp", ".temp");
			delOldFileUnderDir(cleaner.getParentFile());

			// 创建临时文件
			String sAddr = (Common.nonNullStr(this.tableAffix.getDataSet()
					.fieldByName(IPrjAudit.MainTable.Affix.AFFIX_FILE)
					.getString()));
			int ilastdot = sAddr.lastIndexOf(".");
			String tempFileType = sAddr.substring(ilastdot, sAddr.length());
			File temp = File.createTempFile("temp", tempFileType);

			// 放入文件输出流
			fos = new FileOutputStream(temp);
			// 放入缓冲输出流，提高效率
			bos = new BufferedOutputStream(fos);
			// 分批读取blob，转化为字节,放入输出流
			int count = 1;
			boolean isSuccessful = true;
			while (true) {
				byte[] buf = PrjAuditStub.getMethod().getDocBlob(
						"rp_audit_affix",
						count++,
						this.tableAffix.getDataSet().fieldByName(
								IPrjAudit.MainTable.Affix.ROW_ID).getString(),
						Global.loginYear);
				if (buf == null) {
					JOptionPane.showMessageDialog(Global.mainFrame, "读取文件失败!",
							"消息", JOptionPane.INFORMATION_MESSAGE);
					isSuccessful = false;
					break;
				} else {
					bos.write(buf);
					if (buf.length < 1024 * 350) {
						break;
					}
				}
			}
			bos.flush();
			// 设置只读
			if (!isCanEdit) {
				temp.setReadOnly();
			}
			// 调用操作系统中的关联方式打开文件
			if (isSuccessful) {
				Runtime rt = Runtime.getRuntime();
				rt.exec("cmd  /c  start  " + temp.getAbsolutePath());
			}
		} catch (Exception e) {
			ErrorInfo.showErrorDialog(e, "读取文件失败！");
		} finally {
			/* 关闭流 */
			try {
				bos.close();
				fos.close();
			} catch (IOException e) {
				// 该异常不必给用户看到，不影响下面的流程
			} finally {
				bos = null;
				fos = null;
			}
		}
	}

	/**
	 * 清空临时文件夹中非当天的文件
	 */
	private void delOldFileUnderDir(File f) {
		if (f.isDirectory()) {
			/* 列出该目录下的所有文件 */
			File[] entries = f.listFiles();
			int size = entries.length;
			/* 逐个删除 */
			for (int i = 0; i < size; i++) {
				long last = entries[i].lastModified() / 1000 / 3600 / 24;
				long today = System.currentTimeMillis() / 1000 / 3600 / 24;
				if (last < today) {
					delFile(entries[i]);
				}
			}
		}
	}

	/**
	 * 删除文件
	 */
	public void delFile(File f) {
		if (f.isFile()) {// 是文件
			f.delete();
		} else if (f.isDirectory()) {// 是目录
			/* 列出该目录下的所有文件 */
			File[] entries = f.listFiles();
			int size = entries.length;
			/* 逐个删除 */
			for (int i = 0; i < size; i++) {
				if (entries[i].isDirectory()) {
					delFile(entries[i]);
				} else {
					entries[i].delete();
				}
			}
		}
	}

	/**
	 * 另存为的监听类
	 * 
	 * @author Administrator
	 * 
	 */
	private class SaveAsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((tableAffix.getDataSet() != null)
					&& !tableAffix.getDataSet().isEmpty()
					&& !tableAffix.getDataSet().bof()
					&& !tableAffix.getDataSet().eof()) {
			} else {
				JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体附件");
				return;
			}
			try {
				String title = tableAffix.getDataSet().fieldByName(
						IPrjAudit.MainTable.Affix.AFFIX_FILE).getString();
				// 创建文本选择器
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(title));
				int status = chooser.showSaveDialog(Global.mainFrame);
				if (status == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile() == null) {
						JOptionPane.showMessageDialog(Global.mainFrame,
								"请选择文件保存地址");
						return;
					}
					// 目标文件地址
					pa.saveAffixFile("rp_audit_affix", tableAffix.getDataSet(),
							chooser.getSelectedFile());
				}
				JOptionPane.showMessageDialog(Global.mainFrame, "保存成功");
			} catch (Exception ee) {
				JOptionPane.showMessageDialog(Global.mainFrame, "保存失败");
			}
		}
	}
}