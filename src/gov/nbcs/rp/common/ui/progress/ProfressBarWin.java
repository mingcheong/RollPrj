/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.progress;

import com.foundercy.pf.util.Global;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;


public class ProfressBarWin extends JWindow
{
	private static final long serialVersionUID = 1L;

	JProgressBar bar = new JProgressBar(0,100);
	JLabel lab = new JLabel("");
	public ProfressBarWin(int max)
	{
		super(Global.mainFrame);
		bar.setMaximum(max);
		init();
	}
	private void init() {
		bar.setValue(0);
		
		JPanel p = new JPanel(new BorderLayout()); 
		p.setBorder(javax.swing.BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		p.add(lab,BorderLayout.NORTH);
		p.add(bar,BorderLayout.CENTER);
		
		this.setContentPane(p);
		this.pack();
		this.setSize(500,50);
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-250,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-50);
		this.show(); 
	}
	public void setValue(int value)
	{
		bar.setValue(value);
	}
	public int getValue()
	{
		return bar.getValue();
	}
	public void setText(String txt)
	{
		lab.setText(txt);
	}
}