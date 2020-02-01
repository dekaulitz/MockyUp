package com.github.dekaulitz.mockyup;

//@SpringBootTest
class MockyupApplicationTests {

//    @Test
//    void contextLoads() {
//        String uri = "/ap.i/v1.0/auth/registr.ation";
//        String newUri = uri.replace(".", "_");
//        Assert.isTrue(uri.equals(newUri), "its not equal");
//    }

//    @Test
//    void parsingURIPath() {
//        String uri = "/api/v1.0/customer/123321/tes";
//        String path = "/api/v1.0/customer/*{userId}/tes";
//        String[] newUri = uri.split("/");
//        String[] newPaht = path.split("/");
//        for (int i = 0; i < newPaht.length; i++) {
//            if (!newPaht[i].equals(newUri[i])) {
//                if (newPaht[i].contains("*")) {
//                    newUri[i] = newPaht[i];
//                }
//            }
//        }
//        uri = String.join("/", newUri);
//        Assert.isTrue(uri.equals(path), "its not same");
//    }
//
//    @Test
//    void getOriginalPath() throws JsonProcessingException {
//       String example="{\r\n    \"header\": {\r\n        \"app-id\": \"1233212\"\r\n    },\r\n    \"response\": {\r\n        \"httpCode\": 500,\r\n        \"response\": {\r\n            \"statusCode\": 500,\r\n            \"message\": \"internal server error\"\r\n        }\r\n    }\r\n}";
//        MockExample mockExample= Json.mapper().readValue(example,MockExample.class);
//        System.out.println(mockExample.getProperty());
//        String uri="?path=/api/v1.0/customer/path?asdasdasdasd=asdasdsa&value=123";
//    }


}
