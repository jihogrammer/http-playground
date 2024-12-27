# HTTP Playground

## Features

단순히 로컬에서 실행만 하면 되는 툴?

---

- `curl` 명령어를 생성하는 게 바인딩할 필요가 있나?
  - 단순하게 "copy to curl" 버튼을 만드는 것이 나을 것 같은데?
  - 단축키: cmd + shift + C
- `run`
  - 단축키: cmd + enter
- 하단에 로그를 출력
  - `[${shortcut}] ${job}: ${description}`
  - `[${status_code}] ${url}\n${resonse}`
- scenario 구성
- environment 구성
- validation 구성
  - script 작성 시 에디터를 넣어야 하네,,,
  - HPQL(Http Playground Query Language)?
    - 단문 검증
      - `$.status.isOk()`
      - `$.json.result = 'OK'`
      - `$.json.result[*].contains = 'Jihogrammer'`
      - `$.json.result[first].name = 'Jihogrammer'`
      - `$.json.result[first]. = 'OK'`
      - `$.text.contains('Hello World')`
      - `$.text.containsIgnoreCase('Hello World')`

## Implementation

### curl 커맨드 생성


