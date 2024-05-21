document.addEventListener("DOMContentLoaded", (e) => {
	
	const rb = document.getElementById("rb");
	const idVozila = document.getElementById("idVozila");
	const odVremena = document.getElementById("odVremena");
	const doVremena = document.getElementById("doVremena");
	const forma = document.getElementById("forma-kazne");
	
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