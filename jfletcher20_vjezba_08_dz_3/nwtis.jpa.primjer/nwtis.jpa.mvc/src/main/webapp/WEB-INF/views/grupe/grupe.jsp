<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List,edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Korisnici,edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Grupe"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JPA MVC - Pregled grupa</title>
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
	<h1>REST MVC - Pregled grupa</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/grupe/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	<%
	if (request.getAttribute("brojGrupa") != null) {
	  Integer brojGrupa = (Integer) request.getAttribute("brojGrupa");
	%>
	<p>Ukupan broj grupa: ${brojGrupa}</p>
	<%
	}
	%>
	<table>
		<tr>
			<th>R.br.
			<th>Grupa</th>
			<th>Naziv</th>
			<th>Korisnici</th>
		</tr>
		<%
		int i = 0;
		List<Grupe> grupe = (List<Grupe>) request.getAttribute("grupe");

		for (Grupe g : grupe) {
		  i++;
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><%=g.getGrupa()%></td>
			<td><%=g.getNaziv()%></td>
			<td>
				<%
				int j = 0;
				for (Korisnici k : g.getKorisnicis()) {
				  if (j > 0) {
				%>, <%
				}
				j++;
				%><%=k.getKorisnik()%>:<%=k.getIme()%> <%=k.getPrezime()%><%
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
