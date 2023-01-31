function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventoryreport";
}

//Global variables
var downloadContent = "";
//BUTTON ACTIONS

function getInventoryList(){
	var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
			downloadContent = data;
	   		displayInventoryList(data);  
	   },
	   error: handleAjaxError
	});
	return false;
}

function processData(){
	var $form = $('#inventory-report-form');
	var brand = $('#inventory-report-form input[name=brand]').val();
	var category = $('#inventory-report-form input[name=category]').val();
	console.log("Brand="+brand);
	console.log("Category="+category);
	if(brand.length != 0 && category.length != 0){
		var url = getInventoryReportUrl() + '/brand/' + brand + '/category/' + category;
		console.log(url);
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
				downloadContent = data;
				displayInventoryList(data);
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
				downloadContent = data;
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
				downloadContent = data;
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
	+ data.category + "</td><td>"
	+ data.quantity + "</td></tr>";
	$("#inventory-table-body").append(row);
}

function displayInventoryList(data){
	$("#inventory-table-body").empty();
    var row = "";
    var sno = 0;
	var dataMap = new Map([]);
	var grandTotalQuantity = 0
	for(var i =0; i < data.length; i++){
		if(dataMap.has(data[i].brand + "-"+ data[i].category)){
			var initialQuantity = dataMap.get(data[i].brand + "-"+ data[i].category);
			var finalQuantity = initialQuantity + data[i].quantity;
			dataMap.set(data[i].brand + "-"+ data[i].category, finalQuantity);
		}
		else{
			dataMap.set(data[i].brand + "-"+ data[i].category, data[i].quantity);
		}
		grandTotalQuantity += data[i].quantity;
	}

	dataMap.forEach(function(value, key){
		sno += 1;
		var strArr = key.split("-");
		row = "<tr><td>" 
		+ sno + "</td><td>" 
		+ strArr[0] + "</td><td>"
		+ strArr[1] + "</td><td>"
		+ value + "</td></tr>";
		$("#inventory-table-body").append(row);
	}
	)
	$("#inventory-table-body").append('<tr styple="font-size:30px; text-align:right;"><td colspan="3">Total</td><td>' + grandTotalQuantity + '</td></tr>');
	
}
function writeInventoryReportFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tab-separated-values;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'inventoryreport.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'inventoryreport.tsv');
    tempLink.click();
}

function downloadReport(){
	console.log(downloadContent);
	writeInventoryReportFileData(downloadContent);
}
//INITIALIZATION CODE
function init(){
	$('#process-data').click(processData);
	$('#refresh-data').click(getInventoryList);
	$('#download-data').click(downloadReport);
}

$(document).ready(init);
$(document).ready(getInventoryList);