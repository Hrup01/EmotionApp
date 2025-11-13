<template>
  <div id="setup">
    <NavBar :title="title" :margin-left="marginLeft"></NavBar>
    <ul>
        <li @click="logOut">
            <img src="@/assets/image/我的_设置/退出.png" alt="">
            <p>退出登录</p>
        </li>
        <li>
            <img src="@/assets/image/我的_设置/注销.png" alt="">
            <p>注销账户</p>
        </li>
    </ul>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'setUp',
    components: {
        NavBar
    },
    data () {
        return {
            title: '设置',
            marginLeft: '129px'
        }
    },
    methods: {
        // 推出登录
        logOut () {

        }
    }
}
</script>

<style lang="less" scoped>
#setup {
    width: 390px;
    height: 852px;
    background: #fdf4e0;
}
ul {
    margin: 7px auto;
    width: 366px;
    height: 132px;
    li {
        padding-left: 10px;
        display: flex;
        align-items: center;
        width: 366px;
        height: 66px;
        background: #ffffff66;
        font-size: 18px;
        img {
            margin-right: 20px;
            width: 32px;
        }
    }
    li:nth-child(1) {
        border-radius: 10px 10px 0 0;
    }
    li:nth-child(2) {
        border-radius: 0 0 10px 10px;
    }
}
</style>