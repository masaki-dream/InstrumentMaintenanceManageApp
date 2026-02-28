<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goList = () => router.push('/instruments')
const goCreate = () => router.push('/instruments/new')

const logout = () => {
  localStorage.removeItem('token')
  router.push('/')
}
</script>

<template>
  <div class="menu-wrap">
    <h2 class="menu-title">メニュー</h2>
    <p class="menu-sub">やりたいことを選んでください。</p>

    <div class="menu-actions">
      <button class="menu-btn primary" @click="goList">
        楽器一覧
        <span class="hint">登録済みの機材を確認</span>
      </button>

      <button class="menu-btn" @click="goCreate">
        楽器登録
        <span class="hint">新しい機材を追加</span>
      </button>

      <button class="menu-btn danger" @click="logout">
        ログアウト
        <span class="hint">セッションを終了</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
/* カード内の中央寄せ（Loginと同じ方向性） */
.menu-wrap{
  width: min(640px, 100%);
  margin: 0 auto;
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.menu-title{
  margin: 0 0 4px;
  font-size: 28px;
  letter-spacing: .02em;
}

.menu-sub{
  margin: 0 0 18px;
  opacity: .85;
  font-size: 14px;
}

/* ボタンを縦に並べて、スマホでも押しやすく */
.menu-actions{
  display: grid;
  gap: 14px;
}

/* 共通ボタン（カード風） */
.menu-btn{
  width: 100%;
  padding: 18px 20px;
  border-radius: 14px;
  cursor: pointer;
  text-align: left;

  background: rgba(255,255,255,0.92);
  border: 1px solid rgba(255,255,255,0.15);
  box-shadow: 0 8px 22px rgba(0,0,0,0.25);

  font-weight: 600;
  font-size: 17px;
  letter-spacing: 0.06em;
  color: #111;

  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;

  transition: transform .12s ease, box-shadow .12s ease, filter .12s ease;
}

.menu-btn:hover{
  transform: translateY(-1px);
  box-shadow: 0 14px 30px rgba(0,0,0,0.28);
  filter: brightness(1.02);
}

.menu-btn:active{
  transform: translateY(0px);
  box-shadow: 0 10px 24px rgba(0,0,0,0.22);
}

/* 右側の説明文 */
.hint{
  font-weight: 400;
  font-size: 12px;
  opacity: .55;
}

/* 強調（一覧） */
.primary{
  border-color: rgba(192,138,90,0.65); /* ウッド寄り */
}

/* ログアウトだけ危険色 */
.danger{
  border-color: rgba(220,53,69,0.35);
}

/* スマホでは説明文を折り返し */
@media (max-width: 520px){
  .menu-btn{
    text-align: center;
    flex-direction: column;
    align-items: center;
  }
  .hint{
    white-space: normal;
  }
}
</style>