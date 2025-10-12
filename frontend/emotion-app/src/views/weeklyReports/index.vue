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
              <img :src="item.url" alt="">
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
export default {
    name: 'weeklyReports',
    components: {
      NavBar
    },
    data () {
      return {
        moodList: [
          { id: 0, url: require('@/assets/image/icon_1.png'), day: '一'},
          { id: 1, url: require('@/assets/image/icon_2.png'), day: '二'},
          { id: 2, url: require('@/assets/image/icon_1.png'), day: '三'},
          { id: 3, url: require('@/assets/image/icon_1.png'), day: '四'},
          { id: 4, url: require('@/assets/image/icon_3.png'), day: '五'},
          { id: 5, url: require('@/assets/image/icon_1.png'), day: '六'},
          { id: 6, url: require('@/assets/image/icon_4.png'), day: '日'}
        ],
        suggestList: [
          { id: 7, url: require('@/assets/image/green.png'), text: '有意识地将注意力停留在积极时刻上，延长和深化愉悦感，促进积极情绪的内化。'},
          { id: 8, url: require('@/assets/image/purple.png'), text: '主动与他人分享您的快乐。社会性的分享能强化积极情绪，并增强您的社会支持网络.'},
          { id: 9, url: require('@/assets/image/yellow.png'), text: '将愉悦感转化为探索新兴趣或技能的动力。成就感的获得能为您持续的幸福感提供新源泉。'},
        ],
        total: 7,
        time: 6,
        moodText: '开心',
        emojiUrl: require('@/assets/image/icon_1.png'),
        title: '心情周报',
      }
    },
    mounted () {
      const progress = this.time / (this.total * 3.5)
      const endDeg = progress * 360
      // console.log(endDeg)
      this.$refs.outer.style.background = `conic-gradient(#A3EBAF 0% ,#BEF3A5 ${endDeg}%, #F0FAF2 ${endDeg}% 360%)`
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
      img {
        margin-top: 4px;
        margin-right: 14px;
        width: 38px;
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