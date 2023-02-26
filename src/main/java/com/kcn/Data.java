package com.kcn;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Data {

    long onlineTime = 0;
    boolean is_attestation = false;
    boolean is_child = false;
    boolean is_teleport = false;
    boolean is_tip = false;
    private int date = new GregorianCalendar().get(Calendar.DATE);

    public void readNbt(@NotNull NbtCompound nbt) {
        NbtCompound authentication = nbt.getCompound("Authentication");
        onlineTime = authentication.getLong("onLineTime");
        is_attestation = authentication.getBoolean("isAttestation");
        is_child = authentication.getBoolean("isChild");
        date = authentication.getInt("date");
        is_teleport = authentication.getBoolean("isTeleport");
        is_tip = authentication.getBoolean("isTip");
    }

    public void writeNbt(@NotNull NbtCompound nbt) {
        NbtCompound authentication = new NbtCompound();
        authentication.putLong("onLineTime", onlineTime);
        authentication.putBoolean("isAttestation", is_attestation);
        authentication.putBoolean("isChild", is_child);
        authentication.putInt("date", date);
        authentication.putBoolean("isTeleport", is_teleport);
        authentication.putBoolean("isTip", is_tip);
        nbt.put("Authentication", authentication);
    }

    public void update(PlayerEntity player) {
        int newDate = new GregorianCalendar().get(Calendar.DATE);
        if (!player.getWorld().isClient()) {
            if (!is_attestation) {
                player.requestTeleport(0, 500, 0);
                if (!is_tip) {
                    player.sendMessage(new LiteralText("请使用 /idCard 命令,完成实名认证,本mod不会存储您的身份证信息,请放心输入." +
                            "源码已在GitHub上存储,若有怀疑请按照以下网址查看源码" +
                            "网址: https://github.com/ChenMgP/AuthenticationMod").formatted(Formatting.BLUE), false);
                    is_tip = true;
                }
            } else {
                if (!is_teleport) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 60, 255, false, false, false));
                    is_teleport = true;
                }
                if (is_child) {
                    if (newDate - date > 0) {
                        date = newDate;
                    } else {
                        onlineTime++;
                        if (onlineTime >= 216000) {
                            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                            serverPlayerEntity.networkHandler.disconnect(new LiteralText("游玩时间已到"));
                        }
                    }
                }
            }
        }
    }

    public boolean isIs_attestation() {
        return is_attestation;
    }

    public void setIs_attestation(boolean is_attestation) {
        this.is_attestation = is_attestation;
    }

    public void setIs_child(boolean is_child) {
        this.is_child = is_child;
    }
}
