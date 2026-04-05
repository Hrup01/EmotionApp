<template>
  <div id="guide-page">
    <img src="@/assets/image/时间.png" alt="" class="top-time" ref="topTime">
    <div class="enter-into" @click="$router.push('/home')">进入情绪小栈</div>
  </div>
</template>

<script>

export default {
    name: "guidePage",
}
</script>

<style lang="less" scoped>
#guide-page {
    width: 390px;
    height: 844px;
    background: url('@/assets/image/引导页/背景_狗+阴影.png') no-repeat center center;
    background-size: cover;
}
.top-time {
  width: 390px;
  background-color: transparent;
}
.enter-into {
    margin: 576px auto 0 auto;
    width: 190px;
    height: 46px;
    border-radius: 46px;
    background: #ffc492;
    box-shadow: inset -2px -2px 4px 0 #cf6d0c40;
    color: #ffffff;
    font-size: 18px;
    line-height: 46px;
    text-align: center;
}
</style>