<template>
  <div id="heal">
    <div class="bg">
      <div class="components wrapper">
        <ul>
          <li v-for="(item, index) in healList" :key="item.id"><heal-item :id="index" :healList="healList"></heal-item></li>
        </ul>
      </div>
      <div class="schedule wrapper">
        <img src="@/assets/image/疗愈空间/进度.png" alt="">
        <div class="detailSchedule">
          <h4>今日疗愈进度</h4>
          <p>已完成30分钟</p>
        </div>
        <div class="continue">继续</div>
      </div>
    </div>
  </div>
</template>

<script>
import healItem from '@/components/healItem.vue';
export default {
    name: 'healSpace',
    components: {
      healItem
    },
    data () {
      return {
        healList: [
          { id: 0, bgUrl: require('@/assets/image/疗愈空间/白噪音背景.png'), url: require('@/assets/image/疗愈空间/白噪音.png'), title: '白噪音', introduce: '聆听大自然的声音', toUrl: '/whiteNoise' },
          { id: 1, bgUrl: require('@/assets/image/疗愈空间/冥想背景.png'), url: require('@/assets/image/疗愈空间/冥想.png'), title: '冥想', introduce: '是时候给身心放一次假了', toUrl: '/whiteNoise' },
          { id: 2, bgUrl: require('@/assets/image/疗愈空间/情绪拼图背景.png'), url: require('@/assets/image/疗愈空间/情绪拼图.png'), title: '情绪拼图', introduce: '把杂乱的情绪慢慢变完整', toUrl: '/whiteNoise' },
          { id: 3, bgUrl: require('@/assets/image/疗愈空间/敲木鱼背景.png'), url: require('@/assets/image/疗愈空间/敲木鱼.png'), title: '敲木鱼', introduce: '木鱼敲击，烦恼-1', toUrl: '/whiteNoise' },
        ]
      }
    }
}
</script>

<style lang="less" scoped>
#heal {
  width: 390px;
  height: 844px;
  background: #ffefcf;
}
.wrapper {
  margin: 0 auto;
  width: 330px;
}
.bg {
  margin-top: 64px;
  margin-left: 12px;
  width: 367px;
  height: 645px;
  border-radius: 20px;
  background: #ffffff99;
}
.components {
  margin-top: 60px;
  ul {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    li {
      margin-bottom: 36px;
    }
  }
}
.schedule {
  margin-top: 44px;
  // padding-top: 25px;
  display: flex;
  align-items: center;
  height: 100px;
  border-radius: 20px;
  background-image: url('@/assets/image/疗愈空间/疗愈进度背景.png');
  background-size: cover;
  img {
    margin-left: 17px;
    width: 50px;
    height: 50px;
  }
  .detailSchedule {
    margin-left: 5px;
    width: 170px;
    h4 {
      color: #261700cc;
      font-size: 16px;
    }
    p {
      color: #00000066;
      font-size: 12px;
      font-weight: 600;
    }
  }
  .continue {
    width: 80px;
    height: 40px;
    border-radius: 20px;
    background: #ffc894;
    box-shadow: 0 2px 4px 0 #ab814f80;
    color: #261700cc;
    font-size: 16px;
    font-weight: 600;
    line-height: 40px;
    text-align: center;
  }
}
</style>