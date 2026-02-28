<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios'

const router = useRouter()

const name = ref('')
const maintenanceType = ref('')
const description = ref('')
const message = ref(null)

const showMessage = (text, type = "success") => {
  message.value = { text, type }
  setTimeout(() => {
    if (message.value?.text === text) message.value = null
  }, 5000)
}

const createInstrument = async () => {
  try {
    const token = localStorage.getItem("token")
    if (!token) {
      router.push('/')
      return
    }

    // ちょいバリデーション（空登録を防ぐ）
    if (!name.value.trim()) {
      showMessage("楽器名を入力してください", "error")
      return
    }

    await api.post(
      "/api/instruments",
      {
        name: name.value,
        maintenanceType: maintenanceType.value,
        description: description.value
      },
      {
        headers: { Authorization: `Bearer ${token}` }
      }
    )

    showMessage("登録しました", "success")

    // すぐ遷移してOKならこのまま。少し見せたいなら setTimeout で遅らせてもOK
    router.push('/instruments')

  } catch (error) {
    console.error(error)
    showMessage("登録失敗", "error")
  }
}
</script>

<template>
  <main class="create">
    <div class="form">
      <h2 class="title">楽器登録</h2>

      <div v-if="message" :class="['alert', message.type]">
        {{ message.text }}
      </div>

      <input
        v-model="name"
        placeholder="楽器名（例：アコギ・ピアノ等）"
      />

      <input
        v-model="maintenanceType"
        placeholder="メンテ内容（例：弦交換、修理等）"
      />

      <input
        v-model="description"
        placeholder="メモ（例：備考・状態等）"
      />

      <div class="actions">
        <button class="btn primary" @click="createInstrument">登録</button>
        <button class="btn ghost" @click="router.push('/menu')">メニューへ戻る</button>
      </div>
    </div>
  </main>
</template>

<style scoped>
/* 画面内のフォーム幅をログイン画面に揃える */
.form{
  width: min(720px, 100%);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

/* 見出し（統一） */
.title{
  text-align: center;
  margin: 0 0 8px;
  font-size: 22px;
  font-weight: 800;
  letter-spacing: .02em;
}

/* input（ログインと統一） */
.form input{
  width: 100%;
  padding: 12px 12px;
  border-radius: 10px;
  border: 1px solid rgba(0,0,0,0.12);
  outline: none;
}

/* ボタン行 */
.actions{
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 6px;
}

/* ボタン（一覧の統一ボタン寄せ） */
.btn{
  width: 160px;
  margin: 0;
  padding: 10px 14px;
  border-radius: 10px;
  border: 1px solid rgba(255,255,255,0.16);
  background: rgba(255,255,255,0.92);
  color: #111;
  cursor: pointer;
  transition: transform .12s ease, box-shadow .12s ease, filter .12s ease;
  box-shadow: 0 8px 20px rgba(0,0,0,0.18);
  font-weight: 600;
  letter-spacing: .02em;
}

.btn:hover{
  transform: translateY(-1px);
  filter: brightness(1.02);
  box-shadow: 0 10px 26px rgba(0,0,0,0.22);
}

.btn:active{ transform: translateY(0); }

.btn.primary{
  background: rgba(255,255,255,0.96);
}

.btn.ghost{
  background: transparent;
  color: rgba(231,234,240,0.92);
  border: 1px solid rgba(255,255,255,0.18);
  box-shadow: none;
}

/* アラート（登録/一覧と統一） */
.alert {
  padding: 12px 16px;
  margin: 6px 0 0;
  border-radius: 10px;
  font-weight: 700;
  font-size: 14px;
  animation: fadeIn 0.3s ease-in-out;
}

.success {
  background-color: #e6f7ed;
  color: #1e7e34;
  border: 1px solid #28a745;
}

.error {
  background-color: #fdecea;
  color: #b02a37;
  border: 1px solid #dc3545;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-4px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>