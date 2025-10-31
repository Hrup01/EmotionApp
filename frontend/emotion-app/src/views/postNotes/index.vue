<template>
  <div id="postNotes">
    <nav-bar :title="navbartitle" :is-post-notes="isPostNotes"></nav-bar>
    <div class="navbarRightPic" @click="createPost">
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
        <!-- <ul class="pic">
            <li>
                <img src="@/assets/image/笔记页面/示例图片1.png" alt="" class="subject">
                <img src="@/assets/image/笔记页面/关闭.png" alt="" class="delete">
            </li>
            <li>
                <img src="" alt="" class="subject">
                <img src="@/assets/image/笔记页面/关闭.png" alt="" class="delete">
            </li>
        </ul> -->
        <!-- <div class="pic">
            <img src="@/assets/image/笔记页面/示例图片1.png" alt="">
            <img src="@/assets/image/笔记页面/示例图片1.png" alt="">
            <img src="@/assets/image/笔记页面/示例图片1.png" alt="">
        </div> -->
    </div>
    <div class="add wrapper">
        <ul>
            <li v-for="item in addList" :key="item.id" :ref="item.ref">
                <input type="file" ref="upload" v-show="0">
                <img :src="item.url" alt="">
                <p>{{ item.text }}</p>
            </li>
        </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import NavBar from '../../components/NavBar.vue'

export default {
    name: 'postNotes',
    components: { 
        NavBar 
    },
    data () {
        return {
            isPostNotes: true,
            token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
            navbartitle: '发布笔记',
            // haveRight: true,
            // rightPic: require('@/assets/image/更多1.png')
            title: '+ 添加标题',
            content: '分享此刻的喜怒哀乐...',
            images: '',
            addList: [
                { id: 0, url: require('@/assets/image/笔记页面/话题.png'), text: '添加话题', ref: 'addtopic' },
                { id: 1, url: require('@/assets/image/笔记页面/图片添加.png'), text: '添加图片', ref: 'addpic' },
                { id: 2, url: require('@/assets/image/笔记页面/本地.png'), text: '添加地点', ref: 'addplace' },
                { id: 3, url: require('@/assets/image/笔记页面/艾特.png'), text: '添加话题', ref: 'at' },
            ]
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
        },
        // 创建帖子
        async createPost () {
            console.log(this.images)
            const res = await axios.post('http://localhost:8080/api/community/posts', this.images, {
                params: {
                    content: this.content,
                },
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log(res)
            // this.$router.go(-1)
        }
    },
    mounted () {
        // 上传图片
        const addpic = this.$refs.addpic
        // console.log(addpic[0])
        addpic[0].addEventListener('click',() => {
            // console.log(this.$refs.upload[0])
            this.$refs.upload[0].click()
            this.$refs.upload[0].addEventListener('change', e => {
                const fd = new FormData()
                console.log(e.target.files[0])
                fd.append('images',e.target.files[0])
                this.images = fd
                console.log(fd)
                // console.log(this.images)
            })
        })
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
    height: 674px;
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
            height: 320px;
            border: 0;
            background-color: #FFF3DB;
            color: #c0bdb7;
            font-size: 12px;
        }
    }
    .pic {
        display: flex;
        overflow-y: auto;
        li {
            position: relative;
            .subject {
                margin-right: 8px;
                flex-shrink: 0;
                width: 152px;
                height: 191px;
                border-radius: 10px;
                background: #cccccc;
            }
            .delete {
                position: absolute;
                top: 10px;
                right: 15px;
                width: 20px;
                height: 20px;
            }
        }
    }
}
.add {
    ul {
        margin-top: 11px;
        display: flex;
        height: 50px;
        overflow-x: auto;
        li {
            margin-right: 28px;
            display: flex;
            align-items: center;
            flex-shrink: 0;
            width: 98px;
            height: 34px;
            border-radius: 20px;
            background: #ffefcf;
            box-shadow: 2px 2px 5px 0 #cc680440;
            img {
                margin-left: 10px;
                margin-right: 8px;
                width: 20px;
                height: 20px;
            }
            p {
                color: #5c5c5c;
                font-size: 12px;
            }
        }
    }
}
</style>