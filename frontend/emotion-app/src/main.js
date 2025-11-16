import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// import axios from 'axios'
import '@/styles/common.less'
import '@/utlis/vant-ui'
import axios from 'axios'


Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

Vue.directive('authImage', {
  "inserted"(el, binding) {
    const token = JSON.parse(localStorage.getItem('userInfo')).token
    axios.get(binding.value, {
      responseType: 'blob',
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then(res => {
      const url = URL.createObjectURL(res.data)
      el.src = url
    })
  }
})

