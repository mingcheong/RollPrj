package gov.nbcs.rp.prjsync.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;




public class PrjSyncDialog extends FDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isOK = false;
	private List prjNameList = null;
	private FComboBox cbPrjName = null;
	private String returnVal = null;



	public PrjSyncDialog(List prjNameList)
	{
		super(Global.mainFrame);
		getContentPane().add(getBasePanel());
		this.prjNameList = prjNameList;
		if (this.prjNameList != null && !this.prjNameList.isEmpty())
		{
			String xmflTmp = "";
			for (int i = 0; i < prjNameList.size(); i++)
			{
				xmflTmp += prjNameList.get(i) + "#" + prjNameList.get(i) + "+";
			}
			if (xmflTmp.length() > 0)
			{
				xmflTmp = xmflTmp.substring(0, xmflTmp.length() - 1);
			}
			cbPrjName.setRefModel(xmflTmp);
			cbPrjName.setValue("");
		}
	}


	private FPanel getBasePanel()
	{
		FButton btnOK = new FButton("", "确定");
		btnOK.setIcon("images/rp/save.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);

		FButton btnCancel = new FButton("", "取消");
		btnCancel.setIcon("images/rp/close.gif");
		btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);

		FToolBar toolbar = new FToolBar();
		FPanel pnlTop = new FPanel();

		toolbar.addControl(btnOK);
		toolbar.addControl(btnCancel);

		cbPrjName = new FComboBox("项目名称");
		cbPrjName.setEditable(true);

		RowPreferedLayout layTop = new RowPreferedLayout(1);
		pnlTop.setLayout(layTop);
		pnlTop.addControl(toolbar, new TableConstraints(1, 1, true, true));
		pnlTop.addControl(cbPrjName, new TableConstraints(1, 1, false, true));

		btnOK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					returnVal = cbPrjName.getValue().toString();
					isOK = true;
					setVisible(false);
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "设置归口单位失败！");
				}
			}
		});
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					isOK = false;
					setVisible(false);
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, e1.getMessage());
				}
			}
		});
		this.setSize(400, 120);
		pnlTop.setLeftInset(10);
		pnlTop.setRightInset(10);
		pnlTop.setBottomInset(10);
		this.getContentPane().add(pnlTop);
		this.dispose();
		this.setTitle("相似项目合并选项");
		this.setModal(true);
		return pnlTop;
	}


	public String getReturnVal()
	{
		return returnVal;
	}

}
