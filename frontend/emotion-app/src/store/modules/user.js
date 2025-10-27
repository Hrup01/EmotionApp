import { getInfo, setInfo } from '@/utlis/storage'

export default {
    namespaced: true,
    state () {
        return {
            // 个人权证相关
            userInfo: getInfo()
        }
    },
    mutations: {
        setUserInfo (state,obj) {
            state.userInfo = obj
            setInfo(obj)
        }
    },
    actions: {},
    getters: {}
}