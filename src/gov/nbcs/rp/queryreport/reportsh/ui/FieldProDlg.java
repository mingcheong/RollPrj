/**
 * @# FieldProDlg.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import gov.nbcs.rp.common.Common;

import gov.nbcs.rp.common.ui.dialog.RpDialog;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

/**
 * ����˵��:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class FieldProDlg extends RpDialog {

	private static final long serialVersionUID = -8887008756845652306L;

	private FieldProPanel pnlPro;

	public FieldProDlg() {
		super(Global.mainFrame, true);
		this.setResizable(true);
	}

	protected void bindListeners() {
		// TODO Auto-generated method stub

	}

	protected boolean confirmClose() {

		return true;
	}

	protected void initComponents() {
		pnlPro = new FieldProPanel();
		// FScrollPane spnlBack = new FScrollPane(pnlPro);
		// this.getBodyPane().add(spnlBack);
		this.getBodyPane().add(pnlPro);

		this.getToolbar().removeAll();
		FButton btnOK = new FButton("btnOK", "ȷ��");
		btnOK.setIcon("images/fbudget/check.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.getToolbar().addControl(btnOK);
		btnOK.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (confirmClose())
					FieldProDlg.this.setVisible(false);
			}
		});

		FButton btnLeft = new FButton("btnLeft", "����");
		btnLeft.setIcon("images/fbudget/a5.gif");
		btnLeft.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.getToolbar().addControl(btnLeft);
		btnLeft.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pnlPro.moveLeft();
			}
		});

		FButton btnRight = new FButton("btnRight", "����");
		btnRight.setIcon("images/fbudget/a6.gif");
		btnRight.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.getToolbar().addControl(btnRight);
		btnRight.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pnlPro.moveRight();
			}
		});

		FButton btnCheck = new FButton("btnCheck", "���ü��");
		btnCheck.setIcon("images/fbudget/check.gif");
		btnCheck.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.getToolbar().addControl(btnCheck);
		btnCheck.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String err = pnlPro.check();
				if (!Common.isNullStr(err)) {
					MessageBox mb = new MessageBox(FieldProDlg.this, err,
							MessageBox.INFOMATION, MessageBox.BUTTON_OK);
					mb.setVisible(true);
					mb.dispose();
					return;
				}
				MessageBox mb = new MessageBox(FieldProDlg.this, "���ͨ��!",
						MessageBox.INFOMATION, MessageBox.BUTTON_OK);
				mb.setVisible(true);
				mb.dispose();
			}
		});

		FButton btnReset = new FButton("btnReset", "����");
		btnReset.setIcon("images/fbudget/cancl.gif");
		btnReset.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.getToolbar().addControl(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnlPro.reset();
			}
		});

		this.setSize(new Dimension(1000, 740));

		this.setTitle("��ѯ����");

	}

	public void show() {
		Tools.centerWindow(this);
		super.show();
	}

	public FieldProPanel getPnlPro() {
		return pnlPro;
	}

	public void setPnlPro(FieldProPanel pnlPro) {
		this.pnlPro = pnlPro;
	}

}
