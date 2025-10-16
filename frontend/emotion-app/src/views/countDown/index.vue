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
      </div>
    </div>
    <div class="optionTiming wrapper">
      <h3>选择定时</h3>
      <div class="option">
        <ul>
          <li></li>
        </ul>
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
      hour: '00',
      minute: '00',
      second: '00'
    }
  }
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
    }
    .maohao {
      margin: 0 6px;
      width: 10px;
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
  }
}
</style>