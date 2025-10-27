<template>
  <div id="app">
    <div class="test">
        <keep-alive>
          <router-link to="nodedetail/1">111</router-link>
          <router-link to="nodedetail/2">111</router-link>
          <router-link to="nodedetail/3">111</router-link>
          <router-link to="nodedetail/4">111</router-link>
          <router-link to="nodedetail/5">111</router-link>
          <router-link to="nodedetail/6">111</router-link>
          <router-link to="nodedetail/7">111</router-link>
          <router-link to="nodedetail/8">111</router-link>
          <router-link to="nodedetail/9">111</router-link>
          <router-link to="nodedetail/10">111</router-link>
          <router-link to="nodedetail/11">111</router-link>
          <router-link to="nodedetail/12">111</router-link>
        </keep-alive>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'testPage',
  data () {
    return {
      token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
    }
  },
  mounted() {
    if (this.$refs.upload) {
      this.$refs.upload.addEventListener('change', e => {
        const fd = new FormData()
        // console.log(e.target.files[0])
        fd.append('images',e.target.files[0])
        console.log(fd)
        // 检查 FormData 内容
        for (let [key, value] of fd.entries()) {
          console.log(key, value);
        }
        axios.post('http://localhost:8080/api/community/posts?content', fd, {
            params: {
                content: '测试上传图片'
            },
            headers: {
                Authorization: 'Bearer ' + this.token
            }
        }).then(res => {
            console.log(res)
        }).catch(err => {
            console.log(err)
        })
    })
    }
  }
}
</script>

<style lang="less" scoped>
#app {
  width: 390px;
  height: 600px;
  overflow-y: auto;
  .test {
    display: flex;
    flex-direction: column;
    
    a {
      margin-top: 10px;
      width: 100px;
      height: 100px;
      background-color: red;
    }
  }
}

</style>