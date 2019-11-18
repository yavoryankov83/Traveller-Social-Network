package com.telerikacademy.socialnetwork.repositories;

import com.telerikacademy.socialnetwork.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  List<User> findAllByEnabledTrueOrderByUpdateDateDesc();

  List<User> findAllByEnabledTrueOrderByUpdateDateDesc(Pageable pageable);

  Optional<User> findByIdAndEnabledTrue(Integer userId);

  Optional<User> findByUsernameAndEnabledTrue(String username);

  boolean existsUserByUsernameAndEnabledTrue(String username);

  boolean existsUserByEmailAndEnabledTrue(String email);

  boolean existsUserByUsernameAndEnabledTrueAndIdIsNotLike(String userName, Integer userId);

  boolean existsUserByEmailAndEnabledTrueAndIdIsNotLike(String userEmail, Integer userId);

  List<User> findAllByEnabledTrueAndUsernameStartingWithOrEmailStartingWithOrFirstNameStartingWithOrLastNameStartingWithOrderByUpdateDateDesc(String username,
                                                                                                                                              String email,
                                                                                                                                              String firstName,
                                                                                                                                              String lastName);
  @Query(value = "select count(a.username) from  authorities a join users u on a.username = u.username\n" +
          "where a.username = ?1", nativeQuery = true)
  Integer getUserRolesCount(String username);
}
