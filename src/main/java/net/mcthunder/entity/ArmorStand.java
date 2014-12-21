package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.packetlib.packet.Packet;

public class ArmorStand extends LivingEntity {
    private boolean small, gravity, arms, baseplate;
    private float headPitch, headYaw, headRoll, bodyPitch, bodyYaw, bodyRoll, leftArmPitch, leftArmYaw, leftArmRoll,
            rightArmPitch, rightArmYaw, rightArmRoll, leftLegPitch, leftLegYaw, leftLegRoll, rightLegPitch, rightLegYaw, rightLegRoll;

    public ArmorStand(Location location) {
        super(location);
        this.type = EntityType.ARMOR_STAND;
        this.small = false;
        this.gravity = true;
        this.arms = true;
        this.baseplate = true;
        this.headPitch = 0;
        this.headYaw = 0;
        this.headRoll = 0;
        this.bodyPitch = 0;
        this.bodyYaw = 0;
        this.bodyRoll = 0;
        this.leftArmPitch = 0;
        this.leftArmYaw = 0;
        this.leftArmRoll = 0;
        this.rightArmPitch = 0;
        this.rightArmYaw = 0;
        this.rightArmRoll = 0;
        this.leftLegPitch = 0;
        this.leftLegYaw = 0;
        this.leftLegRoll = 0;
        this.rightLegPitch = 0;
        this.rightLegYaw = 0;
        this.rightLegRoll = 0;
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.SMALL, this.small);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.GRAVITY, this.gravity);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.ARMS, this.arms );
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.BASEPLATE, this.baseplate);
        //ID is just to store in a random spot sorta since multiple per same metadata id
        this.metadata.setMetadata(1000, new EntityMetadata(11, MetadataType.FLOAT, this.headPitch));
        this.metadata.setMetadata(1001, new EntityMetadata(11, MetadataType.FLOAT, this.headYaw));
        this.metadata.setMetadata(1002, new EntityMetadata(11, MetadataType.FLOAT, this.headRoll));
        this.metadata.setMetadata(1003, new EntityMetadata(12, MetadataType.FLOAT, this.bodyPitch));
        this.metadata.setMetadata(1004, new EntityMetadata(12, MetadataType.FLOAT, this.bodyYaw));
        this.metadata.setMetadata(1005, new EntityMetadata(12, MetadataType.FLOAT, this.bodyRoll));
        this.metadata.setMetadata(1006, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmPitch));
        this.metadata.setMetadata(1007, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmYaw));
        this.metadata.setMetadata(1008, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmRoll));
        this.metadata.setMetadata(1009, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmPitch));
        this.metadata.setMetadata(1010, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmYaw));
        this.metadata.setMetadata(1011, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmRoll));
        this.metadata.setMetadata(1012, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegPitch));
        this.metadata.setMetadata(1013, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegYaw));
        this.metadata.setMetadata(1014, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegRoll));
        this.metadata.setMetadata(1015, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegPitch));
        this.metadata.setMetadata(1016, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegYaw));
        this.metadata.setMetadata(1017, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegRoll));
    }

    @Override
    public void ai() {

    }

    @Override
    public Packet getPacket() {
        return null;
    }
}