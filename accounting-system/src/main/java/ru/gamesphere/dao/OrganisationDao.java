package ru.gamesphere.dao;

import generated.tables.records.OrganisationsRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import ru.gamesphere.model.Organisation;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.ConnectionManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.*;
import static generated.Tables.INVOICE_POSITIONS;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.field;

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

    public static void getFirstTenOrganisationsByProductQuantity() {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Table<?> a = table(
                    select(INVOICES.ORGANISATION_ID.as("org_id"), INVOICE_POSITIONS.QUANTITY.as("quantity"))
                            .from(INVOICE_POSITIONS)
                            .innerJoin(INVOICES)
                            .on(INVOICE_POSITIONS.INVOICE_ID.eq(INVOICES.ID))
            ).as("a");

            Result<Record2<String, Serializable>> records = context.select(ORGANISATIONS.NAME.as("org_name"),
                            coalesce(sum(a.field(INVOICE_POSITIONS.QUANTITY)), 0).as("quantity"))
                    .from(a)
                    .rightJoin(ORGANISATIONS).on(a.field("org_id", ORGANISATIONS.ID.getType()).eq(ORGANISATIONS.ID))
                    .groupBy(field("org_name"))
                    .orderBy(field("quantity").desc())
                    .limit(10)
                    .fetch();

            records.forEach(record -> System.out.println(record.get("org_name") + " " + record.get("quantity")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getOrganisationsByProductQuantity(@NotNull List<Product> products,@NotNull List<Integer> thresholds) {
        if (products.size() != thresholds.size()) {
            throw new IllegalStateException("For each product thresholds must be specified");
        }

        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Condition condition = noCondition();

            for (int i = 0; i < products.size(); i++) {
                condition = condition.or(INVOICE_POSITIONS.PRODUCT_ID.eq(products.get(i).getId())
                        .and(sum(INVOICE_POSITIONS.QUANTITY).greaterThan(BigDecimal.valueOf(thresholds.get(i)))));
            }

            Table<?> a = table(
                    select(INVOICES.ORGANISATION_ID.as("org_id"), sum(INVOICE_POSITIONS.QUANTITY).as("quantity"))
                            .from(INVOICE_POSITIONS)
                            .innerJoin(INVOICES)
                            .on(INVOICE_POSITIONS.INVOICE_ID.eq(INVOICES.ID))
                            .groupBy(INVOICE_POSITIONS.PRODUCT_ID, INVOICES.ORGANISATION_ID)
                            .having(condition)
            ).as("a");

            Result<Record2<String, BigDecimal>> records = context.select(ORGANISATIONS.NAME.as("org_name"),
                            sum(a.field(INVOICE_POSITIONS.QUANTITY)).as("quantity"))
                    .from(a)
                    .innerJoin(ORGANISATIONS).on(a.field("org_id", ORGANISATIONS.ID.getType()).eq(ORGANISATIONS.ID))
                    .groupBy(field("org_name"))
                    .orderBy(field("quantity").desc())
                    .fetch();

            records.forEach(record -> System.out.println(record.get("org_name") + " " + record.get("quantity")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getCompanyProductListByPeriod(@NotNull Timestamp start, @NotNull Timestamp end) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Table<?> a = table(
                    select(INVOICES.ID.as("id"),
                            INVOICES.ORGANISATION_ID)
                            .from(INVOICES)
                            .where(field("date").between(start, end))
            ).as("a");

            Table<?> b = table(
                    select(ORGANISATIONS.NAME.as("org_name"),
                            a.field("id", INVOICES.ID.getType()).as("invoice_id"))
                            .from(a)
                            .rightJoin(ORGANISATIONS)
                            .on(a.field("organisation_id", INVOICES.ORGANISATION_ID.getType()).eq(ORGANISATIONS.ID))
            ).as("b");

            Result<Record2<String, String>> records = context.select(b.field("org_name", ORGANISATIONS.NAME.getType()),
                            PRODUCTS.NAME.as("product_name"))
                    .from(b)
                    .leftJoin(INVOICE_POSITIONS)
                    .on(b.field("invoice_id", INVOICES.ID.getType()).eq(INVOICE_POSITIONS.INVOICE_ID))
                    .leftJoin(PRODUCTS)
                    .on(INVOICE_POSITIONS.PRODUCT_ID.eq(PRODUCTS.ID))
                    .groupBy(b.field("org_name", ORGANISATIONS.NAME.getType()), field("product_name"))
                    .orderBy(b.field("org_name", ORGANISATIONS.NAME.getType()))
                    .fetch();

            for (Record2<String, String> record : records) {
                String productName = record.get("product_name") == null ? "" : record.get("product_name", String.class);
                System.out.println(record.get("org_name") + "\t"
                        + productName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
