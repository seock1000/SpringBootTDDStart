# 스프링 부트 TDD - 입문부터 실전까지 정확하게

"스프링 부트 TDD - 입문부터 실전까지 정확하게" 인프런 강의의 실습 코드 저장소입니다.

## 응용프로그램 빌드

```bash
./gradlew build
```

## 응용프로그램 실행

```bash
./gradlew bootRun
```


## API 목록

### 판매자 회원가입

요청
- 메서드: POST
- 경로: /seller/signup
- 헤더: 
  ```
    Content-Type: application/json
  ```
- 본문:
  ```
     CreateSellerCommand {
        email: String,
        username: String,
        password: String
     }
  ```

- curl 명령 예시:
```bash
curl -i -X POST 'http://localhost:8080/seller/signup' \
-H 'Content-Type: application/json' \
-d '{
  "email": "seller1@example.com",
  "username": "seller1",
  "password": "seller1-password"
  }'
```

성공 응답:
- 상태 코드: 204 NO_CONTENT

정책
- 이메일 주소는 유일해야 한다.
- 사용자 이름은 유일해야 한다.
- 사용자 이름은 3자 이상의 영문자, 숫자, 하이픈, 밑줄문자로 구성되어야 한다.
- 비밀번호는 8자 이상의 문자로 구성되어야 한다.

테스트
- [x] 올바르게 요청하면 204 NO_CONTENT 응답을 반환한다.
- [x] email 속성이 지정되지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] email 속성이 올바른 형식을 따르지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] username 속성이 지정되지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] username 속성이 올바른 형식을 따르지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] username 속성이 올바른 형식을 따르면 204 NO_CONTENT 응답을 반환한다.
- [x] password 속성이 지정되지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] password 속성이 올바른 형식을 따르지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] 이미 존재하는 이메일 주소로 요청하면 400 BAD_REQUEST 응답을 반환한다.
- [x] 이미 존재하는 사용자 이름으로 요청하면 400 BAD_REQUEST 응답을 반환한다.
- [x] 비밀번호를 올바르게 암호화 한다.

### 판매자 접근 토큰 발행

요청
- 메서드: POST
- 경로: /seller/issueToken
- 본문
  ```
  IssueSellerToken {
    email: string,
    password: string
  }
  ```
- curl 명령 예시
  ```bash
  curl -i -X POST 'http://localhost:8080/seller/issueToken' \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "seller1@example.com",
    "password": "seller1-password"
  }'
  ```

성공 응답
- 상태코드: 200 OK

  본문
  ```
  AccessTokenCarrier {
    accessToken: string
  }
  ```

테스트
- [x] 올바르게 요청하면 200 OK 상태코드를 반환한다
- [x] 올바르게 요청하면 접근 토큰을 반환한다
- [x] 접근 토큰은 JWT 형식을 따른다
- [x] 존재하지 않는 이메일 주소가 사용되면 400 Bad Request 상태코드를 반환한다
- [x] 잘못된 비밀번호가 사용되면 400 Bad Request 상태코드를 반환한다


### 구매자 회원가입

요청
- 메서드: POST
- 경로: /shopper/signUp
- 헤더:
  ```
    Content-Type: application/json
  ```
- 본문:
  ```
     CreateShopperCommand {
        email: String,
        username: String,
        password: String
     }
  ```
- curl 명령 예시:
  - ```bash
    curl -i -X POST 'http://localhost:8080/shopper/signup' \
    -H 'Content-Type: application/json' \
    -d '{
      "email": "shopper1@example.com",
      "username": "shopper1",
      "password": "shopper1-password"
      }'
    ```
    
성공 응답:
- 상태 코드: 204 NO_CONTENT
- 본문: 없음

정책
- 이메일 주소는 유일해야 한다.
- 사용자 이름은 유일해야 한다.
- 사용자 이름은 3자 이상의 영문자, 숫자, 하이픈, 밑줄문자로 구성되어야 한다.
- 비밀번호는 8자 이상의 문자로 구성되어야 한다.

테스트
- [x] 올바르게 요청하면 204 NO_CONTENT 응답을 반환한다.
- [x] email 속성이 지정되지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] email 속성이 올바른 형식을 따르지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] username 속성이 지정되지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] username 속성이 올바른 형식을 따르지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] username 속성이 올바른 형식을 따르면 204 NO_CONTENT 응답을 반환한다.
- [x] password 속성이 지정되지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] password 속성이 올바른 형식을 따르지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] 이미 존재하는 이메일 주소로 요청하면 400 BAD_REQUEST 응답을 반환한다.
- [x] 이미 존재하는 사용자 이름으로 요청하면 400 BAD_REQUEST 응답을 반환한다.
- [x] 비밀번호를 올바르게 암호화 한다.

### 구매자 접근 토큰 발행

요청
- 메서드: POST
- 경로: /shopper/issueToken
- 본문
  ```
  IssueShopperToken {
    email: string,
    password: string
  }
  ```
- curl 명령 예시
  ```bash
  curl -i -X POST 'http://localhost:8080/shopper/issueToken' \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "shopper1@example.com"
    "password": "shopper1-password"
  }'
  ```
성공 응답
- 상태코드: 200 OK
  본문
  ```
  AccessTokenCarrier {
    accessToken: string
  }
  ```
테스트
- [ ] 올바르게 요청하면 200 OK 상태코드를 반환한다  
- [ ] 올바르게 요청하면 접근 토큰을 반환한다 // 코드변경이 아주 작은 부분으로 두 가지를 합쳐서 다룸
- [x] 올바르게 요청하면 200 OK 상태코드와 접근 토큰을 반환한다
- [x] 접근 토큰은 JWT 형식을 따른다
- [x] 존재하지 않는 이메일 주소가 사용되면 400 Bad Request 상태코드를 반환한다
- [x] 잘못된 비밀번호가 사용되면 400 Bad Request 상태코드를 반환한다


### 판매자 정보 조회

요청
- 메서드: GET
- 경로: /seller/me
- 헤더:
  ```
    Authorization: Bearer {token}
  ```
- curl 명령 예시:
  ```bash
  curl -i -X GET 'http://localhost:8080/seller/me' \
  -H 'Authorization
  : Bearer {token}'
  ```
성공 응답:
- 상태 코드: 200 OK
- 본문:
  ```
  SellerMeView {
    id: String(UUID),
    email: String,
    username: String
  }
  ```
  
테스트
- [x] 올바른 접근 토큰을 사용하면 200 OK 응답을 반환한다.
- [x] 접근 토큰을 사용하지 않으면 401 UNAUTHORIZED 응답을 반환한다.
- [x] 서로 다른 판매자의 식별자는 서로 다르다.
- [x] 같은 판매자의 식별자는 항상 같다.
- [x] 판매자의 기본 정보가 올바르게 설정된다.


### 구매자 정보 조회

요청
- 메서드: GET
- 경로: /shopper/me
- 헤더:
  ```
    Authorization: Bearer {token}
  ```
- curl 명령 예시:
  ```bash
  curl -i -X GET 'http://localhost:8080/shopper/me' \
  -H 'Authorization
  : Bearer {token}'
  ```
성공 응답:
- 상태 코드: 200 OK
- 본문:
  ```
  ShopperMeView {
    id: String(UUID),
    email: String,
    username: String
  }
  ```
테스트
- [x] 올바른 접근 토큰을 사용하면 200 OK 응답을 반환한다.
- [x] 접근 토큰을 사용하지 않으면 401 UNAUTHORIZED 응답을 반환한다.
- [x] 서로 다른 구매자의 식별자는 서로 다르다.
- [x] 같은 구매자의 식별자는 항상 같다.
- [x] 구매자의 기본 정보가 올바르게 설정된다.


### 판매자 상품 등록
요청
- 메서드: POST
- 경로: /seller/products
- 헤더:
  ```
    Authorization: Bearer {token}
    Content-Type: application/json
  ```
- 본문:
  ```
  RegisterProductCommand {
    name: String,
    imgUri: String,
    description: String,
    priceAmount: number,
    stockQuantity: number
  }
  ```
- curl 명령 예시:
  ```bash
  curl -i -X POST 'http://localhost:8080/seller/products
  -H 'Authorization: Bearer {token}'
  -H 'Content-Type: application/json' \
  -d '{
    "name": "상품 이름",
    "imgUrl": "https://example.com/product.jpg",
    "description": "상품 설명",
    "priceAmount": 10000,
    "stockQuantity": 50
  }'
  ```
성공 응답:
- 상태 코드: 201 CREATED
- 헤더:
  ```
    Location: /seller/products/{id}
  ```
  
테스트
- [x] 올바르게 요청하면 201 CREATED 응답을 반환한다.
- [x] 판매자가 아닌 사용자가 요청하면 403 FORBIDDEN 응답을 반환한다.
- [x] 이미지 URL이 올바른 형식을 따르지 않으면 400 BAD_REQUEST 응답을 반환한다.
- [x] 올바르게 요청하면 등록된 상품 정보에 접근하는 Location 헤더를 반환한다.

### 판매자 상품 조회
요청
- 메서드: GET
- 경로: /seller/products/{id}
- 헤더:
  ```
    Authorization: Bearer {token}
  ```
- curl 명령 예시:
  ```bash
  curl -i -X GET 'http://localhost:8080/seller/products
  -H 'Authorization: Bearer {token}'
  ```
성공 응답:
- 상태 코드: 200 OK
- 본문:
  ```
  SellerProductView {
    id: String(UUID),
    name: String,
    imgUrl: String,
    description: String,
    priceAmount: number,
    stockQuantity: number,
    registeredTimeUtc: String(YYYY-MM-DDTHH:mm:ss.sss)
  }
  ```
  
테스트
- [x] 올바른 접근 토큰을 사용하면 200 OK 응답을 반환한다.
- [x] 판매자가 아닌 사용자가 요청하면 403 FORBIDDEN 응답을 반환한다.
- [x] 존재하지 않는 상품 ID를 사용하면 404 NOT_FOUND 응답을 반환한다.
- [x] 다른 판매자가 등록한 상품 ID를 사용하면 404 NOT_FOUND 응답을 반환한다.
- [x] 상품 식별자를 올바르게 반환한다.
- [x] 상품 정보를 올바르게 반환한다.
- [x] 상품 등록 시간을 올바르게 반환한다.


### 판매자 상품 목록 조회
요청
- 메서드: GET 
- 경로: /seller/products
- 헤더:
  ```
    Authorization: Bearer {token}
  ```
- curl 명령 예시:
  ```bash
  curl -i -X GET 'http://localhost:8080/seller/products'
  -H 'Authorization: Bearer {token}'
  ```
성공 응답:
- 상태 코드: 200 OK
- 본문:
  ```
  ArrayCarrier<SellerProductView> {
    items: [SellerProductView {
      id: String(UUID),
      name: String,
      imgUrl: String,
      description: String,
      priceAmount: number,
      stockQuantity: number,
      registeredTimeUtc: String(YYYY-MM-DDTHH:mm:ss.sss)
    }]
  }
  ```

테스트
- [x] 올바르게 요청하면 200 OK 응답을 반환한다
- [x] 판매자가 등록한 모든 상품을 반환한다
- [x] 다른 판매자가 등록한 상품이 포함되지 않는다
- [ ] 상품 정보를 올바르게 반환한다
- [ ] 상품 등록 시간을 올바르게 반환한다
- [ ] 상품 목록을 등록시점 역순으로 정렬한다
