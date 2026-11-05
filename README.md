# Cadastro de Produtos - Android com SQLite

![Android](https://img.shields.io/badge/Platform-Android%2016%20(API%2036)-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SQLite](https://img.shields.io/badge/Database-SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Material Design](https://img.shields.io/badge/UI-Material%20Design%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.6-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

Aplicativo Android nativo desenvolvido em **Java** no **Android Studio**, com persistência local de dados utilizando **SQLite** (`SQLiteOpenHelper`). O projeto adota componentes visuais modernos do **Material Design 3**, arquitetura limpa orientada a objetos e recursos avançados de interface e usabilidade.

> 📸 **Prints e Demonstrações**: Todas as capturas de tela do funcionamento e testes no emulador **Pixel 10 Pro (API 37.2 - Android 16)** estão organizadas na pasta [`prints/`](./prints) deste repositório.

---

## 🎯 Contexto e Objetivos

Projeto desenvolvido no curso de **Análise e Desenvolvimento de Sistemas (ADS)**, com foco em:
- Persistência estruturada em banco de dados relacional local (*Offline-First*).
- Ciclo de vida de Activities e manipulação de eventos de interface.
- Implementação de listas dinâmicas com adapter customizado (`ArrayAdapter<Produto>`).
- Validação robusta de formulários e tratamento defensivo de erros.
- Adaptação a telas modernas com suporte a barras do sistema (*Edge-to-Edge* via `WindowInsetsCompat`).

---

## 🚀 Diferenciais e Melhorias Implementadas

Além dos requisitos básicos de inserção e listagem, o projeto foi aprimorado com funcionalidades de nível de produção:

1. **Correção de Sintaxe SQL no Roteiro Inicial**:  
   Identificou-se e corrigiu-se uma falha de concatenação no comando de criação da tabela DDL (`COLUMN_ID + "INTEGER..."` sem espaço delimitador), garantindo a integridade da inicialização do banco.
2. **Superação de Modelo Anêmico (`Produto.java`)**:  
   Em vez de manipular strings soltas (`List<String>`), foi criada a entidade de domínio encapsulada `Produto` (com `id`, `nome` e `preco`), viabilizando a busca e exclusão precisa por chave primária (`id`).
3. **Seleção Múltipla e Exclusão em Lote**:  
   Cada item da lista conta com uma `CheckBox` interativa sincronizada ao adapter, acompanhada de um seletor mestre **"Selecionar Todos"** para marcação e remoção em massa.
4. **Confirmações de Segurança com `AlertDialog`**:  
   Prevenção contra exclusões acidentais em três níveis:
   - Exclusão individual via ícone de lixeira.
   - Exclusão em lote dos itens selecionados.
   - Limpeza completa do banco de dados (*reset* total da base).
5. **Validação Defensiva de Entrada**:  
   Feedback imediato via `TextInputLayout.setError()` e `Toast`:
   - Nome obrigatório com no mínimo 3 caracteres.
   - Preço numérico obrigatório maior que zero, aceitando vírgula ou ponto como separador decimal.

---

## 🏗️ Arquitetura do Código

```
cadastro-produtos-android-sqlite/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/cadastroprodutos/
│   │   │   ├── MainActivity.java        # Controle de UI, eventos e diálogos
│   │   │   ├── Produto.java             # Entidade de domínio (id, nome, preco)
│   │   │   ├── ProdutoAdapter.java      # Adapter customizado com controle de seleção
│   │   │   └── ProdutoDbHelper.java     # Gerenciamento SQLite (DDL, CRUD, queries)
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml    # Layout principal com Material Components
│   │   │   │   └── item_produto.xml     # Item de lista com checkbox e lixeira
│   │   │   └── values/                  # Strings, temas e cores
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── gradle/                              # Version catalog (libs.versions.toml) e wrapper
├── prints/                              # Capturas de tela dos testes no Pixel 10 Pro
├── build.gradle
├── settings.gradle
├── .gitignore
├── LICENSE
└── README.md
```

---

## 📱 Ambiente de Execução e Testes

Os testes funcionais e capturas de tela foram realizados no seguinte ambiente:
- **IDE**: Android Studio
- **Dispositivo Simulado**: Google Pixel 10 Pro
- **Sistema Operacional**: Android 16 (API Level 37.2 / compileSdk 36)
- **Persistência**: SQLite 3

---

## 📸 Demonstração e Testes

As capturas de tela completas de todas as etapas (cadastro, seleção múltipla, exclusão e validação de erros) estão na pasta [`prints/`](./prints). Destaques:

| Cadastro e Listagem | Seleção Múltipla para Exclusão |
| :---: | :---: |
| ![Itens Salvos](prints/20%20-%20Itens%20Salvos%20no%20Banco%20de%20Dados.png) | ![Marcando Itens](prints/21%20-%20Marcando%20Itens%20para%20Deletar.png) |

| Confirmação de Limpeza Total | Validação de Entrada |
| :---: | :---: |
| ![Limpando Banco](prints/23%20-%20Limpando%20Banco%20de%20Dados.png) | ![Testando Erros](prints/25%20-%20Testando%20Erros%20-%20Parte%201.png) |

---

## 🛠️ Como Abrir e Compilar o Projeto

1. Clone ou baixe este repositório:
   ```bash
   git clone https://github.com/daniel88ubajara/cadastro-produtos-android-sqlite.git
   ```
2. Abra o **Android Studio**.
3. Selecione **File > Open...** e aponte para a pasta `cadastro-produtos-android-sqlite`.
4. Aguarde a sincronização inicial do Gradle (`Sync Project with Gradle Files`).
5. Execute o app em um emulador ou dispositivo físico conectado clicando em **Run 'app'** (`Shift + F10`).

---

## 👤 Autor

Desenvolvido por **Daniel Lima**  
Estudante de Análise e Desenvolvimento de Sistemas (ADS).

[![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?style=flat&logo=github)](https://github.com/daniel88ubajara)
