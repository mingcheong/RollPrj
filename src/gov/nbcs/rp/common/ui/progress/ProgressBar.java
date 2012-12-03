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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;


/**
 * 进度条.
 */
public class ProgressBar {

	/** The global count. 公用的进度条计数*/
	private static int globalCount = 0;

	/**
	 * 公用的进度条
	 */
	private static ProgressBar globalRefreshingBar = createRefreshing();

	/**
	 * Show global refreshing bar. 仅当计数值==1时，显示公用的进度条
	 */
	public static void showGlobalRefreshingBar() {
//		synchronized (globalRefreshingBar) {
			globalCount++;
			if (globalCount == 1) {
				show(globalRefreshingBar);
			}
//		}
	}

	/**
	 * Hide global refreshing bar. 仅当计数值<=0时，隐藏公用的进度条
	 */
	public static void hideGlobalRefreshingBar() {
//		synchronized (globalRefreshingBar) {
			globalCount--;
			if (globalCount <= 0) {
				hide(globalRefreshingBar);
				globalCount = 0;
			}
//		}
	}

	/**
	 * 创建 "正在获取数据，请稍候······" 的模态进度提示条
	 * 
	 * <p>
	 * 提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（<code>start()</code>）之后。
	 * 
	 * @return the progress bar
	 */
	public static ProgressBar createRefreshing() {
		return createRefreshing("正在获取数据，请稍候······");
	}

	/**
	 * 创建模态进度提示条
	 * 
	 * <p>
	 * 提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（<code>start()</code>）之后。
	 * 
	 * @param title
	 *            the title
	 * 
	 * @return the progress bar
	 */
	public static ProgressBar createRefreshing(String title) {
		return createRefreshing(Global.mainFrame, title);		
	} 
	
	/**
	 * 创建模态进度提示条
	 * 
	 * <p>
	 * 提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（<code>start()</code>）之后。
	 *  
	 * @param frame
	 *            the frame
	 * @param title
	 *            the title
	 * 
	 * @return the progress bar
	 */
	public static ProgressBar createRefreshing(JFrame frame, String title) {
		return new ProgressBar(frame, title, true,
				false);		
	}
	/**
	 * 创建模态进度提示条
	 * 
	 * <p>
	 * 提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（<code>start()</code>）之后。
	 *  
	 * @param frame
	 *            the frame
	 * @param title
	 *            the title
	 * 
	 * @return the progress bar
	 */
	public static ProgressBar createRefreshing(JDialog frame, String title) {
		return new ProgressBar(frame, title, true,
				false);		
	}
	
	/**
	 * 初始化指定的模态进度提示条
	 * @param pBar
	 * @param frame
	 * @param title
	 * @return
	 */
	public static ProgressBar createRefreshing(ProgressBar pBar, JFrame frame, String title) {
		if (pBar == null) {
			pBar = new ProgressBar(frame, title, true,
					false);	
		}
		pBar.setTitle(title);
		return pBar;	
	}

	/**
	 * 初始化指定的模态进度提示条
	 * @param pBar
	 * @param frame
	 * @param title
	 * @return
	 */
	public static ProgressBar createRefreshing(ProgressBar pBar, JDialog dialog, String title) {
		if (pBar == null) {
			pBar = new ProgressBar(dialog, title, true,
					false);	
		}
		pBar.setTitle(title);
		return pBar;	
	}
	
	/**
	 * 显示指定的进度条，
	 * 
	 * @param pgBar
	 */
	public static void show(ProgressBar pgBar) {
		if (pgBar != null) {
			pgBar.show();
		}
	}

	/**
	 * 隐藏指定的进度条
	 * 
	 * @param pgBar
	 */
	public static void hide(ProgressBar pgBar) {
		if (pgBar != null) {
			pgBar.hide();
		}
	}

	/**
	 * 销毁指定的进度条
	 * 
	 * @param pgBar
	 */
	public static void dispose(ProgressBar pgBar) {
		if (pgBar != null) {
			pgBar.dispose();
			pgBar = null;
		}
	}

	private JDialog dialog;

	private JProgressBar progressBar;

	String title;

	/**
	 * Instantiates a new progress bar.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 *            <p>
	 *            提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（start()）之后。
	 */
	public ProgressBar(JFrame owner, String title, boolean modal) {
		this(owner, title, modal, true);
	}

	/**
	 * Instantiates a new progress bar.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 * @param autoShow
	 *            the auto show
	 *            <p>
	 *            提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（start()）之后。
	 */
	public ProgressBar(JFrame owner, String title, boolean modal,
			boolean autoShow) {
		dialog = new JDialog(owner, modal);
		init(title, autoShow);
	}

	/**
	 * Instantiates a new progress bar.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 *            <p>
	 *            提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（start()）之后。
	 */
	public ProgressBar(JDialog owner, String title, boolean modal) {
		this(owner, title, modal, true);
	}

	/**
	 * Instantiates a new progress bar.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param modal
	 *            the modal
	 * @param autoShow
	 *            the auto show
	 *            <p>
	 *            提示：显示模态提示条的操作（<code>show()</code>），应该在业务线程被调用（start()）之后。
	 */
	public ProgressBar(JDialog owner, String title, boolean modal,
			boolean autoShow) {
		dialog = new JDialog(owner, modal);
		init(title, autoShow);
	}

	/**
	 * Inits the UI.
	 * 
	 * @param title
	 *            the title
	 * @param autoShow
	 *            the auto show
	 */
	private void init(String title, boolean autoShow) {
		progressBar = new JProgressBar(0, 100);
		progressBar.setBorderPainted(false);
//		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(true);
		progressBar.setString(title);
		Dimension d1 = new Dimension(400, 25);
		progressBar.setSize(d1);
		dialog.setUndecorated(true);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(progressBar, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Dimension d = new Dimension(400, 25);
		dialog.setSize(d);
		dialog
				.setLocation(
						(Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2,
						(Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2);
		dialog.setResizable(false);
		if (autoShow) {
			show();// dialog.setVisible(true);
		}
	}

	private Timer prgTimer;

	/**
	 * Show.
	 * <p>
	 * 提示：显示模态提示条的操作，应该在业务线程被调用（start()）后才执行。
	 */
	public void show() {
		progressBar.setValue(0);
		prgTimer = new Timer();
		prgTimer.schedule(new TimerTask() {

			public void run() {
				int p = progressBar.getValue();
				if (p == progressBar.getMaximum()) {
					p = progressBar.getMinimum();
				}
				progressBar.setValue(p + 5 - p / 20);
			}
			
		}, 500, 500);
		dialog.show();// setVisible(true);
	}

	/**
	 * Hide.
	 */
	public void hide() {
		if (prgTimer != null) {
			prgTimer.cancel();
		}
		progressBar.setValue(100);
		dialog.hide();// setVisible(false);
	}

	/**
	 * Dispose.
	 */
	public void dispose() {
		if (prgTimer != null) {
			prgTimer.cancel();
		}
		progressBar.setValue(100);
		dialog.dispose();
	}

	public int getValue() {
		return progressBar.getValue();
	}

	public void setValue(int value) {
		progressBar.setValue(value);
	}

	public void setTitle(String title) {
		progressBar.setString(title);
	}

	public String getTitle() {
		return progressBar.getString();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		final ProgressBar pgBar = new ProgressBar(frame, "正在刷新数据，请稍候······",
				true, false);
		new Thread() {
			public void run() {
				try {
					//业务逻辑代码……
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					pgBar.dispose();
				}
			}
		}.start();
		pgBar.show();

	}

}
