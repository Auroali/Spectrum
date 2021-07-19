package de.dafuqs.pigment.inventories;

import de.dafuqs.pigment.blocks.altar.AltarBlockEntity;
import de.dafuqs.pigment.inventories.slots.ReadOnlySlot;
import de.dafuqs.pigment.inventories.slots.StackFilterSlot;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import de.dafuqs.pigment.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.pigment.registries.PigmentItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class AltarScreenHandler extends AbstractRecipeScreenHandler<Inventory> {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final RecipeBookCategory category;

    public AltarScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(PigmentScreenHandlerTypes.ALTAR, PigmentRecipeTypes.ALTAR, RecipeBookCategory.CRAFTING, syncId, playerInventory);
    }

    protected AltarScreenHandler(ScreenHandlerType<?> type, RecipeType<? extends AltarCraftingRecipe> recipeType, RecipeBookCategory recipeBookCategory, int i, PlayerInventory playerInventory) {
        this(type, recipeType, recipeBookCategory, i, playerInventory, new SimpleInventory(AltarBlockEntity.INVENTORY_SIZE), new ArrayPropertyDelegate(2));
    }

    public AltarScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        this(PigmentScreenHandlerTypes.ALTAR, PigmentRecipeTypes.ALTAR, RecipeBookCategory.CRAFTING, syncId, playerInventory, inventory, propertyDelegate);
    }

    protected AltarScreenHandler(ScreenHandlerType<?> type, RecipeType<? extends AltarCraftingRecipe> recipeType, RecipeBookCategory recipeBookCategory, int i, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(type, i);
        this.category = recipeBookCategory;
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.world;

        checkSize(inventory, AltarBlockEntity.INVENTORY_SIZE);
        checkDataCount(propertyDelegate, 2);

        // crafting slots
        int m;
        int n;
        for(m = 0; m < 3; ++m) {
            for(n = 0; n < 3; ++n) {
                this.addSlot(new Slot(inventory, n + m * 3, 30 + n * 18, 19 + m * 18));
            }
        }

        // pigment slots
        this.addSlot(new StackFilterSlot(inventory, 9, 44, 77, PigmentItems.AMETHYST_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 10, 44 + 18, 77, PigmentItems.CITRINE_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 11, 44 + 2 * 18, 77, PigmentItems.TOPAZ_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 12, 44 + 3 * 18, 77, PigmentItems.ONYX_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 13, 44 + 4 * 18, 77, PigmentItems.MOONSTONE_POWDER));

        // crafting tablet slot
        this.addSlot(new StackFilterSlot(inventory, AltarBlockEntity.CRAFTING_TABLET_SLOT_ID, 93, 19, PigmentItems.CRAFTING_TABLET));

        // preview slot
        this.addSlot(new ReadOnlySlot(inventory, AltarBlockEntity.PREVIEW_SLOT_ID, 127, 37));

        // player inventory
        int l;
        for(l = 0; l < 3; ++l) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, 112 + l * 18));
            }
        }

        // player hotbar
        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 170));
        }

        this.addProperties(propertyDelegate);
    }

    public void populateRecipeFinder(RecipeMatcher recipeMatcher) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider)this.inventory).provideRecipeInputs(recipeMatcher);
        }
    }

    public void clearCraftingSlots() {
        for(int i = 0; i < 9; i++) {
            this.getSlot(i).setStack(ItemStack.EMPTY);
        }
    }

    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.inventory, this.world);
    }

    public int getCraftingResultSlotIndex() {
        return 16;
    }

    public int getCraftingWidth() {
        return 3;
    }

    public int getCraftingHeight() {
        return 3;
    }

    public int getCraftingSlotCount() {
        return 9;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Environment(EnvType.CLIENT)
    public int getCraftingProgress() {
        int craftingTime = this.propertyDelegate.get(0); // craftingTime
        int craftingTimeTotal = this.propertyDelegate.get(1); // craftingTimeTotal
        return craftingTimeTotal != 0 && craftingTime != 0 ? craftingTime * 24 / craftingTimeTotal : 0;
    }

    public boolean isCrafting() {
        return this.propertyDelegate.get(0) > 0; // craftingTime
    }

    @Environment(EnvType.CLIENT)
    public RecipeBookCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != 1;
    }

    // Shift-Clicking
    // 0-8: crafting slots
    // 9-13: pigment slots
    // 14: crafting tablet
    // 15: preview slot
    // 16: hidden output slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack clickedStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack clickedStack = slot.getStack();
            clickedStackCopy = clickedStack.copy();

            if(index < AltarBlockEntity.PREVIEW_SLOT_ID) {
                // altar => player inv
                if (!this.insertItem(clickedStack, 15, 51, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(PigmentItems.AMETHYST_POWDER)) {
                if(!this.insertItem(clickedStack, 9, 10, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(PigmentItems.CITRINE_POWDER)) {
                if(!this.insertItem(clickedStack, 10, 11, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(PigmentItems.TOPAZ_POWDER)) {
                if(!this.insertItem(clickedStack, 11, 12, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(PigmentItems.ONYX_POWDER)) {
                if(!this.insertItem(clickedStack, 12, 13, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(PigmentItems.MOONSTONE_POWDER)) {
                if(!this.insertItem(clickedStack, 13, 14, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(PigmentItems.CRAFTING_TABLET)) {
                if(!this.insertItem(clickedStack, AltarBlockEntity.CRAFTING_TABLET_SLOT_ID, 15, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // crafting grid
            if (!this.insertItem(clickedStack, 0, 8, false)) {
                return ItemStack.EMPTY;
            }

            if (clickedStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (clickedStack.getCount() == clickedStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, clickedStack);
        }

        return clickedStackCopy;
    }


}
