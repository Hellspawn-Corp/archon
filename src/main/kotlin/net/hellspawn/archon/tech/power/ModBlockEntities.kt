package net.hellspawn.archon.tech.power

import net.hellspawn.archon.tech.power.blocks.GeneratorBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlockEntities {
    lateinit var GENERATOR: BlockEntityType<GeneratorBlockEntity>

    fun registerAll() {
        GENERATOR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of("archontech", "generator"),
            BlockEntityType.Builder.create(
                { pos, state -> GeneratorBlockEntity(ModBlockEntities.GENERATOR, pos, state) },
                ModBlocks.GENERATOR_BLOCK
            ).build(null)
        )
    }
}