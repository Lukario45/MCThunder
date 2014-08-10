package net.mcthunder.packet.essentials;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface PacketEncrypter {
    public abstract int getDecryptOutputSize(int paramInt);

    public abstract int getEncryptOutputSize(int paramInt);

    public abstract int decrypt(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
            throws Exception;

    public abstract int encrypt(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
            throws Exception;
}