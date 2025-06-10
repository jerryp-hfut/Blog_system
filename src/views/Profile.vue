<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'
import { useRouter } from 'vue-router'

const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
const blogs = ref([])
const errorMsg = ref('')
const router = useRouter()

const fetchMyBlogs = async () => {
  if (!user.value) return
  const res = await api.get('/blogs')
  blogs.value = res.data.filter(b => b.user && b.user.id === user.value.id)
}

const delBlog = async (id) => {
  if (!confirm('确定要删除这篇博客吗？')) return
  try {
    await api.delete(`/blogs/${id}`)
    fetchMyBlogs()
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || '删除失败'
  }
}

const editing = ref(false)
const editUser = ref({ ...user.value })
const avatarUrl = ref('https://ui-avatars.com/api/?name=' + user.value.username + '&background=007aff&color=fff')
const avatarFile = ref(null)
const oldPassword = ref('')
const newPassword = ref('')
const resetMsg = ref('')
const resetMsgColor = ref('#888')

function startEdit() {
  editing.value = true
  editUser.value = { ...user.value }
}
function cancelEdit() {
  editing.value = false
}
function onAvatarChange(e) {
  const file = e.target.files[0]
  if (file) {
    avatarFile.value = file
    const reader = new FileReader()
    reader.onload = ev => {
      avatarUrl.value = ev.target.result
    }
    reader.readAsDataURL(file)
  }
}
async function saveEdit() {
  errorMsg.value = ''
  try {
    // 头像上传
    if (avatarFile.value) {
      const form = new FormData()
      form.append('file', avatarFile.value)
      form.append('userId', user.value.id)
      const res = await api.post('/users/avatar', form, { headers: { 'Content-Type': 'multipart/form-data' } })
      user.value.avatar = res.data.url
      avatarUrl.value = res.data.url
    }
    // 同步用户名和邮箱到后端
    const res2 = await api.put(`/users/${user.value.id}`, {
      ...user.value,
      username: editUser.value.username,
      email: editUser.value.email
    })
    user.value.username = res2.data.username
    user.value.email = res2.data.email
    localStorage.setItem('user', JSON.stringify(user.value))
    editing.value = false
    fetchMyBlogs() // 刷新我的博客作者名
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || '保存失败，请检查网络或后端日志！'
  }
}
async function resetPassword() {
  resetMsg.value = ''
  if (!oldPassword.value || !newPassword.value) {
    resetMsg.value = '请输入原密码和新密码'
    resetMsgColor.value = 'red'
    return
  }
  try {
    const res = await api.post('/users/reset-password', {
      userId: user.value.id,
      oldPassword: oldPassword.value,
      newPassword: newPassword.value
    })
    resetMsg.value = res.data.message || '密码重置成功'
    resetMsgColor.value = 'green'
    oldPassword.value = ''
    newPassword.value = ''
  } catch (e) {
    resetMsg.value = e?.response?.data?.message || '重置失败'
    resetMsgColor.value = 'red'
  }
}

onMounted(fetchMyBlogs)
</script>

<template>
  <div class="profile-container">
    <h2>个人中心</h2>
    <div class="user-info">
      <div class="avatar-wrap">
        <img :src="user.avatar ? user.avatar : avatarUrl" class="avatar" />
        <label v-if="editing" class="avatar-upload">
          <input type="file" accept="image/*" @change="onAvatarChange" style="display:none" />
          更换头像
        </label>
      </div>
      <div v-if="!editing">
        <div class="username">{{ user.username }}</div>
        <div class="email">{{ user.email }}</div>
        <button class="edit-btn" @click="startEdit">编辑资料</button>
      </div>
      <div v-else class="edit-form">
        <input v-model="editUser.username" class="input" placeholder="用户名" />
        <input v-model="editUser.email" class="input" placeholder="邮箱" />
        <div class="edit-actions">
          <button class="save-btn" @click="saveEdit">保存</button>
          <button class="cancel-btn" @click="cancelEdit">取消</button>
        </div>
        <div class="reset-password">
          <h4 style="margin:16px 0 6px 0;">重置密码</h4>
          <input v-model="oldPassword" class="input" type="password" placeholder="原密码" />
          <input v-model="newPassword" class="input" type="password" placeholder="新密码" />
          <button class="save-btn" @click="resetPassword">重置密码</button>
          <div v-if="resetMsg" :style="{color: resetMsgColor, marginTop: '6px'}">{{ resetMsg }}</div>
        </div>
      </div>
    </div>
    <h3 style="margin-top:32px;">我的博客</h3>
    <div v-if="blogs.length === 0" class="empty">暂无博客</div>
    <div class="blog-list">
      <div v-for="blog in blogs" :key="blog.id" class="blog-card">
        <div class="blog-title">{{ blog.title }}</div>
        <div class="blog-meta">
          <span class="time">🕒 {{ blog.createTime?.replace('T',' ') }}</span>
        </div>
        <div class="blog-content">{{ blog.content.slice(0, 80) }}{{ blog.content.length > 80 ? '...' : '' }}</div>
        <button class="del-btn" @click="delBlog(blog.id)">删除</button>
      </div>
    </div>
    <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 700px;
  margin: 40px auto;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(0,0,0,0.08);
  padding: 32px 32px 24px 32px;
  font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;
}
h2 {
  text-align: center;
  font-weight: 600;
  color: #222;
  margin-bottom: 24px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 10px;
}
.avatar-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 18px;
}
.avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  box-shadow: 0 2px 8px 0 rgba(0,122,255,0.08);
}
.avatar-upload {
  margin-top: 6px;
  font-size: 13px;
  color: #007aff;
  cursor: pointer;
  background: #f1f3f4;
  border-radius: 8px;
  padding: 2px 10px;
  box-shadow: 0 1px 2px 0 rgba(0,0,0,0.03);
  transition: background 0.2s;
}
.avatar-upload:hover {
  background: #eaf4ff;
}
.username {
  font-size: 18px;
  font-weight: 600;
  color: #222;
}
.email {
  font-size: 14px;
  color: #888;
}
.edit-btn {
  background: linear-gradient(90deg,#007aff 0%,#34c759 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 6px 18px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 8px;
  box-shadow: 0 2px 8px 0 rgba(0,122,255,0.08);
  transition: background 0.2s;
}
.edit-btn:hover {
  background: linear-gradient(90deg,#005ecb 0%,#28a745 100%);
}
.edit-form {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.edit-actions {
  display: flex;
  gap: 10px;
  margin-top: 6px;
}
.save-btn {
  background: linear-gradient(90deg,#34c759 0%,#007aff 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 6px 18px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 2px 8px 0 rgba(52,199,89,0.08);
  transition: background 0.2s;
}
.save-btn:hover {
  background: linear-gradient(90deg,#28a745 0%,#005ecb 100%);
}
.cancel-btn {
  background: #f1f3f4;
  color: #888;
  border: none;
  border-radius: 8px;
  padding: 6px 18px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.04);
  transition: background 0.2s;
}
.cancel-btn:hover {
  background: #eaf4ff;
}
.reset-password {
  margin-top: 24px;
  padding: 16px;
  background: #f8fafd;
  border-radius: 12px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.04);
}
.blog-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-top: 10px;
}
.blog-card {
  background: #f8fafd;
  border-radius: 14px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.04);
  padding: 18px 22px 14px 22px;
  border: 1px solid #f0f0f0;
  position: relative;
}
.blog-title {
  font-size: 18px;
  font-weight: 600;
  color: #222;
  margin-bottom: 6px;
}
.blog-meta {
  font-size: 13px;
  color: #888;
  margin-bottom: 6px;
}
.blog-content {
  color: #444;
  font-size: 15px;
  margin-top: 2px;
}
.del-btn {
  position: absolute;
  top: 18px;
  right: 22px;
  background: linear-gradient(90deg,#ff3b30 0%,#ff9500 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 4px 16px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 2px 8px 0 rgba(255,59,48,0.08);
  transition: background 0.2s;
}
.del-btn:hover {
  background: linear-gradient(90deg,#d70015 0%,#ff9500 100%);
}
.empty {
  text-align: center;
  color: #bbb;
  font-size: 18px;
  margin: 40px 0;
}
.error-msg {
  color: #ff3b30;
  margin-top: 10px;
  text-align: center;
  font-size: 15px;
}
</style>
