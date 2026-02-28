import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import MenuView from '../views/MenuView.vue'
import InstrumentListView from '../views/InstrumentListView.vue'
import InstrumentCreateView from '../views/InstrumentCreateView.vue'
import SignUpView from '../views/SignUpView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/signup', component: SignUpView },
  { path: '/menu', component: MenuView },
  { path: '/instruments', component: InstrumentListView },
  { path: '/instruments/new', component: InstrumentCreateView },
  { path: '/instruments/:id', component: () => import('../views/InstrumentDetailView.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token")

  // ログインとサインアップ以外のURL直打ちは入れないように制御する（menu画面等に直打ちで入られないように対策）
  if (to.path === '/' || to.path === '/signup') {
    next()
  } else {
    if (!token) {
      next('/')
    } else {
      next()
    }
  }
})

export default router