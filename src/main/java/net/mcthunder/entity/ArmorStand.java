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
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.SMALL, this.small = false);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.GRAVITY, this.gravity = true);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.ARMS, this.arms = true);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.BASEPLATE, this.baseplate = true);
        //ID is just to store in a random spot sorta since multiple per same metadata id
        this.metadata.setMetadata(11, this.headPitch = 0);
        this.metadata.setMetadata(1000, new EntityMetadata(11, MetadataType.FLOAT, this.headYaw = 0));
        this.metadata.setMetadata(1001, new EntityMetadata(11, MetadataType.FLOAT, this.headRoll = 0));
        this.metadata.setMetadata(12, this.bodyPitch = 0);
        this.metadata.setMetadata(1002, new EntityMetadata(12, MetadataType.FLOAT, this.bodyYaw = 0));
        this.metadata.setMetadata(1003, new EntityMetadata(12, MetadataType.FLOAT, this.bodyRoll = 0));
        this.metadata.setMetadata(13, this.leftArmPitch = 0);
        this.metadata.setMetadata(1004, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmYaw = 0));
        this.metadata.setMetadata(1005, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmRoll = 0));
        this.metadata.setMetadata(14, this.rightArmPitch = 0);
        this.metadata.setMetadata(1006, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmYaw = 0));
        this.metadata.setMetadata(1007, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmRoll = 0));
        this.metadata.setMetadata(15, this.leftLegPitch = 0);
        this.metadata.setMetadata(1008, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegYaw = 0));
        this.metadata.setMetadata(1009, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegRoll = 0));
        this.metadata.setMetadata(16, this.rightLegPitch = 0);
        this.metadata.setMetadata(1010, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegYaw = 0));
        this.metadata.setMetadata(1011, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegRoll = 0));
    }

    @Override
    public Packet getPacket() {
        return null;
    }

    @Override
    public void ai() {

    }

    public void setSmall(boolean small) {
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.SMALL, this.small = small);
        updateMetadata();
    }

    public boolean getSmall() {
        return this.small;
    }

    public void setGravity(boolean gravity) {
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.GRAVITY, this.gravity = gravity);
        updateMetadata();
    }

    public boolean hasGravity() {
        return this.gravity;
    }

    public void setShowArms(boolean arms) {
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.ARMS, this.arms = arms);
        updateMetadata();
    }

    public boolean showArms() {
        return this.arms;
    }

    public void setHasBaseplate(boolean baseplate) {
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.BASEPLATE, this.baseplate = baseplate);
        updateMetadata();
    }

    public boolean hasBaseplate() {
        return this.baseplate;
    }

    public void setHeadPitch(float headPitch) {
        this.metadata.setMetadata(11, this.headPitch = headPitch);
        updateMetadata();
    }

    public float getHeadPitch() {
        return this.headPitch;
    }

    public void setHeadYaw(float headYaw) {
        this.metadata.setMetadata(1000, new EntityMetadata(11, MetadataType.FLOAT, this.headYaw = headYaw));
        updateMetadata();
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    public void setHeadRoll(float headRoll) {
        this.metadata.setMetadata(1001, new EntityMetadata(11, MetadataType.FLOAT, this.headRoll = headRoll));
        updateMetadata();
    }

    public float getHeadRoll() {
        return this.headRoll;
    }

    public void setBodyPitch(float bodyPitch) {
        this.metadata.setMetadata(12, this.bodyPitch = bodyPitch);
        updateMetadata();
    }

    public float getBodyPitch() {
        return this.bodyPitch;
    }

    public void setBodyYaw(float bodyYaw) {
        this.metadata.setMetadata(1002, new EntityMetadata(12, MetadataType.FLOAT, this.bodyYaw = bodyYaw));
        updateMetadata();
    }

    public float getBodyYaw() {
        return this.bodyYaw;
    }

    public void setBodyRoll(float bodyRoll) {
        this.metadata.setMetadata(1003, new EntityMetadata(12, MetadataType.FLOAT, this.bodyRoll = bodyRoll));
        updateMetadata();
    }

    public float getBodyRoll() {
        return this.bodyRoll;
    }

    public void setLeftArmPitch(float leftArmPitch) {
        this.metadata.setMetadata(13, this.leftArmPitch = leftArmPitch);
        updateMetadata();
    }

    public float getLeftArmPitch() {
        return this.leftArmPitch;
    }

    public void setLeftArmYaw(float leftArmYaw) {
        this.metadata.setMetadata(1004, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmYaw = leftArmYaw));
        updateMetadata();
    }

    public float getLeftArmYaw() {
        return this.leftArmYaw;
    }

    public void setLeftArmRoll(float leftArmRoll) {
        this.metadata.setMetadata(1005, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmRoll = leftArmRoll));
        updateMetadata();
    }

    public float getLeftArmRoll() {
        return this.leftArmRoll;
    }

    public void setRightArmPitch(float rightArmPitch) {
        this.metadata.setMetadata(14, this.rightArmPitch = rightArmPitch);
        updateMetadata();
    }

    public float getRightArmPitch() {
        return this.rightArmPitch;
    }

    public void setRightArmYaw(float rightArmYaw) {
        this.metadata.setMetadata(1006, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmYaw = rightArmYaw));
        updateMetadata();
    }

    public float getRightArmYaw() {
        return this.rightArmYaw;
    }

    public void setRightArmRoll(float rightArmRoll) {
        this.metadata.setMetadata(1007, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmRoll = rightArmRoll));
        updateMetadata();
    }

    public float getRightArmRoll() {
        return this.rightArmRoll;
    }

    public void setLeftLegPitch(float leftLegPitch) {
        this.metadata.setMetadata(15, this.leftLegPitch = leftLegPitch);
        updateMetadata();
    }

    public float getLeftLegPitch() {
        return this.leftLegPitch;
    }

    public void setLeftLegYaw(float leftLegYaw) {
        this.metadata.setMetadata(1008, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegYaw = leftLegYaw));
        updateMetadata();
    }

    public float getLeftLegYaw() {
        return this.leftLegYaw;
    }

    public void setLeftLegRoll(float leftLegRoll) {
        this.metadata.setMetadata(1009, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegRoll = leftLegRoll));
        updateMetadata();
    }

    public float getLeftLegRoll() {
        return this.leftLegRoll;
    }

    public void setRightLegPitch(float rightLegPitch) {
        this.metadata.setMetadata(16, this.rightLegPitch = rightLegPitch);
        updateMetadata();
    }

    public float getRightLegPitch() {
        return this.rightLegPitch;
    }

    public void setRightLegYaw(float rightLegYaw) {
        this.metadata.setMetadata(1010, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegYaw = rightLegYaw));
        updateMetadata();
    }

    public float getRightLegYaw() {
        return this.rightLegYaw;
    }

    public void setRightLegRoll(float rightLegRoll) {
        this.metadata.setMetadata(1011, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegRoll = rightLegRoll));
        updateMetadata();
    }

    public float getRightLegRoll() {
        return this.rightLegRoll;
    }
}