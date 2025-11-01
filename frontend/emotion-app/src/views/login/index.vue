<template>
  <div id="loginPage">
    <div class="call">
        <h1>HELLO ^ ^</h1>
        <p>欢迎使用情绪小栈 </p>
    </div>
    <div class="subject" ref="subject">
        <div class="top">
            <div class="login" @click="toLogin">
                <p ref="loginMode">账号登录</p>
                <img src="@/assets/image/line.png" alt="" v-show="isLogin">
            </div>
            <div class="register" @click="toRegister">
                <p ref="registerMode">注册账号</p>
                <img src="@/assets/image/line.png" alt="" v-show="!isLogin">
            </div>
        </div>
        <div class="loginTab" v-if="isLogin">
            <input type="text" placeholder="请输入账号" name="account" class="account-icon" v-model="account">
            <input type="password" placeholder="请输入密码" name="password" class="password-icon" v-model="password" v-show="flag === 1">
            <img src="../../assets/image/eye.png" alt="" class="eye" @click="changeFlag">
            <input type="text" placeholder="请输入密码" name="password" class="password-icon" v-model="password" v-show="flag === 0">
        </div>
        <!-- 注册 -->
        <div class="registerTab" v-else>
            <input type="text" placeholder="请输入账号" name="account" class="account-icon" v-model="account">
            <input type="password" placeholder="请输入密码" name="password" class="password-icon" v-model="password" v-show="flag === 1">
            <img src="../../assets/image/eye.png" alt="" class="eye" @click="changeFlag">
            <input type="text" placeholder="请输入密码" name="password" class="password-icon" v-model="password" v-show="flag === 0">
            <!-- 再次输入密码 -->
            <input type="password" placeholder="请再次输入密码" name="password-again" class="password-again-icon" v-model="passwordAgain" v-show="aginFlag === 1">
            <img src="../../assets/image/eye.png" alt="" class="password-again-eye" @click="changeLastFlag">
            <input type="text" placeholder="请再次输入密码" name="password-again" class="password-again-icon" v-model="passwordAgain" v-show="aginFlag === 0">
        </div>
        <div class="bottom">
            <div class="cir" @click="checked" ref="cir"></div>
            <p>已经阅读并同意<span>用户协议、隐私政策</span></p>
        </div>
    </div>
    <button @click="login" ref="login" v-if="isLogin">登录</button>
    <!-- 注册 -->
    <button @click="register" ref="register" v-else>注册</button>
    <!-- 登录 -->
    <div class="loginOther" v-if="isLogin">
        <div class="left">验证码登录</div>
        <div class="right">
            <div class="forget">忘记密码</div>
            <div class="register">立即注册</div>
        </div>
    </div>
    <!-- 注册 -->
     <div class="registerOther" v-else>
        <div class="toLogin">已有密码？去登录</div>
    </div>
  </div>
</template>

<script>
// import { userLogin } from '@/api/login'
import axios from 'axios'
export default {
    name: 'loginPage',
    data () {
        return {
            flag: 1,
            aginFlag: 1,
            account: '',
            password: '',
            passwordAgain: '',
            isCheck: 0,
            isLogin: true
        }
    },
    methods: {
        // 密码是否可见
        changeFlag () {
            if (this.flag === 1) {
                 this.flag = 0
            }
            else {
                this.flag = 1
            }
        },
        changeLastFlag() {
            if (this.aginFlag === 1) {
                 this.aginFlag = 0
            }
            else {
                this.aginFlag = 1
            }
        },
        // 是否勾选同意协议
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
        // 登录
        async login () {
            // 1.判断
            if (!this.isCheck) return this.$toast('请先同意协议')
            // 2.改变效果
            // this.$refs.login.classList.add('active')
            // 3.提交用户信息
            const res = await axios.post('http://localhost:8080/api/auth/login', { 
                username: this.account,
                password: this.password
            })
            console.log(res)
            // 4.存储 userInfo (token、信息)
            this.$store.commit('user/setUserInfo', res.data.data)
            // 5.重置表单
            this.account = ''
            this.password = ''
            this.$refs.cir.classList.remove('check')
            // 6.跳转页面
            this.$router.push('/home')
        },
        // 注册
        async register () {
            // 判断 --> 1.两次的密码要一致
            if (this.password !== this.passwordAgain) return this.$toast('两次的密码要一致')
            // 2.有无同意协议
            if (!this.isCheck) return this.$toast('请先同意协议')
            const res = await axios.post('http://localhost:8080/api/auth/register', {
                username: this.account,
                password: this.password
            })
            console.log(res)
            // 重置表单
            this.account = ''
            this.password = ''
            this.passwordAgain = ''
            this.$refs.cir.classList.remove('check')
        },
        // 改变模式样式
        switchingMode () {
            const subject = this.$refs.subject
            // console.log(subject.childNodes[1])
            if (subject.childNodes[1].classList.contains('registerTab')) {
                // console.log('登录页面',11)
                subject.style.height = '424px'
            }else {
                subject.style.height = '345px'
            }
        },
        // 变为注册模式
        toRegister () {
            this.isLogin = false
            this.$refs.loginMode.style.color = '#00000080'
            this.$refs.registerMode.style.color = '#F2A600'
            this.$nextTick(() => {
                this.switchingMode()
            })
        },
        // 变为登录模式
        toLogin () {
            this.isLogin = true
            this.$refs.loginMode.style.color = '#F2A600'
            this.$refs.registerMode.style.color = '#00000080'
            this.$nextTick(() => {
                this.switchingMode()
            })
        }
    },
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
        // width: 135px;
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
            position: relative;
            color: #00000080;
            // background-color: #FFE5AD;
            // border-radius: 0 30px 0 20px;
            img {
                position: absolute;
                top: 47px;
                left: 72px;
                width: 28px;
            }
        }
        .login,
        .register {
            font-size: 18px;
            font-weight: 550;
        }
    }
    .loginTab,
    .registerTab {
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
        [name = password],
        [name = password-again] {
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
            top: 110px;
            right: 60px;
            width: 20px;
        }
        .password-again-eye {
            position: absolute;
            top: 205px;
            right: 60px;
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
.loginOther {
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
        height: 15px;
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
.registerOther {
    color: #4781b3;
    font-size: 12px;
    .toLogin {
        margin-top: 30px;
        margin-left: 121px;
        width: 98px;
        border-bottom:1px #4781b3 solid;
    }
}
.active {
    background-color: #fff;
    border: 3px #FFC13B solid;
    color: #FFC13B
}
</style>