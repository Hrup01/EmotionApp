<template>
  <div id="whiteNoise">
    <NavBar :title="title" :toUrl="toUrl"></NavBar>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'whiteNoise',
    components: {
        NavBar
    },
    data () {
        return {
            title: '白噪音',
            toUrl: '',
        }
    }
}
</script>

<style lang="less" scoped>
#whiteNoise {
    width: 390px;
    height: 844px;
    background: linear-gradient(160.4deg, #ffe2de 0%, #fdf4e4 100%);
}
</style>