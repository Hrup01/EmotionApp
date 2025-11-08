export default {
    namespaced: true,
    state () {
        return {
            // 手账的名称和id
            bulletJournalDetail: []
        }
    },
    mutations: {
        // 设置手账的名称
        setName(state, bulletJournalDetail) {
            state.bulletJournalDetail = bulletJournalDetail
        }
    },
    actions: {},
    getters: {}
}