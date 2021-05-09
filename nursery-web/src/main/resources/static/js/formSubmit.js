var Script = function () {
    //校验密码
    function checkPassword() {
        //1.获取密码值
        var password = $("#password").val();
        //2.定义正则
        var reg_password = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,20}$/;
        var flag = reg_password.test(password);
        if (flag) {
            $("#password").css("border", "");
        } else {
            alert("密码由8-20位（数字和大小写、字符组成的）!!");
            $("#password").css("border", "1px solid red");
        }
        return flag;
    }

    //校验重复密码
    function checkVerifyPass() {
        var password = $("#password").val();
        var verifyPass = $("#verifyPass").val();
        var flag = false;
        if (password == verifyPass) {
            flag = true;
        }
        if (flag) {
            $("#verifyPass").css("border", "");
        } else {
            alert("请和输入的密码一致！！")
            $("#verifyPass").css("border", "1px solid red");
        }
        return flag;
    }

    function nickname(){
        var nickname = $("#nickname").val();
        if (nickname.length<6){
            alert("请输入长度大于6！！")
            $("#nickname").css("border", "1px solid red");
        }
    }

    //注册
    $("#btn_reg").click(function (e) {
        if (checkPassword() && checkVerifyPass() && nickname()) {
            e.preventDefault()
            $.ajax({
                type: "POST",
                url: "/consumer/register2",
                data: $("#registForm").serialize(),
                success: function(msg){
                    alert(msg.message);
                    if (msg.success || msg.code=="10000" || msg.message=="操作成功") {
                        window.location.href = '/login';
                    }
                }
            });
        }
    });

    $("#password").blur(checkPassword);
    $("#verifyPass").blur(checkVerifyPass);
    $("#nickname").blur(nickname);

}();