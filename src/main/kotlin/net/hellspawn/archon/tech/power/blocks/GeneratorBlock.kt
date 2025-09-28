package net.hellspawn.archon.tech.power.blocks
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class GeneratorBlock(settings: Settings) : Block(settings) {
    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val be = world.getBlockEntity(pos)
        if (be is GeneratorBlockEntity) {
            be.generateEnergy()
        }
    }
}