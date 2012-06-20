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
    if (arr.length>1)
        inMessage=arr[1].trim();

    switch(command)  {
        case "players": initPersList(inMessage); break;
        case "offer": sendOffer(); break;
        case "start": newGame(inMessage); break;
        case "status":  gameOver(inMessage); break;
        case "rejected": alert("Choose another player"); break;
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
    doSend("CONN:"+name);
    doSend("players");
    deleteRows(3) ;
    addPlayerList();

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
//    var title = document.createElement("p");
//    title.innerHTML = "Choose player";
//    table.appendChild(title);

    array =inMessage.split(" ");

    if(array[0]=="") {
        alert("Вы единственный игрок. Ждите."); }
     else{
    var inputRadio="";
    for ( keyVar in array ) {

        inputRadio += "<input name='rad' type='radio' id=''"+ array[keyVar]+"'>"+array[keyVar]+"<br>"
      }
    document.getElementById('namelist').innerHTML=inputRadio


    var button = document.createElement("p");
    button.innerHTML = "<input name='Submit' type='Button' value='Submit' onclick='setPlayer()'>";
    table.appendChild(button); }
}

function GetSelectedItem() {
    var chosen = ""  ;
     for (i = 0; i <array.length; i++) {
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
  var table = document.getElementById('editable');
  table.innerHTML="<tr>  <td style='border: 4px solid green;' width='70' height='70' id='00'  align='center' onclick=' alert(\"Yf\");'> </td> " +
      "  <td  style='border: 4px solid green;' width='70' height='70' id='01'  align='center'> </td>" +
      " <td style='border: 4px solid green;' width='70' height='70' id='02'  align='center'> </td>  </tr>" +


        " <tr> <td style='border: 4px solid green;' width='70' height='70' id='10'  align='center'> </td> " +
        "  <td style='border: 4px solid green;' width='70' height='70' id='11'  align='center'> </td> " +
            "  <td style='border: 4px solid green;' width='70' height='70' id='12'  align='center'> </td> </tr>" +


        " <tr>  <td style='border: 4px solid green;' width='70' height='70' id='20'  align='center'> </td> " +
        "  <td style='border: 4px solid green;' width='70' height='70' id='21'  align='center'> </td> " +
            " <td style='border: 4px solid green;' width='70' height='70' id='22'  align='center'> </td> </tr>";

table.style.border="4px solid white border-collapse: collapse;" ;
    table.align="center";
}
function go(){
    alert("Yf");
}

function put(inMessage)  {
     var cell=document.getElementById(inMessage);

    cell.innerHTML=mark2;
}

$(document).ready(function(){
    $('table#editable td').click(function(e){
        var t = e.target || e.srcElement;
        alert(t.id);
       //получаем подтверждение хода
        if(turn){
            if(cell.innerHTML=="O"||cell.innerHTML=="X") {alert ("You can't put mark here!")}
           else{
                alert(t.id);
            doSend("PUT:"+ t.id);
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
//<form name=myform>
//    <input type=button value="Try it now"
//    onClick="if(confirm('Do you want to play with..?'))
//startGame();">
//    </form>


function sendOffer(){

if(confirm('Do you want to play with..?'))  doSend("OFFERRESPONSE: YES");
else  doSend("OFFERRESPONSE: NO"); }


