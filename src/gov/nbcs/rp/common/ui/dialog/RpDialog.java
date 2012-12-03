/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.dialog;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Tools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


/**
 * The class FbDialog. <br>
 * 1、默认屏幕居中，大小800x600；<br>
 * 2、含工具栏，含“关闭”按钮；
 *
 */
public abstract class RpDialog extends FDialog {

	/** The btn close. */
	private FButton btnClose;

	/** The toolbar. */
	private FToolBar toolbar;

	/** The body pane. */
	private FPanel bodyPane;

	/**
	 * Instantiates a new fb dialog.
	 */
	public RpDialog() {
		super();
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param modal
	 *            the modal
	 */
	public RpDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 * @param gc
	 *            the gc
	 */
	public RpDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 */
	public RpDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 */
	public RpDialog(Dialog owner, String title) {
		super(owner, title);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 */
	public RpDialog(Dialog owner) {
		super(owner);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param modal
	 *            the modal
	 */
	public RpDialog(Frame owner, boolean modal) {
		super(owner, modal);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 * @param gc
	 *            the gc
	 */
	public RpDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 */
	public RpDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 */
	public RpDialog(Frame owner, String title) {
		super(owner, title);
		init();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 */
	public RpDialog(Frame owner) {
		super(owner);
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Tools.centerWindow(this);
		setSize(800, 600);

		FPanel basePanel = new FPanel();
		getContentPane().add(basePanel);

		RowPreferedLayout baseLayout = new RowPreferedLayout(50);
		baseLayout.setRowHeight(5);
		basePanel.setLayout(baseLayout);

		toolbar = new FToolBar();
		// RowPreferedLayout toolbarLayout = new RowPreferedLayout(50);
		// toolbarLayout.setRowHeight(45);
		// toolbar.setLayout(toolbarLayout);
		basePanel.addControl(toolbar, new TableConstraints(6, 50, false, true));

		btnClose = new FButton("btnClose", "关闭");
		btnClose.setIcon("images/fbudget/close.gif");
		btnClose.setVerticalTextPosition(SwingConstants.BOTTOM);
		toolbar.addControl(btnClose);
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (confirmClose()) {
					RpDialog.this.setVisible(false);
				}
			}
		});

		toolbar.addSeparator();

		bodyPane = new FPanel();
		bodyPane.setLayout(new BorderLayout());
		basePanel.addControl(bodyPane, new TableConstraints(1, 50, true, true));

		initComponents();
		bindListeners();
	}

	/**
	 * Confirm close. 实现确认关闭。如可以关闭，返回True
	 * 
	 * @return true, if confirm close
	 */
	protected abstract boolean confirmClose();

	/**
	 * Inits the components. 自动在构造函数中调用。
	 */
	protected void initComponents() {
	}

	/**
	 * Bind listeners. 自动在构造函数中调用。
	 */
	protected void bindListeners() {
	}

	/**
	 * Gets the toolbar.
	 * 
	 * @return the toolbar
	 */
	public FToolBar getToolbar() {
		return toolbar;
	}

	/**
	 * Gets the body pane.
	 * 
	 * @return the body pane
	 */
	public Container getBodyPane() {
		return bodyPane;
	}

	/**
	 * Reset the toolbar.
	 */
	public void resetToolbar() {
		toolbar.removeAll();
		toolbar.addControl(btnClose);
		toolbar.validate();
		toolbar.repaint();
	}

}
