<template>
  <div id="NaVBar">
    <div class="left" @click="$router.go(-1)">
      <img src="@/assets/image/jt_left.png" alt="">
      <h3>{{ title }}</h3>
    </div>
    <!-- <div class="right">
      <img src="@/assets/image/更多1.png" alt="">
    </div> -->
  </div>
</template>

<script>

export default {
  props: ['title'],
  // data () {
  //   return {
  //     rightPic: '@/assets/image/更多1.png'
  //   }
  // }
}
</script>

<style lang="less" scoped>
#NaVBar {
  padding-top: 50px;
  padding-left: 13px;
  display: flex;
  justify-content: space-between;
  .left {
    margin-right: 14px;
    display: flex;
    height: 24px;
    // width: 24px;
    img {
    margin-left: 9px;
    margin-top: 3px;
    width: 24px;
    }
    h3 {
      margin-left: 14px;
      font-size: 18px;
    }
  }
  // .right {
  //   margin-top: 3px;
  //   margin-right: 22px;
  //   img {
  //     width: 24px;
  //     height: 24px;
  //   }
  // }
}
</style>