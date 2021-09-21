package com.orange.erable.vault.api;

import com.bettercloud.vault.*;
import com.orange.erable.vault.api.VaultApi;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class VaultApiTest extends AbstractTestScaffolding{
    @Test
    public void getSecretByKey() throws VaultException{
        VaultApi vaultApi = new VaultApi();
        String secret = vaultApi.getSecretByKey("password");
        String expectedSecret = "admin";
        assertEquals(expectedSecret, secret);
    }
}