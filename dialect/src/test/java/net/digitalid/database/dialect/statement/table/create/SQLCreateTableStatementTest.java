package net.digitalid.database.dialect.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.string.SQLStringLiteralBuilder;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.constraint.SQLConstraintName;
import net.digitalid.database.dialect.identifier.constraint.SQLConstraintNameBuilder;
import net.digitalid.database.dialect.statement.SQLStatementTest;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLForeignKeyConstraint;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLForeignKeyConstraintBuilder;
import net.digitalid.database.unit.Unit;

import org.junit.Test;

public class SQLCreateTableStatementTest extends SQLStatementTest {
    
    @Test
    public void testCreateTableStatement() {
        final @Nonnull SQLColumnDeclaration firstColumnDeclaration = SQLColumnDeclarationBuilder.withName(firstColumn).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).withNotNull(true).withPrimaryKey(true).withAutoIncrement(true).build();
        final @Nonnull SQLColumnDeclaration secondColumnDeclaration = SQLColumnDeclarationBuilder.withName(secondColumn).withType(SQLTypeBuilder.withType(CustomType.BOOLEAN).build()).withNotNull(true).withDefaultValue(SQLBooleanLiteral.TRUE).build();
        final @Nonnull SQLColumnDeclaration thirdColumnDeclaration = SQLColumnDeclarationBuilder.withName(thirdColumn).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).withNotNull(true).withCheck(thirdColumn.between(SQLStringLiteralBuilder.withString("hello").build(), SQLStringLiteralBuilder.withString("world").build())).build();
        
        final @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnName> columns = ImmutableList.withElements(firstColumn);
        final @Nonnull SQLReference reference = SQLReferenceBuilder.withTable(qualifiedTable).withColumns(columns).withUpdateOption(SQLReferenceOption.CASCADE).withDeleteOption(SQLReferenceOption.RESTRICT).build();
        final @Nonnull SQLConstraintName constraintName = SQLConstraintNameBuilder.withString("self_reference").build();
        final @Nonnull SQLForeignKeyConstraint foreignKeyConstraint = SQLForeignKeyConstraintBuilder.withColumns(columns).withReference(reference).withName(constraintName).build();
        
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatementBuilder.withTable(qualifiedTable).withColumnDeclarations(ImmutableList.withElements(firstColumnDeclaration, secondColumnDeclaration, thirdColumnDeclaration)).withTableConstraints(ImmutableList.withElements(foreignKeyConstraint)).build();
        assertThat(SQLDialect.unparse(createTableStatement, Unit.DEFAULT)).isEqualTo("CREATE TABLE IF NOT EXISTS \"default\".\"test_table\" (\"first_column\" INT NOT NULL PRIMARY KEY AUTOINCREMENT, \"second_column\" BOOLEAN NOT NULL DEFAULT (TRUE), \"third_column\" VARCHAR(64) NOT NULL CHECK ((\"third_column\") BETWEEN (\"hello\") AND (\"world\")), CONSTRAINT \"self_reference\" FOREIGN KEY (\"first_column\") REFERENCES \"default\".\"test_table\" (\"first_column\") ON DELETE RESTRICT ON UPDATE CASCADE)");
    }
    
}
