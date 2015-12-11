balance service with akka http
===============================

Simple balance service that shows how to use `akka-http` and `akka-streams` to build microservices.

start server:

    sbt run

see home page

    http://localhost:8080/

list balances

    http://localhost:8080/balances

execute collect total with callback

    curl -i http://localhost:8080/balances/mark/total?callback=http://localhost:8080/ext/callback/balance/total

    HTTP/1.1 200 OK
    Server: akka-http/2.3.12
    Content-Type: text/plain; charset=UTF-8
    Content-Length: 2

    OK


check health

    http://localhost:8080/health