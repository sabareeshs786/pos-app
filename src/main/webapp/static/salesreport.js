function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
}

//BUTTON ACTIONS

function getSalesList(){
	var url = getSalesReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displaySalesList(data);  
	   },
	   error: handleAjaxError
	});
	return false;
}

function processData(){
	var $form = $('#brand-report-form')
	var brand = $('#brand-report-form input[name=brand]').val();
	var category = $('#brand-report-form input[name=category]').val();
	if(brand.length != 0 && category.length != 0){
		var url = getSalesReportUrl() + '/brand/' + brand + '/category/' + category;
		console.log(url);
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
					displaySalesList1(data);  
			},
			error: handleAjaxError
		 });
	}
	else if(brand.length != 0 && category.length == 0){
		var url = getSalesReportUrl() + '/brand/' + brand;
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
					displaySalesList(data);  
			},
			error: handleAjaxError
		 });
	}
	else if(brand.length == 0 && category.length != 0){
		var url = getSalesReportUrl() + '/category/' + category;
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
					displaySalesList(data);  
			},
			error: handleAjaxError
		 });
	}
	else{
		alert("Enter brand or category");
	}
}

//UI DISPLAY METHODS

function displaySalesList1(data){
	$("#brand-table-body").empty();
    var row = "";
	var sno = 1;
	row = "<tr><td>" 
	+ sno + "</td><td>" 
	+ data.brand + "</td><td>"
	+ data.category + "</td></tr>";
	$("#brand-table-body").append(row);
}

function displaySalesList(data){
	$("#brand-table-body").empty();
    var row = "";
    var sno = 0;
	for (var i = 0; i < data.length; i++) {
		sno += 1;
		row = "<tr><td>" 
		+ sno + "</td><td>" 
		+ data[i].brand + "</td><td>"
		+ data[i].category + "</td></tr>";
		$("#brand-table-body").append(row);
	}
	
}

//INITIALIZATION CODE
function init(){
	$('#process-data').click(processData);
	$('#refresh-data').click(getSalesList)
}

$(document).ready(init);
$(document).ready(getSalesList);