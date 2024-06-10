import request from '@/utils/request'

// 用户注册
export function register(username, password,email) {
    const data = {
        username,
        password,
        email
    }
    return request({
        url: '/user/register',
        method: 'post',
        data: data
    })
}