// GLOBAL VARIABLES
var $addModal = $('#add-user-modal');
var $email = $('#user-form input[name=email]');
var $password = $('#user-form input[name=password]');
var $add = $('#add-user');

var $editModal = $('#edit-user-modal');
var $editEmail = $('#edit-user-form input[name=email]');
var $editPassword = $('#edit-user-form input[name=password]');
var $update = $('#update-user');

var oldData = "";
var newData = "";


function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/user";
}

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#user-form");
	var json = toJson($form);
	if(validator(json)){
	var url = getUserUrl();
	
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
			handleAjaxSuccess('User added successfully');
			$addModal.modal('toggle');
	   		getUserList();   
	   },
	   error: function(response){
			handleAjaxError(response);
			clearAddData();
	   }
	});
	}
	return false;
}

function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: handleAjaxError
	});
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getUserList();    
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayUserList(data){
	var $tbody = $('#user-table-body');
	$tbody.empty();
	var sno = 0;
	for(var i in data){
		var e = data[i];
		sno += 1;
		var buttonHtml = ' <button onclick="displayEditUser(' + e.id + ')" class="btn btn-secondary">edit</button> &nbsp;&nbsp;';
		buttonHtml += '<button onclick="deleteUser(' + e.id + ')" class="btn btn-secondary">delete</button>';
		var row = '<tr>'
		+ '<td>' + sno + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + e.role + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	enableOrDisable();
}

function displayEditUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
			displayUserEdit(data);
	   },
	   error: function(response){
			handleAjaxError(response);
	   }
	});
}

function displayUserEdit(data){
	$('#edit-user-form input[name=id]').val(data.id);
	$editEmail.val(data.email);
	if(data.role == 'supervisor'){
		$('#supervisor').attr('checked', true);
	}
	else{
		$('#operator').attr('checked', false);
	}
	$update.attr('disabled', true);
	oldData = data;
	newData = data;
}

function setEmailInvalid(message){
	$editEmail.addClass('is-invalid');
	$('#eivf').attr('style', 'display: none;')
	$('#eivf').html(message);
}

function validateEditForm(){
	newData = {
		'email': $editEmail.val(),
		'role': $('select[name=role]').val()
	}
	if($editEmail.val() == ''){
		setEmailInvalid('Please enter email');
	}
	else{
		if($editEmail.hasClass('is-invalid')){
			$editEmail.removeClass('is-invalid');
		}
		$update.attr('disabled', false);
	}
	if(oldData.email == newData.email && oldData.role == newData.role){
		$update.attr('disabled', true);
	}
	else{
		$update.attr('disabled', false);
	}
}

function addData(){
	$('#add-user-modal').modal('toggle');
}

function clearEditData(){
	if($editModal.hasClass('show')){
		$editModal.modal('toggle');
	}
}

function validateEmailUtil(){
	$email.off();
	$email.on('input', validateEmail);
}

function validatePasswordUtil(){
	$password.off();
	$password.on('input', validatePassword);
}

function validateEmail(){
	if($email.val() == ''){
		$email.addClass('is-invalid');
		$('#eivf').html('Please enter email');
		$('#eivf').attr('style', 'display: block;');
	}
	else{
		if($email.hasClass('is-invalid')){
			$email.removeClass('is-invalid');
			$('#eivf').attr('style', 'display: none;');
		}
	}
	if(!$email.hasClass('is-invalid') && !$password.hasClass('is-invalid')){
		$add.attr('disabled', false);
	}
	else{
		$add.attr('disabled', true);
	}
}

function validatePassword(){
	if($password.val() == ''){
		$password.addClass('is-invalid');
		$('#pivf').html('Please enter password');
		$('#pivf').attr('style', 'display: block;');
	}
	else{
		if($password.hasClass('is-invalid')){
			$password.removeClass('is-invalid');
			$('#pivf').attr('style', 'display: none;');
		}
	}
	if(!$email.hasClass('is-invalid') && !$password.hasClass('is-invalid')){
		$add.attr('disabled', false);
	}
	else{
		$add.attr('disabled', true);
	}
}

function clearAddData(){
	$email.val('');
	$password.val('');
	$add.attr('disabled', true);
	if($addModal.hasClass('show')){
		$addModal.modal('toggle');
	}
}

function updateUser(){
	var id = $("#edit-user-form input[name=id]").val();	
	var url = getUserUrl() + "/" + id;


	//Set the values to update
	var $form = $("#edit-user-form");
	var json = toJson($form);
	if(validator(json)){
		$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			handleAjaxSuccess("Update successfull!!!");
			getUserList();
			clearEditData();
		},
		error: function(response){
			handleAjaxError(response);
		}
		});
	}
	
	return false;
}

//INITIALIZATION CODE
function init(){
	$('#cancel1').click(clearAddData);
	$('#cancel2').click(clearAddData);
	$('#cancel3').click(clearEditData);
	$('#cancel4').click(clearEditData);
	$('#add-data').click(addData);
	$('#add-user').click(addUser);
	$('#refresh-data').click(getUserList);
	$add.attr('disabled', true);
	$update.attr('disabled', true);
	$editEmail.on('input', validateEditForm);
	$update.click(updateUser);
}

$(document).ready(init);
$(document).ready(getUserList);
$(document).ready(enableOrDisable);