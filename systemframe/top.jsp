<%@ page contentType="text/html; charset=GBK"%>
<%@ page errorPage="all_errorpage.jsp"%>
<html>
<head>
<title>top</title>
<script language="javascript">
<!--
        function win_open(win_name){
                if(strTmp==''){
                        strTmp=win_name;
                }else{
                        strTmp=strTmp.concat(",",win_name);
                }
        }
        var strTmp="";
        function pop_window(win_name,url){
                win_open(win_name);
                eval(win_name+"=window.open(url,'"+win_name+"','menubar=yes');");
        }
        function close_all(){

                var tmp_window='';
                while(strTmp!=''){
                        if(strTmp.indexOf(',')!=-1){
                                tmp_window=strTmp.substring(0,strTmp.indexOf(','));
                                eval(tmp_window+".document.write('<center>test!</center>')");
                                eval(tmp_window+".close();");
                                strTmp=strTmp.substring(strTmp.indexOf(',')+1);
                        }else{
                                eval(strTmp+".close();");
                                strTmp='';
                        }
                }
//                var newwin = window.open("","testwindow","");
//                if(newwin.parent.closed){
//
//                  newwin.location="/login.do?step=logout";
//                }
//                else{
//
//                  newwin.close();
//                }
        }
//-->
</script>
</head>

<body leftmargin="0" topmargin="0" onUnload="close_all()">
<table width="100%" height="45" border="0" cellpadding="0" cellspacing="0" bgcolor="006699">
  <tr>
    <td><div align="left"><img src="1024-top1.jpg" width="473" height="45">
     
    </div></td>
    <td></td>
    <td><div align="right"><img src="1024-top3.gif" width="551" height="45"></div></td>
  </tr>
</table>
</body>
</html>

<%out.flush();%>