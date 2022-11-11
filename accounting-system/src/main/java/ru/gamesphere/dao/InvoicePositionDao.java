package ru.gamesphere.dao;

import generated.Tables;
import generated.tables.records.InvoicePositionsRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.gamesphere.model.InvoicePosition;
import ru.gamesphere.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.INVOICE_POSITIONS;

public class InvoicePositionDao implements Dao<InvoicePosition> {

    private final static String GET_SQL = """
            SELECT *
            FROM invoice_positions
            WHERE id = ?
            """;

    private final static String ALL_SQL = """
            SELECT *
            FROM invoice_positions
            """;

    private final static String SAVE_SQL = """
            INSERT INTO invoice_positions (id, product_id, invoice_id, price, quantity)
            VALUES (?, ?, ?, ?, ?)
            """;

    private final static String UPDATE_SQL = """
            UPDATE invoice_positions
            SET product_id = ?, invoice_id = ?, price = ?, quantity = ?
            where id = ?
            """;

    private final static String DELETE_SQL = """
            DELETE FROM invoice_positions
            where id = ?
            """;

    private final static String GET_BY_INVOICE_ID_SQL = """
            SELECT *
            FROM invoice_positions
            WHERE invoice_id = ?
            """;

    @Override
    public @NotNull InvoicePosition get(int id) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            InvoicePositionsRecord invoicePosition = context.selectFrom(INVOICE_POSITIONS)
                    .where(INVOICE_POSITIONS.ID.eq(id))
                    .fetchOne();

            return new InvoicePosition(invoicePosition.getId(),
                    invoicePosition.getProductId(),
                    invoicePosition.getInvoiceId(),
                    invoicePosition.getPrice(),
                    invoicePosition.getQuantity());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull List<@NotNull InvoicePosition> all() {
        List<InvoicePosition> invoicePositions = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Result<InvoicePositionsRecord> record = context.selectFrom(INVOICE_POSITIONS)
                    .fetch();

            record.forEach(invoicePosition -> invoicePositions.add(
                            new InvoicePosition(invoicePosition.getId(),
                                    invoicePosition.getProductId(),
                                    invoicePosition.getInvoiceId(),
                                    invoicePosition.getPrice(),
                                    invoicePosition.getQuantity()
                            )
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invoicePositions;
    }

    @Override
    public void save(@NotNull InvoicePosition entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.insertInto(INVOICE_POSITIONS,
                            INVOICE_POSITIONS.ID,
                            INVOICE_POSITIONS.PRODUCT_ID,
                            INVOICE_POSITIONS.INVOICE_ID,
                            INVOICE_POSITIONS.PRICE,
                            INVOICE_POSITIONS.QUANTITY)
                    .values(entity.getId(), entity.getProductId(), entity.getInvoiceId(), entity.getPrice(), entity.getQuantity())
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull InvoicePosition entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.update(INVOICE_POSITIONS)
                    .set(INVOICE_POSITIONS.PRODUCT_ID, entity.getProductId())
                    .set(INVOICE_POSITIONS.INVOICE_ID, entity.getInvoiceId())
                    .set(INVOICE_POSITIONS.PRICE, entity.getPrice())
                    .set(INVOICE_POSITIONS.QUANTITY, entity.getQuantity())
                    .where(INVOICE_POSITIONS.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull InvoicePosition entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.delete(INVOICE_POSITIONS)
                    .where(INVOICE_POSITIONS.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvoicePosition> getByInvoiceId(int invoiceId) {
        List<InvoicePosition> invoicePositions = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Result<InvoicePositionsRecord> record = context.selectFrom(INVOICE_POSITIONS)
                    .where(INVOICE_POSITIONS.INVOICE_ID.eq(invoiceId))
                    .fetch();

            record.forEach(invoicePosition -> invoicePositions.add(
                            new InvoicePosition(invoicePosition.getId(),
                                    invoicePosition.getProductId(),
                                    invoicePosition.getInvoiceId(),
                                    invoicePosition.getPrice(),
                                    invoicePosition.getQuantity()
                            )
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invoicePositions;
    }
}
