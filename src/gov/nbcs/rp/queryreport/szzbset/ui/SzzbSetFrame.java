/**
 * @# SzzbSetFrame.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.MessageBox;

/**
 * ����˵��:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * ��Ȩ���У��㽭����
 * <P>
 * δ������˾��ɣ��������κη�ʽ���ƻ�ʹ�ñ������κβ��֣�
 * <P>
 * ��Ȩ�߽��ܵ�����׷����
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-4-3
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class SzzbSetFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1897694965981192339L;

	private SzzbSet szzbSet;

	private String reportID;

	private String setYear;

	private DefineReport defineReport;

	public SzzbSetFrame(String sReportID, String setYear,
			DefineReport defineReport) {
		super("��֧�ܱ����");
		this.reportID = sReportID;
		this.setYear = setYear;
		this.defineReport = defineReport;
		initUI();

	}

	private void initUI() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit
				.getDefaultToolkit().getScreenSize().height - 30);
		szzbSet = new SzzbSet(this, reportID, setYear, defineReport);

		FPanel pnlButton = new FPanel();
		pnlButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		FButton btnSave = new FButton("SAVE", "����");
		btnSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				szzbSet.saveInfos();

			}

		});
		FButton btnClose = new FButton("CLOSE", "�ر�");
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (canClose()) {
					szzbSet.setChanged(false);
					SzzbSetFrame.this.dispose();
				}

			}

		});
		pnlButton.add(btnSave);
		pnlButton.add(new JLabel());
		pnlButton.add(btnClose);
		FPanel pnlBack = new FPanel();
		pnlBack.setLayout(new BorderLayout());
		pnlBack.add(szzbSet, BorderLayout.CENTER);

		pnlBack.add(pnlButton, BorderLayout.SOUTH);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.getContentPane().add(pnlBack);
	}

	private boolean canClose() {
		if (szzbSet.isChanged()) {
			MessageBox mb = new MessageBox(this, "������û�б���ȷ��Ҫ�˳���?",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK
							| MessageBox.BUTTON_CANCEL);
			mb.show();
			if (mb.result == MessageBox.OK)
				return true;
			return false;
		}
		return true;
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			if (canClose())
				super.processWindowEvent(e);
		} else {
			// ���������¼�������JFrame����
			super.processWindowEvent(e);
		}
	}
}
