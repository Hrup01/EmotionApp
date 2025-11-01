import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import countDown from './modules/countDown'
import woodFish from './modules/woodFish'
import diary from './modules/diary'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user,
    countDown,
    woodFish,
    diary
  }
})
