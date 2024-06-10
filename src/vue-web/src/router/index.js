import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('../views/Common/HomeView.vue'),
        name: 'home'
      },
      // 新增游戏相关路由
      {
        path: 'game-selection',
        component: () => import('../views/Common/GameSelection.vue')
      },
      {
        path: 'game/sudoku',
        component: () => import('../views/Game/Sudoku/Sudoku.vue')
      },
      {
        path: 'game/story',
        component: () => import('../views/Game/Story/StoryRelay.vue')
      },
      // 聊天页面路由
      {
        path: 'chat',
        component: () => import('../views/ChatWithAI.vue')
      },
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Common/Login.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Common/Register.vue')
  },
  {
    path: '/game/memory',
    component: () => import('../views/Game/MemoryGame/MemoryGame.vue')
  },
]

// 防止连续点击多次路由报错
let routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router