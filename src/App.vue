<script setup>
import { ref, computed } from 'vue'
import { RouterView, RouterLink, useRouter } from 'vue-router'

const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
const router = useRouter()

// 判断是否为管理员
const isAdmin = computed(() => user.value && user.value.role === 'admin')

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  user.value = null
  router.push('/login')
}
window.addEventListener('storage', () => {
  user.value = JSON.parse(localStorage.getItem('user') || 'null')
})

// 登录后自动刷新 user
if (window.localStorage) {
  window.addEventListener('storage', () => {
    user.value = JSON.parse(localStorage.getItem('user') || 'null')
  })
}

// 登录/注册后页面刷新时也同步 user
if (!user.value || !user.value.username) {
  user.value = JSON.parse(localStorage.getItem('user') || 'null')
}
</script>

<template>
  <nav style="margin-bottom: 2em;">
    <RouterLink to="/">博客列表</RouterLink>
    <span v-if="user && user.username"> | <RouterLink to="/create">发布博客</RouterLink></span>
    <span v-if="isAdmin"> | <RouterLink to="/categories">分类管理</RouterLink> | <RouterLink to="/tags">标签管理</RouterLink></span>
    <span v-if="user && user.username"> | <RouterLink to="/profile">个人中心</RouterLink></span>
    <span v-if="user && user.username">欢迎，{{ user.username }} <a href="#" @click.prevent="logout">退出</a></span>
    <RouterLink v-else to="/login">登录</RouterLink>
  </nav>
  <RouterView />
</template>

<style scoped>
nav a {
  margin: 0 8px;
  text-decoration: none;
  color: #646cff;
}
nav a.router-link-exact-active {
  font-weight: bold;
  color: #42b983;
}
</style>
