<template>
  <div id="noteDetail">
    <div class="top">
        <NavBar :title="navbarTitle" :margin-left="marginLeft"></NavBar>
        <div class="navbarRightPic">
            <img src="@/assets/image/笔记详情/更多.png" alt="" class="more" @click="changeFlag">
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
        <div class="userMessage wrapper">
            <img src="@/assets/image/笔记详情/头像1.png" alt="">
            <div class="text">
                <div class="name">CaCa</div>
                <div class="time">2025-9-26</div>
            </div>
            <div class="attention" @click="changeFollowed">
                <p v-if="!followed">关注</p>
                <p v-else>已关注</p>
            </div>
        </div>
        <div class="content wrapper">
            <p>这是一张图片！</p>
            <div class="pic">
                <img src="@/assets/image/笔记详情/示例图1.png" alt="">
            </div>
        </div>
        <div class="function wrapper">
            <div class="left" @click="changeCollect">
                <img src="@/assets/image/笔记详情/收藏-未激活态.png" alt="" v-if="!isCollect">
                <img src="@/assets/image/笔记详情/收藏-激活态.png" alt="" v-else>
            </div>
            <div class="right">
                <div class="like" @click="changeLike">
                    <img src="@/assets/image/笔记详情/喜欢-未激活态.png" alt="" v-if="!isLike">
                    <img src="@/assets/image/笔记详情/喜欢-激活态.png" alt="" v-else>
                    <p>{{ likeCount }}</p>
                </div>
                <div class="comment">
                    <img src="@/assets/image/笔记详情/评论.png" alt="">
                    <p>{{ commentCount }}</p>
                </div>
            </div>
        </div>
    </div>
    <div class="commentSection">
        <div class="title">评论</div>
        <div class="border"></div>
        <ul>
            <li v-for="(item, index) in commentList" :key="item.id">
                <div class="headPic">
                    <img :src="item.headPic" alt="">
                </div>
                <div class="message">
                    <div class="name">{{ item.name }}</div>
                    <div class="comment">{{ item.contant }}</div>
                    <div class="time">{{ item.time }}<p>回复</p></div>
                </div>
                <div class="like"  @click="changeCommentLike(index)">
                    <img src="@/assets/image/笔记详情/评论区喜欢-未激活态.png" alt="" v-if="!item.isCommentLike">
                    <img src="@/assets/image/笔记详情/喜欢-激活态.png" alt="" v-else>
                    <p>{{ item.likeCount }}</p>
                </div>
            </li>
        </ul>
    </div>
    <div class="sendComment wrapper">
        <textarea name="" id="" v-model="myComment" @focus="changeCotent" ref="textarea"></textarea>
        <div class="send" @click="sendComment" ref="send">发送</div>
        <div class="add"><img src="@/assets/image/笔记详情/添加.png" alt=""></div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import axios from 'axios'
export default {
    name: 'noteDetail',
    components: {
        NavBar
    },
    data () {
        return {
            navbarTitle: '笔记',
            marginLeft: '130px',
            flag: false,
            likeCount: 123,
            commentCount: '',
            followed: false,
            isLike: false,
            isCollect: false,
            token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
            postId: this.$route.params.id,
            commentList: [
                { id: 0, headPic: require('@/assets/image/笔记详情/头像2.png'), name: 'NaNa', contant: '是一张很好的照片！', time: '2025-9-27', isCommentLike: false, likeCount: 123 },
                { id: 1, headPic: require('@/assets/image/笔记详情/头像3.png'), name: 'NaNa', contant: '是一张很好的照片！', time: '2025-9-27', isCommentLike: false, likeCount: 123 },
                { id: 2, headPic: require('@/assets/image/笔记详情/头像3.png'), name: 'NaNa', contant: '是一张很好的照片！', time: '2025-9-27', isCommentLike: false, likeCount: 123 },
            ],
            myComment: '点我发评论'
        }
    },
    methods: {
        // 关注
        async toFollowed () {
            const res = await axios.post(`http://localhost:8080/api/community/follow/${this.postId}`, {}, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('关注',res)
        },
        // 取消关注
        async deleteFollowed () {
            const res = await axios.delete(`http://localhost:8080/api/community/follow/${this.postId}`, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('取消关注',res)
        },
        changeFollowed () {
            this.followed = this.followed ? false : true
            if (this.followed) this.toFollowed()
            else this.deleteFollowed()
        },
        // 收藏
        async collect () {
            const res = await axios.post(`http://localhost:8080/api/community/posts/${this.postId}/favorite`, {}, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('收藏',res)
        },
        // 取消收藏
        async deleteCollect () {
            const res = await axios.delete(`http://localhost:8080/api/community/posts/${this.postId}/favorite`, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('取消收藏',res)
        },
        // 点赞帖子
        async postLikePost () {
            // console.log(postId)
            const res = await axios.post(`http://localhost:8080/api/community/posts/${this.postId}/like`, {}, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('点赞帖子',res)
            // 帖子点赞数加1
            this.likeCount += 1
        },
        // 取消点赞帖子
        async deletetLikePost () {
            // console.log('取消点赞帖子请求 token:', this.token)
            const res = await axios.delete(`http://localhost:8080/api/community/posts/${this.postId}/like`, {
                headers: {
                    Authorization: 'Bearer ' + this.token,
                    // 'Content-Type': 'application/json'
                }
            })
            console.log('取消点赞帖子',res)
            // 帖子点赞数减1
            this.likeCount -= 1
        },
        // 点赞评论
        async postLikeComment () {
            // console.log(postId)
            const res = await axios.post(`http://localhost:8080/api/community/posts/${this.postId}/like`, {}, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('点赞评论',res)
        },
        // 点赞收藏相关
        changeFlag () {
            this.flag = this.flag ? false : true
        },
        changeCollect () {
            this.isCollect = this.isCollect ? false : true
            if (this.isCollect) this.collect()
            else this.deleteCollect()
        },
        changeLike () {
            this.isLike = this.isLike ? false : true
            if (this.isLike) this.postLikePost()
            else this.deletetLikePost()
        },
        changeCommentLike (index) {
            this.commentList[index].isCommentLike = this.commentList[index].isCommentLike ? false : true
            // 评论点赞数变化
            if (this.commentList[index].isCommentLike) {
                this.commentList[index].likeCount += 1
            } else {
                this.commentList[index].likeCount -= 1
            }
        },
        // 改变文本域内容
        changeCotent () {
            this.myComment = '',
            this.$refs.textarea.addEventListener('blur', () => {
                if (this.myComment === '') {
                    this.myComment = '点我发评论'
                }
                this.$refs.send.style.background = '#FEFBF6'
                this.$refs.send.style.color = '#0000004d'
            })
            if (this.myComment != '点我发评论') {
                this.$refs.send.style.background = '#FCD78B'
                this.$refs.send.style.color = '#00000099'
            }
        },
        // 发送评论
        async sendComment () {
            if (this.myComment === '点我发评论') return
            // 获得发送评论的时间
            const date = new Date()
            const year = date.getFullYear()
            const month = date.getMonth() + 1
            const day = date.getDate()
            const time = year + '-' + month + '-' + day
            // 1.直接给帖子评论
            const res = await axios.post(`http://localhost:8080/api/community/posts/${this.postId}/comments`, {
                contain: this.myComment
            }, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            console.log('发送评论',res)
            // 将内容推送到评论列表
            this.commentList.unshift({ id: this.commentList.length,
                headPic: require('@/assets/image/笔记详情/头像2.png'),
                name: 'NaNa',
                contant: this.myComment,
                time,
                isCommentLike: false,
                likeCount: 0
            })
            console.log('评论列表',this.commentList)
            // 改变评论数
            this.commentCount += 1
            // 文本域恢复默认状态
            this.myComment = '点我发评论'
            // 2.回复他人的评论
        }
    },
    async mounted () {
        // 获取帖子详情
        const postDetailRes = await axios.get(`http://localhost:8080/api/community/posts/${this.postId}`, {
            headers: {
                Authorization: 'Bearer ' + this.token
            }
        })
        console.log('获取帖子详情',postDetailRes)
        // 获取帖子评论列表
        const postCommentRes = await axios.get(`http://localhost:8080/api/community/posts/${this.postId}/comments`, {
            headers: {
                Authorization: 'Bearer ' + this.token
            }
        })
        console.log('获取帖子评论列表',postCommentRes)
        // 评论数
        this.commentCount = this.commentList.length
        // this.commentList = postCommentRes.data.data
        // console.log(this.commentList)
        // 查看帖子是否收藏
        const collectRes = await axios.get(`http://localhost:8080/api/community/posts/${this.postId}/favorite-status`, {
            headers: {
                Authorization: 'Bearer ' + this.token
            }
        })
        console.log('查看帖子是否收藏',collectRes)
        this.isCollect = collectRes.data.data.isFavorited
        // 查看用户是否关注
        const userFollowedtRes = await axios.get(`http://localhost:8080/api/community/follow-status/${this.postId}`, {
            headers: {
                Authorization: 'Bearer ' + this.token
            }
        })
        console.log('查看用户是否关注',userFollowedtRes)
        this.followed = userFollowedtRes.data.data.isFollowing
    }
}
</script>

<style lang="less" scoped>
* {
    font-weight: 600;
}
#noteDetail {
    width: 390px;
    height: 852px;
    background: #fbedd0;
}
.wrapper {
    margin: 0 auto;
    width: 364px;
}
.top {
    width: 390px;
    height: 514px;
    background: #fdf6e8;
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
    .userMessage {
        margin-top: 25px;
        display: flex;
        align-items: center;
        img {
            margin-right: 12px;
            width:50px;
            height:50px;
        }
        .name {
            color: #000000cc;
        }
        .time {
            color: #00000080;
            font-size: 14px;
        }
        .attention {
            margin-left: 165px;
            width: 63px;
            height: 36px;
            border-radius: 36px;
            border: 1px solid #fbedd0;
            background: #f9e2b4;
            p {
                color: #826403;
                font-size: 14px;
                line-height: 36px;
                text-align: center;
            }
        }
    }
    .content {
        p {
            margin-top: 14px;
            color: #000000cc;
        }
        .pic {
            margin-top: 10px;
            width: 100%;
            height: 280px;
        }
    }
    .function {
        margin-top: 8px;
        display: flex;
        justify-content: space-between;
        img {
            width: 20px;
        }
        .right {
            display: flex;
            .like,
            .comment {
                margin-left: 18px;
                display: flex;
                align-items: center;
                p {
                    margin-left: 8px;
                    color: #7d4105;
                }
            }
        }
    }
}
.commentSection {
    margin-top: 10px;
    width: 390px;
    height: 240px;
    background-color: #fdf6e8;
    .title {
        margin-left: 12px;
        height: 47px;
        line-height: 47px;
    }
    ul {
        width: 390px;
        height: 200px;
        overflow-y: auto;
        li {
            position: relative;
            // margin-top: -3px;
            padding-top: 9px;
            padding-bottom: 12px;
            display: flex;
            width: 390px;
            height: 96px;
            border-top: 2px solid #fbedd0;
            // border-bottom: 2px solid #fbedd0;
            .headPic {
                margin-left: 12px;
                img {
                    margin-right: 16px;
                    width: 40px;
                    height: 40px;
                }
            }
            .message {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                width: 255px;
                .comment {
                    color: #000000cc;
                    font-size: 14px;
                    font-weight: 400;
                }
                .time {
                    display: flex;
                    color: #000000cc;
                    font-size: 10px;
                    font-weight: 400;
                    p {
                        margin-left: 4px;
                    }
                }
            }
            .like {
                position: absolute;
                top: 15px;
                right: 12px;
                display: flex;
                align-items: center;
                img {
                    width: 22px;
                    height: 22px;
                }
                p {
                    margin-left: 6px;
                    width: 28px;
                    color: #5c5c5c;
                    font-size: 14px;
                }
            }
        }
    }
}
.sendComment {
    padding-top: 16px;
    display: flex;
    width: 390px;
    height: 80px;
    background-color: #fff;
    textarea {
        margin-left: 22px;
        margin-right: 14px;
        padding: 10px 60px 10px 19px;
        width: 314px;
        height: 40px;
        border-radius: 50px;
        border: 0 ;
        background: #fae9c7;
        color: #0000004d;
        font-size: 14px;
        font-weight: 400;
    }
    .send {
        position: absolute;
        bottom: 28px;
        right: 62px;
        width: 48px;
        height: 30px;
        border-radius: 30px;
        background: #FEFBF6;
        color: #0000004d;
        font-size: 14px;
        line-height: 30px;
        text-align: center;
    }
    .add {
        img {
            margin-top: 6px;
            margin-right: 12px;
            width: 28px;
            height: 28px;
        }
    }
}
</style>