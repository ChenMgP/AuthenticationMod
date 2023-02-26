package com.kcn;

import com.kcn.command.IdCardCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> IdCardCommand.register(dispatcher)
        );
    }
}
