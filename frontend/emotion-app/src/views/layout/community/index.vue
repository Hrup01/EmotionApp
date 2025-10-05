<template>
  <div id="community">
    <!-- 头部导航栏 -->
    <div class="top wrapper">
      <img src="@/assets/image/汉堡图标.png" alt="">
      <div class="navbar">
        <div class="attention" ref="attention">
          <router-link to="/community/attention">
            <p>关注</p>
            <img src="@/assets/image/阴影.png" alt="">
          </router-link>
        </div>
        <div class="recommend active" ref="recommend">
          <router-link to="/community/recommend">
            <p>推荐</p>
            <img src="@/assets/image/阴影.png" alt="">
          </router-link>
        </div>
      </div>
      <img src="@/assets/image/添加.png" alt="">
    </div>
    <!-- 二级导航出口 -->
    <router-view></router-view>
  </div>
</template>

<script>

export default {
    name: 'communicationSpace',
    mounted () {
      const attention = this.$refs.attention
      // console.log(attention)
      const recommend = this.$refs.recommend
      const as = document.querySelectorAll('#community .navbar div')
      // console.log(as)
      const imgs = document.querySelectorAll('#community .navbar img')
      // console.log(imgs)

      // 一开始就先激活
      as.forEach((item, index) => {
        // console.log(item)
        if (item.classList.contains('active')) {
          item.style.color = '#383838'
          // console.log(imgs[index])
          imgs[index].style.display = 'block'
        }
      })

      const removeActived = function (label) {
        for (let i = 0; i < 2; i++) {
          if (!as[i].classList.contains(label)) {
            as[i].style.color = '#575757'
            imgs[i].style.display = 'none'
          }
        }
      }

      attention.addEventListener('click', function () {
        this.style.color = '#383838'
        imgs[0].style.display = 'block'
        removeActived('attention')
      })
      recommend.addEventListener('click', function () {
        this.style.color = '#383838'
        imgs[1].style.display = 'block'
        removeActived('recommend')
      })
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
  .navbar {
    display: flex;
    // line-height: 32px;
    div {
      margin: 2px 23px;
      width: 40px;
      color: #575757;
      font-size: 18px;
      font-weight: 600;
      text-align: center;
      a {
        display: flex;
        flex-direction: column;
        justify-content: center;
        img {
          display: none;
          width: 39px;
          height: 10px;
        }
      }
    }
  }
}
</style>