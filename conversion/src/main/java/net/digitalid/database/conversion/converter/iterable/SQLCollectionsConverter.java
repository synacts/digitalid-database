package net.digitalid.database.conversion.converter.iterable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableHashSet;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.collections.readonly.ReadOnlySet;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.TypeUnknownException;
import net.digitalid.utility.conversion.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLCollectionsConverter extends SQLIterableConverter<Collection<?>> {
    
    @Override 
    protected @Nonnull @NullableElements Collection<?> getCollection(@Nonnull Object object) {
        Require.that(object instanceof Collection<?>).orThrow("The object is not an instance of Collection.");
        assert object instanceof Collection;
        
        return (Collection<?>) object;
    }
    
    private @Nonnull Class<?> getInstantiableClassForType(Class<?> type) throws TypeUnknownException {
        if (ReadOnlyList.class.isAssignableFrom(type)) {
            return FreezableArrayList.class;            
        } else if (List.class.isAssignableFrom(type)) {
            return FreezableArrayList.class;
        } else if (ReadOnlySet.class.isAssignableFrom(type)) {
            return FreezableHashSet.class;
        } else if (Set.class.isAssignableFrom(type)) {
            return FreezableHashSet.class;
        } else {
            throw TypeUnknownException.get(type);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Collection<?> recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result, @Nonnull @NonNullableElements Annotation[] annotations) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        
        final @Nullable @NullableElements Object[] elementValues = recoverNullableObjectArray(result, annotations);
        if (elementValues == null) {
            return null;
        }
        
        @Nonnull Class<?> collectionType;
        if (Modifier.isAbstract(type.getModifiers())) {
            try {
                collectionType = getInstantiableClassForType(type);
            } catch (TypeUnknownException e) {
                throw RecoveryException.get(type, "Cannot find an instantiable class.");
            }
        } else {
            collectionType = type;
        }
        @Nonnull Object collectionObject;
        try {
            collectionObject = collectionType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw RecoveryException.get(type, "Failed to instantiate an instance of type '" + type + "'", e);
        }
        
        final @Nonnull @NullableElements Collection<Object> collection = (Collection<Object>) collectionObject;
        Collections.addAll(collection, elementValues);
        return collection;
    }
    
}
