<template>
    <div class="navbar">
        <div class="attention" ref="attention">
          <router-link :to="navbarList.firstToUrl">
            <p ref="p1">{{ navbarList.firstName }}</p>
            <img :src="navbarList.bottomPicUrl" alt="" ref="pic1">
          </router-link>
        </div>
        <div class="recommend active" ref="recommend">
          <router-link :to="navbarList.secondToUrl">
            <p ref="p2">{{ navbarList.sencondName }}</p>
            <img :src="navbarList.bottomPicUrl" alt="" ref="pic2">
          </router-link>
        </div>
      </div>
</template>

<script>

export default {
  props: ['navbarList', 'pStyle', 'picStyle'],
  mounted () {
        const attention = this.$refs.attention
        // console.log(attention)
        const recommend = this.$refs.recommend
        const as = document.querySelectorAll('.navbar div')
        // console.log(as)
        const imgs = document.querySelectorAll('.navbar img')
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
        if (this.pStyle || this.picStyle) {
          // 改变p标签样式
          const p1 = this.$refs.p1
          const p2 = this.$refs.p2
          p1.style.color = this.pStyle.color
          p1.style.fontSize = this.pStyle.fontSize
          p2.style.color = this.pStyle.color
          p2.style.fontSize = this.pStyle.fontSize
          // 改变图片样式
          const pic1 = this.$refs.pic1
          const pic2 = this.$refs.pic2
          pic1.style.width = this.picStyle.width
          pic1.style.height = this.picStyle.height
          pic1.style.marginTop = this.picStyle.marginTop
          pic2.style.width = this.picStyle.width
          pic2.style.height = this.picStyle.height
          pic2.style.marginTop = this.picStyle.marginTop
          }
    }
}
</script>

<style lang="less" scoped>
.navbar {
    display: flex;
    justify-content: space-around;
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
        align-items: center;
        img {
          display: none;
          width: 39px;
          height: 10px;
        }
      }
    }
  }
</style>