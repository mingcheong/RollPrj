package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.datactrl.DataSet;

import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

public class HistoryAuditPanel extends FPanel {

	/**
	 * 查询项目的审核轨迹
	 */
	private static final long serialVersionUID = 1L;

	private FTextArea mcontext;

	private FTextArea acontext;

	private String bName;

	private PrjAuditDTO pd;

	/**
	 * 构造函数
	 * @param branchID
	 *            分为1：主管处室 2：绩效处 3：预算处
	 */
	public HistoryAuditPanel(PrjAuditDTO pd, int branchCode) {
		this.pd = pd;
		RowPreferedLayout lay = new RowPreferedLayout(1);
		this.setLayout(lay);
		FLabel mlabel = new FLabel();
		mlabel.setText("审核的主要内容");
		mcontext = new FTextArea("");
		mcontext.setProportion(0f);
		mcontext.setEditable(false);
		FLabel alabel = new FLabel();
		alabel.setText("审核意见");
		acontext = new FTextArea("");
		acontext.setProportion(0f);
		acontext.setEditable(false);
		this.addControl(mlabel, new TableConstraints(1, 1, false, true));
		this.addControl(mcontext, new TableConstraints(1, 10, true, true));
		this.addControl(alabel, new TableConstraints(1, 1, true, true));
		this.addControl(acontext, new TableConstraints(1, 10, true, true));
		this.setLeftInset(10);
		this.setRightInset(10);
		this.setTopInset(10);
		this.setBottomInset(10);
	}

	public void doQuery(String aDivCode, String aPrjCode) {
		// TODO 查询
		try {
			StringBuffer sql = new StringBuffer();
			DataSet dsU = pd.getPrjUnitInfo();
			String wid2 = "1";
			if (dsU.locate("code", bName)) {
				wid2 = dsU.fieldByName("cvalue").getString();
			}
			// 这个地方要获取当前用户获取的步骤，好限制住内部人只能看到内部人的审核意见
//			String rightMax = pd.getMaxStepByUserCode();
			sql
					.append("select n.wname,m.auditcontext,m.last_ver from fb_p_appr_audit_wstate m,fb_p_appr_audit_wstep n");
			sql.append(" where m.wid = n.wid and m.wid !=0");
			sql.append(" and m.wid in (" + wid2 + ")");
			sql.append(" and prj_code ='" + pd.getPrjCode() + "'");
			sql.append(" union all ");
			sql
					.append(" select p.nname,p.auditcontext,p.last_ver from fb_p_appr_audit_nstate p,fb_p_appr_audit_wstep q");
			sql.append(" where q.wid = p.wid and q.wid !=0");
			sql.append(" and q.wid in (" + wid2 + ")");
			sql.append(" and prj_code ='" + pd.getPrjCode() + "'");
			sql.append(" and p.isend = 1");
//			sql.append(" and q.wid in (" + rightMax + ")");

			DataSet ds = DBSqlExec.client().getDataSet(sql.toString());
			StringBuffer value = new StringBuffer();
			if ((ds != null) && !ds.isEmpty()) {
				ds.beforeFirst();
				while (ds.next()) {
					value.append("审核意见：   ");
					value.append("\n");
					value.append(Common.isNullStr(ds
							.fieldByName("auditcontext").getString()) ? "无"
							: ds.fieldByName("auditcontext").getString());
					value.append("\n");
					value.append("\n");
					value.append("审核人：   "
							+ ds.fieldByName("wname").getString().trim()
							+ "         ");
					value.append("审核日期： "
							+ ds.fieldByName("last_ver").getString().trim());
					value.append("\n");
					value.append("-----------------------------------------------------------------------------");
					value.append("\n");
				}
				acontext.setValue(value);
			} else {
				acontext.setValue("无");
			}
			if("zg".equals(bName)) {
				pd.setZgText(value.toString());
			}
			if ("jx".equals(bName)) {
				pd.setJxText(value.toString());
			}
			if ("ys".equals(bName)) {
				pd.setYsText(value.toString());
			}
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "查询出错");
			acontext.setValue("无");
		}
	}
}
