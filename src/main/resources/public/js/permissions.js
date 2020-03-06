$(document).ready( function () {
				//load permissions
				var permissions = sessionStorage.getItem('permissions');
				//if no permissions, load access token and fetch permissions from api
				if(permissions == null){
					var access_token = sessionStorage.getItem('access_token');
					//access_token['username'];
				}
				//use permissions
				if(jQuery.inArray("", permissions)){
					//do something
					var a = 'g';
				}
				
			});