<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Početna stranica</title>
    </head>
    <body>
        <h1>REST MVC - Početna stranica</h1>
        <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis svih kazni</a>
            </li>
            <li>
            <h2>Pretraživanje kazni u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni">
                    <table>
                        <tr>
                            <td>Od vremena: </td>
                            <td><input name="odVremena"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Do vremena: </td>
                            <td><input name="doVremena"/>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati kazne "></td>
                        </tr>                        
                    </table>
                </form>
            </li>                     
        </ul>          
    </body>
</html>
