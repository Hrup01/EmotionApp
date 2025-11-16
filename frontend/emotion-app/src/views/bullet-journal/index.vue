<template>
  <div id="bulletJournal">
    <NavBar :title="navbarTitle" :margin-left="marginLeft"></NavBar>
    <div class="navbarRightPic">
        <img src="@/assets/image/更多1.png" alt="" class="more" @click="changeFlag">
        <div class="hiden" ref="hiden" v-show="flag">
            <div class="blackList" @click="blackList = true">
                <img src="@/assets/image/手账/删除本页.png" alt="">
                <p>删除手账</p>
            </div>
            <div class="report" @click="report = true">
                <img src="@/assets/image/手账/新增页面.png" alt="">
                <p>新增页面</p>
            </div>
        </div>
    </div>
    <div class="body" ref="body">
        <!-- canvas画布 -->
        <canvas ref="canvas" width="360" height="522" @touchstart="startPainting" @touchend="finishPainting" @touchmove="painting"></canvas>
        <!-- 文本域 -- 文字 -->
    </div>
    <div class="option">
        <img src="@/assets/image/手账/页面选择_左.png" alt="" class="prev">
        <div class="page">{{ currentPage }}/{{ totlePage }}</div>
        <img src="@/assets/image/手账/页面选择_右.png" alt="" class="next">
    </div>
    <!-- 功能 -->
    <div class="footer">
        <div class="material-library item" @click="isMaterialLibrary = true">
            <img src="@/assets/image/手账/素材库.png" alt="">
            <p>素材库</p>
        </div>
        <div class="text item" @click="checkText = checkText ? false : true">
            <img src="@/assets/image/手账/文本.png" alt="" v-if="!checkText">
            <img src="@/assets/image/手账/文本_选中.png" alt="" class="isCheck" v-else>
            <p>文本</p>
        </div>
        <div class="painting item" @click="isDrawing = true">
            <img src="@/assets/image/手账/涂鸦.png" alt="">
            <p>涂鸦</p>
        </div>
        <div class="add-pic item">
            <img src="@/assets/image/手账/添加图片.png" alt="">
            <p>添加图片</p>
        </div>
        <div class="template item" @click="isTemplate = true">
            <img src="@/assets/image/手账/手账模板.png" alt="">
            <p>手账模板</p>
        </div>
    </div>
    <!-- 素材库详情 -->
    <div class="material-library-detail" v-show="isMaterialLibrary">
        <div class="mask"></div>
        <div class="subject">
            <div class="top wrapper">
                <div class="cancel" @click="isMaterialLibrary = false">取消</div>
                <div class="title">素材库</div>
            </div>
            <ul class="wrapper">
                <li v-for="item in materialLibraryList" :key="item.id"><img :src="item.url" alt=""></li>
            </ul>
        </div>
    </div>
    <!-- 涂鸦详情 -->
    <div class="painting-detail" v-show="isDrawing">
        <div class="thickness">
            <img src="@/assets/image/手账/减.png" alt="" class="minus" @touchstart="tothin" @touchend="stopChangeThickness">
            <div class="num">{{ lineWidth }}</div>
            <img src="@/assets/image/手账/加.png" alt="" class="add" @touchstart="tofat" @touchend="stopChangeThickness">
        </div>
        <div class="top wrapper">
            <div class="cancel" @click="isDrawing = false">取消</div>
            <div class="title">涂鸦</div>
        </div>
        <ul>
            <li v-for="(item, index) in paintingList" :key="item.id"><img :src="item.url" alt="" :ref="item.ref" @click="changeColor(item.color, index, item.ref)"></li>
        </ul>
    </div>
    <!-- 手账模板详情 -->
    <div class="template-detail" v-show="isTemplate">
        <div class="top wrapper">
            <div class="cancel" @click="isTemplate = false">取消</div>
            <div class="title">更换内页</div>
        </div>
        <ul ref="tempalteBox" @click="changeTemplate">
            <!-- <li><img src="@/assets/image/手账/内页1.png" alt="" ref="firstPic"></li>
            <li><img src="@/assets/image/手账/内页2.png" alt="" ref="secondPic"></li>
            <li><img src="@/assets/image/手账/内页3.png" alt="" ref="lastPic"></li> -->
            <li v-for="item in templateList" :key="item.id" v-show="item.current"><img :src="item.url" alt=""></li>
        </ul>
        <div class="bottom">
            <img src="@/assets/image/手账/页面选择_左.png" alt="" class="prev" @click="switchPrevPage">
            <div class="page">{{ templateCurrentPage }}/{{ templateTotlePage }}</div>
            <img src="@/assets/image/手账/页面选择_右.png" alt="" class="next" @click="switchNextPage">
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'bulletJournal',
    components: {
        NavBar
    },
    data () { 
        return {
            navbarTitle: '我的手账',
            marginLeft: '115px',
            flag: false,
            // 当前手账页面
            currentPage: 1,
            // 总手账数量
            totlePage: 5,
            checkText: false,
            // 素材
            isMaterialLibrary: false,
            materialLibraryList: [
                { id: 0, url: require('@/assets/image/手账/素材1.png') },
                { id: 1, url: require('@/assets/image/手账/素材2.png') },
                { id: 2, url: require('@/assets/image/手账/素材3.png') },
                { id: 3, url: require('@/assets/image/手账/素材4.png') },
                { id: 4, url: require('@/assets/image/手账/素材5.png') },
                { id: 5, url: require('@/assets/image/手账/素材6.png') },
                { id: 6, url: require('@/assets/image/手账/素材7.png') },
                { id: 7, url: require('@/assets/image/手账/素材8.png') },
                { id: 8, url: require('@/assets/image/手账/素材9.png') },
                { id: 9, url: require('@/assets/image/手账/素材10.png') },
                { id: 10, url: require('@/assets/image/手账/素材11.png') },
                { id: 11, url: require('@/assets/image/手账/素材12.png') }
            ],
            // 涂鸦
            isDrawing: false,
            paintingList: [
                { id: 0, url: require('@/assets/image/手账/铅笔.png'), color: '#000', ref: 'black' },
                { id: 1, url: require('@/assets/image/手账/蜡笔_绿.png'), color: 'green', ref: 'green' },
                { id: 2, url: require('@/assets/image/手账/蜡笔_红.png'), color: 'red', ref: 'red' },
                { id: 3, url: require('@/assets/image/手账/蜡笔_黄.png'), color: 'yellow', ref: 'yellow' },
                { id: 4, url: require('@/assets/image/手账/蜡笔_橙6.png'), color: 'orange', ref: 'orange' },
                { id: 5, url: require('@/assets/image/手账/蜡笔_蓝.png'), color: 'blue', ref: 'blue' },
                { id: 6, url: require('@/assets/image/手账/蜡笔_紫.png'), color: 'purple', ref: 'purple' },
                { id: 7, url: require('@/assets/image/手账/橡皮.png'), color: '', ref: 'eraser' }
            ],
            // 模板
            isTemplate: false,
            templateList: [
                { id: 0, url: require('@/assets/image/手账/内页1.png'), isCheck: false, current: true, prev: false, next: false },
                { id: 1, url: require('@/assets/image/手账/内页2.png'), isCheck: false, current: true, prev: false, next: false },
                { id: 2, url: require('@/assets/image/手账/内页3.png'), isCheck: false, current: true, prev: false, next: false },
                { id: 3, url: require('@/assets/image/手账/内页4.png'), isCheck: false, current: false, prev: false, next: true },
                { id: 4, url: require('@/assets/image/手账/内页5.png'), isCheck: false, current: false, prev: false, next: true },
                { id: 5, url: require('@/assets/image/手账/内页6.png'), isCheck: false, current: false, prev: false, next: true },
                { id: 6, url: require('@/assets/image/手账/内页7.png'), isCheck: false, current: false, prev: false, next: false },
                { id: 7, url: require('@/assets/image/手账/内页8.png'), isCheck: false, current: false, prev: false, next: false },
                { id: 8, url: require('@/assets/image/手账/内页9.png'), isCheck: false, current: false, prev: false, next: false }
            ],
            templateCurrentPage: 1,
            templateTotlePage: 3,
            // 画布
            stroke: '#000',
            canvas: null,
            ctx: null,
            canvasOffesetX: 0,
            canvasOffesetY: 0,
            // 画笔粗细
            lineWidth: 5,
            // 是否开始画画
            isPainting: false,
            // 标记是否使用橡皮擦
            isEraser: false,
            timer: null,
        }
    },
    methods: {
        changeFlag () {
            this.flag = this.flag ? false : true
        },
        // 通过循环改变数组中(模板选中)显示状态
        changeState (fnum) {
            this.templateList.map((item) => {
                if (fnum === 0) {
                    if (item.id < 3) {
                        item.current = true
                        item.prev = false
                        item.next = false
                    }else if (item.id > 5) {
                        item.current = false
                        item.prev = false
                        item.next = false
                    }else {
                        item.current = false
                        item.prev = false
                        item.next = true
                    }
                }
                if (fnum === 3) {
                    if (item.id < 3) {
                        item.current = false
                        item.prev = true
                        item.next = false
                    }else if (item.id > 5) {
                        item.current = false
                        item.prev = false
                        item.next = true
                    }else {
                        item.current = true
                        item.prev = false
                        item.next = false
                    }
                }
                if (fnum === 6) {
                    if (item.id < 3) {
                        item.current = false
                        item.prev = false
                        item.next = false
                    }else if (item.id > 5) {
                        item.current = true
                        item.prev = false
                        item.next = false
                    }else {
                        item.current = false
                        item.prev = true
                        item.next = false
                    }
                }
            })
            // console.log(this.templateList)
        },
        // 模板部分切换上一页
        switchPrevPage () {
            if (this.templateCurrentPage === 1) return
            this.templateCurrentPage -= 1
            const urls = this.templateList.filter((item) => item.prev === true)
            const fnum = urls[0].id
            this.changeState (fnum)
        },
        // 模板部分切换下一页
        switchNextPage () {
            if (this.templateCurrentPage === this.templateTotlePage) return
            this.templateCurrentPage += 1
            const urls = this.templateList.filter((item) => item.next === true)
            const fnum = urls[0].id
            this.changeState (fnum)
        },
        // 更换模板
        changeTemplate (e) {
            // 1.选中模板出现边框 -- add('isCheck')
            // console.log(e.target.parentNode)
            // 1.1先清除其他选中样式
            const lis = document.querySelectorAll('#bulletJournal .template-detail ul li')
            // console.log(lis)
            lis.forEach((item) => {
                // console.log(item)
                if (item.classList.contains('isCheck')) item.classList.remove('isCheck')
            })
            // 1.2是否有 'isCheck'
            if (!e.target.parentNode.classList.contains('isCheck')) e.target.parentNode.classList.add('isCheck')
            else e.target.parentNode.classList.remove('isCheck')
            // 2.更换模板
            // console.log(e.target.src)
            this.$refs.body.style.background = `url('${e.target.src}')`
            this.$refs.body.style.backgroundSize = 'cover'
            this.$refs.canvas.style.background = 'transparent'
            this.isTemplate = false
        },
        // 开始画画
        startPainting (e) {
            const touch = e.touches[0]
            const x = touch.clientX - this.canvasOffesetX
            const y = touch.clientY - this.canvasOffesetY
            this.ctx.beginPath()
            this.ctx.moveTo(x, y)
            this.isPainting = true
        },
        // 结束画画
        finishPainting () {
            this.isPainting = false
        },
        // 正在画画
        painting (e) {
            if (!this.isPainting) return
            const touch = e.touches[0]
            const x = touch.clientX - this.canvasOffesetX
            const y = touch.clientY - this.canvasOffesetY
            // 如果是橡皮 --- 镂空
            if (this.isEraser) {
                this.ctx.globalCompositeOperation = 'destination-out'
                // console.log('是橡皮', this.isEraser)
            }else {
                // 重置为默认
                this.ctx.globalCompositeOperation = 'source-over'
                // 画笔颜色
                this.ctx.strokeStyle = this.stroke
                // console.log('不是橡皮', this.stroke)
            }
            // 画笔大小
            this.ctx.lineWidth = this.lineWidth
            // 线条末端线帽样式
            this.ctx.lineCap = 'round'
            // 使用直线连接子路径 -- lineTo
            this.ctx.lineTo(x, y)
            // 着色
            this.ctx.stroke()
        },
        // 改变画笔颜色
        changeColor (color, index, ref) {
            // console.log(index)
            // console.log(this.isEraser)
            if (index === this.paintingList.length - 1) {
                this.isEraser = true
            }
            else {
                this.isEraser = false
                this.stroke = color
            }
            // console.log(color)
            // 其他画笔恢复原位
            this.paintingList.forEach((item, idx) => {
                if (idx !== index) {
                    this.$refs[item.ref][0].style.bottom = '-25%'
                }
            })
            // 画笔突出
            // console.log(this.$refs[ref][0])
            this.$refs[ref][0].style.bottom = '-5%'
            
        },
        // 改变画笔粗细
        tothin () {
            // 一直按着 -- 一直增加
            this.timer = setInterval(() => {
                if (this.lineWidth > 1) this.lineWidth --
                else clearInterval(this.timer)
            }, 100)
        },
        tofat () {
            this.timer = setInterval(() => {
                if (this.lineWidth < 20) this.lineWidth ++
                else clearInterval(this.timer)
            }, 100)  
        },
        stopChangeThickness () {
            clearInterval(this.timer)
        },
    },
    mounted () {
        // 刚进来时手账区域为默认样式
        this.$refs.body.style.background = '#F8E294'
        this.$refs.body.style.backgroundSize = 'cover'
        this.$refs.canvas.style.background = '#fff'
        // 绘画功能
        this.canvas = this.$refs.canvas
        this.ctx = this.canvas.getContext('2d')
        const canvasRect = this.canvas.getBoundingClientRect()
        this.canvasOffesetX = canvasRect.left
        this.canvasOffesetY = canvasRect.top
        // this.canvas.width = window.innerWidth - this.canvasOffesetX
        // this.canvas.height = window.innerHeight - this.canvasOffesetY
        // 默认选中铅笔
        this.changeColor('#000', 0, 'black')
    }
}
</script>

<style lang="less" scoped>
#bulletJournal {
    width: 390px;
    height: 844px;
    background: url('@/assets/image/手账/背景.png') no-repeat;
    background-size: cover;
}
.wrapper {
    margin: 0 auto;
    display: flex;
    width: 363px;
    .cancel {
        margin-top: 13px;
        margin-left: 7px;
        color: #707070;
    }
    .title {
        margin-top: 12px;
        margin-left: 113px;
        font-size: 18px;
    }
}
.navbarRightPic {
    .more {
        position: fixed;
        top: 55px;
        right: 22px;
        width: 24px;
    }
    .hiden {
        margin-top: 6px;
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
            color: #5e5e5e;;
            font-size: 10.95px;
            font-weight: 600;
        }
        .blackList,
        .report {
            display: flex;
        }
    }
}
// 画布
.body {
    margin: 26px 0;
    padding: 32px 15px;
    width: 390px;
    height: 586px;
    background: url('@/assets/image/手账/内页1.png') no-repeat;
    background-size: cover;
    canvas {
        width: 360px;
        height: 522px;
        border-radius: 20px;
        // background: #ffffff;
    }
}
.option {
    margin: 0 auto;
    display: flex;
    align-items: center;
    width: 160px;
    img {
        width: 24px;
    }
    .page {
        width: 100px;
        height: 30px;
        border-radius: 20px;
        background: #ffe48c;
        line-height: 30px;
        text-align: center;
    }
}
.footer {
    margin-top: 10px;
    padding: 13px;
    display: flex;
    justify-content: space-between;
    width: 390px;
    height: 100px;
    background: #fceda7;
    img {
        margin-top: 5px;
        width: 36px;
    }
    .item {
        display: flex;
        flex-direction: column;
        align-items: center;
        p {
            margin-top: 10px;
            color: #000000cc;
            font-size: 12px;
        }
        .isCheck {
            margin-top: -1px;
        }
    }
}
// 素材库详情
.material-library-detail {
    position: fixed;
    bottom: 0;
    width: 390px;
    height: 844px;
    .mask {
        width: 390px;
        height: 484px;
        background: #0000004d;
    }
    .subject {
        width: 390px;
        height: 360px;
        background: #fceda7;
        ul {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            li {
                margin-top: 22px;
                display: flex;
                justify-content: center;
                align-items: center;
                width: 80px;
                height: 80px;
                border-radius: 16px;
                background: #ffffffcc;
            }
            li:nth-child(1),
            li:nth-child(8) {
                img {
                    width: 78px;
                }
            }
            li:nth-child(2),
            li:nth-child(7) {
                img {
                    width: 84px;
                }
            }
            li:nth-child(3),
            li:nth-child(6) {
                img {
                    width: 70px;
                }
            }
            li:nth-child(4) {
                img {
                    width: 75px;
                }
            }
            li:nth-child(9) {
                img {
                    width: 64px;
                }
            }
            li:nth-child(10) {
                img {
                    width: 58px;
                }
            }
            li:nth-child(11) {
                img {
                    width: 46px;
                }
            }
            li:nth-child(12) {
                img {
                    width: 59px;
                }
            }
        }
    }
}
// 涂鸦详情
.painting-detail {
    position: fixed;
    bottom: 0;
    width: 390px;
    height: 140px;
    background: #fceda7;
    .thickness {
        margin-left: 143px;
        position: fixed;
        bottom: 140px;
        display: flex;
        align-items: center;
        width: 97px;
        img {
            width: 20px;
        }
        .num {
            width: 57px;
            text-align: center;
            color: #000000cc;
            font-size: 14px;
        }
    }
    ul {
        margin-top: 12px;
        padding: 0 6px;
        display: flex;
        justify-content: space-between;
        height: 100px;
        img {
            position: absolute;
            bottom: -25%;
            width: 40px;
        }
        li {
            width: 40px;
        }
        li:nth-child(1) {
            width: 50px;
            img {
                width: 50px;
            }
        }
        li:nth-child(8) {
            width: 60px;
            img {
                width: 60px;
            }
        }
    }
}
.template-detail {
    position: fixed;
    bottom: 0;
    width: 390px;
    height: 250px;
    background: #fceda7;
    ul {
        margin: 14px auto;
        display: flex;
        justify-content: space-between;
        // gap: 22px;
        width: 344px;
        height: 160px;
        // overflow: hidden;
        li {
            flex-shrink: 0;
            width: 106px;
            img {
                margin-top: 3px;
                width: 100px;
            }
        }
        .isCheck {
            img {
                margin-top: 0;
                width: 106px;
                border: 6px solid #f7b12f;
                border-radius: 4px;
            }
        }
    }
    .bottom {
        margin: -6px auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 145px;
        img {
            width: 24px;
        }
        .page {
            color: #000000cc;
            font-size: 14px;
        }
    }
}
</style>