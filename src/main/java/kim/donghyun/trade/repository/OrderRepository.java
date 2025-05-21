package kim.donghyun.trade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kim.donghyun.trade.entity.Order;
import kim.donghyun.trade.entity.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByStatus(OrderStatus status);
	List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
