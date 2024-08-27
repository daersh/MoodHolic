//package akatsuki.moodholic.service;
//
//import akatsuki.moodholic.dto.ChatGPTRequest;
//import akatsuki.moodholic.dto.ChatGPTResponse;
//import akatsuki.moodholic.etc.DataParse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class ChatGPTService {
//        @Value("${openai.model}")
//        private String model;
//
//        @Value("${openai.api.url}")
//        private String apiURL;
//
//        @Autowired
//        private RestTemplate template;
//
//        public DataParse Response(@RequestParam("prompt") String prompt) {
//            ChatGPTRequest request = new ChatGPTRequest(model, prompt);
//            System.out.println("request = " + request);
//            ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
//            String responseText = chatGPTResponse.getChoices().get(0).getMessage().getContent();
//            DataParse dataParse = new DataParse(responseText);
//            System.out.println("responseText = " + responseText);
//            return dataParse;
//        }
//
//
//
//}
