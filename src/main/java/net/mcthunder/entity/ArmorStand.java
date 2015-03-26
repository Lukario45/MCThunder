package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

public class ArmorStand extends Entity {
    private float headPitch = 0, headYaw = 0, headRoll = 0, bodyPitch = 0, bodyYaw = 0, bodyRoll = 0, leftArmPitch = 0, leftArmYaw = 0,
            leftArmRoll = 0, rightArmPitch = 0, rightArmYaw = 0, rightArmRoll = 0, leftLegPitch = 0, leftLegYaw = 0, leftLegRoll = 0,
            rightLegPitch = 0, rightLegYaw = 0, rightLegRoll = 0;
    private boolean small = false, gravity = true, arms = true, baseplate = true, marker = false;

    public ArmorStand(Location location) {
        super(location);
        this.type = EntityType.ARMOR_STAND;
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.SMALL, this.small);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.GRAVITY, this.gravity);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.ARMS, this.arms);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.BASEPLATE, this.baseplate);
        //ID is just to store in a random spot sorta since multiple per same metadata id
        this.metadata.setMetadata(11, this.headPitch);
        this.metadata.setMetadata(1000, new EntityMetadata(11, MetadataType.FLOAT, this.headYaw));
        this.metadata.setMetadata(1001, new EntityMetadata(11, MetadataType.FLOAT, this.headRoll));
        this.metadata.setMetadata(12, this.bodyPitch);
        this.metadata.setMetadata(1002, new EntityMetadata(12, MetadataType.FLOAT, this.bodyYaw));
        this.metadata.setMetadata(1003, new EntityMetadata(12, MetadataType.FLOAT, this.bodyRoll));
        this.metadata.setMetadata(13, this.leftArmPitch);
        this.metadata.setMetadata(1004, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmYaw));
        this.metadata.setMetadata(1005, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmRoll));
        this.metadata.setMetadata(14, this.rightArmPitch);
        this.metadata.setMetadata(1006, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmYaw));
        this.metadata.setMetadata(1007, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmRoll));
        this.metadata.setMetadata(15, this.leftLegPitch);
        this.metadata.setMetadata(1008, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegYaw));
        this.metadata.setMetadata(1009, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegRoll));
        this.metadata.setMetadata(16, this.rightLegPitch);
        this.metadata.setMetadata(1010, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegYaw));
        this.metadata.setMetadata(1011, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegRoll));
    }

    public ArmorStand(World w, CompoundTag tag) {
        super(w, tag);
        IntTag disabledSlots = tag.get("DisabledSlots");
        ListTag equipment = tag.get("Equipment");
        if (equipment != null) {
            CompoundTag hand = equipment.get(0);
            CompoundTag boots = equipment.get(1);
            CompoundTag leggings = equipment.get(2);
            CompoundTag chestplate = equipment.get(3);
            CompoundTag helmet = equipment.get(4);
        }
        ByteTag marker = tag.get("Marker");//1 true, 0 false
        ByteTag invisible = tag.get("Invisible");//1 true, 0 false
        ByteTag noBasePlate = tag.get("NoBasePlate");//1 true, 0 false
        ByteTag noGravity = tag.get("NoGravity");//1 true, 0 false
        CompoundTag pose = tag.get("Pose");
        ByteTag showArms = tag.get("ShowArms");//1 true, 0 false
        ByteTag small = tag.get("Small");//1 true, 0 false
        this.marker = marker != null && marker.getValue() == (byte) 1;
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.INVISIBLE, this.invisible = invisible != null && invisible.getValue() == (byte) 1);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.SMALL, this.small = small != null && small.getValue() == (byte) 1);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.GRAVITY, this.gravity = noGravity != null && noGravity.getValue() == (byte) 0);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.ARMS, this.arms = showArms != null && showArms.getValue() == (byte) 1);
        this.metadata.setBit(MetadataConstants.ARMOR_STAND, MetadataConstants.ArmorFlags.BASEPLATE, this.baseplate = noBasePlate != null && noBasePlate.getValue() == (byte) 0);
        //ID is just to store in a random spot sorta since multiple per same metadata id
        FloatTag xRot = null, yRot = null, zRot = null;
        ListTag head = pose.get("Head");
        if (head != null) {
            xRot = head.get(0);
            yRot = head.get(1);
            zRot = head.get(2);
        }
        this.metadata.setMetadata(11, this.headPitch = xRot == null ? 0 : xRot.getValue());
        this.metadata.setMetadata(1000, new EntityMetadata(11, MetadataType.FLOAT, this.headYaw = yRot == null ? 0 : yRot.getValue()));
        this.metadata.setMetadata(1001, new EntityMetadata(11, MetadataType.FLOAT, this.headRoll = zRot == null ? 0 : zRot.getValue()));
        ListTag body = pose.get("Body");
        if (body != null) {
            xRot = body.get(0);
            yRot = body.get(1);
            zRot = body.get(2);
        }
        this.metadata.setMetadata(12, this.bodyPitch = xRot == null ? 0 : xRot.getValue());
        this.metadata.setMetadata(1002, new EntityMetadata(12, MetadataType.FLOAT, this.bodyYaw = yRot == null ? 0 : yRot.getValue()));
        this.metadata.setMetadata(1003, new EntityMetadata(12, MetadataType.FLOAT, this.bodyRoll = zRot == null ? 0 : zRot.getValue()));
        ListTag leftArm = pose.get("LeftArm");
        if (leftArm != null) {
            xRot = leftArm.get(0);
            yRot = leftArm.get(1);
            zRot = leftArm.get(2);
        }
        this.metadata.setMetadata(13, this.leftArmPitch = xRot == null ? 0 : xRot.getValue());
        this.metadata.setMetadata(1004, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmYaw = yRot == null ? 0 : yRot.getValue()));
        this.metadata.setMetadata(1005, new EntityMetadata(13, MetadataType.FLOAT, this.leftArmRoll = zRot == null ? 0 : zRot.getValue()));
        ListTag rightArm = pose.get("RightArm");
        if (rightArm != null) {
            xRot = rightArm.get(0);
            yRot = rightArm.get(1);
            zRot = rightArm.get(2);
        }
        this.metadata.setMetadata(14, this.rightArmPitch = xRot == null ? 0 : xRot.getValue());
        this.metadata.setMetadata(1006, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmYaw = yRot == null ? 0 : yRot.getValue()));
        this.metadata.setMetadata(1007, new EntityMetadata(14, MetadataType.FLOAT, this.rightArmRoll = zRot == null ? 0 : zRot.getValue()));
        ListTag leftLeg = pose.get("LeftLeg");
        if (leftLeg != null) {
            xRot = leftLeg.get(0);
            yRot = leftLeg.get(1);
            zRot = leftLeg.get(2);
        }
        this.metadata.setMetadata(15, this.leftLegPitch = xRot == null ? 0 : xRot.getValue());
        this.metadata.setMetadata(1008, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegYaw = yRot == null ? 0 : yRot.getValue()));
        this.metadata.setMetadata(1009, new EntityMetadata(15, MetadataType.FLOAT, this.leftLegRoll = zRot == null ? 0 : zRot.getValue()));
        ListTag rightLeg = pose.get("RightLeg");
        if (rightLeg != null) {
            xRot = rightLeg.get(0);
            yRot = rightLeg.get(1);
            zRot = rightLeg.get(2);
        }
        this.metadata.setMetadata(16, this.rightLegPitch = xRot == null ? 0 : xRot.getValue());
        this.metadata.setMetadata(1010, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegYaw = yRot == null ? 0 : yRot.getValue()));
        this.metadata.setMetadata(1011, new EntityMetadata(16, MetadataType.FLOAT, this.rightLegRoll = zRot == null ? 0 : zRot.getValue()));
    }

    @Override
    public Packet getPacket() {
        return null;//new ServerSpawnObjectPacket(this.entityID, ObjectType.ARMOR_STAND, this.location.getX(), this.location.getY(), this.location.getZ(),
                //this.location.getYaw(), this.location.getPitch());
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

    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    public boolean hasMarker() {
        return this.marker;
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

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}