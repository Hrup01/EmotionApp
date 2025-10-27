<template>
  <div id="whiteNoise">
    <NavBar :title="title"></NavBar>
    <router-link to="/countDown" class="countDown"><img src="@/assets/image/白噪音/定时.png" alt=""></router-link>
    <div class="header">
        <div class="pic">
            <img src="@/assets/image/白噪音/图片1.png" alt="">
            <img src="@/assets/image/白噪音/图片2.png" alt="">
        </div>
        <div class="text">
            <img src="@/assets/image/白噪音/图片3.png" alt="">
            <p>选择一缕轻响</p>
            <p>与大自然一起平静身心</p>
            <img src="@/assets/image/白噪音/图片4.png" alt="">
        </div>
    </div>
    <div class="body wrapper">
        <ul>
            <li v-for="(item, index) in whiteNoiseList" :key="item.id" @click="playAudio(index)">
                <audio :src="item.audioUrl" :ref="item.ref"></audio>
                <div class="pic"><img :src="item.picUrl" alt=""></div>
                <p>{{ item.text }}</p>
            </li>
        </ul>
        <!-- <stateItem :list="whiteNoiseList" :is-white-noise="isWhiteNoise"></stateItem> -->
    </div>
    <div class="footer wrapper">
        <div class="prev" @click="prevAudio"><img src="@/assets/image/白噪音/切换-左.png" alt=""></div>
        <div class="control">
            <div class="play" v-if="isPlay" @click="pauseAudio"><img src="@/assets/image/白噪音/开始.png" alt=""></div>
            <div class="pause" v-else @click="ctrlPlayAudio"><img src="@/assets/image/白噪音/暂停.png" alt=""></div>
        </div>
        <div class="next" @click="nextAudio"><img src="@/assets/image/白噪音/切换-右.png" alt=""></div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
// import stateItem from '@/components/stateItem.vue'
export default {
    name: 'whiteNoise',
    components: {
        NavBar,
        // stateItem
    },
    data () {
        return {
            title: '白噪音',
            isWhiteNoise: true,
            // prevOptionAudio: '',
            currentOptionAudio: '',
            // nextOptionAudio: '',
            isPlay: false,
            whiteNoiseList: [
                { id: 0, audioUrl: require('@/assets/audio/白噪音/海洋.mp3'), picUrl: require('@/assets/image/白噪音/海洋.png'), text: '海洋', ref: 'sea' },
                { id: 1, audioUrl: require('@/assets/audio/白噪音/山间瀑布.mp3'), picUrl: require('@/assets/image/白噪音/山间瀑布.png'), text: '山间瀑布', ref: 'waterfall' },
                { id: 2, audioUrl: require('@/assets/audio/白噪音/森林鸟鸣.mp3'), picUrl: require('@/assets/image/白噪音/森林鸟鸣.png'), text: '森林鸟鸣', ref: 'birdsong' },
                { id: 3, audioUrl: require('@/assets/audio/白噪音/雨声.mp3'), picUrl: require('@/assets/image/白噪音/雨声.png'), text: '雨声', ref: 'rain' },
                { id: 4, audioUrl: require('@/assets/audio/白噪音/雷鸣.mp3'), picUrl: require('@/assets/image/白噪音/雷鸣.png'), text: '雷鸣', ref: 'thunderous' },
                { id: 5, audioUrl: require('@/assets/audio/白噪音/篝火.mp3'), picUrl: require('@/assets/image/白噪音/篝火.png'), text: '篝火', ref: 'bonfire' },
                { id: 6, audioUrl: require('@/assets/audio/白噪音/风声.mp3'), picUrl: require('@/assets/image/白噪音/风声.png'), text: '风声', ref: 'wind' },
                { id: 7, audioUrl: require('@/assets/audio/白噪音/暴风雪.mp3'), picUrl: require('@/assets/image/白噪音/暴风雪.png'), text: '暴风雪', ref: 'snowstorm' },
                { id: 8, audioUrl: require('@/assets/audio/白噪音/麦浪.mp3'), picUrl: require('@/assets/image/白噪音/麦浪声.png'), text: '麦浪声', ref: 'wheat' },
                { id: 9, audioUrl: require('@/assets/audio/白噪音/风铃声.mp3'), picUrl: require('@/assets/image/白噪音/风铃声.png'), text: '风铃声', ref: 'windchime' },
                { id: 10, audioUrl: require('@/assets/audio/白噪音/夏日蝉鸣.mp3'), picUrl: require('@/assets/image/白噪音/夏日蝉鸣.png'), text: '夏日蝉鸣', ref: 'cicadas' },
                { id: 11, audioUrl: require('@/assets/audio/白噪音/海豚鲸鱼声.mp3'), picUrl: require('@/assets/image/白噪音/海豚声.png'), text: '海豚声', ref: 'dolphins' },
                { id: 12, audioUrl: require('@/assets/audio/白噪音/竹径深处.mp3'), picUrl: require('@/assets/image/白噪音/竹径深处.png'), text: '竹径深处', ref: 'bamboo' },
                { id: 13, audioUrl: require('@/assets/audio/白噪音/翻书写字声.mp3'), picUrl: require('@/assets/image/白噪音/翻书写字声.png'), text: '翻书写字声', ref: 'write' },
                { id: 14, audioUrl: require('@/assets/audio/白噪音/风扇声.mp3'), picUrl: require('@/assets/image/白噪音/风扇声.png'), text: '风扇声', ref: 'fan' },
                { id: 15, audioUrl: require('@/assets/audio/白噪音/古寺钟声.mp3'), picUrl: require('@/assets/image/白噪音/古寺钟声.png'), text: '古寺钟声', ref: 'bell' },
            ]
        }
    },
    methods: {
        playAudio (id) {
            const name = this.whiteNoiseList[id].ref
            // console.log(this.$refs[name][0])
            // 当点击下一个音频时循环判断是否含有active类 --> 有 -- pause() --> classList.contain -- foreach
            this.whiteNoiseList.forEach(item => {
                if (this.$refs[item.ref][0].classList.contains('active')) {
                    // console.log(this.$refs[item.ref][0])
                    this.$refs[item.ref][0].pause()
                    // console.log(11)
                    this.$refs[item.ref][0].classList.remove('active')
                }
            })
            this.$refs[name][0].play()
            // console.log(11)
            this.$refs[name][0].classList.add('active')
            // 记录当前选择的白噪音是数组的第几个元素
            this.currentOptionAudio = id
            // console.log(this.currentOptionAudio)
            // 改变控件图片
            this.isPlay = true
        },
        pauseAudio () {
            this.isPlay = false
            const id = this.currentOptionAudio
            // 获取要暂停的元素
            const name = this.whiteNoiseList[id].ref
            this.$refs[name][0].pause()
        },
        // 下面控件的操作
        ctrlPlayAudio () {
            this.isPlay = true
            // 获取选择白噪音的索引值
            const id = this.currentOptionAudio
            this.playAudio(id)
        },
        // 切换上一首
        prevAudio () {
            const id = this.currentOptionAudio - 1
            if (id < 0) return
            // console.log(this.whiteNoiseList[id].ref)
            this.playAudio(id)
        },
        nextAudio () {
            const id = this.currentOptionAudio + 1
            if (id > 15) return
            console.log(this.whiteNoiseList[id].ref)
            this.playAudio(id)
        }
    }
}
</script>

<style lang="less" scoped>
#whiteNoise {
    width: 390px;
    height: 844px;
    background: linear-gradient(160.4deg, #ffe2de 0%, #fdf4e4 50%, #fdf4e4 100%);
}
.wrapper {
    margin: 0 auto;
    width: 353px;
}
.countDown {
    padding: 4px;
    position: fixed;
    top: 50px;
    right: 24px;
    width: 32px;
    height: 32px;
    border-radius: 8px;
    background: #ffd4cc;
    box-shadow: 0 2px 4px 0 #00000040;
    img {
        width: 24px;
        height: 24px;
    }
}
.header {
    margin-top: 25px;
    margin-left: 13px;
    display: flex;
    align-items: center;
    width: 390px;
    .pic {
        position: relative;
        width: 44%;
        height: 155px;
        img {
            position: absolute;
        }
        img:nth-child(1) {
            top: 0;
            width: 40px;
            height: 34px;
        }
        img:nth-child(2) {
            top: 10px;
            left: 30px;
            width: 134px;
            height: 145px;
        }
    }
    .text {
        flex: 1;
        img:nth-child(1) {
            top: 40px;
            left: 180px;
            width: 25px;
            height: 17px;
        }
        img:last-child {
            position: relative;
            left: 170px;
            // top: 105px;
            right: 8px;
            width: 25px;
            height: 17px;
        }
        p {
            margin-bottom: 4px;
            margin-left: 28px;
            color: #000000b3;
            font-size: 14px;
            font-weight: 600;
        }
    }
}
.body {
    margin-top: 7px;
    ul {
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        li {
            margin: 12px 0;
            padding-top: 20px;
            width: 79px;
            height: 96px;
            border-radius: 9px;
            border: 1px solid #ecd1cd;
            background: #ffffff;
            .pic {
                margin: 0 auto;
                display: flex;
                justify-content: center;
                align-items: center;
                width: 40px;
                height: 40px;
                img {
                    // display: block;
                    // margin: 0 auto;

                    object-fit: contain;
                    width: 32px;
                }
            }
            p {
                margin-top: 2px;
                text-align: center;
                color: #000000b3;
                font-size: 14px;
                font-weight: 600;
            }
        }
    }
}
.footer {
    margin-top: 12px;
    padding: 0 83px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 353px;
    height: 58px;
    border-radius: 46.72px;
    background: #f8e7e1;
    border: 5px solid #fff;
    img {
        width: 26px;
        height: 26px;
    }
}
</style>