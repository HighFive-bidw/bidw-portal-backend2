package com.ktds.bidw.aiquery.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktds.bidw.aiquery.service.AiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class AiClientServiceImpl implements AiClientService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final int maxTokens;
    private final boolean useMockLlm;

    public AiClientServiceImpl(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${service.ai.api-key}") String apiKey,
            @Value("${service.ai.model:claude-3-haiku-20240307}") String model,
            @Value("${service.ai.max-tokens:4000}") int maxTokens,
            @Value("${service.ai.use-mock-llm:false}") boolean useMockLlm) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
        this.maxTokens = maxTokens;
        this.useMockLlm = useMockLlm;

        log.info("AI Client Service initialized with model: {}, Mock LLM: {}", model, useMockLlm);
    }

    @Override
    public String askAi(String prompt, List<Map<String, String>> context, String dataContext) {
        // 모의 LLM 사용 여부 확인
        if (useMockLlm) {
            return generateLocalResponse(prompt, dataContext);
        }

        try {
            // Anthropic API용 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");

            // 시스템 메시지 내용 (별도의 최상위 파라미터로 사용됨)
            String systemMessage = "당신은 데이터 분석 전문가입니다. 주어진 리포트 데이터를 기반으로 사용자의 질문에 정확하고 통찰력 있는 답변을 제공해주세요. "
                    + "데이터 컨텍스트: " + dataContext;

            // 메시지 구성 (시스템 메시지 제외)
            List<Map<String, String>> messages = buildMessages(prompt, context);

            // Anthropic API 요청 본문 구성 - 수정된 부분
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("system", systemMessage); // 시스템 메시지는 별도 최상위 파라미터로 전달
            requestBody.put("messages", messages);   // 사용자 메시지와 AI 응답만 포함

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.debug("Sending request to Anthropic API");

            // Anthropic API 호출
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.anthropic.com/v1/messages",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.debug("Received response from Anthropic API");

            // 응답 파싱
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            // Claude API 응답 구조에 맞게 파싱
            if (responseMap.containsKey("content")) {
                List<Map<String, Object>> contentList = (List<Map<String, Object>>) responseMap.get("content");
                if (!contentList.isEmpty() && contentList.get(0).containsKey("text")) {
                    return (String) contentList.get(0).get("text");
                }
            }

            return "AI 응답을 파싱할 수 없습니다.";

        } catch (Exception e) {
            log.error("AI API 호출 중 오류 발생", e);
            return "AI 서비스 연결 중 문제가 발생했습니다: " + e.getMessage();
        }
    }

    // 사용자 메시지와 기존 대화 컨텍스트만 포함하는 메시지 목록 생성 (시스템 메시지 제외)
    private List<Map<String, String>> buildMessages(String prompt, List<Map<String, String>> context) {
        // 대화 컨텍스트 처리를 위한 새 리스트 생성
        List<Map<String, String>> messages = new ArrayList<>();

        // 기존 컨텍스트가 있으면 추가 (시스템 메시지는 제외)
        if (context != null && !context.isEmpty()) {
            for (Map<String, String> message : context) {
                if (!"system".equals(message.get("role"))) { // 시스템 메시지 제외
                    messages.add(message);
                }
            }
        }

        // 사용자 질문 추가
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        return messages;
    }

    /**
     * 로컬에서 간단한 응답을 생성합니다.
     */
    private String generateLocalResponse(String prompt, String dataContext) {
        // 리포트 이름 추출
        String reportName = "리포트";
        if (dataContext != null && dataContext.contains("리포트 이름:")) {
            int start = dataContext.indexOf("리포트 이름:") + 7;
            int end = dataContext.indexOf("\n", start);
            if (end > start) {
                reportName = dataContext.substring(start, end).trim();
            }
        }

        // 간단한 응답 생성
        String lowercasePrompt = prompt.toLowerCase();

        if (lowercasePrompt.contains("안녕") || lowercasePrompt.contains("hi") ||
                lowercasePrompt.contains("hello")) {
            return "안녕하세요! BIDW 포털의 AI 도우미입니다. " + reportName + "에 대해 어떤 점이 궁금하신가요?";
        }

        if (lowercasePrompt.contains("요약") || lowercasePrompt.contains("설명") ||
                lowercasePrompt.contains("알려줘") || lowercasePrompt.contains("보여줘")) {
            return reportName + "의 주요 내용을 분석했습니다. 이 데이터에서는 특정 패턴이 관찰되며, "
                    + "주목할 만한 트렌드가 있습니다. 더 구체적인 질문을 해주시면 자세한 분석을 제공해 드리겠습니다.";
        }

        // 기본 응답
        return reportName + "에 대한 질문을 분석 중입니다. 현재 시스템은 제한된 기능으로 운영 중이며, "
                + "기본적인 응답만 제공 가능합니다. 보다 구체적인 질문이나 다른 주제에 대해 문의해주세요.";
    }
}