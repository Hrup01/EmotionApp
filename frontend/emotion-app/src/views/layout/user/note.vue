<template>
  <div id="note">
    <ul class="wrapper">
      <li v-for="item in noteList" :key="item.id">
          <noteItem
          :id="item.id"
          :noteList="noteList"
          ></noteItem>
      </li>
    </ul>
  </div>
</template>

<script>
import noteItem from '@/components/noteItem.vue'
export default {
    name: 'notePage',
    components: {
      noteItem
    },
    data () {
      return {
        noteList: [
            { id: 0, pic: require('@/assets/image/笔记背景1.png'), text: '这里是第一段文字', likeCount: 123, commentCount: 123 },
            { id: 1, pic: require('@/assets/image/笔记背景2.png'), text: '这里是第二段文字', likeCount: 123, commentCount: 123 },
            { id: 2, pic: require('@/assets/image/笔记背景3.png'), text: '这里是第三段文字', likeCount: 123, commentCount: 123 },
            { id: 3, pic: require('@/assets/image/笔记背景4.png'), text: '这里是第四段文字', likeCount: 123, commentCount: 123 }
        ]
      }
    }
}
</script>

<style lang="less" scoped>
.wrapper {
  margin: 0 auto;
  width: 366px;
}
ul {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  height: 340px;
  overflow-y: auto;
}
</style>