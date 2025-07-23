# 🚀 EXECUTAR INTERFACE GRÁFICA ACESSÍVEL

## ⚡ **Execução Rápida**

### **Método 1: Automático (Recomendado)**
```bash
# Navegar para a pasta GUI
cd gui

# Executar (compila automaticamente se necessário)
run-gui.bat
```

### **Método 2: Manual**
```bash
# 1. Compilar sistema principal (se necessário)
cd C:\Users\olive\Documents\GitHub\sistema-bancario-poo
compile.bat

# 2. Ir para pasta GUI
cd gui

# 3. Compilar GUI
javac -d out -cp "..\out" src/main/java/com/nttdata/banco/gui/MainGUI.java src/main/java/com/nttdata/banco/gui/theme/*.java src/main/java/com/nttdata/banco/gui/utils/*.java src/main/java/com/nttdata/banco/gui/components/*.java src/main/java/com/nttdata/banco/gui/view/*.java

# 4. Executar GUI
java -cp "out;..\out" com.nttdata.banco.gui.MainGUI
```

---

## ♿ **TESTE OS RECURSOS DE ACESSIBILIDADE**

### **🎨 Temas para Deficiência Visual:**
1. Abrir Menu → Visualizar → Temas
2. Testar todos os 5 temas:
   - **Claro** - Padrão com alto contraste
   - **Escuro** - Para ambientes com pouca luz  
   - **Alto Contraste** - Máximo contraste (preto/branco)
   - **Protanopia/Deuteranopia** - Para daltonismo vermelho-verde
   - **Tritanopia** - Para daltonismo azul-amarelo

### **📏 Fontes Escaláveis:**
1. **Via Menu:** Visualizar → Tamanho da Fonte → Escolher tamanho
2. **Via Teclado:** Ctrl+Plus (aumentar) ou Ctrl+Minus (diminuir)
3. Testar todos os 5 tamanhos: Pequeno → Gigante

### **🔊 Feedback Sonoro:**
1. Menu → Acessibilidade → Feedback Sonoro
2. Marcar/desmarcar para testar sons
3. Clicar nos botões para ouvir feedback

### **⌨️ Navegação por Teclado:**
1. **Tab** - Navegar entre botões e menus
2. **Enter/Espaço** - Ativar botão focado
3. **Alt+A** - Menu Arquivo
4. **Alt+V** - Menu Visualizar
5. **Alt+C** - Menu Acessibilidade  
6. **Alt+H** - Menu Ajuda
7. **C** - Botão Clientes
8. **O** - Botão Contas
9. **T** - Botão Transações
10. **I** - Botão Investimentos
11. **R** - Botão Relatórios

### **📱 Screen Readers (Leitores de Tela):**
Se você tem NVDA, JAWS ou Windows Narrator:
1. Ativar o leitor de tela
2. Abrir a interface gráfica
3. Navegar com Tab ou setas
4. Todos os elementos serão lidos automaticamente
5. Mensagens de status são anunciadas

---

## 🎯 **FUNCIONALIDADES DISPONÍVEIS**

### **✅ Já Implementado:**
- ✅ Interface principal completamente acessível
- ✅ Sistema de temas (5 opções)
- ✅ Fontes escaláveis (5 tamanhos)
- ✅ Navegação por teclado 100%
- ✅ Feedback sonoro opcional
- ✅ Suporte a screen readers
- ✅ Integração com sistema bancário core
- ✅ Carregamento de dados iniciais

### **🚧 Em Desenvolvimento (Placeholder):**
- 🚧 Tela de gestão de clientes
- 🚧 Tela de gestão de contas  
- 🚧 Tela de transações
- 🚧 Tela de investimentos
- 🚧 Tela de relatórios

**Nota:** As telas específicas mostram mensagens informativas indicando que serão implementadas. A arquitetura e acessibilidade estão prontas para expansão.

---

## 🏆 **SISTEMA COMPLETO**

Agora você tem **3 interfaces funcionais:**

### **1. 💻 Sistema Console**
```bash
# Executar sistema original
run.bat
```

### **2. 🧪 Testes Automatizados**  
```bash
# Executar todos os testes
run-tests.bat
```

### **3. 🎨 Interface Gráfica Acessível**
```bash
# Executar GUI acessível
cd gui
run-gui.bat
```

---

## 🌟 **DESTAQUES DE ACESSIBILIDADE**

### **♿ Para TODAS as Deficiências:**
- **👁️ Visual:** Temas, fontes, screen readers, alto contraste
- **👂 Auditiva:** Feedback visual, indicadores sem som
- **🦾 Motora:** Navegação teclado, áreas grandes de clique
- **🧠 Cognitiva:** Interface simples, linguagem clara, tooltips

### **🏅 Padrões Internacionais:**
- ✅ **WCAG 2.1 AA** - Diretrizes de acessibilidade web
- ✅ **Section 508** - Padrão do governo americano
- ✅ **EN 301 549** - Padrão europeu de acessibilidade

**🎯 RESULTADO: Uma interface verdadeiramente INCLUSIVA!**
