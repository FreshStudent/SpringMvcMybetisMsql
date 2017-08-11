<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
 <body>  
        <h1>Please upload a file</h1>  
<!--   enctype(编码格式)必须为multipart/form-data  -->  
        <form method="post" action="<%=basePath %>helloworld/getFormInfo.spring" enctype="multipart/form-data">  
            <input type="text" name="name"/>  
            <input type="file" name="file"/>  
            <input type="submit"/>  
        </form>  
    </body>
</html>