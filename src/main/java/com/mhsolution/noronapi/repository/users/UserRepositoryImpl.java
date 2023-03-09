package com.mhsolution.noronapi.repository.users;

import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.Tables;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dslContext;

    private final TimeUtils currentDateTime;

    public UserRepositoryImpl(DSLContext dslContext,
                              TimeUtils currentDateTime) {
        this.dslContext = dslContext;
        this.currentDateTime = currentDateTime;
    }

    @Override
    public List<Users> findAll(int pageNum, int limit) {
        return dslContext.selectFrom(Tables.USERS)
                .where(Tables.USERS.DELETE_AT.isNull())
                .limit(limit)
                .offset((pageNum - 1) * limit)
                .fetchInto(Users.class);
    }

    @Override
    public List<Users> findAllByListId(List<Integer> listUserId) {
        return dslContext.selectFrom(Tables.USERS)
                .where(Tables.USERS.ID.in(listUserId))
                .fetchInto(Users.class);
    }

    @Override
    public Users findByID(int id) {
        return dslContext.selectFrom(Tables.USERS)
                .where(Tables.USERS.ID.eq(id))
                .fetchOneInto(Users.class);
    }

    @Override
    public void add(Users user) {
        dslContext.insertInto(Tables.USERS,
                        Tables.USERS.USERNAME,
                        Tables.USERS.EMAIL,
                        Tables.USERS.PHONE_NUMBER,
                        Tables.USERS.PASS,
                        Tables.USERS.AVATAR,
                        Tables.USERS.CREATE_AT,
                        Tables.USERS.UPDATE_AT,
                        Tables.USERS.DELETE_AT)
                .values(user.getUsername(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getPass(),
                        user.getAvatar(),
                        user.getCreateAt(),
                        user.getUpdateAt(),
                        user.getDeleteAt())
                .execute();

    }

    @Override
    public void update(Users user, int userId) {
        dslContext.update(Tables.USERS)
                .set(Tables.USERS.USERNAME, user.getUsername())
                .set(Tables.USERS.PHONE_NUMBER, user.getPhoneNumber())
                .set(Tables.USERS.EMAIL, user.getEmail())
                .set(Tables.USERS.UPDATE_AT, user.getUpdateAt())
                .where(Tables.USERS.ID.eq(userId))
                .execute();
    }

    @Override
    public void delete(int id) {
        dslContext.update(Tables.USERS)
                .set(Tables.USERS.DELETE_AT, currentDateTime.getCurrentDateTime())
                .where(Tables.USERS.ID.eq(id))
                .execute();
    }

    @Override
    public boolean usersExistsByEmail(String email) {
        return dslContext.fetchExists(
                dslContext.selectOne()
                        .from(Tables.USERS)
                        .where(Tables.USERS.EMAIL.eq(email))
        );
    }
}
