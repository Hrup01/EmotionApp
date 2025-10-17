// import { getPlayTime } from "@/utlis/storage"

export default {
    namespaced: true,
    state () {
        return {
            // 个人权证相关
            playTime: ''
        }
    },
    mutations: {
        setwhiteNoisePlayTime (state,time) {
            state.playTime = time
        }
    },
    actions: {},
    getters: {}
}