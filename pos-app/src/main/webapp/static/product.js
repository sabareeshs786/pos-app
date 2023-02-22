function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	if(validator(json)){
		var url = getProductUrl();

		$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
			handleAjaxSuccess("Product added successfully!!!");
			getProductListUtil();
		},
		error: handleAjaxError
		});
	}
	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;
	console.log(url)

	//Set the values to update
	var $form = $("#product-edit-form");
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
			handleAjaxSuccess("Product updated successfully!!!");
			getProductListUtil();   
		},
		error: handleAjaxError
		});
	}
	return false;
}

function getProductListUtil(){
	var pageSize = $('#inputPageSize').val();
	getProductList(0, pageSize);
}

function getProductList(pageNumber, pageSize){
	var url = getProductUrl() + '?page-number=' + pageNumber + '&page-size=' + pageSize;
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displayProductList(data.content, pageNumber*pageSize);
			$('#selected-rows').html('<h5>Selected ' + (pageNumber*pageSize + 1) + ' to ' + (pageNumber*pageSize + data.content.length) + ' of ' + data.totalElements +'</h5>');
			paginator(data, "getProductList", pageSize);
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function isValid(uploadObject) {
	if(uploadObject.hasOwnProperty('barcode') &&
		uploadObject.hasOwnProperty('brand') &&
		uploadObject.hasOwnProperty('category') &&
		uploadObject.hasOwnProperty('name') &&
		uploadObject.hasOwnProperty('mrp') &&
		Object.keys(uploadObject).length==5){
			return true;
	}
	return false;
}

function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results;
	if(isValid(fileData[0])){
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
		getProductListUtil();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getProductUrl();

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

function displayProductList(data, sno){
	$("#product-table-body").empty();
    var row = "";
	for (var i = 0; i < data.length; i++) {
	sno += 1;
	var buttonHtml = ' <button onclick="displayEditProduct(' + data[i].id + ')">edit</button>'
	row = "<tr><td>" 
	+ sno + "</td><td>" 
	+ data[i].barcode + "</td><td>"
	+ data[i].brand + "</td><td>"
	+ data[i].category + "</td><td>"
	+ data[i].name + "</td><td>" 
	+ data[i].mrp + "</td><td>"
	+ buttonHtml 
	+ "</td></tr>";
	$("#product-table-body").append(row);
	}
	enableOrDisable();
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			console.log(data);
	   		displayProduct(data); 
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=id]").val(data.id)
	$("#product-edit-form input[name=barcode]").val(data.barcode);	
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$('#edit-product-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
	$('#inputPageSize').on('change', getProductListUtil);
}

$(document).ready(init);
$(document).ready(getProductListUtil);
$(document).ready(enableOrDisable);