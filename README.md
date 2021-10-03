# api-throttling
Test for Atypon

This implements an API throttling mechanisim with the below specifications:

3 dumy REST APIs where two of them have the throttling feature. The criteria for throttling depends on a specefic parameter in the request (its name is throttling); where if it exists, then the server should check the number of calls during one minute and make sure that it doesn't exceed 10 calls, if the number of calls is greater than 10, then the server should throttle the request for 2 seconds. Also, if the number of queued throttled requests exceeds 10 requests at any time, the server should return "Service Unavailable".
