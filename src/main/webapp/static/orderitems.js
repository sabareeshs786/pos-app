function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderItemsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/orderitems";
}

function getOrderId(){
	var orderId = $("meta[name=orderId]").attr("content");
	return orderId;
}

function getOrderItems(){
	var url = getOrderUrl() + "/" + getOrderId();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			displayOrderItems(data);
	   },
	   error: handleAjaxError
	});	
}

//UI DISPLAY METHODS

function displayOrderItems(data){
	$('#order-items-table-body').empty();
	var row = '';
	var sno = 0;
	console.log(data);
	for(var i = 0; i < data.length; i++){
		var buttonHtml = ' <button onclick="displayEditOrderItem(' + data[i].id + ')">Edit</button>'
		sno += 1;
		row = '<tr><td>' + sno + '</td>'
		+ '<td>' + data[i].productName + '</td>'
		+ '<td>' + data[i].quantity + '</td>'
		+ '<td>' + data[i].sellingPrice + '</td>'
		+ '<td>' + data[i].mrp + '</td>'
		+ '<td>' + buttonHtml + '</td></tr>'
		$('#order-items-table-body').append(row);
	}
}
function displayEditOrderItem(id){
	var url = getOrderItemsUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItem(data);   
	   },
	   error: handleAjaxError
	});	
}

function displayOrderItem(data){
	console.log("Order item data: " + data);
	$("#edit-order-item-form input[name=barcode]").val(data.barcode);	
	$("#edit-order-item-form input[name=quantity]").val(data.quantity);
	$("#edit-order-item-form input[name=sellingPrice]").val(data.sellingPrice);
	$("#edit-order-item-form input[name=id]").val(data.id);
	$('#edit-order-item-modal').modal('toggle');
}

function updateOrderItem(){
	$('#edit-order-item-modal').modal('toggle');
	var id = $("#edit-order-item-form input[name=id]").val();

	var url = getOrderItemsUrl() + "/" + id;


	//Set the values to update
	var $form = $("#edit-order-item-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getOrderItems();   
	   },
	   error: handleAjaxError
	});

	return false;
}
//INITIALIZATION CODE
function init(){
	$('#update-order-item').click(updateOrderItem);
}

$(document).ready(init);
$(document).ready(getOrderItems);
