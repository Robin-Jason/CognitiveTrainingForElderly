import request from '@/utils/request';

export function startStory(data) {
    return request({
        url: '/story/start',
        method: 'post',
        data: data
    });
}

export function continueStory(data) {
    return request({
        url: '/story/continue',
        method: 'post',
        data: data
    });
}