<template>
  <div id="AI">
    <nav-bar :is-transparent="isTransparent"></nav-bar>
    <div class="right">
      <img src="@/assets/image/AI/新增对话.png" alt="" @click="addNewChatMessage">
      <img src="@/assets/image/AI/历史记录进入按钮.png" alt="" @click="isHistory = true">
    </div>
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
          <div class="answer" v-else-if="item.role === 'assistant'">
            <div class="item">
              <img src="@/assets/image/AI/AI头像.png" alt="">
              <!-- <p v-if="isLoading"></p> -->
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
    <!-- 历史会话记录 -->
    <div class="history" v-show="isHistory">
      <div class="mask"></div>
      <div class="subject">
        <div class="cancel">
          <img src="@/assets/image/AI/返回.png" alt="" @click="isHistory = false">
          <p>历史会话</p>
        </div>
        <div class="record">
          <div class="week" v-show="weekHistoryRecords.length > 0">
            <div class="time">本周</div>
            <ul>
              <li v-for="item in weekHistoryRecords" :key="item.chatId">
                <p @click="getConversationContent(item.chatId)">{{ item.summary }}</p>
                <img src="@/assets/image/AI/关闭.png" alt="" v-show="showDelect" @click="delectHistoryRecord(item.chatId)">
              </li>
            </ul>
          </div>
          <div class="month" v-show="monthHistoryRecords.length > 0">
            <div class="time">本月</div>
            <ul>
              <li v-for="item in monthHistoryRecords" :key="item.chatId">
                <p>{{ item.summary }}</p>
                <img src="@/assets/image/AI/关闭.png" alt="" v-show="showDelect" @click="delectHistoryRecord(item.chatId)">
              </li>
            </ul>
          </div>
          <div class="year" v-show="yearHistoryRecords.length > 0">
            <div class="time">本年</div>
            <ul>
              <li v-for="item in yearHistoryRecords" :key="item.chatId">
                <p>{{ item.summary }}</p>
                <img src="@/assets/image/AI/关闭.png" alt="" v-show="showDelect" @click="delectHistoryRecord(item.chatId)">
              </li>
            </ul>
          </div>
        </div>
        <div class="bin">
          <img src="@/assets/image/AI/删除.png" alt="" @click="showDelect = showDelect ? false : true">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/*
请求: 1.获取ai回答 -- methods -- 完成
      2.添加历史会话 -- 离开组件时 -- beforeDestroy -- 完成
      3.删除历史会话 -- methods
      4.获取全部历史会话 -- 组件渲染完成 -- mounted -- 完成 (获取chatId)
      5.获取会话内容 -- methods
*/
import axios from 'axios'
import NavBar from '@/components/NavBar.vue'
import { generateChatID } from '@/utlis/chatID'
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
        intervalId: null,
        chatId: '',
        // 历史会话的chatId
        // historyChatId: '',
        isLoading: false,
        // 历史会话记录相关
        isHistory: false,
        showDelect: false,
        // 本周历史会话记录
        weekHistoryRecords: [],
        // 本月历史会话记录
        monthHistoryRecords: [],
        // 本年历史会话记录
        yearHistoryRecords: [],
        // 点击了历史会话
        isClickHistoryChat: false
      }
    },
    methods: {
      // 获取AI回答
      async getAIAnswer () {
        const res = await axios.get('http://localhost:8080/ai/chat', {
          params: {
            prompt: this.userSentence,
            chatId: this.chatId
          },
          responseType: 'text',
          headers: {
              Authorization: 'Bearer ' + this.token
          }
        })
        // 先推送一个空的ai回答占位符
        this.chatMessages.push({ role: 'assistant', content: '' })
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

        // try {
        //   await axios.get('http://localhost:8080/ai/chat', {
        //     params: {
        //       prompt: this.userSentence,
        //       chatId: this.chatId
        //     },
        //     responseType: 'text',
        //     headers: {
        //         Authorization: 'Bearer ' + this.token
        //     },
        //     onDownloadProgress: (progressEvent) => {
        //       console.log(progressEvent.currentTarget)
        //       const chunk = progressEvent.currentTarget.responseText
        //       const regex = /\{[^}]+\}/g
        //       const chunks = chunk.match(regex) || []
        //       chunks.forEach((chunk) => {
        //         if (chunk.trim()) {
        //           const { data } = JSON.parse(chunk)
        //           // console.log(data)
        //           // this.answer += data
        //           const lastMsg = this.chatMessages.findLast(item => item.role === 'ai')
        //           console.log(lastMsg)
        //           if (lastMsg) {
        //             lastMsg.content += data
        //             this.$set(lastMsg, 'content', lastMsg.content)
        //           }
        //         }
        //       })
        //       this.$nextTick(() => {
        //           const box = this.$refs.haveContent
        //           box[box.length - 1].scrollIntoView({ behavior: 'smooth' })
        //       })
        //     }
        //   })
        // } catch (error) {
        //   console.log('请求失败')
        // }
      },
      // 发送消息
      sendMessage () {
        this.haveContent = true
        if (this.message === '向小栈发送信息') return
        if (this.message !== '') {
            this.userSentence = this.message.trim()
            // 推送 用户信息 到聊天记录
            this.chatMessages.push({ role: 'user', content: this.userSentence })
            // 先推送一个空的ai回答占位符
            // this.chatMessages.push({ role: 'ai', content: '' })
            // 自动滚动到底部
            this.$nextTick(() => {
                const box = this.$refs.haveContent
                box[box.length - 1].scrollIntoView({ behavior: 'smooth' })
            })
        }
        this.message = '向小栈发送信息'
        this.$refs.textarea.blur()
        // this.$refs.message.style.bottom = '31px'
        this.answer = ''
        // 发送消息后开启加载状态
        this.isLoading = true 
        // 获得后端传回的ai回答
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
      // 生成唯一的chatId
      initChatId () {
        const storedChatId = sessionStorage.getItem('chatId')
        if (storedChatId) {
          this.chatId = storedChatId
        }else {
          const newChatId = generateChatID()
          sessionStorage.setItem('chatId', newChatId)
          this.chatId = newChatId
        }
      },
      // 结束前推送会话记录
      async postConversation () {
        const res = await axios.post(`http://localhost:8080/ai/chatHistory/${this.chatId}`, {}, {
          headers: {
              Authorization: 'Bearer ' + this.token
          }
        })
        console.log('推送会话记录: ', res)
      },
      // 获取会话内容
      async getConversationContent (historyChatId) {
        this.isClickHistoryChat = true
        const res = await axios.get(`http://localhost:8080/ai/message/${historyChatId}`, {
          headers: {
              Authorization: 'Bearer ' + this.token
          }
        })
        console.log('具体会话内容',res)
        // 前面如果有对话记录则清空
        this.isHistory = false
        this.chatMessages = []
        // 将数组倒过来赋值给chatMessages
        this.chatMessages = res.data.data.reverse()
        // console.log('倒过来数组',res.data.data.reverse())
        // 将本地存储的内容改为当前这个
        localStorage.setItem('chatMessages', JSON.stringify(this.chatMessages))
      },
      // 新增对话
      addNewChatMessage () {
        this.isClickHistoryChat = false
        // 清空当前对话记录
        this.chatMessages = []
        this.haveContent = false
        // 生成新的chatId
        const newChatId = generateChatID()
        sessionStorage.setItem('chatId', newChatId)
        this.chatId = newChatId
        // 清空本地存储的聊天记录和更新chatId
        localStorage.removeItem('chatMessages')
      },
      // 删除历史会话记录
      async delectHistoryRecord (chatId) {
        await axios.delete(`http://localhost:8080/ai/chatHistory/${chatId}`, {
          headers: {
              Authorization: 'Bearer ' + this.token
          }
        })
        // console.log('删除历史会话记录', res)
        // 将记录从数组中删去
        this.weekHistoryRecords = this.weekHistoryRecords.filter(item => item.chatId != chatId)
      }
    },
    created () {
      // 创建组件时就生成chatId
      this.initChatId()
    },
    async mounted () {
      this.isClickHistoryChat = true
      // 先从本地存储获取聊天记录和chatId
      const storedMessages = localStorage.getItem('chatMessages')
      const prevChatId = localStorage.getItem('prevChatId')
      // 更改会话存储中的chatId
      sessionStorage.setItem('chatId', prevChatId)
      if (storedMessages) {
        this.chatMessages = JSON.parse(storedMessages)
        this.haveContent = this.chatMessages.length > 0 ? true : false
      }
      // 获取全部历史会话记录
      const res = await axios.get('http://localhost:8080/ai/chatHistory', {
        headers: {
              Authorization: 'Bearer ' + this.token
          }
      })
      // console.log('全部历史会话记录: ',res.data.data)
      // 过滤掉空对话和没有时间的对话
      const filteredData = res.data.data.filter(item => item.summary != '空对话' && item.createTime != null)
      // console.log('过滤后的历史会话记录: ', filteredData)
      // 判断该会话记录距离现在过去多久，以一周，一月，一年分类显示
      const now = new Date()
      filteredData.forEach(item => {
        const createTime = new Date(item.createTime)
        const diffTime = now - createTime
        const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))
        if (diffDays <= 7) {
          item.timeCategory = '本周'
        } else if (diffDays <= 30) {
          item.timeCategory = '本月'
        } else if (diffDays <= 365) {
          item.timeCategory = '本年'
        } else {
          item.timeCategory = '更早'
        }
      })
      // console.log('分类后的历史会话记录: ', filteredData)
      // this.historyRecords = filteredData
      this.weekHistoryRecords = filteredData.filter(item => item.timeCategory === '本周')
      this.monthHistoryRecords = filteredData.filter(item => item.timeCategory === '本月')
      this.yearHistoryRecords = filteredData.filter(item => item.timeCategory === '本年')
    },
    beforeDestroy () {
      // 移除会话存储中的chatId
      sessionStorage.removeItem('chatId')
      // 如果没有对话则不推送会话记录
      if (this.chatMessages.length === 0) return
      if (this.isClickHistoryChat) return
      if (this.intervalId) {
        clearInterval(this.intervalId)
      }
      this.postConversation()
      // 将聊天记录和chatId存到本地存储
      localStorage.setItem('chatMessages', JSON.stringify(this.chatMessages))
      localStorage.setItem('prevChatId', this.chatId)
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
.right {
  position: absolute;
  top: 38px;
  right: 12px;
  img {
    width: 48px;
  }
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
.history {
  position: fixed;
  top: 0;
  display: flex;
  width: 390px;
  height: 844px;
  .mask {
    width: 90px;
    height: 100%;
    background: #00000080;
  }
  .subject {
    padding: 28px 16px;
    width: 300px;
    height: 100%;
    background: #ffefcf;
    .cancel {
      display: flex;
      align-items: center;
      img {
        margin-right: 78px;
        width: 24px;
      }
    }
    .record {
      margin-top: 50px;
      .time {
        margin: 16px 0;
        color: #6b6b6b;
        font-size: 14px;
      }
      ul {
        li {
          display: flex;
          align-items: center;
          width: 272px;
          height: 60px;
          img {
            margin-top: 2px;
            width: 16px;
            height: 16px;
          }
          p {
            width: 252px;
            color: #472000cc;
            font-size: 14px;
          }
        }
      }
    }
    .bin {
      position: fixed;
      right: 15px;
      bottom: 20px;
      img {
        width: 24px;
      }
    }
  }
}
</style>