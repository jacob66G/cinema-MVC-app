package com.example.Cinema.repository;

import com.example.Cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userName = :email")
    boolean findByUserEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.phone = :phone")
    boolean findByUserPhone(@Param("phone") String phone);
}
