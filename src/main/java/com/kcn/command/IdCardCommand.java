package com.kcn.command;

import com.kcn.IDataGetter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class IdCardCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("idCard")
                .then(argument("number", StringArgumentType.string())
                        .executes(context -> broadcast(StringArgumentType.getString(context, "number"), context.getSource().getPlayer()))));
    }

    public static int broadcast(String number, PlayerEntity player) {
        if (!player.getWorld().isClient()) {
            if (((IDataGetter) player).getData().isIs_attestation()) {
                player.sendMessage(new LiteralText("请勿重复认证").formatted(Formatting.RED), false);
                return 0;
            }
            if (number.length() != 18) {
                player.sendMessage(new LiteralText("身份证格式有误").formatted(Formatting.RED), false);
                return -1;
            } else {
                int i1 = Integer.parseInt(String.valueOf(number.charAt(0))) * 7;
                int i2 = Integer.parseInt(String.valueOf(number.charAt(1))) * 9;
                int i3 = Integer.parseInt(String.valueOf(number.charAt(2))) * 10;
                int i4 = Integer.parseInt(String.valueOf(number.charAt(3))) * 5;
                int i5 = Integer.parseInt(String.valueOf(number.charAt(4))) * 8;
                int i6 = Integer.parseInt(String.valueOf(number.charAt(5))) * 4;
                //-------
                int a = Integer.parseInt(String.valueOf(number.charAt(6)));
                int b = Integer.parseInt(String.valueOf(number.charAt(7)));
                int c = Integer.parseInt(String.valueOf(number.charAt(8)));
                int d = Integer.parseInt(String.valueOf(number.charAt(9)));
                int i7 = a * 2;
                int i8 = b;
                int i9 = c * 6;
                int i10 = d * 3;
                //-------
                int i11 = Integer.parseInt(String.valueOf(number.charAt(10))) * 7;
                int i12 = Integer.parseInt(String.valueOf(number.charAt(11))) * 9;
                int i13 = Integer.parseInt(String.valueOf(number.charAt(12))) * 10;
                int i14 = Integer.parseInt(String.valueOf(number.charAt(13))) * 5;
                int i15 = Integer.parseInt(String.valueOf(number.charAt(14))) * 8;
                int i16 = Integer.parseInt(String.valueOf(number.charAt(15))) * 4;
                int i17 = Integer.parseInt(String.valueOf(number.charAt(16))) * 2;
                int x = (i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12 + i13 + i14 + i15 + i16 + i17) % 11;
                String y = "";
                switch (x) {
                    case 0 -> y = "1";
                    case 1 -> y = "0";
                    case 2 -> y = "X";
                    case 3 -> y = "9";
                    case 4 -> y = "8";
                    case 5 -> y = "7";
                    case 6 -> y = "6";
                    case 7 -> y = "5";
                    case 8 -> y = "4";
                    case 9 -> y = "3";
                    case 10 -> y = "2";
                }
                if (y.equals(String.valueOf(number.charAt(17)))) {
                    player.sendMessage(new LiteralText("验证成功").formatted(Formatting.GREEN), false);
                    ((IDataGetter) player).getData().setIs_attestation(true);
                    Calendar calendar = new GregorianCalendar();
                    int i = calendar.get(Calendar.YEAR);
                    StringBuilder sb = new StringBuilder();
                    int year = Integer.parseInt(sb.append(a).append(b).append(c).append(d).toString());
                    if (i - year < 18) {
                        ((IDataGetter) player).getData().setIs_child(true);
                        player.sendMessage(new LiteralText("根据系统检测,你为未成年人，每日享有3小时的游玩时间").formatted(Formatting.AQUA), false);
                    } else {
                        player.sendMessage(new LiteralText("根据系统检测,你为成年人,请控制游玩时间,注意身体").formatted(Formatting.AQUA), false);
                    }
                } else {
                    player.sendMessage(new LiteralText("身份证格式有误").formatted(Formatting.RED), false);
                }
            }
            return 0;
        }
        return 0;
    }
}
