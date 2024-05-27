<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Kazna" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pregled kazni</title>
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
    <script>
	    function toggleDetalji(id) {
	        var detalji = document.getElementById(id);
	        detalji.hidden = !detalji.hidden;
	    }
	</script>
    </head>
    <body>
    	<div class="card">
			<nav id="page-nav">
				<a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetna">🏠 Kontroler kazni</a>
				<a class="current-tab" href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis kazni</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis radara</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/vozila/ispisVozila">Ispis vozila</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisVoznji">Ispis simulacija</a>
			</nav>
	        <h1>Pregled kazni</h1>
	        <table>
		        <tr>
			        <th>R.br.</th>
			        <th>Vozilo</th>
			        <th>Vrijeme</th>
			        <th>Brzina</th>
			        <th>GPS širina</th>
			        <th>GPS dužina</th>
			        <th>Ekstra</th>
		        </tr>
				<%
				int i = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
				List<Kazna> kazne = (List<Kazna>) request.getAttribute("kazne");
				String mult = kazne.size() != 1 ? "a" : "";
				for(Kazna k: kazne) {
					i++;
					Date vrijeme = new Date(k.getVrijemeKraj() * 1000);%>
					<tr>
						<td class="desno"><%= i %></td>
						<td><a href="${pageContext.servletContext.contextPath}/mvc/kazne/<%= k.getId() %>/ispisKazni"><%= k.getId() %></a></td>
						<td style="font-size: 10px;"><%= sdf.format(vrijeme) %></td>
						<td><%= k.getBrzina() %></td>
						<td><%= k.getGpsSirina() %></td>
						<td><%= k.getGpsDuzina() %></td>
			            <td>
			                <button onclick="toggleDetalji('detalji<%= i %>')">Svi podaci</button>
			            </td>
			        </tr>
			        <tr id="detalji<%= i %>" hidden>
			            <td colspan="8">
			                <table>
			                    <tr><td>Vrijeme Početak:</td><td><%= sdf.format(new Date(k.getVrijemePocetak() * 1000)) %></td></tr>
			                    <tr><td>Vrijeme Kraj:</td><td><%= sdf.format(new Date(k.getVrijemeKraj() * 1000)) %></td></tr>
			                    <tr><td>GPS Širina Radar:</td><td><%= k.getGpsSirinaRadar() %></td></tr>
			                    <tr><td>GPS Dužina Radar:</td><td><%= k.getGpsDuzinaRadar() %></td></tr>
			                </table>
			            </td>
			        </tr><%
			    }%>
				<tfoot><td></td><td></td><td style="text-align: center">Prikazano: <%= kazne.size() %> rezultat<%= mult %></td></tfoot>
				<p><%= (String) request.getAttribute("vrijednosti") != null ? (String) request.getAttribute("vrijednosti") : "" %></p>
	        </table>
        </div>
    </body>
</html>
