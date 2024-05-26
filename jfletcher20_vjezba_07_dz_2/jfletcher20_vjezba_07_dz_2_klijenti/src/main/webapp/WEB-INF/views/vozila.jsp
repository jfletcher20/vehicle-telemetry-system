<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Voznja" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pregled simulacija</title>
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
    </head>
    <body>
    	<div class="card">
			<nav id="page-nav">
				<a href="${pageContext.servletContext.contextPath}">Početna</a>
				<a class="current-tab" href="${pageContext.servletContext.contextPath}/mvc/vozila/ispisVozila">Ispis vozila</a>
			</nav>
	        <h1>Pregled vozila</h1>
	        <table>
		        <tr><th>R.br.<th>ID</th><th>Vrijeme</th><th>Brzina</th><th>Uk. km</th><th>GDP širina</th><th>GPS dužina</th></tr>
				<%
				int i = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
				List<Vozila> vozila = (List<Vozila>) request.getAttribute("vozila");
				String mult = voznje.size() != 1 ? "a" : "";
				for(Vozila v: vozila) {
					i++;
					Date vrijeme = new Date(v.getVrijeme() * 1000);%>
					<tr>
						<td class="desno"><%= i %></td>
						<td><%= v.getId() %></td>
						<td><%= sdf.format(vrijeme) %></td>
						<td><%= v.getBrzina() %></td>
						<td><%= v.getUkupnoKm() %></td>
						<td><%= v.getGpsSirina() %></td>
						<td><%= v.getGpsDuzina() %></td>
					</tr><%
				}%>
				<tfoot><td></td><td></td><td style="text-align: center">Prikazano: <%= vozila.size() %> rezultat<%= mult %></td></tfoot>
				<p><%= (String) request.getAttribute("vrijednosti") != null ? (String) request.getAttribute("vrijednosti") : "" %></p>
	        </table>
        </div>
    </body>
</html>
