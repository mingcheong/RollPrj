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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
 

public class DoubleProcessBarWin extends JWindow
{
	private static final long serialVersionUID = 1L;

	JProgressBar bar = new JProgressBar(0,100);
	JProgressBar subBar = new JProgressBar(0,100);
	JLabel lab = new JLabel("");
	public DoubleProcessBarWin(String title,int max,JFrame frame)
	{
		super(frame); 
		bar.setMaximum(max);
		init();
	}
	private void init() {
		bar.setValue(0);
		
		JPanel p = new JPanel(new BorderLayout()); 
//		p.setBackground(Color.DARK_GRAY);
		p.setBorder(javax.swing.BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		p.add(lab,BorderLayout.NORTH);
		JPanel barPane = new JPanel(new BorderLayout()); 
		p.add(barPane,BorderLayout.CENTER);
		bar.setPreferredSize(new Dimension(500,30));
		subBar.setPreferredSize(new Dimension(500,30));
		barPane.add(bar,BorderLayout.NORTH);
		barPane.add(subBar,BorderLayout.SOUTH);
		
		this.setContentPane(p);
		this.pack();
		this.setSize(500,80);
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
	public void setSubValue(int value)
	{
		subBar.setValue(value);
	}
	public void setSubMaxValue(int max)
	{
		subBar.setMaximum(max);
	}
	public int getSubValue()
	{
		return subBar.getValue();
	}
}