# MVP 勾選清單（第一期）

主文檔：[`../microsip-like-linphone.md`](../microsip-like-linphone.md)  
**範圍已凍結** — 只做下方項目；快速鍵／BLF、右鍵選單、Chat、會議等第一期不做。

完成定義：設帳號 → 撥打 → 接聽 → 掛斷；三 Tab 電話／紀錄／聯絡人；與 MicroSIP 同帳號可對打。

## A. 準備

- [x] MicroSIP 已安裝並用測試 SIP 帳號打通
- [ ] 本 App 已編譯，同帳號打通
- [x] 已截 MicroSIP 關鍵畫面到 `screenshots/`
- [x] `01-feature-matrix.md` P0 列已填「是否已有／對應路徑」
- [x] `02-screen-map.md` 導覽決策已填
- [x] 範圍凍結（不再加第一期需求）

## B. 導覽（P0）

- [x] 撥號成為預設首頁，或首屏可一眼進入撥號（`startCallFragment` startDestination）
- [x] Chat 入口已隱藏（底欄移除 + `hideConversations=true`）
- [x] Meetings／Conference 入口已隱藏（底欄移除 + `hideMeetings=true`）
- [x] 主 Tab 僅：電話／通話記錄／聯絡人
- [x] Contacts → 可撥號（既有聯絡人詳情／點撥流程保留）
- [x] History → 可回撥（既有紀錄流程保留；FAB 進電話 Tab）

## C. 通話核心（P0）

- [ ] 撥號盤可撥出
- [ ] 來電可接聽／拒接
- [ ] 通話中靜音
- [ ] 通話中免持／聽筒切換
- [ ] 掛斷
- [ ] DTMF 可用
- [ ] Hold／恢復（通話中明顯露出）

## D. 帳號與狀態（P0）

- [x] 啟動不強制 Welcome／Login（直接進撥號；帳號從側欄配置）
- [x] 可新增／編輯第三方 SIP 帳號（首層：帳號名稱／SIP主機／使用者／網域／密碼／Transport；其餘摺疊）
- [x] 註冊成功／失敗狀態可見（側欄 AccountModel registrationState）
- [ ] 掉線有提示（至少基本）

## E. 通訊錄與紀錄（P0）

- [ ] 通訊錄列表可用
- [ ] 通話紀錄列表可用
- [ ] 回撥流程順暢

## F. P1（第二階段，不擋第一期）

- [ ] 通話轉接
- [ ] 視訊開關（若產品需要）
- [ ] 多帳號切換
- [ ] 進階網路／編解碼設定收斂到第二層
- [ ] 設定分組精簡對齊 `09-settings.png` 常用項

## 第一期明確不做

- [x] 快速鍵／BLF（F21）— 不做
- [x] Chat／訊息主路徑 — 隱藏
- [x] 會議／CONF — 隱藏
- [x] 繼續補 MicroSIP 次要截圖 — 停止

## G. 品牌（行為穩定後）

- [ ] 應用名稱／圖示
- [ ] 主題色與撥號鍵樣式
- [ ] About／說明頁
- [ ] 授權與歸屬聲明已檢查

## H. 測試

- [ ] 與 MicroSIP 對打通過
- [ ] 權限流程（麥克風、通知）通過
- [ ] 鎖定螢幕／背景來電行為可接受
- [ ] 回歸：註冊、撥出、接聽、掛斷各 3 次無崩潰
