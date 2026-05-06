# Stage 00 - 暖身:API 串接

## 目標

把 Spring Boot + Spring AI + Anthropic Claude 串起來,確認 hello world 能跑。

完成此階段後,你應該能回答:
- [ ] 我能解釋什麼是 token、為什麼要計費
- [ ] 我能解釋 streaming 跟一般 response 的差別與適用情境

---

## 前置作業

### 1. 申請 Anthropic API Key

到 https://console.anthropic.com/ 申請帳號,儲值 5 美金,建立一個 API key。

### 2. 設定環境變數

```bash
# Linux / macOS
export ANTHROPIC_API_KEY=sk-ant-xxxxxxxxxxxxx

# 想永久設定的話,寫入 ~/.zshrc 或 ~/.bashrc
echo 'export ANTHROPIC_API_KEY=sk-ant-xxx' >> ~/.zshrc
```

> ⚠️ **絕對不要**把 API key 寫死在程式碼或 commit 進 git。
> `.gitignore` 已經排除 `.env` 檔案。

### 3. 確認 Java 與 Maven 版本

```bash
java -version   # 需要 Java 25 (本專案設定為 Java 25 LTS)
mvn -version    # 需要 3.9+

# 如果 mvn -version 顯示的 JDK 不是 25,設定 JAVA_HOME:
# export JAVA_HOME=$(/usr/libexec/java_home -v 25)   # macOS
# export JAVA_HOME=/usr/lib/jvm/java-25-openjdk      # Linux
```

> 💡 **裝 Java 25 的快速方式**:
> - macOS: `brew install openjdk@25`
> - Linux: 用 [SDKMAN!](https://sdkman.io/) `sdk install java 25-tem`
> - Windows: 從 [Adoptium](https://adoptium.net/) 下載 Temurin 25

---

## 啟動方式

從**專案根目錄** (`ai-learning-journey/`) 執行:

```bash
# 第一次需要先 install common 模組
mvn install -pl common -am

# 啟動 stage-00
mvn spring-boot:run -pl stage-00-warmup
```

啟動成功會看到:
```
Tomcat started on port 8080 (http) with context path '/'
Started Stage00WarmupApplication in X.XXX seconds
```

---

## 測試

### 方法 A:用 IntelliJ IDEA / VS Code 開啟 `requests.http`

直接點 endpoint 旁邊的綠色三角形即可執行。

### 方法 B:用 curl

```bash
# 健康檢查
curl http://localhost:8080/actuator/health

# 同步 chat
curl -X POST http://localhost:8080/api/chat \
     -H "Content-Type: application/json" \
     -d '{"message":"用一句話介紹自己"}'

# 串流 chat (-N 表示不要 buffer)
curl -N -X POST http://localhost:8080/api/chat/stream \
     -H "Content-Type: application/json" \
     -d '{"message":"從 1 數到 5"}'
```

---

## 學習任務

### 任務 1:跑通 hello world(必做)

確認 `/api/chat` 能正常回應,能看到 `inputTokens` 跟 `outputTokens`。

### 任務 2:觀察 token 用量(必做)

送幾個不同長度的 message,記錄 `inputTokens` 跟 `outputTokens`:

| Message | inputTokens | outputTokens |
|---------|-------------|--------------|
| "你好" | ? | ? |
| "用 200 字解釋什麼是 RAG" | ? | ? |
| "用 500 字..." | ? | ? |

回答:
- 中文 1 個字大約消耗幾個 token?
- input 跟 output 哪個比較貴?(去查 Claude 定價表)

### 任務 3:體驗串流(必做)

跑 `/api/chat/stream` 端點,觀察:
- 文字是怎麼一塊一塊送回來的?
- 跟同步呼叫相比,使用者體驗有什麼差別?

### 任務 4:換個 model 玩玩(選做)

修改 `application.yml`:
- 改成 `claude-haiku-4-5`,問同樣問題,比較速度跟品質
- 改成 `claude-opus-4-6`,觀察成本變化

### 任務 5:亂改 temperature(選做)

把 `temperature` 從 0.7 改成 0:
- 同樣問題問 5 次,輸出有什麼不同?

把 `temperature` 改成 1.5:
- 又會發生什麼?

---

## 常見問題

### Q1: 啟動報錯「ANTHROPIC_API_KEY 未設定」

確認環境變數有設定,且**重啟 IDE / 終端機** 讓變數生效。

### Q2: 回應 401 / 認證錯誤

API key 有問題,到 https://console.anthropic.com/ 確認 key 還有效、額度還有。

### Q3: 串流端點直接回傳完整內容,沒有 SSE 效果

確認:
- request 裡面的內容夠長(太短的回應會一次回完)
- 用 curl 時加 `-N` 參數
- 瀏覽器 / Postman 有些工具會自動 buffer SSE,看不到效果是正常的

---

## 下一步

完成上面 5 個任務,確認都做過、都有觀察心得後,進入:

➡️ **Stage 01 - Prompt Engineering**

把這個 chat service 升級成「結構化輸出」的版本,
體會 prompt 寫法對輸出品質的影響。
