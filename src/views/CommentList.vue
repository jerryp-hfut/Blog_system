<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const comments = ref([])

const fetchComments = async () => {
  const res = await api.get('/comments')
  comments.value = res.data
}

onMounted(fetchComments)
</script>

<template>
  <div class="comment-container">
    <h2>评论管理</h2>
    <div v-if="comments.length === 0" class="empty">暂无评论</div>
    <div class="comment-list">
      <div v-for="c in comments" :key="c.id" class="comment-card">
        <div class="comment-content">{{ c.content }}</div>
        <div class="comment-meta">
          <span class="user">👤 {{ c.user?.username || c.user?.id }}</span>
          <span class="blog">📝 博客ID: {{ c.blog?.id }}</span>
          <span class="time">🕒 {{ c.createTime?.replace('T',' ') }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment-container {
  max-width: 600px;
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
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.comment-card {
  background: #f8fafd;
  border-radius: 12px;
  box-shadow: 0 1px 4px 0 rgba(0,0,0,0.04);
  padding: 14px 18px 10px 18px;
  border: 1px solid #f0f0f0;
}
.comment-content {
  font-size: 16px;
  color: #333;
  margin-bottom: 6px;
}
.comment-meta {
  font-size: 13px;
  color: #888;
  display: flex;
  gap: 18px;
  flex-wrap: wrap;
}
.user, .blog, .time {
  display: inline-block;
}
.empty {
  text-align: center;
  color: #bbb;
  font-size: 18px;
  margin: 40px 0;
}
</style>
