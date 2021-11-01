


document.getElementById('registerForm').addEventListener('submit', register);
let noticeModal = document.getElementById("noticeModal");

let uNameBox = document.getElementById('usernameTextbox');
let pBox = document.getElementById('passwordTextbox');

let fNameBox = document.getElementById('firstNameTextbox');
let lNameBox = document.getElementById('lastNameTextbox');

let emailBox = document.getElementById('emailTextbox');
let cCheckBox = document.getElementById('confirmCheckbox');
let dataIsConfirmed = false;

let noticeHeader = document.getElementById("noticeHeader");
let noticeDetailDiv = document.getElementById("noticeDetailDiv");

cCheckBox.addEventListener('click', ()=> {
	dataIsConfirmed = !dataIsConfirmed
	console.log("dataIsConfirmed: " + dataIsConfirmed);
	});

async function register(e){
	e.preventDefault();
	
	let url = "http://localhost:8080/ERS/api/register";
	let firstName = fNameBox.value;
	let lastName = lNameBox.value; 
	let username = uNameBox.value;
	let password = pBox.value;
	let email = emailBox.value;
	let title = "credential query";
	let body = "this is our body value";

	let user = {
			firstName,
			lastName,
			username,
			password,
			email
		};
		
		console.log(user);
		
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
		//throw some error - badCredentials();	
	}
}

function handleResponse(res)
{

	// parse data to see if manager or employee, then send to home
	console.log(res);
	
	if (res === "Registration Successful")
	{
		
		//notify registration successful
		successMessage();
	}
	else
	{
		//notify registration was bad
		failureMessage();
	}
}
	
	function goToLogin(){
	window.location.href = "login.html";
}

function refresh(){
	window.location.href = "register.html";
}

function hideModal(){
  noticeModal.style.display = "none";
}
	
function successMessage()
{
	try
	{
		console.log(noticeModal);
		
		//change noticeHeader, change noticeDetailDiv info
		//header
		noticeHeader.innerHTML = "Registration Successful";
		//change color from red to blue
		
		//detail
		noticeDetailDiv.innerHTML = "<p>You have been registered</p>" + 
			"<p>Go to login, or continue here...</p>";
		
		//change color
		document.getElementById('modalHeader').style.backgroundColor = 'blue';
		document.getElementById('modalFooter').style.backgroundColor = 'blue';
		
		//show modal
		noticeModal.style.display = "block";
	}
	catch(e){alert(e.message);}
}

function failureMessage()
{
	try
	{
		console.log(noticeModal);
		noticeHeader.innerHTML = "Registration failed: ";
		noticeDetailDiv.innerHTML = "<p>You have NOT been registered!</p>" + 
			"<p>Double check your information, or try another username.</p>";
		
		//change color
		document.getElementById('modalHeader').style.backgroundColor = 'red';
		document.getElementById('modalFooter').style.backgroundColor = 'red';
		
		//show modal
		noticeModal.style.display = "block";
	}
	catch(e){alert(e.message);}
}






