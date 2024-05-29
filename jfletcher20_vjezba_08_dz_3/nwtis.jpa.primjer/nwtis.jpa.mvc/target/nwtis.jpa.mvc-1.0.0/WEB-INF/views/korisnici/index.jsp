<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JPA MVC - Rad s korisnicima</title>
    </head>
    <body>
        <h1>JPA MVC - Rad s korisnicima</h1>
        <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/index.jsp">Početna stranica</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/grupe/pocetak">Rad s grupama</a>
            </li>            
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/noviKorisnik">Dodavanje novog korisnika</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/ispisKorisnika">Ispis korisnika</a>
            </li>
            <li>
            <h2>Pretraživanje korisnika</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/korisnici/pretrazivanjeKorisnika">
                    <table>
                        <tr>
                            <td>Ime: </td>
                            <td><input name="ime"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Prezime: </td>
                            <td><input name="prezime"/>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati korisnike "></td>
                        </tr>                        
                    </table>
                </form>
            </li>                     
        </ul>          
    </body>
</html>
