package ru.gamesphere.dao;

import generated.tables.records.ProductsRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.field;

public class ProductDao implements Dao<Product> {

    @Override
    public @NotNull Product get(int id) {
        try (Connection connection = ConnectionManager.open()) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            ProductsRecord product = context.selectFrom(PRODUCTS)
                    .where(PRODUCTS.ID.eq(id))
                    .fetchOne();

            return new Product(product.getId(), product.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull List<@NotNull Product> all() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final Result<ProductsRecord> records = context.selectFrom(PRODUCTS)
                    .fetch();

            records.forEach(record -> products.add(new Product(record.get(PRODUCTS.ID), record.get(PRODUCTS.NAME))));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    @Override
    public void save(@NotNull Product entity) {
        try (Connection connection = ConnectionManager.open()) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.insertInto(PRODUCTS,
                            PRODUCTS.ID,
                            PRODUCTS.NAME)
                    .values(entity.getId(), entity.getName())
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Product entity) {
        try (Connection connection = ConnectionManager.open()) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.update(PRODUCTS)
                    .set(PRODUCTS.NAME, entity.getName())
                    .where(PRODUCTS.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Product entity) {
        try (Connection connection = ConnectionManager.open()) {
            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.delete(PRODUCTS)
                    .where(PRODUCTS.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void getQuantityAndSumOfProductsByPeriodForEachDay(@NotNull Timestamp start, @NotNull Timestamp end) {
        int totalSum = 0;
        int totalQuantity = 0;

        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Table<?> a = table(
                    select(PRODUCTS.NAME.as("name"),
                            INVOICE_POSITIONS.PRICE.as("price"),
                            INVOICE_POSITIONS.QUANTITY.as("quantity"),
                            INVOICE_POSITIONS.INVOICE_ID.as("invoice_id"))
                            .from(INVOICE_POSITIONS)
                            .innerJoin(PRODUCTS).on(INVOICE_POSITIONS.PRODUCT_ID.eq(PRODUCTS.ID))
            ).as("a");

            Result<Record4<LocalDateTime, String, BigDecimal, BigDecimal>> records = context.select(INVOICES.DATE.as("date"),
                            a.field("name", PRODUCTS.NAME.getType()).as("product_name"),
                            sum(a.field("price", INVOICE_POSITIONS.PRICE.getType())).as("sum_price"),
                            sum(a.field("quantity", INVOICE_POSITIONS.QUANTITY.getType())).as("sum_quantity"))
                    .from(a)
                    .innerJoin(INVOICES)
                    .on(INVOICES.ID.eq(a.field("invoice_id", INVOICE_POSITIONS.INVOICE_ID.getType())))
                    .where(field("date").between(start, end))
                    .groupBy(INVOICES.DATE, field("name"))
                    .orderBy(INVOICES.DATE)
                    .fetch();

            for (Record4<LocalDateTime, String, BigDecimal, BigDecimal> record : records) {
                int currentSum = record.get("sum_price", INVOICE_POSITIONS.PRICE.getType());
                int currentQuantity = record.get("sum_quantity", INVOICE_POSITIONS.QUANTITY.getType());
                totalSum += currentSum;
                totalQuantity += currentQuantity;

                System.out.println(Timestamp.valueOf(record.get("date", INVOICES.DATE.getType())) + "\t"
                        + record.get("product_name") + "\t"
                        + currentSum + " "
                        + currentQuantity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Total: " + totalSum + " " + totalQuantity);
    }

    public static void getAveragePriceByPeriod(@NotNull Timestamp start, @NotNull Timestamp end) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Table<?> a = table(
                    select(PRODUCTS.NAME.as("name"),
                            INVOICE_POSITIONS.PRICE.as("price"),
                            INVOICE_POSITIONS.INVOICE_ID.as("invoice_id"))
                            .from(INVOICE_POSITIONS)
                            .innerJoin(PRODUCTS).on(INVOICE_POSITIONS.PRODUCT_ID.eq(PRODUCTS.ID))
            ).as("a");

            Result<Record2<String, BigDecimal>> records = context.select(a.field("name", PRODUCTS.NAME.getType()).as("product_name"),
                            avg(a.field("price", INVOICE_POSITIONS.PRICE.getType())).as("average_price"))
                    .from(a)
                    .innerJoin(INVOICES)
                    .on(INVOICES.ID.eq(a.field("invoice_id", INVOICE_POSITIONS.INVOICE_ID.getType())))
                    .where(field("date").between(start, end))
                    .groupBy(a.field("name"))
                    .orderBy(a.field("name"))
                    .fetch();

            records.forEach(record -> System.out.println(record.get("product_name") + "\t" + record.get("average_price", Integer.class)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
