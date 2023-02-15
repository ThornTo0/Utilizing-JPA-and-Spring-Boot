package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member limit" +
                                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<OrderSimpleQueryDTO> findOrderDTOs() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.OrderSimpleQueryDTO(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDTO.class)
                .getResultList();
        )
    }

//    public List<Order> findAllByString(OrderSearch orderSearch) {
//    }

//    public List<Order> findAll(OrderSearch orderSearch){}
}
