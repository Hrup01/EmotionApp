// 约定通用键名
const INFO_KEY = 'emotion_app_info'

// 获取个人信息
export const getInfo = () => {
    const defaultObj = { token: '', userId: '' }
    const result = localStorage.getItem(INFO_KEY)
    return result ? JSON.parse(result) : defaultObj
}

// 设置个人信息
export const setInfo = (obj) => {
    localStorage.setItem(INFO_KEY, JSON.stringify(obj))
}

// 移除个人信息
export const removeInfo = () => {
    localStorage.removeItem(INFO_KEY)
}

// // 获取播放时间
// export const getPlayTime = () => {
    
// }

// // 设置播放时间
// export const setPlayTime = (time) => {

// }