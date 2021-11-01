

let loginForm = document.getElementById('loginForm').addEventListener('submit', login);


//get elements and query strings
let params = new URLSearchParams(window.location.search);
let errorModal = document.getElementById("errorModal");

let uNameBox = document.getElementById('usernameTextbox');
let pBox = document.getElementById('passwordTextbox');


function checkCredentials()
{
	//call to the api

	post(url, obj);
}

async function login(e){
	e.preventDefault();
	
	let url = "http://localhost:8080/ERS/api/login";
	let username = uNameBox.value;
	let password = pBox.value;
	let title = "credential query";
	let body = "this is our body value";

	let user = {
			username,
			password
		};
		
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(user)
		});
		let res = await req.json();
		handleResponse(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
		badCredentials();	
	}
}

function handleResponse(res)
{
	// parse data to see if manager or employee, then send to home
	console.log(res);
	
	if (res != null)
	{
		sessionStorage.setItem("userString", JSON.stringify(res));
		if (res.userRoleId == 1)
		{
	    	window.location.href = "employee.html";
	    }
	    else{window.location.href = "manager.html";}
	}
	else
	{
		badCredentials();
	}
}


function badCredentials()
{
	try
	{
		console.log(errorModal);
		errorModal.style.display = "block";
	}
	catch(e){alert(e.message);}
}

function goToRegister(){
	window.location.href = "register.html";
}

function refresh(){
	window.location.href = "login.html";
}

function hideModal(){
  errorModal.style.display = "none";
}
/*
// When the user clicks anywhere outside of the modal, close it
(window.onclick = function(event) {
  if (event.target == errorModal) {
    errorModal.style.display = "none";
    refresh();
  }
})(); */