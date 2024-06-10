<template>
  <div class="game-selection">
    <el-row :gutter="20" justify="center">
      <el-col :span="6" v-for="game in games" :key="game.name">
        <el-button class="box-button clickable-button" @click="navigateTo(game.route)">
          <div class="button-content">
            <span class="button-title">{{ game.name }}</span>
            <div class="game-description">{{ game.description }}</div>
          </div>
        </el-button>
      </el-col>
    </el-row>
    <el-card class="history-card">
      <div slot="header" class="clearfix">
        <span>历史游戏记录</span>
      </div>
      <el-table :data="allHistory" style="width: 100%">
        <el-table-column prop="name" label="游戏名称" width="150"></el-table-column>
        <el-table-column prop="date" label="日期" width="150"></el-table-column>
        <el-table-column prop="duration" label="持续时间" width="150"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'GameSelection',
  data() {
    return {
      games: [
        {
          name: '数独游戏',
          description: '填充数字，挑战你的逻辑思维。',
          route: '/game/sudoku',
          history: [
            { date: '2024-05-01', duration: '30分' },
            { date: '2024-05-02', duration: '45分' }
          ]
        },
        {
          name: '卡牌记忆',
          description: '测试你的记忆力，找到匹配的卡牌。',
          route: '/game/memory',
          history: [
            { date: '2024-05-03', duration: '20分' },
            { date: '2024-05-04', duration: '25分' }
          ]
        },
        {
          name: '故事接龙',
          description: '和亲朋好友一起讲故事，锻炼你的创造力。',
          route: '/game/story',
          history: [
            { date: '2024-05-05', duration: '40分' },
            { date: '2024-05-06', duration: '60分' }
          ]
        }
      ]
    };
  },
  computed: {
    allHistory() {
      return this.games.flatMap(game =>
          game.history.map(record => ({
            name: game.name,
            date: record.date,
            duration: record.duration
          }))
      );
    }
  },
  methods: {
    navigateTo(route) {
      this.$router.push(route).catch(err => console.error(err));
    }
  }
};
</script>

<style scoped>
.game-selection {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.el-row {
  width: 100%;
  display: flex;
  justify-content: center;
}

.box-button {
  cursor: pointer;
  width: 300px;
  height: 200px;
  margin: 20px;
  font-size: 18px;
  transition: box-shadow 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  color: black;
  border: 1px solid #dcdfe6;
}

.button-content {
  text-align: center;
}

.button-title {
  font-size: 24px;
}

.game-description {
  font-size: 16px;
  padding: 10px 0;
}

.clickable-button:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
}

.history-card {
  margin-top: 40px;
  width: 80%;
}
</style>