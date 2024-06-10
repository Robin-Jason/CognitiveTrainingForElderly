<template>
  <div>
    <div class="side-bar">
      <el-menu class="el-menu-vertical-demo" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
        <el-menu-item index="/" @click="navigateTo('/')">
          <i class="el-icon-house"></i>
          <span slot="title">首页</span>
        </el-menu-item>

        <el-submenu index="2">
          <template slot="title">
            <img src="@/assets/icon/CognitiveGame.png" alt="Cognitive Game Icon" class="menu-icon" />
            <span>健脑游戏</span>
          </template>
          <el-menu-item index="/game/sudoku" @click="navigateTo('/game/sudoku')">数独游戏</el-menu-item>
          <el-menu-item index="/game/memory" @click="navigateTo('/game/memory')">卡牌记忆</el-menu-item>
          <el-menu-item index="/game/story" @click="navigateTo('/game/story')">故事接龙</el-menu-item>
        </el-submenu>

        <el-menu-item index="/chat" @click="navigateTo('/chat')">
          <i class="el-icon-message"></i>
          <span slot="title">与蓝心聊天</span>
        </el-menu-item>

<!--        <el-submenu index="3">-->
<!--          <template slot="title">-->
<!--            <i class="el-icon-menu"></i>-->
<!--            <span>用户管理</span>-->
<!--          </template>-->
<!--          <el-menu-item index="/rbac/user/list" @click="navigateTo('/rbac/user/list')">-->
<!--            <i class="el-icon-menu"></i>-->
<!--            <span slot="title">用户列表</span>-->
<!--          </el-menu-item>-->
<!--          <el-menu-item index="/rbac/user/add" @click="navigateTo('/rbac/user/add')">-->
<!--            <i class="el-icon-setting"></i>-->
<!--            <span slot="title">添加用户</span>-->
<!--          </el-menu-item>-->
<!--        </el-submenu>-->

        <el-menu-item index="/logout" @click="logout">
          <i class="el-icon-setting"></i>
          <span slot="title">退出</span>
        </el-menu-item>
      </el-menu>

      <div class="main-content">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script>
import { removeToken } from '@/utils/auth'

export default {
  name: "MyComponent",
  methods: {
    navigateTo(route) {
      this.$router.push(route);
    },
    logout() {
      this.$confirm('确定注销并退出系统吗？', '提示').then(() => {
        removeToken(); // 清除token
        this.$router.push({ path: '/login' }); // 重定向到登录页面
      }).catch(() => {});
    }
  }
};
</script>

<style lang="scss" scoped>
.side-bar {
  display: flex;
  height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px;
}

.menu-icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}
</style>