<template>
  <div id="myComment">
    <nav-bar :title="title"></nav-bar>
    <messageList :height="componentHeight" :list="myCommentList" :is-comment="isComment"></messageList>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import messageList from '@/components/messageList.vue';
export default {
    name: 'myComment',
    components: { 
        NavBar,
        messageList
    },
    data () {
        return {
            title: '评论',
            componentHeight: '745px',
            isComment: true,
            myCommentList: [
                { id: 0, url: require('@/assets/image/消息中心/头像1.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: '小栈', content: '好看！' },
                { id: 1, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 2, url: require('@/assets/image/消息中心/头像3.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 3, url: require('@/assets/image/消息中心/头像4.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 4, url: require('@/assets/image/消息中心/头像5.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 5, url: require('@/assets/image/消息中心/头像6.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 6, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 7, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 8, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 9, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 10, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 11, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 12, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
                { id: 13, url: require('@/assets/image/消息中心/头像2.png'), notebgUrl: require('@/assets/image/我的关注_新增关注_评论_点赞/示例图片.png'), name: 'CaCa', content: '好看！' },
            ]
        }
    }
}
</script>

<style lang="less" scoped>
#myComment {
    width: 390px;
    height: 844px;
    background: #fdf4e4;
}
</style>