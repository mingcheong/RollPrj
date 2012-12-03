package gov.nbcs.rp.common.export.action;

import java.util.ArrayList;
import java.util.List;

import jxl.write.WritableSheet;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.export.ibs.IExport;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

public class ExportAction {

	// ������Ϣ���ݼ�
	DataSet dsConfig = null;
	
	/**
	 * ��ȡ�����
	 * 
	 * @param reportID
	 *            �������
	 * @return
	 */
	public String getTitleWithDataSet(List lstConfig,String reportID) {
		String title = null;
		lstConfig = this.getConfigDataset(reportID);
		if ((lstConfig == null) || lstConfig.isEmpty()) {
			return title;
		}
//			if (dsConfig.locate(IExport.sKeyFiled, reportID))
		title = Common.nonNullStr(((XMLData) lstConfig.get(0)).get(IExport.sTitle));
		return title;
	}

	
	/**
	 * ��ȡ�����
	 * 
	 * @param reportID
	 *            �������
	 * @return
	 */
	public String getTitleWithDataSet(String reportID) {
		String title = null;
		dsConfig = this.getConfigDataset();
		if ((dsConfig == null) || dsConfig.isEmpty()) {
			return title;
		}
		// if (dsConfig.locate(IExport.sKeyFiled, reportID))
		try {
			if (dsConfig.locate("report_id", reportID)) {
				title = dsConfig.fieldByName(IExport.sTitle).getString();
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return title;
	}
	/**
	 * ��ȡ�����
	 * 
	 * @param reportIDs
	 *            �������
	 * @return
	 */
	public String[] getTitleWithDataSet(String[] reportIDs) {
		String[] title = new String[reportIDs.length];
		DataSet dsConfig = getConfigDataset();
		if ((dsConfig == null) || dsConfig.isEmpty()) {
			return title;
		}
		try {
			for (int i = 0; i < reportIDs.length; i++) {
				if (dsConfig.locate(IExport.sKeyFiled, reportIDs[i])) {
					title[i] = dsConfig.fieldByName(IExport.sTitle).getString();
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}
		return title;
	}

	/**
	 * ��ȡС����
	 * 
	 * @param reportIDs
	 *            �������
	 * @return
	 */
	public List getTitle_ChildWithDataSet(String[] reportIDs) {
		List title = new ArrayList();
		String[] child = new String[2];
		DataSet dsConfig = getConfigDataset();
		if ((dsConfig == null) || dsConfig.isEmpty()) {
			return title;
		}
		try {
			for (int i = 0; i < reportIDs.length; i++) {
				if (dsConfig.locate(IExport.sKeyFiled, reportIDs[i])) {
					child[0] = dsConfig.fieldByName(IExport.sChildTitle1)
							.getString();
					child[1] = dsConfig.fieldByName(IExport.sChildTitle2)
							.getString();
				}
				title.add(child);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}
		return title;
	}

	/**
	 * ��ȡС����
	 * 
	 * @param reportID
	 *            �������
	 * @return
	 */
	public String[] getTitle_ChildWithDataSet(List lstConfig,String reportID) {
		// С�������ݼ�
		String title[] = new String[2];
		// ������Ϣ���ݼ�
//		DataSet dsConfig = getConfigDataset();
		lstConfig = getConfigDataset(reportID);
		if ((lstConfig == null) || lstConfig.isEmpty()) {
			// ���������Ϣ��Ϊ��
			return title;
		}
		title[0] = Common.nonNullStr(((XMLData) lstConfig.get(0))
				.get(IExport.sChildTitle1));
		title[1] = Common.nonNullStr(((XMLData) lstConfig.get(0))
				.get(IExport.sChildTitle2));
		return title;
	}
	
	/**
	 * ��ȡС����
	 * 
	 * @param reportID
	 *            �������
	 * @return
	 */
	public String[] getTitle_ChildWithDataSet() {
		// С�������ݼ�
		String title[] = new String[2];
		// ������Ϣ���ݼ�
		DataSet dsConfig = getConfigDataset();
//		lstConfig = getConfigDataset(reportID);
		if ((dsConfig == null) || dsConfig.isEmpty()) {
			// ���������Ϣ��Ϊ��
			return title;
		}
		try{
			if ((dsConfig != null) && !dsConfig.isEmpty()) {
				title[0] = dsConfig.fieldByName(IExport.sChildTitle1).getString();
				title[0] = dsConfig.fieldByName(IExport.sChildTitle1).getString();
			}
		}catch(Exception ee){
			ee.printStackTrace();
			return title;
		}
		return title;
	}


	/**
	 * ���ñ���������Ϣ�����ݼ�
	 * 
	 * @param aTableName
	 * @param aFilter
	 */
	public DataSet getConfigDataset() {
		try {
			if (dsConfig == null) {
		        StringBuffer sqls = new StringBuffer();
		        sqls.append("select REPORT_ID,");
		        sqls.append("REPORT_TITLE,");
		        sqls.append("REPORT_LINE,");
		        sqls.append("REPORT_TITLE_A1,");
		        sqls.append("REPORT_TITLE_A2,");
		        sqls.append("REPORT_TITLE_FONT,");
		        sqls.append("REPORT_TITLE_FONT_SIZE,");
		        sqls.append("PRINT_SCALE,");
		        sqls.append("PAPER_SIZE,");
		        sqls.append("PRINT_ORIENT,");
		        sqls.append("MARGIN_LEFT,");
		        sqls.append("MARGIN_RIGHT,");
		        sqls.append("MARGIN_TOP,");
		        sqls.append("MARGIN_BOTTOM,");
		        sqls.append("TOP_TITLE_STARTROW,");
		        sqls.append("TOP_TITLE_ENDROW,");
		        sqls.append("LEFT_TITLE_STARTCOL,");
		        sqls.append("LEFT_TITLE_ENDCOL,");
		        sqls.append("REPORT_TITLE_HEIGHT,");
		        sqls.append("REPORT_TITLE_ORIENT,");
		        sqls.append("REPORT_TITLE_A1_HEIGHT,");
		        sqls.append("REPORT_TITLE_A2_HEIGHT,");
		        sqls.append("REPORT_TITLE_A1_FONT_SIZE,");
		        sqls.append("REPORT_TITLE_A2_FONT_SIZE,");
		        sqls.append("REPORT_TITLE_A1_ORIENT,");
		        sqls.append("REPORT_TITLE_A2_ORIENT,");
		        sqls.append("REPORT_TITLE_A1_FONT,");
		        sqls.append("REPORT_TITLE_A2_FONT,");
		        sqls.append("REPORT_TITLE_A1_USE,");
		        sqls.append("REPORT_TITLE_A2_USE,");
		        sqls.append("REPORT_BODY_FONT,");
		        sqls.append("REPORT_BODY_FONT_SIZE,");
		        sqls.append("REPORT_BODY_HEIGHT,");
		        sqls.append("REPORT_AUTONEXT,");
		        sqls.append("REPORT_PARAM_A1_ORIENT,");
		        sqls.append("REPORT_PARAM_A2_ORIENT,");
		        sqls.append("REPORT_PARAM_A1_POSTION,");
		        sqls.append("REPORT_PARAM_A2_POSTION,");
		        sqls.append("MODIFYTIME,");
		        sqls.append("IS_USEMODEL");
		        sqls.append(" from fb_s_printinfo ");
				dsConfig = ExportStub.getMethod().getDataset(sqls.toString());
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return dsConfig;
	}

	/**
	 * ���ñ���������Ϣ�����ݼ�
	 * 
	 * @param aTableName
	 * @param aFilter
	 */
	public List getConfigDataset(String report_id){
		try {
	        StringBuffer sqls = new StringBuffer();
	        sqls.append("select ");
	        sqls.append("REPORT_TITLE,");
	        sqls.append("REPORT_TITLE_A1,");
	        sqls.append("REPORT_TITLE_A2,");
	        sqls.append("REPORT_TITLE_FONT,");
	        sqls.append("REPORT_TITLE_FONT_SIZE,");
	        sqls.append("PRINT_SCALE,");
	        sqls.append("PAPER_SIZE,");
	        sqls.append("PRINT_ORIENT,");
	        sqls.append("MARGIN_LEFT,");
	        sqls.append("MARGIN_RIGHT,");
	        sqls.append("MARGIN_TOP,");
	        sqls.append("MARGIN_BOTTOM,");
	        sqls.append("TOP_TITLE_STARTROW,");
	        sqls.append("TOP_TITLE_ENDROW,");
	        sqls.append("LEFT_TITLE_STARTCOL,");
	        sqls.append("LEFT_TITLE_ENDCOL,");
	        sqls.append("REPORT_TITLE_HEIGHT,");
	        sqls.append("REPORT_TITLE_ORIENT,");
	        sqls.append("REPORT_TITLE_A1_HEIGHT,");
	        sqls.append("REPORT_TITLE_A2_HEIGHT,");
	        sqls.append("REPORT_TITLE_A1_FONT_SIZE,");
	        sqls.append("REPORT_TITLE_A2_FONT_SIZE,");
	        sqls.append("REPORT_TITLE_A1_ORIENT,");
	        sqls.append("REPORT_TITLE_A2_ORIENT,");
	        sqls.append("REPORT_TITLE_A1_FONT,");
	        sqls.append("REPORT_TITLE_A2_FONT,");
	        sqls.append("REPORT_TITLE_A1_USE,");
	        sqls.append("REPORT_TITLE_A2_USE,");
	        sqls.append("REPORT_BODY_FONT,");
	        sqls.append("REPORT_BODY_FONT_SIZE,");
	        sqls.append("REPORT_BODY_HEIGHT,");
	        sqls.append("REPORT_AUTONEXT,");
	        sqls.append("REPORT_PARAM_A1_ORIENT,");
	        sqls.append("REPORT_PARAM_A2_ORIENT,");
	        sqls.append("REPORT_PARAM_A1_POSTION,");
	        sqls.append("REPORT_PARAM_A2_POSTION,");
	        sqls.append("MODIFYTIME,");
	        sqls.append("IS_USEMODEL");
	        sqls.append(" from fb_s_printinfo where report_id = '");
	        sqls.append(report_id+"' and set_year = ");
	        sqls.append(Global.loginYear);
			return ExportStub.getMethod().executeQuery(sqls.toString());
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}
	}

	/**
	 * �����ڱ�ͷ����һ����ͬ�ĵ�Ԫ����кź��к�
	 * 
	 * @param rowsum
	 *            ����
	 * @param colsum
	 *            ����
	 * @param avalue
	 *            ֵ
	 * @param headerCell
	 *            ��Ԫ��
	 * @return
	 */
	private int[] getSameValueCell(int kk,int rowsum, int colsum, int icolb,
			int irowb, String avalue, Object[][] headerCell) {
//		int kk = ExportPropType.num_ChildTitlesCount + 1;
		int[] cellinfo=new int[2];
		if (rowsum==1){
			cellinfo[0] = icolb;
			cellinfo[1] = irowb;
			return cellinfo;
		}
		int irow = kk;
		int icol = 0;
		for (int jh = icolb; jh < colsum; jh++) {
			if (jh == icolb) {
				// ���ͱ���Ƚ�
				icol = icolb;
				continue;
			}
			// ������
			if (!Common.isEqual(headerCell[jh][irowb - kk], avalue)
					&& Common.isNullStr(Common.nonNullStr(headerCell[jh][irowb
							- kk]))) {
				// ����ҵ����ǿո񣬶��Ҳ��ȣ������ж���������ֵ����������������������
				boolean bb = true;
				int rownow = irowb;
				for (int k = irowb - kk - 1; k >= rowsum - 6; k--) {
					// ����ѭ��
					if (rownow == 3) {
						break;
					} else {
						rownow--;
					}
					if (!Common.isNullStr(Common.nonNullStr(headerCell[jh][k]))) {
						bb = false;
					}
				}
				if (!bb) {
					break;
				}
				icol = jh - 1;
				continue;
			}
			if (!Common.isEqual(headerCell[jh][irowb - kk], avalue)
					&& !Common.isNullStr(Common.nonNullStr(headerCell[jh][irowb
							- kk]))) {
				// ����ҵ��Ĳ��ȣ����Ҳ��ǿո�
				icol = jh - 1;
				break;
			}
			if (Common.isEqual(headerCell[jh][irowb - kk], avalue)
					&& !Common.isNullStr(Common.nonNullStr(headerCell[jh][irowb
							- kk]))) {
				// ����ҵ�����ȣ����Ҳ��ǿո�
				if (jh == (colsum - 1)) {
					icol = colsum - 1;
				} else {
					icol = jh;
				}
				continue;
			}
			if (jh == colsum - 1) {
				icol = colsum - 2;
				break;
			}
		}
		for (int ih = irowb; ih < rowsum + 1; ih++) {
			if (ih == irowb) {
				irow = irowb;
				continue;
			}
			if (!Common.isEqual(headerCell[icolb][ih - kk], avalue)
					&& Common.isNullStr(Common.nonNullStr(headerCell[icolb][ih
							- kk]))) {
				// ����ҵ��Ĳ���ȣ������ǿո����������
				irow = ih;
				continue;
			}
			if (!Common.isEqual(headerCell[icolb][ih - kk], avalue)
					&& !Common.isNullStr(Common.nonNullStr(headerCell[icolb][ih
							- kk]))) {
				// ����ҵ��Ĳ��ȣ����Ҳ��ǿո�
				irow = ih - 1;
				break;
			}
			if (Common.isEqual(headerCell[icolb][ih - kk], avalue)
					&& !Common.isNullStr(Common.nonNullStr(headerCell[icolb][ih
							- kk]))) {
				// ����ҵ�����ȣ����Ҳ��ǿո�
				irow = ih;
				continue;
			}
			if (ih == rowsum) {
				irow = rowsum + 1;
				break;
			}
		}
		cellinfo[0] = icol;
		cellinfo[1] = irow;
		return cellinfo;
	}

	/**
	 * �ϲ���ͷ��ͬ��Ϣ�ĵ�Ԫ��
	 * 
	 * @param ws
	 * @param headerCell
	 * @param rowsum
	 * @param colsum
	 * @throws Exception
	 */
	public void mergeCells(boolean needTitle,WritableSheet ws, Object[][] headerCell, int rowsum,
			int colsum) throws Exception {
		int[] cellInfo = null;
		int icolb = 0;
		int icole = 0;
		int irowb = ExportPropType.num_ChildTitlesCount + 1;
		int irowe = 0;
		int kk = ExportPropType.num_ChildTitlesCount + 1;
		if (!needTitle){
			kk=0;
			irowb =0;
		}
		for (int ih = kk; ih < rowsum; ih++) {
			// icolb = 0;
			irowb = ih;
			for (int jh = 0; jh < colsum; jh++) {
				icolb = jh;
				if (Common
						.isNullStr(Common.nonNullStr(headerCell[jh][ih - kk]))) {
					continue;
				}
				cellInfo = getSameValueCell(kk,rowsum, colsum, icolb, ih, Common
						.nonNullStr(headerCell[jh][ih - kk]), headerCell);
				icole = cellInfo[0];
				irowe = cellInfo[1];
				if (icolb > icole) {
					ws.mergeCells(icolb, irowb, colsum, irowe);
					jh = colsum + 1;
					break;
				}
				if ((icolb == icole) && (irowb == irowe)) {
					continue;
				}
				ws.mergeCells(icolb, irowb, icole, irowe);
				icolb = icole + 1;
				jh = icole;
			}
		}
	}
}
