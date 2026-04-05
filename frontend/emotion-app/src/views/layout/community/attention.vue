<template>
  <div class="attention wrapper">
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
  name: 'attentionPage',
  components: {
    contentItem
  },
  data () {
    return {
      contentList: [
        { id: 0, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '222', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 1, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '111', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 2, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 3, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 4, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
        { id: 5, headPortraitUrl: require('@/assets/image/头像1.png'), bgUrl: require('@/assets/image/背景1.png'), authorName: '小小栈', likeCount: 123, commentCount: 123, title: '云朵是天空温柔的枕头' },
      ],
    }
  },
  // async mounted () {
  //   const token = JSON.parse(localStorage.getItem('emotion_app_info')).token
  //   // console.log(token)
  //   // 获取关注列表
  //   const res = await axios.get('http://localhost:8080/api/community/feed', {
  //     params: {
  //       type: 'following',
  //       page: 0,
  //       size: 10,
  //     },
  //     headers: {
  //         Authorization: 'Bearer ' + token
  //     }
  //   })
  //   console.log(res)
  //   // this.contentList = res.data.data
  // }
}
</script>

<style lang="less" scoped>
.attention {
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