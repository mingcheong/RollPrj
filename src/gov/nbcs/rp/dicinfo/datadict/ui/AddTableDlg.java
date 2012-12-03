/**
 * @# AddTableDlg.java    <文件名>
 */
package gov.nbcs.rp.dicinfo.datadict.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import gov.nbcs.rp.basinfo.common.BOCache;

import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:添加字典信息的选择对话框，
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class AddTableDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058579385239720339L;

	private static boolean isPressOK = false;// 是否确定

	private static List arrTableSelect;// 选择的表

	private static String tabProp;// 表类别

	private static int page;// 记录选择的页面，标志着要添加的类型

	private String title = "添加表...";

	private FCheckBox chxUse;

	private FTree treTable;// 普通表

	private FTree treDefTable;// 预定义表

	private FTabbedPane tpnlCenter;

	private FComboBox cbxType;// 表分类

	public static final int PAGE_DEFTABLE = 0;// 页面零为预定义树

	public static final int PAGE_TABLE = 1;// 页面一为普通树

	// 用于缓存
	private List lstAllTable;// 全部视图表

	private List lstUnUseTable;// 未使用视图表

	private List lstAllDefTable;// 全部预定义表

	private List lstUnUseDefTable;// 未使用预定表

	IDataDictBO dataDictBO;

	public AddTableDlg() {
		super(Global.mainFrame, true);
		arrTableSelect = null;
		isPressOK = false;
		dataDictBO = (IDataDictBO) BOCache.getBO("rp.dataDictService");
		initUI();
	}

	// 初始化界面
	private void initUI() {
		this.setTitle(title);

		chxUse = new FCheckBox();
		chxUse.setTitle("已使用的表不显示");
		chxUse.setTitlePosition("right");
		chxUse.setValue(new Boolean(true));
		chxUse.setPreferredSize(new Dimension(200, 30));
		chxUse.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				initTreeData(!((Boolean) arg0.getNewValue()).booleanValue());
			}
		});

		FPanel pnlTop = new FPanel();
		pnlTop.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		pnlTop.setLayout(new FlowLayout(FlowLayout.LEFT));

		cbxType = new FComboBox();
		String sTypeRef = dataDictBO.getTableTypeRefString();
		cbxType.setRefModel(sTypeRef);
		cbxType.setTitle("表类别");
		cbxType.setPreferredSize(new Dimension(200, 20));

		pnlTop.add(chxUse);
		pnlTop.add(cbxType);
		// ---------------------
		tpnlCenter = new FTabbedPane();

		// 普通表树
		treTable = new FTree();
		treTable.setRoot("表选择");
		treTable.setIsCheck(true);
		treTable.setRootVisible(false);
		FScrollPane spnlTable = new FScrollPane(treTable);
		// 预定义表树
		treDefTable = new FTree();
		treDefTable.setRoot("表选择");

		treDefTable.setIsCheck(true);
		FScrollPane spnlDefTable = new FScrollPane(treDefTable);
		tpnlCenter.add(spnlDefTable, "业务报表");
		tpnlCenter.add(spnlTable, "数据库表/视图");
		
		// --------------------
		// 按钮
		FPanel pnlBottom = new FPanel();
		pnlBottom.setLayout(new FlowLayout(FlowLayout.CENTER));

		FButton btnOK = new FButton("OK", "确定");
		btnOK.addActionListener(new BtnOkClick());
		FButton btnCancel = new FButton("CANEL", "取消");
		btnCancel.addActionListener(new BtnCancelClick());

		pnlBottom.add(btnOK);
		pnlBottom.add(new JLabel("   "));
		pnlBottom.add(btnCancel);
		// --------------
		FPanel pnlBack = new FPanel();
		pnlBack.setLayout(new BorderLayout());
		pnlBack.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		pnlBack.add(pnlTop, BorderLayout.NORTH);
		pnlBack.add(tpnlCenter, BorderLayout.CENTER);
		pnlBack.add(pnlBottom, BorderLayout.SOUTH);

		initTreeData(false);

		this.getContentPane().add(pnlBack);

		Dimension d = new Dimension(500, 600);

		this.setSize(d);
		this
				.setLocation(
						(Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2,
						(Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2);
		this.setVisible(true);

	}

	public static List getArrTableSelect() {
		return arrTableSelect;
	}

	public static boolean isPressOK() {
		return isPressOK;
	}

	// 确定按钮事件
	class BtnOkClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int iCurIndex = tpnlCenter.getSelectedIndex();
			List lstSelected;
			if (iCurIndex == PAGE_DEFTABLE) {// 预定义表列
				lstSelected = treDefTable.getAllLeafSelectedDatas();
			} else {
				lstSelected = treTable.getAllLeafSelectedDatas();
			}
			if (lstSelected == null || lstSelected.size() == 0) {
				new MessageBox("请选择要添加的表/视图!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			// 去掉不是表的选项，和已使用的表
			int iCount = lstSelected.size();
			String sUseTable = "";
			for (int i = iCount - 1; i >= 0; i--) {
				XMLData aData = (XMLData) lstSelected.get(i);
				if ("1".equals(""))
					lstSelected.remove(i);
				else if ("1".equals("")) {

					sUseTable = sUseTable + "{"
							+"" + "}";

					lstSelected.remove(i);
				}
			}

			if (!sUseTable.equals("")) {
				new MessageBox("表  " + sUseTable + "  已使用将不再进行添加操作!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			}
			if (lstSelected.size() == 0) {
				new MessageBox("请选择要添加的表(视图)!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			tabProp = (String) cbxType.getValue();
			if (tabProp == null || tabProp.equals("")) {
				new MessageBox("请选择要添加的表类别!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			// 设置返回的表
			page = tpnlCenter.getSelectedIndex();
			arrTableSelect = lstSelected;
			isPressOK = true;
			AddTableDlg.this.dispose();
		}

	}

	// 取消按钮事件
	class BtnCancelClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			AddTableDlg.this.dispose();

		}

	}

	private void initTreeData(boolean isAll) {

		try {

			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

			List lstData;
			// 由于要缓存，所以这里做判断
			{
				if (isAll) {
					if (lstAllDefTable == null)
						lstAllDefTable = dataDictBO.getTableInfo(
								IDataDictBO.DEFINE_MODEL, isAll,Global.loginYear);
					lstData = lstAllDefTable;

				} else {
					if (lstUnUseDefTable == null)
						lstUnUseDefTable = dataDictBO.getTableInfo(
								IDataDictBO.DEFINE_MODEL, isAll,Global.loginYear);
					lstData = lstUnUseDefTable;
				}
				treDefTable.setData(lstData);
			}
			{// 第二页所库中表的情况
				if (isAll) {
					if (lstAllTable == null)
						lstAllTable = dataDictBO.getTableInfo(
								IDataDictBO.TABLE_MODEL, isAll,Global.loginYear);
					lstData = lstAllTable;

				} else {
					if (lstUnUseTable == null)
						lstUnUseTable = dataDictBO.getTableInfo(
								IDataDictBO.TABLE_MODEL, isAll,Global.loginYear);
					lstData = lstUnUseTable;
				}
				treTable.setData(lstData);
			}

			treTable.updateUI();
			treTable.expendedNode(treTable.getRoot());
			treDefTable.updateUI();
			treDefTable.expendedNode(treDefTable.getRoot());
			page = tpnlCenter.getSelectedIndex();
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}

	public static int getPage() {
		return page;
	}

	public static String getTabProp() {
		return tabProp;
	}

	public static void clearSelect() {
		arrTableSelect = null;
	}
}
