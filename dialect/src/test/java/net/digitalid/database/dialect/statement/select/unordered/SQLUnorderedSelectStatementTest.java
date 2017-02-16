package net.digitalid.database.dialect.statement.select.unordered;

import javax.annotation.Nonnull;

import net.digitalid.utility.immutable.ImmutableList;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteralBuilder;
import net.digitalid.database.dialect.identifier.column.SQLQualifiedColumn;
import net.digitalid.database.dialect.identifier.column.SQLQualifiedColumnBuilder;
import net.digitalid.database.dialect.statement.SQLStatementTest;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLGroupClause;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLGroupClauseBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLSimpleSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLSimpleSelectStatementBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.columns.SQLAllColumnsBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.join.SQLJoinClause;
import net.digitalid.database.dialect.statement.select.unordered.simple.join.SQLJoinClauseBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.join.SQLJoinOperator;
import net.digitalid.database.dialect.statement.select.unordered.simple.join.constraints.SQLUsingJoinConstraint;
import net.digitalid.database.dialect.statement.select.unordered.simple.join.constraints.SQLUsingJoinConstraintBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLJoinSource;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLJoinSourceBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLTableSource;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLTableSourceBuilder;
import net.digitalid.database.unit.Unit;

import org.junit.Test;

public class SQLUnorderedSelectStatementTest extends SQLStatementTest {
    
    @Test
    public void testUnorderedSelectStatement() {
        final @Nonnull SQLTableSource tableSource = SQLTableSourceBuilder.withSource(qualifiedTable).build();
        final @Nonnull SQLUsingJoinConstraint joinConstraint = SQLUsingJoinConstraintBuilder.withColumns(columns).build();
        final @Nonnull SQLJoinClause joinClause = SQLJoinClauseBuilder.withOperator(SQLJoinOperator.INNER).withLeftSource(tableSource).withRightSource(tableSource).withConstraint(joinConstraint).build();
        final @Nonnull SQLJoinSource joinSource = SQLJoinSourceBuilder.withSource(joinClause).withAlias(tableAlias).build();
        final @Nonnull SQLQualifiedColumn qualifiedColumn = SQLQualifiedColumnBuilder.withTable(tableAlias).withColumn(firstColumn).build();
        final @Nonnull SQLNumberComparisonBooleanExpression whereClause = qualifiedColumn.equal(SQLNumberLiteralBuilder.withValue(8l).build());
        final @Nonnull SQLGroupClause groupClause = SQLGroupClauseBuilder.withColumns(ImmutableList.withElements(firstColumn)).withExpression(whereClause).build();
        final @Nonnull SQLSimpleSelectStatement selectStatement = SQLSimpleSelectStatementBuilder.withColumns(ImmutableList.withElements(SQLAllColumnsBuilder.withTable(tableAlias).build())).withSources(ImmutableList.withElements(joinSource)).withDistinct(true).withWhereClause(whereClause).withGroupClause(groupClause).build();
        assertThat(SQLDialect.unparse(selectStatement, Unit.DEFAULT)).isEqualTo("SELECT DISTINCT \"t\".* FROM ((\"default\".\"test_table\") INNER (\"default\".\"test_table\") USING (\"first_column\", \"second_column\", \"third_column\")) AS \"t\" WHERE (\"t\".\"first_column\") = (8) GROUP BY \"first_column\" HAVING (\"t\".\"first_column\") = (8)");
    }
    
}
