### Environments
- Spring
- Java
- MySQL
- MyBatis

### 테이블 구성
- 투자 상품 meta : ProductMetaEntity
- 투자 정보(현재 누적 투자금액, 투자자 수) : ProductInvestingEntity
- 유저 구매 정보 : UserProductEntity

### 전제 조건
- 투자 상품 meta 데이터는 db에 세팅된다고 가정하고, meta 데이터 업로드 시 투자정보 레코드도 생성된다고 가정한다.
- 한명이 하나의 투자상품에 중복 투자할 수 있다는 조건이 없으므로, 중복 투자 불가능 한 것으로 한다.
- 최대 투자 금액을 넘어서는 구매 요청 건은 허용하지 않는다.

### 핵심 문제
#### 동시에 다수의 유저가 투자 시 투자 정보 데이터의 무결성 문제  
- update 구문을 사용해서 최소한으로 해당 row만 lock을 걸고, 그 시점의 필드 값에 더하는 방식으로 처리.
#### 최대 투자 금액 도달 시점에 초과 할 수 있는 문제
- where절에 해당 레코드의 투자금액 필드값을 이용해 현재 투자금을 더해도 초과하지 않을 때에만 업데이트 처리.
```sql
UPDATE product_investing SET accumulated_investing_amount = accumulated_investing_amount + {add_investing_amount}, investing_user_count = investing_user_count + 1 
WHERE product_id = {product_id} AND (accumulated_investing_amount + {add_investing_amount}) <= {total_investing_amount}
```