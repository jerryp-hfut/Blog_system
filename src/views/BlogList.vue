<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '../api'
import { useRouter } from 'vue-router'

const blogs = ref([])
const categories = ref([])
const tags = ref([])
const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

const selectedCategory = ref('')
const selectedTag = ref('')
const search = ref('')
const page = ref(1)
const pageSize = 5

const fetchBlogs = async () => {
  const res = await api.get('/blogs')
  blogs.value = res.data
}
const fetchMeta = async () => {
  const [catRes, tagRes] = await Promise.all([
    api.get('/categories'),
    api.get('/tags')
  ])
  categories.value = catRes.data
  tags.value = tagRes.data
}

const filteredBlogs = computed(() => {
  let list = blogs.value
  if (selectedCategory.value) {
    list = list.filter(b => b.category && b.category.id == selectedCategory.value)
  }
  if (selectedTag.value) {
    list = list.filter(b => b.tags && b.tags.some(t => t.id == selectedTag.value))
  }
  if (search.value) {
    list = list.filter(b => b.title.includes(search.value))
  }
  return list
})

const pagedBlogs = computed(() => {
  const start = (page.value - 1) * pageSize
  return filteredBlogs.value.slice(start, start + pageSize)
})
const totalPages = computed(() => Math.ceil(filteredBlogs.value.length / pageSize))

onMounted(() => {
  fetchBlogs()
  fetchMeta()
})
</script>

<template>
  <div class="home-container">
    <div class="home-header">
      <img v-if="user" :src="user.avatar ? (user.avatar.startsWith('http') ? user.avatar : '/avatar/' + user.avatar.replace('/avatar/', '')) : 'https://ui-avatars.com/api/?name=' + user.username + '&background=007aff&color=fff'" class="avatar" />
      <div class="welcome">
        <span v-if="user">欢迎回来，<b>{{ user.username }}</b>！</span>
        <span v-else>欢迎来到博客系统！</span>
      </div>
    </div>
    <div class="filters">
      <input v-model="search" class="input search" placeholder="搜索标题..." />
      <select v-model="selectedCategory" class="input">
        <option value="">全部分类</option>
        <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
      </select>
      <select v-model="selectedTag" class="input">
        <option value="">全部标签</option>
        <option v-for="tag in tags" :key="tag.id" :value="tag.id">{{ tag.name }}</option>
      </select>
    </div>
    <div v-if="pagedBlogs.length === 0" class="empty">暂无博客</div>
    <div class="blog-list">
      <div v-for="blog in pagedBlogs" :key="blog.id" class="blog-card" @click="router.push(`/blog/${blog.id}`)">
        <img v-if="blog.coverImage" :src="blog.coverImage.startsWith('http') ? blog.coverImage : '/cover/' + blog.coverImage.replace('/cover/', '')" 
             class="blog-cover" style="width:100%;max-height:220px;object-fit:cover;border-radius:10px;margin-bottom:10px;box-shadow:0 2px 8px #e0e0e0;" />
        <div class="blog-title">{{ blog.title }}</div>
        <div class="blog-meta">
          <span class="author">
            <img v-if="blog.user && blog.user.avatar" :src="blog.user.avatar.startsWith('http') ? blog.user.avatar : '/avatar/' + blog.user.avatar.replace('/avatar/', '')" class="avatar" style="width:22px;height:22px;margin-right:4px;vertical-align:middle;" />
            <img v-else :src="'https://ui-avatars.com/api/?name=' + (blog.user?.username || '未知') + '&background=007aff&color=fff'" class="avatar" style="width:22px;height:22px;margin-right:4px;vertical-align:middle;" />
            {{ blog.user?.username || '未知' }}
          </span>
          <span v-if="blog.category" class="category">📁 {{ blog.category.name }}</span>
          <span v-if="blog.tags && blog.tags.length" class="tags">
            <span v-for="tag in blog.tags" :key="tag.id" class="tag">#{{ tag.name }}</span>
          </span>
        </div>
        <div class="blog-content">{{ blog.content.slice(0, 80) }}{{ blog.content.length > 80 ? '...' : '' }}</div>
      </div>
    </div>
    <div v-if="totalPages > 1" class="pagination">
      <button :disabled="page===1" @click="page--">上一页</button>
      <span>第 {{ page }} / {{ totalPages }} 页</span>
      <button :disabled="page===totalPages" @click="page++">下一页</button>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  max-width: 800px;
  margin: 40px auto;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(0,0,0,0.08);
  padding: 32px 32px 24px 32px;
  font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;
}
.home-header {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 18px;
}
.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  box-shadow: 0 2px 8px 0 rgba(0,122,255,0.08);
}
.welcome {
  font-size: 20px;
  color: #222;
  font-weight: 500;
}
.filters {
  display: flex;
  gap: 12px;
  margin-bottom: 18px;
}
.input {
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  padding: 8px 12px;
  font-size: 15px;
  background: #fafbfc;
  transition: border 0.2s;
  outline: none;
}
.input:focus {
  border: 1.5px solid #007aff;
  background: #fff;
}
.search {
  flex: 1;
}
.blog-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.blog-card {
  background: #f8fafd;
  border-radius: 14px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.04);
  padding: 18px 22px 14px 22px;
  cursor: pointer;
  transition: box-shadow 0.2s, background 0.2s;
  border: 1px solid #f0f0f0;
}
.blog-card:hover {
  background: #f1f6ff;
  box-shadow: 0 4px 16px 0 rgba(0,122,255,0.10);
}
.blog-title {
  font-size: 20px;
  font-weight: 600;
  color: #222;
  margin-bottom: 8px;
}
.blog-meta {
  font-size: 13px;
  color: #888;
  margin-bottom: 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
.author, .category, .tags, .time {
  display: inline-block;
}
.tag {
  background: #eaf4ff;
  color: #007aff;
  border-radius: 6px;
  padding: 1px 7px;
  margin-right: 4px;
  font-size: 12px;
}
.blog-content {
  color: #444;
  font-size: 15px;
  margin-top: 2px;
}
.empty {
  text-align: center;
  color: #bbb;
  font-size: 18px;
  margin: 40px 0;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 18px;
  margin-top: 24px;
}
.pagination button {
  background: #f1f3f4;
  border: none;
  border-radius: 8px;
  padding: 6px 18px;
  font-size: 15px;
  color: #007aff;
  cursor: pointer;
  transition: background 0.2s;
}
.pagination button:disabled {
  color: #aaa;
  background: #f8fafd;
  cursor: not-allowed;
}
</style>
