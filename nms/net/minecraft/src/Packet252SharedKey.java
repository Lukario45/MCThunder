package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import javax.crypto.SecretKey;

public class Packet252SharedKey extends Packet
{
    private byte[] sharedSecret = new byte[0];
    private byte[] verifyToken = new byte[0];

    /**
     * Secret AES key decrypted from sharedSecret via the server's private RSA key
     */
    private SecretKey sharedKey;

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.sharedSecret = readBytesFromStream(par1DataInputStream);
        this.verifyToken = readBytesFromStream(par1DataInputStream);
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeByteArray(par1DataOutputStream, this.sharedSecret);
        writeByteArray(par1DataOutputStream, this.verifyToken);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleSharedKey(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 2 + this.sharedSecret.length + 2 + this.verifyToken.length;
    }

    /**
     * Return secretKey, decrypting it from the sharedSecret byte array if needed
     */
    public SecretKey getSharedKey(PrivateKey par1PrivateKey)
    {
        return par1PrivateKey == null ? this.sharedKey : (this.sharedKey = CryptManager.decryptSharedKey(par1PrivateKey, this.sharedSecret));
    }

    /**
     * Return the secret AES sharedKey (used by client only)
     */
    public SecretKey getSharedKey()
    {
        return this.getSharedKey((PrivateKey)null);
    }

    /**
     * Return verifyToken, decrypting it with the server's RSA private key
     */
    public byte[] getVerifyToken(PrivateKey par1PrivateKey)
    {
        return par1PrivateKey == null ? this.verifyToken : CryptManager.decryptData(par1PrivateKey, this.verifyToken);
    }
}
