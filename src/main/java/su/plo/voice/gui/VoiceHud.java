package su.plo.voice.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import su.plo.voice.client.VoiceClient;

public class VoiceHud {
    private final MinecraftClient client;

    public VoiceHud() {
        this.client = MinecraftClient.getInstance();
    }

    public void render() {
        if(!VoiceClient.isConnected()) {
            return;
        }

        final PlayerEntity player = client.player;
        final InGameHud inGameHud = client.inGameHud;
        final MatrixStack matrixStack = new MatrixStack();

        if (player == null) return;

        if(VoiceClient.socketUDP.ping.timedOut) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, VoiceClient.MICS);

            inGameHud.drawTexture(matrixStack,
                    VoiceClient.getClientConfig().getMicIconPosition().getX(client),
                    VoiceClient.getClientConfig().getMicIconPosition().getY(client),
                    0,
                    16,
                    16,
                    16);
            return;
        }

        if(VoiceClient.isMuted() || VoiceClient.getServerConfig().getMuted().containsKey(player.getUuid())) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, VoiceClient.MICS);

            inGameHud.drawTexture(matrixStack,
                    VoiceClient.getClientConfig().getMicIconPosition().getX(client),
                    VoiceClient.getClientConfig().getMicIconPosition().getY(client),
                    16,
                    0,
                    16,
                    16);
        } else if(VoiceClient.isSpeaking()) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, VoiceClient.MICS);

            if(VoiceClient.isSpeakingPriority()) {
                inGameHud.drawTexture(matrixStack,
                        VoiceClient.getClientConfig().getMicIconPosition().getX(client),
                        VoiceClient.getClientConfig().getMicIconPosition().getY(client),
                        16,
                        16,
                        16,
                        16);
            } else {
                inGameHud.drawTexture(matrixStack,
                        VoiceClient.getClientConfig().getMicIconPosition().getX(client),
                        VoiceClient.getClientConfig().getMicIconPosition().getY(client),
                        0,
                        0,
                        16,
                        16);
            }
        }
    }
}