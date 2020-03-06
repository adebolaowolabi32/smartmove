(function($) {
    "use strict";
		var urlParams = new URLSearchParams(window.location.search);
		var role = urlParams.get('role');
		if(role == 'operator'){
			$('#layoutSidenav_nav').load('../operators/sidebar.html');
		}
		else if(role == 'regulator'){
			$('#layoutSidenav_nav').load('../regulators/sidebar.html');
		}
		else if(role == 'agent'){
			$('#layoutSidenav_nav').load('../agents/sidebar.html');
		}

		$('#dash').attr('href','../dashboards/index.html?role=' + role);
		$('#header').load('../../header.html');
		$('#footer').load('../../footer.html');
		// Add active state to sidbar nav links
		var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
			$("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
				if (this.href === path) {
					$(this).addClass("active");
				}
			});

		// Toggle the side navigation
		$("#sidebarToggle").on("click", function(e) {
			e.preventDefault();
			$("body").toggleClass("sb-sidenav-toggled");
		});
		
		$.ajaxSetup({
			   headers:{
				  'Authorization': Cookies.get('auth')
			   }
		});
})(jQuery);


