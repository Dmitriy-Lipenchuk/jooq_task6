/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.Keys;
import generated.Public;
import generated.tables.records.InvoicePositionsRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Check;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InvoicePositions extends TableImpl<InvoicePositionsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.invoice_positions</code>
     */
    public static final InvoicePositions INVOICE_POSITIONS = new InvoicePositions();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InvoicePositionsRecord> getRecordType() {
        return InvoicePositionsRecord.class;
    }

    /**
     * The column <code>public.invoice_positions.id</code>.
     */
    public final TableField<InvoicePositionsRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.invoice_positions.product_id</code>.
     */
    public final TableField<InvoicePositionsRecord, Integer> PRODUCT_ID = createField(DSL.name("product_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.invoice_positions.invoice_id</code>.
     */
    public final TableField<InvoicePositionsRecord, Integer> INVOICE_ID = createField(DSL.name("invoice_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.invoice_positions.price</code>.
     */
    public final TableField<InvoicePositionsRecord, Integer> PRICE = createField(DSL.name("price"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.invoice_positions.quantity</code>.
     */
    public final TableField<InvoicePositionsRecord, Integer> QUANTITY = createField(DSL.name("quantity"), SQLDataType.INTEGER.nullable(false), this, "");

    private InvoicePositions(Name alias, Table<InvoicePositionsRecord> aliased) {
        this(alias, aliased, null);
    }

    private InvoicePositions(Name alias, Table<InvoicePositionsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.invoice_positions</code> table reference
     */
    public InvoicePositions(String alias) {
        this(DSL.name(alias), INVOICE_POSITIONS);
    }

    /**
     * Create an aliased <code>public.invoice_positions</code> table reference
     */
    public InvoicePositions(Name alias) {
        this(alias, INVOICE_POSITIONS);
    }

    /**
     * Create a <code>public.invoice_positions</code> table reference
     */
    public InvoicePositions() {
        this(DSL.name("invoice_positions"), null);
    }

    public <O extends Record> InvoicePositions(Table<O> child, ForeignKey<O, InvoicePositionsRecord> key) {
        super(child, key, INVOICE_POSITIONS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<InvoicePositionsRecord, Integer> getIdentity() {
        return (Identity<InvoicePositionsRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<InvoicePositionsRecord> getPrimaryKey() {
        return Keys.INVOICE_POSITIONS_PKEY;
    }

    @Override
    public List<ForeignKey<InvoicePositionsRecord, ?>> getReferences() {
        return Arrays.asList(Keys.INVOICE_POSITIONS__INVOICE_POSITIONS_PRODUCT_ID_FKEY, Keys.INVOICE_POSITIONS__INVOICE_POSITIONS_INVOICE_ID_FKEY);
    }

    private transient Products _products;
    private transient Invoices _invoices;

    /**
     * Get the implicit join path to the <code>public.products</code> table.
     */
    public Products products() {
        if (_products == null)
            _products = new Products(this, Keys.INVOICE_POSITIONS__INVOICE_POSITIONS_PRODUCT_ID_FKEY);

        return _products;
    }

    /**
     * Get the implicit join path to the <code>public.invoices</code> table.
     */
    public Invoices invoices() {
        if (_invoices == null)
            _invoices = new Invoices(this, Keys.INVOICE_POSITIONS__INVOICE_POSITIONS_INVOICE_ID_FKEY);

        return _invoices;
    }

    @Override
    public List<Check<InvoicePositionsRecord>> getChecks() {
        return Arrays.asList(
            Internal.createCheck(this, DSL.name("invoice_positions_price_check"), "((price >= 0))", true),
            Internal.createCheck(this, DSL.name("invoice_positions_quantity_check"), "((quantity > 0))", true)
        );
    }

    @Override
    public InvoicePositions as(String alias) {
        return new InvoicePositions(DSL.name(alias), this);
    }

    @Override
    public InvoicePositions as(Name alias) {
        return new InvoicePositions(alias, this);
    }

    @Override
    public InvoicePositions as(Table<?> alias) {
        return new InvoicePositions(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public InvoicePositions rename(String name) {
        return new InvoicePositions(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InvoicePositions rename(Name name) {
        return new InvoicePositions(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public InvoicePositions rename(Table<?> name) {
        return new InvoicePositions(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
