package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class SysRefColTree extends FDialog {

	private static final long serialVersionUID = 1L;

	DataSet ds;

	String idName;

	String textName;

	CustomTree customTree;

	FTextField ftxtSearch;// 输出查询内容

	public SysRefColTree(String rootName, DataSet ds, String idName,
			String textName, SysCodeRule codeRule) throws Exception {
		super(Global.mainFrame);
		this.setSize(500, 600);
		this.setResizable(false);
		this.dispose();
		this.setModal(true);

		this.ds = ds;
		this.idName = idName;
		this.textName = textName;

		// 主窗体上放入panel，作为主布局panel
		FPanel mainPanel = new FPanel();
		RowPreferedLayout mainLayout = new RowPreferedLayout(1);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLeftInset(10);
		mainPanel.setRightInset(10);
		mainPanel.setTopInset(10);

		// 请输入查找内容
		ftxtSearch = new FTextField();
		ftxtSearch.setTitle("请输入查找内容:");
		ftxtSearch.setProportion(0.25f);

		// 两个查找按钮
		FFlowLayoutPanel findPanel = new FFlowLayoutPanel();
		findPanel.setAlignment(FlowLayout.CENTER);
		// 查找按钮
		FButton findButton = new FButton();
		findButton.setText("查找");
		findButton.addActionListener(new SearchInfo(true));

		FButton findNextButton = new FButton();
		findNextButton.setText("查找下一个");
		findPanel.addControl(findButton);
		findPanel.addControl(findNextButton);
		findNextButton.addActionListener(new SearchInfo(false));

		// 树
		FScrollPane treePanel = new FScrollPane();
		customTree = new CustomTree(rootName, ds, idName, textName, codeRule);
		treePanel.addControl(customTree);

		// "确定"、"取消"按钮
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		// 确定按钮
		FButton okButton = new FButton();
		okButton.setText("确定");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);

			}
		});
		// 取消按钮
		FButton cancelButton = new FButton();
		cancelButton.setText("取消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		choosePanel.addControl(okButton);
		choosePanel.addControl(cancelButton);

		// 把元素都加入到mainPanel中
		mainPanel.addControl(ftxtSearch,
				new TableConstraints(1, 1, false, true));
		mainPanel
				.addControl(findPanel, new TableConstraints(2, 1, false, true));
		mainPanel.add(treePanel, new TableConstraints(8, 1, true, true));
		mainPanel.addControl(choosePanel, new TableConstraints(2, 1, false,
				true));
		this.getContentPane().add(mainPanel);
	}

	private String searchId(String sName, boolean bBeforeFirst)
			throws Exception {
		if (sName == null)
			return null;
		if (bBeforeFirst)
			ds.beforeFirst();

		String sBookmark = ds.toogleBookmark();
		while (ds.next()) {
			if (ds.fieldByName(textName).getString().indexOf(sName) >= 0) {
				return ds.fieldByName(idName).getString();
			}
		}
		ds.gotoBookmark(sBookmark);
		return null;
	}

	private class SearchInfo implements ActionListener {
		boolean bBeforeFirst;

		public SearchInfo(boolean bBeforeFirst) {
			super();
			this.bBeforeFirst = bBeforeFirst;
		}

		public void actionPerformed(ActionEvent arg0) {
			String sValue = ftxtSearch.getValue().toString();
			try {
				String sId = searchId(sValue, bBeforeFirst);
				if (sId == null) {
					if (bBeforeFirst)
						JOptionPane.showMessageDialog(customTree, "未找到！", "提示",
								JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(customTree, "搜索完毕！",
								"提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				MyTreeNode myTreeNode = customTree.getNodeById(sId);
				TreePath path = new TreePath(myTreeNode.getPath());
				customTree.getSelectionModel().setSelectionPath(path);
				customTree.scrollPathToVisible(path);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColTree.this,
						"查找发生错误,错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}
}
