package net.digitalid.database.conversion.testenvironment.embedded;

import javax.annotation.Nonnull;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;

import net.digitalid.database.annotations.metadata.Embedd;


/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class EmbeddedConvertibles  {
    
    @Embedd
    public final @Nonnull Convertible1 convertible1;
    
    @Embedd
    public final @Nonnull Convertible2 convertible2;
    
    EmbeddedConvertibles(@Nonnull Convertible1 convertible1, @Nonnull Convertible2 convertible2) {
        this.convertible1 = convertible1;
        this.convertible2 = convertible2;
    }
    
}
