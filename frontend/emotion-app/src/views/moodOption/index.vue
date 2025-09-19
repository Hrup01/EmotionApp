<template>
  <div id="moodOption">
    <div class="NaVBar">
      <div class="left"><img src="@/assets/image/jt_left.png" alt=""></div>
      <div class="right"><h3>选择心情</h3></div>
    </div>
    <div class="top wrapper">
      <!-- 上方的表情 -->
      <div class="pic"><img :src="currentEmojiUrl" alt="" class="optionMood"></div>
      <div class="mood">
        <!-- 上方表情对应的文字 -->
        <h3 v-if="flag">{{ currentEmojiName }}</h3>
        <input type="text" name="emojiName" v-else v-model="currentEmojiName" @keydown="confirmText" ref="editText">
        <img src="@/assets/image/pen.png" alt="" @click="changeText" v-show="flag">
      </div>
      <div class="text" @click="$router.push(`/log/${currentEmojiEnglish}`)">就这样</div>
      <div class="recentlyOption">
        <ul>
          <!-- 后面用 js 渲染 -->
          <li><img src="@/assets/image/11.png" alt="" @click="currentEmojiUrl = require('@/assets/image/11.png');currentEmojiName = '困'"></li>
          <li><img src="@/assets/image/8.png" alt="" @click="currentEmojiUrl = require('@/assets/image/8.png');currentEmojiName = '生气'"></li>
          <li><img src="@/assets/image/7.png" alt="" @click="currentEmojiUrl = require('@/assets/image/7.png');currentEmojiName = '自责'"></li>
          <li><img src="@/assets/image/11.png" alt="" @click="currentEmojiUrl = require('@/assets/image/11.png');currentEmojiName = '困'"></li>
        </ul>
        <p>最近</p>
      </div>
    </div>
    <div class="bottom">
      <ul @click="optionMood">
        <li v-for="(item,index) in moodList" :key="item.id">
          <img :src="item.url" alt="" :data-id="index">
          <p>{{ item.name }}</p>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
    name: 'moodOption',
    data () {
      return {
        flag: 'ture',
        currentEmojiUrl: require('@/assets/image/10.png'),
        currentEmojiName: '开心',
        currentEmojiEnglish: 'happy',
        moodList: [
          { id: 0, url: require('@/assets/image/1.png'), name: '邪恶', english: 'evil' },
          { id: 1, url: require('@/assets/image/2.png'), name: '满足', english: 'satisfy' },
          { id: 2, url: require('@/assets/image/3.png'), name: '叹气', english: 'sign' },
          { id: 3, url: require('@/assets/image/4.png'), name: '伤心', english: 'sad' },
          { id: 4, url: require('@/assets/image/5.png'), name: '无奈', english: 'helpless' },
          { id: 5, url: require('@/assets/image/6.png'), name: '疑问', english: 'query' },
          { id: 6, url: require('@/assets/image/7.png'), name: '自责', english: 'selfBlame' },
          { id: 7, url: require('@/assets/image/8.png'), name: '生气', english: 'angry' },
          { id: 8, url: require('@/assets/image/9.png'), name: '期待', english: 'expect' },
          { id: 9, url: require('@/assets/image/10.png'), name: '开心', english: 'happy' },
          { id: 10, url: require('@/assets/image/11.png'), name: '困', english: 'tired' },
          { id: 11, url: require('@/assets/image/12.png'), name: '晕', english: 'faint' }
        ],
        lastOption: []
      }
    },
    methods: {
      optionMood (e) {
        // console.log(e.target.dataset.id)
        this.currentEmojiUrl = this.moodList[e.target.dataset.id].url
        this.currentEmojiName = this.moodList[e.target.dataset.id].name
        this.currentEmojiEnglish = this.moodList[e.target.dataset.id].english
      },
      changeText () {
        this.flag = false
        this.$nextTick(() => {
          this.$refs.editText.focus()
          this.currentEmojiName = ''
        })
      },
      confirmText (e) {
        // console.log(e.key)
        if (e.key === 'Enter') {
          // console.log(11)
          this.flag = true 
          // 修改对应对象中的 name 属性 --- arr.findIndex() --- 返回下标索引
          const index = this.moodList.findIndex(item => item.url === this.currentEmojiUrl)
          this.moodList[index].name = this.currentEmojiName
          // console.log(this.moodList)
        }
      }
    },
}
</script>

<style lang="less" scoped>
#moodOption {
  padding-top: 60px;
  height: 844px;
  background-color: #FEF8EC;
}
.wrapper {
  margin: 0 auto;
  width: 366px;
}
.NaVBar {
  margin-left: 12px;
  display: flex;
  .left {
    margin-right: 14px;
    height: 24px;
    width: 24px;
    img {
      margin-left: 9px;
      margin-top: 1px;
      width: 24px;
    }
  }
  h3 {
    font-size: 18px;
  }
}
.top {
  height: 320px;
  .pic {
    height: 145px;
  }
  .optionMood {
    margin-top: 9px;
    margin-left: 114px;
    width: 139px;
  }
  .mood {
    display: flex;
    // margin-left: 154px;
    justify-content: center;
    text-align: center;
    font-size: 20px;
    img {
      margin-top: 8px;
      margin-left: 3px;
      width: 18px;
      height: 17px;
    }
    [name = emojiName] {
      border: 0;
      background-color: #FEF8EC;
      font-size: 24px;
      font-weight: 600;
      text-align: center;
      overflow: hidden;
    }
  }
  .text {
    margin-left: 138px;
    margin-top: 16px;
    width: 90px;
    height: 30px;
    border-radius: 10px;
    background: #fec076;
    text-align: center;
    line-height: 30px;
    font-size: 16px;
    font-weight: 700;
    cursor: pointer;
  }
  .recentlyOption {
    ul {
      margin-top: 14px;
      margin-left: 100px;
      padding: 5px 10px;
      display: flex;
      justify-content: space-between;
      width: 170px;
      height: 40px;
      border-radius: 30px;
      background: #ffe9c8;
      img {
        width: 33px;
        height: 33px;
      }
    }
    p {
      margin-top: 16px;
      margin-left: 171px;
      width: 24px;
      height: 17px;
      color: #797979;
      font-size: 12px;
      font-weight: 700;
    }
  }
}
.bottom {
  ul {
    margin-top: 16px;
    padding: 0 10px;
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    // gap: ;
    li {
      height: 120px;
      width: 80px;
      img {
        width: 80px;
      }
      p {
        color: #000000;
        font-size: 16px;
        font-weight: 700;
        text-align: center;
      }
    }
  }
}
</style>