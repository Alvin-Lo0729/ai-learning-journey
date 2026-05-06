# AI 應用開發學習路徑

> 一份從零打造 AI 應用、循序漸進掌握 Prompt / RAG / Agent / Harness Engineering 的實作路徑
>
> **目標讀者**:有後端開發經驗(Java / Spring)、想透過實際做專案理解 AI 應用核心概念的工程師
>
> **總時程**:約 4~6 個月,每週 5~8 小時

---

## 給協助我的 AI 助理

當我下次帶這份檔案來找你時,請依照下列原則協助我:

1. **先問我目前進行到哪個階段、上次卡在哪裡**,不要從頭開始重講。
2. 我的學習方式是**從實作回推概念**,所以請優先給我可以動手的具體任務,概念說明要對應到當下的痛點。
3. 我已經有 Java / Spring Boot 後端開發經驗(電商平台),請用後端工程師習慣的類比來解釋 AI 概念。
4. 程式碼以 **Java + Spring AI** 為主,Python 範例只在 Spring AI 沒有對應功能時才用。
5. 每個階段完成後,請問我有沒有遇到困難、是否要進下一階段,而不是自動推進。
6. 如果我問「為什麼要這樣設計」,請優先用「如果不這樣會發生什麼問題」來回答,而不是直接給結論。
7. 我的工具偏好:UML 用 PlantUML、文件用 Asciidoctor、程式碼倉庫用 GitLab。
8. 如果這份計畫的某些技術已經過時(例如 Spring AI 版本、Anthropic API 變動),請主動提醒我並建議調整。

---

## 我的背景與目標

- **背景**:Java/Spring 後端工程師,在電商平台工作
- **熟悉技術**:Java、Spring Framework、PostgreSQL、ELK(Kibana / Elasticsearch)、GitLab
- **不熟悉**:AI 應用開發、向量資料庫、LLM 整合
- **學習目標**:能獨立設計並實作可進 production 的 AI 功能(優先聚焦電商場景)
- **每階段都要產出可放 GitHub / 履歷的成品**

---

## 學習路徑總覽

| 階段 | 主題 | 預估時間 | 核心概念 |
|------|------|---------|---------|
| 0 | 暖身:API 串接 | 1 週 | API 呼叫、Spring AI 基礎 |
| 1 | Prompt Engineering | 1 週 | 結構化輸出、few-shot、CoT |
| 2 | RAG 基礎 | 3 週 | Embedding、向量資料庫、retrieval |
| 3 | Tool Use / Function Calling | 2 週 | Tool schema、multi-turn |
| 4 | Context Engineering | 2 週 | Context 預算管理、prompt caching |
| 5 | Agent Loop | 3 週 | ReAct pattern、自主執行 |
| 6 | Harness Engineering | 4 週 | Observability、guardrail、eval |
| 7 | 延伸方向 | 看興趣 | MCP / Multi-agent / 工作整合 |

---

## 階段 0:暖身 — API 串接(第 1 週,~5 小時)

### 目標
把開發環境跟 Anthropic API 串起來,確認能 hello world。

### 任務清單
- [ ] 申請 Anthropic API key,儲值 $5 美金
- [ ] 建立 Spring Boot 3.x + Spring AI 最小專案
- [ ] 寫一個 endpoint:`POST /chat`,輸入文字,回傳 Claude 的回應
- [ ] 試試 streaming response(Server-Sent Events)
- [ ] 對照 Anthropic 官方課程 [Building with the Claude API](https://anthropic.skilljar.com/claude-with-the-anthropic-api) 前兩章

### 自然學到的概念
- API 呼叫流程、token 計費、streaming
- Spring AI 的 `ChatClient` 抽象層

### 產出
一個會講話的 Spring Boot service。

### 完成檢核
- [ ] 我能解釋什麼是 token、為什麼要計費
- [ ] 我能解釋 streaming 跟一般 response 的差別與適用情境

---

## 階段 1:Prompt Engineering(第 2 週,~5 小時)

### 目標
讓 service 做一件有用的事,並體會 prompt 寫法的差異。

### 任務清單
- [ ] 設計任務:輸入商品評論,輸出 JSON `{sentiment, summary, issues}`
- [ ] **故意先寫爛 prompt**(例如「分析這段評論」),記錄輸出結果
- [ ] 逐步改進:
  - [ ] 加角色設定(system prompt)
  - [ ] 加 few-shot 範例(2~3 個輸入輸出對)
  - [ ] 加輸出格式約束
  - [ ] 用 tool use 強制 JSON 輸出
  - [ ] 加思維鏈(讓 Claude 先列重點再給結論)
- [ ] 對比每次改進的輸出差異,寫成筆記

### 自然學到的概念
- System prompt vs User prompt 的角色差異
- Few-shot prompting 為什麼有效
- Structured output 的不同實作方式
- Chain-of-Thought / 思維鏈

### 產出
商品評論分析 API,Postman 丟資料能得到結構化結果。

### 對應工作場景
客服工單自動分類、商品 Q&A 重點抽取。

### 完成檢核
- [ ] 我能說出 4 種改善 prompt 品質的方法
- [ ] 我知道什麼時候該用 tool use 強制輸出格式,什麼時候用文字描述就夠

---

## 階段 2:RAG 基礎(第 3~5 週,~20 小時)

### 目標
讓 service 能基於我提供的資料回答,而不是亂掰。

### 任務清單

**資料準備**
- [ ] 用 Claude 產生 50~200 筆假商品資料(名稱、描述、規格、FAQ)
- [ ] 存成 JSON 或 CSV

**基礎建設**
- [ ] 安裝 PostgreSQL + pgvector 擴充(或選 Chroma)
- [ ] Spring AI 設定向量資料庫連線

**Ingestion Pipeline**
- [ ] 讀商品資料 → 切 chunk → 算 embedding → 存資料庫
- [ ] 思考 chunk 大小怎麼選(實驗 256 / 512 / 1024 tokens)

**查詢流程**
- [ ] 使用者問題 → 算 embedding → 找 top-K → 塞進 prompt → Claude 回答
- [ ] **故意做失敗案例**:問資料庫沒有的問題、問模糊問題,記錄會發生什麼

**評估**
- [ ] 準備 20 組 Q&A pair
- [ ] 寫腳本測 retrieval hit rate(top-5 結果有沒有包含正確答案)

**進階(有時間再做)**
- [ ] 加 Hybrid Search(向量 + 關鍵字)
- [ ] 加 Reranker

### 自然學到的概念
- Embedding 與語意搜尋原理
- Chunk 切割策略的工程權衡
- 向量資料庫 vs 傳統資料庫
- **Retrieval 才是 RAG 的瓶頸,不是 LLM**(必踩坑)
- 為什麼純向量搜尋不夠

### 產出
能回答商品問題的 API,例如「有什麼藍色襯衫?」、「這耳機防水嗎?」

### 完成檢核
- [ ] 我能畫出 RAG 的完整架構圖(用 PlantUML)
- [ ] 我能解釋為什麼相同問題在不同 chunk size 下會得到不同答案
- [ ] 我能說出至少 3 種改善 retrieval 品質的方法

---

## 階段 3:Tool Use / Function Calling(第 6~7 週,~12 小時)

### 目標
讓 Claude 能呼叫我的 Spring service,而不只是基於文字回答。

### 任務清單
- [ ] 定義 3 個 tool:
  - [ ] `searchProducts(keyword, priceRange)`
  - [ ] `getOrderStatus(orderId)`
  - [ ] `checkInventory(productId)`
- [ ] 用 Spring AI 的 `@Tool` annotation(或手動定義 schema)註冊給 Claude
- [ ] 寫對話式 endpoint,測試:
  - [ ] 「幫我查訂單 12345 的狀態」(單一 tool)
  - [ ] 「有沒有 1000 元以下的藍牙耳機?」(帶參數)
  - [ ] 「找一支 1500 元以下的耳機,看有沒有貨」(連續 tool)
- [ ] 觀察 Claude 怎麼決定要用哪個 tool、怎麼處理 tool 失敗

### 自然學到的概念
- Tool schema 設計(參數命名、描述寫法,**比想像中重要**)
- Multi-turn tool use(連續呼叫多個 tool)
- 錯誤處理:tool 回傳錯誤時 Claude 的反應
- **Agent 的雛形:LLM + 工具 + 迭代 = Agent**

### 產出
有實際業務功能的對話式電商助理。

### 完成檢核
- [ ] 我能解釋為什麼 tool 描述寫不好會讓 Claude 用錯 tool
- [ ] 我能設計一個 tool schema,讓 Claude 不容易誤用

---

## 階段 4:Context Engineering(第 8~9 週,~10 小時)

### 目標
處理「對話越來越長 / 文件越來越多」的問題。

### 任務清單
- [ ] 把階段 3 的 service 改成支援多輪對話
- [ ] **故意讓對話超過 30 輪**,觀察:
  - [ ] Token 用量怎麼飆升
  - [ ] 模型開始忘記前面的事
  - [ ] Latency 怎麼變慢
- [ ] 實作對應解法:
  - [ ] **Summarization**:超過 N 輪時把前面壓縮成摘要
  - [ ] **Sliding window**:只保留最近 K 輪
  - [ ] **Selective retrieval**:對話歷史也存 vector,需要時才撈
  - [ ] **Prompt caching**(Anthropic 原生支援,大幅降費用)
- [ ] 比較不同策略的效果與成本

### 自然學到的概念
- Context window 是有限資源,要當預算管理
- 不是所有資訊都要塞 context,要動態篩選
- Prompt caching 對重複 system prompt 場景的成本影響

### 產出
能撐長對話、cost 與 latency 在合理範圍的助理。

### 完成檢核
- [ ] 我能比較 4 種 context 管理策略的優缺點
- [ ] 我知道我的應用場景該選哪一種

---

## 階段 5:Agent Loop(第 10~12 週,~15 小時)

### 目標
從「單次 tool call」進化到「自主多步驟執行」。

### 任務清單
- [ ] 設計需要多步驟的任務,例如:
  > 「找一支評價最好、1500 元以下、有降噪的藍牙耳機,查庫存,有貨就建預訂單」
- [ ] 自己寫 agent loop(不要用 LangChain4j 等高階框架,先理解原理)
- [ ] 必備機制:
  - [ ] Max iteration 限制(防無限 loop)
  - [ ] Cost 上限監控
  - [ ] 每一步的 logging
- [ ] 觀察 agent 失控的場景:
  - [ ] 重複呼叫同一個 tool
  - [ ] 用錯 tool
  - [ ] 產生幻覺資料

### 自然學到的概念
- Agent 自主性 vs 可控性的權衡
- ReAct pattern(Reason → Act → Observe)
- 為什麼 agent 會失控
- **這就是 Harness Engineering 要解決的問題**

### 產出
會自主完成多步驟任務的 agent(雖然還不穩定)。

### 必讀
- Anthropic 的「Building Effective Agents」文章

### 完成檢核
- [ ] 我能用 PlantUML 畫出 agent loop 的流程
- [ ] 我能列出至少 5 種 agent 失控的失敗模式

---

## 階段 6:Harness Engineering(第 13~16 週,~20 小時)

### 目標
把階段 5 的 agent 變成能上 production 的東西。

### 任務清單

**Observability 層**
- [ ] LLM call 的 input/output 記到 ELK
- [ ] 追蹤 token 用量、cost、latency
- [ ] 用 trace ID 串起一個 task 的所有步驟
- [ ] 建 Kibana dashboard 視覺化

**Guardrail 層**
- [ ] 輸入過濾:防止 prompt injection
- [ ] 輸出驗證:tool call 是否合法、參數合理
- [ ] 危險操作加人工確認(例如建訂單要按確認)

**Reliability 層**
- [ ] LLM API 失敗的 retry(用 Resilience4j)
- [ ] Tool 失敗時 agent 怎麼恢復
- [ ] Circuit breaker(連續失敗暫停)

**Evaluation 層**
- [ ] 準備 30~50 組測試案例
- [ ] 寫自動化評估:同樣問題跑 5 次測一致性
- [ ] 換 model 比較(Sonnet vs Opus vs Haiku),比品質與成本
- [ ] 建立 prompt 改動的 A/B test 流程

### 自然學到的概念
- Harness 的全貌:tool design + guardrail + observability + eval
- 為什麼 evaluation 是 AI 系統的關鍵
- 怎麼安全地修改 prompt 而不破壞既有行為

### 產出
**有完整工程素質的 production-ready agent demo**(放履歷很有說服力)。

### 完成檢核
- [ ] 我能對任何一個 LLM 應用提出 5 個觀察重點
- [ ] 我能設計一個 eval 流程驗證新版本沒有 regression

---

## 階段 7:延伸方向(看興趣選)

### 方向 A:MCP(Model Context Protocol)
- 學 MCP 協定,把 tool 封裝成 MCP server
- 讓你的 agent、Claude Code、Cursor 都能用
- 對應課程:[Introduction to MCP](https://anthropic.skilljar.com/introduction-to-model-context-protocol)

### 方向 B:Multi-Agent System
- 設計多 agent 協作(Planner → Executor → Reviewer)
- 2026 年熱門方向

### 方向 C:RAG 進階
- GraphRAG、Agentic RAG、self-correcting RAG
- 多模態 RAG(圖片 + 文字)

### 方向 D:整合到實際工作
- 找公司真實需求(智能客服、商品描述生成、異常訂單偵測)
- 提案 → POC → 上線

---

## 推薦資源

### 官方課程(Anthropic Skilljar)
| 課程 | 對應階段 | 優先度 |
|------|---------|--------|
| Building with the Claude API | 0~3 | ⭐⭐⭐ |
| Introduction to MCP | 7-A | ⭐⭐ |
| MCP: Advanced Topics | 7-A | ⭐ |
| AI Fluency: Framework & Foundations | 0(暖身) | ⭐ |

### 必讀文章
- Anthropic:Building Effective Agents
- Anthropic:Harness Design for Long-Running Application Development
- Anthropic 官方 Cookbook(GitHub)

### Spring AI 資源
- Spring AI 官方文件
- Spring AI 範例 repo

### 持續追蹤
- Anthropic Engineering Blog
- Spring AI release notes
- Hacker News(每週的 AI 討論)

---

## 學習原則(我給自己的提醒)

1. **每個階段選一個跟電商相關的場景**,不做純 demo,累積「AI × 電商」實戰經驗
2. **每個階段要寫 blog 或筆記**(GitLab / Notion / 個人網站)
3. **保留 20% 時間「亂玩」**:看新工具、新論文、別人的做法
4. **不要太早跳到 Harness / Agent**,前面紮實後面才會穩
5. **找夥伴一起跑**,不要一個人埋頭做
6. **每階段完成才進下一階段**,不要平行開太多戰線
7. **遇到撞牆,先寫下來「我以為會發生什麼,實際發生什麼」**,這是最好的學習筆記

---

## 進度追蹤

> 開始時請填寫,並隨進度更新

- 開始日期:____________
- 目前階段:____________
- 上次更新:____________
- 卡住的點:____________
- 下次想討論:____________

---

## 附錄:概念對照表(後端工程師視角)

| AI 概念 | 後端類比 |
|---------|---------|
| Prompt Engineering | 寫一支 SQL query |
| RAG | 把資料庫接上來給 SQL 用 |
| Context Engineering | 決定 SELECT 哪些欄位、JOIN 哪些表 |
| Tool Use | 讓 LLM 呼叫你的 Spring service |
| Agent | 一個會自主迭代的 service |
| Harness Engineering | 整個服務的架構:transaction、retry、circuit breaker、logging、監控、權限控管 |
| Embedding | 把文字轉成可以做相似度比較的向量 |
| Vector DB | 專門做相似度搜尋的資料庫 |
| Eval | 給 AI 應用寫的單元測試 + 整合測試 |
