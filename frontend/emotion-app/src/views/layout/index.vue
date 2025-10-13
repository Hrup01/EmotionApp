<template>
  <div id="layout">
    <!-- 二级路由出口 -->
    <router-view></router-view>
    <!-- 底部导航栏 -->
    <div class="tabbar">
      <div class="home active" ref="home">
        <router-link to="/home">
          <img src="@/assets/image/首页_未激活.png" alt="" width="40px" height="38px">
          <img src="@/assets/image/阴影.png" alt="" class="shade">
          <p>首页</p>
        </router-link>
      </div>
      <div class="heal" ref="heal">
        <router-link to="/heal">
          <img src="@/assets/image/疗愈空间_未激活.png" alt="" width="38px">
          <img src="@/assets/image/阴影.png" alt="" class="shade">
          <p>疗愈空间</p>
        </router-link>
      </div>
      <div class="community" ref="community">
        <router-link to="/community">
          <img src="@/assets/image/交流空间_激活.png" alt="" width="38px" height="38px">
          <img src="@/assets/image/阴影.png" alt="" class="shade">
          <p>交流空间</p>
        </router-link>
      </div>
      <div class="user" ref="user">
        <router-link to="/user">
          <img src="@/assets/image/我的_未激活.png" alt="" width="34px" height="36px">
          <img src="@/assets/image/阴影.png" alt="" class="shade">
          <p>我的</p>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>

export default {
    name: 'layoutPage',
    data () {
      return {

      }
    },
    mounted () {
      const home = this.$refs.home
      const heal = this.$refs.heal
      const community = this.$refs.community
      const user = this.$refs.user
      const as = document.querySelectorAll('#layout .tabbar div')
      const shades = document.querySelectorAll('#layout .tabbar .shade')

      // 一开始就先激活
      as.forEach((item, index) => {
        // console.log(item.childNodes[0])
        if (item.childNodes[0].classList.contains('router-link-active')) {
          console.log(11)
          item.style.marginTop = '5px'
          shades[index].style.display = 'block'
          // item.classList.remove('active')
        }
      })

      const removeActived = function (label) {
        for (let i = 0; i < 4; i++) {
          if (!as[i].classList.contains(label)) {
            as[i].style.marginTop = '17px'
            shades[i].style.display = 'none'
          }
        }
      }

      home.addEventListener('click', function () {
        // const img = document.querySelector('.home .shade')
        this.style.marginTop = '5px'
        shades[0].style.display = 'block'
        // 清除其他激活后的样式
        removeActived('home')
      })
      heal.addEventListener('click', function () {
        // const img = document.querySelector('.heal .shade')
        this.style.marginTop = '5px'
        shades[1].style.display = 'block'
        // 清除其他激活后的样式
        removeActived('heal')
      })
      community.addEventListener('click', function () {
        // const img = document.querySelector('.community .shade')
        this.style.marginTop = '5px'
        shades[2].style.display = 'block'
        // 清除其他激活后的样式
        removeActived('community')
      })
      user.addEventListener('click', function () {
        // const img = document.querySelector('.user .shade')
        this.style.marginTop = '5px'
        shades[3].style.display = 'block'
        // 清除其他激活后的样式
       removeActived('user')
      })
    }
}
</script>

<style lang="less" scoped>

// 底部导航栏
.tabbar {
  padding: 0px 23px;
  display: flex;
  justify-content: space-between;
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 94px;
  background-color: #FEEFCE;
  box-shadow: 0 -3px 8px 0 #d4c39f;
  div {
    margin-top: 17px;
    width: 48px;
    a {
      img {
        display: block;
        margin: 0 auto;
      }
      .shade {
        display: none;
        margin: auto;
        width: 39px;
        height: 12px;
      }
      p {
        margin-top: 4px;
        color: #00000099;
        font-size: 12px;
        font-weight: 600;
        text-align: center;
      }
    }
  }
}
</style>