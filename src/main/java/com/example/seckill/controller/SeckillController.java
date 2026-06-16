package com.example.seckill.controller;

import com.example.seckill.interceptor.LoginInterceptor;
import com.example.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final SeckillService seckillService;

    @PostMapping("/order")
    public ResponseEntity<String> doSeckill(@RequestParam Long goodsId) {
        Long userId = LoginInterceptor.USER_HOLDER.get();
        try {
            String responseMsg = seckillService.seckill(userId, goodsId);
            return ResponseEntity.ok(responseMsg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}