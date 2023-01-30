//GLOBAL VARIABLES
var htmlContent = '';

function getHtmlContent(){
	htmlContent = $('#place-order-form').html();
}

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

function getMode(){
	var mode = $("meta[name=mode]").attr("content");
	return mode;
}

function getOrderItemsUtil(){
	var pageSize = $('#inputPageSize').val();
	getOrderItems(0, pageSize);
}

function getOrderItems(pageNumber, pageSize){
	var url = getOrderUrl() + "/" + getOrderId() + '/' + pageNumber + '/' + pageSize;
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			displayOrderItems(data.content, pageNumber*pageSize);
			var pagination = "";
			   for (var i = data.number; i < data.number + 3 && i < data.totalPages; i++) {
				   var active = "";
				   if (i == data.number) {
				   active = "active";
				   }
				   pagination += "<li class='page-item " + active + "'><a class='page-link' href='#pageNumber=" + (i+1) +"' onclick='getOrderItems(" + i + ", " + pageSize + ")'>" + (i + 1) + "</a></li>";
			   }
			   if (data.number > 0) {
				   pagination = "<li class='page-item'><a class='page-link' href='#pageNumber=" + data.number +"' id='previous'>Previous</a></li>" + pagination;
			   }
			   if (data.number < data.totalPages - 1) {
				   pagination = pagination + "<li class='page-item'><a class='page-link' href='#pageNumber=" + (data.number + 2) + "' id='next'>Next</a></li>";
			   }
			   $("#paginationContainer").html(pagination);
			   $("#previous").click(function() {
				   getOrderItems(data.number - 1, pageSize);
			   });
			   $("#next").click(function() {
				   getOrderItems(data.number + 1, pageSize);
			   }); 
	   },
	   error: handleAjaxError
	});	
}

//UI DISPLAY METHODS

function displayOrderItems(data, sno){
	$('#order-items-table-body').empty();
	if(getMode() == 'edit'){
		$('#order-items-table-head').append('<th scope="col">Actions</th>');
	}
	var row = '';
	console.log(data);
	for(var i = 0; i < data.length; i++){
		var buttonHtml = ' <button onclick="displayEditOrderItem(' + data[i].id + ')">Edit</button>'
		sno += 1;
		row = '<tr><td>' + sno + '</td>'
		+ '<td>' + data[i].productName + '</td>'
		+ '<td>' + data[i].quantity + '</td>'
		+ '<td>' + data[i].sellingPrice + '</td>'
		+ '<td>' + data[i].mrp + '</td>';
		if(getMode() == 'edit'){
			row += '<td>' + buttonHtml + '</td></tr>';
		}
		else{
			row += '</tr>';
		}
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
	   		getOrderItemsUtil();   
	   },
	   error: handleAjaxError
	});

	return false;
}

function addRow(){
	$('#place-order-form').append(htmlContent);
}

//BUTTON ACTIONS
function addItemToExistingOrder(event){
	var $form = $("#place-order-form");
	var orderId =  getOrderId();
	var json = toJsonArray($form);
	for(var i=0; i < json.length; i++){
		json[i]['quantity'] = parseInt(json[i]['quantity']);
		json[i]['sellingPrice'] = parseFloat(json[i]['sellingPrice']);
	}
	json = JSON.stringify(json);
	var url = getOrderUrl();
	console.log(json);
	// $.ajax({
	//    url: url,
	//    type: 'POST',
	//    data: json,
	//    headers: {
    //    	'Content-Type': 'application/json'
    //    },	   
	//    success: function(response) {
	// 	history.back();
	//    },
	//    error: handleAjaxError
	// });

	return false;
}

//INITIALIZATION CODE
function init(){
	$('#place-order-confirm').click(addItemToExistingOrder);
	$('#add-row').click(addRow);
	$('#update-order-item').click(updateOrderItem);
	$('#inputPageSize').on('change', getOrderItemsUtil);
}

$(document).ready(init);
$(document).ready(getOrderItemsUtil);
$(document).ready(getHtmlContent)