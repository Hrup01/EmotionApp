<template>
  <div class="noteItem">
    <img :src="noteList[id].pic" alt="" class="pic">
    <div class="message">
        <div class="bc"></div>
        <p class="text">{{ noteList[id].text }}</p>
        <div class="respond">
            <div class="like">
                <img src="@/assets/image/白色喜欢.png" alt="">
                <p>{{ noteList[id].likeCount }}</p>
            </div>
            <div class="comment">
                <img src="@/assets/image/白色评论.png" alt="">
                <p>{{ noteList[id].commentCount }}</p>
            </div>
        </div>
    </div>
  </div>
</template>

<script>

export default {
    props: ['noteList', 'id']
}
</script>

<style lang="less" scoped>
.noteItem {
    position: relative;
    margin-top: 6px;
    height: 216px;
    width: 179px;
    border-radius: 10px;
    // background-color: pink;
    .pic {
        width: 100%;
        height: 100%;
        // background-image: url();
        background-size: cover;
    }
    .message {
        .bc {
            position: absolute;
            bottom: 0;
            width: 100%;
            height: 45px;
            border-radius: 0 0 10px 10px;
            opacity: 0.6;
            background: #cccccc;
        }
        p {
            font-size: 14px;
            color: #fff;
        }
        .text {
            margin-left: 8px;
            position: absolute;
            bottom: 26px;
            width: 92%;
        }
        .respond {
            position: absolute;
            right: 2px;
            bottom: 3px;
            display: flex;
            .like,
            .comment {
                margin-right: 6px;
                display: flex;
                align-items: center;
                img {
                    margin-right: 4px;
                    width: 16px;
                    height: 16px;
                }
            }
        }
    }
}
</style>