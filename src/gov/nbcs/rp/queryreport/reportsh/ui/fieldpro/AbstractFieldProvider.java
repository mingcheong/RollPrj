/**
 * @# AbstractFieldProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.border.LineBorder;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.framework.systemmanager.ImagePanel;
import com.foundercy.pf.util.Global;

/**
 * 功能说明:
 *<P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public abstract class AbstractFieldProvider extends FPanel implements
		IFieldProvider {

	private static final Dimension PREFERRED_SIZE_ENABLE = new Dimension(192,
			290);

	private static final Dimension PREFERRED_SIZE_DISABLE = new Dimension(192,
			50);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// private static final String IMG_FOCUS = "images/fbudget/focus.gif";

	// private static final String IMG_NOT_FOCUS =
	// "images/fbudget/notfocus.gif";

	private static final String IMG_HOR = "images/fbudget/hor.gif";

	private static final String IMG_VER = "images/fbudget/ver.gif";

	private static final String IMG_NOT_USED = "images/fbudget/notused.gif";

	// public static final Color colH = new Color(255, 143, 243);

	// public static final Color colV = new Color(95, 255, 237);

	public static final Color colActived = new Color(200, 200, 238);

	public static final Color colDisactived = new Color(168, 168, 168);

	public static final String USE_HOR = "HOR";// 横向

	public static final String USE_VER = "VER";// 纵向

	public static final String USE_NO_USE = "NO_USE";// 不用

	public static final String TYPEREF = USE_NO_USE + "#不启用+" + USE_HOR
			+ "#横向扩展+" + USE_VER + "#纵向汇总";

	private boolean isFocused = false;

	private IFocusChangeListener focusListener;

	protected MouseClick comCick = new MouseClick();

	FPanel pnlTitle;

	FLabel lblTitle;

	FPanel pnlBody;

	// ImagePanel imgFocus;

	// ImagePanel imgNotFocus;

	ImagePanel imgHor;

	ImagePanel imgVer;

	ImagePanel imgNotUsed;

	// 使用状态
	FComboBox cbxUseType;

	public AbstractFieldProvider() {
		FPanel pnlTop = new FPanel();
		pnlTop.setLayout(new BorderLayout());
		pnlTitle = new FPanel();
		pnlTitle.setBorder();
		pnlTitle.setPreferredSize(new Dimension(90, 28));
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT);

		// imgFocus = new ImagePanel(IMG_FOCUS);
		// imgFocus.transferFocusBackward();
		// imgFocus.setPreferredSize(new Dimension(20, 20));
		// imgFocus.setVisible(false);
		// pnlTitle.add(imgFocus);
		//
		// imgNotFocus = new ImagePanel(IMG_NOT_FOCUS);
		// imgNotFocus.transferFocusBackward();
		// imgNotFocus.setPreferredSize(new Dimension(20, 20));
		// pnlTitle.add(imgNotFocus);

		imgHor = new ImagePanel(IMG_HOR);
		imgHor.transferFocusBackward();
		imgHor.setPreferredSize(new Dimension(24, 24));
		imgHor.setVisible(false);
		pnlTitle.add(imgHor);

		imgVer = new ImagePanel(IMG_VER);
		imgVer.transferFocusBackward();
		imgVer.setPreferredSize(new Dimension(24, 24));
		pnlTitle.add(imgVer);

		imgNotUsed = new ImagePanel(IMG_NOT_USED);
		imgNotUsed.transferFocusBackward();
		imgNotUsed.setPreferredSize(new Dimension(24, 24));
		pnlTitle.add(imgNotUsed);

		pnlTitle.setLayout(fl);
		lblTitle = new FLabel();
		lblTitle.setText("数据");
		pnlTitle.add(lblTitle);
		pnlTitle.setBackground(colDisactived);

		cbxUseType = new FComboBox();
		cbxUseType.setTitle("使用状态");
		// cbxUseType.setTitleVisible(false);
		cbxUseType.setRefModel(TYPEREF);
		cbxUseType.setValue(USE_NO_USE);
		cbxUseType.setPreferredSize(new Dimension(200, 20));
		cbxUseType.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				setEnabled(USE_HOR.equals(arg0.getNewValue()));
				setStatusByUseType(!USE_NO_USE.equals(arg0.getNewValue()));
				focusListener.changeUseType(AbstractFieldProvider.this);
			}
		});

		pnlTop.add(pnlTitle, BorderLayout.NORTH);
		pnlTop.add(cbxUseType, BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add(pnlTop, BorderLayout.NORTH);
		pnlBody = new FPanel();
		pnlBody.setLayout(new BorderLayout());
		this.add(pnlBody, BorderLayout.CENTER);
		this.setBorder(new LineBorder(new Color(127, 157, 185), 1));
		// this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(PREFERRED_SIZE_ENABLE);

		cbxUseType.addMouseListener(comCick);
		cbxUseType.getEditor().addMouseListener(comCick);
		// Component[] subCmps = cbxUseType.getComponents();
		// if (subCmps != null && subCmps.length > 0) {
		// for (int i = 0; i < subCmps.length; i++) {
		// Component component = subCmps[i];
		// if (component != null)
		// component.addMouseListener(comCick);
		// }
		// }
		lblTitle.addMouseListener(comCick);
		// imgFocus.addMouseListener(comCick);
		// imgNotFocus.addMouseListener(comCick);
		imgHor.addMouseListener(comCick);
		imgVer.addMouseListener(comCick);
		imgNotUsed.addMouseListener(comCick);
		lblTitle.addMouseListener(comCick);
		pnlTitle.addMouseListener(comCick);
		pnlBody.addMouseListener(comCick);
		init();
	}

	public FPanel getFilterPanel() {

		return this;
	}

	public FPanel getBodyPanel() {
		return pnlBody;
	}

	public FPanel getDispPanel() {
		return this;
	}

	public boolean isActived() {
		return !USE_NO_USE.equals(cbxUseType.getValue());
	}

	public void reset() {
		cbxUseType.setValue(USE_NO_USE);
		setStatusByUseType(false);
	}

	public void setStatusByUseType(boolean enable) {
		// imgNotFocus.setVisible(!enable);
		// imgFocus.setVisible(enable);
		imgHor.setVisible(USE_HOR.equals(cbxUseType.getValue()));
		imgVer.setVisible(USE_VER.equals(cbxUseType.getValue()));
		imgNotUsed.setVisible(USE_NO_USE.equals(cbxUseType.getValue()));
		pnlTitle.repaint();
	}

	abstract void init();

	public abstract String check();

	public static DataSet getSelectDs(CustomTree tree) {

		MyTreeNode nodes[] = tree.getSelectedNodes(false);
		Map mapBook = new HashMap();
		int iCount = nodes.length;
		for (int i = 0; i < iCount; i++) {
			mapBook.put(nodes[i].getBookmark(), null);
		}
		DataSet typeDs = tree.getDataSet();

		String book = typeDs.toogleBookmark();
		try {
			DataSet ds = DataSet.create();
			typeDs.beforeFirst();
			while (typeDs.next()) {
				if (mapBook.containsKey(typeDs.toogleBookmark())) {
					ds.append();
					ds.setOriginData(typeDs.getOriginData());
				}
			}
			return ds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				typeDs.gotoBookmark(book);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void setEnabled(boolean isEnabled);

	public void setFocus(boolean isFocus) {
		if (this.isFocused == isFocus)
			return;
		this.isFocused = isFocus;
		pnlTitle.setBackground(!isFocused ? colDisactived : colActived);
		pnlTitle.repaint();
	}

	public boolean isFocused() {
		return isFocused;
	}

	/**
	 * 取得了焦点
	 */
	protected void ganFocuse() {
		this.isFocused = true;
		pnlTitle.setBackground(!isFocused ? colDisactived : colActived);
		if (focusListener != null)
			focusListener.getFocus(this);
		pnlTitle.repaint();
	}

	public void setFouceListener(IFocusChangeListener list) {
		focusListener = list;
	}

	class MouseClick extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			ganFocuse();
		}
	}

	public boolean isHorFieldPro() {
		return USE_HOR.equals(cbxUseType.getValue());
	}

	public void setTitle(String title) {
		lblTitle.setText(title);
	}

	public static String getFilter() {
		return " where LOGIN_ID='" + Global.getUserId() + "'";
	}

	/**
	 * Gets the total header fields. 总计行字段
	 * 
	 * @param lstV the lst v
	 * 
	 * @return the total header fields
	 */
	public static String getTotalHeaderFields(List lstV) {
		StringBuffer sbHeaderFields = new StringBuffer();
		int i = 0;
		for (Iterator itr = lstV.iterator(); itr.hasNext();i++) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			if (i == 0) {
				sbHeaderFields.append(",").append(pro.getTotalHHeaderSql());
			} else {
				sbHeaderFields.append(",").append(pro.getEmptyHHeaderSql());
			}
		}
		return sbHeaderFields.substring(1);
	}

	public static String getHeaderFields(List lstV, IFieldProvider thisPro) {
		StringBuffer sbHeaderFields = new StringBuffer();
		for (Iterator itr = lstV.iterator(); itr.hasNext();) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			if (thisPro == pro) {
				sbHeaderFields.append(",").append(pro.getHHeaderSql());
			} else {
				sbHeaderFields.append(",").append(pro.getEmptyHHeaderSql());
			}
		}
		return sbHeaderFields.substring(1);
	}

	public static String getHeaderFieldsAlias(List lstV, IFieldProvider thisPro) {
		StringBuffer sbHeaderFields = new StringBuffer();
		for (Iterator itr = lstV.iterator(); itr.hasNext();) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			if (thisPro == pro) {
				sbHeaderFields.append(",").append(pro.getHHeaderSqlAlias());
			} else {
				sbHeaderFields.append(",").append(pro.getEmptyHHeaderSql());
			}
		}
		return sbHeaderFields.substring(1);
	}

	public static String getGroupFields(List lstV) {
		StringBuffer sbGroupFields = new StringBuffer();
		for (Iterator itr = lstV.iterator(); itr.hasNext();) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			sbGroupFields.append(",").append(pro.getGroupBySql());
		}
		return sbGroupFields.substring(1);
	}

	public static String getGroupFields(List lstV, IFieldProvider thisPro,
			String codeField, String lvl) {
		StringBuffer sbGroupFields = new StringBuffer();
		for (Iterator itr = lstV.iterator(); itr.hasNext();) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			if (thisPro == pro) {
				sbGroupFields.append(",").append("substr(" + codeField + ",0,")
						.append(lvl.length()).append(")");
			} else {
				sbGroupFields.append(",").append(pro.getGroupBySql());
			}
		}
		return sbGroupFields.substring(1);
	}

	/**
	 * Gen xh total. 总计行的序号
	 * 
	 * @param lstV the lst v
	 * 
	 * @return the string
	 */
	public static String genXHTotal(List lstV) {
		StringBuffer sbGroupFields = new StringBuffer();
		int idx = 0;
		for (Iterator itr = lstV.iterator(); itr.hasNext(); idx++) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			sbGroupFields.append("||substr('000000000000000000000000',0,"
					+ pro.getCurrLvlLength() + ")");

		}
		return sbGroupFields.substring(2);
	}

	/**
	 * 单层字段的序号
	 * 
	 * @param lstV
	 * @param thisPro
	 * @return
	 */
	public static String genXHSingle(List lstV, IFieldProvider thisPro) {
		StringBuffer sbGroupFields = new StringBuffer();
		boolean afterNow = false;
		int idx = 0;
		for (Iterator itr = lstV.iterator(); itr.hasNext(); idx++) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			if (thisPro == pro) {
				sbGroupFields.append("||substr(").append(pro.getOrderBySql())
						.append(
								"||'000000000000000000000000',0,"
										+ pro.getCurrLvlLength() + ")");
				afterNow = true;
			} else {
				if (afterNow) {
					sbGroupFields
							.append("||substr('000000000000000000000000',0,"
									+ pro.getCurrLvlLength() + ")");
				} else {
					sbGroupFields.append("||substr(").append(
							pro.getOrderBySql()).append(
							"||'000000000000000000000000',0,"
									+ pro.getCurrLvlLength() + ")");
				}
			}
		}
		return sbGroupFields.substring(2);
	}

	/**
	 * 多层字段的序号（非末级行）
	 * 
	 * @param lstV
	 * @param thisPro
	 * @param codeField
	 * @param lvl
	 * @param xh
	 * @return
	 */
	public static String genXH(List lstV, IFieldProvider thisPro,
			String codeField, String lvl, String xh) {
		StringBuffer sbGroupFields = new StringBuffer();
		boolean afterNow = false;
		int idx = 0;
		for (Iterator itr = lstV.iterator(); itr.hasNext(); idx++) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			if (thisPro == pro) {
				sbGroupFields.append("||substr(").append(
						"substr(" + codeField + ",0,").append(lvl.length())
						.append(")").append(
								"||'000000000000000000000000',0,"
										+ pro.getCurrLvlLength() + ")");
				afterNow = true;
			} else {
				if (afterNow) {
					sbGroupFields
							.append("||substr('000000000000000000000000',0,"
									+ pro.getCurrLvlLength() + ")");
				} else {
					sbGroupFields.append("||substr(").append(
							pro.getOrderBySql()).append(
							"||'000000000000000000000000',0,"
									+ pro.getCurrLvlLength() + ")");
				}
			}
		}
		return sbGroupFields.substring(2);
	}

	/**
	 * 多层字段的序号（末级行）
	 * 
	 * @param lstV
	 * @param thisPro
	 * @param codeField
	 * @param lvl
	 * @param xh
	 * @return
	 */
	public static String genLastXH(List lstV, IFieldProvider thisPro,
			String codeField, String lvl, String xh) {
		StringBuffer sbGroupFields = new StringBuffer();
		boolean afterNow = false;
		int idx = 0;
		for (Iterator itr = lstV.iterator(); itr.hasNext(); idx++) {
			IFieldProvider pro = (IFieldProvider) itr.next();
			if (thisPro == pro) {
				sbGroupFields.append("||substr(").append(
						"substr(" + codeField + ",0,").append(lvl.length())
						.append(")").append(
								"||'000000000000000000000000',0,"
										+ pro.getCurrLvlLength() + ")");
				afterNow = true;
			} else {
				if (afterNow) {
					sbGroupFields
							.append("||substr('000000000000000000000000',0,"
									+ pro.getCurrLvlLength() + ")");
				} else {
					if (pro.getCurrLvlLength() > 0) {
						sbGroupFields.append("||substr(substr(").append(
								pro.getOrderBySql()).append(",0,").append(
								pro.getCurrLvlLength()).append(
								")||'000000000000000000000000',0,"
										+ pro.getCurrLvlLength() + ")");
					} else {
						sbGroupFields.append("||substr(").append(
								pro.getOrderBySql()).append(
								"||'000000000000000000000000',0,"
										+ pro.getCurrLvlLength() + ")");
					}
				}
			}
		}
		return sbGroupFields.substring(2);
	}

	/**
	 * 默认实现，适用单一层次的元素
	 */
	public String getSqlGroup(String fields, String sumFields,
			String tableName, List lstV, int idx, int topCount) {
		StringBuffer sbSql = new StringBuffer();
		if (idx == 0) {
			sbSql.append("select ").append(getTotalHeaderFields(lstV)).append(
					",").append(sumFields).append(",").append(genXHTotal(lstV))
					.append(" as XH from ").append(tableName).append(
							getFilter()).append(" and ").append(getFilterSql())
					.append(" group by ").append(getGroupFields(lstV)).append(" union all ");
		}
		// String strXH = ("000" + idx);
		// strXH = strXH.substring(strXH.length() - 3);
		sbSql.append("select ").append(getHeaderFields(lstV, this)).append(",")
				.append(sumFields).append(",").append(genXHSingle(lstV, this))
				.append(" as XH from ").append(tableName).append(getFilter())
				.append(" and ").append(getFilterSql()).append(" group by ")
				.append(getGroupFields(lstV));
		return sbSql.toString();
	}

	/**
	 * 生成分组的语句，并分组的上级更新名称字段
	 * 
	 * @param lvlPro
	 *            　级次提供器
	 * @param fields
	 *            TODO
	 * @param sumFields
	 *            　汇总的字段
	 * @param asisTableName
	 *            　名称字段的辅助表
	 * @param connectField
	 *            　两个表的关联字段
	 * @param lstV
	 *            TODO
	 * @param fieldProvider
	 *            TODO
	 * @param curLvl
	 *            　当前的级次
	 * @return
	 * @throws Exception
	 */
//	public static String genGroupSql(LevelProvider lvlPro, String modelCode,
//			String codeField, String fields, String sumFields,
//			String asisTableName, String connectField, String nameField,
//			int idx, List lstV, IFieldProvider fieldProvider) throws Exception {
//
//		// String strXH = ("000" + idx);
//		// strXH = strXH.substring(strXH.length() - 3);
//
//		lvlPro.setSCurrentCode(modelCode);
//
//		StringBuffer sb = new StringBuffer();
//		
//		String strXH0;
//		if (fieldProvider.isGroupUp())
//			while (true) {
//				String lvl = lvlPro.getParentCode();
//				if (Common.isNullStr(lvl))
//					break;
//				int iLvl = lvlPro.getRule().levelOf(lvl);
//				// 还要去除CODE是空的数据
//				String xh = BaseUtil.stufCharPre("" + iLvl, 3, '0');
//				strXH0 = genXH(lstV, fieldProvider, codeField, lvl, xh);
//				sb.append(" select substr(" + codeField + ",0,").append(
//						lvl.length()).append(") as ").append(codeField).append(
//						",").append(strXH0).append(" as xh,").append(sumFields)
//						.append(" from ").append(IRepDisplay.TEMP_TABLE)
//						.append(getFilter()).append(
//								" and length(" + codeField + ")>").append(
//								lvl.length()).append(" and ").append(codeField)
//						.append("  is not null ").append(" group by ").append(
//								getGroupFields(lstV, fieldProvider, codeField,
//										lvl)).append(" union all ");
//			}
//
//		// // 添加未级
//		// sb.append(
//		// "select " + codeField + "," + genXH(lstV, fieldProvider)
//		// + " as xh,").append(sumFields).append(" from ").append(
//		// IRepDisplay.TEMP_TABLE).append(" where ").append(codeField)
//		// .append("  is not null ").append(
//		// " group by " + getGroupFields(lstV) + "");
//
//		// 添加未级
//		if (!Common.isNullStr(modelCode)) {
//			int iLvl = lvlPro.getRule().levelOf(modelCode);
//			// 还要去除CODE是空的数据
//			String xh = BaseUtil.stufCharPre("" + iLvl, 3, '0');
//			strXH0 = genLastXH(lstV, fieldProvider, codeField, modelCode, xh);
//			sb.append(" select substr(" + codeField + ",0,").append(
//					modelCode.length()).append(") as ").append(codeField)
//					.append(",").append(strXH0).append(" as xh,").append(
//							sumFields).append(" from ").append(
//							IRepDisplay.TEMP_TABLE).append(getFilter()).append(
//							" and ").append(codeField).append("  is not null ")
//					.append(" group by ").append(
//							getGroupFields(lstV, fieldProvider, codeField,
//									modelCode));
//		}
//
//		// 最上面包一层　
//		String sSql = "select " + getHeaderFieldsAlias(lstV, fieldProvider)
//				+ "," + fields + ", XH from (" + sb.toString() + ") a ,"
//				+ asisTableName + " b where a." + codeField + "=b."
//				+ connectField;// + " order by a." + codeField + ",xh";
//
//		// 合计行
//		StringBuffer sbTotal = new StringBuffer();
//		if (idx == 0) {
//			String strXH = genXHTotal(lstV);
//			sbTotal.append(" union all select " + getTotalHeaderFields(lstV)
//				+ "," + fields + ", XH from (select substr(" + codeField + ",0,").append(
//					modelCode.length()).append(") as ").append(codeField)
//					.append(",").append(strXH).append(" as xh,").append(
//							sumFields).append(" from ").append(
//							IRepDisplay.TEMP_TABLE).append(getFilter()).append(" and ").append(
//							codeField).append("  is not null ").append(
//							" group by ").append(
//							getGroupFields(lstV, fieldProvider, codeField,
//									modelCode)).append(")");
//		}
//		
//		return sSql + sbTotal.toString();
//
//	}

	public String getHHeaderSqlAlias() {
		return null;
	}

	public boolean isGroupUp() {
		return false;
	}

	public String[][] getExtFields() {
		return null;
	}

	public boolean isOrderByMoney() {
		return false;
	}

	public boolean hasSpecifiedOrder() {
		return false;
	}
}
