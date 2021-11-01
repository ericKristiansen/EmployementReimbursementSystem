
	/* List and allocate variables */
	let createRequestButton; //use to launch new request
	let requestDetailButton; //use to launch request detail
	let employeeDetailButton; //use too launch employee detail
	let logoutButton; //use to initiate logout
	
	let userNameSpan; //use to store employee username
	let employee; //use to store employee data
	
	let empInfoModal; //use to toggle display of modal
	let newRequestModal; //use to toggle display of modal
	let requestDetailModal; //use to toggle display of modal
	
	let noticeModal;
	
	let user;
	
	let disabledClasses = "form-control text-white bg-secondary";
	let enabledClasses = "form-control text-black bg-light";
	
	//Employee Form
	document.getElementById('empInfoForm').addEventListener('submit', updatEmployeeInfo);
	let formEmpIdBox;
	let formFNameBox;
	let formLNameBox;
	let formUNameBox;
	let formPWordBox;
	let formEmailBox;
	let formSaveInfoCheckBox;
	let formInfoSubmit;
	
	//New request Form
	document.getElementById('newRequestForm').addEventListener('submit', submitNewRequest);
	let requestTypeSelect;
	let amountBox;
	let descriptionBox;
	let newRequestCheckbox;
	
	
	

(()=> { 
	createRequestButton = document.getElementById('createRequestButton');
	employeeDetailButton = document.getElementById('empInfoAction');
	empInfoModal = document.getElementById('empInfoModal');
	newRequestModal = document.getElementById('newRequestModal');
	requestDetailModal = document.getElementById('requestDetailModal');
	userNameSpan = document.getElementById('userNameSpan');
	noticeModal = document.getElementById("noticeModal");

	//form elements
	formEmpIdBox = document.getElementById('empNumber');
	formFNameBox = document.getElementById('firstName');
	formLNameBox = document.getElementById('lastName');
	formUNameBox = document.getElementById('username');
	formPWordBox = document.getElementById('password');
	formEmailBox = document.getElementById('email');
	formSaveInfoCheckBox = document.getElementById('editCheckButton');
	formInfoSubmit = document.getElementById('empInfoSubmit');


	//new request form
	requestTypeSelect = document.getElementById('typeSelect');
	amountBox = document.getElementById('newRequestAmount');
	descriptionBox = document.getElementById('newRequestDescription');
	newRequestCheckbox = document.getElementById('confirmNewRequestCheckbox');



	try
	{
	user = JSON.parse(sessionStorage.getItem("userString"));
	
	
	try{getRequestsByUserId();}catch(e){console.log(e.message);}
	
	console.log(user);

	userNameSpan.innerHTML = user.userName;
	}catch(e){
		goToLogin();
	}


})();


function displayModal(pModal){
	pModal.style.display = 'block';
}

function hideModal(pModal){
	pModal.style.display = 'none';
}

function toggleModal(pModal){
	pModal.style.display = (pModal.style.display == 'none')? 'block': 'none';
}


function empInfoAction(){
	// load modal with info
	formEmpIdBox.value = user.id;
	formFNameBox.value = user.firstName;
	formLNameBox.value = user.lastName;
	formUNameBox.value = user.userName;
	formPWordBox.value = user.password;
	formEmailBox.value = user.email;
	formSaveInfoCheckBox.checked = false;

	setEmpFormTextboxesDisabled(true);
	
	//show modal
	displayModal(empInfoModal);
}

function checkboxChange(){
	setEmpFormTextboxesDisabled(!formSaveInfoCheckBox.checked)
	let label = document.getElementById('editInfoLabel');
	label.innerHTML = (label.innerHTML == "Edit My Info") ? "Cancel Edit": "Edit My Info";
	document.getElementById('confirmCheckbox').checked = false;
}

//disable/enable form fields, and save button 

function logoutAction(){
	//delete session variables
	sessionStorage.clear(); 
	
	//redirect to login
	goToLogin();
}

function goToLogin()
{
	window.location.href = "login.html";
}

function setEmpFormTextboxesDisabled(disabled)
{
	formFNameBox.disabled = disabled;
	formLNameBox.disabled = disabled;
	formUNameBox.disabled = disabled;
	formPWordBox.disabled = disabled;
	formEmailBox.disabled = disabled;
	formInfoSubmit.disabled = disabled;

	let classList = disabled ? disabledClasses: enabledClasses;
	console.log(classList);

	formFNameBox.className = classList;
	formLNameBox.className = classList;
	formUNameBox.className = classList;
	formPWordBox.className = classList;
	formEmailBox.className = classList;
}

async function updatEmployeeInfo(e){
	e.preventDefault();
	
	let url = "http://localhost:8080/ERS/api/updateUserInfo";
	let firstName = formFNameBox.value;
	let lastName = formLNameBox.value; 
	let username = formUNameBox.value;
	let password = formPWordBox.value;
	let email = formEmailBox.value;
	let id = user.id;

	let newInfo = {
			id,
			firstName,
			lastName,
			username,
			password,
			email
		};
		
		console.log("attempting update");
		hideModal(empInfoModal);
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		handleEmpInfoUpdateResponse(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
}

function handleEmpInfoUpdateResponse(res)
{
	// parse data to see if manager or employee, then send to home
	console.log(res);
	
	if (res === "Update Successful")
	{
		console.log('Success....');
		updateSuccessMessage();
	}
	else
	{
		console.log('Failure....');
		updateFailureMessage();
	}
}
	
function updateSuccessMessage()
{
	try
	{
		console.log(noticeModal);

		noticeHeader.innerHTML = "Update Successful";
		//change color from red to blue
		
		//detail
		noticeDetailDiv.innerHTML = "<p>Your information was updated</p>" + 
			"<p>logging out...</p>";
		
		//change color
		document.getElementById('modalHeader').style.backgroundColor = 'blue';
		document.getElementById('modalFooter').style.backgroundColor = 'blue';
		
		//show modal
		noticeModal.style.display = "block";
		
		//logout in five seconds
		setTimeout(logoutAction, 3000);
	}
	catch(e){alert(e.message);}
}

function updateFailureMessage()
{
	try
	{
		noticeHeader.innerHTML = "Update Failed...";
		noticeDetailDiv.innerHTML = "<h2 style='text-align: center;'>Data was not accepted!</h2>";
		
		//change color
		document.getElementById('modalHeader').style.backgroundColor = 'red';
		document.getElementById('modalFooter').style.backgroundColor = 'red';
		
		//show modal
		noticeModal.style.display = "block";
	}
	catch(e){
		alert(e.message);
		}
}

function refresh(){
	window.location.reload();
}



/* New Request Section ***************************************************/
async function submitNewRequest(e){
	e.preventDefault();
	
	let url = "http://localhost:8080/ERS/api/submitNewRequest";
	let id = user.id;
	let amount = amountBox.value; 
	let description = descriptionBox.value; 
	let typeId = requestTypeSelect.value;

	let newInfo = {
			id,
			amount,
			description,
			typeId,
		};
		
	//clear controls
	newRequestCheckbox.checked = false;
	amountBox.value = "";
	descriptionBox.value = "";
	requestTypeSelect.value = "";
		
	console.log("attempting submit new request: " + newInfo);
	hideModal(newRequestModal);
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		handleNewRequestResponse(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
}


//New Request Failure
function handleNewRequestResponse(res)
{
	// parse data to see if manager or employee, then send to home
	console.log(res);
	
	if (res != "New Request Failure")
	{
		console.log('Success....');
		populateRequests(res);
	}
	else
	{
		console.log('Failure....');
		newRequestFailureMessage();
	}
}


function populateRequests(res)
{
	try
	{
		// populate list of 
		console.log('...populate list...');
		console.log(res);
	
		let tableBody = document.getElementById('requestTableBody');
		tableBody.innerHTML = "";

		for (var i=0; i < res.length; i++) {
			let str = "<tr id='row" + res[i].id + "'>" +
				"<th scope='row'>" + res[i].id + "</th>" +
				"<td>" + getReimbType(res[i].typeId) + "</td>" +
				"<td>$" + res[i].amount + ".00</td>" +
				"<td>" + res[i].description + "</td>" +
				"<td>" + getReimbStatus(res[i].reimbStatusId) + "</td>" +
				"</tr>";
				
   			tableBody.innerHTML += str;
		}
	}
	catch(e){alert(e.message);}
}


function getReimbStatus(statusId)
{
	let result = "";
	switch(statusId)
	{
		case 1: result = "PENDING";
			break;
		case 2: result = "APPROVED";
			break;
		case 3: result = "DENIED";
			break;
		default:
			console.log("status not defined?: " + statusId);
	}
	return result;
}

function getReimbType(typeId)
{
	let result = "";
	switch(typeId)
	{
		case 1: result = "LODGING";
			break;
		case 2: result = "TRAVEL";
			break;
		case 3: result = "FOOD";
			break;
		case 4: result = "OTHER";
			break;
		default:
			console.log("type not defined?: " + typeId);
	}
	return result;
}

function newRequestFailureMessage()
{
	try
	{
		noticeHeader.innerHTML = "New Request Failed...";
		noticeDetailDiv.innerHTML = "<h2 style='text-align: center;'>Data was not accepted!</h2>";
		
		//change color
		document.getElementById('modalHeader').style.backgroundColor = 'red';
		document.getElementById('modalFooter').style.backgroundColor = 'red';
		
		//show modal
		noticeModal.style.display = "block";
	}
	catch(e){
		alert(e.message);
		}
}

/* Get requests by userId */
async function getRequestsByUserId(){
	
	let url = "http://localhost:8080/ERS/api/getRequestsByUserId";
	let id = user.id;

	let newInfo = {
			id
		};
		
		console.log("attempting get requests by id: " + id);
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		populateRequests(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
}

async function getRequestsByStatusIdAndUserId(statusId){
	
	let url = "http://localhost:8080/ERS/api/getRequestsByStatusIdAndUserId";
	let id = user.id;

	let newInfo = {
			id,
			statusId
		};
		
		console.log("attempting get requests by status id and user id: " + statusId + " : " + id);
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		populateRequests(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
}



