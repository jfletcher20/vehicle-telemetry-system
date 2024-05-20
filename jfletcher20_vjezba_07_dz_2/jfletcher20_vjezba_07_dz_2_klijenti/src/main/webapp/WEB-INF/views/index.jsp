<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Kazna" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>REST MVC</title>
		<link rel="stylesheet"
			href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
	</head>
	<body>
		<div class="card">
			<nav>
				<a class="current-tab" href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
					stranica</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
					svih kazni</a>
			</nav>
			<h1>Početna</h1>
			<h2>Pretraživanje kazni u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
				class="fixed-form">
				<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
				<fieldset>
				<%
				List<Kazna> kazne = (List<Kazna>) request.getAttribute("kazne");
				kazne.sort((a, b) -> a.getVrijemeKraj() > b.getVrijemeKraj() ? 1 : a.getVrijemeKraj() == b.getVrijemeKraj() ? 0 : -1);
				long odVremena = kazne.size() > 0 ? kazne.get(0).getVrijemeKraj() : 0;
				long doVremena = kazne.size() > 0 ? kazne.get(kazne.size() - 1).getVrijemeKraj() : 0;
				%>
				<div style="display: flex; flex-direction: row;">
					<label for="odVremena">Od vremena
						<input name="odVremena"	min=0 type="number" value="<%= odVremena %>">
					</label>
					<label for="doVremena">Do vremena
						<input name="doVremena" min=0 type="number" value="<%= doVremena %>">
					</label>
				</div>
				</fieldset>
				<input type="submit" value="Dohvati kazne">
			</form>
		</div>
	</body>
</html>
