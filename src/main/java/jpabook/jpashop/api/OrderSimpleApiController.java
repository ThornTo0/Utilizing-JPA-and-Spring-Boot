package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne : (ManyToOne, OneToOne)
 * * * -> 양방향이 걸리는 위치는 @JsonIgnore를 다 걸어주어야 함!
 * * * -> 지연로딩으로 지정되어 있음(Proxy[byteBuddy 대신 들어가 있음(가짜 객체)])
 * * * * -> 객체자체에서 뽑을려고 하는데(Proxy 객체로는 접근이 불가능하여 발생하는 오류가 발생)
 * * * * -> 지연로딩 자체로 Null로 출력할 수 있게 하는 모듈이 존재함( 그러나 권장하지 않음! )
 * Order
 * Order -> Member
 * Order -< Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // 무조건 Entity를 외부로 노출하지 말아라!
    // 연관계가 있는 필요없는 정보까지 다 가져오게 됨
    // 쓸모없는 정보 노출 && 많은 쿼리문 호출 ==> 성능 저하 및 보안 이슈
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1(){
//       return orderRepository.findAllByString(new OrderSearch());
        return null;
    }

    // Entity -> DTO로 변경하여 위의 문제점을 해결
    // But!! --> N + 1 문제가 발생 ( order 조회 1번 + order(member) 조회 2번 + order(deliver) 조회 2번 )
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDTO> orderV2(){
//        return orderRepository.findAllByString(new OrderSearch()).stream()
//                .map(o -> new SimpleOrderDTO(o))
//                .collect(Collectors.toList());
        return null;
    }

    // 쿼리 1번만 나감 ( N + 1 문제를 튜닝하여 해결 )
    // 지연 로딩 자체가 일어나지 않음( Lazy or Eager를 걱정할 필요가 없음 )
    // 단점 : SQL로 Entity로 찍어서 다 가져왔다는 단점이 있긴 있음
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDTO> orderV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        // DTO로 변환
        List<SimpleOrderDTO> result = orders.stream()
                .map(o -> new SimpleOrderDTO(o))
                .collect(Collectors.toList());
        return result;
    }

    // DTO로 데이터를 가져오는 방법을 사용
    // 결과 : Select 절에서 내가 원하는 정보만을 select하는 이점이 존재함!
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDTO> orderV4(){
        return orderRepository.findOrderDTOs();
    }

    // 결론 : V3과 V4는 trade-off 관계 ( 상황 봐가면서 사용 )
    //  V3 장점/단점 : 코드를 재사용할 수 있음(코드가 깔끔), 네트워크를 많이 사용함
    //  V4 장점/단점 : 네트워크를 덜 사용함, 코드를 재사용하기 힘듦( 코드가 지저분 함 )
    //  화면에 종속적인 쿼리는 따로 분리해서 사용하면, V4를 사용하는 것이 유지보수 상에서는 좋을 수 있음

    @Data
    static class SimpleOrderDTO{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
