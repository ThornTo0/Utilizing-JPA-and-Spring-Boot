package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    // Entity를 직접 노출하기 때문에 사용하지 마라!!
    @GetMapping("/api/v1/orders")
    public List<Order> orderV1(){
//        List<Order> all = orderRepository.findAllByString(new OrderSearch());
//        for (Order order : all) {
//            order.getMember().getName();
//            order.getDelivery().getAddress();
//            List<OrderItem> orderItems = order.getOrderItems();
//            orderItems.stream().forEach( o -> o.getItem().getName());
//        }

        return null;
    }

    //
    @GetMapping("/api/v2/orders")
    public List<OrderDTO> orderV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        orders.stream().map(o -> new OrderDTO(o))
                .collect(Collectors.toList());

        return collect;
    }

    @Getter
    static class OrderDTO{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        // Entity에 의존하는 모든 것들을 다 끊어야 된다.
        // OrderItem로 Entity이므로 DTO로 변경하여야 한다.
//      private List<OrderItem> orderItems;

        private List<OrderItemDTO> orderItems;


        public OrderDTO(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDTO(orderItem))
                    .collect(Collectors.toList());
        }

        @Getter
        static class OrderItemDTO{

            private String itemName;  //상품명
            private int orderPrice; // 주문 가격
            private int count; // 주문 수량
            public OrderItemDTO(OrderItem orderItem){
                this.itemName = orderItem.getItem().getName();
                this.orderPrice = orderItem.getItem().getPrice();
                this.count = orderItem.getCount();
            }
        }
    }
}
