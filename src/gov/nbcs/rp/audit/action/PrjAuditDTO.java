/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title ��Ŀ��Ч-������
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */

package gov.nbcs.rp.audit.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.audit.ibs.IPrjAudit;
// gov.nbcs.rp.audit.ui.PrjApprExpertSug;
// import gov.nbcs.rp.audit.ui.PrjApprInputMainPanel;
// import gov.nbcs.rp.audit.ui.PrjApprMainUI;
// import gov.nbcs.rp.audit.ui.PrjApprSpeedPanel;
// import gov.nbcs.rp.audit.ui.PrjApprStandardPanel;
// import gov.nbcs.rp.audit.ui.PrjApprTargetPanel;
// import gov.nbcs.rp.audit.ui.PrjGeneralPanel;
// import gov.nbcs.rp.audit.ui.PrjTablePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;

public class PrjAuditDTO {

	private static String loginDepCode = "";

	/** ��λ���ݼ� */
	private DataSet dsDiv;

	/** ��Ŀ�����ݼ� */
	private DataSet dsPrjTree;

	/** ��Ŀ��Ч������Ϣ���ݼ� */
	private DataSet dsPrjAppr;

	/** ��Ŀ�б����ݼ� */
	private DataSet dsPrj;

	/** ��Ŀ���� */
	private String sPrjCode;

	/** ��Ŀ�б�reportUI */
	private ReportUI reportUI;

	/** ��Ŀ�б��reportUI������ */
	private PropertyProvider pp;

	/** ��Ŀ�б��report */
	private Report report;

	/** ָ��reportUI */
	private ReportUI reportUIStd;

	/** ָ���reportUI������ */
	private PropertyProvider ppStd;

	/** ָ���report */
	private Report reportStd;

	/** ��Ŀ��reportUI */
	private ReportUI reportUIPrj;

	/** ��Ŀ����reportUI������ */
	private PropertyProvider ppPrj;

	/** ��Ŀ��report */
	private Report reportPrj;

	// /** ��Ŀ��Ч¼�������� */
	// private PrjApprInputMainPanel pip;
	//
	// /** ��Ŀ�ſ�¼����� */
	// private PrjGeneralPanel pgp;
	//
	// /** ��Ŀ��ЧĿ��¼����� */
	// private PrjApprTargetPanel ptp;
	//
	// /** ��Ч��������Ŀ�б���� */
	// private PrjTablePanel ptpp;
	//
	// /** ��Ŀ��Ч����ָ��¼����� */
	// private PrjApprStandardPanel psp;
	//
	// /** ר��������ģ�� */
	// private PrjApprExpertSug pes;
	//
	// /** ��Ŀ���ȡ���ǰ���������� */
	// private PrjApprSpeedPanel pspp;

	// private HistoryAuditPanel hazg;
	//
	// private HistoryAuditPanel hajx;
	//
	// private HistoryAuditPanel hays;

	/** �Ƿ���Ա༭ */
	private boolean isEdit = false;

	/** ��λ���� */
	private String divCode;

	/** �Ƿ��ǳ�ʼ״̬ */
	private boolean isInit = true;

	private DataSet dsAffix;

	// private PrjApprMainUI mainUI;

	private DataSet dsStd;

	private List lstAuditStep;

	private CustomTable auditPrjTable;

	private String userID;

	// ��������õ���һЩ��Ϣ
	// /** �Ƿ��Ǹ����� */
	// private boolean isFZR;
	//
	// /** �Ƿ�������� */
	// private boolean isSHR;

	// /** �ڲ�������̱��� */
	// private String nid;

	// /** ��˲������� */
	// private int auditType = 0;

	// /** �ⲿ������̱��� */
	// private String wid;
	//
	// /** �ⲿ������̱��� */
	// private String wName;
	//
	// /** �ⲿ�����Ƿ���� */
	// private boolean bWIsEnd = false;
	//
	// /** �ڲ������Ƿ�ȫ������ */
	// private boolean bNIsAllEnd = false;
	//
	// /** �ڲ������Ƿ���� */
	// private boolean bNisEnd;
	//
	// private String pbr;
	//
	// private String fzrBM;

	private String WidByUser;

	private String WidByUser_P;
	
	private String WidByUser_N;
	
	private int WidByUser_Num;

	private int batchNo = -1;
	private int dataType = -1;

	private List prjCodes;

	public int getBathcNo() {
		if (-1 == batchNo) {
			try {
				// batchNo = PubInterfaceStub.getMethod().getCurBatchNO();
			} catch (Exception ee) {
				batchNo = 2;
			}
		}
		return batchNo;
	}

	public int getDataType() {
		if (-1 == dataType) {
			dataType = GlobalEx.isFisVis() ? 1 : 0;
		}
		return dataType;
	}

	// public String getFzrBM() {
	// return fzrBM;
	// }
	//
	// public void setFzrBM(String fzrBM) {
	// this.fzrBM = fzrBM;
	// }
	//
	// public String getPbr() {
	// return pbr;
	// }
	//
	// public void setPbr(String pbr) {
	// this.pbr = pbr;
	// }

	public void setAuditPrjTable(CustomTable table) {
		this.auditPrjTable = table;
	}

	public CustomTable getAuditPrjTable() {
		return this.auditPrjTable;
	}

	/** ���������� */
	private int iMaxstep;

	public void setStandardData(DataSet ds) {
		this.dsAffix = ds;
	}

	public DataSet getStandardData() {
		return this.dsStd;
	}

	public void setAffixData(DataSet dsAffix) {
		this.dsAffix = dsAffix;
	}

	public DataSet getAffixData() {
		return this.dsAffix;
	}

	public void setisInit(boolean isInit) {
		this.isInit = isInit;
	}

	public boolean getisInit() {
		return this.isInit;
	}

	public void setisEdit(boolean isEditState) {
		this.isEdit = isEditState;
	}

	public boolean getisEdit() {
		return this.isEdit;
	}

	public void setDivCode(String divCode) {
		this.divCode = divCode;
	}

	public String getDivCode() {
		return this.divCode;
	}

	// public void setPrjApprSpeedPanel(PrjApprSpeedPanel pspp) {
	// this.pspp = pspp;
	// }
	//
	// public PrjApprSpeedPanel getPrjApprSpeedPanel() {
	// return this.pspp;
	// }
	//
	// public void setPrjTablePanel(PrjTablePanel ptpp) {
	// this.ptpp = ptpp;
	// }
	//
	// public PrjTablePanel getPrjTablePanel() {
	// return this.ptpp;
	// }
	//
	// public void setPrjApprStandardPanel(PrjApprStandardPanel psp) {
	// this.psp = psp;
	// }
	//
	// public PrjApprStandardPanel getPrjApprStandardPanel() {
	// return this.psp;
	// }
	//
	// public void setPrjApprExpertSug(PrjApprExpertSug pes) {
	// this.pes = pes;
	// }
	//
	// public PrjApprExpertSug getPrjApprExpertSug() {
	// return this.pes;
	// }
	//
	// public void setPrjApprTargetPanel(PrjApprTargetPanel ptp) {
	// this.ptp = ptp;
	// }
	//
	// public PrjApprTargetPanel getPrjApprTargetPanel() {
	// return this.ptp;
	// }
	//
	// public void setPrjInputMainPanel(PrjApprInputMainPanel pip) {
	// this.pip = pip;
	// }
	//
	// public PrjApprInputMainPanel getPrjInputMainPanel() {
	// return this.pip;
	// }
	//
	// public void setPrjGeneralPanel(PrjGeneralPanel pgp) {
	// this.pgp = pgp;
	// }
	//
	// public PrjGeneralPanel getPrjGeneralPanel() {
	// return this.pgp;
	// }

	public void setReportPP(PropertyProvider pp) {
		this.ppPrj = pp;
	}

	public PropertyProvider getReportPP() {
		return this.ppPrj;
	}

	public void setReport(Report aReport) {
		this.reportPrj = aReport;
	}

	public Report getReport() {
		return this.reportPrj;
	}

	public void setReportUI(ReportUI aReportUI) {
		this.reportUIPrj = aReportUI;
	}

	public ReportUI getReportUI() {
		return this.reportUIPrj;
	}

	public void setLReportPP(PropertyProvider pp) {
		this.pp = pp;
	}

	public PropertyProvider getLReportPP() {
		return this.pp;
	}

	public void setLReport(Report aReport) {
		this.report = aReport;
	}

	public Report getLReport() {
		return this.report;
	}

	public void setLReportUI(ReportUI aReportUI) {
		this.reportUI = aReportUI;
	}

	public ReportUI getLReportUI() {
		return this.reportUI;
	}

	public void setPrjCode(String aPrjCode) {
		this.sPrjCode = aPrjCode;
	}

	public String getPrjCode() {
		return this.sPrjCode;
	}

	public void setDivData(DataSet dsDiv) {
		this.dsDiv = dsDiv;
	}

	public DataSet getDivData() {
		return dsDiv;
	}

	public void setPrjTreeData(DataSet dsPrj) {
		this.dsPrjTree = dsPrj;
	}

	public DataSet getPrjTreeData() {
		return dsPrjTree;
	}

	public void setPrjApprData(DataSet dsPrjAppr) {
		this.dsPrjAppr = dsPrjAppr;
	}

	public DataSet getPrjApprData() {
		return dsPrjAppr;
	}

	public void setPrjData(DataSet dsPrj) {
		this.dsPrj = dsPrj;
	}

	public DataSet getPrjData() {
		return dsPrj;
	}

	public void setStdReportUI(ReportUI aReportUI) {
		this.reportUIStd = aReportUI;
	}

	public ReportUI getStdReportUI() {
		return this.reportUIStd;
	}

	public void setStdReportPP(PropertyProvider pp) {
		this.ppStd = pp;
	}

	public PropertyProvider getStdReportPP() {
		return this.ppStd;
	}

	public void setStdReport(Report aReport) {
		this.reportStd = aReport;
	}

	public Report getStdReport() {
		return this.reportStd;
	}

	/**
	 * �Ƿ�����Ч�����
	 * 
	 * @return 0:û�����Ȩ�ޡ� 1����λû������ 2����λ����λ���Ա༭�����������Ա༭ 9:�������
	 */
	public int getAuditCanEdit(String divCode, String prjCode) {
		try {
			String depCode = getDepCode();
			DataSet dsStep1 = getAuditStepDataSet(IPrjAudit.PrjAuditTable.PrjAuditSetTable.AUDIT_OPER
					+ " like '%" + Global.user_code + "%'");
			if (!((dsStep1 != null) && !dsStep1.isEmpty())) {
				dsStep1 = getAuditStepDataSet(IPrjAudit.PrjAuditTable.PrjAuditSetTable.AUDIT_OPER
						+ " like '%" + Common.nonNullStr(depCode) + "%'");
				if (!((dsStep1 != null) && !dsStep1.isEmpty())) {
					return 0;
				}
			}
			// �Ȳ�ѯ��ǰ���״̬�����Ϊ�գ������һ����ʼ��¼
			DataSet dsCurState = getAuditCurStateDataSet(prjCode);
			if ((dsCurState != null) && !dsCurState.isEmpty()) {
				dsCurState.beforeFirst();
				dsCurState.next();
				String stepID = dsCurState.fieldByName(
						IPrjAudit.PrjAuditTable.audit_state).getString();
				String isEnd = dsCurState.fieldByName(
						IPrjAudit.PrjAuditTable.AUDIT_ISEND).getString();
				
				String isUser = dsStep1.fieldByName(
						IPrjAudit.PrjAuditTable.PrjAuditSetTable.IS_USER)
						.getString();
				boolean auditIsEnd = "1".equals(isEnd);
				if (GlobalEx.isFisVis()) {
					// ����ǲ����û���¼
					if (auditIsEnd) {
						// ��ǰ����Ŀ�����ɻ����ڵ�λ��
						stepID = String.valueOf(Integer.parseInt(stepID) + 1);
					}
					if (stepID.equals(String.valueOf(getMaxStep()))){
						//��������һ�����
						stepID = String.valueOf(getMaxStep());
					}
				} else {
					// ����ǵ�λ�û���¼
					if ("0".equals(stepID) && !auditIsEnd) {
						// ��λδ��
						return 2;
					}
				}
				if (dsStep1.locate(
						IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_ID,
						stepID)) {
					setStepID(stepID);
					setStepName(dsStep1.fieldByName("STEP_NAME").getString());
					String auditOper = dsStep1
							.fieldByName(
									IPrjAudit.PrjAuditTable.PrjAuditSetTable.AUDIT_OPER)
							.getString();
					if ("1".equals(isUser)) {
						// ������Ԫ�����û�
						if (auditOper.indexOf(Global.user_code) >= 0) {
							return 9;
						} else {
							return 0;
						}
					} else {
						// ������Ԫ����ҵ����
						if (auditOper.indexOf(depCode) >= 0) {
							return 9;
						} else {
							return 0;
						}
					}
				} else {
					return 1;
				}
			} else {
				// ���û��������̣���λ����¼�룬����������¼�뼰���
				if (GlobalEx.isFisVis()) {
					return 2;
				} else {
					return 0;
				}
			}
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "��ȡ��˲������");
			return 0;
		}
	}

	public boolean getUserIsFZR(String wid) {
		try {
			DataSet dsStep = getAuditStepDataSet(Common.isNullStr(wid) ? ""
					: " wid in (" + wid + ")");
			if (dsStep.locate(IPrjAudit.AuditNewInfo.AudtiStep.WID, wid)) {
				String shr = dsStep.fieldByName(
						IPrjAudit.AuditNewInfo.AudtiStep.PRJFZR).getString();
				// �ж��Ƿ�������ˣ�������Ҫȷ��������Ǵ��һ�����
				return shr.indexOf(Global.user_code) >= 0;
			}
		} catch (Exception ee) {
			return false;
		}
		return false;
	}

	// public boolean isFZR() {
	// return isFZR;
	// }
	//
	// public void setFZR(boolean isFZR) {
	// this.isFZR = isFZR;
	// }
	//
	// public boolean isSHR() {
	// return isSHR;
	// }
	//
	// public void setSHR(boolean isSHR) {
	// this.isSHR = isSHR;
	// }
	//
	// public String getWid() {
	// return wid;
	// }

	/**
	 * ��ȡ��½�û�����������������ļ�����λ
	 * 
	 * @return
	 */
	public String getMaxStepByUserCode(boolean isPrior,boolean isNext) {
		
		String stepID = "isnull(step_id,0)";
		if(!isNext)
		{
		if (isPrior) {
			stepID = "isnull(step_id,0)-1";
			if (!Common.isNullStr(WidByUser_P)) {
				return WidByUser_P;
			}
		} else {
			if (!Common.isNullStr(WidByUser)) {
				return WidByUser;
			}
		}
	}else 
	{
		stepID = "isnull(step_id,0)+1";
	}
		
		WidByUser = "";
		WidByUser_P = "";
		WidByUser_N = "";

		try {
			String sql = "select " + stepID + " as name from rp_s_audit_step"
					+ " where instr(audit_oper,'" + getUserCode() + "')>=1"
					+ " or audit_oper like '" + getDepCode() + "%' ";
			DataSet ds = DBSqlExec.client().getDataSet(sql);
			ds.beforeFirst();
			int i = 0;
			while (ds.next()) {
				if(!isNext)
				{
				if (i == 0) {
					if (isPrior) {
						WidByUser_P = WidByUser_P
								+ ds.fieldByName("name").getString();
				
					} else {
						WidByUser = WidByUser
								+ ds.fieldByName("name").getString();
					}
				} else {
					if (isPrior) {
						WidByUser_P = WidByUser_P + ","
								+ ds.fieldByName("name").getString();
					} else {
						WidByUser = WidByUser + ","
								+ ds.fieldByName("name").getString();
					}
				}
				}
				else
				{
					if (i == 0) {
						WidByUser_N = WidByUser_N
						+ ds.fieldByName("name").getString();
						
					}
					else
					{
						WidByUser_N = WidByUser_N + ","
						+ ds.fieldByName("name").getString();
					}
					
				}
				i++;
			}
			if (isPrior&&!isNext) {
				return WidByUser_P;
			}
			else if(isNext)
			{
				return WidByUser_N;
			}
				
			else {
				return WidByUser;
			}
		} catch (Exception ee) {
			return "0";
		}
	}

	//
	// public void setWid(String wid) {
	// this.wid = wid;
	// }
	//
	// public void setAuditType(int auditType) {
	// this.auditType = auditType;
	// }
	//
	// public int getAuditType() {
	// return this.auditType;
	// }
	//
	// public String getNid() {
	// return nid;
	// }
	//
	// public void setNid(String nid) {
	// this.nid = nid;
	// }
	//
	// public boolean isWEnd() {
	// return bWIsEnd;
	// }
	//
	// public void setIsWEnd(boolean isEnd) {
	// bWIsEnd = isEnd;
	// }
	//
	// public boolean isNIsAllEnd() {
	// return bNIsAllEnd;
	// }
	//
	// public void setNIsAllEnd(boolean isEnd) {
	// bNIsAllEnd = isEnd;
	// }
	//
	// public boolean isNEnd() {
	// return bNisEnd;
	// }
	//
	// public void setIsNEnd(boolean nEnd) {
	// bNisEnd = nEnd;
	// }
	//
	// public String getWName() {
	// return wName;
	// }
	//
	// public void setWName(String name) {
	// wName = name;
	// }

	private DataSet dsStep;

	/**
	 * ��ȡ�������
	 * 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public DataSet getAuditStepDataSet(String filter) throws Exception {
		String sql = "select * from "
				+ IPrjAudit.PrjAuditTable.PrjAuditSetTable.TABLENAME_AUDIT_SET;
		if (!Common.isNullStr(filter)) {
			sql = sql + " where " + filter;
		}
		if (Common.isNullStr(filter)) {
			if (dsStep == null) {
				dsStep = DBSqlExec.client().getDataSet(sql);
			}
		} else {
			return DBSqlExec.client().getDataSet(sql);
		}
		return dsStep;
	}

	/**
	 * ��ȡ��Ŀ�ڲ��������
	 * 
	 * @param prjCode
	 * @param wid
	 * @return
	 * @throws Exception
	 */
	// public DataSet getAuditNStateDataSet(String prjCode, String wid)
	// throws Exception {
	// return DBSqlExec.client().getDataSet(
	// "select * from "
	// + IPrjAudit.AuditNewInfo.AuditPrjNState.TableName
	// + " where " + IPrjAudit.AuditNewInfo.AudtiStep.WID
	// + "='" + wid + "' and "
	// + IPrjAudit.AuditNewInfo.AuditPrjNState.PRJ_CODE + "='"
	// + prjCode + "'");
	// }
	/**
	 * ��ȡ��Ŀ��ǰ״̬
	 * 
	 * @param prjCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getAuditCurStateDataSet(String prjCode) throws Exception {
		if (Common.isNullStr(prjCode)) {
			return null;
		}
		String sql = "select * from "
				+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR + " where "
				+ IPrjAudit.PrjAuditTable.prj_code + "='" + prjCode
				+ "' and set_year = " + Global.loginYear + " and rg_code = "
				+ Global.getCurrRegion();
		DataSet ds = DBSqlExec.client().getDataSet(sql);
		if ((ds != null) && !ds.isEmpty()) {
			return ds;
		} else {
			return null;
		}
	}

	/**
	 * ��λ������Ϣ
	 * 
	 * @param prjCode
	 * @return
	 * @throws Exception
	 */
	public static InfoPackage sendAuditInfoByDiv(String prjCode)
			throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		String filter = IPrjAudit.PrjAuditTable.prj_code + "='" + prjCode
				+ "' and set_year = " + Global.loginYear + " and rg_code = "
				+ Global.getCurrRegion() + " and "
				+ IPrjAudit.PrjAuditTable.audit_state + ">0";
		int count = DBSqlExec.client().getRecordCount(
				IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR, filter);
		if (count > 0) {
			info.setSuccess(false);
			info.setsMessage("����Ŀ�Ѿ�����������̣�ֻ���˻ص���λ�ſ�����������");
			return info;
		} else {
			String filter1 = IPrjAudit.PrjAuditTable.prj_code + "='" + prjCode
					+ "' and set_year = " + Global.loginYear
					+ " and rg_code = " + Global.getCurrRegion();
			StringBuffer sqlD = new StringBuffer();
			sqlD.append("delete from ");
			sqlD.append(IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
			sqlD.append(" where " + filter1);
			StringBuffer sqlI = new StringBuffer();
			sqlI.append("insert into "
					+ IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
			sqlI.append("(ROW_ID, DIV_CODE, DIV_NAME, PRJ_CODE,");
			sqlI.append("PRJ_NAME, AUDIT_USERID,AUDIT_USERNAME,AUDIT_STATE,");
			sqlI.append("IS_BACK,AUDIT_TIME,SET_YEAR,EN_ID,AUDIT_ISEND)");
			sqlI.append("select newid,div_code,div_name,prj_code,prj_name,");
			sqlI.append("'" + GlobalEx.user_code + "','"
					+ GlobalEx.getUserName() + "',0,0,");
			sqlI.append("'" + Tools.getCurrDate() + "',");
			sqlI.append(Global.loginYear + ",1,en_id");
			sqlI.append(" from rp_xmjl");
			List list = new ArrayList();
			list.add(sqlD.toString());
			list.add(sqlI.toString());
			QueryStub.getClientQueryTool().executeBatch(list);
		}
		return info;
	}
	
	/**
	 * ��ȡ��������
	 * 
	 * @param type
	 *            0:��λ���� 1����Ŀ����
	 * @param divCodes
	 * @return
	 */
	public String getArraySqlFilter(String tag, List Codes) {
		StringBuffer sql = new StringBuffer();
		if (Codes == null) {
			return "";
		}
		if (Codes.size() == 0) {
			return "";
		} else {
			sql.append(tag+" in(");
			for (int i = 0; i < Codes.size(); i++) {
				if (i == 0) {
					sql.append("'");
				} else {
					sql.append(",'");
				}
				sql.append(Codes.get(i));
				sql.append("'");
			}
			sql.append(")");
		}
		return sql.toString();
	}

	/**
	 * ��ȡ��Ŀ�ⲿ�������
	 * 
	 * @param prjCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getAuditWStateDataSet(String prjCode) throws Exception {
		return DBSqlExec.client().getDataSet(
				"select * from "
						+ IPrjAudit.AuditNewInfo.AuditPrjWState.TableName
						+ " where "
						+ IPrjAudit.AuditNewInfo.AuditCurPrjState.PRJ_CODE
						+ "='" + prjCode + "'");
	}

	/**
	 * ��ȡ�û������������λ��
	 * 
	 * @return IPrjAppr.AuditInfo.UserTypeInfo
	 */
	// public int getLoginUserToStep() {
	// List list = this.getAuditStep();
	// String roleid = GlobalEx.getRoleId();
	// int step = 0;
	// for (int i = 0; i < list.size(); i++) {
	// Map map = (Map) list.get(i);
	// if (roleid.equalsIgnoreCase(Common.nonNullStr(map
	// .get(IPrjAppr.AuditInfo.StateInfo.role_id.toLowerCase())))) {
	// return Integer.parseInt(Common.nonNullStr(map
	// .get(IPrjAppr.AuditInfo.StateInfo.step.toLowerCase())));
	// }
	// }
	// return step;
	// }
	// public String getStepName(int step) {
	// String stepName = null;
	// List list = this.getAuditStep();
	// for (int i = 0; i < list.size(); i++) {
	// Map map = (Map) list.get(i);
	// if (step == Integer.parseInt(Common.nonNullStr(map.get("step")))) {
	// stepName = Common.nonNullStr(map
	// .get(IPrjAppr.AuditInfo.StateInfo.role_name
	// .toLowerCase()));
	// break;
	// }
	// }
	// return stepName;
	// }
	public void setAuditStep(List lst) {
		lstAuditStep = lst;
	}

	public List getAuditStep() {
		if (lstAuditStep == null) {
			lstAuditStep = PrjAuditStub.getMethod().getStepAllInfo(
					Global.loginYear);
		}
		return lstAuditStep;
	}

	/**
	 * ��ȡ��˵��������
	 * 
	 * @return
	 */
	public int getMaxStep() {
		if (this.iMaxstep == 0) {
			// List list = getAuditStep();
			// for (int i = 0; i < list.size(); i++) {
			// Map map = (Map) list.get(i);
			// int stepNow = Integer.parseInt(Common
			// .nonNullStr(map.get("wid")));
			// if (stepNow > iMaxstep)
			// iMaxstep = stepNow;
			// }
			try {
				iMaxstep = DBSqlExec
						.client()
						.getIntValue(
								"select max(step_id) from "
										+ IPrjAudit.PrjAuditTable.PrjAuditSetTable.TABLENAME_AUDIT_SET);
			} catch (Exception ee) {
				return 0;
			}
		}
		return iMaxstep;
	}

	public static List getSynBackTableList() {
		List list = new ArrayList();
		for (int i = 0; i < 9; i++) {
			XMLData map = new XMLData();
			String filter1 = "1=1 and DIV_CODE like '{p_div_code}%' and BATCH_NO = '{p_batch_no}' and DATA_TYPE = '{p_data_type}'";
			String filter2 = "1=1";
			switch (i) {
			case 0:
				map.put("tab_en_name", "FB_P_BASE_APPR");
				map.put("condition", filter1);
				break;
			case 1:
				map.put("tab_en_name", "FB_P_APPR");
				map.put("condition", filter1);
				break;
			case 2:
				map.put("tab_en_name", "FB_P_APPR_DETAIL");
				map.put("condition", filter1);
				break;
			case 3:
				map.put("tab_en_name", "FB_P_AFFIX_APPR");
				map.put("condition", filter1);
				break;
			case 4:
				map.put("tab_en_name",
						IPrjAudit.AuditNewInfo.AuditCurPrjState.TableName);
				map.put("condition", filter2);
				break;
			case 5:
				map.put("tab_en_name",
						IPrjAudit.AuditNewInfo.AuditPrjNState.TableName);
				map.put("condition", filter2);
				break;
			case 6:
				map.put("tab_en_name",
						IPrjAudit.AuditNewInfo.AuditPrjWState.TableName);
				map.put("condition", filter2);
				break;
			case 7:
				map.put("tab_en_name",
						IPrjAudit.AuditNewInfo.AudtiStep.TableName);
				map.put("condition", filter2);
				break;
			case 8:
				map.put("tab_en_name", "FB_P_APPR_AUDIT_AFFIX");
				map.put("condition", filter1);
				break;
			default:
				break;
			}
			list.add(map);
		}
		return list;
	}

	public static String[] getTransTableNames() {
		List list = getSynBackTableList();
		String[] ss = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			ss[i] = Common.nonNullStr(map.get("tab_en_name"));
		}
		return ss;
	}

	// public HistoryAuditPanel getHazg() {
	// return hazg;
	// }
	//
	// public void setHazg(HistoryAuditPanel hazg) {
	// this.hazg = hazg;
	// }
	//
	// public HistoryAuditPanel getHajx() {
	// return hajx;
	// }
	//
	// public void setHajx(HistoryAuditPanel hajx) {
	// this.hajx = hajx;
	// }
	//
	// public HistoryAuditPanel getHays() {
	// return hays;
	// }
	//
	// public void setHays(HistoryAuditPanel hays) {
	// this.hays = hays;
	// }

	public String getUserCode() {
		if (Common.isNullStr(userID)) {
			try {
				userID = DBSqlExec.client().getStringValue(
						"select user_code from sys_usermanage where user_id = '"
								+ Global.getUserId() + "'");
			} catch (Exception ee) {
				userID = "";
			}
		}
		// return Global.user_code;
		return userID;

	}

	/**
	 * ��ȡ���ұ���
	 * 
	 * @return
	 */
	public String getDepCode() {
		if (Common.isNullStr(loginDepCode)) {
			try {
				loginDepCode = DBSqlExec.client().getStringValue(
						"select dep_code from vw_fb_department where dep_id = '"
								+ GlobalEx.getUserBeLong() + "'");
			} catch (Exception ee) {
				return "";
			}
		}
		return loginDepCode;
	}

	private DataSet dsUnit;

	public DataSet getPrjUnitInfo() throws Exception {
		if (dsUnit == null) {
			return DBSqlExec
					.client()
					.getDataSet(
							"select * from fb_s_pubcode where typeid='PRJAPPRAISEUNITTYPE'");
		} else {
			return dsUnit;
		}
	}

	// ��ȡ���д�����Ϣ�����ṩ��ȡ����
	public String getZGWID() {
		try {
			if (getPrjUnitInfo().locate("code", "zg")) {
				return dsUnit.fieldByName("cvalue").getString();
			}
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "��ȡ���ܴ�����Ϣ����");
		}
		return "";
	}

	// ��ȡ���д�����Ϣ�����ṩ��ȡ����
	public String getJXWID() {
		try {
			if (getPrjUnitInfo().locate("code", "jx")) {
				return dsUnit.fieldByName("cvalue").getString();
			}
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "��ȡ���ܴ�����Ϣ����");
		}
		return "";
	}

	// ��ȡ���д�����Ϣ�����ṩ��ȡ����
	public String getYSWID() {
		try {
			if (getPrjUnitInfo().locate("code", "ys")) {
				return dsUnit.fieldByName("cvalue").getString();
			}
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "��ȡ���ܴ�����Ϣ����");
		}
		return "";
	}

	private String zgText;

	public String getZgText() {
		return zgText;
	}

	public void setZgText(String zgText) {
		this.zgText = zgText;
	}

	public String getJxText() {
		return jxText;
	}

	public void setJxText(String jxText) {
		this.jxText = jxText;
	}

	public String getYsText() {
		return ysText;
	}

	public void setYsText(String ysText) {
		this.ysText = ysText;
	}

	private String jxText;
	private String ysText;

	private String sUserWid;

	/**
	 * ��ȡ�û�����������еĲ���
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUserWid() throws Exception {
		if (sUserWid != null) {
			return sUserWid;
		}
		String sql = "select wid from fb_p_appr_audit_wstep where instr(opid,'"
				+ this.getUserCode() + "') >0 or instr('" + this.getDepCode()
				+ "',opid)>0";

		DataSet ds = DBSqlExec.client().getDataSet(sql);
		int i = 0;
		if ((ds != null) && !ds.isEmpty()) {
			ds.beforeFirst();
			while (ds.next()) {
				if (i == 0) {
					sUserWid = IPrjAudit.AuditNewInfo.WID + "=="
							+ ds.fieldByName("WID").getString();
				} else {
					sUserWid = sUserWid + "||" + IPrjAudit.AuditNewInfo.WID
							+ "==" + ds.fieldByName("WID").getString();
				}
				i++;
			}
		} else {
			sUserWid = "";
		}
		if (sUserWid.indexOf("1") >= 0) {
			if (i == 0) {
				sUserWid = IPrjAudit.AuditNewInfo.WID + "==0";
			} else {
				sUserWid = sUserWid + "||" + IPrjAudit.AuditNewInfo.WID + "==0";
			}
		}
		String nn = "";
		String sqlN = " select nid from fb_p_appr_audit_nstate where instr(nid,'"
				+ getUserCode() + "') >0 ";
		DataSet dsN = DBSqlExec.client().getDataSet(sqlN);
		int j = 0;
		if ((dsN != null) && !dsN.isEmpty()) {
			dsN.beforeFirst();
			while (dsN.next()) {
				if (i == 0) {
					nn = IPrjAudit.AuditNewInfo.NID + "=='"
							+ dsN.fieldByName("NID").getString() + "'";
				} else {
					nn = nn + "||" + IPrjAudit.AuditNewInfo.NID + "=='"
							+ dsN.fieldByName("NID").getString() + "'";
				}
				j++;
			}
		} else {
			nn = "";
		}
		if (!Common.isNullStr(sUserWid)) {
			if (!Common.isNullStr(nn)) {
				sUserWid = sUserWid + "||" + nn;
			}
		} else {
			sUserWid = nn;
		}
		return sUserWid;
	}

	public List getAuditPrjCodes() {
		return prjCodes;
	}

	public void setAuditPrjCodes(List prjCodes) {
		this.prjCodes = prjCodes;
	}
	
	public String stepID;
	
	public String stepName;

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	
	

}
