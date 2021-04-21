function showPrint(){
    var datum = JSON.parse(window.localStorage.getItem('qcProjectValue'));
    console.log(datum)
    var sta = datum[datum.length - 1].sta;
    $("#name").html(             datum.bedNum        );
    $("#target").html(                 datum.id            );
    $("#average").html(                 sta           );
    $("#standard").html(                 datum.id            );
    var projectValue = JSON.parse(window.localStorage.getItem('qcProjectValue'));
    console.log(projectValue);
    $("#projects").children().each(function (i) {
        console.log(i);
        // $(this).html("1");
        if (i > 0 && projectValue.length > i){
            $(this).children().each(function (j) {
                switch (j) {
                    case 0:
                        $(this).html(projectValue[i-1].date.substring(3));
                        break;
                    case 1:
                        $(this).html(projectValue[i-1].density);
                        break;
                    case 2:
                        if (projectValue[i+14] === undefined){
                            break;
                        }
                        $(this).html(projectValue[i+14].date.substring(3));
                        break;
                    case 3:
                        if (projectValue[i+14] === undefined){
                            break;
                        }
                        $(this).html(projectValue[i + 14].density);
                        break;
                    default:
                        break;
                }
            })
        }
    });
    console.log(datum.id+"printValue");
    // $("#patientInfo").html(printValue[0].id);



    var canvas = document.getElementById('qcCanvas');
    //  计算画布的宽度
    var width = canvas.offsetWidth/10;
        //  计算画布的高度
    var height = canvas.offsetHeight/10;

    var ctx = canvas.getContext('2d');
    canvas.width = width;
    canvas.height = height;
    // console.log(projectList);
    ctx.clearRect(0, 0, 2000, 2000);
    var ctx1 = canvas.getContext('2d');
    ctx1.lineWidth = 1.5;
    ctx1.strokeStyle = "black";
    ctx1.beginPath();
    for (var j = 0; j < projectValue.length - 1; j++) {
        ctx1.lineTo((projectValue.length - j) * canvas.height/30 + 1, canvas.height / 2 - projectValue[j].proportionateDensity);
        ctx1.lineTo((projectValue.length - j) * canvas.height/30 + 1, canvas.height / 2 - projectValue[j].proportionateDensity - 1);
        ctx1.lineTo((projectValue.length - j) * canvas.height/30 + 1, canvas.height / 2 - projectValue[j].proportionateDensity + 1);
        ctx1.lineTo((projectValue.length - j) * canvas.height/30 + 1, canvas.height / 2 - projectValue[j].proportionateDensity);
    }
    ctx.strokeStyle="#000";
    ctx1.stroke();
}
function printAndClose() {
    window.print(); // 调用打印方法
    setTimeout(cancel, 1000);// cancel是调用的方法，不需要括号，延时1秒
}
// 关闭打印页面
function cancel() {
    // window.close();
}