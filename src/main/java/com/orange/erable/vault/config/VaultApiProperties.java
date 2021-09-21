package com.orange.erable.vault.config;

import com.orange.erable.common.conf.ConfigLoader;
import com.orange.erable.common.conf.ITechnicalProperties;

public enum VaultApiProperties implements ITechnicalProperties {
    VAULT_API_SECRETSPATH("vault.api.secretsPath"),
    VAULT_API_CONNECTIONTO("vault.api.connectionTo"),
    VAULT_API_RECEIVETO("vault.api.receiveTo");

    private final String name;
    private final boolean mandatory;

    VaultApiProperties(String name) {
        this.name = name;
        this.mandatory = true;
    }

    /**
     * Gets the mandatory.
     *
     * @return The first existing value :
     * <ol>
     * <li>La {@link System#getProperty(String) system property} define by any application
     * </ol>
     */
    public String getValue() {
        return ConfigLoader.getInstance().findProperties(this);
    }

    @Override
    public String getPropName() {
        return name;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }
}