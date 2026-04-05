<template>
  <div id="privateLetter">
    <div class="top">
        <nav-bar></nav-bar>
        <div class="userMessage">
            <img src="@/assets/image/私信/头像.png" alt="" class="headPic">
            <div class="name">{{ username }}</div>
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
        <div v-for="(item, index) in messageList" :key="index" :class="{myContent: item.sender === 'me', answer: item.sender === 'other'}">
            <div class="item">{{ item.content }}</div>
        </div>
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
            username: '',
            flag: false,
            isAddFunction: false,
            toUserId: this.$route.params.id,
            token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
            content: '发送消息...',
            answer: '',
            str: '',
            // 消息列表
            messageList: []
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
    },
    mounted () {
        // 接收路由参数
        this.username = this.$route.params.username
        // 使用websocket获取与指定用户的私信对话
        let ws = new WebSocket(`ws://localhost:8080/webSocket?token=${this.token}`)
        const openEvent = () => {
            console.log('连接成功')
            heartCheck.start()
        }
        const getMessage = (event) => {
            const messageData = JSON.parse(event.data)
            console.log('收到的信息', messageData)
            heartCheck.reset()
            this.answer = messageData.content
            this.messageList.push({ content: this.answer, sender: 'other' })
        }
        const closeEvent = () => {
            console.log('连接关闭')
            heartCheck.reset()
            reconnect()
        }
        const errorEvent = () => {
            console.log('连接错误')
            reconnect()
        }
        ws.addEventListener('open', openEvent) // 服务器开启
        ws.addEventListener('message', getMessage) // 接收信息
        ws.addEventListener('close', closeEvent) // 服务器关闭
        ws.addEventListener('error', errorEvent) // 错误
        // 发送信息
        const sendMessage = () => {
            if (this.content === '发送消息...') return
            if (this.content != '') {
                const message = {
                    toUserName: this.username,
                    content: this.content.trim()
                }
                ws.send(JSON.stringify(message))
                // ws.send('发送到服务器的消息')
                this.messageList.push({ content: this.content.trim(), sender: 'me' })
            }
            setTimeout(( () => this.content = '发送消息...'),100)
        }
        this.$refs.send.addEventListener('click', sendMessage)
        // 重连机制
        const reconnect = () => {
            setTimeout(() => {
                console.log('正在重连...')
                // 若存在旧实例则先销毁
                if (ws) {
                    ws.removeEventListener('open', openEvent)
                    ws.removeEventListener('message', getMessage)
                    ws.removeEventListener('close', closeEvent)
                    ws.removeEventListener('error', errorEvent)
                    ws.close()
                }
                ws = new WebSocket(`ws://localhost:8080/webSocket?token=${this.token}`)
                ws.addEventListener('open', openEvent)
                ws.addEventListener('message', getMessage)
                ws.addEventListener('close', closeEvent)
                ws.addEventListener('error', errorEvent)
                this.$refs.send.addEventListener('click', sendMessage)
            }, 5000)
        }

        // 心跳检测对象
        const heartCheck = {
            // 每30秒发送一次心跳
            timeout: 30000,
            // 存储定时器ID，用于后续清理
            timeoutObj: null,
            serveTimeoutObj: null,
            // 重置心跳定时器，清除之前的心跳和服务端超时定时器，避免定时器堆积
            reset: () => {
                clearTimeout(this.timeoutObj)
                clearTimeout(this.serveTimeoutObj)
                return this
            },
            start: () => {
                // 启动心跳：30 秒后发送心跳包 ws.send("ping")，告知服务端连接正常
                const self = this // 清除了之前定时器的对象
                self.timeoutObj = setTimeout(() => {
                    ws.send('ping')

                    // 启动服务端超时检测：同时设置一个 30 秒的定时器，若 30 秒内未收到服务端返回的心跳响应（pong），则认为连接异常，执行 ws.close() 关闭连接，进而触发重连逻辑
                    self.serveTimeoutObj = setTimeout(() => {
                        ws.close()
                    }, self.timeout)
                }, self.timeout)
            }
        }
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