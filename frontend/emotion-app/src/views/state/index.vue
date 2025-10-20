<template>
  <div id="myState">
    <div class="header wrapper">
        <img src="@/assets/image/状态/关闭.png" alt="" class="cancel" @click="$router.back()">
        <div class="middle">
            <div class="title">我的状态</div>
            <p>设置后状态24小时有效</p>
        </div>
    </div>
    <div class="mood wrapper">
        <p>心情</p>
        <stateItem :list="moodList"></stateItem>
    </div>
    <div class="doing wrapper">
        <p>我正在</p>
        <stateItem :list="doingList"></stateItem>
    </div>
  </div>
</template>

<script>
import stateItem from '@/components/stateItem.vue'
export default {
    name: 'myState',
    components: {
        stateItem
    },
    data () {
        return {
            moodList: [
                { id: 0, picUrl: require('@/assets/image/状态/美滋滋.png'), text: '美滋滋', ref: 'happy' },
                { id: 1, picUrl: require('@/assets/image/状态/元气满满.png'), text: '元气满满', ref: 'fullvitality' },
                { id: 2, picUrl: require('@/assets/image/状态/委屈.png'), text: '委屈', ref: 'grievance' },
                { id: 3, picUrl: require('@/assets/image/状态/惊吓.png'), text: '惊吓', ref: 'scare' },
                { id: 4, picUrl: require('@/assets/image/状态/感动.png'), text: '感动', ref: 'affect' },
                { id: 5, picUrl: require('@/assets/image/状态/难过.png'), text: '难过', ref: 'sad' },
                { id: 6, picUrl: require('@/assets/image/状态/生气.png'), text: '生气', ref: 'angry' },
                { id: 7, picUrl: require('@/assets/image/状态/无语.png'), text: '无语', ref: 'speechless' },
            ],
            doingList: [
                { id: 0, picUrl: require('@/assets/image/状态/打游戏.png'), text: '打游戏', ref: 'play' },
                { id: 1, picUrl: require('@/assets/image/状态/追剧.png'), text: '追剧', ref: 'binge-atch' },
                { id: 2, picUrl: require('@/assets/image/状态/冥想.png'), text: '冥想', ref: 'meditation' },
                { id: 3, picUrl: require('@/assets/image/状态/喝奶茶.png'), text: '喝奶茶', ref: 'drink-milk-tea' },
                { id: 4, picUrl: require('@/assets/image/状态/睡觉.png'), text: '睡觉', ref: 'sleep' },
                { id: 5, picUrl: require('@/assets/image/状态/逛街.png'), text: '逛街', ref: 'shopping' },
                { id: 6, picUrl: require('@/assets/image/状态/生病.png'), text: '生病', ref: 'fall-ill' },
                { id: 7, picUrl: require('@/assets/image/状态/带娃.png'), text: '带娃', ref: 'taking-care-of-childrem' },
                { id: 8, picUrl: require('@/assets/image/状态/运动.png'), text: '运动', ref: 'exercise' },
                { id: 9, picUrl: require('@/assets/image/状态/听歌.png'), text: '听歌', ref: 'listen-to-music' },
                { id: 10, picUrl: require('@/assets/image/状态/冲冲冲.png'), text: '冲冲冲', ref: 'rush' },
                { id: 11, picUrl: require('@/assets/image/状态/求锦鲤.png'), text: '求锦鲤', ref: 'seeking-koi' },
            ]
        }
    },
    methods: {
    }
}
</script>

<style lang="less" scoped>
#myState {
    width: 390px;
    height: 852px;
    background: #faf4e6;
    font-weight: 600;
}
.wrapper {
    margin: 0 auto;
    width: 364px;
}
.header {
    margin-top: 40px;
    display: flex;
    // align-items: center;
    height: 54px;
    img {
        margin-right: 97px;
        width: 24px;
        height: 24px;
    }
    .middle {
        display: flex;
        flex-direction: column;
        align-items: center;
        .title {
            color: #000000;
            font-size: 18px;
        }
        p {
            margin-top: 2px;
            color: #00000080;
            font-size: 12px;
        }
    }
}
.mood {
    margin-top: 10px;
    color: #000000cc;
}
.doing {
    margin-top: 30px;
    color: #000000cc;
}
</style>