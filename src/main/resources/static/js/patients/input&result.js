var urlhead = getPort();
var quasi = 0;
var projectNameList = [];
var code = 0;
// var rackNo = 0;
var key = 0;
var timeout;
var scrollLength = 0;

//根据时间获得项目列表
function getProjectListByDate() {
    // console.log("获取当天所有项目");
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/getProjectListByData',
        async: true,
        data: {
            endtime: new Date().getFullYear() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getDate()
        },
        jsonp: 'jsoncallback',
        success: function (event) {

            if (quasi >= 1) {
                quasi++;
                //列表长度
                var size = $("#tab tr").length;
                $("#tab tr").slice(2, size - 30).remove();
                dealProject(event);
            } else {
                irLoad(event);
            }
        },
        error: function () {
            alert("error")
        }
    });

};


function getProjectListByDataTenToEnd() {
    // console.log("获取当天所有项目");
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/getProjectListByDataTenToEnd',
        async: true,
        data: {
            endtime: new Date().getFullYear() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getDate()
        },
        jsonp: 'jsoncallback',
        success: function (event) {

            irLoad(event);
        },
        error: function () {
            alert("error")
        }
    });

};

function dealProject(event) {
    var tr;
    var jsonArr = JSON.parse(event);
    var l = 1;
    var leng = 1;
    //创建一个查找表  函数
    var maxCode = jsonArr['maxCode'];
    let markCanBeDelete = 0;//标记是否可以删除
    var turn = 0;
    var completed = 0;
    var hangInTheAir = 0;
    for (var i = jsonArr['minCode']; i <= maxCode; i++) {
        var jsonArrElement = jsonArr[i];

        if (null == jsonArrElement) {
            continue;
        }
        tr = "<tr>" +
            "<td>" + i + "</td>" +
            "<td>" + ("null" === jsonArrElement[0].barCode ? "" : jsonArrElement[0].barCode) + "</td>";
        for (let j = 0; j < 6; j++) {
            tr += "<td";
            //循环9次
            for (let k = 0; k < jsonArrElement.length; k++) {
                turn++;
                //循环jsonArrElement
                var progress = "";
                var fac;
                //取当前jsonArrElement[k].projectParamId;
                var paramId = jsonArrElement[k].projectParamId;

                var id = $('#tab tr:nth-child(2) td:nth-child(' + (j + 3) + ')')[0].id;
                // console.log(id, paramId);
                if (id == paramId && paramId != "") {
                    //如果项目id相等
                    progress = jsonArrElement[k].progress;
                    if (progress.indexOf("%") >= 0) {
                        //计数
                        hangInTheAir++;
                        // 带有百分号为正在做的项目，为深蓝色
                        tr += " style='background:#6bb3b3;color:white' ";//007DDB
                    } else {
                        //计数
                        completed++;
                        //已经出结果的项目白色
                        var abnormal = jsonArrElement[k].abnormal;
                        // console.log(abnormal);
                        var title = "";
                        if (abnormal != null && abnormal != "null") {

                            if (abnormal == 1) {
                                tr += " style='background:rgb(254, 154, 154);' ";
                                title += "无试剂次数  ";
                            }
                            else if (abnormal == 2) {
                                tr += " style='background:#ffff01' ";
                                title += "缺少试剂次数(≤5)  ";
                            } else {
                                tr += " style='background:rgb(254, 154, 154);' ";
                                title += "其他异常  ";
                            }
                        } else {
                            tr += " style='background:white;' ";
                        }
                        if (jsonArrElement[k].absorbanceLow == 1) {
                            title += "吸光度高↑  ";
                        }
                        if (jsonArrElement[k].absorbanceHeight == 1) {
                            title += "吸光度低↓  ";
                        }
                        if (title != "") {
                            tr += "title ='" + title + "'"
                        }
                        markCanBeDelete = 1;
                        if (progress <= 0.5) {
                            progress = "≤0.5";//This inspection reports expression statements which are not assignments or calls. Such statements have no
                            // dubious semantics, are normally the result of programmer error.
                        }
                    }
                    fac = 1;
                } else {
                    fac = 0;
                }
                if (fac === 1) {
                    jsonArrElement[k].projectParamId = "";
                    break;
                }
            }
            tr += ">" + progress + "</td>";
            progress = "";
        }
        tr += "<td><select disabled>";
        for (let j = 0; j < 5; j++) {
            //1到5架号
            var a = j + 1;
            var b = jsonArrElement[0].rack;
            if (a == b)
            //相同的架号则选中
                tr += "<option selected>" + (j + 1) + "</option>";
            else
                tr += "<option> " + (j + 1) + "</option>";
        }
        tr += "</select></td><td><select disabled>";
        for (let j = 0; j < 5; j++) {
            //1到5位号
            var a = j + 1;
            var b = jsonArrElement[0].place;
            if (a == b)
            //相同的位号则选中
                tr += "<option selected>" + (j + 1) + "</option>";
            else
                tr += "<option> " + (j + 1) + "</option>";
        }
        if (markCanBeDelete === 1) {
            tr += "</select></td>" +
                "<td style='background-color: #ececec'></td>" +
                "</tr>";
            markCanBeDelete = 0;
            leng++;
        } else {
            tr += "</select></td>" +
                "<td style='color: red'>X</td>" +
                "</tr>";
        }

        // console.log(jsonArrElement);
        // console.log("paramid:" + i);
        $('#tab tr:eq(' + l + ')').after(tr);
        l++;
    }
    console.log("循环次数" + leng);
    maxCode++;
    for (var i = 9; i < leng; i++) {
        scrollLength += $(" tr")[i].clientHeight;
    }
    // if (leng > 9) {
    //     scrollLength = (leng - 9) * 30;
    // }
    console.log(hangInTheAir + "hangInTheAir" + completed + "completed");
    $("#yijiance_n").html(completed);
    $("#daijiance_n").html(hangInTheAir);
    $("#zongjiance_n").html(hangInTheAir + completed);
    for (let j = 0; j < 30; j++) {
        var jQtr = $('#tab tr:eq(' + l + ')').next();
        if (jQtr.length === 0) {
            //maxCodeh Checks that jQuery selectors are used in an efficient way. It warns about duplicated selectors which could be cached and optionally
            // about attribute and pseudo-selectors usage.
            tr = "<tr>" +
                "<td>" + (maxCode++) + "</td>" +
                "<td> </td>" +
                "<td>" +
                "<li>&nbsp</li>" +
                "</td>" +
                "<td>" +
                "<li>&nbsp</li>" +
                "</td>" +
                "<td>" +
                "<li>&nbsp</li>" +
                "</td>" +
                "<td>" +
                "<li>&nbsp</li>" +
                "</td>" +
                "<td>" +
                "<li>&nbsp</li>" +
                "</td>" +
                "<td>" +
                "<li>&nbsp</li>" +
                "</td>" +

                "<td><select >" +
                "<option>1</option>" +
                "<option>2</option>" +
                "<option>3</option>" +
                "<option>4</option>" +
                "<option>5</option>" +
                "</select></td>" +
                "<td><select >" +
                "<option>1</option>" +
                "<option>2</option>" +
                "<option>3</option>" +
                "<option>4</option>" +
                "<option>5</option>" +
                "</select></td>" +
                "<td></td>" +
                "</tr>";
            $('#tab tr:eq(' + l + ')').after(tr);
        }
        l++;
    }

    if (quasi <= 2) {
        $("#testAndShow").scrollTop(scrollLength - 90);
    }

    // console.log("这是后台推送的消息：" + event[11]);
}

function circularReading() {
    loadProjectList();
    //z这是第一次进入界面。先获取试剂位置，后续要用。
    getRegentPlace();
    getProjectListByDataTenToEnd();
    //然后进入循环 读取数据和仪器状态
    timeout = setTimeout(readDateAndState, 1000);
    setTimeout(function () {
        $("#testAndShow").scrollTop(scrollLength - 90);
    }, 1000);
}


function readDateAndState() {
    getRegentPlace();
    getProjectListByDate();
    getEquipmentState();
    timeout = setTimeout(readDateAndState, 15000);
}

//获得试剂位置
function getRegentPlace() {
    // getProjectListByDate();
    $.ajax({
        type: 'get',
        url: urlhead + '/regentPlace/getRegentPlace',
        async: true,
        data: {},
        jsonp: 'jsoncallback',
        success: function (event) {
            // console.log(event.length);
            // console.log(event[0]);
            console.log(event);
            // regentBottleSet();
            for (let i = 0; i < 6; i++) {
                var eventElement = event[i];
                if (eventElement.name == null){
                    continue;
                }
                $("#n" + (i + 1)).html(eventElement.name);
                var total = eventElement.total;
                var count = eventElement.count;
                var total_2 = eventElement.total_2;
                var count_2 = eventElement.count_2;
                var demo1 = $("#J_demo" + (i * 2 + 1));
                demo1.attr("stroke-dasharray", "" + count / total * 156 + ",10000");
                var demo2 = $("#J_demo" + (i * 2 + 2));
                demo2.attr("stroke-dasharray", "" + count_2 / total_2 * 156 + ",10000");
                var t1 = $("#t" + (i * 2 + 1));
                t1.html(count);
                var t2 = $("#t" + (i * 2 + 2));
                t2.html(count_2);
                var $tab = $("#tab tr:nth-child(2)").find("td:nth-child(" + (i + 3) + ")");// td:nth-child("+2+i+")
                // console.log($tab)
                $tab.html(eventElement.name);
                $tab.attr("id", eventElement.project_param_id);
            }

        },
        error: function () {
            alert("error")
        }
    });
}



/**
 * 初始化方法；加载项目列表
 */
function irLoad(event) {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/projectMap',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            quasi++;
            loadProjectList()
            dealProject(event);
        },
        error: function () {
            $('#projectname').html("请联系管理员");
        }
    });
}


function loadProjectList() {
    $('#tab').html(
        "<tr class='tab-head' style='height: 25px'>" +
        "</tr>" +
        "<tr style=';font-size: 1px;position:fixed;top:63px;left:28px;height: 26px;z-index:2;width: 820px;opacity: 1;background: #c6d5f7;border: 0px solid #b6c6e9'>" +
        "    <td height='26'>样本号</td>" +
        "    <td width='96' style='font-size: 1px'>条码号</td>" +
        "    <td></td>" +
        "    <td> </td>" +
        "    <td> </td>" +
        "    <td> </td>" +
        "    <td> </td>" +
        "    <td> </td>" +
        "    <td>架 号</td>" +
        "    <td>位 号</td>" +
        "    <td>操 作</td>" +
        "</tr>");
}
/**
 * 保存项目
 */
function saveProject(projectParamid, placeNo, rackNo, code) {
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/saveProject',
        async: true,
        data: {
            projectParamId: projectParamid,
            humanCode: code,
            placeNo: placeNo,
            rackNo: rackNo,
            type: $("#tobeQC").is(':checked') ? 3 : 1,
        },
        jsonp: 'jsoncallback',
        success: function () {
            console.log("保存成功");
        },
        error: function () {
            $('#projectname').html("请联系管理员");
        }
    });
}

/**
 * 保存项目列表
 */
function saveProjectList(projectList) {
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/saveProjectList',
        async: true,
        data: {
            projectListStr: JSON.stringify(projectList),
        },
        jsonp: 'jsoncallback',
        success: function (event) {
            console.log("保存成功");
            if (event == -2) {
                alert("请检查试剂位置是否选择!");
            }
        },
        error: function () {
            $('#projectname').html("请联系管理员");
        }
    });
}


/**
 * 获取仪器状态数据
 */
function getEquipmentState() {
    $.ajax({
        type: 'get',
        url: urlhead + '/equipmentState/selectOne?id=1',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            $("#rackNo").html(data.rackNo);//架号
            $("#placeNo").html(data.placeNo);//位号
            var $pureWater = $("#pureWater");
            var pureWater = data.pureWater;
            if (pureWater === 1) {
                $pureWater.html("<img src='../../css/images/inputAndResult/chunshui.png' height='59' width='40'>")
            } else {
                $pureWater.html("<img src='../../css/images/inputAndResult/feishui.png' height='59' width='40'>")
            }
            var wasteWater = data.wasteWater;
            var $wasteWater = $("#wasteWater");
            if (wasteWater === 1) {
                $wasteWater.html("<img src='../../css/images/inputAndResult/chunshui.png' height='59' width='40'>")
            } else {
                $wasteWater.html("<img src='../../css/images/inputAndResult/feishui.png' height='59' width='40'>")
            }
        },
        error: function () {
            $('#projectname').html("请联系管理员");
        }
    })
}

/**
 *查下一个用户code，并且保存 手动
 */
$(document).ready(function () {
    $('#save').click(function () {
        pjsaveList();
    });
});


//保存手动测试项目
function pjsaveList() {
    var projectList = [];
    // 遍历 tr
    var turn = 1;
    $('#tab tr').each(function (i) {
        var trMark = 0;
        // 遍历 tr 的各个 td
        $(this).children('td').each(function (j) {
            var project = {};
            var tdMark = 0;
            if (i === 1) {
                console.log("i");
                var findSelect = $(this).find("select")[0];
                if (null != findSelect) {
                    // projectNameList[j] = findSelect.value;
                    // console.log(projectNameList);
                }
            } else {
                var findLi = $(this).find("li")[0];
                if (null != findLi && "liChance" === findLi.className) {
                    //
                    tdMark = 1;
                    var rackId = $('#tab tr:eq(' + i + ') td:nth-child(9)').find("select")[0].value;
                    var placeId = $('#tab tr:eq(' + i + ') td:nth-child(10)').find("select")[0].value;
                    var projectParamId = $('#tab tr:nth-child(2) td:nth-child(' + (j + 1) + ')')[0].id;
                    // alert(projectNameList[j-2].projectParamId)
                    var humanCode = $('#tab tr:eq(' + i + ') td:nth-child(1)')[0].innerText;
                    project.projectParamId = projectParamId;
                    project.placeId = placeId;
                    project.rackId = rackId;
                    project.humanCode = humanCode;
                    projectList.push(project);
                    // saveProject(projectParamId, placeId, rackId,humanCode);
                    // console.log(humanCode);
                    turn++;
                }
            }
            if (tdMark === 1) {
                trMark = 1;
            }
        });
        if (trMark === 1) {
            code++;
        }

    });
    // console.log(projectList);
    saveProjectList(projectList);
    setTimeout(getProjectListByDate, 500);
    setTimeout(refush, 500);
}


//保存自动测试项目
function saveAutoList() {
    var projectList = $("#projectname input").not(".layui-btn-primary");

    // console.log(projectList.length);
    $.each(projectList, function (i, project) {
        for (var j = 1; j < 6; j++) {
            saveProject(project.id, null, null);
        }
        alert("保存成功,project.id" + project.value);
    });
    getProjectListByDate();
}


//发送自动检测项目
function sendAutoList() {
    // console.log(projectList.length);
    send(-1, code, 5);
}

//发送手动检测项目
function sendList() {
    // console.log(projectList.length);
    send(-2, code, 5);
}


/**
 * 获取仪器状态数据
 */
function deleteProjects() {
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/deleteProjects',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            refush();
        },
        error: function () {
            $('#projectname').html("请联系管理员");
        }
    })
}

$(document).ready(function () {
    $('#delete').on('click', function () {
        deleteProjects();
    });
    $('html').mousedown(function () {
        //鼠标按下
        key = 1;
    }).mouseup(function () {
        //鼠标松开
        key = 0;
    });
    $('#tab').on('mousedown', 'li', function () {
        //鼠标按下
        key = 1;
        if ($(this).hasClass("liChance")) {
            $(this).removeClass("liChance");
            return;
        }
        $(this).attr("class", "liChance");
    }).on('mouseup', 'li', function () {
        //鼠标松开
        key = 0;
    }).on('mouseenter', 'li', function () {
        //鼠标进入
        if (key === 1) {
            if ($(this).hasClass("liChance")) {
                $(this).removeClass("liChance");
                return;
            }
            $(this).attr("class", "liChance");
        }
    });


    $(".start").click(function () {
        console.log("start");
        sendList();

        // setTimeout(refush,500);
        // circularReading();//改刷新时间
    });

    $(".suspend").click(function () {
        console.log("suspend");
        clearTimeout(timeout);
    });

    $(".stop").click(function () {
        console.log("stop");
        clearTimeout(timeout);
    });
    var deleteHumanCode;
    var deltd;
    var deltr;
    var deltab;
    var deltd1;
    $("#tab ").on('click', 'td:nth-child(1)', function () {
        // 第一列单击修改样本号事件
        var td = $(this);
        console.log(td[0].innerHTML);
        if (0 < td.find("input").length) {
            var inputText = td.find("input")[0].innerText;
            if (inputText > 0) {
                $(this).html(inputText);
            }
            return;
        }
        if ('测试显示&amp;操作区' === td[0].innerHTML) return;
        var input = '<input my= ' + $(this)[0].innerText + ' type="number" style="width: 42px;border: none;padding: 0;height: 18px;">';
        td.html(input);
        td.find("input")[0].focus();
    }).on('blur ', 'input', function () {
        //blur 失去焦点
        var message = $(this)[0].value;
        if ($(this)[0].value === "") {
            message = $(this).attr("my");
        }
        var fp = $(this).parent();
        fp.html(message);
        // $(this).attr("my")
        console.log(fp);
        //将接下来的第一列都修改
        var sp = fp.parent();
        var nextAll = sp.nextAll();
        nextAll.each(function (i) {
            var td = $(this).find("td:nth-child(1)");
            console.log(td.innerHTML);
            td.html((i + 1 + parseInt(message)));
        });
        console.log(nextAll);
    }).on('click', 'td:nth-child(11)', function () {
        ///删除
        deltd = $(this);
        // console.log(deltd1);
        // console.log(deltr);
        deleteHumanCode = deltd.parent().children()[0].innerHTML;
        if ("X" == deltd[0].innerHTML) {
            var deleteProjectBox = $("#deleteProjectBox");
            // noinspection JSAnnotator
            // if (deltr ==null || deltr.parentNode == null || deltr[0] == "td"){
            // }else {
            deleteProjectBox.find("#delProjectSure").html("确认删除项目" + deleteHumanCode + '吗？');
            deleteProjectBox.fadeIn("slow");
            // }
        }
    });
    //删除项目
    $("#delProject").on('click', function () {
        var $tab = $("#tab td:nth-child(1)");
        $tab.each(function (i) {
            if ($tab[i].innerText == deleteHumanCode) {
                deltd1 = $tab[i];
            }
        });
        // console.log($tab.html());
        // console.log(deltd1.parentNode);
        // console.log(deltd1);
        deltd1.parentNode.parentNode.removeChild(deltd1.parentNode);

        $.ajax({
            type: 'GET',
            url: urlhead + '/patient/deleteByHumanCode',
            async: true,
            data: {
                humanCode: deleteHumanCode
            },
            jsonp: 'jsoncallback',
            success: function (event) {
                if (deltr != null && deltr.parentNode != null) {
                    deltr.parentNode.removeChild(deltr);
                }
                refush();
                $("#deleteProjectBox").fadeOut("fast");
            },
            error: function () {
                alert("error")
            }
        });
        deltr = null;
    });

    $(".regent-bottle td").on('click', function () {
        var loginBox = $("#LoginBox");
        loginBox.find("#updateBox").html("确认修改试剂位置" + $(this).attr("place"));
        loginBox.find("#updateBox").attr("place", $(this).attr("place"));
        loginBox.fadeIn("slow");
    });
    //关闭悬浮的窗口
    $(".close_btn").hover(function () {
        $(this).css({color: 'black'})
    }, function () {
        $(this).css({color: '#999'})
    }).on('click', function () {
        $("#LoginBox").fadeOut("fast");
        $("#deleteProjectBox").fadeOut("fast");
        $("#mask").css({display: 'none'});
    });
    //修改    试剂位置
    $("#updateBox").on("click", function () {
        var parent = $(this).parent().parent();
        var regent_project = parent.find("#regent_project");
        var type = parent.find("#regent_type")[0].value;
        var id = regent_project[0].value;
        console.log(id);
        var place = $(this).attr("place");
        updateRegentPlace(place, id, place, type);
        setTimeout(refush, 500);
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

    /**
     * 推出
     */
    $("#tuichu").on('click', function () {
        $.ajax({
            type: 'GET',
            url: urlhead + '/adjusted/tuichu',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    })
    /**
     * 推入进
     */
    $("#tuijin").on('click', function () {
        $.ajax({
            type: 'GET',
            url: urlhead + '/adjusted/tuijin',
            async: true,
            jsonp: 'jsoncallback',
            success: function (event) {
            },
            error: function () {
                alert("error");
            }
        });
    })
});

function refush() {
    location = location;

}


function time() {//
    var date = new Date();
    $("#date").html("" + date.getFullYear() + "年"
        + (new Date().getMonth() + 1) + "月" + new Date().getDate() + "日&nbsp" + date.getHours()
        + ":" + date.getMinutes() + ":" + date.getSeconds() + " ");
    setTimeout(function () {
        time();
    }, 500);
}

var timeout1;
function alertWin(id) {
    $(".opacity_bg").show(); // 显示背景层,覆盖当前页面内容
    $("#dialog").show();
    var url;
    if (id === 1) {
        url = "scaling.html";
    }
    if (id === 2) {
        url = "qc.html";    // 加载弹出页
    }
    // $("#dialog").html("<iframe scrolling='no' src=" + url + " height=750 width=1280 frameborder=null style='z-index: 1023;'></iframe>") ;    // 加载弹出页
    $("#dialog").load(url);    // 加载弹出页
    if (id === 1){
        setTimeout(getOneProjectsScalingTime(1), 100);
        setTimeout(setBEDate, 100);
    }else {
        setTimeout(qc_load, 100);
        setTimeout(setBEDate, 100);
        // timeout1 = setTimeout(readqc, 15000);
    }

    setTimeout(sc_irload, 100);


}

/**
 * 获取在做的项目qc
 */
function readqc() {
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/getProjectListByDataTenToEnd',
        async: true,
        data: {
            endtime: new Date().getFullYear() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getDate()
        },
        jsonp: 'jsoncallback',
        success: function (event) {

            irLoad(event);
        },
        error: function () {
            alert("error")
        }
    });
}


function iclose() {
    $(".opacity_bg").hide(); // 隐藏背景层
    $("#dialog").empty().hide(); // 清除弹出页
}






/**
 *修改试剂位置
 */
function updateRegentPlace(id, projectParamId, place, type) {
    $.ajax({
        type: 'get',
        url: urlhead + '/regentPlace/updateRegentPlace',
        async: true,
        data: {
            id: id,
            projectParamId: projectParamId,
            place: place,
            type: type
        },
        jsonp: 'jsoncallback',
        success: function (event) {
        },
        error: function () {
            alert("error")
        }
    });
}