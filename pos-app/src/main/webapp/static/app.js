function getRole(){
	var role = $("meta[name=role]").attr("content");
	return role;
}

function validator(jsonStr){
    jsonObj = JSON.parse(jsonStr);
    for (var [key, value] of Object.entries(jsonObj)) {
        if(typeof(value) == "string"){
            value = value.trim();
        }
        if(value == null || value == undefined || value == ''){
            $("#error-message").notify("Value for "+ key + " is not entered or can't be interpreted");
            return false;
        }
    }
    return true;
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

function enableOrDisable(){
    console.log("Current users role: "+getRole());
	if(getRole() != 'supervisor'){
		$('input').attr('disabled', true);
		$('button').attr('disabled', true);
        $('#adminButton').attr("style", "display:none;");
	}
	else{
		$('input').attr('disabled', false);
		$('button').attr('disabled', false);
        $('#adminButton').attr("style", "display:block;");
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
    $("#error-message").notify(response.message, "error");
}

function handleAjaxSuccess(response){
 $("#success-message").notify(response, "success");
}

function readFileData(file, callback){
    var linesParsed = 0;
    var results = [];
    var config = {
        header: true,
        delimiter: "\t",
        skipEmptyLines: true,
        dynamicTyping: true,
        worker: true,
        step: function(row){
            linesParsed++;
            results.push(row.data);
            if(linesParsed > 500){
                this.abort();
            }
        },
        complete: function() {
            console.log(results);
            callback(results);
        },
        error: function(results){
            console.log(results);
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

function onlyNonNegativeInt() {
    $('input[type="number"]').keypress(function(event) {
      var keycode = (event.keyCode ? event.keyCode : event.which);
      if (!(keycode >= 48 && keycode <= 57)) {
        event.preventDefault();
      }
    });
}

function isAlphaNumeric(str) {
    return /^[a-zA-Z0-9]{8,}$/.test(str);
}
function noOfCharLimiter(){
    $('input[type="text"]').keypress(function(event) {
        var len = $(this).val().length;
        var str = $(this).val();
        var isAlNum = isAlphaNumeric(str);
        if (len >= 20 && isAlNum) {
            event.preventDefault();
        }
      });
      $('input[type="password"]').keypress(function(event) {
        var len = $(this).val().length;
        if (len >= 20) {
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

function paginator(data, func, pageSize){
    var pagination = '';
			for (var i = data.number - 2; i < data.number + 5 && i < data.totalPages; i++) {
				if(i < 0)
					continue;
				var active = "";
				if (i == data.number) {
				active = "active";
				}
				pagination += "<li class='page-item " + active + "'><a class='page-link' href='#pageNumber=" + (i+1) +"' onclick='" + func +"(" + i + ", " + pageSize + ")'>" + (i + 1) + "</a></li>";
			}
			if (data.number > 0) {
				pagination = "<li class='page-item'><a class='page-link' href='#pageNumber=" + data.number +"' id='previous'>Previous</a></li>" + pagination;
			}
			if (data.number < data.totalPages - 1) {
				pagination = pagination + "<li class='page-item'><a class='page-link' href='#pageNumber=" + (data.number + 2) + "' id='next'>Next</a></li>";
			}
			$("#paginationContainer").html(pagination);
			$("#previous").click(function() {
				eval(func)(data.number - 1, pageSize);
			});
			$("#next").click(function() {
				eval(func)(data.number + 1, pageSize);
			});
}

function paginatorForReport(data, func, brand, category, pageSize){
    var pagination = '';
			for (var i = data.number - 2; i < data.number + 5 && i < data.totalPages; i++) {
				if(i < 0)
					continue;
				var active = "";
				if (i == data.number) {
				active = "active";
				}
				pagination += "<li class='page-item " + active 
                + "'><a class='page-link' href='#pageNumber=" + (i+1) 
                +"' onclick='" + func +"(\"" + brand +"\", \"" + category + "\", " + i + ", " + pageSize + ")'>" + (i + 1) + "</a></li>";
			}
			if (data.number > 0) {
				pagination = "<li class='page-item'><a class='page-link' href='#pageNumber=" + data.number +"' id='previous'>Previous</a></li>" + pagination;
			}
			if (data.number < data.totalPages - 1) {
				pagination = pagination + "<li class='page-item'><a class='page-link' href='#pageNumber=" + (data.number + 2) + "' id='next'>Next</a></li>";
			}
			$("#paginationContainer").html(pagination);
			$("#previous").click(function() {
				eval(func)(brand, category, data.number - 1, pageSize);
			});
			$("#next").click(function() {
				eval(func)(brand, category, data.number + 1, pageSize);
			});
}

function init(){
    $("#close-button").click(function() {
        $("#error-message").hide();
    });
    $("#close-button").click(function() {
        $("#success-message").hide();
    });
}

$(document).ready(init);
$(document).ready(onlyNonNegativeInt);
$(document).ready(decimalNumber);
$(document).ready(noOfCharLimiter);