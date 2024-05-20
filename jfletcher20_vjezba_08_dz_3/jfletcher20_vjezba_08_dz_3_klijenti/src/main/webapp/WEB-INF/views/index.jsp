<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
		<h1>Početna</h1>
		<a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
			stranica</a>
		<a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
			svih kazni</a>
		<h2>Pretraživanje kazni u intervalu</h2>
		<form method="post"
			action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni"
			class="fixed-form">
			<input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
			<fieldset>
			<label for="odVremena">Od vremena
				<input name="odVremena"	min=0 type="number">
			</label>
			<label for="doVremena">Do vremena
				<input name="doVremena" min=0 type="number">
			</label>
			</fieldset>
			<input type="submit" value=" Dohvati kazne ">
		</form>
	</div>
</body>
</html>
