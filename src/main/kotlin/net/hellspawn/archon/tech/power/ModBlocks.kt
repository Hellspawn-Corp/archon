package net.hellspawn.archon.tech.power

import net.hellspawn.archon.tech.power.blocks.GeneratorBlock
import net.minecraft.block.Block
import net.minecraft.block.AbstractBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {
    lateinit var GENERATOR_BLOCK: Block

    fun registerAll() {
        GENERATOR_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of("archontech", "generator"),
            GeneratorBlock(AbstractBlock.Settings.create())
        )
    }

    fun <T : BlockEntity> createTicker(
        type: BlockEntityType<T>,
        expectedType: BlockEntityType<T>,
        ticker: BlockEntityTicker<T>
    ): BlockEntityTicker<T>? {
        return if (type == expectedType) ticker else null
    }
}