/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.util.Global;

public class PrintDialog extends FDialog{
	
	private static final long serialVersionUID = 1L;

		public PrintDialog( Report report ){
		super(Global.mainFrame);
		try{
			FPanel pnlBase = new FPanel();
			this.setSize(600, 600);          // ���ô����С
			this.setResizable(false);        // ���ô����С�Ƿ�ɱ�
			this.getContentPane().add(pnlBase);
			this.dispose();                  // ��������Զ�������
			this.setTitle("��ӡ���ý���");  // ���ô������
			this.setModal(true);   
		}catch( Exception ee ){
			ee.printStackTrace();
		}
	}
}
