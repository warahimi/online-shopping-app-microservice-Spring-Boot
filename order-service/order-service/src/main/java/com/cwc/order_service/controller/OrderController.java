package com.cwc.order_service.controller;

import com.cwc.order_service.dto.OrderRequest;
import com.cwc.order_service.dto.OrderResponse;
import com.cwc.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest)
    {
        orderService.placeOrder(orderRequest);
        return new ResponseEntity<>("Order Successfully created", HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders()
    {
        List<OrderResponse> allOrders = orderService.getAllOrders();
        if(allOrders.size() >0)
        {
            return new ResponseEntity<>(allOrders, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
