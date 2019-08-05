$.extend(validatePrompt, {
    realname:{
        onFocus:"2-20位字符，可由中文或英文组成",
        succeed:"",
        isNull:"请输入联系人姓名",
        error:{
            badLength:"联系人姓名长度只能在2-20位字符之间",
            badFormat:"联系人姓名只能由中文或英文组成"
        }
    },

    tel:{
        onFocus:"请填写联系人常用的电话，以便于我们联系，如：0527-88105500",
        succeed:"",
        isNull:"请输入您的手机号码",
        error:"手机号格式错误，请重新输入"
    },



    companysite:{
        onFocus:"如：http://www.321kuky.com",
        succeed:"",
        isNull:"请输入公司网址",
        error:{
            badLength:"网址长度只能在80位字符之内",
            badFormat:"网址格式不正确。应如：http://www.321kuky.com"
        }
    }
});

$.extend(validateFunction, {
    realname:function(option) {
        var length = validateRules.betweenLength(option.value.replace(/[^\x00-\xff]/g, "**"), 2, 20);
        var format = validateRules.isRealName(option.value);
        if (!length) {
            validateSettings.error.run(option, option.prompts.error.badLength);
        } else {
            if (!format) {
                validateSettings.error.run(option, option.prompts.error.badFormat);
            }
            else {
                validateSettings.succeed.run(option);
            }
        }
    },
    department:function(option) {
        var bool = (option.value == -1);
        if (bool) {
            validateSettings.isNull.run(option, "");
        }
        else {
            validateSettings.succeed.run(option);
        }
    },
    tel:function(option) {
        var format = validateRules.isTel(option.value);
        if (!format) {
            validateSettings.error.run(option, option.prompts.error);
        }
        else {
            validateSettings.succeed.run(option);
        }
    },



    FORM_validate:function() {
        $("#username").jdValidate(validatePrompt.username, validateFunction.username, true);
        $("#pwd").jdValidate(validatePrompt.pwd, validateFunction.pwd, true)
        $("#pwd2").jdValidate(validatePrompt.pwd2, validateFunction.pwd2, true);
        $("#authcode").jdValidate(validatePrompt.authcode, validateFunction.authcode, true);
        $("#realname").jdValidate(validatePrompt.realname, validateFunction.realname, true);
        $("#department").jdValidate(validatePrompt.department, validateFunction.department, true);
        $("#tel").jdValidate(validatePrompt.tel, validateFunction.tel, true);
        $("#mail").jdValidate(validatePrompt.mail, validateFunction.mail, true);
        return validateFunction.FORM_submit(["#username","#pwd","#pwd2","#mail","#realname","#tel"]);
    }
});




//默认下用户名框获得焦点
setTimeout(function() {
    $("#username").get(0).focus();
}, 0);
//用户名验证
$("#username").jdValidate(validatePrompt.username, validateFunction.username);
//密码验证
$("#pwd").bind("keyup",function(){
	validateFunction.pwdstrength();
}).jdValidate(validatePrompt.pwd, validateFunction.pwd)
//二次密码验证
$("#pwd2").jdValidate(validatePrompt.pwd2, validateFunction.pwd2);
//邮箱验证
$("#mail").jdValidate(validatePrompt.mail, validateFunction.mail);
//推荐人用户名
$("#referrer").bind("keydown",function(){
	$(this).css({"color":"#333333","font-size":"14px"});
}).bind("keyup",function(){
	if($(this).val() == "" || $(this).val() == "可不填"){
		$(this).css({ "color": "#999999", "font-size": "12px" });
	}
}).bind("blur",function(){
	if($(this).val() == "" || $(this).val() == "可不填"){
		$(this).css({"color":"#999999","font-size":"12px"}).jdValidate(validatePrompt.referrer, validateFunction.referrer, "可不填");
	}
})
//验证码验证
$("#authcode").jdValidate(validatePrompt.authcode, validateFunction.authcode);
//联系人姓名验证
$("#realname").jdValidate(validatePrompt.realname, validateFunction.realname);
//部门验证
$("#department").jdValidate(validatePrompt.department, validateFunction.department);
//固定电话验证
$("#tel").jdValidate(validatePrompt.tel, validateFunction.tel);
//手机验证
$("#mobile").jdValidate(validatePrompt.mobile, validateFunction.mobile);
//网址验证
$("#companysite").jdValidate(validatePrompt.companysite, validateFunction.companysite);
//显示密码事件
$("#viewpwd").bind("click", function() {
    if ($(this).attr("checked") == true) {
        validateFunction.showPassword("text");
        $("#o-password").addClass("pwdbg");
    } else {
        validateFunction.showPassword("password");
        $("#o-password").removeClass("pwdbg");
    }
});

//键盘输入验证码验证
$("#authcode").bind('keyup', function(event) {
    if (event.keyCode == 13) {
        $("#registsubmit").click();
    }
});
//确认协议才能提交
$("#protocol").click(function() {
    if ($("#protocol").attr("checked") != true) {
        $("#registsubmit").attr({ "disabled": "disabled" });
		$("#registsubmit").addClass("disabled");
    }
    else {
        $("#registsubmit").removeAttr("disabled");
		$("#registsubmit").removeClass("disabled");
    }
});
//表单提交验证和服务器请求
$("#registsubmit").click(function() {
    var flag = validateFunction.FORM_validate();
    if (flag) {
        $(this).attr({"disabled":"disabled"}).attr({"value":"提交中,请稍等"});
        $.ajax({
            type: "POST",
            url: "http://www.pinzhi365.com/regist/"+$("#authcode").val(),
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: $("#formpersonal").serialize(),
            success: function(result) {
                if (result) {
                    alert("注册成功");
                    window.location = "http://www.pinzhi365.com/index";
                }else{
                    alert("注册失败");
                    alert(result);
                }
            }
        });
    }
});