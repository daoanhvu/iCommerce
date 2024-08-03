package com.icommerce.shopping.repository;

import com.icommerce.shopping.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>,
        JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserIdAndPaid(Long userId, boolean paid);
}
