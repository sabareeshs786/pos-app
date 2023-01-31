function getRole(){
	var info = $("meta[name=info]").attr("content");
	var json = JSON.parse(info);
	var role = json.role;
	return role;
}

function enableOrDisable(){
	if(getRole() != 'supervisor'){
		$('input').attr('disabled', true);
		$('button').attr('disabled', true);
	}
	else{
		$('input').attr('disabled', false);
		$('button').attr('disabled', false);
	}
}

//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}
function toJsonArray($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = [];
    var numOfKeys = 0;
    var numOfValues = 0;
    var keys = new Set();
    data.push(new Object())
    for(s in serialized){
        if(serialized[s]['name'] in data.at(-1)){
            data.push(new Object());
            data.at(-1)[serialized[s]['name']] = serialized[s]['value'];
        }
        else{
            data.at(-1)[serialized[s]['name']] = serialized[s]['value'];
        }
    }
    var json = JSON.stringify(data);
    return data;
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	alert(response.message);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tab-separated-values;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'downloadbrand.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'downloadbrand.tsv');
    tempLink.click(); 
}

function loadEmailAndPassword(){
    $("#login-form input[name=email]").val("hari@gmail.com");
    $("#login-form input[name=password]").val("1234");
}
function onlyNonNegativeInt() {
    $('input[type="number"]').keypress(function(event) {
      var keycode = (event.keyCode ? event.keyCode : event.which);
      if (!(keycode >= 48 && keycode <= 57)) {
        event.preventDefault();
      }
    });
  }

function decimalNumber(){
    var asciiValueOfDot = ".".charCodeAt(0);
    $('.decimal-number').keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
    if (!(keycode >= 48 && keycode <= 57) && (keycode != 46 || $(this).val().indexOf('.') != -1) && keycode != 8) {
      event.preventDefault();
    }
    });
}
$(document).ready(loadEmailAndPassword);
$(document).ready(onlyNonNegativeInt);
$(document).ready(decimalNumber);