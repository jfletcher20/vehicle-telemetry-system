<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Kazna" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pregled kazni</title>
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
    </head>
    <body>
    	<div class="card">
			<nav id="page-nav">
				<a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
					stranica</a>
				<a class="current-tab" href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
					svih kazni</a>
			</nav>
	        <h1>Pregled kazni</h1>
	        <table>
		        <tr><th>R.br.<th>Vozilo</th><th>Vrijeme</th><th>Brzina</th><th>GDP širina</th><th>GPS dužina</th></tr>
				<%
				int i = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
				List<Kazna> kazne = (List<Kazna>) request.getAttribute("kazne");
				String mult = kazne.size() != 1 ? "a" : "";
				for(Kazna k: kazne) {
					i++;
					Date vrijeme = new Date(k.getVrijemeKraj() * 1000);%>
					<tr><td class="desno"><%= i %></td><td><%= k.getId() %></td><td><%=  sdf.format(vrijeme) %></td><td><%= k.getBrzina() %></td><td><%= k.getGpsSirina() %></td><td><%= k.getGpsDuzinaRadar() %></td></tr>	  
					<%
				}
				%>
				<tfoot><td></td><td></td><td style="text-align: center">Prikazano: <%= kazne.size() %> rezultat<%= mult %></td></tfoot>
				<p><%= (String) request.getAttribute("values") != null ? (String) request.getAttribute("values") : "" %></p>
	        </table>
        </div>
    </body>
</html>
