<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Radar" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pregled radara</title>
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
    </head>
    <body>
    	<div class="card">
			<nav id="page-nav">
				<a href="${pageContext.servletContext.contextPath}">Početna</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis kazni</a>
				<a class="current-tab" href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis radara</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/vozila/ispisVozila">Ispis vozila</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisVoznji">Ispis simulacija</a>
			</nav>
	        <h1>Pregled radara</h1>
	        <table>
		        <tr><th>R.br.<th>Radar</th><th>Maks udaljenost</th><!-- <th>Maks brzina</th> --><th>GPS širina</th><th>GPS dužina</th></tr>
				<%
				int i = 0;
				List<Radar> radari = (List<Radar>) request.getAttribute("radari");
				String mult = radari.size() != 1 ? "a" : "";
				for(Radar r: radari) {
					i++;%>
					<tr>
						<td class="desno"><%= i %></td>
						<td><%= r.getId() %></td>
						<td><%= r.getMaksUdaljenost() %></td>
						<!-- <td><%= ""/*r.getMaksBrzina()*/ %></td> -->
						<td><%= r.getGpsSirina() %></td>
						<td><%= r.getGpsDuzina() %></td>
					</tr><%
				}%>
				<tfoot><td></td><td></td><td style="text-align: center">Prikazano: <%= radari.size() %> rezultat<%= mult %></td></tfoot>
				<p><%= (String) request.getAttribute("vrijednosti") != null ? (String) request.getAttribute("vrijednosti") : "" %></p>
	        </table>
        </div>
    </body>
</html>
