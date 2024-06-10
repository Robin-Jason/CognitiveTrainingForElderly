<template>
  <div id="app">
    <main>
      <div class="form">
        <h2>Log in</h2>
        <el-form :model="loginForm" :rules="rules" ref="form" class="login-form">
          <el-form-item prop="username">
            <div class="input-field">
              <i class="fas fa-user"></i>
              <el-input type="text" v-model="loginForm.username" placeholder="Username"></el-input>
            </div>
          </el-form-item>
          <el-form-item prop="password">
            <div class="input-field">
              <i class="fas fa-lock"></i>
              <el-input type="password" v-model="loginForm.password" placeholder="Password" :show-password="showPassword"></el-input>
              <i class="showHidePassword" @click="togglePasswordVisibility"></i>
            </div>
          </el-form-item>
          <div class="checkbox-text">
            <div class="checkbox-content">
              <el-checkbox v-model="rememberMe">Remember me</el-checkbox>
            </div>
            <a href="#" class="forgot-password">Forgot password?</a>
          </div>
          <div class="button">
            <el-button type="primary" @click="do_login" :loading="loading">Log in</el-button>
          </div>
          <p>Don't have an account yet? <a href="#" @click.prevent="goToRegister">Sign up</a></p>
        </el-form>
      </div>
    </main>
  </div>
</template>

<script>
import { login } from "@/api/login";
import { setToken } from "@/utils/auth";

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      rememberMe: false,
      showPassword: false,
      loading: false
    };
  },
  methods: {
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
    do_login() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          // 登录处理逻辑中增加网络请求
          login(this.loginForm.username, this.loginForm.password)
              .then(res => {
                console.log(res); // 打印后端返回的数据
                setToken(res.data.accessToken); // res.data.accessToken是从后端接口返回的token
                this.$router.push({ path: '/' });
              })
              .catch(() => {
                // 登录失败，显示错误提示
                this.$message.error('用户名或密码错误');
                this.loading = false;
              });
        }
      });
    },
    goToRegister() {
      this.$router.push({ path: '/register' });
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
  bottom: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.1));
  backdrop-filter: blur(5px); /* 添加这一行来实现模糊效果 */
}

header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  padding: 20px 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 99;
}

@media screen and (max-width: 990px) {
  .logo img {
    display: none;
  }
  .navigation {
    margin: 0 auto;
  }
}

@media screen and (max-width: 500px) {
  .logo img {
    display: none;
  }
  .navigation {
    margin: 0 auto;
    display: flex;
    flex-direction: column;
  }
  .navigation a {
    margin-top: 10px;
  }
}

.logo {
  user-select: none;
}

.navigation a {
  position: relative;
  font-size: 1.1em;
  color: #ffa500;
  text-decoration: none;
  font-weight: 500;
  margin-left: 20px;
}

.navigation a::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -6px;
  width: 100%;
  height: 3px;
  background: #333;
  transform-origin: right;
  transform: scaleX(0);
  transition: transform 0.5s;
}

.navigation a:hover::after {
  transform: scaleX(1);
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
.checkbox-content label,
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
input[type="password"] {
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

.form .checkbox-text {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20px;
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