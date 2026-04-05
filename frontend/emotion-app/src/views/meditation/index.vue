<template>
  <div id="meditation">
    <nav-bar :title="navbarTitle"></nav-bar>
    <div class="body wrapper">
        <div class="basis">
            <ul>
                <li class="item" v-for="item in basisList" :key="item.id">
                    <audio :src="item.audioUrl"></audio>
                    <div class="top"><img :src="item.bgUrl" alt=""></div>
                    <div class="bottom">
                        <img src="@/assets/image/冥想/开始.png" alt="">
                        <div class="text">
                            <p class="time">{{ item.time }}</p>
                            <p>{{ item.text }}</p>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
        <div class="title">
            <img src="@/assets/image/冥想/推荐.png" alt="">
            <p>推荐给你</p>
        </div>
        <div class="recommend">
            <ul>
                <li class="item" v-for="item in recommendList" :key="item.id">
                    <audio :src="item.audioUrl" ref="audio"></audio>
                    <div class="top"><img :src="item.bgUrl" alt=""></div>
                    <div class="bottom">
                        <img src="@/assets/image/冥想/开始.png" alt="" @click="paly">
                        <div class="text">
                            <p class="time">{{ item.time }}</p>
                            <p>{{ item.text }}</p>
                        </div>
                        <!-- <audio src="@/assets/audio/冥想/10分钟观呼吸.mp3"></audio> -->
                    </div>
                </li>
            </ul>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'meditationPage',
    components: {
        NavBar
    },
    data () {
        return {
            navbarTitle: '冥想',
            basisList: [
                { id: 0, bgUrl: require('@/assets/image/冥想/晨起冥想.png'), audioUrl: require('@/assets/audio/冥想/晨起冥想9分钟.mp3'), time: '9分钟', text: '晨起冥想练习' },
                { id: 1, bgUrl: require('@/assets/image/冥想/呼吸练习.png'), audioUrl: require('@/assets/audio/冥想/10分钟观呼吸.mp3'), time: '10分钟', text: '观呼吸冥想练习' },
                { id: 2, bgUrl: require('@/assets/image/冥想/潜意识冥想.png'), audioUrl: require('@/assets/audio/冥想/20分钟整理潜意识冥想.mp3'), time: '20分钟', text: '整理潜意识冥想' },
                { id: 3, bgUrl: require('@/assets/image/冥想/睡前冥想.png'), audioUrl: require('@/assets/audio/冥想/30分钟入睡冥想.mp3'), time: '30分钟', text: '深度睡前冥想' },
            ],
            recommendList: [
                { id: 1, bgUrl: require('@/assets/image/冥想/推荐_黄底.png'), audioUrl: require('@/assets/audio/冥想/晨起冥想9分钟.mp3'), time: '10分钟', text: '练习与现在的样子和平相处' },
                { id: 2, bgUrl: require('@/assets/image/冥想/推荐_绿底.png'), audioUrl: require('@/assets/audio/冥想/晨起冥想9分钟.mp3'), time: '30分钟', text: '【内观呼吸】口令引导' },
            ]
        }
    },
    methods: {
        paly () {
            this.$refs.audio.paly()
            console.log(this.$refs.audio)
        }
    }
}
</script>

<style lang="less" scoped>
#meditation {
    width: 390px;
    height: 852px;
    background: #fdf4e4;
    font-weight: 600;
}
.wrapper {
    margin: 0 auto;
    width: 366px;
}
.body {
    margin-top: 12px;
    padding: 16px 11px;
    width: 366px;
    height: 651px;
    border-radius: 20px;
    background: #fff9ef;
    ul {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        .item {
            margin-bottom: 18px;
            width: 163px;
            height: 180px;
            border-radius: 20px;
            // background: #f9d6de;
            .top {
                position: relative;
                width: 100%;
                height: 130px;
                img {
                    position: absolute;
                    right: 0;
                    bottom: 0;
                    width: 109px;
                    height: 108px;
                }
            }
            .bottom {
                display: flex;
                align-items: center;
                width: 100%;
                height: 50px;
                background-color: #fff;
                img {
                    margin-left: 10px;
                    margin-right: 4px;
                    width: 26px;
                    height: 24px;
                }
                p {
                    font-size: 12px;
                }
            }
        }
    }
    .title {
        display: flex;
        align-items: center;
        img {
            margin-right: 8px;
            width: 28px;
            height: 24px;
        }
        p {
            font-size: 20px;
        }
    }
    .basis {
        .item:nth-child(1) {
            background-color: #f9d6de;
        }
        .item:nth-child(2) {
            background-color: #DFEEEB;
        }
        .item:nth-child(3) {
            background-color: #ECEBE7;
        }
        .item:nth-child(4) {
            background-color: #CBC7E4;
        }
    }
    .recommend {
        margin-top: 13px;
        ul {
            .item {
                .top {
                    img {
                        right: 35px;
                        width: 93px;
                        height: 110px;
                    }
                }
            }
        }
        .item:nth-child(1) {
            background-color: #FFEDC0;
        }
        .item:nth-child(2) {
            background-color: #D7E7DC;
        }
    }
}
</style>