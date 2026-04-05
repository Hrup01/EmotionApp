<template>
  <div id="clockIn">
    <nav-bar :title="navbarTitle" :margin-left="marginLeft"></nav-bar>
    <div class="header">
        <div class="integral wrapper">
            <p>我的积分</p>
            <div class="num">{{ integral }}</div>
            <img src="@/assets/image/打卡奖励/大星星.png" alt="">
        </div>
        <div class="daily-clock-in wrapper">
            <div class="title">每日打卡领奖励</div>
            <ul>
                <li v-for="item in starList" :key="item.id" ref="starItem">
                    <div class="subject">
                        <p>{{ item.addNum }}</p>
                        <img :src="item.url" alt="">
                    </div>
                    <p class="day">{{ item.day }}</p>
                </li>
            </ul>
            <div class="immediately-clock-in" ref="clockIn" @click="clockIn">立即打卡</div>
        </div>
    </div>
    <div class="footer wrapper">
        <div class="title">做任务领奖励</div>
        <ul>
            <li v-for="(item, index) in taskList" :key="item.id">
                <img :src="item.taskPicUrl" alt="">
                <div class="middle">
                    <div class="task">{{ item.desc }}</div>
                    <div class="award">
                        <img :src="item.starUrl" alt="" class="star">
                        <p>{{ item.addNum }}</p>
                    </div>
                </div>
                <div class="to-finish" ref="finish" @click="toFinish(index)">去完成</div>
            </li>
        </ul>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'clockIn',
    components: {
        NavBar
    },
    data () {
        return {
            navbarTitle: '打卡奖励',
            marginLeft: '118px',
            integral: 200,
            starList: [
                { id: 0, url: require('@/assets/image/打卡奖励/星星1.png'), addNum: '+1', day: '今天', isClockIn: true },
                { id: 1, url: require('@/assets/image/打卡奖励/星星2.png'), addNum: '+2', day: '第2天', isClockIn: false },
                { id: 2, url: require('@/assets/image/打卡奖励/星星3.png'), addNum: '+3', day: '第3天', isClockIn: false },
                { id: 3, url: require('@/assets/image/打卡奖励/星星4.png'), addNum: '+4', day: '第4天', isClockIn: false },
                { id: 4, url: require('@/assets/image/打卡奖励/星星1.png'), addNum: '+5', day: '第5天', isClockIn: false },
                { id: 5, url: require('@/assets/image/打卡奖励/星星3.png'), addNum: '+6', day: '第6天', isClockIn: false },
                { id: 6, url: require('@/assets/image/打卡奖励/星星4.png'), addNum: '+7', day: '第7天', isClockIn: false },
            ],
            taskList: [
                { id: 0, taskPicUrl: require('@/assets/image/打卡奖励/日记.png'), starUrl: require('@/assets/image/打卡奖励/星星1.png'), addNum: '+20', integral: 20, desc: '写一篇日记', isFinish: false, toUrl: '/moodOption' },
                { id: 1, taskPicUrl: require('@/assets/image/打卡奖励/笔记.png'), starUrl: require('@/assets/image/打卡奖励/星星2.png'), addNum: '+20', integral: 20, desc: '发布一篇笔记', isFinish: false, toUrl: '/postNotes' },
                { id: 2, taskPicUrl: require('@/assets/image/打卡奖励/冥想.png'), starUrl: require('@/assets/image/打卡奖励/星星3.png'), addNum: '+20', integral: 20, desc: '进行一次冥想', isFinish: false, toUrl: '/meditation' },
                { id: 3, taskPicUrl: require('@/assets/image/打卡奖励/手账.png'), starUrl: require('@/assets/image/打卡奖励/星星4.png'), addNum: '+10', integral: 10, desc: '完成一次手账', isFinish: false, toUrl: '/#' },
                { id: 4, taskPicUrl: require('@/assets/image/打卡奖励/拼图.png'), starUrl: require('@/assets/image/打卡奖励/星星1.png'), addNum: '+10', integral: 10, desc: '完成一次情绪拼图', isFinish: false, toUrl: '/#' },
            ]
        }
    },
    methods: {
        // 每日打卡
        clockIn () {
            // console.log(this.$refs.clockIn)
            this.$refs.clockIn.style.background = '#fff'
            this.$refs.clockIn.style.color = '#666666'
            this.$refs.clockIn.style.border = '2px solid #ebebeb';
            this.integral += 1
        },
        // 完成任务获得奖励
        toFinish (id) {
            if (this.taskList[id].isFinish) return
            this.taskList[id].isFinish = true
            // 将积分添加到总积分中
            this.integral += this.taskList[id].integral
            // 跳转到对应页面
            // this.$router.push(`${this.taskList[id].toUrl}`)
            // 替换类名
            // console.log(this.$refs.finish[id])
            this.$refs.finish[id].classList.remove('to-finish')
            this.$refs.finish[id].classList.add('finished')
        },
        
    },
    mounted () {
        // const starItems = this.$refs.starItem
        const taskItems = this.$refs.finish
        // console.log(items)
        // const renderClockIn = () => {
        //     this.starList.map((item, index) => {
        //         if (!item.isClockIn) {
        //             // console.log(items[index].children[0])
        //             items[index].children[0].style.background = '#f2f2f2'
        //         }
        //     })
        // }
        // renderClockIn()
        // 渲染任务完成情况
        this.taskList.map((item, index) => {
            if (item.isFinish) {
                // console.log(taskItems[index])
                taskItems[index].classList.remove('to-finish')
                taskItems[index].classList.add('finished')
            }
        })
        
    }
}
</script>

<style lang="less" scoped>
#clockIn {
    width: 390px;
    height: 844px;
    background: #faf4e6;
}
.wrapper {
    margin: 0 auto;
    width: 366px;
}
.header {
    width: 390px;
    height: 340px;
    .integral {
        margin-top: 16px;
        padding-top: 16px;
        width: 366px;
        height: 110px;
        border-radius: 20px 20px 0 0;
        background: linear-gradient(26.8deg, #ffffff 0%, #fec38a 100%);
        box-shadow: inset 2px 6px 16px 0 #ffffffcc;
        p {
            margin-left: 32px;
            color: #000000a1;
            font-size: 12px;
        }
        .num {
            margin-left: 23px;
            color: #a65e00;
            font-size: 36px;
        }
        img {
            position: absolute;
            top: 52px;
            right: -5px;
            width: 150px;
            height: 140px;
        }
    }
    .daily-clock-in {
        margin-top: -10px;
        padding: 18px 10px;
        width: 366px;
        height: 213px;
        border-radius: 20px 20px 10px 10px;
        background: #ffffff;
        box-shadow: 0 -2px 10px 0 #00000033;
        .title {
            color: #000000cc;
        }
        ul {
            margin-top: 16px;
            display: flex;
            justify-content: space-between;
            li {
                .subject {
                    padding: 6px 0;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                    align-items: center;
                    width: 46px;
                    height: 60px;
                    border-radius: 7.71px;
                    background: linear-gradient(185.5deg, #ffd5056e 0%, #ffeb87 99%, #fca38b 100%);
                    img {
                        width: 30px;
                    }
                }
                p {
                    color: #8c5f31;
                    font-size: 12px;
                }
                .day {
                    margin-top: 6px;
                    font-size: 10px;
                    text-align: center;
                }
            }
        }
    }
    .immediately-clock-in {
        margin: 18px auto;
        width: 200px;
        height: 40px;
        border-radius: 30px;
        background: linear-gradient(151.6deg, #ffc300 30%, #ff8d1ae6 100%);
        color: #ffffff;
        line-height: 40px;
        text-align: center;
    }
}
.footer {
    width: 366px;
    height: 415px;
    border-radius: 20px;
    background: #ffffff;
    .title {
        margin-top: 18px;
        margin-left: 12px;
    }
    ul {
        margin-top: 16px;
        display: flex;
        flex-wrap: wrap;
        li {
            padding: 0 16px 0 28px;
            display: flex;
            align-items: center;
            width: 366px;
            height: 68px;
            img {
                width: 42px;
            }
            .middle {
                margin-left: 12px;
                width: 208px;
                .task {
                    color: #000000b3;
                    font-size: 14px;
                }
                .award {
                    display: flex;
                    align-items: center;
                    .star {
                        width: 20px;
                    }
                    p {
                        color: #7a7a7a;
                        font-size: 12px;
                    }
                }
            }
            .to-finish,
            .finished {
                width: 70px;
                height: 36px;
                border-radius: 30px;
                font-size: 14px;
                text-align: center;
            }
            .to-finish {
                background: #fced62;
                color: #000000ad;
                line-height: 36px;
            }
            .finished {
                border: 2px solid #ebebeb;
                color: #00000066;
                line-height: 33px;
            }
        }
    }
}
</style>