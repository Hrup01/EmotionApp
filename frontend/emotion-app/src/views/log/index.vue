<template>
  <div id="logPage">
    <div class="top">
        <NavBar :title="title" :toUrl="toUrl"></NavBar>
    </div>
    <div class="title">
        <img src="../../assets/image/icon_notebook.png" alt="">
        <h3>记录情绪日志</h3>
    </div>
    <div class="write">
        <div class="wrapper">
            <div class="date">
                <div class="exactDate">{{ exactDate }}</div>
                <div class="week">{{ week }}</div>
            </div>
            <div class="border"></div>
            <textarea name="moodLog" id="" placeholder="输入心得，记录你的情绪变化" v-model="content"></textarea>
            <img src="../../assets/image/icon_add_pic.png" alt="" class="pic">
        </div>
    </div>
    <button>完成</button>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: "logPage",
    components: {
        NavBar
    },
    data () {
        return {
            exactDate: '',
            week: '',
            content: '',
            title: '日志打卡',
            toUrl: '/moodOption'
        }
    },
    mounted () {
        const date = new Date()
        const year =  date.getFullYear()
        const month = date.getMonth()
        const day = date.getDate()
        const arr = ['周天','周一','周二','周三','周四','周五','周六']
        this.week = arr[date.getDay()]
        this.exactDate = `${year}年${month + 1}月${day}日`
    }
}
</script>

<style lang="less" scoped>
#logPage {
    height: 844px;
    background-color: #FEF8EC;
}
.wrapper {
    margin: 0 auto;
    width: 257px;
}
.top {
    height: 95px;
    background: #d3e4c3;

}
.title {
    margin-top: 20px;
    margin-left: 51px;
    display: flex;
    height: 26px;
    img {
        margin-right: 10px;
        width: 25px;
    }
    h3 {
        font-size: 18px;
    }
}
.write {
    margin: 20px auto;
    width: 331px;
    height: 449px;
    border-radius: 30px;
    border: 2px solid #e4ceb0;
    background: #ffffff;
    width: 600;
    .date {
        margin-top: 19px;
        display: flex;
        height: 23px;
        font-weight: 600;
        font-size: 15px;
        .exactDate {
            margin-right: 109px;
        }
    }
    .border {
        margin-top: 5px;
        margin-bottom: 20px;
        height: 2px;
        background-color: #E4CEB0;
    }
    textarea {
        padding-left: 20px;
        resize: none;
        width: 257px;
        height: 280px;
        // background-color: pink;
        background: url(../../assets/image/bianji.png) no-repeat;
        background-size: 18px;
        border: 0;
        color: #c0bdb7;
        font-size: 12px;
        font-weight: 550;
    }
    .pic {
        width: 75px;
    }
}
button {
    margin: 4px 100px;
    width: 190px;
    height: 46px;
    border-radius: 50px;
    border: 1px solid #ff9c29;
    background: #ffb45d;
    box-shadow: 4px 8px 8px 0 #fed09a;
    font-size: 20px;
    font-weight: 600;
}
</style>