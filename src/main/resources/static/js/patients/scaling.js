var urlhead = getPort();
var quasi;
var quaslength = 0;
var paramid = 1;

/**
 * 初始化方法；加载项目列表。左侧列表
 */
function sc_irload() {
    $.ajax({
        type: 'get',
        url: urlhead + '/regentPlace/getRegentPlace',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            var projectList = data;
            projectNameList = projectList;
            var tabStr = "";
            $.each(projectList, function (i, project) {
                if (i > 5) {
                    return;
                }
                tabStr += "<div ><span style='width: 100%'>" + (project.name == null ? '' : project.name) + "</span><input disabled type='checkbox' id=" + project.project_param_id + " value=" + project.name + " ></div>";
            });
            $('#SC_projectList').html(tabStr);
        },
        error: function () {
            console.log("请联系管理员")
        }
    });
}


/**
 * 获得最后一个定标项目 (over)
 */
function getLatestOne(projectParamId) {
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/getLatestOne',
        data: {
            projectParamId: projectParamId
        },
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            var projectList = data;
            var tabStr = "";
            // console.log(projectList);

            $('#scaling_deal tr').each(function (i, project) {
                // console.log(i);
                tabStr = "<tr>";
                if (i > 0) {
                    var projectListI = projectList[i - 1];
                    // console.log(projectListI);

                    $(this).find("td").each(function (j) {
                        if (projectListI == null) {
                            $(this).html("");
                            return;
                        }
                        switch (j) {
                            case 0:
                                $(this).html(i + "<input hidden value=" + projectListI.id + ">");
                                break;
                            case 1:
                                var absorbance = parseFloat(projectListI.absorbance);
                                $(this).html((absorbance == null || isNaN(absorbance)) ? "" : absorbance.toFixed(4));
                                break;
                            case 2:
                                $(this).html(projectListI.factor);
                                break;
                            case 3:
                                $(this).html(projectListI.density);
                                break;
                            case 4:
                                $(this).html(projectListI.rack_no);
                                break;
                            case 5:
                                $(this).html(projectListI.place_no);
                                break;
                            case 6:
                        }
                    });
                }
                tabStr += "</tr>";
            });
        },
        error: function () {
            console.log("请联系管理员")
        }
    });
}

/**
 * 获得一个定标项目的时间 (over)
 */
function getOneProjectsScalingTime(projectParamId) {
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/getOneProjectsScalingTime',
        async: true,
        data: {
            projectParamId: projectParamId
        },
        jsonp: 'jsoncallback',
        success: function (event) {
            var projectList = event;
            var tabStr = "";
            var choiceTime = "";
            // console.log(projectList);
            var scalingMark = 0;
            $.each(projectList, function (i, project) {
                tabStr += "<div style='height: max-content'><input ";
                if ("" !== project.check && project.check != null) {
                    tabStr += "checked";
                    scalingMark = 1;
                    choiceTime = project.date;
                }
                tabStr += " id=" + project.id + " style='float: left;width: 5%' name='project_date' title=" + project.date + " type='radio'><li style='width: 92%' ";
                if (i == 0) {
                    tabStr += "class='checkedColor'";
                }
                tabStr += ">" + project.date + "</li></div>";

            });
            $('#scaling_time').html(tabStr);
            if (scalingMark == 0) {
                var canvas = document.getElementById('canvas');
                var ctx = canvas.getContext('2d');
                ctx.clearRect(0, 0, 2000, 2000);

                alert("该项目暂未选中定标");
            }
            if (choiceTime != "") {
                getOneProjectsAllScalingByCon(paramid, 2, choiceTime)
            }
        },
        error: function () {
            console.log("请联系管理员")
        }
    });
}

/**
 *根据时间获得定标项目/并画出曲线
 */
function getOneProjectsAllScalingByCon(id, type, time) {
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/getOneProjectsAllScalingByCon',
        async: true,
        data: {
            id: id,
            type: type,
            time: time
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            var projectList = data.projects;
            var valueList = data.value;

            var canvas = document.getElementById('canvas');
            var ctx = canvas.getContext('2d');
            var calList = [];
            ctx.clearRect(0, 0, 2000, 2000);
            if (null != valueList && valueList.length > 0) {
                var mi = 1;
                var k  =0;
                $('#scaling_deal tr').each(function (i) {
                    // console.log(i);
                    if (i > 0) {
                        var projectListI = projectList[i - mi+k];
                        if (null != projectListI && projectListI.type === 6) {

                            for (let j = i; j <= projectList.length ; j++) {
                                console.log(j+"len"  +mi);

                                var projectListElement = projectList[j - mi];
                                if (projectListElement.type === 6){
                                    calList.push(projectListElement);
                                }
                                else{
                                    break;
                                }
                                k++;
                                // mi--;
                            }
                            console.log(i - mi +k);
                            projectListI = projectList[i - mi +k];
                        }
                        // console.log(projectListI);

                        $(this).find("td").each(function (j) {
                            if (projectListI == null) {
                                $(this).html("");
                                return;
                            }
                            switch (j) {
                                case 0:
                                    $(this).html(i + "<input hidden value=" + projectListI.id + ">");
                                    break;
                                case 1:
                                    var absorbance = parseFloat(projectListI.absorbance);
                                    $(this).html((absorbance == null || isNaN(absorbance)) ? "" : absorbance.toFixed(5));
                                    break;
                                case 2:
                                    $(this).html(projectListI.factor);
                                    break;
                                case 3:
                                    $(this).html(projectListI.density);
                                    break;
                                case 4:
                                    $(this).html(projectListI.rackNo);
                                    break;
                                case 5:
                                    $(this).html(projectListI.placeNo);
                                    break;
                                case 6:
                            }
                        });
                    }
                });

                var lenMi = valueList.length - 2;
                if (valueList[lenMi] ==undefined) {

                } else {
                    var step = valueList[lenMi].step;
                    // console.log(projectList);
                    var ctx1 = canvas.getContext('2d');
                    ctx1.lineWidth = 1;
                    ctx1.strokeStyle = "red";
                    ctx1.beginPath();
                    for (var j = 0; j < valueList.length-1; j++) {
                        //画曲线
                        ctx1.lineTo(j * 200 / (valueList.length - 3) + 31, canvas.height - valueList[j] - 34);
                        // console.log(j)
                    }
                    ctx1.stroke();

                    var relList = valueList[lenMi+1];
                    // console.log(relList.length);
                    var ctx5 = canvas.getContext('2d');
                    ctx5.lineWidth = 1;
                    ctx5.strokeStyle = "black";
                    if (relList.length > 1){

                        ctx5.beginPath();
                        for (var j = 0; j < valueList.length; j++) {
                            //画曲线
                            ctx5.lineTo(j * 200 / (relList.length) + 31, canvas.height - relList[j] - 34);
                            // console.log(j)
                        }

                    }
                    ctx5.stroke();
                    var algorithm = valueList[lenMi].algorithm;
                    $("#scalingAlgorithm").find("option").each(function () {
                        if ($(this)[0].value === algorithm) {
                            $(this)[0].selected = true;
                        } else {
                            $(this)[0].selected = false;
                        }
                    });
                    var max = valueList[lenMi].max;
                    var min = valueList[lenMi].min;
                    var maxX = valueList[lenMi].maxX;
                    var minX = valueList[lenMi].minX;
                    var ctx2 = canvas.getContext('2d');
                    ctx2.font = "DIN";//italic bold  small-caps
                    for (var k = 0; k <= 5; k++) {
                        if (step == null) {
                            break;
                        }
                        //画y
                        ctx2.lineWidth = 1;
                        var y = (k * ((max - min) / 5) + min);//四分之一 4
                        if (y.toFixed(0) == '-0') {
                            y = 0;
                        }
                        if (k === 0 || k === 5) {
                            ctx2.strokeText(y.toFixed(2), 0, 200 - k * 38.2);//四分之一 46.5
                        } else {
                            ctx2.strokeText(y.toFixed(3), 0, 200 - k * 40);
                        }
                    }
                    //画x
                    for (var h = 0; h <= 5; h++) {
                        var digit = 1;
                        if (maxX - minX < 0.1) {
                            digit = 2;
                        }
                        ;
                        if (step == null) {
                            break;
                        }
                        var digitLength = step.toString().length - 1;
                        var x = minX + (h * (+(maxX - minX) / 5));
                        if ((h === 5)) {
                            ctx2.strokeText(x.toFixed(0), h * 38.2 + 32, 214);//
                        } else {
                            ctx2.strokeText(x.toFixed(digitLength), h * 40 + (26 - digitLength * 3), 214);
                        }

                    }
                    ctx2.stroke();
                    var ctx3 = canvas.getContext('2d');
                    for (var m = 0; m < projectList.length; m++) {
                        ctx3.strokeStyle = "#3db7ff";
                        ctx3.lineWidth = 2;
                        var density = projectList[m].absorbance;
                        if (density > max) {
                            density = max;
                        }
                        // console.log(projectList[m].absorbance,density);
                        var gap = max - min;
                        if (gap === 0) {
                            ctx3.strokeText('*', projectList[m].absorbance * (200 / (lenMi) * 10) + 32, 205);
                        }
                        //画点
                        // console.log("点的位置",projectList[m].absorbance * (200 / (valueList.length - 1) * 10) + 31, density);
                        console.log("点的位置", (projectList[m].density - minX) * (200 / (maxX - minX)) + 31, density);
                        ctx3.strokeText('*', (projectList[m].density - minX) * (200 / (maxX - minX)) + 31, 207 - ((density - min) * (200 / (max - min))));
                    }
                }
            }
                $('#cal tr').each(function (i) {
                    // console.log(i);
                    if (i > 0) {
                        var calListI = calList[i - 1];
                        $(this).find("td").each(function (j) {
                            if (calListI == null) {
                                $(this).html("");
                                return;
                            }
                            switch (j) {
                                case 0:
                                    $(this).html(i + "<input hidden value=" + calListI.id + ">");
                                    break;
                                case 1:
                                    var absorbance = parseFloat(calListI.absorbance);
                                    $(this).html((absorbance == null || isNaN(absorbance)) ? "" : absorbance.toFixed(5));
                                    break;
                                case 2:
                                    $(this).html(calListI.factor);
                                    break;
                                case 3:
                                    $(this).html(calListI.density);
                                    break;
                                case 4:
                                    $(this).html(calListI.rackNo);
                                    break;
                                case 5:
                                    $(this).html(calListI.placeNo);
                                    break;
                                case 6:
                            }
                        });
                    }
                });

        },
        error: function () {
            console.log("请联系管理员")
        }
    });

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
            // starttime: $("#scaling_time .checkedColor").html(),
            density: density,
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
/**
 * 保存校准项目 saveProject
 */
function saveCalProject(id, type, absorbance, factor, density, place_no, rack_no) {
    console.log(place_no);
    $.ajax({
        type: 'get',
        url: urlhead + '/productTest/saveProject',
        async: true,
        data: {
            starttime: $("#scaling_time .checkedColor").html(),
            density: density,
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

/**
 * 添加定标项目计算方法
 */
function insertScalingAlgorithm(Algorithm) {
    $.ajax({
        type: 'get',
        url: urlhead + "/scaling/insertScalingAlgorithm",
        data: {
            algorithm: Algorithm,
        },
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            // $('#projectname').html("保存成功");
            console.log("添加定标项目计算方法保存成功")
        },
        error: function () {
            console.log("请联系管理员")
        }

    });
}

var args;

/***
 * @apiNote 保存电子定标校准
 * @author tzhh
 * @date 2021/6/8 15:39
 * @param null
 * @return {@link null}
 **/
function saveCalList() {
    console.log("into saveList");
    $("#cal tr").each(function (i) {
        if ($(this).find("td")[3].innerHTML === "" || $(this).find("td")[3].innerHTML === "-" || $(this).find("td")[0].innerHTML === "" || $(this).find("td")[0].innerHTML === "-") {
            console.log("1");
            return;
        }
        if (i > 0) {
            var absorbance = 0;
            var factor = 0;
            var density = 0;
            var rack_no = 0;
            var place_no = 0;
            $(this).find("td").each(function (j) {
                switch (j) {
                    case 0:
                        break;
                    case 1:
                        absorbance = "";// $(this)[0].innerHTML;
                        break;
                    case 2:
                        factor = $(this)[0].innerHTML===""?undefined:$(this)[0].innerHTML;
                        //$(this)[0].innerHTML;
                        break;
                    case 3:
                        density = $(this)[0].innerHTML;
                        break;
                    case 4:
                        rack_no = $(this)[0].innerHTML;
                        break;
                    case 5:
                        place_no = $(this)[0].innerHTML;
                        break;
                    case 6:
                }
            });
            ;


            timeout = setTimeout(saveCalProject, (i-1) * 1000, paramid, 6, absorbance, factor, density, place_no, rack_no)
        }
    });
}

/***
 * @apiNote 保存定标项目saveList
 * @author tzhh
 * @date 2021/6/8 15:39
 * @param null
 * @return {@link null}
 **/
function saveList() {
    console.log("into saveList");
    $("#scaling_deal tr").each(function (i) {
        if ($(this).find("td")[3].innerHTML === "" || $(this).find("td")[3].innerHTML === "-" || $(this).find("td")[0].innerHTML === "" || $(this).find("td")[0].innerHTML === "-") {
            console.log("1");
            return;
        }
        if (i > 0) {
            var absorbance = 0;
            var factor = 0;
            var density = 0;
            var rack_no = 0;
            var place_no = 0;
            $(this).find("td").each(function (j) {
                switch (j) {
                    case 0:
                        break;
                    case 1:
                        absorbance = "";// $(this)[0].innerHTML;
                        break;
                    case 2:
                        factor = undefined;//$(this)[0].innerHTML;
                        break;
                    case 3:
                        density = $(this)[0].innerHTML;
                        break;
                    case 4:
                        rack_no = $(this)[0].innerHTML;
                        break;
                    case 5:
                        place_no = $(this)[0].innerHTML;
                        break;
                    case 6:
                }
            });
            ;


            timeout = setTimeout(saveProject, i * 150, paramid, 2, absorbance, factor, density, place_no, rack_no)
        }
    });
    setTimeout(getOneProjectsScalingTime, 1050, paramid);

    setTimeout(function () {
        args = $("#scaling_time input")[0].id;
        console.log(args);
        setTimeout(updateProjectsScaling, 100, paramid, args);
    }, 1200);

}

function delOne(projectId) {
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/delOne',
        async: true,
        data: {
            projectId: projectId,
        },
        jsonp: 'jsoncallback',
        success: function (data) {

        },
        error: function () {
            console.log("请联系管理员")
        }
    })

}

function updateScalingAlgorithm(dateId, Algorithm) {
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/updateScalingAlgorithm',
        async: true,
        data: {
            dateid: dateId,
            algorithm: Algorithm
        },
        jsonp: 'jsoncallback',
        success: function (data) {

        },
        error: function () {
            console.log("请联系管理员")
        }
    })

}

function del(dateId, paramId) {
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/deleteOneScaling',
        async: true,
        data: {
            dateId: dateId,
            paramId: paramId
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
 *修改单个定标项目数值 updateProjectParam
 */
function updateProject(id, absorbance, factor, density, rackNo, placeNo) {
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/updateProject',
        data: {
            id: id,
            absorbance: absorbance,
            factor: factor,
            density: density,
            rackNo: rackNo,
            placeNo: placeNo
        },
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            console.log("修改定标项目数值" + data);
        },
        error: function () {
            console.log("请联系管理员")
        }
    });
}

/**
 * 修改某个项目的定标
 */
function updateProjectsScaling(projectParamid, projectId) {
    console.log(projectId);
    $.ajax({
        type: 'get',
        url: urlhead + '/scaling/updateProjectsScaling',
        async: true,
        data: {
            projectParamId: projectParamid,
            projectId: projectId,
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            if (quaslength > 0) {
            }
        },
        error: function () {
            $('#message').html("请联系管理员");
        }
    });
}

$(document).ready(function () {
    $("#SC_projectList").on('click', 'div', function () {
        //点击项目列表的的方法
        paramid = $(this).find("input")[0].id;
        // getOneProjectsAllScaling(paramid);
        getLatestOne(paramid);
        getOneProjectsScalingTime(paramid);
        // getQcProjects(paramid);

    });
    $("#scaling_time").on('click', 'li', function () {
        var title = $(this).parent().find("input")[0].title;
        // console.log(title);
        getOneProjectsAllScalingByCon(paramid, 2, title);
    });


    $('#clear').on("click", function () {
        $('#scaling_deal tr').each(function (i) {
            // console.log(i);
            if (i > 0) {
                $(this).find("td").each(function (j) {
                    $(this).html(null);
                })
            }
        })
    })
});


var code = 0;
var rackNo = 0;
var overList;
$(document).ready(function () {

    $("#scaling_deal").bind("contextmenu", function () {
        return false;
    })
    $('tr').on("click", function () {

    });
    var MM = new Date().getUTCMonth() + 1;
    if (MM < 10) {
        MM = "0" + MM;
    }
    var dd = new Date().getDate();
    if (dd < 10) {
        dd = "0" + dd;
    }
    $("#date").attr("value", new Date().getFullYear() + "-" + (MM) + "-" + dd);


    $("#scaling_deal,#cal").on('click', 'td', function () {
        // 单击td修改样本号事件
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
        if ('序号' === innerHTML || innerHTML.indexOf("吸光度") >= 0 || '因子' === innerHTML || innerHTML.indexOf('浓度') >= 0 || '架号' === innerHTML || '位号' === innerHTML) return;
        // if ($(this)[0].innerText === "")
        var input = '<input my=' + ($(this)[0].innerText === "" ? "" : $(this)[0].innerText) + ' type="number" style="width: 100%;border: none;padding: 0;height: 18px;" value=' + $(this)[0].innerText + '>';
        var attribute = $(this)[0].attributes[0];
        if (attribute != null) {
            var className = attribute.value;
            if (className == 'rack' || className == 'place') {
                input = '<input my=' + ($(this)[0].innerText === "" ? "" : $(this)[0].innerText) + ' type="number" max="5" min="1" placeholder="1~5" oninput="if(value < 1 ){value =1};if(value > 5 ){value = value.substring(0,1)};if(value > 5 ){value = 5}" style="width: 100%;border: none;padding: 0;height: 18px;" value=' + $(this)[0].innerText + '>';
            }
        }
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

    $("#scaling_deal tr").mousedown(function (e) {
        //右键为3
        if (3 == e.which) {
            var jQueryElement = $(this).find("td");
            delOne(jQueryElement.find("input")[0].value);
            console.log(jQueryElement.find("input")[0].value)
        }
    });


    $("#send").on('click', function () {
        sendList();
    });

    $("#sc_save").on('click', function () {
        //保存/添加
        var seconds = new Date().getSeconds();
        for (; new Date().getSeconds() >= 58;) {
        }
        console.log(seconds);
        saveList();
        // var date = $("#date").innerHTML;
        var $scalingAlgorithm = $("#scalingAlgorithm");
        var Algorithm = $scalingAlgorithm[0].value;
        insertScalingAlgorithm(Algorithm);
    });


    $("#cal_save").on('click', function () {
        //保存
        var seconds = new Date().getSeconds();
        for (; new Date().getSeconds() >= 58;) {
        }
        console.log(seconds);
        console.log($("#scaling_time .checkedColor").html())
        saveCalList();
    });

    /**
     * @apiNote 修改定标参数
     * @author tzhh
     * @date 2021/6/8 15:40
     * @param null
     * @return {@link null}
     **/
    $("#update").on('click', function () {
        $("#scaling_deal tr").each(function (i) {
            if ($(this).find("td")[0].innerHTML === "" || $(this).find("td")[0].innerHTML === "-") {
                // console.log("1");
                return;
            }
            if (i > 0) {
                var id = 0;
                var absorbance = 0;
                var factor = 0;
                var density = 0;
                var rackNo = 0;
                var placeNo = 0;
                $(this).find("td").each(function (j) {
                    switch (j) {
                        case 0:
                            id = $(this).find("input")[0].value;
                            break;
                        case 1:
                            absorbance = $(this)[0].innerHTML;
                            break;
                        case 2:
                            factor = $(this)[0].innerHTML===""?undefined:$(this)[0].innerHTML;
                            break;
                        case 3:
                            density = $(this)[0].innerHTML;
                            break;
                        case 4:
                            rackNo = $(this)[0].innerHTML;
                            break;
                        case 5:
                            placeNo = $(this)[0].innerHTML;
                            break;
                        case 6:
                    }
                });
                updateProject(id, absorbance, factor, density, rackNo, placeNo);
            }
        })

        $("#cal tr").each(function (i) {
            if ($(this).find("td")[0].innerHTML === "" || $(this).find("td")[0].innerHTML === "-") {
                // console.log("1");
                return;
            }
            if (i > 0) {
                var id = 0;
                var absorbance = 0;
                var factor = 0;
                var density = 0;
                var rackNo = 0;
                var placeNo = 0;
                $(this).find("td").each(function (j) {
                    switch (j) {
                        case 0:
                            id = $(this).find("input")[0].value;
                            break;
                        case 1:
                            absorbance = $(this)[0].innerHTML;
                            break;
                        case 2:
                            factor = $(this)[0].innerHTML===""?undefined:$(this)[0].innerHTML;
                            break;
                        case 3:
                            density = $(this)[0].innerHTML;
                            break;
                        case 4:
                            rackNo = $(this)[0].innerHTML;
                            break;
                        case 5:
                            placeNo = $(this)[0].innerHTML;
                            break;
                        case 6:
                    }
                });
                updateProject(id, absorbance, factor, density, rackNo, placeNo);
            }
        })
        setTimeout(getOneProjectsScalingTime, 1050, paramid);

        // setTimeout(function () {
        //     args = $("#scaling_time input")[0].id;
        //     console.log(args);
        //     setTimeout(updateProjectsScaling, 100, paramid, args);
        // }, 1200);
    });
    $("#scaling_time").on("click", 'input', function () {
        //
        updateProjectsScaling(paramid, $(this)[0].id);
    });
    $("#SC_projectList").on("click", "div", function () {
        $("#QCprojectName").html("当前项目：<br>" + $(this).find("span")[0].innerHTML)
        $("#SC_projectList div").each(function () {
            $(this).removeClass("checkedColor");
        });
        $(this).attr("class", "checkedColor");
    });

    $("#scaling_time").on("click", "li", function () {
        $("#scaling_time li").each(function () {
            $(this).removeClass("checkedColor");
        });
        $(this).attr("class", "checkedColor");
    });

    $("#scalingAlgorithm").on("change", function () {
        console.log($(this)[0].value);
        var $scalingTimeChecked = $("#scaling_time .checkedColor");
        if ($scalingTimeChecked[0] != null) {
            console.log($scalingTimeChecked[0].innerText);
            var dateId = $scalingTimeChecked[0].innerText;
            var Algorithm = $(this)[0].value;
            updateScalingAlgorithm(dateId, Algorithm);
        }
    })
    $("#del").on("click", function () {
        var $scalingTimeChecked = $("#scaling_time .checkedColor");
        if ($scalingTimeChecked[0] != null) {
            console.log($scalingTimeChecked[0].innerText);
            var dateId = $scalingTimeChecked[0].innerText;
            del(dateId, paramid);
        }
        setTimeout(getOneProjectsScalingTime, 1050, paramid);
    });
});

//发送手动检测项目
function sendList() {
    // console.log(projectList.length);
    send(-2, code, 5);
}

