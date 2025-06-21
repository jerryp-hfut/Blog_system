<script setup>
import { ref } from 'vue'
import api from '../api'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const email = ref('')
const errorMsg = ref('')
const router = useRouter()

const register = async () => {
  errorMsg.value = ''
  try {
    await api.post('/users/register', {
      username: username.value,
      password: password.value,
      email: email.value
    })
    router.push('/login')
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || '注册失败'
  }
}
</script>

<template>
  <div class="auth-container">
    <h2>注册</h2>
    <form class="auth-form" @submit.prevent="register">
      <div class="form-group">
        <label>用户名：</label>
        <input v-model="username" required class="input" placeholder="请输入用户名" />
      </div>
      <div class="form-group">
        <label>邮箱：</label>
        <input v-model="email" required type="email" class="input" placeholder="请输入邮箱" />
      </div>
      <div class="form-group">
        <label>密码：</label>
        <input v-model="password" type="password" required class="input" placeholder="请输入密码" />
      </div>
      <button type="submit" class="submit-btn">注册</button>
      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
    </form>
  </div>
</template>

<style scoped>
.auth-container {
  max-width: 400px;
  margin: 60px auto 0 auto;
  background: #f7fafd;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(0,0,0,0.10);
  padding: 36px 32px 28px 32px;
  font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;
}
h2 {
  text-align: center;
  font-weight: 700;
  color: #1a2233;
  margin-bottom: 28px;
  letter-spacing: 1px;
}
.auth-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
label {
  font-size: 15px;
  color: #3a3a3a;
  margin-bottom: 2px;
}
.input {
  border: 1.5px solid #e0e6ed;
  border-radius: 10px;
  padding: 10px 14px;
  font-size: 16px;
  background: #f4f7fa;
  color: #222;
  transition: border 0.2s, box-shadow 0.2s;
  outline: none;
}
.input:focus {
  border: 1.5px solid #007aff;
  background: #fff;
  box-shadow: 0 0 0 2px #e3f0ff;
}
.submit-btn {
  background: linear-gradient(90deg,#007aff 0%,#34c759 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 12px 0;
  font-size: 17px;
  font-weight: 700;
  cursor: pointer;
  margin-top: 10px;
  box-shadow: 0 2px 8px 0 rgba(0,122,255,0.08);
  transition: background 0.2s, box-shadow 0.2s;
}
.submit-btn:hover {
  background: linear-gradient(90deg,#005ecb 0%,#28a745 100%);
  box-shadow: 0 4px 16px 0 rgba(0,122,255,0.12);
}
.error-msg {
  color: #ff3b30;
  margin-top: 10px;
  text-align: center;
  font-size: 15px;
  font-weight: 600;
}
.auth-link {
  text-align: center;
  margin-top: 18px;
  color: #555;
  font-size: 15px;
}
.auth-link a {
  color: #007aff;
  text-decoration: underline;
  margin-left: 4px;
  transition: color 0.2s;
}
.auth-link a:hover {
  color: #005ecb;
}
</style>
