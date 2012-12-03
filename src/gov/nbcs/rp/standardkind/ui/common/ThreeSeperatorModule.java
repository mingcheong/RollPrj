
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
 * Title: 三分界面抽象类
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
	// 日志记录
	protected static Logger log = Logger.getLogger(ThreeSeperatorModule.class);

	// 左面板
	protected FPanel leftPanel;

	// 分割面板
	protected FSplitPane fSplitPane;

	// 右面板
	protected FPanel rightPanel;

	//当前状态
	protected int currentState = VarCons.STATE_BRO;
	//前一个状态
	protected int preState = VarCons.STATE_BRO;
	//系统状态接口
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
		//系统状态
//		sysState = SysStateFactory.getSysState();//new SysStateImp();
		try {
			this.bSystemState = true;
		} catch (Exception e) {
			log.error("加载系统当前状态出错！");
		}
		//
		this.currentState = VarCons.STATE_BRO;
		setButtonOnToolBarState();
	}
	public void initialToolBar() {
		// 增加工具条
		try {
			this.createToolBar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("创建工具条出现问题......");
		}
	}
	/**
	 * 设置左面板信息
	 * 包括布局等信息
	 *
	 */
	public abstract void setLeftPanelInfo();
	/**
	 * 设置右面板信息
	 * 包括布局等信息
	 */
	public abstract void setRightPanelInfo();
	/**
	 * 想要处里的细节，覆盖这个方法
	 */
	public abstract void doWhatYouWantToDo();
	/**
	 * 以下操作，可以根据需要进行Overide
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
	 * 设置工具条上按钮状态
	 * @param isLeaf
	 * 是否是树形结构的 叶子结点
	 */
	public void setButtonOnToolBarState() {
		/*
		 * 点增加  保存 取消 可用
		 * 点修改  保存 取消 可用
		 * 点叶结点 修改 删除可用
		 * 点根目录 只有 增加可用
		 */
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			FButton btns = (FButton) controls.get(i);
			//-----------------------------------
			if ("增加".equals(btns.getText()))
				btns.setEnabled(false);
			if ("修改".equals(btns.getText()))
				btns.setEnabled(false);
			if ("删除".equals(btns.getText()))
				btns.setEnabled(false);
			if ("取消".equals(btns.getText()))
				btns.setEnabled(false);
			if ("保存".equals(btns.getText()))
				btns.setEnabled(false);
			if ("特例单位".equals(btns.getText()))
				btns.setEnabled(false);
			
			switch (currentState) {
			case VarCons.STATE_BRO:
				if ("增加".equals(btns.getText()))
					btns.setEnabled(true);

				break;
			case VarCons.STATE_ADD:
				if ("取消".equals(btns.getText()))
					btns.setEnabled(true);
				if ("保存".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_SAVE:
				if ("增加".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_EDIT:
				if ("取消".equals(btns.getText()))
					btns.setEnabled(true);
				if ("保存".equals(btns.getText()))
					btns.setEnabled(true);
				if ("特例单位".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_DELE:
				if ("增加".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_CANL:
				
				break;
			case VarCons.STATE_SELECT:
				if ("增加".equals(btns.getText()))
					btns.setEnabled(true);
				if ("修改".equals(btns.getText()))
					btns.setEnabled(true);
				if ("删除".equals(btns.getText()))
					btns.setEnabled(true);	

				if ("特例单位".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			case VarCons.STATE_SELECT_PARENT:
				if ("增加".equals(btns.getText()))
					btns.setEnabled(true);
				break;
			}
		}
		/*
		 *根据系统当前状态决定，工具栏上的按钮是否可用
		 */
		if(!bSystemState) {
			for (int i = 0; i < controls.size(); i++) {
				FButton btns = (FButton) controls.get(i);
				//-----------------------------------
				if ("增加".equals(btns.getText()))
					btns.setEnabled(false);
				if ("修改".equals(btns.getText()))
					btns.setEnabled(false);
				if ("删除".equals(btns.getText()))
					btns.setEnabled(false);
				if ("取消".equals(btns.getText()))
					btns.setEnabled(false);
				if ("保存".equals(btns.getText()))
					btns.setEnabled(false);
				if ("特例单位".equals(btns.getText()))
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
	 * 检查某个表达式是否包含必要比较符号，比如>,>=,like 等
	 * @param express
	 * @return 返回null 表示正确，否则返回错误解释
	 */
	public String checkExpressCompareSign(String express){
		if(express==null) return null;
		//System.out.println("express=="+express);
		int find = 0;//=0 没有找到，=1 找到
		//转换成小写的
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
		
		return find==1?null:"表达式需要设置范围或比较符号！";
	}
	public boolean isBSystemState() {
		return bSystemState;
	}
}
