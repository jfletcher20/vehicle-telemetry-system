<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Kazna" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>REST MVC</title>
		<link rel="stylesheet"
			href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
		<script src="${pageContext.servletContext.contextPath}/resources/scripts/forma.js"></script>
		<script src="${pageContext.servletContext.contextPath}/resources/scripts/nav.js"></script>
	</head>
	<body>
		<div class="card">
			<nav id="page-nav">
				<a class="current-tab" href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
					stranica</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
					svih kazni</a>
			</nav>
			<h1>Pregled podataka</h1>
			<div id="tab-kazne-content" class="tab">
				<h2>Pretraživanje kazni</h2>
				<p>Unosom validnog indeksa (rednog broja) kazne, unos ostalih vrijednosti će se onemogućiti jer je njih tada nepotrebno unijeti.</p>
				<p>Unosom validne vrijednosti u nekom od preostalih polja, unos indeksa kazne će se onemogućiti jer je tada besmisleno unijeti indeks kazne.</p>
				<p>Unosom ID-a vozila, rezultat će biti samo kazne za zadano vozilo.</p>
				<form id="forma-kazne" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
					<fieldset>
						<%
						List<Kazna> kazne = (List<Kazna>) request.getAttribute("kazne");
						kazne.sort((a, b) -> a.getVrijemeKraj() > b.getVrijemeKraj() ? 1 : a.getVrijemeKraj() == b.getVrijemeKraj() ? 0 : -1);
						long odVremena = kazne.size() > 0 ? kazne.get(0).getVrijemeKraj() : 0;
						long doVremena = kazne.size() > 0 ? kazne.get(kazne.size() - 1).getVrijemeKraj() : 0;
						int redniBrojMax = kazne.size() - 1;
						%>
						<div class="row">
							<label for="rb">Indeks kazne
								<input name="rb" id="rb" min=0 max=<%= redniBrojMax %> type="number" pattern="[0-9]">
							</label>
							<label for="idVozila">ID Vozila
								<input name="idVozila" id="idVozila" type="number" pattern="[0-9]">
							</label>
						</div>
						<div class="row">
							<label for="odVremena">Od vremena
								<input name="odVremena"	id="odVremena" min=0 type="number" value=<%= odVremena %> pattern="[0-9]">
							</label>
							<label for="doVremena">Do vremena
								<input name="doVremena" id="doVremena"  min=0 type="number" value=<%= doVremena %> pattern="[0-9]">
							</label>
						</div>
					</fieldset>
					<input type="submit" value="Dohvati kazne">
				</form>
			</div>
			<div id="tab-radari-content" class="tab" hidden>
				<h2>Pretraživanje radara</h2>
				<form id="forma-radari" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
					<fieldset>
						<div class="row">
							<label for="rb">Indeks kazne
								<input name="rb" id="rb" min=0 max=<%= redniBrojMax %> type="number" pattern="[0-9]">
							</label>
							<label for="idVozila">ID Vozila
								<input name="idVozila" id="idVozila" type="number" pattern="[0-9]">
							</label>
						</div>
						<div class="row">
							<label for="odVremena">Od vremena
								<input name="odVremena"	id="odVremena" min=0 type="number" value=<%= odVremena %> pattern="[0-9]">
							</label>
							<label for="doVremena">Do vremena
								<input name="doVremena" id="doVremena"  min=0 type="number" value=<%= doVremena %> pattern="[0-9]">
							</label>
						</div>
					</fieldset>
					<input type="submit" value="Dohvati radare">
				</form>
			</div>
			<div id="tab-simulacije-content" class="tab" hidden>
				<h2>Pretraživanje simulacija</h2>
				<form id="forma-kazne" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
					<fieldset>
						<div class="row">
							<label for="rb">Indeks kazne
								<input name="rb" id="rb" min=0 max=<%= redniBrojMax %> type="number" pattern="[0-9]">
							</label>
							<label for="idVozila">ID Vozila
								<input name="idVozila" id="idVozila" type="number" pattern="[0-9]">
							</label>
						</div>
						<div class="row">
							<label for="odVremena">Od vremena
								<input name="odVremena"	id="odVremena" min=0 type="number" value=<%= odVremena %> pattern="[0-9]">
							</label>
							<label for="doVremena">Do vremena
								<input name="doVremena" id="doVremena"  min=0 type="number" value=<%= doVremena %> pattern="[0-9]">
							</label>
						</div>
					</fieldset>
					<input type="submit" value="Dohvati simulacije">
				</form>
			</div>
			<div id="tab-vozila-content" class="tab" hidden>
				<h2>Pretraživanje vozila</h2>
				<form id="forma-kazne" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
					<fieldset>
						<div class="row">
							<label for="rb">Indeks kazne
								<input name="rb" id="rb" min=0 max=<%= redniBrojMax %> type="number" pattern="[0-9]">
							</label>
							<label for="idVozila">ID Vozila
								<input name="idVozila" id="idVozila" type="number" pattern="[0-9]">
							</label>
						</div>
						<div class="row">
							<label for="odVremena">Od vremena
								<input name="odVremena"	id="odVremena" min=0 type="number" value=<%= odVremena %> pattern="[0-9]">
							</label>
							<label for="doVremena">Do vremena
								<input name="doVremena" id="doVremena"  min=0 type="number" value=<%= doVremena %> pattern="[0-9]">
							</label>
						</div>
					</fieldset>
					<input type="submit" value="Dohvati vozila">
				</form>
			</div>
			<nav id="tab-nav">
				<a class="current-tab tab-nav" id="tab-kazne">Kazne</a>
				<a class="tab-nav" id="tab-radari">Radari</a>
				<a class="tab-nav" id="tab-simulacije">Simulacije</a>
				<a class="tab-nav" id="tab-vozila">Vozila</a>
			</nav>
		</div>
	</body>
</html>
