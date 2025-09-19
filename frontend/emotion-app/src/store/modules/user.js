export default {
    namespace: true,
    state () {
        return {
            // 个人权证相关
            userInfo: {
                token: '',
                userId: ''
            }
        }
    },
    mutation: {
        setUserInfo (state,obj) {
            state.userInfo = obj
        }
    },
    actions: {},
    getters: {}
}