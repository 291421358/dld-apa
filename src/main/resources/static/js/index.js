$(document).ready(function () {
    $("#menu").on("click",'li',function (data) {
        var i = data.currentTarget.value;
        jumpPage(i);
        $("#menu li").each(function () {
            $(this).removeAttr("style");
        })
        $(this).css("background","#3766ed").css("height","30px");
    });

    $("#quitBtn").on("click",function () {
        window.location.href="about:blank";
        closeJava()
        window.close();

    });
    $("#closeBtn").on("click",function () {
        $("#quit").fadeOut("fast");
        $("#mask").css({display: 'none'});
        $(".opacity_bg").hide(); // 隐藏背景层
        $("#dialog").empty().hide(); // 清除弹出页
    });
    $("#in").on("click",function () {
        verification();
    });
});
function closeJava() {
    $.ajax({
        type : "GET",
        url : urlhead + "/index/close",
        async : true,
        jsonp : "jsoncallback",
        success : function () {

        },
        error : function () {

        }
    })
}
function time() {//
    var date = new Date();
    $("#time").html("<li style='font-family: 隶书;float: left'>&nbsp&nbsp" + date.getFullYear() + "年"
        + (new Date().getMonth() + 1) + "月" + new Date().getDate() + "日&nbsp" + date.getHours()
        + ":" + date.getMinutes() + ":" + date.getSeconds() + "</li>");
    setTimeout(function () {
        time();
    }, 500);
}

var urls = ["/pages/patients/scaling.html","/pages/patients/input&result.html","/pages/param/projectparam.html","/pages/search/sampleResult.html"
,"/pages/equipment/adjust&AD&parts.html","/pages/laser/laserSpectrum.html"];
function jumpPage(i) {
    var url = "";
    console.log(i);
    switch (i) {
        case 1:
            url = urls[i-1];
            break;
        case 2:
            url = urls[i-1];
            break;
        case 3:
            url = urls[i-1];
            break;
        case 4:
            url = urls[i-1];
            break;

        case 5:
            url = urls[i-1];
            break;
        case 6:
            url = urls[i-1];
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

function  verification() {
    console.log($("#username")[0].value);
    $.ajax({
        type:"GET",
        url : urlhead + "/loginer/verification?un="+$("#username")[0].value+"&pa="+$("#password")[0].value,
        async:true,
        date:{
            un : $("#username")[0].value,
            pa : $("#password")[0].value,
        },
        jsonp: 'jsoncallback',
        success:function (e) {
            var a = "<li class='mli' value='2'>检测界面</li>"
            var b = "<li class='mli' value='3'>参数设置</li>"
            var c = "<li class='mli' value='4'>查询打印</li>"
            var d = "<li class='mli' value='5'>仪器维护</li>"
            var f = "<lr  onclick='' id='tem'> <img style='padding-top: 7px;height: 33px' src='css/images/temp.png'> </lr>"
            if (e == 1){
                $("#menu").html(a+b+c+d+f);
                $("#show").hide();
            }
            else if (e == 2){
                $("#menu").html(a+b+c+f);
                $("#show").hide();
            }
            else
                alert("密码错误")
        },
        error: function () {

        }
    })
}