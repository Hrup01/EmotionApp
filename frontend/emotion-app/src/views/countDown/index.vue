<template>
  <div id="countDownPage">
    <NavBar></NavBar>
    <div class="top wrapper">
      <h3>倒计时</h3>
      <div class="countDown">
        <div class="hour time">{{ hour }}</div>
        <div class="maohao time">:</div>
        <div class="minute time">{{ minute }}</div>
        <div class="maohao time">:</div>
        <div class="second time">{{ second }}</div>
        <p v-if="hour === '00' && minute === '00' && second === '00'">暂未开启</p>
        <p v-else @click="startCounting">开始</p>
      </div>
    </div>
    <div class="optionTiming wrapper">
      <h3>选择定时</h3>
      <div class="option">
        <ul>
          <li class="item" v-for="(item, index) in timeList" :key="item.id">
            <p>{{ item.text }}</p>
            <div class="pic" @click="changeCheck(index)">
              <img src="@/assets/image/倒计时/选项.png" alt="" v-if="!item.isCheck">
              <img src="@/assets/image/倒计时/选中.png" alt="" v-else>
            </div>
          </li>
        </ul>
      </div>
      <div class="otherSet">
        <h3>其他设置</h3>
        <div class="autoStop">
          <p>播放完后自动关闭</p>
          <div class="pic" @click="finshStop = finshStop ? false : true">
            <img src="@/assets/image/倒计时/选项.png" alt="" v-if="!finshStop">
            <img src="@/assets/image/倒计时/选中.png" alt="" v-else>
          </div>
        </div>
      </div>
    </div>
    <div class="userDefined" v-show="isUserDefined">
      <div class="timing">
        <div class="top">
          <div class="title">自定义定时</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
  name: 'countDown',
  components: {
    NavBar
  },
  data () {
    return {
      totalSecond: '0',
      hour: '60',
      minute: '00',
      second: '02',
      finshStop: false,
      isUserDefined: false,
      timeList: [
        { id: 0, text: '不定时', isCheck: false, totalSecond: '' },
        { id: 1, text: '10分钟', isCheck: false, totalSecond: '600' },
        { id: 2, text: '20分钟', isCheck: false, totalSecond: '1200' },
        { id: 3, text: '30分钟', isCheck: false, totalSecond: '1800' },
        { id: 4, text: '40分钟', isCheck: false, totalSecond: '2400' },
        { id: 5, text: '50分钟', isCheck: false, totalSecond: '3000' },
        { id: 6, text: '60分钟', isCheck: false, totalSecond: '3600' },
      ],
      timer: null
    }
  },
  methods: {
    // 点击改变选中状态
    changeCheck (index) {
      // 遍历数组 将另一个isCheck: true改成false
      this.timeList.map((item) => {
        // console.log(item.isCheck)
        if (item.isCheck) item.isCheck = false
      })
      this.timeList[index].isCheck = this.timeList[index].isCheck ? false : true
      // 上方倒计时也随之改变
      // console.log(this.timeList[index].totalSecond)
      this.totalSecond = this.timeList[index].totalSecond
      this.render()
    },
    // 渲染页面倒计时
    render () {
      this.hour = Math.floor(this.totalSecond / 60 / 60)
      this.minute = Math.floor(this.totalSecond / 60)
      this.second = this.totalSecond % 60
      // 补0
      this.hour = this.hour < 10 ? '0' + this.hour : this.hour
      this.minute = this.minute < 10 ? '0' + this.minute : this.minute === 60 ? '00' : this.minute
      this.second = this.second < 10 ? '0' + this.second : this.second
    },
    // 开始倒计时
    updateCountdown () {
      this.render()
      // 减少总秒数
      this.totalSecond --
      if (this.totalSecond < 0) clearInterval(this.timer)
    },
    startCounting () {
      this.timer = setInterval(this.updateCountdown,1000)
    }
  },
  mounted () {
    // 组件渲染完成后将totalSecond转成对应的时分秒
    this.render()
  },
}
</script>

<style lang="less" scoped>
#countDownPage {
  width: 390px;
  height: 852px;
  background: #fdf4e4;
  font-weight: 600;
  h3 {
    color: #180326d4;
    font-size: 20px;
  }
}
.wrapper {
  margin: 0 auto;
  width: 341px;
}
.top {
  margin-top: 14px;
  .countDown {
    margin-top: 14px;
    padding: 30px 16px;
    display: flex;
    align-items: center;
    width: 341px;
    height: 110px;
    border-radius: 10px;
    border: 1px solid #fce5a6;
    background: #fff9ef;
    .time {
      width: 44px;
      height: 51px;
      color: #ffce71;
      font-size: 36px;
      font-weight: 400;
    }
    .maohao {
      margin: 0 6px;
      width: 10px;
    }
    p {
      margin-left: 58px;
      color: #00000066;
      font-size: 18px;
    }
  }
}
.optionTiming {
  margin-top: 34px;
  .option {
    margin-top: 14px;
    width: 100%;
    height: 350px;
    background: #fff9ef;
    border-radius:10px;
    border: 1px solid #fce5a6;
    ul {
      li {
        margin-left: 16px;
        display: flex;
        align-items: center;
        height: 50px;
        p {
          width: 278px;
          color: #000000b3;
        }
        img {
          width: 18px;
          height: 18px;
        }
      }
    }
  }
}
.otherSet {
  h3 {
    margin: 24px 0 14px 0;
  }
  .autoStop {
    padding-left: 16px;
    display: flex;
    align-items: center;
    width: 341px;
    height: 50px;
    border-radius: 19px;
    border: 1px solid #fce5a6;
    background: #fff9ef;
    p {
      width: 278px;
      color: #000000b3;
    }
    img {
      width: 18px;
      height: 18px;
    }
  }
}
.userDefined {
  position: fixed;
  top: 0;
  width: 390px;
  height: 844px;
  background: #00000080;
  .timing {
    position: fixed;
    left: 25px;
    bottom: 34px;
    width: 341px;
    height: 350px;
    border-radius: 20px;
    border: 1px solid #f9c844;
    background: #fff9ef;
    .top {
      margin: 0;
      width: 341px;
      height: 56px;
      border-radius: 20px 20px 0 0;
      background: #fce5a6a8;
      .title {
        margin: 16px;
        color: #000000b3;
      }
    }
  }
}
</style>