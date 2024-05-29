<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.Properties,edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Korisnici,edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Grupe"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JPA MVC - Prodruživanje grupa korisnika</title>
<style type="text/css">
table, th, td {
	border: 1px solid;
}

th {
	text-align: center;
	font-weight: bold;
}

.desno {
	text-align: right;
}
</style>
<script type="text/javascript">
	function obrisiGrupe() {
		azurirajGrupe("grupeOdabrane", "grupeOstale");
	}

	function dodajGrupe() {
		azurirajGrupe("grupeOstale", "grupeOdabrane");
	}

	function azurirajGrupe(grupeOdabrane, grupeOstale) {
		var grupeOdabrane = document.getElementById(grupeOdabrane);
		var grupeOstale = document.getElementById(grupeOstale);
		var broj = grupeOdabrane.selectedOptions.length;
		for (var i = 0; i < broj; i++) {
			var zaBrisanje = grupeOdabrane.selectedOptions[i];
			grupeOstale.appendChild(new Option(zaBrisanje.text,
					zaBrisanje.value));
		}
		for (var i = 0; i < broj; i++) {
			var zaBrisanje = grupeOdabrane.selectedOptions[broj - i - 1];
			grupeOdabrane.removeChild(zaBrisanje);
		}
	}
	function pripremiGrupe() {
		var grupeOdabrane = document.getElementById("grupeOdabrane");
		var odabraneGrupe = document.getElementById("odabraneGrupe");
		var broj = grupeOdabrane.options.length;
		var odabrano = "";
		for (var i = 0; i < broj; i++) {
			if (i > 0) {
				odabrano += ", ";
			}
			var zaUpis = grupeOdabrane.options[i];
			odabrano += zaUpis.value;
		}
		odabraneGrupe.value = odabrano;
		return true;
	}
	function potvrdiBrisanjeKorisnika() {
		if (confirm("Obrisati korisnika?")) {
			return true;
		} else {
			return false;
		}
	}
</script>
</head>
<body>
	<h1>JPA MVC - Prodruživanje grupa korisnika</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Početna
				stranica</a></li>
		<%
		boolean pogreska = false;
		if (request.getAttribute("poruka") != null) {
		  String poruka = (String) request.getAttribute("poruka");
		  Object oPogreska = request.getAttribute("pogreska");
		  if (oPogreska != null) {
		    pogreska = (Boolean) oPogreska;
		  }
		  if (poruka.length() > 0) {
		    String klasa = "";
		    if (pogreska) {
		  klasa = "poruka";
		    }
		%>
		<li>
			<p class="<%=klasa%>">${poruka}</p>
		</li>
		<%
		}
		}
		%>
	</ul>
	<br />
	<%
	if (!pogreska) {

	  Korisnici korisnik = (Korisnici) request.getAttribute("korisnik");
	  Properties grupe = (Properties) request.getAttribute("grupe");
	  String odabraneGrupePrije = (String) request.getAttribute("odabraneGrupePrije");
	%>
	<ul>
		<li>Korisnik: ${korisnik.getKorisnik()}</li>
		<li>Ime: ${korisnik.getIme()}</li>
		<li>Prezime: ${korisnik.getPrezime()}</li>
		<li>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/korisnici/brisiKorisnika"
				onsubmit="return potvrdiBrisanjeKorisnika()">
				<input type="hidden" name="korisnik"
					value="${korisnik.getKorisnik()}" /> <input type="hidden"
					name="${mvc.csrf.name}" value="${mvc.csrf.token}" /> <input
					type="submit" value=" Obriši korisnika ">
			</form>
		</li>
	</ul>
	<form method="post"
		action="${pageContext.servletContext.contextPath}/mvc/korisnici/pridruziGrupe"
		onsubmit="return pripremiGrupe()">
		<input type="hidden" name="korisnik" value="${korisnik.getKorisnik()}" />
		<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
		<input type="hidden" name="odabraneGrupe" id="odabraneGrupe" value="" />
		<input type="hidden" name="odabraneGrupePrije" id="odabraneGrupePrije"
			value="${odabraneGrupePrije}" />
		<table>
			<tr>
				<td style="width: 300px"><input type="button"
					onclick="obrisiGrupe()" value="Obriši grupe" /><br /> <select
					size="10" id="grupeOdabrane" name="grupeOdabrane"
					multiple="multiple">
						<%
						for (Grupe g : korisnik.getGrupes()) {
						%><option value="<%=g.getGrupa()%>"><%=g.getNaziv()%></option>
						<%
						}
						%>
				</select></td>
				<td style="width: 300px"><input type="button"
					onclick="dodajGrupe()" value="Dodaj grupe" /><br /> <select
					size="10" id="grupeOstale" name="grupeOstale" multiple="multiple">
						<%
						for (Object g : grupe.keySet()) {
						%><option value="<%=g.toString()%>"><%=grupe.get(g).toString()%></option>
						<%
						}
						%>
				</select></td>
			</tr>
			<tr>
				<td colspan="2" style="align-items: center;"><input
					type="submit" value=" Upiši promjene "></td>
			</tr>
		</table>
	</form>
	<%
	}
	%>
</body>
</html>
