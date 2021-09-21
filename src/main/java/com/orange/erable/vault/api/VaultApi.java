package com.orange.erable.vault.api;

import com.orange.erable.common.utils.ErableLoggerWrapper;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.orange.erable.vault.config.VaultApiProperties;

import com.bettercloud.vault.*;
import com.bettercloud.vault.api.Auth;
import com.bettercloud.vault.api.Logical;
import com.bettercloud.vault.response.AuthResponse;
import com.bettercloud.vault.response.LogicalResponse;

/***
 * the VaultApi class represents an interface to query vault server remotely in a generic way.
 */
public class VaultApi{
    private static final ErableLoggerWrapper LOGGER = ErableLoggerWrapper.getInstance(VaultApi.class);

    private String vaultUrl;
    private String vaultNamespace;
    private String roleid;
    private String secretid;
    private String vaultSecretsPath;
    private Integer vaultConnectionTo;
    private Integer vaultReceiveTo;

    public VaultApi(){
        String path = System.getenv("VAULT_APPROLE_FILE");
        File file = new File(path);
        String messageException = "[VaultApi Exception]: failed to open " + path;
        try (InputStream input = new FileInputStream(file.getAbsolutePath())) {
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            this.vaultUrl = prop.getProperty("vault_addr");
            this.vaultNamespace = prop.getProperty("vault_namespace");
            this.roleid = prop.getProperty("roleid");
            this.secretid = prop.getProperty("secretid");
        } catch (IOException e) {
            LOGGER.error(messageException, e);
        }
        this.vaultSecretsPath = VaultApiProperties.VAULT_API_SECRETSPATH.getValue();
        this.vaultConnectionTo = Integer.valueOf(VaultApiProperties.VAULT_API_CONNECTIONTO.getValue());
        this.vaultReceiveTo = Integer.valueOf(VaultApiProperties.VAULT_API_RECEIVETO.getValue());
    }

    public String getSecretByKey(String key){
        String secret = "";
        String messageException = "[VaultApi Exception]: failed to get secret " + key + " in namespace" + this.vaultNamespace;

        try{
            final VaultConfig baseConfig = new VaultConfig().address(this.vaultUrl)
                                                            .openTimeout(this.vaultConnectionTo)
                                                            .readTimeout(this.vaultReceiveTo)
                                                            .nameSpace(this.vaultNamespace);

            //retrieving Vault token
            final Auth vaultAuth = new Vault(baseConfig.build()).auth();
            AuthResponse authResp = vaultAuth.loginByAppRole(this.roleid, this.secretid);

            //retrieve BRMC Passwords
            //kv-v2 secrets engine
            final Logical vaultLogical = new Vault(baseConfig.token(authResp.getAuthClientToken()).build(), 2).logical();
            LogicalResponse logResp = vaultLogical.read(String.join("/", this.vaultSecretsPath, "application"));

            secret = logResp.getData().get(key);
        } catch(VaultException e) {
            LOGGER.error(messageException, e);
        }

        return secret;
    }
}