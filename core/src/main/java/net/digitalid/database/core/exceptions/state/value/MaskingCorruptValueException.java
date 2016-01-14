package net.digitalid.database.core.exceptions.state.value;

import javax.annotation.Nonnull;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.system.auxiliary.StringUtility;

/**
 * This exception allows to mask other exceptions as a corrupt value exception.
 */
@Immutable
public class MaskingCorruptValueException extends CorruptValueException {
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    /**
     * Creates a new masking corrupt state exception that masks the given cause.
     * 
     * @param cause the cause to be masked as a masking corrupt state exception.
     */
    protected MaskingCorruptValueException(@Nonnull Exception cause) {
        super(StringUtility.prependWithIndefiniteArticle(cause.getClass().getSimpleName(), true) + " is masked as a corrupt value exception.", cause);
    }
    
    /**
     * Returns a new masking corrupt state exception that masks the given cause.
     * 
     * @param cause the cause to be masked as a masking corrupt state exception.
     * 
     * @return a new masking corrupt state exception that masks the given cause.
     */
    @Pure
    public static @Nonnull MaskingCorruptValueException get(@Nonnull Exception cause) {
        return new MaskingCorruptValueException(cause);
    }
    
}
