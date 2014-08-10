package net.mcthunder.packet.login.client;

import net.mcthunder.packet.essentials.CryptUtil;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionResponsePacket
        implements Packet {
    private byte[] sharedKey;
    private byte[] verifyToken;

    private EncryptionResponsePacket() {
    }

    public EncryptionResponsePacket(SecretKey secretKey, PublicKey publicKey, byte[] verifyToken) {
        this.sharedKey = CryptUtil.encryptData(publicKey, secretKey.getEncoded());
        this.verifyToken = CryptUtil.encryptData(publicKey, verifyToken);
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return CryptUtil.decryptSharedKey(privateKey, this.sharedKey);
    }

    public byte[] getVerifyToken(PrivateKey privateKey) {
        return CryptUtil.decryptData(privateKey, this.verifyToken);
    }

    public void read(NetIn in) throws IOException {
        this.sharedKey = in.readPrefixedBytes();
        this.verifyToken = in.readPrefixedBytes();
    }

    public void write(NetOut out) throws IOException {
        out.writePrefixedBytes(this.sharedKey);
        out.writePrefixedBytes(this.verifyToken);
    }

    public boolean isPriority() {
        return true;
    }
}