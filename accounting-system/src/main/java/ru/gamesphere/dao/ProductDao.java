package ru.gamesphere.dao;

import generated.tables.records.ProductsRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.gamesphere.model.Product;
import ru.gamesphere.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.PRODUCTS;

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
}
