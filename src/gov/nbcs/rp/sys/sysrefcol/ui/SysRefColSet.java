package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * 引用列编辑界面
 * 

 * 
 */
public class SysRefColSet extends FDialog {

	private static final long serialVersionUID = 1L;

	private FPanel fpnlRefColArray[]; // 三个页面数组

	private FPanel fpnlRefCol;// 存放三个页面的FPanel

	private int iRefColFlag = 0; // 标志当前显示第几个页面

	private int iOperationType;// 存放操作类型

	private FButton fbtnTest;

	private FButton fbtnPrev;

	private FButton fbtnNext;

	private FButton fbtnDown;

	private FButton fbtnCancel;

	private DataSet dsRefCol;

	private DataSet dsRefColDetail;

	private DataSet dsRefColRelatable;

	private final ISysRefCol sysRefColServ = SysRefColI
			.getMethod();

	public SysRefColSet(DataSet dsRefCol) throws Exception {
		super(Global.mainFrame);
		this.setSize(800, 600);
		this.setResizable(false);
		this.dispose();
		this.setModal(true);
		this.setTitle("引用列设置向导");

		this.dsRefCol = dsRefCol;
		init(); // 初始化界面
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				try {
					cacelOperate();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SysRefColSet.this,
							"关闭引用列编辑界面发生错误，错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	// 初始化界面
	private void init() {
		// 创建fpnlPanel放到FDialog
		FPanel fpnlPanel = new FPanel();
		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setRowHeight(25);
		fpnlPanel.setLayout(rLay);
		this.getContentPane().add(fpnlPanel);
		// 设置上半部分Panel(不包括按钮Panel),存放不同的Panel
		fpnlRefCol = new FPanel();
		fpnlRefCol.setLayout(new RowPreferedLayout(1));
		// 创建三个页面
		fpnlRefColArray = new FPanel[3];
		fpnlRefColArray[0] = new SysRefColOnePanel();
		fpnlRefColArray[0].setTitle("第一步：引用列主信息设置");
		fpnlRefColArray[1] = new SysRefColTwoPanel();
		fpnlRefColArray[1].setTitle("第二步：引用列列内容设置");
		fpnlRefColArray[2] = new SysRefColThreePanel();
		fpnlRefColArray[2].setTitle("第三步：引用列列属性设置");

		fpnlRefCol.addControl(fpnlRefColArray[iRefColFlag],
				new TableConstraints(1, 1, true, true));
		// 按钮Panel
		FPanel fpnlBtnButtom = new FPanelBtnButtom();
		// 设置按钮状态
		setButtonState();
		fpnlPanel
				.addControl(fpnlRefCol, new TableConstraints(1, 1, true, true));
		fpnlPanel.addControl(fpnlBtnButtom, new TableConstraints(1, 1, false,
				true));
	}

	/**
	 * 按钮面板(放上一步、下一步、完成等按钮)
	 * 
	 * @author qzc
	 * 
	 */
	private class FPanelBtnButtom extends FPanel {

		private static final long serialVersionUID = 1L;

		public FPanelBtnButtom() {
			this.setLeftInset(10);
			this.setRightInset(10);
			// 设置fpnlBtnButtom的布局
			RowPreferedLayout rLayout = new RowPreferedLayout(7);
			rLayout.setColumnGap(2);
			rLayout.setColumnWidth(82);
			this.setLayout(rLayout);
			fbtnTest = new FButton("fbtnTest", "测试内容");
			fbtnTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					InfoPackage infoPackage = null;
					try {
						infoPackage = ((SysRefColOnePanel) fpnlRefColArray[0])
								.testSql(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(SysRefColSet.this,
								infoPackage.getsMessage(), "提示",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					} else {
						JOptionPane.showMessageDialog(SysRefColSet.this,
								"测试通过！", "提示", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			fbtnPrev = new FButton("fbtnPrev", "上一步");
			fbtnPrev.addActionListener(new AddActionListenerpre());
			fbtnNext = new FButton("fbtnNext", "下一步");
			fbtnNext.addActionListener(new AddActionListenernext());
			fbtnDown = new FButton("fbtnDown", "完 成");
			fbtnDown.addActionListener(new DownActionListener());
			fbtnCancel = new FButton("fbtnCancel", "取 消");
			fbtnCancel.addActionListener(new CancelActionListener());
			FLabel flblEmpty1 = new FLabel();
			FLabel flblEmpty2 = new FLabel();

			// 测试内容FButton
			this.addControl(fbtnTest, new TableConstraints(1, 1, false, false));
			// 空Label
			this
					.addControl(flblEmpty1, new TableConstraints(1, 1, false,
							true));
			// 上一步
			this.addControl(fbtnPrev, new TableConstraints(1, 1, false, false));
			// 下一步
			this.addControl(fbtnNext, new TableConstraints(1, 1, false, false));
			// 完成
			this.addControl(fbtnDown, new TableConstraints(1, 1, false, false));
			// 空Label
			this.addControl(flblEmpty2,
					new TableConstraints(1, 1, false, false));
			// 取消
			this.addControl(fbtnCancel,
					new TableConstraints(1, 1, false, false));
		}
	}

	/**
	 * 下一步按钮“按钮事件
	 */
	private class AddActionListenernext implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if (!checkFillInfo())
					return;
				setupOperate();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColSet.this,
						"下一步操作发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			iRefColFlag = iRefColFlag + 1;
			refreshShowPanel();
		}
	}

	/**
	 * 上一步按钮“按钮事件
	 */
	private class AddActionListenerpre implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			iRefColFlag = iRefColFlag - 1;
			refreshShowPanel();
		}
	}

	private void refreshShowPanel() {
		fpnlRefCol.removeAll();
		setButtonState();
		fpnlRefCol.setLayout(new RowPreferedLayout(1));
		fpnlRefCol.addControl(fpnlRefColArray[iRefColFlag],
				new TableConstraints(1, 1, true, true));
		fpnlRefCol.updateUI();

	}

	/**
	 * 设置下排按钮的状态
	 * 
	 */
	private void setButtonState() {
		if (iRefColFlag == 0) {
			fbtnTest.setVisible(true);
			fbtnPrev.setEnabled(false);
			fbtnNext.setEnabled(true);
			fbtnDown.setVisible(false);
		} else if (iRefColFlag > 0 && iRefColFlag < fpnlRefColArray.length - 1) {
			fbtnTest.setVisible(false);
			fbtnPrev.setEnabled(true);
			fbtnNext.setEnabled(true);
			fbtnDown.setVisible(false);
		}
		if (iRefColFlag == fpnlRefColArray.length - 1) {
			fbtnTest.setVisible(false);
			fbtnPrev.setEnabled(true);
			fbtnNext.setEnabled(false);
			if (SysRefColOperation.rcoView == iOperationType)
				fbtnDown.setVisible(false);
			else
				fbtnDown.setVisible(true);
		}
	}

	/**
	 * 判断当前界面填写的信息是否完整，正确
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkFillInfo() throws Exception {
		InfoPackage infoPackage;
		switch (iRefColFlag) {
		case 0:
			infoPackage = ((SysRefColOnePanel) fpnlRefColArray[0])
					.nextOperate();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(this, infoPackage.getsMessage(),
						"提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			break;
		case 1:
			infoPackage = ((SysRefColTwoPanel) fpnlRefColArray[1])
					.nextOperate();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(this, infoPackage.getsMessage(),
						"提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			break;
		case 2:
			break;
		}
		return true;
	}

	/**
	 * 点击上一步、下一步每步不同的操作
	 * 
	 * @throws Exception
	 * 
	 */
	private void setupOperate() throws Exception {
		switch (iRefColFlag) {
		case 0:
			// 将第一步填写的sql信息传给第二步
			((SysRefColTwoPanel) fpnlRefColArray[1])
					.setDsQuery(((SysRefColOnePanel) fpnlRefColArray[0])
							.getDsDetSql());
			((SysRefColTwoPanel) fpnlRefColArray[1]).showInfo(dsRefCol,
					dsRefColDetail, iOperationType);
			break;
		case 1:
			// 将表头List信息传给第三步
			((SysRefColThreePanel) fpnlRefColArray[2])
					.setTableColumnInfo(((SysRefColTwoPanel) fpnlRefColArray[1])
							.getTableColumnInfo());
			((SysRefColThreePanel) fpnlRefColArray[2]).showInfo(dsRefCol,
					dsRefColDetail, dsRefColRelatable, iOperationType);
			break;
		case 2:
			break;
		}
	}

	/**
	 * 根据不同的编辑类型，设置控件值和控件状态（可编辑或不可编辑)
	 * 
	 * @throws Exception
	 * 
	 */
	public void initInfo(int iOperationType) throws Exception {
		this.iOperationType = iOperationType;
		if (iOperationType == SysRefColOperation.rcoAdd)
			addRefCol();
		else if (iOperationType == SysRefColOperation.rcoModify)
			dsRefCol.edit();

		String sRefColId = dsRefCol.fieldByName("refcol_id").getString();
		dsRefColDetail = sysRefColServ.getRefColDetailWithRefColId(sRefColId,
				Global.loginYear);
		dsRefColRelatable = sysRefColServ.getRefColRelatable(sRefColId,
				Global.loginYear);
		((SysRefColOnePanel) fpnlRefColArray[0]).showInfo(dsRefCol,
				iOperationType);
		iRefColFlag = 0;
		refreshShowPanel();
	}

	/**
	 * 增加引用列
	 * 
	 * @throws Exception
	 */
	private void addRefCol() throws Exception {
		String sRefColId = PubInterfaceStub.getMethod()
				.getMaxCode("fb_s_RefCol", "REFCOL_ID",
						"set_Year = " + Global.loginYear, 4);
		dsRefCol.append();
		dsRefCol.fieldByName("Refcol_Id").setValue(sRefColId);
		dsRefCol.fieldByName("RefCol_Kind").setValue(new Integer(0));
	}

	private class DownActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				InfoPackage infoPackage = ((SysRefColThreePanel) fpnlRefColArray[2])
						.nextOperate();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(SysRefColSet.this,
							infoPackage.getsMessage(), "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (SysRefColOperation.rcoAdd == iOperationType) {
					DataSet ds = DataSetUtil.subDataSet(dsRefCol, DataSet.FOR_APPEND);
					ds.edit();
					sysRefColServ.saveRefCol(ds, dsRefColDetail,
							dsRefColRelatable);
				} else if (SysRefColOperation.rcoModify == iOperationType) {
					DataSet ds = DataSetUtil.subDataSet(dsRefCol, DataSet.FOR_UPDATE);
					ds.edit();
					sysRefColServ.saveRefCol(ds, dsRefColDetail,
							dsRefColRelatable);
				}
				dsRefCol.applyUpdate();
				SysRefColSet.this.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColSet.this,
						"引用列保存发生错误，错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private class CancelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				cacelOperate();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColSet.this,
						"取消操作发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void cacelOperate() throws Exception {
		dsRefCol.cancel();
		SysRefColSet.this.setVisible(false);
	}
}
