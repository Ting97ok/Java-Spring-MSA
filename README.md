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
3. 해당 강의에서 제안할 구현
  - 비즈니스 결정을 내리진 않지만 수행할 작업을 정의
  - 트랜잭션으로 묶어야 하는 도메인 로직과 그 외 로직을 aggregation ( 로직 조합 ) 하는 역할로 한정

#### Interfaces Layer
1. Request Parameter 는 제거하고, 외부에 리턴하는 Response 도 최소한을 유지 하도록 하자
 - API는 한번 외부에 오픈하면 바꿀 수 없는 것으라고 생각하자
 - 처음부터 제한적으로 설계하고 구현해야한다

2. 서비스간의 통신 기술은 Interfaces Layer 에서만 사용되도록 하자
 - 사용자한테 받는 데이터는 Domain layer에서 사용하는 일은 없어야 한다

## 권장하는 구현 방식
### 개발 디자인 문서를 작성한 후에 구현을 시작하자
- 개발 시작 전 개발 디자인 문서를 작성하고 동료와 함께 공유한다
- 서비스 구현에 대한 목표와 설계, 제약 사항 등을 미리 생각해본 후에 개발을 시작한다
- 개발 디자인 문서를 작성 후 동료들과 리뷰 과정을 거친다
- 인수인게 과정에서도 개발 디자인 문서를 전달하면 구조 파악을 비교적 빠르게 진행할 수 있다

### 테이블 설계를 먼저 하지 말고 핵심 도메인 도출을 먼저하자
- 테이블은 도메인 객체를 영속화 하기 위한 그릇 정도의 역할로 생각하는 것이 좋다
- 주요 요구사항과 제약 등을 감안하면서 핵심 도메인 객체를 도출
- 특정 기능을 수행하기 위해 도메인 간에 주고 받아야 하는 메시지를 먼저 정의한다

### 변수명, 메서드명에 많은 신경을 쓰자
- 현업에서 사용하는 보편적인 언어를 최대한 반영
- 네이밍 규칙을 세우고 운영하자

### APi 명세에서 request 와 response 의 프로퍼티는 필수값만 유지되도록 한다
- request, response를 최소화한다
- 요구하는 request 가 많다는 것은 해당 메서드가 처리해야 하는 것이 많다는 의미일 수 있다
- response에 목적에 맞지 않는 불필요한 응답이 있다면 추후 특정 프로퍼티 제거는 쉽지않다

### setter 는 쓰지 않거나 최소화한다
- setter는 캡슐화를 깨뜨리는 주범
- 도메인 객체를 생성할 때에는 생성자를 활용하여 생성한다
- 상태 변경은 적절한 메서드로 사용한다
- 이를 통해 정합성을 유지하는데 도움이 된다

### 트랜젝션의 사용과 범위 설정은 여러 번 고민 후 결정하자
- 트랜잭션은 도메인의 데이터의 정합성을 위한 필수 기능
- 범위가 작을수록 좋다
- 외부 3rd party 호출 로직이 있다면 적절한 타임 아웃 설정 필수
- 필요에 따라서 트랜잭션 내에 포함시키지 않는 것도 고려한다

### 도메인 객체가 무조건 DB에 저장되는 것은 아니다

### try-catch 는 필요한 경우가 아니라면 쓰지 말자
- 불필요한 try-catch 는 로직 흐름을 파악하기 어렵게 만들고 코드의 양만 늘리는 주범이다
- Exception 을 catch 했을 때 추가적인 로직 구현이 필요한 경우에만 선언한다
- 그 외에는 발생한 Execption 을 그대로 throw 하는 것이 구현도 깔끔하고 로직 흐름에도 좋다

### 꼭 필요한 상태 (Status) 만 선언하자
- 상태값은 도메인의 Identity 만큼 중요한 프로퍼티다 도메인 객체의 상태를 판별하고, 그에 따라 적절한 로직 실행이 가능하기 때문
- 너무 세분화된 상태값은 도메인을 이해하기 어렵게 만든다
- 도메인 객체를 통해 이루고자 하는 기능을 만들어 내는데 필요한 상태만 선언해도 좋다

### 주요 로직의 테스트 코드는 본인을 살리는 길이다
- 개발 과정 중에도 수시로 요구사항이 변경되고 추가된다 그 때 테스트 코드를 돌려가며 피드백을 받으면 버그 찾기도 쉽고 기능 구현도 빠르게 진행할 수 있다

### 우선 목표한 기능이 동작하도록 구현하는데 집중한다

### 무조건 정석대로 구현할 필요는 없다

### 대체키에 대하여
- 엔티티의 식별자는 외부에 오픈하거나 오용되지 않도록 주의하고 대체키를 오픈하는 것이 좋다

#### 대체키 사용 이유
- Long 형태의 유저 아이디로 URL 사용시 숫자 조작으로 침범가능
- 외부 연동 서비스를 이용할 때 DB변경으로 인해 PK 형식이 변경되면 수정이 어렵다

#### 구현
- String 기반의 token 생성 unique index 로 설정하여 사용
- 1천만건 이하 = 상관없다 1천만건 이상 = UUID 를 rearranged하여 사용 (생성 날짜는 고유적으로 남는데 그것을 맨 앞으로 옮기는 작업이라고 함)

### 의존성 역전 원칙 (DIP)
- 추상화 레벨이 높은 상위 수준의 모듈이 추상화 레벨이 낮은 하위 모듈에 의존하면 안된다

### 로깅의 중요성
- 서비스를 이용하는 유저가 에러를 접하게 되면 개발팀은 이를 즉시 인지할 수 있어야 한다
- API 레벨의 request, response 로깅은 추후 리펙토링에 필수 항목이 될 수 있다

### API 응답 체계
- API 응답 체꼐는 시스템 전체가 일관되고 명확한 형태를 가진다면, 어떠한 구조든 문제가 되지 않는다
- 현재 강의에서는 CommonControllerAdvice 에서 별도로 정의한 BaseException 을 확장한 Exception 이 발생하는 경우,  
  코드를 구현한 개발자가 이미 인지한 예외상황 이라고 판단하여 http status : 200 에 result : FAIL 을 내려준다  
  200 FAIL의 경우 해당 도메인 담당자가 해결한다
- 예상치 못한 Exception 발생하는 경우 500 FAIL을 발생시키고 모니터링이 필수다

### 보상 트랜잭션
- MSA 환경에서는 하나의 프로세스를 완성하기 위해서는 여러 도메인의 연산을 모두 처리해야 하는 경우가 있다
- MSA 구조에서는 전체 rollback이 쉽지 않다 ( 서버가 나뉘어 있기 때문 )
- 이를 위한 개념이 보상 트랜잭션이다
- 보상 트랜잭션은 일반 데이터베이스 롤백과는 달리 추가적인 실행을 통해 기존 작업을 보완하는 방식에 가깝다

