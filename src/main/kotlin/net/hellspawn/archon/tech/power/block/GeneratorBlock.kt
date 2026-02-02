package net.hellspawn.archon.tech.power.block
import net.hellspawn.archon.tech.power.ModBlockEntities
import net.hellspawn.archon.tech.power.block.entity.GeneratorBlockEntity
import net.hellspawn.archon.tech.util.log.Log
import net.hellspawn.archon.tech.util.log.LogLevel
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import kotlin.reflect.jvm.jvmName

class GeneratorBlock(settings: BlockBehaviour.Properties) : Block(settings), EntityBlock {
    private val TAG = GeneratorBlock::class.jvmName

    override fun tick(
        blockState: BlockState,
        serverLevel: ServerLevel,
        blockPos: BlockPos,
        randomSource: RandomSource
    ) {
        val be = serverLevel.getBlockEntity(blockPos)
        if (be is GeneratorBlockEntity) {
            be.generateEnergy()
        }
        super.tick(blockState, serverLevel, blockPos, randomSource)
    }

    override fun useWithoutItem(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        blockHitResult: BlockHitResult
    ): InteractionResult {
        level.let {
            if(!level.isClientSide) {
                Log.log(LogLevel.DEBUG, TAG, "onUse - RUNNING ON SERVER!")
            }
        }
        Log.log(LogLevel.DEBUG, TAG, "onUse - Block used at position: $blockPos, by player: ${player.name}")
        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult)
    }

    override fun newBlockEntity(
        pos: BlockPos,
        state: BlockState
    ): BlockEntity? {
        return pos.let { p ->
            state.let { s ->
                Log.log(LogLevel.DEBUG, TAG, "createBlockEntity - Block created at position: $pos")
                GeneratorBlockEntity(ModBlockEntities.GENERATOR, pos, state)
            }
        }
    }

    override fun <T : BlockEntity> getTicker(
        world: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (type === ModBlockEntities.GENERATOR) {
            ModBlockEntities.getTicker() as BlockEntityTicker<T>
        } else null
    }
}