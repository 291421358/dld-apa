    function showPrint(){
    var datum = JSON.parse(window.localStorage.getItem('printValue'))[0];

    $("#bedNum").html(             datum.bedNum        );
    $("#id").html(                 datum.id            );
    $("#sex").html(                 datum.sex            );
    $("#paramid").html(                 datum.id            );
    $("#inpatientArea").html(      datum.inpatientArea );
    $("#inspectionDate").html(     datum.inspectionDate==null ? "" : datum.inspectionDate.substring(0,10) ) ;
    $("#inspectionDept").html(     datum.inspectionDept);
    $("#name").html(               datum.name          )   ;
    $("#remark").html(             datum.remark        );
    $("#sampleNum").html(          datum.sampleNum     );
    $("#sampleType").html(          datum.sampleType     );
    $("#testDate").html(           datum.testDate==null ? "" : datum.testDate.substring(0,10));
    console.log(window.localStorage.getItem('printValue'));
    $("#testDoc1").html(          datum.testDoc     );
    $("#testDoc").html(          datum.testDoc     );
    $("#examineDoc").html(          datum.examineDoc     );
    var projectValue = JSON.parse(window.localStorage.getItem('projectValue'));

    $("#projects").children().each(function (i) {
    console.log(i);
    // $(this).html("1");
    if (i > 0 && projectValue.length > i-1){
    $(this).children().each(function (j) {
    switch (j) {
    case 0:
    $(this).html(j+1);
    break;``
    case 1:
    $(this).html(projectValue[i-1].chinese_name);
    break;
    case 2:
    $(this).html(projectValue[i-1].name);
    break;
    case 3:
    var density = projectValue[i - 1].density;
    if (density <= 0.5){
    density = "≤0.5";
}
    $(this).html(density);
    break;
    case 4:
    $(this).html(projectValue[i-1].meterage_unit);
    break;
    case 5:
    var mark ="";
    if (projectValue[i-1].density >projectValue[i-1].normal_high/100)
    mark="↑";
    if (projectValue[i-1].density <projectValue[i-1].normal_high/100)
    mark="↓";
    $(this).html(mark);
    break;
    case 6:
    $(this).html(projectValue[i-1].normal_low/100 +"-"+projectValue[i-1].normal_high/100);
    break;
    default:
    break;
}
})
}
})
    console.log(datum.id+"printValue");
    // $("#patientInfo").html(printValue[0].id);
}
    function printAndClose() {
    window.print(); // 调用打印方法
    setTimeout(cancel, 1000);// cancel是调用的方法，不需要括号，延时1秒
}
    // 关闭打印页面
    function cancel() {
    // window.close();
}