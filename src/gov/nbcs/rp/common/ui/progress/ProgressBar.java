/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
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
 * ������.
 */
public class ProgressBar {

	/** The global count. ���õĽ���������*/
	private static int globalCount = 0;

	/**
	 * ���õĽ�����
	 */
	private static ProgressBar globalRefreshingBar = createRefreshing();

	/**
	 * Show global refreshing bar. ��������ֵ==1ʱ����ʾ���õĽ�����
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
	 * Hide global refreshing bar. ��������ֵ<=0ʱ�����ع��õĽ�����
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
	 * ���� "���ڻ�ȡ���ݣ����Ժ򡤡���������" ��ģ̬������ʾ��
	 * 
	 * <p>
	 * ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�<code>start()</code>��֮��
	 * 
	 * @return the progress bar
	 */
	public static ProgressBar createRefreshing() {
		return createRefreshing("���ڻ�ȡ���ݣ����Ժ򡤡���������");
	}

	/**
	 * ����ģ̬������ʾ��
	 * 
	 * <p>
	 * ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�<code>start()</code>��֮��
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
	 * ����ģ̬������ʾ��
	 * 
	 * <p>
	 * ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�<code>start()</code>��֮��
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
	 * ����ģ̬������ʾ��
	 * 
	 * <p>
	 * ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�<code>start()</code>��֮��
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
	 * ��ʼ��ָ����ģ̬������ʾ��
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
	 * ��ʼ��ָ����ģ̬������ʾ��
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
	 * ��ʾָ���Ľ�������
	 * 
	 * @param pgBar
	 */
	public static void show(ProgressBar pgBar) {
		if (pgBar != null) {
			pgBar.show();
		}
	}

	/**
	 * ����ָ���Ľ�����
	 * 
	 * @param pgBar
	 */
	public static void hide(ProgressBar pgBar) {
		if (pgBar != null) {
			pgBar.hide();
		}
	}

	/**
	 * ����ָ���Ľ�����
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
	 *            ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�start()��֮��
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
	 *            ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�start()��֮��
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
	 *            ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�start()��֮��
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
	 *            ��ʾ����ʾģ̬��ʾ���Ĳ�����<code>show()</code>����Ӧ����ҵ���̱߳����ã�start()��֮��
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
	 * ��ʾ����ʾģ̬��ʾ���Ĳ�����Ӧ����ҵ���̱߳����ã�start()�����ִ�С�
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
		final ProgressBar pgBar = new ProgressBar(frame, "����ˢ�����ݣ����Ժ򡤡���������",
				true, false);
		new Thread() {
			public void run() {
				try {
					//ҵ���߼����롭��
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
