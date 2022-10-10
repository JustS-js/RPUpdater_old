package net.just_s.rpupdater.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.just_s.rpupdater.RPUpdMod;
import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Map;
import java.util.Set;

@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {
	private static final File rpudpDir = new File(RPUpdMod.MC.getResourcePackDir(), "RPUpdDir");
	private static final Set<ResourcePackProvider> rpudpProviders = ImmutableSet.copyOf(new ResourcePackProvider[]{
			RPUpdMod.MC.getResourcePackProvider(),
			new FileResourcePackProvider(rpudpDir, RPUpdMod.PACK_SOURCE_RPUPD)});
	@Final @Shadow
	private ResourcePackProfile.Factory profileFactory;
	@Final @Shadow
	private Set<ResourcePackProvider> providers;

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/resource/ResourcePackProfile$Factory;[Lnet/minecraft/resource/ResourcePackProvider;)V")
	private void injectInit(CallbackInfo info) {
		if (!rpudpDir.exists())
			if (!rpudpDir.mkdir())
				RPUpdMod.LOGGER.error("Could not create directory for RPUpdater");
	}

	@Inject(at = @At("HEAD"), method = "providePackProfiles", cancellable = true)
	private void injectLoadFromDir(CallbackInfoReturnable<Map<String, ResourcePackProfile>> cir) {
		Map<String, ResourcePackProfile> map = Maps.newTreeMap();
		for (ResourcePackProvider resourcePackProvider : rpudpProviders) {
			resourcePackProvider.register((profile) -> {
				map.put(profile.getName(), profile);
			}, this.profileFactory);
		}
		for (ResourcePackProvider resourcePackProvider : this.providers) {
			resourcePackProvider.register((profile) -> {
				map.put(profile.getName(), profile);
			}, this.profileFactory);
		}

		cir.setReturnValue(ImmutableMap.copyOf(map));
	}
}
