package com.example.Cinema.repository;

import com.example.Cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    User findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.phone = :phone AND u.id != :userId")
    User findByPhoneAndIdIsNot(@Param("phone") String phone, @Param("userId") long userId);
}
