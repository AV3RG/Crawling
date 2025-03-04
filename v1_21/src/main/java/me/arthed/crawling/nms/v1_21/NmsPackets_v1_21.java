package me.arthed.crawling.nms.v1_21;

import me.arthed.nms.NmsPackets;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NmsPackets_v1_21 implements NmsPackets {

    private final int blockId = -8854;
    private final int floorBlockId = -8855;
    private final DataWatcher dataWatcher;

    public NmsPackets_v1_21() {
        World world = Bukkit.getWorlds().get(0);
        FallingBlock fallingBlock = (FallingBlock) world.spawnEntity(new Location(world, 0, 0, 0), EntityType.FALLING_BLOCK);
        fallingBlock.setGravity(false);
        this.dataWatcher = ((CraftEntity)fallingBlock).getHandle().ar();
        fallingBlock.remove();
    }

    public void spawnFakeBlocks(Player player, Block block, Block floorBlock, Material fakeFloorMaterial) {
        PacketPlayOutSpawnEntity spawnBlockPacket = new PacketPlayOutSpawnEntity(
                this.blockId,
                UUID.randomUUID(), // entity uuid
                block.getX() + 0.5,
                block.getY() + 0.0,
                block.getZ() + 0.5,
                0.0f, //yaw
                0.0f, //pitch
                EntityTypes.C,
                net.minecraft.world.level.block.Block.i(((CraftBlock)block).getNMS()), //material id
                new Vec3D(0, 1, 0), // velocity,
                0.0
        );
        PacketPlayOutEntityMetadata blockMetadataPacket = new PacketPlayOutEntityMetadata(this.blockId, this.dataWatcher.b());
        PacketPlayOutSpawnEntity spawnBlockPacket2 = new PacketPlayOutSpawnEntity(
                this.floorBlockId,
                UUID.randomUUID(), // entity uuid
                floorBlock.getX() + 0.5,
                floorBlock.getY() + 0.001f,
                floorBlock.getZ() + 0.5,
                90.0f, //yaw
                0.0f, //pitch
                EntityTypes.C,
                net.minecraft.world.level.block.Block.i(((CraftBlock)floorBlock).getNMS()), //material id
                new Vec3D(0, 1, 0), // velocity,
                0.0
        );
        PacketPlayOutEntityMetadata blockMetadataPacket2 = new PacketPlayOutEntityMetadata(this.floorBlockId, this.dataWatcher.b());
        ((CraftPlayer)player).getHandle().c.a(spawnBlockPacket);
        ((CraftPlayer)player).getHandle().c.a(blockMetadataPacket);
        ((CraftPlayer)player).getHandle().c.a(spawnBlockPacket2);
        ((CraftPlayer)player).getHandle().c.a(blockMetadataPacket2);
        player.sendBlockChange(floorBlock.getLocation(), fakeFloorMaterial == null ? Bukkit.createBlockData(Material.STONE) : Bukkit.createBlockData(fakeFloorMaterial));
    }

    public void removeFakeBlocks(Player player) {
        PacketPlayOutEntityDestroy destroyOldBlockPacket = new PacketPlayOutEntityDestroy(this.blockId);
        PacketPlayOutEntityDestroy destroyOldBlockPacket2 = new PacketPlayOutEntityDestroy(this.blockId-1);
        ((CraftPlayer)player).getHandle().c.a(destroyOldBlockPacket);
        ((CraftPlayer)player).getHandle().c.a(destroyOldBlockPacket2);
    }


}
