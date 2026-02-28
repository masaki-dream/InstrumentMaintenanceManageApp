<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios'

const router = useRouter()

const username = ref('')
const password = ref('')
const message = ref(null)

const showMessage = (text, type = "success") => {
  message.value = { text, type }

  setTimeout(() => {
    if (message.value?.text === text) message.value = null
  }, 5000)
}

const login = async () => {
  try {
    const response = await api.post("/api/auth/login", {
      username: username.value,
      password: password.value
    })

    localStorage.setItem("token", response.data.token)
    router.push('/menu')

  } catch (error) {
    localStorage.removeItem("token")
    showMessage("ログイン失敗", "error") // ← 統一
  }
}
</script>

<template>
  <div class="login-form">
    <h2 class="login-title">ログイン</h2>

    <div v-if="message" :class="['alert', message.type]">
      {{ message.text }}
    </div>

    <input v-model="username" placeholder="ユーザー名" />
    <input v-model="password" type="password" placeholder="パスワード" />

    <button @click="login">ログイン</button>

    <button class="signup" @click="router.push('/signup')">
      アカウント新規作成
    </button>
  </div>
</template>

<style scoped>
/* alert（他画面と同じ見た目＆5秒で消える） */
.alert {
  padding: 10px 12px;
  border-radius: 6px;
  font-weight: bold;
}

.success {
  background: #e6f7ed;
  color: #1e7e34;
  border: 1px solid #28a745;
}

.error {
  background: #fdecea;
  color: #b02a37;
  border: 1px solid #dc3545;
}

/* 既存 */
.login-form{
  width: min(520px, 100%);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.login-title{
  text-align: center;
  margin: 0 0 8px;
}

.login-form input{
  width: 100%;
  padding: 12px 12px;
  border-radius: 8px;
}

.login-form button{
  width: 160px;
  margin: 6px auto 0;
  padding: 10px 14px;
  border-radius: 8px;
  cursor: pointer;
}

.signup{
  margin-top: 16px;
  font-size: 14px;
  background: none;
  border: none;
  color: #007bff;
  cursor: pointer;
  width: auto;
  margin: 14px auto 0;
  padding: 0;
}
</style>