<%@page contentType="text/html;charset=GBK"%>
<html>
<head>
<meta HTTP-EQUIV="Content-Type" content="text/html; charset=GBK">
<link rel="stylesheet" type="text/css" href="../news.css">
<title>国库集中支付网络</title>
<style type="text/css">
<!--
a {  font-size: 9pt; color: #000000; text-decoration: none}
body {  font-size: 9pt; color: #000000; text-decoration: none}
table {  font-size: 9pt; color: #000000; text-decoration: none}
a:link {  font-size: 9pt; color: #000000; text-decoration: none}
a:hover {  font-size: 9pt; color: #0000CC; text-decoration: none}
a:active {  font-size: 9pt; color: #CC0000}

.navPoint{font-family: "Webdings";font-size:9pt;color:#0000ff;cursor:hand;}
p{
font-size:9pt;
-->
</style>

<script>
var flag = false;
function switchSysBar(){
  if(!flag) {
     flag = true;
     document.all("frmTitle").style.display = "none";
     document.all("switchPoint").src = "photo//go.gif";
  }else {
     flag = false;
     document.all("frmTitle").style.display = "block";
     document.all("switchPoint").src = "photo//out.gif";
  }
}
</script>
</head>
<body scroll="no" style="MARGIN: 0px">
<table border="1" width="100%" height="100%" bordercolorlight="#A0998D" bordercolordark="#FFFFFF" cellspacing="0" cellpadding="0">
  <tr bordercolordark="#ffffff" bordercolorlight="#000000">
    <td id="frmTitle" name="leftFrame" nowrap valign="middle" align="center" width="0%">
      <iframe id=leftFrame name="leftFrame" style="HEIGHT: 100%; VISIBILITY: inherit; WIDTH: 140; Z-INDEX: 2" frameborder=0 src="left.jsp" scrolling="auto"></iframe>
    </td>
    <td title="点击此处可以切换菜单" style="cursor:hand" bgcolor="#007BB0" width="0" valign="middle" onclick="switchSysBar()"><img id="switchPoint" src="photo//out.gif" alt="关闭/打开菜单"></td>
    <td style="width:100%" width="99%"> <iframe id="mainFrame" name="mainFrame" style="HEIGHT: 100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 1" scrolling="auto" frameborder="0" src="homepagenew.jsp"></iframe>
    </td>
  </tr>
</table>
</body>
</html>
<%out.flush();%>