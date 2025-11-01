<template>
  <div id="privateLetter">
    <div class="top">
        <nav-bar></nav-bar>
        <div class="userMessage">
            <img src="@/assets/image/私信/头像.png" alt="" class="headPic">
            <div class="name">CaCa</div>
            <div class="navbarRightPic">
                <img src="@/assets/image/私信/更多1.png" alt="" class="more" @click="changeFlag">
                <div class="hiden" ref="hiden" v-show="flag">
                    <div class="blackList" @click="blackList = true">
                        <img src="@/assets/image/拉黑用户.png" alt="">
                        <p>拉黑用户</p>
                    </div>
                    <div class="report" @click="report = true">
                        <img src="@/assets/image/举报用户.png" alt="">
                        <p>举报用户</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="body" ref="body">
        <!-- <div class="myContent">
            <div class="item">{{ content }}</div>
        </div>
        <div class="answer">
          <div class="item">{{ answer }}</div>
        </div> -->
    </div>
    <div class="footer" ref="footer">
        <div class="sendMessage">
            <textarea name="" id="" v-model="content" @focus="changeContent" ref="textarea"></textarea>
            <div class="send" ref="send">发送</div>
            <div class="add"><img src="@/assets/image/私信/添加.png" alt="" @click="addFunction"></div>
        </div>
        <div class="moreFunction" v-show="isAddFunction">
            <input type="file" ref="upload">
            <img src="@/assets/image/私信/照片.png" alt="" class="addPic" @click="addPic">
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
// import axios from 'axios'
export default {
    name: 'privateLetter',
    components: {
        NavBar
    },
    data () {
        return {
            flag: false,
            isAddFunction: false,
            toUserId: this.$route.params.id,
            token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
            content: '发送消息...',
            answer: '',
            str: ''
        }
    },
    methods: {
        // 举报、拉黑组件出现
        changeFlag () {
            this.flag = this.flag ? false : true
        },
        // 改变文本域内容
        changeContent () {
            if (this.content === '发送消息...') this.content = ''
            this.$refs.textarea.addEventListener('blur', () => {
                if (this.content === '') this.content = '发送消息...'
            })
        },
        // 添加功能（图片）
        addFunction () {
            // 1.改变样式
            this.isAddFunction = true
            const footer = this.$refs.footer
            if (this.isAddFunction) {
                footer.style.height = '300px'
                footer.style.borderRadius = '30px 30px 0 0'
                footer.style.background = '#ffffff'
            }
        },
        // 发送图片
        addPic () {
            this.$refs.upload.click()
        },
        // 发送消息
        // async sendMessage () {
        //     if (this.content === '发送消息...') return
        //     // console.log(11)
        //     if (this.content != '') {
        //         // 异步任务 --> 微任务
        //         const res = await axios.post(`http://localhost:8080/api/community/dm/${this.toUserId}`, {
        //             // 去除前后空格
        //             content: this.content.trim()
        //         }, {
        //             headers: {
        //                 Authorization: 'Bearer ' + this.token
        //             }
        //         })
        //         console.log('我发送的信息',res)
        //         // 渲染页面
        //         this.render()
        //     }
        //     // 异步任务 --> 宏任务
        //     setTimeout(( () => this.content = '发送消息...'),100)
        // },
        // 获取与指定用户的私信对话
        // async getUserMessage () {
        //     const res = await axios.get(`http://localhost:8080/api/community/dm/${this.toUserId}`, {
        //         params: {
        //             page: 0,
        //             size: 20
        //         },
        //         headers: {
        //             Authorization: 'Bearer ' + this.token
        //         }
        //     })
        //     console.log('对方发送的信息',res)
        //     this.answer = res.data.data[0].content
        //     this.render()
        // },
        // 渲染页面函数
        render () {
        if (this.myContent !== '') {
          this.str += ` <div data-v-160026a2 class="myContent">
                          <div data-v-160026a2 class="item">${this.content}</div>
                        </div>  `
        }
        if (this.answer !== '') {
          this.str += ` <div data-v-160026a2 class="answer">
                          <div data-v-160026a2 class="item">${this.answer}</div>
                        </div> `
        }
        // 等dom元素挂载后再渲染
        this.$nextTick(() => {
          this.$refs.body.innerHTML = this.str
        })
      },
    },
    mounted () {
        // 使用websocket获取与指定用户的私信对话
        // let ws = new WebSocket(`ws://localhost:8080/ws/dm/${this.toUserId}?token=${this.token}`)
        // const openEvent = () => {
        //     console.log('连接成功')
        // }
        // const getMessage = (event) => {
        //     const messageData = JSON.parse(event.data)
        //     console.log('收到的信息', messageData)
        //     this.answer = messageData.content
        //     this.render()
        // }
        // const closeEvent = () => {
        //     console.log('连接关闭')
        //     reconnect()
        // }
        // const errorEvent = () => {
        //     console.log('连接错误')
        // }
        // ws.addEventListener('open', openEvent)
        // ws.addEventListener('message', getMessage)
        // ws.addEventListener('close', closeEvent)
        // ws.addEventListener('error', errorEvent)
        // // 发送信息
        // const sendMessage = () => {
        //     if (this.content === '发送消息...') return
        //     if (this.content != '') {
        //         const message = {
        //             toUserId: this.toUserId,
        //             content: this.content.trim()
        //         }
        //         ws.send(JSON.stringify(message))
        //         this.render()
        //     }
        //     setTimeout(( () => this.content = '发送消息...'),100)
        // }
        // this.$refs.send.addEventListener('click', sendMessage)
        // // 重连机制
        // const reconnect = () => {
        //     setTimeout(() => {
        //         console.log('正在重连...')
        //         ws = new WebSocket(`ws://localhost:8080/ws/dm/${this.toUserId}?token=${this.token}`)
        //         ws.addEventListener('open', openEvent)
        //         ws.addEventListener('message', getMessage)
        //         ws.addEventListener('close', closeEvent)
        //         ws.addEventListener('error', errorEvent)
        //         this.$refs.send.addEventListener('click', sendMessage)
        //     }, 5000)
        // }
    }
}
</script>

<style lang="less" scoped>
#privateLetter {
    width: 390px;
    height: 844px;
    background: #fdf4e4;
    font-weight: 600;
}
.top {
    height: 104px;
    .userMessage {
        position: absolute;
        top: 42px;
        left: 50px;
        display: flex;
        align-items: center;
        .headPic {
            margin-right: 14px;
            width: 46px;
            height: 46px;
        }
        p {
            color: #363636;
            font-size: 18px;
        }
        .navbarRightPic {
            .more {
                position: fixed;
                top: 50px;
                right: 22px;
                width: 24px;
            }
            .hiden {
                margin-top: 4px;
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
                    color: #000000b3;
                    font-size: 10.95px;
                    font-weight: 600;
                }
                .blackList,
                .report {
                    display: flex;
                }
            }
        } 
    }
}
.body {
    padding-top: 16px;
    display: flex;
    flex-direction: column; 
    width: 390px;
    height: 740px;
    background: #fcfaf5;
    font-size: 15px;
    font-weight: 600;
    overflow-y: auto;
    .myContent,
    .answer {
        width: fit-content;
        max-width: 100%;
        word-break: break-word;
        .item {
            margin-bottom: 6px;
            padding: 12px 17px;
        }
    }
    .myContent {
        align-self: flex-end;
        .item {
            margin-right: 12px;
            border-radius: 26px 0 26px 26px;
            background: #aff0ef;
        }
    }
    .answer {
        align-self: flex-start;
        .item {
            margin-left: 12px;
            display: flex;
            border-radius: 0 26px 26px 26px;
            background: #FFE5B8;
            img {
                margin-right: 12px;
                flex: none;
                width: 46px;
                height: 46px;
            }
            p {
                padding: 12px 17px;
                border-radius: 26px;
                background: #fbddd9;
            }
        }
    }
}
.footer {
    position: fixed;
    bottom: 0;
    width: 390px;
    height: 100px;
    // background-color: #fff;
    .sendMessage {
        padding-top: 24px;
        display: flex;
        align-items: center;
        textarea {
            margin-left: 29px;
            margin-right: 10px;
            padding: 10px 28px;
            width: 302px;
            height: 40px;
            border-radius: 36px;
            border: 1px solid #fdf4e4;
            background: #f2cd8d3d;
            color: #543a0d33;
            font-size: 14px;
        }
        .send {
            position: absolute;
            right: 65px;
            width: 48px;
            height: 30px;
            border-radius: 30px;
            background: #fefbf6;
            color: #0000004d;
            font-size: 14px;
            line-height: 30px;
            text-align: center;
        }
        img {
            width: 32px;
            height: 32px;
        }
    }
    .moreFunction {
        margin-top: 23px;
        margin-left: 40px;
        img {
            width: 56px;
            height: 56px;
        }
        input {
            display: none;
        }
    }
}
</style>