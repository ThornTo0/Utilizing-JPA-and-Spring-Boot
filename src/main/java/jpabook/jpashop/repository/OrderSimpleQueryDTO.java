package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class OrderSimpleQueryDTO {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDTO(Long orderId, String name, LocalDateTime orderDate,
                               OrderStatus orderStatus, Address address){
        this.orderId = this.orderId;
        this.name = this.name;
        this.orderDate = this.orderDate;
        this.orderStatus = this.orderStatus;
        this.address = this.address;
    }
}
