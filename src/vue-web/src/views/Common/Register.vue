<template>
  <div id="app">
    <main>
      <div class="form">
        <h2>注册</h2>
        <el-form :model="registerForm" :rules="rules" ref="form" class="register-form">
          <el-form-item prop="username">
            <div class="input-field">
              <i class="fas fa-user"></i>
              <el-input type="text" v-model="registerForm.username" placeholder="用户名"></el-input>
            </div>
          </el-form-item>
          <el-form-item prop="email">
            <div class="input-field">
              <i class="fas fa-envelope"></i>
              <el-input type="email" v-model="registerForm.email" placeholder="邮箱"></el-input>
            </div>
          </el-form-item>
          <el-form-item prop="password">
            <div class="input-field">
              <i class="fas fa-lock"></i>
              <el-input type="password" v-model="registerForm.password" placeholder="密码" :show-password="showPassword"></el-input>
              <i class="showHidePassword" @click="togglePasswordVisibility"></i>
            </div>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <div class="input-field">
              <i class="fas fa-lock"></i>
              <el-input type="password" v-model="registerForm.confirmPassword" placeholder="确认密码" :show-password="showPassword"></el-input>
              <i class="showHidePassword" @click="togglePasswordVisibility"></i>
            </div>
          </el-form-item>
          <div class="button">
            <el-button type="primary" @click="do_register" :loading="loading">注册</el-button>
          </div>
          <p>已有账号？<a href="/login">登录</a></p>
        </el-form>
      </div>
    </main>
  </div>
</template>

<script>
import { register } from "@/api/register";

export default {
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }, { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }]
      },
      showPassword: false,
      loading: false
    };
  },
  methods: {
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
    do_register() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.registerForm.password !== this.registerForm.confirmPassword) {
            this.$message.error('两次输入的密码不一致');
            return;
          }
          this.loading = true;
          register(this.registerForm.username, this.registerForm.password, this.registerForm.email)
              .then(() => {
                this.$message.success('注册成功');
                this.$router.push({ path: '/login' });
              })
              .catch(() => {
                this.$message.error('注册失败，请重试');
                this.loading = false;
              });
        }
      });
    }
  }
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap");

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Poppins", sans-serif;
}

html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

#app {
  height: 100%;
}

main {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: url(../../assets/LoginBg.png) no-repeat center center fixed;
  background-size: cover;
  margin: 0;
  padding: 0;
}

main::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;background: linear-gradient(transparent, rgba(0, 0, 0, 0.1));
  backdrop-filter: blur(5px); /* 添加这一行来实现模糊效果 */
}

.form {
  position: relative;
  padding: 40px 30px;
  background-color: rgba(46, 43, 43, 0.222);
  box-shadow: 0 0 10px;
  border-radius: 20px;
}

.form h2,
p,
.input-field::placeholder {
  color: #fff;
}

.form a {
  color: #fff;
}

.input-field {
  position: relative;
  height: 50px;
  width: 100%;
  margin-top: 30px;
}

.input-field input {
  position: absolute;
  height: 100%;
  width: 100%;
  padding: 0 35px;
  border: none;
  outline: none;
  font-size: 16px;
  border-bottom: 2px solid #ccc;
  border-top: 2px solid transparent;
  transition: all 0.2s ease;
}

input[type="text"],
input[type="password"],
input[type="email"] {
  background-color: transparent;
}

.input-field i {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  color: #ffffff;
  font-size: 23px;
  transition: all 0.2s ease;
}

.input-field i.showHidePassword {
  right: 0;
  cursor: pointer;
  padding: 10px;
}

.form .button button {
  height: 50px;
  width: 350px;
  display: block;
  margin-right: auto;
  margin-left: auto;
  align-items: center;
  border: none;
  color: #fff;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 1px;
  border-radius: 6px;
  background-color: #ffa500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.button button:hover {
  background-color: #333;
}
</style>