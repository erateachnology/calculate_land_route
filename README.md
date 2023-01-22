This service is to find route for given two countries.

Used Technology
Java 17
Spring Boot
Maven
Docker
Setup

Enter folder path of application
Open terminal and run:
docker build -t route-service .

After application becomes docker image, run this command:
docker run -p9000:9000 route-service

Test
Route	HTTP Verb	POST body	Description	Response
localhost:9000/routing/CZE/ITA	GET	{
"route": [
"CZE",
"AUT",
"ITA"
]
}
