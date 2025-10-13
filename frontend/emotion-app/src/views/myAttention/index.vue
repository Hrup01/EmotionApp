<template>
  <div id="myAttention">
    <nav-bar :title="title"></nav-bar>
    <div class="allFollow">
        <p>全部关注</p>
        <div class="count">({{ followCount }}人)</div>
    </div>
    <messageList :height="componentHeight" :list="attentionList" :isMyAttention="isMyAttention" @changeFollow="changeFollow"></messageList>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import messageList from '@/components/messageList.vue';
export default {
    name: 'myAttention',
    components: { 
        NavBar,
        messageList
    },
    data () {
        return {
            title: '我的关注',
            componentHeight: '729px',
            isMyAttention: true,
            attentionList: [
                { id: 0, url: require('@/assets/image/消息中心/头像1.png'), name: '小栈', followed: false },
                { id: 1, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
                { id: 2, url: require('@/assets/image/消息中心/头像3.png'), name: 'CaCa', followed: true },
                { id: 3, url: require('@/assets/image/消息中心/头像4.png'), name: 'CaCa', followed: true },
                { id: 4, url: require('@/assets/image/消息中心/头像5.png'), name: 'CaCa', followed: false },
                { id: 5, url: require('@/assets/image/消息中心/头像6.png'), name: 'CaCa', followed: false },
                { id: 6, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
                { id: 7, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
                { id: 8, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: true },
                { id: 9, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
                { id: 10, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
                { id: 11, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
                { id: 12, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
                { id: 13, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', followed: false },
            ],
            followCount: 6
        }
    },
    methods: {
        changeFollow (newState) {
            // console.log(newState)
            const { id, followed } = newState
            // console.log(id, followed, this.attentionList[id])
            this.attentionList[id].followed = followed
        }
    }
}
</script>

<style lang="less" scoped>
#myAttention {
    width: 390px;
    height: 844px;
    background: #fdf4e4;
}
.allFollow {
    margin-top: 11px;
    margin-left: 40px;
    display: flex;
    font-size: 14px;
    font-weight: 600;
    p {
        margin-right: 8px;
        color: #000000cc;
    }
    .count {
        color: #00000080;
    }
}
.messageList {
    margin-top: 4px;
}
</style>