# Java-Spring-MSA
## 프로젝트 구조 및 설계
### 도메인 주도 설계 (DDD)
복잡한 도메인을 모델링하고 표현력있게 설계하는 것을 DDD 라고 한다

### Layer 구조
레이어간의 참조 관계에서는 단방향 의존을 유지하고,  
계층간 호출에서는 인터페이스를 통한 호출이 되도록 한다

### Layer 별 특징과 역할
- 사용자 인터페이스 (interfaces) : 사용자에게 정보를 보여주고 사용자의 명령을 해석하는 역할
  - Controller
  - Dto
  - Mapper (Converter)
- 응용 계층 (application) : 업무상 중요하거나 다른 시스템의 응용 계층과 상호 작용하는데 필요한 계층
  - Facade
- 도메인 계층 (doamain) : 업무 개념, 업무 상황 정보, 업무 규칙을 표현하는 계층 업무용 소프트웨어의 핵심
  - Entity
  - Service
  - Command
  - Criteria
  - Info
  - Reader
  - Store
  - Executor
  - Factory (interface)
- 인프라 스트럭쳐 계층 (infrastructure) : 상위 계층을 지원하는 일반화된 기술적 기능 제공
  - low level 구현체

### Layer 간 참조 관계
- application 과 Infrastructure 는 domain layer 를 바라 보게 하고 양방향 참조는 허용하지 않게 한다
- domain layer 는 low level 의 기술에 상관없이 독립적으로 존재할 수 있어야 한다

### Layer 별 구현 상세
#### Domain Layer
1. Service 에서는 해당 도메인의 전체 흐름을 파악할 수 있도록 구현한다
  - 이를 위해 추상화 레벨을 많이 높인다
  - 어떤 업무를 어떤 순서로 처리했는지가 중요한 관심사
  - 적절한 인터페이스 사용
  - 세세한 기술 구현은 Infrastructure 의 implements 클래스가 담당

2. 모든 클래스명이 Service 로 선언될 필요는 없다
  - 주요 도메인의 흐름을 관리하는 Service 하나로 유지
  - 서포트 역할을 하는 클래스는 Service 이외의 네이밍

3. Service 간에는 참조 관계를 가지지 않도록 한다
  - Service 간에는 참조 관계를 가지지 않도록 원칙을 세우는 것이 좋다
  - Service 내의 로직은 추상화 수준을 높게
  - 실제 구현체는 잘게 쪼개어 구현

#### Infrastructure Layer
1. domain layer 에 사용되는 추상화된 인터페이스를 구현한다
2. 추상화된 인터페이스를 구현하는 것이므로 비교적 구현에서 자유도를 높게 가져간다
3. 구현체 간에 참조 관계를 허용한다
  - 로직의 재활용을 위해 구현체를 의존 관계로 활용해도 된다
  - 순환 참조가 발생하지 않도록 적절한 상하관계를 정의하는 것이 좋다

4. @Component를 활용한다
 - 동일한 bean 이라도 @Service 와 @Component 를 구분하여 선언하여 명시적인 의미 부여

#### Application Layer
1. 트랜잭션으로 묶여야 하는 도메인 로직과 그 외의 로직을 aggregation 하는 역할로 한정
2. 클래스 네이밍은 Facade 로 정한다

#### Interfaces Layer
1. Request Parameter 는 제거하고, 외부에 리턴하는 Response 도 최소한을 유지 하도록 하자
 - API는 한번 외부에 오픈하면 바꿀 수 없는 것으라고 생각하자
 - 처음부터 제한적으로 설계하고 구현해야한다

2. 서비스간의 통신 기술은 Interfaces Layer 에서만 사용되도록 하자
 - 사용자한테 받는 데이터는 Domain layer에서 사용하는 일은 없어야 한다
