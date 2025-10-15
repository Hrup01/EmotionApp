<template>
  <div id="community">
    <!-- 头部导航栏 -->
    <div class="top wrapper">
      <img src="@/assets/image/汉堡图标.png" alt="">
      <sencond-nav-bar :navbar-list="navbarList"></sencond-nav-bar>
      <img src="@/assets/image/添加.png" alt="">
    </div>
    <!-- 二级导航出口 -->
    <router-view></router-view>
  </div>
</template>

<script>
import sencondNavBar from '@/components/sencondNavBar.vue';
export default {
    name: 'communicationSpace',
    components: {
      sencondNavBar
    },
    data () {
      return {
        navbarList: { firstName: '关注', sencondName: '推荐', bottomPicUrl: require('@/assets/image/阴影.png'), firstToUrl: '/community/attention', secondToUrl: '/community/recommend' }
      }
    }
}
</script>

<style lang="less" scoped>
#community {
  height: 844px;
  background-color: #FFEFCF;
}
.wrapper {
  margin: 0 auto;
  width: 366px;
}
.top {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  height: 44px;
  img {
    width: 32px;
    height: 32px;
  }
}
</style>