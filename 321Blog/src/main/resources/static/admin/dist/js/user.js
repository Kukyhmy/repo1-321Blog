$(function () {
    $("#jqGrid").jqGrid({
        url: '/admin/user/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden: true},
            {label: '用户名称', name: 'loginUserName', index: 'loginUserName', width: 80},
            {label: '用户昵称', name: 'nickName', index: 'nickName', width: 60},
            {label: '用户权限', name: 'authorityList2', index: 'authorityList2', width: 200,formatter:authorityList2Formatter},
            {label: '是否锁定', name: 'locked', index: 'locked', width: 150,formatter: lockedFormatter},
            {label: '用户头像', name: 'avatar', index: 'avatar', width: 200, formatter: coverImageFormatter},
            {label: '用户邮箱', name: 'email', index: 'email', width: 130},
            {label: '联系方式', name: 'phone', index: 'phone', width: 120},
            {label: '详细信息', name: 'detail', index: 'detail', width: 200},
        ],
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
function authorityList2Formatter(cellvalue) {
    var arr=new Array();
   // alert(cellvalue);  //ROLE_ADMIN,ROLE_USER
    arr=cellvalue.toString().split(",");//注split可以用字符或字符串分割
    if(arr.length==3){
        return "  <div class=\"form-check\"><label class=\"form-check-label\"><input class=\"form-check-input\" checked type=\"checkbox\" value=\"\">"+arr[0]+"<span class=\"form-check-sign\"><span class=\"check delete\"  delete-id="+arr[0]+"></span></span></label></div>" +
            "<div class=\"form-check\"><label class=\"form-check-label\"><input class=\"form-check-input\" checked type=\"checkbox\" value=\"\">"+arr[1]+"<span class=\"form-check-sign\"><span class=\"check delete\"  delete-id="+arr[1]+"></span></span></label></div>" +
            "<div class=\"form-check\"><label class=\"form-check-label\"><input class=\"form-check-input\" checked type=\"checkbox\" value=\"\">"+arr[2]+"<span class=\"form-check-sign\"><span class=\"check delete\"  delete-id="+arr[2]+"></span></span></label></div>";
    }
    if(arr.length==2){
         return"  <div class=\"form-check\"><label class=\"form-check-label\"><input class=\"form-check-input\" checked type=\"checkbox\" value=\"\">"+arr[0]+"<span class=\"form-check-sign\"><span class=\"check delete\"  delete-id="+arr[0]+"></span></span></label></div>" +
            "<div class=\"form-check\"><label class=\"form-check-label\"><input class=\"form-check-input\" checked type=\"checkbox\" value=\"\">"+arr[1]+"<span class=\"form-check-sign\"><span class=\"check delete\"  delete-id="+arr[1]+"></span></span></label></div>" ;
    }

    if(arr == false){
        return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">无分配 </button>";
    }
    if(arr.length==true) { //!isEmpty(arr)&&
        return"   <div class=\"form-check\"><label class=\"form-check-label\"><input class=\"form-check-input\" checked type=\"checkbox\" value=\"\">"+arr[0]+"<span class=\"form-check-sign\"><span class=\"check delete\"  delete-id="+arr[0]+"></span></span></label></div>";
    }

}
    function coverImageFormatter(cellvalue) {   //cellvalue表示当前单元格的值
        return "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='avatar'/>";
    }
    function lockedFormatter(cellvalue) {  //是否锁定 0未锁定 1已锁定无法登陆
         if (cellvalue == 0) {
             return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">未锁定 </button>" +
                 "<div class=\"togglebutton update\" update-locked=\'0\'><label><input type=\"checkbox\"  ><span class=\"toggle\"></span></label></div>";
         }
         else if (cellvalue == 1) { //btn-success
             return "<button type=\"button\" class=\"btn btn-block  btn-inverse btn-sm\" style=\"width: 50%;\">已锁定</button>" +
                 "<div class=\"togglebutton update\" update-locked=\'1\'><label><input type=\"checkbox\"  checked=\"\"><span class=\"toggle\"></span></label></div>";
         }
       /* if (cellvalue == 0) {
            return
                " <div class=\"togglebutton\"><label><input type=\"checkbox\"><span class=\"toggle\"></span>未锁定</label></div>";
        }
        else if (cellvalue == 1) { //btn-success
            return
                "<div class=\"togglebutton\"><label><input type=\"checkbox\" checked=\"\"><span class=\"toggle\"></span>已锁定无法登录</label></div>";
        }*/
    }

});

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}
$(document).on("click",".delete",function () {
    var id = getSelectedRow();
    var authorityName = $(this).attr("delete-id");
    if(id == null){
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除"+authorityName+"权限吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "DELETE",
                url: "/admin/users/delete/"+id+"/"+authorityName,
                contentType: "application/json",
                success: function (r) {
                    if (r.resultCode == 200) {
                        swal("删除成功", {
                            icon: "msg-success.html",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    }
);
});
$(document).on("click", ".update", function() {
    var id = getSelectedRow();
    var locked = $(this).attr("update-locked");
    if (id == null) {

        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要更改数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "PUT",
                url: "/admin/users/edit/"+id+"/"+locked,
                contentType: "application/json",
               // data: JSON.stringify(id),
                success: function (r) {
                    if (r.resultCode == 200) {
                        swal("修改成功", {
                            icon: "msg-success.html",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    }
);
});
function editUser(cellvalue) {
    var id = getSelectedRow();
   // var locked2 = $("#gridId").getCell(id,locked);
   // var celldata = $("#gridId").jqGrid('getCell',id,"locked");
   // var celldata =  $("#gridId").getCell(id, "loginUserName");
    // var pcdm= $("#gridId").getCell(id,"locked");
   // alert(celldata);
    //var locked2=$("#gridId").jqGrid("getCell",id,"是否锁定");
    if (id == null) {

        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要更改数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "PUT",
                url: "/admin/users/edit",
                contentType: "application/json",
                data: JSON.stringify(id,cellvalue),
                success: function (r) {
                    if (r.resultCode == 200) {
                        swal("修改成功", {
                            icon: "msg-success.html",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    }
);

}
/**
 * 权限名称 验证8到10位（字母，下划线）
 *AVV_SDD
 * @param authorityName
 * @returns {boolean}
 */
function validAuthorityName(authorityName) {
    var pattern = /^[A-Z]+((_[^_])|[A-Z])*[A-Z]$/;
    if (pattern.test(authorityName.trim())) {
        return (true);
    } else {
        return (false);
    }
}
function userAdd(authorityName) {
    var ids = getSelectedRows();
    if (!validAuthorityName(authorityName)) {
        swal("权限名称不规范(请参考：ROLE_USER)", {
            icon: "error",
        });
    } else {
        var url = '/admin/user/save/'+ authorityName;
        $.ajax({
            type: 'POST',
            url: url,
            contentType: "application/json",
            data: JSON.stringify(ids),
            success: function (result) {
                if (result.resultCode == 200) {
                    swal("保存成功", {
                        icon: "msg-success.html",
                    });
                    reload();
                }
                else {
                    swal(result.message, {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    }
}

function deleteUser() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除用户吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "DELETE",
                url: "/admin/users/delete",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function (r) {
                    if (r.resultCode == 200) {
                        swal("删除成功", {
                            icon: "msg-success.html",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    }
);
}
