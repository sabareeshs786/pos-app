function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

var barcodes = [];
var quantities = [];
var sellingPrices = [];

function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displayOrderList(data);  
	   },
	   error: handleAjaxError
	});
}

function addItem(){
	var barcode = $('#place-order-form input[name=barcode]').val();
	var quantity = $('#place-order-form input[name=quantity]').val();
	var sellingPrice = $('#place-order-form input[name=sellingPrice]').val();
	barcodes.push(barcode);
	quantities.push(quantity);
	sellingPrice.push(sellingPrice);
	$('#place-order-form input[name=barcode]').val().empty();
	$('#place-order-form input[name=quantity]').val().empty();
	$('#place-order-form input[name=sellingPrice]').val().empty();
}

function displayOrderModal(){
	$('#place-order-modal').modal('toggle');
	// while($('#place-order-modal').is(':visible')){
	// 	var barcode = $('#place-order-form input[name=barcode]').val();
	// 	if(barcode.length == 0){

	// 	}
	// } 
}

function placeOrder(){
	$('#place-order-modal').modal('toggle');
	var $form = $("#place-order-form");
	var json = toJsonArray($form);
	json = JSON.stringify(json);
	var url = getOrderUrl();
	console.log(json);
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
		},
		error: handleAjaxError
	 });
 
	 return false;
}

//UI DISPLAY METHODS

function displayOrderList(data){
	$("#order-table-body").empty();
    var row = "";
    var sno = 0;
	for (var i = 0; i < data.length; i++) {
	sno += 1;
	var buttonHtml = '<button onclick="displayOrderItemsView(' + data[i].id + ')">View</button>&nbsp;&nbsp;'
					 + '<button onclick="displayOrderItemsEdit(' + data[i].id + ')">Edit</button>';
	row = "<tr><td>" 
	+ sno + "</td><td>" 
	+ data[i].id + "</td><td>"
	+ data[i].time + "</td><td>" 
	+ data[i].totalAmount + "</td><td>"
	+ buttonHtml 
	+ "</td></tr>";
	$("#order-table-body").append(row);
	}
	
}

function displayOrderItemsOfanId(data){
	var rows = '';
	var sno = 0;
	console.log(data);
	for(var i = 0; i < data.length; i++){
		sno += 1;
		rows += '<tr><td>' + sno + '</td>'
		+ '<td>' + data.productName + '</td>'
		+ '<td>' + data.sellingPrice + '</td>'
		+ '<td>' + data.mrp + '</td></tr>';
		
	}
	return rows;
}

function displayOrderItemsView(id){
	window.location.href = "./orderitems/" + id + '/' + 'view';
}

function displayOrderItemsEdit(id){
	window.location.href = "./orderitems/" + id + '/' + 'edit';
}
//INITIALIZATION CODE
function init(){
	$('#place-order').click(displayOrderModal);
	$('#add-item').click(addItem);
}

$(document).ready(init);
$(document).ready(getOrderList);
