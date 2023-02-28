function init(){
    $('#signupButton').attr("style", "display:block;");
    $('#loginButton').attr("style", "display:none;");
    console.log("Role->>>>")
    console.log($info.getRole());
}
$(document).ready(init);