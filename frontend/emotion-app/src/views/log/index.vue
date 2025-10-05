<template>
  <div id="logPage">
    <div class="top">
        <NavBar :title="title" :toUrl="toUrl"></NavBar>
        <img src="@/assets/image/bc.png" alt="" @click="postDiary">
    </div>
    <div class="time wrapper">
        <div class="exactDay">{{ exactDate }}</div>
        <div class="week">{{ week }}</div>
    </div>
    <div class="content wrapper">
        <input type="text" name="title" class="title" v-model="currentTitle" @click="changeTitle" ref="title">
        <div class="border"></div>
        <ul ref="dashed">
            <!-- js动态渲染 -->
        </ul>
        <textarea name="log" id="" class="diary" v-model="currentCotent" @click="changeContent" ref="diary"></textarea>
        <!-- 富文本编辑器 -->
         <!-- <div>
            <tinymceEditor ref="editor" v-model="value"></tinymceEditor>
         </div> -->
    </div>
    <div class="chartLet">
        <img :src="item.url" alt="" :class="item.name" v-for="item in picList" :key="item.id">
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
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
            currentTitle: '+ 添加标题',
            currentCotent: '输入心得，记录你的情绪变化',
            exactDate: '',
            week: '',
            content: '',
            title: '日志打卡',
            toUrl: '/moodOption',
            haveImg: false,
            picList: [
                { id: 0, url: require('@/assets/image/pen.png'), name: 'pen' },
                { id: 1, url: require('@/assets/image/hug.png'), name: 'hug' },
                { id: 2, url: require('@/assets/image/heart.png'), name: 'heart' },
                { id: 3, url: require('@/assets/image/moon.png'), name: 'moon' },
                { id: 4, url: require('@/assets/image/swim.png'), name: 'swim' },
                { id: 5, url: require('@/assets/image/star.png'), name: 'star' }
            ],
        }
    },
    methods: {
        changeTitle () {
            this.currentTitle = ''
            this.$refs.title.addEventListener('blur', () => {
                if (this.currentTitle === '') {
                    this.currentTitle = '+ 添加标题'
                }
            })
            this.$refs.title.addEventListener('keyup', (e) => {
                // console.log(e)
                if (e.key === 'Enter') {
                    // console.log(11)
                    if (this.currentTitle !== '') {
                        this.currentTitle = this.currentTitle.trim()
                    }
                    else {
                        this.currentTitle = '+ 添加标题'
                    }
                    this.$refs.title.blur()
                }
            })
        },
        changeContent () {
            if (this.currentCotent === '输入心得，记录你的情绪变化') this.currentCotent = ''
            this.$refs.diary.addEventListener('blur', () => {
                if (this.currentCotent === '') this.currentCotent = '输入心得，记录你的情绪变化'
            })
        },
        postDiary () {
            // 1.清除标题和日记内容
            this.currentTitle = '+ 添加标题'
            this.currentCotent = '输入心得，记录你的情绪变化'
            // 2.post
        }
    },
    created () {
        
    },
    mounted () {
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
    },
    updated () {
        
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
        left: 225px;
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
        width: 274px;
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
        // background: transparent;
        background: transparent url('@/assets/image/bj.png') no-repeat 0px 16px;
        background-size: 20px;
        width: 274px;
        height: 552px;
        resize: none;
        border: 0;
        color: #c0bdb7;
        text-indent: 2em;
        font-size: 12px;
        width: 600;
        line-height: 46px;
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
</style>