<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Kazna" %>

<%!

String sadrzaj(String datoteka, ServletContext application) {
  String html = application.getRealPath("/resources/html/forms/" + datoteka + ".html");
  try {
      java.nio.file.Path path = java.nio.file.Paths.get(html);
      String sadrzaj = new String(java.nio.file.Files.readAllBytes(path), "UTF-8");
      return sadrzaj + "\n";
  } catch (Exception e) {
      return "Error u citanju datoteke: " + e.getMessage() + "\n";
  }
}

%>

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
				<a class="current-tab" href="${pageContext.servletContext.contextPath}">Početna</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis kazni</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis radara</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/vozila/ispisVozila">Ispis vozila</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisVoznji">Ispis simulacija</a>
			</nav>
			<h1>MVC</h1>
			<h4>Sustav za telemetriju i praćenje e-vozila.</h4>
			<div id="tab-kazne-content" class="tab" hidden>
				<h2>Upravljanje kaznama</h2>
				<p>Unosom validnog indeksa (rednog broja) kazne, unos ostalih vrijednosti će se onemogućiti jer je njih tada nepotrebno unijeti.</p>
				<p>Unosom validne vrijednosti u nekom od preostalih polja, unos indeksa kazne će se onemogućiti jer je tada besmisleno unijeti indeks kazne.</p>
				<p>Unosom ID-a vozila, rezultat će biti samo kazne za zadano vozilo.</p>
				<form id="forma-kazne" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
					<fieldset>
						<div class="row">
							<label for="rb">Indeks kazne
								<input name="rb" id="rb" min=0 type="number" pattern="[0-9]">
							</label>
							<label for="idVozila">ID Vozila
								<input name="idVozila" id="idVozila" type="number" pattern="[0-9]">
							</label>
						</div>
						<div class="row">
							<label for="odVremena">Od vremena
								<input name="odVremena"	id="odVremena" min=0 type="number" pattern="[0-9]">
							</label>
							<label for="doVremena">Do vremena
								<input name="doVremena" id="doVremena"  min=0 type="number" pattern="[0-9]">
							</label>
						</div>
					</fieldset>
					<input type="submit" value="Dohvati kazne">
				</form>
			</div>
			<div id="tab-radari-content" class="tab" hidden>
				<h2>Upravljanje radarima</h2>
				<form id="forma-radari" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/radari/pretrazivanjeRadara"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
	                <% out.println(sadrzaj("radari-forma", application)); %>
                </form>
			</div>
			<div id="tab-vozila-content" class="tab">
				<h2>Upravljanje vozilima</h2>
				<form id="forma-vozila" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjeVozila"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
	                <% out.println(sadrzaj("vozila-forma", application)); %>
				</form>
				<h2>Dodavanje vozila</h2>
				<form id="forma-vozila-post" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/vozila/postJSON"
					class="fixed-form">
	                <% out.println(sadrzaj("vozila-forma-post", application)); %>
				</form>
			</div>
			<div id="tab-simulacije-content" class="tab" hidden>
				<h2>Upravljanje simulacijama</h2>
				<form id="forma-simulacije" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeVoznji"
					class="fixed-form">
					<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
	                <% out.println(sadrzaj("simulacije-forma", application)); %>
				</form>
				<h2>Dodavanje voznji simulaciji</h2>
				<form id="forma-simulacije-post" method="post"
					action="${pageContext.servletContext.contextPath}/mvc/simulacije/dodajSimulacijuVoznje"
					class="fixed-form">
					<% out.println(sadrzaj("simulacije-forma-post", application)); %>
				</form>
			</div>
			<nav id="tab-nav">
				<a class="tab-nav" id="tab-kazne" href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetna">Kazne</a>
				<a class="tab-nav" id="tab-radari" href="${pageContext.servletContext.contextPath}/mvc/radari/pocetna">Radari</a>
				<a class="current-tab tab-nav" id="tab-vozila" href="${pageContext.servletContext.contextPath}/mvc/vozila/pocetna">Vozila</a>
				<a class="tab-nav" id="tab-simulacije" href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetna">Simulacije</a>
			</nav>
		</div>
	</body>
</html>
