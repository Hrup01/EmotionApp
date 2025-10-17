<template>
  <div id="test">
    <div @click="fn1">1</div>
    <!-- <div @click="fn2">2</div>
    <div @click="fn3">3</div>
    <div @click="fn4">4</div> -->
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'testPage',
  methods: {
    async fn1 () {
        const token = JSON.parse(localStorage.getItem('emotion_app_info')).token
        // console.log(token)
        const res = await axios.get('http://localhost:8080/ai/chat', {
            params: {
              prompt: '你好',
              chatId: 12345
            },
            headers: {
                Authorization: 'Bearer ' + token
            }
        })
        // const res = await axios.get('http://localhost:8080/api/emotion/diary', {
        //     params: {
        //         date: '2024-01-01'
        //     },
        //     headers: {
        //         Authorization: 'Bearer ' + token
        //     }
        // })
        console.log(res)
    }
  }
}
</script>

<style lang="less" scoped>
#test {
    height: 844px;
    div {
        margin-top: 10px;
        width: 20px;
        height: 20px;
        background-color: pink;
    }
}

</style>