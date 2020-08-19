(function($) {
    "use strict";
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
})(jQuery);


$(document).ready(function() {
    $('#dataTable').DataTable();

    $("#state").change(function() {
        sendAjaxStateRequest();
    });
    $("#make").change(function() {
        sendAjaxMakeRequest();
    });
});

function sendAjaxStateRequest() {
    var state = $("#state").val();
    $.get( "/terminals/getlgas?state=" + state, function( data ) {
        $("#lga").empty();
        data.forEach(function(item, i) {
            var option = "<option value = " + item + ">" + item +  "</option>";
            $("#lga").append(option);
        });
    });
};

function sendAjaxMakeRequest() {
    var make = $("#make").val();
    $.get( "/vehicleCategories/getmodels?make=" + make, function( data ) {
        $("#model").empty();
        data.forEach(function(item, i) {
            var option = "<option value = " + item.id + ">" + item.name +  "</option>";
            $("#model").append(option);
        });
    });
};

/*
$(function() {
 'use strict';
  // Autocomplete
  $('#location').autocomplete({
	  minLength: 1,
	  delay: 50,
	  source: function (request, response) {
		$.getJSON(
		 'http://gd.geobytes.com/AutoCompleteCity?callback=?&q='+request.term,
		  function (data) {
			 response(data);
		}
	);
	},
  });
});*/
