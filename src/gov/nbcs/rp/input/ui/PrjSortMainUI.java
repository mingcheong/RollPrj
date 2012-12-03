package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class PrjSortMainUI extends RpModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	CustomTree treeEn = null;

	CustomTable ct = null;

	private String enID = "";
	
	private String identity = "";

	public void initize() {
		// TODO Auto-generated method stub
		try {
			// 初始化数据
			initData();
			// 添加主面板
			this.add(getBasePanel());
			this.createToolBar();
			// 设置初始化界面和按钮状态

		} catch (Exception e) {
			// 开发时使用 e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "界面初始化失败");
		}
	}

	private void initData() throws Exception {
		if(GlobalEx.isFisVis()){
			identity = "cs";//财政用户
		}else{
			identity = "en";//单位用户			
		}
	}

	/**
	 * 获取主面板
	 * 
	 * @return
	 * @throws Exception
	 */
	public FPanel getBasePanel() throws Exception {

		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(300);
		FScrollPane pnlTree = new FScrollPane();
		DataSet ds = PubInterfaceStub.getMethod().getDivDataPop(
				Global.loginYear);
		treeEn = new CustomTree("预算单位", ds, "en_id", "code_name", "parent_id",
				null, "div_code");
		treeEn.reset();
		treeEn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					if ((treeEn != null) && (treeEn.getSelectedNode() != null)
							&& (treeEn.getSelectedNode() != null)) {
						MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode()
								.getUserObject();
						if ((enPer == null) || "".equals(enPer.getValue())) {
							return;
						}
						if (enPer.getIsLeaf()) {
							enID = enPer.getValue();
							try {
								DataSet dsCTable = PrjInputStub.getMethod()
										.getXmSortListByEnID(
												Global.loginYear,
												Global.getCurrRegion(),
												enPer.getValue(),identity);
								ct.setDataSet(dsCTable);
								ct.reset();
								dsCTable.edit();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		CustomTreeFinder cf = new CustomTreeFinder(treeEn);
		pnlTree.addControl(treeEn);

		FPanel pnlCF = new FPanel();
		RowPreferedLayout layCF = new RowPreferedLayout(1);
		pnlCF.setLayout(layCF);
		pnlCF.addControl(cf, new TableConstraints(1, 1, false, true));
		pnlCF.addControl(pnlTree, new TableConstraints(1, 1, true, true));
		pnlCF.setLeftInset(10);
		pnlCF.setRightInset(10);
		pnlCF.setBottomInset(10);

		String[] columnText = new String[] { "预算单位", "项目编码", "项目名称", "设置排序" };
		String[] columnField = null ; 
		String[] editFields = null; 
		if("cs".equals(identity)){
			columnField = new String[] { "div_name", "xmbm", "xmmc","cs_sort" };
			editFields = new String[] { "cs_sort" };
		}else{
			columnField = new String[] { "div_name", "xmbm", "xmmc","en_sort" };
			editFields = new String[] { "en_sort" };
		}
		ct = new CustomTable(columnText, columnField, null, false, editFields);
		ct.reset();
		ct.getTable().getTableHeader().setPreferredSize(new Dimension(0, 30));
		ct.getTable().setRowHeight(30);
		ct.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
		ct.getTable().repaint();

		FPanel lPanel = new FPanel();
		RowPreferedLayout lLay = new RowPreferedLayout(1);
		lPanel.setLayout(lLay);
		lPanel.addControl(ct, new TableConstraints(1, 1, true, true));
		lPanel.setLeftInset(10);
		lPanel.setRightInset(10);
		lPanel.setBottomInset(10);

		pnlInfo.addControl(pnlCF);
		pnlInfo.addControl(lPanel);
		pnlBase.addControl(pnlInfo, new TableConstraints(1, 1, true, true));
		return pnlBase;
	}

	public void doSave() {
		ct.getTable().setFocusable(true);
		List saveList = new ArrayList();
		
			try {
				for (int i = 0; i < ct.getTable().getRowCount(); i++) {
					String bmk = ct.rowToBookmark(i);
					if (ct.getDataSet().gotoBookmark(bmk)) {
						String xmxh = ct.getDataSet().fieldByName("xmxh").getString();
						if("cs".equals(identity)){
							String cs_sort = ct.getDataSet().fieldByName("cs_sort").getString();
							saveList.add("update rp_xmjl set cs_sort = '"+cs_sort+"' where xmxh = '"+xmxh+"'");
						}else{
							String cs_sort = ct.getDataSet().fieldByName("en_sort").getString();
							saveList.add("update rp_xmjl set en_sort = '"+cs_sort+"' where xmxh = '"+xmxh+"'");
						}
						
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		

		try {
			PrjInputStub.getMethod().postData(saveList);
			DataSet ds = PrjInputStub.getMethod().getXmSortListByEnID(
					Global.loginYear, Global.getCurrRegion(), enID,identity);
			ct.setDataSet(ds);
			ct.reset();
			ds.edit();
			JOptionPane.showMessageDialog(null, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "保存失败");
		}
	}

	/**
	 * 取消
	 */
	public void doCancel() {
		DataSet dsCTable;
		try {
			dsCTable = PrjInputStub.getMethod().getXmSortListByEnID(
					Global.loginYear, Global.getCurrRegion(), enID,identity);
			ct.setDataSet(dsCTable);
			ct.reset();
			dsCTable.edit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
