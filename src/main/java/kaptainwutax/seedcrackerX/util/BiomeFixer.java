package kaptainwutax.seedcrackerX.util;

import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mcbiome.biome.Biomes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

public class BiomeFixer {

    //now this is what I call a hack-fix
    static final short[] idTransform = new short[61];

    static {
        idTransform[0] = 127;
        idTransform[1] = 1;
        idTransform[2] = 129;
        idTransform[3] = 12;
        idTransform[4] = 140;
        idTransform[5] = 2;
        idTransform[6] = 6;
        idTransform[7] = 4;
        idTransform[8] = 132;
        idTransform[9] = 27;
        idTransform[10] = 29;
        idTransform[11] = 155;
        idTransform[12] = 32;
        idTransform[13] = 160;
        idTransform[14] = 5;
        idTransform[15] = 30;
        idTransform[16] = 35;
        idTransform[17] = 36;
        idTransform[18] = 3;
        idTransform[19] = 131;
        idTransform[20] = 34;
        idTransform[21] = 163;
        idTransform[22] = 21;
        idTransform[23] = 23;
        idTransform[24] = 168;
        idTransform[25] = 37;
        idTransform[26] = 165;
        idTransform[27] = 38;
        idTransform[28] = 177;
        idTransform[29] = 178;
        idTransform[30] = 179;
        idTransform[31] = 180;
        idTransform[32] = 181;
        idTransform[33] = 182;
        idTransform[34] = 7;
        idTransform[35] = 11;
        idTransform[36] = 16;
        idTransform[37] = 26;
        idTransform[38] = 25;
        idTransform[39] = 44;
        idTransform[40] = 45;
        idTransform[41] = 48;
        idTransform[42] = 0;
        idTransform[43] = 24;
        idTransform[44] = 46;
        idTransform[45] = 49;
        idTransform[46] = 10;
        idTransform[47] = 50;
        idTransform[48] = 14;
        idTransform[49] = 174;
        idTransform[50] = 175;
        idTransform[51] = 8;
        idTransform[52] = 172;
        idTransform[53] = 171;
        idTransform[54] = 170;
        idTransform[55] = 173;
        idTransform[56] = 9;
        idTransform[57] = 42;
        idTransform[58] = 41;
        idTransform[59] = 40;
        idTransform[60] = 43;
    }

    public static Biome swap(net.minecraft.world.biome.Biome biome) {
        int mcId = MinecraftClient.getInstance().getNetworkHandler()
                .getRegistryManager().get(Registry.BIOME_KEY).getRawId(biome);
        return Biomes.REGISTRY.get(mcIdToLibId(mcId));
    }

    public static net.minecraft.world.biome.Biome swap(Biome biome) {
        return BuiltinRegistries.BIOME.get(biome.getId());
    }

    private static int mcIdToLibId(int mcId) {
        short id = idTransform[mcId];
        if (id > 173) return 127;
        return id;
    }


}
