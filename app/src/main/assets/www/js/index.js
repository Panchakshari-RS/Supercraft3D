var initDone = false;
var cognitoPoolId = "us-east-1:75f62414-b651-471f-9211-69165861a261";
var bucketName = "trovamedia";

$(document).ready(function() {
    if(!initDone) {
        console.log( "document loaded" );
        initDone = true;
    }
});

function signup() {
    console.log('signup: ' + "Called ............");
    executeChat("signup");
    return false;
}
function details() {
    console.log('details: ' + "Called ............");
    executeChat("details");
    return false;
}
function chat() {
    console.log('chat: ' + "Called ............");
    executeChat("chat");
    return false;
}

function executeChat(what) {
    console.log('executeChat: ' + "Called ............");
    JSInterface.launchTwitterDigitsActivity(what);
    //JSInterface.initTrovaChat();
}
