package com.iafenvoy.unified.mixin;

import com.iafenvoy.unified.data.UnifiedConfig;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.SynchronizeTagsS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

@Mixin(SynchronizeTagsS2CPacket.class)
public class SynchronizeTagsS2CPacketMixin {
    @Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at = @At("RETURN"))
    private void readUnifiedData(PacketByteBuf buf, CallbackInfo ci) {
        List<Identifier> ids = new LinkedList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            ids.add(buf.readIdentifier());
        UnifiedConfig.onSync(ids.stream());
    }

    @Inject(method = "write", at = @At("RETURN"))
    private void writeUnifiedData(PacketByteBuf buf, CallbackInfo ci) {
        List<Identifier> ids = UnifiedConfig.getAllTags();
        buf.writeInt(ids.size());
        ids.forEach(buf::writeIdentifier);
    }
}
