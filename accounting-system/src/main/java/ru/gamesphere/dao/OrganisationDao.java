package ru.gamesphere.dao;

import generated.tables.records.OrganisationsRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.gamesphere.model.Organisation;
import ru.gamesphere.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.ORGANISATIONS;

public class OrganisationDao implements Dao<Organisation> {

    @Override
    public @NotNull Organisation get(int id) {
        try (Connection connection = ConnectionManager.open()) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final OrganisationsRecord record = context.selectFrom(ORGANISATIONS)
                    .where(ORGANISATIONS.ID.eq(id))
                    .fetchOne();

            return new Organisation(record.getId(),
                    record.getName(),
                    record.getInn(),
                    record.getBankAccount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull List<@NotNull Organisation> all() {
        List<Organisation> organisations = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Result<OrganisationsRecord> result = context.selectFrom(ORGANISATIONS)
                    .fetch();

            result.forEach(organisation -> organisations.add(
                            new Organisation(organisation.getId(),
                                    organisation.getName(),
                                    organisation.getInn(),
                                    organisation.getBankAccount()
                            )
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return organisations;
    }

    @Override
    public void save(@NotNull Organisation entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.insertInto(ORGANISATIONS,
                            ORGANISATIONS.ID,
                            ORGANISATIONS.NAME,
                            ORGANISATIONS.INN,
                            ORGANISATIONS.BANK_ACCOUNT)
                    .values(entity.getId(), entity.getName(), entity.getInn(), entity.getBankAccount())
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Organisation entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.update(ORGANISATIONS)
                    .set(ORGANISATIONS.NAME, entity.getName())
                    .set(ORGANISATIONS.INN, entity.getInn())
                    .set(ORGANISATIONS.BANK_ACCOUNT, entity.getBankAccount())
                    .where(ORGANISATIONS.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Organisation entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.delete(ORGANISATIONS)
                    .where(ORGANISATIONS.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
