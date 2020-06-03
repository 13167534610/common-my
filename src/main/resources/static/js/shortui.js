var dragDiv = $(".shortui-drag");
var dragFlag = false;
var x = 0;//鼠标按下时x轴距离
var y = 0;//鼠标按下时y轴距离
var vleft = 0;//元素当前x轴距离
var vtop = 0;//元素当前y轴距离
var w = 200;
var h = 300;
dragDiv.mousedown(function (e) {
    dragDiv = $(this);
    if (!window.isNaN(dragDiv.attr("drag-area-width"))){
        w = parseInt(dragDiv.attr("drag-area-width"));
    }
    if (!window.isNaN(dragDiv.attr("drag-area-width"))){
        h = parseInt(dragDiv.attr("drag-area-height"));
    }
    x = e.pageX;
    y = e.pageY;
    vleft = dragDiv.position().left;
    vtop = dragDiv.position().top;
    /*console.log("w: "+w);
    console.log("h: "+h);
    console.log("x: "+x);
    console.log("y: "+y);
    console.log("vleft: "+vleft);
    console.log("vtop: "+vtop);
    console.log("sumX: "+(vleft + w));
    console.log("sumY: "+(vtop + h));*/
    if (x > vleft && x < (vleft + w) && y > vtop && y < (vtop + h)){
        dragFlag = true;
        dragDiv.css("cursor","move");
    }
});
dragDiv.mouseup(function () {
    dragFlag = false;
    dragDiv.css("cursor","default");
});
$(document).mousemove(function (e) {
    if(dragFlag == false)return;
    //获取x和y
    var mx = e.pageX - x;//鼠标移动x轴距离
    var my = e.pageY - y;//鼠标移动x轴距离
    //计算移动后的左偏移量和顶部的偏移量
    var nl = mx + vleft;
    var nt = my + vtop;
    dragDiv.css("left",nl + 'px');
    dragDiv.css("top",nt + 'px');
});

