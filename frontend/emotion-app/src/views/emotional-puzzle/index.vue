<template>
  <div id="emotional-puzzle">
    <div class="header">
        <NavBar :title="NavBarTitle"></NavBar>
        <div class="subject wrapper">
            <div class="right">
                <div class="progress">{{ progress }}%</div>
            </div>
            <div class="left">
                <div class="look" @touchstart="lookAnswer" @touchend="cancelAnswer">
                    <img src="@/assets/image/拼图/查看.png" alt="">
                    <p>查看</p>
                </div>
                <div class="option" @click="$router.push('/puzzleOption')">
                    <img src="@/assets/image/拼图/拼图选择.png" alt="">
                    <p>拼图选择</p>
                </div>
            </div>
        </div>
    </div>
    <div class="body">
        <div class="skew-bg"></div>
        <div class="bg">
            <div class="shell">
                <div class="time">
                    <img src="@/assets/image/拼图/计时.png" alt="">
                    <p>{{ hour }}.{{ minute }}.{{ second }}</p>
                </div>
                <ul>
                    <li v-for="(item, index) in shuffledPieces" :key="item.originalIndex" @touchstart="startMove(index, $event)" @touchend="endMove(index)" @touchmove="moving">
                        <img :src="item.url" alt="">
                    </li>
                </ul>
            </div>
            <div class="function">
                <div class="back">
                    <img src="@/assets/image/拼图/按键_左.png" alt="">
                </div>
                <div class="play">
                    <div class="start" v-if="isStart" @click="pausePlay">
                        <img src="@/assets/image/拼图/开始.png" alt="">
                    </div>
                    <div class="pause" v-else @click="startPlay">
                        <img src="@/assets/image/拼图/暂停.png" alt="">
                    </div>
                </div>
                <div class="restart" @click="reset">
                    <img src="@/assets/image/拼图/重置.png" alt="">
                </div>
            </div>
        </div>
    </div>
    <div class="hint" v-show="isSave">
      <div class="subject">
        <div class="title">是否保存拼图记录</div>
        <div class="button">
            <div class="no item" @click="doNotSave">否</div>
            <div class="yes item" @click="saving">是</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import axios from 'axios'
export default {
    name: 'emotionalPuzzle',
    components: {
        NavBar
    },
    data () {
        return {
            token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
            NavBarTitle: '情绪拼图',
            progress: 0,
            totalSecond: 1,
            hour: '00',
            minute: '00',
            second: '00',
            timer: null,
            // 拼图碎片对应的正确位置
            originalPieces: [
                { originalIndex: 0, url: require('@/assets/image/情绪拼图切割/简单2/part_01.png') },
                { originalIndex: 1, url: require('@/assets/image/情绪拼图切割/简单2/part_02.png') },
                { originalIndex: 2, url: require('@/assets/image/情绪拼图切割/简单2/part_03.png') },
                { originalIndex: 3, url: require('@/assets/image/情绪拼图切割/简单2/part_04.png') },
                { originalIndex: 4, url: require('@/assets/image/情绪拼图切割/简单2/part_05.png') },
                { originalIndex: 5, url: require('@/assets/image/情绪拼图切割/简单2/part_06.png') },
                { originalIndex: 6, url: require('@/assets/image/情绪拼图切割/简单2/part_07.png') },
                { originalIndex: 7, url: require('@/assets/image/情绪拼图切割/简单2/part_08.png') },
                { originalIndex: 8, url: require('@/assets/image/情绪拼图切割/简单2/part_09.png') },
                { originalIndex: 9, url: require('@/assets/image/情绪拼图切割/简单2/part_10.png') },
                { originalIndex: 10, url: require('@/assets/image/情绪拼图切割/简单2/part_11.png') },
                { originalIndex: 11, url: require('@/assets/image/情绪拼图切割/简单2/part_12.png') },
                { originalIndex: 12, url: require('@/assets/image/情绪拼图切割/简单2/part_13.png') },
                { originalIndex: 13, url: require('@/assets/image/情绪拼图切割/简单2/part_14.png') },
                { originalIndex: 14, url: require('@/assets/image/情绪拼图切割/简单2/part_15.png') },
                { originalIndex: 15, url: require('@/assets/image/情绪拼图切割/简单2/part_16.png') },
            ],
            // 打乱后的碎片数组 --- 最终渲染
            shuffledPieces: [],
            resetBackups: [], // 备份数组，用于重置拼图
            lookBackups: [], // 备份数组，用于查看原拼图后恢复当前进度
            // 开始计时
            isStart: false,
            // 移动拼图碎片相关
            activeIndex: null, // 正在移动的碎片索引
            startX: 0,
            startY: 0,
            currentX: 0,
            currentY: 0,
            // 是否保存记录
            isSave: false
        }
    },
    methods: {
        // 渲染页面倒计时
        render () {
            this.hour = Math.floor(this.totalSecond / 60 / 60)
            this.minute = Math.floor(this.totalSecond / 60)
            this.second = this.totalSecond % 60
            // 补0
            this.hour = this.hour < 10 ? '0' + this.hour : this.hour
            this.minute = this.minute < 10 ? '0' + this.minute : this.minute === 60 ? '00' : this.minute
            this.second = this.second < 10 ? '0' + this.second : this.second
            },
        // 开始拼图 + 定时功能
        startPlay () {
            this.isStart = true
            this.timer = setInterval(() => {
                this.render()
                this.totalSecond ++
            }, 1000)
        },
        // 暂停拼图
        pausePlay () {
            this.isSave = true
            this.isStart = false
            clearInterval(this.timer)
        },
        // Fisher-Yates洗牌算法：公平打乱数组
        shufflePieces () {
            // 复制原数组，避免修改原数据
            const temPieces = this.originalPieces
            // console.log(temPieces)
            // 从最后一位往前遍历，每次随机交换当前位与前面任意位
            for (let i = temPieces.length - 1; i > 0; i--) {
                const randomIdx = Math.floor(Math.random() * (i + 1))
                // 交换两个碎片
                // [temPieces[i], temPieces[randomIdx]] = [temPieces[randomIdx], temPieces[i]]
                const temi = temPieces[randomIdx]
                temPieces[randomIdx] = temPieces[i]
                temPieces[i] = temi
            }
            // 赋值给渲染用的数组
            this.shuffledPieces = temPieces
            // console.log(this.shuffledPieces)
            // 将这种排列情况存到本地存储中
            localStorage.setItem('random-puzzle', JSON.stringify(this.shuffledPieces))
            // 备份数组，用于重置拼图
            this.backupsPieces = JSON.stringify(this.shuffledPieces)
        },
        // 移动拼图
        startMove (index, e) {
            if (!this.isStart) return this.$toast('要按开始按钮哦')
            // console.log(index, e.touches[0])
            // 记录初始按下的碎片索引和x,y
            this.activeIndex = index
            this.startX = e.touches[0].clientX
            this.startY = e.touches[0].clientY
        },
        moving (e) {
            if (!this.isStart) return
            // console.log(22)
            // 记录现在的x,y
            this.currentX = e.touches[0].clientX
            this.currentY = e.touches[0].clientY
        },
        endMove () {
            if (!this.isStart) return
            // console.log(33)
            // 计算差值
            const x = this.currentX - this.startX
            const y = this.currentY - this.startY
            // console.log('x: ', x,'y: ', y)
            const arr = this.shuffledPieces
            // 1.左右移动 
            // 如果|x| > 60px && |x| > |y| ---右移
            if (x > 55 && Math.abs(x) > Math.abs(y)) {
                // console.log('右移')
                // 与下一个元素交换索引
                // 如果索引值为15 or 3 or 7 or 11 -- return
                if (this.activeIndex % 4 === 3) return
                const a = arr[this.activeIndex]
                const b = arr[this.activeIndex + 1]
                arr.splice(this.activeIndex, 1, b)
                arr.splice(this.activeIndex + 1, 1, a)
            }else if (x < -55 && Math.abs(x) > Math.abs(y)) {
                // 如果索引值为12 or 0 or 4 or 8 -- return
                if (this.activeIndex % 4 === 0) return
                const a = arr[this.activeIndex]
                const b = arr[this.activeIndex - 1]
                arr.splice(this.activeIndex, 1, b)
                arr.splice(this.activeIndex - 1, 1, a)
            }else if (y > 55 && Math.abs(y) > Math.abs(x)) {
                // 如果索引值为12 - 15 -- return
                if (this.activeIndex <= 15 && this.activeIndex >= 12) return
                const a = arr[this.activeIndex]
                const b = arr[this.activeIndex + 4]
                arr.splice(this.activeIndex, 1, b)
                arr.splice(this.activeIndex + 4, 1, a)
            } else if (y < -55 && Math.abs(y) > Math.abs(x)) {
                // 如果索引值为0 - 3 -- return
                if (this.activeIndex <= 3 && this.activeIndex >= 0) return
                const a = arr[this.activeIndex]
                const b = arr[this.activeIndex - 4]
                arr.splice(this.activeIndex, 1, b)
                arr.splice(this.activeIndex - 4, 1, a)
            }
            this.calculateProgress()
        },
        // 不保存拼图
        doNotSave () {
            this.isSave = false
            // 继续计时
            this.startPlay()
        },
        // 保存
        saving () {
            this.isSave = false
            // 将当前进度保存到本地存储当中
            localStorage.setItem('random-puzzle', JSON.stringify(this.shuffledPieces))
            localStorage.setItem('random-puzzle-progress', this.progress)
            localStorage.setItem('random-puzzle-time', this.totalSecond)
        },
        // 重置
        reset () {
            // 1.重置拼图
            this.shuffledPieces = JSON.parse(this.backupsPieces)
            // 2.暂停拼图 重置时间
            clearInterval(this.timer)
            this.totalSecond = 1
            this.hour = '00'
            this.minute = '00'
            this.second = '00'
            if (this.isStart) this.isStart = false
        },
        // 查看答案
        lookAnswer () {
            this.lookBackups = this.shuffledPieces
            this.shuffledPieces = this.originalPieces
        },
        cancelAnswer () {
            this.shuffledPieces = this.lookBackups
            this.lookBackups = []
        },
        // 判断进度
        calculateProgress () {
            let matchedCount = 0
            this.shuffledPieces.forEach((piece, index) => {
                if (piece.originalIndex === this.originalPieces[index].originalIndex) {
                    matchedCount ++
                }
            })
            this.progress = (matchedCount / this.originalPieces.length) * 100
            // console.log('progress', this.progress)
        }
    },
    async mounted () {
        // 开始就判断
        this.calculateProgress()
        // this.shufflePieces ()
        if (!localStorage.getItem('random-puzzle')) return this.shufflePieces ()
        if (!localStorage.getItem('random-puzzle-progress')) this.progress = 0
        else this.progress = localStorage.getItem('random-puzzle-progress')
        if (!localStorage.getItem('random-puzzle-time')) this.totalSecond = 0
        else this.totalSecond = JSON.parse(localStorage.getItem('random-puzzle-time'))
        this.shuffledPieces = JSON.parse(localStorage.getItem('random-puzzle'))
        // 备份数组，用于重置拼图
        this.backupsPieces = JSON.stringify(this.shuffledPieces)
        // console.log(this.shuffledPieces)
        // 获取拼图碎片
        const res = await axios.get('http://localhost:8080/api/puzzle/image/rank/rank1/url', {
            headers: {
                Authorization: 'Bearer ' + this.token
            }
        })
        console.log(res)
        // this.originalPieces = res.data.parts
        // console.log(this.originalPieces)
    },
    // updated () {
    //     // 判断操作后的数组与原数组有无索引相同的,若有,则数量*6.25

    // }
}
</script>

<style lang="less" scoped>
#emotional-puzzle {
    width: 390px;
    height: 852px;
    background: #feefce;
}
.wrapper {
    margin: 0 auto;
    width: 342px;
}
.header {
    width: 390px;
    height: 220px;
    border-radius: 0 0 20px 20px;
    background: #f3d084;
    .subject {
        margin-top: 20px;
        display: flex;
        .right {
            width: 201px;
            .progress {
                width: 100px;
                height: 56px;
                border-radius: 10px;
                border: 2px solid #0000005c;
                background: #ffffffe6;
                font-size: 18px;
                line-height: 56px;
                text-align: center;
            }
        }
        .left {
            display: flex;
            flex: 1;
            justify-content: space-between;
            .look,
            .option {
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                width: 56px;
                height: 56px;
                border-radius: 10px;
                border: 2px solid #0000005c;
                background: #ffffffe6;
                p {
                    color: #000000cc;
                    font-size: 12px;
                }
            }
            .look {
                img {
                    width: 34px;
                }
            }
            .option {
                img {
                    width: 28px;
                }
            }
        }
    }
}
.body {
    position: absolute;
    top: 190px;
    width: 390px;
    height: 844px;
    .skew-bg {
        position: absolute;
        top: 6px;
        margin-left: 22px;
        width: 348px;
        height: 590px;
        border-radius: 20px;
        background: #e8ddb3;
        transform: rotate(-3deg);
        z-index: 0;
    }
    .bg {
        position: absolute;
        padding: 20px;
        top: 14px;
        margin-left: 24px;
        width: 342px;
        height: 570px;
        border-radius: 20px;
        background: #f7f3dc;
        z-index: 1;
        .shell {
            padding: 20px 10px;
            width: 302px;
            height: 430px;
            border-radius: 10px;
            background: #ffffff;
            .time {
                display: flex;
                align-items: center;
                img {
                    margin-right: 3px;
                    width: 24px;
                }
            }
            ul {
                margin-top: 10px;
                display: flex;
                flex-wrap: wrap;
                // justify-content: space-between;
                li {
                    margin: 0 2px;
                    img {
                        width: 66px;
                    }
                }
            }
        }
        .function {
            margin-top: 20px;
            padding: 0 36px;
            display: flex;
            justify-content: space-between;
            width: 302px;
            height: 65px;
            border-radius: 65px;
            border: 2px solid #0000006e;
            background: #ffffffe0;
            img {
                margin-top: 12px;
                width: 42px;
            }
        }
    }
}
.hint {
    position: absolute;
    top: 0;
    width: 390px;
    height: 852px;
    background: #00000080;
    z-index: 2;
    .subject {
        margin: 336px auto;
        width: 342px;
        height: 133px;
        border-radius: 10px;
        background: #fffffff5;
        .title {
        margin-top: 18px;
        text-align: center;
        color: #000000cc;
        font-size: 18px;
        }
        .button {
        margin: 24px auto;
        display: flex;
        justify-content: space-between;
        width: 240px;
        .item {
            width: 100px;
            height: 40px;
            border-radius: 8px;
            border: 1px solid #0000003d;
            line-height: 38px;
            text-align: center;
            font-size: 18px;
        }
        .no {
            background: #f3cf80;
        }
        }
    }
}
</style>