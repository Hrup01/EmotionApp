<template>
  <div id="myDraft">
    <NavBar :title="title"></NavBar>
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
import NavBar from '@/components/NavBar.vue'
import noteItem from '@/components/noteItem.vue'
export default {
    name: 'myDraft',
    components: {
      NavBar,
      noteItem
    },
    data () {
      return {
        title: '我的草稿',
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
#myDraft {
  width: 390px;
  height: 852px;
  background: #faf4e6;
}
.wrapper {
  margin: 0 auto;
  width: 366px;
}
ul {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  // height: 844px;
  overflow-y: auto;
}
</style>