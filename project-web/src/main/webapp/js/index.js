$(document).ready(function () {

//  var rootPath = "http://192.168.35.29:8080/aicreate";
    var rootPath = "http://localhost:8080/aicreate";

    var hint = "请输入记录名称";

    var hintColor = "#adadad"; //提示信息的颜色

    var selectRecordIndex = -1;
    var selectedRecord = undefined; //选中的示教案列


    var loadRecords = function () {
        $.ajax({
            type: 'GET',
            url: rootPath + "/demos",
            success: function (data) {
                if (data != null) {
                    $("#records").empty();
                    for (var i = 0; i < data.length; i++) {
                        var li = $("<li></li>").text(data[i]);
                        $("#records").append(li);
                    }
                }
            }
        });
    }

    /**
     * 加载示教案列数据
     */
    loadRecords();

    var toggleBtn = function (clicked, btn) {
        if (clicked) {
            btn.removeClass("btn-success")
        } else {
            btn.addClass("btn-success")
        }
    }

    /**
     * 连接按钮
     */
    /*
      $("#conn").click(function () {
        var clicked = $(this).hasClass("btn-success");
        var type ;
        var errorMsg ;
        if(clicked){
          type = "DELETE";
          errorMsg = "断开设备失败";
        }else{
          type = "POST";
          errorMsg = "连接设备失败";
        }
        $.ajax({
          type: type,
          url: rootPath+"/preparation/connection/" ,
          success: function(data){
            if(data == true){
              toggleBtn(clicked,$("#conn"));
            }else{
              alert(errorMsg);
            }
          },
          error:function ( ) {
            alert(errorMsg);
          }
        });
      });

      */
    /**
     * 爆闸按钮
     */
    /*
      $("#break").click(function () {
        var clicked = $(this).hasClass("btn-success");
        var errorMsg;
        var path;
        if(clicked){
          errorMsg = "关闭爆闸失败";
          path = "/preparation/breakoff";
        }else{
          errorMsg = "打开爆闸失败";
          path = "/preparation/breakon";
        }
        $.ajax({
          type: 'PUT',
          url: rootPath+path ,
          success: function(data){
            if(data == true){
              toggleBtn(clicked,$("#break"));
            }else{
              alert(errorMsg);
            }
          },
          error:function ( ) {
            alert(errorMsg);
          }
        });
      });

      */
    /**
     * 通电按钮
     */
    /*
      $("#serv").click(function () {
        var clicked = $(this).hasClass("btn-success");
        var path;
        var errorMsg;
        if(clicked){
          path = "/preparation/servoff";
          errorMsg = "断电失败";
        }else{
          path = "/preparation/servon";
          errorMsg = "通电失败";
        }
        $.ajax({
          type: 'PUT',
          url: rootPath+path ,
          success: function(data){
            if(data == true){
              toggleBtn(clicked,$("#serv"));
            }else{
              alert(errorMsg);
            }
          },
          error:function ( ) {
            alert(errorMsg);
          }
        });
      });
    */

    /**
     * 复位按钮
     */
    $("#home").click(function () {
        homeClick(true);
        $.ajax({
            type: 'PUT',
            url: rootPath + "/preparation/home/",
            success: function (data) {
                if (data != true) {
                    alert("复位失败");
                }
                homeClick(false);
            },
            error: function () {
                alert("复位失败");
                homeClick(false);
            }
        });
    });

    var homeClick = function (clicked) {
        var home = $("#home");
        if (clicked) {
            home.addClass("btn-success")
            home.attr("disabled", "disabled")
        } else {
            home.removeClass("btn-success");
            home.removeAttr("disabled");
        }
    }

    /**
     * 开始记录
     */
    $("#recordStart").click(function () {
        $.ajax({
            type: 'PUT',
            url: rootPath + "/demos/record/",
            success: function (data) {
                if (data == true) {
                    $("#recordStart").addClass("btn-success")
                    $("#recordStart").attr("disabled", "disabled")
                    $("#recordSave").removeAttr("disabled");
                } else {
                    alert("开始记录失败");
                }
            },
            error: function () {
                alert("开始记录失败");
            }
        });
    });

    $(function () {
        var record = $('#recordName');
        record.focus(function () {
            inputHint(record, hint);
        }).blur(function () {
            inputHint(record, hint);
        })
    });

    /**
     * input标签的hint设置
     * @param input
     * @param hint
     */
    var inputHint = function (input, hint) {
        if (input.val() == "") {
            input.val(hint);
        } else if (input.val() == hint) {
            input.val("");
        }
    }

    /**
     * 弹出保存模态框
     */
    $("#recordSave").click(function () {
        $(this).addClass("btn-success")
        $('#recordModal').modal({
            keyboard: false,
            backdrop: "static",
        })
    });


    /**
     * 保存示教记录
     */
    $("#saveBtn").click(function () {
        var demoName = $('#recordName').val();
        if (demoName == "" || demoName == hint) {
            alert("记录名称不能为空");
            return;
        }
        $.ajax({
            type: 'POST',
            url: rootPath + "/demos/record/" + demoName,
            success: function (data) {
                if (data == true) {
                    $('#recordModal').modal("hide");
                    alert("保存记录成功");
                    loadRecords();
                } else {
                    alert("保存记录失败");
                }
            },
            error: function () {
                alert("保存记录失败");
            }
        });
    });

    /**
     * 在隐藏模态框后还原示教名称提示
     */
    $('#recordModal').on('hide.bs.modal', function () {
        $('#recordName').val(hint);
        $('#recordName').css("color", "#adadad");
        $("#recordSave").removeClass("btn-success");
        $("#recordSave").attr("disabled", "disabled");
        $("#recordStart").removeClass("btn-success");
        $("#recordStart").removeAttr("disabled");
    })

    /**
     * 选择示教案列
     */
    $("#records").on("click", "li", function () {
        $("#records li").each(function () {
            $(this).css("color", "#555555");
        })
        $(this).css("color", "#4cae4c");
        selectedRecord = $(this).text();
        selectRecordIndex = $(this).index();
        $("#recordReshow").removeAttr("disabled")
    });

    /**
     * 复现示教案列
     */
    $("#recordReshow").click(function () {
        if (selectedRecord == undefined) {
            alert("未选择示教案列");
            return;
        }
        $(this).addClass("btn-success");
        $(this).attr("disabled", "disabled");
        $.ajax({
            type: 'PUT',
            url: rootPath + "/demos/" + selectedRecord,
            success: function (data) {
                if (data == true) {
                    $("#records li:eq(" + selectRecordIndex + ")").css("color", "#555555");
                    selectedRecord = undefined;
                    selectRecordIndex = -1;
                    $("#recordReshow").removeClass("btn-success");
                }
            }
        });
    });

    $("#command").focus(function () {
        $("#command").on("keypress", function (event) {
            if (event.keyCode == "13") {
                var command = $("#command").val();
                if (command == "") {
                    $("#command").blur();
                    return;
                }
                $.ajax({
                    type: 'PUT',
                    url: rootPath + "/preparation/command/" + command,
                    success: function (data) {

                    }
                });
                $("#command").blur();
            }
        })
    })

    $("#command").blur(function () {
        $("#command").val("");
    })

    /**
     * 许可按钮
     */
    $("#permit").click(function () {
        $.ajax({
            headers: {
                timestamp : 1512988045435,
                token : "62529482cf86b30714739997d76e7ce62615498d4069a09b"
            },
            type: 'POST',
            url: "http://localhost:8080/adminmap/api",
            contentType: 'application/json;charset=utf-8',
            success: function (data) {
                if (data != true) {
                    alert("复位失败");
                }
                homeClick(false);
            },
            error: function () {
                alert("复位失败");
                homeClick(false);
            }
        });
    });


});