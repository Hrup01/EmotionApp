<template>
  <div id="home">
    <img src="@/assets/image/时间.png" alt="" class="top-time" ref="topTime">
    <!-- 侧边栏按键 -->
    <div class="sidebarPic" v-show="!showSidebar" @click="showSidebar = true"><img src="@/assets/image/首页/汉堡图标.png" alt=""></div>
    <div class="occupied" v-show="showSidebar"></div>
    <div class="pet wrapper">
      <div class="apperance">
        <img src="@/assets/image/首页/装扮.png" alt="" class="dress">
        <img src="@/assets/image/首页/狗.png" alt="" class="dog">
      </div>
      <div class="introduce">
        <div class="name">
          <p v-if="flag === 0">{{ petName }}</p>
          <input type="text" v-model="petName" ref="name" @keyup.enter="toBlur" v-else>
          <img src="@/assets/image/首页/编辑.png" alt="" ref="pen" @click="changeName">
        </div>
        <div class="age">2025年9月24日 1岁</div>
      </div>
    </div>
    <div class="slides wrapper">
      <ul>
        <!-- <li v-for="item in slidesList" :key="item.id"> -->
        <li>
          <img src="@/assets/image/首页/1.png" alt="" ref="slideshow">
        </li>
      </ul>
      <!-- 轮播图左右蒙版 -->
      <div class="mask">
        <div class="left" @click="clickLeft" ref="prev"></div>
        <div class="right" @click="clickRight" ref="next"></div>
      </div>
    </div>
    <div class="component wrapper">
      <router-link to="/moodOption">
        <img src="@/assets/image/首页/日记.png" alt="" class="dairy">
        <p>日记</p>
      </router-link>
      <router-link to="">
        <img src="@/assets/image/首页/涂鸦.png" alt="" class="graffiti">
        <p>手账</p>
      </router-link>
      <router-link to="/weeklyReports">
        <img src="@/assets/image/首页/周报.png" alt="" class="weeklyReport">
        <p>周报</p>
      </router-link>
      <router-link to="">
        <img src="@/assets/image/首页/心理测评.png" alt="" class="psychological">
        <p>心理测评</p>
      </router-link>
    </div>
    <div class="ai wrapper">
      <div class="top">
        <img src="@/assets/image/首页/ai情绪教练.png" alt="">
        <p>AI情绪教练</p>
      </div>
      <div class="bottom">
        <p>有情绪的话，找我聊聊吧~</p>
        <router-link to="/ai">开始聊天</router-link>
      </div>
    </div>
    <!-- 侧边栏 -->
    <div class="sidebar" v-show="showSidebar">
      <div class="body">
        <div class="cancel" @click="showSidebar = false"><img src="@/assets/image/侧边栏/关闭.png" alt=""></div>
        <div class="user">
          <img src="@/assets/image/侧边栏/头像.png" alt="">
          <p>{{ username }}</p>
        </div>
        <div class="components">
          <ul>
            <li v-for="item in componentList" :key="item.id">
              <router-link :to="item.toUrl">
                <img :src="item.url" alt="">
                <p>{{ item.name }}</p>
              </router-link>
            </li>
          </ul>
        </div>
      </div>
      <div class="mask"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'homePage',
  data () {
    return {
      petName: '小栈',
      flag: 0,
      showSidebar: false,
      slidesList: [
        { id: 0, url: require('@/assets/image/首页/1.png') },
        { id: 1, url: require('@/assets/image/首页/2.png') },
        { id: 2, url: require('@/assets/image/首页/3.png') },
        { id: 3, url: require('@/assets/image/首页/4.png') },
        { id: 4, url: require('@/assets/image/首页/5.png') },
        { id: 5, url: require('@/assets/image/首页/6.png') },
        { id: 6, url: require('@/assets/image/首页/7.png') },
        { id: 7, url: require('@/assets/image/首页/8.png') },
        { id: 8, url: require('@/assets/image/首页/9.png') },
      ],
      componentList: [
        { id: 0, url: require('@/assets/image/侧边栏/日记本.png'), name: '日记本', toUrl: '/diaryNoteBook' },
        { id: 1, url: require('@/assets/image/侧边栏/消息中心.png'), name: '消息中心', toUrl: '/messageCenter' },
        { id: 2, url: require('@/assets/image/侧边栏/草稿.png'), name: '我的草稿', toUrl: '/moodOption' },
        { id: 3, url: require('@/assets/image/侧边栏/我的收藏.png'), name: '我的收藏', toUrl: '/moodOption' },
        { id: 4, url: require('@/assets/image/侧边栏/我的关注.png'), name: '我的关注', toUrl: '/moodOption' },
        { id: 5, url: require('@/assets/image/侧边栏/浏览历史.png'), name: '浏览历史', toUrl: '/moodOption' },
        { id: 6, url: require('@/assets/image/侧边栏/帮助与反馈.png'), name: '帮助与反馈', toUrl: '/moodOption' },
        { id: 7, url: require('@/assets/image/侧边栏/社区公约.png'), name: '社区公约', toUrl: '/moodOption' },
      ],
      i: 0,
      timer: null,
      username: '用户名'
    }
  },
  methods: {
    // 本地存储宠物名
    setPetName () {
      localStorage.setItem('emotion_app_petName', this.petName)
    },
    changeName () {
      this.$refs.pen.style.display = 'none'
      this.flag = 1
      // 自动聚焦并清空名字 --> $nextTick
      this.$nextTick(() => {
        // console.log(this.$refs.name)
        this.$refs.name.focus()
        this.petName = ''
        // 失焦出现笔 input消失
        this.$refs.name.addEventListener('blur', () => {
          if (this.petName === '') this.petName = localStorage.getItem('emotion_app_petName')
          this.flag = 0
          this.$refs.pen.style.display = 'block'
          this.setPetName()
        })
      })
    },
    // 回车失焦
    toBlur () {
      this.$refs.name.blur()
      this.setPetName()
    },
    // 点击轮播
    clickSlideshow (mask) {
      if (mask === 'left') {
        this.i++
        if (this.i > (this.slidesList.length - 1)) this.i = 0
        this.$refs.slideshow.src = this.slidesList[this.i].url
        // console.log(this.i)
      }else {
        this.i--
        if (this.i < 0) this.i = (this.slidesList.length - 1)
        this.$refs.slideshow.src = this.slidesList[this.i].url
        // console.log(this.i)
      }
    },
    // 点击左侧
    clickLeft () {
      this.clickSlideshow('left')
    },
    // 点击右侧
    clickRight () {
      this.clickSlideshow('right')
    }
  },
  mounted () {
    // 获取本地存储的宠物名
    this.petName = localStorage.getItem('emotion_app_petName')

    // 自动轮播
    this.timer = setInterval(() => {
      this.$refs.prev.click()
    },3000)
    // 点击时关闭定时器
    this.$refs.prev.addEventListener('mousedown',() => {
      clearInterval(this.timer)
    })
    this.$refs.prev.addEventListener('mouseup',() => {
        this.timer = setInterval(() => {
            this.$refs.prev.click()
        },3000)
    })
    this.$refs.next.addEventListener('mousedown',() => {
        clearInterval(this.timer)
    })
    this.$refs.next.addEventListener('mouseup',() => {
        this.timer = setInterval(() => {
            this.$refs.prev.click()
        },3000)
    })
  },
  beforeDestroy () {
    // 组件结束后销毁定时器
    clearInterval(this.timer)
  }
}

</script>

<style lang="less" scoped>
#home {
  width: 390px;
  height: 844px;
  background: #fdf4e4;
}
.wrapper {
  margin: 0 auto;
  width: 353px;
}
.top-time {
  width: 390px;
  background-color: #FDF4E4;
}
.sidebarPic {
  // margin-top: 20px;
  margin-left: 12px;
  img {
    width: 30px;
    height: 30px;
  }
}
.occupied {
  margin-top: 34px;
}
.pet {
  margin-top: 12px;
  position: relative;
  display: flex;
  // justify-content: space-around;
  height: 160px;
  .apperance {
    .dress {
      position: absolute;
      left: 8px;
      top: 0;
      width: 20px;
      height: 20px;
    }
    .dog {
      margin-left: 35px;
      width: 95px;
      height: 135px;
    }
  }
  .introduce {
    margin-left: 50px;
    color: #000000cc;
    font-weight: 550;
    .name {
      margin-top: 45px;
      display: flex;
      align-items: center;
      font-size: 16px;
      input {
        border: 0;
        width: 10em;
        background-color: #FDF4E4;
      }
      img {
        margin-left: 2px;
        width: 14px;
        height: 14px;
      }
    }
    .age {
      margin-top: 2px;
      font-size: 12px;
    }
  }
}
.slides {
  position: relative;
  ul {
    display: flex;
    overflow: hidden;
    // width: 100%;
    li {
      margin-left: 5px;
      flex-shrink: 0;
      img {
        width: 350px;
        height: 230px;
      }
    }
  }
  .mask {
    position: absolute;
    top: 0;
    display: flex;
    justify-content: space-between;
    height: 230px;
    width: 353px;
    .left,
    .right {
      width: 50px;
    }
  }
}
.component {
  margin-top: 12px;
  padding: 0 10px;
  display: flex;
  justify-content: space-between;
  text-align: center;
  .dairy {
    width: 48px;
    height: 52px;
  }
  .graffiti {
    width: 50px;
    height: 47px;
  }
  .weeklyReport {
    width: 52px;
    height: 52px;
  }
  .psychological {
    width: 52px;
    height: 52px;
  }
  p {
    margin-top: 8px;
    color: #000000;
    font-size: 14px;
    font-weight: 600;
  }
}
.ai {
  margin-top: 22px;
  padding: 8px 20px;
  height: 110px;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 2px 4px 0 #fcd6a5;
  .top {
    display: flex;
    align-items: center;
    img {
      width: 20px;
      height: 23px;
    }
    p {
      margin-left: 8px;
      color: #000000cc;
      font-size: 14px;
      font-weight: 600;
    }
  }
  .bottom {
    margin-top: 5px;
    display: flex;
    justify-content: space-between;
    width: 313px;
    height: 65px;
    border-radius: 8px;
    background: #fdf5e7;
    p {
      margin-left: 16px;
      line-height: 65px;
      color: #1a0040d4;
      font-size: 14px;
      font-weight: 600;
    }
    a {
      margin-top: 15px;
      margin-right: 8px;
      width: 80px;
      height: 36px;
      border-radius: 36px;
      background: #f7968d;
      color: #ffffff;
      font-size: 14px;
      font-weight: 600;
      line-height: 36px;
      text-align: center;
    }
  }
}
.sidebar {
  position: fixed;
  top: 0;
  display: flex;
  width: 390px;
  height: 844px;
  z-index: 1;
  // background-color: pink;
  .body {
    width: 60%;
    background: #fcf5e8;
    font-weight: 600;
    color: #000000cc;
    .cancel {
      margin-top: 32px;
      margin-left: 14px;
      img {
        width: 28px;
        height: 28px;
      }
    }
    .user {
      margin-top: 30px;
      margin-left: 28px;
      display: flex;
      align-items: center;
      img {
        margin-right: 15px;
        width: 59px;
        height: 59px;
      }
      p {
        font-size: 20px;
      }
    }
    .components {
      margin-top: 45px;
      margin-left: 28px;
      img {
        margin-right: 10px;
        width: 30px;
        height: 30px;
      }
      ul {
        li {
          height: 60px;
          a {
            display: flex;
            align-items: center;
          }
        }
      }
    }
  }
  .mask {
    flex: 1;
    background: #00000080;
  }
}
</style>