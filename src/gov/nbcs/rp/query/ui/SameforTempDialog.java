package gov.nbcs.rp.query.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;




public class SameforTempDialog extends FDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isOK = false;
	private FComboBox dataType = null;
	private String returnVal = null;



	public SameforTempDialog()
	{
		super(Global.mainFrame);
		getContentPane().add(getBasePanel());
		String xmflTmp = "1_0#һ���걨��+1_1#һ�������+2_0#�����걨��+2_1#���������";
		dataType.setRefModel(xmflTmp);
		dataType.setValue("");
	}


	private FPanel getBasePanel()
	{
		FButton btnOK = new FButton("", "ȷ��");
		btnOK.setIcon("images/rp/save.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);

		FButton btnCancel = new FButton("", "ȡ��");
		btnCancel.setIcon("images/rp/close.gif");
		btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);

		FToolBar toolbar = new FToolBar();
		FPanel pnlTop = new FPanel();

		toolbar.addControl(btnOK);
		toolbar.addControl(btnCancel);

		dataType = new FComboBox("����");

		RowPreferedLayout layTop = new RowPreferedLayout(1);
		pnlTop.setLayout(layTop);
		pnlTop.addControl(toolbar, new TableConstraints(1, 1, true, true));
		pnlTop.addControl(dataType, new TableConstraints(1, 1, false, true));

		btnOK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					returnVal = dataType.getValue().toString();
					if (StringUtils.isEmpty(returnVal))
						JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�����Σ�");
					isOK = true;
					setVisible(false);
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ���������ͣ�");
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
		this.setSize(300, 120);
		pnlTop.setLeftInset(10);
		pnlTop.setRightInset(10);
		pnlTop.setBottomInset(10);
		this.getContentPane().add(pnlTop);
		this.dispose();
		this.setTitle("ͬ������ѡ��");
		this.setModal(true);
		return pnlTop;
	}


	public String getReturnVal()
	{
		return returnVal;
	}

}
