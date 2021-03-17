var urlAH = window.location.host;
var domain = urlAH.split('/'); //以“/”进行分割

if( domain[2] ) {
    domain = domain[2];
} else {
    domain = ''; //如果url不正确就取空
}
var urlhead = domain;

function getPort(){
    return urlhead;
}
/**
<
div class="layui-form-item layui-row">部件检测区</div>
 <div class="layui-row">
 <div class="layui-col-md6" >
 <div class="layui-form-item item" >
 <label class="layui-form-label">试剂盘</label>
 <div class="layui-form-item" style="text-align: center;border: white  ">
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="试剂盘归零">
 </div>
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="转到试剂位">
 <input type="text" title="11" value="11" style="width: 100px;border: #B2B2B2">
 </div>
 </div>
 </div>
 <div class="layui-form-item item">
 <label class="layui-form-label">样本盘</label>
 <div class="layui-form-item" style="text-align: center">
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="样本盘归零">
 </div>
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="转到样本盘">
 <input type="text" title="11" value="11" style="width: 100px;border: #B2B2B2">
 </div>
 </div>
 </div>
 <div class="layui-form-item item">
 <label class="layui-form-label">反应盘</label>
 <div class="layui-form-item" style="text-align: center">
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="反应盘归零">
 </div>
 <div class="layui-form-item">
 <input type="button"  class="layui-btn layui-btn-primary" value="转到反应盘">
 <input type="text" title="11" value="11" style="width: 100px;border: #B2B2B2">
 </div>
 </div>
 </div>
 </div>
 <div class="layui-col-md6">
 <div class="layui-form-item item">
 <label class="layui-form-label">试剂注射器</label>
 <div class="layui-form-item" style="text-align: center">
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="注射器归零">
 </div>
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="吸取试剂量">
 <input type="text" title="11" value="11" style="width: 100px;border: #B2B2B2">
 </div>
 </div>
 </div>
 <div class="layui-form-item item">
 <label class="layui-form-label">样本注射器</label>
 <div class="layui-form-item" style="text-align: center">
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="注射器归零">
 </div>
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="吸取样本量">
 <input type="text" title="11" value="11" style="width: 100px;border: #B2B2B2">
 </div>
 </div>
 </div>
 <div class="layui-form-item item" style="text-align: center">
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="试管进一位">

 </div>
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="吸样架归零">
 </div>
 <div class="layui-form-item">
 <input type="button" class="layui-btn layui-btn-primary" value="初&nbsp;&nbsp;&nbsp;&nbsp;始&nbsp;&nbsp;&nbsp;化">
 </div>
 </div>
 </div>
 </div>

 $(document).ready(function () {
        var $item = $(".item");
        $item.mouseover(function () {
            $(this).css("background-color","#83c7f2");
            $(this).css("border","#8387b2 2px solid");
        });

        $item.mouseout(function () {
            $(this).css("background","none");
            $(this).css("border","#ffffff 2px solid");
        })
    })
**/