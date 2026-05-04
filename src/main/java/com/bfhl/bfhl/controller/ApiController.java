package com.bfhl.bfhl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bfhl.bfhl.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {

    private static final String OFFICIAL_EMAIL = "nomita0755.be23@chitkara.edu.in";

    private final GeminiService geminiService;

    public ApiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // ---------------- GET /health ----------------
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> res = new HashMap<>();
        res.put("is_success", true);
        res.put("official_email", OFFICIAL_EMAIL);
        return ResponseEntity.ok(res);
    }

    // ---------------- POST /bfhl ----------------
    @PostMapping(value = "/bfhl", consumes = "*/*")
    public ResponseEntity<Map<String, Object>> bfhl(@RequestBody String body) {

        Map<String, Object> res = new HashMap<>();
        res.put("official_email", OFFICIAL_EMAIL);

        if (body == null || body.isBlank()) {
            res.put("is_success", false);
            return ResponseEntity.badRequest().body(res);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> request = mapper.readValue(body, Map.class);

            if (request == null || request.isEmpty()) {
                res.put("is_success", false);
                return ResponseEntity.badRequest().body(res);
            }

            if (request.containsKey("fibonacci")) {

                Object val = request.get("fibonacci");
                if (!(val instanceof Integer)) {
                    res.put("is_success", false);
                    return ResponseEntity.badRequest().body(res);
                }
                int n = (Integer) val;
                if (n < 0) {
                    res.put("is_success", false);
                    return ResponseEntity.badRequest().body(res);
                }
                res.put("data", fibonacci(n));

            } else if (request.containsKey("prime")) {

                Object val = request.get("prime");
                if (!(val instanceof List)) {
                    res.put("is_success", false);
                    return ResponseEntity.badRequest().body(res);
                }
                List<Integer> arr = (List<Integer>) val;
                res.put("data", primes(arr));

            } else if (request.containsKey("lcm")) {

                Object val = request.get("lcm");
                if (!(val instanceof List) || ((List<?>) val).isEmpty()) {
                    res.put("is_success", false);
                    return ResponseEntity.badRequest().body(res);
                }
                List<Integer> arr = (List<Integer>) val;
                res.put("data", lcm(arr));

            } else if (request.containsKey("hcf")) {

                Object val = request.get("hcf");
                if (!(val instanceof List) || ((List<?>) val).isEmpty()) {
                    res.put("is_success", false);
                    return ResponseEntity.badRequest().body(res);
                }
                List<Integer> arr = (List<Integer>) val;
                res.put("data", hcf(arr));

            } else if (request.containsKey("AI")) {

                Object val = request.get("AI");
                if (val == null || val.toString().isBlank()) {
                    res.put("is_success", false);
                    return ResponseEntity.badRequest().body(res);
                }
                String question = val.toString();
                res.put("data", geminiService.askGemini(question));

            } else {
                res.put("is_success", false);
                return ResponseEntity.badRequest().body(res);
            }

            res.put("is_success", true);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            res.put("is_success", false);
            return ResponseEntity.internalServerError().body(res);
        }
    }

    // ---------------- MATH LOGIC ----------------

    List<Integer> fibonacci(int n) {
        List<Integer> list = new ArrayList<>();
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            list.add(a);
            int c = a + b;
            a = b;
            b = c;
        }
        return list;
    }

    boolean isPrime(int x) {
        if (x < 2) return false;
        for (int i = 2; i * i <= x; i++) {
            if (x % i == 0) return false;
        }
        return true;
    }

    List<Integer> primes(List<Integer> arr) {
        List<Integer> ans = new ArrayList<>();
        for (int x : arr) {
            if (isPrime(x)) ans.add(x);
        }
        return ans;
    }

    int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    int hcf(List<Integer> arr) {
        int result = arr.get(0);
        for (int x : arr) {
            result = gcd(result, x);
        }
        return result;
    }

    int lcm(List<Integer> arr) {
        int result = arr.get(0);
        for (int x : arr) {
            result = (result / gcd(result, x)) * x;
        }
        return result;
    }
}





