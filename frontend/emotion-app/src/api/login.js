import request from '@/utlis/request'

// 1. 登录接口
export const userLogin = (username,password) => {
    return request.post('/api/auth/login',{
            username,
            password
        })
}