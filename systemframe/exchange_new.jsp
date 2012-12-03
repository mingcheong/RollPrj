<%@ page language="java" import="java.util.*,com.foundercy.pf.util.XMLData" pageEncoding="gb2312"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'exchange_new.jsp' starting page</title>
    <SCRIPT language='javascript'>
         function _download(strFileName,data_url){
         	 // document.execCommand("saveAs", "true", strFileName);
              document.DownloadForm.download.value=data_url;
              document.DownloadForm.strFileName.value=strFileName;
              document.DownloadForm.action="download.do";
              document.DownloadForm.submit();
         }
         
         function _search(){
             document.DownloadForm.download.value="aaa";
             document.DownloadForm.action="download.do";
             document.DownloadForm.submit();
         }
     </SCRIPT>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <body>
  <%
  	String result_up = (String)request.getAttribute("successOrFail");
  	String username = (String)request.getAttribute("user_code");
  	String password = (String)request.getAttribute("password");
  %>
    <TABLE>
       <html:form action="/upload" enctype="multipart/form-data">
  <%
  	if(result_up!=null) {
  		
  %>
  		<TR>
  			<TD colspan="3">
  <%
  		if(result_up.equals("success")) {
  %>
  			<font size='4' color="red">上传已成功，您上传的文件是: <bean:write name="UpLoadForm" property="fname" /></font>
  <%
  		}
  		if(result_up.equals("fail")) {
  %>
  			<font size='4' color="red">上传失败 <bean:write name="UpLoadForm" property="fname" /></font>
  <%
  		}	
  %>			
  			</TD>
  		</TR>
  <%
  	}
  %>
    	<TR>
    		<TD>文件上传</TD>
    		<TD><html:hidden property="username" value="<%=username%>"></html:hidden></TD>
    		<TD><html:hidden property="password" value="<%=password%>"></html:hidden></TD>
    	</TR>
    	<TR>
    		<TD></TD>
    		<TD><html:file property="photo" /></TD>
    		<TD><html:submit>上&nbsp;&nbsp传</html:submit></TD>
    	</TR>
       </html:form>
    </TABLE>
    <BR>
    <BR>
    <BR>
    <TABLE align="center">
       <html:form action="/download">
        <TR>
        	<TD colspan="4"><logic:notEmpty name="DownloadForm" property="fname"> <font size='4' color="red"> <bean:write name="DownloadForm" property="fname"/> </font></logic:notEmpty></TD>
        	<TD><html:hidden property='download'/></TD>
        	<TD><input type="hidden" name="strFileName"></TD>
        </TR>
    	<TR>
    		<TD>文件下载</TD>
    		<TD><html:hidden property="username" value="<%=username%>" /></TD>
    		<TD><html:hidden property="password" value="<%=password%>" /></TD>
    		<TD><html:hidden property="year" value="2006" /></TD>
    		<TD><html:hidden property="system" value="999" /></TD>
    		<TD><input name="button"  type="button" onclick="_search()" value="查&nbsp;&nbsp;询"/></TD>
    	</TR>
    	<TR>
    		<TD align="center">文件名称</TD>
    		<TD align="center">数据类型</TD>
    		<TD align="center">大小</TD>
    		<TD align="center">上传时间</TD>
    		<TD align="center">上传用户</TD>
    		<TD>&nbsp; </TD>
    	</TR>
 <%
 	List list = (List)request.getAttribute("result");
 	if(list!=null) {
 		for(Iterator it = list.iterator();it.hasNext();) {
 			XMLData xmldata = (XMLData)it.next();
 			String data_url = (String)xmldata.get("data_url");
         	String strFileName = data_url.substring(data_url.lastIndexOf("/") + 1);
         	String data_type_name = (String)xmldata.get("data_type_name");
         	Long file_size = (Long)xmldata.get("file_size");
         	String upload_time = (String)xmldata.get("upload_time");
         	String user_name = (String)xmldata.get("user_name");
 %>
 	    <TR>
            <TD><%=strFileName%></TD>
            <TD><%=data_type_name%></TD>
            <TD><%=file_size%></TD>
            <TD><%=upload_time%></TD>
            <TD><%=user_name%></TD>
            <TD><input type="button" name="button" value="下载" onclick="_download('<%=strFileName%>','<%=data_url%>')"></TD>
       </TR>
 <%
 		}
 	}
 %>
       </html:form>
    </TABLE>
  </body>
</html>
