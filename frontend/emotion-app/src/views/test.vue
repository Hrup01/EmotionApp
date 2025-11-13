<template>
  <div id="app">
    <tooltip></tooltip>
  </div>
</template>

<script>
import axios from 'axios'
import tooltip from '@/components/tooltip.vue'
export default {
  name: 'testPage',
  components: {
    tooltip
  },
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