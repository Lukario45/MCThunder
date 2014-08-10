package net.mcthunder.packet.login.server;

import net.mcthunder.packet.essentials.CryptUtil;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;
import java.security.PublicKey;

public class EncryptionRequestPacket
        implements Packet {
    private String serverId;
    private PublicKey publicKey;
    private byte[] verifyToken;

    private EncryptionRequestPacket() {
    }

    public EncryptionRequestPacket(String serverId, PublicKey publicKey, byte[] verifyToken) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    public String getServerId() {
        return this.serverId;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    public void read(NetIn in) throws IOException {
        this.serverId = in.readString();
        this.publicKey = CryptUtil.decodePublicKey(in.readPrefixedBytes());
        this.verifyToken = in.readPrefixedBytes();
    }

    public void write(NetOut out) throws IOException {
        out.writeString(this.serverId);
        out.writePrefixedBytes(this.publicKey.getEncoded());
        out.writePrefixedBytes(this.verifyToken);
    }

    public boolean isPriority() {
        return true;
    }
}