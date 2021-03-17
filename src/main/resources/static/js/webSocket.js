var command = "";
var websocket = null;
var host = document.location.host;
// 获得当前登录人员的userName
// console.log(huausername);
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket('ws://' + host + '/webSocket/user1');
} else {
    console.log('当前浏览器 Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    console.log("WebSocket连接发生错误");
    setMessageInnerHTML("WebSocket连接发生错误");
};

//连接成功建立的回调方法
websocket.onopen = function () {
    console.log("Websocket连接成功:WebSocket 21 Line");
    setMessageInnerHTML("Websocket连接成功");
};

//接收到消息的回调方法
websocket.onmessage = function (event) {
    // console.log("接收到消息的回调方法");
    console.log("这是后台推送的消息：" + event.data);
    // 　　　　 websocket.close();
    // 　　　　console.log("webSocket已关闭！")
    if (event.data.id === '701') {
        console.log("端口连接失败，请检查接口");
        return;
    }
    if (event.data != null && event.data !== "连接成功定时发送消息类" && event.data != "serialPort:null"){
        var parse = JSON.parse(event.data);
        if (parse.id === "qc") {
            alert("质控结束");
            set_qc(parse);
        }
    }
};

//连接关闭的回调方法
websocket.onclose = function () {
    setMessageInnerHTML("WebSocket连接关闭");
};

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
};

//关闭WebSocket连接
function closeWebSocket() {
    websocket.close();
}


function send(number,number2,code) {
    var msg = {
        number:number,
        number2:number2,
        message: command,
        code: code,
        To: "user1"
    };
    try {
        console.log(JSON.stringify(msg));
        websocket.send(JSON.stringify(msg));
    } catch (e) {
        return;
    }

}