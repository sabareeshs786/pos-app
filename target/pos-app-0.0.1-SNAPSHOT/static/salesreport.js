//Global variables
var downloadContent = "";
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
}

function toArrayOfJsonObjects(){
    var arr = [];
	for(var i=0; i < downloadContent.brands.length; i++){
		arr.push({});
		arr.at(-1)['brand'] = downloadContent.brands[i];
		arr.at(-1)['category'] = downloadContent.categories[i];
		arr.at(-1)['quantity'] = downloadContent.quantities[i];
		arr.at(-1)['revenue'] = downloadContent.totalAmounts[i];
	}
    return arr;
}
//BUTTON ACTIONS

function getSalesReportList(){
	var url = getSalesReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
			downloadContent = data;
	   		displaySalesReportList(data);
	   },
	   error: handleAjaxError
	});
	return false;
}

function processData(){
	var url = getSalesReportUrl() + '/filter';
	var $form = $('#sales-report-form');
	var json = toJson($form);

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		dataType : 'json',
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(data) {
				downloadContent = data;
				displaySalesReportList(data);
		},
		error: handleAjaxError
	 });
 
	 return false;
}

//UI DISPLAY METHODS

function displaySalesReportList(data){
	$('#start-date').empty();
	$('#start-date').append(data.startDate);
	$('#end-date').empty();
	$('#end-date').append(data.endDate);
	console.log(data);
	$("#sales-report-table-all-body").empty();
    var row = "";
	for (var i = 0; i < data.brands.length; i++) {
		console.log(data.totalAmounts[i]);
		row = "<tr><td>"
		+ data.brands[i] + "</td><td>"
		+ data.categories[i] + "</td><td>"
		+ data.quantities[i] + "</td><td>" + 
		+ data.totalAmounts[i] +"</td></tr>";
		$("#sales-report-table-all-body").append(row);
	}
	
}

function setdates(){
	console.log($('#sales-report-form input[name=startDate]').val());
	$('#sales-report-form input[name=startDate]').val(getDateAsstring(6));
	$('#sales-report-form input[name=endDate]').val(getDateAsstring());
}

function getDateAsstring(offsetMonths=0){
	const d = new Date();
	var date = d.getDate();
	var month = d.getMonth();
	var year = d.getFullYear();

	month += 1;
	month -= offsetMonths;
	while(month < 0){
		month += 12;
		year -= 1;
	}
	if(month == 2 && date > 28){
		date = 28;
	}

	if(date.toString().length == 1){
		date = '0' + date.toString();
	}
	if(month.toString().length == 1){
		month = '0' + month.toString();
	}
	year = year.toString();
	var dateString = year + "-"+ month + "-" + date +"T" + 
	d.getHours().toString() + ":" + d.getMinutes().toString() + ":" + d.getSeconds();
	console.log(dateString);
	return dateString;
}

function writeSalesReportFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tab-separated-values;charset=utf-8;'});
	var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'salesreport.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'salesreport.tsv');
    tempLink.click();
}

function downloadReport(){
	arr = toArrayOfJsonObjects();
	writeSalesReportFileData(arr);
}

//INITIALIZATION CODE
function init(){
	$('#process-data').click(processData);
	$('#refresh-data').click(getSalesReportList);
	$('#download-data').click(downloadReport);
}

$(document).ready(init);
$(document).ready(getSalesReportList);
$(document).ready(setdates);