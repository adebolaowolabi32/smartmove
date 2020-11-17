function loadDashboard() {
		var urlParams = new URLSearchParams(window.location.search);
		var role = urlParams.get('role');
		if(role == 'operator'){
			var url = "../../templates/dashboards/operators.html";
		}
		else if(role == 'regulator'){
			var url = "../../templates/dashboards/regulators.html";
		}
		else if(role == 'agent'){
			var url = "../../templates/dashboards/agents.html";
		}
		else url = "";
		
		$('#dashboard').load(url, function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#dashboard').appendChild(clone);	
			addScript('../../assets/demo/chart-area-demo.js');
			addScript('../../assets/demo/chart-bar-demo.js');
			addScript('../../assets/demo/chart-pie-demo.js');

		});	
};

function loadCreateVehicle() {
		$('#main').load("../../templates/vehicles/create.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
		});		
};

function loadCreateAgent() {
		$('#main').load("../../templates/agents/create.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
		});		
};

function loadCreateOperator() {
		$('#main').load("../../templates/operators/create.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
		});		
};

function loadCreateRegulator() {
		$('#main').load("../../templates/regulators/create.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
		});		
};

function loadCreateTerminal() {
		$('#main').load("../../templates/terminals/create.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
		});		
};

function loadCreateRoute() {
		$('#main').load("../../templates/routes/create.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
		});		
};

function loadCreateDevice() {
		$('#main').load("../../templates/devices/create.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
		});		
};

function loadUpdateVehicle() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/vehicles/edit.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveVehicle(id);
		});		
};

function loadUpdateAgent() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/agents/edit.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveUser(id);
		});		
};

function loadUpdateOperator() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/operators/edit.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			
			retrieveUser(id);
		});		
};

function loadUpdateRegulator() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/regulators/edit.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveUser(id);
		});		
};

function loadUpdateTerminal() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/terminals/edit.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveTerminal(id);
		});		
};

function loadUpdateRoute() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/routes/edit.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveRoute(id);
		});		
};

function loadUpdateDevice() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/devices/edit.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);
			retrieveUser(id);
		});		
};


function addScript( src ) {
  var s = document.createElement( 'script' );
  s.setAttribute( 'src', src );
  document.body.appendChild( s );
}

function retrieveVehicle(id) {
	 $.get(url + '/vehicles/' + id, function(data, status){
			$('#name').val(data[0].name);
			$('#regno').val(data[0].regNo);
			$('#owner').val(data[0].owner);
			$('#device').val(data[0].device);
			$('#route').val(data[0].route);

		//alert("Data: " + data + "\nStatus: " + status);
	  }, "json");
}

function retrieveUser(id) {
	 $.get(url + '/users/' + id, function(data, status){
			$('#name').val(data[0].name);
			$('#addr').val(data[0].address);
			$('#owner').val(data[0].emailAddress);
			$('#email').val(data[0].mobileNumber);
			$('#owner').val(data[0].owner);

		//alert("Data: " + data + "\nStatus: " + status);
	  }, "json");
}

function retrieveTerminal(id) {
	 $.get(url + '/terminals/' + id, function(data, status){
			$('#name').val(data[0].name);
			$('#addr').val(data[0].address);
			$('#owner').val(data[0].operator);

		//alert("Data: " + data + "\nStatus: " + status);
	  }, "json");
}


function retrieveRoute(id) {
	 $.get(url + '/routes/' + id, function(data, status){
			$('#name').val(data[0].name);
			$('#start').val(data[0].start);
			$('#stop').val(data[0].stop);
			$('#price').val(data[0].price);

		//alert("Data: " + data + "\nStatus: " + status);
	  }, "json");
}

function retrieveDevice(id) {
	 $.get(url + '/devices/' + id, function(data, status){
			$('#name').val(data[0].name)
			$('#id').val(data[0].deviceId)
			$('#type').val(data[0].type)
			$('#vhardware').val(data[0].versionHardware)
			$('#vsoftware').val(data[0].versionSoftware)
			$('#tnp').val(data[0].periodTransactionUpload)
			$('#gpsp').val(data[0].periodGps)
			$('#ftype').val(data[0].fareType)
			$('#status').val(data[0].statusP)

		//alert("Data: " + data + "\nStatus: " + status);
	  }, "json");
}

function retrieveTransaction(id) {
	 $.get(url + '/transactions/' + id, function(data, status){
			$('#id').val(data[0].id);
			$('#type').val(data[0].type);
			$('#amt').val(data[0].amount);
			$('#did').val(data[0].deviceId);
			$('#date').val(data[0].timeDate);

		//alert("Data: " + data + "\nStatus: " + status);
	  }, "json");
}

function save(url, body) {
	   $.post("demo_test_post.asp", body, 
	  function(data, status){
		  
		  return data;
		//alert("Data: " + data + "\nStatus: " + status);
	  });
}

function getAgents() {
		$('#main').load("../../templates/agents/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
	
			$('#dataTable').DataTable({
			"ajax": "../../data/agent_objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'address' },
				{ data: 'email' },
				{ data: 'phone' },
				{ data: 'regulator' },
				],
			"columnDefs": [{
				"targets": -1,
				"data": null,
				"defaultContent": "<button type='button' class='btn btn-primary'><i class='far fa-eye'></i></button><button type='button' class='btn btn-danger'><i class='far fa-trash-alt'></i></button>"
				}]
			});			
		});
		
		
};

function getOperators() {
		$('#main').load("../../templates/operators/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/operator_objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'position' },
				{ data: 'salary' },
				{ data: 'start_date' },
				{ data: 'office' },
				{ data: 'extn' }
				]
			});			
		});
};

function getRegulators() {
		$('#main').load("../../templates/regulators/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/regulator_objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'position' },
				{ data: 'salary' },
				{ data: 'start_date' },
				{ data: 'office' },
				{ data: 'extn' }
				]
			});			
		});
		
		
};

function getTerminals() {
		$('#main').load("../../templates/terminals/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'position' },
				{ data: 'salary' },
				{ data: 'start_date' },
				{ data: 'office' },
				{ data: 'extn' }
				]
			});			
		});
		
		
};

function getVehicles() {
		$('#main').load("../../templates/vehicles/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'position' },
				{ data: 'salary' },
				{ data: 'start_date' },
				{ data: 'office' },
				{ data: 'extn' }
				]
			});			
		});
		
		
};

function getRoutes() {
		$('#main').load("../../templates/routes/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/route_objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'start' },
				{ data: 'stop' },
				{ data: 'price' }
				]
			});			
		});
		
		
};

function getDevices() {
		$('#main').load("../../templates/devices/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/device_objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'position' },
				{ data: 'salary' },
				{ data: 'start_date' },
				{ data: 'office' },
				{ data: 'extn' }
				]
			});			
		});
		
		
};

function getTransactions() {
		$('#main').load("../../templates/transactions/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'position' },
				{ data: 'salary' },
				{ data: 'start_date' },
				{ data: 'office' },
				{ data: 'extn' }
				]
			});			
		});
		
		
};

function getSettlements() {
		$('#main').load("../../templates/settlements/get.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			$('#dataTable').DataTable({
			"ajax": "../../data/objects.txt",
			"columns": [
				{ data: 'id' },
				{ data: 'name' },
				{ data: 'position' },
				{ data: 'salary' },
				{ data: 'start_date' },
				{ data: 'office' },
				{ data: 'extn' }
				]
			});			
		});
		
		
};

function loadVehicleDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/vehicles/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveVehicle(id);
		});		
};

function loadAgentDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/agents/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveUser(id);
		});		
};

function loadOperatorDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/operators/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			
			retrieveUser(id);
		});		
};

function loadRegulatorDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/regulators/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveUser(id);
		});		
};

function loadTerminalDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/terminals/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveTerminal(id);
		});		
};

function loadRouteDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/routes/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveRoute(id);
		});		
};

function loadDeviceDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/devices/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);
			retrieveUser(id);
		});		
};

function loadTransactionDetails() {
		var urlParams = new URLSearchParams(window.location.search);
		var id = urlParams.get('id');
		$('#main').load("../../templates/transactions/view.html", function(response, status, xhr) {
			var template = document.querySelector('template');
			var clone = document.importNode(template.content, true);
			document.querySelector('#main').appendChild(clone);	
			retrieveTransaction(id);
		});		
};


function validateBvn(){

    var val = document.getElementById("bvn").value;
    console.log(val);

   if(val.trim().length<10){
    //show typing
    document.getElementById('bvnDiv').innerHTML = "<em class='blink' style='color:green;'>Checking...</em>";
    //document.getElementById("name").value = "";
    //document.getElementById("gender").selectedIndex = "0";
    return;
   }
   else if(val.trim()==="1002002345"){
      //set name field
      //document.getElementById("name").value = "Earnest Erihbra Suru";
      //set male as gender
      //document.getElementById("gender").selectedIndex = "1";
      //show div message
      document.getElementById('bvnDiv').innerHTML = "<em class='blink' style='color:green;'>BVN Validated successfully.</em>";
      return;
   }else{
    document.getElementById('bvnDiv').innerHTML = "<em class='blink' style='color:red;'>BVN could not be validated.</em>";
    return;
   }
};

function validateIdByNIMC(){

    var val = document.getElementById("idNumber").value;

   if(val.trim().length<10){
    //show typing
    document.getElementById('idNumberDiv').innerHTML = "<em class='blink' style='color:green;'>Checking...</em>";
    return;
   }
   else if(val.trim()==="4005006789"){
      //show div message
      document.getElementById('idNumberDiv').innerHTML = "<em class='blink' style='color:green;'>ID number validated successfully.</em>";
      return;
   }else{
    document.getElementById('idNumberDiv').innerHTML = "<em class='blink' style='color:red;'>ID number could not be validated.</em>";
    return;
}

};

function validatePhoneNumber(){
 var val = document.getElementById("contactMobile").value
    console.log(val)

   if(val.trim().length<11){
    //show typing
    document.getElementById('contactMobileDiv').innerHTML = "<em class='blink' style='color:green;'>Checking...</em>";
    return;
   }
   else if(val.trim()==="09021711733"){
      //show div message
      document.getElementById('contactMobileDiv').innerHTML = "<em class='blink' style='color:green;'>Mobile phone number validated successfully.</em>";
      return;
   }else{
    document.getElementById('contactMobileDiv').innerHTML = "<em class='blink' style='color:red;'>Mobile phone number could not be validated.</em>";
    return;
}

};

function validateNextOfPhone(){

 var val = document.getElementById("nextOfKinMobile").value
    console.log(val)
   if(val.trim().length<11){
    //show typing
    document.getElementById('nextOfKinMobileDiv').innerHTML = "<em class='blink' style='color:green;'>Checking...</em>";
    return;
   }
   else if(val.trim()==="08028594179"){
      //show div message
      document.getElementById('nextOfKinMobileDiv').innerHTML = "<em class='blink' style='color:green;'>Mobile phone number validated successfully.</em>";
      return;
   }else{
    document.getElementById('nextOfKinMobileDiv').innerHTML = "<em class='blink' style='color:red;'>Mobile phone number could not be validated.</em>";
    return;
}
};

function checkSelection(){
var selectedCategory = document.getElementById("idCategory").selectedIndex;

if(selectedCategory===7){
  document.getElementById("otherIdCategoryDiv").style.display = "block";
}
else{
 document.getElementById("otherIdCategoryDiv").style.display = "none";
}

if(selectedCategory==6){
 document.getElementById("idNumberDiv").style.display = "none";
}else{
 document.getElementById("idNumberDiv").style.display = "block";
}

}

