import request from '@/utils/request'

// 发送聊天消息
export function sendMessage(content, sessionId, queryParams = {}) {
    const data = {
        content,
        sessionId,
        queryParams
    }
    return request({
        url: '/chat/send',
        method: 'post',
        data: data
    })
}