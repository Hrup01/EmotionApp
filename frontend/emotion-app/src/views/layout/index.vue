<template>
  <div id="layout">
    <!-- 二级路由出口 -->
    <router-view></router-view>
    <!-- 底部导航栏 -->
    <div class="tabbar">
      <div class="home active" ref="home">
        <router-link to="/home">
          <img src="@/assets/image/首页_激活.png" alt="" width="40px" height="38px">
          <!-- <img src="@/assets/image/首页_未激活.png" alt="" width="40px" height="38px" style="display: none;"> -->
          <img src="@/assets/image/阴影.png" alt="" class="shade">
          <p>首页</p>
        </router-link>
      </div>
      <div class="heal" ref="heal">
        <router-link to="/heal">
          <img src="@/assets/image/疗愈空间_激活.png" alt="" width="38px">
          <!-- <img src="@/assets/image/疗愈空间_未激活.png" alt="" width="38px" style="display: none;"> -->
          <img src="@/assets/image/阴影.png" alt="" class="shade">
          <p>疗愈空间</p>
        </router-link>
      </div>
      <div class="community" ref="community">
        <router-link to="/community">
          <img src="@/assets/image/交流空间_激活.png" alt="" width="38px" height="38px">
          <!-- <img src="@/assets/image/交流空间_未激活.png" alt="" width="38px" height="38px" style="display: none;"> -->
          <img src="@/assets/image/阴影.png" alt="" class="shade">
          <p>交流空间</p>
        </router-link>
      </div>
      <div class="user" ref="user">
        <router-link to="/user">
          <img src="@/assets/image/我的_激活.png" alt="" width="34px" height="36px">
          <!-- <img src="@/assets/image/我的_未激活.png" alt="" width="34px" height="36px" style="display: none;"> -->
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
    methods: {
      setActiveTab () {
        // const home = this.$refs.home
        // const heal = this.$refs.heal
        // const community = this.$refs.community
        // const user = this.$refs.user
        const as = document.querySelectorAll('#layout .tabbar div')
        const shades = document.querySelectorAll('#layout .tabbar .shade')
        // const homeImg = home.querySelectorAll('.home img')
        // const healImg = heal.querySelectorAll('.heal img')
        // const communityImg = community.querySelectorAll('.community img')
        // const userImg = user.querySelectorAll('.user img')
        as.forEach((item, index) => {
          console.log(11)
          // 激活时样式
          if (item.childNodes[0].classList.contains('router-link-active')) {
            // console.log(11)
            item.style.marginTop = '5px'
            shades[index].style.display = 'block'
            // item.classList.remove('active')
            // 切换图片显示状态
            // if (item.classList.contains('home')) {
            //   console.log(11)
            //   homeImg[0].style.display = 'none'
            //   homeImg[1].style.display = 'block'
            // } else if (item.classList.contains('heal')) {
            //   console.log(22)
            //   healImg[0].style.display = 'none'
            //   healImg[1].style.display = 'block'
            // } else if (item.classList.contains('community')) {
            //   console.log(33)
            //   communityImg[0].style.display = 'none'
            //   communityImg[1].style.display = 'block'
            // } else if (item.classList.contains('user')) {
            //   console.log(44)
            //   userImg[0].style.display = 'none'
            //   userImg[1].style.display = 'block'
            // }
          }
        })
      }
    },
    mounted () {
      const home = this.$refs.home
      const heal = this.$refs.heal
      const community = this.$refs.community
      const user = this.$refs.user
      const as = document.querySelectorAll('#layout .tabbar div')
      const shades = document.querySelectorAll('#layout .tabbar .shade')
      // const homeImg = home.querySelectorAll('.home img')
      // const healImg = heal.querySelectorAll('.heal img')
      // const communityImg = community.querySelectorAll('.community img')
      // const userImg = user.querySelectorAll('.user img')

      // 一开始就先激活
      // as.forEach((item, index) => {
      //   // console.log(item.childNodes[0])
      //   // 激活时样式
      //   if (item.childNodes[0].classList.contains('router-link-active')) {
      //     // console.log(11)
      //     item.style.marginTop = '5px'
      //     shades[index].style.display = 'block'
      //     // item.classList.remove('active')
      //     // 切换图片显示状态
      //     if (item.classList.contains('home')) {
      //       homeImg[0].style.display = 'none'
      //       homeImg[1].style.display = 'block'
      //     } else if (item.classList.contains('heal')) {
      //       healImg[0].style.display = 'none'
      //       healImg[1].style.display = 'block'
      //     } else if (item.classList.contains('community')) {
      //       communityImg[0].style.display = 'none'
      //       communityImg[1].style.display = 'block'
      //     } else if (item.classList.contains('user')) {
      //       userImg[0].style.display = 'none'
      //       userImg[1].style.display = 'block'
      //     }
      //   }
      // })
      this.setActiveTab()

      const removeActived = function (label) {
        for (let i = 0; i < 4; i++) {
          if (!as[i].classList.contains(label)) {
            as[i].style.marginTop = '17px'
            shades[i].style.display = 'none'
            // if (as[i].classList.contains('home')) {
            //   homeImg[0].style.display = 'block'
            //   homeImg[1].style.display = 'none'
            // } else if (as[i].classList.contains('heal')) {
            //   healImg[0].style.display = 'block'
            //   healImg[1].style.display = 'none'
            // } else if (as[i].classList.contains('community')) {
            //   communityImg[0].style.display = 'block'
            //   communityImg[1].style.display = 'none'
            // } else if (as[i].classList.contains('user')) {
            //   userImg[0].style.display = 'block'
            //   userImg[1].style.display = 'none'
            // }
          }
        }
      }

      home.addEventListener('click', function () {
        // const img = document.querySelector('.home .shade')
        this.style.marginTop = '5px'
        shades[0].style.display = 'block'
        // homeImg[0].style.display = 'none'
        // homeImg[1].style.display = 'block'
        // 清除其他激活后的样式
        removeActived('home')
      })
      heal.addEventListener('click', function () {
        // const img = document.querySelector('.heal .shade')
        this.style.marginTop = '5px'
        shades[1].style.display = 'block'
        // healImg[0].style.display = 'none'
        // healImg[1].style.display = 'block'
        // 清除其他激活后的样式
        removeActived('heal')
      })
      community.addEventListener('click', function () {
        // const img = document.querySelector('.community .shade')
        this.style.marginTop = '5px'
        shades[2].style.display = 'block'
        // communityImg[0].style.display = 'none'
        // communityImg[1].style.display = 'block'
        // 清除其他激活后的样式
        removeActived('community')
      })
      user.addEventListener('click', function () {
        // const img = document.querySelector('.user .shade')
        this.style.marginTop = '5px'
        shades[3].style.display = 'block'
        // userImg[0].style.display = 'none'
        // userImg[1].style.display = 'block'
        // 清除其他激活后的样式
       removeActived('user')
      })
    },
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