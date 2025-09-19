<template>
  <div id="NaVBar">
    <div class="left" @click="$router.push('/moodOption')"><img src="../../assets/image/jt_left.png" alt=""></div>
    <div class="right"><h3>日志打卡</h3></div>
  </div>
</template>

<script>

export default {
  
}
</script>

<style lang="less" scoped>
#NaVBar {
    padding-top: 50px;
    padding-left: 13px;
    // width: 390px;
    // height: 106px;
    // background: #d3e4c3;
    display: flex;
    .left {
        margin-right: 14px;
        height: 24px;
        width: 24px;
        img {
        margin-left: 9px;
        margin-top: 7px;
        width: 8px;
        }
    }
    h3 {
        font-size: 18px;
    }
}
</style>