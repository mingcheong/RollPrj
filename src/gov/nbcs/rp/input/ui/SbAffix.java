package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditStub;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.fileupload.*;



public class SbAffix extends RpModulePanel implements PrjActionUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PrjAuditAction pa;
	
	/** ��Ŀ���� */
	public FTextField tfAffix;
	
	private FButton btnAdd;
	private FButton btnDel;
	private FButton btnSaveAs;
	private FButton btnAffixView;
	public CustomTable tableAffix;
	//private List prjCodes;
	private int editstate = 0;
	private JFileChooser fileChooser;
	private InputStream is = null;
	private ByteArrayOutputStream data = null;
	private byte[] result;
	private String rowid;
	
	private String auditXmbm = "";//��Ŀ����
	private String auditXmxh = "";
	private String en_id = "";
	private String en_code = "";
	
	public SbAffix(PrjAuditAction pa){
		this.pa = pa;
	}
	
	public void setEn_id(String en_id){
		this.en_id = en_id;
	}
	public String getEn_id(){
		return this.en_id;
	}
	public void setEn_code(String en_code){
		this.en_code = en_code;
	}
	public String getEn_code(){
		return this.en_code;
	}
	
	public void setAuditXmxh(String auditXmxh){
		this.auditXmxh = auditXmxh;
	}
	
	public String getAuditXmxh(){
		return this.auditXmxh;
	}
	public void setAuditXmbm(String auditXmbm){
		this.auditXmbm = auditXmbm;
	}
	
	public String getAuditXmbm(){
		return this.auditXmbm;
	}
	
	public void setEnabledAdd(boolean b){
		this.btnAdd.setEnabled(b);
	}
	public void setEnabledDel(boolean b){
		this.btnDel.setEnabled(b);
	}
	
	public FPanel setBottomPanel() {		
		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		lay.setRowHeight(14);
		pnlBase.setLayout(lay);
		tfAffix = new FTextField("������");
		tfAffix.setProportion(0.3f);
		btnAdd = new FButton("add", "����");
		btnDel = new FButton("del", "ɾ��");
		btnSaveAs = new FButton("saveas", "���Ϊ");
		btnAffixView = new FButton("btnView", "�鿴");
		String[] columnText = new String[] { "��������", "�����ļ�����",
				"��������" };
		String[] columnField = new String[] {"affix_title","affix_file","affix_type"};
		try {
			tableAffix = new CustomTable(columnText, columnField, null, false,
					null);
		} catch (Exception eee) {
			ErrorInfo.showErrorDialog(eee, "���ɸ�����ʧ��");
		}
		FPanel pnlD = new FPanel();
		RowPreferedLayout layD = new RowPreferedLayout(8);
		layD.setRowHeight(30);
		layD.setRowGap(300);
		layD.setColumnGap(10);
		pnlD.setLayout(layD);
		
		pnlD.addControl(btnAdd, new TableConstraints(1, 1, false, true));
		pnlD.addControl(btnDel, new TableConstraints(1, 1, false, true));

		pnlD.addControl(btnAffixView, new TableConstraints(1, 1, false, true));
		pnlD.addControl(btnSaveAs, new TableConstraints(1, 1, false, true));
		
		this.btnAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if("".equals(auditXmbm)){
					JOptionPane.showMessageDialog(null, "��ѡ�������Ŀ");
					return;
				}
				try {
					 
					
					editstate = 1;
					createChoose();
					int returnval = fileChooser.showOpenDialog(Global.mainFrame);
					if (returnval == JFileChooser.CANCEL_OPTION) {
						return;
					}
					File selectedFile = fileChooser.getSelectedFile();
					saveAffixInfo(selectedFile, 1);
					
					
					is = new FileInputStream(selectedFile);
					FileItem fi = null ;
					fi.write(selectedFile);
				

					
					
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
				} catch (Exception ee) {
					ErrorInfo.showErrorDialog(ee, "��������ʧ��");
				}
			}
		});
		btnAffixView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewFile(true);
				} catch (Exception ee) {
					ErrorInfo.showErrorDialog(ee, "�����鿴ʧ��");
				}
			}
		});
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveAffixInfo(null, 3);
				} catch (Exception ee) {
					ErrorInfo.showErrorDialog(ee, "����ɾ��ʧ��");
				}
			}
		});
		btnSaveAs.addActionListener(new SaveAsActionListener());
		pnlBase.addControl(pnlD, new TableConstraints(2, 1, false, true));
		pnlBase.addControl(tableAffix, new TableConstraints(10, 1, true, true));
		pnlBase.setTopInset(10);
		pnlBase.setLeftInset(10);
		pnlBase.setRightInset(10);
		pnlBase.setBottomInset(10);
		return pnlBase ;
	}
	
	/**
	 * ���ñ������
	 * 
	 */
	public void setTableData() throws Exception {
		String sql = "select ROW_ID,SET_YEAR,XMXH,XMBM,AFFIX_TITLE,AFFIX_TYPE,AFFIX_FILE,LAST_VER"
				+ " from "
				+ " RP_XM_AUDIT_AFFIX " + " where set_year = " + Global.loginYear+" and xmxh = '"+this.getAuditXmxh()+"'";
		DataSet ds = DBSqlExec.client().getDataSet(sql);
		this.tableAffix.setDataSet(ds);
		tableAffix.reset();
		this.setTableProp();
	}
	
	public void setTableProp() throws Exception {
		tableAffix.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableAffix.reset();
		tableAffix.getTable().getColumnModel().getColumn(0).setPreferredWidth(
				250);
		tableAffix.getTable().getColumnModel().getColumn(1).setPreferredWidth(
				300);
		tableAffix.getTable().getColumnModel().getColumn(2).setPreferredWidth(
				200);
		tableAffix.getTable().setRowHeight(25);
		tableAffix.getTable().getTableHeader().setBackground(
				new Color(250, 228, 184));
		tableAffix.getTable().getTableHeader().setPreferredSize(new Dimension(30,30));
	}
	
	/**
	 * �����ļ�ѡ����
	 * 
	 * @throws Exception
	 */
	private void createChoose() throws Exception {
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("ѡ�񸽼�");
		// �趨���õ��ļ��ĺ�׺��
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				// if (f.isDirectory()) {
				// return true;
				// }
				// return false;
				return true;
			}

			public String getDescription() {
				return "�����ļ�(*.*)";
			}
		});
	}
	
	/**
	 * 
	 * @param file
	 * @param state
	 *            0������� 1������ 2���޸� 3��ɾ��
	 * @throws Exception
	 */
	private void saveAffixInfo(File file, int state) throws Exception {
		StringBuffer sql = new StringBuffer();

		if (1 == state) {
			tableAffix.getDataSet().append();
			String path = file.getPath();
			String title = path.substring(path.lastIndexOf("\\") + 1, path
					.lastIndexOf("."));
			String type = path.substring(path.lastIndexOf(".") + 1, path
					.length());
			sql.append("insert into rp_xm_audit_affix");
			sql.append("(ROW_ID,SET_YEAR,RG_CODE,LAST_VER,EN_ID,EN_CODE,XMXH,XMBM,AFFIX_TITLE,AFFIX_TYPE,AFFIX_FILE,AFFIX_CONT");
			sql.append(") values ('");
			rowid = UUID.randomUUID().toString();
			sql.append(rowid);
			sql.append("','");
			sql.append(Global.loginYear);
			sql.append("','");
			sql.append(Global.getCurrRegion());
			sql.append("','");
			sql.append(Tools.getCurrDate());
			sql.append("','");
			sql.append(this.getEn_id());
			sql.append("','");
			sql.append(this.getEn_code());
			sql.append("','");
			sql.append(this.getAuditXmxh());
			sql.append("','");
			sql.append(this.getAuditXmbm());
			sql.append("','");
			sql.append(title);
			sql.append("','");
			sql.append(type);
			sql.append("','");
			sql.append(title + "." + type);
			sql.append("',");
			sql.append("empty_blob()");
			sql.append(")");
		} else if (3 == state) {
			if ((tableAffix.getDataSet() != null)
					&& !tableAffix.getDataSet().isEmpty()
					&& !tableAffix.getDataSet().bof()
					&& !tableAffix.getDataSet().eof()) {
				sql.append("delete from rp_xm_audit_affix");
				sql.append(" where row_id");
				sql.append("='");
				sql.append(tableAffix.getDataSet().fieldByName("row_id").getString());
				sql.append("'");
				sql.append(" and set_year = ");
				sql.append(Global.loginYear);
			} else {
				JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ��Ҫɾ���ĸ���");
				return;
			}
		}
	
		QueryStub.getClientQueryTool().executeUpdate(sql.toString());
		tableAffix.getDataSet().applyUpdate();
		setTableData();
	}
	
	/**
	 * ���渽���ļ�
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
			PrjAuditStub.getMethod().modifyDocBlobs("RP_XM_AUDIT_AFFIX", row_id,
					Global.getSetYear(), result);
			is.close();
			data.close();
		}
	}
	
	/**
	 * �鿴(ͨ��WINDOWS�ӿڴ�)
	 * 
	 * @throws Exception
	 */
	protected void viewFile(boolean isCanEdit) throws Exception {
		if ((tableAffix.getDataSet() != null)
				&& !tableAffix.getDataSet().isEmpty()
				&& !tableAffix.getDataSet().bof()
				&& !tableAffix.getDataSet().eof()) {
		} else {
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ����帽��");
			return;
		}
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			// ���ݼ�¼��������ȡ�ļ�����
			// �����ʱ�ļ����е�����ǰ����ʷ�ļ�
			File cleaner = File.createTempFile("temp", ".temp");
			delOldFileUnderDir(cleaner.getParentFile());

			// ������ʱ�ļ�
			String sAddr = (Common.nonNullStr(this.tableAffix.getDataSet()
					.fieldByName("affix_file")
					.getString()));
			int ilastdot = sAddr.lastIndexOf(".");
			String tempFileType = sAddr.substring(ilastdot, sAddr.length());
			File temp = File.createTempFile("temp", tempFileType);

			// �����ļ������
			fos = new FileOutputStream(temp);
			// ���뻺������������Ч��
			bos = new BufferedOutputStream(fos);
			// ������ȡblob��ת��Ϊ�ֽ�,���������
			int count = 1;
			boolean isSuccessful = true;
			while (true) {
				byte[] buf = PrjAuditStub.getMethod().getDocBlob(
						"rp_xm_audit_affix",
						count++,
						this.tableAffix.getDataSet().fieldByName("row_id").getString(),
						Global.loginYear);
				if (buf == null) {
					JOptionPane.showMessageDialog(Global.mainFrame, "��ȡ�ļ�ʧ��!",
							"��Ϣ", JOptionPane.INFORMATION_MESSAGE);
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
			// ����ֻ��
			if (!isCanEdit) {
				temp.setReadOnly();
			}
			// ���ò���ϵͳ�еĹ�����ʽ���ļ�
			if (isSuccessful) {
				Runtime rt = Runtime.getRuntime();
				rt.exec("cmd  /c  start  " + temp.getAbsolutePath());
			}
		} catch (Exception e) {
			ErrorInfo.showErrorDialog(e, "��ȡ�ļ�ʧ�ܣ�");
		} finally {
			/* �ر��� */
			try {
				bos.close();
				fos.close();
			} catch (IOException e) {
				// ���쳣���ظ��û���������Ӱ�����������
			} finally {
				bos = null;
				fos = null;
			}
		}
	}
	
	/**
	 * �����ʱ�ļ����зǵ�����ļ�
	 */
	private void delOldFileUnderDir(File f) {
		if (f.isDirectory()) {
			/* �г���Ŀ¼�µ������ļ� */
			File[] entries = f.listFiles();
			int size = entries.length;
			/* ���ɾ�� */
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
	 * ɾ���ļ�
	 */
	public void delFile(File f) {
		if (f.isFile()) {// ���ļ�
			f.delete();
		} else if (f.isDirectory()) {// ��Ŀ¼
			/* �г���Ŀ¼�µ������ļ� */
			File[] entries = f.listFiles();
			int size = entries.length;
			/* ���ɾ�� */
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
	 * ���Ϊ�ļ�����
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
				JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ����帽��");
				return;
			}
			try {
				String title = tableAffix.getDataSet().fieldByName("affix_file").getString();
				// �����ı�ѡ����
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(title));
				int status = chooser.showSaveDialog(Global.mainFrame);
				if (status == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile() == null) {
						JOptionPane.showMessageDialog(Global.mainFrame,
								"��ѡ���ļ������ַ");
						return;
					}
					// Ŀ���ļ���ַ
					pa.saveAffixFileForXm("rp_xm_audit_affix", tableAffix.getDataSet(),
							chooser.getSelectedFile());
				}
				JOptionPane.showMessageDialog(Global.mainFrame, "����ɹ�");
			} catch (Exception ee) {
				JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��");
			}
		}
	}
	
	

    public void SaveIntoserver(boolean type) {
    	if(type)
    	{
    		
    	
//    		save
    	}
    	
    	
			
		}
	

	

	public void initize() {
		// TODO Auto-generated method stub
		
	}

	public void doExport() {
		// TODO Auto-generated method stub
		
	}

	public void doSendToAudit() {
		// TODO Auto-generated method stub
		
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
	
	public void setButtonState(boolean isEdit){
		this.btnAdd.setEnabled(isEdit);
		this.btnDel.setEnabled(isEdit);
	}

	public void doAuditToBack() {
		// TODO Auto-generated method stub
		
	}

	public void doBackToAudit() {
		// TODO Auto-generated method stub
		
	}

	public void doUpdatedlg() {
		// TODO Auto-generated method stub
		
	}

}
