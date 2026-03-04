<script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import api from '@/api/axios'

  const route = useRoute()
  const router = useRouter()

  const instrument = ref(null)
  const message    = ref(null)

  const newType = ref('')
  const newPerformedAt = ref('') // input[type=datetime-local] 用（文字列）

  const pad2 = (n) => String(n).padStart(2, '0')

  // datetime-local 形式 "YYYY-MM-DDTHH:mm"
  const toDateTimeLocal = (d) =>
    `${d.getFullYear()}-${pad2(d.getMonth() + 1)}-${pad2(d.getDate())}T${pad2(d.getHours())}:${pad2(d.getMinutes())}`

  const normalizeDateTime = (v) => (v && v.length === 16 ? `${v}:00` : v)

  // 履歴管理で入力される日付の最小値は、2000年とする。
  const minDateTime = "2000-01-01T00:00"

  // 履歴管理で入力される日付の最大値は、今の時間 + 10分後 とする。
  const max = new Date()
  max.setMinutes(max.getMinutes() + 10)
  const maxDateTime = toDateTimeLocal(max)

  const showMessage = (text, type = "success") => {
    message.value = { text, type }
    setTimeout(() => {
      if (message.value?.text === text) {
        message.value = null
      }
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

  const fetchDetail = async () => {
    const token = localStorage.getItem("token")
    if (!token) {
      router.push('/')
      return
    }

    const id = route.params.id

    try {
      const response = await api.get(`/api/instruments/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      instrument.value = response.data
    } catch (error) {
      console.error(error)
      message.value = "詳細取得失敗"
    }
  }

  // 詳細画面の中でメンテナンス開始処理
  const startMaintenance = async () => {
    const token = localStorage.getItem("token")
    if (!token) { router.push('/'); return }

    // 簡易バリデーション（必須）
    if (!newType.value.trim()) { showMessage("メンテナンス内容を入力してください", "error"); return }
    if (!newPerformedAt.value) { showMessage("実施日を入力してください", "error"); return }

    try {
      await api.post(
        `/api/instruments/${route.params.id}/maintenances/start`,
        {
          type: newType.value,
          performedAt: normalizeDateTime(newPerformedAt.value),
        },
        { headers: { Authorization: `Bearer ${token}` } }
      )

      showMessage("メンテナンス開始しました", "success")
      await fetchDetail()
    } catch (error) {
      console.error(error)

      const serverMsg =
        error?.response?.data?.message ||
        error?.response?.data ||
        null

      showMessage(serverMsg ?? "開始失敗", "error")
    }
  }

  // 詳細画面の中でメンテナンス終了処理
  const completeMaintenance = async () => {
    const token = localStorage.getItem("token")
    if (!token) { router.push('/'); return }

    if (!newType.value.trim()) { showMessage("メンテナンス内容を入力してください", "error"); return }
    if (!newPerformedAt.value) { showMessage("実施日を入力してください", "error"); return }

    try {
      await api.post(
        `/api/instruments/${route.params.id}/maintenances/complete`,
        {
          type: newType.value,
          performedAt: normalizeDateTime(newPerformedAt.value),
        },
        { headers: { Authorization: `Bearer ${token}` } }
      )

      showMessage("メンテナンス完了しました", "success")
      await fetchDetail()
    } catch (error) {
      console.error(error)

      const serverMsg =
        error?.response?.data?.message ||
        error?.response?.data ||
        null

      showMessage(serverMsg ?? "完了失敗", "error")
    }
  }

  onMounted(fetchDetail)
</script>

<template>
  <main class="detail">
    <div class="wrap">
      <h2 class="title">機材詳細</h2>

      <!-- 上部アクション（統一ボタン） -->
      <div class="top-actions">
        <button class="btn ghost" @click="router.push('/instruments')">一覧へ戻る</button>
        <button class="btn ghost" @click="router.push('/menu')">メニューへ戻る</button>
      </div>

      <div v-if="message" :class="['alert', message.type]">
        {{ message.text }}
      </div>

      <div v-if="instrument" class="content">
        <!-- 概要 -->
        <div class="summary">
          <div class="kv">
            <div class="k">楽器</div>
            <div class="v">{{ instrument.name }}</div>
          </div>
          <div class="kv">
            <div class="k">状態</div>
            <div class="v">
              <span class="badge" :data-status="instrument.status">
                {{ statusLabel(instrument.status) }}
              </span>
            </div>
          </div>
        </div>

        <h3 class="section-title">メンテナンス履歴</h3>

        <!-- 入力カード（サブカード） -->
        <div class="history-card">
          <h4 class="history-title">メンテナンス入力（開始/完了で登録）</h4>

          <div class="form">
            <input v-model="newType" placeholder="例：弦交換、清掃" />
            <input
              v-model="newPerformedAt"
              type="datetime-local"
              :min="minDateTime"
              :max="maxDateTime"
            />
          </div>

          <div class="center-actions">
            <button
              v-if="instrument.status === 'NOT_MAINTAINED'"
              class="btn primary"
              @click="startMaintenance"
            >
              開始
            </button>

            <button
              v-else-if="instrument.status === 'MAINTAINING'"
              class="btn primary"
              @click="completeMaintenance"
            >
              完了
            </button>

            <button
              v-else
              class="btn primary"
              @click="startMaintenance"
            >
              再開
            </button>
          </div>
        </div>

        <!-- 履歴リスト -->
        <ul class="history-list" v-if="instrument.histories?.length">
          <li v-for="(h, index) in instrument.histories" :key="index" class="history-item">
            <span class="time">{{ h.performedAt }}</span>
            <span class="sep">/</span>
            <span class="type">{{ h.type }}</span>
          </li>
        </ul>

        <div v-else class="empty">
          まだ履歴がありません。
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
/* 全体フォーム幅（ログイン/登録と揃える） */
.wrap{
  width: min(860px, 100%);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.title{
  text-align: center;
  margin: 0 0 6px;
  font-size: 22px;
  font-weight: 800;
  letter-spacing: .02em;
}

.top-actions{
  display: flex;
  gap: 10px;
  justify-content: center;
  flex-wrap: wrap;
}

/* ボタン（統一） */
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

/* アラート（統一） */
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
  to   { opacity: 1; transform: translateY(0); }
}

/* 概要（ID/名前/状態） */
.summary{
  display: grid;
  grid-template-columns: 1fr;
  gap: 10px;
  margin-top: 4px;
}

.kv{
  display: grid;
  grid-template-columns: 90px 1fr;
  gap: 10px;
  align-items: center;
  padding: 10px 12px;
  border-radius: 12px;
  border: 1px solid rgba(255,255,255,0.10);
  background: rgba(0,0,0,0.08);
}

.k{
  color: rgba(231,234,240,0.75);
  font-weight: 700;
}

.v{
  font-weight: 700;
}

/* ステータスバッジ */
.badge{
  display: inline-flex;
  align-items: center;
  padding: 6px 10px;
  border-radius: 999px;
  border: 1px solid rgba(255,255,255,0.14);
  background: rgba(255,255,255,0.08);
  font-size: 12px;
  letter-spacing: .02em;
}

/* 状態ごとにほんのり色味（強すぎない） */
.badge[data-status="NOT_MAINTAINED"]{ background: rgba(255,255,255,0.08); }
.badge[data-status="MAINTAINING"]{ background: rgba(192,138,90,0.18); }
.badge[data-status="COMPLETED"]{ background: rgba(90,160,192,0.16); }

.section-title{
  margin: 10px 0 4px;
  font-size: 22px;
  font-weight: 800;
  text-align: center;
}

/* 入力サブカード（登録画面の雰囲気に寄せる） */
.history-card{
  border-radius: 14px;
  border: 1px solid rgba(255,255,255,0.14);
  background: rgba(0,0,0,0.10);
  padding: 16px;
  box-shadow: 0 10px 26px rgba(0,0,0,0.18);
  /* 追加：iOSのinput描画のはみ出しをカード内に収める */
  overflow: hidden;
}

.history-title{
  margin: 0 0 12px;
  font-size: 14px;
  font-weight: 800;
  color: rgba(231,234,240,0.92);
}

.form{
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: min(720px, 100%);
  margin: 0 auto;
}

/* input（統一） */
.form input{
  width: 100%;
  padding: 12px 12px;
  border-radius: 10px;
  border: 1px solid rgba(0,0,0,0.12);
  outline: none;

/* iOS/Safari対策：datetime-local が横幅を突き抜けるのを防ぐ */
  box-sizing: border-box;
  min-width: 0;
  max-width: 100%;
}

/* datetime-local のはみ出し対策（iOS対策強化） */
.form input[type="datetime-local"]{
  color-scheme: light;

  display: block;
  width: 100%;
  max-width: 100%;
  min-width: 0;

  box-sizing: border-box;
  -webkit-appearance: none;
}

.center-actions{
  display: flex;
  justify-content: center;
  margin-top: 14px;
}

/* 履歴表示 */
.history-list{
  margin: 8px 0 0;
  padding: 0;
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.history-item{
  display: flex;
  gap: 10px;
  align-items: center;
  padding: 12px 12px;
  border-radius: 12px;
  border: 1px solid rgba(255,255,255,0.10);
  background: rgba(0,0,0,0.06);
}

.time{
  font-variant-numeric: tabular-nums;
  color: rgba(231,234,240,0.86);
}
.sep{
  color: rgba(231,234,240,0.45);
}
.type{
  color: rgba(231,234,240,0.92);
  font-weight: 700;
}

.empty{
  margin-top: 8px;
  color: rgba(231,234,240,0.70);
  padding: 12px 10px;
  text-align: center;
  border-radius: 12px;
  border: 1px dashed rgba(255,255,255,0.18);
}

/* スマホ最適化 */
@media (max-width: 560px){
  .kv{ grid-template-columns: 70px 1fr; }
  .btn{ width: 150px; }
}
</style>