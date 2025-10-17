<template>
  <div id="privateLetter">
    <nav-bar></nav-bar>
    <div class="userMessage">
        <img src="@/assets/image/私信/头像.png" alt="">
        <div class="name">CaCa</div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';
export default {
    name: 'privateLetter',
    components: {
        NavBar
    }
}
</script>

<style lang="less" scoped>
#privateLetter {
    width: 390px;
    height: 104px;
    background: #fdf4e4;
    font-weight: 600;
}
.userMessage {
    position: absolute;
    top: 42px;
    left: 50px;
    display: flex;
    align-items: center;
    img {
        margin-right: 14px;
        width: 46px;
        height: 46px;
    }
    p {
        color: #363636;
        font-size: 18px;
    }
}
</style>