package ru.gamesphere.dao;

import generated.tables.records.InvoicesRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.gamesphere.model.Invoice;
import ru.gamesphere.model.InvoicePosition;
import ru.gamesphere.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.INVOICES;

public class InvoiceDao implements Dao<Invoice> {

    @NotNull
    private final InvoicePositionDao invoicePositionDao;

    public InvoiceDao(@NotNull InvoicePositionDao invoicePositionDao) {
        this.invoicePositionDao = invoicePositionDao;
    }

    @Override
    public @NotNull Invoice get(int id) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            InvoicesRecord record = context.selectFrom(INVOICES)
                    .where(INVOICES.ID.eq(id))
                    .fetchOne();
            List<InvoicePosition> invoicePositions = invoicePositionDao.getByInvoiceId(id);

            return new Invoice(record.getId(),
                    Timestamp.valueOf(record.getDate()),
                    record.getOrganisationId(),
                    invoicePositions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull List<@NotNull Invoice> all() {
        List<Invoice> invoices = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Result<InvoicesRecord> record = context.selectFrom(INVOICES)
                    .fetch();

            record.forEach(invoice -> invoices.add(
                            new Invoice(invoice.getId(),
                                    Timestamp.valueOf(invoice.getDate()),
                                    invoice.getOrganisationId(),
                                    invoicePositionDao.getByInvoiceId(invoice.getId())
                            )
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invoices;
    }

    @Override
    public void save(@NotNull Invoice entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.insertInto(INVOICES,
                            INVOICES.ID,
                            INVOICES.DATE,
                            INVOICES.ORGANISATION_ID)
                    .values(entity.getId(), entity.getDate().toLocalDateTime(), entity.getOrganisationId())
                    .execute();

            for (InvoicePosition position : entity.getInvoicePositions()) {
                invoicePositionDao.save(position);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Invoice entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.update(INVOICES)
                    .set(INVOICES.DATE, entity.getDate().toLocalDateTime())
                    .set(INVOICES.ORGANISATION_ID, entity.getOrganisationId())
                    .where(INVOICES.ID.eq(entity.getId()))
                    .execute();

            for (InvoicePosition position : entity.getInvoicePositions()) {
                invoicePositionDao.update(position);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Invoice entity) {
        try (Connection connection = ConnectionManager.open()) {
            DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.delete(INVOICES)
                    .where(INVOICES.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
