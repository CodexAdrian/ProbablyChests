package org.cloudwarp.probablychests.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;

public class PCChestScreen extends HandledScreen<PCChestScreenHandler> {
    private static final Identifier TEXTURE = ProbablyChests.id("textures/gui/pc_chest_gui.png");

    private final int rows;

    public PCChestScreen(PCChestScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.rows = handler.getRows();
        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        titleX = this.playerInventoryTitleX;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        context.drawTexture(TEXTURE, x, y + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
