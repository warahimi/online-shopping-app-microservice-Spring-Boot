package com.cwc.order_service.service;

import com.cwc.order_service.dto.OrderLineItemsDto;
import com.cwc.order_service.dto.OrderRequest;
import com.cwc.order_service.dto.OrderResponse;
import com.cwc.order_service.model.Order;
import com.cwc.order_service.model.OrderLineItems;
import com.cwc.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // for constructor injection
public class OrderService {
    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequest)
    {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtosList().stream()
                .map(orderLineItemsDto -> mapOderListItemsDtoToOderListItems(orderLineItemsDto))
                .collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
    }
    private OrderLineItems mapOderListItemsDtoToOderListItems(OrderLineItemsDto orderLineItemsDto)
    {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    public List<OrderResponse> getAllOrders()
    {
        List<Order> orders = orderRepository.findAll();
        if(!orders.isEmpty())
        {
            List<OrderResponse> results = orders.stream()
                    .map(order -> mapOrderToDto(order))
                    .collect(Collectors.toList());
            return results;
        }
       return null;
    }


    private OrderResponse mapOrderToDto(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setId(order.getId());
        List<OrderLineItemsDto> orderLineItemsDtos = order.getOrderLineItemsList().stream()
                .map(o -> oderLineToOrderLineDto(o))
                .collect(Collectors.toList());

        orderResponse.setOrderLineItemsDtosList(orderLineItemsDtos);
        return orderResponse;
    }
    private OrderLineItemsDto oderLineToOrderLineDto(OrderLineItems orderLineItems)
    {
//        ModelMapper modelMapper = new ModelMapper();
//        return modelMapper.map(orderLineItems, OrderLineItemsDto.class);
        OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
        orderLineItemsDto.setId(orderLineItems.getId());
        orderLineItemsDto.setSkuCode(orderLineItems.getSkuCode());
        orderLineItemsDto.setQuantity(orderLineItems.getQuantity());
        orderLineItemsDto.setPrice(orderLineItems.getPrice());
        return orderLineItemsDto;
    }
}
