/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:�任����Դ���
 * </p>
 * <p>
 * Description:

 */
public class ChangeSourcePanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private CustomTree ftreePayOutFS;

	private FTextField ftxtName;

	// ֧����Դ����
	private String sFundSource;

	// ֧���ʽ���Դ
	private List lstPfsCode;

	public ChangeSourcePanel(List lstPfsCode) {
		this.lstPfsCode = lstPfsCode;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ʾ�任�ʽ���Դ���淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void init() throws NumberFormatException, Exception {
		// �������ݿ�ӿ�
//		ISysIaeStru sysIaeStruServ = SysIaeStruI.getMethod();
//		DataSet dsPfs = sysIaeStruServ.getPayOutFSTre(Integer
//				.parseInt(Global.loginYear));

//		// ����֧���ʽ���Դ��
//		ftreePayOutFS = new CustomTree(IPayOutFS.PFS_ROOT, dsPfs,
//				IPayOutFS.LVL_ID, ISysIaeStru.NAME, IPayOutFS.PAR_ID, null,
//				IPayOutFS.LVL_ID, true);
		ftreePayOutFS.setIsCheckBoxEnabled(true);
		ftreePayOutFS.addMouseListener(new PayOutTreeMouseListene());
		FScrollPane fScroll = new FScrollPane(ftreePayOutFS);

		ftxtName = new FTextField("�ʽ���Դ���ƣ�");
		ftxtName.setProportion(0.2f);

		this.setLayout(new RowPreferedLayout(1));
		this.addControl(fScroll, new TableConstraints(1, 1, true, true));
		this.addControl(ftxtName, new TableConstraints(1, 1, false, true));

		if (lstPfsCode != null && !lstPfsCode.isEmpty()) {

			String pfsCodeField = lstPfsCode.get(0).toString();
			pfsCodeField = StringUtils.replace(pfsCodeField, "isnull(", "");
			pfsCodeField = StringUtils.replace(pfsCodeField, ",0)", "");
			pfsCodeField = StringUtils.replace(pfsCodeField, "+", ",");
			// �����ʽ���Դ��ѡ
//			SetSelectTree.setIsCheck(ftreePayOutFS, pfsCodeField.split(","),
//					IPayOutFS.PFS_ENAME, IPayOutFS.LVL_ID);
			PayOutTreeMouseListene PayOutTreeMouseListene = new PayOutTreeMouseListene();
			PayOutTreeMouseListene.mouseClicked(null);
		}

	}

	private class PayOutTreeMouseListene extends MouseAdapter {

		public void mouseClicked(MouseEvent arg0) {
			try {
				sFundSource = getFieldFname();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"���ѡ���ʽ���Դ���Ʒ������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			ftxtName.setValue(sFundSource);
		}

	}

	/**
	 * ���ѡ��֧������Դֵ����֯���ֶδ�,��֧������Դ���
	 * 
	 * @return �����б�,0���ʽ���Դ��ɵ��ֶ�ֵ�� 1���ʽ���Դ��ɲ����������s
	 * @throws Exception
	 */
	public List getFieldEname() throws Exception {

		String sFieldEname = "";
		String sFilter = "";
		MyTreeNode[] selectPfsNodes = ftreePayOutFS.getSelectedNodes(true);
		DataSet dsPfs = ftreePayOutFS.getDataSet();
		int iCount = selectPfsNodes.length;
		if (iCount == 0)
			return null;
		for (int i = 0; i < iCount; i++) {
			if (dsPfs.gotoBookmark(selectPfsNodes[i].getBookmark())) {
				if (!Common.isNullStr(sFieldEname)) {
					sFieldEname = sFieldEname + "+";
				}
				if (!Common.isNullStr(sFilter)) {
					sFilter = sFilter + " or ";
				}
				sFieldEname = sFieldEname + "isnull("
						+ dsPfs.fieldByName(IPayOutFS.PFS_ENAME).getString()
						+ ",0)";
				sFilter = sFilter
						+ dsPfs.fieldByName(IPayOutFS.PFS_ENAME).getString()
						+ "<>0";
			}
		}
		if (!Common.isNullStr(sFilter)) {
			sFilter = "(" + sFilter + ")";
		}
		List lstResult = new ArrayList();
		lstResult.add(sFieldEname);
		lstResult.add(sFilter);

		return lstResult;
	}

	/**
	 * ���ѡ��֧������Դ�������ƣ���֯���ֶδ�
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getFieldFname() throws Exception {
//		MyTreeNode[] selectPfsNodes = SysUntPub.getSelectNode(ftreePayOutFS);
//		if (selectPfsNodes == null || selectPfsNodes.length == 0)
			return "";
	}

	/**
	 * �õ�ѡ�е��ʽ���Դ����
	 * 
	 * @return
	 */
	public String getFundSourceFName() {
		return ftxtName.getValue().toString();
	}

}
