// axios 实例及通用请求封装
import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token
  }
  return config
})

// 用户信息持久化扩展：自动补充 role 字段（仅演示，实际应后端返回）
// 若后端已返回 user.role，可删除此段
if (localStorage.getItem('user')) {
  const user = JSON.parse(localStorage.getItem('user'))
  // 只在 role 字段缺失时补充，且不覆盖后端返回的 role
  if (!user.role) {
    // 兼容“潘序”为管理员
    user.role = user.username === '潘序' ? 'admin' : 'user'
    localStorage.setItem('user', JSON.stringify(user))
  }
}

export default api
