<template>
  <div id="puzzleDetail">
    <NavBar :left-pic="leftPic"></NavBar>
    <div class="subject">
      <div class="simpleness">
        <div class="title"><img src="@/assets/image/拼图/简单.png" alt=""></div>
        <ul>
          <li><img src="@/assets/image/拼图/简单1.png" alt=""></li>
          <li><img src="@/assets/image/拼图/简单2.png" alt=""></li>
          <li><img src="@/assets/image/拼图/简单3.png" alt=""></li>
        </ul>
      </div>
      <div class="border"></div>
      <div class="medium">
        <div class="title"><img src="@/assets/image/拼图/中等.png" alt=""></div>
        <ul>
          <li><img src="@/assets/image/拼图/中等1.png" alt=""></li>
          <li><img src="@/assets/image/拼图/中等2.png" alt=""></li>
          <li><img src="@/assets/image/拼图/中等3.png" alt=""></li>
        </ul>
      </div>
      <div class="border"></div>
      <div class="difficulty">
        <div class="title"><img src="@/assets/image/拼图/困难.png" alt=""></div>
        <ul>
          <li><img src="@/assets/image/拼图/困难1.png" alt=""></li>
          <li><img src="@/assets/image/拼图/困难2.png" alt=""></li>
          <li><img src="@/assets/image/拼图/困难3.png" alt=""></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
  components: {
    NavBar
  },
  data () {
    return {
      leftPic: require('@/assets/image/拼图/关闭.png')
    }
  }
}
</script>

<style lang="less" scoped>
#puzzleDetail {
  width: 390px;
  height: 852px;
  background: #faf4e6;
}
.subject {
  margin: 9px auto;
  padding: 0 8px;
  width: 366px;
  height: 740px;
  border-radius: 10px;
  background: #fdf9f0;
  .title {
    margin: 30px 0;
    img {
      margin-left: 8px;
      width: 91px;
    }
  }
  ul {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    li {
      img {
        width: 110px;
      }
    }
  }
  .border {
    margin-top: 29px;
    width: 350px;
    height: 2px;
    background-color: #f5d99c;
  }
}
</style>