
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandreport";
}

//BUTTON ACTIONS

function getBrandList(){
	var url = getBrandReportUrl();
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
	return false;
}

function processData(){
	var $form = $('#brand-report-form')
	var brand = $('#brand-report-form input[name=brand]').val();
	var category = $('#brand-report-form input[name=category]').val();
	if(brand.length != 0){
		var url = getBrandReportUrl() + '/brand/' + brand;
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
	// if(category.length != 0){
	// 	var url = getBrandReportUrl() + '/category' + category;
	// 	$.ajax({
	// 		url: url,
	// 		type: 'GET',
	// 		dataType : 'json',
	// 		contentType : 'application/json',
	// 		success: function(data) {
	// 				displayBrandList(data);  
	// 		},
	// 		error: handleAjaxError
	// 	 });
	//}
}

//UI DISPLAY METHODS

function displayBrandList(data){
	$("#brand-table-body").empty();
    var row = "";
    var sno = 0;
	for (var i = 0; i < data.length; i++) {
	sno += 1;
	var buttonHtml = '<button onclick="deleteBrand(' + data[i].id + ')">delete</button>'
	buttonHtml += ' <button onclick="displayEditBrand(' + data[i].id + ')">edit</button>'
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
}

$(document).ready(init);
$(document).ready(getBrandList);