# AI Learning Journey

> 從零學習 AI 應用開發的多模組實作專案
> 對應 [ai-learning-roadmap.md](./ai-learning-roadmap.md) 的學習路徑

---

## 專案結構

```
ai-learning-journey/
├── pom.xml                          # parent pom,管理所有版本與共用 dependencies
├── README.md                        # 本檔案
├── ai-learning-roadmap.md           # 完整學習路徑說明 (建議放這層)
│
├── common/                          # 共用模組:utility、DTO base、共用設定
│   ├── pom.xml
│   └── src/main/java/...
│
├── stage-00-warmup/                 # 階段 0:API 串接 ✅
│   ├── pom.xml
│   ├── README.md
│   └── src/...
│
├── stage-01-prompt-engineering/     # 階段 1:Prompt Engineering (尚未建立)
├── stage-02-rag-basics/             # 階段 2:RAG 基礎
├── stage-03-tool-use/               # 階段 3:Tool Use
├── stage-04-context-engineering/    # 階段 4:Context Engineering
├── stage-05-agent-loop/             # 階段 5:Agent Loop
└── stage-06-harness-engineering/    # 階段 6:Harness Engineering
```

---

## 技術棧

| 類別 | 選擇 | 版本 |
|------|------|------|
| 語言 | Java | 25 LTS |
| 建構工具 | Maven | 3.9+ |
| 應用框架 | Spring Boot | 3.5.6 |
| AI 框架 | Spring AI | 1.1.5 |
| LLM 提供者 | Anthropic Claude | claude-sonnet-4-5 |

> 💡 **為什麼是 Spring Boot 3.5 而不是 4.0?**
> Spring Boot 4.0 已釋出,但 Spring AI 2.0 (對應 Boot 4) 還在 milestone 階段,GA 預計 2026 年中。
> 學習階段選穩定組合可避免追 milestone 版本踩坑。等 stage 0~6 跑完,Spring AI 2.0 也 GA 了再升級。

---

## 如何使用

### 第一次設定

```bash
# 1. 設定 API key 環境變數
export ANTHROPIC_API_KEY=sk-ant-xxx

# 2. install 所有模組到本地 Maven repo
mvn install -DskipTests

# 3. 進入想做的階段
cd stage-00-warmup
cat README.md
```

### 啟動某個階段

```bash
# 從 root 啟動 (推薦,不會混淆 working directory)
mvn spring-boot:run -pl stage-00-warmup

# 切換階段時,只要換 -pl 後面的模組名即可
mvn spring-boot:run -pl stage-01-prompt-engineering
```

### 新增階段

1. 在 root `pom.xml` 的 `<modules>` 加上新模組名稱
2. 複製 `stage-00-warmup/pom.xml` 改 artifactId
3. 在新模組底下建立同樣的 src 結構

---

## 學習原則

- 每個階段都有獨立 `README.md` 說明任務跟產出
- **每個階段完成才進下一階段**,不要平行開太多戰線
- 每階段選一個跟電商相關的場景做,累積實戰經驗
- 遇到撞牆時寫下「我以為會發生什麼,實際發生什麼」

---

## 給協助我的 AI 助理

請對照 `ai-learning-roadmap.md` 的「給協助我的 AI 助理」段落了解協作方式。

簡單版:
1. 先問我目前進行到哪個階段、上次卡在哪裡
2. 優先給可動手的任務,概念說明對應到當下的痛點
3. 用 Java + Spring AI 為主,後端工程師熟悉的類比講解
4. 我的工具偏好:UML 用 PlantUML、文件用 Asciidoctor、程式碼倉庫用 GitLab
