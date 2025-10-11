<template>
  <div id="AI">
    <div class="title wrapper">AI情绪教练</div>
    <div class="dialog wrapper">
      <!-- 无对话时 -->
      <div class="noContent" v-if="!haveContent">
        <img src="@/assets/image/首页头像.png" alt="">
        <div class="hint">
          <p>你好呀~我是你的情绪教练小栈~</p>
          <p>跟我讲讲你现在感觉怎么样吧~</p>
        </div>
      </div>

      <!-- 有对话时 -->
      <div class="box" v-else>
        <div class="haveContent" ref="haveContent">
          <!-- <div class="userSentence">
              <div class="item">{{ userSentence }}</div>
          </div>
          <div class="answer">
            <div class="item">
              <img src="@/assets/image/头像.png" alt="">
              <p>{{ answer }}</p>
            </div>
          </div> -->
        </div>
      </div>

      <!-- 发送消息 -->
      <div class="message" ref="message">
        <textarea name="message" v-model="message" @focus="changeMessage" ref="textarea"></textarea>
        <div class="send">
          <img src="@/assets/image/发送.png" alt="" @click="sendMessage">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import request from '@/utlis/request'
export default {
    name: 'AIPage',
    data () {
      return {
        message: '向小栈发送信息',
        answer: '',
        userSentence: '',
        haveContent: false,
        str: ''
      }
    },
    methods: {
      render () {
        if (this.userSentence !== '') {
          this.str += ` <div data-v-3cd368c5 class="userSentence">
                          <div data-v-3cd368c5 class="item">${this.userSentence}</div>
                        </div>  `
        }
        if (this.answer !== '') {
          this.str += ` <div data-v-3cd368c5 class="answer">
                          <div data-v-3cd368c5 class="item">
                            <img data-v-3cd368c5 src="${require('@/assets/image/AI头像.png')}" alt="">
                            <p data-v-3cd368c5 >${this.answer}</p>
                          </div>
                        </div> `
        }
        if (this.haveContent === false) this.haveContent = true
            // 等dom元素挂载后再渲染
        this.$nextTick(() => {
          this.$refs.haveContent.innerHTML = this.str
        })
      },
      // async getAIAnswer () {
      //   const res = await request.get('')
      //   this.str = res.data
      //   this.render()
      // },

      sendMessage () {
        // 发送消息
        if (this.message === '向小栈发送信息') return
        if (this.message !== '') {
            this.userSentence = this.message.trim()
            this.render()
        }
        this.message = '向小栈发送信息'
        this.$refs.textarea.blur()
        // this.$refs.message.style.bottom = '31px'
        // 获得后端传回的ai回答
        // getAIAnswer()
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
    }
}
</script>

<style lang="less" scoped>
#AI {
  height: 852px;
  background: url('@/assets/image/图案组合.png') no-repeat #ffefcf;
  background-size: 390px;
}
.wrapper {
  margin: 0 auto;
  width: 365px;
}
.title {
  margin-top: 160px;
  text-align: center;
  color: #000000cc;
  font-size: 24px;
  font-weight: 600;
}
.dialog {
  // position: relative;
  margin-top: 21px;
  padding: 28px 16px;
  height: 619px;
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
      }
      .userSentence {
        align-self: flex-end;
        .item {
          margin-bottom: 10px;
          padding: 12px 17px;
          border-radius: 26px;
          background: #dff1f0;
        }
      }
      .answer {
        align-self: flex-start;
        .item {
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