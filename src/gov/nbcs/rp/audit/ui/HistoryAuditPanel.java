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
	 * ��ѯ��Ŀ����˹켣
	 */
	private static final long serialVersionUID = 1L;

	private FTextArea mcontext;

	private FTextArea acontext;

	private String bName;

	private PrjAuditDTO pd;

	/**
	 * ���캯��
	 * @param branchID
	 *            ��Ϊ1�����ܴ��� 2����Ч�� 3��Ԥ�㴦
	 */
	public HistoryAuditPanel(PrjAuditDTO pd, int branchCode) {
		this.pd = pd;
		RowPreferedLayout lay = new RowPreferedLayout(1);
		this.setLayout(lay);
		FLabel mlabel = new FLabel();
		mlabel.setText("��˵���Ҫ����");
		mcontext = new FTextArea("");
		mcontext.setProportion(0f);
		mcontext.setEditable(false);
		FLabel alabel = new FLabel();
		alabel.setText("������");
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
		// TODO ��ѯ
		try {
			StringBuffer sql = new StringBuffer();
			DataSet dsU = pd.getPrjUnitInfo();
			String wid2 = "1";
			if (dsU.locate("code", bName)) {
				wid2 = dsU.fieldByName("cvalue").getString();
			}
			// ����ط�Ҫ��ȡ��ǰ�û���ȡ�Ĳ��裬������ס�ڲ���ֻ�ܿ����ڲ��˵�������
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
					value.append("��������   ");
					value.append("\n");
					value.append(Common.isNullStr(ds
							.fieldByName("auditcontext").getString()) ? "��"
							: ds.fieldByName("auditcontext").getString());
					value.append("\n");
					value.append("\n");
					value.append("����ˣ�   "
							+ ds.fieldByName("wname").getString().trim()
							+ "         ");
					value.append("������ڣ� "
							+ ds.fieldByName("last_ver").getString().trim());
					value.append("\n");
					value.append("-----------------------------------------------------------------------------");
					value.append("\n");
				}
				acontext.setValue(value);
			} else {
				acontext.setValue("��");
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
			ErrorInfo.showErrorDialog(ee, "��ѯ����");
			acontext.setValue("��");
		}
	}
}
