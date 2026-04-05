<template>
  <div id="logPage">
    <div class="top">
        <NavBar :title="nabbarTitle"></NavBar>
        <img src="@/assets/image/bc.png" alt="" @click="postDiary">
    </div>
    <div class="time wrapper">
        <div class="exactDay">{{ exactDate }}</div>
        <div class="week">{{ week }}</div>
    </div>
    <div class="content wrapper">
        <input type="text" name="title" class="title" v-model="title" @click="changeTitle" ref="title">
        <div class="border"></div>
        <ul ref="dashed">
            <!-- js动态渲染 -->
        </ul>
        <!-- 实际输入 -->
        <textarea name="log" id="" class="diary" v-model="content" @click="changeContent" ref="diary"></textarea>
        <!-- 实际显示 -->
        <div class="realShow" ref="realShow">{{ realShow }}</div>
    </div>
    <!-- 富文本编辑器 -->
    <div class="richTextEditor" style="display: none;">
        <div class="color"><img src="@/assets/image/t.png" alt="" @click="changeColor"></div>
        <div class="strong"><img src="@/assets/image/b.png" alt="" @click="toStrong"></div>
        <div class="incline"><img src="@/assets/image/I.png" alt="" @click="toIncline"></div>
        <div class="underline"><img src="@/assets/image/U.png" alt="" @click="addUnderline"></div>
        <div class="numSort"><img src="@/assets/image/Num.png" alt="" @click="numSort"></div>
        <div class="alphabetSort"><img src="@/assets/image/abc.png" alt="" @click="alphabetSort"></div>
        <div class="pic">
            <img src="@/assets/image/add_pic.png" alt="" @click="addPic">
            <input type="file" class="upload" ref="upload">
        </div>
    </div>
    <div class="chartLet">
        <img :src="item.url" alt="" :class="item.name" v-for="item in picList" :key="item.id">
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import axios from 'axios'
// import request from '@/utlis/request'
// import tinymce from '@/components/tinymce.vue'
// import tinymceEditor from '@/components/tinymceEditor.vue'
export default {
    name: "logPage",
    components: {
        NavBar,
        // tinymceEditor
    },
    data () {
        return {
            token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
            nabbarTitle: '日志打卡',
            title: '+ 添加标题',
            content: '输入心得，记录你的情绪变化',
            changeStyleContnet: '',
            // 具体日期
            exactDate: '',
            // 星期几
            week: '',
            // 情绪类型
            emotionType: this.$route.params.mood,
            haveImg: false,
            picList: [
                { id: 0, url: require('@/assets/image/pen.png'), name: 'pen' },
                { id: 1, url: require('@/assets/image/hug.png'), name: 'hug' },
                { id: 2, url: require('@/assets/image/heart.png'), name: 'heart' },
                { id: 3, url: require('@/assets/image/moon.png'), name: 'moon' },
                { id: 4, url: require('@/assets/image/swim.png'), name: 'swim' },
                { id: 5, url: require('@/assets/image/star.png'), name: 'star' }
            ],
            realShow: '',
            str: ''
        }
    },
    methods: {
        // 改变标题
        changeTitle () {
            this.title = ''
            this.$refs.title.addEventListener('blur', () => {
                if (this.title === '') {
                    this.title = '+ 添加标题'
                }
            })
            this.$refs.title.addEventListener('keyup', (e) => {
                // console.log(e)
                if (e.key === 'Enter') {
                    // console.log(11)
                    if (this.title !== '') {
                        this.title = this.title.trim()
                    }
                    else {
                        this.title = '+ 添加标题'
                    }
                    this.$refs.title.blur()
                }
            })
        },
        // 改变内容
        changeContent () {
            if (this.content === '输入心得，记录你的情绪变化') this.content = ''
            this.$refs.diary.addEventListener('blur', () => {
                if (this.content === '') this.content = '输入心得，记录你的情绪变化'
            })
            // this.realShow = this.currentCotent
            // const diary = this.$refs.diary
            // diary.addEventListener('change', () => {
            //     this.realShow = this.currentCotent
            // })
        },
        // 发布日志
        async postDiary () {
            // 1.清除标题和日记内容
            this.currentTitle = '+ 添加标题'
            this.currentContent = '输入心得，记录你的情绪变化'
            // 2.post
            console.log(this.emotionType)
            // const diaryRes = await axios.post('http://localhost:8080/api/emotion/diary', {}, {
            //     params: {
            //         diaryDate: this.exactDate,
            //         emotionType: this.emotionType,
            //         content: this.content
            //     },
            //     headers: {
            //         Authorization: 'Bearer ' + this.token
            //     }
            // })
            // console.log('发布日志结果',diaryRes)
            const moodRes = await axios.post('http://localhost:8080/dayEmotionRecord', {}, {
                params: {
                    emotion: this.emotionType
                },
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('上传心情结果', moodRes)
            this.$router.push('/home')
        },
        changeColor () {
            // const realShow = this.$refs.realShow
            // console.log(realShow) // <div data-v-b919abc6="" class="realShow"></div>

        },
        toStrong () {

        },
        toIncline () {

        },
        addUnderline () {

        },
        numSort () {

        },
        alphabetSort () {

        },
        addPic () {
            const upload = this.$refs.upload 
            upload.click()
            // upload.addEventListener('change', (e) => {
            //     const fd = new FormData()
            //     fd.append('img', e.target.files[0])
            //     // console.log(fd)
            //     request({
            //         url: '',
            //         method: 'post',
            //         data: fd
            //     }).then(result => {
            //         const imgUrl = result.data.data.url
            //     })
            // })
        }
    },
    created () {
        
    },
    mounted () {
        // 获取具体日期和星期几
        const date = new Date()
        const year =  date.getFullYear()
        const month = date.getMonth()
        const day = date.getDate()
        const arr = ['周天','周一','周二','周三','周四','周五','周六']
        this.week = arr[date.getDay()]
        this.exactDate = `${year}年${month + 1}月${day}日`

        // 渲染虚线
        let str = ''
        for (let i = 0; i < 12; i++) {
            str += '<li data-v-b919abc6 class="bg"></li>'
        }
        this.$refs.dashed.innerHTML = str
        // console.log(this.$refs.dashed)

        // this.realShow = this.currentContent
    },
    updated () {
        // this.realShow = this.currentContent
    }
}
</script>

<style lang="less" scoped>
#logPage {
    height: 844px;
    background: linear-gradient(140.1deg, #fef2f1 0%, #ffe8d9 100%);
}
.wrapper {
    margin: 0 auto;
    width: 325px;
}
.top {
    display: flex;
    img {
        position: relative;
        top: 50px;
        right: 20px;
        width: 24px;
        height: 24px;
    }
}
.time {
    margin-top: 12px;
    padding: 0 67px;
    display: flex;
    justify-content: space-between;
    color: #000000b3;
    font-size: 16px;
    font-weight: 550;
}
.content {
    margin-top: 16px;
    padding: 0 22px;
    position: relative;
    height: 647px;
    border-radius: 30px;
    border: 2px solid #e4ceb0;
    background: #fef8ec;   
    .title {
        margin: 16px 0;
        color: #00000099;
        width: 100%;
        font-size: 18px;
        font-weight: 400;
        text-align: center;
        border: 0;
        border-radius: 15px;
        background-color: #FEF8EC;
    } 
    .border {
        height: 0;
        border: 2px solid #e4ceb0;
    }
    .bg {
        width: 276px;
        height: 46px;
        opacity: 1;
        border-bottom: 2px dashed #e3e3e3;
        z-index: 0;
    }
    .diary {
        padding-top: 4px;
        // padding-left: 22px;
        position: absolute;
        top: 62px;
        left: 16;
        z-index: 1;
        background: transparent;
        background: transparent url('@/assets/image/bj.png') no-repeat 0px 16px;
        background-size: 20px;
        width: 274px;
        height: 552px;
        resize: none;
        border: 0;
        color: #c0bdb7;
        // color: transparent;
        text-indent: 2em;
        font-size: 12px;
        font-weight: 600;
        line-height: 46px;
        z-index: 2;
    }
    .realShow {
        position: absolute;
        top: 66px;
        left: 16;
        width: 274px;
        height: 552px;
        word-break: break-all;
        text-overflow: ellipsis;
        color: #c0bdb7;
        text-indent: 2em;
        font-size: 12px;
        font-weight: 600;
        line-height: 46px;
        z-index: 1;
    }
}
.chartLet {
    img {
        position: fixed;
    }
    .pen {
        top: 270px;
        left: 13px;
        width: 46px;
        height: 46px;
    }
    .star {
        top: 300px;
        right: 30px;
        width: 25px;
        height: 25px;
    }
    .hug {
        top: 330px;
        right: 9px;
        width: 48px;
        height: 55px;
    }
    .heart {
        top: 480px;
        left: 12px;
        width: 53px;
        height: 36px;
    }
    .moon {
        top: 520px;
        right: 10px;
        width: 60px;
        height: 50px;
    }
    .swim {
        top: 667px;
        right: 18px;
        width: 75px;
        height: 69px;
    }
}
// 富文本编辑器
.richTextEditor {
    padding-top: 18px; 
    position: fixed;
    bottom: 0;
    display: flex;
    justify-content: space-around;
    width: 100%;
    height: 60px;
    background-color: #FEF8EC;
    img {
        width: 24px;
        height: 24px;
    }
    .upload {
        display: none;
    }
}
</style>