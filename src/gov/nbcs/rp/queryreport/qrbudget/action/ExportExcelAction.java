/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.export.action.ExportToExcel;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.progress.ProfressBarWin;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.DivObject;
import gov.nbcs.rp.queryreport.qrbudget.ui.ExportExcelUI;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

/**
 * <p>
 * Title: ��ѯ������ģ��
 * </p>
 * <p>
 * Description:��ѯ������ģ��
 * </p>
 * <p>

 */
public class ExportExcelAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	private ExportExcelUI exportExcelUI;;

	private CustomTree ftreDivName;

	private CustomTree ftreReportName;

	private String sExportType;

	private String sFileType;

	private String Hpath = null;
	private String Opath = null;
	private String path = null;

	public void actionPerformed(ActionEvent arg0) {

		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof ExportExcelUI) {
			exportExcelUI = (ExportExcelUI) modulePanel;
			try {
				sExportType = exportExcelUI.getFrdoExportType().getValue()
						.toString();
				sFileType = exportExcelUI.getFrdoFileType().getValue()
						.toString();
				ftreDivName = exportExcelUI.getFtreDivName();
				MyTreeNode[] myTreeNode = getSelectedNodes(ftreDivName,
						sExportType);
				if (myTreeNode.length == 0
						|| (myTreeNode.length == 1 && myTreeNode[0] == ftreDivName
								.getRoot())) {
					if ("0".equals(sExportType)){
					    JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�񵼳���ҵ���ң�",
							     "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					} else if ("1".equals(sExportType)){
						JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�񵼳������ܾ֣�",
							     "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�񵼳��ĵ�λ��",
							     "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					}
					return;
				}
				// �ж�ѡ��Ĺ���������
				if ("0".equals(sFileType) && myTreeNode.length > 255) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"ѡ��λ����EXCEL���������ƣ�", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				ftreReportName = exportExcelUI.getFtreReportName();
				myTreeNode = ftreReportName.getSelectedNodes(true);
				if (myTreeNode.length == 0
						|| (myTreeNode.length == 1 && myTreeNode[0] == ftreReportName
								.getRoot())) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"��ѡ�񵼳���ѯ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				// �ж�ѡ��Ĺ���������
				if ("1".equals(sFileType) && myTreeNode.length > 255) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"��ѡ��λ����EXCEL���������ƣ�", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				// ��ȡĬ��·��//////////////////////////////////////////
				if (Common.isNullStr(Hpath)) {
					Properties pp = new Properties();
					String ffile = "C:\\Documents and Settings\\"
						+ System.getProperty("user.name") + "\\exportfiles\\";
					String efile = ffile + "EXPORTSET.properties";
					File fps = new File(efile);
					if (!fps.exists()) {
						// ��������ļ��������ڴ���			  
				       File expPath = new File(ffile);
				       if(!expPath.exists()){
				    	   expPath.mkdirs();
					   }
				       File tempFile  = new File(expPath,"EXPORTSET.properties");
				       tempFile.createNewFile();
					}
					InputStream fpis = new FileInputStream(fps);
					pp.load(fpis);
					fpis.close();
					Hpath = pp.getProperty("BUDGETEXPORTPATH");
				//	Opath = pp.getProperty("BUDGETEXPORTPATH");
				}
                Opath = Hpath;
                ///////////////////////////////////////////////////////
				JFileChooser jfc = new JFileChooser(Hpath);
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choose = jfc.showOpenDialog(Global.mainFrame);
                    
				if (choose == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					Hpath = file.getAbsolutePath();
					// ����·��/////////////////////////////////////
					if (!Common.nonNullStr(Opath).equals(Hpath+"\\")){
						Properties ppp = new Properties();
						// ��ȡWORKSHEET
						String epfile = "C:\\Documents and Settings\\"
							+ System.getProperty("user.name") + "\\exportfiles\\EXPORTSET.properties";
						File fp = new File(epfile);
						InputStream fisp = new FileInputStream(fp);
						ppp.load(fisp);
						OutputStream fos = new FileOutputStream(fp);
						ppp.setProperty("BUDGETEXPORTPATH", Hpath+"\\");
						ppp.store(fos, "modify parameters");
						fos.close();
						fisp.close();
					}
				    ///////////////////////////////
					path = Hpath
							+ "\\��ѯ����������"
							+ new SimpleDateFormat("yyyy-MM-dd")
									.format(new SimpleDateFormat("yyyy-MM-dd")
											.parse(Tools.getServerDate()))
							+ "\\";
					if (path != null && !path.endsWith("\\")) {
						path = path + "\\";
					}

					file = new File(path);
					// �����Ŀ¼������,�򴴽�֮
					if (file.exists() == false) {
						file.mkdirs();
					}

				} else {
					path = null;
					return;
				}

				// ����ļ��Ƿ��Ѵ���
				if (checkFileIsExist(path)) {
					return;
				}

				MyProgressBar myProgressBar = new MyProgressBar();
				myProgressBar.display();

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"ִ�е���Excel�������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private List getFileIsExist(String sPath) throws Exception {
		List lstMsg = null;
		File f;
		if ("0".equals(sFileType)) { // ����λ�����ļ�
			MyTreeNode[] divNode = getSelectedNodes(ftreDivName, sExportType);
			int len = divNode.length;
			for (int i = 0; i < len; i++) {
				ftreDivName.getDataSet().gotoBookmark(divNode[i].getBookmark());
				String sDivCodeName = ftreDivName.getDataSet().fieldByName(
						IPubInterface.DIV_CODE).getString()
						+ ftreDivName.getDataSet().fieldByName(
								IPubInterface.DIV_NAME).getString();
				// ���ͬ�����ļ����ھ�ɾ��
				String fileName = sPath + sDivCodeName + ".XLS";
				f = new File(fileName);
				if (f.exists()) {
					if (lstMsg == null) {
						lstMsg = new ArrayList();
					}
					lstMsg.add(fileName);
				}

			}
		} else if ("1".equals(sFileType)) { // ���������������ļ�

			MyTreeNode[] reportNode = this.ftreReportName
					.getSelectedNodes(true);
			int len = reportNode.length;
			for (int i = 0; i < len; i++) {
				this.ftreReportName.getDataSet().gotoBookmark(
						reportNode[i].getBookmark());
				// �õ����������ַ���
				String sReportName = this.ftreReportName.getDataSet()
						.fieldByName(IQrBudget.REPORT_CNAME).getString();
				// ���ͬ�����ļ����ھ�ɾ��
				String fileName = sPath + sReportName + ".XLS";
				f = new File(fileName);
				if (f.exists()) {
					if (lstMsg == null) {
						lstMsg = new ArrayList();
					}
					lstMsg.add(fileName);
				}
			}
		}
		return lstMsg;

	}

	/**
	 * �ļ��Ƿ����
	 * 
	 * @param lstFileName
	 * @return true:���ڣ�false:������
	 * @throws Exception
	 */
	private boolean checkFileIsExist(String sPath) throws Exception {

		List lstFileName = getFileIsExist(sPath);

		if (lstFileName == null) {
			return false;
		} else {
			int size = lstFileName.size();
			if (size == 0)
				return false;

			// ��ʾ�Ƿ�ȷ��ɾ��
			if (JOptionPane.showConfirmDialog(Global.mainFrame,
					"�ļ��Ѵ��ڣ��Ƿ��滻?�ǣ��滻�ļ�����ȡ������������", "��ʾ",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
				return true;
			}

			File f;
			for (int i = 0; i < size; i++) {
				f = new File(lstFileName.get(i).toString());
				if (f.exists()) {
					f.delete();
				}
			}
			return false;
		}
	}

	private class MyProgressBar implements Runnable {

		public MyProgressBar() {
		}

		public void display() {
			Thread myThread = new Thread(this);
			myThread.start();
		}

		public void run() {
			doExecute();
		}
	}

	private void doExecute() {

		if ("0".equals(sFileType)) { // ���ݵ�λ�����ļ�
			doExecuteWithDiv();
		} else { // ���ݱ��������ļ�
			doExecuteWithFile();
		}
	}

	/**
	 * ���ݵ�λ�����ļ���ÿ����λһ���ļ���
	 * 
	 */
	private void doExecuteWithDiv() {
		MyTreeNode[] divNode = getSelectedNodes(ftreDivName, sExportType);
		int len = divNode.length;

		int bResult = ReportExcel.FALSE_OPTION;
		ProfressBarWin pf = new ProfressBarWin(len);
		pf.setText("���ڵ���Excel�����Ժ򡤡���������");

		// �汾��
		String sVerNo = exportExcelUI.getFpnlToolBar().getVerNo();
		boolean isNotExportWhenEmpty = exportExcelUI.getFpnlToolBar()
				.getIsNotExportWhenEmpty();

		try {
			for (int i = 0; i < len; i++) {

				ftreDivName.getDataSet().gotoBookmark(divNode[i].getBookmark());
				String sDivCodeName = ftreDivName.getDataSet().fieldByName(
						IPubInterface.DIV_CODE).getString()
						+ ftreDivName.getDataSet().fieldByName(
								IPubInterface.DIV_NAME).getString();
				pf.setText("���ڵ���" + sDivCodeName + "��λ���ݣ����Ժ򡤡���������");
				// �õ���λ�����ַ���
				String sDivName = ftreDivName.getDataSet().fieldByName(
						IPubInterface.DIV_NAME).getString();
				// �õ���ѯbatch��ѯ����
				InfoPackage infoPackage = exportExcelUI.getFpnlToolBar()
						.getFilter();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"����Ԥ���ѯ����Excel�������󣬴�����Ϣ:"
									+ infoPackage.getsMessage(), "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String sStatusWhere = infoPackage.getsMessage();

				// �õ���λ��ѯ����
				List lstDept = new ArrayList();
				List lstDiv = new ArrayList();

				if ("0".equals(sExportType)) { // ��ҵ����
					lstDept.add(ftreDivName.getDataSet().fieldByName(
							IQrBudget.EN_ID).getString());
				} else { // �����ܾ�, �����㵥λ
					DivObject divObject = new DivObject();
					divObject.sDivCode = ftreDivName.getDataSet().fieldByName(
							IPubInterface.DIV_CODE).getString();
					divObject.isLeaf = divNode[i].isLeaf();
					lstDiv.add(divObject);
				}

				ExportExcel exportExcel = new ExportExcel(sDivName, lstDept,
						lstDiv, sStatusWhere, sVerNo, isNotExportWhenEmpty);
				exportExcel.setSPath(path);
				exportExcel.doReportExcelWithDiv(ftreReportName, sDivCodeName);
				ExportToExcel eep = exportExcel.getEep();
				// eep.setTitle(sDivCodeName + "��ѯ��");
				if (eep.doExport()) {
					bResult = ReportExcel.TRUE_OPTION;
				} else {
					if (eep.getIsCancel()) {
						bResult = ReportExcel.CANCEL_OPTION;
					}
					else{
						bResult = ReportExcel.FALSE_OPTION;
					}
				}
				pf.setValue(i + 1);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"���\"����\"��ť�������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		} finally {
			pf.dispose();
		}

		if (bResult == ReportExcel.CANCEL_OPTION)
			return;
		else if (bResult == ReportExcel.TRUE_OPTION)
			new MessageBox("�����ļ��ɹ���", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		else
			new MessageBox("�����ļ�ʧ�ܣ�", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
	}

	/**
	 * ���ݱ����ļ����ɱ���ÿ����������һ���ļ���
	 * 
	 */
	private void doExecuteWithFile() {
		MyTreeNode[] divNode = getSelectedNodes(this.ftreDivName, sExportType);

		MyTreeNode[] reportNode = this.ftreReportName.getSelectedNodes(true);
		int len = reportNode.length;
		int bResult = ReportExcel.FALSE_OPTION;
		ProfressBarWin pf = new ProfressBarWin(len);
		pf.setText("���ڵ���Excel�����Ժ򡤡���������");

		// �汾��
		String sVerNo = exportExcelUI.getFpnlToolBar().getVerNo();
		boolean isNotExportWhenEmpty = exportExcelUI.getFpnlToolBar()
				.getIsNotExportWhenEmpty();

		try {
			// �õ���λ��ѯ����
			List lstDept = new ArrayList();
			List lstDiv = new ArrayList();

			for (int i = 0; i < divNode.length; i++) {
				ftreDivName.getDataSet().gotoBookmark(divNode[i].getBookmark());
				if ("0".equals(sExportType)) { // ��ҵ����
					lstDept.add(ftreDivName.getDataSet().fieldByName(
							IQrBudget.EN_ID).getString());
				} else { // �����ܾ�, �����㵥λ
					DivObject divObject = new DivObject();
					divObject.sDivCode = ftreDivName.getDataSet().fieldByName(
							IPubInterface.DIV_CODE).getString();
					divObject.isLeaf = divNode[i].isLeaf();
					lstDiv.add(divObject);
				}
			}

			String sFileType = exportExcelUI.getFrdoFileType().getValue()
					.toString();
			for (int i = 0; i < len; i++) {
				this.ftreReportName.getDataSet().gotoBookmark(
						reportNode[i].getBookmark());
				// �õ����������ַ���
				String sReportName = this.ftreReportName.getDataSet()
						.fieldByName(IQrBudget.REPORT_CNAME).getString();
				pf.setText("���ڵ���" + sReportName + "��ѯ�����ݣ����Ժ򡤡���������");

				// �õ���ѯbatch��ѯ����
				InfoPackage infoPackage = exportExcelUI.getFpnlToolBar()
						.getFilter();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"����Ԥ���ѯ����Excel�������󣬴�����Ϣ:"
									+ infoPackage.getsMessage(), "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String sStatusWhere = infoPackage.getsMessage();

				ExportExcel exportExcel = new ExportExcel(lstDept, lstDiv,
						sStatusWhere, sVerNo, sFileType, isNotExportWhenEmpty);
				exportExcel.setSPath(path);
				exportExcel.doReportExcelWithFile(this.ftreReportName,
						this.ftreDivName, reportNode[i], divNode, sExportType);
				ExportToExcel eep = exportExcel.getEep();
				// eep.setTitle(sReportName + "��ѯ��");
				if (eep.doExport()) {
					bResult = ReportExcel.TRUE_OPTION;
				} else {
					if (eep.getIsCancel()) {
						bResult = ReportExcel.CANCEL_OPTION;
					}
					bResult = ReportExcel.FALSE_OPTION;
				}
				pf.setValue(i + 1);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"���\"����\"��ť�������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		} finally {
			pf.dispose();
		}

		if (bResult == ReportExcel.CANCEL_OPTION)
			return;
		else if (bResult == ReportExcel.TRUE_OPTION)
			new MessageBox("�����ļ��ɹ���", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		else
			new MessageBox("�����ļ�ʧ�ܣ�", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
	}

	/**
	 * ��ȡ����ѡ��Ľ��
	 * 
	 * @param onlyLeaf
	 *            �Ƿ�ֻ���Ҷ�ӽ��
	 * @return
	 */
	private MyTreeNode[] getSelectedNodes(CustomTree ftreDivName,
			String sExportType) {
		if ("0".equals(sExportType)) { // ��ҵ����
			return getSelectedNodes(1, ftreDivName);

		} else if ("1".equals(sExportType)) { // �����ܾ�
			return getSelectedNodes(2, ftreDivName);
		} else if ("2".equals(sExportType)) { // �����㵥λ
			return ftreDivName.getSelectedNodes(true);

		}

		return null;
	}

	/**
	 * ���ݲ�λ��ѡ�еĽڵ�
	 * 
	 * @param iLev
	 *            ���
	 * @param ftreDivName
	 *            ��λ��F
	 * @return
	 */
	private MyTreeNode[] getSelectedNodes(int iLev, CustomTree ftreDivName) {
		DefaultMutableTreeNode root = ftreDivName.getRoot();
		if (root != null) {
			List cache = new ArrayList();
			for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration
					.hasMoreElements();) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
						.nextElement();

				if (node.getLevel() == iLev) {
					MyPfNode myPfNode = (MyPfNode) node.getUserObject();
					if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
						continue;
					if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
						cache.add(node);
						continue;
					}
					if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
						continue;
					}
				}
			}
			MyTreeNode result[] = new MyTreeNode[cache.size()];
			System.arraycopy(cache.toArray(), 0, result, 0, result.length);
			return result;
		}
		return null;
	}

}
