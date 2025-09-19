import request from '@/utlis/request'

// 1. post 用户信息
export const postUserMessage = () => {
    return request.post('http://127.0.0.1:8080/login',{
            username: 'test',
            password: '123456'
        })
}