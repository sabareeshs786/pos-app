//Global variables
var downloadContent = "";
function getSchedulerUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/scheduler";
}

// function toArrayOfJsonObjects(){
//     var arr = [];
// 	for(var i=0; i < downloadContent.brands.length; i++){
// 		arr.push({});
// 		arr.at(-1)['brand'] = downloadContent.brands[i];
// 		arr.at(-1)['category'] = downloadContent.categories[i];
// 		arr.at(-1)['quantity'] = downloadContent.quantities[i];
// 		arr.at(-1)['revenue'] = downloadContent.totalAmounts[i];
// 	}
//     return arr;
// }
//BUTTON ACTIONS

function getSchedulerReportList(){
	var url = getSchedulerUrl();

	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
			downloadContent = data;
			console.log(data);
	   		displaySchedulerReportList(data);
	   },
	   error: handleAjaxError
	});
	return false;
}

// function processData(){
// 	var url = getSchedulerUrl();
// 	var $form = $('#sales-report-form');
// 	var json = toJson($form);
// 	console.log(json);
// 	$.ajax({
// 		url: url,
// 		type: 'POST',
// 		data: json,
// 		dataType : 'json',
// 		headers: {
// 			'Content-Type': 'application/json'
// 		},	   
// 		success: function(data) {
// 				downloadContent = data;
// 				displaySchedulerReportList(data);
// 		},
// 		error: handleAjaxError
// 	 });
 
// 	 return false;
// }

//UI DISPLAY METHODS

function displaySchedulerReportList(data){
	$("#scheduler-report-table-all-body").empty();
    var row = "";
	for (var i = 0; i < data.length; i++) {
		row = "<tr><td>"
		+ data[i].date + "</td><td>"
		+ data[i].invoicedOrdersCount + "</td><td>"
		+ data[i].invoicedItemsCount + "</td><td>"
		+ data[i].totalRevenue +"</td></tr>";
		$("#scheduler-report-table-all-body").append(row);
	}
}

// function setdates(){
// 	console.log($('#sales-report-form input[name=startDate]').val());
// 	$('#sales-report-form input[name=startDate]').val(getDateAsstring(6));
// 	$('#sales-report-form input[name=endDate]').val(getDateAsstring());
// }

// function getDateAsstring(offsetMonths=0){
// 	const d = new Date();
// 	var date = d.getDate();
// 	var month = d.getMonth();
// 	var year = d.getFullYear();
// 	var hour = d.getHours();
// 	var minute = d.getMinutes();
// 	var second = d.getSeconds();

// 	month += 1;
// 	month -= offsetMonths;
// 	while(month < 0){
// 		month += 12;
// 		year -= 1;
// 	}
// 	if(month == 2 && date > 28){
// 		date = 28;
// 	}

// 	if(date.toString().length == 1){
// 		date = '0' + date.toString();
// 	}
// 	if(month.toString().length == 1){
// 		month = '0' + month.toString();
// 	}
// 	if(hour.toString().length == 1){
// 		hour = '0' + hour.toString();
// 	}
// 	if(minute.toString().length == 1){
// 		minute = '0' + minute.toString();
// 	}
// 	if(second.toString().length == 1){
// 		second = '0' + second.toString();
// 	}

// 	year = year.toString();
// 	var dateString = year + "-"+ month + "-" + date +"T" + 
// 	hour + ":" + minute + ":" + second;
// 	console.log(dateString);
// 	return dateString;
// }

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
        fileUrl = navigator.msSaveBlob(blob, 'posdaysales.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'posdaysales.tsv');
    tempLink.click();
}

function downloadReport(){
	writeSalesReportFileData(downloadContent);
}

//INITIALIZATION CODE
function init(){
	// $('#process-data').click(processData);
	$('#refresh-data').click(getSchedulerReportList);
	$('#download-data').click(downloadReport);
}

$(document).ready(init);
// $(document).ready(setdates);
$(document).ready(getSchedulerReportList);