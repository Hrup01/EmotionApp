<template>
  <div id="woodFish">
    <nav-bar :title="navbarTitle"></nav-bar>
    <div class="top wrapper">
      <ul>
        <li class="dressUp" @click="isDressUp = true">
          <img src="@/assets/image/木鱼/装扮.png" alt="">
          <p>木鱼装扮</p>
        </li>
        <li class="char" @click="isCharDetail = true">
          <img src="@/assets/image/木鱼/悬浮文字.png" alt="">
          <p>悬浮文字</p>
        </li>
        <li @click="$router.push('/woodFish/moreSet')">
          <img src="@/assets/image/木鱼/设置.png" alt="">
          <p>更多设置</p>
        </li>
      </ul>
    </div>
    <div class="body wrapper" ref="scene">
      <div class="time">
        <div class="title">今天已敲<span>{{ time }}</span>次</div>
        <img src="@/assets/image/木鱼/小木鱼棍.png" alt="" class="smallStick">
        <img src="@/assets/image/木鱼/小木鱼.png" alt="">
      </div>
      <!-- 敲击出现悬浮文字 -->
      <div class="suspendedText" style="display: none;">
        <ul>
          <li v-for="item in charDetailList" 
          :key="item.id" 
          v-show="item.ref !== 'custom'"
          class="item"
          >
            {{ item.name }}
          </li>
        </ul>  
      </div>
      <div class="bigWoodFish">
        <img src="@/assets/image/木鱼/大木鱼棍.png" alt="" class="bigStick" ref="stick">
        <img src="@/assets/image/木鱼/大木鱼.png" alt="" ref="stuff" @mousedown="strickWoodFish">
      </div>
    </div>
    <!-- 更多木鱼装扮 -->
    <div class="woodFishStyle" v-show="isDressUp">
      <div class="jiantou"></div>
      <div class="kuangjia">
        <ul class="dressUp">
          <li><img src="" alt=""></li>
        </ul>
      </div>
    </div>
    <!-- 悬浮文字详情 -->
    <div class="charDetail wrapper char" v-show="isCharDetail">
      <div class="jiantou"></div>
      <div class="fangkuang">
        <ul class="char">
          <li class="item" v-for="item in charDetailList" :key="item.id" @click="addChar" :ref="item.ref">{{ item.name }}</li>
          <!-- <li class="item"></li>
          <li class="item"></li>
          <li class="item"></li>
          <li class="item"></li> -->
        </ul>
      </div>
    </div>
    <!-- 自定义文字 -->
    <div class="customChar" v-show="isAddChar">
      <div class="hollowOut">
        <div class="top">
          <img src="@/assets/image/木鱼/悬浮文字.png" alt="">
          <p>悬浮文字</p>
        </div>
      </div>
      <!-- <div class="bottom">
        <div class="jiantou"></div>
        <div class="fangkuang">
          <ul>
            <li class="item" v-for="item in charDetailList" :key="item.id" @click="addChar" :ref="item.ref">{{ item.name }}</li>
          </ul>
        </div>
      </div> -->
      <div class="addChar">
        <div class="title">添加悬浮字体</div>
        <input type="text" ref="text" v-model="newAddChar">
        <div class="border"></div>
        <div class="bottom char">
          <div class="cancel" @click="cancelAdd">取消</div>
          <div class="confirm" @click="confirmAdd">确定</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
  name: 'woodFish',
  components: {
      NavBar
  },
  data () {
    return {
      navbarTitle: '电子木鱼',
      time: 0,
      // dressUpList: [
      //   { id: 0, woodFishUrl: require(''), stickUrl: require(''), ref: '' },
      //   { id: 1, woodFishUrl: require(''), stickUrl: require(''), ref: '' },
      //   { id: 2, woodFishUrl: require(''), stickUrl: require(''), ref: '' },
      //   { id: 3, woodFishUrl: require(''), stickUrl: require(''), ref: '' },
      // ],
      charDetailList: [
        { id: 0, name: '无', ref: 'no' },
        { id: 1, name: '功德+1', ref: 'merit' },
        { id: 2, name: '快乐+1', ref: 'happy' },
        { id: 3, name: '烦恼-1', ref: 'trouble' },
        { id: 4, name: '自定义', ref: 'custom' },
      ],
      // suspendedText: [],
      newAddChar: '',
      isDressUp: false,
      isCharDetail: false,
      isAddChar: false,
      stuffUrl: '',
      senceUrl: '',
    }
  },
  methods: {
    // 点击自定义出现添加状态
    addChar(event) {
      // 如果点击自定义，出现自定义组件
      if (event.target.innerText === '自定义') {
        // console.log('11')
        // 改变该元素样式
        // console.log(this.$refs.custom)
        this.$refs.custom[0].style.backgroundColor = '#cdc0ba'
        this.isAddChar = true
        // 文本框自动聚焦
        this.$nextTick(() => {
          this.$refs.text.focus()
        })
      }
    },
    // 自定义状态点击按钮后恢复原样式
    recover () {
      this.isAddChar = false
      this.$refs.custom[0].style.backgroundColor = '#fff'
      this.newAddChar = ''
    },
    // 取消添加
    cancelAdd () {
      this.recover()
    },
    // 确认添加
    confirmAdd () {
      // 如果文本框内内容不为空，则将文字添加到数组中 -- unshift
      if (this.newAddChar) {
        this.charDetailList.unshift({id: this.charDetailList.length, name: this.newAddChar, ref: 'customChar'})
        // console.log(this.charDetailList) 
      }
      this.recover()
    },
    // 敲击木鱼
    strickWoodFish () {
      const stuff = this.$refs.stuff
      this.$refs.stick.classList.add('strick')
      // 木鱼缩放
      stuff.style.transform = 'scale(0.9)'
      // 自动获取原样
      setTimeout(() => {
        this.$refs.stick.classList.remove('strick')
        stuff.style.transform = 'scale(1)'
      }, 200)
      // 敲击次数 +1
      this.time += 1
    },
  },
  mounted () {
    // 获取更多设置页面传来的数据
    // 场景
    const sceneObj = JSON.parse(localStorage.getItem('woodFishScene'))
    if (sceneObj.remark === 'none') return
    // 若仓库中没有数据 --- 即未到更多设置页面选择，则从本地存储中获取上一次选择的物品
    if (!this.$store.state.woodFish.scene.url) {
      this.sceneUrl = sceneObj.url
    }else {
      this.sceneUrl = this.$store.state.woodFish.scene.url
    }
    // console.log('当前场景', this.sceneUrl)
    // console.log(this.sceneUrl, this.$refs.scene)
    if (this.sceneUrl) {
      this.$refs.scene.style.background = `url(${this.sceneUrl}) no-repeat`
      this.$refs.scene.style.backgroundSize = 'cover'
      this.$refs.scene.style.borderRadius = '40px'
    }
    // 物品
    const stuffObj = JSON.parse(localStorage.getItem('woodFishStuff'))
    if (!this.$store.state.woodFish.stuff) {
      this.stuffUrl = stuffObj.url
      // console.log(this.stuffUrl)
      
    }else {
      // console.log(11)
      this.stuffUrl = this.$store.state.woodFish.stuff.url
    }
    if (this.stuffUrl) {
      this.$refs.stuff.src = this.stuffUrl
    }

    // 点击其他地方取消展示文字详情/木鱼装扮
    window.addEventListener('click', (e) => {
      // console.log(e.target)
      if (!e.target.parentNode.classList.contains('char')) this.isCharDetail = false
      if (!e.target.parentNode.classList.contains('dressUp')) this.isDressUp = false
    })
  },
}
</script>

<style lang="less" scoped>
#woodFish {
  width: 390px;
  height: 844px;
  background-color: #FDF4E4;
  font-weight: 600;
}
.wrapper {
  margin: 0 auto;
  width: 366px;
}
.top {
  margin-top: 12px;
  ul {
    display: flex;
    justify-content: space-between;
    li {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 110px;
      height: 50px;
      border-radius: 50px;
      background: #fff9ef;
      img {
        margin-right: 2px;
        width: 28px;
        height: 31px;
      }
      p {
        color: #000000cc;
        font-size: 14px;
      }
    }
    li:nth-child(2) img {
      width: 30px;
    }
    li:nth-child(3) img {
      width: 28px;
      height: 26px;
    }
  }
}
.body {
  position: relative;
  margin-top: 18px;
  width: 366px;
  height: 626px;
  border-radius: 20px;
  background: #fff9ef;
  .time {
    position: relative;
    margin-top: 35px;
    margin-left: 22px;
    width: 90px;
    height: 90px;
    border-radius: 10px;
    background: #ecddca;
    .title {
      margin-top: 10px;
      margin-left: 10px;
      color: #796c5c;
      font-size: 10px;
      span {
        margin: 0 2px;
        color: #997a46;
        font-size: 14px;
      }
    }
    .smallStick {
      position: absolute;
      left: 62px;
      bottom: 38px;
      width: 22px;
    }
    img:last-child {
      position: absolute;
      left: 19px;
      bottom: 8px;
      width: 52px;
    }
  }
  // 悬浮文字
  .suspendedText {
    margin-top: 10px;
    width: 100%;
    height: 270px;
    background-color: #fff;
  }
  .bigWoodFish {
    .bigStick {
      position: absolute;
      left: 230px;
      bottom: 185px;
      width: 62px;
      // transition: all 0.1s;
    }
    img:last-child {
      position: absolute;
      left: 75px;
      bottom: 75px;
      width: 188px;
    }
    .strick {
      transform: rotate(-70deg);
    }
  }
}
.woodFishStyle {
  position: absolute;
  top: 115px;
  left: 15px;
  .jiantou {
    margin-left: 60px;
    width: 0;
    height: 0;
    border-left: 30px solid #fff;
    border-top: 45px solid transparent;
    border-right: 80px solid transparent;
    transform: rotate(-20deg) scale(0.7);
  }
  .kuangjia {
    width: 220px;
    height: 80px;
    background: #fff;
    border-radius: 10px;
  }
}
.charDetail {
  position: absolute;
  top: 140px;
  left: 50%;
  width: 390px;
  height: 200px;
  transform: translateX(-50%);
  .jiantou {
    margin: 0 auto;
    width: 0;
    height: 0;
    border-left: 25px solid transparent;
    border-right: 25px solid transparent;
    border-bottom: 30px solid #fff;
  }
  .fangkuang {
    margin-left: 85px;
    padding: 10px;
    width: 220px;
    // height: 82px;
    border-radius: 10px;
    background: #fff;
    box-shadow: 2px 6px 8px #00000040;
    ul {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      li {
        width: 60px;
        height: 26px;
        border-radius: 10px;
        border: 1px solid #cdc0ba;
        color: #000000cc;
        font-size: 12px;
        line-height: 26px;
        text-align: center;
      }
    }
  }
}
.customChar {
  position: absolute;
  top: 0;
  width: 390px;
  height: 844px;
  background: #00000080;
  .hollowOut {
    .top {
      position: absolute;
      top: 74px;
      left: 140px;
      display: flex;
      justify-content: center;
      align-items: center;
      width: 110px;
      height: 50px;
      border-radius: 50px;
      background: #fff9ef;
      font-size: 14px;
      img {
        width: 30px;
      }
    }
    .bottom {
      margin-top: 100px;
    }
  }
  .addChar {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 341px;
    height: 177px;
    border-radius: 20px;
    background: #faf7f2;
    .title {
      margin-top: 16px;
      color: #000000cc;
      font-size: 20px;
      text-align: center;
    }
    input {
      margin-top: 26px;
      margin-left: 31px;
      width: 280px;
      height: 26px;
      border: none;
      background-color: #faf7f2;
    }
    .border {
      margin: 4px auto;
      width: 280px;
      height: 2px;
      background-color: #997A46;
    }
    .bottom {
      margin: 30px 28px 0 179px;
      display: flex;
      justify-content: space-between;
      color: #000000cc;
      font-size: 14px;
      text-align: center;
      .cancel,
      .confirm {
        width: 60px;
        height: 30px;
        border-radius: 336px;
        line-height: 30px;
      }
      .cancel {
        border: 1px solid #f9d590;
      }
      .confirm {
        background: #f9d590;
      }
    }
  }
}
</style>