<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Voznja" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pregled simulacija</title>
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
				<a href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetna">🏠 Kontroler simulacija</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis kazni</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis radara</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/vozila/ispisVozila">Ispis vozila</a>
				<a class="current-tab" href="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisVoznji">Ispis simulacija</a>
			</nav>
	        <h1>Pregled simulacija voznji</h1>
	        <table>
			    <tr>
			        <th>R.br.</th>
			        <th>ID</th>
			        <th>Vrijeme</th>
			        <th>Brzina</th>
			        <th>Uk. km</th>
			        <th>GPS širina</th>
			        <th>GPS dužina</th>
			        <th>Ekstra</th>
			    </tr>
				<%
				int i = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
				List<Voznja> voznje = (List<Voznja>) request.getAttribute("voznje");
				String mult = voznje.size() != 1 ? "a" : "";
				for(Voznja v: voznje) {
					i++;
					Date vrijeme = new Date(v.getVrijeme() * 1000);%>
					<tr>
						<td class="desno"><%= i %></td>
						<td><a href="${pageContext.servletContext.contextPath}/mvc/simulacije/<%= v.getId() %>/ispisVoznji"><%= v.getId() %></a></td>
						<td style="font-size: 10px;"><%= sdf.format(vrijeme) %></td>
			            <td><%= v.getBrzina() %></td>
			            <td><%= v.getUkupnoKm() %></td>
			            <td><%= v.getGpsSirina() %></td>
			            <td><%= v.getGpsDuzina() %></td>
			            <td>
			                <button onclick="toggleDetalji('detalji<%= i %>')">Svi podaci</button>
			            </td>
			        </tr>
			        <tr id="detalji<%= i %>" hidden>
			            <td colspan="8">
			                <table>
			                    <tr><td>Snaga:</td><td><%= v.getSnaga() %></td></tr>
			                    <tr><td>Struja:</td><td><%= v.getStruja() %></td></tr>
			                    <tr><td>Visina:</td><td><%= v.getVisina() %></td></tr>
			                    <tr><td>GPS Brzina:</td><td><%= v.getGpsBrzina() %></td></tr>
			                    <tr><td>Napon Baterija:</td><td><%= v.getNaponBaterija() %></td></tr>
			                    <tr><td>Temp Vozila:</td><td><%= v.getTempVozila() %></td></tr>
			                    <tr><td>Postotak Baterija:</td><td><%= v.getPostotakBaterija() %></td></tr>
			                    <tr><td>Kapacitet Baterija:</td><td><%= v.getKapacitetBaterija() %></td></tr>
			                    <tr><td>Temp Baterija:</td><td><%= v.getTempBaterija() %></td></tr>
			                    <tr><td>Preostalo Km:</td><td><%= v.getPreostaloKm() %></td></tr>
			                </table>
			            </td>
			        </tr>
			    <%
			    }
			    %>
				<tfoot><td></td><td></td><td style="text-align: center">Prikazano: <%= voznje.size() %> rezultat<%= mult %></td></tfoot>
				<p><%= (String) request.getAttribute("vrijednosti") != null ? (String) request.getAttribute("vrijednosti") : "" %></p>
	        </table>
        </div>
    </body>
</html>
