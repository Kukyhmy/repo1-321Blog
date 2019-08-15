$(function () {
    $("#jqGrid").jqGrid({
        url: '/admin/blogs/list',
        datatype: "json",
        colModel: [
    /*
    colModel:列名称对应的model，该model内的各个列要和colNames的各个列进行一一对应
    可以对各个列设置属性，name属性药设置为json数据的key名称,也就是我们的Bean的对应属性名称，
    width：宽度,editable代表是否可编辑，edittype代表编辑框的类型，可以是  text、select、texare、checkbox等类型,
    formatter:格式化显示的数据，
    unformat：处于编辑状态后反格式化到以前的数据
    */
            {label: 'id', name: 'blogId', index: 'blogId', width: 50, key: true, hidden: true},
            {label: '标题', name: 'blogTitle', index: 'blogTitle', width: 140},
            {label: '预览图', name: 'blogCoverImage', index: 'blogCoverImage', width: 120, formatter: coverImageFormatter},
            {label: '浏览量', name: 'blogViews', index: 'blogViews', width: 60},
            {label: '状态', name: 'blogStatus', index: 'blogStatus', width: 60, formatter: statusFormatter},
            {label: '博客分类', name: 'blogCategoryName', index: 'blogCategoryName', width: 60},
            {label: '添加时间', name: 'createTime', index: 'createTime', width: 90}
        ],
        height: 700,   //代表表格的高度
        rowNum: 10,   //设置表格默认显示记录的条数，用于分页
        rowList: [10, 20, 50],  //供用户选择每页显示的记录数
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,  //指是否可以进行多选
        pager: "#jqGridPager",  //用指定表格的工具栏   #jqGridPager是页面某个DIV的ID
        jsonReader: {              //  解析json数据的参数 很重要 定义了 后台分页参数的名字。
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {   //向后台交互时，所设置的参数名称对应的值
            page: "page",   //// 表示请求页码的参数名称
            rows: "limit",  //// 表示请求行数的参数名称
            order: "order",  //// 表示采用的排序方式的参数名称
        },
        gridComplete: function () {        //设置表格数据加载完毕后，所执行的操作 数据加载完成后
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function coverImageFormatter(cellvalue) {   //cellvalue表示当前单元格的值
        return "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='coverImage'/>";
    }

    function statusFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">草稿</button>";
        }
        else if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">发布</button>";
        }
    }

});

/**
 * 搜索功能
 */
function search() {
    //标题关键字
    var keyword = $('#keyword').val();
    if (!validLength(keyword, 20)) {
        swal("搜索字段长度过大!", {
            icon: "error",
        });
        return false;
    }
    //数据封装
    var searchData = {keyword: keyword};
    //传入查询条件参数
    $("#jqGrid").jqGrid("setGridParam", {postData: searchData});
    //点击搜索按钮默认都从第一页开始
    $("#jqGrid").jqGrid("setGridParam", {page: 1});
    //提交post并刷新表格
    $("#jqGrid").jqGrid("setGridParam", {url: '/admin/blogs/list'}).trigger("reloadGrid");
}

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function addBlog() {
    window.location.href = "/admin/blogs/edit";
}

function editBlog() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.location.href = "/admin/blogs/edit/" + id;
}

function deleteBlog() {
    var ids = getSelectedRows();
    if (ids == null) {
        toastr.error("请选择您要删除的blog");
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/admin/blogs/delete",
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