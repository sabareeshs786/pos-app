var $brand = $('#brand-form input[name=brand]');
var $category = $('#brand-form input[name=category]');
var $add = $('#add-brand');
var $error1 = $('#error-message1');
var $editBrand = $('#brand-edit-form input[name=brand]');
var $editCategory = $('#brand-edit-form input[name=category]');
var $error2 = $('#error-message2');
var $update = $('#update-brand');
var oldData = "";
var newData = "";
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	if(validator(json)){
		var url = getBrandUrl();
		
		$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			$error1.attr('style', 'display:none;');
			$error1.text('');
			handleAjaxSuccess("Success!!!\nBrand "+response.brand+" and category "+response.category+" added successfully!!");
			getBrandListUtil();
			$('#add-brand-modal').modal('toggle');
		},
		error: function(response){
			var response = JSON.parse(response.responseText);
			$error1.attr('style', 'display:block; color:red;');
			$error1.text(response.message);
			disableAdd();
			}
		});
	}
	return false;
}
function validateAddBrandUtil(){
	$brand.off();
	$brand.on('input', validateAddBrand);
}
function validateAddCategoryUtil(){
	$category.off();
	$category.on('input', validateAddCategory);
}
function validateAddBrand(){
	if($brand.val().length == 0){
		if($brand.hasClass('is-valid')){
			$brand.removeClass('is-valid');
		}
		$brand.addClass('is-invalid');
		$('#bvf1').attr('style', 'display:none;');
		$('#bivf1').attr('style', 'display:block;');
		disableAdd();
	}
	else{
		if($brand.hasClass('is-invalid')){
			$brand.removeClass('is-invalid');
		}
		$brand.addClass('is-valid');
		$('#bivf1').attr('style', 'display:none;');
		$('#bvf1').attr('style', 'display:block;');
		enableAdd();
	}
}

function validateAddCategory(){
	if($category.val().length == 0){
		if($category.hasClass('is-valid')){
			$category.removeClass('is-valid');
		}
		$category.addClass('is-invalid');
		$('#cvf1').attr('style', 'display:none;');
		$('#civf1').attr('style', 'display:block;');
		enableOrDisableAdd()
	}
	else{
		if($category.hasClass('is-invalid')){
			$category.removeClass('is-invalid');
		}
		$category.addClass('is-valid');
		$('#civf1').attr('style', 'display:none;');
		$('#cvf1').attr('style', 'display:block;');
		enableOrDisableAdd();
	}
}

function validateEditForm(){
	if($editBrand.val().length == 0){
		if($editBrand.hasClass('is-vaild')){
			$editBrand.removeClass('is-valid');
		}
		$('#ebif1').attr("style", "display:block;");
		$editBrand.addClass('is-invalid');
		$update.attr('disabled', true);
	}
	else{
		if($editBrand.hasClass('is-invalid')){
			$editBrand.removeClass('is-invalid');
		}
		$('#ebif1').attr("style", "display:none;");
		$editBrand.addClass('is-valid');
	}
	if($editCategory.val().length == 0){
		if($editCategory.hasClass('is-vaild')){
			$editCategory.removeClass('is-valid');
		}
		$('#ecif1').attr("style", "display:block;");
		$editCategory.addClass('is-invalid');
		$update.attr('disabled', true);
	}
	else{
		if($editCategory.hasClass('is-invalid')){
			$editCategory.removeClass('is-invalid');
		}
		$('#ecif1').attr("style", "display:none;");
		$editCategory.addClass('is-valid');
	}
	
	newData = {
		'brand': $editBrand.val(),
		'category': $editCategory.val()
	};

	console.log("Old");
	console.log(oldData);
	console.log("New");
	console.log(newData);

	if((oldData.brand == newData.brand && oldData.category == newData.category)
		|| ($editBrand.hasClass('is-invalid') || $editCategory.hasClass('is-invalid'))){
		$update.attr('disabled', true);
	}
	else{
		$update.attr('disabled', false);
	}
}

function updateBrand(event){
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getBrandUrl() + "/" + id;


	//Set the values to update
	var $form = $("#brand-edit-form");
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
			$error2.attr('style', 'display:none;');
			$error2.text('');
			var jsonObj = JSON.parse(json);
			handleAjaxSuccess("Update successfull!!!");
			getBrandListUtil();
			$('#edit-brand-modal').modal('toggle');
		},
		error: function(response){
			var response = JSON.parse(response.responseText);
			$error2.attr('style', 'display:block; color:red;');
			$error2.text(response.message);
		}
		});
	}
	
	return false;
}

function getBrandListUtil(){
	var pageSize = $('#inputPageSize').val();
	getBrandList(0, pageSize);
}

function getBrandList(pageNumber, pageSize){
	var url = getBrandUrl() + '?page-number=' + pageNumber + '&page-size=' + pageSize;
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json; charset=utf-8',
	   success: function(data) {
	   		displayBrandList(data.content,pageNumber*pageSize);
			$('#selected-rows').html('Showing ' + (pageNumber*pageSize + 1) + ' to ' + (pageNumber*pageSize + data.content.length) + ' of ' + data.totalElements);
			paginator(data, "getBrandList", pageSize);
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function isValid(uploadObject) {
	if(uploadObject.hasOwnProperty('brand') &&
		uploadObject.hasOwnProperty('category') &&
		Object.keys(uploadObject).length==2){
			return true;
	}
	return false;
}

function processData(){
	var file = $('#brandFile')[0].files[0];
	console.log(file);
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results;
	if(isValid(fileData[0])){
		console.log(fileData);
		uploadRows();
	}
	else{
		$("#error-message").notify("Invalid file", "error");
	}
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		getBrandListUtil();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getBrandUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();
	   },
	   error: function(response){
	   		row.error=response.responseText;
	   		errorData.push(row);
			uploadRows();
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data, sno){
	$("#brand-table-body").empty();
    var row = "";
	for (var i = 0; i < data.length; i++) {
	sno += 1;
	var buttonHtml = '<button onclick="displayEditBrand(' + data[i].id + ')">edit</button>'
	row = "<tr><td>" 
	+ sno + "</td><td>" 
	+ data[i].brand + "</td><td>"
	+ data[i].category + "</td><td>" 
	+ buttonHtml 
	+ "</td></tr>";
	$("#brand-table-body").append(row);
	}
	enableOrDisable();
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: function(response){
			handleAjaxError(response);
	   }
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brand-modal').modal('toggle');
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
	$update.attr('disabled', true);
	oldData = {
		'brand': $editBrand.val(),
		'category': $editCategory.val()
	};
	newData = {
		'brand': $editBrand.val(),
		'category': $editCategory.val()
	};
}

function enableAdd(){
	$add.attr('disabled', false);
}
function disableAdd(){
	$add.attr('disabled', true);
}
function enableOrDisableAdd(){
	var brand = $brand.val();
	var category = $category.val();
	if(brand.length > 0 && category.length > 0 && 
		!$brand.hasClass('is-invalid') && !$category.hasClass('is-invalid')){
		$add.attr('disabled', false);
	}
	else{
		$add.attr('disabled', true);
	}
}
function addData(){
	$('#add-brand-modal').modal('toggle');
}

function clearEditData(){
	$editBrand.val('');
	$editCategory.val('');
	$error2.val('');
	$update.attr('disabled', true);
	$('#edit-brand-modal').modal('toggle');
}
function clearAddData(){
	$('#brand-form input[name=brand]').val('');
	$('#brand-form input[name=brand]').val('');
	$error1.val('');
	$add.attr('disabled', true);
	$('#add-brand-modal').modal('toggle');
}

function clearUploadData(){
	$('#rowCount').text('0');
	$('#processCount').text('0');
	$('#errorCount').text('0');
	$('#brandFile').val('');
	$('#upload-brand-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#cancel1').click(clearAddData);
	$('#cancel2').click(clearAddData);
	$('#cancel3').click(clearEditData);
	$('#cancel4').click(clearEditData);
	$('#cancel5').click(clearUploadData);
	$('#cancel6').click(clearUploadData);
	$('#add-data').click(addData);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
	$('#inputPageSize').on('change', getBrandListUtil);
	$editBrand.on('input', validateEditForm);
	$editCategory.on('input', validateEditForm);
}

$(document).ready(init);
$(document).ready(getBrandListUtil);
$(document).ready(enableOrDisable);

