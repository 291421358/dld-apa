var timeout;
var quasistrs = new Map();
var te;
var quasi;

function stirSpin() {
    $.ajax({
        type: 'POST',
        url: urlhead + '/adjusted/stirSpin',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
        },
        error: function () {
            alert("error")
        }
    });
}

/**
 * 移开反应杯 读透射
 */
function removeCupReadTransmission() {
    $.ajax({
        type: 'POST',
        url: urlhead + '/adjusted/removeCupReadTransmission',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
        },
        error: function () {
            alert("error")
        }
    });
}
/**
 * 移开反应杯 读散射
 */
function removeCupReadScattering() {
    $.ajax({
        type: 'POST',
        url: urlhead + '/adjusted/removeCupReadScattering',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
        },
        error: function () {
            alert("error")
        }
    });
}
/**
 * 移回反应杯
 */
function moveBackCup() {
    $.ajax({
        type: 'POST',
        url: urlhead + '/adjusted/moveBackCup',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
        },
        error: function () {
            alert("error")
        }
    });
}

//
$(document).ready(function () {
    //调校
    $("#adjust input[type=button]").mousedown(function () {
        command = this.name;
        console.log("down");
        if ("null" === command || command === "") {
            console.log("null");
        } else {
            sendfun();
        }
    }).mouseup(function () {
        console.log("up");
        clearTimeout(timeout);
    }).mouseleave(function () {
        console.log("leave");
        clearTimeout(timeout);
    });
    //光准
    $("#lightQuasi input[type=button]").click(function () {
        command = this.name;
        send(-1, -1, 4);
    });
    //读透射ad
    $("#ad input[type=button]").click(function () {
        $("#lightQuasi input[type=button]").attr("type","hidden");
        if (this.name === "STOP") {
            $("#lightQuasi input[type=hidden]").attr("type","button");
            $("#ads").attr("type","button");
            $("#adt").attr("type","button");
            clearTimeout(timeout);
            moveBackCup();
            return;
        }
        te = this.name;
        te = command;
        command = this.name;
        console.log(this.name);
        if (this.name === "READ_TEMPERATURE") {
            command = this.name;
            send(-1, -1, 3);
            command = te;
            return;
        }
        if (this.id == "ads") {

            removeCupReadScattering();
        }
        if (this.id == "adt") {
            removeCupReadTransmission();
        }
        $("#adt").attr("type","hidden");
        $("#ads").attr("type","hidden");
        runEvery1Sec();
    });
    $("#plungerPump1").on('click',function () {
        plungerPump1();
    })
    $("#plungerPump2").on('click',function () {
        plungerPump2();
    })
    $("#readAddress").on('click',function () {
        readAddress();
    });
    $("#writeAddress").on('click',function () {
        writeAddress();
    });
    /**
     * 初始化
     */
    $("#init").on('click', function () {
        $.ajax({
            type: 'GET',
            url: urlhead + '/adjusted/init',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
    $("#cleanEight").on('click', function () {
        $.ajax({
            type: 'GET',
            url: urlhead + '/adjusted/cleanEight',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
    //注射器吸收
    $("#syringeAbsorb").on('click', function () {
        console.log(($("#volume")[0].value).toString(16));
        $.ajax({
            type: 'GET',
            url: urlhead + '/adjusted/syringeAbsorb?volume=' + parseInt($("#volume")[0].value).toString(16),
            date: {
                volume: $("#volume")[0].value,
            },
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
    //注射器吹气
    $("#syringeBlow").on('click', function () {
        $.ajax({
            type: 'GET',
            url: urlhead + '/adjusted/syringeBlow?volume=' + parseInt($("#volume")[0].value).toString(16),
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
    //进水
    $("#inlet").on('click', function () {
        $.ajax({
            type: 'POST',
            url: urlhead + '/adjusted/inlet',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
    //进水停止
    $("#inletSto").on('click', function () {
        $.ajax({
            type: 'POST',
            url: urlhead + '/adjusted/inletSto',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
    //出水
    $("#effluent").on('click', function () {
        $.ajax({
            type: 'POST',
            url: urlhead + '/adjusted/effluent',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
//出水停止
    $("#effluentSto").on('click', function () {
        $.ajax({
            type: 'POST',
            url: urlhead + '/adjusted/effluentSto',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    });
});

function plungerPump1(){
    $.ajax({
        type: 'GET',
        url: urlhead + '/adjusted/plungerPump1?value='+$("#plungerPump1Value")[0].value,
        date: {
        },
        async: true,
        jsonp: 'jsoncallback',
        success: function (event) {
        },
        error: function () {
            alert("error");
        }
    });
}

function plungerPump2(){
    $.ajax({
        type: 'GET',
        url: urlhead + '/adjusted/plungerPump2?value='+$("#plungerPump2Value")[0].value,
        date: {
        },
        async: true,
        jsonp: 'jsoncallback',
        success: function (event) {
        },
        error: function () {
            alert("error");
        }
    });
}

//读取地址码
function readAddress() {
    $.ajax({
        type: 'GET',
        url: urlhead + '/adjusted/readAddress?address='+$("#address")[0].value,
        date: {
            address: $("#address")[0].value,
        },
        async: true,
        jsonp: 'jsoncallback',
        success: function (event) {
            // var value = parseInt(event).toString(10);
            $("#value")[0].value = event;
        },
        error: function () {
            alert("error");
        }
    });
}
//写入地址码
function writeAddress() {
    $.ajax({
        type: 'POST',
        url: urlhead + '/adjusted/writeAddress?address=' + $("#address")[0].value +'&value='+ $("#value")[0].value,
        date: {
            address: $("#address")[0].value,
            value: $("#value")[0].value,
        },
        async: true,
        jsonp: 'jsoncallback',
        success: function (event) {
        },
        error: function () {
            alert("error");
        }
    });
}
//循环 调校
function sendfun() {
    timeout = setTimeout(sendfun, 250);
    var $num = $("#num")[0].value;
    var $num2 = $("#num2")[0].value;
    $num = parseInt($num).toString(16);
    $num2 = parseInt($num2).toString(16);

    if ($num.length < 2) {
        $num = "0" + $num;
    }
    if ($num2.length < 2) {
        $num2 = "0" + $num2;
    }
    send($num, $num2, 2);
}

//循环读ad
function runEvery1Sec() {
    clearTimeout(timeout);
    timeout = setTimeout(runEvery1Sec, 1200);
    send(-1, -1, 3);
}



//处理光准
function dealLightQuasi(parse) {
    quasi = parse.msg;
    var canvas = document.getElementById('myCanvas');
    document.getElementById('myCanvas');
    if (canvas.getContext) {
        quasistrs.clear();
        var ctx = canvas.getContext('2d');
        ctx.fillStyle = "white";
        ctx.beginPath();
        ctx.fillRect(0, 0, 2000, 2000);
        ctx.closePath();
    }
    console.log(quasi.length);
    draw();
    drawQuasi();
    drawFont();
    if (parse.id === '701') {
        console.log("端口连接失败，请检查接口");
    }
    console.log("readLightQuasi");
}

//处理ad
function dealReadAD(parse) {
    for (var key in parse) {

        if (key !== "id") {
            console.log(key+parse[key]);
            document.getElementsByName(key)[0].value = parse[key];
        }
    }
}

//接收到消息的回调方法
websocket.onmessage = function (event) {
    console.log("这是后台推送的消息：" +event.data);
    if (event.data != null && event.data !== "连接成功定时发送消息类" && event.data != "serialPort:null"){
        var parse = JSON.parse(event.data);
        if (parse.id === "readLightQuasi") {
            dealLightQuasi(parse);
        }
        if (parse.id === "readAD") {
            dealReadAD(parse);
        }
    }
};

//画出曲线
function draw() {
    var canvas = document.getElementById('myCanvas');
    document.getElementById('myCanvas');
    if (canvas.getContext) {
        var ctx = canvas.getContext('2d');
        ctx.lineWidth = 1;
        ctx.strokeStyle = "black";
        ctx.beginPath();
        ctx.moveTo(0, 200 -quasi[1]);
        // quasi.splice(0, 50);
        for (var i = 0; i < quasi.length; i++) {
            if (quasi[i] === "254") {
                quasistrs.set(i, quasi[i]);
                continue;
            }
            if (quasi[i] === "0" && i % 2 === 0) {
                quasi.splice(i, 1);
                quasistrs.set(i, quasi[i]);
                continue;
            }
            ctx.lineTo(i, (200 - quasi[i]) );
        }
        ctx.stroke();
    }
}

function drawQuasi() {
    var canvas = document.getElementById('myCanvas');
    document.getElementById('myCanvas');
    if (canvas.getContext) {
        var ctx = canvas.getContext('2d');
        ctx.lineWidth = 0.5;
        ctx.strokeStyle = "red";
        ctx.beginPath();
        quasistrs.forEach(function (value, key) {
            ctx.moveTo(key, 0);
            ctx.lineTo(key, value);
        })
    }
    ctx.stroke();
}

function drawFont() {
    var canvas = document.getElementById('myCanvas');
    if (canvas.getContext) {
        var ctx = canvas.getContext('2d');
        console.log(canvas.width + "");
        ctx.strokeStyle = "black";
        ctx.font = "17px";
        ctx.strokeText("65535", 0, 10);
        ctx.strokeText("32767.5", 0, 100);
        ctx.strokeText("0", 0, 200);
    }

}
