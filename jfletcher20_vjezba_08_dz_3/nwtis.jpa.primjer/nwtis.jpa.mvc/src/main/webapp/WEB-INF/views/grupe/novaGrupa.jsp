<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JPA MVC - Dodavanje grupe</title>
        <style type="text/css">
.poruka {
	color: red;
}
        </style>
    </head>
    <body>
        <h1>Dodavanje grupe</h1>
       <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/grupe/pocetak">Početna stranica</a>
            </li>
            <%
            if(request.getAttribute("poruka") != null) {
              String poruka = (String) request.getAttribute("poruka");
              Object oPogreska = request.getAttribute("pogreska");
              boolean pogreska = false;
              System.out.println(oPogreska);
              if(oPogreska != null) {
                pogreska = (Boolean) oPogreska;
              }
              if(poruka.length() > 0) {
                String klasa = "";
                if(pogreska) {
                  klasa = "poruka";
                }
                %>
                <li>
                <p class="<%= klasa%>">${poruka}</p>
                </li>
                <%
              }
            }
            %>  
            <li><p>Podaci grupe:</p>          
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/grupe/dodajGrupu">
                    <table>
                        <tr>
                            <td>Grupa: </td>
                            <td><input name="grupa" size="20" value="${grupa}"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Naziv: </td>
                            <td><input name="naziv" size="50"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dodaj grupu "></td>
                        </tr>                        
                    </table>
                </form>
            </li>                     
        </ul>   
    </body>
</html>
