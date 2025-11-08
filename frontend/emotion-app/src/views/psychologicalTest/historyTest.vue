<template>
  <div id="historyTest">
    <NavBar :title="title"></NavBar>
    <div class="body">
        <div class="item" v-for="item in resultList" :key="item.id">
            <div class="title">{{ item.title }}</div>
            <div class="time">{{ item.time }}</div>
            <div class="result">{{ item.result }}</div>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    components: {
        NavBar
    },
    data () {
        return {
            title: '历史测评',
            resultList: [
                { id: 0, title: '第3次测评', time: '测评完成时间 2025-10-30 21:40:18', result: '测评结果：焦虑情绪轻微，多为短期应激反应，通过休息、运动等方式可自行缓解，无需过度担心。' },
                { id: 1, title: '第2次测评', time: '测评完成时间 2025-10-19 21:40:18', result: '测评结果：焦虑情绪中度，可能影响部分日常生活，建议尝试正念冥想、白噪音舒缓、规律作息调整。' },
                { id: 2, title: '第1次测评', time: '测评完成时间 2025-10-10 21:40:18', result: '测评结果：焦虑情绪较明显，已对生活、工作或人际关系造成干扰，建议尽快联系心理医生或心理咨询师进行专业评估与干预。' }
            ]
        }
    }
}
</script>

<style lang="less" scoped>
#historyTest {
    width: 390px;
    height: 852px;
    background: #faf4e6;
}
.body {
    margin: 12px auto;
    padding: 20px;
    width: 339px;
    height: 700px;
    border-radius: 20px;
    background: #fdf9f0;
    overflow-y: auto;
    .item {
        margin-bottom: 20px;
        border-bottom: 2px solid #F7E1AF;
        .title {
            color: #000000c7;
            font-size: 18px;
        }
        .time {
            margin: 12px 0;
            color: #00000080;
            font-size: 12px;
        }
        .result {
            margin-bottom: 18px;
            color: #000000c7;
            font-size: 14px;
        }
    }
}
</style>