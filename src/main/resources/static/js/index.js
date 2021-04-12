$(document).ready(function () {
    $("#menu li").click(function (data) {
        var i = data.currentTarget.value;
        jumpPage(i);
        $("#menu li").each(function () {
            $(this).removeAttr("style");
        })
        $(this).css("background","#3766ed").css("height","30px");
    });

    $("#quitBtn").on("click",function () {
        window.location.href="about:blank";
        window.close();

    });
    $("#closeBtn").on("click",function () {
        $("#quit").fadeOut("fast");
        $("#mask").css({display: 'none'});
        $(".opacity_bg").hide(); // 隐藏背景层
        $("#dialog").empty().hide(); // 清除弹出页
    });
    $("#in").on("click",function () {
       $("#show").hide()
    });
});

function time() {//
    var date = new Date();
    $("#time").html("<li style='font-family: 隶书;float: left'>&nbsp&nbsp" + date.getFullYear() + "年"
        + (new Date().getMonth() + 1) + "月" + new Date().getDate() + "日&nbsp" + date.getHours()
        + ":" + date.getMinutes() + ":" + date.getSeconds() + "</li>");
    setTimeout(function () {
        time();
    }, 500);
}

var url = "";

function jumpPage(i) {
    console.log(i);
    switch (i) {
        case 1:
            url = "/pages/patients/scaling.html";
            break;
        case 2:
            url = "/pages/patients/input&result.html";
            break;
        case 3:
            url = "/pages/param/projectparam.html";
            break;
        case 4:
            url = "/pages/search/sampleResult.html";
            break;

        case 5:
            url = "/pages/equipment/adjust&AD&parts.html";
            break;
        case 6:
            url = "/pages/laser/laserSpectrum.html";
            break;
        case 7:
            suspend();
            return;

        default:
            return
    }
    console.log(url+"?ranparam="+Math.random());
    $("#content").html("<iframe scrolling='no' src=" + url + " height=720 width=1270 frameborder=null style='z-index: 1023;'></iframe>")
}
$("#deleteProjectBox").fadeOut("fast");
$("#mask").css({display: 'none'});


var urlhead = getPort();
function suspend() {
    $("#quit").fadeIn("slow");
    $(".opacity_bg").show(); // 显示背景层,覆盖当前页面内容
    $("#dialog").show();
}


function fullScreen(el) {
    var rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen,
        wscript;
    rfs.call(el);
    wscript = new ActiveXObject("WScript.Shell");
    if (wscript) {
        wscript.SendKeys("{F11}");
    }
}


function getTem() {
    getEquipmentState();
    setTimeout(getTem,10000);
}
function init() {
    $.ajax({
        type: 'GET',
        url: urlhead + '/adjusted/connect',
        async: true,
        jsonp: 'jsoncallback',
        success: function (event) {
        },
        error: function () {
            alert("error");
        }
    });
}
/**
 * 获取仪器状态数据
 */
function getEquipmentState(){
    $.ajax({
        type: 'get',
        url: urlhead + '/equipmentState/selectOne?id=1',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            $("#tem").html("<img style='float: left;margin-left: 0px;margin-top: 2px;height:33px;'  src=\"css/images/temp.png\"> <div style='float: left;margin-top: 10px;margin-left: 10px'>"+data.reactTemp+"°C</div>");
        },
        error: function () {
            alert("请联系管理员");
        }
    })
}


this.start = new Date().getTime()
let code = ''
let lastTime, nextTime
let lastCode, nextCode
let that = this
document.onkeypress = function (e) {
     console.log("111")
}


function iclose(){

}


$("html,body").css("overflow","hidden").css("height","100%");
document.body.addEventListener('touchmove', self.welcomeShowedListener, false);

function showGif() {
    $("#show").show();
}