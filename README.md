## Test
mvn test

## Usage (launch a Tomcat8.5)
mvn package cargo:run 

Inside src/test/resources
curl  -X POST -H "Content-Type: application/json" -d '@sites.json' http://localhost:8090/sample/crawl/multiple

Needs a MongoDB instance running localhost:27017 (or change default values in defaults.properties / DefaultsConfiguration)

Connect to MongoDB (crawler) and verify results using

db.CrawlResults.find({"RESULT":"MARFEELIZABLE"})
db.CrawlResults.find({"RESULT":"NOT_MARFEELIZABLE"})
db.CrawlResults.find({"RESULT":"ERROR"})
