<template>
  <div id="user">
    <div class="header">
      <img src="@/assets/image/时间.png" alt="" class="top-time" ref="topTime">
      <div class="top">
        <img src="@/assets/image/我的/编辑2.png" alt="" class="editor" @click="$router.push('/user/editInformation')">
        <img src="@/assets/image/我的/设置.png" alt="" class="set">
      </div>
      <div class="userMessage">
        <img src="@/assets/image/我的/头像.png" alt="">
        <p class="username">CaCa</p>
        <div class="bottom">
          <div class="attention">
            <div class="count">{{ attentionCount }}</div>
            <p>关注</p>
          </div>
          <div class="fans">
            <div class="count">{{ fansCount }}</div>
            <p>粉丝</p>
          </div>
        </div>
      </div>
      <div class="function">
        <ul ref="function">
          <li class="item" v-for="item in functionList" :key="item.id" :ref="item.ref" @click="$router.push(item.toUrl)">
            <img :src="item.bgUrl" alt="" class="bg">
            <div class="content">
              <img :src="item.picUrl" alt="" class="pic">
              <p>{{ item.name }}</p>
            </div>
          </li>
        </ul>
        <!-- <scrollContainer>
          <li class="item" v-for="item in functionList" :key="item.id" :ref="item.ref" @click="$router.push(item.toUrl)">
            <img :src="item.bgUrl" alt="" class="bg">
            <div class="content">
              <img :src="item.picUrl" alt="" class="pic">
              <p>{{ item.name }}</p>
            </div>
          </li>
        </scrollContainer> -->
      </div>
    </div>
    <div class="footer">
      <sencond-nav-bar :navbar-list="navbarList" :p-style="pStyle" :pic-style="picStyle"></sencond-nav-bar>
      <!-- 二级导航出口 -->
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import sencondNavBar from '@/components/sencondNavBar.vue'
// import scrollContainer from '@/components/scrollContainer.vue'
export default {
    name: 'userPage',
    components: {
      sencondNavBar,
      // scrollContainer
    },
    data () {
      return {
        attentionCount: 100,
        fansCount: 100,
        functionList: [
          { id: 0, bgUrl: require('@/assets/image/我的/状态背景.png'), picUrl: require('@/assets/image/我的/状态.png'), name: '状态选择', ref: 'state', toUrl: '/state' },
          { id: 1, bgUrl: require('@/assets/image/我的/日记背景.png'), picUrl: require('@/assets/image/我的/日记.png'), name: '日记记录', ref: 'dairy', toUrl: '/moodOption' },
          { id: 2, bgUrl: require('@/assets/image/我的/手账背景.png'), picUrl: require('@/assets/image/我的/手账.png'), name: '手账记录', ref: 'scrapbook', toUrl: '/bulletJournalRecord' },
          { id: 3, bgUrl: require('@/assets/image/我的/打卡背景.png'), picUrl: require('@/assets/image/我的/打卡.png'), name: '打卡奖励', ref: 'clockin', toUrl: '/clockIn' },
          { id: 4, bgUrl: require('@/assets/image/我的/商店背景.png'), picUrl: require('@/assets/image/我的/商店.png'), name: '商店', ref: 'shop', toUrl: '/state' }
        ],
        navbarList: { firstName: '笔记', sencondName: '收藏', bottomPicUrl: require('@/assets/image/我的/笔记_选中.png'), firstToUrl: '/user/note', secondToUrl: '/user/collect' },
        pStyle: { color: '#300000', fontSize: '14px' },
        picStyle: { width: '25px', height: '3px', marginTop: '4px' }
      }
    },
}
</script>

<style lang="less" scoped>
.header {
  width: 390px;
  height: 360px;
  background-image: url('@/assets/image/我的/背景图.png');
  background-size: cover;
  font-weight: 600;
  // background-color: pink;
  .top-time {
    width: 390px;
    background-color: transparent;
  }
  .top {
    // margin-top: 66px;
    img {
      position: absolute;
      top: 50px;
      width: 20px;
      height: 20px;
    }
    .editor {
      right: 56px;
    }
    .set {
      right: 14px;
    }
  }
  .userMessage {
    margin-top: 60px;
    display: flex;
    flex-direction: column;
    align-items: center;
    img {
      width: 80px;
      height: 80px;
    }
    .username {
      margin-top: 4px;
      color: #000000cc;
      font-size: 18px;
    }
    .bottom {
      display: flex;
      .attention,
      .fans {
        margin: 5px 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .count {
          color: #05005e;
          font-size: 20px;
        }
        p {
          color: #000000cc;
          font-size: 14px;
        }
      }
    }  
  }
  .function {
    margin-top: 20px;
    ul {
      margin-left: 12px;
      display: flex;
      // width: 378px;
      overflow-x: auto;
      .item {
        position: relative;
        margin-right: 8px;
        flex-shrink: 0;
        width: 130px;
        height: 60px;
        border-radius: 10px;
        // background-color: pink;
        .bg {
          position: absolute;
          top: 0;
          width: 130px;
          height: 60px;
          z-index: 1;
        }
        .content {
          position: relative;
          display: flex;
          align-items: center;
          height: 100%;
          z-index: 2;
          .pic {
            margin-left: 14px;
            width: 28px;
            height: 28px;
          }
          p {
            margin-left: 20px;
            color: #380000;
            font-size: 14px;
          }
        }
      }
    }
  }
}
.footer {
  width: 390px;
  height: 388px;
  background: #fdf4e4;
  .navbar {
    margin-top: 15px;
    height: 35px;
  }
}
</style>