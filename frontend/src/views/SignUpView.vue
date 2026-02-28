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

const signUp = async () => {
  if (!username.value.trim() || !password.value.trim()) {
    showMessage("ユーザー名とパスワードを入力してください", "error")
    return
  }

  try {
    await api.post("/api/users", {
      username: username.value,
      password: password.value
    })

    showMessage("登録成功。ログインしてください。", "success")

    setTimeout(() => {
      router.push("/")
    }, 1000)

  } catch (error) {
    console.error(error)
    showMessage("登録に失敗しました", "error")
  }
}
</script>

<template>
  <div class="signup-form">
    <h2 class="signup-title">アカウント新規作成</h2>

    <div v-if="message" :class="['alert', message.type]">
      {{ message.text }}
    </div>

    <input v-model="username" placeholder="ユーザー名" />
    <input v-model="password" type="password" placeholder="パスワード" />

    <button @click="signUp">登録する</button>

    <button class="back" @click="router.push('/')">
      ログインへ戻る
    </button>
  </div>
</template>

<style scoped>
/* ====== ログイン画面と“同じスケール感”に揃える ====== */

.signup-form{
  width: min(520px, 100%);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.signup-title{
  text-align: center;
  margin: 0 0 8px;
}

/* input：ログインと同じ */
.signup-form input{
  width: 100%;
  padding: 12px 12px;
  border-radius: 8px;
}

/* 登録ボタン：ログインと同じ幅 */
.signup-form button{
  width: 160px;
  margin: 6px auto 0;
  padding: 10px 14px;
  border-radius: 8px;
  cursor: pointer;
}

/* 「ログインへ戻る」はリンク風（ログインの signup と同じ扱い） */
.back{
  background: none;
  border: none;
  color: #007bff;
  font-size: 14px;
  width: auto;
  margin: 14px auto 0;
  padding: 0;
}

/* メッセージ */
.alert{
  padding: 10px 12px;
  border-radius: 6px;
  font-weight: bold;
}

.success{
  background: #e6f7ed;
  color: #1e7e34;
}

.error{
  background: #fdecea;
  color: #b02a37;
}
</style>