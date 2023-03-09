package com.mhsolution.noronapi.repository.users;

import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;

import java.util.List;

public interface UserRepository {

    List<Users> findAll(int pageNum, int limit);

    List<Users> findAllByListId(List<Integer> listUserId);

    Users findByID(int id);

    void add(Users user);

    void update(Users user, int id);

    void delete(int id);

    boolean usersExistsByEmail(String email);
}
