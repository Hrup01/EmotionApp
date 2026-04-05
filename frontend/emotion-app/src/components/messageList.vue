<template>
    <div class="messageList wrapper">
        <ul ref="messageList">
            <li v-for="item in list" :key="item.id">
                <img :src="item.url" alt="" class="headPic" @click="$router.push(`/otherHomePage?name=${item.name}&followed=true&url=${item.url}`)">
                <div class="text">
                    <div class="name">{{ item.name }} <p v-show="isComment">对我的笔记发表了评论</p></div>
                    <p>{{ item.content }}</p>
                </div>
                <!-- 评论页面出现笔记背景 -->
                <img :src="item.notebgUrl" alt="" class="notebg"  v-show="isComment">
                <!-- 关注页面 --- 已关注和回关 -->
                <div class="attention" v-show="isAttention" @click="changeFollowState(item.id)">
                    <div class="followed" v-if="item.followed">已关注</div>
                    <div class="toAttention" v-else>回关</div>
                </div>
                <!-- 我的关注 -->
                <div class="myAttention" v-show="isMyAttention" @click="changeFollowState(item.id)">
                    <div class="followed" v-if="item.followed">互相关注</div>
                    <div class="toAttention" v-else>已关注</div>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>

export default {
    props: ['list', 'id', 'height', 'isComment', 'isAttention', 'isMyAttention'],
    data () {
        return {
            // followed: false
        }
    },
    methods: {
        changeFollowState (id) {
            // console.log(this.list[id].followed)
            if (this.list[id].followed) {
                this.$emit('changeFollow', { id, followed: false })
            }else {
                this.$emit('changeFollow', { id, followed: true })
            }
        },
        // // 跳转到他人主页 -- 传递数组
        // toOtherHomePage () {
        //     this.$router.push('/otherHomePage?')
        // }
    },
    mounted () {
        // 根据传来的数据设置高度
        // console.log(this.height)
        this.$refs.messageList.style.height = this.height
    }
}
</script>

<style lang="less" scoped>
.wrapper {
    margin: auto;
    width: 345px;
}
.messageList {
    margin-top: 20px;
    width: 345px;
    border-radius: 20px;
    background: #ffffff;
    font-weight: 600;
    ul {
        padding-top: 8px;
        height: 623px;
        overflow-y: auto;
        li {
            margin-top: 8px;
            padding: 0 25px;
            display: flex;
            align-items: center;
            height: 64px;
            .headPic {
                margin-right: 14px;
                width: 46px;
                height: 46px;
            }
            .notebg {
                justify-items: end;
                width: 40px;
                height: 40px;
            }
            .text {
                width: 200px;
            }
            .name {
                display: flex;
                align-items: center;
                color: #363636;
                font-size: 14px;
                p {
                    margin-left: 6px;
                    color: #363636cc;
                    font-size: 10px;
                    font-weight: 600;
                }
            }
            p {
                color: #363636cc;
                font-size: 12px;
                font-weight: normal;
            }
            .attention,
            .myAttention {
                width: 76px;
                height: 28px;
                border-radius: 28px;
                border: 1px solid #fce5a6;
                color: #362200cc;
                font-size: 14px;
                line-height: 28px;
                text-align: center;
            }
        }
    }
}
</style>