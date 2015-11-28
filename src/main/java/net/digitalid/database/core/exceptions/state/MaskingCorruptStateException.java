package net.digitalid.utility.database.exceptions.state;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.auxiliary.StringUtility;

/**
 * This exception allows to mask other exceptions as a corrupt state exception.
 */
@Immutable
public class MaskingCorruptStateException extends CorruptStateException {
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    /**
     * Creates a new masking corrupt state exception that masks the given cause.
     * 
     * @param cause the cause to be masked as a masking corrupt state exception.
     */
    protected MaskingCorruptStateException(@Nonnull Exception cause) {
        super(StringUtility.prependWithIndefiniteArticle(cause.getClass().getSimpleName(), true) + " is masked as a corrupt state exception.", cause);
    }
    
    /**
     * Returns a new masking corrupt state exception that masks the given cause.
     * 
     * @param cause the cause to be masked as a masking corrupt state exception.
     * 
     * @return a new masking corrupt state exception that masks the given cause.
     */
    @Pure
    public static @Nonnull MaskingCorruptStateException get(@Nonnull Exception cause) {
        return new MaskingCorruptStateException(cause);
    }
    
}
