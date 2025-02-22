package edu.harvard.iq.dataverse;

import edu.harvard.iq.dataverse.dataaccess.DataAccess;
import java.util.Locale;

import javax.persistence.MappedSuperclass;
import org.apache.commons.lang3.StringUtils;

/**
 * A {@link DvObject} that can contain other {@link DvObject}s.
 * 
 * @author michael
 */
@MappedSuperclass
public abstract class DvObjectContainer extends DvObject {
	
    
    //Default to "file" is for tests only
    public static final String DEFAULT_METADATA_LANGUAGE = Locale.getDefault().getDisplayLanguage();
    public static final String DEFAULT_METADATA_LANGUAGE_CODE = Locale.getDefault().getLanguage();
    public static final String UNDEFINED_METADATA_LANGUAGE_CODE = "undefined"; //Used in dataverse.xhtml as a non-null selection option value (indicating inheriting the default)
    
    
    public void setOwner(Dataverse owner) {
        super.setOwner(owner);
    }
	
	@Override
	public Dataverse getOwner() {
		return super.getOwner()!=null ? (Dataverse)super.getOwner() : null;
	}
    
    protected abstract boolean isPermissionRoot();
    
    @Override
    public boolean isEffectivelyPermissionRoot() {
        return isPermissionRoot() || (getOwner() == null);
    }

    private String storageDriver=null;
    
    private String metadataLanguage=null;
    
    public String getEffectiveStorageDriverId() {
        String id = storageDriver;
        if (StringUtils.isBlank(id)) {
            if (this.getOwner() != null) {
                id = this.getOwner().getEffectiveStorageDriverId();
            } else {
                id = DataAccess.DEFAULT_STORAGE_DRIVER_IDENTIFIER;
            }
        }
        return id;
    }
    
    public String getStorageDriverId() {
        if (storageDriver == null) {
            return DataAccess.UNDEFINED_STORAGE_DRIVER_IDENTIFIER;
        }
        return storageDriver;
    }

    public void setStorageDriverId(String storageDriver) {
        if (storageDriver != null && storageDriver.equals(DataAccess.UNDEFINED_STORAGE_DRIVER_IDENTIFIER)) {
            this.storageDriver = null;
        } else {
            this.storageDriver = storageDriver;
        }
    }
    
    public String getEffectiveMetadataLanguage() {
        String ml = metadataLanguage;
        if (StringUtils.isBlank(ml)) {
            if (this.getOwner() != null) {
                ml = this.getOwner().getEffectiveMetadataLanguage();
            } else {
                ml = UNDEFINED_METADATA_LANGUAGE_CODE;
            }
        }
        return ml;
    }
    
    public String getMetadataLanguage() {
        if (metadataLanguage == null) {
            return UNDEFINED_METADATA_LANGUAGE_CODE;
        }
        return metadataLanguage;
    }

    public void setMetadataLanguage(String ml) {
        if (ml != null && ml.equals(UNDEFINED_METADATA_LANGUAGE_CODE)) {
            this.metadataLanguage = null;
        } else {
            this.metadataLanguage = ml;
        }
    }
    
}
