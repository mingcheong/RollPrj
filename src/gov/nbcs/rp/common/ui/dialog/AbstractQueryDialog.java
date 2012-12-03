/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
/**
 * 
 * ��ѯ�Ի��������:ͳһ����ģʽ
 * ������Ĺ����ڱ�������ɣ�
 * ����ҵ���߼��Ĺ�������������ʵ��
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
	/* *******************����������ʼ*****************************/
	/**�������*/
	private FPanel mainPanel = null;
	/**�����: ��ѯ����*/
	private FPanel northPanel = null;
	/**�м�Ĺ������:��Ҫ����*/
	protected FScrollPane scrollPane = null;
	/**�����: ȷ��,ȡ����ť*/
	private FFlowLayoutPanel southPanel = null;
	/**�հ����,ռλ��*/
	private FLabel blankLabel = null;
	/**�ɿͻ�����Ա�ڴ�����������Լ���Ҫ������*/
	protected FPanel middlePanel=new FPanel();
	
	/**��ѯ�ֶ�*/
	protected FTextField queryTextField = null;
	/**��ѯ��ť*/
	protected FButton queryButton = null;
	/**��ѯ��һ����ť*/
	protected FButton queryNextButton = null;
	/**ȷ����ť*/
	protected FButton okButton = null;
	/**ȡ����ť*/
	protected FButton cancelButton = null;
	/* *******************������������*****************************/
	/**�Ի���ı��� */
	protected String dialogTitle ;
	/**
	 * ���췽��
	 */
	public AbstractQueryDialog(String dialogTitle,JFrame frame){
		super(frame);
		this.dialogTitle = dialogTitle ;
	}

	/**
	 * ��ʼ��
	 */
	protected void initialize() {
        setSize(new java.awt.Dimension(DIALOG_WIDTH,DIALOG_HEIGHT));//��С
        setTitle(dialogTitle);//����
        setModal(true);//ģ̬
        setResizable(false);//��������
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//�ر�ʱ�Ĳ���
        initialBasicInfo();
        setContentPane(getMainPanel());//������
        addBeanListener();//Ϊ�����Ӽ����¼�
        Tools.centerWindow(this);//������ʾ���м�
	}


	/**
	 * �õ������
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
	 * �ϲ���壬���ò�ѯ�ı�����ѯ��ť����һ����ť��
	 * @return
	 */
	private FPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new FPanel();
			/* ���У�1�зŲ�ѯ�ı���2�з���2����ѯ��ť*/
			RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(2);
			northPanel.setLayout(rowPreferedLayout);
			northPanel.setSize(550,80);
			rowPreferedLayout.setColumnGap(5);
			rowPreferedLayout.setColumnWidth(350);
			northPanel.setTopInset(5);
			northPanel.setRightInset(5);
			northPanel.setBottomInset(5);
			northPanel.setLeftInset(5);
			/*��1��*/
			northPanel.addControl(getQueryTextField(), new TableConstraints(1,1,false,false));
			/*��2��*/
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
	 * ���ù������
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
	 * �ײ���壬����ȷ����ȡ����ť
	 * @return
	 */
	private FFlowLayoutPanel getSouthPanel() {
		if (southPanel == null) {
			/*��ʽ������壬Ŀ��ֻ��Ϊ�˾���*/
			southPanel = new FFlowLayoutPanel();
			southPanel.addControl(getOkButton());
			southPanel.addControl(getBlankLabel());
			southPanel.addControl(getCancelButton());
		}
		return southPanel;
	}

	/**
	 * ��ѯ�ı���
	 * @return
	 */
	private FTextField getQueryTextField() {
		if (queryTextField == null) {
			queryTextField = new FTextField();
			queryTextField.setTitle("������������ݣ�");
			queryTextField.setProportion(0.28f);
		}
		return queryTextField;
	}


	/**
	 * ��ѯ��ť
	 * @return
	 */
	private FButton getQueryButton() {
		if (queryButton == null) {
			queryButton = new FButton();
			queryButton.setText("����");
		}
		return queryButton;
	}


	/**
	 * ��ѯ��һ����ť
	 * @return
	 */
	private FButton getQueryNextButton() {
		if (queryNextButton == null) {
			queryNextButton = new FButton();
			queryNextButton.setText("������һ��");
		}
		return queryNextButton;
	}


	/**
	 * ȷ����ť
	 * @return
	 */
	private FButton getOkButton() {
		if (okButton == null) {
			okButton = new FButton();
			okButton.setText("ȷ��");
		}
		return okButton;
	}

	/**
	 * ȡ����ť
	 * @return
	 */
	private FButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new FButton();
			cancelButton.setText("ȡ��");
		}
		return cancelButton;
	}

	/**
	 * �հױ�ǩ������ռλ
	 */
	protected FLabel getBlankLabel(){
		if (blankLabel == null) {
			blankLabel = new FLabel();
			blankLabel.setBorder(null);
		}
		return blankLabel;
	}
	
	/*************************���󷽷�: ��ʼ***********************************/
	/**
	 * ��ʼ�����Լ�����Ҫ��һЩ��Ϣ�����磬DataSet,Tree,Table
	 */
	protected abstract void initialBasicInfo();
	/**
	 * Ϊ�����Ӽ����¼�
	 */
	protected abstract void addBeanListener();
	/**
	 * 
	 * ������ǣ������߱�� ����������Ҫ���ݣ���飬��������ͬ��������������
	 */
	protected abstract void addLeadingActor();

	/*************************���󷽷�: ����***********************************/	
}