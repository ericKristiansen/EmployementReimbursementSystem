

//Modals
let employeeListModal;
let empRequestModal;
let empInfoModal;
let noticeModal;

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


let userNameSpan; //use to store employee username
let user;
	
let disabledClasses = "form-control text-white bg-secondary";
let enabledClasses = "form-control text-black bg-light";


//onload initial set-up
(() => {
	//modals
	empRequestModal = document.getElementById('empRequestModal');
	employeeListModal = document.getElementById('employeeListModal');
	empInfoModal = document.getElementById('empInfoModal');

	//Emp form elements
	formEmpIdBox = document.getElementById('empNumber');
	formFNameBox = document.getElementById('firstName');
	formLNameBox = document.getElementById('lastName');
	formUNameBox = document.getElementById('username');
	formPWordBox = document.getElementById('password');
	formEmailBox = document.getElementById('email');
	formSaveInfoCheckBox = document.getElementById('editCheckButton');
	formInfoSubmit = document.getElementById('empInfoSubmit');

	userNameSpan = document.getElementById('userNameSpan');
	noticeModal = document.getElementById("noticeModal");


	try
	{
	user = JSON.parse(sessionStorage.getItem("userString"));
	
	//if not admin, push to employee page
	if (user.userRoleId == 1){window.location.href = "employee.html";}
	
	try{
		getAllRequests();
		}catch(e)
		{console.log(e.message);}
	
	console.log(user);

	userNameSpan.innerHTML = user.userName;
	
	}catch(e){
		goToLogin();
	}


})();





//functions

function byEmployeeAction(){
	
	getAllUsers();
	
	displayModal(employeeListModal);
}

async function getAllUsers()
{
		
	let url = "http://localhost:8080/ERS/api/getAllUsers";
	let id = user.id;

	let newInfo = {
			id
		};
		
		console.log("attempting to get all users...");
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		populateUsers(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
}

function populateUsers(res){
	console.log('populate users...');
	try
	{	
		let tableBody = document.getElementById('employeeTableBody');
		tableBody.innerHTML = "";

		for (var i=0; i < res.length; i++) {
			let id = res[i].id;
			let fullName = res[i].firstName + " " + res[i].lastName;
			let str = "<tr onclick='getUserRequestsAction(" + id + ", " + "\"" +  fullName + "\"" + ");'>" +
				"<th scope='row'>" + id + "</th>" +
				"<td>" + fullName + "</td>" +
				"<td>" + res[i].userName + "</td>" +
				"</tr>";
				
   			tableBody.innerHTML += str;
		}
		
		
	}
	catch(e){alert(e.message);}
	
}

function getUserRequestsAction(userId, fullName)
{
	console.log(fullName + " selected - userId: " + userId);
	getRequestsByUserId(userId, fullName);
	
	//hide users modal
	hideModal(employeeListModal);
	
	//show user requests modal
	displayModal(empRequestModal);
	
}

async function getAllRequests(){
	
	let url = "http://localhost:8080/ERS/api/getAllRequests";
	let id = user.id;

	let newInfo = {
			id
		};
		
		console.log("attempting get all requests");
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		populateMainRequests(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
}


function populateMainRequests(res){
	
	try
	{
		// populate list of 
		console.log('...populate main list...');
		console.log(res);
	
		let tableBody = document.getElementById('requestTableBody');
		tableBody.innerHTML = "";


		for (var i=0; i < res.length; i++) {
			let requestId = res[i].id;
			let approveTd = "<td>";
			
			if (res[i].reimbStatusId == 1){
				//pending
				approveTd += "<img src='../images/approvedCheckbox.png' alt='denied' width='50' height='50' " +
				  "onClick='approveRequestMain(" + requestId + ", " + true + ");' style='cursor: pointer;'/>" +
				 "<img src='../images/deniedCheckbox.png' alt='denied' width='50' height='50' " +
				  "onClick='approveRequestMain(" + requestId + ", " + false + ");' style='cursor: pointer;'/>" +
				"</td>" 
			}
			else if (res[i].reimbStatusId == 2){
				//approved
				approveTd += "<img src='../images/approvedCheckbox.png' alt='denied' width='50' height='50'/></td>" 
			}
			if (res[i].reimbStatusId == 3){
				//denied
				approveTd += "<img src='../images/deniedCheckbox.png' alt='denied' width='50' height='50'/></td>" 
			}
			
			let str = "<tr>" +
				"<th scope='row'>" + res[i].authorId + "</th>" +
				"<td>" + requestId + "</td>" +
				"<td>" + getReimbType(res[i].typeId) + "</td>" +
				"<td>$" + res[i].amount + ".00</td>" +
				"<td>" + res[i].description + "</td>" +
				"<td>" + getReimbStatus(res[i].reimbStatusId) + "</td>" +
				approveTd +
				"</tr>";
				
   			tableBody.innerHTML += str;
		}
	}
	catch(e){alert(e.message);}
}



async function approveRequestMain(requestId, isApproved){
	console.log(requestId + " is approved: " + isApproved);
	
	let url = "http://localhost:8080/ERS/api/approveRequest";

	let newInfo = {
			requestId,
			isApproved
		};
		
		console.log("attempting to approve request");
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		populateMainRequests(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}

}

async function getRequestsByStatusId(statusId){
		let url = "http://localhost:8080/ERS/api/getRequestsByStatusId";

	let newInfo = {
			statusId
		};
		
		console.log("attempting get requests by StatusId: " + statusId);
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		populateMainRequests(res);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
	
}

/* Get requests by userId */
async function getRequestsByUserId(id, fullName){
	
	let url = "http://localhost:8080/ERS/api/getRequestsByUserId";

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
		populateUserRequests(res, id, fullName);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
}

function populateUserRequests(res, userId, fullName)
{
	
	console.log("populate user requests...", res);
	
	try
	{
		// populate list of 
		console.log('...populate list...');
		console.log(res);
	
		let tableBody = document.getElementById('employeeRequestTableBody');
		tableBody.innerHTML = "";


		for (var i=0; i < res.length; i++) {
			let requestId = res[i].id;
			let approveTd = "<td>";
			
			if (res[i].reimbStatusId == 1){
				//pending
				approveTd += "<img src='../images/approvedCheckbox.png' alt='denied' width='50' height='50' " +
				  "onClick='approveRequest(" + userId + ", \"" + fullName + "\"," +
				  requestId + ", " + true +");' style='cursor: pointer;'/>" +
				 "<img src='../images/deniedCheckbox.png' alt='denied' width='50' height='50' " +
				  "onClick='approveRequest(" + userId + ", \"" + fullName + "\"," +
				  requestId + ", " + false +");' style='cursor: pointer;'/>" +
				"</td>" 
			}
			else if (res[i].reimbStatusId == 2){
				//approved
				approveTd += "<img src='../images/approvedCheckbox.png' alt='denied' width='50' height='50'/></td>" 
			}
			if (res[i].reimbStatusId == 3){
				//denied
				approveTd += "<img src='../images/deniedCheckbox.png' alt='denied' width='50' height='50'/></td>" 
			}
			
			let str = "<tr>" +
				"<th scope='row'>" + fullName + "</th>" +
				"<td>" + requestId + "</td>" +
				"<td>" + getReimbType(res[i].typeId) + "</td>" +
				"<td>$" + res[i].amount + ".00</td>" +
				"<td>" + res[i].description + "</td>" +
				"<td>" + getReimbStatus(res[i].reimbStatusId) + "</td>" +
				approveTd +
				"</tr>";
				
   			tableBody.innerHTML += str;
		}
	}
	catch(e){alert(e.message);}

}

async function approveRequest(userId, fullName, requestId, isApproved)
{
	console.log(userId + " : " + requestId + " is approved: " + isApproved);
	
		let url = "http://localhost:8080/ERS/api/approveRequestForUser";

	let newInfo = {
			userId,
			requestId,
			isApproved
		};
		
		console.log("attempting to approve request");
		
	try{
		let req = await fetch(url, {
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify(newInfo)
		});
		let res = await req.json();
		populateUserRequests(res, userId, fullName);
		
	}catch(e)
	{
		console.log(`Error: ${e.message}`);
	}
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


function empInfoAction() {
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

function displayModal(pModal) {
	pModal.style.display = 'block';
}

function hideModal(pModal) {
	pModal.style.display = 'none';
}

function refresh() {
	window.location.reload();
}