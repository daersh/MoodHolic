//package akatsuki.moodholic.controller;
//
//import akatsuki.moodholic.dto.ChatGPTRequest;
//import akatsuki.moodholic.dto.ChatGPTResponse;
//import akatsuki.moodholic.etc.DataParse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequestMapping("/api/v1/chatGpt")
//public class ChatGPTController {
//
//    @Value("${openai.model}")
//    private String model;
//
//    @Value("${openai.api.url}")
//    private String apiURL;
//
//    @Autowired
//    private RestTemplate template;
//
//    @GetMapping("/chat")
//    public String Response(@RequestParam("prompt") String prompt) {
//        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
//        System.out.println("request = " + request);
//        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
//        String responseText = chatGPTResponse.getChoices().get(0).getMessage().getContent();
//        DataParse dataParse = new DataParse(responseText);
//
//        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
//    }
//
//
//}
