/*
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
			<form id="forma" method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
				class="fixed-form">
				<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
				<fieldset>
				<%
				List<Kazna> kazne = (List<Kazna>) request.getAttribute("kazne");
				kazne.sort((a, b) -> a.getVrijemeKraj() > b.getVrijemeKraj() ? 1 : a.getVrijemeKraj() == b.getVrijemeKraj() ? 0 : -1);
				long odVremena = kazne.size() > 0 ? kazne.get(0).getVrijemeKraj() : 0;
				long doVremena = kazne.size() > 0 ? kazne.get(kazne.size() - 1).getVrijemeKraj() : 0;
				int redniBrojMax = kazne.size();
				%>
				<div class="row">
					<label for="rb">Redni broj
						<input name="rb" id="rb" min=1 max=redniBrojMax type="number">
					</label>
					<label for="doVremena">ID Vozila
						<input name="idVozila" id="idVozila" min=0 type="number">
					</label>
				</div>
				<div class="row">
					<label for="odVremena">Od vremena
						<input name="odVremena"	id="odVremena" min=0 type="number" value="<%= odVremena %>">
					</label>
					<label for="doVremena">Do vremena
						<input name="doVremena" id="doVremena"  min=0 type="number" value="<%= doVremena %>">
					</label>
				</div>
				</fieldset>
				<input type="submit" value="Dohvati kazne">
			</form>
		</div>
	</body>
</html>
*/

document.addEventListener("DOMContentLoaded", (e) => {
	
	const rb = document.getElementById("rb");
	const idVozila = document.getElementById("idVozila");
	const odVremena = document.getElementById("odVremena");
	const doVremena = document.getElementById("doVremena");
	const form = document.getElementById("forma");
	
	forma.reset();
	idVozila.placeholder = rb.placeholder = odVremena.placeholder = doVremena.placeholder = "";
	
	function zamijeniVrijednost(element) {
		if (element.value === "") {
			element.value = element.placeholder;
			element.placeholder = "";
		} else {
			element.placeholder = element.value;
			element.value = "";
		}
	}
	
	function omoguciUnos(element) {
		element.disabled = false;
		zamijeniVrijednost(element);
	}
	
	function onemoguciUnos(element) {
		element.disabled = true;
		zamijeniVrijednost(element);
	}
	
	rb.addEventListener("input", () => {
		if (rb.value !== "") {
            [idVozila, odVremena, doVremena].forEach(onemoguciUnos);
		} else {
			[idVozila, odVremena, doVremena].forEach(omoguciUnos);
		}
	});
	
	function onemoguciRb() {
		var vrijednost = idVozila.value + odVremena.value + doVremena.value;
		if (vrijednost !== "") {
			rb.disabled = true;
			zamijeniVrijednost(rb);
		} else {
			rb.disabled = false;
			zamijeniVrijednost(rb);
		}
	}
	
	[idVozila, odVremena, doVremena].forEach((element) => element.addEventListener("input", onemoguciRb));
	onemoguciRb();
	
});