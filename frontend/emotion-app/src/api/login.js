// import request from '@/utlis/request'
import axios from 'axios'

// 1. 登录接口
export const userLogin = (username,password) => {
    return axios.post('http://localhost:8080/api/auth/login',{
            username,
            password
        })
}