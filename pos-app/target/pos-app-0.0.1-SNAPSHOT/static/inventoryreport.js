function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/inventoryreport";
}

//Global variables
var downloadContent = "";
//BUTTON ACTIONS

function getInventoryListUtil(){
	var pageSize = $('#inputPageSize').val();
	var brand = $('#inventory-report-form input[name=brand]').val();
	var category = $('#inventory-report-form input[name=category]').val();
	if(brand == undefined)
		brand = '';
	if(category == undefined)
		category = '';
	getInventoryList(brand, category, 0, pageSize);
}

function getInventoryList(brand, category, pageNumber, pageSize){
	var url = getInventoryReportUrl()+ 
	'?brand=' + brand + 
	'&category=' + category + 
	'&pagenumber=' + pageNumber + 
	'&size=' + pageSize;

	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
			console.log(data);
			downloadContent = data.content;
	   		displayInventoryList(data.content, pageNumber*pageSize);
			paginatorForReport(data, "getInventoryList", brand, category, pageSize);
	   },
	   error: function(response){
		handleAjaxError(response);
		$('#inventory-report-form input[name=brand]').val('');
		$('#inventory-report-form input[name=category]').val('');
	   }
	});
	return false;
}

//UI DISPLAY METHODS

function displayInventoryList(data, sno){
	$("#inventory-table-body").empty();
    var row = "";
	// var dataMap = new Map([]);
	// var grandTotalQuantity = 0
	// for(var i =0; i < data.length; i++){
	// 	if(dataMap.has(data[i].brand + "-"+ data[i].category)){
	// 		var initialQuantity = dataMap.get(data[i].brand + "-"+ data[i].category);
	// 		var finalQuantity = initialQuantity + data[i].quantity;
	// 		dataMap.set(data[i].brand + "-"+ data[i].category, finalQuantity);
	// 	}
	// 	else{
	// 		dataMap.set(data[i].brand + "-"+ data[i].category, data[i].quantity);
	// 	}
	// 	grandTotalQuantity += data[i].quantity;
	// }

	// dataMap.forEach(function(value, key){
		// sno += 1;
		// var strArr = key.split("-");
		// row = "<tr><td>" 
		// + sno + "</td><td>" 
		// + strArr[0] + "</td><td>"
		// + strArr[1] + "</td><td>"
		// + value + "</td></tr>";
		// $("#inventory-table-body").append(row);
	// }
	// )
	for(var i=0; i < data.length; i++){
		sno += 1;
		// var strArr = key.split("-");
		row = "<tr><td>" 
		+ sno + "</td><td>" 
		+ data[i].barcode + "</td><td>"
		+ data[i].productName + "</td><td>"
		+ data[i].brand + "</td><td>"
		+ data[i].category + "</td><td>"
		+ data[i].quantity + "</td></tr>";
		$("#inventory-table-body").append(row);
	}
	// $("#inventory-table-body").append('<tr styple="font-size:30px; text-align:right;"><td colspan="3">Total</td><td>' + grandTotalQuantity + '</td></tr>');
	
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

function rearrange(downloadContent){
	var downloadContentRearranged = [];
	for(var i=0; i < downloadContent.length; i++){
		var rearrangedObj = {};
		rearrangedObj.productId = downloadContent[i].productId;
		rearrangedObj.brand = downloadContent[i].brand;
		rearrangedObj.category = downloadContent[i].category;
		rearrangedObj.productName = downloadContent[i].productName;
		rearrangedObj.barcode = downloadContent[i].barcode;
		rearrangedObj.quantity = downloadContent[i].quantity;
		downloadContentRearranged.push(rearrangedObj);
	}
	console.log(downloadContentRearranged);
	return downloadContentRearranged;
}

function downloadReport(){
	var brand = $('#inventory-report-form input[name=brand]').val();
	var category = $('#inventory-report-form input[name=category]').val();
	if(brand == undefined)
		brand = '';
	if(category == undefined)
		category = '';
	var url = getInventoryReportUrl()+ 
	'?brand=' + brand + 
	'&category=' + category + 
	'&pagenumber=' + 
	'&size=';
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
		data = rearrange(data);
		downloadContent.sort(function(a, b){
			return a.barcode - b.barcode;
		})
		writeInventoryReportFileData(data);
	   },
	   error: function(response){
		handleAjaxError(response);
		$('#inventory-report-form input[name=brand]').val('');
		$('#inventory-report-form input[name=category]').val('');
	   }
	});

	return false;
	
}
//INITIALIZATION CODE
function init(){
	$('#process-data').click(getInventoryListUtil);
	$('#download-data').click(downloadReport);
	$('#reset-data').click(getInventoryListUtil);
}

$(document).ready(init);
$(document).ready(getInventoryListUtil);