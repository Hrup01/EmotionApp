<template>
    <div id="diaryDetail">
        <NavBar :is-diary-note-book="isDiaryNoteBook" :title="navbarTitle" :margin-left="marginLeft"></NavBar>
        <div class="optionDate">
            <p>{{ currentMonth }}月</p>
            <img src="@/assets/image/日记本/下拉时间选择.png" alt="" @click="showOption = showOption ? false : true">
        </div>
        <div class="option-month" v-show="showOption">
            <ul>
                <li @click="changeMonth(Number(otherMonth.first), 1)">{{ otherMonth.first }}月</li>
                <li @click="changeMonth(Number(otherMonth.last), 2)">{{ otherMonth.last }}月</li>
            </ul>
        </div>
        <!-- 虚拟列表
         <div class="virtual-list-container"></div> -->
        <div class="list">
            <!-- 时间轴 -->
            <div class="timer-shaft"></div>
            <div class="item" v-for="item in diaryList" :key="item.id">
                <div class="time">
                    <div class="time-node"></div>
                    <div class="time-detail">{{ item.date }}</div>
                </div>
                <div class="diary">
                    <div class="title">{{ item.title }}</div>
                    <div class="content">{{ item.content }}</div>
                    <!-- 如果有图片则渲染 -->
                    <div class="pic">
                        <img :src="item.urlOne" alt="" v-show="item.urlOne">
                        <img :src="item.urlTwo" alt="" v-show="item.urlTwo">
                        <img :src="item.urlThree" alt="" v-show="item.urlThree">
                    </div>
                </div>
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
            isDiaryNoteBook: true,
            navbarTitle: '2025-冬',
            marginLeft: '120px',
            currentMonth: '01',
            showOption: false,
            otherMonth: {
                first: '02',
                last: '03'
            },
            diaryList: [
                { id: 0, date: '2025/10/01', title: '人生如旅', content: '那一年我执着于寻找答案，在每处风景里追问意义。直到某个寻常的黄昏，我忽然明白——生命不是等待风暴过去，而是学会在雨中起舞。所有的跋涉都不是徒劳，它们最终都会沉淀成生命的厚度。', urlOne: '', urlTwo: '' },
                { id: 1, date: '2025/10/03', title: '纯粹时刻', content: '今天只拍了天空。没有山峦剪影，没有飞鸟点缀，只有夕阳在云层后静静燃烧。我突然意识到，自己总是在寻找完美的构图，却忘了最美的从来无需装饰。就像此刻的心情，简单，却足够丰盈。', urlOne: require('@/assets/image/日记本/示例图_单图.png'), urlTwo: '', urlThree: '' },
                { id: 2, date: '2025/10/07', title: '海边的独白', content: '翻看这些照片，耳边又响起浪涛声。那时的我正被各种选择困扰，而眼前这片辽阔的夕阳让我明白：所有的焦虑都太自以为是。夕阳每天沉入海底，第二天照样升起，我何必执着于每一个决定都必须正确。', urlOne: require('@/assets/image/日记本/示例图_双图1.png'), urlTwo: require('@/assets/image/日记本/示例图_双图2.png'), urlThree: '' },
                { id: 3, date: '2025/10/09', title: '人生如旅', content: '那一年我执着于寻找答案，在每处风景里追问意义。直到某个寻常的黄昏，我忽然明白——生命不是等待风暴过去，而是学会在雨中起舞。所有的跋涉都不是徒劳，它们最终都会沉淀成生命的厚度。', urlOne: require('@/assets/image/日记本/示例图_三图1.png'), urlTwo: require('@/assets/image/日记本/示例图_三图2.png'), urlThree: require('@/assets/image/日记本/示例图_三图3.png') },
                // { id: 4, date: '2025/10/01', title: '人生如旅', content: '那一年我执着于寻找答案，在每处风景里追问意义。直到某个寻常的黄昏，我忽然明白——生命不是等待风暴过去，而是学会在雨中起舞。所有的跋涉都不是徒劳，它们最终都会沉淀成生命的厚度。', urlOne: '', urlTwo: '', urlThree: '' },
                // { id: 5, date: '2025/10/01', title: '人生如旅', content: '那一年我执着于寻找答案，在每处风景里追问意义。直到某个寻常的黄昏，我忽然明白——生命不是等待风暴过去，而是学会在雨中起舞。所有的跋涉都不是徒劳，它们最终都会沉淀成生命的厚度。', urlOne: '', urlTwo: '', urlThree: '' },
            ]
        }
    },
    methods: {
        // 改变展示月份
        changeMonth (month, id) {
            // 补零操作
            month = month < 10 ? '0' + month : month
            // 改变选项中的月份 -- 判断大小
             if (id === 1) {
                if (Number(this.currentMonth) < Number(this.otherMonth.last)) {
                    // console.log('id = 1 -- 1', this.currentMonth, '<', this.otherMonth.last)
                    this.otherMonth.first = this.currentMonth
                    
                }else {
                    // console.log('id = 1 -- 2', this.currentMonth, '>', this.otherMonth.last)
                    this.otherMonth.first = this.otherMonth.last
                    this.otherMonth.last = this.currentMonth
                }
            }else if (id === 2) {
                if (Number(this.currentMonth) < Number(this.otherMonth.first)) {
                    console.log('id = 2 -- 1', this.currentMonth, '<', this.otherMonth.first)
                    this.otherMonth.last = this.otherMonth.first
                    this.otherMonth.first = this.currentMonth
                    
                }else {
                    console.log('id = 2 -- 2', this.currentMonth, '>', this.otherMonth.first)
                    // console.log(11)
                    this.otherMonth.last = this.currentMonth
                }
            }
            this.currentMonth = `${month}`
            // console.log('month: ', month)
            // console.log('currentMonth: ', this.currentMonth, 'first: ', this.otherMonth.first, 'last:', this.otherMonth.last)
            // console.log('----------------------------------------------')
            this.showOption = false
        }
    },
    mounted () {
        // 获取选择的年份和季节
        const year = this.$store.state.diary.year
        const season = this.$store.state.diary.season
        // console.log('year:', year, 'season:', season)
        if (season === 'spring') this.navbarTitle = `${year}-春`
        if (season === 'summer') this.navbarTitle = `${year}-夏`
        if (season === 'autumn') this.navbarTitle = `${year}-秋`
        if (season === 'winter') this.navbarTitle = `${year}-冬`
        // 获取一季度所包含的三个月份
        let month = this.$store.state.diary.month
        // console.log(month.firstMonth)
        // 若为空则默认春季 --- 01 - 03
        if (month.firstMonth) {
            this.currentMonth = month.firstMonth
            this.otherMonth.first = month.sencondMonth
            this.otherMonth.last = month.lastMonth
        }
    }
}
</script>

<style lang="less" scoped>
#diaryDetail {
  width: 390px;
  height: 844px;
  background: #feefce;
}
.optionDate {
    margin-top: 16px;
    display: flex;
    align-items: center;
    p {
        margin: 0 10px 0 12px;
        color: #000000cc;
    }
    img {
        width: 16px;
    }
}
.option-month {
    ul {
        position: absolute;
        width: 390px;
        height: 734px;
        background-color: #00000080;
        z-index: 2;
        li {
            width: 390px;
            height: 50px;
            background-color: #FEEFCE;
            line-height: 50px;
            text-align: center;
            color: #000000c9;
        }
    }
}
.list {
    margin-top: 17px;
    padding-left: 18px;
    width: 390px;
    height: 716px;
    // background-color: #fff;
    overflow-y: auto;
    .timer-shaft {
        position: fixed;
        margin-left: 6px;
        height: 844px;
        width: 3px;
        background: #c47729;
    }
    .item {
        .time {
            display: flex;
            .time-node {
                width: 14px;
                height: 14px;
                border-radius: 14px;
                background: #c47729;
            }
            .time-detail {
                margin-left: 10px;
                color: #000000bd;
                font-size: 12px;
            }
        }
    }
    .diary {
        margin: 10px 0 20px 24px;
        padding: 16px;
        width: 324px;
        border-radius: 16px;
        background-color: #fff;
        .title {
             color: #000000b3;
        }
        .content {
            margin-top: 8px;
            color: #000000b3;
            font-size: 12px;
        }
        .pic {
            margin-top: 18px;
            display: flex;
            justify-content: space-between;
            gap: 12px;
            // width: 170px;
        }
    }
}
</style>