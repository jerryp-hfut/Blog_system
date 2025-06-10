<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const categories = ref([])
const name = ref('')
const errorMsg = ref('')

const fetchCategories = async () => {
  const res = await api.get('/categories')
  categories.value = res.data
}

const addCategory = async () => {
  errorMsg.value = ''
  if (!name.value) return
  try {
    await api.post('/categories', { name: name.value })
    name.value = ''
    fetchCategories()
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || '添加失败'
  }
}

onMounted(fetchCategories)
</script>

<template>
  <div class="cat-container">
    <h2>分类管理</h2>
    <div class="cat-list">
      <div v-for="cat in categories" :key="cat.id" class="cat-card">
        <span class="cat-name">{{ cat.name }}</span>
      </div>
    </div>
    <form class="cat-form" @submit.prevent="addCategory">
      <input v-model="name" class="input" placeholder="新分类名" />
      <button class="add-btn" type="submit">添加分类</button>
    </form>
    <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
  </div>
</template>

<style scoped>
.cat-container {
  max-width: 400px;
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
.cat-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 18px;
}
.cat-card {
  background: #f8fafd;
  border-radius: 10px;
  box-shadow: 0 1px 4px 0 rgba(0,0,0,0.04);
  padding: 8px 18px;
  font-size: 15px;
  color: #007aff;
  border: 1px solid #f0f0f0;
}
.cat-name {
  font-weight: 500;
}
.cat-form {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}
.input {
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  padding: 8px 12px;
  font-size: 15px;
  background: #fafbfc;
  flex: 1;
  outline: none;
  transition: border 0.2s;
}
.input:focus {
  border: 1.5px solid #007aff;
  background: #fff;
}
.add-btn {
  background: linear-gradient(90deg,#007aff 0%,#34c759 100%);
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 8px 18px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 2px 8px 0 rgba(0,122,255,0.08);
  transition: background 0.2s;
}
.add-btn:hover {
  background: linear-gradient(90deg,#005ecb 0%,#28a745 100%);
}
.error-msg {
  color: #ff3b30;
  margin-top: 10px;
  text-align: center;
  font-size: 15px;
}
</style>
