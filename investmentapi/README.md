## 투자 서비스를 제공하는 REST API 서버

### 요구 사항
**카카오페이의 부동산/신용투자 서비스**
- 사용자는 원하는 부동산/신용 투자 상품을 투자할 수 있습니다.
- 투자상품이 오픈될 때, 다수의 고객이 동시에 투자를 합니다.
- 투자 후 투자상품의 누적 투자모집 금액,투자자 수가 증가됩니다.
- 총 투자모집 금액 달성 시 투자는 마감되고 상품은 Soldout 됩니다.
- 사용자 식별값은 숫자 형태이며 "X-USER-ID"라는 HTTP Header로 전달됩니다.
- 투자 상품 조회 시 상품 모집기간(started_at, finished_at) 내의 상품만 응답합니다.
- 투자 시 총 투자금액을 넘어서면 sold-out 상태를 응답합니다.

<br>

### 기술 스택
- Java (OpenJDK 11)
- Spring Boot (2.5.12) + Embedded Tomcat
- MySQL (5.7)
- MyBatis (3.5.6)
- Lombok (1.18.22)

<br>

### 아키텍쳐
**Controller - Service - Mapper(Repository)**

#### Controller
- InvestingController : 투자 서비스 api 제공
- MetaDataController : 투자 상품 메타 데이터 등록 api 제공

#### Service
- InvestingService : 투자 서비스 api의 비즈니스 로직 구현
- DataService : 투자 상품 메타 데이터 및 테스트 데이터 등록/삭제 로직 구현

#### Mapper
- InvestingDBMapper : DB 쿼리가 설계된 인터페이스

#### DB Table 매핑 클래스
- ProductMetaEntity (product_meta) : 투자 상품 메타 데이터 테이블과 매핑
- ProductInvestingEntity (product_investing) : 투자 상품 데이터(현재 누적 투자금액, 투자자 수) 테이블과 매핑
- UserProductEntity (user_product) : 유저의 투자 상품 정보 테이블과 매핑

#### Presentation 클래스
- ProductDto : 투자 상품 메타 데이터와 상품 데이터를 포함
- UserProductDto : 투자 상품 메타정보와 자신의 투자정보 포함

#### 에러 처리
어플리케이션 내에서 임의로 발생시킨 InvestingException과 RuntimeException은 
인터셉트해서 Forbidden으로 응답하며, ResultCode 값과 메시지로 응답함

<br>

### API 명세
- GET : /investing/products
  - 투자 상품 정보 리스트 요청
  - 응답 : List<ProductDto>, json  
  

- POST : /investing/user/products/{product_id:int}/investing-amount/{investing_amount:long}
  - 상품 투자 요청 (유저데이터 생성 및 투자 정보 업데이트)
  - 응답 : 없음([]), json
  

- GET : /user/products
  - 유저의 투자 정보 요청
  - 응답 : List<UserProductProto>, json  

<br>

### 전제 조건
- 투자 상품 메타 데이터는 db에 세팅된다고 가정하고, 메타 데이터 업로드 시 투자정보 레코드도 생성된다고 가정한다.
- 한명이 하나의 투자상품에 중복 투자할 수 있다는 조건이 없으므로, 중복 투자 불가능 한 것으로 한다.
- 최대 투자 금액을 넘어서는 구매 요청 건은 허용하지 않는다.

<br>

### 핵심 문제
#### 동시에 다수의 유저가 투자 시 투자 정보 데이터의 무결성 문제  
- update 구문을 사용해서 최소한으로 해당 row만 lock을 걸고, 그 시점의 필드 값에 더하는 방식으로 처리.
#### 최대 투자 금액 도달 시점에 초과 할 수 있는 문제
- where절에 해당 레코드의 투자금액 필드값을 이용해 현재 투자금을 더해도 초과하지 않을 때에만 업데이트 처리.
```sql
UPDATE product_investing SET accumulated_investing_amount = accumulated_investing_amount + {add_investing_amount}, investing_user_count = investing_user_count + 1 
WHERE product_id = {product_id} AND (accumulated_investing_amount + {add_investing_amount}) <= {total_investing_amount}
```