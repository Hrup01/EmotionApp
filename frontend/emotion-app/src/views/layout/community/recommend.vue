<template>
  <div class="recommend wrapper">
    <contentItem 
      v-for="item in contentList"
      :key="item.id"
      :contentList="contentList" 
      :likeCount="likeCount"
      :commentCount="commentCount"
      :title="title">
    </contentItem>
  </div>
</template>

<script>
import contentItem from '@/components/contentItem.vue';
export default {
  name: 'recommendPage',
  components: {
    contentItem
  },
  data () {
    return {
      contentList: [
        { id: 0, headPortraitUrl: require('@/assets/image/头像1.png'), username: '小小栈' },
        { id: 1, headPortraitUrl: require('@/assets/image/头像1.png'), username: '小小栈' },
        { id: 2, headPortraitUrl: require('@/assets/image/头像1.png'), username: '小小栈' },
        { id: 3, headPortraitUrl: require('@/assets/image/头像1.png'), username: '小小栈' },
        { id: 4, headPortraitUrl: require('@/assets/image/头像1.png'), username: '小小栈' },
        { id: 5, headPortraitUrl: require('@/assets/image/头像1.png'), username: '小小栈' },
      ],
      likeCount: 123,
      commentCount: 123,
      title: '云朵是天空温柔的枕头'
    }
  }
}
</script>

<style lang="less" scoped>
.recommend {
  margin-bottom: 94px;
  height: 682px;
  overflow: auto;
}
.wrapper {
  margin: 0 auto;
  width: 366px;
}
.bottom {
  width: 100%;
  height: 94px;
  background-color: pink;
}
</style>