# News crawler

This is a projecct that collects live news from various sources (Google news API, Google news websites, Yahoo news website and a large collection of RSS feeds). All data are stored in a mongoDB instance.

### Dependences
```xml
		<dependency>
			<groupId>org.codeartisans</groupId>
			<artifactId>org.json</artifactId>
			<version>20150729</version>
		</dependency>
		<dependency>
			<groupId>com.google.code.gson</groupId>
			<artifactId>gson</artifactId>
			<version>2.4</version>
		</dependency>
		<dependency>
			<groupId>org.mongodb</groupId>
			<artifactId>mongo-java-driver</artifactId>
			<version>3.1.0</version>
		</dependency>
		<dependency>
			<groupId>org.seleniumhq.selenium</groupId>
			<artifactId>selenium-firefox-driver</artifactId>
			<version>2.48.2</version>
		</dependency>
		<dependency>
			<groupId>log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.17</version>
		</dependency>
```
You need both java and maven for compiling the classes.
You also need to run a mongoDB ("localhost", "27017").
Make sure you have firefox for sellinum.

The main classes in this project are:
  - main.googleNewsApiMongoDB (Googlenews API)
  - main.GoogleNewsScraper (Googlenews Website)
  - main.YahooNewsScraper (Yahoonews Website)
  - main.RSScollectorMultiThreading (RSS collector)

To compile each of them, you have to:
  - Modify the pom file to your desired class: 
```xml
<mainClass>main.googleNewsApiMongoDB</mainClass>
```
  - compile the code with: mvn clean compile assembly:single
  - Run the jar file with: 
```sh
java -Dlog4j.debug -Dlog4j.configuration=file:log4j.properties -jar googleNewsApiMongoDB.jar "ip address"
```

Note: each class have different args, check the method Usage() wthith each class file.
Note: each class has a number of static parameters that you have to make sure they apply to your configs.

#### main.googleNewsApiMongoDB
```json
{
	"_id" : ObjectId("5655080f05da27f47b0e777c"),
	"title" : "Turkey Russia jet: Marine killed in pilot rescue bid",
	"titleUrl" : "http://www.bbc.co.uk/news/world-middle-east-34917485",
	"content" : "A Russian marine has been killed on a helicopter mission to rescue the crew of a jet downed by Turkey near the Syrian border on Tuesday. He died when his helicopter came under fire from rebels in northern Syria, where the plane crashed. Rebel fire from&nbsp;...",
	"imageUrl" : "http://ichef.bbci.co.uk/news/624/cpsprodpb/5FCE/production/_86862542_030283823-1.jpg",
	"publisher" : "BBC News",
	"publishedDate" : "2015-11-25 00:33:45",
	"language" : "en",
	"engineSource" : "GoogleNewsAPIV1.0",
	"collectionDate" : "2015-11-25 00:59:59",
	"topic" : "headlines",
	"ned" : "uk"
}
```
#### main.GoogleNewsScraper
```json
{
	"_id" : ObjectId("56492b9a05da27d33d938ced"),
	"title" : "A France-US Anti-Islamist Alliance",
	"titleUrl" : "http://www.wsj.com/articles/a-france-u-s-anti-islamist-alliance-1447626342",
	"content" : "Even before the French-born Kouachi brothers went on a shooting rampage at the Charlie Hebdo satirical magazine in January, French officials knew their luck was running out.",
	"publisher" : "Wall Street Journal",
	"publishedDate" : "Sun, 15 Nov 2015 23:04:26 +0000",
	"topic" : "headlines",
	"NED" : "uk"
}
```
#### main.YahooNewsScraper
```json
{
	"_id" : ObjectId("564b695d05da270181fa36a4"),
	"title" : "Russia: Plane brought down by homemade …",
	"titleUrl" : "https://uk.news.yahoo.com/russia-plane-brought-down-homemade-explosive-device-084256761.html",
	"content" : "MOSCOW (AP) — A homemade explosive device brought down a Russian passenger plane over Egypt last month, the head of Russia's FSB security service said Tuesday, … More »",
	"publisher" : "Associated Press",
	"publishedDate" : "2015-11-17 16:41:02",
	"topic" : "africa",
	"engineSource" : "yahooNews",
	"collectionDate" : "2015-11-17 17:52:29"
}
```
#### main.RSScollectorMultiThreading
```json
{
	"_id" : ObjectId("564e8c1f05da27b9f7cb32db"),
	"title" : "Rep. Davis Attends Prism Grand Opening in East Windsor",
	"titleUrl" : "http://cthousegop.com/2015/03/rep-davis-attends-prism-grand-opening-in-east-windsor/",
	"content" : "<p>EAST WINDSOR – Rep. Christopher Davis (R-57) attended the open house celebration of Prism Analytical Technologies’ brand new Research and Development facility on North Road in East Windsor on March 27th, meeting with executives and engineers who explained the company’s operations and product. The company focuses on manufacturing and selling machinery and kits that collect [&#8230;]</p>\n<p>The post <a rel=\"nofollow\" href=\"http://cthousegop.com/2015/03/rep-davis-attends-prism-grand-opening-in-east-windsor/\">Rep. Davis Attends Prism Grand Opening in East Windsor</a> appeared first on <a rel=\"nofollow\" href=\"http://cthousegop.com\">Connecticut House Republicans</a>.</p>\n",
	"imageUrl" : "",
	"publisher" : "Connecticut House Republicans ",
	"publishedDate" : "2015-03-31 16:33:03",
	"language" : "en-US",
	"engineSource" : "RSScollectorV1.0",
	"collectionDate" : "2015-11-20 02:57:35",
	"RSSurl" : "http://cthousegop.com/feed/?cat=55%2C56"
}
```

License
----

MIT


