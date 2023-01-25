function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
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
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
				getProductList();
		},
		error: handleAjaxError
	 });
 
	 return false;
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
	var dateString = year + "-"+ month + "-" + date;
	console.log(dateString);
	return dateString;
}

//INITIALIZATION CODE
function init(){
	$('#process-data').click(processData);
	$('#refresh-data').click(getSalesReportList);
}

$(document).ready(init);
$(document).ready(getSalesReportList);
$(document).ready(setdates);