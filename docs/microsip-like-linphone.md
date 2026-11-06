# 用 Linphone 做成接近 MicroSIP 的 Android Softphone — 做法文檔

> 專案：`Epro_LinPhone`  
> 目標：在現有 Linphone Android 上，做出「功能接近 MicroSIP、操作習慣接近 MicroSIP」的 softphone。  
> 配套填表：`docs/microsip-migration/`  
> **範圍狀態：已凍結（2026-11-06）** — Phase 1～2 完成；**B. 導覽已實作**（三 Tab + 預設撥號）。

---

## 0. 第一期範圍凍結（以截圖為準）

### 要做（In scope）

| 區塊 | 內容 |
|------|------|
| 導覽 | 三 Tab：電話／通話記錄／聯絡人；預設進撥號；藏 Chat、Meetings |
| 撥號／通話 | 撥出、來電接聽拒接、掛斷、靜音、免持、DTMF、Hold |
| 紀錄／聯絡人 | 列表、搜尋感、回撥／點撥 |
| 帳號 | 新增／編輯同表單；首層欄位（主機、使用者、網域、密碼、Transport…） |
| 狀態 | 線上／註冊狀態 + 目前分機 |
| 設定（精簡） | 鈴聲、編碼（至少 PCMA/PCMU）、必要通話／網路項；分組不要一頁塞滿 |

### 第二階段再做（P1，不擋主路徑）

- 轉接、視訊、多帳號、帳號／設定進階欄位摺疊對齊  

### 明確不做（Out of scope，第一期）

- 快速鍵／BLF（F21）  
- Chat／訊息、CONF／會議、REC 當主路徑  
- 桌面專屬：永遠最上層、開機啟動、多螢幕、系統熱鍵、匯出匯入  
- 再截更多 MicroSIP 選單／右鍵（除非實作中發現缺關鍵流程）  

詳見：`microsip-migration/01-feature-matrix.md`、`02-screen-map.md`、`03-mvp-checklist.md`。

---

## 1. 目標與原則

### 目標

- 用現有 `Epro_LinPhone` 改出接近 MicroSIP 的 **通話體驗** 與 **主流程 UI**
- MicroSIP 僅作 **產品／畫面／流程參考**，不是 API 來源

### 原則

| 要做 | 不要做 |
|------|--------|
| 功能對照表：MicroSIP 功能 → Linphone 能力 | 移植 MicroSIP MFC / C++ |
| UI／導覽改成撥號為中心 | 找「MicroSIP Android API」（不存在） |
| 一律用 **Linphone SDK（Core API）** | 第一期就追求像素級複製 Windows |
| 先 P0，再視覺品牌 | 同時大改 SDK 與全 UI |
| **範圍已凍結，先交付主路徑** | 邊做邊加次要選單功能 |

### 第一期非目標

- 完整複製 MicroSIP 每個進階選項
- 同步維護 Windows MicroSIP
- 會議、複雜群聊、Meeting、快速鍵／BLF（不需要可先隱藏／不做）

### 授權提醒

本專案基於 Linphone，受 **GPLv3 / 商業授權** 約束。改 UI、換品牌 **不代表** 可忽略授權義務。發佈前請核對 `README.md` 與 `LICENSE`。

---

## 2. 總體步驟（照順序）

```
① 安裝體驗 MicroSIP
② 填功能清單（01-feature-matrix）
③ 填畫面對照（02-screen-map）
④ 定 MVP（03-mvp-checklist）
⑤ 簡化導覽（撥號為主、弱化聊天／會議）
⑥ 逐頁改 UI（先 layout，少動業務邏輯）
⑦ 對齊通話流程與行為
⑧ 精簡帳號／設定
⑨ 品牌化（名稱、圖示、主題）
⑩ 對打測試與回歸
```

---

## 3. Phase 0：準備

### 3.1 兩邊都先打通

1. Windows 安裝官方 [MicroSIP](https://www.microsip.org/)，用真實 SIP 帳號註冊並通話成功  
2. 編譯執行本專案，**同一組 SIP 帳號** 註冊並通話成功  

### 3.2 官方 MicroSIP 源碼（僅供參考，非 Android API）

- 下載：https://www.microsip.org/source （GPL v2 源碼包）  
- 用途：對照它有哪些畫面、按鈕、設定項  
- **不要**把其中的介面接到 Android  

### 3.3 本倉庫配套目錄

```
docs/
  microsip-like-linphone.md          ← 本文
  microsip-migration/
    01-feature-matrix.md             ← 功能對照（請填）
    02-screen-map.md                 ← 畫面對照（請填）
    03-mvp-checklist.md              ← MVP 勾選（請填）
    screenshots/                     ← 自行放入 MicroSIP 截圖
```

---

## 4. Phase 1：功能清單

打開並填寫：[`microsip-migration/01-feature-matrix.md`](microsip-migration/01-feature-matrix.md)

**優先級**

- **P0**：沒有就不算「像電話」
- **P1**：MicroSIP 常用，第二階段補
- **P2**：可延後或隱藏

填表時在「Linphone 對應」欄寫本專案路徑（Fragment / ViewModel），不要寫 MicroSIP 的 C++ 函式名。

---

## 5. Phase 2：畫面對照（本專案實際路徑）

| MicroSIP 畫面 | 期望體驗 | 本專案起點（Kotlin） | 主要 Layout |
|---------------|----------|----------------------|-------------|
| 主視窗／撥號 | 開 App 偏撥號、少干擾 | `ui/main/history/fragment/StartCallFragment.kt` | `res/layout/start_call_fragment.xml`、`start_call_numpad_bottom_sheet.xml` |
| 通話紀錄 | 列表回撥 | `ui/main/history/fragment/HistoryListFragment.kt`、`HistoryFragment.kt` | `history_list_fragment.xml` |
| 通訊錄 | 點一下可撥 | `ui/main/contacts/fragment/ContactsListFragment.kt`、`ContactFragment.kt` | `contacts_list_fragment.xml` |
| 來電 | 大接聽／拒接 | `ui/call/fragment/IncomingCallFragment.kt` | （call 相關 layout） |
| 撥出中 | 顯示對方、可取消 | `ui/call/fragment/OutgoingCallFragment.kt` | |
| 通話中 | 靜音／免持／DTMF／轉接／掛斷 | `ui/call/fragment/ActiveCallFragment.kt` | |
| 轉接 | 選人或輸入號碼 | `ui/call/fragment/TransferCallFragment.kt` | `call_transfer_fragment.xml` |
| 帳號登入／設定 | 欄位少、好填 | `ui/assistant/*`、`ui/main/settings/fragment/AccountSettingsFragment.kt`、`AccountProfileFragment.kt` | assistant / account 相關 layout |
| 設定 | 精簡 | `ui/main/settings/fragment/SettingsFragment.kt` | `settings` 相關 |
| 聊天（可選） | 第一期可藏 | `ui/main/chat/fragment/ConversationsListFragment.kt` | |
| 會議（可藏） | 第一期建議藏 | `ui/main/meetings/fragment/MeetingsListFragment.kt`、`ui/call/conference/*` | |

導覽總圖：

- 主流程：`res/navigation/main_nav_graph.xml`  
  - **目前預設首頁**：`app:startDestination="@id/historyListFragment"`（通話紀錄，不是撥號）  
  - 撥號頁：`startCallFragment`（多半從 history 再進去）  
- 通話流程：`res/navigation/call_nav_graph.xml`、`ui/call/CallActivity.kt`  
- 入口 Activity：`ui/main/MainActivity.kt`  

截圖與備註填：[`microsip-migration/02-screen-map.md`](microsip-migration/02-screen-map.md)

---

## 6. Phase 3：對到 Linphone API（概念）

實作查 **Linphone SDK**，不是 MicroSIP：

| 能力 | 方向（概念） | 本專案可先搜 |
|------|----------------|--------------|
| 註冊／帳號 | `Core`、`Account`、`AccountParams` | `Assistant*`、`AccountSettings*`、`coreContext` |
| 撥打 | invite / Call API | `StartCallViewModel`、`NewCallFragment` |
| 接聽／掛斷 | `call.accept()` / `terminate()` | `IncomingCallFragment`、`ActiveCallFragment` |
| 靜音 | mic mute | `CallActivity` / call ViewModel |
| 揚聲器 | `AudioDevice` | `AudioDevicesMenuDialogFragment` |
| Hold | `pause` / `resume` | Active call 相關 |
| 轉接 | transfer API | `TransferCallFragment` |
| DTMF | `sendDtmf` | 通話中小鍵盤 |
| 通訊錄 | `Friend` + `contactsManager` | `ContactsListFragment`、`coreContext.contactsManager` |
| 紀錄 | `CallLog` | `HistoryListFragment` |
| 註冊狀態 | Account registration listener | Main / account UI |

**規則：** 每做一個功能，在功能表填「用哪個現有 ViewModel／API」。

---

## 7. Phase 4：MVP 範圍

勾選進度：[`microsip-migration/03-mvp-checklist.md`](microsip-migration/03-mvp-checklist.md)

### 必須有（P0）

1. 設定一個 SIP 帳號，並顯示 Online／Offline  
2. 主路徑能快速進入撥號盤  
3. 撥出、來電、通話中基本控制（靜音、免持、掛斷）  
4. 通訊錄點撥、紀錄回撥  

### 第一期刻意隱藏或弱化

- Meetings / Conference 入口  
- 複雜 Chat（若只要語音電話）  
- Welcome 多頁、Developer 設定  
- 過長的進階設定樹  

完成 MVP 行為後，再做「看起來像自有產品／接近 MicroSIP 密度」。

---

## 8. Phase 5：導覽改造（最關鍵）

MicroSIP 精神：**少分頁、撥號為中心、快速撥打**。

建議資訊架構：

```
Main
 ├─ Dialer（建議作為預設或一鍵直達）
 ├─ Contacts
 ├─ History
 └─ Settings（含帳號）
Call（全螢幕）
 ├─ Outgoing
 ├─ Incoming
 └─ In-call
```

### 建議實作順序（對應本專案）

1. 閱讀 `main_nav_graph.xml`、`MainActivity.kt`、主頁 Tab／Drawer 相關 Fragment  
2. 決定：  
   - 把 `startDestination` 改成更接近撥號的體驗，**或**  
   - 保留 history，但首屏提供極顯眼的撥號入口／自動打開 numpad  
3. 隱藏或移除 Chat／Meetings 的導覽入口（layout + navigation + 選單）  
4. 確認 Contacts / History → 一鍵進通話仍正常  

**每改一項就真機點一遍導覽**，避免一次大改後迷路。

---

## 9. Phase 6：逐頁 UI

每一頁固定流程：

1. 看 MicroSIP 截圖 → 列出要保留的按鈕與狀態  
2. 打開對應 Fragment + layout  
3. 先改 layout／可見性／文案，**少改業務邏輯**  
4. 缺行為再補 ViewModel／Core 呼叫  

### 6.1 撥號頁

- 檔案：`StartCallFragment` + `start_call_fragment.xml` + `start_call_numpad_bottom_sheet.xml`  
- 重點：數字鍵、顯示號碼、刪除、撥打；減少聊天／會議干擾元素  

### 6.2 來電／撥出／通話中

- `IncomingCallFragment`、`OutgoingCallFragment`、`ActiveCallFragment`  
- 通話中優先對齊：時長、靜音、免持、DTMF、轉接、掛斷  

### 6.3 通訊錄／紀錄

- 列表簡潔；點列可撥或進詳情再撥  

### 6.4 帳號設定（欄位向 MicroSIP 靠攏）

常見欄位：

- Display / Account name  
- SIP server（domain）  
- Username / Password  
- Proxy（若需要）  
- Transport（UDP／TCP／TLS）  

進階（編解碼、STUN、ICE）放第二層。

起點：`ThirdPartySipAccountLoginFragment`、`AccountSettingsFragment`。

---

## 10. Phase 7：行為對齊檢查清單

同一 SIP 帳號，MicroSIP vs 本 App：

- [ ] 註冊成功／失敗提示清楚  
- [ ] 撥出狀態正確  
- [ ] 來電可接／可拒（Android 通知／全螢幕與 Windows 不同，屬預期）  
- [ ] 靜音有效  
- [ ] 免持切換正常  
- [ ] Hold 可恢復（若列入 P0／P1）  
- [ ] 轉接成功（P1）  
- [ ] DTMF 對 IVR 有效  
- [ ] 掉線／取消註冊有提示  
- [ ] 權限：麥克風、通知、（可選）電話相關  

Android 另測：殺程序再開、電池優化、螢幕鎖定來電。

---

## 11. Phase 8：品牌化（行為穩定後再做）

1. 應用名稱、圖示、啟動畫面  
2. 主題色、字型、撥號鍵樣式  
3. 替換或弱化原品牌展示（仍須遵守授權與歸屬聲明）  
4. About／說明頁改為你的產品說明  

---

## 12. 明確不要做的事

1. 不要尋找或封裝「MicroSIP 功能接口」給 Android 用  
2. 不要把 `microsip.cpp` / Dialog 當 Android 架構來搬  
3. 不要第一期重寫 linphone-sdk  
4. 不要第一期做會議、完整 IM、完整設定樹  

---

## 13. 建議時程（單人粗估）

| 週 | 內容 |
|----|------|
| 第 1 週 | 填三份表、截圖、導覽簡化、撥號好找 |
| 第 2 週 | 來電／通話中按鈕與流程 |
| 第 3 週 | 通訊錄／紀錄／帳號設定精簡 |
| 第 4 週 | 視覺品牌、隱藏多餘功能、回歸測試 |

---

## 14. 每日工作模板

```text
今天對齊的 MicroSIP 功能：____
對應 Fragment / layout：____
是否已有 API／現成邏輯：是 / 否
今天改動：____
真機驗證結果：____
剩餘問題：____
```

---

## 15. 完成定義（Definition of Done）

第一期完成需同時滿足：

1. 使用者能用接近 MicroSIP 的路徑完成：設帳號 → 撥打 → 接聽 → 掛斷  
2. 主路徑不強制經過聊天／會議  
3. `03-mvp-checklist.md` 中 P0 全部勾完  
4. 與 MicroSIP 使用同一帳號對打／互通正常  

---

## 16. 你現在立刻做的 4 件事

1. 安裝 MicroSIP，截主畫面／來電／通話中／設定，放到 `docs/microsip-migration/screenshots/`  
2. 填 `01-feature-matrix.md`（先只填 P0）  
3. 填 `02-screen-map.md`（標出要留／要藏的按鈕）  
4. 研究如何把「撥號」變成預設或首要入口（從 `main_nav_graph.xml` 的 `startDestination` 與 `StartCallFragment` 開始）  

完成以上四步後，再動第一個正式 UI 改動：**導覽 + 撥號頁**。
