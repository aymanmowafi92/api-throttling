# api-throttling
Test for Atypon

This implements an API throttling mechanisim with the below specifications:

3 dummy REST APIs where two of them have the throttling feature. The criteria for throttling depends on a specefic parameter in the request (its name is throttling); where if it exists, then the server should check the number of calls during one minute and make sure that it doesn't exceed 10 calls, if the number of calls is greater than 10, then the server should throttle the request for 2 seconds. Also, if the number of queued throttled requests exceeds 10 requests at any time, the server should return "Service Unavailable".

Due date : 4/Oct/2021

In order to test this, please run this program as spring boot app and from the postman you can send requests to any of the below dummy APIs:
1- http://localhost:8080/api-throttling/service1?throttling
2- http://localhost:8080/api-throttling/service2?throttling
3- http://localhost:8080/api-throttling/service3?throttling

Knowing that the throttling feature is enabled only on sevice1 and serice2.
