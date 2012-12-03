/**
 * @# FieldProPanel.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.IFilterProvider;
import gov.nbcs.rp.queryreport.reportsh.ui.fieldpro.FieldProFactory;
import gov.nbcs.rp.queryreport.reportsh.ui.fieldpro.FunFieldProvider;
import gov.nbcs.rp.queryreport.reportsh.ui.fieldpro.IFieldProvider;
import gov.nbcs.rp.queryreport.reportsh.ui.fieldpro.IFocusChangeListener;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.lowagie.text.Font;

/**
 * 功能说明:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class FieldProPanel extends FSplitPane implements IFocusChangeListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	List lstFieldPro = new ArrayList();
	List lstFieldProH = new ArrayList();
	List lstFieldProV = new ArrayList();	
	FPanel pnlFieldProH = new FPanel();
	FPanel pnlFieldProV = new FPanel();

	/** The lst h. 横向且有效的 */
	List lstH = new ArrayList();

	/** The lst v. 纵向且有效的 */
	List lstV = new ArrayList();

	public FieldProPanel() {
		
		pnlFieldProH.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlFieldProV.setLayout(new FlowLayout(FlowLayout.LEFT));

		IFieldProvider aPro;
		// -------------- 横向 --------------------
		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_ACCT);
		addProH(aPro);

		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_ACCT_TYPE);
		addProH(aPro);

		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_ACCT_JJ);
		addProH(aPro);

		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_DIV_FMKIND);
		addProH(aPro);

		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_DIV_KIND);
		addProH(aPro);

		// -------------- 纵向 --------------------
		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_FUN);
		addProV(aPro);
		
		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_DEP);
		addProV(aPro);

//		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_DIV);
//		addProV(aPro);

		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_PRJSORT);
		addProV(aPro);

		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_PROJECT);
		addProV(aPro);

//		aPro = FieldProFactory.getFieldProvider(IFilterProvider.TYPE_PRJSTATUS);
//		addProV(aPro);

		this.setDividerLocation(325);		
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);		
		
		// 横向
		FPanel pnlH = new FPanel();
		RowPreferedLayout layoutH = new RowPreferedLayout(2);
		pnlH.setLayout(layoutH);
		layoutH.setColumnWidth(24);
		layoutH.setColumnGap(0);
		pnlH.setBackground(new Color(200, 238, 200));

		FLabel lblH = new FLabel();
		lblH.setFontSize(20);
		lblH.setFontStyle(Font.BOLD);
		lblH.setText("<html>横<br>向<br>扩<br>展</html>");
		lblH.setVerticalTextPosition(SwingConstants.TOP);
		
		pnlH.addControl(lblH, new TableConstraints(1, 1, true, false));
		FScrollPane spnlH = new FScrollPane(pnlFieldProH);
		pnlH.addControl(spnlH, new TableConstraints(1, 1, true, true));
		this.addControl(pnlH);	
		
		// 纵向
		FPanel pnlV = new FPanel();
		RowPreferedLayout layoutV = new RowPreferedLayout(2);
		pnlV.setLayout(layoutV);
		layoutV.setColumnWidth(24);
		layoutV.setColumnGap(0);
		pnlV.setBackground(new Color(200, 200, 238));

		FLabel lblV = new FLabel();
		lblV.setFontSize(20);
		lblV.setFontStyle(Font.BOLD);
		lblV.setText("<html>纵<br>向<br>汇<br>总</html>");
		lblV.setVerticalTextPosition(SwingConstants.TOP);
		
		pnlV.addControl(lblV, new TableConstraints(1, 1, true, false));
		FScrollPane spnlV = new FScrollPane(pnlFieldProV);
		pnlV.addControl(spnlV, new TableConstraints(1, 1, true, true));
		this.addControl(pnlV);	
	}

	private void addProH(IFieldProvider aPro) {
		aPro.setFouceListener(this);
		lstFieldPro.add(aPro);
		lstFieldProH.add(aPro);
		pnlFieldProH.add(aPro.getDispPanel());
	}

	private void addProV(IFieldProvider aPro) {
		aPro.setFouceListener(this);
		lstFieldPro.add(aPro);
		lstFieldProV.add(aPro);
		pnlFieldProV.add(aPro.getDispPanel());
	}
	
	public void changeUseType(IFieldProvider pro) {
		if (pro.isHorFieldPro()) { // 移至横向列表
			if (lstFieldProV.contains(pro)) {
				lstFieldProV.remove(pro);
				int idx = getLastActivedProIndex(lstFieldProH);
				if (idx >= 0 && idx < lstFieldProH.size() - 1)
					lstFieldProH.add(idx + 1, pro);
				else if (idx >= lstFieldProH.size() - 1)
					lstFieldProH.add(pro);
				else 
					lstFieldProH.add(0, pro);
			}
		} else if (pro.isActived()) { // 移至纵向列表
			if (lstFieldProH.contains(pro)) {
				lstFieldProH.remove(pro);
				int idx = getLastActivedProIndex(lstFieldProV);
				if (idx >= 0 && idx < lstFieldProV.size() - 1)
					lstFieldProV.add(idx + 1, pro);
				else if (idx >= lstFieldProV.size() - 1)
					lstFieldProV.add(pro);
				else
					lstFieldProV.add(0, pro);
					
			}
		}
		refreshPro();
	}

	public void getFocus(IFieldProvider pro) {
		int iCount = lstFieldPro.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstFieldPro.get(i);
			if (aPro == pro) {
				aPro.setFocus(true);
			} else
				aPro.setFocus(false);
		}

	}

	public void moveLeft() {
		List lstPro = getFocusProList();
		if (lstPro == null) return;
		int index = getFocusProIndex();
		if (index < 1)
			return;
		Object obj = lstPro.set(index - 1, lstPro.get(index));
		lstPro.set(index, obj);
		refreshPro();

	}

	public void moveRight() {
		List lstPro = getFocusProList();
		if (lstPro == null) return;
		int index = getFocusProIndex();
		if (index < 0 || index >= lstPro.size() - 1)
			return;
		Object obj = lstPro.set(index + 1, lstPro.get(index));
		lstPro.set(index, obj);
		refreshPro();
	}

	public String check() {
		lstH = new ArrayList();
		lstV = new ArrayList();
		StringBuffer sb = new StringBuffer();
		int iCount = lstFieldProH.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstFieldProH.get(i);
			String err = aPro.check();
			if (!Common.isNullStr(err))
				sb.append(err);
			if (aPro.isHorFieldPro())
				lstH.add(aPro);
			else if (aPro.isActived())
				lstV.add(aPro);
		}
		iCount = lstFieldProV.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstFieldProV.get(i);
			String err = aPro.check();
			if (!Common.isNullStr(err))
				sb.append(err);
			if (aPro.isHorFieldPro())
				lstH.add(aPro);
			else if (aPro.isActived())
				lstV.add(aPro);
		}
		if (Common.isNullStr(sb.toString())) {
			// if (lstH.isEmpty() && lstV.isEmpty())
			// return "未进行设置。";
			// else if (lstH.isEmpty())
			// return "未进行横向扩展的设置。";
			// else
			if (lstV.isEmpty())
				return "未进行纵向汇总的设置。";
			return "";
		} else
			return sb.toString();
	}
	 
	/**
	 * Gets the focus pro panel.
	 * 
	 * @return the focus pro panel
	 */
	private List getFocusProList() {
		IFieldProvider focusPro = getFocusPro();
		if (focusPro != null) {
			if (lstFieldProH.contains(focusPro)) {
				return lstFieldProH;
			} else if (lstFieldProV.contains(focusPro)) {
				return lstFieldProV;
			} 
		}
		return null;
	}

	/**
	 * Gets the focus pro.
	 * 
	 * @return the focus pro
	 */
	private IFieldProvider getFocusPro() {
		int iCount = lstFieldPro.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstFieldPro.get(i);
			if (aPro.isFocused())
				return aPro;
		}
		return null;		
	}	

	/**
	 * Gets the focus pro index.
	 * 
	 * @return the focus pro index
	 */
	private int getFocusProIndex() {
		List lstPro = getFocusProList();
		if (lstPro == null) return -1;
		int iCount = lstPro.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstPro.get(i);
			if (aPro.isFocused())
				return i;
		}
		return -1;
	}
	
	private int getLastActivedProIndex(List lstPro) {
		for (int i = lstPro.size() - 1; i >= 0; i--) {
			IFieldProvider pro = (IFieldProvider) lstPro.get(i);
			if (pro.isActived())
				return i;
		}
		return -1;
	}

	/**
	 * 是否选了资金来源
	 * @return
	 */
	public boolean isSelectFun() {
		int iCount = lstFieldPro.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstFieldPro.get(i);
			if (aPro.isActived() && aPro instanceof FunFieldProvider)
				return true;
		}
		return false;
	}
	
	public boolean isOrderByMoney() {
		int iCount = lstFieldPro.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstFieldPro.get(i);
			if (aPro.isActived() && aPro.isOrderByMoney()) {
				return true;
			}
		}
		return false;
	}

	public List getHList() {
		return lstH;
	}

	public List getVList() {
		return lstV;
	}

	private void refreshPro() {
		{ // H
			int iCount = lstFieldProH.size();
			pnlFieldProH.removeAll();
			pnlFieldProH.setLayout(new FlowLayout(FlowLayout.LEFT));
			for (int i = 0; i < iCount; i++) {
				pnlFieldProH.add(((IFieldProvider) lstFieldProH.get(i)).getDispPanel());
			}
		}
		{ // V
			int iCount = lstFieldProV.size();
			pnlFieldProV.removeAll();
			pnlFieldProV.setLayout(new FlowLayout(FlowLayout.LEFT));
			for (int i = 0; i < iCount; i++) {
				pnlFieldProV.add(((IFieldProvider) lstFieldProV.get(i)).getDispPanel());
			}
		}
		this.validate();
		this.repaint();
	}

	public void reset() {
		int iCount = lstFieldPro.size();
		for (int i = 0; i < iCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstFieldPro.get(i);
			aPro.reset();
		}
	}

}
