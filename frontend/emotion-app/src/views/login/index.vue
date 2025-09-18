<template>
  <div id="loginPage">
    <div class="call">
        <h1>HELLO ^ ^</h1>
        <p>欢迎使用_______ </p>
    </div>
    <div class="subject">
        <div class="top">
            <div class="login">
                <p>账号登录</p>
                <img src="../../assets/image/line.png" alt="">
            </div>
            <div class="register">注册账号</div>
        </div>
        <div class="center">
            <input type="text" placeholder="请输入账号" name="account" class="account-icon" v-model="account">
            <input type="password" placeholder="请输入密码" name="password" class="password-icon" v-model="password" v-show="flag === 1">
            <img src="../../assets/image/eye.png" alt="" class="eye" @click="changeFlag">
            <input type="text" placeholder="请输入密码" name="password" class="password-icon" v-model="password" v-show="flag === 0">
        </div>
        <div class="bottom">
            <div class="cir" @click="checked" ref="cir"></div>
            <p>已经阅读并同意<span>用户协议、隐私政策</span></p>
        </div>
    </div>
    <button @click="postMessage" ref="login">登录</button>
    <div class="other">
        <div class="left">验证码登录</div>
        <div class="right">
            <div class="forget">忘记密码</div>
            <div class="register">立即注册</div>
        </div>
    </div>
  </div>
</template>

<script>
export default {
    name: 'loginPage',
    data () {
        return {
            flag: 1,
            account: '',
            password: '',
            isCheck: 0
        }
    },
    methods: {
        changeFlag () {
            if (this.flag === 1) {
                 this.flag = 0
            }
            else {
                this.flag = 1
            }
        },
        checked () {
            if (!this.isCheck) {
                this.isCheck = 1
                this.$refs.cir.classList.add('check')
                // console.log(11)
            }
            else {
                this.isCheck = 0
                this.$refs.cir.classList.remove('check')
            }
        },
        postMessage () {
            //判断
            //重置表单
            this.account = ''
            this.password = ''
            this.$refs.cir.classList.remove('check')
            //改变效果
            // this.$refs.login.classList.add('active')
        }
    }
}
</script>

<style lang="less" scoped>
#loginPage {
    padding-top: 105px;
    padding-left: 24px;
    height: 844px;
    background: linear-gradient(148.3deg, 
    #fdf4e0 0%, 
    #ffffff 100%
    );
}
.call {
    width: 100%;
    // height: 40px;
    text-align: left;
    // font-family: "苹方";
    h1 {
        color: #000000;
        font-size: 28px;
        font-weight: 400;
    }
    p {
        margin-top: 4px;
        width: 135px;
        height: 26px;
        color: #00000099;
        font-size: 18px;
        font-weight: 550;
    }
}
.subject {
    margin-top: 32px;
    width: 342px;
    height: 345px;
    border-radius: 30px;
    background: #ffffff;
    box-shadow: 0 20px 10px 0 #00000040;
    .top {
        display: flex;
        justify-content: space-between;
        text-align: center;
        height: 60px;
        line-height: 60px;
        cursor: pointer;
        .login {
            position: relative;
            width: 171px;
            color: #F2A600;
            // background-color: #fff;
            // border-radius: 30px 30px 0 0;
            img {
                position: absolute;
                top: 47px;
                left: 72px;
                width: 28px;
            }
        }
        .register {
            flex: 1;
            color: #00000080;
            // background-color: #FFE5AD;
            // border-radius: 0 30px 0 20px;
        }
        .login,
        .register {
            font-size: 18px;
            font-weight: 550;
        }
    }
    .center {
        margin-top: 51px;
        margin-left: 10px;
        position: relative;
        width: 100%;
        // height: 23px;
        color: #aaaaaa99;
        font-size: 16px;
        font-weight: 400;
        [name = account] {
            padding-left: 74px;
            width: 300px;
            height: 50px;
            border-radius: 60px;
            border: 0;
            background: #fdf5e180 url('../../assets/image/zhanghao.png') no-repeat 37px;
            background-size: 20px;
        }
        [name = password] {
            padding-left: 74px;
            margin-top: 44px;
            width: 300px;
            height: 50px;
            border-radius: 60px;
            border: 0;
            background: #fdf5e180 url('../../assets/image/mima.png') no-repeat 37px;
            background-size: 20px;
        }
        .eye {
            position: absolute;
            right: 60px;
            top: 110px;
            width: 20px;
        }
    }
    .bottom {
        display: flex;
        margin-top: 25px;
        margin-left: 33px;
        .check {
            background-color: #AAAAAA;
        }
        .cir {
            width: 12px;
            height: 12px;
            border: 1px solid #979797;
            border-radius: 12px;
        }
        p {
            margin-left: 6px;
            color: #999999;
            font-size: 10px;
            line-height: 12px;
            span {
                color: #4b9aff;
                height: 12px;
                border-bottom: 1px #4B9AFF solid;
            }
        }
    }
}
button {
    margin-top: 40px;
    width: 342px;
    height: 50px;
    border-radius: 50px;
    background-image: linear-gradient(-38.7deg, #ffe5ad 0%, #ffc13b 100%);
    border: 0;
    color: #ffffff;
    font-size: 20px;
    transition: all 1s;
}
.other {
    margin-top: 53px;
    padding: 16px;
    display: flex;
    justify-content: space-between;
    width: 342px;
    color: #4781b3;
    font-size: 12px;
    cursor: pointer;
    .left,
    .right {
        height: 12px;
    }
    .left {
        border-bottom:1px #4781b3 solid;
    }
    .right {
        display: flex;
        .register {
            margin-left: 20px;
            border-bottom:1px #4781b3 solid;
        }
        .forget {
            border-bottom:1px #4781b3 solid;
        }
    }
}
.active {
    background-color: #fff;
    border: 3px #FFC13B solid;
    color: #FFC13B
}
</style>