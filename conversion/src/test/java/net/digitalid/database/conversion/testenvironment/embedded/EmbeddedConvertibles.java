package net.digitalid.database.conversion.testenvironment.embedded;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.converter.Convertible;

/**
 *
 */
public class EmbeddedConvertibles implements Convertible {
    
    public final @Nonnull Convertible1 convertible1;
    
    public final @Nonnull Convertible2 convertible2;
    
    private EmbeddedConvertibles(@Nonnull Convertible1 convertible1, @Nonnull Convertible2 convertible2) {
        this.convertible1 = convertible1;
        this.convertible2 = convertible2;
    }
    
    public static @Nonnull EmbeddedConvertibles get(@Nonnull Convertible1 convertible1, @Nonnull Convertible2 convertible2) {
        return new EmbeddedConvertibles(convertible1, convertible2);
    }
    
}
