# 🎨 Interface Gráfica - Sistema Bancário NTT Data

## 🌟 **Características Principais**

### ♿ **Acessibilidade Digital**
- **WCAG 2.1 AA Compliant** - Atende diretrizes internacionais
- **Screen Reader Support** - Compatível com NVDA, JAWS, etc.
- **Navegação por Teclado** - 100% navegável sem mouse
- **Alto Contraste** - Cores adequadas para baixa visão
- **Fontes Escaláveis** - Tamanhos ajustáveis
- **Feedback Sonoro** - Alertas auditivos opcionais

### 🎯 **Funcionalidades**
- Interface moderna e intuitiva
- Todas as funcionalidades do sistema console
- Design responsivo
- Temas personalizáveis
- Atalhos de teclado
- Tooltips informativos

## 📁 **Estrutura de Arquivos**

```
gui/
├── src/main/java/com/nttdata/banco/gui/
│   ├── MainGUI.java                 # Classe principal da GUI
│   ├── controller/                  # Controladores MVC
│   │   ├── MainController.java      # Controlador principal
│   │   ├── ClienteController.java   # Gestão de clientes
│   │   ├── ContaController.java     # Gestão de contas
│   │   └── TransacaoController.java # Transações
│   ├── view/                        # Telas e janelas
│   │   ├── MainWindow.java          # Janela principal
│   │   ├── ClienteView.java         # Tela de clientes
│   │   ├── ContaView.java           # Tela de contas
│   │   └── TransacaoView.java       # Tela de transações
│   ├── components/                  # Componentes customizados
│   │   ├── AccessibleButton.java    # Botão acessível
│   │   ├── AccessibleTextField.java # Campo de texto acessível
│   │   └── AccessibleTable.java     # Tabela acessível
│   ├── utils/                       # Utilitários
│   │   ├── AccessibilityUtils.java  # Utilitários de acessibilidade
│   │   ├── KeyboardUtils.java       # Atalhos de teclado
│   │   └── SoundUtils.java          # Feedback sonoro
│   └── theme/                       # Temas e cores
│       ├── ThemeManager.java        # Gerenciador de temas
│       ├── AccessibleColors.java    # Cores acessíveis
│       └── FontManager.java         # Gerenciamento de fontes
├── resources/                       # Recursos
│   ├── icons/                       # Ícones
│   ├── sounds/                      # Sons de feedback
│   └── themes/                      # Arquivos de tema
└── README.md                        # Este arquivo
```

## 🚀 **Como Executar**

### Compilar
```bash
# Navegar para a pasta gui
cd gui

# Compilar (inclui dependências do sistema principal)
javac -d out -cp "../out" src/main/java/com/nttdata/banco/gui/*.java src/main/java/com/nttdata/banco/gui/**/*.java
```

### Executar
```bash
# Executar a interface gráfica
java -cp "out;../out" com.nttdata.banco.gui.MainGUI
```

## ♿ **Recursos de Acessibilidade**

### **Para Usuários com Deficiência Visual**
- Screen readers totalmente suportados
- Descrições detalhadas de todos os elementos
- Navegação sequencial lógica
- Contraste mínimo 4.5:1
- Redimensionamento de fonte até 200%

### **Para Usuários com Deficiência Motora**
- Navegação 100% por teclado
- Teclas de atalho customizáveis
- Áreas de clique grandes (mínimo 44px)
- Tempo de resposta configurável

### **Para Usuários com Deficiência Auditiva**
- Feedback visual para todos os sons
- Legendas em alertas sonoros
- Indicadores visuais de estado

### **Para Usuários com Deficiência Cognitiva**
- Interface clara e consistente
- Linguagem simples
- Confirmações em ações críticas
- Ajuda contextual disponível

## 🎨 **Temas Disponíveis**
- **Claro** - Tema padrão com alto contraste
- **Escuro** - Ideal para ambientes com pouca luz
- **Alto Contraste** - Máximo contraste para baixa visão
- **Protanopia/Deuteranopia** - Adaptado para daltonismo
- **Tritanopia** - Adaptado para daltonismo azul-amarelo

## ⌨️ **Atalhos de Teclado**
- `Ctrl+N` - Novo cliente/conta
- `Ctrl+S` - Salvar
- `Ctrl+F` - Buscar
- `F1` - Ajuda
- `F5` - Atualizar
- `Esc` - Voltar/Cancelar
- `Alt+1-9` - Navegar entre abas
- `Ctrl+Plus/Minus` - Aumentar/diminuir fonte

## 🔧 **Tecnologias Utilizadas**
- **Java Swing** - Interface gráfica
- **Accessibility API** - Suporte a tecnologias assistivas
- **MVC Pattern** - Arquitetura organizacional
- **Sistema Bancário Core** - Reutilização do sistema existente

---

**🎯 Desenvolvido com foco total em inclusão e acessibilidade digital!**
