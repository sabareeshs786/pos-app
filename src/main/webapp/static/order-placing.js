
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

//GLOBAL VARIABLES
var htmlContent = '';

function getHtmlContent(){
	htmlContent = $('#place-order-form').html();
}

//BUTTON ACTIONS
function addItemToExistingOrder(event){
	var $form = $("#place-order-form");
	var json = toJsonArray($form);
	for(var i=0; i < json.length; i++){
		json[i]['quantity'] = parseInt(json[i]['quantity']);
		json[i]['sellingPrice'] = parseFloat(json[i]['sellingPrice']);
	}
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
		history.back();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateOrder(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-edit-form input[name=id]").val();	
	var url = getOrderUrl() + "/" + id;


	//Set the values to update
	var $form = $("#order-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();   
	   },
	   error: handleAjaxError
	});

	return false;
}

function addRow(){
	$('#place-order-form').append(htmlContent);
}

//INITIALIZATION CODE
function init(){
	$('#place-order-confirm').click(addItemToExistingOrder);
	$('#add-row').click(addRow);
}

$(document).ready(init);
$(document).ready(getHtmlContent)
