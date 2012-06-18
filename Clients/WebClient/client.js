//--------------инициализация клиента

window.addEventListener("load", init, false);

var wsUri = "ws://127.0.0.1:10509";
var output;

function init()
{   output = document.getElementById("output");
    openWebSocket();
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

var command;
var inMessage;
//принятие и оработка запроса
function onMessage(evt)
{
    var mess = evt.data;
    var arr = mess.split(':');

    command= arr[0].toLowerCase();
    if (inMessage!=null)
        inMessage=arr[1].trim();

    switch(command)  {
        case "players": initPersList(inMessage); break;
        case "offer": //Offer message
            break;
        case "start": newGame(inMessage); break;
        case "status":  gameOver(inMessage); break;
        case "rejected": writeToScreen("Choose another player"); break;
        case "put":  put(inMessage);break;
        default: alert("Unknown command was sent. It was "+command); break;
    }
    writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data+'</span>');
}

function onError(evt)
{
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend(message){
    writeToScreen(message);
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
    alert(name);
    doSend("CONN:"+name);
    doSend("players");
    location.replace('ChoosePlayer.html');
}
//----------------выбор соперника

function setPlayer(){
    var plaeyr = GetSelectedItem(); //добаить в выбранному элементу ID player
    doSend("OPPONENT:"+plaeyr);
    location.replace('WebView.html');
}

function initPersList(inMessage){
    var array = inMessage.split(" ");
    if(array.length==0)  alert("Вы единственный игрок. Ждите.")
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
function newGame(inMessage){
    if (inMessage == "cross") {
        turn = true;
        mark = "X";
        mark2="O";
    } else {
        turn = false;
        mark = "O";
        mark2="X";
    }
   }
function put(inMessage)  {
     var cell=document.getElementById(inMessage);

    cell.innerHTML=mark2;
}

$(document).ready(function(){
    $('table#game td').click(function(e){
        var t = e.target || e.srcElement;

       //получаем подтверждение хода
        if(turn){
            if(cell.innerHTML=="O"||cell.innerHTML=="X") {alert ("You can't put mark here!")}
           else{
            doSend("PLAYER:"+ t.id);
            t.innerHTML=mark; turn=false;}
        }
        else alert("Wait for your turn");
    });
});

function gameOver(inMessage){
        if (won){alert("You won!");}
        else{alert("You've lost!");}
         //вызывается предложение сиграть сново
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





