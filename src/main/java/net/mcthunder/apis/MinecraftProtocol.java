package net.mcthunder.apis;

import net.mcthunder.auth.GameProfile;
import net.mcthunder.auth.UserAuthentication;
import net.mcthunder.auth.expection.AuthenticationException;
import net.mcthunder.listeners.ClientListener;
import net.mcthunder.listeners.ServerListener;
import net.mcthunder.packet.HandshakePacket;
import net.mcthunder.packet.essentials.*;
import net.mcthunder.packet.ingame.client.*;
import net.mcthunder.packet.ingame.client.player.*;
import net.mcthunder.packet.ingame.client.window.*;
import net.mcthunder.packet.ingame.client.world.ClientUpdateSignPacket;
import net.mcthunder.packet.ingame.server.*;
import net.mcthunder.packet.ingame.server.entity.*;
import net.mcthunder.packet.ingame.server.entity.player.*;
import net.mcthunder.packet.ingame.server.entity.spawn.*;
import net.mcthunder.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import net.mcthunder.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import net.mcthunder.packet.ingame.server.scoreboard.ServerTeamPacket;
import net.mcthunder.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import net.mcthunder.packet.ingame.server.window.*;
import net.mcthunder.packet.ingame.server.world.*;
import net.mcthunder.packet.login.client.EncryptionResponsePacket;
import net.mcthunder.packet.login.client.LoginStartPacket;
import net.mcthunder.packet.login.server.EncryptionRequestPacket;
import net.mcthunder.packet.login.server.LoginDisconnectPacket;
import net.mcthunder.packet.login.server.LoginSuccessPacket;
import net.mcthunder.packet.status.client.StatusPingPacket;
import net.mcthunder.packet.status.client.StatusQueryPacket;
import net.mcthunder.packet.status.server.StatusPongPacket;
import net.mcthunder.packet.status.server.StatusResponsePacket;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;

public class MinecraftProtocol extends PacketProtocol {
    private ProtocolMode mode = ProtocolMode.HANDSHAKE;
    private PacketHead header = new DefaultPacketHeader();
    private AESEncryption encrypt;
    private GameProfile profile;
    private String accessToken = "";
    private ClientListener clientListener;

    private MinecraftProtocol() {
    }

    public MinecraftProtocol(ProtocolMode mode) {
        if ((mode != ProtocolMode.LOGIN) && (mode != ProtocolMode.STATUS)) {
            throw new IllegalArgumentException("Only login and status modes are permitted.");
        }

        this.mode = mode;
        if (mode == ProtocolMode.LOGIN) {
            this.profile = new GameProfile((UUID) null, "Player");
        }

        this.clientListener = new ClientListener();
    }

    public MinecraftProtocol(String username) {
        this(ProtocolMode.LOGIN);
        this.profile = new GameProfile((UUID) null, username);
        this.clientListener = new ClientListener();
    }

    public MinecraftProtocol(String username, String using, boolean token) throws AuthenticationException {
        this(ProtocolMode.LOGIN);
        String clientToken = UUID.randomUUID().toString();
        UserAuthentication auth = new UserAuthentication(clientToken);
        auth.setUsername(username);
        if (token)
            auth.setAccessToken(using);
        else {
            auth.setPassword(using);
        }

        auth.login();
        this.profile = auth.getSelectedProfile();
        this.accessToken = auth.getAccessToken();
        this.clientListener = new ClientListener();
    }

    public boolean needsPacketSizer() {
        return true;
    }

    public boolean needsPacketEncryptor() {
        return true;
    }

    public PacketHead getPacketHeader() {
        return this.header;
    }

    public AESEncryption getEncryption() {
        return this.encrypt;
    }

    public void newClientSession(Client client, Session session) {
        if (this.profile != null) {
            session.setFlag("profile", this.profile);
            session.setFlag("access-token", this.accessToken);
        }

        setMode(this.mode, true, session);
        session.addListener(this.clientListener);
    }

    @Override
    public void newServerSession(Server server, Session session) {
        setMode(ProtocolMode.HANDSHAKE, false, session);
        session.addListener(new ServerListener());
    }

    public void enableEncryption(Key key) {
        try {
            this.encrypt = new AESEncryption(key);
        } catch (GeneralSecurityException e) {
            throw new Error("Failed to enable protocol encryption.", e);
        }
    }

    public ProtocolMode getMode() {
        return this.mode;
    }

    public void setMode(ProtocolMode mode, boolean client, Session session) {
        clearPackets();
        switch (mode.ordinal()) {
            case 1:
                if (client)
                    initClientHandshake(session);
                else {
                    initServerHandshake(session);
                }

                break;
            case 2:
                if (client)
                    initClientLogin(session);
                else {
                    initServerLogin(session);
                }

                break;
            case 3:
                if (client)
                    initClientGame(session);
                else {
                    initServerGame(session);
                }

                break;
            case 4:
                if (client)
                    initClientStatus(session);
                else {
                    initServerStatus(session);
                }

        }

        this.mode = mode;
    }

    private void initClientHandshake(Session session) {
        registerOutgoing(0, HandshakePacket.class);
    }

    private void initServerHandshake(Session session) {
        registerIncoming(0, HandshakePacket.class);
    }

    private void initClientLogin(Session session) {
        registerIncoming(0, LoginDisconnectPacket.class);
        registerIncoming(1, EncryptionRequestPacket.class);
        registerIncoming(2, LoginSuccessPacket.class);

        registerOutgoing(0, LoginStartPacket.class);
        registerOutgoing(1, EncryptionResponsePacket.class);
    }

    private void initServerLogin(Session session) {
        registerIncoming(0, LoginStartPacket.class);
        registerIncoming(1, EncryptionResponsePacket.class);

        registerOutgoing(0, LoginDisconnectPacket.class);
        registerOutgoing(1, EncryptionRequestPacket.class);
        registerOutgoing(2, LoginSuccessPacket.class);
    }

    private void initClientGame(Session session) {
        registerIncoming(0, ServerKeepAlivePacket.class);
        registerIncoming(1, ServerJoinGamePacket.class);
        registerIncoming(2, ServerChatPacket.class);
        registerIncoming(3, ServerUpdateTimePacket.class);
        registerIncoming(4, ServerEntityEquipmentPacket.class);
        registerIncoming(5, ServerSpawnPositionPacket.class);
        registerIncoming(6, ServerUpdateHealthPacket.class);
        registerIncoming(7, ServerRespawnPacket.class);
        registerIncoming(8, ServerPlayerPositionRotationPacket.class);
        registerIncoming(9, ServerChangeHeldItemPacket.class);
        registerIncoming(10, ServerPlayerUseBedPacket.class);
        registerIncoming(11, ServerAnimationPacket.class);
        registerIncoming(12, ServerSpawnPlayerPacket.class);
        registerIncoming(13, ServerCollectItemPacket.class);
        registerIncoming(14, ServerSpawnObjectPacket.class);
        registerIncoming(15, ServerSpawnMobPacket.class);
        registerIncoming(16, ServerSpawnPaintingPacket.class);
        registerIncoming(17, ServerSpawnExpOrbPacket.class);
        registerIncoming(18, ServerEntityVelocityPacket.class);
        registerIncoming(19, ServerDestroyEntitiesPacket.class);
        registerIncoming(20, ServerEntityMovementPacket.class);
        registerIncoming(21, ServerEntityPositionPacket.class);
        registerIncoming(22, ServerEntityRotationPacket.class);
        registerIncoming(23, ServerEntityPositionRotationPacket.class);
        registerIncoming(24, ServerEntityTeleportPacket.class);
        registerIncoming(25, ServerEntityHeadLookPacket.class);
        registerIncoming(26, ServerEntityStatusPacket.class);
        registerIncoming(27, ServerEntityAttachPacket.class);
        registerIncoming(28, ServerEntityMetadataPacket.class);
        registerIncoming(29, ServerEntityEffectPacket.class);
        registerIncoming(30, ServerEntityRemoveEffectPacket.class);
        registerIncoming(31, ServerSetExperiencePacket.class);
        registerIncoming(32, ServerEntityPropertiesPacket.class);
        registerIncoming(33, ServerChunkDataPacket.class);
        registerIncoming(34, ServerMultiBlockChangePacket.class);
        registerIncoming(35, ServerBlockChangePacket.class);
        registerIncoming(36, ServerBlockValuePacket.class);
        registerIncoming(37, ServerBlockBreakAnimPacket.class);
        registerIncoming(38, ServerMultiChunkDataPacket.class);
        registerIncoming(39, ServerExplosionPacket.class);
        registerIncoming(40, ServerPlayEffectPacket.class);
        registerIncoming(41, ServerPlaySoundPacket.class);
        registerIncoming(42, ServerSpawnParticlePacket.class);
        registerIncoming(43, ServerNotifyClientPacket.class);
        registerIncoming(44, ServerSpawnGlobalEntityPacket.class);
        registerIncoming(45, ServerOpenWindowPacket.class);
        registerIncoming(46, ServerCloseWindowPacket.class);
        registerIncoming(47, ServerSetSlotPacket.class);
        registerIncoming(48, ServerWindowItemsPacket.class);
        registerIncoming(49, ServerWindowPropertyPacket.class);
        registerIncoming(50, ServerConfirmTransactionPacket.class);
        registerIncoming(51, ServerUpdateSignPacket.class);
        registerIncoming(52, ServerMapDataPacket.class);
        registerIncoming(53, ServerUpdateTileEntityPacket.class);
        registerIncoming(54, ServerOpenTileEntityEditorPacket.class);
        registerIncoming(55, ServerStatisticsPacket.class);
        registerIncoming(56, ServerPlayerListEntryPacket.class);
        registerIncoming(57, ServerPlayerAbilitiesPacket.class);
        registerIncoming(58, ServerTabCompletePacket.class);
        registerIncoming(59, ServerScoreboardObjectivePacket.class);
        registerIncoming(60, ServerUpdateScorePacket.class);
        registerIncoming(61, ServerDisplayScoreboardPacket.class);
        registerIncoming(62, ServerTeamPacket.class);
        registerIncoming(63, ServerPluginMessagePacket.class);
        registerIncoming(64, ServerDisconnectPacket.class);

        registerOutgoing(0, ClientKeepAlivePacket.class);
        registerOutgoing(1, ClientChatPacket.class);
        registerOutgoing(2, ClientPlayerInteractEntityPacket.class);
        registerOutgoing(3, ClientPlayerMovementPacket.class);
        registerOutgoing(4, ClientPlayerPositionPacket.class);
        registerOutgoing(5, ClientPlayerRotationPacket.class);
        registerOutgoing(6, ClientPlayerPositionRotationPacket.class);
        registerOutgoing(7, ClientPlayerDigPacket.class);
        registerOutgoing(8, ClientPlayerPlaceBlockPacket.class);
        registerOutgoing(9, ClientChangeHeldItemPacket.class);
        registerOutgoing(10, ClientPlayerAnimationPacket.class);
        registerOutgoing(11, ClientPlayerActionPacket.class);
        registerOutgoing(12, ClientSteerVehiclePacket.class);
        registerOutgoing(13, ClientCloseWindowPacket.class);
        registerOutgoing(14, ClientWindowActionPacket.class);
        registerOutgoing(15, ClientConfirmTransactionPacket.class);
        registerOutgoing(16, ClientCreativeInventoryActionPacket.class);
        registerOutgoing(17, ClientEnchantItemPacket.class);
        registerOutgoing(18, ClientUpdateSignPacket.class);
        registerOutgoing(19, ClientPlayerAbilitiesPacket.class);
        registerOutgoing(20, ClientTabCompletePacket.class);
        registerOutgoing(21, ClientSettingsPacket.class);
        registerOutgoing(22, ClientRequestPacket.class);
        registerOutgoing(23, ClientPluginMessagePacket.class);
    }

    private void initServerGame(Session session) {
        registerIncoming(0, ClientKeepAlivePacket.class);
        registerIncoming(1, ClientChatPacket.class);
        registerIncoming(2, ClientPlayerInteractEntityPacket.class);
        registerIncoming(3, ClientPlayerMovementPacket.class);
        registerIncoming(4, ClientPlayerPositionPacket.class);
        registerIncoming(5, ClientPlayerRotationPacket.class);
        registerIncoming(6, ClientPlayerPositionRotationPacket.class);
        registerIncoming(7, ClientPlayerDigPacket.class);
        registerIncoming(8, ClientPlayerPlaceBlockPacket.class);
        registerIncoming(9, ClientChangeHeldItemPacket.class);
        registerIncoming(10, ClientPlayerAnimationPacket.class);
        registerIncoming(11, ClientPlayerActionPacket.class);
        registerIncoming(12, ClientSteerVehiclePacket.class);
        registerIncoming(13, ClientCloseWindowPacket.class);
        registerIncoming(14, ClientWindowActionPacket.class);
        registerIncoming(15, ClientConfirmTransactionPacket.class);
        registerIncoming(16, ClientCreativeInventoryActionPacket.class);
        registerIncoming(17, ClientEnchantItemPacket.class);
        registerIncoming(18, ClientUpdateSignPacket.class);
        registerIncoming(19, ClientPlayerAbilitiesPacket.class);
        registerIncoming(20, ClientTabCompletePacket.class);
        registerIncoming(21, ClientSettingsPacket.class);
        registerIncoming(22, ClientRequestPacket.class);
        registerIncoming(23, ClientPluginMessagePacket.class);

        registerOutgoing(0, ServerKeepAlivePacket.class);
        registerOutgoing(1, ServerJoinGamePacket.class);
        registerOutgoing(2, ServerChatPacket.class);
        registerOutgoing(3, ServerUpdateTimePacket.class);
        registerOutgoing(4, ServerEntityEquipmentPacket.class);
        registerOutgoing(5, ServerSpawnPositionPacket.class);
        registerOutgoing(6, ServerUpdateHealthPacket.class);
        registerOutgoing(7, ServerRespawnPacket.class);
        registerOutgoing(8, ServerPlayerPositionRotationPacket.class);
        registerOutgoing(9, ServerChangeHeldItemPacket.class);
        registerOutgoing(10, ServerPlayerUseBedPacket.class);
        registerOutgoing(11, ServerAnimationPacket.class);
        registerOutgoing(12, ServerSpawnPlayerPacket.class);
        registerOutgoing(13, ServerCollectItemPacket.class);
        registerOutgoing(14, ServerSpawnObjectPacket.class);
        registerOutgoing(15, ServerSpawnMobPacket.class);
        registerOutgoing(16, ServerSpawnPaintingPacket.class);
        registerOutgoing(17, ServerSpawnExpOrbPacket.class);
        registerOutgoing(18, ServerEntityVelocityPacket.class);
        registerOutgoing(19, ServerDestroyEntitiesPacket.class);
        registerOutgoing(20, ServerEntityMovementPacket.class);
        registerOutgoing(21, ServerEntityPositionPacket.class);
        registerOutgoing(22, ServerEntityRotationPacket.class);
        registerOutgoing(23, ServerEntityPositionRotationPacket.class);
        registerOutgoing(24, ServerEntityTeleportPacket.class);
        registerOutgoing(25, ServerEntityHeadLookPacket.class);
        registerOutgoing(26, ServerEntityStatusPacket.class);
        registerOutgoing(27, ServerEntityAttachPacket.class);
        registerOutgoing(28, ServerEntityMetadataPacket.class);
        registerOutgoing(29, ServerEntityEffectPacket.class);
        registerOutgoing(30, ServerEntityRemoveEffectPacket.class);
        registerOutgoing(31, ServerSetExperiencePacket.class);
        registerOutgoing(32, ServerEntityPropertiesPacket.class);
        registerOutgoing(33, ServerChunkDataPacket.class);
        registerOutgoing(34, ServerMultiBlockChangePacket.class);
        registerOutgoing(35, ServerBlockChangePacket.class);
        registerOutgoing(36, ServerBlockValuePacket.class);
        registerOutgoing(37, ServerBlockBreakAnimPacket.class);
        registerOutgoing(38, ServerMultiChunkDataPacket.class);
        registerOutgoing(39, ServerExplosionPacket.class);
        registerOutgoing(40, ServerPlayEffectPacket.class);
        registerOutgoing(41, ServerPlaySoundPacket.class);
        registerOutgoing(42, ServerSpawnParticlePacket.class);
        registerOutgoing(43, ServerNotifyClientPacket.class);
        registerOutgoing(44, ServerSpawnGlobalEntityPacket.class);
        registerOutgoing(45, ServerOpenWindowPacket.class);
        registerOutgoing(46, ServerCloseWindowPacket.class);
        registerOutgoing(47, ServerSetSlotPacket.class);
        registerOutgoing(48, ServerWindowItemsPacket.class);
        registerOutgoing(49, ServerWindowPropertyPacket.class);
        registerOutgoing(50, ServerConfirmTransactionPacket.class);
        registerOutgoing(51, ServerUpdateSignPacket.class);
        registerOutgoing(52, ServerMapDataPacket.class);
        registerOutgoing(53, ServerUpdateTileEntityPacket.class);
        registerOutgoing(54, ServerOpenTileEntityEditorPacket.class);
        registerOutgoing(55, ServerStatisticsPacket.class);
        registerOutgoing(56, ServerPlayerListEntryPacket.class);
        registerOutgoing(57, ServerPlayerAbilitiesPacket.class);
        registerOutgoing(58, ServerTabCompletePacket.class);
        registerOutgoing(59, ServerScoreboardObjectivePacket.class);
        registerOutgoing(60, ServerUpdateScorePacket.class);
        registerOutgoing(61, ServerDisplayScoreboardPacket.class);
        registerOutgoing(62, ServerTeamPacket.class);
        registerOutgoing(63, ServerPluginMessagePacket.class);
        registerOutgoing(64, ServerDisconnectPacket.class);
    }

    private void initClientStatus(Session session) {
        registerIncoming(0, StatusResponsePacket.class);
        registerIncoming(1, StatusPongPacket.class);

        registerOutgoing(0, StatusQueryPacket.class);
        registerOutgoing(1, StatusPingPacket.class);
    }

    private void initServerStatus(Session session) {
        registerIncoming(0, StatusQueryPacket.class);
        registerIncoming(1, StatusPingPacket.class);

        registerOutgoing(0, StatusResponsePacket.class);
        registerOutgoing(1, StatusPongPacket.class);
    }
}