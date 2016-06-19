package net.digitalid.database.conversion.testenvironment.inherited;

import net.digitalid.utility.conversion.converter.Convertible;

/**
 *
 */
public class SuperClass implements Convertible {
    
    public final boolean flag;
    
    protected SuperClass(boolean flag) {
        this.flag = flag;
    }
    
}
