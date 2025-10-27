export default {
    namespaced: true,
    state () {
        return {
            // 白噪音播放时间
            playTime: 0
        }
    },
    mutations: {
        // 设置白噪音播放时间
        setwhiteNoisePlayTime (state,time) {
            state.playTime = time
            // console.log('白噪音播放时间已设置为：' + state.playTime + '秒')
        },
        // 改变countDown时间
        changePlayTime (state) {
            // 减少播放时间 -- 每次调用减少1秒
            state.playTime -= 1
        }
    },
    actions: {},
    getters: {}
}