/*


			<div id="tab-kazne-content">
			</div>
			<div id="tab-radari-content">
			</div>
			<nav id="tab-nav">
				<a class="current-tab tab-nav" id="tab-kazne">Kazne</a>
				<a class="tab-nav" id="tab-radari">Radari</a>
				<a class="tab-nav" id="tab-simulacije">Simulacije</a>
				<a class="tab-nav" id="tab-vozila">Vozila</a>
			</nav>
	
			
*/

// when a given tab link is clicked, show the corresponding tab content
document.addEventListener("DOMContentLoaded", (e) => {
	const tabs = document.querySelectorAll(".tab-nav");
	// when a tab from tabs is clicked, show the corresponding tab content by accessing the tab's id and adding "-content" to it
	tabs.forEach(tab => {
		tab.addEventListener("click", (e) => {
			const tabId = e.target.id;
			const tabContent = document.getElementById(tabId + "-content");
			const tabContents = document.querySelectorAll(".tab");
			tabContents.forEach(content => content.hidden = true);
			tabContent.hidden = false;
			tabs.forEach(tab => tab.classList.remove("current-tab"));
			tab.classList.add("current-tab");
		});
	});
	
});