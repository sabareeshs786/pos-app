
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandreport";
}
//Global variables
var downloadContent = "";
//BUTTON ACTIONS

function getBrandList(){
	var url = getBrandReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
			downloadContent = data;
	   		displayBrandList(data);  
	   },
	   error: handleAjaxError
	});
	return false;
}

function processData(){
	var $form = $('#brand-report-form');
	var brand = $('#brand-report-form input[name=brand]').val();
	var category = $('#brand-report-form input[name=category]').val();
	if(brand.length != 0 && category.length != 0){
		var url = getBrandReportUrl() + '/brand/' + brand + '/category/' + category;
		console.log(url);
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
				downloadContent = data;
				displayBrandList1(data);  
			},
			error: handleAjaxError
		 });
	}
	else if(brand.length != 0 && category.length == 0){
		var url = getBrandReportUrl() + '/brand/' + brand;
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
				downloadContent = data;
				displayBrandList(data);  
			},
			error: handleAjaxError
		 });
	}
	else if(brand.length == 0 && category.length != 0){
		var url = getBrandReportUrl() + '/category/' + category;
		$.ajax({
			url: url,
			type: 'GET',
			dataType : 'json',
			contentType : 'application/json',
			success: function(data) {
				downloadContent = data;
				displayBrandList(data);  
			},
			error: handleAjaxError
		 });
	}
	else{
		alert("Enter brand or category");
	}
}

//UI DISPLAY METHODS

function displayBrandList1(data){
	$("#brand-table-body").empty();
    var row = "";
	var sno = 1;
	row = "<tr><td>" 
	+ sno + "</td><td>" 
	+ data.brand + "</td><td>"
	+ data.category + "</td></tr>";
	$("#brand-table-body").append(row);
}

function displayBrandList(data){
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
function writeBrandReportFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tab-separated-values;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'brandreport.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'brandreport.tsv');
    tempLink.click();
}

function downloadReport(){
	console.log(downloadContent);
	writeBrandReportFileData(downloadContent);
}

//INITIALIZATION CODE
function init(){
	$('#process-data').click(processData);
	$('#refresh-data').click(getBrandList);
	$('#download-data').click(downloadReport)
}

$(document).ready(init);
$(document).ready(getBrandList);