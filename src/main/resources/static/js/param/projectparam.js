/**
 * 初始化方法；加载项目列表
 */

var availableTags =  new Array();
function load() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/projectList',
        async: true,
        jsonp: 'jsoncallback',
        success: function (data) {
            var projectList = data.nameMap;
            var reagentPlace = data.reagentPlace;
            var reagentPlacelist = "<div class='bottom-btn'>";
            $.each(reagentPlace, function (i, project) {
                var name = '';
                for (let j = 0; j <projectList.length-1 ; j++) {
                  if (projectList[i].id = project.project_param_id){
                      name = projectList[i].name;
                  }
                }
                reagentPlacelist += "<button class='btn-project' type='button' style='' " +
                    "paramid='" + project.project_param_id +
                    "' onclick='onePoject(" + project.project_param_id + ")'>" +
                    name +
                    "</button>";
            });
            reagentPlacelist += "</div>";
            $('#placeList').html(reagentPlacelist);
            var tabStr = "<div class='bottom-btn'>";
            $.each(projectList, function (i, project) {
                var a = new Map;
                // a.set("label",);
                availableTags[i] = {};
                availableTags[i].label = projectList[i].id+"-"+projectList[i].name;
                availableTags[i].id = projectList[i].id;
                // $('#info').html(project);
                var name = project.name;
                if (name == '') {
                    name = project.id;
                }
                tabStr += "<button class='btn-project' type='button' style='' " +
                    "paramid='" + project.id +
                    "' onclick='onePoject(" + project.id + ")'>" +
                    name +
                    "</button>";
            });
            console.log(availableTags)
            // $('#info').html(tabStr);

        },
        error: function () {
            $('#info').html("请联系管理员");
        }
    });
}

/**
 * 查询项目详情。
 * @param id
 */
function onePoject(id) {
    if (id ===0 ){
        return;
    }
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/oneProject',
        async: true,
        data: {
            id: id
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            var elementsByTagName = document.getElementsByTagName("option");
            // alert(elementsByTagName.length);
            for (var i = 0; i < elementsByTagName.length; i++) {
                elementsByTagName[i].removeAttribute("selected");
            }
            for (var key in data) {
                var datum = data['' + key];
                var $1 = $('#' + key);
                $1.attr("value", datum);
                if (null != $1[0]) {
                    if ($1[0].tagName == "SELECT") {
                        // alert(datum);
                        $("#" + key + " option[value='" + datum + "']").attr("selected", "selected");
                    }
                }
            }
        },
        error: function () {
            $('#details').html("有问题");
        }
    });
};


/**
 * 修改项目详情。
 * @param id
 */
function updateProjectParam() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/update',
        async: true,
        data: {
            id: $("#id").val(),
            name: $("#name").val(),
            chineseName: $("#chineseName").val(),
            computeMethod: $("#computeMethod").val(),
            sampleSize: $("#sampleSize").val(),
            sampleType: $("#sampleType").val(),
            reagentQuantityNo1: $("#reagentQuantityNo1").val(),
            reagentQuantityNo2: $("#reagentQuantityNo2").val(),
            mainWavelength: $("#mainWavelength").val(),
            auxiliaryWavelength: $("#auxiliaryWavelength").val(),
            decimalDigit: $("#decimalDigit").val(),
            meterageUnit: $("#meterageUnit").val(),
            minAbsorbance: $("#minAbsorbance").val(),
            maxAbsorbance: $("#maxAbsorbance").val(),
            dilutionMultiple: $("#dilutionMultiple").val(),
            dilutionPosition: $("#dilutionPosition").val(),
            mainIndicationBegin: $("#mainIndicationBegin").val(),
            mainIndicationEnd: $("#mainIndicationEnd").val(),
            auxiliaryIndicationBegin: $("#auxiliaryIndicationBegin").val(),
            auxiliaryIndicationEnd: $("#auxiliaryIndicationEnd").val(),
            normalLow: $("#normalLow").val(),
            normalHigh: $("#normalHigh").val(),
            modifiedFormulaA: $("#modifiedFormulaA").val(),
            modifiedFormulaB: $("#modifiedFormulaB").val(),
            otherModifiedFormulaA: $("#otherModifiedFormulaA").val(),
            otherModifiedFormulaB: $("#otherModifiedFormulaB").val(),
            dilutionDelayPeriod: $("#dilutionDelayPeriod").val(),
            diluent_place: $("#diluent_place").val(),
            diluent_size: $("#diluent_size").val(),
            dilution_sample_size: $("#dilution_sample_size").val(),
        },
        jsonp: 'jsoncallback',
        success: function (data) {
            load();
            onePoject($("#id").val());
            document.getElementById("frm1").reset();
        },
        error: function () {
            load();
            $('#details').html("修改失败");
        }
    });
};

/**
 * 添加项目参数
 * 
 */
function  insertProject() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/insert',
        async: true,
        data: {
            id: $("#id").val(),
            name: $("#name").val(),
            chineseName: $("#chineseName").val(),
            computeMethod: $("#computeMethod").val(),
            sampleSize: $("#sampleSize").val(),
            sampleType: $("#sampleType").val(),
            reagentQuantityNo1: $("#reagentQuantityNo1").val(),
            reagentQuantityNo2: $("#reagentQuantityNo2").val(),
            mainWavelength: $("#mainWavelength").val(),
            auxiliaryWavelength: $("#auxiliaryWavelength").val(),
            decimalDigit: $("#decimalDigit").val(),
            meterageUnit: $("#meterageUnit").val(),
            minAbsorbance: $("#minAbsorbance").val(),
            maxAbsorbance: $("#maxAbsorbance").val(),
            dilutionMultiple: $("#dilutionMultiple").val(),
            dilutionPosition: $("#dilutionPosition").val(),
            mainIndicationBegin: $("#mainIndicationBegin").val(),
            mainIndicationEnd: $("#mainIndicationEnd").val(),
            auxiliaryIndicationBegin: $("#auxiliaryIndicationBegin").val(),
            auxiliaryIndicationEnd: $("#auxiliaryIndicationEnd").val(),
            normalLow: $("#normalLow").val(),
            normalHigh: $("#normalHigh").val(),
            modifiedFormulaA: $("#modifiedFormulaA").val(),
            modifiedFormulaB: $("#modifiedFormulaB").val(),
            otherModifiedFormulaA: $("#otherModifiedFormulaA").val(),
            otherModifiedFormulaB: $("#otherModifiedFormulaB").val(),
            dilutionDelayPeriod: $("#dilutionDelayPeriod").val(),
            diluent_place: $("#diluent_place").val(),
            diluent_size: $("#diluent_size").val(),
            dilution_sample_size: $("#dilution_sample_size").val(),

        },
        jsonp: 'jsoncallback',
        success: function (data) {
            load();
            onePoject($("#id").val());
            document.getElementById("frm1").reset();
        },
        error: function () {
            load();
            $('#details').html("添加失败");
        }
    });
}
/**
 * 通过二维码
 * 码修改项目详情。
 * @param id
 */
function updateProjectQR() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/update',
        async: true,
        data: {
            id: $("#id").val(),
            name: $("#QR_name").val(),
            chineseName: $("#QR_chineseName").val(),
            computeMethod: $("#QR_computeMethod").val(),
            sampleSize: $("#QR_sampleSize").val(),
            sampleType: $("#QR_sampleType").val(),
            reagentQuantityNo1: $("#QR_reagentQuantityNo1").val(),
            reagentQuantityNo2: $("#QR_reagentQuantityNo2").val(),
            mainWavelength: $("#QR_mainWavelength").val(),
            auxiliaryWavelength: $("#QR_auxiliaryWavelength").val(),
            decimalDigit: $("#QR_decimalDigit").val(),
            meterageUnit: $("#QR_meterageUnit").val(),
            minAbsorbance: $("#QR_minAbsorbance").val(),
            maxAbsorbance: $("#QR_maxAbsorbance").val(),
            dilutionMultiple: $("#QR_dilutionMultiple").val(),
            dilutionPosition: $("#QR_dilutionPosition").val(),
            mainIndicationBegin: $("#QR_mainIndicationBegin").val(),
            mainIndicationEnd: $("#QR_mainIndicationEnd").val(),
            auxiliaryIndicationBegin: $("#QR_auxiliaryIndicationBegin").val(),
            auxiliaryIndicationEnd: $("#QR_auxiliaryIndicationEnd").val(),
            normalLow: $("#QR_normalLow").val(),
            normalHigh: $("#QR_normalHigh").val(),
            modifiedFormulaA: $("#QR_modifiedFormulaA").val(),
            modifiedFormulaB: $("#QR_modifiedFormulaB").val(),
            otherModifiedFormulaA: $("#QR_otherModifiedFormulaA").val(),
            otherModifiedFormulaB: $("#QR_otherModifiedFormulaB").val(),
            dilutionDelayPeriod: $("#QR_dilutionDelayPeriod").val()

        },
        jsonp: 'jsoncallback',
        success: function (data) {
            var text = $("#QRCode");
            text.fadeOut("fast");
            load();
            onePoject($("#id").val());
            document.getElementById("frm1").reset();

        },
        error: function () {
            load();
            $('#details').html("有问题");
        }
    });
};

$(function () {

    $("#keyword").autocomplete({
        source: availableTags,
        select: function( event, ui ) {
            onePoject(ui.item.id)
            // alert(ui.item.value)
        },
    });
});
$(document).ready(function () {
    //{'param':[{'pn':'ARU','me':'2','sas':'5','sat':'2','ftd':'300','std':'0','mw':'4','aw':'7','u':'2','mia':'1','maa':'3','spm':'1','epm':'18','spa':'0','epa':'0','nh':'40','nl':'0'}]}


    $('#text').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            event.preventDefault();
            var QRCodeStr = this.value;//.replaceAll("'","\"");
            var QRCodeJsonStr = eval('(' + QRCodeStr + ')');//QRCodeStr.parseJSON();
            var param = QRCodeJsonStr.z[0];
            var scal = QRCodeJsonStr.t;

            $('#QR_name').attr("value", param.b); //QRCodeStr.substring(36,39));
            $('#QR_chineseName').attr("value", param.a); //QRCodeStr.substring(36,39));
            $('#QR_sampleSize').attr("value", param.d)//QRCodeStr.substring(39,40));
            var $QRComputeMethod = $('#QR_computeMethod');
            var computeMethod = $QRComputeMethod.find(".qr" + param.c)//QRCodeStr.substring(41,42));
            computeMethod.prop("selected", 'selected');

            var $1 = $('#QR_sampleType');
            var message = $1.find(".qr" + param.e);//QRCodeStr.substring(41,42));
            message.prop("selected", 'selected');

            $('#QR_reagentQuantityNo1').attr("value", param.f);//QRCodeStr.substring(42,45));
            $('#QR_reagentQuantityNo2').attr("value", param.g);//QRCodeStr.substring(46,47));

            $('#QR_mainWavelength option[value=' + param.h + ']').prop("selected", 'selected');//QRCodeStr.substring(46,47)

            $('#QR_auxiliaryWavelength option[value=' + param.i + ']').prop("selected", 'selected');//QRCodeStr.substring(49,50)

            var $decimalDigit = $('#QR_decimalDigit');
            var ecimalDigit = $decimalDigit.find(".qr" + param.s);//QRCodeStr.substring(52,53));
            ecimalDigit.prop("selected", 'selected');
            $("#QR_meterageUnit option[value='" + param.j + "']").attr("selected", "selected");

            $('#QR_minAbsorbance').attr("value", param.k);//QRCodeStr.substring(56,57));
            $('#QR_maxAbsorbance').attr("value", param.l);//QRCodeStr.substring(57,58));
            $('#QR_mainIndicationBegin').attr("value", param.m);//QRCodeStr.substring(58,59));
            $('#QR_mainIndicationEnd').attr("value", param.n);//QRCodeStr.substring(59,61));
            $('#QR_auxiliaryIndicationBegin').attr("value", param.o);//QRCodeStr.substring(61,62));
            $('#QR_auxiliaryIndicationEnd').attr("value", param.p);//QRCodeStr.substring(62,63));
            $('#QR_normalHigh').attr("value", param.q);//QRCodeStr.substring(63,65));
            $('#QR_normalLow').attr("value", param.r);//QRCodeStr.substring(65,67));
            $('#QRCode').fadeIn('slow');

            //加载定标

            $('#scaling_deal tr').each(function (i) {
                // console.log(i);
                if (i > 0) {
                    $(this).find("td").each(function (j) {
                        switch (j) {
                            case 0:
                                $(this).html(i);
                                break;
                            case 1:
                                $(this).html(scal[(i - 1) * 2]);
                                break;
                            case 2:
                                break;
                            case 3:
                                $(this).html(scal[(i - 1) * 2 + 1]);
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                        }
                    });
                }
            });
            var a = QRCodeJsonStr.u;
            switch (a) {
                case  "1":
                    a = "三次样条函数";
                    break;
                case  "2":
                    a = "一次曲线";
                    break;
                case "3":
                    a = "二次曲线拟合";
                    break;
                case "4":
                    a = "Logistic-4p";
            }
            $("#a").html(a);

            //回车执行查询
            $('#button').click();
        }
    });


    $("body").bind('keydown', function (event) {
        if (event.keyCode == "27") {
            var text = $("#QRCode");
            text.fadeOut("fast");
        } else {
        }
    });

    $("#createQRCode").click(function () {
        createQRCode();
    })
});

function toUnicode(arr) {

    var strArr = [];
    for (var i = 0; i < arr.length; i++) {
        strArr.push(arr[i].title);
    }
    var str = strArr.join(',');

    function isChinese(s) {
        return /[\u4e00-\u9fa5]/.test(s);
    }

    var aa = ch2Unicdoe(str);
    var bb = aa.split(',')
    for (var i = 0; i < arr.length; i++) {
        for (var j = 0; j < bb.length; j++) {
            arr[i].title = bb[i];
        }
    }
    return arr;
}

function ch2Unicdoe(str) {
    if (!str) {
        return;
    }
    var unicode = '';
    for (var i = 0; i < str.length; i++) {
        var temp = str.charAt(i);
        if (isChinese(temp)) {
            unicode += '\\u' + temp.charCodeAt(0).toString(16);
        }
        else {
            unicode += temp;
        }
    }
    return unicode;
}

function createQRCode() {
    $.ajax({
        type: 'get',
        url: urlhead + '/parameter/createQRCode',
        async: true,
        data: {
            id: $("#id").val(),
            total:$("#total").val()
        },
        jsonp: 'jsoncallback',
        success: function (data) {
        },
        error: function () {
        }
    });
}


//保存手动测试项目
function saveList() {
    var projectList = [];
    // 遍历 tr
    $('#scaling_deal tr').each(function (i) {
        var project = {};
        // 遍历 tr 的各个 td
        $(this).find('td').each(function (j) {
            if (i > 0) {
                switch (j) {
                    case 0:
                        $(this).html(i);
                        break;
                    case 1:
                        console.log($(this));

                        project.absorbance = $(this)[0].innerHTML;
                        break;
                    case 2:
                        break;
                    case 3:
                        console.log($(this));

                        project.density = $(this)[0].innerHTML;
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                }
            }
        });

        project.projectParamId = $("#id").val();
        project.type = 2;
        var myDate = new Date();
        project.endtime = myDate.toLocaleDateString('chinese', {hour12: false});
        if (project.density != null && project.density !== "") {
            projectList.push(project);
        }

    });
    saveProjectList(projectList);

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

function QRCommit() {
    var id = $("#id").val();
    if (null == id || "" == id) {
        alert("请选择项目");
        return;
    }
    updateProjectQR();
    saveList();
    var a = $("#a")[0].innerText;
    if (a == "Logistic-4p") {
        a = "RodBard";
    }

    insertScalingAlgorithm(a);
}