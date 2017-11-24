package com.barclays.fraud.springboot

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SpringBootGatlingSimulation extends Simulation {

	val baseURL : String = {
			Option(System.getenv("MIN_TEST_URL")).getOrElse("http://localhost:8080")
	}

	val httpProtocol = http
		.baseURL(baseURL)
		.inferHtmlResources(BlackList(""".*\.css""", """.*\.js""", """.*\.ico"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.9,en-US;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val scn = scenario("MinTestSimulation")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))

	setUp(scn.inject(atOnceUsers(10))).protocols(httpProtocol).assertions(
		global.responseTime.max.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}