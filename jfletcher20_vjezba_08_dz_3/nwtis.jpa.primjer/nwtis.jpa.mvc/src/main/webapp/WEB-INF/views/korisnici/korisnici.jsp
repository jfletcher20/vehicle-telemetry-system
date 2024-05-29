<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List,edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Korisnici,edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Grupe"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JPA MVC - Pregled korisnika</title>
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
</head>
<body>
	<h1>JPA MVC - Pregled korisnika</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	<%
	if (request.getAttribute("brojKorisnika") != null) {
	  Integer brojKorisnika = (Integer) request.getAttribute("brojKorisnika");
	%>
	<p>Ukupan broj korisnika: ${brojKorisnika}</p>
	<%
	}
	%>
	<table>
		<tr>
			<th>R.br.
			<th>Korisnik</th>
			<th>Ime</th>
			<th>Prezime</th>
			<th>Email</th>
			<th>Grupe</th>
		</tr>
		<%
		int i = 0;
		List<Korisnici> korisnici = (List<Korisnici>) request.getAttribute("korisnici");
		for (Korisnici k : korisnici) {
		  i++;
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/mvc/korisnici/pridruziGrupe?korisnik=<%= k.getKorisnik() %>"><%=k.getKorisnik()%></a></td>
			<td><%=k.getIme()%></td>
			<td><%=k.getPrezime()%></td>
			<td><%=k.getEmail()%></td>
			<td>
				<%
				int j = 0;
				for (Grupe g : k.getGrupes()) {
				  if (j > 0) {
				%>, <%
				}
				j++;
				%><%=g.getGrupa()%>
				<%
				}
				%>
			</td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
