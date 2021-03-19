var doc = new Map();
var urlhead = getPort();



/**
 * 获得所有医生
 */
function getAllDoc() {
    $.ajax({
        type: 'get',
        url: urlhead + '/doctor/queryAll',
        async: true,
        data: {
            // type: type
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            for (var id in data) {
                var $docDiv = $("#div-" + data[id].type + "");
                $docDiv.html("<select ondblclick='select_" + data[id].type + "()' id=" + data[id].type + "><option value='' selected></option></select>");
            }
            for (var id in data) {
                var $docDiv = $("#div-" + data[id].type + " select");
                var html = $docDiv.html();
                html += "<option  value='" + data[id].name + "'>" + data[id].name + "</option>";
                $docDiv.html(html);
            }
            $("#inspection").html("<label>送检医生</label><br>");
            $("#examine").html("<label>检查医生</label><br>");
            $("#test").html("<label>测试医生</label><br>");
            var a = 0;
            var b = 0;
            var c = 0;
            for (var id in data) {
                var type1 = data[id].type;
                var $1;
                switch (type1) {
                    case"examineDoctor":
                        $1 = $("#examine");
                        a++;
                        break;
                    case"testDoctor":
                        $1 = $("#test");
                        b++;
                        break;
                    case"inspectionDoc":
                        $1 = $("#inspection");
                        c++;
                        break;
                }
                var html = $1.html();
                html += "<input type='checkbox' id='delDoc' name='delDoc' value=" + data[id].id + ">" + data[id].name + "<br>";
                $1.html(html);
            }
            var docLength = (a > b ? a : b) > c ? (a > b ? a : b) : c;
            // console.log(docLength);
            $("#deleteDocBox").height((docLength + 1) * 23 + 30);
            $(".doc").height(((docLength + 1) * 23) + 5)
        },
        error: function () {

        }

    });
}

function delDoc(ids) {
    console.log("delete docs" + ids);
    $.ajax({
        type: 'post',
        url: urlhead + '/doctor/deleteByIds',
        async: true,
        data: {
            ids: ids,
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            getAllDoc();

        },
        error: function () {
        }
    });
}

function queryAllPatient(id, code ,testDate) {
    $.ajax({
        type: 'get',
        url: urlhead + '/patient/queryAll',
        async: true,
        data: {
            id: id,
            code: code,
            testDate: testDate
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            window.localStorage.setItem('printValue', JSON.stringify(data));// 插入 对象转字符串
            var elementsByTagName = document.getElementsByTagName("option");
            // alert(elementsByTagName.length);
            for (var i = 0; i < elementsByTagName.length; i++) {
                elementsByTagName[i].removeAttribute("selected");
            }

            var datum = data[0];
            console.log(datum);

            if (datum == null) {
                getProjectsByCon(testDate, id);
                return;
            }
            getProjectsByCon(datum.testDate, datum.id);
            $("#bedNum")[0].value = ((datum == null || datum.bedNum == null || datum.bedNum == "") ? "" : datum.bedNum);
            $("#code")[0].value = datum.code;
            $("#examineDoctor option[value='" + datum.examineDoc + "']").attr("selected", "selected");
            $("#id")[0].value = datum.id;
            $("#inpatientArea")[0].value = datum.inpatientArea;
            $("#inspectionDate")[0].value = datum.inspectionDate == null ? "" : datum.inspectionDate.substring(0, 10);
            $("#inspectionDept")[0].value = datum.inspectionDept;
            $("#inspectionDoc option[value='" + datum.inspectionDoc + "']").attr("selected", "selected");
            $("#name")[0].value = datum.name;
            $("#remark")[0].value = datum.remark;
            $("#sampleNum")[0].value = datum.sampleNum;
            console.log("datum.testDate"+datum.testDate);
            $("#testDate")[0].value = (datum.testDate == null ? "" : datum.testDate.substring(0, 10));
            $("#testDoctor option[value='" + datum.testDoc + "']").attr("selected", "selected");

        },
        error: function () {

        }

    });
}



function getProjectsByCon(starttime, humancode) {
    $.ajax({
        type: 'get',
        url: urlhead + '/patient/getProjectsByCon',
        async: true,
        data: {
            starttime: starttime,
            humancode: humancode
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            window.localStorage.setItem('projectValue', JSON.stringify(data));// 插入 对象转字符串
            // console.log(data)
            $("#projects").children().each(function (i) {
                // $(this).html("1");
                if (i > 0 && data.length > i - 1) {
                    if ($(this).find("input").length === 0) {
                        $(this).html($(this).html() + "<input hidden value=" + data[i - 1].id + ">");

                    } else {
                        var input = $(this).find("input");
                        input.attr("value", data[i - 1].id);

                    }

                    $(this).children().each(function (j) {
                        switch (j) {
                            case 0:
                                $(this).html(j + 1);

                                break;
                            case 1:
                                $(this).html(data[i - 1].chinese_name);
                                break;                                 /* Main
                                case 1:                                     * loop
                                    $(this).html(data[i - 1].chinese_name); * for patient
                                    break;                                  */
                            case 2:
                                $(this).html(data[i - 1].name);
                                break;
                            case 3:
                                var density = data[i - 1].density;
                                if (density <= 0.5) {
                                    density = "≤0.5";
                                }
                                $(this).html(density);
                                break;
                            case 4:
                                $(this).html(data[i - 1].meterage_unit);
                                break;
                            case 5:
                                var mark = "";
                                if (data[i - 1].density > data[i - 1].normal_high / 100)
                                    mark = "↑";
                                if (data[i - 1].density < data[i - 1].normal_high / 100)
                                    mark = "↓";
                                $(this).html(mark);
                                break;
                            case 6:
                                $(this).html(data[i - 1].normal_low / 100 + "-" + data[i - 1].normal_high / 100);
                                break;
                            default:
                                break;
                        }
                    })
                } else if (i > 0) {
                    if (data[i - 1] == null) {
                        $(this).children().each(function (j) {
                            switch (j) {
                                case 0:
                                    $(this).html("");
                                    break;
                                case 1:
                                    $(this).html("");
                                    break;
                                case 2:
                                    $(this).html("");
                                    break;
                                case 3:
                                    $(this).html("");
                                    break;
                                case 4:
                                    $(this).html("");
                                    break;
                                case 5:
                                    $(this).html("");
                                    break;
                                case 6:
                                    $(this).html("");
                                    break;
                                default:
                                    break;
                            }
                        });
                    }
                }
            })
        },
        error: function () {
            $("#projects").children().each(function (i) {
                    // $(this).html("1");
                    if (i > 0) {
                        $(this).children().each(function (j) {
                            switch (j) {
                                case 0:
                                    $(this).html("");
                                    break;
                                case 1:
                                    $(this).html("");
                                    break;
                                case 2:
                                    $(this).html("");
                                    break;
                                case 3:
                                    $(this).html("");
                                    break;
                                case 4:
                                    $(this).html("");
                                    break;
                                case 5:
                                    $(this).html("");
                                    break;
                                case 6:
                                    $(this).html("");
                                    break;
                                default:
                                    break;
                            }
                        });
                    }
                }
            )
        }

    });
}

function insertDoc(value, type) {
    $.ajax({
        type: 'post',
        url: urlhead + '/doctor/insert',
        async: true,
        data: {
            name: value,
            type: type
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            getAllDoc();
        },
        error: function () {
        }
    });
}


function saveDoctor(value, id) {

    if (value === "") {
        var message = $("#input-" + id).parent();
        message.html(doc.get(id));
    } else {
        insertDoc(value, id);

    }

}

function select_examineDoctor() {
    var $parent = $("#examineDoctor").parent();
    doc.set("examineDoctor", $parent.html());
    var inputStr = "<input id='input-examineDoctor' placeholder='请输入审核医生'  ondblclick='saveDoctor(value,\"examineDoctor\")' >";
    $parent.html(inputStr);
}

function select_testDoctor() {
    var $parent = $("#testDoctor").parent();
    doc.set("testDoctor", $parent.html());
    var inputStr = "<input id='input-testDoctor' placeholder='请输入审核医生'  ondblclick='saveDoctor(value,\"testDoctor\")' >";
    $parent.html(inputStr);
}

function select_inspectionDoc() {
    var $parent = $("#inspectionDoc").parent();
    doc.set("inspectionDoc", $parent.html());
    var inputStr = "<input id='input-inspectionDoc' placeholder='请输入审核医生'  ondblclick='saveDoctor(value,\"inspectionDoc\")' >";
    $parent.html(inputStr);
}


$(document).ready(function () {
    var timeout;
    $("#query").click(function () {
        queryAllPatient($("#id")[0].value, $("#code")[0].value ,$("#testDate")[0].value);
    });
    var MM = new Date().getUTCMonth() + 1;
    if (MM < 10) {
        MM = "0" + MM;
    }
    var dd = new Date().getDate();
    if (dd < 10) {
        dd = "0" + dd;
    }
    $("#testDate").attr("value", new Date().getFullYear() + "-" + (MM) + "-" + dd);
    $("#inspectionDate").attr("value", new Date().getFullYear() + "-" + (MM) + "-" + dd);

    $("#deleteDoc").on('click', function () {
        var deleteDocBox = $("#deleteDocBox");
        deleteDocBox.find("#updateBox").attr("place", $(this).attr("place"));
        deleteDocBox.fadeIn("slow");
    });
    $("#close_btn").hover(function () {
        $(this).css({color: 'black'})
    }, function () {
        $(this).css({color: '#999'})
    }).on('click', function () {
        $("#deleteDocBox").fadeOut("fast");
        $("#mask").css({display: 'none'});
    });
    $("#updateBox").on("click", function () {
        var delDocs = [];
        $("input[name='delDoc']:checked").each(function () {
            delDocs.push($(this).val());
        });
        delDoc(delDocs.toString());
    });
    $("#printf").on('click', function () {
        window.open("./print.html", '打印', 'height=600, width=900, top=0,left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
    });
    $("#updatePatient").on('click', function () {
        if ("" === $("#id")[0].value || null == $("#id")[0].value) {
            alert("请先查询样本信息后再进行修改？？？");
            return
        }
        $.ajax({
            type: 'get',
            url: urlhead + '/patient/update',
            async: true,
            data: {
                id: $("#id")[0].value,
                code: $("#code")[0].value,
                name: $("#name")[0].value,
                sex: $("#sex")[0].value,
                inpatientArea: $("#inpatientArea")[0].value,
                bedNum: $("#bedNum")[0].value,
                sampleNum: $("#sampleNum")[0].value,
                sampleType: $("#sampleType")[0].value,
                inspectionDept: $("#inspectionDept")[0].value,
                inspectionDoc: $("#inspectionDoc")[0].value,
                inspectionDate: $("#inspectionDate")[0].value,
                testDoc: $("#testDoctor")[0].value,
                testDate: $("#testDate")[0].value,
                examineDoc: $("#examineDoctor")[0].value,
                remark: $("#remark")[0].value,
                // type: type
            },
            jsonp: 'jsoncallback',
            success: function (data) {
                queryAllPatient($("#id")[0].value, $("#code")[0].value ,$("#testDate")[0].value);
            },
            error: function () {

            }

        });

    });
//监听项目列表并弹出曲线窗口
    $(".layui-form-item").on("click", function () {
        var jInput = $(this).find("input")[0];
        if (jInput == null) {
            return;
        }
        $.ajax({
                type: 'get',
                url: urlhead + '/productTest/selectCurveById',
                async: true,
                data: {
                    id: jInput.value,
                },
                jsonp: 'jsoncallback',
                success: function (data) {
                    var canvasDiv = $("#canvasDiv");
                    canvasDiv.html("<canvas id='canvas' width='260' height='250' style='float: left'></canvas><div id='curves' style='float: left;height: 250px;overflow: scroll'  ></div>");
                    canvasDiv.fadeIn("slow");
                    var canvas = canvasDiv.find("#canvas")[0];
                    var ctx = canvas.getContext('2d');
                    ctx.clearRect(0, 0, 2000, 2000);
                    ctx.stroke();
                    console.log(data);
                    var curveDiv = "";
                    $.each(data, function (i, project) {
                        var ctx3 = canvas.getContext('2d');
                        ctx3.strokeStyle = "red";
                        ctx3.lineWidth = 1;
                        ctx3.lineTo(project.x + 30, 210 - project.y);
                        var number = parseInt((data.length - 1) / 7);
                        if ((i % number === 0 && i!==0 && data.length-1-i >= number) || i===1 || i===data.length-1) {
                            var ctx4 = canvas.getContext('2d');

                            ctx4.strokeText(i, 200 / (data.length - 1) * i+15, 242);
                        }
                        if (i !== data.length-1) {

                            curveDiv += "<div>"+(i+1)+ ": "+project.abs+"</div>";
                        }
                    });
                    var ctx2 = canvas.getContext('2d');
                    ctx2.strokeText(data[data.length - 1].min, 0, 210);
                    ctx2.strokeText(data[data.length - 1].max, 0, 222 - data[data.length - 1].maxY);
                    // ctx2.strokeText(data.length, 219, 212);
                    ctx2.stroke();
                    console.log(curveDiv);
                    var curve = $("#curves").html(curveDiv);


                },
                error: function () {

                }

            }
        )
    });
//监听body 关闭曲线窗口
    $("body").on("click", function (e) {

        if ($(e.target).attr('id') != 'canvas') {
            var canvasDiv = $("#canvasDiv");
            canvasDiv.fadeOut("fast");
        } else {
        }
    });

});

window.onload = function () {
    getAllDoc();
};
