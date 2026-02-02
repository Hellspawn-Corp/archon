package net.hellspawn.archon.tech.power

import net.hellspawn.archon.tech.Archontech
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Function


object ModBlocks {
    lateinit var GENERATOR_BLOCK: Block

    fun registerAll() {
        // TODO: Change this from stone to something else
        GENERATOR_BLOCK = register(
            "generator",
            Function { settings: BlockBehaviour.Properties -> Block(settings) },
            BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE),
            true
        )
    }

    fun <T : BlockEntity> createTicker(
        type: BlockEntityType<T>,
        expectedType: BlockEntityType<T>,
        ticker: BlockEntityTicker<T>
    ): BlockEntityTicker<T>? {
        return if (type == expectedType) ticker else null
    }

    private fun register(
        name: String,
        blockFactory: Function<BlockBehaviour.Properties?, Block>,
        settings: BlockBehaviour.Properties,
        shouldRegisterItem: Boolean
    ): Block {
        // Create a registry key for the block
        val blockKey: ResourceKey<Block> = keyOfBlock(name)
        // Create the block instance
        val block: Block = blockFactory.apply(settings.setId(blockKey))

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            val itemKey = keyOfItem(name)

            val blockItem = BlockItem(block, Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem)
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block)
    }

    private fun keyOfBlock(name: String): ResourceKey<Block> {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Archontech.MOD_ID, name))
    }

    private fun keyOfItem(name: String): ResourceKey<Item> {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Archontech.MOD_ID, name))
    }
}