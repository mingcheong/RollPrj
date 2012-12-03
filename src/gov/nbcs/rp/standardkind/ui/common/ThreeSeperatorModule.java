
package gov.nbcs.rp.standardkind.ui.common;

import java.util.List;

import org.apache.log4j.Logger;

import gov.nbcs.rp.common.action.ActionedUI;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
/**
 * ThreeSeperatorModule.java
 * <p>
 * Title: ���ֽ��������
 * </p>
 * <p>
 * Description:
 * |-------------------|
 * |-------------------|
 * |     |             |
 * |     |             |
 * |     |             |
 * |     |             |
 * |-------------------|
 * </p>

 */
public abstract class ThreeSeperatorModule extends FModulePanel implements
		ActionedUI {
	// ��־��¼
	protected static Logger log = Logger.getLogger(ThreeSeperatorModule.class);

	// �����
	protected FPanel leftPanel;

	// �ָ����
	protected FSplitPane fSplitPane;

	// �����
	protected FPanel rightPanel;

	//��ǰ״̬
	protected int currentState = VarCons.STATE_BRO;
	//ǰһ��״̬
	protected int preState = VarCons.STATE_BRO;
	//ϵͳ״̬�ӿ�
//	protected ISysState sysState;
	//
	private boolean bSystemState = true;
	
	public void initize() {
		fSplitPane = new FSplitPane();
		fSplitPane.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		fSplitPane.setDividerLocation(200);
		this.add(fSplitPane);
		//
		leftPanel = new FPanel();
		setLeftPanelInfo();
		//
		fSplitPane.addControl(leftPanel);
		//
		rightPanel = new FPanel();
		setRightPanelInfo();
		//
		fSplitPane.addControl(rightPanel);
		//
		doWhatYouWantToDo();
		initialToolBar();
		//ϵͳ״̬
//		sysState = SysStateFactory.getSysState();//new SysStateImp();
		try {
			this.bSystemState = true;
		} catch (Exception e) {
			log.error("����ϵͳ��ǰ״̬����");
		}
		//
		this.currentState = VarCons.STATE_BRO;
		setButtonOnToolBarState();
	}
	public void initialToolBar() {
		// ���ӹ�����
		try {
			this.createToolBar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("������������������......");
		}
	}
	/**
	 * �����������Ϣ
	 * �������ֵ���Ϣ
	 *
	 */
	public abstract void setLeftPanelInfo();
	/**
	 * �����������Ϣ
	 * �������ֵ���Ϣ
	 */
	public abstract void setRightPanelInfo();
	/**
	 * ��Ҫ�����ϸ�ڣ������������
	 */
	public abstract void doWhatYouWantToDo();
	/**
	 * ���²��������Ը�����Ҫ����Overide
	 */
	public void doAdd() {
		log.info("default do add...");
		preState = currentState;
		this.currentState = VarCons.STATE_ADD;
		this.setButtonOnToolBarState();
	}

	public void doDelete() {
		log.info("default do del...");
		preState = currentState;
		this.currentState = VarCons.STATE_DELE;
		this.setButtonOnToolBarState();
	}

	public void doCancel() {
		log.info("default do cancel...");
		currentState = preState;
		this.setButtonOnToolBarState();
	}

	public void doInsert() {
		log.info("default do insert...");

	}

	public void doModify() {
		log.info("default do modify...");
		preState = currentState;
		this.currentState = VarCons.STATE_EDIT;
		this.setButtonOnToolBarState();
	}

	public void doSave() {
		log.info("default do save...");
		preState = currentState;
		this.currentState = VarCons.STATE_SAVE;
		this.setButtonOnToolBarState();
	}

	public void doClose() {
		log.info("default do close...");
		((FFrame) Global.mainFrame).closeMenu();
	}
	/**
	 * ���ù������ϰ�ť״̬
	 * @param isLeaf
	 * �Ƿ������νṹ�� Ҷ�ӽ��
	 */
	public void setButtonOnToolBarState() {
		/*
		 * ������  ���� ȡ�� ����
		 * ���޸�  ���� ȡ�� ����
		 * ��Ҷ��� �޸� ɾ������
		 * ���Ŀ¼ ֻ�� ���ӿ���
		 */
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			FButton btns = (FButton) controls.get(i);
			//-----------------------------------
			if ("����".equals(btns.getText()))
				btns.setEnabled(false);
			if ("�޸�".equals(btns.getText()))
				btns.setEnabled(false);
			if ("ɾ��".equals(btns.getText()))
				btns.setEnabled(false);
			if ("ȡ��".equals(btns.getText()))
				btns.setEnabled(false);
			if ("����".equals(btns.getText()))
				btns.setEnabled(false);
			if ("������λ".equals(btns.getText()))
				btns.setEnabled(false);
			
			switch (currentState) {
			case VarCons.STATE_BRO:
				if ("����".equals(btns.getText()))
					btns.setEnabled(true);

				break;
			case VarCons.STATE_ADD:
				if ("ȡ��".equals(btns.getText()))
					btns.setEnabled(true);
				if ("����".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_SAVE:
				if ("����".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_EDIT:
				if ("ȡ��".equals(btns.getText()))
					btns.setEnabled(true);
				if ("����".equals(btns.getText()))
					btns.setEnabled(true);
				if ("������λ".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_DELE:
				if ("����".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_CANL:
				
				break;
			case VarCons.STATE_SELECT:
				if ("����".equals(btns.getText()))
					btns.setEnabled(true);
				if ("�޸�".equals(btns.getText()))
					btns.setEnabled(true);
				if ("ɾ��".equals(btns.getText()))
					btns.setEnabled(true);	

				if ("������λ".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_SELECT_PARENT:
				if ("����".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			}
		}
		/*
		 *����ϵͳ��ǰ״̬�������������ϵİ�ť�Ƿ����
		 */
		if(!bSystemState) {
			for (int i = 0; i < controls.size(); i++) {
				FButton btns = (FButton) controls.get(i);
				//-----------------------------------
				if ("����".equals(btns.getText()))
					btns.setEnabled(false);
				if ("�޸�".equals(btns.getText()))
					btns.setEnabled(false);
				if ("ɾ��".equals(btns.getText()))
					btns.setEnabled(false);
				if ("ȡ��".equals(btns.getText()))
					btns.setEnabled(false);
				if ("����".equals(btns.getText()))
					btns.setEnabled(false);
				if ("������λ".equals(btns.getText()))
					btns.setEnabled(false);
			}
		}

	}
	public int getCurrentState() {
		return currentState;
	}
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	public int getPreState() {
		return preState;
	}
	public void setPreState(int preState) {
		this.preState = preState;
	}
	
	/**
	 * ���ĳ�����ʽ�Ƿ������Ҫ�ȽϷ��ţ�����>,>=,like ��
	 * @param express
	 * @return ����null ��ʾ��ȷ�����򷵻ش������
	 */
	public String checkExpressCompareSign(String express){
		if(express==null) return null;
		//System.out.println("express=="+express);
		int find = 0;//=0 û���ҵ���=1 �ҵ�
		//ת����Сд��
		express = express.toLowerCase();
		String[] compareSign = new String[]{
				">",">=","<","<=","!=","=","<>"," like "," is null"," is "," between "," exists "
		};
		for(int i=0;i<compareSign.length;i++){
			//System.out.println("compareSign=="+compareSign[i]);
			if(express.indexOf(compareSign[i])>-1){
				//System.out.println("compareSign2222=="+compareSign[i]);
				find = 1;
				break;
			}
		}
		
		return find==1?null:"���ʽ��Ҫ���÷�Χ��ȽϷ��ţ�";
	}
	public boolean isBSystemState() {
		return bSystemState;
	}
}
