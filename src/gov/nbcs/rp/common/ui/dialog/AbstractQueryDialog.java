/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
/**
 * 
 * 查询对话框抽象类:统一界面模式
 * 画界面的工作在本类里完成，
 * 处理业务逻辑的工作留待子类里实现
 */
package gov.nbcs.rp.common.ui.dialog;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Tools;

import java.awt.BorderLayout;

import javax.swing.JFrame;


public abstract class AbstractQueryDialog extends FDialog{

	public static final int DIALOG_WIDTH=550;
	public static final int DIALOG_HEIGHT=560;
	/* *******************面板组件：开始*****************************/
	/**整体面板*/
	private FPanel mainPanel = null;
	/**上面板: 查询工具*/
	private FPanel northPanel = null;
	/**中间的滚动面板:主要内容*/
	protected FScrollPane scrollPane = null;
	/**下面板: 确定,取消按钮*/
	private FFlowLayoutPanel southPanel = null;
	/**空白面板,占位子*/
	private FLabel blankLabel = null;
	/**由客户程序员在此面析中添加自己想要的内容*/
	protected FPanel middlePanel=new FPanel();
	
	/**查询字段*/
	protected FTextField queryTextField = null;
	/**查询按钮*/
	protected FButton queryButton = null;
	/**查询下一个按钮*/
	protected FButton queryNextButton = null;
	/**确定按钮*/
	protected FButton okButton = null;
	/**取消按钮*/
	protected FButton cancelButton = null;
	/* *******************面板组件：结束*****************************/
	/**对话框的标题 */
	protected String dialogTitle ;
	/**
	 * 构造方法
	 */
	public AbstractQueryDialog(String dialogTitle,JFrame frame){
		super(frame);
		this.dialogTitle = dialogTitle ;
	}

	/**
	 * 初始化
	 */
	protected void initialize() {
        setSize(new java.awt.Dimension(DIALOG_WIDTH,DIALOG_HEIGHT));//大小
        setTitle(dialogTitle);//标题
        setModal(true);//模态
        setResizable(false);//不可缩放
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//关闭时的操作
        initialBasicInfo();
        setContentPane(getMainPanel());//添加组件
        addBeanListener();//为组件添加监听事件
        Tools.centerWindow(this);//让其显示在中间
	}


	/**
	 * 得到主面板
	 * @return
	 */
	private FPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new FPanel();
			
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getNorthPanel(), java.awt.BorderLayout.NORTH);
			mainPanel.add(getScrollPane(), java.awt.BorderLayout.CENTER);
			mainPanel.add(getSouthPanel(), java.awt.BorderLayout.SOUTH);
		}
		return mainPanel;
	}
	/**
	 * 上部面板，放置查询文本，查询按钮，下一个按钮等
	 * @return
	 */
	private FPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new FPanel();
			/* 两列，1列放查询文本框，2列放置2个查询按钮*/
			RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(2);
			northPanel.setLayout(rowPreferedLayout);
			northPanel.setSize(550,80);
			rowPreferedLayout.setColumnGap(5);
			rowPreferedLayout.setColumnWidth(350);
			northPanel.setTopInset(5);
			northPanel.setRightInset(5);
			northPanel.setBottomInset(5);
			northPanel.setLeftInset(5);
			/*第1列*/
			northPanel.addControl(getQueryTextField(), new TableConstraints(1,1,false,false));
			/*第2列*/
			RowPreferedLayout secondlayout = new RowPreferedLayout(2);
			secondlayout.setColumnWidth(60);
			secondlayout.setColumnGap(5);
			FPanel secondPanel = new FPanel();
			secondPanel.setLayout(secondlayout);

			secondPanel.addControl(getQueryButton(),new TableConstraints(1,1,false,false));
			secondPanel.addControl(getQueryNextButton(),new TableConstraints(1,1,false,true));

			northPanel.addControl(secondPanel,new TableConstraints(1,1,false,true));
		}
		return northPanel;
	}

	/**
	 * 放置滚动面板
	 * @return
	 */
	private FScrollPane getScrollPane() {
		if (scrollPane == null) {
			addLeadingActor();
			scrollPane = new FScrollPane(middlePanel);
			scrollPane.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204,204,255),5), javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED)));
		}
		return scrollPane;
	}

	/**
	 * 底部面板，放置确定，取消按钮
	 * @return
	 */
	private FFlowLayoutPanel getSouthPanel() {
		if (southPanel == null) {
			/*流式布局面板，目的只是为了居中*/
			southPanel = new FFlowLayoutPanel();
			southPanel.addControl(getOkButton());
			southPanel.addControl(getBlankLabel());
			southPanel.addControl(getCancelButton());
		}
		return southPanel;
	}

	/**
	 * 查询文本框
	 * @return
	 */
	private FTextField getQueryTextField() {
		if (queryTextField == null) {
			queryTextField = new FTextField();
			queryTextField.setTitle("请输入查找内容：");
			queryTextField.setProportion(0.28f);
		}
		return queryTextField;
	}


	/**
	 * 查询按钮
	 * @return
	 */
	private FButton getQueryButton() {
		if (queryButton == null) {
			queryButton = new FButton();
			queryButton.setText("查找");
		}
		return queryButton;
	}


	/**
	 * 查询下一个按钮
	 * @return
	 */
	private FButton getQueryNextButton() {
		if (queryNextButton == null) {
			queryNextButton = new FButton();
			queryNextButton.setText("查找下一个");
		}
		return queryNextButton;
	}


	/**
	 * 确定按钮
	 * @return
	 */
	private FButton getOkButton() {
		if (okButton == null) {
			okButton = new FButton();
			okButton.setText("确定");
		}
		return okButton;
	}

	/**
	 * 取消按钮
	 * @return
	 */
	private FButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new FButton();
			cancelButton.setText("取消");
		}
		return cancelButton;
	}

	/**
	 * 空白标签，用于占位
	 */
	protected FLabel getBlankLabel(){
		if (blankLabel == null) {
			blankLabel = new FLabel();
			blankLabel.setBorder(null);
		}
		return blankLabel;
	}
	
	/*************************抽象方法: 开始***********************************/
	/**
	 * 初始化，自己所需要的一些信息，比如，DataSet,Tree,Table
	 */
	protected abstract void initialBasicInfo();
	/**
	 * 为组件添加监听事件
	 */
	protected abstract void addBeanListener();
	/**
	 * 
	 * 添加主角：树或者表格 设置面板的主要内容，这块，根据需求不同，可以自行设置
	 */
	protected abstract void addLeadingActor();

	/*************************抽象方法: 结束***********************************/	
}