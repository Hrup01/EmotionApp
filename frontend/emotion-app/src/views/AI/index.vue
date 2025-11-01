<template>
  <div id="AI">
    <nav-bar :is-transparent="isTransparent"></nav-bar>
    <div class="title wrapper">AI情绪教练</div>
    <div class="dialog wrapper">
      <!-- 无对话时 -->
      <div class="noContent" v-if="!haveContent">
        <img src="@/assets/image/AI/首页头像.png" alt="">
        <div class="hint">
          <p>你好呀~我是你的情绪教练小栈~</p>
          <p>跟我讲讲你现在感觉怎么样吧~</p>
        </div>
      </div>

      <!-- 有对话时 -->
      <div class="box" v-else>
        <div class="haveContent" ref="haveContent" v-for="(item, index) in chatMessages" :key="index">
          <!-- <div class="userSentence" v-if="userSentence">
              <div class="item">{{ userSentence }}</div>
          </div>
          <div class="answer" v-if="answer.length > 0">
            <div class="item">
              <img src="@/assets/image/AI头像.png" alt="">
              <p>{{ answer.join('') }}</p>
            </div>
          </div> -->
          <div class="userSentence" v-if="item.role === 'user'">
              <div class="item">{{ item.content }}</div>
          </div>
          <div class="answer" v-else-if="item.role === 'ai'">
            <div class="item">
              <img src="@/assets/image/AI/AI头像.png" alt="">
              <p>{{ item.content }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 发送消息 -->
      <div class="message" ref="message">
        <textarea name="message" v-model="message" @focus="changeMessage" ref="textarea"></textarea>
        <div class="send">
          <img src="@/assets/image/AI/发送.png" alt="" @click="sendMessage">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
// import request from '@/utlis/request'
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'AIPage',
    components: {
      NavBar
    },
    data () {
      return {
        isTransparent: true,
        token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
        message: '向小栈发送信息',
        answer: '',
        userSentence: '',
        haveContent: false,
        // 存储所有聊天记录
        chatMessages: [],
        intervalId: null
      }
    },
    methods: {
      // 获取AI回答
      async getAIAnswer () {
        const res = await axios.get('http://localhost:8080/ai/chat', {
          params: {
            prompt: this.userSentence,
            chatId: 1
          },
          responseType: 'stream',
          headers: {
                Authorization: 'Bearer ' + this.token
            }
        })
        // 先推送一个空的ai回答占位符
        this.chatMessages.push({ role: 'ai', content: '' })
        // console.log(JSON.parse(res.data))
        const aiAnswer = JSON.parse(res.data)
        aiAnswer.forEach(item => {
            this.answer += item.data
        })
        // console.log(this.answer)
        let currentIndex = 0
        if (this.intervalId) {
            clearInterval(this.intervalId)
        }
        this.intervalId = setInterval(() => {
            if (currentIndex < this.answer.length) {
                this.chatMessages[this.chatMessages.length - 1].content += this.answer[currentIndex]
                // 自动滚动到底部
                this.$nextTick(() => {
                    const box = this.$refs.haveContent
                    box[box.length - 1].scrollIntoView({ behavior: 'smooth' })
                })
                currentIndex++
            } else {
                clearInterval(this.intervalId)
                this.intervalId = null
            }
        }, 50)
      },
      // 发送消息
      sendMessage () {
        this.haveContent = true
        if (this.message === '向小栈发送信息') return
        if (this.message !== '') {
            this.userSentence = this.message.trim()
            // 推送 用户信息 到聊天记录
            this.chatMessages.push({ role: 'user', content: this.userSentence })
            // 自动滚动到底部
            this.$nextTick(() => {
                const box = this.$refs.haveContent
                box[box.length - 1].scrollIntoView({ behavior: 'smooth' })
            })
        }
        this.message = '向小栈发送信息'
        this.$refs.textarea.blur()
        // this.$refs.message.style.bottom = '31px'
        // 获得后端传回的ai回答
        this.answer = ''
        this.getAIAnswer()
      },

      changeMessage () {
        // 使文本域到键盘上方
        // this.$refs.message.style.bottom = '0'
        // 改变文本域内容
        this.message = ''
        this.$refs.textarea.addEventListener('blur', () => {
            if (this.message === '') {
                this.message = '向小栈发送信息'
            }
        })
      },
    },
    beforeDestroy () {
      if (this.intervalId) {
        clearInterval(this.intervalId)
      }
    }
}
</script>

<style lang="less" scoped>
#AI {
  height: 852px;
  background: url('@/assets/image/AI/图案组合.png') no-repeat #ffefcf;
  background-size: 390px;
}
.wrapper {
  margin: 0 auto;
  width: 365px;
}
.title {
  margin-top: 100px;
  text-align: center;
  color: #000000cc;
  font-size: 24px;
  font-weight: 600;
}
.dialog {
  // position: relative;
  margin-top: 21px;
  padding: 28px 16px;
  height: 610px;
  border-radius: 30px;
  background: #fff9ef;
  
  .noContent {
    img {
      margin-top: 27px;
      margin-left: 128px;
      width: 88px;
    }
    .hint {
      margin-top: 12px;
      p {
        margin-top: 4px;
        text-align: center;
        color: #00000080;
        font-size: 12px;
        font-weight: 600;
      }
    }
  }
  .message {
    position: fixed;
    left: 25px;
    bottom: 31px;
    display: flex;
    justify-content: space-between;
    textarea {
      padding: 14px;
      width: 284px;
      height: 50px;
      border-radius: 56px;
      border: 1px solid #f59a23;
      background: #ffffff;
      color: #0000004d;
      // line-height: 50px;
      font-size: 16px;
      font-weight: 550;
      overflow-y: auto;
    }
    .send {
      margin-left: 10px;
      padding: 10px;
      width: 48px;
      height: 48px;
      border-radius: 48px;
      background: #f59a23;
      img {
        width: 26px;
      }
    }
  }
  // 有对话时
  .box {
    height: 510px;  
    overflow-y: auto;
    .haveContent {
      display: flex;
      flex-direction: column;
      // height: 510px;  
      font-size: 15px;
      font-weight: 600;
      // overflow-y: auto;
      .userSentence,
      .answer {
        width: fit-content;
        max-width: 100%;
        word-break: break-word;
        .item {
          margin-bottom: 10px;
          padding: 12px 17px;
          border-radius: 26px;
        }
      }
      .userSentence {
        align-self: flex-end;
        .item {
          background: #dff1f0;
        }
      }
      .answer {
        align-self: flex-start;
        .item {
          padding: 0;
          display: flex;
          margin-bottom: 10px;
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
  }
}
</style>