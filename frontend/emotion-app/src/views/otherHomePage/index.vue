<template>
  <div id="otherHomePage">
    <NavBar :isTransparent="isTransparent"></NavBar>
    <div class="navbarRightPic">
        <img src="@/assets/image/更多1.png" alt="" class="more" @click="changeFlag">
        <div class="hiden" ref="hiden" v-show="flag">
            <div class="blackList" @click="blackList = true">
                <img src="@/assets/image/拉黑用户.png" alt="">
                <p>拉黑用户</p>
            </div>
            <div class="report" @click="report = true">
                <img src="@/assets/image/举报用户.png" alt="">
                <p>举报用户</p>
            </div>
        </div>
    </div>
    <div class="subject">
        <div class="headPic">
            <img :src="userUrl" alt="">
        </div>
        <div class="detail">
            <div class="attention"><p>关注</p>{{ attention }}</div>
            <div class="fans"><p>粉丝</p>{{ fans }}</div>
        </div>
        <div class="followedState" @click="changeFollowedState">
            <div class="toAttention" v-if="!isfollowed">关注</div>
            <div class="followed" v-else>
                <div class="following">
                    <p>正在关注</p>
                    <img src="@/assets/image/已关注.png" alt="">
                </div>
                <div class="privateLetter" @click="$router.push(`/privateLetter/${username}`)">
                    <p>私信</p>
                    <img src="@/assets/image/私信.png" alt="">
                </div>
            </div>
        </div>
        <div class="note">
            <h6>笔记</h6>
            <ul>
                <li v-for="item in noteList" :key="item.id">
                    <noteItem
                    :id="item.id"
                    :noteList="noteList"
                    ></noteItem>
                </li>
            </ul>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import noteItem from '@/components/noteItem.vue'
export default {
    name: 'otherHomePage',
    components: {
        NavBar,
        noteItem
    },
    data () {
        return {
            isTransparent: 1,
            flag: false,
            blackList: false,
            report: false,
            isfollowed: false,
            userId: this.$route.params.id,
            attention: 123,
            fans: 123,
            noteList: [
                { id: 0, pic: require('@/assets/image/笔记背景1.png'), text: '这里是第一段文字', likeCount: 123, commentCount: 123 },
                { id: 1, pic: require('@/assets/image/笔记背景2.png'), text: '这里是第二段文字', likeCount: 123, commentCount: 123 },
                { id: 2, pic: require('@/assets/image/笔记背景3.png'), text: '这里是第三段文字', likeCount: 123, commentCount: 123 },
                { id: 3, pic: require('@/assets/image/笔记背景4.png'), text: '这里是第四段文字', likeCount: 123, commentCount: 123 }
            ],
            // 用户信息
            username: '',
            userUrl: ''
        }
    },
    methods: {
        changeFlag () {
            this.flag = this.flag ? false : true
        },
        // 是否关注
        changeFollowedState () {
            this.isfollowed = this.isfollowed ? false : true
        }
    },
    mounted () {
        // 获取传过来的参数
        const query = this.$route.query
        // console.log(query)
        this.username = query.name
        this.userUrl = query.url
        console.log(this.userUrl)
        if (query.followed === 'true') {
            this.isfollowed = true
        } else {
            this.isfollowed = false
        }
    }
}
</script>

<style lang="less" scoped>
#otherHomePage {
    height: 844px;
    background-image: url('@/assets/image/主页背景.png');
    background-size: cover;
}
.navbarRightPic {
    .more {
        position: fixed;
        top: 50px;
        right: 22px;
        width: 24px;
    }
    .hiden {
        margin-top: 4px;
        padding-left: 16px;
        position: fixed;
        right: 29px;
        width: 104px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        height: 66px;
        border-radius: 8px;
        background: #fff3db;
        img {
            margin-right: 9px;
            width: 18.25px;
            height: 18.77px;
        }
        p {
            margin-top: 2px;
            color: #000000b3;
            font-size: 10.95px;
            font-weight: 600;
        }
        .blackList,
        .report {
            display: flex;
        }
    }
}
.subject {
    padding: 0 12px;
    position: fixed;
    bottom: 0;
    width: 100%;
    height: 538px;
    border-radius: 40px 40px 0 0;
    background: #fff3db;
    box-shadow: -2px -6px 16px 0 #00000040;
    // overflow-y: auto;
    .headPic {
        position: fixed;
        bottom: 485px;
        right: 145px;
        img {
            width: 100px;
        }
    }
    .detail {
        margin-top: 66px;
        padding: 0 96px;
        display: flex;
        justify-content: space-between;
        color: #545454;
        font-size: 16px;
        font-weight: 500;
        .attention,
        .fans {
            display: flex;
            p {
                margin-right: 6px;
            }
        }
    }
    .followedState {
        .toAttention {
            margin: 16px auto;
            width: 104px;
            height: 40px;
            border-radius: 30px;
            background: #ffda8f;
            text-align: center;
            line-height: 40px;
        }
        .followed {
            margin: 16px 0;
            display: flex;
            img {
                width: 20px;
                height: 20px;
            }
            p {
                margin-left: 17px;
                margin-right: 4px;
            }
            .following,
            .privateLetter {
                display: flex;
                align-items: center;
                height: 40px;
                border-radius: 30px;
            }
            .following {
                margin-left: 66px;
                margin-right: 10px;
                width: 122px;
                background: #ffe7b8;
                color: #91723499;
            }
            .privateLetter {
                width: 88px;
                background: #ffda8f;
            }
        }
    }
    .note {
        color: #404040cc;
        h6 {
            font-size: 20px;
        }
        ul {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            height: 340px;
            overflow-y: auto;
        }
    }
}
</style>