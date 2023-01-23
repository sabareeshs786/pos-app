
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventoryreport";
}

//BUTTON ACTIONS

function getInventoryList(){
	var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displayInventoryList(data);  
	   },
	   error: handleAjaxError
	});
	return false;
}

function processData(){
	var $form = $('#inventory-report-form')
	var inventory = $('#inventory-report-form input[name=brand]').val();
	var category = $('#inventory-report-form input[name=category]').val();
	if(brand.length != 0 && category.length != 0){
		var url = getInventoryReportUrl() + '/brand/' + brand + '/category/' + category;
		console.log(url);
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
					displayInventoryList1(data);  
			},
			error: handleAjaxError
		 });
	}
	else if(brand.length != 0 && category.length == 0){
		var url = getInventoryReportUrl() + '/brand/' + brand;
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
					displayInventoryList(data);  
			},
			error: handleAjaxError
		 });
	}
	else if(brand.length == 0 && category.length != 0){
		var url = getInventoryReportUrl() + '/category/' + category;
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
					displayInventoryList(data);  
			},
			error: handleAjaxError
		 });
	}
	else{
		alert("Enter brand or category");
	}
}

//UI DISPLAY METHODS

function displayInventoryList1(data){
	$("#inventory-table-body").empty();
    var row = "";
	var sno = 1;
	row = "<tr><td>" 
	+ sno + "</td><td>" 
	+ data.brand + "</td><td>"
	+ data.category + "</td></tr>";
	$("#inventory-table-body").append(row);
}

function displayInventoryList(data){
	$("#inventory-table-body").empty();
    var row = "";
    var sno = 0;
	for (var i = 0; i < data.length; i++) {
		sno += 1;
		row = "<tr><td>" 
		+ sno + "</td><td>" 
		+ data[i].brand + "</td><td>"
		+ data[i].category + "</td></tr>";
		$("#inventory-table-body").append(row);
	}
	
}

//INITIALIZATION CODE
function init(){
	$('#process-data').click(processData);
	$('#refresh-data').click(getInventoryList)
}

$(document).ready(init);
$(document).ready(getInventoryList);