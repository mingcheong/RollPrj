<%@ page language="java" import="java.util.*,com.foundercy.pf.util.XMLData" pageEncoding="gb2312"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String username = (String)request.getParameter("user_code");
			String password = (String)request.getParameter("password");
            List list = (List)request.getAttribute("result");
%>



<html>
  <HEAD>
     <TITLE>JSP for DownloadForm form</TITLE>
     <SCRIPT language='javascript'>
         function _download(strFileName,data_url){
         	 // document.execCommand("saveAs", "true", strFileName);
              document.DownloadCryptographForm.download.value=data_url;
              document.DownloadCryptographForm.strFileName.value=strFileName;
              document.DownloadCryptographForm.action="downloadCryptograph.do";
              document.DownloadCryptographForm.submit();
         }
         
         function up(){
             alert("ok");
             window.location="test.jsp";
         }
         
         function _search(){
             document.DownloadCryptographForm.download.value="aaa";
             document.DownloadCryptographForm.action="downloadCryptograph.do";
             document.DownloadCryptographForm.submit();
         }
     </SCRIPT>
  </HEAD>
  <BODY>
     <B><FONT size='6'>密文下载：</FONT></B> 
     <html:form action="/downloadCryptograph">
     <html:hidden property='download'/>
     <input type="hidden" name="strFileName">
       &nbsp;&nbsp;&nbsp;&nbsp;
        用&nbsp;&nbsp;户<html:text property="username" value="<%=username%>"/> 
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        密&nbsp;&nbsp;码<html:password property="password" value="<%=password%>"/> <BR><BR>
       &nbsp;&nbsp;&nbsp;&nbsp;
        系&nbsp;&nbsp;统<html:select property="system"> 
                            <option value="000">
                                000 公共
                            <option value="001">
                                001 系统配置工具
                            <option value="002">
                                002 账务处理
                            <option value="101">
                                101 部门预算
                            <option value="111">
                                111 指标管理
                            <option value="112">
                                112 计划管理
                            <option value="115">
                                115 支付管理
                            <option value="121">
                                121 工资统发
                            <option value="201">
                                201 管理查询
                            <option value="211">
                                211 资金监控
                            <option value="301">
                                301 交换中心
                            <option value="311">
                                311 外围平台
                            <option value="999" selected="true">
                                999 部门版
                       </html:select>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        年&nbsp;&nbsp;度<html:select property="year">
							<option value="2001">
								2001
							<option value="2002">
								2002
							<option value="2003">
								2003
							<option value="2004">
								2004
							<option value="2005">
								2005
							<option value="2006" selected="true">
								2006
							<option value="2007">
								2007
							<option value="2008">
								2008
							<option value="2009">
								2009
						</html:select>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       <input  type="button" value="查询" onclick="_search()"/><BR><BR>
       
       <logic:notEmpty name="DownloadCryptographForm" property="fname">
            <font size='4' color="red">文件下载失败
            <bean:write name="DownloadCryptographForm" property="fname"/>
       </logic:notEmpty>

       <%
       		System.out.println(list);
       		if(list!=null) {
       		Iterator it = list.iterator();
       %>
       <table border='1'>
         <tr>
           <TD>
             &nbsp;
           </TD>
           <TD align="center">
               文件名称
           </TD>
           <TD align="center">
               数据类型
           </TD>
           <TD align="center">
               大小
           </TD>
           <TD align="center">
               上传时间
           </TD>
           <TD align="center">
               上传用户
           </TD>
           <TD>
              &nbsp;
           </TD>
         </tr>
         <%
           XMLData xmldata = null;
         	while(it.hasNext()) {
         		xmldata = (XMLData)it.next();
         	    String data_url = (String)xmldata.get("data_url");
         	    String strFileName = data_url.substring(data_url.lastIndexOf("/") + 1);
         		String data_type_name = (String)xmldata.get("data_type_name");
         		Long file_size = (Long)xmldata.get("file_size");
         		String upload_time = (String)xmldata.get("upload_time");
         		String user_name = (String)xmldata.get("user_name");
         %>
         <tr>
         <td>&nbsp;</td>
         <td><%=data_url%></td>
         <td><%=data_type_name%></td>
         <td><%=file_size%></td>
         <td><%=upload_time%></td>
         <td><%=user_name%></td>
         <td><input type="button" name="button" value="下载" onclick="_download('<%=strFileName%>','<%=data_url%>')"></td>
         </tr>         
         <%
         }
       %>
         
       </table>            
       <%
       		}
       %>
       </html:form>  
  </BODY>
</html>
