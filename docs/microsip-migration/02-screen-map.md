# 畫面對照（截圖 + 要留／要藏）

主文檔：[`../microsip-like-linphone.md`](../microsip-like-linphone.md)  
功能表：[`01-feature-matrix.md`](01-feature-matrix.md)

## Phase 2 狀態

- [x] MicroSIP 關鍵截圖已放入 `screenshots/`
- [x] 各主畫面元素已拆解（要留／可弱化）
- [x] 導覽決策已填
- [x] 帳號「編輯帳戶／新增帳戶」截圖（`07`／`08`）
- [x] 全局「設定」截圖（`09-settings.png`）
- [x] **範圍凍結**：不再擴 Phase 2 截圖／需求；其餘選單（含右鍵）第一期不做  
- [x] 快速鍵已記錄為 P2（`10-shortcuts.png`），第一期不做

截圖清單：

| 檔名 | 內容 |
|------|------|
| `screenshots/01-incoming-and-dialer-and-calling.png` | 來電對話框 + 被叫端撥號頁（有來電提示）+ 主叫端撥號中 |
| `screenshots/02-in-call-connected.png` | 雙端已連線通話中（Hold／結束／轉接） |
| `screenshots/03-main-dialer-idle.png` | 空閒撥號頁 + 線上狀態 |
| `screenshots/04-call-history.png` | 通話記錄列表 |
| `screenshots/05-contacts.png` | 聯絡人（空列表） |
| `screenshots/06-menu-contacts.png` | 聯絡人 + 主選單（帳戶／設定等） |
| `screenshots/07-edit-account.png` | 編輯帳戶完整表單（欄位清單） |
| `screenshots/08-add-account.png` | 新增帳戶表單（與編輯同欄位、空白預設） |
| `screenshots/09-settings.png` | 全局設定（音訊／編碼／視訊／網路等） |
| `screenshots/10-shortcuts.png` | 快速鍵（8 組標籤／號碼／類型／BLF） |

---

## 整體 IA（從截圖歸納）

MicroSIP 主體只有 **3 個 Tab** + 右上角選單 + 底列狀態：

```
[電話] [通話記錄] [聯絡人]                    [▼選單]
──────────────── 內容區 ────────────────
（搜尋列：紀錄／聯絡人頁）
[● 線上 / 通話狀態文字]              [分機 1000]
```

Android 目標 IA（第一期）：

```
Bottom/Top：電話(撥號) | 通話記錄 | 聯絡人 | （設定進選單）
通話全螢幕：來電 / 撥出 / 通話中
隱藏：Chat、Meetings 主導覽
```

---

## 畫面：主視窗／撥號（空閒）

- MicroSIP 截圖：`screenshots/03-main-dialer-idle.png`（亦見 `01-...` 中間窗）
- 本專案起點：`StartCallFragment` / `start_call_fragment.xml` / `start_call_numpad_bottom_sheet.xml`

### 版面結構（上→下）

1. Tab：電話 / 通話記錄 / 聯絡人  
2. 號碼輸入框（可下拉）  
3. 3×4 數字鍵盤（含字母提示）  
4. 輔助鍵：`R`（重撥／相關）、`+`、`C`（清除）  
5. 主操作列：視訊圖示 | **撥號** | 訊息圖示  
6. 揚聲器音量滑桿、麥克風音量滑桿  
7. 快捷開關：`DND` / `AA` / `CONF` / `REC`  
8. 狀態列：綠點「線上」+ 右下角帳號 `1000`

### 要保留的元素（P0／接近 MicroSIP）

- 撥號為首要 Tab／首屏可達  
- 號碼顯示列 + 數字鍵盤  
- 清除／刪除鍵  
- 中央大「撥號」按鈕  
- 底部註冊狀態（Online）+ 目前帳號／分機  
- （可選 P1）視訊撥打入口  

### 要隱藏／弱化的元素（第一期）

| 元素 | 建議 | 原因 |
|------|------|------|
| 訊息圖示 | 隱藏或弱化 | 對齊 F14：第一期不做／藏 Chat |
| CONF | 隱藏 | 對齊 F18：藏會議 |
| REC | 弱化／設定內 | 非 MicroSIP 主路徑必顯 |
| DND / AA | 可進設定或次要列 | AA=F17 P2；DND 可 P1 |
| 桌面式雙音量滑桿 | Android 用系統／通話中音訊選單代替 | 手機 UX 不同，不必 1:1 |
| `R` 鍵 | 可做成「重撥上一通」或省略 | 非 P0 |

### Android 適配備註

- 不要追求 Windows 窄窗像素級；保留「鍵盤為主、少分頁」密度即可  
- 音量：優先通話中切聽筒／免持，不必兩條常駐滑桿  
- 數字鍵通話中可兼 DTMF（MicroSIP 連線後鍵盤仍在）  

---

## 畫面：來電

- MicroSIP 截圖：`screenshots/01-incoming-and-dialer-and-calling.png`（左窗「來電」）
- 本專案起點：`IncomingCallFragment`

### 要保留的元素

- 標題／語意：來電  
- 大字顯示來電者（如 `1001`）  
- 顯示 From／To SIP 位址（可第二行較小字）  
- **接聽**、**拒絕**  
- （P1）接聽視訊  

### 要隱藏／弱化的元素

- 無特別要藏；保持按鈕少而大  

### Android 適配備註

- 必須處理通知／全螢幕 intent／鎖定畫面（Windows 無此問題）  
- 佈局可用全螢幕卡片，不必小對話框  

---

## 畫面：撥出中／通話中

- MicroSIP 截圖：  
  - 撥出：`01-...` 右窗（紅鈕 End + Calling）  
  - 已連線：`02-in-call-connected.png`（Hold | 結束 | Transfer）
- 本專案起點：`OutgoingCallFragment`、`ActiveCallFragment`、`TransferCallFragment`

### 狀態對照

| MicroSIP | UI 特徵 |
|----------|---------|
| 撥出中 | 主按鈕變紅「End／結束」；狀態「Calling…」；側鍵 Hold／Transfer 可能仍在 |
| 已連線 | 狀態「Connected／已連線 0:xx」；Hold + 紅結束 + Transfer；Mic 電平可動 |

### 要保留的元素（P0）

- 大顆紅色 **結束／掛斷**  
- **Hold／暫停**（對齊 F09；MicroSIP 通話中明確露出）  
- 狀態文字 + 時長  
- 對方號碼／名稱  
- 鍵盤仍可用（DTMF）  

### 要保留的元素（P1）

- **Transfer／轉接**（對齊 F10；圖示在紅鈕右側）  

### 要隱藏／弱化的元素

- CONF／訊息等同撥號頁決策  
- 桌面音量滑桿 → 改音訊裝置選單／系統音量  

### Android 適配備註

- MicroSIP 通話中仍留在「電話」Tab；Android 用 `CallActivity` 全螢幕更合適  
- Hold、Transfer 應在通話中主列可見，不要深藏選單（對齊截圖）  

---

## 畫面：通話紀錄

- MicroSIP 截圖：`screenshots/04-call-history.png`
- 本專案起點：`HistoryListFragment`、`HistoryFragment`

### 要保留的元素

- 列表欄位感：名字、號碼、時間、時長、結果／訊息  
- 方向圖示：去電／來電／未接（色箭頭）  
- 底部搜尋列  
- 底列：線上狀態 + 帳號  

### 互動期望

- 點一列可回撥（或帶入撥號列）  
- 選取高亮可有  

### 要隱藏／弱化的元素

- 不必強制五欄桌面表格；Android 可用兩行列表呈現同等資訊  

### Android 適配備註

- 現況 `main_nav_graph` startDestination = History → Phase 5 改為 Dialer 優先後，History 仍保留為 Tab  

---

## 畫面：通訊錄

- MicroSIP 截圖：`screenshots/05-contacts.png`、`06-menu-contacts.png`
- 本專案起點：`ContactsListFragment`、`ContactFragment`

### 要保留的元素

- 欄：名字、號碼、（訊息欄可弱化）  
- 底部搜尋  
- 底列狀態 + 帳號  
- 點聯絡人可撥號  

### 要隱藏／弱化的元素

- 「訊息」欄若導向 Chat → 第一期弱化  
- 空列表可接受，需有新增入口（可在選單或 FAB）  

### Android 適配備註

- 可沿用現有聯絡人，但導覽上與撥號、紀錄平級（三 Tab）  

---

## 畫面：主選單／帳號／設定

- MicroSIP 截圖：`screenshots/06-menu-contacts.png`、`screenshots/07-edit-account.png`、`screenshots/08-add-account.png`
- 本專案起點：`DrawerMenuFragment`、`AccountSettingsFragment`、`AccountSettingsViewModel`、`SettingsFragment`、`ui/assistant/ThirdPartySipAccountLoginFragment`

### 選單項目（截圖可見）

| MicroSIP 選單 | 第一期 |
|---------------|--------|
| 啟用 | 保留（帳號啟用／停用） |
| 編輯帳戶 | **P0** 保留 |
| 新增帳戶… | P1 保留 |
| 設定 | **P0** 保留 |
| 快速鍵 | **P2／延後**（見下方；非桌面鍵盤熱鍵，是快捷撥號列） |
| 永遠保持在最上層 | 不做（桌面專屬） |
| 匯出／匯入 | P2／延後 |
| 前往網站 | 可改成你的產品連結或隱藏 |
| 說明（含版本） | 保留 About |
| 離開 | Android 不需要同等項 |

### 新增／編輯帳戶表單（同一套 UI）

- 編輯：`07-edit-account.png`（已填 `1000` / `172.17.7.53`）  
- 新增：`08-add-account.png`（空白預設）  

**結論：新增與編輯共用同一表單**，Android 用同一頁 + `add|edit` 模式即可。

| 差異點 | 新增帳戶 | 編輯帳戶 |
|--------|----------|----------|
| 欄位集合 | 相同 | 相同 |
| 預設值 | 空；Transport=UDP；公眾位置=自動；註冊 300；Keep-Alive 15；加密／隱密號=關閉 | 帶入現有帳號 |
| 顯示密碼 | 有「顯示密碼」核取方塊 | 建議兩模式都做 |
| 移除帳戶 | **無** | **有** |
| 儲存／取消 | 有 | 有 |

#### 首層（P0：第一屏必顯）

| MicroSIP 欄位 | 必填感 | Android 對齊建議 |
|---------------|--------|------------------|
| 帳號名稱 | 常用 | 本機顯示用標籤 |
| SIP 主機 | 常用 | registrar／伺服器主機 |
| 使用者 * | 必填 | username |
| 網域 * | 必填 | domain |
| 密碼 + 顯示密碼 | 必填感 | password（可切顯示） |
| 傳送（Transport） | 常用 | 預設 UDP |
| 儲存／取消 | 必有 | 主按鈕 |
| 移除帳戶 | 僅編輯 | 編輯模式頁底危險操作 |

#### 次層（P1：進階摺疊）

| MicroSIP 欄位 | 新增預設 | 說明 |
|---------------|----------|------|
| SIP 代理 | 空 | outbound proxy |
| 登入（Login） | 空 | auth id；與使用者不同才填 |
| 顯示姓名 | 空 | Display name |
| 語音信箱號碼 | 空 | |
| 撥號前綴／撥號方案 | 空 | 企業撥號規則 |
| 隱藏來電顯示 | 關閉 | |
| 媒體加密 | 關閉 | SRTP 等 |
| 公眾位置 | 自動 | |
| 更新註冊（秒） | 300 | |
| 保持活躍（秒） | 15 | |
| 發布狀態 | 未勾 | presence |
| 允許 IP 覆寫 | 未勾 | |
| ICE | 未勾 | 對齊 F16 |
| 禁用會話計時器 | 未勾 | |

#### 不做或不放帳號頁（桌面／全局設定）

- 永遠最上層、離開  
- 編解碼清單、DND、AA、錄音 → 放**全局設定**第二層  
- 快速鍵 → 見下一節（P2）  

### 快速鍵（`10-shortcuts.png`，選單「快速鍵 Ctrl+S」）

這不是 Windows 系統熱鍵表，而是 **最多 8 組快捷鈕／速度撥號（可含 BLF）**。

| 欄位 | 說明 |
|------|------|
| 標籤 | 按鈕顯示名稱 |
| 號碼 | 要撥／監控的號碼 |
| 類型 | 預設「撥號」（截圖 1～8 皆為撥號） |
| 切換 | checkbox |
| BLF | Busy Lamp Field 狀態燈 |
| 啟用 | 是否啟用整組快速鍵 |
| 底部位置 | 快速鍵列放視窗底部 |

**第一期決策：不做／延後（P2）**

- 主路徑（撥號／來電／通話／紀錄／聯絡人／帳號）不依賴它  
- BLF 屬進階 PBX 場景，Android 要另做訂閱與 UI  
- 若以後要做，Android 可做成「常用聯絡人／速度撥號」列，不必 1:1 複製 8 槽桌面表  

**對應功能表：** 可增 F21「快速鍵／速度撥號／BLF」= P2。

### Android 實作提示

- **一個 Account 頁**：`mode=add|edit`；edit 才顯示「移除」  
- 第一期先做首層欄位，其餘進階摺疊  
- 對照 `AccountSettingsFragment`／`ThirdPartySipAccountLoginFragment` 往 MicroSIP 欄位收斂  
- 新增預設值對齊上表（UDP、300、15、加密關）  

### 全局設定（`09-settings.png`，選單「設定 Ctrl+P」）

本專案起點：`SettingsFragment`、`SettingsAdvancedFragment`、`SettingsAdvancedCallFragment`、`SettingsViewModel`。

MicroSIP 是**單頁密集表單**；Android 建議拆成分組頁／摺疊，不要一屏塞滿。

#### 首層／常用（P0～P1，第一期可露出）

| MicroSIP 項 | 截圖狀態／備註 | Android 建議 |
|-------------|----------------|--------------|
| 鈴聲 | 有路徑 + 音量滑桿 | 選系統／App 鈴聲 |
| 響鈴／喇叭／麥克風裝置 | 皆「預設值」 | 用系統預設；通話中再選 AudioDevice |
| 回音消除 EC | 已勾 | 對應 Core 音訊設定 |
| 禁用視訊 | 未勾 | 全局關視訊開關 |
| 自動接聽 | 「控制按鈕」（工具列 AA） | 對齊 F17；可放設定，主畫面可不露 |
| 拒接來電 | 「控制按鈕」 | 來電頁已有拒接即可 |
| DTMF | 自動 | 通常保持自動 |
| 呼叫等待 | 已勾 | 多通來電行為 |
| 處理 IP 變更 | 已勾 | 行動網切換有用 |
| 音訊編碼（啟用） | 目前 **PCMA / PCMU** | 對齊 F16；可進階頁管理 |
| 儲存／取消 | 有 | 必有 |

#### 進階（P1／P2，放「進階設定」）

| MicroSIP 項 | 備註 |
|-------------|------|
| 單呼叫模式 | 限制同時通話數 |
| 麥克風擴音／軟體等級調整 | 桌面音訊細節 |
| 可用編碼清單（Opus/G.722/G.729…）+ 啟用列表 | 雙欄搬移 UI → Android 用 checklist |
| VAD、Opus 2ch | |
| 攝影機、視訊編碼、H.264/H.263/VP8/VP9、視訊位元率 256 | 視訊進階 |
| 通話錄音路徑、MP3/WAV、REC | 錄音目錄選 Android 儲存位置 |
| 呼叫轉移（轉發） | 與通話中 Transfer 不同，屬無條件轉寄類 |
| 使用者目錄、預設列表動作 | 通訊錄整合 |
| 聲音事件、來電鈴聲行為 | |
| 來源埠／rport、RTP 埠範圍 | 網路進階 |
| DNS／DNS SRV、STUN 主機 | 網路；STUN 常與帳號 ICE 一起看 |
| 啟用記錄檔 | debug |
| 啟用本地帳戶 | |
| 禁用訊息 | **可直接用來達成第一期藏 Chat（F14）** |
| 禁用名稱查詢 | |

#### 不做或改用 Android 系統能力（桌面專屬）

| MicroSIP 項 | 處理 |
|-------------|------|
| 隨機彈出位置、多監視器 | 不做 |
| 支援耳機／媒體按鈕（桌面） | 改用 Android 音訊／藍牙路由 |
| 系統啟動時執行 | 可改「開機無」；Android 用開機廣播需謹慎，第一期不做 |
| 軟體更新（每週） | 改商店更新／自有更新策略 |
| 發送崩潰報告 | 可接你方崩潰蒐集，非必須 1:1 |
| 桌面絕對路徑錄音目錄 | 改 SAF／App 專屬目錄 |

#### 設定頁 Android 資訊架構建議

```
設定
 ├─ 聲音與鈴聲（P0）
 ├─ 通話行為（自動接聽、呼叫等待、單呼叫…）（P1）
 ├─ 音訊／視訊編碼（P1）
 ├─ 網路（STUN、埠、DNS）（P1）
 ├─ 錄音（P2）
 └─ 其他／關於
帳號相關 → 仍走「編輯帳戶」，不要重複塞進全局設定首屏
```

---

## 導覽決策（必填）— 已依截圖定案

| 項目 | 決定 | 備註 |
|------|------|------|
| 預設首頁 | **Dialer（電話）** | 對齊 MicroSIP 預設在「電話」Tab；現況 history 要改 |
| Chat 入口 | **隱藏** | 撥號列訊息圖示一併藏 |
| Meetings 入口 | **隱藏** | CONF 一併藏 |
| 撥號進入方式 | **主 Tab「電話」** | 等於 MicroSIP 三 Tab 的第一個 |
| 主 Tab 集合 | 電話 / 通話記錄 / 聯絡人 | 設定走選單／齒輪，不當第四主業務 Tab |
| 狀態列 | 需要 | 註冊狀態 + 目前分機／帳號常駐 |
| 通話中主按鈕 | 紅結束 + Hold + Transfer | Transfer 可標 P1，但位置對齊截圖 |

---

## 與功能表的對應（方便開做）

| 畫面決策 | 對應功能 ID |
|----------|-------------|
| 預設撥號 Tab | F19 |
| 藏 Chat／訊息鈕 | F14 |
| 藏 CONF／Meetings | F18 |
| 來電三鍵 | F05、F11(視訊接聽 P1) |
| 通話中 Hold／紅結束／轉接 | F08、F09、F10 |
| 紀錄列表回撥 | F13 |
| 聯絡人 | F12 |
| 線上狀態 | F02 |
| 編輯帳戶／設定選單 | F01、F16 |
| 全局設定（鈴聲、編碼、AA、STUN…） | F16、F17、部分通話行為 |
| 設定內「禁用訊息」 | 可支撐 F14 隱藏 Chat |

---

## Phase 2 完成後下一步

1. 勾 `03-mvp-checklist.md` 的 A 區準備項  
2. 進入 **Phase 3/4/5**：不必再挖 API；直接改導覽（F19）+ 藏入口（F18/F14）  
3. 建議第一個程式改動：**三 Tab IA + 預設進撥號頁**  

**Phase 2 結束。** 帳號／設定／主通話截圖已夠用；右鍵選單與更多選項第一期不追。下一步：實作導覽（F19）+ 藏 Chat／會議（F14／F18）。
