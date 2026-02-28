<script setup>
import { ref } from 'vue'
import axios from 'axios'

const username = ref('')
const password = ref('')
const result = ref('')
const instruments = ref([]) // ★ 一覧保存用

// ===== ログイン =====
const login = async () => {
  try {
    const response = await axios.post(
      'http://localhost:8080/api/auth/login',
      {
        username: username.value,
        password: password.value
      }
    )

    result.value = "ログイン成功"
    localStorage.setItem("token", response.data.token)

  } catch (error) {
    console.error(error)
  localStorage.removeItem("token") // token削除
  result.value = "ログイン失敗"
  }
}

// ===== 認証テスト =====
const callSecureApi = async () => {
  try {
    const token = localStorage.getItem("token")

    const response = await axios.get(
      "http://localhost:8080/api/test/hello",
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    )

    result.value = response.data

  } catch (error) {
    console.error(error)
    result.value = "認証付きAPI失敗"
  }
}

// ===== 楽器一覧取得 =====
const fetchInstruments = async () => {
  try {

    const token = localStorage.getItem("token")

    if (!token) {
      result.value = "ログインしてください"
      return
    }

    const response = await axios.get(
      "http://localhost:8080/api/instruments",
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    )

    instruments.value = response.data
    result.value = "一覧取得成功"

  } catch (error) {
    console.error(error)
    result.value = "一覧取得失敗"
  }
}
</script>

<template>
  <main>
    <h2>ログインテスト</h2>

    <input v-model="username" placeholder="username" />
    <br />
    <input v-model="password" type="password" placeholder="password" />
    <br />

    <button @click="login">ログイン</button>
    <button @click="callSecureApi">認証付きAPIを呼ぶ</button>
    <button @click="fetchInstruments">楽器一覧取得</button>

    <p>{{ result }}</p>

    <hr />

    <h3>楽器一覧</h3>
    <ul>
      <li v-for="instrument in instruments" :key="instrument.id">
        ID: {{ instrument.id }} |
        名前: {{ instrument.name }} |
        状態: {{ instrument.status }}
      </li>
    </ul>

  </main>
</template>