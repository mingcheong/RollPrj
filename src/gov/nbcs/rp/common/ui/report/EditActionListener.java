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

import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

public class EditActionListener  implements DataSetProcListener  {
    Report report;
    
    EditActionListener(Report report) {
        this.report = report;
    }

    public void beforeProc(DataSetEvent event) throws Exception {
        report.getUI().setRowSelect(false);
    }

    public void afterProc(DataSetEvent event) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
