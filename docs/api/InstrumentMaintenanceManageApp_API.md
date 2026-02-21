# API設計

## 1. 本APIの目的

本APIは、音楽機材（ギター・ピアノ等）のメンテナンス業務を
Webアプリケーションとして管理することを目的とする。

本設計では以下を重視している。

- RESTful API設計
- HTTPメソッド・ステータスコードの適切な使い分け
- 冪等性を意識した設計
- 業務状態遷移を表現するAPI
- 単純なCRUDに留まらない設計

## 2. システム構成

本システムは、フロントエンドとバックエンドを完全に分離した
SPA + REST API 構成を採用する。

- フロントエンド（画面層）
  - Vue 3（Vite）
  - REST APIを介してバックエンドと通信する
  - 画面表示・ユーザー操作・API呼び出しを担当
  - 業務ロジックは持たない

- バックエンド（API層）
  - Spring Boot
  - RESTful APIとして業務処理を提供
  - JSON形式でレスポンスを返却する
  - 業務ロジックはService層に集約する

- インフラ
  - Render
  - Spring Bootアプリケーションをデプロイ

本アプリケーションのバックエンドは、
API提供に特化したControllerのみを持つ。

- ApiController
  - RESTful APIを提供
  - 業務処理を実行し、JSONを返却する

画面描画を目的としたWebControllerは実装しない。

## 3. 業務ドメイン概要

本アプリでは、機材（Instrument）は以下の状態を持つ。

- 未メンテナンス
- メンテナンス中
- メンテナンス完了

メンテナンスは「履歴」として管理され、
1つの機材に対して複数回実施される。

## 3.1 メンテナンス状態遷移

未メンテナンス
  ↓ 開始
メンテナンス中
  ↓ 完了
メンテナンス完了

## 4. リソース設計方針

- 名詞を基本としたリソース設計を行う
- フロントエンド（Vue）から操作しやすい粒度でAPIを定義する
- 業務上の操作は「状態遷移」として表現する
- 単純な更新（PUT）ではなく、業務イベントをAPI化する

## 5. API一覧

| 機能 | メソッド | エンドポイント |
|----|----|----|
| 機材一覧取得 | GET | /api/instruments |
| 機材詳細取得 | GET | /api/instruments/{id} |
| メンテナンス開始 | POST | /api/instruments/{id}/maintenance/start |
| メンテナンス完了 | POST | /api/instruments/{id}/maintenance/complete |

## 6. メンテナンス開始API

### 概要
指定した機材のメンテナンスを開始する。
本APIはVueフロントエンドから呼び出されることを想定している。

### エンドポイント
POST /api/instruments/{instrumentId}/maintenance/start

### リクエスト
- Path
  - instrumentId: 機材ID

### 正常レスポンス
- 201 Created

### 異常レスポンス
- 404 Not Found（機材が存在しない）
- 409 Conflict（既にメンテナンス中）

### 冪等性
- 既にメンテナンス中の場合、状態は変化しない

## 7. ステータスコード設計方針

- 正常系
  - 200 OK
  - 201 Created
  - 204 No Content
- クライアントエラー
  - 400 Bad Request
  - 401 Unauthorized
  - 403 Forbidden
  - 404 Not Found
  - 409 Conflict

## 8. 例外処理設計

- 業務例外
  - 業務ルール違反（メンテナンス中に再開始など）
- システム例外
  - 想定外エラー

例外は @ControllerAdvice により共通処理を行う。

## 9. 認証・認可

- JWTによる認証を採用
- AuthorizationヘッダにBearerトークンを指定
- 権限
  - ADMIN
  - USER

