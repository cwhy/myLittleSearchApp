package myLittleSearchApp;

import static spark.Spark.*;

public class HelloLittleSpark {
    public static void main(String[] args) {
		HelloLucene.index();
    	staticFileLocation("public");
        get("/search/all/:name", (req, res) -> {
        	String sResult = HelloLucene.search(req.params(":name"));
        	return sResult;
        });
        get("/search/:type/:name", (req, res) -> {
        	return req.params(":type") +  req.params(":name");
        });
    }
}
