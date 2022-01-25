$(function(){

    $("ul.tabs li").click(function(){
        var tabId = $(this).attr("data-tab");

        $("ul.tabs li").removeClass("current");
        $(".tab-content").removeClass("current");

        $(this).addClass("current");
        $("#"+ tabId).addClass("current");
    })

})