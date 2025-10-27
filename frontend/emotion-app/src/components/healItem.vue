<template>
    <div class="healItem">
        <router-link :to="healList[id].toUrl">
            <img :src="healList[id].bgUrl" alt="" class="bg">
            <img :src="healList[id].url" alt="">
            <h4>{{ healList[id].title }}</h4>
            <p>{{ healList[id].introduce }}</p>
        </router-link>
    </div>
</template>

<script>

export default {
  props: ['healList', 'id']
}
</script>

<style lang="less" scoped>
a {
    position: relative;
    padding: 16px;
    display: block;
    width: 150px;
    height: 150px;
    border-radius: 20px;
    // background-image: url('@/assets/image/疗愈空间/白噪音背景.png');
    .bg {   
        position: absolute;
        left: 0;
        top: 0;
        width: 150px;
        height: 150px;
        background-size: cover;
        z-index: 0;
    }
    img,
    h4,
    p {
        position: relative;
    }
    img {
        width: 46px;
        height: 46px;
        z-index: 1;
    }
    h4 {
        margin-top: 13px;
        color: #ffffff;
        font-size: 18px;
        z-index: 1;
    }
    p {
        margin-top: 4px;
        color: #ffffffb3;
        font-size: 12px;
        z-index: 1;
    }
}
</style>