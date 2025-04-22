package com.iafenvoy.unified;

import com.iafenvoy.unified.screen.UnifyScreenHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.screen.ScreenHandlerType;
import org.slf4j.Logger;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Unified {
    public static final String MOD_ID = "unified";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Supplier<ScreenHandlerType<UnifyScreenHandler>> HANDLER_TYPE;
    public static BooleanSupplier shouldSyncFromServer = () -> false;
}
