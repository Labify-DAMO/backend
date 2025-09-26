package com.labify.backend.auth.repository;

import com.labify.backend.auth.entity.RefreshToken;
import com.labify.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional <RefreshToken> findByTokenHash(String tokenHash);
    List<RefreshToken> findAllByUserAndRevokedFalse(User user);
    List<RefreshToken> findAllByUser(User user);
}
