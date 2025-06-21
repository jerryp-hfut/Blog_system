<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'
import { useRouter } from 'vue-router'

const title = ref('')
const content = ref('')
const categoryId = ref('')
const tagIds = ref([])
const categories = ref([])
const tags = ref([])
const router = useRouter()
const errorMsg = ref('')
const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
const coverImage = ref('')
const coverFile = ref(null)
const newTag = ref('')

const fetchMeta = async () => {
  // 获取分类
  const catRes = await api.get('/categories')
  categories.value = catRes.data
  // 获取标签
  const tagRes = await api.get('/tags')
  tags.value = tagRes.data
}

const onCoverChange = async (e) => {
  const file = e.target.files[0]
  if (file) {
    const form = new FormData()
    form.append('file', file)
    const res = await api.post('/blogs/cover', form, { headers: { 'Content-Type': 'multipart/form-data' } })
    coverImage.value = res.data.url
  }
}

const addTag = async () => {
  if (!newTag.value.trim()) return
  try {
    // 先请求后端创建标签
    const res = await api.post('/tags', { name: newTag.value.trim() })
    // 新标签加入本地tags列表并自动选中
    tags.value.push(res.data)
    tagIds.value.push(res.data.id)
    newTag.value = ''
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || '添加标签失败'
  }
}

const submit = async () => {
  errorMsg.value = ''
  if (!user.value) {
    router.push('/login')
    return
  }
  try {
    await api.post('/blogs', {
      title: title.value,
      content: content.value,
      category: categoryId.value ? { id: categoryId.value } : null,
      tags: tagIds.value.map(id => ({ id })),
      user: { id: user.value.id },
      coverImage: coverImage.value
    })
    router.push('/')
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || '发布失败，请检查必填项！'
  }
}

onMounted(fetchMeta)
</script>

<template>
  <div class="blog-create-container">
    <h2>发布博客</h2>
    <form class="blog-form" @submit.prevent="submit">
      <div class="form-group">
        <label>封面图片：</label>
        <input type="file" accept="image/*" @change="onCoverChange" />
        <img v-if="coverImage" :src="coverImage" style="max-width:100%;margin-top:8px;border-radius:10px;box-shadow:0 2px 8px #eee;" />
      </div>
      <div class="form-group">
        <label>标题：</label>
        <input v-model="title" required class="input" placeholder="请输入标题" />
      </div>
      <div class="form-group">
        <label>内容：</label>
        <textarea v-model="content" required rows="6" class="input textarea" placeholder="请输入内容"></textarea>
      </div>
      <div class="form-group">
        <label>分类：</label>
        <select v-model="categoryId" class="input">
          <option value="">请选择</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>标签：</label>
        <div class="tags">
          <span v-for="tag in tags" :key="tag.id" class="tag-item">
            <input
              type="checkbox"
              :value="tag.id"
              v-model="tagIds"
              :id="`tag-${tag.id}`"
              :aria-label="`Select ${tag.name} tag`"
            />
            <label :for="`tag-${tag.id}`">{{ tag.name }}</label>
          </span>
        </div>
        <div class="custom-tag-row">
          <input v-model="newTag" class="input" placeholder="自定义新标签" />
          <button type="button" class="submit-btn" @click="addTag">添加标签</button>
        </div>
      </div>
      <button type="submit" class="submit-btn">发布</button>
      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
    </form>
  </div>
</template>

<style scoped>
.blog-create-container {
  max-width: 500px;
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
  margin-bottom: 28px;
  letter-spacing: 1px;
}
.blog-form {
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
  color: #555;
  margin-bottom: 2px;
}
.input {
  color: #222;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  padding: 10px 14px;
  font-size: 16px;
  background: #fafbfc;
  transition: border 0.2s;
  outline: none;
}
.input:focus {
  border: 1.5px solid #007aff;
  background: #fff;
}
.textarea {
  min-height: 100px;
  resize: vertical;
}
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.tag-item {
  display: flex;
  align-items: center;
  background: #f1f3f4;
  border-radius: 8px;
  padding: 2px 10px 2px 4px;
  font-size: 14px;
  color: #333;
  box-shadow: 0 1px 2px 0 rgba(0,0,0,0.03);
}
.tag-item input[type="checkbox"] {
  accent-color: #007aff;
  margin-right: 4px;
}
.submit-btn {
  background: linear-gradient(90deg,#007aff 0%,#34c759 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 12px 0;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 10px;
  box-shadow: 0 2px 8px 0 rgba(0,122,255,0.08);
  transition: background 0.2s;
}
.submit-btn:hover {
  background: linear-gradient(90deg,#005ecb 0%,#28a745 100%);
}
.error-msg {
  color: #ff3b30;
  margin-top: 10px;
  text-align: center;
  font-size: 15px;
}
.new-tag {
  display: flex;
  align-items: center;
  gap: 10px;
}
.custom-tag-row {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-top: 8px;
}
</style>
