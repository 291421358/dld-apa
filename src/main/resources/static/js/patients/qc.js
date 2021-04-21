var urlhead = getPort();
var Oid;
var quaslength = 0;

/**
 * 获得某个项目的所有QC项目
 */
function getOneProjectsAllQcByProjectParamId(id) {
    Oid = id;
    $.ajax({
        type: 'get',
        url: urlhead + '/qc/getOneProjectsAllQcByProjectParamId',
        async: true,
        data: {
            projectParamId: id,
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            var projectList = data.projects;
            var canvas = document.getElementById('qcCurve');

            if (canvas.getContext) {
                var ctx = canvas.getContext('2d');
                ctx.clearRect(0, 0, 2000, 2000);
                $.each(projectList,function (i, project) {
                    var ctx3 = canvas.getContext('2d');
                    ctx3.strokeStyle = "blue";
                    ctx3.lineWidth = 0.99;
                    ctx3.strokeText((project.density )+"("+(project.endtime ).toString().substring(5,11).toString()+")", (i - 1) * 30 + 30, 200 - project.density - (i % 6 * 2 - 2) * 5);
                    ctx3.strokeText(project.id  , (i - 1) * 30+30, 210);
                });
                var ctx4 = canvas.getContext('2d');
                ctx4.strokeStyle = "black";
                ctx4.lineWidth = 0.99;
                ctx4.strokeText("0", 3, 200-180);
                ctx4.strokeText("50", 3, 200-50);
                ctx4.strokeText("100", 3, 200-100);
                ctx4.strokeText("150", 3, 200-150);
                ctx4.stroke();
                var ctx5 = canvas.getContext('2d');
                ctx5.strokeStyle = "black";
                ctx5.lineWidth = 0.99;
                ctx5.beginPath();
                ctx5.lineTo(0, 100);
                ctx5.lineTo(canvas.width, 100);
                ctx5.stroke();
                var ctx1 = canvas.getContext('2d');
                ctx1.lineWidth = 2;
                ctx1.strokeStyle = "red";
                ctx1.beginPath();
                $.each(projectList, function (i, project) {
                    ctx1.lineTo((i - 1) * 30 + 25, 200 - project.density);

                });
                ctx1.stroke();

            }
        },
        error: function () {
            $('#message').html("请联系管理员");
        }
    });
}

function updateDensity() {
    console.log("111111");
    $.ajax({
        type: 'post',
        url: urlhead+'/qc/updateDensity',
        async: true,
        data: {
            id: $("#projectId").val(),
            density: $("#density").val(),
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            alert("第"+$("#projectId").val()+"个项目修改为"+$("#density").val());
            getOneProjectsAllQcByProjectParamId(Oid);
        },
        error: function () {
            $('#details').html("有异常");
        }
    });
}
/**
 * 初始化方法；加载项目列表
 */
function irload() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/projectList',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            var projectList = data;
            var tabStr = "<label class='layui-form-label' style='text-align: left'>项目名</label>";
            $.each(projectList, function (i, project) {
                tabStr += "<div class='layui-form-item'>";
                tabStr += "<input class='layui-btn layui-btn-primary' type='button' paramid=" + project.id + " value=" + project.name + " >";
                tabStr += "</div>";
            });
            $('#projectName').html(tabStr);
            layui.use(['form', 'table'], function () {
                var table = layui.table;
                //监听单元格编辑
                table.on('edit(table1)', function (obj) {
                    var value = obj.value //得到修改后的值
                        , data = obj.data //得到所在行所有键值
                        , field = obj.field; //得到字段
                    layer.msg('[ID: ' + data.id + '] ' + field + ' 字段更改为：' + value);
                });
            });
        },
        error: function () {
            $('#projectName').html("请联系管理员");
        }
    });
}

/**
 * 获得某个项目的所有质控项目
 */
function getQcProjects(id) {
    $.ajax({
        type: 'get',
        url: urlhead + '/qC/getQcProjects',
        async: true,
        data: {
            projectParamId: id,
            beginDate: $("#QCBeginDate")[0].value,
            endDate: $("#QCEndDate")[0].value,
            type:$("#type").val(),
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            window.localStorage.setItem('qcProjectValue', JSON.stringify(data));// 插入 对象转字符串
            var projectList = data;
            var canvas = document.getElementById('qcCanvas');
            var ctx = canvas.getContext('2d');
            // console.log(projectList);
            ctx.clearRect(0, 0, 2000, 2000);
            var ctx1 = canvas.getContext('2d');
            ctx1.lineWidth = 2;
            ctx1.strokeStyle = "black";
            ctx1.beginPath();
            for (var j = 0; j < projectList.length - 1; j++) {
                ctx1.lineTo((projectList.length - j) * 55 - 63, canvas.height / 2 - projectList[j].proportionateDensity);
                ctx1.lineTo((projectList.length - j) * 55 - 63, canvas.height / 2 - projectList[j].proportionateDensity - 3);
                ctx1.lineTo((projectList.length - j) * 55 - 63, canvas.height / 2 - projectList[j].proportionateDensity + 3);
                ctx1.lineTo((projectList.length - j) * 55 - 63, canvas.height / 2 - projectList[j].proportionateDensity);
            }
            ctx1.stroke();
            var avg = $("#avg");
            var sta = projectList[projectList.length - 1].sta;
            // console.log(sta);
            avg.find("span").each(function (i) {
                var coefficient = (1.3 - i * 0.1);
                //取得30%~-30%的数
                var fixValue = (sta * coefficient).toFixed(2);
                // console.log(message);
                $(this).html(fixValue);
            });
            var dateStr = "";
            for (var j = projectList.length - 2; j >= 0; j--) {
                //取得时间
                dateStr += "<div style='float: left;width: 55px'>" + projectList[j].date.substring(3) + "<input hidden value=" + projectList[j].id + "></div>"
            }
            $("#dates").html(dateStr);
        },
        error: function () {
            console.log("请联系管理员")
        }
    })
}
/**
 * 保存个项目 saveProject
 */
function saveProject(id, type, absorbance, factor, density, place_no, rack_no) {
    console.log(place_no);
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/saveProject',
        async: true,
        data: {
            cupNumber: density,
            projectParamId: id,
            type: type,
            placeNo: place_no,
            rackNo: rack_no,
            factor: factor,
            absorbance: absorbance
        },
        jsonp: 'jsoncallback',
        success: function () {
            console.log("保存成功")
        },
        error: function () {
            console.log("请联系管理员")
        }
    });
}



//保存项目saveQC
function saveQC() {
    console.log("into 保存项目saveQC");
    $("#qc_deal tr").each(function (i) {
        if ($(this).find("td")[0].innerHTML === "" || $(this).find("td")[0].innerHTML === "-") {
            console.log("1");
            return;
        }
        if (i > 0) {
            var rack_no = 0;
            var place_no = 0;
            var density = 0;
            $(this).find("td").each(function (j) {
                switch (j) {
                    case 0:
                        density = $(this)[0].innerHTML;
                        break;
                    case 1:
                        place_no = $(this)[0].innerHTML;
                        break;
                    case 2:
                        rack_no = $(this)[0].innerHTML;
                        break;
                    case 3:
                        break;
                }
            });
            var type = 3;
            if (i % 3 == 2){
                type = 4;
            }
            if (i % 3 == 0){
                type = 5;
            }

            if (rack_no >0 && place_no >0) {
                saveProject(paramid, type, null, null, density, place_no, rack_no);
            }
        }
    });
}

/**
 * 修改qc标准值
 * @param projectParamNum
 * @param staQuality
 */
function updateQCSta(projectParamNum, staQuality) {
    $.ajax({
        type: 'get',
        url: urlhead + '/qC/update',
        async: true,
        data: {
            id: null,
            projectParamNum: projectParamNum,
            staQuality: staQuality
        },
        jsonp: 'jsoncallback',
        success: function (data) {

        },
        error: function () {
            console.log("请联系管理员")
        }
    })
}
/**
 * 查询qc标准值
 */
function presetQc() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/presetQc',
        async: true,
        data: {},
        jsonp: 'jsoncallback',
        success: function (data) {
            var $SCProjectList = $("#SC_projectList input");
            for (let i = 0; i < $SCProjectList.length; i++) {
                var id = $SCProjectList[i].id;
                console.log(id);
                for (let j = 0; j < data.length; j++) {
                    if (data[j].id == id) {
                        var presetDensityHight = $("#qc_deal tr:nth-child("+(3*i+2)+") td:nth-child(1)")[0];
                        var presetDensityMid = $("#qc_deal tr:nth-child("+(3*i+3)+") td:nth-child(1)")[0];
                        var presetDensityLow = $("#qc_deal tr:nth-child("+(3*i+4)+") td:nth-child(1)")[0];
                        presetDensityHight.innerText =data[j].presetDensityHight;
                        presetDensityMid.innerText =data[j].presetDensityMid;
                        presetDensityLow.innerText =data[j].presetDensityLow;
                    }
                }
            }

        },
        error: function () {
            console.log("请联系管理员")
        }
    })
}
function noParamGetQcProjects() {
    getQcProjects(paramid);
}
/**
 * 保存质控项目
 */

$("#qc_save").on('click', function () {
    var seconds = new Date().getSeconds();
    for (; new Date().getSeconds() >= 58;) {
    }
    console.log(seconds);
    saveQC();
});

$("#qc_deal").on('click', 'td', function () {
    // 质控项目
    var td = $(this);
    var innerHTML = td[0].innerHTML;
    console.log(innerHTML);
    if (0 < td.find("input").length) {
        var inputText = td.find("input")[0].innerText;
        if (inputText > 0) {
            $(this).html(inputText);
        }
        return;
    }
    if ('序号' === innerHTML || innerHTML.indexOf("吸光度") >= 0 || '预设浓度' === innerHTML || '检测结果' === innerHTML || '架号' === innerHTML || '位号' === innerHTML) return;

    var input = '<input  my=' + ($(this)[0].innerText === "" ? "-" : $(this)[0].innerText) + ' type="number" style="width: 100%;border: none;padding: 0;height: 13px;" value=' + $(this)[0].innerText + '>';
    td.html(input);
    td.find("input")[0].focus();
}).on('blur ', 'input', function () {
    //blur 失去焦点
    var message = $(this)[0].value;
    if ($(this)[0].value === "") {
        message = $(this)[0].value;
    }
    var fp = $(this).parent();
    fp.html(message);
});


var updateId = "";
$("#dates").on("click", 'div', function () {

    var updateQC = $("#updateQC");
    updateId = $(this).find("input")[0].value;
    $("#updateValue").html($(this)[0].textContent + "<input id='newQCValue'><input hidden value=" + updateId + ">");
    updateQC.fadeIn("slow");
});

$("#closeQCBox").on("click", function () {
    var updateQC = $("#updateQC");
    updateQC.fadeOut("fast");
});
//修改定标值
$("#doUpdateQC").on("click", function () {
    var value = $("#newQCValue")[0].value;
    console.log(value);
    if (value == "") {
        alert("⚠：没有输入要修该的值，如需修改请重新输入")
    } else {
        updateProject(updateId, null, null, value, null, null);
    }
    var updateQC = $("#updateQC");
    updateQC.fadeOut("fast");
    setTimeout(noParamGetQcProjects, 500);
});
//
$("#avg").on("click", function () {
    // paramid
    var updateQCSta = $("#updateQCSta");
    $("#updateStaValue").html("<input id='newQCStaValue'>");
    updateQCSta.fadeIn("slow");
});
$("#closeQCStaBox").on("click", function () {
    var updateQC = $("#updateQCSta");
    updateQC.fadeOut("fast");
});
//修改定标标准值
$("#doUpdateQCSta").on("click", function () {
    var value = $("#newQCStaValue")[0].value;
    console.log(value);
    if (value == "") {
        alert("⚠：没有输入要修该的值，如需修改请重新输入")
    } else {
        updateQCSta(paramid, value);
    }
    var updateQC = $("#updateQCSta");
    updateQC.fadeOut("fast");
    setTimeout(noParamGetQcProjects, 500);
});
//查询
$("#QCBeginDate").on("change", function () {
    noParamGetQcProjects();
});
$("#QCEndDate").on("change", function () {
    noParamGetQcProjects();
});
$("#type").on("change", function () {
    noParamGetQcProjects();
});
$("#test").on("click",function () {
        sendList();
});
//初始化开始时间结束时间
function setBEDate() {
    var date = new Date();
    var month = new Date().getMonth();
    var monthPl = month + 1;
    var day = new Date().getDate();
    if (month < 10) {
        month = 0 + "" + month
    }
    if (monthPl < 10) {
        monthPl = 0 + "" + monthPl
    }
    if (day < 10) {
        day = 0 + "" + day
    }
    var value = date.getFullYear() + "-" + (month) + "-" + day;
    console.log(value);
    $("#QCBeginDate").val(value);
    var value1 = date.getFullYear() + "-" + (monthPl) + "-" + day;
    $("#QCEndDate").val(value1);

}
//条件查询用的数组
var availableTags =  [];
function qc_load() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/projectList',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            var projectList = data.nameMap;
            $.each(projectList, function (i) {
                availableTags[i] = {};
                availableTags[i].label = projectList[i].id+"-"+projectList[i].name;
                availableTags[i].id = projectList[i].id;
            });
            console.log(availableTags)
        },
        error: function () {
            $('#info').html("请联系管理员");
        }
    });
}

$(function () {

    $("#keyword").autocomplete({
        source: availableTags,
        width: 100,
        select: function( event, ui ) {
            paramid = ui.item.id;
            noParamGetQcProjects();
        },
    });





    /**
     *     打开打印界面
     */
    $("#printf").on('click', function () {
        window.open("./qcPrint.html", '打印', 'height=1200, width=864, top=0,left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
    });
});


function set_qc(parse) {
    $('#SC_projectList input').each(function (i) {
            if ($(this).id == parse.ppi){
                var tr1 = $("#qc_deal tr:nth-child("+(i*2+1)+")");
                tr1.find(" td:nth-child(0)");
                $("#qc_deal tr:nth-child("+(i*2+1)+") td:nth-child(3)").html(parse.density);
            }
    })
}
