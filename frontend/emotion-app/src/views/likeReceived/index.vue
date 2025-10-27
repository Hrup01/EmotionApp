<template>
  <div id="likeReceived">
    <nav-bar :title="title"></nav-bar>
    <messageList :height="componentHeight" :list="likeReceivedList"></messageList>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import messageList from '@/components/messageList.vue';
export default {
    name: 'likeReceived',
    components: { 
        NavBar,
        messageList
    },
    data () {
        return {
            title: '收到的赞',
            componentHeight: '745px',
            likeReceivedList: [
                { id: 0, url: require('@/assets/image/消息中心/头像1.png'), name: '小栈', content: '赞了我的笔记' },
                { id: 1, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 2, url: require('@/assets/image/消息中心/头像3.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 3, url: require('@/assets/image/消息中心/头像4.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 4, url: require('@/assets/image/消息中心/头像5.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 5, url: require('@/assets/image/消息中心/头像6.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 6, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 7, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 8, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '赞了我的笔记' },
                { id: 9, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '赞了我的笔记' },
            ]
        }
    }
}
</script>

<style lang="less" scoped>
#likeReceived {
    width: 390px;
    height: 844px;
    background: #fdf4e4;
}
</style>