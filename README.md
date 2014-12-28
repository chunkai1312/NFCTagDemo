# NFCTagDemo
103-1 NTUST RFID資訊安全 Programming Homework #1

## Requirements
設計一款 Android NFC 應用程式，包含以下功能：

### Reading Tag
* 可讀出 Tag 的類型與最大儲存容量 (Bytes)。
* 可讀出 Tag 的儲存內容 (TNF_WELL_KNOWN with RTD_URI)。
* 當讀出 URI 格式為 Tel (e.g. tel:xxxxxxxxxx) 時，可立即撥打電話。

### Writing Tag
* 可建立 TNF_WELL_KNOWN with RTD_URI (tel) 型態的 Record。
* 將建立的 Record 包裝為 NDEF Message 然後寫入 Tag。


