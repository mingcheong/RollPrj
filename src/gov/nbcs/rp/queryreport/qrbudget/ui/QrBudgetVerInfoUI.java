package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTimeChooser;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

public class QrBudgetVerInfoUI extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	CustomTree ftreVerInfo;

	FTextField ftxtVerNo;

	FTextArea ftxtVerRemark;

	FTimeChooser ftVerDate;

	String EditorType = "";// ���ӣ��޸ģ��鿴��־

	QrBudgetVerInfoUI mainUI = this;

	public void initize() {
		try {
			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// �������
			DataSet dsVer = QrBudgetI.getMethod().getVerInfo();
			ftreVerInfo = new CustomTree("����", dsVer, IQrBudget.VER_NO,
					IQrBudget.FULLNAME, "parent_id", null, IQrBudget.VER_NO);
			ftreVerInfo.getDataSet().addDataChangeListener(new verinfoChange());
			fSplitPane.addControl(ftreVerInfo);

			// ���ұ���ϸ��Ϣ
			FPanel rightPanel = new FPanel();
			fSplitPane.addControl(rightPanel);
			rightPanel.setLeftInset(10);
			rightPanel.setTopInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);
			RowPreferedLayout rLayout = new RowPreferedLayout(1);
			rLayout.setColumnWidth(400);
			rightPanel.setLayout(rLayout);
			{
				ftxtVerNo = new FTextField("��ѯ�汾�ţ�");
				ftxtVerNo.setProportion(0.3f);
				ftxtVerNo.getEditor().addKeyListener(new ShieldInput());

				ftVerDate = new FTimeChooser("�汾���ڣ�");

				ftxtVerRemark = new FTextArea("�汾˵����");
				ftxtVerRemark.setProportion(0.3f);

				setEnabled(false);
				rightPanel.addControl(ftxtVerNo, new TableConstraints(1, 1,
						false, false));
				rightPanel.addControl(ftVerDate, new TableConstraints(1, 1,
						false, false));
				rightPanel.addControl(ftxtVerRemark, new TableConstraints(3, 1,
						false, false));
			}
			this.createToolBar();
			List controls = this.getToolbarPanel().getSubControls();
			for (int i = 0; i < controls.size(); i++) {
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("�޸�".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("׼������".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
			this.setVisible(true);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ѯ�汾�������󣬴�����Ϣ:"
					+ ex.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);

		}

	}

	public void setEnabled(boolean flag) {
		ftxtVerNo.setEditable(flag);
		ftxtVerRemark.setEditable(flag);
		ftVerDate.setEnabled(flag);
		ftVerDate.setEditable(flag);
	}

	public class ShieldInput extends KeyAdapter {
		public void keyTyped(KeyEvent e) {
			char c = e.getKeyChar();
			if (c >= '0' && c <= '9') {
				return;
			}
			if (c == '.') {
			}
			e.setKeyChar('\0');
		}

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_BACK_SPACE:
				return;
			case KeyEvent.VK_DELETE:
				return;
			case KeyEvent.VK_LEFT:
				break;
			case KeyEvent.VK_RIGHT:
				break;
			case KeyEvent.VK_ENTER:
				break;
			}
			e.setKeyCode(0);
		}

	}

	public void initEditer() {
		this.ftxtVerNo.setValue("");
		this.ftVerDate.setValue("");
		this.ftxtVerRemark.setValue("");
	}

	public void doAdd() {
		ftreVerInfo.setEnabled(false);
		initEditer();
		setEnabled(true);
		EditorType = IQrBudget.ADD;
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("�޸�".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("׼������".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
		}

	}

	public void doDelete() {
		try {
			MyTreeNode node = ftreVerInfo.getSelectedNode();
			MyTreeNode root = (MyTreeNode) ftreVerInfo.getRoot();
			if (node != null && node != root) {
				if (JOptionPane
						.showConfirmDialog(this, "���Ƿ�ȷ��Ҫɾ���ð汾?", "��ʾ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
					return;
				DataSet dsVer = ftreVerInfo.getDataSet();
				dsVer.gotoBookmark(node.getBookmark());
				String verNo = dsVer.fieldByName("ver_no").getString();
				InfoPackage infopackage = new InfoPackage();
				infopackage = QrBudgetI.getMethod().delVerInfo(verNo);
				if (!infopackage.getSuccess()) {
					JOptionPane.showMessageDialog(this, infopackage
							.getsMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				JOptionPane.showMessageDialog(this, "��ѡ��Ҫɾ���İ汾��", "��ʾ",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			initEditer();
			// ˢ�°汾��
			refreshVerInfo();
			List controls = this.getToolbarPanel().getSubControls();
			for (int i = 0; i < controls.size(); i++) {
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("�޸�".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("׼������".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void refreshVerInfo() {
		try {
			// ˢ�°汾��
			DataSet dsVer = QrBudgetI.getMethod().getVerInfo();
			ftreVerInfo.setDataSet(dsVer);
			ftreVerInfo.reset();
			ftreVerInfo.repaint();
			ftreVerInfo.getDataSet().addDataChangeListener(new verinfoChange());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private class verinfoChange implements DataChangeListener {

		private static final long serialVersionUID = 1L;

		public void onDataChange(DataChangeEvent event) throws Exception {
			initEditer();
			MyTreeNode node = ftreVerInfo.getSelectedNode();
			MyTreeNode root = (MyTreeNode) ftreVerInfo.getRoot();
			if (node != null && node != root) {
				DataSet dsVer = ftreVerInfo.getDataSet();
				dsVer.gotoBookmark(node.getBookmark());
				ftxtVerNo.setValue(dsVer.fieldByName("ver_no").getString());
				ftVerDate.setValue(dsVer.fieldByName("ver_date").getString());
				ftxtVerRemark.setValue(dsVer.fieldByName("ver_remark")
						.getString());
				List controls = mainUI.getToolbarPanel().getSubControls();
				for (int i = 0; i < controls.size(); i++) {
					if ("����".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(true);
					if ("�޸�".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(true);
					if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(true);
					if ("����".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(false);
					if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(false);
					if ("׼������".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(true);

				}

			}
		}

	}

	public void doCancel() {
		this.ftreVerInfo.setEnabled(true);
		if (this.EditorType.equals(IQrBudget.ADD))
			initEditer();
		setEnabled(false);
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("�޸�".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("׼������".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
		}
		new verinfoChange();
	}

	public void doInsert() {
		// TODO Auto-generated method stub

	}

	public void doModify() {
		try {
			MyTreeNode node = ftreVerInfo.getSelectedNode();
			MyTreeNode root = (MyTreeNode) ftreVerInfo.getRoot();
			if (node != null && node != root) {
				ftreVerInfo.setEnabled(false);
				setEnabled(true);
				// �汾�Ų����޸�
				this.ftxtVerNo.setEditable(false);
				EditorType = IQrBudget.MODIFY;
				List controls = this.getToolbarPanel().getSubControls();
				for (int i = 0; i < controls.size(); i++) {
					if ("����".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(false);
					if ("�޸�".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(false);
					if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(false);
					if ("����".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(true);
					if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(true);
					if ("׼������".equals(((FButton) controls.get(i)).getText()))
						((FButton) controls.get(i)).setEnabled(false);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void doSave() {
		try {
			InfoPackage infopackage = new InfoPackage();
			infopackage = QrBudgetI.getMethod().saveVerInfo(
					ftxtVerNo.getValue().toString(),
					ftVerDate.getValue().toString(),
					ftxtVerRemark.getValue().toString(), EditorType);
			if (!infopackage.getSuccess()) {
				JOptionPane.showMessageDialog(this, infopackage.getsMessage(),
						"��ʾ", JOptionPane.ERROR_MESSAGE);
				return;
			}
			new verinfoChange();
			if (ftxtVerNo.getValue() != null)
				ftreVerInfo.expandTo(IQrBudget.VER_NO, ftxtVerNo.getValue()
						.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "���淢�����󣬴�����Ϣ��"
					+ ex.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			return;
		}
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("�޸�".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(true);
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("׼������".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
		}
		ftreVerInfo.setEnabled(true);
		this.refreshVerInfo();
	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();

	}

	public CustomTree getTreVerInfo() {
		return this.ftreVerInfo;
	}

	public QrBudgetVerInfoUI getMainUI() {
		return this.mainUI;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
