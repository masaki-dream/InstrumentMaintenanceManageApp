# 🎸 Instrument Maintenance Management App

音楽機材（ギター・ピアノ等）のメンテナンス業務を管理する  
SPA + REST API構成のWebアプリケーションです。

---

# 📌 開発背景

私はアコースティックギターを5年間、ピアノを数ヶ月演奏してきました。

しかし、

- いつ弦交換をしたのか
- いつ掃除をしたのか

を記録しておらず、管理が曖昧になっていることに不満を感じました。

この経験から、  
「音楽機材メンテナンス管理アプリ」を開発しました。

---

# 🔥 このアプリでアピールしているポイント

### ✅ RESTful API設計
- 名詞ベースのリソース設計
- HTTPメソッドの適切な使い分け
- ステータスコード設計の明確化

---

### ✅ 業務状態遷移をAPIで表現
単純なCRUDではなく、業務イベントをAPIとして設計。

例：
```md
### 状態遷移API

```http
POST /api/instruments/{id}/maintenances/start
→ 状態を 未メンテナンス → メンテナンス中 に遷移

```http
POST /api/instruments/{id}/maintenances/complete
→ 状態を メンテナンス中 → メンテナンス終了 に遷移
