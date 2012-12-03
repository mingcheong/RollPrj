package gov.nbcs.rp.generateBudget.ui;

import gov.nbcs.rp.generateBudget.action.GeBudgetAction;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class GeQrChoose extends FDialog {
	private FComboBox Choose = null;
	private FComboBox Choose2 = null;
	String prjCodes = "";
	String xmmcs = "";
	List lBudgetData;
	private static final long serialVersionUID = 1L;

	public GeQrChoose(List lBudgetData) {
		super(Global.mainFrame);
		setSize(400, 300);
		setResizable(false);
		setTitle("生成预算");
		getContentPane().add(getBasePanel());
		dispose();
		setModal(true);
		this.lBudgetData = lBudgetData;
	}

	private FPanel getBasePanel() {
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnl.setLayout(lay);

		String ChooseBh = "1#一上+2#二上";
		String ChooseBh1 = "0#原始数+1#财政审核数";
		this.Choose = new FComboBox("生成预算批次：");
		Choose.setProportion(0.5f);
		this.Choose2 = new FComboBox("生成预算数据类型：");
		Choose2.setProportion(0.5f);
		this.Choose.setRefModel(ChooseBh);
		this.Choose2.setRefModel(ChooseBh1);
		pnl.addControl(this.Choose, new TableConstraints(1, 1, false, true));
		pnl.addControl(this.Choose2, new TableConstraints(1, 1, false, true));
		FPanel buttonPanel = new FPanel();
		FButton okButton = new FButton("okButton", "确 认");
		okButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					// int typeCode = 1;
					try {
						GeBudgetAction.getMethod().insertTempBudget(
								Global.loginYear, Choose.getValue().toString(),
								Choose2.getValue().toString(), lBudgetData);
						JOptionPane.showMessageDialog(Global.mainFrame,
								"生成预算成功！");

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				GeQrChoose.this.dispose();
			}
		});
		FButton cancelButton = new FButton("cencelButton", "取 消");
		cancelButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				GeQrChoose.this.dispose();
			}
		});
		buttonPanel.addControl(okButton);
		buttonPanel.addControl(cancelButton);
		pnl.addControl(buttonPanel);
		pnl.setLeftInset(40);
		pnl.setTopInset(40);
		pnl.setRightInset(100);
		getContentPane().add(pnl);

		return pnl;
	}
}