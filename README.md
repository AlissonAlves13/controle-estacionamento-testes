# Sistema de Controle de Estacionamento

Projeto Final da disciplina de **Testes de Software** (IFPB - Campus Campina Grande), focado na aplicação prática de técnicas de teste funcional, testes unitários automatizados, testes parametrizados, testes de integração, regressão, requisitos não funcionais e integração contínua.

## Integrantes da Equipe
* **Alisson Alves dos Santos**
* **Pedro Henrique Rodrigues da Silva**

---

## Descrição do Sistema
O sistema gerencia o fluxo de entrada e saída de veículos em um estacionamento rotativo, controlando a ocupação de vagas, a validação de placas, o cálculo tarifário por tempo de permanência e categoria de veículo, além de regras de autorização para liberação manual gratuita.

---

## Principais Regras de Negócio
* **R1 — Capacidade e Lotação:** O estacionamento possui capacidade máxima definida na inicialização (ex.: `1` a `50` vagas). Entradas são rejeitadas caso o pátio esteja lotado.
* **R2 — Validação de Placa e Duplicidade:** A placa deve obedecer ao formato brasileiro padrão (`ABC1234`) ou Mercosul (`ABC1D23`). Entradas nulas, vazias ou de veículos que já possuem ticket em aberto são rejeitadas.
* **R3 — Tolerância e Faixas de Cobrança:**
  * Permanência de até `15` minutos: isenta (`R$ 0,00`).
  * Permanência de `16` a `60` minutos (1ª hora): tarifa fixa de `R$ 10,00`.
  * Permanência acima de `60` minutos: `R$ 10,00` + `R$ 5,00` por hora adicional ou fração.
  * Horário de saída anterior ao horário de entrada deve lançar exceção (`IllegalArgumentException`).
* **R4 — Desconto por Categoria e Convênio:** Motocicletas pagam `50%` do valor calculado; automóveis pagam `100%`; veículos cadastrados como mensalistas são isentos de cobrança avulsa.
* **R5 — Segurança e Controle de Acesso:** Apenas usuários com perfil `GERENTE` podem realizar a liberação gratuita manual de um veículo. Operadores comuns (`OPERADOR`) ou requisições sem identificação são bloqueados com `SecurityException`.

---

## Tecnologias Utilizadas
* **Java 17**
* **Apache Maven** (gerenciamento de dependências e ciclo de build)
* **JUnit 5 (Jupiter)** (framework de testes unitários, parametrizados e de integração)
* **Git e GitHub** (versionamento com branches e Pull Requests)
* **GitHub Actions** (execução automatizada da suíte de testes a cada `push` e `pull_request`)

---

## Como Executar o Projeto e os Testes

### Pré-requisitos
* JDK 17 ou superior instalado
* Apache Maven 3.8+ instalado

### Executar a suíte completa de testes
Na raiz do projeto, execute:
```bash
mvn test