import request from '@/utlis/request'

// 1. 登录接口
export const userLogin = (username,password) => {
    return request.post('http://127.0.0.1:8080/login',{
            username,
            password
        })
}