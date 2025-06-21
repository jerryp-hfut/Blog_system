<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'

const route = useRoute()
const blog = ref(null)
const comments = ref([])
const commentContent = ref('')
const errorMsg = ref('')
const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

const fetchBlog = async () => {
  const res = await api.get(`/blogs/${route.params.id}`)
  blog.value = res.data
}
const fetchComments = async () => {
  const res = await api.get('/comments')
  comments.value = res.data.filter(c => c.blog?.id == route.params.id)
}
const submitComment = async () => {
  errorMsg.value = ''
  if (!user.value) {
    errorMsg.value = '请先登录后评论！'
    return
  }
  if (!commentContent.value) {
    errorMsg.value = '评论内容不能为空！'
    return
  }
  try {
    await api.post('/comments', {
      blog: { id: blog.value.id },
      user: { id: user.value.id },
      content: commentContent.value
    })
    commentContent.value = ''
    fetchComments()
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || '评论失败'
  }
}
const likeBlog = async () => {
  try {
    const res = await api.post(`/blogs/${blog.value.id}/like-user`, { userId: user.value.id })
    if (res.data.likes !== undefined) {
      blog.value.likes = res.data.likes
    } else if (res.data.message) {
      alert(res.data.message)
    }
  } catch (e) {
    alert(e?.response?.data?.message || '点赞失败')
  }
}
onMounted(() => {
  fetchBlog()
  fetchComments()
})
</script>

<template>
  <div v-if="!blog">加载中...</div>
  <div v-else>
    <img v-if="blog.coverImage" :src="blog.coverImage.startsWith('http') ? blog.coverImage : '/cover/' + blog.coverImage.replace('/cover/', '')"
         style="width:100%;max-height:320px;object-fit:cover;border-radius:12px;margin-bottom:18px;box-shadow:0 2px 8px #e0e0e0;" />
    <h2>{{ blog.title }}</h2>
    <div style="color: #888;">作者：{{ blog.user?.username || '未知' }} | 分类：{{ blog.category?.name || '无' }}
      <span v-if="user && user.username" style="margin-left:2em;cursor:pointer;color:#007aff;user-select:none;" @click="likeBlog">👍 {{ blog.likes || 0 }}</span>
      <span v-else style="margin-left:2em;color:#bbb;">👍 {{ blog.likes || 0 }}</span>
    </div>
    <div style="margin: 1em 0; text-align: left;">{{ blog.content }}</div>
    <div>
      <strong>标签：</strong>
      <span v-for="tag in blog.tags || []" :key="tag.id" style="margin-right: 8px;">{{ tag.name }}</span>
    </div>
    <hr />
    <h3>评论区</h3>
    <div v-if="comments.length === 0">暂无评论</div>
    <ul>
      <li v-for="c in comments" :key="c.id">
        <b>{{ c.user?.username || c.user?.id }}</b>：{{ c.content }}
        <span style="color:#888; margin-left:1em;">{{ c.createTime ? c.createTime.replace('T',' ').slice(0,19) : '' }}</span>
      </li>
    </ul>
    <div style="margin-top:1em">
      <textarea v-model="commentContent" rows="3" style="width:100%" placeholder="写下你的评论..." :disabled="!user || !user.username"></textarea>
      <button @click="submitComment" :disabled="!user || !user.username">发表评论</button>
      <span v-if="errorMsg" style="color:red">{{ errorMsg }}</span>
    </div>
  </div>
</template>
