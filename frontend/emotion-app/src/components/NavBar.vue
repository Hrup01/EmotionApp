<template>
  <div id="NaVBar">
    <div class="left" @click="$router.go(-1)">
      <img src="@/assets/image/jt_left.png" alt="" v-if="!leftPic">
      <img :src="leftPic" alt="" class="otherPic" v-else>
      <!-- <img src="@/assets/image/私信/头像.png" alt="" class="headPic"> -->
      <h3 ref="title">{{ title }}</h3>
    </div>
    <!-- <div class="right" v-show="haveRight">
      <img :src="rightPic" alt="">
    </div> -->
  </div>
</template>

<script>

export default {
  props: ['title', 'haveRight', 'leftPic', 'rightPic', 'marginLeft'],
  // data () {
  //   return {
  //     rightPic: '@/assets/image/更多1.png'
  //   }
  // }
  mounted () {
    // console.log(this.marginLeft)
    this.$refs.title.style.marginLeft = this.marginLeft
  }
}
</script>

<style lang="less" scoped>
#NaVBar {
  padding-top: 20px;
  padding-left: 13px;
  display: flex;
  justify-content: space-between;
  width: 340px;
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
    .otherPic {
      width: 24px;
      height: 24px;
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