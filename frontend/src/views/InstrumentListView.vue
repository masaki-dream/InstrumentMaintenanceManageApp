<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios'

const router = useRouter()
const instruments = ref([])
const message = ref(null)
const editingId = ref(null)

const editName = ref('')
const editMaintenanceType = ref('')
const editDescription = ref('')

const showMessage = (text, type = "success") => {
  message.value = { text, type }
  setTimeout(() => {
    if (message.value?.text === text) message.value = null
  }, 5000)
}

const statusLabel = (status) => {
  switch (status) {
    case "NOT_MAINTAINED": return "未メンテナンス"
    case "MAINTAINING": return "メンテナンス中"
    case "COMPLETED": return "完了"
    default: return status
  }
}

const statusClass = (status) => {
  switch (status) {
    case "NOT_MAINTAINED": return "s-not"
    case "MAINTAINING": return "s-doing"
    case "COMPLETED": return "s-done"
    default: return ""
  }
}

const fetchInstruments = async () => {
  const token = localStorage.getItem("token")
  if (!token) {
    router.push('/')
    return
  }

  try {
    const response = await api.get("/api/instruments", {
      headers: { Authorization: `Bearer ${token}` }
    })
    instruments.value = response.data
  } catch (error) {
    console.error(error)
    showMessage("取得失敗", "error")
  }
}

const deleteInstrument = async (id) => {
  const token = localStorage.getItem("token")
  if (!window.confirm("削除しますか？")) return

  try {
    await api.delete(`/api/instruments/${id}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    showMessage("削除成功", "success")
    await fetchInstruments()
  } catch (error) {
    console.error(error)
    showMessage("削除失敗", "error")
  }
}

const startEdit = (instrument) => {
  editingId.value = instrument.id
  editName.value = instrument.name
  editMaintenanceType.value = instrument.maintenanceType || ''
  editDescription.value = instrument.description || ''
}

const updateInstrument = async () => {
  const token = localStorage.getItem("token")

  try {
    await api.put(`/api/instruments/${editingId.value}`, {
      name: editName.value,
      maintenanceType: editMaintenanceType.value,
      description: editDescription.value
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })

    showMessage("更新成功", "success")
    editingId.value = null
    await fetchInstruments()
  } catch (error) {
    console.error(error)
    showMessage("更新失敗", "error")
  }
}

const logout = () => {
  localStorage.removeItem("token")
  router.push('/')
}

onMounted(fetchInstruments)
</script>

<template>
  <main class="page">
    <div class="head">
      <h2 class="title">楽器一覧</h2>

      <div class="toolbar">
        <button class="btn" @click="router.push('/instruments/new')">楽器登録へ</button>
        <button class="btn" @click="router.push('/menu')">メニューへ戻る</button>
        <button class="btn danger" @click="logout">ログアウト</button>
      </div>
    </div>

    <div v-if="message" :class="['alert', message.type]">
      {{ message.text }}
    </div>

    <div v-if="instruments.length === 0" class="empty">
      まだ楽器がありません。右上の「楽器登録へ」から追加できます。
    </div>

    <ul class="list" v-else>
      <li v-for="instrument in instruments" :key="instrument.id" class="item">
        <!-- 編集中 -->
        <div v-if="editingId === instrument.id" class="edit">
          <div class="edit-grid">
            <input v-model="editName" placeholder="楽器名" />
            <input v-model="editMaintenanceType" placeholder="メンテ内容" />
            <input v-model="editDescription" placeholder="メモ" />
          </div>

          <div class="actions">
            <button class="btn primary" @click="updateInstrument">保存</button>
            <button class="btn ghost" @click="editingId = null">キャンセル</button>
          </div>
        </div>

        <!-- 通常表示 -->
        <div v-else class="row">
          <div class="left">
            <div class="name">{{ instrument.name }}</div>
            <div class="meta">
              <span class="chip" :class="statusClass(instrument.status)">
                {{ statusLabel(instrument.status) }}
              </span>
              <span v-if="instrument.maintenanceType" class="muted">・{{ instrument.maintenanceType }}</span>
            </div>
          </div>

          <div class="actions">
            <button class="btn" @click="router.push(`/instruments/${instrument.id}`)">詳細</button>
            <button class="btn" @click="startEdit(instrument)">更新</button>
            <button class="btn danger" @click="deleteInstrument(instrument.id)">削除</button>
          </div>
        </div>
      </li>
    </ul>
  </main>
</template>

<style scoped>
.page{
  width: 100%;
}

.head{
  display:flex;
  align-items:flex-start;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 10px;
}

.title{
  margin: 0;
  font-size: 22px;
  letter-spacing: .02em;
}

.toolbar{
  display:flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-end;
}

/* --- list card --- */
.list{
  list-style: none;
  padding: 0;
  margin: 14px 0 0;
  display:flex;
  flex-direction: column;
  gap: 12px;
}

.item{
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.10);
  border-radius: 14px;
  padding: 14px 14px;
  box-shadow: 0 10px 26px rgba(0,0,0,0.20);
}

.row{
  display:flex;
  align-items:center;
  gap: 14px;
}

.left{
  min-width: 0;
  flex: 1;
}

.name{
  font-size: 18px;
  font-weight: 700;
  letter-spacing: .02em;
}

.meta{
  margin-top: 6px;
  display:flex;
  align-items:center;
  gap: 8px;
  color: rgba(231,234,240,0.85);
}

.muted{
  opacity: .75;
  font-size: 12px;
}

/* status chips */
.chip{
  display:inline-flex;
  align-items:center;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  border: 1px solid rgba(255,255,255,0.12);
  background: rgba(255,255,255,0.06);
}
.s-not{ background: rgba(192,138,90,0.18); }
.s-doing{ background: rgba(90,160,192,0.16); }
.s-done{ background: rgba(120,200,140,0.14); }

/* actions */
.actions{
  display:flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

/* buttons unified */
.btn{
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

.btn:active{
  transform: translateY(0);
}

.btn.primary{
  background: rgba(255,255,255,0.96);
}

.btn.ghost{
  background: transparent;
  color: rgba(231,234,240,0.92);
  border: 1px solid rgba(255,255,255,0.18);
  box-shadow: none;
}

.btn.danger{
  border-color: rgba(255,80,80,0.35);
}

/* edit mode */
.edit-grid{
  display:grid;
  grid-template-columns: 1fr;
  gap: 10px;
  margin-bottom: 12px;
}

.edit-grid input{
  width: 100%;
  padding: 12px 12px;
  border-radius: 10px;
}

/* alerts (統一：他画面と同じ感じ) */
.alert {
  padding: 12px 16px;
  margin: 14px 0 6px;
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

.empty{
  margin-top: 14px;
  padding: 16px 14px;
  border-radius: 12px;
  border: 1px dashed rgba(255,255,255,0.18);
  color: rgba(231,234,240,0.85);
  background: rgba(255,255,255,0.03);
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-4px); }
  to { opacity: 1; transform: translateY(0); }
}

/* responsive: actions wrap nicely */
@media (max-width: 720px){
  .head{
    flex-direction: column;
    align-items: stretch;
  }
  .toolbar{
    justify-content: flex-start;
  }
  .row{
    flex-direction: column;
    align-items: stretch;
  }
  .actions{
    justify-content: flex-start;
  }
}
</style>