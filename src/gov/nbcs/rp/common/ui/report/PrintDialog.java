/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.util.Global;

public class PrintDialog extends FDialog{
	
	private static final long serialVersionUID = 1L;

		public PrintDialog( Report report ){
		super(Global.mainFrame);
		try{
			FPanel pnlBase = new FPanel();
			this.setSize(600, 600);          // 设置窗体大小
			this.setResizable(false);        // 设置窗体大小是否可变
			this.getContentPane().add(pnlBase);
			this.dispose();                  // 窗体组件自动充满。
			this.setTitle("打印设置界面");  // 设置窗体标题
			this.setModal(true);   
		}catch( Exception ee ){
			ee.printStackTrace();
		}
	}
}
