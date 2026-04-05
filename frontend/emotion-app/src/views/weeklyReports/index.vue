<template>
  <div id="weeklyReports">
    <NavBar :title="title"></NavBar>
    <div class="record wrapper">
      <h5>本周心情记录</h5>
      <ul>
        <li v-for="item in moodList" :key="item.id">
          <p>{{ item.day }}</p>
          <img :src="item.url" alt="">
        </li>
      </ul>
      <div class="statistics">
        <p>本周共记录情绪</p>
        <div class="total">{{ time }}次</div>
      </div>
    </div>

    <div class="middle wrapper">
      <!-- 左边部分 -->
      <div class="proportion" v-if="time">
        <p>本周占比最大的情绪是</p>
        <h3>{{ moodText }}</h3>
        <div class="pic">
          <img :src="emojiUrl" alt="" class="emoji">
          <img src="@/assets/image/huangguan.png" alt="" class="huangguan">
        </div>
        <p class="tip">保持好心情~</p>
      </div>
      <!-- 0记录版本 -->
       <div class="zeroRecording" v-else>
        <p>本周还没记录心情哦</p>
        <p>去记录心情吧！</p>
        <div class="toMoodOption" @click="$router.push('/moodOption')">
          <p>选择心情</p>
          <img src="@/assets/image/jt_right.png" alt="">
        </div>
       </div>

      <!-- 右边部分 -->
      <div class="time">
        <p>本周日记打卡次数</p>
        <!-- 打卡次数可视化 -->
        <div class="circular" ref="outer">
          <div class="inner" ref="inner"><p>{{ time }}次</p></div>
        </div>
      </div>
    </div>

    <div class="bottom wrapper">
      <div class="haveRecording" v-if="time">
        <h2>本周情绪建议</h2>
        <div class="suggest">
          <ul>
            <li v-for="item in suggestList" :key="item.id">
              <img :src="item.url" alt="" v-show="item.text !== ''">
              <p>{{ item.text }}</p>
            </li>
          </ul>
          <div class="toAI" @click="$router.push('/ai')">
            <p>AI情绪教练</p>
            <img src="@/assets/image/jt_right.png" alt="">
          </div>
        </div>
      </div>
      <!--  0记录版本 -->
      <div class="zeroRecording" v-else>
        <div class="remind">
          <img src="@/assets/image/空记录.png" alt="">
          <p>本周还没记录心情，小栈无法给出建议哦</p>
          <p>去记录心情吧！</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import axios from 'axios';
export default {
    name: 'weeklyReports',
    components: {
      NavBar
    },
    data () {
      return {
        token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
        moodList: [
          { id: 0, url: require('@/assets/image/1.png'), day: '一'},
          { id: 1, url: require('@/assets/image/空缺心情_笑.png'), day: '二'},
          { id: 2, url: require('@/assets/image/10.png'), day: '三'},
          { id: 3, url: require('@/assets/image/10.png'), day: '四'},
          { id: 4, url: require('@/assets/image/空缺心情_笑.png'), day: '五'},
          { id: 5, url: require('@/assets/image/3.png'), day: '六'},
          { id: 6, url: require('@/assets/image/空缺心情_笑.png'), day: '日'}
        ],
        suggestList: [
          { id: 7, url: require('@/assets/image/green.png'), text: ''},
          { id: 8, url: require('@/assets/image/purple.png'), text: ''},
          { id: 9, url: require('@/assets/image/yellow.png'), text: ''},
        ],
        total: 7,
        time: '',
        moodText: '开心',
        emojiUrl: require('@/assets/image/icon_1.png'),
        title: '心情周报',
      }
    },
    async mounted () {
      // 获取上周每日记录
      const moodResponse = await axios.get('http://localhost:8080/dayEmotionRecord', {
        headers: {
          Authorization: `Bearer ${this.token}`
        }
      })
      console.log('上周每日记录', moodResponse.data.data)
      // 获取上周一周周报
      const weeklyReports = await axios.get('http://localhost:8080/weeklyReport', {
        headers: {
          Authorization: `Bearer ${this.token}`
        }
      })
      console.log('上周一周周报', weeklyReports.data.data)
      // 记录次数
      this.time = weeklyReports.data.data.count
      // 渲染打卡次数可视化
      const progress = this.time / (this.total * 3.5)
      const endDeg = progress * 360
      // console.log(endDeg)
      this.$refs.outer.style.background = `conic-gradient(#A3EBAF 0% ,#BEF3A5 ${endDeg}%, #F0FAF2 ${endDeg}% 360%)`
      // 最大占比情绪
      this.moodText = weeklyReports.data.data.moreEmotion
      // AI建议
      const emotionAdvice = weeklyReports.data.data.emotionAdvice
      // console.log('emotionAdvice', emotionAdvice)
      // 定义正则，用于分割#
      const regex = /#([^#]+)#/g
      const suggestList = []
      let match
      // 循环提取建议内容
      while ((match = regex.exec(emotionAdvice)) !== null) {
        suggestList.push(match[1])
      }
      // 将建议内容赋值到suggestList中
      for (let i = 0; i < this.suggestList.length; i++) {
        this.suggestList[i].text = suggestList[i] || ''
      }
    }
}
</script>

<style lang="less" scoped>
.wrapper {
  width: 355px;
  margin: 0 auto;
}
#weeklyReports {
  height: 844px;
  background-color: #FEF8EC;
}
.record {
  margin-top: 12px;
  padding-top: 19px;
  height: 177px;
  border-radius: 20px;
  background: #ffffffe6;
  color: #000000cc;
  h5 {
    font-size: 14px;
    text-align: center;
  }
  ul {
    margin-top: 16px;
    padding: 0 12px;
    display: flex;
    justify-content: space-between;
    text-align: center;
    font-size: 14px;
    color: #00000066;
    font-weight: 600;
    img {
      margin-top: 12px;
      width: 40px;
    }
  }
}
.statistics {
  margin-top: 17px;
  display: flex;
  justify-content: center;
  height: 23px;
  line-height: 23px;
  font-weight: 600;
  p {
    font-size: 12px;
    text-align: center;
    color: #000000b3;
  }
  .total {
    margin-left: 12px;
    color: #fd8d8c;
    font-size: 16px;
  }
}
.middle {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  .proportion,
  .time,
  .zeroRecording {
    padding: 17px 21px;
    width: 173px;
    height: 185px;
    border-radius: 20px;
    background: #ffffffe6;
    p {
      font-size: 12px;
      font-weight: 600;
      color: #000000b3;
    }
    h3 {
      height: 30px;
      font-size: 20px;
      text-align: center;
      color: #000000cc;
      background: url('@/assets/image/underline.png') no-repeat 40px 18px;
      background-size: 44px;
    }
    .pic {
      margin-top: 6px;
      position: relative;
      height: 82px;
      .emoji {
        position: absolute;
        left: 22px;
        width: 82px;
      }
      .huangguan {
        position: absolute;
        top: -18px;
        left: 80px;
        width: 40px;
      }
    }
    .tip {
      font-size: 10px;
      color: #00000099;
    }
    // 打卡次数可视化
    .circular {
      position: relative;
      margin-top: 13px;
      width: 123px;
      height: 123px;
      border-radius: 123px;
      background: conic-gradient(rgb(34,197,94) 0%, rgb(34,197,94) 58%, rgb(209,240,220) 58%, rgb(5,228,42) 360%);
      transition: all 0.5s;
      box-shadow: 0 4px 4px 0 #00000040;
      .inner {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
        width: 85%;
        height: 85%;
        border-radius: 99px;
        background-color: #fff;
        box-shadow: 0 4px 4px 0 #00000040 inset;
        p {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%,-50%);
          color: #00000080;
          font-size: 24px;
        }
      }
    }
  }
  // 0记录样式
  .zeroRecording {
    padding: 55px 32px;
    p {
      text-align: center;
    }
    .toMoodOption {
      margin-top: 8px;
      padding: 0 16px;
      display: flex;
      width: 108px;
      height: 36px;
      border-radius: 10px;
      background: #ffb870;
      box-shadow: 0 2px 4px 0 #00000040;
      p {
        color: #ffffff;
        font-weight: normal;
        font-size: 12px;
        line-height: 36px;
      }
      img {
        margin-top: 8px;
        margin-left: 8px;
        width: 20px;
        height: 20px;
        vertical-align: middle;
      }
    }
  }
}
.bottom{
  margin-top: 16px;
  .suggest,
  .remind {
    margin-top: 12px;
    padding-top: 7px;
    padding-right: 20px;
    height: 263px;
    border-radius: 20px;
    background: #ffffffe6;
  }
  h2 {
    font-size: 18px;
    font-weight: 600;
  }
  .suggest {
    li {
      margin-top: 28px;
      margin-left: 19px;
      display: flex;
      align-items: center;
      img {
        flex-shrink: 0;
        margin-top: 4px;
        margin-right: 14px;
        // width: 38px;
        height: 24px;
        
      }
      p {
        color: #000000b3;
        font-size: 12px;
        font-weight: 600;
      }
    }
    .toAI {
      margin-top: 11px;
      margin-left: 200px;
      padding-top: 10px;
      padding-left: 18px;
      display: flex;
      justify-content: space-between;
      width: 115px;
      height: 36px;
      border-radius: 10px;
      background: #ffb870;
      box-shadow: 0 2px 4px 0 #00000040;
      p {
        // margin-right: 6px;
        color: #ffffff;
        font-size: 12px;
        font-weight: 600;
      }
      img {
        position: relative;
        top: -2px;
        margin-right: 11px;
        width: 20px;
        height: 20px;
      }
    }
  }
  // 0记录样式
  .remind {
    padding-top: 36px;
    img {
      margin-left: 92px;
      width: 171px;
      height: 109px;
    }
     p {
      margin-left: 20px;
      text-align: center;
      color: #0000004d;
      font-size: 12px;
      font-weight: 600;
    }
  }
}
</style>