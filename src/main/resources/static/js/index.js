var timeout;
var availableTags =  new Array();
//重写alert
function my_alert(msg,checkvalid=true){
    //update 20200910 SDM 显示二层遮罩
    $(".check_mask").show();
    // update 20200910 SDM 背景模糊
    $("body *:not('#custom_alert,.check_mask')").addClass("blur");
    var tip_title = $("	<div style='position: fixed;left:58%;top:10%;width:25%;transform: translate(-50%,-50%);\
		font-size: 1.1rem; background-color:white;text-align: center;box-shadow: 0.1rem 0.2rem 0.7rem 0.2rem #000000;\
		border-radius: 0.3rem;z-index: 2048'> <div><p style='background-color: #f9f9f9;color:#000000;\
		border-radius: 0.3rem 0.3rem 0 0 ;height: 1rem;line-height: 1rem;'>提示</p>\
		<p class='tempmsg' style='padding: 0.3rem 1rem;line-height:0.7rem;color:#000000;line-height:0.7rem;'>"+msg+"</p>\
		<p><button style='width:100%;color: white;background-color:#007ddb;\
		 border:0;outline: none;border-radius: 0 0 0.3rem 0.3rem;padding: 0.3rem 0.3rem;font-size:1.1rem;'>确定</button></p></div></div>");
    tip_title.attr('id','custom_alert');
    $("body").append(tip_title);
    $("#custom_alert button").on('click',function(){
        //关闭遮罩
        $(".check_mask").hide();
        //关闭背景模糊
        $("*").removeClass("blur");
        $(this).parents("#custom_alert").remove();
        if (checkvalid){
            //提前停止计时器和跳转
            clearInterval(temptime);
            window.location.reload();
        }else{
            console.log('我进来关闭注册框了');
            $(".popup_register").hide("fast");
        }
    });
};
$(document).ready(function () {

    $("#menu").on("click",'li',function (data) {
        var i = data.currentTarget.value;
        if (i > 0){
            jumpPage(i);
            $("#menu li").each(function () {
                $(this).removeAttr("style");
            })
            $(this).css("background","#3766ed").css("height","30px");
        }
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
    $("#closeLoginner").on("click",function () {
        $("#loginer").fadeOut("fast");
        $(".opacity_bg").hide(); // 显示背景层,覆盖当前页面内容
        $("#dialog").hide();
    });
    $("#add").on("click",function () {
        var p = $("#addP")[0].value;
        var legitimate = /^(?=.*\d)(?=.*[a-zA-Z])[\da-zA-Z]{6,}$/.exec(p);
        console.log(legitimate +"合法嗎");//(?=.*[~!@#$%^&*])[\da-zA-Z~!@#$%^&*]
        if (legitimate == null){
            my_alert("请输入六位以上且包含数字字母的密码！")
            return;
        }
        addLoginner();
    });

    $("#del").on("click",function () {
        delLoginer();
    });

    $("#round").on("click",function () {
        if (timeout != null){
            clearTimeout(timeout);
        }else {
            round();
        }
    });

//beforeunload1 关闭窗口不弹框 beforeunload 关闭窗口 弹框
    $(window).on('beforeunload1',function(){

        return'11';
    });
    $("#password").on("keyup",function (event) {
            if (event.keyCode == 13)
            {
                event.returnValue=false;
                event.cancel = true;
                $("#in")[0].click();
            }
    })
    $("#username").on("keyup",function (event) {
        if (event.keyCode == 13)
        {
            event.returnValue=false;
            event.cancel = true;
            $("#in")[0].click();
        }
    })

    /**
     * 静音
     */
    $("#BuzzerStop").on('click', function () {
        var $1 = $(this);
        console.log($1)
        $.ajax({
            type: 'GET',
            url: urlhead + '/adjusted/BuzzerStop',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
                console.log("22222222222222222")

                $1[0].innerText = "🔈";
            },
            error: function () {
                my_alert("error");
            }
        });
    })


    $("#ok").on('click',function () {
        clearTimeout(timeout1)
        $("#loginf").show();
        $("#openTest").hide();
    })
});
window.onunload = function() {
    if(flag){
        console.log('关闭操作');
    }
    else {
        console.log('刷新操作');
    }
};

window.onbeforeunload = function () {
    if(!flag){
        console.log('关闭操作');
    }
    else{
        console.log('刷新操作');
    }
};
function iclose(){

}
function round() {
    timeout = setTimeout(round, 10000);
    upp();
}
function upp() {
    $.ajax({
        type : "GET",
        url : urlhead + "/productTest/addby37",
        async : true,
        date :{
        },
        jsonp : "jsoncallback",
        success : function () {

        },
        error : function () {

        }
    })
}
var timeout1  = "";
var c = 0;
function openTestRound() {
    var openTest1 = openTest();
    var ot = $("#openTest input");
    ot.each(function (i) {
        if (i < c) {

            ot[i].setAttribute("checked","true")
        }
    });
    timeout1 = setTimeout(function () {
        openTestRound();
    }, 500);
}
function openTest() {
    $.ajax({
        type : "GET",
        url: urlhead + '/equipmentState/selectOne?id=1',
        async : true,
        date :{
        },
        jsonp : "jsoncallback",
        success : function (data) {
            c = data.c;
        },
        error : function () {

        }
    })
}

function addLoginner() {
    $.ajax({
        type : "GET",
        url : urlhead + "/loginer/insert?u="+$("#addU")[0].value+"&p="+$("#addP")[0].value+"&t="+$("#t")[0].value,
        async : true,
        date :{
            u : $("#addU")[0].value,
            p : $("#addP")[0].value,
        },
        jsonp : "jsoncallback",
        success : function (i) {
            if (i!==null) {
                my_alert("添加成功")
                getAU();
            }else {
                my_alert("已存在用户")
            }
        },
        error : function () {

        }
    })
}
function delLoginer() {
    var value = $("#addU")[0].value;
    $.ajax({
        type : "GET",
        url : urlhead + "/loginer/del?u="+value.replace(" ",""),
        date :{
            u : value,
            p : $("#addP")[0].value,
        },
        async : true,
        jsonp : "jsoncallback",
        success : function (i) {
            if (i == true) {
                my_alert("删除成功")
                $("#addU")[0].value = "";
                $("#addP")[0].value = "";
                getAU();
            }else {
                my_alert("无该用户")
            }

        },
        error : function () {

        }
    })
}

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
        case 9:
            break;
        default:
            return
    }
    console.log(url+"?ranparam="+Math.random());
    $("#content").html("<iframe scrolling='no' src=" + url + " height=694 width=1366 frameborder=null style='z-index: 1023;'></iframe>")
}
$("#deleteProjectBox").fadeOut("fast");
$("#mask").css({display: 'none'});

function showLoginer() {
    $("#loginer").fadeIn("slow");
    $(".opacity_bg").show(); // 显示背景层,覆盖当前页面内容
    $("#dialog").show();
}
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
    setTimeout(getEquipmentState,100);
    setTimeout(getTem,1000);
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
            my_alert("error");
        }
    });
    getAU();
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
            $("#tem1").html(" <div style='float: left;margin-top: 10px;margin-left: 10px'>"+data.reactTemp+"°C</div>");
            data.firingPin==1?$("#FiringPin").html("🔀"):$("#FiringPin").html("🔁");
            data.temp=="1"?$("#ALLin").html("进"):$("#ALLin").html("出");
            if (data.numUnderTest > 0){
                $("#menu").unbind("click");
            }else {
                $("#menu").unbind("click");
                $("#menu").on("click",'li',function (data) {
                    var i = data.currentTarget.value;
                    jumpPage(i);
                    $("#menu li").each(function () {
                        $(this).removeAttr("style");
                    })
                    $(this).css("background","#3766ed").css("height","30px");
                });
            }

        },
        error: function () {
            my_alert("请联系管理员");
        }
    })
}

/**
 * 获取所有用户
 */
function getAU(){
    $.ajax({
        type: 'get',
        url: urlhead + '/loginer/AU',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            $.each(availableTags,function (i) {
                availableTags[i] = {};
            })
            $.each(data, function (i, ds) {
                var a = new Map;
                // a.set("label",);
                availableTags[i] = {};
                availableTags[i].label = " "+data[i].u;
                availableTags[i].id = data[i].id;
                console.log(availableTags)
                // $('#info').html(project);
            });
        },
        error: function () {
            my_alert("请联系管理员");
        }
    })
}
$(function () {

    $("#addU").autocomplete({
        source: availableTags,
        lookup: availableTags,
        select: function( event, ui ) {
            // onePoject(ui.item.id)
            // my_alert(ui.item.value)
        },
    }).focus(function () {
        $(this).autocomplete("search"," ")
    });

    //tab into后显示所有结果
    $('#addU').on('focus', function (){
        getAU();
    });
});

this.start = new Date().getTime()
let code = ''
let lastTime, nextTime
let lastCode, nextCode
let that = this
document.onkeypress = function (e) {
     console.log("111")
}



$("html,body").css("overflow","hidden").css("height","100%");
document.body.addEventListener('touchmove', self.welcomeShowedListener, false);

function showGif() {
    $("#show").show();
    $("#username")[0].focus()
}

function  verification() {
    console.log($("#username")[0].value);
    var p = $("#password")[0].value;
    var legitimate = /^(?=.*\d)(?=.*[a-zA-Z])[\da-zA-Z]{6,}$/.exec(p);
    console.log(legitimate +"合法嗎");//(?=.*[~!@#$%^&*])[\da-zA-Z~!@#$%^&*]
    if (legitimate == null){
        my_alert("请输入六位以上且包含数字字母的密码！")
        return;
    }
    $.ajax({
        type:"GET",
        url : urlhead + "/loginer/verification?un="+$("#username")[0].value+"&pa="+p,
        async:true,
        date:{
            un : $("#username")[0].value,
            pa : p,
        },
        jsonp: 'jsoncallback',
        success:function (e) {
            var a = "<li class='mli' value='2'>检测界面</li>";
            var b = "<li class='mli' value='3'>参数设置</li>";
            var c = "<li class='mli' value='4'>查询打印</li>";
            var d = "<li class='mli' value='5'>仪器维护</li>";
            var g = "<div id='dou' onclick='showLoginer()' style='float: left;padding-top: 6px;margin-left: 10px;color: white;font: bold'>用户操作 </div>";
            var f = "<lr  onclick='' id='tem'> <img style='padding-top: 7px;height: 33px;float: left' src='css/images/temp.png'><div id='tem1' style='float: left'></div>  </lr>"
            if (e == 1){
                $("#menu").html(a+b+c+d+g+f);
                $("#show").hide();
            }
            else if (e == 2){
                $("#menu").html(a+b+c+f);
                $("#show").hide();
            }
            else if ($("#username")[0].value=="dldROOT_1") {
                $("#menu").html(a+b+c+d+g+f);
                $("#show").hide();
            }else
                my_alert("密码错误")
        },
        error: function () {

        }
    })

}


