import { getInfo, setInfo } from '@/utlis/storage'

export default {
    namespace: true,
    state () {
        return {
            // 个人权证相关
            userInfo: getInfo()
        }
    },
    mutation: {
        setUserInfo (state,obj) {
            state.userInfo = obj
            setInfo(obj)
        }
    },
    actions: {},
    getters: {}
}