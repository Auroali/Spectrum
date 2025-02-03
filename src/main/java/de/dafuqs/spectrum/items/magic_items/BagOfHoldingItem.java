package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.inventories.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class BagOfHoldingItem extends Item {
	
	public BagOfHoldingItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		EnderChestInventory enderChestInventory = user.getEnderChestInventory();
		if (enderChestInventory != null) {
			user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerx) -> new BagOfHoldingScreenHandler(syncId, playerx.getInventory(), playerx.getEnderChestInventory()), Text.translatable("container.enderchest")));
			
			return TypedActionResult.consume(itemStack);
		} else {
			return TypedActionResult.success(itemStack, world.isClient);
		}
	}
	
	@Override
	public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
		if(clickType != ClickType.RIGHT || slot.getStack().isEmpty())
			return false;
		
		ItemStack remaining = addToEnderChest(player, slot.getStack());
		if(remaining.getCount() != slot.getStack().getCount())
			playInsertSound(player);
		
		if(!player.getWorld().isClient)
			slot.setStack(remaining);
		return true;
	}
	
	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if(clickType != ClickType.RIGHT || otherStack.isEmpty())
			return false;
		
		ItemStack remaining = addToEnderChest(player, otherStack);
		// play the bundle insert sound if something was inserted
		if(remaining.getCount() != otherStack.getCount())
			playInsertSound(player);
		
		// the creative inventory calls this method from the client,
		// which ends up just deleting the item without the isClient check
		if(!player.getWorld().isClient)
			cursorStackReference.set(remaining);
		return true;
	}
	
	private ItemStack addToEnderChest(PlayerEntity entity, ItemStack stack) {
		if(entity.getWorld().isClient || entity.getEnderChestInventory() == null)
			return ItemStack.EMPTY;
		
		return entity.getEnderChestInventory().addStack(stack);
	}
	
	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
}
