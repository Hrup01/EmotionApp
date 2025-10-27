<template>
  <div id="messageCenter">
    <nav-bar :title="title"></nav-bar>
    <div class="header wrapper">
        <div class="attention item">
            <div class="pic" @click="$router.push('/newFollow')"><img src="@/assets/image/消息中心/关注.png" alt=""></div>
            <p>新增关注</p>
        </div>
        <div class="praise item">
            <div class="pic" @click="$router.push('/likeReceived')"><img src="@/assets/image/消息中心/赞.png" alt=""></div>
            <p>收到的赞</p>
        </div>
        <div class="comment item">
            <div class="pic" @click="$router.push('/comment')"><img src="@/assets/image/消息中心/评论.png" alt=""></div>
            <p>评论</p>
        </div>
    </div>
    <message-list :height="componentHeight" :list="messageList"></message-list>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import MessageList from '../../components/messageList.vue'
export default {
    name: 'messageCenter',
    components: { 
        NavBar,
        MessageList 
    },
    data () {
        return {
            title: '消息中心',
            messageList: [
                { id: 0, url: require('@/assets/image/消息中心/头像1.png'), name: '小栈', content: '你好！欢迎来到情绪小栈' },
                { id: 1, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '你好！' },
                { id: 2, url: require('@/assets/image/消息中心/头像3.png'), name: 'CaCa', content: '你好！' },
                { id: 3, url: require('@/assets/image/消息中心/头像4.png'), name: 'CaCa', content: '你好！' },
                { id: 4, url: require('@/assets/image/消息中心/头像5.png'), name: 'CaCa', content: '你好！' },
                { id: 5, url: require('@/assets/image/消息中心/头像6.png'), name: 'CaCa', content: '你好！' },
                { id: 6, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '你好！' },
                { id: 7, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '你好！' },
                { id: 8, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '你好！' },
                { id: 9, url: require('@/assets/image/消息中心/头像2.png'), name: 'CaCa', content: '你好！' },
            ],
            componentHeight: '623px'
        }
    }
}
</script>

<style lang="less" scoped>
#messageCenter {
    width: 390px;
    height: 844px;
    background: #fdf4e4;
    font-weight: 600;
}
.wrapper {
    margin: 0 auto;
    width: 345px;
}
.header {
    margin: 21px auto;
    padding: 13px 42px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 100px;
    border-radius: 20px;
    background: #ffffff;
    .pic {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 50px;
        height: 50px;
        border-radius: 20px;
        img {
            width: 28px;
            height: 28px;
        }
    }
    .item {
        width: 56px;
        p {
            margin-top: 6px;
            color: #000000b3;
            font-size: 14px;
            text-align: center;
        }
    }
    .attention {
        .pic {
            background: linear-gradient(142.8deg, #d1ea9e 30%, #00c42b03 100%);
            box-shadow: 2px 2px 4px 0 #12870040;
        }
    }
    .praise {
        .pic {
            background: linear-gradient(304.4deg, #fccccc 35%, #fa002a03 100%);
            box-shadow: 2px 2px 4px 0 #85040440;
        }
    }
    .comment {
        .pic {
            margin-left: 2px;
            background: linear-gradient(142.7deg, #93edec 0%, #05a7f703 100%);
            box-shadow: 2px 2px 4px 0 #bce0e0;
        }
    }
}
</style>