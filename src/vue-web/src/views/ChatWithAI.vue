<template>
  <div class="chat-page">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>与蓝心聊天</span>
          </div>
          <div class="messages" ref="messagesContainer">
            <el-scrollbar wrap-class="scrollbar-wrapper">
              <div v-for="(msg, index) in messages" :key="index" class="message-card" :class="{'user-message': msg.source === 'user', 'ai-message': msg.source === 'ai'}">
                <p>{{ msg.content }}</p>
                <span class="timestamp">{{ msg.timestamp }}</span>
              </div>
              <div v-if="loading" class="loading-message">
                <el-loading text="AI 正在输入..."></el-loading>
              </div>
            </el-scrollbar>
          </div>
          <div class="input-area">
            <el-input
                v-model="message"
                placeholder="输入消息..."
                @keyup.enter.native="sendMessage"
                class="input-with-select"
            >
              <el-button slot="append" icon="el-icon-send" @click="sendMessage">发送</el-button>
              <el-button slot="append" icon="el-icon-microphone" @click="startSpeechToText">语音</el-button>
            </el-input>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { sendMessage } from '@/api/chat'

export default {
  data() {
    return {
      message: '',
      messages: [],
      sessionId: this.generateSessionId(),
      recognition: null,
      loading: false,
    };
  },
  mounted() {
    if ('webkitSpeechRecognition' in window) {
      this.initSpeechRecognition();
    } else {
      console.warn('Web Speech API is not supported in this browser.');
    }
  },
  methods: {
    sendMessage() {
      if (this.message.trim() !== '') {
        const userMessage = { source: 'user', content: this.message, timestamp: this.getCurrentTimestamp() };
        this.messages.push(userMessage);
        const chatMessage = {
          content: this.message,
          sessionId: this.sessionId,
          queryParams: {}
        };
        this.message = ''; // Clear input after send
        this.scrollToBottom();
        this.sendToAI(chatMessage);
      }
    },
    async sendToAI(chatMessage) {
      this.loading = true;
      try {
        const response = await sendMessage(chatMessage.content, chatMessage.sessionId, chatMessage.queryParams);
        const aiMessage = { source: 'ai', content: response.data.content, timestamp: this.getCurrentTimestamp() };
        this.messages.push(aiMessage);
      } catch (error) {
        console.error('Error sending message to AI:', error);
        const aiMessage = { source: 'ai', content: '对不起，AI 服务暂时不可用。', timestamp: this.getCurrentTimestamp() };
        this.messages.push(aiMessage);
      } finally {
        this.loading = false;
        this.scrollToBottom();
      }
    },
    startSpeechToText() {
      if (this.recognition) {
        this.recognition.start();
      } else {
        alert('当前浏览器不支持语音输入功能');
      }
    },
    initSpeechRecognition() {
      this.recognition = new webkitSpeechRecognition();
      this.recognition.lang = 'zh-CN'; // 设置语言
      this.recognition.continuous = false;
      this.recognition.interimResults = false;

      this.recognition.onresult = (event) => {
        if (event.results.length > 0) {
          this.message = event.results[0][0].transcript;
          this.sendMessage();
        }
      };

      this.recognition.onerror = (event) => {
        console.error('Speech recognition error:', event);
        alert('语音识别出错，请重试');
      };

      this.recognition.onend = () => {
        console.log('Speech recognition service disconnected');
      };
    },
    generateSessionId() {
      return 'session-' + Math.random().toString(36).substr(2, 9);
    },
    getCurrentTimestamp() {
      const now = new Date();
      return now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer;
        container.scrollTop = container.scrollHeight;
      });
    }
  }
};
</script>

<style scoped>
.chat-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.box-card {
  width: 100%;
  max-width: 1200px; /* 增加最大宽度 */
  margin: auto;
}

.messages {
  height: 600px; /* 增加高度 */
  width: 900px;
  overflow-y: auto;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #e2e2e2;
  border-radius: 8px;
  margin-bottom: 10px;
}

.scrollbar-wrapper {
  height: 100%;
}

.message-card {
  margin: 5px 0;
  padding: 10px;
  border-radius: 15px;
  position: relative;
  max-width: 80%;
}

.user-message {
  background-color: #409eff;
  margin-left: auto;
  color: #fff;
  text-align: right;
}

.ai-message {
  background-color: #e2e2e2;
  margin-right: auto;
  color: #333;
}

.timestamp {
  display: block;
  font-size: 0.8em;
  color: #999;
  margin-top: 5px;
}

.loading-message {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  color: #409eff;
}

.input-area {
  display: flex;
  align-items: center;
}

.input-with-select {
  width: 100%;
}

.el-input .el-button + .el-button {
  margin-left: 5px;
}

.el-input .el-button {
  padding: 0 15px;
}

.el-input__inner {
  height: 40px;
  line-height: 40px;
}

.el-card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>