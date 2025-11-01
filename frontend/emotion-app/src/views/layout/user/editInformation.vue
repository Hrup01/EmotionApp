<template>
  <div id="editInformation">
    <div class="header">
      <NavBar :title="navbarTitle" :is-transparent="isTransparent"></NavBar>
      <img src="@/assets/image/编辑个人资料/图片添加.png" alt="">
    </div>
    <div class="body">
      <img src="@/assets/image/编辑个人资料/头像.png" alt="" class="headPic">
      <img src="@/assets/image/编辑个人资料/编辑.png" alt="" class="edit">
      <div class="message">
        <div class="name item">
          <div class="title">昵称</div>
          <input type="text" v-model="name">
          <div class="border"></div>
        </div>
        <div class="gender item">
          <div class="title">性别</div>
          <input type="text" v-model="gender">
          <div class="border"></div>
        </div>
        <div class="birthday item">
          <div class="title">生日</div>
          <input type="text" v-model="birthday">
          <div class="border"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';
export default {
  components: {
    NavBar
  },
  data () {
    return {
      navbarTitle: '编辑个人资料',
      isTransparent: true,
      name: 'CaCa',
      gender: '不展示',
      birthday: '2000/6/17'
    }
  }
}
</script>

<style lang="less" scoped>
// #editInformation {
//   width: 390px;
//   height: 844px;
//   background: #faf4e6;
// }
.header {
  width: 390px;
  height: 250px;
  background: url('@/assets/image/我的/背景图.png');
  background-size: cover;
  img {
    margin-top: 51px;
    margin-left: 177px;
    width: 36px;
  }
}
.body {
  // position: relative;
  width: 390px;
  height: 594px;
  background: #faf4e6;
  .headPic {
    position: absolute;
    top: 191px;
    left: 23px;
    width: 110px;
    z-index: 1;
  }
  .edit {
    position: absolute;
    top: 271px;
    left: 106px;
    width: 30px;
    z-index: 2;
  }
  .message {
    margin-top: 94px;
    .item {
      margin: 0 auto;
      width: 305px;
      .title {
        margin-bottom: 8px;
        color: #290000cc;
        font-size: 14px;
      }
      input {
        width: 100%;
        background-color: #faf4e6;
        color: #000000b3;
        font-size: 18px;
        border: 0;
      }
      .border {
        margin-bottom: 24px;
        width: 100%;
        height: 2px;
        background-color: #F7DBBE;
      }
    }
  }
}
</style>