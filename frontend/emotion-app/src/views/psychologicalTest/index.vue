<template>
  <div id="psychologicalTestIndex">
    <NavBar :title="title"></NavBar>
    <router-link to="/historyTest" class="to-history-test">
        <img src="@/assets/image/测评/历史测评.png" alt="">
        <p>历史测评</p>
    </router-link>
    <div class="title">轻拥情绪，舒缓身心</div>
    <div class="bg"><img src="@/assets/image/测评/插图.png" alt=""></div>
    <div class="subject">
        <p>本测评共10题，旨在快速评估近期焦虑情绪状态，提供情绪调节建议，每题请根据近2周内的实际感受，从选项中选择最符合的一项</p>
        <router-link to="/psychologicalTest">开始填写</router-link>
    </div>
    <div class="statement">
        <p>测评结果仅作参考，不构成任何专业医疗或心理建议。</p>
        <p>如遇危机，请立即寻求专业帮助。</p>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'psychologicalTestIndex',
    components: {
        NavBar
    },
    data () {
        return {
            title: '心理测评'
        }
    }
}
</script>

<style lang="less" scoped>
#psychologicalTestIndex {
    width: 390px;
    height: 852px;
    background: #faf4e6;
}
.to-history-test {
    position: fixed;
    top: 55px;
    right: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    img {
        width: 22px;
    }
    p {
        font-size: 12px;
    }
}
.title {
    margin-top: 40px;
    margin-left: 26px;
    color: #543a00;
    font-size: 20px;
}
.bg {
    margin: 0 auto;
    width: 256px;
    img {
        margin-top: 54px;
        width: 100%;
    }
}
.subject {
    margin: 0 auto;
    padding: 20px;
    width: 319px;
    height: 200px;
    border-radius: 15.38px;
    background: #f4e3bc;
    p {
        margin-bottom: 45px;
        color: #000000b3;
        font-size: 14px;
    }
    a {
        margin: 0 auto;
        display: block;
        width: 250px;
        height: 56px;
        border-radius: 60px;
        background: #f3cf80;
        line-height: 56px;
        text-align: center;
        font-size: 18px;
    }
}
.statement {
    margin: 0 auto;
    margin-top: 120px;
    width: 240px;
    color: #00000080;
    font-size: 10px;
    text-align: center;
}
</style>