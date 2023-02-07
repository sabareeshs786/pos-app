function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/order";
}

function getInvoiceUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/invoice/download";
}
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function generateInvoicePdf(id){
	var url = getInvoiceUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		dataType : 'json',
		contentType : 'application/json',
		xhrFields: {
			responseType: 'blob'
		 },
		success: function(blob) {
			var link=document.createElement('a');
			link.href=window.URL.createObjectURL(blob);
			link.download="Invoice" + new Date() + ".pdf";
			link.click();
				
		},
		error: handleAjaxError
	 });
	 return false;
}

var barcodes = [];
var quantities = [];
var sellingPrices = [];

function getOrderListUtil(){
	var pageSize = $('#inputPageSize').val();
	getOrderList(0, pageSize);
}

function getOrderList(pageNumber, pageSize){
	var url = getOrderUrl()  + '/' + pageNumber + '/' + pageSize;
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		displayOrderList(data.content, pageNumber*pageSize);
			   $('#selected-rows').html('<h5>Selected ' + (pageNumber*pageSize + 1) + ' to ' + (pageNumber*pageSize + data.content.length) + ' of ' + data.totalElements +'</h5>');
			   var pagination = "";
			   for (var i = data.number; i < data.number + 3 && i < data.totalPages; i++) {
				   var active = "";
				   if (i == data.number) {
				   active = "active";
				   }
				   pagination += "<li class='page-item " + active + "'><a class='page-link' href='#pageNumber=" + (i+1) +"' onclick='getOrderList(" + i + ", " + pageSize + ")'>" + (i + 1) + "</a></li>";
			   }
			   if (data.number > 0) {
				   pagination = "<li class='page-item'><a class='page-link' href='#pageNumber=" + data.number +"' id='previous'>Previous</a></li>" + pagination;
			   }
			   if (data.number < data.totalPages - 1) {
				   pagination = pagination + "<li class='page-item'><a class='page-link' href='#pageNumber=" + (data.number + 2) + "' id='next'>Next</a></li>";
			   }
			   $("#paginationContainer").html(pagination);
			   $("#previous").click(function() {
				   getOrderList(data.number - 1, pageSize);
			   });
			   $("#next").click(function() {
				   getOrderList(data.number + 1, pageSize);
			   }); 
	   },
	   error: handleAjaxError
	});
	return false;
}

function getProduct(barcode){
	var url = getProductUrl() + '/' + barcode;
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
	   		return data;
	   },
	   error: handleAjaxError
	});
	return false;
}

function addItem(){
	var barcode = $('#place-order-form input[name=barcode]').val();
	var quantity = $('#place-order-form input[name=quantity]').val();
	var sellingPrice = $('#place-order-form input[name=sellingPrice]').val();
	var json = {'barcode': barcode, 
				'quantity': quantity, 
				'sellingPrice': sellingPrice
			   };
	json = JSON.stringify(json);
	if(validator(json)){
		barcodes.push(barcode);
		quantities.push(quantity);
		sellingPrices.push(sellingPrice);
		$('#place-order-form input[name=barcode]').val('');
		$('#place-order-form input[name=quantity]').val(1);
		$('#place-order-form input[name=sellingPrice]').val(0.00);
	}
}

function displayOrderModal(){
	$('#place-order-modal').modal('toggle');
}

function placeOrder(){
	$('#place-order-modal').modal('toggle');
	var json = { 'barcodes':barcodes, 
				'quantities': quantities, 
				'sellingPrices':sellingPrices
			};
	var json = JSON.stringify(json);
	if(validator(json)){
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
				handleAjaxSuccess(response);
				getOrderListUtil();
			},
			error: handleAjaxError
	 });
	}
	 return false;
}

//UI DISPLAY METHODS

function displayOrderList(data, sno){
	$("#order-table-body").empty();
    var row = "";
	for (var i = 0; i < data.length; i++) {
	sno += 1;
	var buttonHtml = '<button onclick="displayOrderItemsView(' + data[i].id + ')">View</button>&nbsp;&nbsp;'
					 + '<button onclick="displayOrderItemsEdit(' + data[i].id + ')">Edit</button>&nbsp;&nbsp;'
					 + '<button onclick="generateInvoicePdf(' + data[i].id + ')">Download Invoice</button>';
	row = "<tr><td>" 
	+ sno + "</td><td>" 
	+ data[i].id + "</td><td>"
	+ data[i].time + "</td><td>" 
	+ data[i].totalAmount + "</td><td>"
	+ buttonHtml
	+ "</td></tr>";
	$("#order-table-body").append(row);
	}
	enableOrDisable();
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
function clearValues(){
	barcodes = [];
	quantities = [];
	sellingPrices = [];
}

//INITIALIZATION CODE
function init(){
	$('#place-order').click(displayOrderModal);
	$('#add-item').click(addItem);
	$('#cancle1').click(clearValues);
	$('#cancel2').click(clearValues);
	$('#place-order-confirm').click(placeOrder);
	$('#inputPageSize').on('change', getOrderListUtil);
}

$(document).ready(init);
$(document).ready(getOrderListUtil);
$(document).ready(enableOrDisable);
