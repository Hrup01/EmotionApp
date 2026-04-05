<template>
  <div class="recommend wrapper" ref="scrollContainer" @scroll="scrollHeight">
    <contentItem 
      v-for="item in contentList"
      :key="item.id"
      :id="item.id"
      :contentList="contentList" 
    >
    </contentItem>
  </div>
</template>

<script>
import contentItem from '@/components/contentItem.vue'
// import axios from 'axios'
export default {
  name: 'recommendPage',
  components: {
    contentItem
  },
  data () {
    return {
      scrollTop: 0, // 记录滚动位置
      contentList: [
        { id: 0, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '222', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 1, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '111', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 2, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈3', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 3, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈4', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 4, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈5', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 5, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈6', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
      ],
    }
  },
  methods: {
    // 直接存储滚动时实时记录的值
    scrollHeight () {
      // 可以根据需要节流/防抖，这里简单记录
      const current = this.getCurrentScrollTop()
      this.scrollTop = current
      // console.log(current)
    },
    // 获取滚动位置
    getCurrentScrollTop () {
      const container = this.$refs.scrollContainer
      // 判断是否是容器滚动
      const isContainerScrollable = container && container.scrollHeight > container.clientHeight //scrollHeight是元素内容的总高度，包括溢出无法显示的部分 clientHeight是容器可见部分的高
      if (isContainerScrollable) {
        // console.log('是容器在滚动') //是容器在滚动
        return container.scrollTop || 0
      }
      // 窗口滚动 --> 兼容不同浏览器的滚动位置获取方法
      return window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0
    },
    // 设置滚动位置 --> 在组件激活时调用(在restoreScroll中用)
    setScrollTop (value) {
      const target = Number(value) || 0
      const container = this.$refs.scrollContainer
      const isContainerScrollable = container && container.scrollHeight > container.clientHeight
      if (isContainerScrollable) {
        container.scrollTop = target // 容器内滚动，直接设置
        // console.log(1)
      } else {
        window.scrollTo(0, target) // 窗口滚动，调用 window.scrollTo(x,y) 方法 --> 滚动到指定坐标
        // console.log(22)
      }
    },
    // 恢复滚动位置 --> 在组件激活时调用
    restoreScroll (value, attempts = 6) { //attempts = 6 --> 重复6次执行
      // 多次尝试恢复，避免图片/布局异步导致失效
      const target = Number(value) || 0 // target是存在会话存储中离开组件时记录的滚动位置
      const apply = () => {
        this.setScrollTop(target)
      }
      // 立即一次
      apply()
      // 追加多次
      for (let i = 1; i < attempts; i++) {
        setTimeout(apply, i * 80)
      }
    }
  },
  // async mounted () {
  //   const token = JSON.parse(localStorage.getItem('emotion_app_info')).token
  //   // console.log(token)
  //   // 获取推荐列表
  //   const res = await axios.get('http://localhost:8080/api/community/feed', {
  //     params: {
  //       type: 'recommend',
  //       page: 0,
  //       size: 10,
  //     },
  //     headers: {
  //         Authorization: 'Bearer ' + token
  //     }
  //   })
  //   console.log('获取推荐列表',res)
  //   // this.contentList = res.data.data
  //   // console.log('打印图片地址',this.contentList[0].imageUrls[0])
  // },
  activated () {
    // 恢复滚动位置（优先从会话存储取）
    this.$nextTick(() => {
      const raw = sessionStorage.getItem('community_recommend_scrollTop')
      const saved = Number(raw) || this.scrollTop || 0 // 优先级：会话存储 --> data中的数据 --> 0
      this.scrollTop = saved
      this.restoreScroll(saved)
    })
  },
  deactivated () {
    // 缓存时记录当前滚动位置
    // 直接使用滚动过程中实时记录的值，避免离开瞬间读取被置为 0
    const current = Number(this.scrollTop) || 0
    sessionStorage.setItem('community_recommend_scrollTop', String(current))
  },
  // beforeRouteLeave是组件级路由导航守卫
  beforeRouteLeave (to, from, next) {
    // 无论是否被 keep-alive 缓存，离开路由时都保存一次
    const current = Number(this.scrollTop) || 0
    sessionStorage.setItem('community_recommend_scrollTop', String(current))
    next()
  }
}
</script>

<style lang="less" scoped>
.recommend {
  margin-bottom: 94px;
  height: 648px;
  overflow: auto;
}
.wrapper {
  margin: 0 auto;
  width: 366px;
}
.bottom {
  width: 100%;
  height: 94px;
  // background-color: pink;
}
</style>