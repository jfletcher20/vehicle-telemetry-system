<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JPA MVC - Rad s grupama</title>
    </head>
    <body>
        <h1>JPA MVC - Rad s grupama</h1>
        <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/index.jsp">Početna stranica</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Rad s korisnicima</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/grupe/novaGrupa">Dodavanje nove grupe</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/grupe/ispisGrupa">Ispis grupa</a>
            </li>
            <li>
            <h2>Pretraživanje grupa</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/grupe/pretrazivanjeGrupa">
                    <table>
                        <tr>
                            <td>Grupa: </td>
                            <td><input name="ime"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Naziv: </td>
                            <td><input name="naziv"/>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati grupe "></td>
                        </tr>                        
                    </table>
                </form>
            </li>                     
        </ul>          
    </body>
</html>
