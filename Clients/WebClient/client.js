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
 //   writeToScreen("CONNECTED");
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
    if (arr.length>1)
        inMessage=arr[1].trim();

    switch(command)  {
        case "players": initPersList(inMessage); break;
        case "offer": sendOffer(); break;
        case "start": newGame(inMessage); break;
        case "win": case "lose": case "draw": gameOver(command); break;
        case "rejected": alert("Choose another player"); break;
        case "put":  put(inMessage); break;
        default: alert("Unknown command was sent. It was "+command); break;
    }
//    writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data+'</span>');
}

function onError(evt)
{
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend(message){
  //  writeToScreen(message);
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
    doSend("players");
    deleteRows(3) ;
}
//----------------выбор соперника

function setPlayer(){
    var player = GetSelectedItem();
    doSend("OPPONENT:"+player);
  }

function playerChoise(){
    deleteRows(3) ;
}

function deleteRows(num) {
    for(var i=0;i<num;i++){
    var table = document.getElementById('editable').deleteRow(-1);
    }
   }

var array;
 function initPersList(inMessage){
    var table = document.getElementById('editable');
    var head = document.getElementById('head');
    head.title = "Choose player";

    array =inMessage.split(" ");

    if(array[0]=="") {
        alert("Вы единственный игрок. Ждите."); }
     else{
    var inputRadio="<p>Choose player</p>";
    for ( keyVar in array ) {

        inputRadio += "<input name='rad' type='radio' id=''"+ array[keyVar]+"'>"+array[keyVar]+"<br>"
      }
    document.getElementById('namelist').innerHTML=inputRadio


    var button = document.createElement("p");
    button.innerHTML = "<input name='Submit' type='Button' value='Submit' onclick='setPlayer()'>";
    table.appendChild(button); }
}

function GetSelectedItem() {
    var chosen = "";
    if(array.length=="1") return array[0];

    for (var i = 0; i <array.length; i++) {
        if (document.list.rad[i].checked) {
            chosen = array[i];
        }
    }

    return chosen;
}

//--------------сессия игры
//получаем X/O и право хода
var mark="X" ;
var turn=true ;
function newGame(inMessage){

    if (inMessage.toLowerCase()=="cross") {
        turn = true;
        mark = "X";
        mark2="O";
    } else {
        turn = false;
        mark = "O";
        mark2="X";
    }
     var form=  document.getElementById('namelist');
    form.innerHTML="<p align='center'>Тic-tac-toe</p>"  ;

  var table = document.getElementById('editable');
  table.innerHTML="<tr>  <td style='border: 4px solid green;' width='70' height='70' id='00'  align='center' onclick='go(this)'> </td> " +
      "  <td  style='border: 4px solid green;' width='70' height='70' id='01'  align='center' onclick='go(this)'> </td>" +
      " <td style='border: 4px solid green;' width='70' height='70' id='02'  align='center' onclick='go(this)'> </td>  </tr>" +


        " <tr> <td style='border: 4px solid green;' width='70' height='70' id='10'  align='center' onclick='go(this)'> </td> " +
        "  <td style='border: 4px solid green;' width='70' height='70' id='11'  align='center' onclick='go(this)'> </td> " +
            "  <td style='border: 4px solid green;' width='70' height='70' id='12'  align='center' onclick='go(this)'> </td> </tr>" +


        " <tr>  <td style='border: 4px solid green;' width='70' height='70' id='20'  align='center' onclick='go(this)'> </td> " +
        "  <td style='border: 4px solid green;' width='70' height='70' id='21'  align='center' onclick='go(this)'> </td> " +
            " <td style='border: 4px solid green;' width='70' height='70' id='22'  align='center' onclick='go(this)'> </td> </tr>";

table.style.border="4px solid white border-collapse: collapse;" ;
table.align="center";
}
function go(elem){
    if(turn){
    if(elem.innerHTML=="O"||elem.innerHTML=="X") {alert ("You can't put mark here!")}
    else{
       doSend("PUT:"+ elem.id);
        elem.innerHTML=mark; turn=false;}
}
else alert("Wait for your turn");

}

function put(inMessage)  {
     var cell=document.getElementById(inMessage);
     cell.innerHTML=mark2;
    turn=true;
}

//$(document).ready(function(){
//    $('table#editable td').click(function(e){
//        var t = e.target || e.srcElement;
//        alert(t.id);
//       //получаем подтверждение хода
//        if(turn){
//            if(t.innerHTML=="O"||t.innerHTML=="X") {alert ("You can't put mark here!")}
//           else{
//                alert(t.id);
//            doSend("PUT:"+ t.id);
//            t.innerHTML=mark; turn=false;}
//        }
//        else alert("Wait for your turn");
//    });
//});

function gameOver(inMessage){
        if (inMessage=="win"){oneMoreGame("You won!");}
        else if(inMessage=="lose"){oneMoreGame("You've lost!");}
    else   oneMoreGame("Nobody has won!");
         //вызывается предложение сиграть сново
}
function sendOffer(){
    if(confirm('Do you want to play with..?'))  doSend("OFFERRESPONSE: YES");
else  doSend("OFFERRESPONSE: NO");
}

function oneMoreGame(message){
    if(confirm(message+'Do you want to play with this player again?'))  doSend("NEXTGAME: YES");
    else  doSend("NEXTGAME: NO");
}

