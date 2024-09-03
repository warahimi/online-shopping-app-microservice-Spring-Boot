package com.cwc.order_service.config;

import com.cwc.order_service.model.Order;
import com.cwc.order_service.model.OrderLineItems;
import com.cwc.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoadData implements CommandLineRunner {
    private final OrderRepository orderRepository;
    @Override
    public void run(String... args) throws Exception {
        if(orderRepository.findAll().size() ==0)
        {
            Order order = new Order();
            OrderLineItems orderLineItems1 = OrderLineItems
                    .builder()
                    .skuCode("Iphone_13")
                    .price(BigDecimal.valueOf(1300))
                    .quantity(2)
                    .build();
            OrderLineItems orderLineItems2 = OrderLineItems
                    .builder()
                    .skuCode("Iphone_14")
                    .price(BigDecimal.valueOf(1500))
                    .quantity(24)
                    .build();

            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderLineItemsList(new ArrayList<>(Arrays.asList(orderLineItems1,orderLineItems2)));
            orderRepository.save(order);
        }

    }
}
