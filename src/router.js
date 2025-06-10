import { createRouter, createWebHistory } from 'vue-router'
import BlogList from './views/BlogList.vue'
import BlogDetail from './views/BlogDetail.vue'
import BlogCreate from './views/BlogCreate.vue'
import CategoryList from './views/CategoryList.vue'
import TagList from './views/TagList.vue'
import CommentList from './views/CommentList.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Profile from './views/Profile.vue'

const routes = [
  { path: '/', component: BlogList },
  { path: '/blog/:id', component: BlogDetail },
  { path: '/create', component: BlogCreate },
  { path: '/categories', component: CategoryList, meta: { admin: true } },
  { path: '/tags', component: TagList, meta: { admin: true } },
  // { path: '/comments', component: CommentList, meta: { admin: true } }, // 注释掉评论管理页面
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/profile', component: Profile }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
