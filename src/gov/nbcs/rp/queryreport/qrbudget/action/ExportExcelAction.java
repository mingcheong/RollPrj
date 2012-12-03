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
 * Title: 查询报表导出模块
 * </p>
 * <p>
 * Description:查询报表导出模块
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
					    JOptionPane.showMessageDialog(Global.mainFrame, "请选择导出的业务处室！",
							     "提示", JOptionPane.INFORMATION_MESSAGE);
					} else if ("1".equals(sExportType)){
						JOptionPane.showMessageDialog(Global.mainFrame, "请选择导出的主管局！",
							     "提示", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(Global.mainFrame, "请选择导出的单位！",
							     "提示", JOptionPane.INFORMATION_MESSAGE);
					}
					return;
				}
				// 判断选择的工作簿数量
				if ("0".equals(sFileType) && myTreeNode.length > 255) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"选择单位超出EXCEL工作簿限制！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				ftreReportName = exportExcelUI.getFtreReportName();
				myTreeNode = ftreReportName.getSelectedNodes(true);
				if (myTreeNode.length == 0
						|| (myTreeNode.length == 1 && myTreeNode[0] == ftreReportName
								.getRoot())) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"请选择导出查询表！", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				// 判断选择的工作簿数量
				if ("1".equals(sFileType) && myTreeNode.length > 255) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"请选择单位超出EXCEL工作簿限制！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				// 获取默认路径//////////////////////////////////////////
				if (Common.isNullStr(Hpath)) {
					Properties pp = new Properties();
					String ffile = "C:\\Documents and Settings\\"
						+ System.getProperty("user.name") + "\\exportfiles\\";
					String efile = ffile + "EXPORTSET.properties";
					File fps = new File(efile);
					if (!fps.exists()) {
						// 如果配置文件不存在在创建			  
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
					// 设置路进/////////////////////////////////////
					if (!Common.nonNullStr(Opath).equals(Hpath+"\\")){
						Properties ppp = new Properties();
						// 获取WORKSHEET
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
							+ "\\查询表批量导出"
							+ new SimpleDateFormat("yyyy-MM-dd")
									.format(new SimpleDateFormat("yyyy-MM-dd")
											.parse(Tools.getServerDate()))
							+ "\\";
					if (path != null && !path.endsWith("\\")) {
						path = path + "\\";
					}

					file = new File(path);
					// 如果该目录不存在,则创建之
					if (file.exists() == false) {
						file.mkdirs();
					}

				} else {
					path = null;
					return;
				}

				// 检查文件是否已存在
				if (checkFileIsExist(path)) {
					return;
				}

				MyProgressBar myProgressBar = new MyProgressBar();
				myProgressBar.display();

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"执行导出Excel发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private List getFileIsExist(String sPath) throws Exception {
		List lstMsg = null;
		File f;
		if ("0".equals(sFileType)) { // 按单位生成文件
			MyTreeNode[] divNode = getSelectedNodes(ftreDivName, sExportType);
			int len = divNode.length;
			for (int i = 0; i < len; i++) {
				ftreDivName.getDataSet().gotoBookmark(divNode[i].getBookmark());
				String sDivCodeName = ftreDivName.getDataSet().fieldByName(
						IPubInterface.DIV_CODE).getString()
						+ ftreDivName.getDataSet().fieldByName(
								IPubInterface.DIV_NAME).getString();
				// 如果同名的文件存在就删除
				String fileName = sPath + sDivCodeName + ".XLS";
				f = new File(fileName);
				if (f.exists()) {
					if (lstMsg == null) {
						lstMsg = new ArrayList();
					}
					lstMsg.add(fileName);
				}

			}
		} else if ("1".equals(sFileType)) { // 按报表名称生成文件

			MyTreeNode[] reportNode = this.ftreReportName
					.getSelectedNodes(true);
			int len = reportNode.length;
			for (int i = 0; i < len; i++) {
				this.ftreReportName.getDataSet().gotoBookmark(
						reportNode[i].getBookmark());
				// 得到报表名称字符串
				String sReportName = this.ftreReportName.getDataSet()
						.fieldByName(IQrBudget.REPORT_CNAME).getString();
				// 如果同名的文件存在就删除
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
	 * 文件是否存在
	 * 
	 * @param lstFileName
	 * @return true:存在，false:不存在
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

			// 提示是否确定删除
			if (JOptionPane.showConfirmDialog(Global.mainFrame,
					"文件已存在，是否替换?是：替换文件，否：取消导出操作。", "提示",
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

		if ("0".equals(sFileType)) { // 根据单位生成文件
			doExecuteWithDiv();
		} else { // 根据报表生成文件
			doExecuteWithFile();
		}
	}

	/**
	 * 根据单位生成文件（每个单位一个文件）
	 * 
	 */
	private void doExecuteWithDiv() {
		MyTreeNode[] divNode = getSelectedNodes(ftreDivName, sExportType);
		int len = divNode.length;

		int bResult = ReportExcel.FALSE_OPTION;
		ProfressBarWin pf = new ProfressBarWin(len);
		pf.setText("正在导出Excel，请稍候······");

		// 版本号
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
				pf.setText("正在导出" + sDivCodeName + "单位数据，请稍候······");
				// 得到单位名称字符串
				String sDivName = ftreDivName.getDataSet().fieldByName(
						IPubInterface.DIV_NAME).getString();
				// 得到查询batch查询条件
				InfoPackage infoPackage = exportExcelUI.getFpnlToolBar()
						.getFilter();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"部门预算查询表导出Excel发生错误，错误信息:"
									+ infoPackage.getsMessage(), "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String sStatusWhere = infoPackage.getsMessage();

				// 得到单位查询条件
				List lstDept = new ArrayList();
				List lstDiv = new ArrayList();

				if ("0".equals(sExportType)) { // 按业务处室
					lstDept.add(ftreDivName.getDataSet().fieldByName(
							IQrBudget.EN_ID).getString());
				} else { // 按主管局, 按基层单位
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
				// eep.setTitle(sDivCodeName + "查询表");
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
					"点击\"导出\"按钮发生错误，错误信息:" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		} finally {
			pf.dispose();
		}

		if (bResult == ReportExcel.CANCEL_OPTION)
			return;
		else if (bResult == ReportExcel.TRUE_OPTION)
			new MessageBox("导出文件成功！", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		else
			new MessageBox("导出文件失败！", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
	}

	/**
	 * 根据报表文件生成报表（每个报表生成一个文件）
	 * 
	 */
	private void doExecuteWithFile() {
		MyTreeNode[] divNode = getSelectedNodes(this.ftreDivName, sExportType);

		MyTreeNode[] reportNode = this.ftreReportName.getSelectedNodes(true);
		int len = reportNode.length;
		int bResult = ReportExcel.FALSE_OPTION;
		ProfressBarWin pf = new ProfressBarWin(len);
		pf.setText("正在导出Excel，请稍候······");

		// 版本号
		String sVerNo = exportExcelUI.getFpnlToolBar().getVerNo();
		boolean isNotExportWhenEmpty = exportExcelUI.getFpnlToolBar()
				.getIsNotExportWhenEmpty();

		try {
			// 得到单位查询条件
			List lstDept = new ArrayList();
			List lstDiv = new ArrayList();

			for (int i = 0; i < divNode.length; i++) {
				ftreDivName.getDataSet().gotoBookmark(divNode[i].getBookmark());
				if ("0".equals(sExportType)) { // 按业务处室
					lstDept.add(ftreDivName.getDataSet().fieldByName(
							IQrBudget.EN_ID).getString());
				} else { // 按主管局, 按基层单位
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
				// 得到报表名称字符串
				String sReportName = this.ftreReportName.getDataSet()
						.fieldByName(IQrBudget.REPORT_CNAME).getString();
				pf.setText("正在导出" + sReportName + "查询表数据，请稍候······");

				// 得到查询batch查询条件
				InfoPackage infoPackage = exportExcelUI.getFpnlToolBar()
						.getFilter();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"部门预算查询表导出Excel发生错误，错误信息:"
									+ infoPackage.getsMessage(), "提示",
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
				// eep.setTitle(sReportName + "查询表");
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
					"点击\"导出\"按钮发生错误，错误信息:" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		} finally {
			pf.dispose();
		}

		if (bResult == ReportExcel.CANCEL_OPTION)
			return;
		else if (bResult == ReportExcel.TRUE_OPTION)
			new MessageBox("导出文件成功！", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		else
			new MessageBox("导出文件失败！", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
	}

	/**
	 * 获取所有选择的结点
	 * 
	 * @param onlyLeaf
	 *            是否只获得叶子结点
	 * @return
	 */
	private MyTreeNode[] getSelectedNodes(CustomTree ftreDivName,
			String sExportType) {
		if ("0".equals(sExportType)) { // 按业务处室
			return getSelectedNodes(1, ftreDivName);

		} else if ("1".equals(sExportType)) { // 按主管局
			return getSelectedNodes(2, ftreDivName);
		} else if ("2".equals(sExportType)) { // 按基层单位
			return ftreDivName.getSelectedNodes(true);

		}

		return null;
	}

	/**
	 * 根据层次获得选中的节点
	 * 
	 * @param iLev
	 *            层次
	 * @param ftreDivName
	 *            单位树F
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
