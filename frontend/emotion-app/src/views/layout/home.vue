<template>
  <div id="home">
    <router-link to="/ai">ai</router-link>
    <!-- <audio src="@/assets/audio/暴风雪.mp3" controls playsinline="auto"></audio> -->
     <div class="testRegister" @click="testGetRequest"></div>
     <div class="testLogin"></div>
  </div>
</template>

<script>
import axios from 'axios'
import { getInfo } from '@/utlis/storage'
// import request from '@/utlis/request.js'
export default {
    name: 'homePage',
    methods: {
      async testGetRequest () {
        const token = getInfo().token
        console.log(token)
        const res = await axios.get('http://localhost:8080/api/emotion/check-in-count',{
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        console.log(res)
      }
    }
}
</script>

<style lang="less" scoped>
.testRegister {
  width: 100px;
  height: 100px;
  background-color: rgb(109, 60, 68);
}
.testLogin {
  width: 100px;
  height: 100px;
  background-color: pink;
}
</style>