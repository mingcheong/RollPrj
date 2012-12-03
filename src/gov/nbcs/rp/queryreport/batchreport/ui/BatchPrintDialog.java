/**
 * Copyright zjyq 版权所有
 * 
 * 部门预算编审系统
 * 
 * @title 
 * 
 * @author qzcun
 * 
 * @version 1.0
 */
package gov.nbcs.rp.queryreport.batchreport.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import gov.nbcs.rp.common.ui.XmlControlContainerTemplate;
import gov.nbcs.rp.common.ui.dialog.RpDialog;
import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.UIControlFactory;
import com.foundercy.pf.control.UITools;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * The Class BatchPrintDialog.
 * 
 * @author Administrator
 */
public class BatchPrintDialog extends RpDialog implements
		XmlControlContainerTemplate {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private FTable tblBody = null;

	private FButton btnPrint = null;

	public BatchPrintDialog() {
		super(Global.mainFrame, true);
		this.setSize(900, 600);
		initize();
	}

	private void initize() {
		try {
			UIControlFactory factory = new UIControlFactory(
					"gov/nbcs/rp/queryreport/batchreport/ui/xml/BatchPrintDialog.xml");
			getBodyPane().add((FPanel) factory.createControl());
			assignCtrlVar();
			btnPrint = new FButton("btn_print", "打印");
			btnPrint.setIcon("rp/images/print.gif");
			btnPrint.setVerticalTextPosition(SwingConstants.BOTTOM);
			getToolbar().addControl(btnPrint);

			bindCtrlEvent();
		} catch (ControlException e) {
			e.printStackTrace();
		}
	}

	public void assignCtrlVar() {
		tblBody = (FTable) UITools.getFirstControlById(this.getBodyPane(),
				"tbl_body");
	}

	public void bindCtrlEvent() {
		btnPrint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MessageBox msg = new MessageBox(BatchPrintDialog.this,
						"确定打印选中行的对应表单吗？", MessageBox.INFOMATION,
						MessageBox.BUTTON_OK | MessageBox.BUTTON_CANCEL);
				msg.setVisible(true);
				if (msg.getResult() == MessageBox.OK) {
					System.out.println("printing...");
				}
			}
		});
	}

	protected boolean confirmClose() {
		return true;
	} 

	public XMLData getCtrlValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetCtrlValue() {
		// TODO Auto-generated method stub
		
	}

	public void setCtrlValue(XMLData xmlData) {
		// TODO Auto-generated method stub
		
	}

}
