<template>
  <div class="story-relay-page">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card" v-loading="loading" element-loading-text="AI 正在输入...">
          <div slot="header" class="clearfix">
            <span>故事接龙</span>
          </div>
          <div class="story-container" ref="storyContainer">
            <el-scrollbar wrap-class="scrollbar-wrapper">
              <div v-for="(part, index) in story" :key="index" class="story-part" :class="{'user-part': part.fromUser, 'ai-part': !part.fromUser}">
                <p>{{ part.content }}</p>
                <span class="timestamp">{{ part.timestamp }}</span>
              </div>
            </el-scrollbar>
          </div>
          <div class="input-area">
            <el-input
                v-model="input"
                placeholder="输入你的故事部分..."
                @keyup.enter.native="handleStory"
                class="input-with-select"
                :disabled="!isUserTurn"
            >
              <el-button slot="append" icon="el-icon-edit" @click="handleStory" :disabled="!isUserTurn">{{ isUserTurn ? '继续故事' : 'AI 正在输入...' }}</el-button>
            </el-input>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { startStory, continueStory } from '@/api/story';

export default {
  data() {
    return {
      input: '',
      story: [],
      sessionId: this.generateSessionId(),
      loading: false,
      isUserTurn: true,
    };
  },
  methods: {
    async handleStory() {
      if (this.input.trim() !== '') {
        const userPart = { fromUser: true, content: this.input, timestamp: this.getCurrentTimestamp() };
        this.story.push(userPart);
        const storyPart = {
          content: this.input,
          sessionId: this.sessionId,
          fromUser: true,  // 确保传递用户身份
        };
        this.input = ''; // 发送后清空输入框
        this.scrollToBottom();
        this.loading = true;
        this.isUserTurn = false;
        try {
          let response;
          if (this.story.length === 1) {
            // 如果是故事的开头，调用 startStory 方法
            response = await startStory(storyPart);
          } else {
            // 否则调用 continueStory 方法
            response = await continueStory(storyPart);
          }
          this.story.push({ fromUser: false, content: response.data.content, timestamp: this.getCurrentTimestamp() });
        } catch (error) {
          console.error('Error handling story:', error);
        } finally {
          this.loading = false;
          this.isUserTurn = true;
          this.scrollToBottom();
        }
      }
    },
    generateSessionId() {
      return Math.random().toString(36).substr(2, 9);
    },
    getCurrentTimestamp() {
      const now = new Date();
      return `${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`;
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.storyContainer;
        container.scrollTop = container.scrollHeight;
      });
    }
  }
};
</script>

<style scoped>
.story-relay-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.box-card {
  width: 100%;
  max-width: 1200px;
  margin: auto;
}

.story-container {
  height: 600px;
  width: 900px;
  overflow-y: auto;
}

.input-area {
  margin-top: 20px;
}

.story-part {
  margin: 10px 0;
  padding: 10px;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.user-part {
  background-color: #e6f7ff;
}

.ai-part {
  background-color: #f9f9f9;
}

.timestamp {
  display: block;
  text-align: right;
  color: #999;
  font-size: 12px;
  margin-top: 5px;
}
</style>