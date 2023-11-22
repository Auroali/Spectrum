package de.dafuqs.spectrum.mixin.compat.colorful_hearts.present;

import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.*;
import net.minecraft.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import terrails.colorfulhearts.render.HeartRenderer;

@Environment(EnvType.CLIENT)
@Mixin(HeartRenderer.class)
public abstract class InGameHudMixin {

	// Execute the display after HealthOverlay did its own, to avoid conflict
	@Inject(method = "renderPlayerHearts", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void renderPlayerHeartsAzureDikeInjector(DrawContext drawContext, PlayerEntity player, int x, int y, int maxHealth, int currentHealth, int displayHealth, int absorption, boolean renderHighlight, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		InGameHud hud = client.inGameHud;
		int scaledWidth = ((InGameHudAccessor) hud).getWidth();
		int scaledHeight = ((InGameHudAccessor) hud).getHeight();

		HudRenderers.renderAzureDike(drawContext, scaledWidth, scaledHeight, player);
	}
}