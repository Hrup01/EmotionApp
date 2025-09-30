<template>
  <div id="NaVBar">
    <div class="left" @click="$router.push(`${toUrl}`)"><img src="@/assets/image/jt_left.png" alt=""></div>
    <div class="right"><h3>{{ title }}</h3></div>
  </div>
</template>

<script>

export default {
  props: ['title','toUrl']
}
</script>

<style lang="less" scoped>
#NaVBar {
    padding-top: 50px;
    padding-left: 13px;
    display: flex;
    .left {
        margin-right: 14px;
        height: 24px;
        width: 24px;
        img {
        margin-left: 9px;
        margin-top: 1px;
        width: 24px;
        }
    }
    h3 {
        font-size: 18px;
    }
}
</style>