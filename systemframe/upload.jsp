<%@ page language="java" pageEncoding="gb2312" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

			%>
<%String username = (String) request.getParameter("user_code");
			String password = (String) request.getParameter("password");

			%>
<html>
	<head>
		<title>JSP for UpLoadForm form</title>
	</head>
	<body>
		<table width="983" height="300" border="0" cellpadding="0" cellspacing="0" background="exchange-top.jpg">
			<tr>
				<td>&nbsp;
					
				</td>
			</tr>
			<tr>
				<td>
					<p>
						<B><FONT size='6'> ���ļ��ϴ���</FONT></B>
					</p>
					<html:form action="/upload" enctype="multipart/form-data">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;����<html:text property="username" value="<%=username%>" /> &nbsp;&nbsp;&nbsp;&nbsp; ��&nbsp;&nbsp;�룺<html:password property="password" value="<%=password%>" />
						<br>
						<br>
&nbsp;&nbsp;&nbsp;&nbsp;�� ��&nbsp;&nbsp;����<html:file property="photo" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:submit>��&nbsp;&nbsp;��</html:submit>
						<br>
						<br>
						<logic:notEmpty name="UpLoadForm" property="fname">
							��
							<%String s = (String) request.getAttribute("successOrFail");
			if (s.equalsIgnoreCase("success")) {

			%>
							<font size='4' color="red">&nbsp;&nbsp;&nbsp;�ϴ��ѳɹ������ϴ����ļ���: <bean:write name="UpLoadForm" property="fname" /> <!--  <LI>Size=<bean:write name="UpLoadForm" property="size"/></LI>  --> </font>
							<%}
			if (s.equalsIgnoreCase("fail")) {

			%>
							<font size='4' color="red">�ϴ�ʧ�� <bean:write name="UpLoadForm" property="fname" /> </font>
							<%}%>
						</logic:notEmpty>
						<P></P>
					</html:form>
				</td>
			</tr>
		</table>
	</body>
</html>
