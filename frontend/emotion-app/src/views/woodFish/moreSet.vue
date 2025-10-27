<template>
  <div id="moreSet">
    <nav-bar :left-pic="navbarLeftPic"></nav-bar>
    <div class="body wrapper">
        <div class="strikeStuff">
            <div class="title">敲击物品</div>
            <ul>
                <li v-for="(item, index) in stuffList" :key="item.id" @click="changeStuff(index)">
                    <img :src="item.url" alt="">
                </li>
            </ul>
        </div>
        <div class="changeScene">
            <div class="title">
                <p>更换场景</p>
                <img src="@/assets/image/木鱼/更换.png" alt="">
            </div>
            <ul>
                <li v-for="(item, index) in sceneList" :key="item.id" @click="changescene(index)">
                    <img :src="item.url" alt="">
                </li>
            </ul>
        </div>
        <div class="tappingMode">
            <div class="title">敲打模式</div>
            <div class="subject">
                <div class="top">
                    <div class="hand" @click="cutHand">手动</div>
                    <div class="auto" @click="cutAuto">自动</div>
                    <div class="selected" ref="selected"></div>
                </div>
                <div class="bottom" v-show="isAuto">
                    <p class="gap">间隔时长</p>
                    <img src="@/assets/image/木鱼/减.png" alt="" class="reduce" @click="reduceTime">
                    <div class="time">{{ time }}</div>
                    <img src="@/assets/image/木鱼/加.png" alt="" class="add" @click="addTime">
                    <p class="second">秒</p>
                </div>
            </div>
        </div>
        <div class="optionTiming">
            <div class="title">选择定时</div>
            <ul>
                <li v-for="(item, index) in timeList" :key="item.id">
                    <p>{{ item.text }}</p>
                    <div class="pic" @click="changeCheck(index)">
                        <img src="@/assets/image/木鱼/选择圈_未选.png" alt="" v-if="!item.isCheck">
                        <img src="@/assets/image/木鱼/选择圈_已选.png" alt="" v-else>
                    </div>
                </li>
            </ul>
        </div>
        <div class="sound">
            <div class="title">声音</div>
            <div class="subject">
                <div class="item" v-for="(item, index) in soundList" :key="item.id" @click="setSound(index)">
                    <img src="@/assets/image/木鱼/声音.png" alt="" class="icon">
                    <p>{{ item.text }}</p>
                    <img src="@/assets/image/木鱼/开.png" alt="" class="open" v-if="item.isOpen">
                    <img src="@/assets/image/木鱼/关.png" alt="" class="close" v-else>
                </div>
                <!-- <div class="item"></div> -->
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
        navbarLeftPic: require('@/assets/image/木鱼/关闭.png'),
        time: 2.5,
        isAuto: true,
        stuffList: [
            { id: 0, url: require('@/assets/image/木鱼/大木鱼.png'), audio: '' },
            { id: 1, url: require('@/assets/image/木鱼/铃铛.png'), audio: '' },
            { id: 2, url: require('@/assets/image/木鱼/敲击物品_2.png'), audio: '' },
            { id: 3, url: require('@/assets/image/木鱼/敲击物品_3.png'), audio: '' },
        ],
        sceneList: [
            { id: 0, url: require('@/assets/image/木鱼/场景1.png'), remark: '' },
            { id: 1, url: require('@/assets/image/木鱼/场景2.png'), remark: '' },
            { id: 2, url: require('@/assets/image/木鱼/场景3.png'), remark: '' },
            { id: 3, url: require('@/assets/image/木鱼/添加.png'), remark: 'none' },
        ],
        timeList: [
            { id: 0, text: '永久', isCheck: false, totalSecond: '' },
            { id: 1, text: '30分钟', isCheck: false, totalSecond: '1800' },
            { id: 2, text: '60分钟', isCheck: false, totalSecond: '3600' },
            { id: 3, text: '自定义', isCheck: false, totalSecond: '' },
        ],
        soundList: [
            { id: 0, text: '音效', isOpen: true },
            { id: 1, text: '震动', isOpen: false },
        ],
      }
    },
    methods: {
        // 更换物品
        changeStuff (id) {
            const stuffObj = this.stuffList[id]
            this.$store.commit('woodFish/setStrikeStuff',stuffObj)
        },
        // 更换场景
        changescene (id) {
            const sceneObj = this.sceneList[id]
            this.$store.commit('woodFish/setBackgroundUrl', sceneObj)
        },
        // 切换手动模式
        cutHand () {
            this.$refs.selected.style.transform = 'translateX(-132px)'
            this.isAuto = false
        },
        // 切换自动模式
        cutAuto () {
            this.$refs.selected.style.transform = 'translateX(0px)'
            this.isAuto = true
        },
        // 减少时间
        reduceTime () {
            if (this.time > 0.5) {
                this.time -= 0.5
            }
        },
        // 增加时间
        addTime () {
            this.time += 0.5
        },
        // 改变选择状态
        changeCheck (index) {
            this.timeList.map((item) => {
                if (item.isCheck) item.isCheck = false
            })
            this.timeList[index].isCheck = this.timeList[index].isCheck ? false : true
        },
        // 声音设置
        setSound (index) {
            this.soundList[index].isOpen = this.soundList[index].isOpen ? false : true
            if (!index) {
                this.$store.commit('woodFish/setAudio', this.soundList[index].isOpen)
            }else {
                this.$store.commit('woodFish/setShake', this.soundList[index].isOpen)
            }
        }
    },
    mounted () {
        // console.log(this.$store.state.woodFish.haveAudio)
        this.soundList[0].isOpen = this.$store.state.woodFish.haveAudio
        this.soundList[1].isOpen = this.$store.state.woodFish.isShake
    }
}
</script>

<style lang="less" scoped>
#moreSet {
  width: 390px;
  height: 844px;
  background-color: #FDF4E4;
  font-weight: 600;
}
.wrapper {
  margin: 0 auto;
  width: 366px;
}
.body {
    margin-top: 13px;
    padding: 10px 32px 0 28px;
    width: 366px;
    height: 718px;
    border-radius: 20px;
    background: #fff9ef;
    .title {
        color: #000000cc;
    }
    ul {
        margin-top: 10px;
        display: flex;
        justify-content: space-between;
    }
    .strikeStuff {
        ul {
            li {
                width: 60px;
                height: 60px;
                border-radius: 10px;
                border: 1px solid #ffcf73;
                background: #ffffff;    
            }
            li:nth-child(1) {
                img {
                    margin-top: 14px;
                    margin-left: 10px;
                    width: 40px;
                }
            }
            li:nth-child(2) {
                img {
                    margin-left: 15px;
                    width: 30px;
                }
            }
            li:nth-child(3) {
                img {
                    margin-top: 10px;
                    margin-left: 10px;
                    width: 40px;
                }
            }
            li:nth-child(4) {
                img {
                    margin-top: 15px;
                    margin-left: 10px;
                    width: 40px;
                }
            }
        }
    }
    .changeScene{
        margin-top: 18px;
        .title {
            display: flex;
            align-items: center;
            img {
                margin-left: 8px;
                width: 20px;
            }
        }
        ul {
            li {
                width: 70px;
                height: 124.28px;
                border-radius: 8px;
                img {
                    width: 70px;
                    height: 124.28px;
                    border-radius: 8px;
                }
                
            }
            // li:nth-child(1) {
            //     background-image: url('@/assets/image/木鱼/场景1.png');
            //     background-size: cover;
            // }
            // li:nth-child(2) {
            //     background-image: url('@/assets/image/木鱼/场景2.png');
            //     background-size: cover;
            // }
            // li:nth-child(3) {
            //     background-image: url('@/assets/image/木鱼/场景3.png');
            //     background-size: cover;
            // }
            li:nth-child(4) {
                background-color: #E1E1E1;
                img {
                    margin-top: 44px;
                    margin-left: 17px;
                    width: 36px;
                    height: 36px;
                }
            }
        }
    }
    .tappingMode {
        margin-top: 17px;
        .subject {
            margin-top: 8px;
            padding:  8px 22px 0 22px;
            width: 311px;
            height: 90px;
            border-radius: 10px;
            background: #ffffff;
            color: #000000cc;
            font-size: 14px;
            .top {
                position: relative;
                display: flex;
                width: 267px;
                height: 36px;
                border-radius: 10px;
                background: #e1e1e1b5;
                line-height: 36px;
                text-align: center;
                .hand,
                .auto {
                    position: relative;
                    width: 50%;
                    z-index: 1;
                }
                .selected {
                    position: absolute;
                    top: 4px;
                    right: 10px;
                    width: 115.18px;
                    height: 28px;
                    border-radius: 10px;
                    background: #ffce71;
                    z-index: 0;
                    transition: all 0.2s;
                }
            }
            .bottom {
                margin-top: 14px;
                display: flex;
                .gap {
                    margin-right: 100px;
                }
                img {
                    width: 20px;
                }
                .time {
                    margin: 0 12px;
                    width: 21px;
                    text-align: center;
                }
                .second {
                    margin-left: 8px;
                }
            }
        }
    }
    .optionTiming {
        margin-top: 16px;
        ul {
            margin-top: 7px;
            display: flex;
            flex-direction: column;
            width: 310px;
            height: 160px;
            border-radius: 10px;
            background: #ffffff;
            li {
                margin-left: 14px;
                display: flex;
                align-items: center;
                height: 40px;
                p {
                    width: 260px;
                    color: #000000b3;
                    font-size: 14px;
                }
                img {
                    width: 16px;
                }
            }
        }
    }
    .sound {
        margin-top: 16px;
        .subject {
            margin-top: 9px;
            display: flex;
            justify-content: space-between;
            .item {
                padding: 0 12px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                width: 128px;
                height: 34px;
                border-radius: 10px;
                border: 1px solid #ffce71;
                background: #ffffff;
                .icon {
                    height: 20px;
                }
                p {
                    width: 32px;
                    color: #000000cc;
                    font-size: 14px;
                }
                .open,
                .close {
                    width: 30px;
                }
            }
        }
    }
}
</style>