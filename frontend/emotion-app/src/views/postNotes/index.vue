<template>
  <div id="postNotes">
    <nav-bar :title="navbartitle"></nav-bar>
    <div class="navbarRightPic">
        <img src="@/assets/image/笔记页面/路径 1.png" alt="">
    </div>
    <div class="body wrapper">
        <div class="top">
            <img src="@/assets/image/笔记页面/头像.png" alt="">
            <p>今天要分享些什么？</p>
        </div>
        <div class="border"></div>
        <div class="noteContent">
            <input type="text" class="title" ref="title" v-model="title" @click="changeTitle">
            <textarea name="content" id="" ref="note" v-model="content" @click="changeContent"></textarea>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '../../components/NavBar.vue'

export default {
    name: 'postNotes',
    components: { 
        NavBar 
    },
    data () {
        return {
            navbartitle: '发布笔记',
            // haveRight: true,
            // rightPic: require('@/assets/image/更多1.png')
            title: '+ 添加标题',
            content: '分享此刻的喜怒哀乐...'
        }
    },
    methods: {
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
        changeContent () {
            if (this.content === '分享此刻的喜怒哀乐...') this.content = ''
            this.$refs.note.addEventListener('blur', () => {
                if (this.content === '') this.content = '分享此刻的喜怒哀乐...'
            })
        }
    }
}
</script>

<style lang="less" scoped>
#postNotes {
    width: 390px;
    height: 852px;
    background: #ffefcf;
    font-weight: 600;
}
.wrapper {
    margin: 0 auto;
    width: 365px;
}
.navbarRightPic {
    img {
        position: fixed;
        top: 55px;
        right: 22px;
        width: 24px;
    }
}
.body {
    margin-top: 12px;
    width: 365px;
    height: 634px;
    border-radius: 30px;
    background: #fff3db;
    .top {
        margin-top: 22px;
        margin-left: 16px;
        display: flex;
        align-items: center; 
        img {
            margin-right: 16px;
            width: 40px;
            height: 40px;
        }
        p {
            color: #575757;
            font-size: 16px;
        }
    }
    .border {
        margin: 12px auto;
        width: 314px;
        height: 1px;
        background-color: #E4CEB0;
    }
    .noteContent {
        margin: 0 auto;
        width: 314px;
        .title {
            border: 0;
            background-color: #FFF3DB;
            color: #00000099;
            font-size: 18px;
            font-weight: 400;
        }
        textarea {
            margin-top: 22px;
            width: 100%;
            height: 300px;
            border: 0;
            background-color: #FFF3DB;
            color: #c0bdb7;
            font-size: 12px;
        }
    }
}
</style>