package br.com.jfestrela.bootstrap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class Aplicacao {

    private static final Logger LOG = LoggerFactory.getLogger(Aplicacao.class);

    void onStart(@Observes StartupEvent event) {
        LOG.info("Aplicacao sendo inicializada...");
    }

    void onStop(@Observes ShutdownEvent event) {
        LOG.info("Aplicacao sendo encerrada...");
    }

}