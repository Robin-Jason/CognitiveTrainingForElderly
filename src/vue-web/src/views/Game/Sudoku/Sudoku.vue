<template>
  <div class="sudoku-container">
    <div class="header">
      欢迎来到数独游戏训练
    </div>
    <div class="main-content">
      <div class="left-panel">
        <el-card class="sudoku-card">
          <div class="profile-section">
            <!-- 添加难度选择下拉菜单 -->
            <el-select v-model="difficulty" placeholder="选择难度">
              <el-option label="简单" value="easy"></el-option>
              <el-option label="中等" value="medium"></el-option>
              <el-option label="困难" value="hard"></el-option>
            </el-select>
            <!-- 显示计时器 -->
            <div class="timer">时间: {{ elapsedTime }} 秒</div>
          </div>
          <div class="game-and-buttons">
            <div class="game-area">
              <div class="sudoku-grid">
                <div
                    v-for="(cell, index) in cells"
                    :key="index"
                    class="sudoku-cell"
                    :class="{ fixed: cell.fixed }"
                >
                  <input
                      ref="input-{{ index }}"
                      type="number"
                      min="1"
                      max="9"
                      v-model.number="cell.value"
                      :disabled="cell.fixed"
                      @keypress="handleKeyPress($event, index)"
                  />
                </div>
              </div>
            </div>
            <div class="button-section">
              <div class="button-group">
                <el-button class="button" type="primary" @click="generateSudoku">生成</el-button>
                <el-button class="button" type="warning" @click="resetGame">重置</el-button>
                <el-button class="button" type="info">填充</el-button>
                <el-button class="button" type="default" @click="provideHint">提示</el-button>
                <el-button class="button" type="primary">检查</el-button>
                <el-button class="button" type="primary" @click="checkSolution">验证</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </div>
      <div class="right-panel">
        <div class="history">
          历史训练记录
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import sudoku from 'sudoku';

export default {
  data() {
    return {
      cells: Array.from({ length: 81 }, () => ({ value: 0, fixed: false })),
      difficulty: 'easy', // 默认难度
      timer: null,
      elapsedTime: 0,
      historyRecords: [
        { date: '2024-05-10', difficulty: '简单', time: 300, result: '成功' },
        { date: '2024-05-11', difficulty: '中等', time: 450, result: '失败' },
        { date: '2024-05-12', difficulty: '困难', time: 600, result: '成功' },
        { date: '2024-05-13', difficulty: '简单', time: 200, result: '成功' },
        { date: '2024-05-14', difficulty: '中等', time: 400, result: '失败' },
        { date: '2024-05-15', difficulty: '困难', time: 550, result: '成功' },
      ],
    };
  },
  created() {
    this.generateSudoku();
    this.startTimer();
  },
  methods: {
    generateSudoku() {
      let puzzle, solution;
      do {
        puzzle = sudoku.makepuzzle();
        solution = sudoku.solvepuzzle(puzzle);
      } while (!solution);

      this.cells = puzzle.map((value, index) => ({
        value: value === null ? 0 : value + 1,
        fixed: value !== null,
      }));
      this.solution = solution.map(value => value + 1);
      this.resetTimer();
      this.startTimer();
    },
    resetGame() {
      this.cells.forEach((cell) => {
        if (!cell.fixed) {
          cell.value = 0;
        }
      });
    },
    checkSolution() {
      const validGroup = (group) => {
        const seen = new Set();
        for (let i = 0; i < 9; i++) {
          const num = group[i].value;
          if (num < 1 || num > 9 || seen.has(num)) return false;
          seen.add(num);
        }
        return true;
      };

      for (let i = 0; i < 9; i++) {
        const row = this.cells.slice(i * 9, i * 9 + 9);
        const col = this.cells.filter((_, index) => index % 9 === i);
        const box = this.cells.filter((_, index) =>
            Math.floor(index / 27) === Math.floor(i / 3) &&
            Math.floor(index % 9 / 3) === i % 3
        );
        if (!validGroup(row) || !validGroup(col) || !validGroup(box)) {
          alert('解答错误，请再试一次！');
          return false;
        }
      }
      alert('恭喜你，解答正确！');
      this.recordHistory('成功');
      return true;
    },
    startTimer() {
      this.timer = setInterval(() => {
        this.elapsedTime++;
      }, 1000);
    },
    resetTimer() {
      clearInterval(this.timer);
      this.elapsedTime = 0;
    },
    handleKeyPress(event, index) {
      if (!/[1-9]/.test(event.key)) {
        event.preventDefault();
      }
    },
    provideHint() {
      const emptyCells = this.cells.filter(cell => cell.value === 0);
      if (emptyCells.length === 0) return;
      const randomCell = emptyCells[Math.floor(Math.random() * emptyCells.length)];
      const randomCellIndex = this.cells.indexOf(randomCell);
      this.cells[randomCellIndex].value = this.solution[randomCellIndex];
    },
    recordHistory(result) {
      const date = new Date().toISOString().split('T')[0];
      this.historyRecords.push({
        date,
        difficulty: this.difficulty,
        time: this.elapsedTime,
        result
      });
    }
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
};
</script>

<style scoped>
.sudoku-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f3d59f;
}

.header, .left-panel, .right-panel, .footer {
  background-color: #fff;
  border: 1px solid #dcdcdc;
  padding: 20px;
  margin: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.main-content {
  display: flex;
  width: 100%;
  justify-content: space-around;
}

.left-panel, .right-panel {
  flex: 1;
  background-color: #f7ebe4
}

.right-panel{
  background-color: #fbf5f2;
}

.footer {
  display: flex;
  justify-content: space-around;
  width: 100%;
}

.footer-button {
  margin: 5px;
}

.sudoku-card {
  width: 100%;
}

.profile-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.timer {
  font-size: 1.2em;
  font-weight: bold;
}

.game-area {
  margin-top: 20px;
}

.sudoku-grid {
  display: grid;
  grid-template-columns: repeat(9, 1fr);
  gap: 5px;
}

.sudoku-cell {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sudoku-cell input {
  width: 100%;
  height: 100%;
  text-align: center;
  font-size: 1.2em;
}

.sudoku-cell.fixed input {
  background-color: #e0e0e0;
}

.button-section {
  margin-top: 20px;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.history {
  font-size: 1em;
  font-weight: bold;
  text-align: center;
}

.history-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.history-table th, .history-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

.history-table th {
  background-color: #f2f2f2;
}

.history-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.history-table tr:hover {
  background-color: #ddd;
}

.footer{
  width: 80%;
  background-color: #f7ebe4;
}

.footer-button:hover{
  color: #f3d59f;
}

.footer-button{
  background-color: #fbf5f2;
}
</style>