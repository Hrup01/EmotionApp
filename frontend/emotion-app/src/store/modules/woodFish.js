export default {
    namespaced: true,
    state () {
        return {
            // 敲击物品
            strikeStuff: {
                url: '',
                audio: ''
            },
            // 木鱼界面背景
            scene: {
                url: '',
                remark: ''
            },
            // 是否有音效
            haveAudio: '',
            // 是否震动
            isShake: ''
        }
    },
    mutations: {
        // 设置敲击物品
        setStrikeStuff (state, stuffObj) {
            state.strikeStuff = stuffObj
            // 将物品存到本地存储中
            localStorage.setItem('woodFishStuff', JSON.stringify(stuffObj))
        },
        // 设置木鱼界面背景
        setBackgroundUrl (state, sceneObj) {
            state.scene = sceneObj
            // console.log('场景已设置', state.scene)
            // 将场景存到本地存储中
            localStorage.setItem('woodFishScene', JSON.stringify(sceneObj))
        },
        // 是否有音效
        setAudio (state, haveAudio) {
            state.haveAudio = haveAudio
            console.log('是否有音效', haveAudio)
        },
        // 是否震动
        setShake (state, isShake) {
            state.isShake = isShake
            console.log('是否震动', isShake)
        }
    },
    actions: {},
    getters: {}
}