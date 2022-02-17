$("ul.tabs li").click(function(){
    var tabId = $(this).attr("data-tab");

    $("ul.tabs li").removeClass("current");
    $(".tab-content").removeClass("current");

    $(this).addClass("current");
    $("#"+ tabId).addClass("current");
});


// 버튼 클릭 이벤트
function adminSelectBtn(){


    $(".admin-chart-content").remove();
    var adminHTML = "<div class='admin-chart-content'>";
    adminHTML += "<div id='chartdiv'> </div>";
    adminHTML += "<div>";
    $(".admin-chart-wrapper").html(adminHTML);


    var year = $("#admin-chart-year").val();
    var month = $("#admin-chart-month").val();
    var day = $("#admin-chart-day").val();
    var category = $("#admin-chart-category").val();
    var local = $("#admin-chart-local").val();

    alert(year + ", " + month);

    year = isEmpty(year, -1);
    month = isEmpty(month, -1);
    day = isEmpty(day, -1);

    category = isEmpty(category, -1);
    local = isEmpty(local, -1);

    // 어떤 값이 선택되었는지에 따라서 각각 다른 차트를 출력해야한다.

    // 1. 아무것도 선택하지 않은 경우
        // 1. 전체 데이터를 올해 기준으로 '달 별' 데이터를 출력한다.
        // 2. 출력되는 데이터는 개설된 강좌 현황입니다.
    if(year == "-1" && month == "-1" && day == "-1" && category == "-1" && local == "-1"){
        $.ajax({
                crossDomain: true,
                url: "/admin/roomJSON",
                data: {"year" : year, "month" : month, "day" : day, "category" : category, "local" : local},
                contentType: "application/json; charset=utf-8",
                method: "GET",
                dataType : "json",
                async : false,
                success: function(data){
                    $(".admin-chart-wrapper").prepend("<div> 공방 개설 현황 (단위 : 달) </div>");
                    console.log(JSON.stringify(data));

                    // 달별 데이터
                    var jan = 0;
                    var feb = 0;
                    var mar = 0;
                    var apr = 0;
                    var may = 0;
                    var jun = 0;
                    var jul = 0;
                    var aug = 0;
                    var sep = 0;
                    var oct = 0;
                    var nov = 0;
                    var dec = 0;

                    var bundleData = $(data.history).map(function(i, history) {
                        // 1. 강의 개설 날짜
                        var date = history.date;
                        console.log(date);

                        // 2. 강의 개설 '월'
                            // 1. 각각 월에 값을 더해서 차트로 출력하기 위함
                        var month = date.split("-")[1];
                        console.log(month);

                        switch(month){
                            case "1":
                                jan = jan + 1;
                                break;
                            case "2":
                                feb = feb + 1;
                                break;
                            case "3":
                                mar = mar + 1;
                                break;
                            case "4":
                                apr = apr + 1;
                                break;
                            case "5":
                                may = may + 1;
                                break;
                            case "6":
                                jun = jun + 1;
                                break;
                            case "7":
                                jul = jul + 1;
                                break;
                            case "8":
                                aug = aug + 1;
                                break;
                            case "9":
                                sep = sep + 1;
                                break;
                            case "10":
                                oct = oct + 1;
                                break;
                            case "11":
                                nov = nov + 1;
                                break;
                            case "12":
                                dec = dec + 1;
                                break;
                        }
                        var category = history.category;
                    });


                    am5.ready(function() {

                        // Create root element
                        // https://www.amcharts.com/docs/v5/getting-started/#Root_element
                        var root = am5.Root.new("chartdiv");

                        // Set themes
                        // https://www.amcharts.com/docs/v5/concepts/themes/
                        root.setThemes([
                            am5themes_Animated.new(root)
                        ]);


                        // Create chart
                        // https://www.amcharts.com/docs/v5/charts/xy-chart/
                        var chart = root.container.children.push(am5xy.XYChart.new(root, {
                            panX: true,
                            panY: true,
                            wheelX: "panX",
                            wheelY: "zoomX"
                        }));

                        // Add cursor
                        // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
                        var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
                        cursor.lineY.set("visible", false);


                        // Create axes
                        // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
                        var xRenderer = am5xy.AxisRendererX.new(root, { minGridDistance: 30 });
                        xRenderer.labels.template.setAll({
                            rotation: -90,
                            centerY: am5.p50,
                            centerX: am5.p100,
                            paddingRight: 15
                        });

                        var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
                            maxDeviation: 0.3,
                            categoryField: "month",
                            renderer: xRenderer,
                            tooltip: am5.Tooltip.new(root, {})
                        }));

                        var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
                            maxDeviation: 0.3,
                            renderer: am5xy.AxisRendererY.new(root, {})
                        }));


                        // Create series
                        // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
                        var series = chart.series.push(am5xy.ColumnSeries.new(root, {
                            name: "Series 1",
                            xAxis: xAxis,
                            yAxis: yAxis,
                            valueYField: "value",
                            sequencedInterpolation: true,
                            categoryXField: "month",
                            tooltip: am5.Tooltip.new(root, {
                                labelText:"{valueY}"
                            })
                        }));

                        series.columns.template.setAll({ cornerRadiusTL: 5, cornerRadiusTR: 5 });
                        series.columns.template.adapters.add("fill", (fill, target) => {
                            return chart.get("colors").getIndex(series.columns.indexOf(target));
                        });

                        series.columns.template.adapters.add("stroke", (stroke, target) => {
                            return chart.get("colors").getIndex(series.columns.indexOf(target));
                        });

                        // Set data
                        var data = [{
                                month: "JAN",
                                value: jan
                            }, {
                                month: "FEB",
                                value: feb
                            }, {
                                month: "MAR",
                                value: mar
                            }, {
                                month: "APR",
                                value: apr
                            }, {
                                month: "MAY",
                                value: may
                            }, {
                                month: "JUN",
                                value: jun
                            }, {
                                month: "JUL",
                                value: jul
                            }, {
                                month: "AUG",
                                value: aug
                            }, {
                                month: "SEP",
                                value: sep
                            }, {
                                month: "OCT",
                                value: oct
                            }, {
                                month: "NOV",
                                value: nov
                            }, {
                                month: "DEC",
                                value: dec
                        }];

                        xAxis.data.setAll(data);
                        series.data.setAll(data);

                        // Make stuff animate on load
                        // https://www.amcharts.com/docs/v5/concepts/animations/
                        series.appear(1000);
                        chart.appear(1000, 100);

                    }); // end am5.ready()
                }
        });
    } else {
        // 2. 연도 선택
        if(year != "-1"){
            // 연도만 선택했을 때는 line 그래프를 출력한다.
            //
            $.ajax({
                crossDomain: true,
                url: "/admin/roomJSON",
                data: {"year" : year, "month" : month, "day" : day, "category" : category, "local" : local},
                contentType: "application/json; charset=utf-8",
                method: "GET",
                dataType : "json",
                async : false,
                success: function(data){
                    $(".admin-chart-wrapper").prepend("<div> 회원들의 공방 등록 현황 (단위 : 연) </div>");
                    // 달별 데이터
                    var jan = 0;
                    var feb = 0;
                    var mar = 0;
                    var apr = 0;
                    var may = 0;
                    var jun = 0;
                    var jul = 0;
                    var aug = 0;
                    var sep = 0;
                    var oct = 0;
                    var nov = 0;
                    var dec = 0;

                    var bundleData = $(data.history).map(function(i, history) {
                        // 1. 강의 개설 날짜
                        var date = history.date;
                        console.log(date);

                        // 2. 강의 개설 '월'
                            // 1. 각각 월에 값을 더해서 차트로 출력하기 위함
                        var month = date.split("-")[1];
                        console.log(month);

                        switch(month){
                            case "1":
                                jan = jan + 1;
                                break;
                            case "2":
                                feb = feb + 1;
                                break;
                            case "3":
                                mar = mar + 1;
                                break;
                            case "4":
                                apr = apr + 1;
                                break;
                            case "5":
                                may = may + 1;
                                break;
                            case "6":
                                jun = jun + 1;
                                break;
                            case "7":
                                jul = jul + 1;
                                break;
                            case "8":
                                aug = aug + 1;
                                break;
                            case "9":
                                sep = sep + 1;
                                break;
                            case "10":
                                oct = oct + 1;
                                break;
                            case "11":
                                nov = nov + 1;
                                break;
                            case "12":
                                dec = dec + 1;
                                break;
                        }
                        var category = history.category;
                    });


                    am5.ready(function() {

                    // Create root element
                    // https://www.amcharts.com/docs/v5/getting-started/#Root_element
                    var root = am5.Root.new("chartdiv");


                    // Set themes
                    // https://www.amcharts.com/docs/v5/concepts/themes/
                    root.setThemes([
                      am5themes_Animated.new(root)
                    ]);


                    // Create chart
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/
                    var chart = root.container.children.push(am5xy.XYChart.new(root, {
                      panX: true,
                      panY: true,
                      wheelX: "panX",
                      wheelY: "zoomX"
                    }));


                    // Add cursor
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
                    var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
                      behavior: "none"
                    }));
                    cursor.lineY.set("visible", false);


                    // Generate random data
                    var date = new Date();
                    date.setHours(0, 0, 0, 0);
                    var value = 100;

                    function generateData() {
                      value = Math.round((Math.random() * 10 - 5) + value);
                      am5.time.add(date, "day", 1);
                      return {
                        date: date.getTime(),
                        value: value
                      };
                    }

                    function generateDatas(count) {
                      var data = [];
                      for (var i = 0; i < count; ++i) {
                        data.push(generateData());
                      }
                      return data;
                    }


                    // Create axes
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
                    var xAxis = chart.xAxes.push(am5xy.DateAxis.new(root, {
                      maxDeviation: 0.2,
                      baseInterval: {
                        timeUnit: "day",
                        count: 1
                      },
                      renderer: am5xy.AxisRendererX.new(root, {}),
                      tooltip: am5.Tooltip.new(root, {})
                    }));

                    var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
                      renderer: am5xy.AxisRendererY.new(root, {})
                    }));


                    // Add series
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
                    var series = chart.series.push(am5xy.LineSeries.new(root, {
                      name: "Series",
                      xAxis: xAxis,
                      yAxis: yAxis,
                      valueYField: "value",
                      valueXField: "date",
                      tooltip: am5.Tooltip.new(root, {
                        labelText: "{valueY}"
                      })
                    }));


                    // Add scrollbar
                    // https://www.amcharts.com/docs/v5/charts/xy-chart/scrollbars/
                    chart.set("scrollbarX", am5.Scrollbar.new(root, {
                      orientation: "horizontal"
                    }));


                    // Set data
                    var data = generateDatas(1200);




                    series.data.setAll(data);


                    // Make stuff animate on load
                    // https://www.amcharts.com/docs/v5/concepts/animations/
                    series.appear(1000);
                    chart.appear(1000, 100);

                    }); // end am5.ready()
                }


            });

            // 3. 월 선택 -> 연도 + 달 데이터를 넘겨서 받아온다.
            if(month != "-1"){
                $.ajax({
                        crossDomain: true,
                        url: "/admin/roomJSON",
                        data: {"year" : year, "month" : month, "day" : day, "category" : category, "local" : local},
                        contentType: "application/json; charset=utf-8",
                        method: "GET",
                        dataType : "json",
                        async : false,
                        success: function(data){

                            console.log(JSON.stringify(data));

                            // 달별 데이터
                            var jan = 0;
                            var feb = 0;
                            var mar = 0;
                            var apr = 0;
                            var may = 0;
                            var jun = 0;
                            var jul = 0;
                            var aug = 0;
                            var sep = 0;
                            var oct = 0;
                            var nov = 0;
                            var dec = 0;

                            var bundleData = $(data.history).map(function(i, history) {
                                // 1. 강의 개설 날짜
                                var date = history.date;
                                console.log(date);

                                // 2. 강의 개설 '월'
                                    // 1. 각각 월에 값을 더해서 차트로 출력하기 위함
                                var month = date.split("-")[1];
                                console.log(month);

                                switch(month){
                                    case "1":
                                        jan = jan + 1;
                                        break;
                                    case "2":
                                        feb = feb + 1;
                                        break;
                                    case "3":
                                        mar = mar + 1;
                                        break;
                                    case "4":
                                        apr = apr + 1;
                                        break;
                                    case "5":
                                        may = may + 1;
                                        break;
                                    case "6":
                                        jun = jun + 1;
                                        break;
                                    case "7":
                                        jul = jul + 1;
                                        break;
                                    case "8":
                                        aug = aug + 1;
                                        break;
                                    case "9":
                                        sep = sep + 1;
                                        break;
                                    case "10":
                                        oct = oct + 1;
                                        break;
                                    case "11":
                                        nov = nov + 1;
                                        break;
                                    case "12":
                                        dec = dec + 1;
                                        break;
                                }
                                var category = history.category;
                            });


                            am5.ready(function() {

                                // Create root element
                                // https://www.amcharts.com/docs/v5/getting-started/#Root_element
                                var root = am5.Root.new("chartdiv");

                                // Set themes
                                // https://www.amcharts.com/docs/v5/concepts/themes/
                                root.setThemes([
                                    am5themes_Animated.new(root)
                                ]);


                                // Create chart
                                // https://www.amcharts.com/docs/v5/charts/xy-chart/
                                var chart = root.container.children.push(am5xy.XYChart.new(root, {
                                    panX: true,
                                    panY: true,
                                    wheelX: "panX",
                                    wheelY: "zoomX"
                                }));

                                // Add cursor
                                // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
                                var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
                                cursor.lineY.set("visible", false);


                                // Create axes
                                // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
                                var xRenderer = am5xy.AxisRendererX.new(root, { minGridDistance: 30 });
                                xRenderer.labels.template.setAll({
                                    rotation: -90,
                                    centerY: am5.p50,
                                    centerX: am5.p100,
                                    paddingRight: 15
                                });

                                var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
                                    maxDeviation: 0.3,
                                    categoryField: "month",
                                    renderer: xRenderer,
                                    tooltip: am5.Tooltip.new(root, {})
                                }));

                                var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
                                    maxDeviation: 0.3,
                                    renderer: am5xy.AxisRendererY.new(root, {})
                                }));


                                // Create series
                                // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
                                var series = chart.series.push(am5xy.ColumnSeries.new(root, {
                                    name: "Series 1",
                                    xAxis: xAxis,
                                    yAxis: yAxis,
                                    valueYField: "value",
                                    sequencedInterpolation: true,
                                    categoryXField: "month",
                                    tooltip: am5.Tooltip.new(root, {
                                        labelText:"{valueY}"
                                    })
                                }));

                                series.columns.template.setAll({ cornerRadiusTL: 5, cornerRadiusTR: 5 });
                                series.columns.template.adapters.add("fill", (fill, target) => {
                                    return chart.get("colors").getIndex(series.columns.indexOf(target));
                                });

                                series.columns.template.adapters.add("stroke", (stroke, target) => {
                                    return chart.get("colors").getIndex(series.columns.indexOf(target));
                                });


                                // Set data
                                var data = [{
                                        month: "JAN",
                                        value: jan
                                    }, {
                                        month: "FEB",
                                        value: feb
                                    }, {
                                        month: "MAR",
                                        value: mar
                                    }, {
                                        month: "APR",
                                        value: apr
                                    }, {
                                        month: "MAY",
                                        value: may
                                    }, {
                                        month: "JUN",
                                        value: jun
                                    }, {
                                        month: "JUL",
                                        value: jul
                                    }, {
                                        month: "AUG",
                                        value: aug
                                    }, {
                                        month: "SEP",
                                        value: sep
                                    }, {
                                        month: "OCT",
                                        value: oct
                                    }, {
                                        month: "NOV",
                                        value: nov
                                    }, {
                                        month: "DEC",
                                        value: dec
                                }];

                                xAxis.data.setAll(data);
                                series.data.setAll(data);

                                // Make stuff animate on load
                                // https://www.amcharts.com/docs/v5/concepts/animations/
                                series.appear(1000);
                                chart.appear(1000, 100);

                            }); // end am5.ready()
                        }
                });
                // 4. 일 선택 -> 연도 + 달 + 일
                    // 1. [시간 단위] 로 회원들의 신청 내역을 확인합니다.
                    // 2. TimeTable 에 HH, HH 형태로 등록되어있습니다.
                    // 3. 클래스가 시작되는 시간을 기준으로 그래프를 출력합니다.
                if(day != "-1"){
                    //
                }

            }

        }
        // 카테고리를 선택했을 경우
        else if(category != "-1"){

        }
        // 지역을 선택했을 경우
        else if(category != "-1"){

        }
    }







}

function isEmpty(str, defaultStr){
    if(typeof str == "undefined" || str == null || str == ""){
        str = defaultStr;
    }
    return str;
}