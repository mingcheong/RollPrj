/**
 * @# FilterDlg.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;


import gov.nbcs.rp.common.ui.dialog.RpDialog;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.FilterFactory;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.GeneralFilter;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.IFilterProvider;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

/**
 * 功能说明:条件设置的对话框
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class FilterDlg extends  RpDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	List lstFilter;

	GeneralFilter generalFilter;

	public FilterDlg() {
		super(Global.mainFrame, true);
	}

	protected void bindListeners() {
	}

	protected boolean confirmClose() {
		return true;
	}

	protected void initComponents() {
		this.setTitle("查询条件设置");
		this.setSize(980, 500);
		lstFilter = new ArrayList();

		FPanel pnlBack = new FPanel();
		pnlBack.setLayout(new FlowLayout(FlowLayout.LEFT));
		IFilterProvider aFilter = FilterFactory
				.createFilter(IFilterProvider.TYPE_GENERAL);
		
		generalFilter = (GeneralFilter) aFilter;
		lstFilter.add(aFilter);
		pnlBack.add(aFilter.getFilterPanel());
		
		

		aFilter = FilterFactory.createFilter(IFilterProvider.TYPE_ACCT);
		lstFilter.add(aFilter);
		pnlBack.add(aFilter.getFilterPanel());

		aFilter = FilterFactory.createFilter(IFilterProvider.TYPE_ACCT_JJ);
		lstFilter.add(aFilter);
		pnlBack.add(aFilter.getFilterPanel());

		aFilter = FilterFactory.createFilter(IFilterProvider.TYPE_PROJECT);
		lstFilter.add(aFilter);
		pnlBack.add(aFilter.getFilterPanel());

		aFilter = FilterFactory.createFilter(IFilterProvider.TYPE_ACCT_TYPE);
		lstFilter.add(aFilter);
		pnlBack.add(aFilter.getFilterPanel());


		

		FScrollPane spnlBack = new FScrollPane(pnlBack);
		this.getBodyPane().add(spnlBack);
		//添加按钮
		this.getToolbar().removeAll();
		FButton btnOK = new FButton("btnOK", "确定");
		btnOK.setIcon("images/fbudget/check.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.getToolbar().addControl(btnOK);
		btnOK.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (confirmClose())
					FilterDlg.this.setVisible(false);
			}
		});

		FButton btnClear = new FButton("btnClear", "清除条件");
		btnClear.setIcon("images/fbudget/cancl.gif");
		btnClear.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.getToolbar().addControl(btnClear);
		btnClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int iCount = lstFilter.size();
				for (int i = 0; i < iCount; i++) {
					((IFilterProvider) lstFilter.get(i)).reset();
				}
			}
		});

	}

	public String getFilter(String align) {
		StringBuffer sb = new StringBuffer();
		int iCount = lstFilter.size();
		for (int i = 0; i < iCount; i++) {
			IFilterProvider aFilter = ((IFilterProvider) lstFilter.get(i));
			if (aFilter.isSelect())
				sb.append(aFilter.getFilter(align)).append(" and ");
		}
		if (sb.length() > 4)
			return sb.substring(0, sb.length() - 4);
		return "";
	}

	public void show() {
		Tools.centerWindow(this);
		super.show();
	}

	public int getBatchNo() {
		return generalFilter.getBatchNo();
	}

	public int getDataType() {
		return generalFilter.getDataType();
	}

	public boolean isCleanZeroColumn() {
		return generalFilter.isCleanZeroColumn();
	}

	public boolean isShowAllInfo() {
		return generalFilter.isShowAllInfo();
	}

	public int getTopCount() {
		return generalFilter.getTopCount();
	}
}
