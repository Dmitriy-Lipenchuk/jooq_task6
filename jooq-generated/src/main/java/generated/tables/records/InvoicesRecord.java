/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import generated.tables.Invoices;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InvoicesRecord extends UpdatableRecordImpl<InvoicesRecord> implements Record3<Integer, LocalDateTime, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.invoices.id</code>.
     */
    public InvoicesRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.invoices.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.invoices.date</code>.
     */
    public InvoicesRecord setDate(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.invoices.date</code>.
     */
    public LocalDateTime getDate() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>public.invoices.organisation_id</code>.
     */
    public InvoicesRecord setOrganisationId(Integer value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.invoices.organisation_id</code>.
     */
    public Integer getOrganisationId() {
        return (Integer) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, LocalDateTime, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Integer, LocalDateTime, Integer> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Invoices.INVOICES.ID;
    }

    @Override
    public Field<LocalDateTime> field2() {
        return Invoices.INVOICES.DATE;
    }

    @Override
    public Field<Integer> field3() {
        return Invoices.INVOICES.ORGANISATION_ID;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public LocalDateTime component2() {
        return getDate();
    }

    @Override
    public Integer component3() {
        return getOrganisationId();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public LocalDateTime value2() {
        return getDate();
    }

    @Override
    public Integer value3() {
        return getOrganisationId();
    }

    @Override
    public InvoicesRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public InvoicesRecord value2(LocalDateTime value) {
        setDate(value);
        return this;
    }

    @Override
    public InvoicesRecord value3(Integer value) {
        setOrganisationId(value);
        return this;
    }

    @Override
    public InvoicesRecord values(Integer value1, LocalDateTime value2, Integer value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InvoicesRecord
     */
    public InvoicesRecord() {
        super(Invoices.INVOICES);
    }

    /**
     * Create a detached, initialised InvoicesRecord
     */
    public InvoicesRecord(Integer id, LocalDateTime date, Integer organisationId) {
        super(Invoices.INVOICES);

        setId(id);
        setDate(date);
        setOrganisationId(organisationId);
    }

    /**
     * Create a detached, initialised InvoicesRecord
     */
    public InvoicesRecord(generated.tables.pojos.Invoices value) {
        super(Invoices.INVOICES);

        if (value != null) {
            setId(value.getId());
            setDate(value.getDate());
            setOrganisationId(value.getOrganisationId());
        }
    }
}
