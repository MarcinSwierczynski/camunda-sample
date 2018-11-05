package net.swierczynski.camunda;

import org.camunda.bpm.client.ExternalTaskClient;

import java.util.logging.Logger;

public class ChargeCardWorker {
    private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("charge-card")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    // Get a process variable
                    String item = externalTask.getVariable("item");
                    Long amount = externalTask.getVariable("amount");
                    LOGGER.info("The charge of a credit card with an amount of '" + amount + "'€ for the item '" + item + "' was approved.");

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}