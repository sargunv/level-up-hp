package me.sargunvohra.mcmods.leveluphp.gui

import com.mojang.blaze3d.systems.RenderSystem
import me.sargunvohra.mcmods.leveluphp.LuhpIds
import me.sargunvohra.mcmods.leveluphp.hpLevelHandlerOpt
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer

private const val BAR_WIDTH = 91
private const val BAR_TOP_OFFSET = 30
private const val HP_LEVEL_COLOR = 0xff3f3f
private const val MC_LEVEL_COLOR = 0x80FF20

fun renderLuhpExpBars(client: Minecraft, player: ClientPlayerEntity) {
    client.profiler.startSection("levelUpHpExpBars")

    RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
    RenderSystem.disableBlend()

    if (client.playerController!!.gameIsSurvivalOrAdventure()) {

        client.textureManager.bindTexture(LuhpIds.EXP_BAR_ICONS_TEXTURE)

        val window = client.mainWindow
        val fontRenderer = client.fontRenderer

        val barsCenterX = window.scaledWidth / 2
        val barsTopY = window.scaledHeight - BAR_TOP_OFFSET

        val hpLevelHandler = player.hpLevelHandlerOpt

        // render luhp exp bar
        if (hpLevelHandler != null) {
            val hpXpTarget = hpLevelHandler.currentXpTarget
            val hpXpFillWidth = if (hpXpTarget != 0) hpLevelHandler.xp * BAR_WIDTH / hpXpTarget else 0
            renderProgress(barsCenterX - BAR_WIDTH, barsTopY + 1, 0, hpXpFillWidth)
        }

        // render vanilla exp bar
        val mcXpFillWidth = (player.experience * BAR_WIDTH).toInt()
        renderProgress(barsCenterX, barsTopY + 1, BAR_WIDTH, mcXpFillWidth)

        // render luhp level
        if (hpLevelHandler != null) {
            val levelStr = "${hpLevelHandler.level}"
            val levelStrWidth: Int = fontRenderer.getStringWidth(levelStr)
            renderLevel(
                fontRenderer,
                levelStr,
                barsTopY,
                barsCenterX - BAR_WIDTH - 1 - levelStrWidth,
                HP_LEVEL_COLOR
            )
        }

        // render vanilla level
        val mcLevel = "" + player.experienceLevel
        renderLevel(
            fontRenderer,
            mcLevel,
            barsTopY,
            barsCenterX + BAR_WIDTH + 2,
            MC_LEVEL_COLOR
        )
    }

    RenderSystem.enableBlend()
    RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)

    client.profiler.endSection()
}

private fun renderProgress(
    left: Int,
    top: Int,
    texX: Int,
    filled: Int
) {
    AbstractGui.blit(left, top, -90, texX.toFloat(), 0F, BAR_WIDTH, 5, 256, 256)
    if (filled > 0)
        AbstractGui.blit(left, top, -90, texX.toFloat(), 5F, filled, 5, 256, 256)
}

private fun renderLevel(
    fontRenderer: FontRenderer,
    str: String,
    top: Int,
    left: Int,
    color: Int
) {
    fontRenderer.drawString(str, (left + 1).toFloat(), top.toFloat(), 0)
    fontRenderer.drawString(str, (left - 1).toFloat(), top.toFloat(), 0)
    fontRenderer.drawString(str, left.toFloat(), (top + 1).toFloat(), 0)
    fontRenderer.drawString(str, left.toFloat(), (top - 1).toFloat(), 0)
    fontRenderer.drawString(str, left.toFloat(), top.toFloat(), color)
}