<template>
  <div class="home">
    <div class="top-bar">
      <div class="user-info">
        <div class="greeting">
          早晨好，李先生！
        </div>
        <div class="datetime">
          2024年5月15日 星期二
        </div>
      </div>
      <div class="time-weather">
        <p>10:05</p>
        <p>晴 - 23°C</p>
      </div>
    </div>

    <div class="content-area">
      <div class="main-content">
        <div class="knowledge-section">
          <h2>你不知道的生活健康知识</h2>
          <div class="image-placeholder">
          </div>
        </div>
        <div class="goal-section">
          <h2>今日目标</h2>
          <p>早上起床做50分钟健身操，下午3点50分做口语练习</p>
        </div>
        <div class="daily-suggestions">
          <h2>每日建议</h2>
          <ul>
            <li>早上起床做50分钟健身操</li>
            <li>下午3点50分做口语练习</li>
            <li>下午6点：0糖食品采购</li>
            <li>晚上8点30分学习视频</li>
          </ul>
        </div>
        <div class="interaction-buttons">
          <el-button icon="el-icon-edit" @click="goToGameSelection" class="action-button">健脑游戏</el-button>
          <el-button icon="el-icon-document" class="action-button">健康报告</el-button>
          <el-button icon="el-icon-chat-dot-round" class="action-button">与蓝心聊天</el-button>
        </div>
      </div>
      <div class="side-panel">
        <div class="health-stats">
          <h2>健康统计</h2>
          <div class="stat-item">
            <span>今日步数</span>
            <span>138/80</span>
          </div>
          <div class="stat-item">
            <span>心率</span>
            <span>93</span>
          </div>
          <div class="stat-item">
            <span>睡眠</span>
            <span>85</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomeView",
  methods: {
    goToGameSelection() {
      this.$router.push('/game-selection');
    }
  }
}
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  font-family: "PingFang SC", sans-serif;
  background-color: #F7F8FA;
  font-size: 18px;
}

.top-bar {
  padding: 20px;
  background-color: #FFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info .greeting,
.user-info .datetime {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.time-weather {
  margin-left: auto;
  padding-left: 50px;
}

.time-weather p {
  margin: 0;
  font-size: 20px;
}

.content-area {
  display: flex;
  flex-direction: row;
  padding: 20px;
}

.main-content {
  flex: 3;
  background-color: #FFF;
  padding: 20px;
  margin: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.knowledge-section,
.goal-section,
.daily-suggestions {
  background-color: #FFF;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.interaction-buttons {
  display: flex;
  gap: 10px;
  justify-content: center;
  padding: 20px;
  background-color: #FFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.action-button {
  flex: 1;
  padding: 10px;
  background-color: #007BFF;
  color: #FFF;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.action-button:hover {
  background-color: #0056b3;
}

.side-panel {
  flex: 1;
  background-color: #FFF;
  padding: 20px;
  margin: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.health-stats {
  background-color: #FFF;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.health-stats h2 {
  margin-top: 0;
  font-size: 20px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #EEE;
}

.stat-item:last-child {
  border-bottom: none;
}

.stat-item span {
  font-size: 18px;
}
</style>