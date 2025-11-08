export default {
    namespaced: true,
    state () {
        return {
            // 进入日记本的季节
            season: '',
            // 进入日记本的年份
            year: '',
            // 属于该季节的三个月份
            month: {
                firstMonth: '',
                sencondMonth: '',
                lastMonth: ''
            }
        }
    },
    mutations: {
        // 设置进入日记本的季节
        setSeason (state, season) {
            state.season = season
            // console.log('设置季节成功', state.season)
        },
        // 设置进入日记本的年份
        setYear (state, year) {
            state.year = year
            // console.log('设置年份成功', state.year)
        },
        // 设置属于该季节的三个月份
        setMonth (state, obj) {
            state.month = obj
            console.log('设置月份成功', state.month)
        }
    },
    actions: {},
    getters: {}
}