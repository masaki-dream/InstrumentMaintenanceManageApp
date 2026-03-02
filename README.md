# 🎸 Instrument Maintenance Management App

音楽機材（ギター・ピアノ等）のメンテナンス業務を管理する  
SPA + REST API構成のWebアプリケーションです。

![CI](https://github.com/<YOUR_GITHUB_ID>/<YOUR_REPOSITORY_NAME>/actions/workflows/ci.yml/badge.svg)

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

---



状態を直接更新するのではなく、  
「開始」「完了」という業務イベントとして表現。

### ✅ 冪等性を意識した設計
- 既にメンテナンス中の場合は状態を変更しない
- 409 Conflictで業務違反を明示

### ✅ 例外の共通ハンドリング
- `@ControllerAdvice` による統一エラーレスポンス
- 業務例外とシステム例外を分離

### ✅ JWTによるステートレス認証
- Bearerトークン方式
- セッション非依存設計

### ✅ CI導入
- GitHub Actionsによる自動ビルド
- mainブランチはCI成功時のみマージ可能

---

# 🏗 アーキテクチャ

## フロントエンド
- Vue 3（Vite）
- SPA構成
- 業務ロジックは持たない

## バックエンド
- Spring Boot
- Controller / Service / Repository のレイヤード構成
- Service層に業務ロジックを集約
- REST API専用設計（画面描画Controllerは未実装）

## データベース
- PostgreSQL 15
- ローカルはDocker
- 本番はRender Managed DB

## インフラ
- Render
- GitHub Actions（CI）

---

# 🧠 ドメイン設計

## 機材の状態

- NOT_MAINTAINED（未メンテナンス）
- IN_PROGRESS（メンテナンス中）
- COMPLETED（メンテナンス終了）

## 状態遷移

NOT_MAINTAINED  
↓ start  
IN_PROGRESS  
↓ complete  
COMPLETED  

メンテナンスは履歴として保持し、  
1つの機材に対して複数回実施可能。

---

# 📡 API一覧

| 機能 | メソッド | エンドポイント |
|------|----------|----------------|
| ユーザー登録 | POST | /api/users |
| ログイン | POST | /api/auth/login |
| 機材一覧取得 | GET | /api/instruments |
| 機材詳細取得 | GET | /api/instruments/{id} |
| 機材登録 | POST | /api/instruments |
| 機材更新 | PUT | /api/instruments/{id} |
| 機材削除 | DELETE | /api/instruments/{id} |
| メンテナンス開始 | POST | /api/instruments/{id}/maintenances/start |
| メンテナンス完了 | POST | /api/instruments/{id}/maintenances/complete |

---

# 🔐 認証
Authorization: Bearer <JWT>

---

# 🎯 技術スタック

- Vue 3
- Spring Boot
- PostgreSQL
- Docker
- Render
- JWT
- GitHub Actions

---

# 🚀 今後の改善予定

- 単体テスト・統合テスト拡充
- API設計書の強化
- システム構成図・ER図追加
- Docker本番完全対応
