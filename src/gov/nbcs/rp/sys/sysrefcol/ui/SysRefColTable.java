package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class SysRefColTable extends FDialog {

	private static final long serialVersionUID = 1L;

	public SysRefColTable(String[] sColumnText, String[] sColumnField,
			int[] iColWidth, DataSet dataSet) throws Exception {
		super(Global.mainFrame);
		this.setSize(500, 600);
		this.setResizable(false);
		this.dispose();
		this.setModal(true);

		FPanel mainPanel = new FPanel();
		mainPanel.setTopInset(10);
		mainPanel.setLeftInset(10);
		mainPanel.setRightInset(10);
		mainPanel.setLayout(new RowPreferedLayout(1));

		CustomTable customTable = new CustomTable(sColumnText, sColumnField,
				dataSet, false);
		customTable.reset();
		customTable.getTable().getTableHeader().setBackground(
				UntPub.HEADER_COLOR);
		customTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < iColWidth.length; i++) {
			customTable.getTable().getColumnModel().getColumn(i + 1)
					.setPreferredWidth(iColWidth[i]);
		}
		mainPanel.addControl(customTable,
				new TableConstraints(1, 1, true, true));

		// "确定"、"取消"按钮
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		// 确定按钮
		FButton okButton = new FButton();
		okButton.setText("确定");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		FButton cancelButton = new FButton();
		cancelButton.setText("取消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		choosePanel.addControl(okButton);
		choosePanel.addControl(cancelButton);

		this.getContentPane().setLayout(new RowPreferedLayout(1));
		this.getContentPane().add(mainPanel,
				new TableConstraints(1, 1, true, true));
		this.getContentPane().add(choosePanel,
				new TableConstraints(2, 1, false, true));
	}
}
