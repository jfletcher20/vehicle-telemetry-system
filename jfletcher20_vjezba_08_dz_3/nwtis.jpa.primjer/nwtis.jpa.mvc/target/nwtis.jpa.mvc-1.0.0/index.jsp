<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JPA MVC - Početna stranica</title>
    </head>
    <body>
        <h1>JPA MVC - Početna stranica</h1>
        <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Rad s korisnicima</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/grupe/pocetak">Rad s grupama</a>
            </li>
        </ul>          
    </body>
</html>
