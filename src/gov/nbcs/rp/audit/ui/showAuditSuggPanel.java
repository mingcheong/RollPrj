package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

public class showAuditSuggPanel extends FDialog {
	private static final long serialVersionUID = 1L;

	private FTextArea ta;

	private boolean isOK = false;

	private int operType = IPrjAudit.AuditNewInfo.OperTypeInfo.doNext;

	private StepSelectPanel aa;

	private int step;

	private PrjAuditDTO pd;

	private FPanel pnlBase;

	private PrjAuditAction pa;

//	private CustomTree treeUserElement;

	private FButton btnOK = null;

	public showAuditSuggPanel(int operType, PrjAuditDTO pd, PrjAuditAction pa,
			PrjAuditUI pau) {
		super(Global.mainFrame); // 指定窗体父亲
		this.operType = operType;
		this.pd = pd;
		this.pa = pa;
//		this.treeUserElement = pau.treeUserElement;
		this.setSize(600, 400); // 设置窗体大小
		this.setResizable(false); // 设置窗体大小是否可变
		this.getContentPane().add(getBasePanel());
		this.dispose(); // 窗体组件自动充满。
		this.setTitle("审核意见录入界面"); // 设置窗体标题
		this.setModal(true); // 设置窗体模态显示
	}

	private FPanel getBasePanel() {
		FToolBar toolbar = new FToolBar();
		FButton btnClose = new FButton("", "关闭");
		btnClose.setIcon("images/rp/close.gif");
		btnClose.setVerticalTextPosition(SwingConstants.BOTTOM);
		if (operType == IPrjAudit.AuditNewInfo.OperTypeInfo.doBack) {
			btnOK = new FButton("", "退回");
		} else {
			btnOK = new FButton("", "提交");
		}
		btnOK.setIcon("images/rp/check.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);
		FButton btnCancel = new FButton("", "取消");
		btnCancel.setIcon("images/rp/cancl.gif");
		btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);

//		FButton btnSave = new FButton("", "保存审核意见");
//		btnSave.setIcon("images/rp/save.gif");
//		btnSave.setVerticalTextPosition(SwingConstants.BOTTOM);

		pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(12);
		pnlBase.setLayout(lay);

		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ibs = getBackStep();
				if ((operType == IPrjAudit.AuditNewInfo.OperTypeInfo.doBack)
						&& (ibs == -1)) {
					JOptionPane.showMessageDialog(pnlBase, "请选择要退回的位置");
					return;
				}
//				if (Common.isNullStr(Common.nonNullStr(ta.getValue()))) {
//					if (JOptionPane
//							.showConfirmDialog(Global.mainFrame, "是否确定不写退回原因",
//									"确认填写退回原因", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
//						return;
//					}
//				}
				List list = new ArrayList();
				StringBuffer sqlBC = new StringBuffer();
				int maxStep = pd.getMaxStep();
				int stepCode = 0;
				StringBuffer sql1 = new StringBuffer();
				sql1.append("select case when audit_state > " + maxStep);
				sql1.append(" then " + maxStep + " else audit_state");
				sql1.append(" end step_id from rp_audit_cur ");
				sql1.append("where "
						+ pd.getArraySqlFilter(
								IPrjAudit.PrjAuditTable.prj_code, pd
										.getAuditPrjCodes()));
				sql1.append(" and set_year = " + Global.loginYear);
				sql1.append(" and rg_code = " + Global.getCurrRegion());
				try {
					stepCode = DBSqlExec.client()
							.getIntValue(sql1.toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (operType == IPrjAudit.AuditNewInfo.OperTypeInfo.doBack) {
					// 如果是退回，则修改当前审核状态，把审核状态递交到
//					if (ibs < 1) {
//						ibs = -1;
//					}
//					ibs = ibs+1 ;
					list.add("update "
							+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR
							+ " set "
							+ IPrjAudit.PrjAuditTable.is_back
							+ "=1,"
							+ IPrjAudit.PrjAuditTable.audit_state
							+ "='"
							+ ibs
							+ "',"
							+ IPrjAudit.PrjAuditTable.AUDIT_ISEND
							+ "=0,"
							+ IPrjAudit.PrjAuditTable.audit_text
							+ "='"
//							+ ta.getValue()
							+ " "
							+ "' "
							+ " where "
							+ pd.getArraySqlFilter(
									IPrjAudit.PrjAuditTable.prj_code, pd
											.getAuditPrjCodes())
							+ " and set_year = " + Global.loginYear
							+ " and rg_code = " + Global.getCurrRegion());
					sqlBC.append("insert into "
							+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_HIS);
					sqlBC.append("(ROW_ID, DIV_CODE, DIV_NAME, PRJ_CODE,");
					sqlBC
							.append("PRJ_NAME, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,audit_isend,");
					sqlBC
							.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,audit_text,rg_code)");
					sqlBC.append("select newid,div_code,div_name,xmbm,xmmc,");
					sqlBC.append("'" + GlobalEx.user_code + "','"
							+ GlobalEx.getUserName() + "'," + stepCode
							+ ",1,1,");
					sqlBC.append("'" + Tools.getCurrDate() + "',");
//					sqlBC.append(Global.loginYear + ",en_id,'" + ta.getValue()
					sqlBC.append(Global.loginYear + ",en_id,'" + " "
							+ "'," + Global.getCurrRegion());
					sqlBC.append(" from rp_xmjl");
					sqlBC.append(" where "
							+ pd.getArraySqlFilter("xmbm", pd
									.getAuditPrjCodes()));
					sqlBC.append(" and set_year = " + Global.loginYear);
					sqlBC.append(" and rg_code = " + Global.getCurrRegion());
					list.add(sqlBC.toString());
				} else {
					if (stepCode == 0) {
						stepCode++;
					}
					if (stepCode != maxStep) {
						list.add("update "
								+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR
								+ " set "
								+ IPrjAudit.PrjAuditTable.is_back
								+ "=0,"
								+ IPrjAudit.PrjAuditTable.audit_state
								+ "='"
								+ (stepCode + 1)
								+ "',"
								+ IPrjAudit.PrjAuditTable.AUDIT_ISEND
								+ "=0,"
								+ IPrjAudit.PrjAuditTable.audit_text
								+ "='"
//								+ ta.getValue()
								+ " "
								+ "', "
								+ IPrjAudit.PrjAuditTable.audit_userid
								+ " ='"
								+ GlobalEx.user_code
								+ "',"
								+ IPrjAudit.PrjAuditTable.audit_username
								+ "='"
								+ GlobalEx.getUserName()
								+ "'"
								+ " where "
								+ pd.getArraySqlFilter(
										IPrjAudit.PrjAuditTable.prj_code, pd
												.getAuditPrjCodes())
								+ " and set_year = " + Global.loginYear
								+ " and rg_code = " + Global.getCurrRegion());
					} else {
						list.add("update "
								+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR
								+ " set "
								+ IPrjAudit.PrjAuditTable.is_back
								+ "=0,"
								+ IPrjAudit.PrjAuditTable.audit_state
								+ "='"
								+ (stepCode +1)
								+ "',"
								+ IPrjAudit.PrjAuditTable.AUDIT_ISEND
								+ "=1,"
								+ IPrjAudit.PrjAuditTable.audit_text
								+ "='"
//								+ ta.getValue()
								+ " "
								+ "', "
								+ IPrjAudit.PrjAuditTable.audit_userid
								+ " ='"
								+ GlobalEx.user_code
								+ "',"
								+ IPrjAudit.PrjAuditTable.audit_username
								+ "='"
								+ GlobalEx.getUserName()
								+ "'"
								+ " where "
								+ pd.getArraySqlFilter(
										IPrjAudit.PrjAuditTable.prj_code, pd
												.getAuditPrjCodes())
								+ " and set_year = " + Global.loginYear
								+ " and rg_code = " + Global.getCurrRegion());
					}
					
//					StringBuffer sqlDH = new StringBuffer();
//					sqlDH.append("delete from ");
//					sqlDH.append(IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_HIS);
//					sqlDH.append(" where "
//							+ pd.getArraySqlFilter(
//									IPrjAudit.PrjAuditTable.prj_code, pd
//											.getAuditPrjCodes()));
//					sqlDH.append(" and set_year = " + Global.loginYear);
//					sqlDH.append(" and rg_code = " + Global.getCurrRegion());
//					sqlDH.append(" and batch_no = ");
//					try {
//						sqlDH.append(PubInterfaceStub.getMethod().getCurState(Global.loginYear, Global.getCurrRegion()));
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//					list.add(sqlDH.toString());
					
					sqlBC.append("insert into "
							+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_HIS);
					sqlBC.append("(ROW_ID, DIV_CODE, DIV_NAME, PRJ_CODE,");
					sqlBC
							.append("PRJ_NAME, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,audit_isend,");
					sqlBC
							.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,audit_text,rg_code)");
					sqlBC.append("select newid,div_code,div_name,xmbm,xmmc,");
					sqlBC.append("'" + GlobalEx.user_code + "','"
							+ GlobalEx.getUserName() + "'," + stepCode
							+ ",1,0,");
					sqlBC.append("'" + Tools.getCurrDate() + "',");
//					sqlBC.append(Global.loginYear + ",en_id,'" + ta.getValue()
					sqlBC.append(Global.loginYear + ",en_id,'" + " "
							+ "'," + Global.getCurrRegion());
					sqlBC.append(" from rp_xmjl");
					sqlBC.append(" where "
							+ pd.getArraySqlFilter("xmbm", pd
									.getAuditPrjCodes()));
					sqlBC.append(" and set_year = " + Global.loginYear);
					sqlBC.append(" and rg_code = " + Global.getCurrRegion());
					list.add(sqlBC.toString());
				}
				try {
					QueryStub.getClientQueryTool().executeBatch(list);
				} catch (Exception e2) {
					ErrorInfo.showErrorDialog(e2, "审核失败");
					isOK = false;
					return;
				}
				JOptionPane.showMessageDialog(Global.mainFrame, "审核成功");
				isOK = true;
				setVisible(false);
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isOK = false;
				setVisible(false);
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isOK = false;
				setVisible(false);
			}
		});
//		btnSave.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					if (ta != null) {
//						String filter;
//						StringBuffer sql = new StringBuffer();
//						for (int i = 0; i < pd.getAuditPrjCodes().size(); i++) {
//							filter = IPrjAudit.PrjAuditTable.prj_code + "='"
//									+ pd.getAuditPrjCodes().get(i) + "' and "
//									+ IPrjAudit.PrjAuditTable.set_year + "="
//									+ Global.loginYear + " and "
//									+ IPrjAudit.PrjAuditTable.rg_code + "="
//									+ GlobalEx.getCurrRegion();
//							if (DBSqlExec
//									.client()
//									.getRecordCount(
//											IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR,
//											filter) <= 0) {
//								// 如果没有审核记录
//								int maxStep = pd.getMaxStep();
//								StringBuffer sql1 = new StringBuffer();
//								sql1.append("select case when audit_state > "
//										+ maxStep);
//								sql1.append(" then " + maxStep
//										+ " else audit_state");
//								sql1.append(" end step_id from rp_audit_cur ");
//								sql1
//										.append("where "
//												+ pd
//														.getArraySqlFilter(
//																IPrjAudit.PrjAuditTable.prj_code,
//																pd
//																		.getAuditPrjCodes()));
//								sql1.append(" and set_year = "
//										+ Global.loginYear);
//								sql1.append(" and rg_code = "
//										+ Global.getCurrRegion());
//								int stepID = 0;
//								try {
//									stepID = DBSqlExec.client().getIntValue(
//											sql1.toString());
//								} catch (Exception e1) {
//									e1.printStackTrace();
//								}
//								sql
//										.append("insert into "
//												+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
//								sql
//										.append("(AUDIT_TEXT,ROW_ID, DIV_CODE, DIV_NAME, PRJ_CODE,");
//								sql
//										.append("PRJ_NAME, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,");
//								sql
//										.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,AUDIT_ISEND)");
//								sql
//										.append("select '"
//												+ ta.getValue()
//												+ "',newid,div_code,div_name,prj_code,prj_name,");
//								sql.append("'" + GlobalEx.user_code + "','"
//										+ GlobalEx.getUserName() + "',"
//										+ stepID + ",0,");
//								sql.append("'" + Tools.getCurrDate() + "',");
//								sql.append(Global.loginYear + ",en_id,0");
//								sql.append(" from rp_xmjl");
//								sql
//										.append(" where "
//												+ pd
//														.getArraySqlFilter(
//																IPrjAudit.PrjAuditTable.prj_code,
//																pd
//																		.getAuditPrjCodes()));
//								sql.append(" and set_year = "
//										+ Global.loginYear);
//								sql.append(" and rg_code = "
//										+ Global.getCurrRegion());
//							} else {
//								// 如果有审核记录
//								sql
//										.append("update "
//												+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
//								sql.append(" set "
//										+ IPrjAudit.PrjAuditTable.audit_text
//										+ "='");
//								sql.append(ta.getValue() + "', ");
//								sql.append(IPrjAudit.PrjAuditTable.audit_time
//										+ "='");
//								sql
//										.append(Tools.getCurrDate()
//												+ "', "
//												+ IPrjAudit.PrjAuditTable.audit_userid
//												+ " ='"
//												+ GlobalEx.user_code
//												+ "',"
//												+ IPrjAudit.PrjAuditTable.audit_username
//												+ "='" + GlobalEx.getUserName()
//												+ "'");
//								sql.append(" where " + filter);
//							}
//						}
//						QueryStub.getClientQueryTool().execute(sql.toString());
//						JOptionPane.showMessageDialog(Global.mainFrame, "保存成功");
//					}
//				} catch (Exception ee) {
//					ErrorInfo.showErrorDialog(ee, "保存审核意见信息出错");
//				}
//				isOK = false;
//			}
//		});
		toolbar.addControl(btnClose);
//		toolbar.addControl(btnSave);
		toolbar.addControl(btnOK);
		toolbar.addControl(btnCancel);
		pnlBase.addControl(toolbar, new TableConstraints(1, 12, true, true));
//		pnlBase.addControl(getSelPanel(), new TableConstraints(4, 12, true,true));
		// pnlBase.addControl(getNStatePanel(), new TableConstraints(4, 12,
		// true,
		// true));
//		pnlBase.addControl(getSugPanel(), new TableConstraints(14, 12, true,
//				true));
		return pnlBase;
	}

	/**
	 * 获取审核步骤显示面板
	 * 
	 * @return
	 */
	private FPanel getSelPanel() {
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(12);
		pnl.setLayout(lay);
		DataSet ds = null;
//		try {
//			ds = pd.getAuditCurStateDataSet(Common.nonNullStr(pd
//					.getAuditPrjCodes().get(0)));
//			ds.beforeFirst();
//			ds.next();
//		} catch (Exception e) {
//			ErrorInfo.showErrorDialog(e, "获取项目当前审核信息出错");
//		}
		aa = new StepSelectPanel(step, pd.getAuditStep(), ds.getOriginData(),
				operType == IPrjAudit.AuditNewInfo.OperTypeInfo.doBack);
		pnl.addControl(aa, new TableConstraints(10, 12, true, true));
		return pnl;
	}

	public int getBackStep() {
		return aa.getSelectBackNode();
	}

	/**
	 * 获取审核意见面板
	 * 
	 * @return
	 */
	private FPanel getSugPanel() {
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(12);
		pnl.setLayout(lay);
		ta = new FTextArea("");
		ta.setProportion(0f);
		FLabel label = new FLabel();
//		label.setText("请填写审核意见");
		pnl.addControl(label, new TableConstraints(1, 10, true, true));
//		try {
//			ta.setValue(pa.getAuditContext(pd.getAuditPrjCodes(), pd
//					.getUserCode()));
//		} catch (Exception ee) {
//			ErrorInfo.showErrorDialog(ee, "获取审核意见失败");
//		}
		pnl.addControl(new FLabel(), new TableConstraints(1, 2, true, true));
//		pnl.addControl(ta, new TableConstraints(5, 12, true, true));
		pnl.addControl(new PrjAuditAffix(pa, pd, true, pd.getAuditPrjCodes()),
				new TableConstraints(5, 12, true, true));
		return pnl;
	}

	/**
	 * 获取录入信息
	 * 
	 * @return
	 */
	public Object getInputData() {
		return ta.getValue();
	}

	/** 面板关闭时是否取消 */
	public boolean getIsOK() {
		return isOK;
	}

	/**
	 * 获取保存的审核意见
	 * 
	 * @return
	 * @throws Exception
	 */
	// private String getAuditContext(String aAuditUser, String aPrjCode)
	// throws Exception {
	// // 分为是否是负责人，如果是负责人，则保存到外部审核流程
	// // 如果不是负责人，只是审核人，则保存到内部审核流程
	// return pa.getAuditContext(aPrjCode, pd.getWid(), aAuditUser);
	// }
	/**
	 * 获取内部审核流程
	 * 
	 * @return
	 * @throws Exception
	 */
//	private FPanel getNStatePanel() {
//		FPanel pnl = new FPanel();
//		RowPreferedLayout lay = new RowPreferedLayout(15);
//		pnl.setLayout(lay);
//		FTextArea taB = new FTextArea("");
//		taB.setProportion(0f);
//		FLabel labelB = new FLabel();
//		labelB.setText("批办人");
//		taB.setEnabled(false);
//		StringBuffer sql = new StringBuffer();
//		sql
//				.append("select b.USER_NAME from fb_p_appr_audit_nstate a,vw_fb_user b");
//		sql.append(" where a.pbr = b.USER_CODE and a.nid = '"
//				+ pd.getUserCode() + "' and prj_code");
//		sql.append("='" + pd.getPrjCode() + "' ");
//		sql.append(" and div_code='" + pd.getDivCode() + "' ");
//		String pbr = "";
//		try {
//			pbr = DBSqlExec.client().getStringValue(sql.toString());
//		} catch (Exception ee) {
//			JOptionPane.showMessageDialog(Global.mainFrame, "加载内部审核流程失败");
//			return pnl;
//		}
//		if (!Common.isNullStr(pbr)) {
//			String[] ap = pbr.split(",");
//			for (int i = 0; i < ap.length; i++) {
//				if (Common.isNullStr(ap[i]))
//					continue;
//				taB.setValue(Common.nonNullStr(taB.getValue())
//						+ ((i == 0) ? "" : "\n") + ap[i]);
//			}
//		}
//		FTextArea taC = new FTextArea("");
//		taC.setProportion(0f);
//		FLabel labelC = new FLabel();
//		labelC.setText("当前阅办人");
//		// FButton btnC = new FButton("c","选择");
//		taC.setEnabled(false);
//		taC.setValue(Global.getUserName());
//
//		final FTextArea taA = new FTextArea("");
//		taA.setProportion(0f);
//		FLabel labelA = new FLabel();
//		labelA.setText("待批办人");
//		FButton btnA = new FButton("c", "选择");
//		taA.setEnabled(false);
//		setDSHContent(taA);
//		pnl.addControl(labelB, new TableConstraints(1, 5, true, true));
//		// pnl.addControl(btnB, new TableConstraints(1, 2, true, true));
//		// pnl.addControl(new FLabel(), new TableConstraints(1, 1, true, true));
//		pnl.addControl(labelC, new TableConstraints(1, 5, true, true));
//		// pnl.addControl(btnC, new TableConstraints(1, 2, true, true));
//		// pnl.addControl(new FLabel(), new TableConstraints(1, 1, true, true));
//		pnl.addControl(labelA, new TableConstraints(1, 2, true, true));
//		pnl.addControl(btnA, new TableConstraints(1, 2, true, true));
//		pnl.addControl(new FLabel(), new TableConstraints(1, 1, true, true));
//
//		pnl.addControl(taB, new TableConstraints(4, 4, true, true));
//		pnl.addControl(new FLabel(), new TableConstraints(1, 1, true, true));
//		pnl.addControl(taC, new TableConstraints(4, 4, true, true));
//		pnl.addControl(new FLabel(), new TableConstraints(1, 1, true, true));
//		pnl.addControl(taA, new TableConstraints(4, 4, true, true));
//		pnl.addControl(new FLabel(), new TableConstraints(1, 1, true, true));
//		pnl.setTitle("批办人信息");
//		// btnA.addActionListener(new ActionListener() {
//		// public void actionPerformed(ActionEvent e) {
//		// String[] prjCodes = new String[] { pd.getPrjCode() };
//		// for (int i = 0; i < prjCodes.length; i++) {
//		// if (pd.getAuditCanEdit(pd.getDivCode(), prjCodes[0]) == 0) {
//		// JOptionPane.showMessageDialog(Global.mainFrame,
//		// "您没有该项目现状态的批办权限");
//		// return;
//		// }
//		// }
//		// ElementTree et = new ElementTree(getUserCodesBySend());
//		// Tools.centerWindow(et);
//		// et.setVisible(true);
//		// InfoPackage info = new InfoPackage();
//		// if (!et.isCancel) {
//		// String[] userCodes = pa.getSelElement(et.getTree(), true);
//		// String[] userNames = pa.getSelElementContent(et.getTree());
//		// for (int i = 0; i < userCodes.length; i++) {
//		// if (pd.getFzrBM().indexOf(userCodes[i]) >= 0) {
//		// JOptionPane.showMessageDialog(pnlBase, "不允许批办给负责人");
//		// return;
//		// }
//		// }
//		// try {
//		// info = PrjAuditStub.getMethod().sentPrjToUser(
//		// Global.user_code, prjCodes, userCodes,
//		// pa.getSelElement(et.getTree(), false),
//		// userNames, Tools.getCurrDate(),
//		// Global.loginYear, pd.getWid(),
//		// String.valueOf(pd.getBathcNo()));
//		// if (!info.getSuccess()) {
//		// JOptionPane.showMessageDialog(Global.mainFrame,
//		// info.getsMessage());
//		// return;
//		// }
//		// setDSHContent(taA);
//		// if (pd.isFZR())
//		// btnOK.setEnabled(pd.isNIsAllEnd());
//		// else {
//		// if (operType == IPrjAudit.AuditNewInfo.OperTypeInfo.doBack)
//		// btnOK.setEnabled(false);
//		// }
//		// } catch (Exception ee) {
//		// ErrorInfo.showErrorDialog(ee, info.getsMessage());
//		// }
//		// }
//		// }
//		// });
//		return pnl;
//	}

	public String[] getUserCodesBySend() {
		String[] userCodes = null;
		StringBuffer sqld = new StringBuffer();
		sqld.append("select nid from fb_p_appr_audit_nstate");
		sqld.append(" where instr(pbr,'" + pd.getUserCode()
				+ "')>=1 and prj_code");
		sqld.append("='" + pd.getPrjCode() + "' ");
		sqld.append(" and div_code='" + pd.getDivCode() + "' ");
		List lA = QueryStub.getClientQueryTool().findBySql(sqld.toString());
		userCodes = new String[lA.size()];
		for (int i = 0; i < lA.size(); i++) {
			Map map = (Map) lA.get(i);
			userCodes[i] = Common.nonNullStr(map.get("nid"));
		}
		return userCodes;
	}

//	private void setDSHContent(FTextArea taA) {
//		try {
//			StringBuffer sqld = new StringBuffer();
//			sqld
//					.append("select nname||'  '||(case when isend=1 then '审核完成' else '未审核' end ) as nname from fb_p_appr_audit_nstate");
//			sqld.append(" where instr(pbr,'" + pd.getUserCode()
//					+ "')>=1 and prj_code");
//			sqld.append("='" + pd.getPrjCode() + "' ");
//			sqld.append(" and div_code='" + pd.getDivCode() + "' ");
//			List lA = QueryStub.getClientQueryTool().findBySql(sqld.toString());
//			// if (!pd.isWEnd())
//			// taA.setValue(Common.nonNullStr(taA.getValue()) + pd.getWName()
//			// + " 负责人");
//			taA.setValue("");
//			for (int i = 0; i < lA.size(); i++) {
//				Map map = (Map) lA.get(i);
//				taA
//						.setValue(Common.nonNullStr(taA.getValue())
//								+ (Common.isNullStr(Common.nonNullStr(taA
//										.getValue())) ? "" : "\n")
//								+ map.get("nname"));
//			}
//		} catch (Exception ee) {
//			ErrorInfo.showErrorDialog(ee, "设置待批办人信息出错");
//		}
//	}

//	private class ElementTree extends FDialog {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		private String[] str;
//
//		private boolean isCancel = false;
//
//		private FPanel pnlBased;
//
//		public ElementTree(String[] str) {
//			super(Global.mainFrame); // 指定窗体父亲
//			this.setResizable(false); // 设置窗体大小是否可变
//			this.str = str;
//			this.setSize(400, 500); // 设置窗体大小
//			this.getContentPane().add(getBasePanel());
//			this.dispose(); // 窗体组件自动充满。
//			this.setTitle("处室人员选择界面"); // 设置窗体标题
//			this.setModal(true); // 设置窗体模态显示
//		}
//
//		private FPanel getBasePanel() {
//			pnlBased = new FPanel();
//			RowPreferedLayout layBase = new RowPreferedLayout(1);
//			pnlBased.setLayout(layBase);
//			FPanel pnlUnder = new FPanel(); // 下面放按钮的面板
//			RowPreferedLayout layUnder = new RowPreferedLayout(10);
//			layUnder.setColumnWidth(60);
//			layUnder.setRowGap(5);
//			pnlUnder.setLayout(layUnder);
//			FButton btnOK = new FButton();
//			btnOK.setText("确定");
//			FButton btnCancel = new FButton();
//			btnCancel.setText("取消");
//			pnlUnder.addControl(new FLabel(), new TableConstraints(1, 3, false,
//					true));
//			pnlUnder.addControl(btnOK, new TableConstraints(1, 2, false, true));
//			pnlUnder.addControl(btnCancel, new TableConstraints(1, 2, false,
//					true));
//			pnlUnder.addControl(new FLabel(), new TableConstraints(1, 2, false,
//					true));
//
//			FScrollPane pnlTree = new FScrollPane();
//			pnlTree.addControl(treeUserElement);
//			// 增加类档树
//			pnlBased.addControl(
//					new SearchPanel(treeUserElement, "code", "name"),
//					new TableConstraints(1, 1, false, true));
//			pnlBased.addControl(pnlTree, new TableConstraints(17, 1, false,
//					true));
//			pnlBased.addControl(pnlUnder, new TableConstraints(1, 1, false,
//					true));
//			// 取消按钮的操作时间
//			btnCancel.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					try {
//						isCancel = true;
//						setVisible(false);
//					} catch (Exception ek) {
//						ek.printStackTrace();
//					}
//				}
//			});
//			// 确定按钮的确定时间
//			btnOK.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					isCancel = false;
//					setVisible(false);
//				}
//			});
//			pnlBased.setTopInset(10);
//			pnlBased.setLeftInset(10);
//			pnlBased.setBottomInset(10);
//			pnlBased.setRightInset(10);
//			pa.setSelElement(treeUserElement, str);
//			return pnlBased;
//		}
//
//		public boolean isCancel() {
//			return this.isCancel;
//		}
//
//		public CustomTree getTree() {
//			return treeUserElement;
//		}
//	}
}