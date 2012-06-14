//--------------инициализация клиента

window.addEventListener("load", init, false);
var wsUri = "ws://127.0.0.1:8888";
var output;

function init()
{   output = document.getElementById("output");
    openWebSocket();
    initUpdate();
}

function openWebSocket()
{
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
}

function onOpen(evt){
    writeToScreen("CONNECTED");
}

function onClose(evt)
{
    writeToScreen("DISCONNECTED");
}



//принятие и оработка запроса
function onMessage(evt)
{
    var mess = evt.data;

    if (mess == "clear"){
        doClear();
    }
    else if (mess.substring(0, 3) == "add"){

        var get=eval(mess.substring(4, mess.length));

        initUpdate(get);
    }
    writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data+'</span>');
}

function onError(evt)
{
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend(message){
    writeToScreen("SENT: " + message);
    websocket.send(message);
}

function writeToScreen(message)
{
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}
//---------------------установка Имени пользоватля

function getName(){
  var name = document.getElementById("inp").value;
    doSend("CONN:"+name);
    location.replace('ChoosePlayer.html');
}
//----------------выбор соперника

function setPlayer(){
    var plaeyr = GetSelectedItem(); //добаить в выбранному элементу ID player
    doSend("OPPONENT:"+plaeyr);
    location.replace('WebView.html');
}

function initUpdate(get){
    for(var i=0;i<get.length;i++){
        fill(-1,get[i].ID,get[i].First_Name,get[i].Last_Name,get[i].Age,get[i].Phone);
    }
}
var array = [ "sss","sdf","grf" ]; //получаем этот список при коннекте к серверу.

function initPersList(){
    var inputRadio="";
    for ( keyVar in array ) {

        inputRadio += "<input name='rad' type='radio' id=''"+ array[keyVar]+"'>"+array[keyVar]+"<br>"
      }
    document.getElementById("namelist").innerHTML=inputRadio
}

function GetSelectedItem() {
    var chosen = ""
     var len = document.list.rad.length
    //alert("length"+len);

    for (i = 0; i <len; i++) {
        if (document.list.rad[i].checked) {
            chosen = array[i];
        }
    }
return chosen;
}

//--------------сессия игры
//получаем X/O и право хода

var mark="X"
var turn=true

$(document).ready(function(){
    $('table#game td').click(function(e){
        var t = e.target || e.srcElement;
    doSend("PLAYER"+mark+":"+ t.id);
       //получаем подтверждение хода
        if(turn){t.innerHTML=mark;}


    });
});

function gameOver(gameOver,won){
    //получаем исход игры
    if(gameOver){
        if (won){alert("You won!");}//тут полученое условие на выграш
        else{alert("You've lost!");}
    }


}
//окно предложить играть сново
$(function(){
    $('#dialog1').dialog({
        autoOpen: false,
//position: ["center","center"],
        width: 300,
//height: 70,
        title: "{Хотите сиграть с этим игроком сново?",
        modal: true,
        buttons: {
            "Да": function() {
                alert("Играем сново");
                //переход в начало игры

            },
            "Нет": function(){alert("Выбор соперника");
                //переход к выбору соперника
            }
        }
    });
})

//это в последствии переделается под передачу данных поля
function tableRowsToJSON() {
    var outstr='[';
    $('table#editable tr.rowdata').each(function() {
        outstr+='{';
        outstr+="ID:'"+$('td:nth-child(1)',this).text()+"',";
        outstr+="First_Name:'"+$('td:nth-child(2)',this).text()+"',";
        outstr+="Last_Name:'"+$('td:nth-child(3)',this).text()+"',";
        outstr+="Age:'"+$('td:nth-child(4)',this).text()+"',";
        outstr+="Phone:'"+$('td:nth-child(5)',this).text()+"'";
        outstr+='},';
    });
    outstr=outstr+"]";
    alert(outstr);
    return outstr;
}



