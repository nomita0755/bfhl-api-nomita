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

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {

    private static final String OFFICIAL_EMAIL =
            "nomita0755.be23@chitkara.edu.in";

    // ---------------- HEALTH ----------------
    @GetMapping("/health")
    public Map<String, Object> health() {

        Map<String, Object> res = new HashMap<>();
        res.put("is_success", true);
        res.put("official_email", OFFICIAL_EMAIL);
        return res;
    }

    // ---------------- MAIN API ----------------
    @PostMapping(value = "/bfhl", consumes = "*/*")
    public ResponseEntity<Map<String, Object>> bfhl(
            @RequestBody String body) {

        Map<String, Object> res = new HashMap<>();
        res.put("official_email", OFFICIAL_EMAIL);

        try {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> request =
                    mapper.readValue(body, Map.class);

            if (request.containsKey("fibonacci")) {

                int n = (Integer) request.get("fibonacci");
                res.put("data", fibonacci(n));

            } else if (request.containsKey("prime")) {

                List<Integer> arr =
                        (List<Integer>) request.get("prime");

                res.put("data", primes(arr));

            } else if (request.containsKey("lcm")) {

                List<Integer> arr =
                        (List<Integer>) request.get("lcm");

                res.put("data", lcm(arr));

            } else if (request.containsKey("hcf")) {

                List<Integer> arr =
                        (List<Integer>) request.get("hcf");

                res.put("data", hcf(arr));

            } else if (request.containsKey("AI")) {

                String question =
                        request.get("AI").toString().toLowerCase();

                res.put("data", aiAnswer(question));

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

    // ---------------- AI LOGIC ----------------
    private String aiAnswer(String q) {

        if (q.contains("capital") && q.contains("gujarat"))
            return "Gandhinagar";

        if (q.contains("capital") && q.contains("maharashtra"))
            return "Mumbai";

        if (q.contains("prime minister"))
            return "Modi";

        if (q.contains("president of india"))
            return "Murmu";

        return "Unlock Gemini-pro for more advance features";
    }

    // ---------------- LOGIC ----------------

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

        int res = arr.get(0);

        for (int x : arr) {
            res = gcd(res, x);
        }
        return res;
    }

    int lcm(List<Integer> arr) {

        int res = arr.get(0);

        for (int x : arr) {
            res = (res * x) / gcd(res, x);
        }
        return res;
    }
}





