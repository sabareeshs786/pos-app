//Global variables
var dataOfItem = null;
var dataOfItemForEdit = null;
var barcodes = [];
var quantities = [];
var sellingPrices = [];
var names = [];
var totals = [];
var barcodeSet = new Set();


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
		xhrFields: {
			responseType: 'blob'
		 },
		success: function(blob) {
			console.log(blob.length);
			var link=document.createElement('a');
			link.href=window.URL.createObjectURL(blob);
			link.download="Invoice" + new Date() + ".pdf";
			link.click();
				
		},
		error: handleAjaxError
	 });
	 return false;
}

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

function fillValues(data){
	// $('#place-order-form input[name=quantity]').attr("readonly", false);
	// $('#place-order-form input[name=sellingPrice]').attr("readonly", false);
	$('#place-order-form input[name=quantity]').val(1);
	$('#place-order-form input[name=sellingPrice]').val(data.mrp);
	$('#add-item').attr('disabled', false);
}

function fillValuesForEdit(data){
	// $('#edit-added-item-form input[name=quantity]').attr("readonly", false);
	// $('#edit-added-item-form input[name=sellingPrice]').attr("readonly", false);
	$('#edit-added-item-form input[name=quantity]').val(1);
	$('#edit-added-item-form input[name=sellingPrice]').val(data.mrp);
	$('#update-added-item').attr('disabled', false);
}

function resetToDefault(){
	$('#add-item').attr('disabled', true);
	$('#place-order-form input[name=quantity]').val('');
	$('#place-order-form input[name=sellingPrice]').val('');
	// $('#place-order-form input[name=quantity]').attr("readonly", true);
	// $('#place-order-form input[name=sellingPrice]').attr("readonly", true);
}

function resetToDefaultForEdit(){
	$('#update-added-item').attr('disabled', true);
	$('#edit-added-item-form input[name=quantity]').val('');
	$('#edit-added-item-form input[name=sellingPrice]').val('');
	// $('#edit-added-item-form input[name=quantity]').attr("readonly", true);
	// $('#edit-added-item-form input[name=sellingPrice]').attr("readonly", true);
}

function getProduct(){
	var barcode = $('#place-order-form input[name=barcode]').val();
	console.log("Barcode: "+barcode);
	var url = getProductUrl() + '/' + barcode;
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
			console.log(data);
			dataOfItem = data;
			fillValues(data);
	   },
	   error: function(response){
			dataOfItem = null;
			resetToDefault();
	   }
	});
	return false;
}

function getProductForEdit(){
	var barcode = $('#edit-added-item-form input[name=barcode]').val();
	console.log("Barcode: "+barcode);
	if(barcode.length >= 4){
	var url = getProductUrl() + '/' + barcode;
	$.ajax({
	   url: url,
	   type: 'GET',
	   dataType : 'json',
	   contentType : 'application/json',
	   success: function(data) {
			console.log("From getProductForEdit: "+data);
			dataOfItemForEdit = data;
			fillValuesForEdit(data);
	   },
	   error: function(response){
			dataOfItemForEdit = null;
			resetToDefaultForEdit();
	   }
	});
}
	return false;
}

function addItem(){
	var barcode = $('#place-order-form input[name=barcode]').val();
	var quantity = $('#place-order-form input[name=quantity]').val();
	var sellingPrice = $('#place-order-form input[name=sellingPrice]').val();
	if(barcodeSet.has(barcode)){
		var barcode = $('#place-order-form input[name=barcode]').val('');
		resetToDefault();
	}
	else{
		barcodeSet.add(barcode);
		var json = {'barcode': barcode, 
					'quantity': quantity, 
					'sellingPrice': sellingPrice
				};
		json = JSON.stringify(json);
		if(validator(json)){
			barcodes.push(barcode);
			quantities.push(quantity);
			sellingPrices.push(sellingPrice);
			names.push(dataOfItem.name);
			totals.push(quantity * parseFloat(sellingPrice));
			console.log("Barcodes: >>: "+barcodes);
			console.log("Quantities: >>: "+quantities);
			console.log("Selling prices: >>: "+sellingPrices);
			updateAddedItemsTable();
			resetToDefault();
		}
	}
}

function updateAddedItemsTable(){
	$('#added-items').empty();
	var sno = 0;
	for(var i=0; i < barcodes.length; i++){
		sno += 1;
		var buttonHtml = '<button onclick="editAddedItem(' + i + ')" class=\"btn btn-success\">Edit</button>'
		+ '&nbsp;<button onclick="deleteAddedItem(' + i + ')" class=\"btn btn-danger\">Delete</button>';
		var row = "<tr><td>"
		+ sno + "</td><td>"
		+ barcodes[i] + "</td><td>"
		+ names[i] + "</td><td>"
		+ quantities[i] + "</td><td>"
		+ sellingPrices[i] + "</td><td>"
		+ totals[i] + "</td><td>"
		+ buttonHtml + "</td></tr>";
		$('#added-items').append(row);
	}
}

function editAddedItem(i){
	console.log("Edit clicked i="+i);
	$('#place-order-modal').modal('toggle');
	$('#edit-added-item-modal').modal('toggle');
	displayEditAddedItem(i);
}

function displayEditAddedItem(i){
	$('#edit-added-item-form input[name=quantity]').attr("readonly", false);
	$('#edit-added-item-form input[name=sellingPrice]').attr("readonly", false);
	$('#edit-added-item-form input[name=quantity]').val(quantities[i]);
	$('#edit-added-item-form input[name=sellingPrice]').val(sellingPrices[i]);
	$('#edit-added-item-form input[name=barcode]').val(barcodes[i]);
	$('#edit-added-item-form input[name=i]').val(i);
	$('#update-added-item').attr("disabled", false);
	getProductForEdit();
}

function updateAddedItem(){
	var i = $('#edit-added-item-form input[name=i]').val();
	barcodes[i] = $('#edit-added-item-form input[name=barcode]').val();
	quantities[i] = $('#edit-added-item-form input[name=quantity]').val();
	sellingPrices[i] = $('#edit-added-item-form input[name=sellingPrice]').val();
	names[i] = dataOfItemForEdit.name;
	totals[i] = quantities[i] * parseFloat(sellingPrices[i]);
	resetToDefaultForEdit();
	$('#edit-added-item-modal').modal('toggle');
	$('#place-order-modal').modal('toggle');
	resetToDefaultForEdit();
	resetToDefault();
	updateAddedItemsTable();
}

function deleteAddedItem(i){
	barcodes.splice(i, 1);
	quantities.splice(i, 1);
	sellingPrices.splice(i, 1);
	names.splice(i, 1);
	totals.splice(i, 1);
	updateAddedItemsTable();
}
function displayOrderModal(){
	clearAll();
	updateAddedItemsTable();
	// Toggle modal
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
				handleAjaxSuccess("Order Placed Successsfully!!!");
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
function clearAll(){
	barcodes = [];
	quantities = [];
	sellingPrices = [];
	names = [];
	totals = [];
	resetToDefault();
	$('#place-order-form input[name=barcode]').val('');
	$('#edit-added-item-form input[name=barcode]').val('');
	$("#added-items").empty();
}

//INITIALIZATION CODE
function init(){
	$('#place-order').click(displayOrderModal);
	$('#add-item').click(addItem);
	$('#cancle1').click(clearAll);
	$('#cancel2').click(clearAll);
	$('#place-order-confirm').click(placeOrder);
	$('#inputPageSize').on('change', getOrderListUtil);
	$('#place-order-form input[name=barcode]').on('change',function(){
		getProduct();
	});
	$('#edit-added-item-form input[name=barcode]').on('change', function(){
		getProductForEdit();
	});
	$('#update-added-item').click(updateAddedItem);
}

$(document).ready(init);
$(document).ready(getOrderListUtil);
$(document).ready(enableOrDisable);
