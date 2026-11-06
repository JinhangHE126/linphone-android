# 功能對照表（MicroSIP → Linphone）

填表說明：

- **優先級**：P0 必做／P1 常用／P2 可延後  
- **Linphone 是否已有**：是／部分／否（指 SDK + 現有 UI 能力，不是「已做成 MicroSIP 樣子」）  
- **本專案對應**：Fragment、ViewModel 或 layout 路徑  
- **狀態（對齊 MicroSIP 體驗）**：未開始／進行中／完成／不做

主文檔：[`../microsip-like-linphone.md`](../microsip-like-linphone.md)

## Phase 1 結論（已核對程式碼）

| 結論 | 說明 |
|------|------|
| P0 通話能力 | **幾乎全部已有**（註冊、撥號、來電、靜音、免持、掛斷、Hold、DTMF、通訊錄、紀錄） |
| P1 | 轉接、視訊、多帳號、編解碼／ICE 等 **已有** |
| P2 | 自動接聽、訊息、快速鍵／BLF → **第一期不做或藏** |
| 真正缺的是 | **體驗／導覽／UI 密度** 還不像 MicroSIP（尤其 F18、F19） |
| 範圍 | **已凍結** — 只交付主路徑；不再加需求 |

Phase 2 已完成。下一步：**實作導覽 + 藏 Chat／會議**（見 `03-mvp-checklist` B 區）。

---

## 功能表

| ID | MicroSIP 功能 | 優先級 | Linphone 是否已有 | 本專案對應 | 狀態 | 備註 |
|----|---------------|--------|-------------------|------------|------|------|
| F01 | SIP 帳號註冊 | P0 | 是 | `ui/assistant/`（如 `ThirdPartySipAccountLoginFragment`）、`AccountSettingsFragment`、`AccountSettingsViewModel` | 進行中 | 首層已對齊 MicroSIP（帳號名稱／SIP主機／使用者／網域／密碼／UDP）；新增預設 UDP／註冊 300／無 ICE |
| F02 | 註冊狀態顯示（Online/Offline） | P0 | 是 | `AccountModel`（`registrationState*`）、`MainViewModel.onAccountRegistrationStateChanged`、Drawer／帳號 UI | 進行中 | 側欄已顯示註冊狀態；狀態列可再對齊 MicroSIP |
| F03 | 撥號盤撥出 | P0 | 是 | `StartCallFragment`、`StartCallViewModel`、`start_call_fragment.xml`、`start_call_numpad_bottom_sheet.xml` | 完成（待真機驗） | 撥號已為首屏 |
| F04 | DTMF | P0 | 是 | `CurrentCallViewModel`（`sendDtmf`）、`call_numpad_bottom_sheet` | 完成（待真機驗） | 通話中上拉 → 撥號盤 |
| F05 | 來電接聽／拒接 | P0 | 是 | `IncomingCallFragment`、`CurrentCallViewModel`／`hangUp` | 完成（待真機驗） | Android 另有通知／全螢幕來電 |
| F06 | 通話中靜音 | P0 | 是 | `CurrentCallViewModel.toggleMuteMicrophone()`、`call_actions_generic` | 完成（待真機驗） | 主列 |
| F07 | 通話中免持／聽筒 | P0 | 是 | `AudioDevicesMenuDialogFragment`、`CurrentCallViewModel.changeAudioOutputDevice` | 完成（待真機驗） | 主列 |
| F08 | 通話中掛斷 | P0 | 是 | `CurrentCallViewModel.hangUp()`、`call_actions_generic` | 完成（待真機驗） | 主列紅鍵 |
| F09 | Hold／恢復 | P0 | 是 | `CurrentCallViewModel.togglePause()`、`call_actions_generic`（`pause_call_main`） | 完成（待真機驗） | 已提到通話主列 |
| F10 | 通話轉接 | P1 | 是 | `TransferCallFragment`、`blindTransferCallTo`／`attendedTransferCallTo`（`CurrentCallViewModel`） | 未開始 | 盲轉／諮詢轉都有 |
| F11 | 視訊開關 | P1 | 是 | `CurrentCallViewModel.toggleVideo()`、`CallActivity`、`ActiveCallFragment` | 未開始 | |
| F12 | 通訊錄瀏覽與點撥 | P0 | 是 | `ContactsListFragment`、`ContactFragment`、`coreContext.contactsManager` | 未開始 | |
| F13 | 通話紀錄與回撥 | P0 | 是 | `HistoryListFragment`、`HistoryFragment`；`main_nav_graph` 目前 `startDestination` | 未開始 | 現為預設首頁 |
| F14 | 簡單訊息 | P2 | 是 | `ConversationsListFragment`、`ConversationFragment`、chat nav | 不做（第一期） | **第一期建議隱藏入口**，非缺功能 |
| F15 | 多帳號切換 | P1 | 是 | `DrawerMenuFragment`、`AccountModel`、`AccountProfileFragment` | 未開始 | |
| F16 | 編解碼／網路（STUN／ICE 等） | P1 | 是 | `SettingsViewModel`（codecs）、`AccountSettingsViewModel.iceEnabled`、`SettingsAdvanced*` | 未開始 | 對照 `09-settings.png`：啟用 PCMA/PCMU；STUN／埠在設定底左 |
| F17 | 自動接聽等 | P2 | 是 | `CorePreferences.autoAnswerEnabled`、`SettingsViewModel.autoAnswerIncomingCalls*` | 未開始 | MicroSIP「自動接聽=控制按鈕」；設定見 `09-settings.png` |
| F18 | 隱藏會議入口 | P0 | 是 | `AbstractMainViewModel.updateAvailableMenus`、`bottom_nav_bar` | 完成 | 底欄已移除 Meetings |
| F19 | 主路徑以撥號為中心 | P0 | 是 | `main_nav_graph` startDestination=`startCallFragment`、三 Tab 底欄 | 完成 | Phone / Call history / Contacts |

## 自行追加

| ID | MicroSIP 功能 | 優先級 | Linphone 是否已有 | 本專案對應 | 狀態 | 備註 |
|----|---------------|--------|-------------------|------------|------|------|
| F20 | | | | | | |
| F21 | 快速鍵／速度撥號／BLF（8 槽） | P2 | 部分 | 無現成同等 UI；presence／訂閱能力需另評 | 不做（第一期） | 見 `10-shortcuts.png`；非鍵盤熱鍵 |

---

## Phase 1 完成標準

- [x] P0／P1／P2 清單列齊  
- [x] 「是否已有」依本專案程式碼核對  
- [x] 標出真正要改的缺口（主要是 F18、F19 + 各頁 UI 對齊，不是缺 SIP 能力）  
- [x] 你瀏覽一遍表格，若有 MicroSIP 上還想要、表上沒有的功能，補到 F20 起  

確認後進入 **Phase 2**：`02-screen-map.md` + `screenshots/`。
