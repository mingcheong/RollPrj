/**
 * @# PreviewDialog.java    <文件名>
 */
package gov.nbcs.rp.common.print;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import gov.nbcs.rp.common.ui.report.Report;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.util.Global;
import com.fr.base.GUIUtils;
import com.fr.cell.core.LayoutFactory;
import com.fr.view.PreviewPane;

/**
 * 功能说明:
 *<P> Copyright 
 * <P>All rights reserved.
 * <P>版权所有：浙江易桥
 * <P>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <P>侵权者将受到法律追究。
 * <P>DERIVED FROM:   NONE
 * <P>PURPOSE:    
 * <P>DESCRIPTION:  
 * <P>CALLED BY:   
 * <P>UPDATE:         
 * <P>DATE:       Jul 26, 2009
 * <P>HISTORY:        1.0
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class PreviewDialog extends FDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3332396145581449809L;

	Report report;
	String reportId;
	String setYear;
	String[] param_Child;
	
	private PreviewPane previewPane;

	public PreviewDialog(Report report, String reportId, String setYear,
			String[] param_Child) {
		super(Global.mainFrame, true);
		this.report = report;
		this.reportId = reportId;
		this.setYear = setYear;
		this.param_Child = param_Child;
		// super("打印预览");
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(LayoutFactory.createBorderLayout());
		previewPane = new PreviewPane();
		contentPane.add(previewPane, "Center");
		setDefaultCloseOperation(2);
		setSize(800, 600);
		this.setTitle("打印预览");
		GUIUtils.centerWindow(this);
		print1(previewPane);
	}

	public PreviewDialog(Report report, String reportId, String setYear,
			String[] param_Child, JDialog parent) {
		super(parent, true);
		// super("打印预览");
		this.report = report;
		this.reportId = reportId;
		this.setYear = setYear;
		this.param_Child = param_Child;
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(LayoutFactory.createBorderLayout());
		previewPane = new PreviewPane();
		contentPane.add(previewPane, "Center");
		setDefaultCloseOperation(2);
		setSize(800, 600);
		this.setTitle("打印预览");
		GUIUtils.centerWindow(this);
		print1(previewPane);
	}

	public void print(Report report) {
		previewPane.print(report);
	}

	public void print(List reportPageList) {
		previewPane.print(reportPageList);
	}

	public void print(Iterator reportPageIterator) {
		previewPane.print(reportPageIterator);
	}
	
	private void print1(PreviewPane previewFrame){
		Component[] comps =  previewFrame.getComponents();
		JButton btn = null;
		for (int i = 0 ; i < comps.length; i++){
			if (comps[i] instanceof JPanel){
				btn = getButton("打印(T)...",(JPanel)comps[i]);
				break;
			}
		}
		ActionListener[] al = btn.getActionListeners();
		for (int i = 0; i < al.length; i++) {
			btn.removeActionListener(al[i]);
		}
		MouseListener[] ml = btn.getMouseListeners();
		for (int i = 0 ;i < ml.length; i++){
			btn.removeMouseListener(ml[i]);
		}
		btn.setEnabled(true);
		btn.addMouseListener(new MouseAdapter(){
		    public void mouseClicked(MouseEvent e) {
				try {
					PrintUtility.print(report, reportId, setYear, false,
							param_Child);
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		});
	}
	
	private JButton getButton(String title, JPanel pnl) {
		int iCount = pnl.getComponentCount();
		for (int i = 0; i < iCount; i++) {
			if (pnl.getComponent(i) instanceof JButton) {
				if (((JButton) pnl.getComponent(i)).getText().equals(title))
					return (JButton) pnl.getComponent(i);

			}
			if (pnl.getComponent(i) instanceof JPanel) {
				JButton btn = getButton(title, (JPanel) pnl.getComponent(i));
				if (btn != null)
					return btn;
			}
		}
		return null;
	}


}
