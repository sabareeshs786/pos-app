function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	console.log(json);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductListUtil();
	   },
	   error: handleAjaxError
	});

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

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductListUtil();   
	   },
	   error: handleAjaxError
	});

	return false;
}

function getProductListUtil(){
	var pageSize = $('#inputPageSize').val();
	getProductList(0, pageSize);
}

function getProductList(pageNumber, pageSize){
	var url = getProductUrl() + '/' + pageNumber + '/' + pageSize;
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displayProductList(data.content, pageNumber*pageSize);
			   var pagination = "";
			   for (var i = data.number; i < data.number + 3 && i < data.totalPages; i++) {
				   var active = "";
				   if (i == data.number) {
				   active = "active";
				   }
				   pagination += "<li class='page-item " + active + "'><a class='page-link' href='#pageNumber=" + (i+1) +"' onclick='getProductList(" + i + ", " + pageSize + ")'>" + (i + 1) + "</a></li>";
			   }
			   if (data.number > 0) {
				   pagination = "<li class='page-item'><a class='page-link' href='#pageNumber=" + data.number +"' id='previous'>Previous</a></li>" + pagination;
			   }
			   if (data.number < data.totalPages - 1) {
				   pagination = pagination + "<li class='page-item'><a class='page-link' href='#pageNumber=" + (data.number + 2) + "' id='next'>Next</a></li>";
			   }
			   $("#paginationContainer").html(pagination);
			   $("#previous").click(function() {
				   getProductList(data.number - 1, pageSize);
			   });
			   $("#next").click(function() {
				   getProductList(data.number + 1, pageSize);
			   });
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
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
	   		row.error=response.responseText
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
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
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
	$("#product-edit-form select[name=brand]").val('<option value="'+data.brand +'" selected>'+data.brand+"</option>");
	$("#product-edit-form select[name=category]").val('<option value="'+data.category+'" selected>'+data.category+"</option>");
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	getBrandListForEdit(data)
	$('#edit-product-modal').modal('toggle');
}

function getBrandListForEdit(dataPresent){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displayBrandListForEdit(data, dataPresent);  
	   },
	   error: handleAjaxError
	});
}
function displayBrandListForEdit(data, dataPresent){
	$("#product-edit-form select[name=brand]").empty();
	$("#product-edit-form select[name=brand]").append('<option value="'+dataPresent.brand +'" selected>'+dataPresent.brand+'</option>');
	$("#product-edit-form select[name=category]").empty();
	$("#product-edit-form select[name=category]").append('<option value="'+dataPresent.category+'" selected>'+dataPresent.category+'</option>');
	var row1 = "";
	var row2 = "";
	console.log(data);
	for(var i=0; i < data.length; i++){
		if(data[i].brand == dataPresent.brand){
			continue;
		}
		row1 = '<option value="' + data[i].brand + '">' + data[i].brand + '</option>';
		$("#product-edit-form select[name=brand]").append(row1);
	}
	for(var i=0; i < data.length; i++){
		if(data[i].category == dataPresent.category){
			continue;
		}
		row2 = '<option value="' + data[i].category + '">' + data[i].category + '</option>';
		$("#product-edit-form select[name=category]").append(row2);
	}
}

function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displayBrandList(data);  
	   },
	   error: handleAjaxError
	});
}
function displayBrandList(data){
	$("#inputBrand").empty();
	$("#inputBrand").append('<option value="" selected>Choose...</option>');
	$("#inputCategory").empty();
	$("#inputCategory").append('<option value="" selected>Choose...</option>');
	var row1 = "";
	var row2 = "";
	console.log(data);
	for(var i=0; i < data.length; i++){
			row1 = '<option value="' + data[i].brand + '">' + data[i].brand + '</option>';
			row2 = '<option value="' + data[i].category + '">' + data[i].category + '</option>';
			$("#inputBrand").append(row1);
			$("#inputCategory").append(row2);
	}
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
$(document).ready(getBrandList)

