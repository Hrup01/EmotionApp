<template>
    <div class="contentItem">
      <div class="top">
        <div class="user">
          <img :src="contentList[id].headPortraitUrl" alt="">
          <div class="username">{{ contentList[id].username }}</div>
        </div>
        <div class="more"><img src="@/assets/image/更多1.png" alt=""></div>
      </div>
      <div class="background"><img :src="contentList[id].bgUrl" alt=""></div>
      <div class="title">{{ contentList[id].title }}</div>
      <div class="respond">
        <div class="like">
          <img src="@/assets/image/喜欢.png" alt="">
          <p>{{ contentList[id].likeCount }}</p>
        </div>
        <div class="comment">
          <img src="@/assets/image/评论.png" alt="">
          <p>{{ contentList[id].commentCount }}</p>
        </div>
      </div>
    </div>
</template>

<script>

export default {
    props: ['contentList', 'id']
}
</script>

<style lang="less" scoped>
.contentItem {
    margin-top: 10px;
    height: 315px;
    background-color: #fff;
    border-radius: 10px;
    .top {
        padding: 8px;
        display: flex;
        justify-content: space-between;
        height: 46px;
        .user {
            display: flex;
            align-items: center;
            img {
                width: 32px;
            }
            .username {

                margin-left: 10px;
                color: #575757;
                font-size: 14px;
                font-weight: 600;
            }
        }
        .more {
            img {
                margin-top: 2px;
                height: 24px;
            }
        }
    }
    .background {
        width: 100%;
        height: 180px;
        img {
            height: 180px;
        }
    }
    .title {
        margin: 11px;
        color: #575757;
        font-size: 16px;
        font-weight: 600;
    }
    .respond {
        margin: 10px 11px;
        width: 146px;
        display: flex;
        justify-content: space-between;
        .like,
        .comment {
            padding: 6px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 69px;
            height: 32px;
            border-radius: 6px;
            background: #ffdc9699;
            img {
                width: 20px;
                height: 20px;
            }
            p {
                color: #575757;
                font-size: 14px;
                font-weight: 600;
            }
        }
    }
}
</style>