package com.orange.erable.vault.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.mashape.unirest.http.Unirest;
import com.orange.erable.common.conf.ConfigLoader;
import com.orange.erable.common.conf.ErableEnvVar;
import com.orange.erable.common.utils.CustomObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public abstract class AbstractTestScaffolding {
    // demarrer wiremockserver sur un autre port
    private static WireMockServer wireMockServer = new WireMockServer(5000);

    @BeforeAll
    public static void setUp() throws Exception {
        Unirest.setObjectMapper(new CustomObjectMapper());
        ConfigLoader.getInstance().loadTechnicalConfig(ErableEnvVar.ERABLE_CONFIG_TECH_DIR.getValue(), "rs");
        configureFor("localhost", 5000);
        wireMockServer.start();
    }

    @AfterAll
    public static void stop() throws Exception {
        wireMockServer.stop();
    }
}
