/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl;

public interface StatefulData {
    int DS_BROWSE = 0x00000001;   
    int DS_EDIT = 0x00000002;
    int DS_INSERT = 0x00000004;
    
    
    int FLOW_CONFIRM_OR_UNTREAD_DONE = 13; //用于录入表时控制按钮可用  表示工作流已确认，已退回状态
    int FLOW_UNTREAD = 5; //表示作流被退回状态
    int FLOW_NOTENABLE= 9;//表示当前用户等条件不成立，所有状态不可用
    int FLOW_ISNOTDIV= 8;//当前用 户不是企业用户
    public int getState();
}
