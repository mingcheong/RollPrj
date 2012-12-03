
<%@ page language="java" import="java.util.*,com.foundercy.pf.util.XMLData" pageEncoding="gb2312"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String username = (String) request.getParameter("user_code");
			String password = (String) request.getParameter("password");
			List list = (List) request.getAttribute("result");
%>



<html>
	<HEAD>
		<TITLE>JSP for DownloadForm form</TITLE>
		<SCRIPT language='javascript'>
         function _download(strFileName,data_url){
         	 // document.execCommand("saveAs", "true", strFileName);
              document.DownloadForm.download.value=data_url;
              document.DownloadForm.strFileName.value=strFileName;
              document.DownloadForm.action="download.do";
              document.DownloadForm.submit();
         }
         
         function up(){
             alert("ok");
             window.location="test.jsp";
         }
         
         function _search(){
             document.DownloadForm.download.value="aaa";
             document.DownloadForm.action="download.do";
             document.DownloadForm.submit();
         }
     </SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</HEAD>
	<BODY topmargin="0">
		<table width="983" height="336" border="0" cellpadding="0" cellspacing="0" background="exchange-body.jpg">
			<tr>
				<td>
					<B><FONT size='6'> &nbsp;&nbsp;文件下载：</FONT></B>
					<html:form action="/download">
						<html:hidden property='download' />
						<input type="hidden" name="strFileName">
&nbsp;&nbsp;&nbsp;&nbsp; 用&nbsp;&nbsp;户<html:text property="username" value="<%=username%>" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 密&nbsp;&nbsp;码<html:password property="password" value="<%=password%>" />
						<BR>
						<BR>
&nbsp;&nbsp;&nbsp;&nbsp; 系&nbsp;&nbsp;统<html:select property="system">
							<option value="801" selected>
								801 部门版&nbsp;&nbsp;
						</html:select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;度<html:select property="year">
							<option value="2001">
								2001年
							<option value="2002">
								2002年
							<option value="2003">
								2003年
							<option value="2004">
								2004年
							<option value="2005">
								2005年
							<option value="2006">
								2006年
							<option value="2007" selected>
								2007年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<option value="2008">
								2008年
							<option value="2009">
								2009年
						</html:select> &nbsp;&nbsp;&nbsp;
                        <input name="button" type="button" onclick="_search()" value="查&nbsp;&nbsp;询" />
						<BR>
						<BR>
						<logic:notEmpty name="DownloadForm" property="fname">
							<font size='4' color="red"> <bean:write name="DownloadForm" property="fname" /> </font>
						</logic:notEmpty>
						<%System.out.println(list);
			           if (list != null) {
				           Iterator it = list.iterator();
				        %>
						<table border='1'align="left">
							<tr>
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
							<%XMLData xmldata = null;
				    while (it.hasNext()) {
					  xmldata = (XMLData) it.next();
					  String data_url = (String) xmldata.get("data_url");
					  String strFileName = data_url.substring(data_url
							.lastIndexOf("/") + 1);
					  String data_type_name = (String) xmldata
							.get("data_type_name");
					  Long file_size = (Long) xmldata.get("file_size");
					  String upload_time = (String) xmldata.get("upload_time");
					  String user_name = (String) xmldata.get("user_name");

					%>
							<tr>
								<td>
									<%=data_url%>
								</td>
								<td>
									<%=data_type_name%>
								</td>
								<td>
									<%=file_size%>
								</td>
								<td>
									<%=upload_time%>
								</td>
								<td>
									<%=user_name%>
								</td>
								<td>
									<input type="button" name="button" value="下载" onclick="_download('<%=strFileName%>','<%=data_url%>')">
								</td>
							</tr>
							<%}

			%>
						</table>
						<%}

		%>
					</html:form>
				</td>
			</tr>
			<tr>
				<td height="61">
					&nbsp;
				</td>
			</tr>
		</table>
	</BODY>
</html>
