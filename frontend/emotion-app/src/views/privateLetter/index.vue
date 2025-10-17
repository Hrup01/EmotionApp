<template>
  <div id="privateLetter">
    <div class="top">
        <nav-bar></nav-bar>
        <div class="userMessage">
            <img src="@/assets/image/私信/头像.png" alt="" class="headPic">
            <div class="name">CaCa</div>
            <div class="navbarRightPic">
                <img src="@/assets/image/私信/更多1.png" alt="" class="more" @click="changeFlag">
                <div class="hiden" ref="hiden" v-show="flag">
                    <div class="blackList" @click="blackList = true">
                        <img src="@/assets/image/拉黑用户.png" alt="">
                        <p>拉黑用户</p>
                    </div>
                    <div class="report" @click="report = true">
                        <img src="@/assets/image/举报用户.png" alt="">
                        <p>举报用户</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="body">

    </div>
    <div class="footer">
        <div class="sendMessage">
            <textarea name="" id="" v-model="content" @focus="changeCotent" ref="textarea"></textarea>
            <div class="add"><img src="@/assets/image/私信/添加.png" alt=""></div>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';
export default {
    name: 'privateLetter',
    components: {
        NavBar
    },
    data () {
        return {
            flag: false,
            content: '发送消息...'
        }
    },
    methods: {
        changeFlag () {
            this.flag = this.flag ? false : true
        },
    }
}
</script>

<style lang="less" scoped>
#privateLetter {
    width: 390px;
    height: 844px;
    background: #fdf4e4;
    font-weight: 600;
}
.top {
    height: 104px;
    .userMessage {
        position: absolute;
        top: 42px;
        left: 50px;
        display: flex;
        align-items: center;
        .headPic {
            margin-right: 14px;
            width: 46px;
            height: 46px;
        }
        p {
            color: #363636;
            font-size: 18px;
        }
        .navbarRightPic {
            .more {
                position: fixed;
                top: 50px;
                right: 22px;
                width: 24px;
            }
            .hiden {
                margin-top: 4px;
                padding-left: 16px;
                position: fixed;
                right: 29px;
                width: 104px;
                display: flex;
                flex-direction: column;
                justify-content: space-around;
                height: 66px;
                border-radius: 8px;
                background: #fff3db;
                img {
                    margin-right: 9px;
                    width: 18.25px;
                    height: 18.77px;
                }
                p {
                    margin-top: 2px;
                    color: #000000b3;
                    font-size: 10.95px;
                    font-weight: 600;
                }
                .blackList,
                .report {
                    display: flex;
                }
            }
        } 
    }
}
.body {
    width: 390px;
    height: 740px;
    background: #fcfaf5;
}
.footer {
    position: fixed;
    bottom: 0;
    width: 390px;
    height: 100px;
    background-color: #fff;
    .sendMessage {
        padding-top: 24px;
        display: flex;
        align-items: center;
        textarea {
            margin-left: 29px;
            margin-right: 10px;
            padding: 10px 28px;
            width: 302px;
            height: 40px;
            border-radius: 36px;
            border: 1px solid #fdf4e4;
            background: #f2cd8d3d;
            color: #543a0d33;
            font-size: 14px;
        }
        img {
            width: 32px;
            height: 32px;
        }
    }
}
</style>