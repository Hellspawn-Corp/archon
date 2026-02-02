package net.hellspawn.archon.tech.power

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.hellspawn.archon.tech.Archontech.MOD_ID
import net.hellspawn.archon.tech.power.block.entity.GeneratorBlockEntity
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

object ModBlockEntities {
    lateinit var GENERATOR: BlockEntityType<GeneratorBlockEntity>

    fun registerAll() {
        GENERATOR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "generator"),
            FabricBlockEntityTypeBuilder.create(
                { pos, state -> GeneratorBlockEntity(GENERATOR, pos, state) },
                ModBlocks.GENERATOR_BLOCK
            ).build()
        )
    }

    fun getTicker(): BlockEntityTicker<GeneratorBlockEntity> =
        BlockEntityTicker { world, pos, state, be -> be.tick() }
}