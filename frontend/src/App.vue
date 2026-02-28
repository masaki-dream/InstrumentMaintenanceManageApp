<script setup>
</script>

<template>
  <div class="app">
    <div class="shell">
      <header class="header">
        <div class="brand">
          <span class="logo">🎸</span>
          <span class="title">Music Gear Care</span>
        </div>
        <div class="subtitle">音楽機材を、いい状態で。</div>
      </header>

      <main class="card">
        <router-view />
      </main>

      <footer class="footer">
        <small>© Instrument Maintenance Manage</small>
      </footer>
    </div>
  </div>
</template>

<style>
/* 全ページ共通 */
:root{
  --bg0:#0f1115;
  --bg1:#151922;
  --card:#1b2130;
  --text:#e7eaf0;
  --muted:#aab2c0;
  --accent:#c08a5a;
  --border:rgba(255,255,255,0.10);
}

*{ box-sizing:border-box; }

html,body,#app{
  height:100%;
  margin:0;
  font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Yu Gothic", sans-serif;
  color:var(--text);
  background:
    radial-gradient(900px 500px at 20% 10%, rgba(192,138,90,0.18), transparent 55%),
    radial-gradient(900px 500px at 80% 20%, rgba(90,160,192,0.12), transparent 55%),
    linear-gradient(180deg, var(--bg0), var(--bg1));
}

a{ color:var(--accent); text-decoration:none; }
a:hover{ text-decoration:underline; }

/* 画面中央に全体ブロックを配置 */
.app{
  min-height:100vh;
  width:100%;
  display:flex;
  justify-content:center;
  align-items:center;
  padding:24px 16px;
}

/* コンテンツ全体の最大幅 */
.shell{
  width:min(720px, 100%);
  display:flex;
  flex-direction:column;
  gap:20px;
}

/* ヘッダー中央 */
.header{
  text-align:center;
}

.footer{
  width: 100%;
  text-align: center;
  color: var(--muted);
  font-size: 12px;
  margin-top: 12px;
}

.brand{
  display:flex;
  align-items:center;
  justify-content:center;
  gap:10px;
}

.logo{
  font-size:28px;
}

.title{
  font-size:24px;
  font-weight:800;
}

.subtitle{
  margin-top:6px;
  color:var(--muted);
  font-size:13px;
}

/* カード */
.card{
  width:100%;
  background:rgba(27,33,48,0.9);
  border:1px solid var(--border);
  border-radius:14px;
  padding:24px;
  box-shadow:0 12px 40px rgba(0,0,0,0.35);

  display:flex;
  justify-content:center;
}



</style>