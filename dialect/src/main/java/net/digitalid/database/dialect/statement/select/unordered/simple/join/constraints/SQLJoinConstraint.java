package net.digitalid.database.dialect.statement.select.unordered.simple.join.constraints;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;

/**
 * The constraint of a join clause.
 * 
 * @see SQLOnJoinConstraint
 * @see SQLUsingJoinConstraint
 */
@Immutable
public interface SQLJoinConstraint extends SQLNode {}
